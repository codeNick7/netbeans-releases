/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2014 Sun Microsystems, Inc.
 */
package org.openide.io;

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.netbeans.api.io.Hyperlink;
import org.netbeans.api.io.OutputColor;
import org.netbeans.api.io.ShowOperation;
import org.netbeans.spi.io.support.Hyperlinks;
import org.netbeans.spi.io.InputOutputProvider;
import org.netbeans.spi.io.support.OutputColorType;
import org.netbeans.spi.io.support.OutputColors;
import org.openide.util.Lookup;
import org.openide.windows.FoldHandle;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOColorPrint;
import org.openide.windows.IOColors;
import org.openide.windows.IOColors.OutputType;
import org.openide.windows.IOContainer;
import org.openide.windows.IOFolding;
import org.openide.windows.IOPosition;
import org.openide.windows.IOPosition.Position;
import org.openide.windows.IOProvider;
import org.openide.windows.IOSelect;
import org.openide.windows.IOTab;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;
import org.openide.windows.OutputWriter;

/**
 * Wrapper class for converting implementations of IOProvider SPI to new SPI
 * interface {@link InputOutputProvider}.
 *
 * @author jhavlin
 */
public final class BridgingInputOutputProvider
        implements InputOutputProvider<InputOutput, OutputWriter, Position, FoldHandle> {

    private final IOProvider delegate;

    public BridgingInputOutputProvider(IOProvider delegate) {
        this.delegate = delegate;
    }

    private static final Logger LOG
            = Logger.getLogger(BridgingInputOutputProvider.class.getName());

    private final Deque<FoldHandle> foldStack = new ArrayDeque<FoldHandle>();

    @Override
    public String getId() {
        return delegate.getName();
    }

    @Override
    public InputOutput getIO(String name, boolean newIO, Lookup lookup) {
        Action[] actions = lookup.lookup(Action[].class);
        IOContainer container = lookup.lookup(IOContainer.class);
        if (container == null && actions == null) {
            return delegate.getIO(name, newIO);
        } else if (newIO) {
            if (container != null && actions != null) {
                return delegate.getIO(name, actions, container);
            } else if (actions != null) {
                return delegate.getIO(name, actions);
            } else {
                return delegate.getIO(name, new Action[0], container);
            }
        } else {
            return delegate.getIO(name, newIO, actions == null ? new Action[0] : actions,
                    container);
        }
    }

    @Override
    public Reader getIn(InputOutput io) {
        return io.getIn();
    }

    @Override
    public OutputWriter getOut(InputOutput io) {
        return io.getOut();
    }

    @Override
    public OutputWriter getErr(InputOutput io) {
        return io.getErr();
    }

    @Override
    public void print(InputOutput io, OutputWriter writer, String text,
            Hyperlink link, OutputColor outputColor, boolean printLineEnd) {

        Color awtColor = outputColorToAwtColor(io, outputColor);
        OutputListener listener = hyperlinkToOutputListener(link);
        boolean listenerImportant = link != null && Hyperlinks.isImportant(link);
        try {
            if (printLineEnd && outputColor == null) {
                writer.println(text, listener, listenerImportant);
            } else if (printLineEnd && IOColorLines.isSupported(io)) {
                IOColorLines.println(io, text, listener, listenerImportant,
                        awtColor);
            } else if (IOColorPrint.isSupported(io)) {
                IOColorPrint.print(io, text, listener, listenerImportant,
                        awtColor);
                if (printLineEnd) {
                    writer.println();
                }
            } else if (printLineEnd) {
                writer.println(text);
            } else {
                writer.print(text);
            }
        } catch (IOException ex) {
            LOG.log(Level.FINE, "Cannot print color or hyperlink", ex); //NOI18N
        }
    }

    @Override
    public Lookup getIOLookup(InputOutput io) {
        if (io instanceof Lookup.Provider) {
            return ((Lookup.Provider) io).getLookup();
        } else {
            return Lookup.EMPTY;
        }
    }

    @Override
    public void resetIO(InputOutput io) {
        try {
            io.getOut().reset();
        } catch (IOException ex) {
            LOG.log(Level.FINE, "Cannot reset InputOutput.", ex);       //NOI18N
        }
    }

    @Override
    public void showIO(InputOutput io,
            Set<ShowOperation> operations) {
        if (operations.contains(ShowOperation.OPEN)
                && operations.contains(ShowOperation.MAKE_VISIBLE)
                && operations.size() == 2) {
            io.select();
        } else {
            IOSelect.select(io, showOperationsToIoSelect(operations));
        }
    }

    /**
     * Translate set of {@link ShowOperation}s to set of
     * {@link IOSelect.AdditionalOperation}s.
     */
    private Set<IOSelect.AdditionalOperation> showOperationsToIoSelect(
            Set<ShowOperation> operations) {
        Set<IOSelect.AdditionalOperation> res
                = EnumSet.noneOf(IOSelect.AdditionalOperation.class);
        for (ShowOperation so : operations) {
            switch (so) {
                case OPEN:
                    res.add(IOSelect.AdditionalOperation.OPEN);
                    break;
                case MAKE_VISIBLE:
                    res.add(IOSelect.AdditionalOperation.REQUEST_VISIBLE);
                    break;
                case ACTIVATE:
                    res.add(IOSelect.AdditionalOperation.REQUEST_ACTIVE);
                    break;
            }
        }
        return res;
    }

    @Override
    public void closeIO(InputOutput io) {
        io.closeInputOutput();
    }

    @Override
    public boolean isIOClosed(InputOutput io) {
        return io.isClosed();
    }

    @Override
    public Position getCurrentPosition(InputOutput io, OutputWriter writer) {
        if (IOPosition.isSupported(io)) {
            return IOPosition.currentPosition(io);
        } else {
            return null;
        }
    }

    @Override
    public void scrollTo(InputOutput io, OutputWriter writer, Position position) {
        position.scrollTo();
    }

    @Override
    public FoldHandle startFold(InputOutput io, OutputWriter writer,
            boolean expanded) {

        if (IOFolding.isSupported(io)) {
            synchronized (foldStack) {
                if (foldStack.isEmpty()) {
                    foldStack.addLast(IOFolding.startFold(io, expanded));
                } else {
                    foldStack.addLast(foldStack.getLast().startFold(expanded));
                }
                return foldStack.getLast();
            }
        } else {
            return null;
        }
    }

    @Override
    public void endFold(InputOutput io, OutputWriter writer, FoldHandle fold) {
        synchronized (foldStack) {
            while (!foldStack.isEmpty()) {
                if (foldStack.removeLast() == fold) {
                    break;
                }
            }
            fold.silentFinish();
        }
    }

    @Override
    public void setFoldExpanded(InputOutput io, OutputWriter writer,
            FoldHandle fold, boolean expanded) {
        fold.setExpanded(expanded);
    }

    @Override
    public String getIODescription(InputOutput io) {
        if (IOTab.isSupported(io)) {
            return IOTab.getToolTipText(io);
        } else {
            return null;
        }
    }

    @Override
    public void setIODescription(InputOutput io, String description) {
        if (IOTab.isSupported(io)) {
            IOTab.setToolTipText(io, description);
        }
    }

    /**
     * Convert a hyperlink to an output listener.
     *
     * @param link The hyperlink.
     * @return The wrapping output listener.
     */
    private static OutputListener hyperlinkToOutputListener(
            final Hyperlink link) {

        if (link == null) {
            return null;
        }
        return new OutputListenerAdapter() {
            @Override
            public void outputLineAction(OutputEvent ev) {
                Hyperlinks.invoke(link);
            }
        };
    }

    /**
     * Convert AWT-independent {@link OutputColor} to {@link java.awt.Color}.
     *
     * @return Appropriate color, or null if default color should be used.
     */
    private static Color outputColorToAwtColor(InputOutput io,
            OutputColor color) {

        if (color == null) {
            return null;
        }
        OutputColorType type = OutputColors.getType(color);
        if (type == OutputColorType.RGB) {
            return new Color(OutputColors.getRGB(color));
        } else if (IOColors.isSupported(io)) {
            switch (type) {
                case DEBUG:
                    return IOColors.getColor(io, OutputType.LOG_DEBUG);
                case FAILURE:
                    return IOColors.getColor(io, OutputType.LOG_FAILURE);
                case WARNING:
                    return IOColors.getColor(io, OutputType.LOG_WARNING);
                case SUCCESS:
                    return IOColors.getColor(io, OutputType.LOG_SUCCESS);
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    private static class OutputListenerAdapter implements OutputListener {

        @Override
        public void outputLineSelected(OutputEvent ev) {
        }

        @Override
        public void outputLineAction(OutputEvent ev) {
        }

        @Override
        public void outputLineCleared(OutputEvent ev) {
        }
    }
}

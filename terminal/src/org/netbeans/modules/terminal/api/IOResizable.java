/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */

package org.netbeans.modules.terminal.api;

import java.awt.Dimension;
import org.openide.util.Lookup;
import org.openide.windows.InputOutput;

/**
 * Capability of an InputOutput which provides notification of
 * size changes.
 * @author ivan
 */
public abstract class IOResizable {

    public static final String PROP_SIZE = "IOResizable.PROP_SIZE"; // NOI18N

    public static final class Size {
	public final Dimension cells;
	public final Dimension pixels;

	public Size(Dimension cells, Dimension pixels) {
	    this.cells = cells;
	    this.pixels = pixels;
	}
    }

    /**
     * Deprecated: Use IONotifier with PROP_SIZE and IOResizable.Size
    // LATER @deprecated Use IONotifier with PROP_SIZE and IOResizable.Size
     */
    // If we mark it as @deprecated then our own builds will be noise.
    // add/removeListener are already marked as deprecated.
    // LATER @Deprecated
    public interface Listener {
	public void sizeChanged(Dimension cells, Dimension pixels);
    }

    private static IOResizable find(InputOutput io) {
        if (io instanceof Lookup.Provider) {
            Lookup.Provider p = (Lookup.Provider) io;
            return p.getLookup().lookup(IOResizable.class);
        }
        return null;
    }

    /**
     * Add a size change listener to the provided IO.
     * @param io IO to operate on.
     * @param listener ... to notify of size changes.
     * @deprecated Use IONotifier with PROP_SIZE.
     */
    @Deprecated
    public static void addListener(InputOutput io, Listener listener) {
	IOResizable ior = find(io);
	if (ior != null) {
	    ior.addListener(listener);
	}
    }

    /**
     * Remove the given size change listener from the provided IO.
     * @param io IO to operate on.
     * @param listener ... to remove.
     * @deprecated Use IONotifier with PROP_SIZE.
     */
    @Deprecated
    public static void removeListener(InputOutput io, Listener listener) {
	IOResizable ior = find(io);
	if (ior != null) {
	    ior.removeListener(listener);
	}
    }

    /**
     * Checks whether this feature is supported for provided IO
     * @param io IO to check on
     * @return true if supported
     */
    public static boolean isSupported(InputOutput io) {
        return find(io) != null;
    }

    /**
     * Add a size change listener to this.
     * @param listener ... to notify of size changes.
     */
    abstract protected void addListener(Listener listener);

    /**
     * Remove the given size change listener from this.
     * @param listener ... to remove.
     */
    abstract protected void removeListener(Listener listener);
}

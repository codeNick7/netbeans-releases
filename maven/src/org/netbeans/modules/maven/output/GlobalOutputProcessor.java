/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.modules.maven.output;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.modules.maven.api.output.OutputProcessor;
import org.netbeans.modules.maven.api.output.OutputVisitor;
import org.netbeans.modules.maven.options.MavenOptionController;
import static org.netbeans.modules.maven.output.Bundle.*;
import org.openide.awt.HtmlBrowser.URLDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;

/**
 * processing start, end and steps of build process
 * @author Milos Kleint
 */
public class GlobalOutputProcessor implements OutputProcessor {
    private static final String SECTION_PROJECT = "project-execute"; //NOI18N
    /*test*/ static final Pattern DOWNLOAD = Pattern.compile("^(\\d+(/\\d*)? ?(M|K|b|KB|B|\\?)\\s*)+$"); //NOI18N
    private static final Pattern LOW_MVN = Pattern.compile("(.*)Error resolving version for (.*): Plugin requires Maven version (.*)"); //NOI18N
    private static final Pattern HELP = Pattern.compile("\\[Help \\d+\\] (https?://.+)"); // NOI18N
    
    /** Creates a new instance of GlobalOutputProcessor */
    public GlobalOutputProcessor() {
    }

    public String[] getRegisteredOutputSequences() {
        return new String[] {SECTION_PROJECT};
    }

    @Messages("TXT_ChangeSettings=NetBeans: Click here to change your settings.")
    public void processLine(String line, OutputVisitor visitor) {
        if (DOWNLOAD.matcher(line).matches()) {
            visitor.skipLine();
            return;
        }
        if ("BUILD SUCCESSFUL".equals(line)) { //NOI18N
            visitor.setColor(Color.GREEN.darker().darker());
            return;
        }
        if (LOW_MVN.matcher(line).matches()) {
            visitor.setLine(line + '\n' + TXT_ChangeSettings());
            visitor.setColor(Color.RED);
            visitor.setOutputListener(new OutputListener() {
                public void outputLineSelected(OutputEvent ev) {}
                public void outputLineAction(OutputEvent ev) {
                    OptionsDisplayer.getDefault().open(OptionsDisplayer.ADVANCED + "/" + MavenOptionController.OPTIONS_SUBPATH); //NOI18N
                }
                public void outputLineCleared(OutputEvent ev) {}
            });
            return;
        }
        final Matcher m = HELP.matcher(line);
        if (m.matches()) {
            visitor.setOutputListener(new OutputListener() {
                public @Override void outputLineAction(OutputEvent ev) {
                    try {
                        URLDisplayer.getDefault().showURLExternal(new URL(m.group(1)));
                    } catch (MalformedURLException x) {
                        Exceptions.printStackTrace(x);
                    }
                }
                public @Override void outputLineSelected(OutputEvent ev) {}
                public @Override void outputLineCleared(OutputEvent ev) {}
            });
        }
    }

    public void sequenceStart(String sequenceId, OutputVisitor visitor) {
        if (sequenceId.startsWith(SECTION_PROJECT)) {
//            visitor.setLine(sequenceId);
        } else {
            visitor.setLine("[" + sequenceId.substring("mojo-execute#".length()) + "]"); //NOI18N
            visitor.setColor(Color.GRAY);
        }
    }

    public void sequenceEnd(String sequenceId, OutputVisitor visitor) {
    }

    public void sequenceFail(String sequenceId, OutputVisitor visitor) {
    }
    
    
}

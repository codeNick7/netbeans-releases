/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package org.netbeans.modules.project.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

@ActionID(
        category = "File",
        id = "org.netbeans.modules.project.ui.actions.CloseOtherProjectsAction"
        )
@ActionRegistration(
        displayName = "#CTL_CloseOtherProjectsAction"
        )
@ActionReference(path = "Menu/File", position = 750)
@Messages("CTL_CloseOtherProjectsAction=C&lose Other Projects")
public final class CloseOtherProjectsAction implements ActionListener {

    private static final RequestProcessor RP = new RequestProcessor(CloseAllProjectsAction.class);
    private final List<Project> selectedProjects;

    public CloseOtherProjectsAction(List<Project> selectedProjects) {
        this.selectedProjects = selectedProjects;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RP.post(new Runnable() { //#249720
            @Override
            public void run() { 
                OpenProjects manager = OpenProjects.getDefault();
                List<Project> openProjects = new ArrayList<Project>(Arrays.asList(manager.getOpenProjects()));
                openProjects.removeAll(selectedProjects);
                if (!openProjects.isEmpty()) {
                    Project[] otherProjectsToBeClosed = openProjects.toArray(new Project[openProjects.size()]);
                    manager.close(otherProjectsToBeClosed);
                }
            }
        });    
    }
}

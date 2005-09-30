/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2005 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.versioning.system.cvss.ui.actions.status;

import org.netbeans.modules.versioning.system.cvss.ui.syncview.CvsSynchronizeTopComponent;
import org.netbeans.modules.versioning.system.cvss.util.Utils;
import org.netbeans.modules.versioning.system.cvss.util.Context;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.api.project.*;
import org.openide.util.NbBundle;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.SystemAction;
import org.openide.filesystems.FileUtil;

import java.awt.event.ActionEvent;

/**
 * Open the Versioning status view for all projects
 * 
 * @author Maros Sandor
 */
public class StatusProjectsAction extends SystemAction {
    
    public StatusProjectsAction() {
        setIcon(null);
        putValue("noIconInMenu", Boolean.TRUE); // NOI18N        
    }

    public String getName() {
        return NbBundle.getMessage(StatusProjectsAction.class, "CTL_MenuItem_StatusProjects_Label");
    }

    public HelpCtx getHelpCtx() {
        return new HelpCtx(StatusProjectsAction.class);
    }

    public boolean isEnabled() {
        Project projects[] = OpenProjects.getDefault().getOpenProjects();
        for (int i = 0; i < projects.length; i++) {
            Project project = projects[i];
            if (Utils.isVersionedProject(project)) {
                return true;
            }
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        RequestProcessor.getDefault().post(new Runnable() {
            public void run() {
                performAction();
            }
        });
    }

    private void performAction() {
        CvsSynchronizeTopComponent stc = CvsSynchronizeTopComponent.getInstance();
        Project [] projects = OpenProjects.getDefault().getOpenProjects();

        Context ctx = Utils.getProjectsContext(projects);
        String title;
        if (projects.length == 1) {
            Project project = projects[0];
            ProjectInformation pinfo = ProjectUtils.getInformation(project);
            title = pinfo.getDisplayName();
        } else {
            title = NbBundle.getMessage(StatusProjectsAction.class, "CTL_StatusProjects_WindowTitle", Integer.toString(projects.length));
        }
        stc.setContentTitle(title);
        stc.setContext(ctx);
        stc.open(); 
        stc.requestActive();
        if (shouldPostRefresh()) {
            stc.performRefreshAction();
        }
    }

    protected boolean shouldPostRefresh() {
        return true;
    }
}

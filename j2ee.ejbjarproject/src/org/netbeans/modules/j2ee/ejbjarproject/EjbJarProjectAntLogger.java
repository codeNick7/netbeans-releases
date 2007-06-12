/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.j2ee.ejbjarproject;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.module.spi.AntEvent;
import org.apache.tools.ant.module.spi.AntLogger;
import org.apache.tools.ant.module.spi.AntSession;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 * Logger which should suppress or prettify typical Ant output from a
 * web project's build-impl.xml.
 * @author Marek Fukala
 */
public final class EjbJarProjectAntLogger extends AntLogger {
    
    /** Default constructor for lookup. */
    public EjbJarProjectAntLogger() {
    }
    
    public boolean interestedInSession(AntSession session) {
        // Even if the initiating project is not a Web Project, suppress these messages.
        // However disable our tricks when running at VERBOSE or higher.
        //return session.getVerbosity() <= AntEvent.LOG_INFO;
        return true;
    }
    
    private static boolean isEjbJarProject(File dir) {
        FileObject projdir = FileUtil.toFileObject(FileUtil.normalizeFile(dir));
        try {
            Project proj = ProjectManager.getDefault().findProject(projdir);
            if (proj != null) {
                // Check if it is a WebProject.
                return proj instanceof EjbJarProject;
            }
        } catch (IOException e) {
            Logger.getLogger("global").log(Level.INFO, null, e);
        }
        return false;
    }
    
    public boolean interestedInScript(File script, AntSession session) {
        if (script.getName().equals("build-impl.xml")) { // NOI18N
            File parent = script.getParentFile();
            if (parent != null && parent.getName().equals("nbproject")) { // NOI18N
                File parent2 = parent.getParentFile();
                if (parent2 != null) {
                    return isEjbJarProject(parent2);
                }
            }
        }
        // Was not a Web Project's nbproject/build-impl.xml; ignore it.
        return false;
    }
    
    public String[] interestedInTargets(AntSession session) {
        return AntLogger.ALL_TARGETS;
    }
    
    public String[] interestedInTasks(AntSession session) {
        // XXX will eventually need them all anyway; as is, could list just javac
        return AntLogger.ALL_TASKS;
    }
    
    public int[] interestedInLogLevels(AntSession session) {
        return new int[] {
            AntEvent.LOG_WARN
        };
    }
    
    public void messageLogged(AntEvent event) {
        // filter out following message
        if (!event.isConsumed() && event.getLogLevel() == AntEvent.LOG_WARN &&
            event.getMessage().startsWith("Trying to override old definition of " + // NOI18N
                "task http://www.netbeans.org/ns/j2ee-ejbjarproject/")) { // NOI18N
            event.consume();
        }
    }

}

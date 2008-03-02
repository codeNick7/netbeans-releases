/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
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
 */

package org.netbeans.modules.ant.debugger;

import java.io.File;
import java.lang.StringBuffer;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.module.api.AntProjectCookie;
import org.apache.tools.ant.module.spi.AntLogger;
import org.apache.tools.ant.module.spi.AntSession;
import org.apache.tools.ant.module.spi.AntEvent;
import org.netbeans.api.debugger.DebuggerEngine;
import org.netbeans.api.debugger.DebuggerInfo;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.spi.debugger.SessionProvider;
import org.openide.execution.ExecutorTask;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Lookup;
        
        
/*
 * AntTest.java
 *
 * Created on 19. leden 2004, 20:03
 */

/**
 *
 * @author  Honza
 */
public class DebuggerAntLogger extends AntLogger {
    
    
    static DebuggerAntLogger getDefault () {
        Iterator it = Lookup.getDefault ().lookup (
            new Lookup.Template (AntLogger.class)
        ).allInstances ().iterator ();
        while (it.hasNext ()) {
            AntLogger al = (AntLogger) it.next ();
            if (al instanceof DebuggerAntLogger) {
                return (DebuggerAntLogger) al;
            }
        }
        throw new InternalError ();
    }
    
    /**
     * Fired only if the build could not even be started.
     * {@link AntEvent#getException} will be non-null.
     * The default implementation does nothing.
     * @param event the associated event object
     */
    public void buildInitializationFailed (AntEvent event) {
//        File script = event.getScriptLocation ();
//        int lineNumber = event.getLine ();
//
//        AntSession session = event.getSession ();
//        String message = event.getMessage ();
//        String target = event.getTargetName ();
//        String task = event.getTaskName ();
//        Set properties = event.getPropertyNames ();
//        Utils.markCurrent (event);
//        S ystem.out.println(event);
    }
    
    /**
     * Fired once when a build is started.
     * The default implementation does nothing.
     * @param event the associated event object
     */
    public void buildStarted (AntEvent event) {
    }
    
    /**
     * Fired once when a build is finished.
     * The default implementation does nothing.
     * @param event the associated event object
     * @see AntEvent#getException
     */
    public void buildFinished (AntEvent event) {
        AntDebugger d = getDebugger (event.getSession (), event);
        if (d == null) return;
        d.buildFinished (event);
        finishDebugging (d);
    }
    
    /**
     * Fired when a target is started.
     * It is <em>not</em> guaranteed that {@link AntEvent#getTargetName}
     * will be non-null (as can happen in some circumstances with
     * <code>&lt;import&gt;</code>, for example).
     * The default implementation does nothing.
     * @param event the associated event object
     */
    public void targetStarted (AntEvent event) {
        AntDebugger d = getDebugger (event.getSession (), event);
        if (d == null) return;
        d.targetStarted (event);
    }
    
    /**
     * Fired when a target is finished.
     * It is <em>not</em> guaranteed that {@link AntEvent#getTargetName}
     * will be non-null.
     * The default implementation does nothing.
     * @param event the associated event object
     */
    public void targetFinished (AntEvent event) {
        AntDebugger d = getDebugger (event.getSession (), event);
        if (d == null) return;
        d.targetFinished (event);
    }
    
    /**
     * Fired when a task is started.
     * It is <em>not</em> guaranteed that {@link AntEvent#getTaskName} or
     * {@link AntEvent#getTaskStructure} will be non-null, though they will
     * usually be defined.
     * {@link AntEvent#getTargetName} might also be null.
     * The default implementation does nothing.
     * @param event the associated event object
     */
    public void taskStarted (AntEvent event) {
        AntDebugger d = getDebugger (event.getSession (), event);
        if (d == null) return;
        d.taskStarted (event);
    }
    
    /**
     * Fired when a task is finished.
     * It is <em>not</em> guaranteed that {@link AntEvent#getTaskName} or
     * {@link AntEvent#getTaskStructure} will be non-null.
     * {@link AntEvent#getTargetName} might also be null.
     * The default implementation does nothing.
     * @param event the associated event object
     */
    public void taskFinished (AntEvent event) {
        AntDebugger d = getDebugger (event.getSession (), event);
        if (d == null) return;
        d.taskFinished (event);
    }

    /**
     * Fired when a message is logged.
     * The task and target fields may or may not be defined.
     * The default implementation does nothing.
     * @param event the associated event object
     */
    public void messageLogged (AntEvent event) {
    }

    /**
     * Mark whether this logger is interested in a given Ant session.
     * @param session a session which is about to be start
     * @return true to receive events about it; by default, false
     */
    public boolean interestedInSession (AntSession session) {
        return true;
    }
    
    /**
     * Mark whether this logger is interested in any Ant script.
     * If true, no events will be masked due to the script location.
     * Note that a few events have no defined script and so will only
     * be delivered to loggers interested in all scripts; typically this
     * applies to debugging messages when a project is just being configured.
     * @param session the relevant session
     * @return true to receive events for all scripts; by default, false
     */
    public boolean interestedInAllScripts (AntSession session) {
        return true;
    }
    
    /**
     * Mark whether this logger is interested in a given Ant script.
     * Called only if {@link #interestedInAllScripts} is false.
     * Only events with a defined script according to {@link AntEvent#getScriptLocation}
     * which this logger is interested in will be delivered.
     * Note that a few events have no defined script and so will only
     * be delivered to loggers interested in all scripts; typically this
     * applies to debugging messages when a project is just being configured.
     * Note also that a single session can involve many different scripts.
     * @param script a particular build script
     * @param session the relevant session
     * @return true to receive events sent from this script; by default, false
     */
    public boolean interestedInScript (File script, AntSession session) {
        return true;
    }

    /**
     * Mark which kinds of targets this logger is interested in.
     * This applies to both target start and finish events, as well as any other
     * events for which {@link AntEvent#getTargetName} is not null, such as task
     * start and finish events, and message log events.
     * If {@link #NO_TARGETS}, no events with specific targets will be sent to it.
     * If a specific list, only events with defined target names included in the list
     * will be sent to it.
     * If {@link #ALL_TARGETS}, all events not otherwise excluded will be sent to it.
     * @param session the relevant session
     * @return a nonempty (and non-null) list of target names; by default, {@link #NO_TARGETS}
     */
    public String[] interestedInTargets (AntSession session) {
        return ALL_TARGETS;
    }
    
    /**
     * Mark which kinds of tasks this logger is interested in.
     * This applies to both task start and finish events, as well as any other
     * events for which {@link AntEvent#getTaskName} is not null, such as
     * message log events.
     * If {@link #NO_TASKS}, no events with specific tasks will be sent to it.
     * If a specific list, only events with defined task names included in the list
     * will be sent to it.
     * If {@link #ALL_TASKS}, all events not otherwise excluded will be sent to it.
     * @param session the relevant session
     * @return a nonempty (and non-null) list of task names; by default, {@link #NO_TASKS}
     */
    public String[] interestedInTasks (AntSession session) {
        return ALL_TASKS;
    }
    
    /**
     * Mark which kinds of message log events this logger is interested in.
     * This applies only to message log events and no others.
     * Only events with log levels included in the returned list will be delivered.
     * @param session the relevant session
     * @return a list of levels such as {@link AntEvent#LOG_INFO}; by default, an empty list
     * @see AntSession#getVerbosity
     */
    public int[] interestedInLogLevels (AntSession session) {
        return new int[] {
            AntEvent.LOG_INFO,
            AntEvent.LOG_DEBUG,
            AntEvent.LOG_ERR,
            AntEvent.LOG_VERBOSE,
            AntEvent.LOG_WARN
        };
    }
    
    /** AntSession => AntDebugger */
    private Map runningDebuggers = new HashMap ();
    /** AntDebugger => AntSession */
    private Map runningDebuggers2 = new HashMap ();
    private Set filesToDebug = new HashSet ();
    /** File => WeakReference -> ExecutorTask */
    private Map fileExecutors = new HashMap();
    
    void debugFile (File f) {
        filesToDebug.add (f);
    }
    
    void fileExecutor(File f, ExecutorTask execTask) {
        fileExecutors.put(f, new WeakReference(execTask));
    }
    
    private void finishDebugging (
        AntDebugger debugger
    ) {
        AntSession session = (AntSession) runningDebuggers2.remove (debugger);
        runningDebuggers.remove (session);
    }
    
    private AntDebugger getDebugger (AntSession s, AntEvent antEvent) {
        AntDebugger d = (AntDebugger) runningDebuggers.get (s);
        if (d != null) return d;
        
        if (!filesToDebug.contains (s.getOriginatingScript ())) 
            return null;
        filesToDebug.remove (s.getOriginatingScript ());
        Reference execRef = (Reference) fileExecutors.remove(s.getOriginatingScript());
        ExecutorTask execTask = null;
        if (execRef != null) {
            execTask = (ExecutorTask) execRef.get();
        }
        
        // start debugging othervise
        try {
            FileObject fo = FileUtil.toFileObject (s.getOriginatingScript ());
            DataObject dob = DataObject.find (fo);
            AntProjectCookie antCookie = (AntProjectCookie) dob.getCookie 
                (AntProjectCookie.class);
            if (antCookie == null)
                throw new NullPointerException ();
            d = startDebugging (antCookie, antEvent, execTask);
            runningDebuggers.put (s, d);
            runningDebuggers2.put (d, s);
            return d;
        } catch (DataObjectNotFoundException ex) {
            ex.printStackTrace ();
            return null;
        }
    }

    private static AntDebugger startDebugging (
        final AntProjectCookie antCookie,
        final AntEvent         antEvent,
        final ExecutorTask     execTask
    ) {
        DebuggerInfo di = DebuggerInfo.create (
            "AntDebuggerInfo",
            new Object[] {
                new SessionProvider () {
                    public String getSessionName () {
                        return antEvent.getSession ().getDisplayName ();
                    }
                    
                    public String getLocationName () {
                        return "localhost";
                    }
                    
                    public String getTypeID () {
                        return "AntSession";
                    }

                    public Object[] getServices () {
                        return new Object[] {};
                    }
                },
                antCookie
            }
        );
        DebuggerEngine[] es = DebuggerManager.getDebuggerManager ().
            startDebugging (di);
        AntDebugger debugger = es[0].lookupFirst(null, AntDebugger.class);
        debugger.setExecutor(execTask);
        return debugger;
    }
}

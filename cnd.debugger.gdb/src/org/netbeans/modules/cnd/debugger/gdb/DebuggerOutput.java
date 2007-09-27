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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
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

package org.netbeans.modules.cnd.debugger.gdb;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.api.debugger.ActionsManagerListener;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.LazyActionsManagerListener;
//import org.netbeans.modules.cnd.debugger.gdb.GdbThread;
import org.netbeans.spi.debugger.ContextProvider;

import org.openide.util.NbBundle;


/**
 * Listens on
 * {@link org.netbeans.api.debugger.ActionsManagerListener#PROP_ACTION_PERFORMED} and
 * {@link org.netbeans.modules.cnd.debugger.gdb.GdbDebugger#PROP_STATE}
 * properties and writes some messages to Debugger Console.
 *
 * @author   Gordon Prieur (copied from Jan Jancura's JPDA implementation)
 */
public class DebuggerOutput extends LazyActionsManagerListener implements PropertyChangeListener {


    // set of all IOManagers
    private static Set          managers = new HashSet();
    
    private GdbDebugger        debugger;
    private IOManager           ioManager;
    private ContextProvider     contextProvider;


    public DebuggerOutput(ContextProvider contextProvider) {
        this.contextProvider = contextProvider;
        this.debugger = (GdbDebugger) contextProvider.lookupFirst(null, GdbDebugger.class);
        
        // close old tabs
        if (DebuggerManager.getDebuggerManager().getSessions().length == 1) {
            Iterator i = managers.iterator();
            while (i.hasNext()) {
                ((IOManager) i.next()).close();
            }
            managers = new HashSet();
        }
        
        // open new tab
        String title = NbBundle.getBundle(IOManager.class).getString("CTL_DebuggerConsole_Title"); // NOI18N                
        ioManager = new IOManager(title);
        managers.add(ioManager);
        
        debugger.addPropertyChangeListener(GdbDebugger.PROP_STATE, this);
    }

    protected synchronized void destroy() {
        debugger.removePropertyChangeListener(GdbDebugger.PROP_STATE, this);
        debugger = null;
        ioManager = null;
    }

    public String[] getProperties() {
        return new String[] {ActionsManagerListener.PROP_ACTION_PERFORMED};
    }

    public void propertyChange(PropertyChangeEvent evt) {
        //GdbThread t;
        String debuggerState;
        IOManager ioManager;
        synchronized (this) {
            if (debugger == null) {
                return;
            }
            //t = debugger.getCurrentThread ();
            debuggerState = debugger.getState();
            ioManager = this.ioManager;
        }
        if (debuggerState == GdbDebugger.STATE_STARTING) {
            print("CTL_Launching", new String[] { }, null); // NOI18N
        } else if (debuggerState == GdbDebugger.STATE_RUNNING) {
            print("CTL_Debugger_running", new String[] { }, null); // NOI18N
        } else if (debuggerState == GdbDebugger.STATE_EXITED) {
            print("CTL_Debugger_finished", new String[] { }, null); // NOI18N
        } else if (debuggerState == GdbDebugger.STATE_NONE) {
            Throwable e = null;
            try {
                synchronized (this) {
                    if (debugger != null) {
                        debugger.waitRunning();
                    }
                }
            } catch (DebuggerStartException ex) {
                e = ex.getTargetException();
            }
            if (e == null) {
                print("CTL_Debugger_finished", null, null); // NOI18N
            } else {
                String message = e.getMessage();
                if (message != null) {
                    ioManager.println(message, null);
                } else {
                    ioManager.println(e.toString(), null);
                }
                //e.printStackTrace ();
            }
            ioManager.closeStream();
        } else if (debuggerState == GdbDebugger.STATE_STOPPED) {
            String language = DebuggerManager.getDebuggerManager().getCurrentSession().getCurrentLanguage();
            print("CTL_Debugger_stopped", new String[] { }, null); // NOI18N
//            String threadName = t.getName ();
//            String methodName = t.getMethodName ();
//            String className = t.getClassName ();
//            int lineNumber = t.getLineNumber (language);
//            try {
//                String sourceName = t.getSourceName (language);
//                String relativePath = EditorContextBridge.getRelativePath 
//                    (t, language);
//                String url = null;
////                synchronized (this) {
////                    if (relativePath != null && engineContext != null) {
////                        url = engineContext.getURL(relativePath, true);
////                    }
////                }
//                IOManager.Line line = null;
//                if (lineNumber > 0 && url != null)
//                    line = new IOManager.Line (
//                        url, 
//                        lineNumber,
//                        debugger
//                    );
//
//                if (lineNumber > 0)
//                    print (
//                        "CTL_Thread_stopped",
//                      //  IOManager.DEBUGGER_OUT + IOManager.STATUS_OUT,
//                        new String[] {
//                            threadName,
//                            sourceName,
//                            methodName,
//                            "" + lineNumber
//                        },
//                        line
//                    );
//                else if (sourceName.length() > 0 && methodName.length() > 0)
//                    print (
//                        "CTL_Thread_stopped_no_line",
//                    //    IOManager.DEBUGGER_OUT + IOManager.STATUS_OUT,
//                        new String[] {
//                            threadName,
//                            sourceName,
//                            methodName
//                        },
//                        line
//                    );
//                else
//                    print (
//                        "CTL_Thread_stopped_no_line_no_source",
//                        new String[] { threadName },
//                        line
//                    );
//            } catch (AbsentInformationException ex) {
//                if (lineNumber > 0)
//                    print (
//                        "CTL_Thread_stopped_no_info",
//                     //   IOManager.DEBUGGER_OUT + IOManager.STATUS_OUT,
//                        new String[] {
//                            threadName,
//                            className,
//                            methodName,
//                            lineNumber > 0 ? "" + lineNumber : ""
//                        },
//                        null
//                    );
//                else
//                    print (
//                        "CTL_Thread_stopped_no_info_no_line",
//                        //IOManager.DEBUGGER_OUT + IOManager.STATUS_OUT,
//                        new String[] {
//                            threadName,
//                            className,
//                            methodName
//                        },
//                        null
//                    );
//            }
        }
    }

    public void actionPerformed(Object action, boolean success) {
        if (success) {
            if (action == ActionsManager.ACTION_CONTINUE) {
                print("CTL_Continue", null, null); // NOI18N
            } else if (action == ActionsManager.ACTION_STEP_INTO) {
                print("CTL_Step_Into", null, null); // NOI18N
            } else if (action == ActionsManager.ACTION_STEP_OUT) {
                print("CTL_Step_Out", null, null); // NOI18N
            } else if (action == ActionsManager.ACTION_STEP_OVER) {
                print("CTL_Step_Over", null, null); // NOI18N
            }
        }
    }

    public IOManager getIOManager() {
        return ioManager;
    }

    // helper methods ..........................................................

    private void print(String message, String[] args, IOManager.Line line) {
        String text = (args == null) ? NbBundle.getMessage(DebuggerOutput.class, message) :
            new MessageFormat(NbBundle.getMessage(DebuggerOutput.class, message)).format(args);

        IOManager ioManager;
        synchronized (this) {
            ioManager = this.ioManager;
            if (ioManager == null) {
                return;
            }
        }
        ioManager.println(text, line);
    }
}

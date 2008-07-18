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

package org.netbeans.modules.debugger.jpda.ui.models;

import com.sun.jdi.AbsentInformationException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;

import javax.swing.SwingUtilities;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.jpda.CallStackFrame;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.api.debugger.jpda.JPDADebugger;
import org.netbeans.api.debugger.jpda.JPDAThread;
import org.netbeans.api.debugger.jpda.JPDAThreadGroup;
import org.netbeans.modules.debugger.jpda.ui.SourcePath;
import org.netbeans.spi.viewmodel.NodeActionsProvider;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.netbeans.spi.viewmodel.Models;
import org.netbeans.spi.viewmodel.TreeModel;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;


/**
 * @author   Martin Entlicher
 */
public class DebuggingActionsProvider implements NodeActionsProvider {

    private Action MAKE_CURRENT_ACTION = Models.createAction (
        NbBundle.getBundle(DebuggingActionsProvider.class).getString("CTL_ThreadAction_MakeCurrent_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                if (node instanceof JPDAThread) {
                    return debugger.getCurrentThread () != node;
                }
                if (node instanceof CallStackFrame) {
                    CallStackFrame f = (CallStackFrame) node;
                    return //f.getThread() == debugger.getCurrentThread() &&
                           !f.equals(debugger.getCurrentCallStackFrame());
                }
                return false;
            }
            
            public void perform (Object[] nodes) {
                if (nodes.length == 0) return ;
                if (nodes[0] instanceof JPDAThread) {
                    ((JPDAThread) nodes [0]).makeCurrent ();
                    goToSource((JPDAThread) nodes [0]);
                }
                if (nodes[0] instanceof CallStackFrame) {
                    CallStackFrame f = (CallStackFrame) nodes[0];
                    f.getThread().makeCurrent();
                    f.makeCurrent ();
                    goToSource(f);
                }
            }
        },
        Models.MULTISELECTION_TYPE_EXACTLY_ONE
    
    );

    private final Action COPY_TO_CLBD_ACTION = Models.createAction (
        NbBundle.getBundle(DebuggingActionsProvider.class).getString("CTL_CallstackAction_Copy2CLBD_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                return true;
            }
            public void perform (Object[] nodes) {
                List<JPDAThread> threads = new ArrayList<JPDAThread>(nodes.length);
                for (Object node : nodes) {
                    if (node instanceof JPDAThread) {
                        threads.add((JPDAThread) node);
                    }
                    if (node instanceof CallStackFrame) {
                        JPDAThread t = ((CallStackFrame) node).getThread();
                        if (!threads.contains(t)) {
                            threads.add(t);
                        }
                    }
                }
                if (threads.isEmpty()) {
                    threads.add(debugger.getCurrentThread());
                }
                stackToCLBD (threads);
            }
        },
        Models.MULTISELECTION_TYPE_ANY
    );

    private static Action GO_TO_SOURCE_ACTION = Models.createAction (
        NbBundle.getBundle(DebuggingActionsProvider.class).getString("CTL_ThreadAction_GoToSource_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                if (!(node instanceof CallStackFrame)) {
                    return false;
                }
                return isGoToSourceSupported ((CallStackFrame) node);
            }
            
            public void perform (Object[] nodes) {
                goToSource((CallStackFrame) nodes [0]);
            }
        },
        Models.MULTISELECTION_TYPE_EXACTLY_ONE
    
    );

    private static final Action POP_TO_HERE_ACTION = Models.createAction (
        NbBundle.getBundle(ThreadsActionsProvider.class).getString("CTL_CallstackAction_PopToHere_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                // TODO: Check whether this frame is deeper then the top-most
                return true;
            }
            public void perform (final Object[] nodes) {
                // Do not do expensive actions in AWT,
                // It can also block if it can not procceed for some reason
                RequestProcessor.getDefault().post(new Runnable() {
                    public void run() {
                        popToHere ((CallStackFrame) nodes [0]);
                    }
                });
            }
        },
        Models.MULTISELECTION_TYPE_EXACTLY_ONE
    );

    private Action SUSPEND_ACTION = Models.createAction (
        NbBundle.getBundle(DebuggingActionsProvider.class).getString("CTL_ThreadAction_Suspend_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                //if (node instanceof MonitorModel.ThreadWithBordel) node = ((MonitorModel.ThreadWithBordel) node).originalThread;
                if (node instanceof JPDAThread) {
                    return !((JPDAThread) node).isSuspended ();
                }
                if (node instanceof JPDAThreadGroup) {
                    return true;
                }
                return false;
            }
            
            public void perform (Object[] nodes) {
                int i, k = nodes.length;
                for (i = 0; i < k; i++) {
                    Object node = (nodes[i] instanceof MonitorModel.ThreadWithBordel) ? 
                            ((MonitorModel.ThreadWithBordel) nodes[i]).originalThread : nodes[i];
                    if (node instanceof JPDAThread)
                        ((JPDAThread) node).suspend ();
                    else
                        ((JPDAThreadGroup) node).suspend ();
                }
            }
        },
        Models.MULTISELECTION_TYPE_ALL
    
    );

    private Action RESUME_ACTION = Models.createAction (
        NbBundle.getBundle(DebuggingActionsProvider.class).getString("CTL_ThreadAction_Resume_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                //if (node instanceof MonitorModel.ThreadWithBordel) node = ((MonitorModel.ThreadWithBordel) node).originalThread;
                if (node instanceof JPDAThread) {
                    return ((JPDAThread) node).isSuspended ();
                }
                if (node instanceof JPDAThreadGroup) {
                    return true;
                }
                return false;
            }
            
            public void perform (Object[] nodes) {
                int i, k = nodes.length;
                for (i = 0; i < k; i++) {
                    Object node = (nodes[i] instanceof MonitorModel.ThreadWithBordel) ? 
                            ((MonitorModel.ThreadWithBordel) nodes[i]).originalThread : nodes[i];
                    if (node instanceof JPDAThread)
                        ((JPDAThread) node).resume ();
                    else
                        ((JPDAThreadGroup) node).resume ();
                }
            }
        },
        Models.MULTISELECTION_TYPE_ALL
    
    );
        
    private Action INTERRUPT_ACTION = Models.createAction (
        NbBundle.getBundle(DebuggingActionsProvider.class).getString("CTL_ThreadAction_Interrupt_Label"),
        new Models.ActionPerformer () {
            public boolean isEnabled (Object node) {
                if (node instanceof MonitorModel.ThreadWithBordel) node = ((MonitorModel.ThreadWithBordel) node).originalThread;
                if (node instanceof JPDAThread)
                    return !((JPDAThread) node).isSuspended ();
                else
                    return false;
            }
            
            public void perform (Object[] nodes) {
                int i, k = nodes.length;
                for (i = 0; i < k; i++) {
                    Object node = (nodes[i] instanceof MonitorModel.ThreadWithBordel) ? 
                            ((MonitorModel.ThreadWithBordel) nodes[i]).originalThread : nodes[i];
                    if (node instanceof JPDAThread) {
                        ((JPDAThread) node).interrupt();
                    }
                }
            }
        },
        Models.MULTISELECTION_TYPE_ALL
    
    );
        
    private JPDADebugger debugger;
    
    
    public DebuggingActionsProvider (ContextProvider lookupProvider) {
        debugger = lookupProvider.lookupFirst(null, JPDADebugger.class);
    }
    
    public Action[] getActions (Object node) throws UnknownTypeException {
        if (node == TreeModel.ROOT)
            return new Action [0];
        if (node instanceof JPDAThreadGroup) {
            return new Action [] {
                RESUME_ACTION,
                SUSPEND_ACTION,
            };
        } else
        if (node instanceof JPDAThread) {
            JPDAThread t = (JPDAThread) node;
            boolean suspended = t.isSuspended ();
            Action a = null;
            if (suspended)
                a = RESUME_ACTION;
            else
                a = SUSPEND_ACTION;
            return new Action [] {
                MAKE_CURRENT_ACTION,
                a,
                INTERRUPT_ACTION // ,
                //GO_TO_SOURCE_ACTION,
            };
        } else
        if (node instanceof CallStackFrame) {
            boolean popToHere = debugger.canPopFrames ();
            if (popToHere) {
                return new Action [] {
                    MAKE_CURRENT_ACTION,
                    POP_TO_HERE_ACTION,
                    GO_TO_SOURCE_ACTION,
                    COPY_TO_CLBD_ACTION,
                };
            } else {
                return new Action [] {
                    MAKE_CURRENT_ACTION,
                    GO_TO_SOURCE_ACTION,
                    COPY_TO_CLBD_ACTION,
                };
            }
        } else
        throw new UnknownTypeException (node);
    }
    
    public void performDefaultAction (Object node) throws UnknownTypeException {
        if (node == TreeModel.ROOT) {
            return;
        }
        if (node instanceof JPDAThread) {
            ((JPDAThread) node).makeCurrent ();
        } else if (node instanceof JPDAThreadGroup) {
            return;
        } else if (node instanceof CallStackFrame) {
            CallStackFrame f = (CallStackFrame) node;
            f.getThread().makeCurrent();
            f.makeCurrent();
            goToSource(f);
        }
        throw new UnknownTypeException (node);
    }

    /** 
     *
     * @param l the listener to add
     */
    public void addModelListener (ModelListener l) {
    }

    /** 
     *
     * @param l the listener to remove
     */
    public void removeModelListener (ModelListener l) {
    }

    private static void popToHere (final CallStackFrame frame) {
        try {
            JPDAThread t = frame.getThread ();
            CallStackFrame[] stack = t.getCallStack ();
            int i, k = stack.length;
            if (k < 2) return ;
            for (i = 0; i < k; i++)
                if (stack [i].equals (frame)) {
                    if (i > 0) {
                        stack [i - 1].popFrame ();
                    }
                    return;
                }
        } catch (AbsentInformationException ex) {
        }
    }

    private void stackToCLBD(List<JPDAThread> threads) {
        StringBuffer frameStr = new StringBuffer(512);
        for (JPDAThread t : threads) {
            if (frameStr.length() > 0) {
                frameStr.append('\n');
            }
            frameStr.append("\"");
            frameStr.append(t.getName());
            frameStr.append("\"\n");
            CallStackFrame[] stack;
            try {
                stack = t.getCallStack ();
            } catch (AbsentInformationException ex) {
                frameStr.append(NbBundle.getMessage(CallStackActionsProvider.class, "MSG_NoSourceInfo"));
                stack = null;
            }
            if (stack != null) {
                int i, k = stack.length;

                for (i = 0; i < k; i++) {
                    frameStr.append(stack[i].getClassName());
                    frameStr.append(".");
                    frameStr.append(stack[i].getMethodName());
                    try {
                        String sourceName = stack[i].getSourceName(null);
                        frameStr.append("(");
                        frameStr.append(sourceName);
                        int line = stack[i].getLineNumber(null);
                        if (line > 0) {
                            frameStr.append(":");
                            frameStr.append(line);
                        }
                        frameStr.append(")");
                    } catch (AbsentInformationException ex) {
                        //frameStr.append(NbBundle.getMessage(CallStackActionsProvider.class, "MSG_NoSourceInfo"));
                        // Ignore, do not provide source name.
                    }
                    if (i != k - 1) frameStr.append('\n');
                }
            }
        }
        Clipboard systemClipboard = getClipboard();
        Transferable transferableText =
                new StringSelection(frameStr.toString());
        systemClipboard.setContents(
                transferableText,
                null);
    }

    private static Clipboard getClipboard() {
        Clipboard clipboard = org.openide.util.Lookup.getDefault().lookup(Clipboard.class);
        if (clipboard == null) {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        }
        return clipboard;
    }

    private static boolean isGoToSourceSupported (CallStackFrame f) {
        String language = DebuggerManager.getDebuggerManager ().
            getCurrentSession ().getCurrentLanguage ();
        SourcePath sp = DebuggerManager.getDebuggerManager().getCurrentEngine().lookupFirst(null, SourcePath.class);
        return sp.sourceAvailable (f, language);
    }
    
    private static void goToSource(final CallStackFrame frame) {
        SwingUtilities.invokeLater (new Runnable () {
            public void run () {
                String language = DebuggerManager.getDebuggerManager ().
                    getCurrentSession ().getCurrentLanguage ();
                SourcePath sp = DebuggerManager.getDebuggerManager().getCurrentEngine().lookupFirst(null, SourcePath.class);
                sp.showSource (frame, language);
            }
        });
    }
    
    private static void goToSource(final JPDAThread thread) {
        SwingUtilities.invokeLater (new Runnable () {
            public void run () {
                String language = DebuggerManager.getDebuggerManager ().
                    getCurrentSession ().getCurrentLanguage ();
                SourcePath sp = DebuggerManager.getDebuggerManager().getCurrentEngine().lookupFirst(null, SourcePath.class);
                sp.showSource (thread, language);
            }
        });
    }

}

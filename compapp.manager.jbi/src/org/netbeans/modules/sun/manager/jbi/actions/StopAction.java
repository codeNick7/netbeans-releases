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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.sun.manager.jbi.actions;

import javax.swing.SwingUtilities;
import org.netbeans.modules.sun.manager.jbi.nodes.Refreshable;
import org.netbeans.modules.sun.manager.jbi.nodes.Stoppable;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.NodeAction;

/**
 * Action to stop one or more JBI Components and/or Service Assemblies.
 * 
 * @author jqian
 */
public class StopAction extends NodeAction {
   
    protected void performAction(final Node[] activatedNodes) {
        
        RequestProcessor.getDefault().post(new Runnable() {
            public void run() {
                try {
                    for (Node node : activatedNodes) {
                        Lookup lookup = node.getLookup();
                        Stoppable stoppable = lookup.lookup(Stoppable.class);
                        
                        if (stoppable != null) {
                            stoppable.stop();
                            
                            final Refreshable refreshable =
                                    lookup.lookup(Refreshable.class);
                            if (refreshable != null){
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        refreshable.refresh();
                                    }
                                });
                            }
                        }
                    }
                } catch(RuntimeException rex) {
                    //gobble up exception
                }
            }
        });
    }
    
    protected boolean enable(Node[] nodes) {
        boolean ret = false;
        
        if (nodes != null && nodes.length > 0) {            
            ret = true;            
            for (Node node : nodes) {
                Stoppable stoppable = node.getLookup().lookup(Stoppable.class);                
                try {
                    if (stoppable != null && !stoppable.canStop()) {
                        ret = false;
                        break;
                    }
                } catch(RuntimeException rex) {
                    //gobble up exception
                }
            }
        }
        
        return ret;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    public String getName() {
        return NbBundle.getMessage(StopAction.class, "LBL_StopAction"); // NOI18N
    }    
}

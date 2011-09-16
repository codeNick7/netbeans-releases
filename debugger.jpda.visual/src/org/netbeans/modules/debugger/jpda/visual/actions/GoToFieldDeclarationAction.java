/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */
package org.netbeans.modules.debugger.jpda.visual.actions;

import org.netbeans.api.debugger.jpda.ObjectVariable;
import org.netbeans.api.debugger.jpda.Variable;
import org.netbeans.modules.debugger.jpda.JPDADebuggerImpl;
import org.netbeans.modules.debugger.jpda.ui.SourcePath;
import org.netbeans.modules.debugger.jpda.visual.JavaComponentInfo;
import org.netbeans.modules.debugger.jpda.visual.JavaComponentInfo.FieldInfo;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.Notification;
import org.openide.awt.NotificationDisplayer;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

/**
 * Go to the component source.
 * 
 * @author Martin Entlicher
 */
public class GoToFieldDeclarationAction extends NodeAction {
    
    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final JavaComponentInfo ci = n.getLookup().lookup(JavaComponentInfo.class);
            if (ci != null) {
                final FieldInfo fieldInfo = ci.getField();
                if (fieldInfo != null) {
                    GoToSourceAction.RP.post(new Runnable() {
                        @Override
                        public void run() {
                            org.netbeans.api.debugger.jpda.Field fieldVariable;
                            JPDADebuggerImpl debugger = ci.getThread().getDebugger();
                            Variable variable = debugger.getVariable(fieldInfo.getParent().getComponent());
                            fieldVariable = ((ObjectVariable) variable).getField(fieldInfo.getName());
                            SourcePath ectx = debugger.getSession().lookupFirst(null, SourcePath.class);
                            ectx.showSource (fieldVariable);
                        }
                    });
                } else {
                    NotifyDescriptor d = new NotifyDescriptor.Message(java.util.ResourceBundle.getBundle("org/netbeans/modules/debugger/jpda/visual/actions/Bundle").getString("MSG_NoFieldInfo"), NotifyDescriptor.WARNING_MESSAGE);
                    DialogDisplayer.getDefault().notifyLater(d);
                }
            }
        }
    }
    
    @Override
    protected boolean enable(Node[] activatedNodes) {
        return true;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(GoToFieldDeclarationAction.class, "CTL_GoToFieldDeclaration");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(GoToFieldDeclarationAction.class);
    }

    
}

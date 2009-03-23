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
package org.netbeans.modules.bugzilla.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.modules.bugzilla.BugzillaConfig;
import org.netbeans.spi.options.OptionsPanelController;

/**
 *
 * @author Tomas Stupka
 */
public final class BugzillaOptionsController extends OptionsPanelController implements ActionListener {
    
    private final BugzillaOptionsPanel panel;
        
    public BugzillaOptionsController() {
        panel = new BugzillaOptionsPanel();
    }
    
    public void update() {
        panel.issuesTextField.setText(BugzillaConfig.getInstance().getIssueRefresh() + "");  // NOI18N
        panel.queriesTextField.setText(BugzillaConfig.getInstance().getQueryRefresh() + ""); // NOI18N
    }
    
    public void applyChanges() {
        String queryRefresh = panel.queriesTextField.getText().trim();
        int r = queryRefresh.equals("") ? 0 : Integer.parseInt(queryRefresh);   // NOI18N
        BugzillaConfig.getInstance().setQueryRefresh(r);

        String issueRefresh = panel.issuesTextField.getText().trim();
        r = issueRefresh.equals("") ? 0 : Integer.parseInt(issueRefresh);       // NOI18N
        BugzillaConfig.getInstance().setIssueRefresh(r);
    }
    
    public void cancel() {
        update();
    }
    
    public boolean isValid() {
        String queryRefresh = panel.queriesTextField.getText().trim();
        String issueRefresh = panel.issuesTextField.getText().trim();

        return isValidRefreshValue(queryRefresh) &&
               isValidRefreshValue(issueRefresh);
    }

    private boolean isValidRefreshValue(String s) {
        if(!s.equals("")) {                                                     // NOI18N
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public boolean isChanged() {
        return !panel.issuesTextField.getText().trim().equals(BugzillaConfig.getInstance().getIssueRefresh() + "") ||  // NOI18N
               !panel.queriesTextField.getText().trim().equals(BugzillaConfig.getInstance().getQueryRefresh() + "");   // NOI18N
    }
        
    public org.openide.util.HelpCtx getHelpCtx() {
        return new org.openide.util.HelpCtx(getClass());
    }
    
    public javax.swing.JComponent getComponent(org.openide.util.Lookup masterLookup) {
        return panel;
    }
    
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        
    }
    
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        
    }
    
    public void actionPerformed(ActionEvent evt) {
        
    }
}

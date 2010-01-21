/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */
package org.netbeans.modules.apisupport.project.ui.customizer;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

final class ClusterizeVisualPanel1 extends JPanel {
    private final ClusterizeWizardPanel1 panel;
    private ProgressHandle handle;

    ClusterizeVisualPanel1(ClusterizeWizardPanel1 aThis) {
        panel = aThis;
        handle = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("org/netbeans/modules/apisupport/project/ui/customizer/Bundle").getString("MSG_ClusterizeScanning"));
        initComponents();
        progressPanel.add(BorderLayout.CENTER, ProgressHandleFactory.createProgressComponent(handle));
        progressName.add(BorderLayout.CENTER, ProgressHandleFactory.createMainLabelComponent(handle));
        putClientProperty("WizardPanel_contentSelectedIndex", Integer.valueOf(0)); // NOI18N
        putClientProperty("WizardPanel_contentData", panel.settings.getSteps()); // NOI18N
        putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE); // NOI18N
        putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE); // NOI18N
        putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE); // NOI18N
    }

    @Override
    public String getName() {
        return panel.settings.getStep(0);
    }

    void showProgress() {
        handle.start();
        progressPanel.setVisible(true);
    }
    void hideProgress() {
        progressPanel.setVisible(false);
        progressPanel.removeAll();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        warningPanel = new javax.swing.JLabel();
        progressPanel = new javax.swing.JPanel();
        progressName = new javax.swing.JPanel();

        org.openide.awt.Mnemonics.setLocalizedText(warningPanel, org.openide.util.NbBundle.getMessage(ClusterizeVisualPanel1.class, "MSG_NotValidCluster", panel.settings.file));
        warningPanel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        progressPanel.setLayout(new java.awt.BorderLayout());

        progressName.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(warningPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(progressName, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(progressPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(warningPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel progressName;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JLabel warningPanel;
    // End of variables declaration//GEN-END:variables
}


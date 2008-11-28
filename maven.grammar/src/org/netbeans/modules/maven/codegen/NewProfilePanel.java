/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.netbeans.modules.maven.codegen;

import org.netbeans.modules.maven.model.pom.POMModel;
import org.openide.awt.Mnemonics;

/**
 *
 * @author mkleint
 */
public class NewProfilePanel extends javax.swing.JPanel {
    private POMModel model;

    public NewProfilePanel(POMModel model) {
        initComponents();
        this.model = model;
        boolean pomPackaging = "pom".equals(model.getProject().getPackaging()); //NOI18N
        if (!pomPackaging) {
            Mnemonics.setLocalizedText(cbPlugins, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.cbPlugins.text2")); // NOI18N
            Mnemonics.setLocalizedText(cbDependencies, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.cbDependencies.text2")); // NOI18N
        }
        lblHint.setText(""); //NOI18N
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        cbActProperty = new javax.swing.JCheckBox();
        cbActOS = new javax.swing.JCheckBox();
        cbActFile = new javax.swing.JCheckBox();
        cbPlugins = new javax.swing.JCheckBox();
        cbDependencies = new javax.swing.JCheckBox();
        lblHint = new javax.swing.JLabel();

        lblId.setLabelFor(txtId);
        org.openide.awt.Mnemonics.setLocalizedText(lblId, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.lblId.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cbActProperty, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.cbActProperty.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cbActOS, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.cbActOS.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cbActFile, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.cbActFile.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cbPlugins, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.cbPlugins.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cbDependencies, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.cbDependencies.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblHint, org.openide.util.NbBundle.getMessage(NewProfilePanel.class, "NewProfilePanel.lblHint.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblHint, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .add(cbDependencies)
                    .add(cbPlugins)
                    .add(cbActFile)
                    .add(cbActOS)
                    .add(cbActProperty)
                    .add(layout.createSequentialGroup()
                        .add(lblId)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtId, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblId)
                    .add(txtId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(cbActProperty)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbActOS)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbActFile)
                .add(18, 18, 18)
                .add(cbPlugins)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbDependencies)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 49, Short.MAX_VALUE)
                .add(lblHint))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbActFile;
    private javax.swing.JCheckBox cbActOS;
    private javax.swing.JCheckBox cbActProperty;
    private javax.swing.JCheckBox cbDependencies;
    private javax.swing.JCheckBox cbPlugins;
    private javax.swing.JLabel lblHint;
    private javax.swing.JLabel lblId;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables

    String getProfileId() {
        return txtId.getText();
    }


    boolean isActivation() {
        return isActiovationByFile() || isActiovationByOS() || isActiovationByProperty();
    }

    boolean isActiovationByProperty() {
        return cbActProperty.isSelected();
    }

    boolean isActiovationByOS() {
        return cbActOS.isSelected();
    }

    boolean isActiovationByFile() {
        return cbActFile.isSelected();
    }

    boolean generateDependencies() {
        return cbDependencies.isSelected();
    }

    boolean generatePlugins() {
        return cbPlugins.isSelected();
    }

}

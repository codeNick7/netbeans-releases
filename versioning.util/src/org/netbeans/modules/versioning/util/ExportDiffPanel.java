/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

/*
 * ExportDiffPanel.java
 *
 * Created on Mar 6, 2009, 10:43:25 PM
 */

package org.netbeans.modules.versioning.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author tomas
 */
public class ExportDiffPanel extends ExportDiffSupport.AbstractExportDiffPanel implements ActionListener {
    private JComponent attachComponent;

    /** Creates new form ExportDiffPanel */
    public ExportDiffPanel(JComponent attachComponent) {
        initComponents();
        this.attachComponent = attachComponent;
        asFileRadioButton.addActionListener(this);
        attachRadioButton.addActionListener(this);
        attachPanel.add(attachComponent);

        attachComponent.setEnabled(false);
        fileTextField.setEnabled(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();

        buttonGroup1.add(asFileRadioButton);
        asFileRadioButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(asFileRadioButton, org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.asFileRadioButton.text")); // NOI18N
        asFileRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asFileRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(attachRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(attachRadioButton, org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.attachRadioButton.text")); // NOI18N

        attachPanel.setLayout(new java.awt.BorderLayout());

        fileTextField.setText(org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.fileTextField.text")); // NOI18N
        fileTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileTextFieldActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.browseButton.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(attachPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(asFileRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(attachRadioButton)
                        .addContainerGap(398, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(asFileRadioButton)
                    .addComponent(fileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(attachRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attachPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
        );

        asFileRadioButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.asFileRadioButton.AccessibleContext.accessibleDescription")); // NOI18N
        attachRadioButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.attachRadioButton.AccessibleContext.accessibleDescription")); // NOI18N
        fileTextField.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.fileTextField.AccessibleContext.accessibleName")); // NOI18N
        fileTextField.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.fileTextField.AccessibleContext.accessibleDescription")); // NOI18N
        browseButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.browseButton.AccessibleContext.accessibleDescription")); // NOI18N

        getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ExportDiffPanel.class, "ExportDiffPanel.AccessibleContext.accessibleDescription")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void asFileRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asFileRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asFileRadioButtonActionPerformed

    private void fileTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    final javax.swing.JRadioButton asFileRadioButton = new javax.swing.JRadioButton();
    final javax.swing.JPanel attachPanel = new javax.swing.JPanel();
    final javax.swing.JRadioButton attachRadioButton = new javax.swing.JRadioButton();
    final javax.swing.JButton browseButton = new javax.swing.JButton();
    private javax.swing.ButtonGroup buttonGroup1;
    final javax.swing.JTextField fileTextField = new javax.swing.JTextField();
    // End of variables declaration//GEN-END:variables

    public void actionPerformed(ActionEvent e) {
        if(asFileRadioButton.isSelected()) {
            attachComponent.setEnabled(false);
            fileTextField.setEnabled(true);
        } else {
            attachComponent.setEnabled(true);
            fileTextField.setEnabled(false);
        }
    }

    @Override
    public String getOutputFileText() {
        return fileTextField.getText();
    }

    @Override
    public void setOutputFileText(String text) {
        fileTextField.setText(text);
    }

    @Override
    public void addOutputFileTextDocumentListener(DocumentListener list) {
        fileTextField.getDocument().addDocumentListener(list);
    }

    @Override
    public void addBrowseActionListener(ActionListener actionListener) {
        browseButton.addActionListener(actionListener);
    }

    @Override
    public boolean isFileOutputSelected() {
        return asFileRadioButton.isSelected();
    }

}

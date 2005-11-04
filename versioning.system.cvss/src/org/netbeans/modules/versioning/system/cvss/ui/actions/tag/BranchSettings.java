/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2005 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.versioning.system.cvss.ui.actions.tag;

import org.netbeans.modules.versioning.system.cvss.settings.CvsModuleConfig;
import org.netbeans.modules.versioning.system.cvss.util.Utils;
import org.netbeans.modules.versioning.system.cvss.ui.selectors.BranchSelector;
import org.netbeans.lib.cvsclient.CVSRoot;
import org.openide.DialogDescriptor;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.io.IOException;
import java.io.File;
import org.openide.util.*;

/**
 * Settings panel for the Branch command.
 * 
 * @author Maros Sandor
 */
class BranchSettings extends javax.swing.JPanel {
    
    private final File[] roots;
    private boolean autoComputeBaseTagName = true;

    public BranchSettings(File [] roots) {
        this.roots = roots;
        initComponents();
        cbTagBase.setSelected(CvsModuleConfig.getDefault().getDefaultValue("BranchSettings.tagBase", true)); // NOI18N
        tfBaseTagName.setText(CvsModuleConfig.getDefault().getDefaultValue("BranchSettings.tagBaseName", NbBundle.getMessage(BranchSettings.class, "BK0001")));  // NOI18N
        tfBaseTagName.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                autoComputeBaseTagName = computeBaseTagName().equals(tfBaseTagName.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                autoComputeBaseTagName = computeBaseTagName().equals(tfBaseTagName.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                autoComputeBaseTagName = computeBaseTagName().equals(tfBaseTagName.getText());
            }
        });
        cbCheckoutBranch.setSelected(CvsModuleConfig.getDefault().getDefaultValue("BranchSettings.checkout", true)); // NOI18N
        tfName.setText(CvsModuleConfig.getDefault().getDefaultValue("BranchSettings.branchName", NbBundle.getMessage(BranchSettings.class, "BK0002"))); // NOI18N
        tfName.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                refreshComponents();
            }

            public void insertUpdate(DocumentEvent e) {
                refreshComponents();
            }

            public void removeUpdate(DocumentEvent e) {
                refreshComponents();
            }
        });
        refreshComponents();
    }

    public boolean isCheckingOutBranch() {
        return cbCheckoutBranch.isSelected();
    }

    public boolean isTaggingBase() {
        return cbTagBase.isSelected();
    }

    public String getBranchName() {
        return tfName.getText();
    }

    public String getBaseTagName() {
        return tfBaseTagName.getText();
    }
    
    public void saveSettings() {
        CvsModuleConfig.getDefault().setDefaultValue("BranchSettings.tagBase", cbTagBase.isSelected());  // NOI18N
        CvsModuleConfig.getDefault().setDefaultValue("BranchSettings.checkout", cbCheckoutBranch.isSelected()); // NOI18N
        CvsModuleConfig.getDefault().setDefaultValue("BranchSettings.branchName", tfName.getText()); // NOI18N
    }

    private String computeBaseTagName() {
        return NbBundle.getMessage(BranchSettings.class, "BK0003", tfName.getText()); // NOI18N
    }
    
    private void refreshComponents() {
        jLabel1.setEnabled(cbTagBase.isSelected());
        tfBaseTagName.setEnabled(cbTagBase.isSelected());
        if (autoComputeBaseTagName && cbTagBase.isSelected()) {
            tfBaseTagName.setText(computeBaseTagName());
        }
        DialogDescriptor dd = (DialogDescriptor) getClientProperty("org.openide.DialogDescriptor"); // NOI18N
        if (dd != null) {
            dd.setValid(tfName.getText().trim().length() > 0);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cbTagBase = new javax.swing.JCheckBox();
        cbCheckoutBranch = new javax.swing.JCheckBox();
        nameLabel = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfBaseTagName = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cbTagBase.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(cbTagBase, java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("CTL_BranchForm_TagBase"));
        cbTagBase.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("TT_BranchForm_TagBase"));
        cbTagBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTagBaseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(cbTagBase, gridBagConstraints);

        cbCheckoutBranch.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(cbCheckoutBranch, java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("CTL_BranchForm_UpdateToBranch"));
        cbCheckoutBranch.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("TT_BranchForm_UpdateToBranch"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(cbCheckoutBranch, gridBagConstraints);

        nameLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("MNE_BranchForm_BranchName").charAt(0));
        nameLabel.setLabelFor(tfName);
        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("CTL_BranchForm_BranchName"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(nameLabel, gridBagConstraints);

        tfName.setColumns(20);
        tfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchNameChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        add(tfName, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("CTL_BranchForm_BrowseBranch"));
        jButton1.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("TT_BranchForm_Browse"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBranches(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        add(jButton1, gridBagConstraints);

        jLabel1.setLabelFor(tfBaseTagName);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("CTL_BranchForm_BaseTagName"));
        jLabel1.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/versioning/system/cvss/ui/actions/tag/Bundle").getString("TT_BranchForm_BaseTagName"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 21, 0, 5);
        add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        add(tfBaseTagName, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void browseBranches(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBranches
        for (int i = 0; i < roots.length; i++) {
            try {
                CVSRoot.parse(Utils.getCVSRootFor(roots[i]));  // raises exception
                BranchSelector selector = new BranchSelector();
                String tag = selector.selectTag(roots[i], null);
                if (tag != null) {
                    tfName.setText(tag);
                }
                return;
            } catch (IOException e) {
                // no root for this file, try next
            }
        }
    }//GEN-LAST:event_browseBranches

    private void branchNameChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchNameChanged
        refreshComponents();
    }//GEN-LAST:event_branchNameChanged

    private void cbTagBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTagBaseActionPerformed
        refreshComponents();
    }//GEN-LAST:event_cbTagBaseActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbCheckoutBranch;
    private javax.swing.JCheckBox cbTagBase;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField tfBaseTagName;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables
}

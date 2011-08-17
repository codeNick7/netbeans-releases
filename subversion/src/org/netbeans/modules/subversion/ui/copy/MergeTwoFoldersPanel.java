/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package org.netbeans.modules.subversion.ui.copy;

import org.tigris.subversion.svnclientadapter.SVNRevision;

/**
 *
 * @author  Petr Kuzel
 */
public class MergeTwoFoldersPanel extends javax.swing.JPanel {

    /** Creates new form WorkdirPanel */
    public MergeTwoFoldersPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();

        setName(org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Message")); // NOI18N

        mergeStartRepositoryFolderLabel.setLabelFor(mergeStartUrlComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(mergeStartRepositoryFolderLabel, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_FirstFolder")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(mergeEndBrowseButton, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Browse2")); // NOI18N

        mergeEndUrlComboBox.setEditable(true);

        mergeAfterRevisionLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modules/subversion/resources/icons/ending_revision.png"))); // NOI18N
        mergeAfterRevisionLabel.setLabelFor(mergeEndRevisionTextField);
        org.openide.awt.Mnemonics.setLocalizedText(mergeAfterRevisionLabel, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Ending")); // NOI18N

        mergeEndRevisionTextField.setText(SVNRevision.HEAD.toString());
        mergeEndRevisionTextField.setToolTipText(org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_EmptyHint")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(mergeStartSearchButton, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Search1")); // NOI18N

        mergeEndRepositoryFolderLabel.setLabelFor(mergeEndUrlComboBox);
        org.openide.awt.Mnemonics.setLocalizedText(mergeEndRepositoryFolderLabel, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Second")); // NOI18N

        mergeStartUrlComboBox.setEditable(true);

        org.openide.awt.Mnemonics.setLocalizedText(mergeStartBrowseButton, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Browse1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(mergeEndSearchButton, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Search2")); // NOI18N

        mergeStartRevisionTextField.setText(SVNRevision.HEAD.toString());
        mergeStartRevisionTextField.setToolTipText(org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_EmptyHint")); // NOI18N

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modules/subversion/resources/icons/starting_revision.png"))); // NOI18N
        jLabel8.setLabelFor(mergeStartRevisionTextField);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Starting")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(mergeStartBrowseRevisionButton, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Browse1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(mergeEndBrowseRevisionButton, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeTwoPanel_Browse1")); // NOI18N

        cbIncludeStartRevision.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(cbIncludeStartRevision, org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeDialog.includeStartRevision.text")); // NOI18N
        cbIncludeStartRevision.setToolTipText(org.openide.util.NbBundle.getMessage(MergeTwoFoldersPanel.class, "CTL_MergeDialog.includeStartRevision.TTtext")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mergeEndRepositoryFolderLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mergeAfterRevisionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mergeStartRepositoryFolderLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbIncludeStartRevision)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mergeEndUrlComboBox, 0, 397, Short.MAX_VALUE)
                            .addComponent(mergeStartUrlComboBox, 0, 397, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mergeStartRevisionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mergeStartSearchButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mergeStartBrowseRevisionButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mergeEndRevisionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mergeEndSearchButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mergeEndBrowseRevisionButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(mergeEndBrowseButton)
                            .addComponent(mergeStartBrowseButton)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeStartUrlComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeStartBrowseButton)
                    .addComponent(mergeStartRepositoryFolderLabel))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeStartRevisionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeStartSearchButton)
                    .addComponent(jLabel8)
                    .addComponent(mergeStartBrowseRevisionButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbIncludeStartRevision)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeEndRepositoryFolderLabel)
                    .addComponent(mergeEndUrlComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeEndBrowseButton))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeEndRevisionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mergeEndSearchButton)
                    .addComponent(mergeAfterRevisionLabel)
                    .addComponent(mergeEndBrowseRevisionButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mergeStartRepositoryFolderLabel.getAccessibleContext().setAccessibleDescription("First Repository Folder");
        mergeEndBrowseButton.getAccessibleContext().setAccessibleDescription("Browse Repository Folders");
        mergeEndUrlComboBox.getAccessibleContext().setAccessibleDescription("Second Repository Folder");
        mergeAfterRevisionLabel.getAccessibleContext().setAccessibleDescription("End Repository Revision for Merging");
        mergeEndRevisionTextField.getAccessibleContext().setAccessibleName("Ending Revision for Merging");
        mergeEndRevisionTextField.getAccessibleContext().setAccessibleDescription("Ending Revision for Merging");
        mergeStartSearchButton.getAccessibleContext().setAccessibleDescription("Search Repository Revision Number");
        mergeEndRepositoryFolderLabel.getAccessibleContext().setAccessibleDescription("Second Repository Folder");
        mergeStartUrlComboBox.getAccessibleContext().setAccessibleDescription("Second Repository Folder");
        mergeStartBrowseButton.getAccessibleContext().setAccessibleDescription("Browse Repository Folders");
        mergeEndSearchButton.getAccessibleContext().setAccessibleDescription("Search Repository Revision");
        mergeStartRevisionTextField.getAccessibleContext().setAccessibleDescription("Staring Repository Revision for Merging");
        jLabel8.getAccessibleContext().setAccessibleDescription("Starting Revision for Merging");
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    final javax.swing.JCheckBox cbIncludeStartRevision = new javax.swing.JCheckBox();
    private javax.swing.JLabel jLabel8;
    final javax.swing.JLabel mergeAfterRevisionLabel = new javax.swing.JLabel();
    final javax.swing.JButton mergeEndBrowseButton = new javax.swing.JButton();
    final javax.swing.JButton mergeEndBrowseRevisionButton = new javax.swing.JButton();
    final javax.swing.JLabel mergeEndRepositoryFolderLabel = new javax.swing.JLabel();
    final javax.swing.JTextField mergeEndRevisionTextField = new javax.swing.JTextField();
    final javax.swing.JButton mergeEndSearchButton = new javax.swing.JButton();
    final javax.swing.JComboBox mergeEndUrlComboBox = new javax.swing.JComboBox();
    final javax.swing.JButton mergeStartBrowseButton = new javax.swing.JButton();
    final javax.swing.JButton mergeStartBrowseRevisionButton = new javax.swing.JButton();
    final javax.swing.JLabel mergeStartRepositoryFolderLabel = new javax.swing.JLabel();
    final javax.swing.JTextField mergeStartRevisionTextField = new javax.swing.JTextField();
    final javax.swing.JButton mergeStartSearchButton = new javax.swing.JButton();
    final javax.swing.JComboBox mergeStartUrlComboBox = new javax.swing.JComboBox();
    // End of variables declaration//GEN-END:variables
    
}

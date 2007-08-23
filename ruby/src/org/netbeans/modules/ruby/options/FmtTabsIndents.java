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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.ruby.options;

import static org.netbeans.modules.ruby.options.FmtOptions.*;
import static org.netbeans.modules.ruby.options.FmtOptions.CategorySupport.OPTION_ID;
import org.netbeans.modules.ruby.options.FmtOptions.CategorySupport;
import org.openide.util.NbBundle;

/**
 *
 * @author  phrebejk
 */
public class FmtTabsIndents extends javax.swing.JPanel {
   
    /** Creates new form FmtTabsIndents */
    public FmtTabsIndents() {
        initComponents();
        // Not yet implemented
        //indentCasesFromSwitchCheckBox.setVisible(false);
        
        expandTabsToSpacesCheckBox.putClientProperty(OPTION_ID, expandTabToSpaces);
        tabSizeField.putClientProperty(OPTION_ID, tabSize);
        indentSizeField.putClientProperty(OPTION_ID, indentSize);
        continuationIndentSizeField.putClientProperty(OPTION_ID, continuationIndentSize);
        reformatCommentsCheckBox.putClientProperty(OPTION_ID, reformatComments);        
        indentHtmlCheckBox.putClientProperty(OPTION_ID, indentHtml);        
        rightMarginField.putClientProperty(OPTION_ID, rightMargin);
    }
    
    public static FormatingOptionsPanel.Category getController() {
        return new CategorySupport(
                "LBL_TabsAndIndents", 
                new FmtTabsIndents(),    // NOI18N   
                NbBundle.getMessage(FmtTabsIndents.class, "SAMPLE_TabsIndents"), // NOI18N
                new String[] { FmtOptions.rightMargin, "30" }
                ); // NOI18N
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField3 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        expandTabsToSpacesCheckBox = new javax.swing.JCheckBox();
        tabSizeLabel = new javax.swing.JLabel();
        tabSizeField = new javax.swing.JTextField();
        indentSizeLabel = new javax.swing.JLabel();
        indentSizeField = new javax.swing.JTextField();
        continuationIndentSizeLabel = new javax.swing.JLabel();
        continuationIndentSizeField = new javax.swing.JTextField();
        rightMarginLabel = new javax.swing.JLabel();
        rightMarginField = new javax.swing.JTextField();
        reformatCommentsCheckBox = new javax.swing.JCheckBox();
        indentHtmlCheckBox = new javax.swing.JCheckBox();

        jTextField3.setText("jTextField3");

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox3, "jCheckBox3");
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));

        setOpaque(false);

        org.openide.awt.Mnemonics.setLocalizedText(expandTabsToSpacesCheckBox, org.openide.util.NbBundle.getMessage(FmtTabsIndents.class, "LBL_TabToSpaces")); // NOI18N
        expandTabsToSpacesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        expandTabsToSpacesCheckBox.setOpaque(false);

        org.openide.awt.Mnemonics.setLocalizedText(tabSizeLabel, org.openide.util.NbBundle.getMessage(FmtTabsIndents.class, "LBL_TabSize")); // NOI18N

        tabSizeField.setColumns(3);

        org.openide.awt.Mnemonics.setLocalizedText(indentSizeLabel, org.openide.util.NbBundle.getMessage(FmtTabsIndents.class, "LBL_IndentSize")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(continuationIndentSizeLabel, org.openide.util.NbBundle.getMessage(FmtTabsIndents.class, "LBL_ContinuationIndentSize")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(rightMarginLabel, org.openide.util.NbBundle.getMessage(FmtTabsIndents.class, "LBL_RightMargin")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(reformatCommentsCheckBox, org.openide.util.NbBundle.getMessage(FmtTabsIndents.class, "LBL_ReformatComments")); // NOI18N
        reformatCommentsCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.openide.awt.Mnemonics.setLocalizedText(indentHtmlCheckBox, org.openide.util.NbBundle.getMessage(FmtTabsIndents.class, "LBL_IndentHTML")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(expandTabsToSpacesCheckBox)
                    .add(continuationIndentSizeLabel)
                    .add(indentSizeLabel)
                    .add(tabSizeLabel)
                    .add(rightMarginLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(rightMarginField)
                    .add(continuationIndentSizeField)
                    .add(indentSizeField)
                    .add(tabSizeField)))
            .add(reformatCommentsCheckBox)
            .add(indentHtmlCheckBox)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(expandTabsToSpacesCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tabSizeLabel)
                    .add(tabSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(indentSizeLabel)
                    .add(indentSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(continuationIndentSizeLabel)
                    .add(continuationIndentSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rightMarginLabel)
                    .add(rightMarginField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(reformatCommentsCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(indentHtmlCheckBox)
                .addContainerGap(156, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField continuationIndentSizeField;
    private javax.swing.JLabel continuationIndentSizeLabel;
    private javax.swing.JCheckBox expandTabsToSpacesCheckBox;
    private javax.swing.JCheckBox indentHtmlCheckBox;
    private javax.swing.JTextField indentSizeField;
    private javax.swing.JLabel indentSizeLabel;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JCheckBox reformatCommentsCheckBox;
    private javax.swing.JTextField rightMarginField;
    private javax.swing.JLabel rightMarginLabel;
    private javax.swing.JTextField tabSizeField;
    private javax.swing.JLabel tabSizeLabel;
    // End of variables declaration//GEN-END:variables
    
}

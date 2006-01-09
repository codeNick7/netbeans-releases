/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2000 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.options.generaleditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

/**
 * Implementation of one panel in Options Dialog.
 *
 * @author Jan Jancura
 */
public class GeneralEditorPanel extends JPanel implements ActionListener {

    private boolean         changed = false;
    private boolean         listen = false;
    
    /** 
     * Creates new form GeneralEditorPanel.
     */
    public GeneralEditorPanel () {
        initComponents ();
        
        loc (lCodeFolding, "Code_Folding");
        loc (lUseCodeFolding, "Code_Folding_Section");
        loc (lCollapseByDefault, "Fold_by_Default");
        loc (lCodeCompletion, "Code_Completion");
        loc (lCodeCompletion2, "Code_Completion_Section");
            
        loc (cbUseCodeFolding, "Use_Folding");
        loc (cbFoldMethods, "Fold_Methods");
        loc (cbFoldInnerClasses, "Fold_Classes");
        loc (cbFoldImports, "Fold_Imports");
        loc (cbFoldJavadocComments, "Fold_JavaDoc");
        loc (cbFoldInitialComments, "Fold_Licence");

        loc (cbAutoPopup, "Auto_Popup_Completion_Window");
        loc (cbInsertSingleProposalsAutomatically, "Insert_Single_Proposals_Automatically");
        loc (cbCaseSensitive, "Case_Sensitive_Code_Completion");
        loc (cbShowDeprecated, "Show_Deprecated_Members");
        loc (cbInsertClosingBracketsAutomatically, "Pair_Character_Completion");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        lCodeFolding = new javax.swing.JLabel();
        lUseCodeFolding = new javax.swing.JLabel();
        lCollapseByDefault = new javax.swing.JLabel();
        cbUseCodeFolding = new javax.swing.JCheckBox();
        cbFoldMethods = new javax.swing.JCheckBox();
        cbFoldInnerClasses = new javax.swing.JCheckBox();
        cbFoldImports = new javax.swing.JCheckBox();
        cbFoldJavadocComments = new javax.swing.JCheckBox();
        cbFoldInitialComments = new javax.swing.JCheckBox();
        lCodeCompletion = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lCodeCompletion2 = new javax.swing.JLabel();
        cbAutoPopup = new javax.swing.JCheckBox();
        cbInsertSingleProposalsAutomatically = new javax.swing.JCheckBox();
        cbCaseSensitive = new javax.swing.JCheckBox();
        cbShowDeprecated = new javax.swing.JCheckBox();
        cbInsertClosingBracketsAutomatically = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();

        lCodeFolding.setText("Code Folding");

        lUseCodeFolding.setText("Use Code Folding:");

        lCollapseByDefault.setText("Collapse by Default:");

        cbUseCodeFolding.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbUseCodeFolding.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbFoldMethods.setText("Methods");
        cbFoldMethods.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbFoldMethods.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbFoldInnerClasses.setText("Inner Classes");
        cbFoldInnerClasses.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbFoldInnerClasses.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbFoldImports.setText("Imports");
        cbFoldImports.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbFoldImports.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbFoldJavadocComments.setText("Javadoc Comments");
        cbFoldJavadocComments.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbFoldJavadocComments.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbFoldInitialComments.setText("Initial Comments");
        cbFoldInitialComments.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbFoldInitialComments.setMargin(new java.awt.Insets(0, 0, 0, 0));

        lCodeCompletion.setText("Code Completion");

        lCodeCompletion2.setText("Code Completion:");

        cbAutoPopup.setText("Auto Popup Code Completion Window");
        cbAutoPopup.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbAutoPopup.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbInsertSingleProposalsAutomatically.setText("Insert Single Proposals Automatically");
        cbInsertSingleProposalsAutomatically.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbInsertSingleProposalsAutomatically.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbCaseSensitive.setText("Case Sensitive Code Completion");
        cbCaseSensitive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbCaseSensitive.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbShowDeprecated.setText("Show Deprecated Members In Code Completion");
        cbShowDeprecated.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbShowDeprecated.setMargin(new java.awt.Insets(0, 0, 0, 0));

        cbInsertClosingBracketsAutomatically.setText("Insert Closing Brackets Automatically");
        cbInsertClosingBracketsAutomatically.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbInsertClosingBracketsAutomatically.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lUseCodeFolding)
                    .add(lCodeCompletion2)
                    .add(lCollapseByDefault))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cbUseCodeFolding)
                    .add(cbFoldMethods)
                    .add(cbFoldInnerClasses)
                    .add(cbFoldImports)
                    .add(cbFoldJavadocComments)
                    .add(cbFoldInitialComments)
                    .add(cbAutoPopup)
                    .add(cbInsertSingleProposalsAutomatically)
                    .add(cbCaseSensitive)
                    .add(cbShowDeprecated)
                    .add(cbInsertClosingBracketsAutomatically))
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(lCodeCompletion)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(lCodeFolding)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(lCodeFolding)
                    .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lUseCodeFolding)
                    .add(cbUseCodeFolding))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lCollapseByDefault)
                    .add(cbFoldMethods))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbFoldInnerClasses)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbFoldImports)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbFoldJavadocComments)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbFoldInitialComments)
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(lCodeCompletion)
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(lCodeCompletion2)
                    .add(cbAutoPopup))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbInsertSingleProposalsAutomatically)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbCaseSensitive)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbShowDeprecated)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbInsertClosingBracketsAutomatically)
                .addContainerGap(73, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbAutoPopup;
    private javax.swing.JCheckBox cbCaseSensitive;
    private javax.swing.JCheckBox cbFoldImports;
    private javax.swing.JCheckBox cbFoldInitialComments;
    private javax.swing.JCheckBox cbFoldInnerClasses;
    private javax.swing.JCheckBox cbFoldJavadocComments;
    private javax.swing.JCheckBox cbFoldMethods;
    private javax.swing.JCheckBox cbInsertClosingBracketsAutomatically;
    private javax.swing.JCheckBox cbInsertSingleProposalsAutomatically;
    private javax.swing.JCheckBox cbShowDeprecated;
    private javax.swing.JCheckBox cbUseCodeFolding;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lCodeCompletion;
    private javax.swing.JLabel lCodeCompletion2;
    private javax.swing.JLabel lCodeFolding;
    private javax.swing.JLabel lCollapseByDefault;
    private javax.swing.JLabel lUseCodeFolding;
    // End of variables declaration//GEN-END:variables
    
    
    private static String loc (String key) {
        return NbBundle.getMessage (GeneralEditorPanel.class, key);
    }
    
    private static void loc (Component c, String key) {
        if (!(c instanceof JLabel)) {
            c.getAccessibleContext ().setAccessibleName (loc ("AN_" + key));
            c.getAccessibleContext ().setAccessibleDescription (loc ("AD_" + key));
        }
        if (c instanceof AbstractButton) {
            Mnemonics.setLocalizedText (
                (AbstractButton) c, 
                loc ("CTL_" + key)
            );
        } else {
            Mnemonics.setLocalizedText (
                (JLabel) c, 
                loc ("CTL_" + key)
            );
        }
    }
    
    private Model model;
    
    void update () {
        listen = false;
        if (model == null) {
            model = new Model ();
            cbUseCodeFolding.addActionListener (this);
            cbFoldMethods.addActionListener (this);
            cbFoldInnerClasses.addActionListener (this);
            cbFoldImports.addActionListener (this);
            cbFoldJavadocComments.addActionListener (this);
            cbFoldInitialComments.addActionListener (this);
            cbAutoPopup.addActionListener (this);
            cbInsertSingleProposalsAutomatically.addActionListener (this);
            cbCaseSensitive.addActionListener (this);
            cbShowDeprecated.addActionListener (this);
            cbInsertClosingBracketsAutomatically.addActionListener (this);
        }
        
        // init code folding
        cbUseCodeFolding.setSelected (model.isShowCodeFolding ());
        cbFoldImports.setSelected (model.isFoldImports ());
        cbFoldInitialComments.setSelected (model.isFoldInitialComment ());
        cbFoldInnerClasses.setSelected (model.isFoldInnerClasses ());
        cbFoldJavadocComments.setSelected (model.isFoldJavaDocComments ());
        cbFoldMethods.setSelected (model.isFoldMethods ());
        updateEnabledState ();
        
        // code completion options
        cbInsertClosingBracketsAutomatically.setSelected 
            (model.isPairCharacterCompletion ());
        cbAutoPopup.setSelected 
            (model.isCompletionAutoPopup ());
        cbShowDeprecated.setSelected 
            (model.isShowDeprecatedMembers ());
        cbInsertSingleProposalsAutomatically.setSelected 
            (model.isCompletionInstantSubstitution ());
        cbCaseSensitive.setSelected
            (model.isCompletionCaseSensitive ());
        
        listen = true;
    }
    
    void applyChanges () {
        
        if (model == null) return;
        // code folding options
        model.setFoldingOptions (
            cbUseCodeFolding.isSelected (),
            cbFoldImports.isSelected (),
            cbFoldInitialComments.isSelected (),
            cbFoldInnerClasses.isSelected (),
            cbFoldJavadocComments.isSelected (),
            cbFoldMethods.isSelected ()
        );
        
        // code completion options
        model.setCompletionOptions (
            cbInsertClosingBracketsAutomatically.isSelected (),
            cbAutoPopup.isSelected (),
            cbShowDeprecated.isSelected (),
            cbInsertSingleProposalsAutomatically.isSelected (),
            cbCaseSensitive.isSelected ()
        );
        changed = false;
    }
    
    void cancel () {
        changed = false;
    }
    
    boolean dataValid () {
        return true;
    }
    
    boolean isChanged () {
        return changed;
    }
    
    public void actionPerformed (ActionEvent e) {
        if (!listen) return;
        if (e.getSource () == cbUseCodeFolding)
            updateEnabledState ();
        changed = true;
    }
    
    
    // other methods ...........................................................
    
    private void updateEnabledState () {
        boolean useCodeFolding = cbUseCodeFolding.isSelected ();
        cbFoldImports.setEnabled (useCodeFolding);
        cbFoldInitialComments.setEnabled (useCodeFolding);
        cbFoldInnerClasses.setEnabled (useCodeFolding);
        cbFoldJavadocComments.setEnabled (useCodeFolding);
        cbFoldMethods.setEnabled (useCodeFolding);
    }
}

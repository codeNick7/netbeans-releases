/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2002 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.testtools.wizards;

/*
 * TestBagSettingsPanel.java
 *
 * Created on April 10, 2002, 1:45 PM
 */

import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import javax.swing.event.DocumentListener;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author  <a href="mailto:adam.sotona@sun.com">Adam Sotona</a>
 */
public class TestBagSettingsPanel extends javax.swing.JPanel implements WizardDescriptor.FinishPanel {

    private static final String DEFAULT_NAME="<default name>";
    
    /** Creates new form TestBagPanel */
    public TestBagSettingsPanel() {
        initComponents();
        DocumentListener list=new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {fireStateChanged();}
            public void removeUpdate(DocumentEvent e) {fireStateChanged();}
            public void changedUpdate(DocumentEvent e) {fireStateChanged();}
        };
        nameField.getDocument().addDocumentListener(list);
        fireStateChanged();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup = new javax.swing.ButtonGroup();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        executorLabel = new javax.swing.JLabel();
        ideRadio = new javax.swing.JRadioButton();
        codeRadio = new javax.swing.JRadioButton();
        attrLabel = new javax.swing.JLabel();
        attrField = new javax.swing.JTextField();
        includeLabel = new javax.swing.JLabel();
        includeField = new javax.swing.JTextField();
        excludeLabel = new javax.swing.JLabel();
        excludeField = new javax.swing.JTextField();
        includeButton = new javax.swing.JButton();
        excludeButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        nameLabel.setText("Test Bag Name: ");
        nameLabel.setDisplayedMnemonic(110);
        nameLabel.setLabelFor(nameField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(nameLabel, gridBagConstraints);

        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameFieldFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(nameField, gridBagConstraints);

        executorLabel.setText("Executor:");
        executorLabel.setDisplayedMnemonic(88);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 4, 4);
        add(executorLabel, gridBagConstraints);

        ideRadio.setMnemonic('D');
        ideRadio.setSelected(true);
        ideRadio.setText("IDE");
        buttonGroup.add(ideRadio);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(ideRadio, gridBagConstraints);

        codeRadio.setMnemonic('C');
        codeRadio.setText("Code");
        buttonGroup.add(codeRadio);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(codeRadio, gridBagConstraints);

        attrLabel.setText("Attributes: ");
        attrLabel.setDisplayedMnemonic(65);
        attrLabel.setLabelFor(attrField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(attrLabel, gridBagConstraints);

        attrField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                attrFieldFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(attrField, gridBagConstraints);

        includeLabel.setText("Execution Includes : ");
        includeLabel.setDisplayedMnemonic(73);
        includeLabel.setLabelFor(includeField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(includeLabel, gridBagConstraints);

        includeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                includeFieldFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(includeField, gridBagConstraints);

        excludeLabel.setText("Execution Excludes: ");
        excludeLabel.setDisplayedMnemonic(69);
        excludeLabel.setLabelFor(excludeField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(excludeLabel, gridBagConstraints);

        excludeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                excludeFieldFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(excludeField, gridBagConstraints);

        includeButton.setText("...");
        includeButton.setPreferredSize(new java.awt.Dimension(30, 20));
        includeButton.setMinimumSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(includeButton, gridBagConstraints);

        excludeButton.setText("...");
        excludeButton.setPreferredSize(new java.awt.Dimension(30, 20));
        excludeButton.setMinimumSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        add(excludeButton, gridBagConstraints);

    }//GEN-END:initComponents

    private void excludeFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_excludeFieldFocusGained
        excludeField.selectAll();
    }//GEN-LAST:event_excludeFieldFocusGained

    private void includeFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_includeFieldFocusGained
        includeField.selectAll();
    }//GEN-LAST:event_includeFieldFocusGained

    private void attrFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_attrFieldFocusGained
        attrField.selectAll();
    }//GEN-LAST:event_attrFieldFocusGained

    private void nameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusGained
        nameField.selectAll();
    }//GEN-LAST:event_nameFieldFocusGained

    public void addChangeListener(javax.swing.event.ChangeListener changeListener) {
    }    
    
    public java.awt.Component getComponent() {
        return this;
    }    
    
    public org.openide.util.HelpCtx getHelp() {
        return new HelpCtx(TestBagSettingsPanel.class);
    }
    
    public void readSettings(Object obj) {
    }
    
    public void removeChangeListener(javax.swing.event.ChangeListener changeListener) {
    }
    
    public void storeSettings(Object obj) {
    }

    public boolean isValid() {
        return true;
    }

    private void fireStateChanged() {
        SwingUtilities.invokeLater (new Runnable () {
            public void run () {
                if (nameField.getText().equals ("")) {
                    nameField.setText(DEFAULT_NAME);
                    nameField.selectAll();
                }
            }
        });            
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    private javax.swing.JRadioButton ideRadio;
    private javax.swing.JLabel includeLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton includeButton;
    private javax.swing.JTextField includeField;
    private javax.swing.JLabel executorLabel;
    private javax.swing.JLabel attrLabel;
    private javax.swing.JTextField attrField;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JLabel excludeLabel;
    private javax.swing.JRadioButton codeRadio;
    private javax.swing.JButton excludeButton;
    private javax.swing.JTextField excludeField;
    // End of variables declaration//GEN-END:variables
    
}

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

package org.netbeans.beaninfo.editors;

import java.awt.Dimension;
import java.util.ResourceBundle;

import org.openide.NotifyDescriptor;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.explorer.propertysheet.editors.EnhancedCustomPropertyEditor;

/*
 * DimensionCustomEditorX.java
 *
 * Created on March 7, 2001, 6:44 PM
 */

/**
 *
 * @author  IanFormanek, Vladimir Zboril
 */
public class DimensionCustomEditor extends javax.swing.JPanel implements EnhancedCustomPropertyEditor {
    
    // the bundle to use
    static ResourceBundle bundle = NbBundle.getBundle (
                                       DimensionCustomEditor.class);

    static final long serialVersionUID =3718340148720193844L;

    /** Creates new form DimensionCustomEditor */
    public DimensionCustomEditor(DimensionEditor editor) {
        initComponents();
        this.editor = editor;
        Dimension dimension = (Dimension)editor.getValue ();
        
        if (dimension == null) dimension = new Dimension (0, 0);
        jLabel1.setText(bundle.getString ("CTL_Dimension"));
        widthLabel.setText (bundle.getString("CTL_Width"));
        widthLabel.setDisplayedMnemonic(bundle.getString("CTL_Width_mnemonic").charAt(0));
        widthLabel.setLabelFor(widthField);
        heightLabel.setText (bundle.getString("CTL_Height"));
        heightLabel.setDisplayedMnemonic(bundle.getString("CTL_Height_mnemonic").charAt(0));
        heightLabel.setLabelFor(heightField);

        widthField.setText ("" + dimension.width);    // NOI18N
        heightField.setText ("" + dimension.height);  // NOI18N
//        HelpCtx.setHelpIDString (this, DimensionCustomEditor.class.getName ());

        widthField.getAccessibleContext().setAccessibleDescription(bundle.getString("ACSD_CTL_Width"));
        heightField.getAccessibleContext().setAccessibleDescription(bundle.getString("ACSD_CTL_Height"));
        getAccessibleContext().setAccessibleDescription(bundle.getString("ACSD_DimensionCustomEditor"));
    }

    public java.awt.Dimension getPreferredSize () {
        return new java.awt.Dimension (280, 160);
    }

    public Object getPropertyValue () throws IllegalStateException {
        try {
            int width = Integer.parseInt (widthField.getText ());
            int height = Integer.parseInt (heightField.getText ());
            if ((width < 0) || (height < 0)) {
                IllegalStateException ise = new IllegalStateException();
                ErrorManager.getDefault().annotate(
                    ise, ErrorManager.ERROR, null, 
                    bundle.getString("CTL_NegativeSize"), null, null);
                throw ise;
            }
            return new Dimension (width, height);
        } catch (NumberFormatException e) {
            IllegalStateException ise = new IllegalStateException();
            ErrorManager.getDefault().annotate(
                ise, ErrorManager.ERROR, null, 
                bundle.getString("CTL_InvalidValue"), null, null);
            throw ise;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        insidePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        widthLabel = new javax.swing.JLabel();
        widthField = new javax.swing.JTextField();
        heightLabel = new javax.swing.JLabel();
        heightField = new javax.swing.JTextField();
        
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        
        insidePanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;
        
        jLabel1.setText("jLabel1");
        jLabel1.setLabelFor(insidePanel);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints2.insets = new java.awt.Insets(12, 12, 0, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints2.weightx = 1.0;
        insidePanel.add(jLabel1, gridBagConstraints2);
        
        widthLabel.setText("jLabel2");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(12, 17, 0, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        insidePanel.add(widthLabel, gridBagConstraints2);
        
        widthField.setColumns(5);
        widthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateInsets(evt);
            }
        });
        
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets(12, 5, 0, 12);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        insidePanel.add(widthField, gridBagConstraints2);
        
        heightLabel.setText("jLabel3");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(5, 17, 0, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints2.weighty = 1.0;
        insidePanel.add(heightLabel, gridBagConstraints2);
        
        heightField.setColumns(5);
        heightField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateInsets(evt);
            }
        });
        
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(5, 5, 0, 12);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        insidePanel.add(heightField, gridBagConstraints2);
        
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        add(insidePanel, gridBagConstraints1);
        
    }//GEN-END:initComponents

    private void updateInsets(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateInsets
        // Add your handling code here:
        try {
            int width = Integer.parseInt (widthField.getText ());
            int height = Integer.parseInt (heightField.getText ());
            editor.setValue (new Dimension (width, height));
        } catch (NumberFormatException e) {
            // [PENDING beep]
        }
    }//GEN-LAST:event_updateInsets
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel insidePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JTextField widthField;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JTextField heightField;
    // End of variables declaration//GEN-END:variables

    private DimensionEditor editor;
}


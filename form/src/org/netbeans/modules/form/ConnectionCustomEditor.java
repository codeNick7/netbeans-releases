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

package org.netbeans.modules.form;

import java.awt.Dialog;
import java.awt.Insets;
import java.beans.MethodDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import org.netbeans.modules.form.ParametersPicker.ComponentComparator;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 * Custom editor for RADConnectionPropertyEditor. Allows specifying a property
 * as another bean, property or method call. (Extracted from ParametersPicker.)
 * 
 * @author Tomas Pavek
 */
class ConnectionCustomEditor extends javax.swing.JPanel {

    private RADConnectionPropertyEditor propertyEditor;
    private FormModel formModel;
    private Class valueType;

    private List<RADComponent> beanList;
    private RADComponent selectedComponent;
    private PropertyDescriptor selectedProperty;
    private MethodDescriptor selectedMethod;
    
    private boolean initialized = false;

    public ConnectionCustomEditor(RADConnectionPropertyEditor propertyEditor,
                                  FormModel formModel, Class valueType)
    {
        this.propertyEditor = propertyEditor;
        this.formModel = formModel;
        this.valueType = valueType;

        initComponents();

        setupBrowseButton(propertyDetailsButton);
        setupBrowseButton(methodDetailsButton);

        beanList = new ArrayList<RADComponent>();
        for (RADComponent radComp : formModel.getAllComponents()) {
            if (valueType.isAssignableFrom(radComp.getBeanClass()))
                beanList.add(radComp);
        }
        if (beanList.size() > 0) {
            Collections.sort(beanList, new ComponentComparator());

            beanCombo.addItem(FormUtils.getBundleString("CTL_CW_SelectBean")); // NOI18N
            for (RADComponent metacomp : beanList) {
                if (metacomp == formModel.getTopRADComponent())
                    beanCombo.addItem(
                        FormUtils.getBundleString("CTL_FormTopContainerName")); // NOI18N
                else
                    beanCombo.addItem(metacomp.getName());
            }
        }
        else beanRadio.setEnabled(false); // no beans on the form are of the required type
        initialized = true;
    }

    private static void setupBrowseButton(JButton button) {
        Insets margin = button.getMargin();
        if (margin.left > 4) {
            margin.left = 4;
            margin.right = 4;
            button.setMargin(margin);
        }
    }

    void setValue(RADConnectionPropertyEditor.RADConnectionDesignValue value) { //, Object realValue
//        if (value == null) {
//            previousValue = realValue != null;
//            return; // can happen if starting without previously set value
//        } else {
//            previousValue = true;               
//        }

        if (value == null) {
            updateControls();
            return;
        }

        switch (value.type) {
            case RADConnectionPropertyEditor.RADConnectionDesignValue.TYPE_BEAN:
                beanRadio.setSelected(true);
                selectedComponent = value.getRADComponent();
                int index = beanList.indexOf(selectedComponent);
                if (index > -1)
                    beanCombo.setSelectedIndex(index+1);
                break;
            case RADConnectionPropertyEditor.RADConnectionDesignValue.TYPE_PROPERTY:
                propertyRadio.setSelected(true);
                selectedComponent = value.getRADComponent();
                selectedProperty = value.getProperty();
                if (selectedComponent.getCodeExpression() == null) {
                    propertyField.setText(
                        FormUtils.getBundleString("CTL_CONNECTION_INVALID")); // NOI18N
                }
                else if (selectedComponent == formModel.getTopRADComponent())
                    propertyField.setText(selectedProperty.getName());
                else
                    propertyField.setText(selectedComponent.getName() + "." + selectedProperty.getName()); // NOI18N
                break;
            case RADConnectionPropertyEditor.RADConnectionDesignValue.TYPE_METHOD:
                methodRadio.setSelected(true);
                selectedComponent = value.getRADComponent();
                selectedMethod = value.getMethod();
                if (selectedComponent.getCodeExpression() == null) {
                    methodField.setText(
                        FormUtils.getBundleString("CTL_CONNECTION_INVALID")); // NOI18N
                }
                else if (selectedComponent == formModel.getTopRADComponent())
                    methodField.setText(selectedMethod.getName());
                else
                    methodField.setText(selectedComponent.getName() + "." + selectedMethod.getName()); // NOI18N
                break;
        }

        // update enabled state
        updateControls();
    }

    private void updateControls() {
        beanCombo.setEnabled(beanRadio.isSelected());

        propertyField.setEnabled(propertyRadio.isSelected());
        propertyDetailsButton.setEnabled(propertyRadio.isSelected());
        if (!propertyRadio.isSelected())
            propertyField.setText(FormUtils.getBundleString("CTL_CW_NoProperty")); // NOI18N

        methodField.setEnabled(methodRadio.isSelected());
        methodDetailsButton.setEnabled(methodRadio.isSelected());
        if (!methodRadio.isSelected())
            methodField.setText(FormUtils.getBundleString("CTL_CW_NoMethod")); // NOI18N
    }   

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        beanRadio = new javax.swing.JRadioButton();
        propertyRadio = new javax.swing.JRadioButton();
        methodRadio = new javax.swing.JRadioButton();
        beanCombo = new javax.swing.JComboBox();
        propertyField = new javax.swing.JTextField();
        methodField = new javax.swing.JTextField();
        propertyDetailsButton = new javax.swing.JButton();
        methodDetailsButton = new javax.swing.JButton();

        FormListener formListener = new FormListener();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.jLabel1.text")); // NOI18N

        buttonGroup1.add(beanRadio);
        beanRadio.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.beanRadio.text")); // NOI18N
        beanRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        beanRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        beanRadio.addActionListener(formListener);

        buttonGroup1.add(propertyRadio);
        propertyRadio.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.propertyRadio.text")); // NOI18N
        propertyRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        propertyRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        propertyRadio.addActionListener(formListener);

        buttonGroup1.add(methodRadio);
        methodRadio.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.methodRadio.text")); // NOI18N
        methodRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        methodRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        methodRadio.addActionListener(formListener);

        beanCombo.addActionListener(formListener);

        propertyField.setEditable(false);
        propertyField.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "CTL_CW_NoProperty")); // NOI18N

        methodField.setEditable(false);
        methodField.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "CTL_CW_NoMethod")); // NOI18N

        propertyDetailsButton.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.propertyDetailsButton.text")); // NOI18N
        propertyDetailsButton.addActionListener(formListener);

        methodDetailsButton.setText(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.methodDetailsButton.text")); // NOI18N
        methodDetailsButton.addActionListener(formListener);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(beanRadio)
                            .add(propertyRadio)
                            .add(methodRadio))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(beanCombo, 0, 270, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(propertyField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(propertyDetailsButton))
                            .add(layout.createSequentialGroup()
                                .add(methodField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(methodDetailsButton))))
                    .add(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(beanRadio)
                    .add(beanCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(propertyRadio)
                    .add(propertyDetailsButton)
                    .add(propertyField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(methodRadio)
                    .add(methodField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(methodDetailsButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        beanRadio.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.beanRadio.accessibleDescription")); // NOI18N
        propertyRadio.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.propertyRadio.accessibleDescription")); // NOI18N
        methodRadio.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.methodRadio.accessibleDescription")); // NOI18N
        beanCombo.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.beanCombo.accessibleDescription")); // NOI18N
        propertyField.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.propertyField.accessibleDescription")); // NOI18N
        methodField.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.methodField.accessibleDescription")); // NOI18N
        propertyDetailsButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.propertyDetailsButton.accessibleDescription")); // NOI18N
        methodDetailsButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ConnectionCustomEditor.class, "ConnectionCustomEditor.methodDetailsButton.accessibleDescription")); // NOI18N
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.ActionListener {
        FormListener() {}
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == beanRadio) {
                ConnectionCustomEditor.this.beanRadioActionPerformed(evt);
            }
            else if (evt.getSource() == propertyRadio) {
                ConnectionCustomEditor.this.propertyRadioActionPerformed(evt);
            }
            else if (evt.getSource() == methodRadio) {
                ConnectionCustomEditor.this.methodRadioActionPerformed(evt);
            }
            else if (evt.getSource() == propertyDetailsButton) {
                ConnectionCustomEditor.this.propertyDetailsButtonActionPerformed(evt);
            }
            else if (evt.getSource() == methodDetailsButton) {
                ConnectionCustomEditor.this.methodDetailsButtonActionPerformed(evt);
            }
            else if (evt.getSource() == beanCombo) {
                ConnectionCustomEditor.this.beanComboActionPerformed(evt);
            }
        }
    }// </editor-fold>//GEN-END:initComponents

    private void beanComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beanComboActionPerformed
        if (!initialized) {
            return;
        }
        int index = beanCombo.getSelectedIndex();
        if (index > 0) {
            selectedComponent = beanList.get(index-1);
            propertyEditor.setValue(new RADConnectionPropertyEditor.RADConnectionDesignValue(selectedComponent));
        }
        else {
            selectedComponent = null;
            propertyEditor.setValue(BeanSupport.NO_VALUE);
        }
    }//GEN-LAST:event_beanComboActionPerformed

    private void methodDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_methodDetailsButtonActionPerformed
        MethodPicker picker = new MethodPicker(formModel, null, valueType);
        picker.setSelectedComponent(selectedComponent);
        picker.setSelectedMethod(selectedMethod);

        String title = FormUtils.getFormattedBundleString(
            "CTL_FMT_CW_SelectMethod", // NOI18N
            new Object[] { valueType.getSimpleName() });

        final DialogDescriptor dd = new DialogDescriptor(picker, title);
        dd.setValid(picker.isPickerValid());
        picker.addPropertyChangeListener("pickerValid", new PropertyChangeListener() { // NOI18N
            public void propertyChange(PropertyChangeEvent evt2) {
                dd.setValid(((Boolean)evt2.getNewValue()).booleanValue());
            }
        });
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setVisible(true);

        if (dd.getValue() == DialogDescriptor.OK_OPTION) {
            selectedComponent = picker.getSelectedComponent();

	    MethodPicker.MethodPickerItem selectedItem = picker.getSelectedMethod();	    
	    selectedMethod = selectedItem.getMethodDescriptor();
	                
            methodField.setEnabled(true);
            if (selectedComponent == formModel.getTopRADComponent()) {
                methodField.setText(selectedItem.getMethodName());
            } else {
                methodField.setText(selectedComponent.getName() + "." + selectedMethod.getName()); // NOI18N
            }

            if (selectedComponent != null && selectedMethod != null) {
                propertyEditor.setValue(new RADConnectionPropertyEditor.RADConnectionDesignValue(selectedComponent, selectedMethod));
            } else if (selectedItem.getMethodName() != null) {
                propertyEditor.setValue(new RADConnectionPropertyEditor.RADConnectionDesignValue(selectedItem.getMethodName()));
            } else {
                propertyEditor.setValue(BeanSupport.NO_VALUE);
            }
        }
    }//GEN-LAST:event_methodDetailsButtonActionPerformed

    private void propertyDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertyDetailsButtonActionPerformed
        PropertyPicker propertyPicker = new PropertyPicker(formModel, null, valueType);
        propertyPicker.setSelectedComponent(selectedComponent);
        propertyPicker.setSelectedProperty(selectedProperty);

        String title = FormUtils.getFormattedBundleString(
            "CTL_FMT_CW_SelectProperty", // NOI18N
            new Object[] { valueType.getSimpleName() });

        final DialogDescriptor dd = new DialogDescriptor(propertyPicker, title);
        dd.setValid(propertyPicker.isPickerValid());
        propertyPicker.addPropertyChangeListener("pickerValid", new PropertyChangeListener() { // NOI18N
            public void propertyChange(PropertyChangeEvent evt2) {
                dd.setValid(((Boolean)evt2.getNewValue()).booleanValue());
            }
        });
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setVisible(true);

        if (dd.getValue() == DialogDescriptor.OK_OPTION) {
            selectedComponent = propertyPicker.getSelectedComponent();

	    PropertyPicker.PropertyPickerItem selectedItem = propertyPicker.getSelectedProperty();
	    	    
            propertyField.setEnabled(true);
            if (selectedComponent == formModel.getTopRADComponent())
                propertyField.setText(selectedItem.getPropertyName());
            else
                propertyField.setText(selectedComponent.getName() + "." + selectedItem.getPropertyName()); // NOI18N

            selectedProperty = selectedItem.getPropertyDescriptor();
            if (selectedProperty == null)
                propertyEditor.setValue(new RADConnectionPropertyEditor.RADConnectionDesignValue(selectedItem.getReadMethodName()));                
            else if (selectedComponent != null && selectedProperty != null)
                propertyEditor.setValue(new RADConnectionPropertyEditor.RADConnectionDesignValue(selectedComponent, selectedProperty));
            else                
                propertyEditor.setValue(BeanSupport.NO_VALUE);
        }
    }//GEN-LAST:event_propertyDetailsButtonActionPerformed

    private void methodRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_methodRadioActionPerformed
        if (methodRadio.isSelected())
            methodDetailsButton.requestFocus();
        updateControls();
    }//GEN-LAST:event_methodRadioActionPerformed

    private void propertyRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertyRadioActionPerformed
        if (propertyRadio.isSelected())
            propertyDetailsButton.requestFocus();
        updateControls();
    }//GEN-LAST:event_propertyRadioActionPerformed

    private void beanRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beanRadioActionPerformed
        if (beanRadio.isSelected())
            beanCombo.requestFocus();
        updateControls();
    }//GEN-LAST:event_beanRadioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox beanCombo;
    private javax.swing.JRadioButton beanRadio;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton methodDetailsButton;
    private javax.swing.JTextField methodField;
    private javax.swing.JRadioButton methodRadio;
    private javax.swing.JButton propertyDetailsButton;
    private javax.swing.JTextField propertyField;
    private javax.swing.JRadioButton propertyRadio;
    // End of variables declaration//GEN-END:variables
    
}

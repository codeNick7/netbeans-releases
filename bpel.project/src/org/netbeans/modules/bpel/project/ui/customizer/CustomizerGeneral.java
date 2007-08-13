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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */


package org.netbeans.modules.bpel.project.ui.customizer;

import java.awt.event.ItemEvent;
import java.io.File;

import javax.swing.JPanel;

import org.netbeans.modules.bpel.project.ProjectConstants;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.util.HelpCtx;
import org.netbeans.modules.compapp.projects.base.ui.customizer.IcanproProjectProperties;

/** Customizer for general project attributes.
 *
 * @author  phrebejk
 */
public class CustomizerGeneral extends JPanel implements IcanproCustomizer.Panel, HelpCtx.Provider {

    private IcanproProjectProperties webProperties;
    private VisualPropertySupport vps;
    private boolean bValidation = true;
    
    /** Creates new form CustomizerCompile */
    public CustomizerGeneral(IcanproProjectProperties webProperties) {
        initComponents();
        this.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerGeneral.class, "ACS_CustomizeGeneral_A11YDesc")); //NOI18N

        this.webProperties = webProperties;
        vps = new VisualPropertySupport(webProperties);
    
        Object validationObject =webProperties.get(ProjectConstants.VALIDATION_FLAG);
        
        // BpelProjectHelper.getInstance().getProjectProperty(IcanproProjectProperties.VALIDATION_FLAG);
        if (validationObject != null ){
            boolean validation = ((Boolean)validationObject).booleanValue();
            if (validation) {
                jCheckBox1.setSelected(true);
            } else {
                jCheckBox1.setSelected(false);
            }
            
        }else {
            jCheckBox1.setSelected(false);
        }
        
    }

    public void initValues(  ) {
        FileObject projectFolder = webProperties.getProject().getProjectDirectory();
        File pf = FileUtil.toFile(projectFolder);
        jTextFieldProjectFolder.setText(pf == null ? "" : pf.getPath()); // NOI18N

        vps.register(jTextFieldProjectType, IcanproProjectProperties.JBI_SE_TYPE);
    }

    
      /** Help context where to find more about the paste type action.
     * @return the help context for this action
     */
    public HelpCtx getHelpCtx() {
        return new HelpCtx(CustomizerGeneral.class);
    } 

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabelProjectName = new javax.swing.JLabel();
        jTextFieldProjectFolder = new javax.swing.JTextField();
        jLabelProjectType = new javax.swing.JLabel();
        jTextFieldProjectType = new javax.swing.JTextField();
        jLabelAssemblyUnit = new javax.swing.JLabel();
        jLabelAssemblyUnitAlias = new javax.swing.JLabel();
        jTextFieldAssemblyUnitAlias = new javax.swing.JTextField();
        jLabelAssemblyUnitDescription = new javax.swing.JLabel();
        jTextFieldAssemblyUnitDescription = new javax.swing.JTextField();
        jLabelApplicationSubAssembly = new javax.swing.JLabel();
        jLabelApplicationSubAssemblyAlias = new javax.swing.JLabel();
        jTextFieldApplicationSubAssemblyAlias = new javax.swing.JTextField();
        jTextFieldApplicationSubAssemblyDescription = new javax.swing.JTextField();
        jLabelApplicationSubAssemblyDescription = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelProjectName.setLabelFor(jTextFieldProjectFolder);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelProjectName, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ProjectFolder_JLabel")); // NOI18N
        jLabelProjectName.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelProjectName.toolTipText")); // NOI18N

        jTextFieldProjectFolder.setEditable(false);
        jTextFieldProjectFolder.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "ACS_CustomizeGeneral_ProjectFolder_A11YDesc")); // NOI18N

        jLabelProjectType.setLabelFor(jTextFieldProjectType);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelProjectType, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ProjectType_JLabel")); // NOI18N
        jLabelProjectType.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelProjectType.toolTipText")); // NOI18N

        jLabelAssemblyUnit.setLabelFor(jLabelAssemblyUnit);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelAssemblyUnit, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_AssemblyUnit_JLabel")); // NOI18N
        jLabelAssemblyUnit.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelAssemblyUnit.toolTipText")); // NOI18N

        jLabelAssemblyUnitAlias.setLabelFor(jTextFieldAssemblyUnitAlias);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelAssemblyUnitAlias, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_AssemblyUnitAlias_JLabel")); // NOI18N
        jLabelAssemblyUnitAlias.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelAssemblyUnitAlias.toolTipText")); // NOI18N

        jLabelAssemblyUnitDescription.setLabelFor(jTextFieldAssemblyUnitDescription);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelAssemblyUnitDescription, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_AssemblyUnitDescription_JLabel")); // NOI18N
        jLabelAssemblyUnitDescription.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelAssemblyUnitDescription.toolTipText")); // NOI18N

        jLabelApplicationSubAssembly.setLabelFor(jLabelAssemblyUnit);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelApplicationSubAssembly, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ApplicationSubAssembly_JLabel")); // NOI18N
        jLabelApplicationSubAssembly.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelApplicationSubAssembly.toolTipText")); // NOI18N

        jLabelApplicationSubAssemblyAlias.setLabelFor(jTextFieldApplicationSubAssemblyAlias);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelApplicationSubAssemblyAlias, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ApplicationSubAssemblyAlias_JLabel")); // NOI18N
        jLabelApplicationSubAssemblyAlias.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelApplicationSubAssemblyAlias.toolTipText")); // NOI18N

        jLabelApplicationSubAssemblyDescription.setLabelFor(jTextFieldApplicationSubAssemblyDescription);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelApplicationSubAssemblyDescription, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ApplicationSubAssemblyDescription_JLabel")); // NOI18N
        jLabelApplicationSubAssemblyDescription.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jLabelApplicationSubAssemblyDescription.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox1, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jCheckBox1.text")); // NOI18N
        jCheckBox1.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jCheckBox1.toolTipText")); // NOI18N
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                validationHandler(evt);
            }
        });

        jCheckBox1.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "CustomizerGeneral.jCheckBox1.AccessibleContext.accessibleName")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabelApplicationSubAssembly))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabelAssemblyUnit))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabelProjectName))
                            .add(layout.createSequentialGroup()
                                .add(36, 36, 36)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabelAssemblyUnitAlias)
                                    .add(jLabelAssemblyUnitDescription)))
                            .add(layout.createSequentialGroup()
                                .add(36, 36, 36)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabelApplicationSubAssemblyAlias)
                                    .add(layout.createSequentialGroup()
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabelApplicationSubAssemblyDescription))))
                            .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabelProjectType)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextFieldProjectFolder, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .add(jTextFieldProjectType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextFieldAssemblyUnitDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .add(jTextFieldAssemblyUnitAlias, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextFieldApplicationSubAssemblyDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .add(jTextFieldApplicationSubAssemblyAlias, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jCheckBox1)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {jLabelApplicationSubAssemblyAlias, jLabelApplicationSubAssemblyDescription, jLabelAssemblyUnitAlias, jLabelAssemblyUnitDescription}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {jLabelProjectName, jLabelProjectType}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelProjectName)
                    .add(jTextFieldProjectFolder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelProjectType)
                    .add(jTextFieldProjectType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(jLabelAssemblyUnit)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelAssemblyUnitAlias))
                    .add(jTextFieldAssemblyUnitAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelAssemblyUnitDescription)
                    .add(jTextFieldAssemblyUnitDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(11, 11, 11)
                .add(jLabelApplicationSubAssembly)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelApplicationSubAssemblyAlias)
                    .add(jTextFieldApplicationSubAssemblyAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelApplicationSubAssemblyDescription)
                    .add(jTextFieldApplicationSubAssemblyDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox1)
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void validationHandler(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_validationHandler
// TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            //bValidation = false;
            webProperties.put(ProjectConstants.VALIDATION_FLAG, true);
         //   BpelProjectHelper.getInstance().setProjectProperty(IcanproProjectProperties.VALIDATION_FLAG, "false", false);
        } else {
            webProperties.put(ProjectConstants.VALIDATION_FLAG, false);
         //   bValidation = true;
         //   BpelProjectHelper.getInstance().setProjectProperty(IcanproProjectProperties.VALIDATION_FLAG, "true",false);
        }
    }//GEN-LAST:event_validationHandler

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabelApplicationSubAssembly;
    private javax.swing.JLabel jLabelApplicationSubAssemblyAlias;
    private javax.swing.JLabel jLabelApplicationSubAssemblyDescription;
    private javax.swing.JLabel jLabelAssemblyUnit;
    private javax.swing.JLabel jLabelAssemblyUnitAlias;
    private javax.swing.JLabel jLabelAssemblyUnitDescription;
    private javax.swing.JLabel jLabelProjectName;
    private javax.swing.JLabel jLabelProjectType;
    private javax.swing.JTextField jTextFieldApplicationSubAssemblyAlias;
    private javax.swing.JTextField jTextFieldApplicationSubAssemblyDescription;
    private javax.swing.JTextField jTextFieldAssemblyUnitAlias;
    private javax.swing.JTextField jTextFieldAssemblyUnitDescription;
    private javax.swing.JTextField jTextFieldProjectFolder;
    private javax.swing.JTextField jTextFieldProjectType;
    // End of variables declaration//GEN-END:variables

}

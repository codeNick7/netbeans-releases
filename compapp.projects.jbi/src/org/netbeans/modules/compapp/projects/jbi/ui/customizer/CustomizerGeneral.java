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

package org.netbeans.modules.compapp.projects.jbi.ui.customizer;

import java.io.File;

import javax.swing.JPanel;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/** Customizer for general project attributes.
 *
 * @author  phrebejk
 */
public class CustomizerGeneral extends JPanel implements JbiJarCustomizer.Panel, HelpCtx.Provider {

    private JbiProjectProperties webProperties;
    private VisualPropertySupport vps;

    /** Creates new form CustomizerCompile */
    public CustomizerGeneral(JbiProjectProperties webProperties) {
        initComponents();
        this.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerGeneral.class, "ACS_CustomizeGeneral_A11YDesc")); // NOI18N

        this.webProperties = webProperties;
        vps = new VisualPropertySupport(webProperties);
    }

    public void initValues(  ) {
        FileObject projectFolder = webProperties.getProject().getProjectDirectory();
        File pf = FileUtil.toFile(projectFolder);
        jTextFieldProjectFolder.setText(pf == null ? "" : pf.getPath()); // NOI18N

        vps.register(jTextFieldAssemblyUnitAlias, JbiProjectProperties.ASSEMBLY_UNIT_ALIAS);
        vps.register(jTextFieldAssemblyUnitDescription, JbiProjectProperties.ASSEMBLY_UNIT_DESCRIPTION);
        vps.register(jTextFieldApplicationSubAssemblyAlias, JbiProjectProperties.APPLICATION_SUB_ASSEMBLY_ALIAS);
        vps.register(jTextFieldApplicationSubAssemblyDescription, JbiProjectProperties.APPLICATION_SUB_ASSEMBLY_DESCRIPTION);

//B        initPlatforms(vps);
    }

//B    private void initPlatforms(VisualPropertySupport vps) {
//B        // Read defined platforms
//B        JavaPlatform[] platforms = JavaPlatformManager.getDefault().getInstalledPlatforms();
//B        List platformNames = new ArrayList ();
//B        for( int i = 0; i < platforms.length; i++ ) {
//B            Specification spec = platforms[i].getSpecification();
//B            if ("j2se".equalsIgnoreCase (spec.getName())) {
//B                platformNames.add(platforms[i].getDisplayName());
//B            }
//B        }
//B        vps.register(jComboBoxTarget, (String[]) platformNames.toArray(new String[platformNames.size()]), JbiProjectProperties.JAVA_PLATFORM);
//B    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabelProjectName = new javax.swing.JLabel();
        jTextFieldProjectFolder = new javax.swing.JTextField();
        jLabelAssemblyUnit = new javax.swing.JLabel();
        jLabelAssemblyUnitAlias = new javax.swing.JLabel();
        jTextFieldAssemblyUnitAlias = new javax.swing.JTextField();
        jLabelAssemblyUnitDescription = new javax.swing.JLabel();
        jTextFieldAssemblyUnitDescription = new javax.swing.JTextField();
        jLabelApplicationSubAssembly = new javax.swing.JLabel();
        jLabelApplicationSubAssemblyAlias = new javax.swing.JLabel();
        jTextFieldApplicationSubAssemblyAlias = new javax.swing.JTextField();
        jLabelApplicationSubAssemblyDescription = new javax.swing.JLabel();
        jTextFieldApplicationSubAssemblyDescription = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelProjectName.setLabelFor(jTextFieldProjectFolder);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelProjectName, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ProjectFolder_JLabel"));

        jTextFieldProjectFolder.setEditable(false);
        jTextFieldProjectFolder.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "ACS_CustomizeGeneral_ProjectFolder_A11YDesc"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabelAssemblyUnit, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_AssemblyUnit_JLabel"));

        jLabelAssemblyUnitAlias.setLabelFor(jTextFieldAssemblyUnitAlias);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelAssemblyUnitAlias, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_AssemblyUnitAlias_JLabel"));

        jTextFieldAssemblyUnitAlias.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/compapp/projects/jbi/ui/customizer/Bundle").getString("ACS_CustomizeGeneral_AssemblyUnitAlias_A11YDesc"));

        jLabelAssemblyUnitDescription.setLabelFor(jTextFieldAssemblyUnitDescription);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelAssemblyUnitDescription, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_AssemblyUnitDescription_JLabel"));

        jTextFieldAssemblyUnitDescription.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/compapp/projects/jbi/ui/customizer/Bundle").getString("ACS_CustomizeGeneral_AssemblyUnitDescription_A11YDesc"));

        org.openide.awt.Mnemonics.setLocalizedText(jLabelApplicationSubAssembly, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ApplicationSubAssembly_JLabel"));
        jLabelApplicationSubAssembly.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getBundle(CustomizerGeneral.class).getString("LBL_CustomizeGeneral_ApplicationSubAssembly_JLabel"));

        jLabelApplicationSubAssemblyAlias.setLabelFor(jTextFieldApplicationSubAssemblyAlias);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelApplicationSubAssemblyAlias, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ApplicationSubAssemblyAlias_JLabel"));

        jTextFieldApplicationSubAssemblyAlias.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/compapp/projects/jbi/ui/customizer/Bundle").getString("LBL_CustomizeGeneral_ApplicationSubAssemblyAlias_A11YDesc"));

        jLabelApplicationSubAssemblyDescription.setLabelFor(jTextFieldApplicationSubAssemblyDescription);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelApplicationSubAssemblyDescription, org.openide.util.NbBundle.getMessage(CustomizerGeneral.class, "LBL_CustomizeGeneral_ApplicationSubAssemblyDescription_JLabel"));

        jTextFieldApplicationSubAssemblyDescription.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/compapp/projects/jbi/ui/customizer/Bundle").getString("LBL_CustomizeGeneral_ApplicationSubAssemblyDescription_A11YDesc"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelProjectName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(26, 26, 26)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(jLabelAssemblyUnitAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabelApplicationSubAssemblyAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabelApplicationSubAssemblyDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jLabelAssemblyUnitDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextFieldProjectFolder, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                            .add(jTextFieldAssemblyUnitAlias, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                            .add(jTextFieldAssemblyUnitDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                            .add(jTextFieldApplicationSubAssemblyAlias, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                            .add(jTextFieldApplicationSubAssemblyDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jLabelApplicationSubAssembly))
                    .add(layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jLabelAssemblyUnit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelProjectName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldProjectFolder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(11, 11, 11)
                .add(jLabelAssemblyUnit)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelAssemblyUnitAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldAssemblyUnitAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelAssemblyUnitDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldAssemblyUnitDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(11, 11, 11)
                .add(jLabelApplicationSubAssembly)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelApplicationSubAssemblyAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldApplicationSubAssemblyAlias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(9, 9, 9)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelApplicationSubAssemblyDescription)
                    .add(jTextFieldApplicationSubAssemblyDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(113, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelApplicationSubAssembly;
    private javax.swing.JLabel jLabelApplicationSubAssemblyAlias;
    private javax.swing.JLabel jLabelApplicationSubAssemblyDescription;
    private javax.swing.JLabel jLabelAssemblyUnit;
    private javax.swing.JLabel jLabelAssemblyUnitAlias;
    private javax.swing.JLabel jLabelAssemblyUnitDescription;
    private javax.swing.JLabel jLabelProjectName;
    private javax.swing.JTextField jTextFieldApplicationSubAssemblyAlias;
    private javax.swing.JTextField jTextFieldApplicationSubAssemblyDescription;
    private javax.swing.JTextField jTextFieldAssemblyUnitAlias;
    private javax.swing.JTextField jTextFieldAssemblyUnitDescription;
    private javax.swing.JTextField jTextFieldProjectFolder;
    // End of variables declaration//GEN-END:variables

  /** Help context where to find more about the paste type action.
     * @return the help context for this action
     */
    public HelpCtx getHelpCtx() {
        return new HelpCtx(CustomizerGeneral.class);
    } 
}

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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
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

package org.netbeans.modules.j2ee.sun.ide.j2ee.ui;

import javax.enterprise.deploy.spi.DeploymentManager;
import org.netbeans.modules.j2ee.sun.api.SunDeploymentManagerInterface;
import org.netbeans.modules.j2ee.sun.ide.j2ee.DeploymentManagerProperties;
import org.openide.util.NbBundle;

/**
 *
 * @author  vkraemer
 */
public class OptionTabVisualPanel extends javax.swing.JPanel {
    
    final private DeploymentManagerProperties dmp;
    //private int startupTimeout; //= 120;
    //private int deploymentTimeout;// = 120;
    
    /** Creates new form OptionTabVisualPanel */
    OptionTabVisualPanel(DeploymentManager deployManager) {
        //this.dm = dm;
        initComponents();
        dmp = new DeploymentManagerProperties(deployManager);
        startupTimeoutValue.setText(dmp.getStartupTimeout()+"");
        startupTimeoutValue.setEnabled(((SunDeploymentManagerInterface) deployManager).isLocal());
        deploymentTimeoutValue.setText(dmp.getDeploymentTimeout()+"");
        enableDirectoryDeployment.setSelected(dmp.isDirectoryDeploymentPossible());
        enableDirectoryDeployment.setEnabled(((SunDeploymentManagerInterface) deployManager).isLocal());
        enableDriverDeployment.setSelected(dmp.isDriverDeploymentEnabled());
        enableDriverDeployment.setEnabled(((SunDeploymentManagerInterface) deployManager).isLocal());
        enableDatabaseStart.setSelected(dmp.isDatabaseStartEnabled());
        enableDatabaseStart.setEnabled(((SunDeploymentManagerInterface) deployManager).isLocal());
    }
    
    private void updateDynamicHelp(String s) {
        dynamicHelpLabel.setText(s);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startupTimeoutLabel = new javax.swing.JLabel();
        startupTimeoutValue = new javax.swing.JTextField();
        deploymentTimeoutLabel = new javax.swing.JLabel();
        deploymentTimeoutValue = new javax.swing.JTextField();
        enableDirectoryDeployment = new javax.swing.JCheckBox();
        enableDriverDeployment = new javax.swing.JCheckBox();
        enableDatabaseStart = new javax.swing.JCheckBox();
        dynamicHelpLabel = new javax.swing.JLabel();

        startupTimeoutLabel.setLabelFor(startupTimeoutValue);
        org.openide.awt.Mnemonics.setLocalizedText(startupTimeoutLabel, org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "LBL_STARTUP_TIMEOUT")); // NOI18N

        startupTimeoutValue.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        startupTimeoutValue.setText(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "OptionTabVisualPanel.startupTimeoutValue.text")); // NOI18N
        startupTimeoutValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                startupTimeoutHelp(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clearDynamicHelp(evt);
            }
        });
        startupTimeoutValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                startupTimeoutValueKeyReleased(evt);
            }
        });

        deploymentTimeoutLabel.setLabelFor(deploymentTimeoutValue);
        org.openide.awt.Mnemonics.setLocalizedText(deploymentTimeoutLabel, org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "LBL_DEPLOYMENT_TIMEOUT")); // NOI18N

        deploymentTimeoutValue.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        deploymentTimeoutValue.setText(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "OptionTabVisualPanel.deploymentTimeoutValue.text")); // NOI18N
        deploymentTimeoutValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                deploymentTimeoutHelp(evt);
            }
        });
        deploymentTimeoutValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                deploymentTimeoutValueKeyReleased(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(enableDirectoryDeployment, org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "CB_DIR_DEPLOY")); // NOI18N
        enableDirectoryDeployment.setMargin(new java.awt.Insets(0, 0, 0, 0));
        enableDirectoryDeployment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableDirectoryDeploymentActionPerformed(evt);
            }
        });
        enableDirectoryDeployment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                directoryDeploymentHelp(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(enableDriverDeployment, org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "CB_DRIVER_DEPLOY")); // NOI18N
        enableDriverDeployment.setMargin(new java.awt.Insets(0, 0, 0, 0));
        enableDriverDeployment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableDriverDeploymentActionPerformed(evt);
            }
        });
        enableDriverDeployment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jdbcDriverDeploymentHelp(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clearDynamicHelp(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(enableDatabaseStart, org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "CB_JAVADB_START")); // NOI18N
        enableDatabaseStart.setMargin(new java.awt.Insets(0, 0, 0, 0));
        enableDatabaseStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableDatabaseStartActionPerformed(evt);
            }
        });
        enableDatabaseStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                enableDatabaseStartHelp(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clearDynamicHelp(evt);
            }
        });

        dynamicHelpLabel.setText(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "OptionTabVisualPanel.dynamicHelpLabel.text")); // NOI18N
        dynamicHelpLabel.setFocusable(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(17, 17, 17)
                        .add(dynamicHelpLabel))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(deploymentTimeoutLabel)
                            .add(startupTimeoutLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(deploymentTimeoutValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .add(startupTimeoutValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)))
                    .add(enableDirectoryDeployment)
                    .add(enableDriverDeployment)
                    .add(enableDatabaseStart))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startupTimeoutLabel)
                    .add(startupTimeoutValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(deploymentTimeoutLabel)
                    .add(deploymentTimeoutValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(enableDirectoryDeployment)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(enableDriverDeployment)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(enableDatabaseStart)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dynamicHelpLabel)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        startupTimeoutValue.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "ACSD_STARTUP_TIMEOUT")); // NOI18N
        deploymentTimeoutValue.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "ACSD_DEPLOYMENT_TIMEOUT")); // NOI18N
        enableDirectoryDeployment.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "ACSD_DirectoryDeploy")); // NOI18N
        enableDriverDeployment.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "ACSD_DRIVER_DEPLOY")); // NOI18N
        enableDatabaseStart.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(OptionTabVisualPanel.class, "ACSD_DATABASE_START")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

private void clearDynamicHelp(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clearDynamicHelp
    updateDynamicHelp(""); // NOI18N
}//GEN-LAST:event_clearDynamicHelp

private void jdbcDriverDeploymentHelp(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jdbcDriverDeploymentHelp
    updateDynamicHelp(NbBundle.getMessage(OptionTabVisualPanel.class,"HLP_DRIVER_DEPLOYMENT")); // NOI18N
}//GEN-LAST:event_jdbcDriverDeploymentHelp

private void directoryDeploymentHelp(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_directoryDeploymentHelp
    updateDynamicHelp(NbBundle.getMessage(OptionTabVisualPanel.class,"HLP_DIRECTORY_DEPLOYMENT")); // NOI18N
}//GEN-LAST:event_directoryDeploymentHelp

private void deploymentTimeoutHelp(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_deploymentTimeoutHelp
    updateDynamicHelp(NbBundle.getMessage(OptionTabVisualPanel.class,"HLP_DEPLOYMENT_TIMEOUT")); // NOI18N
}//GEN-LAST:event_deploymentTimeoutHelp

private void startupTimeoutHelp(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_startupTimeoutHelp
    updateDynamicHelp(NbBundle.getMessage(OptionTabVisualPanel.class,"HLP_STARTUP_TIMEOUT")); // NOI18N
}//GEN-LAST:event_startupTimeoutHelp

private void enableDriverDeploymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableDriverDeploymentActionPerformed
      dmp.setDriverDeploymentEnabled(enableDriverDeployment.isSelected());
}//GEN-LAST:event_enableDriverDeploymentActionPerformed

    private void enableDirectoryDeploymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableDirectoryDeploymentActionPerformed
        dmp.setDirectoryDeploymentPossible(enableDirectoryDeployment.isSelected());
    }//GEN-LAST:event_enableDirectoryDeploymentActionPerformed

    private void deploymentTimeoutValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deploymentTimeoutValueKeyReleased
        int val = dmp.getDeploymentTimeout();
        String sval = deploymentTimeoutValue.getText();
        try {
            int tval = Integer.parseInt(sval);
            if (0 <= tval) {
                dmp.setDeploymentTimeout(tval);
                if (sval.charAt(0) == '0') {
                    deploymentTimeoutValue.setText(tval+"");   // NOI18N                  
                }
            } else {
                deploymentTimeoutValue.setText(val+"");     // NOI18N    
            }
        } catch (NumberFormatException nfe) {
            if ("".equals(sval)) {
                deploymentTimeoutValue.setText("0");   // NOI18N   
                dmp.setDeploymentTimeout(0);
            } else {
                deploymentTimeoutValue.setText(val+""); // NOI18N
            }
        }

    }//GEN-LAST:event_deploymentTimeoutValueKeyReleased

    private void startupTimeoutValueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_startupTimeoutValueKeyReleased
        int val = dmp.getStartupTimeout();
        String sval = startupTimeoutValue.getText();
        try {
            int tval = Integer.parseInt(sval);
            if (0 <= tval) {
                dmp.setStartupTimeout(tval);
                if (sval.charAt(0) == '0') {
                    startupTimeoutValue.setText(tval+"");   // NOI18N                    
                }
            } else {
                startupTimeoutValue.setText(val+"");        // NOI18N
            }
        } catch (NumberFormatException nfe) {
            if ("".equals(sval)) {
                startupTimeoutValue.setText("0");           // NOI18N 
                dmp.setStartupTimeout(0);
            } else {
                startupTimeoutValue.setText(val+"");        // NOI18N
            }
        }
    }//GEN-LAST:event_startupTimeoutValueKeyReleased

private void enableDatabaseStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableDatabaseStartActionPerformed
    dmp.setDatabaseStartEnabled(enableDatabaseStart.isSelected());
}//GEN-LAST:event_enableDatabaseStartActionPerformed

private void enableDatabaseStartHelp(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_enableDatabaseStartHelp
    updateDynamicHelp(NbBundle.getMessage(OptionTabVisualPanel.class, "HLP_JAVADB_START")); // NOI18N
}//GEN-LAST:event_enableDatabaseStartHelp

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel deploymentTimeoutLabel;
    private javax.swing.JTextField deploymentTimeoutValue;
    private javax.swing.JLabel dynamicHelpLabel;
    private javax.swing.JCheckBox enableDatabaseStart;
    private javax.swing.JCheckBox enableDirectoryDeployment;
    private javax.swing.JCheckBox enableDriverDeployment;
    private javax.swing.JLabel startupTimeoutLabel;
    private javax.swing.JTextField startupTimeoutValue;
    // End of variables declaration//GEN-END:variables
    
}

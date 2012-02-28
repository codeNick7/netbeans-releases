/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
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
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */
package org.netbeans.modules.javafx2.scenebuilder.options;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.netbeans.modules.javafx2.scenebuilder.Home;
import org.netbeans.modules.javafx2.scenebuilder.impl.SBHomeFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

final class SBOptionsPanel extends javax.swing.JPanel {    
    private final SBOptionsPanelController controller;
    
    private class HomeDef {
        private Home home;
        private String displayname;

        public HomeDef(Home home, String displayname) {
            this.home = home;
            this.displayname = displayname;
        }

        public HomeDef(Home home) {
            this.home = home;
            this.displayname = home.getPath();
        }

        @Override
        public String toString() {
            return displayname;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final HomeDef other = (HomeDef) obj;
            if ((this.home == null) ? (other.home != null) : !this.home.equals(other.home)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + (this.home != null ? this.home.hashCode() : 0);

            return hash;
        }
    }
    
    final private List<HomeDef> userDefinedSBHomes = new ArrayList<HomeDef>();
    final private List<HomeDef> predefinedSBHomes = new ArrayList<HomeDef>();
    
    @NbBundle.Messages({
        "MSG_InvalidHome=Please, select a valid Scene Builder home...",
        "LBL_Browse=Browse..."
    })
    SBOptionsPanel(SBOptionsPanelController controller) {
        this.controller = controller;
        
        initComponents();
        
        sbHome.setNullSelectionMessage(Bundle.MSG_InvalidHome());
        sbHome.setGrowAction(new AbstractAction(Bundle.LBL_Browse()) {

            @Override
            public void actionPerformed(ActionEvent e) {
                browseAddNewRuntime();
            }
        });
        sbHomeInfo.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sbHomeLabel = new javax.swing.JLabel();
        sbHomeInfo = new javax.swing.JLabel();
        saveAllModified = new javax.swing.JCheckBox();
        sbHome = new org.netbeans.modules.javafx2.scenebuilder.options.GrowingComboBox();

        sbHomeLabel.setLabelFor(sbHome);
        org.openide.awt.Mnemonics.setLocalizedText(sbHomeLabel, org.openide.util.NbBundle.getMessage(SBOptionsPanel.class, "SBOptionsPanel.sbHomeLabel.text")); // NOI18N

        sbHomeInfo.setFont(sbHomeInfo.getFont().deriveFont((sbHomeInfo.getFont().getStyle() | java.awt.Font.ITALIC), sbHomeInfo.getFont().getSize()-3));
        org.openide.awt.Mnemonics.setLocalizedText(sbHomeInfo, org.openide.util.NbBundle.getMessage(SBOptionsPanel.class, "SBOptionsPanel.sbHomeInfo.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(saveAllModified, org.openide.util.NbBundle.getMessage(SBOptionsPanel.class, "SBOptionsPanel.saveAllModified.text")); // NOI18N
        saveAllModified.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAllModifiedActionPerformed(evt);
            }
        });

        sbHome.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sbHomeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sbHomeLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sbHomeInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveAllModified, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sbHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sbHomeLabel)
                    .addComponent(sbHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sbHomeInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveAllModified)
                .addGap(0, 8, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveAllModifiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllModifiedActionPerformed
        controller.setSaveBeforeLaunch(saveAllModified.isSelected());
    }//GEN-LAST:event_saveAllModifiedActionPerformed

    private void sbHomeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sbHomeItemStateChanged

    }//GEN-LAST:event_sbHomeItemStateChanged

    @NbBundle.Messages("LBL_Default=Default")
    void load() {
        predefinedSBHomes.clear();
        userDefinedSBHomes.clear();
        
        Home selected = controller.getSbHome();
        Home defaultHome = controller.getDefaultSBHome();
        HomeDef hd = null, selectedHd = null;
        if (defaultHome != null) {
            hd = new HomeDef(defaultHome, Bundle.LBL_Default() + " (" + defaultHome.getPath() + ")");
            if (selected != null && selected.equals(hd.home)) {
                selectedHd = hd;
            }
            predefinedSBHomes.add(hd);
        }
        
        for(Home s : controller.getUserDefinedHomes()) {
            hd = new HomeDef(s);
            userDefinedSBHomes.add(hd);
            if (selected != null && selectedHd == null && selected.equals(hd.home)) {
                selectedHd = hd;
            }
        }
        saveAllModified.setSelected(controller.isSaveBeforeLaunch());
        GrowingComboBox.GrowingListModel<HomeDef> model = sbHome.getModel();
        model.setPredefined(predefinedSBHomes);
        model.setUserDefined(userDefinedSBHomes);
        if (selectedHd != null) {
            model.setSelectedItem(selectedHd);
        }
    }

    void store() {
        List<Home> userDefs = new ArrayList<Home>();
        GrowingComboBox.GrowingListModel<HomeDef> model = sbHome.getModel();
        for(HomeDef hd : model.getUserDefined()) {
            userDefs.add(hd.home);
        }
        controller.setUserDefinedHomes(userDefs);
        controller.setSbHome(((HomeDef)sbHome.getSelectedItem()).home);
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox saveAllModified;
    private org.netbeans.modules.javafx2.scenebuilder.options.GrowingComboBox sbHome;
    private javax.swing.JLabel sbHomeInfo;
    private javax.swing.JLabel sbHomeLabel;
    // End of variables declaration//GEN-END:variables

    @NbBundle.Messages({
        "LBL_BrowseSBHome=Select a Valid Scene Builder Home",
        "MSG_InvalidSBHome=<html>Selected location <p><b>{0}</b></p> does not represent a valid JavaFX Scene Builder installation."
    })
    private void browseAddNewRuntime() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(Bundle.LBL_BrowseSBHome());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setFileHidingEnabled(false);
        String path = new File(System.getProperty("user.home")).getAbsolutePath(); //NOI18N

        if (path.length() > 0) {
            File f = new File(path);
            if (f.exists()) {
                chooser.setSelectedFile(f);
            }
        }
        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
            File f = chooser.getSelectedFile();
            String newRuntimePath = f.getAbsolutePath();
            Home h = SBHomeFactory.getDefault().loadHome(newRuntimePath);
            if (h != null) {
                HomeDef newHd = new HomeDef(h);
                sbHome.getModel().addUserDefined(newHd);
                sbHome.getModel().setSelectedItem(newHd);
            } else {
                NotifyDescriptor nd = new NotifyDescriptor.Message(Bundle.MSG_InvalidSBHome(newRuntimePath), NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notifyLater(nd);
            }
        }
    }
}

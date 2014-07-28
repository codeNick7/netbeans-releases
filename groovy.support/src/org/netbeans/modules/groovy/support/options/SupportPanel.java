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

package org.netbeans.modules.groovy.support.options;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JLabel;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.modules.groovy.support.api.GroovySettings;
import org.netbeans.modules.groovy.support.spi.GroovyOptionsSubpanel;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.HtmlBrowser;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * Groovy settings
 *
 * @author Martin Adamek
 */
@OptionsPanelController.Keywords(
        location = OptionsDisplayer.ADVANCED, 
        tabTitle = "Groovy",
        keywords = {"groovy", "grails", "gsp"})
final class SupportPanel extends javax.swing.JPanel {

    private final Collection<GroovyOptionsSubpanel> subpanels;

    SupportPanel(@NonNull Collection<? extends GroovyOptionsSubpanel> subpanels) {

        this.subpanels = new ArrayList<GroovyOptionsSubpanel>(subpanels);
        initComponents();

        int y = 1;
        for (GroovyOptionsSubpanel subpanel: this.subpanels) {
            GridBagConstraints constr = new GridBagConstraints();
            constr.gridx = 1;
            constr.gridy = y;
            constr.weightx = 1.0;
            constr.fill = GridBagConstraints.HORIZONTAL;
            constr.insets = new Insets(0, 0, 8, 0);
            subpanelWrapper.add(subpanel.getComponent(), constr);
            y++;
        }

        GridBagConstraints constr = new GridBagConstraints();
        constr.gridx = 1;
        constr.gridy = y;
        constr.weightx = 1.0;
        constr.weighty = 1.0;
        constr.fill = GridBagConstraints.BOTH;
        subpanelWrapper.add(new JLabel(), constr);
        // TODO listen to changes in form fields and call controller.changed()
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        docLabel = new javax.swing.JLabel();
        groovyDocTextField = new javax.swing.JTextField();
        chooseDocButton = new javax.swing.JButton();
        subpanelWrapper = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.jLabel2.text")); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel2MousePressed(evt);
            }
        });

        docLabel.setLabelFor(groovyDocTextField);
        org.openide.awt.Mnemonics.setLocalizedText(docLabel, org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.docLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chooseDocButton, org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.chooseDocButton.text")); // NOI18N
        chooseDocButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseDocButtonActionPerformed(evt);
            }
        });

        subpanelWrapper.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subpanelWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addContainerGap(174, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(docLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(groovyDocTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chooseDocButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(docLabel)
                    .addComponent(groovyDocTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseDocButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subpanelWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
        );

        groovyDocTextField.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.groovyDocTextField.accessibleName")); // NOI18N
        groovyDocTextField.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.groovyDocTextField.accessibleDescription")); // NOI18N
        chooseDocButton.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.chooseDocButton.accessibleName")); // NOI18N
        chooseDocButton.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(SupportPanel.class, "SupportPanel.chooseDocButton.accessibleDescription")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

private void chooseDocButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseDocButtonActionPerformed
    File file = new FileChooserBuilder(SupportPanel.class)
            .setDirectoriesOnly(true)
            .setDefaultWorkingDirectory(null)
            .setTitle(NbBundle.getMessage(SupportPanel.class, "LBL_Select_Directory"))
            .setApproveText(NbBundle.getMessage(SupportPanel.class, "LBL_Select_Directory"))
            .showOpenDialog();
    if (file != null) {
        if (!new File(new File(file, "groovy-jdk"), "index.html").isFile()) {
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
                NbBundle.getMessage(SupportPanel.class, "LBL_Not_groovy_doc"),
                NotifyDescriptor.Message.WARNING_MESSAGE));
            return;
        }
        groovyDocTextField.setText(file.getAbsolutePath());
    }
}//GEN-LAST:event_chooseDocButtonActionPerformed

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        try {
            HtmlBrowser.URLDisplayer.getDefault().showURL(new URL("http://groovy.codehaus.org")); // NOI18N
        } catch (MalformedURLException murle) {
            Exceptions.printStackTrace(murle);
        }
    }//GEN-LAST:event_jLabel2MousePressed

    void load() {
        GroovySettings groovyOption = GroovySettings.getInstance();
        String text = groovyOption.getGroovyDoc();
        if (text == null) {
            text = "";
        }
        groovyDocTextField.setText(text);

        for (GroovyOptionsSubpanel subpanel: this.subpanels) {
            subpanel.load();
        }
    }

    void store() {
        GroovySettings groovyOption = GroovySettings.getInstance();
        groovyOption.setGroovyDoc(groovyDocTextField.getText().trim());

        for (GroovyOptionsSubpanel subpanel: this.subpanels) {
            subpanel.store();
        }
    }

    boolean valid() {
        String groovyDoc = groovyDocTextField.getText().trim();
        if ("".equals(groovyDoc)) {
            return false;
        }
        for (GroovyOptionsSubpanel subpanel: this.subpanels) {
            if (!subpanel.valid()) {
                return false;
            }
        }
        return true;
    }

    boolean changed() {
        String text = GroovySettings.getInstance().getGroovyDoc();
        if (text == null) {
            text = "";
        }
        if (!text.equals(groovyDocTextField.getText().trim())) {
            return true;
        }
        boolean isChanged = false;
        for (GroovyOptionsSubpanel subpanel: this.subpanels) {
            isChanged |= subpanel.changed();
        }
        return isChanged;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseDocButton;
    private javax.swing.JLabel docLabel;
    private javax.swing.JTextField groovyDocTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel subpanelWrapper;
    // End of variables declaration//GEN-END:variables

}

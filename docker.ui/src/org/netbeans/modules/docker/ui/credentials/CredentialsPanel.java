/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2015 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2015 Sun Microsystems, Inc.
 */
package org.netbeans.modules.docker.ui.credentials;

import java.awt.Dialog;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import org.netbeans.modules.docker.api.Credentials;
import org.netbeans.modules.docker.api.CredentialsManager;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.Mnemonics;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author Petr Hejl
 */
public class CredentialsPanel extends javax.swing.JPanel {

    private final DefaultListModel<CredentialsItem> model = new DefaultListModel<>();

    /**
     * Creates new form CredentialsPanel
     */
    public CredentialsPanel() {
        initComponents();

        CredentialsManager manager = CredentialsManager.getDefault();
        try {
            for (Credentials c : manager.getAllCredentials()) {
                model.addElement(new CredentialsItem(c));
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        credentialsList.setModel(model);
    }

    private Set<String> getRegistries() {
        Set<String> ret = new HashSet<>(model.size());
        for (Enumeration<CredentialsItem> e = model.elements(); e.hasMoreElements(); ) {
            CredentialsItem i = e.nextElement();
            ret.add(i.getCredentials().getRegistry());
        }
        return ret;
    }

    private static class CredentialsItem {

        private final Credentials credentials;

        public CredentialsItem(Credentials credentials) {
            this.credentials = credentials;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        @Override
        public String toString() {
            return credentials.getRegistry();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        credentialsList = new javax.swing.JList<>();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();

        credentialsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                credentialsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(credentialsList);

        org.openide.awt.Mnemonics.setLocalizedText(addButton, org.openide.util.NbBundle.getMessage(CredentialsPanel.class, "CredentialsPanel.addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeButton, org.openide.util.NbBundle.getMessage(CredentialsPanel.class, "CredentialsPanel.removeButton.text")); // NOI18N
        removeButton.setEnabled(false);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(editButton, org.openide.util.NbBundle.getMessage(CredentialsPanel.class, "CredentialsPanel.editButton.text")); // NOI18N
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    @NbBundle.Messages({
        "LBL_Add=&Add",
        "LBL_AddTitle=Add"
    })
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        JButton actionButton = new JButton();
        Mnemonics.setLocalizedText(actionButton, Bundle.LBL_Add());
        CredentialsDetailPanel panel = new CredentialsDetailPanel(actionButton, getRegistries());
        DialogDescriptor descriptor = new DialogDescriptor(panel, Bundle.LBL_AddTitle(), true,
                new Object[] {actionButton, DialogDescriptor.CANCEL_OPTION}, actionButton,
                DialogDescriptor.DEFAULT_ALIGN, null, null);
        descriptor.setClosingOptions(new Object[] {actionButton, DialogDescriptor.CANCEL_OPTION});
        panel.setMessageLine(descriptor.createNotificationLineSupport());

        Dialog dlg = null;
        try {
            dlg = DialogDisplayer.getDefault().createDialog(descriptor);
            dlg.setVisible(true);

            if (descriptor.getValue() == actionButton) {
                try {
                    Credentials credentials = panel.getCredentials();
                    CredentialsManager.getDefault().setCredentials(credentials);
                    model.addElement(new CredentialsItem(credentials));
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } finally {
            if (dlg != null) {
                dlg.dispose();
            }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    @NbBundle.Messages({
        "LBL_OK=&OK",
        "LBL_EditTitle=Edit"
    })
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        JButton actionButton = new JButton();
        Mnemonics.setLocalizedText(actionButton, Bundle.LBL_OK());
        CredentialsDetailPanel panel = new CredentialsDetailPanel(actionButton, getRegistries());
        int index = credentialsList.getSelectedIndex();
        panel.setCredentials(credentialsList.getSelectedValue().getCredentials());
        DialogDescriptor descriptor = new DialogDescriptor(panel, Bundle.LBL_EditTitle(), true,
                new Object[] {actionButton, DialogDescriptor.CANCEL_OPTION}, actionButton,
                DialogDescriptor.DEFAULT_ALIGN, null, null);
        descriptor.setClosingOptions(new Object[] {actionButton, DialogDescriptor.CANCEL_OPTION});
        panel.setMessageLine(descriptor.createNotificationLineSupport());

        Dialog dlg = null;
        try {
            dlg = DialogDisplayer.getDefault().createDialog(descriptor);
            dlg.setVisible(true);

            if (descriptor.getValue() == actionButton) {
                try {
                    Credentials credentials = panel.getCredentials();
                    CredentialsManager.getDefault().setCredentials(credentials);
                    model.setElementAt(new CredentialsItem(credentials), index);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        } finally {
            if (dlg != null) {
                dlg.dispose();
            }
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        for (CredentialsItem i : credentialsList.getSelectedValuesList()) {
            try {
                CredentialsManager.getDefault().removeCredentials(i.getCredentials());
                model.removeElement(i);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void credentialsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_credentialsListValueChanged
        boolean enable = !credentialsList.isSelectionEmpty();
        editButton.setEnabled(enable);
        removeButton.setEnabled(enable);
    }//GEN-LAST:event_credentialsListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JList<CredentialsItem> credentialsList;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables
}

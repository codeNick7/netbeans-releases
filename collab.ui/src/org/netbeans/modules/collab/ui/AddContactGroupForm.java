/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2005 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.modules.collab.ui;

import com.sun.collablet.CollabException;
import com.sun.collablet.CollabManager;
import com.sun.collablet.CollabSession;

import org.openide.*;
import org.openide.util.*;

import java.awt.Dialog;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import org.netbeans.modules.collab.*;
import org.netbeans.modules.collab.core.Debug;


/**
 *
 * @author  sherylsu
 */
public class AddContactGroupForm extends javax.swing.JPanel {
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel groupLbl;
    private javax.swing.JTextField groupTextField;

    // End of variables declaration//GEN-END:variables
    private CollabSession session;
    private DialogDescriptor dialogDescriptor;

    /**
     *
     *
     */
    public AddContactGroupForm(CollabSession session) {
        this.session = session;

        initComponents();

        dialogDescriptor = new DialogDescriptor(
                this, NbBundle.getMessage(AddContactGroupForm.class, "TITLE_AddContactGroupForm")
            ); // NOI18N
        dialogDescriptor.setValid(false);

        // Listen to changes to the search field in order to enable or 
        // disable the find button
        groupTextField.getDocument().addDocumentListener(
            new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                }

                public void insertUpdate(DocumentEvent e) {
                    dialogDescriptor.setValid(groupTextField.getText().length() > 0);
                }

                public void removeUpdate(DocumentEvent e) {
                    dialogDescriptor.setValid(groupTextField.getText().length() > 0);
                }
            }
        );
    }

    /**
     *
     *
     */
    public void addContactGroup() {
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dialogDescriptor);

        try {
            dialog.show();

            if (dialogDescriptor.getValue() == DialogDescriptor.OK_OPTION) {
                if (sessionExists()) {
                    String groupName = groupTextField.getText().trim();

                    if (session.getContactGroup(groupName) != null) {
                        // Notify user that group already exists
                        String message = NbBundle.getMessage(
                                AddContactGroupForm.class, "MSG_AddContactGroupForm_GroupAlreadyExists", groupName
                            );
                        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(message));
                    } else {
                        try {
                            session.createContactGroup(groupName);
                        } catch (CollabException e) {
                            String message = NbBundle.getMessage(
                                    AddContactGroupForm.class, "MSG_AddContactGroupForm_GroupNotCreated",
                                    new Object[] { groupName, e.getMessage() }
                                );
                            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(message));
                            Debug.debugNotify(e);
                        }
                    }
                }
            }
        } finally {
            dialog.dispose();
        }
    }

    private boolean sessionExists() {
        // check session status #5080138
        CollabSession[] sessions = CollabManager.getDefault().getSessions();

        for (int i = 0; i < sessions.length; i++) {
            if (sessions[i].equals(this.session)) {
                return true;
            }
        }

        return false;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() { //GEN-BEGIN:initComponents

        java.awt.GridBagConstraints gridBagConstraints;

        groupLbl = new javax.swing.JLabel();
        groupTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        groupLbl.setText(
            java.util.ResourceBundle.getBundle("org/netbeans/modules/collab/ui/Bundle").getString(
                "LBL_AddContactGroupForm_EnterGroupName"
            )
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(groupLbl, gridBagConstraints);

        groupTextField.setColumns(32);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(groupTextField, gridBagConstraints);
    } //GEN-END:initComponents
}

/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.apisupport.project.ui.customizer;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.apisupport.project.Util;
import org.netbeans.modules.apisupport.project.ui.UIUtil;
import org.openide.util.NbBundle;

/**
 * Panel for choosing a <em>friend</em>.
 *
 * @author  Martin Krauskopf
 */
public class AddFriendPanel extends JPanel {
    
    static final String VALID_PROPERTY = "isPanelValid"; // NOI18N
    
    boolean valid = false;
    
    /** Creates new form AddFriendPanel */
    public AddFriendPanel(final SingleModuleProperties props) {
        initComponents();
        // helps prevents flickering
        friends.setPrototypeDisplayValue("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM"); // NOI18N
        Component editorComp = friends.getEditor().getEditorComponent();
        if (editorComp instanceof JTextComponent) {
            ((JTextComponent) editorComp).getDocument().addDocumentListener(new UIUtil.DocumentAdapter() {
                public void insertUpdate(DocumentEvent e) {
                    checkValidity();
                }
            });
        }
        friends.setEnabled(false);
        friends.setModel(CustomizerComponentFactory.createComboWaitModel());
        friends.setSelectedItem(CustomizerComponentFactory.WAIT_VALUE);
        ModuleProperties.RP.post(new Runnable() {
            public void run() {
                final String[] friendCNBs = props.getAvailableFriends();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        DefaultComboBoxModel model = new DefaultComboBoxModel();
                        for (int i = 0; i < friendCNBs.length; i++) {
                            model.addElement(friendCNBs[i]);
                        }
                        friends.setModel(model);
                        friends.setEnabled(true);
                        checkValidity();
                        // data are loaded lets LayoutManager do its work
                        friends.setPrototypeDisplayValue(null);
                        Window w = SwingUtilities.getWindowAncestor(AddFriendPanel.this);
                        if (w != null && w.getWidth() < w.getPreferredSize().getWidth()) {
                            w.pack();
                        }
                    }
                });
            }
        });
    }
    
    private void checkValidity() {
        String cnb = getFriendCNB();
        if (cnb.length() == 0) {
            setErrorMessage(NbBundle.getMessage(AddFriendPanel.class, "MSG_FriendMayNotBeBlank"));
        } else if (CustomizerComponentFactory.WAIT_VALUE == friends.getSelectedItem()) {
            setErrorMessage(""); // do not show errormessage during "Please wait..." state
        } else if (!Util.isValidJavaFQN(cnb)) {
            setErrorMessage(NbBundle.getMessage(AddFriendPanel.class, "MSG_FriendIsNotValidCNB"));
        } else {
            setErrorMessage(null);
        }
    }
    
    String getFriendCNB() {
        return friends.getEditor().getItem().toString().trim();
    }
    
    private void setErrorMessage(String errMessage) {
        this.errorMessage.setText(errMessage == null ? " " : errMessage);
        boolean valid = errMessage == null;
        if (this.valid != valid) {
            this.valid = valid;
            firePropertyChange(AddFriendPanel.VALID_PROPERTY, !valid, valid);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        friendsTxt = new javax.swing.JLabel();
        friends = new javax.swing.JComboBox();
        errorMessage = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        friendsTxt.setLabelFor(friends);
        org.openide.awt.Mnemonics.setLocalizedText(friendsTxt, org.openide.util.NbBundle.getMessage(AddFriendPanel.class, "LBL_FriendModule"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        add(friendsTxt, gridBagConstraints);

        friends.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(friends, gridBagConstraints);

        errorMessage.setForeground(javax.swing.UIManager.getDefaults().getColor("nb.errorForeground"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        add(errorMessage, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel errorMessage;
    public javax.swing.JComboBox friends;
    public javax.swing.JLabel friendsTxt;
    // End of variables declaration//GEN-END:variables
    
}

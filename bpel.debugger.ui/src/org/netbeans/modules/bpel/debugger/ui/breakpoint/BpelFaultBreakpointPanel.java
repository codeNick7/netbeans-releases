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

package org.netbeans.modules.bpel.debugger.ui.breakpoint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.modules.bpel.debugger.api.EditorContextBridge;
import org.netbeans.modules.bpel.debugger.api.breakpoints.BpelFaultBreakpoint;

import org.netbeans.spi.debugger.ui.Controller;
import org.openide.util.NbBundle;


/**
 * @author  Jan Jancura
 */
public class BpelFaultBreakpointPanel extends JPanel implements Controller/*, org.openide.util.HelpCtx.Provider*/ {
    
    private BpelFaultBreakpoint         breakpoint;
    private boolean                     createBreakpoint = false;
    
    private static BpelFaultBreakpoint createBreakpoint() {
        BpelFaultBreakpoint mb = BpelFaultBreakpoint.create(
                EditorContextBridge.getCurrentProcessQName(),
                null);
        return mb;
    }
    
    
    public BpelFaultBreakpointPanel() {
        this(createBreakpoint());
        createBreakpoint = true;
    }
    
    public BpelFaultBreakpointPanel(BpelFaultBreakpoint b) {
        breakpoint = b;
        initComponents();
        if (breakpoint.getProcessQName() != null) {
            tfProcessNamespace.setText(breakpoint.getProcessQName().getNamespaceURI());
            tfProcessName.setText(breakpoint.getProcessQName().getLocalPart());
        }
        if (breakpoint.getFaultQName() != null) {
            tfFaultNamespace.setText(breakpoint.getFaultQName().getNamespaceURI());
            tfFaultName.setText(breakpoint.getFaultQName().getLocalPart());
            cbAllFaults.setSelected(false);
        } else {
            tfFaultNamespace.setEnabled(false);
            tfFaultName.setEnabled(false);
            cbAllFaults.setSelected(true);
        }
        
        // <RAVE>
        // The help IDs for the AddBreakpointPanel panels have to be different from the
        // values returned by getHelpCtx() because they provide different help
        // in the 'Add Breakpoint' dialog and when invoked in the 'Breakpoints' view
        
        //TODO:implement
//        putClientProperty("HelpID_AddBreakpointPanel", "debug.add.breakpoint.java.exception"); // NOI18N
        
        // </RAVE>
    }
    
    // <RAVE>
    // Implement getHelpCtx() with the correct helpID
    //TODO:implement!
//    public org.openide.util.HelpCtx getHelpCtx() {
//        return new org.openide.util.HelpCtx("NetbeansDebuggerBreakpointExceptionJPDA"); // NOI18N
//    }
    // </RAVE>
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pSettings = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfProcessName = new javax.swing.JTextField();
        tfProcessNamespace = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfFaultNamespace = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfFaultName = new javax.swing.JTextField();
        cbAllFaults = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        pSettings.setLayout(new java.awt.GridBagLayout());

        pSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("L_Fault_Breakpoint_BorderTitle")));
        jLabel2.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("MN_L_Fault_Breakpoint_Process_Namespace").charAt(0));
        jLabel2.setLabelFor(tfProcessNamespace);
        jLabel2.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("L_Fault_Breakpoint_Process_Namespace"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(jLabel2, gridBagConstraints);
        jLabel2.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_L_Fault_Breakpoint_Process_Namespace"));

        jLabel3.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("MN_L_Fault_Breakpoint_Process_Name").charAt(0));
        jLabel3.setLabelFor(tfProcessName);
        jLabel3.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("L_Fault_Breakpoint_Process_Name"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(jLabel3, gridBagConstraints);
        jLabel3.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_L_Fault_Breakpoint_Process_Name"));

        tfProcessName.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("TTT_TF_Fault_Breakpoint_Process_Name"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(tfProcessName, gridBagConstraints);
        tfProcessName.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_TF_Fault_Breakpoint_Process_Name"));

        tfProcessNamespace.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("TTT_TF_Fault_Breakpoint_Process_Namespace"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(tfProcessNamespace, gridBagConstraints);
        tfProcessNamespace.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_TF_Fault_Breakpoint_Process_Namespace"));

        jLabel4.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("MN_L_Fault_Breakpoint_Fault_Namespace").charAt(0));
        jLabel4.setLabelFor(tfFaultNamespace);
        jLabel4.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("L_Fault_Breakpoint_Fault_Namespace"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(jLabel4, gridBagConstraints);
        jLabel4.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_L_Fault_Breakpoint_Fault_Namespace"));

        tfFaultNamespace.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_L_Fault_Breakpoint_Fault_Namespace"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(tfFaultNamespace, gridBagConstraints);
        tfFaultNamespace.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_TF_Fault_Breakpoint_Fault_Namespace"));

        jLabel5.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("MN_L_Fault_Breakpoint_Fault_Name").charAt(0));
        jLabel5.setLabelFor(tfFaultName);
        jLabel5.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("L_Fault_Breakpoint_Fault_Name"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(jLabel5, gridBagConstraints);
        jLabel5.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_L_Fault_Breakpoint_Fault_Name"));

        tfFaultName.setToolTipText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("TTT_TF_Fault_Breakpoint_Fault_Name"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(tfFaultName, gridBagConstraints);
        tfFaultName.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("ACSD_TF_Fault_Breakpoint_Fault_Name"));

        cbAllFaults.setMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("MN_CB_Fault_Breakpoint_All_Faults").charAt(0));
        cbAllFaults.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/bpel/debugger/ui/breakpoint/Bundle").getString("CB_Fault_Breakpoint_All_Faults"));
        cbAllFaults.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbAllFaults.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cbAllFaults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAllFaultsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pSettings.add(cbAllFaults, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(pSettings, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void cbAllFaultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAllFaultsActionPerformed
        if (cbAllFaults.isSelected()) {
            tfFaultNamespace.setEnabled(false);
            tfFaultName.setEnabled(false);
        } else {
            tfFaultNamespace.setEnabled(true);
            tfFaultName.setEnabled(true);
        }
    }//GEN-LAST:event_cbAllFaultsActionPerformed

    
    // Controller implementation ...............................................
    
    /**
     * Called when "Ok" button is pressed.
     *
     * @return whether customizer can be closed
     */
    public boolean ok() {
        String processNamespace = tfProcessNamespace.getText().trim();
        String processName = tfProcessName.getText().trim();
        String faultNamespace = tfFaultNamespace.getText().trim();
        String faultName = tfFaultName.getText().trim();
        boolean isAllFaults = cbAllFaults.isSelected();
        
        if (processName.equals("")) {
            JOptionPane.showMessageDialog(this,
                    NbBundle.getMessage(BpelFaultBreakpointPanel.class,
                    "MSG_No_Process_Name_Spec")); //NOI18N
            return false;
        }
        
        if (!isAllFaults && faultName.equals("")) {
            JOptionPane.showMessageDialog(this,
                    NbBundle.getMessage(BpelFaultBreakpointPanel.class,
                    "MSG_No_Fault_Name_Spec")); //NOI18N
            return false;
        }
        
        breakpoint.setProcessQName(new QName(
                processNamespace.equals("") ? null : processNamespace,
                processName));
        
        if (isAllFaults) {
            breakpoint.setFaultQName(null);
        } else {
            breakpoint.setFaultQName(new QName(
                    faultNamespace.equals("") ? null : faultNamespace,
                    faultName));
        }
        
        if (createBreakpoint) 
            DebuggerManager.getDebuggerManager().addBreakpoint(breakpoint);
        return true;
    }
    
    /**
     * Called when "Cancel" button is pressed.
     *
     * @return whether customizer can be closed
     */
    public boolean cancel() {
        return true;
    }
    
    /**
     * Return <code>true</code> whether value of this customizer 
     * is valid (and OK button can be enabled).
     *
     * @return <code>true</code> whether value of this customizer 
     * is valid
     */
    public boolean isValid() {
        return true;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbAllFaults;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pSettings;
    private javax.swing.JTextField tfFaultName;
    private javax.swing.JTextField tfFaultNamespace;
    private javax.swing.JTextField tfProcessName;
    private javax.swing.JTextField tfProcessNamespace;
    // End of variables declaration//GEN-END:variables
    
}

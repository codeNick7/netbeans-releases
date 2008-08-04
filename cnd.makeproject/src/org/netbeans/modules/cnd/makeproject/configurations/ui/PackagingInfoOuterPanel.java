/*
 * PackagingInfo2Panel.java
 *
 * Created on July 28, 2008, 12:42 PM
 */

package org.netbeans.modules.cnd.makeproject.configurations.ui;

import javax.swing.JPanel;

/**
 *
 * @author  thp
 */
public class PackagingInfoOuterPanel extends javax.swing.JPanel {

    /** Creates new form PackagingInfo2Panel */
    public PackagingInfoOuterPanel(PackagingInfoPanel innerPanel) {
        java.awt.GridBagConstraints gridBagConstraints;
        
        initComponents();
        
        remove(packagingHeaderOuterPanel);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(innerPanel, gridBagConstraints);
        
        innerPanel.setDocArea(docTextArea);
        innerPanel.refresh();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        packagingHeaderOuterPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        docTextArea = new javax.swing.JTextArea();
        docTextArea.setBackground(getBackground());

        setLayout(new java.awt.GridBagLayout());

        org.jdesktop.layout.GroupLayout packagingHeaderOuterPanelLayout = new org.jdesktop.layout.GroupLayout(packagingHeaderOuterPanel);
        packagingHeaderOuterPanel.setLayout(packagingHeaderOuterPanelLayout);
        packagingHeaderOuterPanelLayout.setHorizontalGroup(
            packagingHeaderOuterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 392, Short.MAX_VALUE)
        );
        packagingHeaderOuterPanelLayout.setVerticalGroup(
            packagingHeaderOuterPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 203, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(packagingHeaderOuterPanel, gridBagConstraints);

        scrollPane.setBorder(null);

        docTextArea.setColumns(20);
        docTextArea.setEditable(false);
        docTextArea.setLineWrap(true);
        docTextArea.setRows(5);
        docTextArea.setWrapStyleWord(true);
        scrollPane.setViewportView(docTextArea);
        docTextArea.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PackagingInfoOuterPanel.class, "PackagingInfoOuterPanel.docTextArea.AccessibleContext.accessibleName")); // NOI18N
        docTextArea.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(PackagingInfoOuterPanel.class, "PackagingInfoOuterPanel.docTextArea.AccessibleContext.accessibleDescription")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        add(scrollPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea docTextArea;
    private javax.swing.JPanel packagingHeaderOuterPanel;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

}

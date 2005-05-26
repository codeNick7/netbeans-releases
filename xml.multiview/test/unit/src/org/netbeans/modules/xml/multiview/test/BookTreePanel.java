/*
 * BookTreePanel.java
 *
 * Created on May 26, 2005, 5:51 PM
 */

package org.netbeans.modules.xml.multiview.test;

import org.netbeans.modules.xml.multiview.test.bookmodel.Book;
import org.netbeans.modules.xml.multiview.ui.TreePanel;
import org.netbeans.modules.xml.multiview.ui.TreeNode;
/**
 *
 * @author  mkuchtiak
 */
public class BookTreePanel extends javax.swing.JPanel implements TreePanel {

    /** Creates new form BookTreePanel */
    public BookTreePanel() {
        initComponents();
    }
    
    public void setModel(TreeNode node) {
        Book book = ((BookTreePanelMVElement.BookNode)node).getBook();
        titleTF.setText(book.getTitle());
        priceTF.setText(book.getPrice());
        paperbackBox.setSelected(book.isPaperback());
        String instock = book.getAttributeValue("instock");
        instockBox.setSelected("yes".equals(instock));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titleLabel = new javax.swing.JLabel();
        titleTF = new javax.swing.JTextField();
        priceLabel = new javax.swing.JLabel();
        priceTF = new javax.swing.JTextField();
        paperbackBox = new javax.swing.JCheckBox();
        instockBox = new javax.swing.JCheckBox();
        filler = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        titleLabel.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(titleLabel, gridBagConstraints);

        titleTF.setColumns(40);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(titleTF, gridBagConstraints);

        priceLabel.setText("Price:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(priceLabel, gridBagConstraints);

        priceTF.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(priceTF, gridBagConstraints);

        paperbackBox.setText("Paperback");
        paperbackBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        paperbackBox.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        add(paperbackBox, gridBagConstraints);

        instockBox.setText("In Stock");
        instockBox.setActionCommand("instock");
        instockBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        instockBox.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 0);
        add(instockBox, gridBagConstraints);

        filler.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(filler, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel filler;
    private javax.swing.JCheckBox instockBox;
    private javax.swing.JCheckBox paperbackBox;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField priceTF;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField titleTF;
    // End of variables declaration//GEN-END:variables
    
}

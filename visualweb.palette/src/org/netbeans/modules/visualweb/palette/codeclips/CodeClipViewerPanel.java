/*
 * NewJPanel.java
 *
 * Created on August 13, 2007, 6:13 PM
 */

package org.netbeans.modules.visualweb.palette.codeclips;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.border.EtchedBorder;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;

/**
 *
 * @author  joelle
 */
public class CodeClipViewerPanel extends javax.swing.JPanel {

    private static final int DIALOG_HEIGHT = 250;

    /** Creates new form NewJPanel */
    public CodeClipViewerPanel(String title, String tooltip, String content) {
        initComponents();

        setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        setClipName(title);
        setContentText(content);
        setToolTip(tooltip);
        
        titleField.getAccessibleContext().setAccessibleName(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_CodeSnippetViewer_Title"));
        titleField.getAccessibleContext().setAccessibleName(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_CodeSnippetViewer_TitleDesc"));

        clipContentTextArea.getAccessibleContext().setAccessibleName(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_EditorPane_Name")); // NOI18N
        clipContentTextArea.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_EditorPane_Desc")); // NOI18N
  
        tooltipField.getAccessibleContext().setAccessibleName(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_CodeSnippetViewer_Tooltip"));
        tooltipField.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_CodeSnippetViewer_Tooltip_Desc"));

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        clipContentTextArea = new javax.swing.JTextArea();
        titleLabel = new javax.swing.JLabel();
        tooltipLabel = new javax.swing.JLabel();
        tooltipField = new javax.swing.JTextField();

        titleField.setText(org.openide.util.NbBundle.getMessage(CodeClipViewerPanel.class, "CodeClipViewerPanel.titleField.text")); // NOI18N

        clipContentTextArea.setColumns(20);
        clipContentTextArea.setRows(5);
        jScrollPane1.setViewportView(clipContentTextArea);

        titleLabel.setText(org.openide.util.NbBundle.getMessage(CodeClipViewerPanel.class, "CodeClipViewerPanel.titleLabel.text")); // NOI18N

        tooltipLabel.setText(org.openide.util.NbBundle.getMessage(CodeClipViewerPanel.class, "CodeClipViewerPanel.tooltipLabel.text")); // NOI18N

        tooltipField.setText(org.openide.util.NbBundle.getMessage(CodeClipViewerPanel.class, "CodeClipViewerPanel.tooltipField.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(layout.createSequentialGroup()
                                .add(tooltipLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(tooltipField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(titleLabel)
                                .add(27, 27, 27)
                                .add(titleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(titleLabel)
                    .add(titleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tooltipLabel)
                    .add(tooltipField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(14, 14, 14)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 202, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea clipContentTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField tooltipField;
    private javax.swing.JLabel tooltipLabel;
    // End of variables declaration//GEN-END:variables

    public String getContentText() {
        return clipContentTextArea.getText();
    }

    public void setContentText(String clipContent) {
        clipContentTextArea.setText(clipContent);
    }

    public String getClipName() {
        return titleField.getText();
    }

    public void setClipName(String clipName) {
        titleField.setText(clipName);
    }
    
    public String getToolTip() {
        return tooltipField.getText();
    }
    public void setToolTip(String tooltip) {
        tooltipField.setText(tooltip);
    }

    private DialogDescriptor dd;
    private Dialog dialog;
    public void setupDialog() {
        dd = new DialogDescriptor(this, getClipName());
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setPreferredSize(new Dimension(screenWidth * 2 / 3, DIALOG_HEIGHT));
        dialog.getAccessibleContext().setAccessibleName(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_Dialog_Name")); // NOI18N
        dialog.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CodeClipViewerPanel.class, "Acc_Dialog_Desc")); // NOI18N
        dialog.setVisible(true);
    }
    
    public boolean isCancelled() {
        if ( dd.getValue().equals(DialogDescriptor.CANCEL_OPTION) ) {
            return true;
        }
        return false;
    }
    

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if(aFlag){
            setupDialog();
        } else {
            dialog.dispose();
        }
    }
    
    
}

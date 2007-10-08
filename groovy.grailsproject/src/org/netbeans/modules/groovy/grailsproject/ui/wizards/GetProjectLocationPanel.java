/*
 * NewGrailsProjectPanel.java
 *
 * Created on October 1, 2007, 2:49 PM
 */

package org.netbeans.modules.groovy.grailsproject.ui.wizards;

import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import javax.swing.JFileChooser;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import java.io.File;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 *
 * @author  schmidtm
 */
public class GetProjectLocationPanel extends WizardSettingsPanel implements DocumentListener {
    
    boolean valid(WizardDescriptor settings) {
        return true;
        }
    
    void read (WizardDescriptor d) {
        //TODO:
    }
    
    void validate (WizardDescriptor d) throws WizardValidationException {
        // nothing to validate
    }

    void store( WizardDescriptor d ) {
        d.putProperty( "setAsMain", setAsMainCheckBox.isSelected() && setAsMainCheckBox.isVisible() ? Boolean.TRUE : Boolean.FALSE ); // NOI18N
        d.putProperty( "projectFolder", projectFolderTextField.getText() ); // NOI18N
        }
    
    
    
    /** Creates new form NewGrailsProjectPanel */
    public GetProjectLocationPanel() {
        initComponents();
        
        // set the default project directory 
        
        String projectsFolderPath = ProjectChooser.getProjectsFolder().getPath();
        projectLocationTextField.setText(projectsFolderPath);
        projectFolderTextField.setText( projectsFolderPath + File.separatorChar + projectNameTextField.getText() );
        
        // register event listeners to auto-update some fields.
        
        projectLocationTextField.getDocument().addDocumentListener( this );
        projectNameTextField.getDocument().addDocumentListener( this );
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectNameLabel = new javax.swing.JLabel();
        projectLocationLabel = new javax.swing.JLabel();
        projectFolderLabel = new javax.swing.JLabel();
        projectNameTextField = new javax.swing.JTextField();
        projectLocationTextField = new javax.swing.JTextField();
        projectFolderTextField = new javax.swing.JTextField();
        browsLocationJButton = new javax.swing.JButton();
        setAsMainCheckBox = new javax.swing.JCheckBox();

        projectNameLabel.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.projectNameLabel.text")); // NOI18N

        projectLocationLabel.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.projectLocationLabel.text")); // NOI18N

        projectFolderLabel.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.projectFolderLabel.text")); // NOI18N

        projectNameTextField.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.projectNameTextField.text")); // NOI18N

        projectLocationTextField.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.projectLocationTextField.text")); // NOI18N

        projectFolderTextField.setEditable(false);
        projectFolderTextField.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.projectFolderTextField.text")); // NOI18N

        browsLocationJButton.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.browsLocationJButton.text")); // NOI18N
        browsLocationJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browsLocationJButtonActionPerformed(evt);
            }
        });

        setAsMainCheckBox.setSelected(true);
        setAsMainCheckBox.setText(org.openide.util.NbBundle.getMessage(GetProjectLocationPanel.class, "GetProjectLocationPanel.setAsMainCheckBox.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(setAsMainCheckBox)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(projectLocationLabel)
                            .add(projectNameLabel)
                            .add(projectFolderLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(projectNameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, projectFolderTextField)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, projectLocationTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
                                .add(18, 18, 18)
                                .add(browsLocationJButton)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(projectNameLabel)
                    .add(projectNameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(projectLocationLabel)
                    .add(projectLocationTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(browsLocationJButton))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(projectFolderLabel)
                    .add(projectFolderTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(setAsMainCheckBox)
                .addContainerGap(152, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browsLocationJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browsLocationJButtonActionPerformed
            JFileChooser chooser = new JFileChooser ();
            FileUtil.preventFileChooserSymlinkTraversal(chooser, null);
            chooser.setDialogTitle(NbBundle.getMessage(GetProjectLocationPanel.class,"GetProjectLocationPanel.FileChooserTitle"));
            chooser.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
            String path = projectLocationTextField.getText();
            if (path.length() > 0) {
                File f = new File (path);
                if (f.exists ()) {
                    chooser.setSelectedFile(f);
                }
            }
            if ( JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) { //NOI18N
                File projectDir = chooser.getSelectedFile();
                projectLocationTextField.setText( projectDir.getAbsolutePath() );
            }   
}//GEN-LAST:event_browsLocationJButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browsLocationJButton;
    private javax.swing.JLabel projectFolderLabel;
    private javax.swing.JTextField projectFolderTextField;
    private javax.swing.JLabel projectLocationLabel;
    private javax.swing.JTextField projectLocationTextField;
    private javax.swing.JLabel projectNameLabel;
    private javax.swing.JTextField projectNameTextField;
    private javax.swing.JCheckBox setAsMainCheckBox;
    // End of variables declaration//GEN-END:variables

    
    public void insertUpdate(DocumentEvent e) {
        updateTexts( e ) ;
    }

    public void removeUpdate(DocumentEvent e) {
        updateTexts( e ) ;
    }

    public void changedUpdate(DocumentEvent e) {
        updateTexts( e ) ;
    }
    
    /** Handles changes in the Project name and project directory
     */
    private void updateTexts( DocumentEvent e ) {
        
        Document doc = e.getDocument();
                
        if ( doc == projectNameTextField.getDocument() || doc == projectLocationTextField.getDocument() ) {
            // Change in the project name
        
            String projectName = projectNameTextField.getText();
            String projectFolder = projectLocationTextField.getText(); 
             
            getProjectFolderTextField().setText( projectFolder + File.separatorChar + projectName );
            
        }                
  
    }

    public javax.swing.JTextField getProjectFolderTextField() {
        return projectFolderTextField;
    }
    
    
}

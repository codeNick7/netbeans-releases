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

package org.netbeans.modules.compapp.test.ui.wizards;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.netbeans.api.project.Project;
import org.openide.WizardDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;

public class NewTestcaseWsdlVisualPanel extends javax.swing.JPanel implements TreeSelectionListener {

    private Project mProject;
    private WsdlTreeView mWsdlTreeView;   
    private NewTestcaseWsdlWizardPanel mPanel;
    
    /** Creates new form NewTestcaseWsdlVisualPanel_1 */
    public NewTestcaseWsdlVisualPanel(Project project, NewTestcaseWsdlWizardPanel panel) {
        mProject = project;     
        mPanel = panel;
        mWsdlTreeView = new WsdlTreeView(mProject);
        mWsdlTreeView.getExplorerManager().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if(!evt.getPropertyName().equals(ExplorerManager.PROP_SELECTED_NODES)) {
                    return;
                }
                FileObject wsdlFile = mWsdlTreeView.getSelectedWsdlFile();
                if (wsdlFile == null) {
                    mWsdlTf.setText("");  // NOI18N
                } else {
                    mWsdlTf.setText(FileUtil.toFile(wsdlFile).getPath());
                }
            }
        });        
        //in initComponents generated code, post creation code for wsdlTreeView in jPanel1
        initComponents(); 
        mWsdlTreeView.getAccessibleContext().setAccessibleName(
                NbBundle.getMessage(NewTestcaseWsdlVisualPanel.class, "ACS_WsdlTreeView_A11YName"));  // NOI18N
        mWsdlTreeView.getAccessibleContext().setAccessibleDescription(
                NbBundle.getMessage(NewTestcaseWsdlVisualPanel.class, "ACS_WsdlTreeView_A11YDesc"));  // NOI18N
        org.jdesktop.layout.GroupLayout jPanel1Layout = (org.jdesktop.layout.GroupLayout) jPanel1.getLayout();
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mWsdlTreeView, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mWsdlTreeView, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        
        JTree tree = (JTree) mWsdlTreeView.getTreeView().getViewport().getComponent(0);
        tree.getSelectionModel().addTreeSelectionListener(this);
        
//        TreeNode root = (TreeNode) tree.getModel().getRoot();
//        int cnt = root.getChildCount();
//        for (int i = 0; i < cnt; i++) {
//        System.out.println(((DefaultMutableTreeNode)root.getChildAt(i)).getPath());
//            tree.expandPath(new TreePath(((DefaultMutableTreeNode)root.getChildAt(i)).getPath()));
//        }
     }
     
    public String getName() {
        return NbBundle.getMessage(NewTestcaseWsdlVisualPanel.class, 
                                   "LBL_Select_the_WSDL_document");  // NOI18N
    }

    public FileObject getSelectedWsdlFile() {
        return mWsdlTreeView.getSelectedWsdlFile();
    }

    public WsdlTreeView getWsdlTreeView() {
        return mWsdlTreeView;
    }
        
    public void valueChanged(TreeSelectionEvent e) {
         mPanel.fireChangeEvent(); // Notify that the panel changed
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        mWsdlTreeView = new WsdlTreeView(mProject);
        mWsdlTreeView.getExplorerManager().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if(!evt.getPropertyName().equals(ExplorerManager.PROP_SELECTED_NODES)) {
                    return;
                }
                FileObject wsdlFile = mWsdlTreeView.getSelectedWsdlFile();
                if (wsdlFile == null) {
                    mWsdlTf.setText("");  // NOI18N
                } else {
                    mWsdlTf.setText(FileUtil.toFile(wsdlFile).getPath());
                }
            }
        });
        jPanel1 = new javax.swing.JPanel();
        mWsdlLbl = new javax.swing.JLabel();
        mWsdlTf = new javax.swing.JTextField();

        getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(NewTestcaseWsdlVisualPanel.class, "ACS_NewTestcaseWsdlVisualPanel_A11YDesc"));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 396, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 271, Short.MAX_VALUE)
        );

        mWsdlLbl.setLabelFor(mWsdlTf);
        mWsdlLbl.setText(org.openide.util.NbBundle.getMessage(NewTestcaseWsdlVisualPanel.class, "LBL_The_WSDL_selected"));

        mWsdlTf.setEditable(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(mWsdlLbl)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mWsdlTf, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(mWsdlLbl)
                    .add(mWsdlTf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel mWsdlLbl;
    private javax.swing.JTextField mWsdlTf;
    // End of variables declaration//GEN-END:variables
    
}

/*
 * SchemaArtifactSelectionPanel.java
 *
 * Created on March 6, 2008, 5:34 PM
 */

package org.netbeans.modules.iep.editor.wizard;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.netbeans.api.project.Project;
import org.netbeans.module.iep.editor.xsd.nodes.SchemaArtifactTreeModel;
import org.netbeans.module.iep.editor.xsd.SchemaArtifactTreeCellEditor;
import org.netbeans.module.iep.editor.xsd.SchemaArtifactTreeCellRenderer;
import org.netbeans.module.iep.editor.xsd.nodes.FolderNode;
import org.netbeans.module.iep.editor.xsd.nodes.SelectableTreeNode;
import org.netbeans.modules.xml.axi.AXIComponent;
import org.netbeans.modules.xml.axi.AXIType;
import org.openide.util.NbBundle;

/**
 *
 * @author  radval
 */
public class SchemaArtifactSelectionPanel extends javax.swing.JPanel {

    private List<AXIComponent> mExistingArtificatNames = new ArrayList<AXIComponent>();
            
    /** Creates new form SchemaArtifactSelectionPanel */
    public SchemaArtifactSelectionPanel() {
        initComponents();
    }

    public SchemaArtifactSelectionPanel(List<AXIComponent> existingArtificatNames, Project project) {
        this();
        FolderNode rootNode = new FolderNode(NbBundle.getMessage(ElementOrTypeChooserHelper.class, "LBL_ByFile_DisplayName"));
        this.mExistingArtificatNames = existingArtificatNames;
        
        SchemaArtifactTreeModel model = new SchemaArtifactTreeModel(rootNode, 
                                                                    project, 
                                                                    existingArtificatNames,
                                                                    jTree1);
        
        
        this.jTree1.setModel(model);
        //this.jTree1.addMouseListener(new TreeMouseListener());
        
        TreeCellRenderer renderer = new SchemaArtifactTreeCellRenderer();
        TreeCellEditor editor = new SchemaArtifactTreeCellEditor(jTree1, renderer, this.mExistingArtificatNames);
        this.jTree1.setCellRenderer(renderer);
        this.jTree1.setCellEditor(editor);
        this.jTree1.setEditable(true);
    }
    
    public List<AXIComponent> getSelectedArtifactNames() {
        return this.mExistingArtificatNames;
    }
            
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        jScrollPane1.setViewportView(jTree1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

    
}

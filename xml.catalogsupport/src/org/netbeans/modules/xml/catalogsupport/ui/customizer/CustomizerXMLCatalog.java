/*
 * CustomizerXMLCatalog.java
 *
 * Created on December 12, 2006, 2:45 PM
 */

package org.netbeans.modules.xml.catalogsupport.ui.customizer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;

import org.openide.util.HelpCtx;
import org.netbeans.api.project.Project;

import org.netbeans.modules.xml.retriever.catalog.CatalogEntry;
import org.netbeans.modules.xml.retriever.catalog.CatalogWriteModel;
import org.netbeans.modules.xml.retriever.catalog.CatalogWriteModelFactory;
import org.netbeans.modules.xml.xam.locator.CatalogModelException;

/**
 *
 * @author  Ajit
 */
public class CustomizerXMLCatalog extends javax.swing.JPanel implements HelpCtx.Provider {
    
    private Project project;
    private CatalogWriteModel cwm;
    private List<CatalogEntry> cEntries = Collections.emptyList();
    private List<CatalogEntry> deletedEntries;
    /** Creates new form CustomizerXMLCatalog */
    public CustomizerXMLCatalog(Project project) {
        this.project = project;
        initComponents();
        initialize();
    }
    
    private void initialize() {
        try {
            cwm = CatalogWriteModelFactory.getInstance().getCatalogWriteModelForProject(project.getProjectDirectory());
            cEntries = new ArrayList<CatalogEntry>(cwm.getCatalogEntries());
            DefaultTableModel model = (DefaultTableModel)catalogTable.getModel();
            for(CatalogEntry cEntry:cEntries) {
                model.addRow(new String[]{cEntry.getSource(),cEntry.getTarget()});
            }
            catalogTable.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
                public void columnAdded(TableColumnModelEvent e) {
                }
                public void columnMarginChanged(ChangeEvent e) {
                }
                public void columnMoved(TableColumnModelEvent e) {
                }
                public void columnRemoved(TableColumnModelEvent e) {
                }
                public void columnSelectionChanged(ListSelectionEvent e) {
                    if(catalogTable.getSelectedColumnCount()==0) {
                        removeButton.setEnabled(false);
                    } else {
                        removeButton.setEnabled(true);
                    }
                }
            });
        } catch (CatalogModelException ex) {
            // show blank ui
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        topLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        catalogTable = new javax.swing.JTable();
        removeButton = new javax.swing.JButton();
        cacheLabel = new javax.swing.JLabel();
        cacheTextField = new javax.swing.JTextField();
        clearCacheButton = new javax.swing.JButton();

        topLabel.setLabelFor(catalogTable);
        org.openide.awt.Mnemonics.setLocalizedText(topLabel, org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "LBL_CustomizerXMLCatalog_CatalogEntries"));
        topLabel.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "HINT_CustomizerXMLCatalog_CatalogEntries"));

        catalogTable.setModel(new DefaultTableModel(
            new String [][]{},
            new String []{org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "LBL_CustomizerXMLCatalog_ReferenceKey"),
                org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "LBL_CustomizerXMLCatalog_ReferencedLocation"),
            }
        ));
        jScrollPane1.setViewportView(catalogTable);

        org.openide.awt.Mnemonics.setLocalizedText(removeButton, org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "LBL_CustomizerXMLCatalog_RemoveButton"));
        removeButton.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "HINT_CustomizerXMLCatalog_RemoveButton"));
        removeButton.setEnabled(false);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEntry(evt);
            }
        });

        cacheLabel.setLabelFor(cacheTextField);
        org.openide.awt.Mnemonics.setLocalizedText(cacheLabel, org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "LBL_CustomizerXMLCatalog_CacheLabel"));
        cacheLabel.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "HINT_CustomizerXMLCatalog_CacheLabel"));

        cacheTextField.setEditable(false);
        cacheTextField.setText("nbproject/private/retrieved");
        cacheTextField.setBorder(null);

        org.openide.awt.Mnemonics.setLocalizedText(clearCacheButton, org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "LBL_CustomizerXMLCatalog_ClearCacheButton"));
        clearCacheButton.setToolTipText(org.openide.util.NbBundle.getMessage(CustomizerXMLCatalog.class, "HINT_CustomizerXMLCatalog_ClearCacheButton"));
        clearCacheButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearCacheDirectory(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(topLabel))
                    .add(layout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(cacheLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cacheTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeButton))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(clearCacheButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(topLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(removeButton)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(cacheLabel)
                    .add(cacheTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(clearCacheButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Called when remove button is clicked.
     * Removes the catalog entries from UI but not from the catalog file.
     */
    private void deleteEntry(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEntry
        DefaultTableModel model = (DefaultTableModel)catalogTable.getModel();
        int[] rowSelection = catalogTable.getSelectedRows();
        if(deletedEntries == null) {
            deletedEntries = new ArrayList<CatalogEntry>(rowSelection.length);
        }
        for(int i = rowSelection.length-1; i>=0; i--) {
            int idx = rowSelection[i];
            model.removeRow(idx);
            deletedEntries.add(cEntries.remove(idx));
        }
        catalogTable.clearSelection();
    }//GEN-LAST:event_deleteEntry

    /**
     * Called when clear cache button is clicked.
     * TODO: provide implementation.
     */    
    private void clearCacheDirectory(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearCacheDirectory
        
    }//GEN-LAST:event_clearCacheDirectory
    
    /**
     * Called from customizer provider when user chooses to apply project modifications.
     */
    public void storeProjectData() {
        if(deletedEntries!=null && cwm !=null) {
            for(CatalogEntry deletedEntry:deletedEntries) {
                try {
                    URI uri = new URI(deletedEntry.getSource());
                    cwm.removeURI(uri);
                } catch (URISyntaxException ex) { // notify?
                } catch (IOException ex) { // notify?
                }
            }
        }
    }

    public HelpCtx getHelpCtx() {
        return new HelpCtx(CustomizerXMLCatalog.class);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cacheLabel;
    private javax.swing.JTextField cacheTextField;
    private javax.swing.JTable catalogTable;
    private javax.swing.JButton clearCacheButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private javax.swing.JLabel topLabel;
    // End of variables declaration//GEN-END:variables
    
}

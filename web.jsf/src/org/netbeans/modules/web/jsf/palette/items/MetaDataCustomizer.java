/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

/*
 * MetaDataCustomizer.java
 *
 * Created on Aug 27, 2009, 3:08:21 PM
 */

package org.netbeans.modules.web.jsf.palette.items;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;

/**
 *
 * @author alexeybutenko
 */
public class MetaDataCustomizer extends javax.swing.JPanel implements ListSelectionListener{

    private Dialog dialog = null;
    private DialogDescriptor descriptor = null;
    private boolean dialogOK = false;

    private MetaData metadata;
    private JTextComponent target;

    private DefaultTableModel tableModel = new MetadataTableModel();

    /** Creates new form MetaDataCustomizer */
    public MetaDataCustomizer(MetaData m, JTextComponent component) {
        metadata = m;
        target = component;
        
        initComponents();
        
        initTable();
    }

    public boolean showDialog() {

        dialogOK = false;

        String displayName = "";
        try {
            displayName = NbBundle.getMessage(MetaDataCustomizer.class, "NAME_jsp-JsfMetadata");
        }
        catch (Exception e) {}

        descriptor = new DialogDescriptor
                (this, displayName, true,
                 DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION,
                 new ActionListener() {
                     public void actionPerformed(ActionEvent e) {
                        if (descriptor.getValue().equals(DialogDescriptor.OK_OPTION)) {
                            evaluateInput();
                            dialogOK = true;
                        }
                        dialog.dispose();
		     }
		 }
                );

        dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(MetaDataCustomizer.class, "ACSN_METADATA_Dialog"));
        dialog.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(MetaData.class, "ACSD_METADATA_Dialog"));
        dialog.setVisible(true);
        repaint();

        return dialogOK;
    }

    private void evaluateInput() {

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
        viewParamTable = new javax.swing.JTable();
        viewParamLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JToggleButton();
        removeButton = new javax.swing.JToggleButton();

        viewParamTable.setModel(tableModel);
        viewParamTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(viewParamTable);
        viewParamTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        viewParamLabel.setLabelFor(viewParamTable);
        viewParamLabel.setText(org.openide.util.NbBundle.getMessage(MetaDataCustomizer.class, "LBL_VIEW_PARAM")); // NOI18N

        addButton.setText(org.openide.util.NbBundle.getMessage(MetaDataCustomizer.class, "LBL_ADD_BUTTON")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText(org.openide.util.NbBundle.getMessage(MetaDataCustomizer.class, "LBL_REMOVE_BUTTON")); // NOI18N
        removeButton.setEnabled(false);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(viewParamLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewParamLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        tableModel.addRow(new String[]{"name","value"});
        viewParamTable.changeSelection(tableModel.getRowCount()-1, 0, false, false);
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        tableModel.removeRow(viewParamTable.getSelectedRow());
    }//GEN-LAST:event_removeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton addButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton removeButton;
    private javax.swing.JLabel viewParamLabel;
    private javax.swing.JTable viewParamTable;
    // End of variables declaration//GEN-END:variables

    public void valueChanged(ListSelectionEvent e) {
        int row = viewParamTable.getSelectedRow();
        if (row != -1) {
            removeButton.setEnabled(true);
        } else {
            removeButton.setEnabled(false);
        }
    }

    // End of variables declaration

    public void initTable() {
        for (Entry entry : metadata.getProperties().entrySet()){
            tableModel.addRow(new String[]{(String)entry.getKey(),(String)entry.getValue()});
        }
        viewParamTable.getSelectionModel().addListSelectionListener(this);
    }
    private class MetadataTableModel extends DefaultTableModel {

        public static final int KEY_COLUMN = 0;
        public static final int VALUE_COLUMN = 1;

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (column == KEY_COLUMN) {
                String value = metadata.removeProperty((String)getValueAt(row, KEY_COLUMN));
                metadata.addProperty((String) aValue,value);
            } else if (column == VALUE_COLUMN) {
                metadata.addProperty((String)getValueAt(row, KEY_COLUMN), (String)aValue);
            }
            super.setValueAt(aValue, row, column);
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            if (column == KEY_COLUMN) {
                return org.openide.util.NbBundle.getMessage(MetaDataCustomizer.class, "LBL_NAME_COLUMN");
            } else if (column == VALUE_COLUMN) {
                return org.openide.util.NbBundle.getMessage(MetaDataCustomizer.class, "LBL_VALUE_COLUMN");
            }
            return "";
        }
    }
}

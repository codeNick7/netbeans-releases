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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.j2ee.common.method.impl;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import org.netbeans.modules.j2ee.common.method.MethodModel;
import org.openide.util.NbBundle;

/**
 *
 * @author  Martin Adamek
 */
public final class ParametersPanel extends javax.swing.JPanel {
    
    private static final int COL_NAME_INDEX = 0;
    private static final int COL_TYPE_INDEX = 1;
    private static final int COL_FINAL_INDEX = 2;
    
    private static final String[] columnNames = {
        NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.LBL_Name"),
        NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.LBL_Type"),
        NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.LBL_Final"),
    };
    
    private final ParamsTableModel tableModel;

    public ParametersPanel(List<MethodModel.Variable> parameters) {
        initComponents();
        
        tableModel = new ParamsTableModel(parameters);
        table.setModel(tableModel);
        
        JComboBox typeCombo = new JComboBox();
        
        ReturnTypeUIHelper.connect(typeCombo);
        TableColumn typeTableColumn = table.getColumnModel().getColumn(COL_TYPE_INDEX);
        typeTableColumn.setCellEditor(new DefaultCellEditor(typeCombo));
        
        table.setRowHeight(typeCombo.getPreferredSize().height);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                updateButtons();
            }
        });
        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateButtons();
            }
        });
    }
    
    public List<MethodModel.Variable> getParameters() {
        return tableModel.getParameters();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        org.openide.awt.Mnemonics.setLocalizedText(addButton, org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeButton, org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.removeButton.text")); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(upButton, org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.upButton.text")); // NOI18N
        upButton.setEnabled(false);
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(downButton, org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.downButton.text")); // NOI18N
        downButton.setEnabled(false);
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(addButton)
                    .add(removeButton)
                    .add(upButton)
                    .add(downButton))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {addButton, downButton, removeButton, upButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(addButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeButton)
                        .add(22, 22, 22)
                        .add(upButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(downButton))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
    int selIndex = table.getSelectedRow();
    int newIndex = selIndex - 1;
    if (newIndex >= 0) {
        tableModel.set(newIndex, tableModel.set(selIndex, tableModel.getParameter(newIndex)));
        table.setRowSelectionInterval(newIndex, newIndex);
        updateButtons();
    }
}//GEN-LAST:event_upButtonActionPerformed

private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
    int selIndex = table.getSelectedRow();
    int newIndex = selIndex + 1;
    if (newIndex < tableModel.getParameters().size()) {
        tableModel.set(newIndex, tableModel.set(selIndex, tableModel.getParameter(newIndex)));
        table.setRowSelectionInterval(newIndex, newIndex);
        updateButtons();
    }
}//GEN-LAST:event_downButtonActionPerformed

private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
    int selectedRow = table.getSelectedRow();
    if (selectedRow > -1) {
        tableModel.removeParameter(selectedRow);
    }
    if (selectedRow == table.getRowCount()) {
        selectedRow--;
    }
    table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
    updateButtons();
}//GEN-LAST:event_removeButtonActionPerformed

private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    int index = tableModel.addParameter();
    table.getSelectionModel().setSelectionInterval(index, index);
    updateButtons();
}//GEN-LAST:event_addButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private javax.swing.JTable table;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
    
    private void updateButtons() {
        int selIndex = table.getSelectedRow();
        boolean oneSelected = table.getSelectedRowCount() == 1;
        
        removeButton.setEnabled(oneSelected);
        upButton.setEnabled(oneSelected && (selIndex > 0));
        downButton.setEnabled(oneSelected && (selIndex < tableModel.getRowCount() - 1));
    }
    
    // accessible for test
    static class ParamsTableModel extends AbstractTableModel {
        
        private final List<MethodModel.Variable> parameters;
        
        public ParamsTableModel(List<MethodModel.Variable> parameters) {
            this.parameters = new ArrayList<MethodModel.Variable>(parameters);
        }
        
        public List<MethodModel.Variable> getParameters() {
            return parameters;
        }
        
        public int addParameter() {
            String name = generateUniqueName("parameter");
            MethodModel.Variable parameter = MethodModel.Variable.create("java.lang.String", name, false);
            int index = parameters.size();
            parameters.add(parameter);
            fireTableRowsInserted(index, index);
            return index;
        }
        
        public void removeParameter(int index) {
            parameters.remove(index);
            fireTableRowsDeleted(index, index);
        }
        
        public MethodModel.Variable getParameter(int index) {
            return parameters.get(index);
        }
        
        public MethodModel.Variable set(int index, MethodModel.Variable parameter) {
            return parameters.set(index, parameter);
        }
        
        public int getRowCount() {
            return parameters.size();
        }
        
        public int getColumnCount() {
            return 3;
        }
        
        public Object getValueAt(int row, int column) {
            Object result = null;
            MethodModel.Variable parameter = parameters.get(row);
            if (parameter != null) {
                switch (column) {
                    case COL_NAME_INDEX: result = parameter.getName(); break;
                    case COL_TYPE_INDEX: result = parameter.getType(); break;
                    case COL_FINAL_INDEX: result = parameter.getFinalModifier(); break;
                    default:
                }
            }
            return result;
        }
        
        public String getColumnName(int column) {
            return columnNames[column];
        }
        
        public boolean isCellEditable(int row, int column) {
            return true;
        }
        
        public void setValueAt(Object aValue, int row, int column) {
            MethodModel.Variable parameter = parameters.get(row);
            MethodModel.Variable changedParameter = MethodModel.Variable.create(
                    column == COL_TYPE_INDEX ? chooseType(aValue, parameter.getType()) : parameter.getType(),
                    column == COL_NAME_INDEX ? chooseName(aValue, parameter.getName()) : parameter.getName(),
                    column == COL_FINAL_INDEX ? (Boolean) aValue : parameter.getFinalModifier()
                    );
            parameters.set(row, changedParameter);
            fireTableCellUpdated(row, column);
        }
        
        // JTable uses this method to determine the default renderer/editor for each cell.
        // If we didn't implement this method, then the last column would contain
        // text ("true"/"false"), rather than a check box.
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
        private String generateUniqueName(String name) {
            List<Integer> numberSuffixes = new ArrayList<Integer>();
            for (MethodModel.Variable variable : parameters) {
                if (!name.equals(variable.getName()) && variable.getName().startsWith(name)) {
                    String suffix = variable.getName().substring(name.length());
                    if (isNumber(suffix)) {
                        numberSuffixes.add(Integer.parseInt(suffix));
                    }
                }
            }
            Collections.sort(numberSuffixes);
            String result = name;
            if (numberSuffixes.size() > 0) {
                int newSuffix = numberSuffixes.get(numberSuffixes.size() - 1) + 1;
                result = name + newSuffix;
            } else if (parameters.size() > 0) {
                result = name + 1;
            }
            return result;
        }
        
        private boolean isNumber(String value) {
            for (char character : value.toCharArray()) {
                if (!Character.isDigit(character)) {
                  return false;
                }
            }
          return true;//!value.trim().equals("");
        }
        
    }

    private static String chooseType(Object aValue, String typeName) {
        if (!(aValue instanceof String)) {
            return "Object";
        }
        if (((String)aValue).trim().equals("")) {
            return typeName;
        }
        return (String) aValue;
    }

    private static String chooseName(Object aValue, String name) {
        if (((String)aValue).trim().equals("")) {
            return name;
        }
        return (String) aValue;
    }
        
}

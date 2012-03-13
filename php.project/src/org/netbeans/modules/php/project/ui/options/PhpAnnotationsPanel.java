/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.project.ui.options;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.netbeans.modules.php.api.util.StringUtils;
import org.netbeans.modules.php.project.annotations.UserAnnotationPanel;
import org.netbeans.modules.php.project.annotations.UserAnnotationTag;
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

public class PhpAnnotationsPanel extends JPanel {

    private static final long serialVersionUID = 89732416546545L;

    @NbBundle.Messages({
        "PhpAnnotationsPanel.table.column.name.title=Name",
        "PhpAnnotationsPanel.table.column.for.title=For"
    })
    static final String[] TABLE_COLUMNS = {
        Bundle.PhpAnnotationsPanel_table_column_name_title(),
        Bundle.PhpAnnotationsPanel_table_column_for_title(),
    };


    final AnnotationsTableModel tableModel;

    // @GuardedBy(EDT)
    List<UserAnnotationTag> annotations = Collections.emptyList();


    public PhpAnnotationsPanel() {
        tableModel = new AnnotationsTableModel();

        initComponents();
        initTable();
        initButtons();
    }

    public List<UserAnnotationTag> getAnnotations() {
        assert EventQueue.isDispatchThread();
        return annotations;
    }

    public void setAnnotations(List<UserAnnotationTag> annotations) {
        assert EventQueue.isDispatchThread();
        this.annotations = annotations;
        tableModel.fireAnnotationsChange();
    }

    private void initTable() {
        // model
        annotationsTable.setModel(tableModel);
        // columns
        annotationsTable.getTableHeader().setReorderingAllowed(false);
        // sorting
        annotationsTable.setAutoCreateRowSorter(true);
        // selections
        annotationsTable.setColumnSelectionAllowed(false);
        annotationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    return;
                }
                setEnabledButtons(annotationsTable.getSelectedRowCount());
            }
        });
        // actions
        annotationsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2
                        && editButton.isEnabled()) {
                    openAnnotationPanel(annotationsTable.getSelectedRow());
                }
            }
        });
    }

    private void initButtons() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAnnotationPanel(null);
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAnnotationPanel(annotationsTable.getSelectedRow());
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAnnotations();
            }
        });
    }

    void setEnabledButtons(int selectedRowCount) {
        deleteButton.setEnabled(selectedRowCount > 0);
        editButton.setEnabled(selectedRowCount == 1);
    }

    void openAnnotationPanel(Integer index) {
        assert EventQueue.isDispatchThread();
        UserAnnotationPanel panel = new UserAnnotationPanel(getAnnotation(index));
        if (panel.open()) {
            UserAnnotationTag annotation = panel.getAnnotation();
            if (index == null) {
                // add
                annotations.add(annotation);
                tableModel.fireAnnotationsChange();
            } else {
                // edit
                annotations.set(index, annotation);
                tableModel.fireAnnotationChange(index);
            }
        }
    }

    void deleteAnnotations() {
        int[] selectedRows = annotationsTable.getSelectedRows();
        assert selectedRows.length > 0 : "No selected annotations?!";
        if (selectedRows.length == 0) {
            return;
        }
        // delete annotations from the end to avoid ArrayIndexOutOfBoundsException
        Arrays.sort(selectedRows);
        for (int i = selectedRows.length - 1; i >= 0; --i) {
            annotations.remove(i);
        }
        tableModel.fireAnnotationsChange();
    }

    private UserAnnotationTag getAnnotation(Integer index) {
        assert EventQueue.isDispatchThread();
        if (index == null) {
            return new UserAnnotationTag(
                    EnumSet.of(UserAnnotationTag.Type.FUNCTION),
                    "sample", // NOI18N
                    "@sample(${param1}, ${param2} = ${value1})", // NOI18N
                    NbBundle.getMessage(PhpAnnotationsPanel.class, "SampleTag.documentation"));
        }
        return annotations.get(index.intValue());
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        annotationsLabel = new JLabel();
        annotationsScrollPane = new JScrollPane();
        annotationsTable = new JTable();
        addButton = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        noteLabel = new JLabel();
        infoLabel = new JLabel();

        annotationsLabel.setLabelFor(annotationsTable);
        Mnemonics.setLocalizedText(annotationsLabel, NbBundle.getMessage(PhpAnnotationsPanel.class, "PhpAnnotationsPanel.annotationsLabel.text")); // NOI18N

        annotationsScrollPane.setViewportView(annotationsTable);

        Mnemonics.setLocalizedText(addButton, NbBundle.getMessage(PhpAnnotationsPanel.class, "PhpAnnotationsPanel.addButton.text")); // NOI18N
        Mnemonics.setLocalizedText(editButton, NbBundle.getMessage(PhpAnnotationsPanel.class, "PhpAnnotationsPanel.editButton.text")); // NOI18N
        editButton.setEnabled(false);

        Mnemonics.setLocalizedText(deleteButton, NbBundle.getMessage(PhpAnnotationsPanel.class, "PhpAnnotationsPanel.deleteButton.text")); // NOI18N
        deleteButton.setEnabled(false);
        Mnemonics.setLocalizedText(noteLabel, NbBundle.getMessage(PhpAnnotationsPanel.class, "PhpAnnotationsPanel.noteLabel.text")); // NOI18N
        Mnemonics.setLocalizedText(infoLabel, NbBundle.getMessage(PhpAnnotationsPanel.class, "PhpAnnotationsPanel.infoLabel.text")); // NOI18N

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()

                .addComponent(annotationsScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(addButton, Alignment.TRAILING).addComponent(editButton, Alignment.TRAILING).addComponent(deleteButton, Alignment.TRAILING))).addGroup(layout.createSequentialGroup()

                .addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(annotationsLabel).addComponent(noteLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(0, 0, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel)
                .addContainerGap())
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {addButton, deleteButton, editButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addComponent(annotationsLabel)

                .addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)

                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(editButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(deleteButton)).addComponent(annotationsScrollPane, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(noteLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(infoLabel))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton addButton;
    private JLabel annotationsLabel;
    private JScrollPane annotationsScrollPane;
    private JTable annotationsTable;
    private JButton deleteButton;
    private JButton editButton;
    private JLabel infoLabel;
    private JLabel noteLabel;
    // End of variables declaration//GEN-END:variables

    //~ Inner classes

    private final class AnnotationsTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 167686524135456L;


        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public int getRowCount() {
            assert EventQueue.isDispatchThread();
            return annotations.size();
        }

        @Override
        public int getColumnCount() {
            return TABLE_COLUMNS.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            assert EventQueue.isDispatchThread();
            UserAnnotationTag annotation = annotations.get(rowIndex);
            if (columnIndex == 0) {
                return annotation.getName();
            } else if (columnIndex == 1) {
                return getTypes(annotation.getTypes());
            }
            throw new IllegalStateException("Unknown column index: " + columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return TABLE_COLUMNS[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public void fireAnnotationChange(int row) {
            for (int i = 0; i < TABLE_COLUMNS.length; i++) {
                fireTableCellUpdated(row, i);
            }
        }

        public void fireAnnotationsChange() {
            fireTableDataChanged();
        }

        @NbBundle.Messages("PhpAnnotationsPanel.value.delimiter=, ")
        private String getTypes(EnumSet<UserAnnotationTag.Type> types) {
            ArrayList<String> list = new ArrayList<String>(types.size());
            for (UserAnnotationTag.Type type : types) {
                list.add(type.getTitle());
            }
            return StringUtils.implode(list, Bundle.PhpAnnotationsPanel_value_delimiter());
        }

    }

}

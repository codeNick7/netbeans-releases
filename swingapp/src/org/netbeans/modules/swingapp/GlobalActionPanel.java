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

package org.netbeans.modules.swingapp;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.form.FormDataObject;
import org.netbeans.modules.form.FormEditor;
import org.netbeans.modules.form.FormEditorSupport;
import org.netbeans.modules.form.FormModel;
import org.netbeans.modules.form.RADComponent;
import org.netbeans.modules.swingapp.actiontable.ActionTableModel;
import org.netbeans.modules.swingapp.actiontable.IconTableCellRenderer;
import org.netbeans.modules.swingapp.util.FilteredTableModel;
import org.netbeans.modules.swingapp.util.TableSorter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.windows.TopComponent;
import org.openide.windows.TopComponentGroup;
import org.openide.windows.WindowManager;

/**
 * The GlobalActionPanel is a dockable top component which lets the user see all actions
 * throughout the application and edit them.
 * @author  joshua.marinacci@sun.com
 */
public class GlobalActionPanel extends javax.swing.JPanel {
    private ActionManager actionManager;
    
    private ActionManager.ActionChangedListener actChangeListener = new ActionManager.ActionChangedListener() {
        public void actionChanged(ProxyAction action) {
            if(realModel != null) {
                realModel.updateAction(action);
            }
        }
    };
    
    private ActionTableModel realModel;
    
    /** Creates new form GlobalActionPanel */
    public GlobalActionPanel() {
        initComponents();
        actionManager = ActionManager.getEmptyActionManager();
        
        
        actionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actionTable.setDefaultRenderer(Icon.class, new IconTableCellRenderer());
        actionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        actionTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    editSelectedAction();
                }
            }
        });
        
        boundComponentList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    jumpToSelectedComponent();
                }
            }
        });
        
        projectCombo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                JLabel label = (JLabel) comp;
                if(value instanceof Project) {
                    label.setText(((Project)value).getProjectDirectory().getName());
                }
                return comp;
            }
        });
        
        projectCombo.setModel(new DefaultComboBoxModel());
        
        classCombo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                JLabel label = (JLabel)comp;
                label.setText(""+value); // NOI18N
                return comp;
            }
        });
        
        actionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ProxyAction act = getSelectedAction();
                if(act != null) {
                    List<RADComponent> list = actionManager.getBoundComponents(act);
                    DefaultListModel mod = new DefaultListModel();
                    for(Object o : list) {
                        mod.addElement(o);
                    }
                    boundComponentList.setModel(mod);
                }
            }
        });
        
        // clear the bound compnent list
        boundComponentList.setModel(new DefaultListModel());
        
        //renderer for the list of components attached to the selected action
        boundComponentList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                JLabel label = (JLabel) comp;
                if(value instanceof RADComponent) {
                    RADComponent rad = (RADComponent) value;
                    StringBuffer sb = new StringBuffer();
                    sb.append(rad.getName());
                    sb.append(", "); //NOI18N
                    if(rad.getFormModel() != null) {
                        sb.append(rad.getFormModel().getName());
                    }
                    label.setText(sb.toString());
                }
                return comp;
            }
        });
        
        filterTextfield.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }
        });
        
        
        // set up the actions
        deleteAction.putValue(Action.NAME,"Delete Action");
        deleteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE")); //NOI18N
        
        // do the first table load
        reloadTable();
        reloadClassesCombo();
        attachTopComponentsListener();
    }
    
    private final static String SHOW_ALL_CLASSES = "All Classes";
    
    private String filterClass = null;
    
    public GlobalActionPanel(FileObject fo) {
        this();
        actionManager = ActionManager.getActionManager(fo);
        actionManager.rescan();
        
        /* //jmarinacci: used by the TreeTableView.
           //I'm leaving it here in case we need to go back to it.
        JFrame frame = new JFrame("Test frame");
        ProxyActionNode[]  nds = new ProxyActionNode[4];
        for(int i=0; i<nds.length; i++) {
            nds[i] = new ProxyActionNode();
        }
         
        Node.Property[] props = new Node.Property[4];
        props[0] = new PrototypeProperty("methodName","Method Name", String.class);
        props[0].setValue("ComparableColumnTTV",Boolean.TRUE);
        props[1] = new PrototypeProperty("classname","Class Name", String.class);
        props[1].setValue("ComparableColumnTTV",Boolean.TRUE);
        props[2] = new PrototypeProperty("id","ID", String.class);
        props[2].setValue("ComparableColumnTTV",Boolean.TRUE);
        props[3] = new PrototypeProperty("task","Task", String.class);
        props[3].setValue("ComparableColumnTTV",Boolean.TRUE);
        TreeTableView ttv = new TreeTableView();
        ttv.setRootVisible(false);
        ttv.setProperties(props);
         
         
        List<ProxyAction> acts = am.getAllActions();
        JPanel panel = new ProxyActionManager(acts);
        panel.setLayout(new BorderLayout());
        panel.add(ttv,"Center");
        frame.setLayout(new BorderLayout());
        frame.add(panel,"Center");
        frame.pack();
        frame.setSize(500,500);
        frame.setVisible(true);*/
        
    }
    
    private void reloadClassesCombo() {
        Object item = classCombo.getSelectedItem();
        Collection<String> classes = actionManager.getAllClasses();
        List classesPlusAll = new ArrayList(classes.size()+1);
        classesPlusAll.add(0,SHOW_ALL_CLASSES);
        classesPlusAll.addAll(classes);
        classCombo.setModel(new DefaultComboBoxModel(classesPlusAll.toArray()));
        classCombo.setSelectedItem(item);
    }
    
    private void reloadProjectsCombo() {
        Set<Project> projects = ActionManager.getKnownProjects();
        if(projects.size() > 0) {
            projectCombo.setModel(new DefaultComboBoxModel(projects.toArray()));
            projectCombo.setSelectedItem(actionManager.getProject());
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        newActionButton = new javax.swing.JButton();
        editActionButton = new javax.swing.JButton();
        deleteActionButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        projectCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        classCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        filterTextfield = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        actionTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        boundComponentList = new javax.swing.JList();
        viewSourceButton = new javax.swing.JButton();

        newActionButton.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.newActionButton.text")); // NOI18N
        newActionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newActionButtonActionPerformed(evt);
            }
        });

        editActionButton.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.editActionButton.text")); // NOI18N
        editActionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionButtonActionPerformed(evt);
            }
        });

        deleteActionButton.setAction(getDeleteAction());
        deleteActionButton.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.deleteActionButton.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.jLabel4.text")); // NOI18N

        projectCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        projectCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectComboActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.jLabel1.text")); // NOI18N

        classCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        classCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classComboActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.jLabel2.text")); // NOI18N

        filterTextfield.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.filterTextfield.text")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(500);

        actionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollPane.setViewportView(actionTable);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.jLabel3.text")); // NOI18N

        boundComponentList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(boundComponentList);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jLabel3)
                .addContainerGap(40, Short.MAX_VALUE))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addContainerGap())
        );

        viewSourceButton.setText(org.openide.util.NbBundle.getMessage(GlobalActionPanel.class, "GlobalActionPanel.viewSourceButton.text")); // NOI18N
        viewSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSourceButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(projectCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(classCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 321, Short.MAX_VALUE)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(filterTextfield, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(newActionButton)
                .add(6, 6, 6)
                .add(editActionButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(viewSourceButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteActionButton)
                .addContainerGap(285, Short.MAX_VALUE))
            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(classCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(jLabel4)
                    .add(projectCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(filterTextfield, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(newActionButton)
                    .add(editActionButton)
                    .add(viewSourceButton)
                    .add(deleteActionButton)))
        );
    }// </editor-fold>//GEN-END:initComponents
    
private void viewSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSourceButtonActionPerformed
    // TODO add your handling code here:
    viewsourceSelectedAction();
}//GEN-LAST:event_viewSourceButtonActionPerformed

    private void projectComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectComboActionPerformed
        if(projectCombo.getSelectedItem() != null) {
            setSelectedProject((Project)projectCombo.getSelectedItem());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_projectComboActionPerformed
    
    private void editActionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionButtonActionPerformed
        editSelectedAction();
    }//GEN-LAST:event_editActionButtonActionPerformed
    
    
    private Action deleteAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            deleteSelectedAction();
        }
    };
    
    Action getDeleteAction() {
        return deleteAction;
    }
    
    private void editSelectedAction() {
        
        ProxyAction act = getSelectedAction();
        if(act == null) { return; }
        String defClassName = act.getClassname();
        ActionEditor editor = new ActionEditor(actionManager.getFileForClass(defClassName));
        editor.setValue(act);
        Component comp = editor.getCustomEditor();
        final DialogDescriptor dd = new DialogDescriptor(comp,"Edit Action Properties",true,null);
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.pack();
        dialog.setVisible(true);
        
        // only update things if the user clicked okay
        if(dd.getValue().equals(DialogDescriptor.OK_OPTION)) {
            try {
                editor.confirmChanges(); // this updates
                reloadTable();
            } catch (IllegalArgumentException ex) {
                ErrorManager.getDefault().notify(ex);
            }
        }
    }
    
    private void viewsourceSelectedAction() {
        ProxyAction act = getSelectedAction();
        if(act == null) return;
        actionManager.jumpToActionSource(act);
    }
    private ProxyAction getSelectedAction() {
        int row = actionTable.getSelectedRow();
        if(row < 0) {
            return null;
        }
        TableSorter sorter = (TableSorter) actionTable.getModel();
        row = sorter.modelIndex(row);
        FilteredTableModel filter = (FilteredTableModel) sorter.getTableModel();
        ActionTableModel model = (ActionTableModel) filter.getTableModel();
        ProxyAction act = model.getAction(row);
        return act;
    }
    
    private void deleteSelectedAction() {
        ProxyAction action = getSelectedAction();
        if(action == null) { return; }
        int retval = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete action: " + action.getId(),
                "Delete Action",JOptionPane.OK_CANCEL_OPTION);
        if(retval == JOptionPane.OK_OPTION) {
            actionManager.deleteAction(action);
            reloadTable();
        }
    }
    
    private void newActionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newActionButtonActionPerformed
        
        // get a file from the project
        FileObject fileInProject =  actionManager.getRoot();
        
        final ActionEditor editor = new ActionEditor(fileInProject, true);//actionManager.getFileForClass(defClassName));
        //editor.setValue(null);
        final ActionPropertyEditorPanel panel = (ActionPropertyEditorPanel) editor.getCustomEditor();
        panel.setMode(ActionPropertyEditorPanel.Mode.NewActionGlobal);
        
        //final CreateNewActionPanel panel = new CreateNewActionPanel(fileInProject);
        final DialogDescriptor dd = new DialogDescriptor(panel,"Create New Action");
        dd.setOptions(new String[] {"Cancel","Create"});
        dd.setValue("Create");
        dd.setModal(true);
        dd.setOptionsAlign(DialogDescriptor.BOTTOM_ALIGN);
        dd.setClosingOptions(new String[0]);
        dd.setLeaf(false);
        dd.setValue("Create");
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dd.setButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(dd.getValue().equals("Cancel") /*|| panel.validateFields()*/) {
                    dialog.setVisible(false);
                } else {
                    if(panel.canCreateNewAction()) {
                        dialog.setVisible(false);
                        editor.setSourceFile(panel.getSelectedSourceFile());
                        editor.createNewAction();
                    } else {
                        //josh: need to update this to handle all of the things that could
                        // go wrong
                        JOptionPane.showMessageDialog(dialog, "Method name cannot be empty");
                    }
                }
            }
        });
        //panel.setDialog(dialog);
        dialog.pack();
        dialog.setVisible(true);
        
        /*if(panel.isInputIsValid()) {
            ProxyAction act = new ProxyAction(panel.getSelectedClassName(),
                    panel.getMethodText());
            act.setTaskEnabled(panel.isAsynchronous());
            act.setAppWide(false); // joshy: hack. make it properly be app wide if in the app class
         
            actionManager.createActionMethod(act);
            actionManager.addNewAction(act);
            reloadTable();
        }*/
    }//GEN-LAST:event_newActionButtonActionPerformed
    
    private void classComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classComboActionPerformed
        if(classCombo.getSelectedItem() == SHOW_ALL_CLASSES) {
            filterClass = null;
            reloadTable();
        } else {
            filterClass = (String) classCombo.getSelectedItem();
            reloadTable();
        }
    }//GEN-LAST:event_classComboActionPerformed
    
    // just reload the table
    private void reloadTable() {
        if(filterClass == null) {
            realModel = new ActionTableModel(actionManager.getAllActions());
        } else {
            realModel = new ActionTableModel(actionManager.getActions(filterClass, false));
        }
        
        actionTable.setModel(new TableSorter(new FilteredTableModel(realModel),actionTable.getTableHeader()));
        filterTable();
        // set the icon and task widths
        actionTable.getColumnModel().getColumn(ActionTableModel.ICON_COLUMN).setPreferredWidth(30);
        actionTable.getColumnModel().getColumn(ActionTableModel.TASK_COLUMN).setPreferredWidth(30);
        // move around
        actionTable.getColumnModel().moveColumn(ActionTableModel.ICON_COLUMN,3);
        actionTable.getColumnModel().moveColumn(ActionTableModel.TASK_COLUMN,4);
        actionTable.getColumnModel().moveColumn(6,5);
        actionTable.getColumnModel().getColumn(6).setPreferredWidth(150);
    }
    
    // rescan for actions, reload the class combo, and call reloadTable();
    public void refresh() {
        /*
        // get the current active top component
        WindowManager wm = WindowManager.getDefault();
        TopComponent activeTC = wm.getRegistry().getActivated();
        if(activeTC==null) {
            return;
        }
         
        // get the file object of the current form
        FormEditorSupport fes = FormEditorSupport.getFormEditor(activeTC);
        System.out.println("form ed support = " + fes);
        if(fes == null) { return; }
        FileObject fo = fes.getFormDataObject().getFormFile();
        System.out.println("fo = " + fo);
        actionManager = ActionManager.getActionManager(fo);
         
        // watch for changes
        actionManager.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                reloadTable();
                reloadClassesCombo();
            }
        });
         */
        
        // rescan the actions
        if(true) {
            actionManager.rescan();
            reloadProjectsCombo();
            reloadClassesCombo();
            reloadTable();
        }
    }
    
    private void setSelectedProject(Project project) {
        actionManager = ActionManager.getActionManager(project);
        reloadClassesCombo();
        reloadTable();
    }
    
    private void filterTable() {
        FilteredTableModel fmodel = (FilteredTableModel) ((TableSorter)actionTable.getModel()).getTableModel();
        fmodel.setFilterString(filterTextfield.getText());
    }
    
    private void jumpToSelectedComponent() {
        RADComponent comp = (RADComponent) boundComponentList.getSelectedValue();
        if(comp != null) {
            FormModel formModel = comp.getFormModel();
            FileObject formFile = FormEditor.getFormDataObject(formModel).getPrimaryFile();
            //force the form to be open. it should already *be* open, however, since we have the form model
            try {
                FormDataObject formDataObject = (FormDataObject) FormDataObject.find(formFile);
                formDataObject.getFormEditor().loadForm();
                formDataObject.getFormEditor().openFormEditor(true);
            } catch (DataObjectNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable actionTable;
    private javax.swing.JList boundComponentList;
    private javax.swing.JComboBox classCombo;
    private javax.swing.JButton deleteActionButton;
    private javax.swing.JButton editActionButton;
    private javax.swing.JTextField filterTextfield;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton newActionButton;
    private javax.swing.JComboBox projectCombo;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JButton viewSourceButton;
    // End of variables declaration//GEN-END:variables
    
    private PropertyChangeListener topcompsListener;
    
    private PropertyChangeListener amListener;
    private void switchedToTopComponent(TopComponent active) {
        FormEditorSupport fes = FormEditorSupport.getFormEditor(active);
        if(fes == null) { return; }
        
        FileObject fo = fes.getFormDataObject().getFormFile();
        if(!AppFrameworkSupport.isFrameworkEnabledProject(fo)) {
            return;
        }
        
        if(amListener == null) {
            amListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    reloadTable();
                    reloadClassesCombo();
                }
            };
        }
        
        if(actionManager != null) {
            actionManager.removePropertyChangeListener(amListener);
            actionManager.removeActionChangedListener(actChangeListener);
        }
        actionManager = ActionManager.getActionManager(fo);
        if(actionManager != null) {
            actionManager.addPropertyChangeListener(amListener);
            actionManager.addActionChangedListener(actChangeListener);
            refresh();
        }
    }
    
    
    private void attachTopComponentsListener() {
        if (topcompsListener != null) {
            return;
        }
        
        topcompsListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                if (TopComponent.Registry.PROP_ACTIVATED.equals(
                        ev.getPropertyName())) {   // activated TopComponent has changed
                    TopComponent active = TopComponent.getRegistry().getActivated();
                    //p("the top component changed");
                    switchedToTopComponent(active);
                    /*
                    if (getSelectedElementType(active) != -1) { // it is our multiview
                        FormEditorSupport fes = getFormEditor(active);
                        if (fes != null) {
                            fes.multiviewTC = (CloneableTopComponent) active;
                            FormDesigner designer = (FormDesigner)active.getClientProperty("formDesigner"); // NOI18N
                            if (designer != null)
                                fes.getFormEditor().setFormDesigner(designer);
                        }
                    }
                    checkFormGroupVisibility();                    */
                } else if (TopComponent.Registry.PROP_OPENED.equals(
                        ev.getPropertyName())) {   // set of opened TopComponents has changed - hasn't some
                    // of our views been closed?
                    //p("set of top components changed ");
                    /*
                    CloneableTopComponent closedTC = null;
                    Set oldSet = (Set) ev.getOldValue();
                    Set newSet = (Set) ev.getNewValue();
                    if (newSet.size() < oldSet.size()) {
                        Iterator it = oldSet.iterator();
                        while (it.hasNext()) {
                            Object o = it.next();
                            if (!newSet.contains(o)) {
                                if (o instanceof CloneableTopComponent)
                                    closedTC = (CloneableTopComponent) o;
                                break;
                            }
                        }
                    }
                    if (getSelectedElementType(closedTC) != -1) { // it is our multiview
                        FormEditorSupport fes = getFormEditor(closedTC);
                        if (fes != null)
                            fes.multiViewClosed(closedTC);
                    }
                    TopComponent active = TopComponent.getRegistry().getActivated();
                    if (active!=null && getSelectedElementType(active) != -1) { // it is our multiview
                        FormEditorSupport fes = getFormEditor(active);
                        if (fes != null) {
                            fes.updateMVTCDisplayName();
                        }
                    }                    */
                }
            }
        };
        
        TopComponent.getRegistry().addPropertyChangeListener(topcompsListener);
        
        // listen for when projects close
        OpenProjects.getDefault().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Project[] open = OpenProjects.getDefault().getOpenProjects();
                if(ActionManager.clearClosedProjects(open)) {
                    reloadProjectsCombo();
                }
            }
        });
        
    }
    
    private void detachTopComponentsListener() {
        if (topcompsListener != null) {
            TopComponent.getRegistry()
                    .removePropertyChangeListener(topcompsListener);
            topcompsListener = null;
            
            TopComponentGroup group = WindowManager.getDefault()
                    .findTopComponentGroup("form"); // NOI18N
            if (group != null)
                group.close();
        }
    }
    
    
}

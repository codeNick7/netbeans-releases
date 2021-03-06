/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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
 */
package org.netbeans.modules.docker.ui.node;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import org.netbeans.modules.docker.api.ExposedPort;
import org.netbeans.modules.docker.api.PortMapping;
import org.openide.util.NbBundle;

/**
 *
 * @author dsergeyev
 */
public class ViewPortBindingsPanel extends javax.swing.JPanel {

    /**
     * Creates new form ViewPortBindingsPanel
     */
    public ViewPortBindingsPanel(List<PortMapping> bindings) {
        initComponents();
        setModel(bindings);
    }
    
    @NbBundle.Messages({
        "LBL_Type=Type",
        "LBL_Port=Port",
        "LBL_HostPort=Host Port",
        "LBL_HostAddress=Host Address"
    })
    private void setModel(List<PortMapping> mappings) {
        int rowNumber = mappings.size();
        mappingsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [rowNumber][],
            new String [] {
                Bundle.LBL_Type(), Bundle.LBL_Port(), Bundle.LBL_HostPort(), Bundle.LBL_HostAddress()
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        DefaultTableCellRenderer integerRenderer = (DefaultTableCellRenderer) mappingsTable.getDefaultRenderer(Integer.class);
        integerRenderer.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer stringRenderer = (DefaultTableCellRenderer) mappingsTable.getDefaultRenderer(String.class);
        stringRenderer.setHorizontalAlignment(JLabel.CENTER);
        int row = 0;
        TableModel model = mappingsTable.getModel();
        for (PortMapping mapping : mappings) {
            String type = mapping.getType().equals(ExposedPort.Type.TCP) ? "tcp" : "udp";
            model.setValueAt(type, row, 0);
            model.setValueAt(mapping.getPort(), row, 1);
            model.setValueAt(mapping.getHostPort(), row, 2);
            model.setValueAt(mapping.getHostAddress(), row, 3);
            row++;
        }
        mappingsTable.setRowSelectionAllowed(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        mappingsTable = new javax.swing.JTable();

        jScrollPane1.setViewportView(mappingsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable mappingsTable;
    // End of variables declaration//GEN-END:variables
}

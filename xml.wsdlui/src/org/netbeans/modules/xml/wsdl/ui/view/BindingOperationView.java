/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
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
 */

/*
 * BindingOperationView.java
 *
 * Created on July 28, 2006, 5:03 PM
 */
package org.netbeans.modules.xml.wsdl.ui.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;

import org.netbeans.modules.xml.wsdl.model.Operation;
import org.netbeans.modules.xml.wsdl.model.PortType;
import org.netbeans.modules.xml.wsdl.ui.netbeans.module.Utility;
import org.openide.util.NbBundle;

/**
 *
 * @author  skini
 */
public class BindingOperationView extends javax.swing.JPanel {

    public static final String ENABLE_OK = "ENABLE_OK";
    private Operation mSelectedOperation;
    private Collection<Operation> mOperations;
    private Object[] mSelectedOperations;

    /**
     * Creates new form BindingOperationView
     */
    public BindingOperationView(Collection<Operation> operations) {
        mOperations = operations;
        initComponents();
        validateAll();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        commonMessagePanel1 = new org.netbeans.modules.xml.wsdl.bindingsupport.common.CommonMessagePanel();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setModel(new DefaultComboBoxModel(getAllOperationNames()));
        jList1.setToolTipText(org.openide.util.NbBundle.getMessage(BindingOperationView.class, "BindingOperationView.jList1.toolTipText")); // NOI18N
        jList1.setName("jList1"); // NOI18N
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);
        jList1.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(BindingOperationView.class, "BindingOperationView.jList1.toolTipText")); // NOI18N

        commonMessagePanel1.setName("commonMessagePanel1"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, commonMessagePanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(commonMessagePanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        JList list = (JList) evt.getSource();
        if (!list.getValueIsAdjusting()) {
            mSelectedOperations = jList1.getSelectedValues();
            validateAll();
        }
    }//GEN-LAST:event_jList1ValueChanged

    public Operation getSelectedOperation() {
        return this.mSelectedOperation;
    }

    public Operation[] getSelectedOperations() {
        Operation[] operations = new Operation[mSelectedOperations.length];
        int i = 0;
        for (Object o : mSelectedOperations) {
            OperationDelegate od = (OperationDelegate) o;
            operations[i] = od.getOperation();
            i++;
        }
        return operations;
    }

    private Vector getAllOperationNames() {

        if (mOperations != null) {
            PortType portType = (PortType) mOperations.iterator().next().getParent();
            List<Operation> overloadedOperations = Utility.getOverloadedOperations(portType);
            Set<String> overloadedOperationsSignatures = new HashSet<String>();
            boolean ambiguousOverloadingerror = false;
            for (Operation overloadedOperation : overloadedOperations) {
                String opSig = Utility.getOperationSignature(overloadedOperation);
                if (!overloadedOperationsSignatures.contains(opSig)) {
                    overloadedOperationsSignatures.add(opSig);
                } else {
                    ambiguousOverloadingerror = true;
                    break;
                }
            }
            
            Vector<OperationDelegate> listData = new Vector<OperationDelegate>(mOperations.size());
            for (Operation operation : mOperations) {
                boolean isOverloaded = overloadedOperations.contains(operation);
                OperationDelegate opD = new OperationDelegate(operation, isOverloaded);
                listData.add(opD);
            }
            
            if (ambiguousOverloadingerror) {
                commonMessagePanel1.setErrorMessage(NbBundle.getMessage(BindingOperationView.class, "MSG_ImproperlyOverloadedOperations"));
            }
            return listData;
        }

        return null;
    }

    private void validateAll() {
        boolean valid = isStateValid();
        firePropertyChange(ENABLE_OK, !valid, valid);
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        validateAll();
    }
    
    public boolean isStateValid() {
        return commonMessagePanel1.isStateValid() && mSelectedOperations != null && mSelectedOperations.length > 0;
    }
    
    private class OperationDelegate {

        private Operation mOperation;
        private boolean isOverLoaded;

        OperationDelegate(Operation operation, boolean isOverloaded) {
            this.mOperation = operation;
            this.isOverLoaded = isOverloaded;
        }

        public Operation getOperation() {
            return this.mOperation;
        }

        @Override
        public String toString() {
            if (isOverLoaded) {
                return Utility.getOperationSignature(mOperation);
            }
            return this.mOperation.getName();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.netbeans.modules.xml.wsdl.bindingsupport.common.CommonMessagePanel commonMessagePanel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

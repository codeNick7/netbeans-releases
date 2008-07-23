/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
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

package org.netbeans.modules.groovy.support.customizer;

import org.netbeans.modules.groovy.support.GroovyProjectExtender;

/**
 *
 * @author Martin Adamek
 */
public class GroovyCustomizerPanel extends javax.swing.JPanel {

    public GroovyCustomizerPanel(GroovyProjectExtender extender) {
        initComponents();
        if (extender == null) {
            enableGroovyCheckBox.setSelected(false);
            enableGroovyCheckBox.setEnabled(false);
        } else if (extender.isGroovyEnabled()) {
            enableGroovyCheckBox.setSelected(true);
            enableGroovyCheckBox.setEnabled(false);
        }
    }

    public boolean isEnablingCheckboxSelected() {
        return enableGroovyCheckBox.isSelected();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        enableGroovyCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        enableGroovyCheckBox.setText(org.openide.util.NbBundle.getMessage(GroovyCustomizerPanel.class, "GroovyCustomizerPanel.enableGroovyCheckBox.text")); // NOI18N
        enableGroovyCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableGroovyCheckBoxActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        jTextArea1.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText(org.openide.util.NbBundle.getMessage(GroovyCustomizerPanel.class, "GroovyCustomizerPanel.jTextArea1.text")); // NOI18N
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setBorder(null);
        jScrollPane1.setViewportView(jTextArea1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(enableGroovyCheckBox)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(enableGroovyCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void enableGroovyCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableGroovyCheckBoxActionPerformed
    if (enableGroovyCheckBox.isSelected()) {
        enableGroovyCheckBox.setEnabled(false);
    }
}//GEN-LAST:event_enableGroovyCheckBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox enableGroovyCheckBox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}

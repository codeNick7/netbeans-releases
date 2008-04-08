/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.netbeans.modules.debugger.jpda.ui.debugging;

import java.awt.Dimension;

/**
 *
 * @author  Dan
 */
public class InfoPanel extends javax.swing.JPanel {

    private static int UNIT_HEIGHT = 30;
    
    /** Creates new form InfoPanel */
    public InfoPanel() {
        initComponents();
        
        hitsPanel.setPreferredSize(new Dimension(0, UNIT_HEIGHT));
        deadlocksPanel.setPreferredSize(new Dimension(0, UNIT_HEIGHT));
        filterPanel.setPreferredSize(new Dimension(0, UNIT_HEIGHT));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hitsPanel = new javax.swing.JPanel();
        deadlocksPanel = new javax.swing.JPanel();
        filterPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        hitsPanel.setBackground(new java.awt.Color(255, 255, 163));
        hitsPanel.setPreferredSize(new java.awt.Dimension(0, 20));
        hitsPanel.setLayout(new java.awt.BorderLayout());
        add(hitsPanel, java.awt.BorderLayout.NORTH);

        deadlocksPanel.setBackground(new java.awt.Color(255, 223, 198));
        deadlocksPanel.setPreferredSize(new java.awt.Dimension(0, 20));
        deadlocksPanel.setLayout(new java.awt.BorderLayout());
        add(deadlocksPanel, java.awt.BorderLayout.CENTER);

        filterPanel.setBackground(new java.awt.Color(255, 209, 223));
        filterPanel.setPreferredSize(new java.awt.Dimension(0, 20));
        filterPanel.setLayout(new java.awt.BorderLayout());
        add(filterPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel deadlocksPanel;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel hitsPanel;
    // End of variables declaration//GEN-END:variables

}

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
 *
 */
package org.netbeans.modules.vmd.midp.palette.wizard;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.mobility.project.J2MEProject;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * @author David Kaspar
 */
public final class AddToPaletteVisualPanel1 extends JPanel {

    private AddToPaletteWizardPanel1 wizardPanel;

    public AddToPaletteVisualPanel1 (AddToPaletteWizardPanel1 wizardPanel) {
        this.wizardPanel = wizardPanel;
        initComponents();
        projectCombo.setRenderer (new ProjectListCellRenderer ());
    }

    public String getName() {
        return "Select Project";
    }

    public Project getActiveProject () {
        return (Project) projectCombo.getSelectedItem();
    }

    public void reload (Project project) {
        Project[] projects = OpenProjects.getDefault ().getOpenProjects ();
        Vector projectsVector = new Vector ();
        for (Project prj : projects)
            if (isMobileProject (prj))
                projectsVector.add (prj);
        projectCombo.setModel (new DefaultComboBoxModel (projectsVector));
        if (project == null) {
            Project prj = OpenProjects.getDefault ().getMainProject ();
            if (isMobileProject (prj))
                project = prj;
        }
        if (project == null  &&  projects.length > 0)
            project = projects[0];
        projectCombo.setSelectedItem (project);
    }

    private boolean isMobileProject (Project prj) {
        // TODO - remove implementation dependency
        return prj instanceof J2MEProject; // "J2MEProject".equals (prj.getClass ().getSimpleName ()); // NOI18N
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectCombo = new javax.swing.JComboBox();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();

        projectCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectComboActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Select Project:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(projectCombo, 0, 376, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(projectCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(243, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void projectComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectComboActionPerformed
    wizardPanel.fireChangeEvent();
}//GEN-LAST:event_projectComboActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox projectCombo;
    // End of variables declaration//GEN-END:variables

    public static class ProjectListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value == null)
                return super.getListCellRendererComponent (list, value, index, isSelected, cellHasFocus);
            ProjectInformation info = ((Project) value).getLookup ().lookup (ProjectInformation.class);
            super.getListCellRendererComponent (list, info != null ? info.getDisplayName () : null, index, isSelected, cellHasFocus);
            if (info != null)
                setIcon (info.getIcon ());
            return this;
        }

    }

}


/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2003 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.java.j2seproject.ui.customizer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.netbeans.api.java.platform.JavaPlatform;
import org.netbeans.api.java.platform.JavaPlatformManager;
import org.netbeans.api.java.platform.Specification;
import org.netbeans.api.project.ant.AntArtifact;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.PropertyUtils;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.ErrorManager;
import org.openide.awt.Mnemonics;

import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 *
 * @author  phrebejk
 */
public class CustomizerCompile extends JPanel implements J2SECustomizer.Panel, HelpCtx.Provider {

    // Helper for storing properties
    private J2SEProjectProperties j2seProperties;
    private VisualPropertySupport vps;
    private VisualClasspathSupport vcs;

    /** Creates new form CustomizerCompile */
    public CustomizerCompile( J2SEProjectProperties j2seProperties ) {
        initComponents();
        this.j2seProperties = j2seProperties;
        vps = new VisualPropertySupport( j2seProperties );
        vcs = new VisualClasspathSupport(
            j2seProperties.getProject(),
            jListClasspath,
            jButtonAddJar,
            jButtonAddLibrary,
            jButtonAddArtifact,
            jButtonEdit,
            jButtonRemove,
            jButtonMoveUp,
            jButtonMoveDown );
        
    }


    public void initValues() {

        vps.register( jCheckBoxDeprecation, J2SEProjectProperties.JAVAC_DEPRECATION );
        vps.register( jCheckBoxDebugInfo, J2SEProjectProperties.JAVAC_DEBUG );                
        vps.register( vcs, J2SEProjectProperties.JAVAC_CLASSPATH );

        jButtonEdit.setVisible( false );

    }

    public HelpCtx getHelpCtx() {
        return new HelpCtx( CustomizerCompile.class );
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jCheckBoxDebugInfo = new javax.swing.JCheckBox();
        jCheckBoxDeprecation = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabelClasspath = new javax.swing.JLabel();
        jScrollClasspath = new javax.swing.JScrollPane();
        jListClasspath = new javax.swing.JList();
        jButtonAddArtifact = new javax.swing.JButton();
        jButtonAddLibrary = new javax.swing.JButton();
        jButtonAddJar = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jButtonMoveUp = new javax.swing.JButton();
        jButtonMoveDown = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EtchedBorder(), new javax.swing.border.EmptyBorder(new java.awt.Insets(12, 12, 12, 12))));
        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxDebugInfo, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Compiler_DebugInfo_JCheckBox"));
        jCheckBoxDebugInfo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(jCheckBoxDebugInfo, gridBagConstraints);
        jCheckBoxDebugInfo.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jCheckBoxDebugInfo"));

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxDeprecation, java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seproject/ui/customizer/Bundle").getString("LBL_CustomizeCompile_Compiler_Deprecation_JCheckBox"));
        jCheckBoxDeprecation.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        add(jCheckBoxDeprecation, gridBagConstraints);
        jCheckBoxDeprecation.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jCheckBoxDeprecation"));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelClasspath.setLabelFor(jListClasspath);
        org.openide.awt.Mnemonics.setLocalizedText(jLabelClasspath, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_JLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        jPanel2.add(jLabelClasspath, gridBagConstraints);
        jLabelClasspath.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jLabelClasspath"));

        jScrollClasspath.setViewportView(jListClasspath);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        jPanel2.add(jScrollClasspath, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddArtifact, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_AddArtifact_JButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel2.add(jButtonAddArtifact, gridBagConstraints);
        jButtonAddArtifact.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jButtonAddArtifact"));

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddLibrary, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_AddLibrary_JButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel2.add(jButtonAddLibrary, gridBagConstraints);
        jButtonAddLibrary.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jButtonAddLibrary"));

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddJar, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_AddJar_JButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        jPanel2.add(jButtonAddJar, gridBagConstraints);
        jButtonAddJar.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jButtonAddJar"));

        org.openide.awt.Mnemonics.setLocalizedText(jButtonEdit, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_Edit_JButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        jPanel2.add(jButtonEdit, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRemove, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_Remove_JButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        jPanel2.add(jButtonRemove, gridBagConstraints);
        jButtonRemove.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jButtonRemove"));

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveUp, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_MoveUp_JButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel2.add(jButtonMoveUp, gridBagConstraints);
        jButtonMoveUp.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jButtonMoveUp"));

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveDown, org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "LBL_CustomizeCompile_Classpath_MoveDown_JButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        jPanel2.add(jButtonMoveDown, gridBagConstraints);
        jButtonMoveDown.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerCompile.class, "ACSD_CustomizerCompile_jButtonMoveDown"));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddArtifact;
    private javax.swing.JButton jButtonAddJar;
    private javax.swing.JButton jButtonAddLibrary;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonMoveDown;
    private javax.swing.JButton jButtonMoveUp;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JCheckBox jCheckBoxDebugInfo;
    private javax.swing.JCheckBox jCheckBoxDeprecation;
    private javax.swing.JLabel jLabelClasspath;
    private javax.swing.JList jListClasspath;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollClasspath;
    // End of variables declaration//GEN-END:variables



}

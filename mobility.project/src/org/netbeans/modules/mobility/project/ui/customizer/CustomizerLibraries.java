/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

/*
 * Customizer.java
 *
 * Created on 23.Mar 2004, 11:31
 */
package org.netbeans.modules.mobility.project.ui.customizer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.netbeans.api.mobility.project.ui.customizer.ProjectProperties;
import org.netbeans.spi.mobility.project.ui.customizer.CustomizerPanel;
import org.netbeans.spi.mobility.project.ui.customizer.support.VisualPropertySupport;
import org.netbeans.spi.mobility.project.ui.customizer.VisualPropertyGroup;
import org.netbeans.modules.mobility.project.DefaultPropertiesDescriptor;
import org.openide.util.NbBundle;

/**
 *
 * @author  Adam Sotona
 */
public class CustomizerLibraries extends JPanel implements CustomizerPanel, VisualPropertyGroup {
    
    private static final String[] PROPERTY_GROUP = new String[] {DefaultPropertiesDescriptor.LIBS_CLASSPATH, DefaultPropertiesDescriptor.EXTRA_CLASSPATH};
    
    private VisualPropertySupport vps;
    final private VisualClasspathSupport vcs;
    private String configuration;
    private ProjectProperties props;
    
    /** Creates new form CustomizerConfigs */
    public CustomizerLibraries() {
        initComponents();
        initAccessibility();
        vcs = new VisualClasspathSupport(
                jTableClasspath,
                jButtonAddJar,
                jButtonAddFolder,
                jButtonAddLibrary,
                "j2se", //NOI18N
                jButtonAddArtifact,
                jButtonRemove,
                jButtonMoveUp,
                jButtonMoveDown );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        defaultCheck = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollClasspath = new javax.swing.JScrollPane();
        jTableClasspath = new javax.swing.JTable();
        jButtonAddArtifact = new javax.swing.JButton();
        jButtonAddLibrary = new javax.swing.JButton();
        jButtonAddJar = new javax.swing.JButton();
        jButtonAddFolder = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jButtonMoveUp = new javax.swing.JButton();
        jButtonMoveDown = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(defaultCheck, NbBundle.getMessage(CustomizerLibraries.class, "LBL_Use_Default")); // NOI18N
        defaultCheck.setMargin(new java.awt.Insets(0, 0, 0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(defaultCheck, gridBagConstraints);
        defaultCheck.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_UseDefault")); // NOI18N

        jLabel1.setLabelFor(jTableClasspath);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Libraries")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(jLabel1, gridBagConstraints);

        jScrollClasspath.setViewportView(jTableClasspath);
        jTableClasspath.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_Libs")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 11);
        add(jScrollClasspath, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddArtifact, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Add_Project")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 11, 0);
        add(jButtonAddArtifact, gridBagConstraints);
        jButtonAddArtifact.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_AddProject")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddLibrary, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Add_Library")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 0);
        add(jButtonAddLibrary, gridBagConstraints);
        jButtonAddLibrary.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_AddLib")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddJar, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Add_Jar")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 0);
        add(jButtonAddJar, gridBagConstraints);
        jButtonAddJar.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_Jar")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddFolder, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Add_Folder")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 23, 0);
        add(jButtonAddFolder, gridBagConstraints);
        jButtonAddFolder.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_Folder")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonRemove, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Remove")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 23, 0);
        add(jButtonRemove, gridBagConstraints);
        jButtonRemove.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_Remove")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveUp, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Move_Up")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 0);
        add(jButtonMoveUp, gridBagConstraints);
        jButtonMoveUp.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_MoveUp")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonMoveDown, NbBundle.getMessage(CustomizerLibraries.class, "LBL_CustLibs_Move_Down")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        add(jButtonMoveDown, gridBagConstraints);
        jButtonMoveDown.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs_MoveDown")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents
    
    private void initAccessibility() {
        getAccessibleContext().setAccessibleName(NbBundle.getMessage(CustomizerLibraries.class, "ACSN_CustLibs"));
        getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(CustomizerLibraries.class, "ACSD_CustLibs"));
    }
    
    public void initValues(ProjectProperties props, String configuration) {
        this.vps = VisualPropertySupport.getDefault(props);
        this.props = props;
        this.configuration = configuration;
        vcs.setProperties(props);
        vps.register(defaultCheck, configuration, this);
    }
    
    public String[] getGroupPropertyNames() {
        return PROPERTY_GROUP;
    }
    
    public void initGroupValues(final boolean useDefault) {
        vcs.setPropertyNames(null, null);
        String libsClasspath = vps.translatePropertyName(configuration, DefaultPropertiesDescriptor.LIBS_CLASSPATH, useDefault);
        String extraClasspath = vps.translatePropertyName(configuration, DefaultPropertiesDescriptor.EXTRA_CLASSPATH, useDefault);
        List<VisualClassPathItem> value = (List<VisualClassPathItem>)props.get(libsClasspath);
        Set<VisualClassPathItem> extra = new HashSet((List<VisualClassPathItem>)props.get(extraClasspath));
        Iterator<VisualClassPathItem> it = value.iterator();
        while (it.hasNext()) {
            VisualClassPathItem vcpi = it.next();
            if (extra.contains(vcpi)) vcpi.setExtra(true);
        }
        vcs.setVisualClassPathItems( value );
        vcs.setPropertyNames(libsClasspath, extraClasspath);
        vcs.setEnabled(!useDefault);
        jTableClasspath.setBackground(UIManager.getDefaults().getColor(useDefault ?  "TextField.inactiveBackground" : "List.background")); //NOI18N
        jLabel1.setEnabled(!useDefault);
    }
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox defaultCheck;
    private javax.swing.JButton jButtonAddArtifact;
    private javax.swing.JButton jButtonAddFolder;
    private javax.swing.JButton jButtonAddJar;
    private javax.swing.JButton jButtonAddLibrary;
    private javax.swing.JButton jButtonMoveDown;
    private javax.swing.JButton jButtonMoveUp;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollClasspath;
    private javax.swing.JTable jTableClasspath;
    // End of variables declaration//GEN-END:variables
    
}

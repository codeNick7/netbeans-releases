/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
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

package org.netbeans.modules.ruby.merbproject.ui.wizards;

import javax.swing.JPanel;
import org.netbeans.modules.ruby.merbproject.ui.wizards.NewMerbProjectWizardIterator.Type;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.NbBundle;

/**
 * First panel in the NewProject wizard. Used for filling in name, and directory
 * of the project.
 */
public final class PanelConfigureProjectVisual extends JPanel {

    private final SettingsPanel projectLocationPanel;
    private final PanelOptionsVisual optionsPanel;
    private final Type type;
    
    PanelConfigureProjectVisual(PanelConfigureProject panel, Type type) {
        initComponents();                
        this.type = type;
        setName(NbBundle.getMessage(PanelConfigureProjectVisual.class,"TXT_NameAndLoc")); // NOI18N
        
        switch (type) {
            case APPLICATION:
                projectLocationPanel = new PanelProjectLocationVisual(panel, type);
                putClientProperty("NewProjectWizard_Title", NbBundle.getMessage(PanelConfigureProjectVisual.class, "TXT_NewJavaApp")); // NOI18N
                jSeparator1.setVisible(true);
                getAccessibleContext().setAccessibleName(NbBundle.getMessage(PanelConfigureProjectVisual.class, "TXT_NewJavaApp")); // NOI18N
                getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(PanelConfigureProjectVisual.class, "ACSD_NewJavaApp")); // NOI18N
                break;
            case EXISTING:
                projectLocationPanel = new PanelProjectLocationExtSrc(panel);
                jSeparator1.setVisible(true);
                putClientProperty("NewProjectWizard_Title", NbBundle.getMessage(PanelConfigureProjectVisual.class, "TXT_JavaExtSourcesProjectLocation")); // NOI18N
                getAccessibleContext().setAccessibleName(NbBundle.getMessage(PanelConfigureProjectVisual.class, "TXT_JavaExtSourcesProjectLocation")); // NOI18N
                getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(PanelConfigureProjectVisual.class, "ACSD_JavaExtSourcesProjectLocation")); // NOI18N
                break;
            default:
                throw new IllegalStateException("unknown type: " + type);
        }
        locationContainer.add( projectLocationPanel, java.awt.BorderLayout.CENTER );
        optionsPanel = new PanelOptionsVisual( panel, type );
        projectLocationPanel.addPropertyChangeListener(optionsPanel);
        optionsContainer.add( optionsPanel, java.awt.BorderLayout.CENTER );
    }
    
    boolean valid( WizardDescriptor wizardDescriptor ) {
        wizardDescriptor.putProperty( WizardDescriptor.PROP_ERROR_MESSAGE, "" ); //NOI18N
        return projectLocationPanel.valid( wizardDescriptor ) && optionsPanel.valid(wizardDescriptor);
    }
    
    void read(WizardDescriptor d) {
        Type lastType = (Type) d.getProperty("ruby-wizard-type");  //NOI18N        
        if (lastType == null || lastType != this.type) {
            //bugfix #46387 The type of project changed, reset values to defaults
            d.putProperty("name", null); // NOI18N
            d.putProperty("projdir", null); // NOI18N
            d.putProperty("platform", null); // NOI18N
        }
        projectLocationPanel.read (d);
        optionsPanel.read (d);
    }
    
    void store( WizardDescriptor d ) {
        d.putProperty("ruby-wizard-type", this.type);   //NOI18N
        projectLocationPanel.store( d );
        optionsPanel.store( d );        
    }
    
    void validate (WizardDescriptor d) throws WizardValidationException {
        projectLocationPanel.validate (d);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        locationContainer = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        optionsContainer = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        locationContainer.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(locationContainer, gridBagConstraints);
        locationContainer.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getBundle(PanelConfigureProjectVisual.class).getString("ACSN_locationContainer"));
        locationContainer.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getBundle(PanelConfigureProjectVisual.class).getString("ACSD_locationContainer"));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 0);
        add(jSeparator1, gridBagConstraints);

        optionsContainer.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(optionsContainer, gridBagConstraints);
        optionsContainer.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getBundle(PanelConfigureProjectVisual.class).getString("ACSN_optionsContainer"));
        optionsContainer.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getBundle(PanelConfigureProjectVisual.class).getString("ACSD_optionsContainer"));

    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel locationContainer;
    private javax.swing.JPanel optionsContainer;
    // End of variables declaration//GEN-END:variables

    
}

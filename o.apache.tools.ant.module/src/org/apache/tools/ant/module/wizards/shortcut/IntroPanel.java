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

package org.apache.tools.ant.module.wizards.shortcut;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

final class IntroPanel extends javax.swing.JPanel {

    private IntroWizardPanel wiz;

    /** Create the wizard panel component and set up some basic properties. */
    public IntroPanel (IntroWizardPanel wiz) {
        this.wiz = wiz;
        initComponents ();
        initAccessibility();
        // Provide a name in the title bar.
        setName (NbBundle.getMessage (IntroPanel.class, "IP_LBL_cfg_basic_opts"));
    }

    // --- VISUAL DESIGN OF PANEL ---
    
    @Override
    public void requestFocus () {
        super.requestFocus ();
        customizeCheck.requestFocus ();
    }

    
    private void initAccessibility () {    
        this.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(IntroPanel.class, "IP_TEXT_select_how_to_install_shortcut"));
        menuCheck.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(IntroPanel.class, "ACS_IP_LBL_add_menu_item"));
        toolbarCheck.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(IntroPanel.class, "ACS_IP_LBL_add_toolbar_button"));
        keyboardCheck.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(IntroPanel.class, "ACS_IP_LBL_add_kbd_shortcut"));        
        customizeCheck.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(IntroPanel.class, "ACS_IP_LBL_cust_code_checkbox"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hintsArea = new javax.swing.JTextArea();
        menuCheck = new javax.swing.JCheckBox();
        toolbarCheck = new javax.swing.JCheckBox();
        keyboardCheck = new javax.swing.JCheckBox();
        customizeCheck = new javax.swing.JCheckBox();

        hintsArea.setEditable(false);
        hintsArea.setFont(javax.swing.UIManager.getFont ("Label.font"));
        hintsArea.setLineWrap(true);
        hintsArea.setText(NbBundle.getMessage(IntroPanel.class, "IP_TEXT_select_how_to_install_shortcut")); // NOI18N
        hintsArea.setWrapStyleWord(true);
        hintsArea.setDisabledTextColor(javax.swing.UIManager.getColor ("Label.foreground"));
        hintsArea.setEnabled(false);
        hintsArea.setOpaque(false);

        org.openide.awt.Mnemonics.setLocalizedText(menuCheck, NbBundle.getMessage(IntroPanel.class, "IP_LBL_add_menu_item")); // NOI18N
        menuCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                someCheckboxClicked(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(toolbarCheck, NbBundle.getMessage(IntroPanel.class, "IP_LBL_add_toolbar_button")); // NOI18N
        toolbarCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                someCheckboxClicked(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(keyboardCheck, NbBundle.getMessage(IntroPanel.class, "IP_LBL_add_kbd_shortcut")); // NOI18N
        keyboardCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                someCheckboxClicked(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(customizeCheck, NbBundle.getMessage(IntroPanel.class, "IP_LBL_cust_code_checkbox")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hintsArea)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menuCheck)
                    .addComponent(toolbarCheck)
                    .addComponent(keyboardCheck)
                    .addComponent(customizeCheck))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(hintsArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(menuCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolbarCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keyboardCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customizeCheck)
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void someCheckboxClicked (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_someCheckboxClicked
        wiz.fireChangeEvent ();
    }//GEN-LAST:event_someCheckboxClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox customizeCheck;
    private javax.swing.JTextArea hintsArea;
    private javax.swing.JCheckBox keyboardCheck;
    private javax.swing.JCheckBox menuCheck;
    private javax.swing.JCheckBox toolbarCheck;
    // End of variables declaration//GEN-END:variables

    public static class IntroWizardPanel implements WizardDescriptor.Panel<ShortcutWizard> {
    
        private IntroPanel panel = null;
        private ShortcutWizard wiz = null;
        
        public void initialize(ShortcutWizard wiz) {
            this.wiz = wiz;
        }
        
        public Component getComponent () {
            return getPanel();
        }
        
        private IntroPanel getPanel() {
            if (panel == null) {
                panel = new IntroPanel(this);
            }
            return panel;
        }

        public HelpCtx getHelp () {
            return HelpCtx.DEFAULT_HELP;
        }

        public boolean isValid () {
            return getPanel().menuCheck.isSelected () ||
                   getPanel().toolbarCheck.isSelected () ||
                   getPanel().keyboardCheck.isSelected ();
        }

        private final ChangeSupport cs = new ChangeSupport(this);
        public final void addChangeListener (ChangeListener l) {
            cs.addChangeListener(l);
        }
        public final void removeChangeListener (ChangeListener l) {
            cs.removeChangeListener(l);
        }
        protected final void fireChangeEvent () {
            // #44409: need to update the PROP_SHOW_* flags before storeSettings is called,
            // because then it will be too late (iterator will already have progressed):
            // XXX workaround should no longer be necessary...
            storeSettings(wiz);
            cs.fireChange();
        }

        public void readSettings(ShortcutWizard wiz) {
            getPanel().customizeCheck.setSelected(flag(ShortcutWizard.PROP_SHOW_CUST));
            getPanel().menuCheck.setSelected(flag(ShortcutWizard.PROP_SHOW_MENU));
            getPanel().toolbarCheck.setSelected(flag(ShortcutWizard.PROP_SHOW_TOOL));
            getPanel().keyboardCheck.setSelected(flag(ShortcutWizard.PROP_SHOW_KEYB));
        }
        private boolean flag(String prop) {
            Boolean val = (Boolean) wiz.getProperty(ShortcutWizard.PROP_SHOW_KEYB);
            return val != null ? val : false;
        }
        public void storeSettings(ShortcutWizard wiz) {
            wiz.putProperty(ShortcutWizard.PROP_SHOW_CUST, getPanel().customizeCheck.isSelected() ? Boolean.TRUE : Boolean.FALSE);
            wiz.putProperty(ShortcutWizard.PROP_SHOW_MENU, getPanel().menuCheck.isSelected() ? Boolean.TRUE : Boolean.FALSE);
            wiz.putProperty(ShortcutWizard.PROP_SHOW_TOOL, getPanel().toolbarCheck.isSelected() ? Boolean.TRUE : Boolean.FALSE);
            wiz.putProperty(ShortcutWizard.PROP_SHOW_KEYB, getPanel().keyboardCheck.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        }
    }

}

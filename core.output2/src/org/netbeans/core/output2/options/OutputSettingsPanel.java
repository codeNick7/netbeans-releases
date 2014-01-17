/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
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
 *
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.netbeans.core.output2.options;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.IOException;
import java.io.Reader;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.core.output2.Controller;
import org.netbeans.core.output2.NbIOProvider;
import org.netbeans.core.output2.ui.AbstractOutputTab;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ColorComboBox;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.IOColorPrint;
import org.openide.windows.IOContainer;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;

@NbBundle.Messages({
    "LBL_Description=Output Window Settings",
    "KW_Output_Window=Output Window",
    "KW_Font=Font",
    "KW_Color=Color",
    "KW_Underline=Underline",
    "KW_Background=Background"
})
@OptionsPanelController.Keywords(
    location = OptionsDisplayer.ADVANCED, tabTitle = "Output",
    keywords = {"#KW_Output_Window", "#KW_Font", "#KW_Color", "#KW_Underline",
    "#KW_Background", "Output Window", "Font", "Color", "Underline"})
public final class OutputSettingsPanel extends javax.swing.JPanel {

    private OutputOptions outputOptions;
    private InputOutput previewInputOutput = null;
    private final OutputSettingsOptionsPanelController controller;
    private LinkStyleModel linkStyleModel = new LinkStyleModel();

    OutputSettingsPanel(OutputSettingsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblFontFamily = new javax.swing.JLabel();
        lblStandardColor = new javax.swing.JLabel();
        lblErrorColor = new javax.swing.JLabel();
        lblBackgroundColor = new javax.swing.JLabel();
        lblLinkColor = new javax.swing.JLabel();
        cmbLinkColor = new ColorComboBox();
        cmbBackgroundColor = new ColorComboBox();
        cmbErrorColor = new ColorComboBox();
        cmbStandardColor = new ColorComboBox();
        lblFontSize = new javax.swing.JLabel();
        spnFontSize = new javax.swing.JSpinner();
        btnSelectFont = new javax.swing.JButton();
        cmbLinkStyle = new javax.swing.JComboBox();
        lblLinkStyle = new javax.swing.JLabel();
        fldFontFamily = new javax.swing.JTextField();
        cmbImportantLinkColor = new ColorComboBox();
        jLabel1 = new javax.swing.JLabel();
        lblUnwrappedOnly = new javax.swing.JLabel();
        lblInputColor = new javax.swing.JLabel();
        cmbInputColor = new ColorComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbDebugColor = new ColorComboBox();
        cmbWarningColor = new ColorComboBox();
        cmbFailureColor = new ColorComboBox();
        cmbSuccessColor = new ColorComboBox();
        previewPanel = new javax.swing.JPanel();
        btnRestore = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(lblTitle, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblTitle.text")); // NOI18N

        lblFontFamily.setLabelFor(btnSelectFont);
        org.openide.awt.Mnemonics.setLocalizedText(lblFontFamily, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblFontFamily.text")); // NOI18N

        lblStandardColor.setLabelFor(cmbStandardColor);
        org.openide.awt.Mnemonics.setLocalizedText(lblStandardColor, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblStandardColor.text")); // NOI18N

        lblErrorColor.setLabelFor(cmbErrorColor);
        org.openide.awt.Mnemonics.setLocalizedText(lblErrorColor, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblErrorColor.text")); // NOI18N

        lblBackgroundColor.setLabelFor(cmbBackgroundColor);
        org.openide.awt.Mnemonics.setLocalizedText(lblBackgroundColor, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblBackgroundColor.text")); // NOI18N

        lblLinkColor.setLabelFor(cmbLinkColor);
        org.openide.awt.Mnemonics.setLocalizedText(lblLinkColor, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblLinkColor.text")); // NOI18N

        cmbLinkColor.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.cmbLinkColor.toolTipText")); // NOI18N
        cmbLinkColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLinkColorActionPerformed(evt);
            }
        });

        cmbBackgroundColor.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.cmbBackgroundColor.toolTipText")); // NOI18N
        cmbBackgroundColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBackgroundColorActionPerformed(evt);
            }
        });

        cmbErrorColor.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.cmbErrorColor.toolTipText")); // NOI18N
        cmbErrorColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbErrorColorActionPerformed(evt);
            }
        });

        cmbStandardColor.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.cmbStandardColor.toolTipText")); // NOI18N
        cmbStandardColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbStandardColorActionPerformed(evt);
            }
        });

        lblFontSize.setLabelFor(spnFontSize);
        org.openide.awt.Mnemonics.setLocalizedText(lblFontSize, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblFontSize.text")); // NOI18N

        spnFontSize.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.spnFontSize.toolTipText")); // NOI18N
        spnFontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnFontSizeStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnSelectFont, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.btnSelectFont.text")); // NOI18N
        btnSelectFont.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.btnSelectFont.toolTipText")); // NOI18N
        btnSelectFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectFontActionPerformed(evt);
            }
        });

        cmbLinkStyle.setModel(linkStyleModel);
        cmbLinkStyle.setSelectedIndex(0);
        cmbLinkStyle.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.cmbLinkStyle.toolTipText")); // NOI18N
        cmbLinkStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLinkStyleActionPerformed(evt);
            }
        });

        lblLinkStyle.setLabelFor(cmbLinkStyle);
        org.openide.awt.Mnemonics.setLocalizedText(lblLinkStyle, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblLinkStyle.text")); // NOI18N

        fldFontFamily.setEditable(false);
        fldFontFamily.setText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.fldFontFamily.text")); // NOI18N

        cmbImportantLinkColor.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.cmbImportantLinkColor.toolTipText")); // NOI18N
        cmbImportantLinkColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbImportantLinkColorActionPerformed(evt);
            }
        });

        jLabel1.setLabelFor(cmbImportantLinkColor);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblUnwrappedOnly, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblUnwrappedOnly.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblInputColor, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.lblInputColor.text")); // NOI18N

        cmbInputColor.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.cmbInputColor.toolTipText")); // NOI18N
        cmbInputColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbInputColorActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.jLabel4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.jLabel5.text")); // NOI18N

        cmbDebugColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDebugColorActionPerformed(evt);
            }
        });

        cmbWarningColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbWarningColorActionPerformed(evt);
            }
        });

        cmbFailureColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFailureColorActionPerformed(evt);
            }
        });

        cmbSuccessColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSuccessColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrorColor)
                    .addComponent(lblStandardColor)
                    .addComponent(lblLinkColor)
                    .addComponent(lblFontFamily)
                    .addComponent(lblBackgroundColor)
                    .addComponent(lblFontSize)
                    .addComponent(jLabel1)
                    .addComponent(lblLinkStyle)
                    .addComponent(lblInputColor))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(fldFontFamily)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectFont))
                    .addComponent(lblUnwrappedOnly, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbLinkStyle, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbImportantLinkColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbLinkColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbInputColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbErrorColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbStandardColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spnFontSize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbBackgroundColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbFailureColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbWarningColor, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbDebugColor, 0, 238, Short.MAX_VALUE)
                            .addComponent(cmbSuccessColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(1, 1, 1))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbBackgroundColor, cmbDebugColor});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFontFamily)
                    .addComponent(btnSelectFont)
                    .addComponent(fldFontFamily, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(lblUnwrappedOnly)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFontSize)
                    .addComponent(spnFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBackgroundColor)
                    .addComponent(cmbBackgroundColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cmbDebugColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStandardColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStandardColor)
                    .addComponent(jLabel3)
                    .addComponent(cmbWarningColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbErrorColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorColor)
                    .addComponent(jLabel4)
                    .addComponent(cmbFailureColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInputColor)
                    .addComponent(cmbInputColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cmbSuccessColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLinkColor)
                    .addComponent(cmbLinkColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbImportantLinkColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLinkStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLinkStyle))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        previewPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        previewPanel.setLayout(new javax.swing.BoxLayout(previewPanel, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(btnRestore, org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.btnRestore.text")); // NOI18N
        btnRestore.setToolTipText(org.openide.util.NbBundle.getMessage(OutputSettingsPanel.class, "OutputSettingsPanel.btnRestore.toolTipText")); // NOI18N
        btnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(previewPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRestore))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnRestore))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFontActionPerformed
        PropertyEditor pe = PropertyEditorManager.findEditor(Font.class);
        if (pe != null) {
            pe.setValue(outputOptions.getFont());
            DialogDescriptor dd = new DialogDescriptor(pe.getCustomEditor(),
                    NbBundle.getMessage(Controller.class,
                    "LBL_Font_Chooser_Title"));                         //NOI18N
            String defaultFont = NbBundle.getMessage(Controller.class,
                    "BTN_Defaul_Font");                                 //NOI18N
            dd.setOptions(new Object[]{DialogDescriptor.OK_OPTION,
                        defaultFont, DialogDescriptor.CANCEL_OPTION});  //NOI18N
            DialogDisplayer.getDefault().createDialog(dd).setVisible(true);
            if (dd.getValue() == DialogDescriptor.OK_OPTION) {
                Font f = (Font) pe.getValue();
                outputOptions.setFont(f);
            } else if (dd.getValue() == defaultFont) {
                outputOptions.setFont(null);
            }
            updateFontField();
        }
    }//GEN-LAST:event_btnSelectFontActionPerformed

    private void cmbStandardColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStandardColorActionPerformed
        Color std = ((ColorComboBox) cmbStandardColor).getSelectedColor();
        if (std != null) {
            outputOptions.setColorStandard(std);
        }
    }//GEN-LAST:event_cmbStandardColorActionPerformed

    private void spnFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnFontSizeStateChanged
        int fontSize = (Integer) spnFontSize.getValue();
        outputOptions.setFont(outputOptions.getFont().deriveFont(
                (float) fontSize));
        updateFontField();
    }//GEN-LAST:event_spnFontSizeStateChanged

    private void cmbBackgroundColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBackgroundColorActionPerformed
        Color bg = ((ColorComboBox) cmbBackgroundColor).getSelectedColor();
        if (bg != null) {
            outputOptions.setColorBackground(bg);
        }
    }//GEN-LAST:event_cmbBackgroundColorActionPerformed

    private void cmbErrorColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbErrorColorActionPerformed
        Color err = ((ColorComboBox) cmbErrorColor).getSelectedColor();
        if (err != null) {
            outputOptions.setColorError(err);
        }
    }//GEN-LAST:event_cmbErrorColorActionPerformed

    private void cmbLinkColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLinkColorActionPerformed
        Color link = ((ColorComboBox) cmbLinkColor).getSelectedColor();
        if (link != null) {
            outputOptions.setColorLink(link);
        }
    }//GEN-LAST:event_cmbLinkColorActionPerformed

    private void cmbImportantLinkColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbImportantLinkColorActionPerformed
        Color iLink = ((ColorComboBox) cmbImportantLinkColor).
                getSelectedColor();
        if (iLink != null) {
            outputOptions.setColorLinkImportant(iLink);
        }
    }//GEN-LAST:event_cmbImportantLinkColorActionPerformed

    private void btnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreActionPerformed
        outputOptions.resetToDefault();
        updateControlsByModel();
    }//GEN-LAST:event_btnRestoreActionPerformed

    private void cmbLinkStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLinkStyleActionPerformed
        outputOptions.setLinkStyle(linkStyleModel.getLinkStyle());
    }//GEN-LAST:event_cmbLinkStyleActionPerformed

    private void cmbInputColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbInputColorActionPerformed
        Color input = ((ColorComboBox) cmbInputColor).getSelectedColor();
        if (input != null) {
            outputOptions.setColorInput(input);
        }
    }//GEN-LAST:event_cmbInputColorActionPerformed

    private void cmbDebugColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDebugColorActionPerformed
        Color debug = ((ColorComboBox) cmbDebugColor).getSelectedColor();
        if (debug != null) {
            outputOptions.setColorDebug(debug);
        }
    }//GEN-LAST:event_cmbDebugColorActionPerformed

    private void cmbWarningColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbWarningColorActionPerformed
        Color warning = ((ColorComboBox) cmbWarningColor).getSelectedColor();
        if (warning != null) {
            outputOptions.setColorWarning(warning);
        }
    }//GEN-LAST:event_cmbWarningColorActionPerformed

    private void cmbFailureColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFailureColorActionPerformed
        Color failure = ((ColorComboBox) cmbFailureColor).getSelectedColor();
        if (failure != null) {
            outputOptions.setColorFailure(failure);
        }
    }//GEN-LAST:event_cmbFailureColorActionPerformed

    private void cmbSuccessColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSuccessColorActionPerformed
        Color success = ((ColorComboBox) cmbSuccessColor).getSelectedColor();
        if (success != null) {
            outputOptions.setColorSuccess(success);
        }
    }//GEN-LAST:event_cmbSuccessColorActionPerformed

    void load() {
        if (previewInputOutput == null) {
            initPreview();
        }
        updateControlsByModel();
    }

    private void selectColor(JComboBox combo, Color color) {
        ((ColorComboBox) combo).setSelectedColor(color);
    }

    private void updateFontField() {
        Font f = outputOptions.getFont();
        fldFontFamily.setText(f.getFamily() + " " + f.getSize());       //NOI18N
    }

    void store() {
        Controller.getDefault().updateOptions(outputOptions);
        OutputOptions.getDefault().assign(outputOptions);
        OutputOptions.storeDefault();
    }

    void cancel() {
        if (previewInputOutput != null) {
            previewInputOutput.closeInputOutput();
        }
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRestore;
    private javax.swing.JButton btnSelectFont;
    private javax.swing.JComboBox cmbBackgroundColor;
    private javax.swing.JComboBox cmbDebugColor;
    private javax.swing.JComboBox cmbErrorColor;
    private javax.swing.JComboBox cmbFailureColor;
    private javax.swing.JComboBox cmbImportantLinkColor;
    private javax.swing.JComboBox cmbInputColor;
    private javax.swing.JComboBox cmbLinkColor;
    private javax.swing.JComboBox cmbLinkStyle;
    private javax.swing.JComboBox cmbStandardColor;
    private javax.swing.JComboBox cmbSuccessColor;
    private javax.swing.JComboBox cmbWarningColor;
    private javax.swing.JTextField fldFontFamily;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblBackgroundColor;
    private javax.swing.JLabel lblErrorColor;
    private javax.swing.JLabel lblFontFamily;
    private javax.swing.JLabel lblFontSize;
    private javax.swing.JLabel lblInputColor;
    private javax.swing.JLabel lblLinkColor;
    private javax.swing.JLabel lblLinkStyle;
    private javax.swing.JLabel lblStandardColor;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUnwrappedOnly;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JSpinner spnFontSize;
    // End of variables declaration//GEN-END:variables

    private void initPreview() {
        previewInputOutput = initPreviewInputOutput();
        outputOptions = ((Lookup.Provider) previewInputOutput).
                getLookup().lookup(OutputOptions.class);
        final Reader in = previewInputOutput.getIn();
        previewInputOutput.setInputVisible(true); // Instead of reading from in.
        previewInputOutput.getOut().println("Standard Output");         //NOI18N
        previewInputOutput.getErr().println("Error Output");            //NOI18N
        OutputListener ol = new OutputListenerImpl();
        try {
            IOColorPrint.print(previewInputOutput, "Standard Link", //NOI18N
                    ol, false, null);
            previewInputOutput.getOut().println();
            IOColorPrint.print(previewInputOutput, "Important Link", //NOI18N
                    ol, true, null);
            previewInputOutput.getOut().println();
        } catch (IOException ex) {
            ex.printStackTrace(previewInputOutput.getErr());
        }
        previewInputOutput.getOut().close();
        previewInputOutput.getErr().close();
        outputOptions.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!evt.getPropertyName().equals(OutputOptions.PROP_INITIALIZED)
                        && outputOptions.isInitialized()) {
                    controller.changed(outputOptions.isChanged());
                }
                updateControlsByModel();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Component component = previewPanel.getComponent(0);
                if (component instanceof AbstractOutputTab) {
                    ((AbstractOutputTab) component).inputSent("Input from keyboard");
                }
                try {
                    in.close();
                } catch (IOException ex) {}
            }
        });
    }

    private InputOutput initPreviewInputOutput() throws NullPointerException {
        IOContainer ioContainer = IOContainer.create(
                new PreviewIOProvider(previewPanel));
        InputOutput io = NbIOProvider.getDefault().getIO(
                "Preview", false, new Action[0], ioContainer);          //NOI18N
        Component component = previewPanel.getComponent(0);
        if (component instanceof AbstractOutputTab) {
            ((AbstractOutputTab) component).getOutputPane().setWrapped(false);
        }
        return io;
    }

    private void updateControlsByModel() {
        updateFontField();
        spnFontSize.setValue(outputOptions.getFont().getSize());
        selectColor(cmbStandardColor, outputOptions.getColorStandard());
        selectColor(cmbErrorColor, outputOptions.getColorError());
        selectColor(cmbInputColor, outputOptions.getColorInput());
        selectColor(cmbBackgroundColor, outputOptions.getColorBackground());
        selectColor(cmbLinkColor, outputOptions.getColorLink());
        selectColor(cmbDebugColor, outputOptions.getColorDebug());
        selectColor(cmbWarningColor, outputOptions.getColorWarning());
        selectColor(cmbFailureColor, outputOptions.getColorFailure());
        selectColor(cmbSuccessColor, outputOptions.getColorSuccess());
        selectColor(cmbImportantLinkColor,
                outputOptions.getColorLinkImportant());
        cmbLinkStyle.setSelectedItem(
                linkStyleModel.itemFor(outputOptions.getLinkStyle()));
        cmbLinkStyle.repaint();
    }

    private static class OutputListenerImpl implements OutputListener {

        public OutputListenerImpl() {
        }

        @Override
        public void outputLineSelected(OutputEvent ev) {
        }

        @Override
        public void outputLineAction(OutputEvent ev) {
        }

        @Override
        public void outputLineCleared(OutputEvent ev) {
        }
    }
}

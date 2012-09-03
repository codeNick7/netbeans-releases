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
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JComboBox;
import org.netbeans.core.output2.Controller;
import org.netbeans.core.output2.NbIOProvider;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ColorComboBox;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOColorPrint;
import org.openide.windows.IOContainer;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;

@NbBundle.Messages({
    "LBL_Description=Output Window Settings"
})
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLinkColor)
                            .addComponent(lblFontFamily)
                            .addComponent(lblBackgroundColor)
                            .addComponent(lblFontSize)
                            .addComponent(jLabel1)
                            .addComponent(lblLinkStyle))
                        .addGap(79, 79, 79)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbLinkStyle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbLinkColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(fldFontFamily)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectFont))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(spnFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 46, Short.MAX_VALUE))
                            .addComponent(cmbImportantLinkColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbBackgroundColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblErrorColor)
                            .addComponent(lblStandardColor))
                        .addGap(104, 104, 104)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbErrorColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbStandardColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFontFamily)
                    .addComponent(btnSelectFont)
                    .addComponent(fldFontFamily, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFontSize)
                    .addComponent(spnFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBackgroundColor)
                    .addComponent(cmbBackgroundColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStandardColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStandardColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbErrorColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorColor))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRestore)))
                .addContainerGap())
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
                .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
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
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                 OutputOptions.getDefault().saveTo(
                        NbPreferences.forModule(Controller.class));
            }
        });
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
    private javax.swing.JComboBox cmbErrorColor;
    private javax.swing.JComboBox cmbImportantLinkColor;
    private javax.swing.JComboBox cmbLinkColor;
    private javax.swing.JComboBox cmbLinkStyle;
    private javax.swing.JComboBox cmbStandardColor;
    private javax.swing.JTextField fldFontFamily;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblBackgroundColor;
    private javax.swing.JLabel lblErrorColor;
    private javax.swing.JLabel lblFontFamily;
    private javax.swing.JLabel lblFontSize;
    private javax.swing.JLabel lblLinkColor;
    private javax.swing.JLabel lblLinkStyle;
    private javax.swing.JLabel lblStandardColor;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JSpinner spnFontSize;
    // End of variables declaration//GEN-END:variables

    private void initPreview() {
        previewInputOutput = initPreviewInputOutput();
        outputOptions = ((Lookup.Provider) previewInputOutput).
                getLookup().lookup(OutputOptions.class);
        previewInputOutput.getOut().println("Standard Output");         //NOI18N
        previewInputOutput.getErr().println("Error Output");            //NOI18N
        OutputListener ol = new OutputListenerImpl();
        try {
            IOColorPrint.print(previewInputOutput, "Standard Link", //NOI18N
                    ol, false, null);
            previewInputOutput.getOut().println();
            IOColorPrint.print(previewInputOutput, "Important Link", //NOI18N
                    ol, true, null);
            previewInputOutput.getOut().print(" ");                     //NOI18N
        } catch (IOException ex) {
            ex.printStackTrace(previewInputOutput.getErr());
        }
        previewInputOutput.getOut().close();
        previewInputOutput.getErr().close();
        outputOptions.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                controller.changed();
                updateControlsByModel();
            }
        });
    }

    private InputOutput initPreviewInputOutput() throws NullPointerException {
        IOContainer ioContainer = IOContainer.create(
                new PreviewIOProvider(previewPanel));
        InputOutput io = NbIOProvider.getDefault().getIO(
                "Preview", false, new Action[0], ioContainer);          //NOI18N
        return io;
    }

    private void updateControlsByModel() {
        updateFontField();
        spnFontSize.setValue(outputOptions.getFont().getSize());
        selectColor(cmbStandardColor, outputOptions.getColorStandard());
        selectColor(cmbErrorColor, outputOptions.getColorError());
        selectColor(cmbBackgroundColor, outputOptions.getColorBackground());
        selectColor(cmbLinkColor, outputOptions.getColorLink());
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

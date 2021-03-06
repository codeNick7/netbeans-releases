/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.analysis.ui.analyzer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import org.netbeans.modules.analysis.spi.Analyzer;
import org.netbeans.modules.php.analysis.options.AnalysisOptions;
import org.netbeans.modules.php.analysis.options.AnalysisOptionsValidator;
import org.netbeans.modules.php.analysis.ui.CodeSnifferStandardsComboBoxModel;
import org.netbeans.modules.php.api.validation.ValidationResult;
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

public class CodeSnifferCustomizerPanel extends JPanel {

    private static final long serialVersionUID = 46872132457657L;
    private static final RequestProcessor RP = new RequestProcessor(CodeSnifferCustomizerPanel.class);

    public static final String ENABLED = "codeSniffer.enabled"; // NOI18N
    public static final String STANDARD = "codeSniffer.standard"; // NOI18N

    final CodeSnifferStandardsComboBoxModel standardsModel = new CodeSnifferStandardsComboBoxModel();
    final Analyzer.CustomizerContext<Void, CodeSnifferCustomizerPanel> context;
    final Preferences settings;


    public CodeSnifferCustomizerPanel(Analyzer.CustomizerContext<Void, CodeSnifferCustomizerPanel> context) {
        assert context != null;

        this.context = context;
        this.settings = context.getSettings();

        initComponents();
        init();
    }

    private void init() {
        enabledCheckBox.addItemListener((e) -> {
            setStandardComponentsEnabled(enabledCheckBox.isSelected());
            setCodeSnifferEnabled();
        });
        boolean isEnabled = settings.getBoolean(ENABLED, false);
        enabledCheckBox.setSelected(isEnabled);

        standardComboBox.setModel(standardsModel);
        standardsModel.fetchStandards(standardComboBox);
        standardsModel.setSelectedItem(settings.get(STANDARD, AnalysisOptions.getInstance().getCodeSnifferStandard()));
        // listeners
        standardComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                validateAndSetData();
            }
        });

        RP.schedule(() -> {
            EventQueue.invokeLater(() -> {
                setStandardComponentsEnabled(isEnabled);
            });
        }, 1000, TimeUnit.MILLISECONDS);
    }

    void validateAndSetData() {
        if (validateData()) {
            setData();
        }
    }

    private boolean validateData() {
        ValidationResult result = new AnalysisOptionsValidator()
                .validateCodeSnifferStandard(standardsModel.getSelectedStandard())
                .getResult();
        if (result.hasErrors()) {
            context.setError(result.getErrors().get(0).getMessage());
            return false;
        }
        if (result.hasWarnings()) {
            context.setError(result.getWarnings().get(0).getMessage());
            return false;
        }
        context.setError(null);
        return true;
    }

    private void setData() {
        settings.put(STANDARD, standardsModel.getSelectedStandard());
    }

    private void setCodeSnifferEnabled() {
        settings.putBoolean(ENABLED, enabledCheckBox.isSelected());
    }

    private void setStandardComponentsEnabled(boolean isEnabled) {
        standardComboBox.setEnabled(isEnabled);
        standardLabel.setEnabled(isEnabled);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        standardLabel = new JLabel();
        standardComboBox = new JComboBox<>();
        enabledCheckBox = new JCheckBox();

        Mnemonics.setLocalizedText(standardLabel, NbBundle.getMessage(CodeSnifferCustomizerPanel.class, "CodeSnifferCustomizerPanel.standardLabel.text")); // NOI18N

        Mnemonics.setLocalizedText(enabledCheckBox, NbBundle.getMessage(CodeSnifferCustomizerPanel.class, "CodeSnifferCustomizerPanel.enabledCheckBox.text")); // NOI18N

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(standardLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(standardComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addComponent(enabledCheckBox)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(enabledCheckBox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(standardLabel)
                    .addComponent(standardComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JCheckBox enabledCheckBox;
    private JComboBox<String> standardComboBox;
    private JLabel standardLabel;
    // End of variables declaration//GEN-END:variables

}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2014 Sun Microsystems, Inc.
 */
package org.netbeans.modules.javascript.nodejs.ui.customizer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.api.project.Project;
import org.netbeans.modules.javascript.nodejs.platform.NodeJsSupport;
import org.netbeans.modules.javascript.nodejs.preferences.NodeJsPreferences;
import org.netbeans.modules.javascript.nodejs.preferences.NodeJsPreferencesValidator;
import org.netbeans.modules.javascript.nodejs.ui.NodeJsPathPanel;
import org.netbeans.modules.javascript.nodejs.ui.options.NodeJsOptionsPanelController;
import org.netbeans.modules.javascript.nodejs.util.ValidationResult;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

final class NodeJsCustomizerPanel extends JPanel {

    private final ProjectCustomizer.Category category;
    private final NodeJsPreferences preferences;
    final NodeJsPathPanel nodeJsPathPanel;

    volatile boolean enabled;
    volatile boolean defaultNode;
    volatile String node;


    public NodeJsCustomizerPanel(ProjectCustomizer.Category category, Project project) {
        assert EventQueue.isDispatchThread();
        assert category != null;
        assert project != null;

        this.category = category;
        preferences = NodeJsSupport.forProject(project).getPreferences();
        nodeJsPathPanel = new NodeJsPathPanel();

        initComponents();
        init();
    }

    private void init() {
        nodePathPanel.add(nodeJsPathPanel, BorderLayout.CENTER);
        // init
        enabled = preferences.isEnabled();
        enabledCheckBox.setSelected(enabled);
        node = preferences.getNode();
        nodeJsPathPanel.setNode(node);
        defaultNode = preferences.isDefaultNode();
        if (defaultNode) {
            defaultNodeRadioButton.setSelected(true);
        } else {
            customNodeRadioButton.setSelected(true);
        }
        // ui
        enableAllFields();
        validateData();
        // listeners
        ItemListener defaultItemListener = new DefaultItemListener();
        category.setStoreListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        enabledCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                enabled = e.getStateChange() == ItemEvent.SELECTED;
                validateData();
                enableAllFields();
            }
        });
        defaultNodeRadioButton.addItemListener(defaultItemListener);
        customNodeRadioButton.addItemListener(defaultItemListener);
        nodeJsPathPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                node = nodeJsPathPanel.getNode();
                validateData();
            }
        });
    }

    void enableAllFields() {
        // default
        defaultNodeRadioButton.setEnabled(enabled);
        configureNodeButton.setEnabled(enabled && defaultNode);
        // custom
        customNodeRadioButton.setEnabled(enabled);
        nodeJsPathPanel.enablePanel(enabled && !defaultNode);
    }

    void validateData() {
        ValidationResult result = new NodeJsPreferencesValidator()
                .validateCustomizer(enabled, defaultNode, node)
                .getResult();
        if (result.hasErrors()) {
            category.setErrorMessage(result.getFirstErrorMessage());
            category.setValid(false);
            return;
        }
        if (result.hasWarnings()) {
            category.setErrorMessage(result.getFirstWarningMessage());
            category.setValid(true);
            return;
        }
        category.setErrorMessage(null);
        category.setValid(true);
    }

    void saveData() {
        preferences.setEnabled(enabled);
        preferences.setNode(node);
        preferences.setDefaultNode(defaultNode);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nodeBbuttonGroup = new ButtonGroup();
        enabledCheckBox = new JCheckBox();
        configureNodeButton = new JButton();
        defaultNodeRadioButton = new JRadioButton();
        customNodeRadioButton = new JRadioButton();
        nodePathPanel = new JPanel();

        Mnemonics.setLocalizedText(enabledCheckBox, NbBundle.getMessage(NodeJsCustomizerPanel.class, "NodeJsCustomizerPanel.enabledCheckBox.text")); // NOI18N

        Mnemonics.setLocalizedText(configureNodeButton, NbBundle.getMessage(NodeJsCustomizerPanel.class, "NodeJsCustomizerPanel.configureNodeButton.text")); // NOI18N
        configureNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                configureNodeButtonActionPerformed(evt);
            }
        });

        nodeBbuttonGroup.add(defaultNodeRadioButton);
        Mnemonics.setLocalizedText(defaultNodeRadioButton, NbBundle.getMessage(NodeJsCustomizerPanel.class, "NodeJsCustomizerPanel.defaultNodeRadioButton.text")); // NOI18N

        nodeBbuttonGroup.add(customNodeRadioButton);
        Mnemonics.setLocalizedText(customNodeRadioButton, NbBundle.getMessage(NodeJsCustomizerPanel.class, "NodeJsCustomizerPanel.customNodeRadioButton.text")); // NOI18N

        nodePathPanel.setLayout(new BorderLayout());

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(enabledCheckBox)
                    .addComponent(customNodeRadioButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(nodePathPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(defaultNodeRadioButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(configureNodeButton))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(enabledCheckBox)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(defaultNodeRadioButton)
                    .addComponent(configureNodeButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customNodeRadioButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nodePathPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void configureNodeButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_configureNodeButtonActionPerformed
        assert EventQueue.isDispatchThread();
        OptionsDisplayer.getDefault().open(NodeJsOptionsPanelController.OPTIONS_PATH);
    }//GEN-LAST:event_configureNodeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton configureNodeButton;
    private JRadioButton customNodeRadioButton;
    private JRadioButton defaultNodeRadioButton;
    private JCheckBox enabledCheckBox;
    private ButtonGroup nodeBbuttonGroup;
    private JPanel nodePathPanel;
    // End of variables declaration//GEN-END:variables

    //~ Inner classes

    private final class DefaultItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            defaultNode = defaultNodeRadioButton.isSelected();
            enableAllFields();
            validateData();
        }

    }

}

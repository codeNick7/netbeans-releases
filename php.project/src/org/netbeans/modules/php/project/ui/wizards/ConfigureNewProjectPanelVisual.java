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

package org.netbeans.modules.php.project.ui.wizards;

import java.awt.BorderLayout;
import java.io.File;
import java.nio.charset.Charset;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.netbeans.modules.php.project.ui.LastUsedFolders;
import org.netbeans.modules.php.project.ui.LocalServer;
import org.netbeans.modules.php.project.ui.LocalServerController;
import org.netbeans.modules.php.project.ui.Utils.EncodingModel;
import org.netbeans.modules.php.project.ui.Utils.EncodingRenderer;
import org.openide.WizardDescriptor;
import org.openide.awt.Mnemonics;
import org.openide.util.NbBundle;

class ConfigureNewProjectPanelVisual extends ConfigurableProjectPanel {

    private static final long serialVersionUID = 51987432745375479L;

    private final LocalServerController localServerComponent;

    ConfigureNewProjectPanelVisual(ConfigureProjectPanel wizardPanel) {
        // Provide a name in the title bar.
        setName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "LBL_ProjectNameLocation"));
        putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, 0); // NOI18N
        // Step name (actually the whole list for reference).
        putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, wizardPanel.getSteps()); // NOI18N

        initComponents();
        localServerComponent = LocalServerController.create(localServerComboBox, localServerButton, new BrowseSources(),
                NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "LBL_SelectSourceFolderTitle"));
        projectFolderPanel.add(BorderLayout.NORTH, projectFolderComponent);
        init();
    }

    private void init() {
        projectNameTextField.getDocument().addDocumentListener(this);
        localServerComponent.addChangeListener(this);

        encodingComboBox.setModel(new EncodingModel());
        encodingComboBox.setRenderer(new EncodingRenderer());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // same problem as in 31086, initial focus on Cancel button
        projectNameTextField.requestFocus();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectNameLabel = new JLabel();
        projectNameTextField = new JTextField();
        sourcesLabel = new JLabel();
        localServerComboBox = new JComboBox();
        localServerButton = new JButton();
        localServerInfoLabel = new JLabel();
        encodingLabel = new JLabel();
        encodingComboBox = new JComboBox();
        separator = new JSeparator();
        projectFolderPanel = new JPanel();

        setFocusTraversalPolicy(null);

        projectNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        projectNameLabel.setLabelFor(projectNameTextField);
        Mnemonics.setLocalizedText(projectNameLabel, NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "LBL_ProjectName"));
        projectNameLabel.setVerticalAlignment(SwingConstants.TOP);

        sourcesLabel.setLabelFor(localServerComboBox);
        Mnemonics.setLocalizedText(sourcesLabel, NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "LBL_Sources"));
        sourcesLabel.setVerticalAlignment(SwingConstants.TOP);

        localServerComboBox.setEditable(true);

        Mnemonics.setLocalizedText(localServerButton,NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "LBL_LocalServerBrowse")); // NOI18N
        Mnemonics.setLocalizedText(localServerInfoLabel, "dummy");
        localServerInfoLabel.setEnabled(false);

        encodingLabel.setLabelFor(encodingComboBox);

        Mnemonics.setLocalizedText(encodingLabel, NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "LBL_Encoding"));
        projectFolderPanel.setLayout(new BorderLayout());

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(projectNameLabel)
                    .add(sourcesLabel)
                    .add(encodingLabel))
                .add(24, 24, 24)
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(projectNameTextField, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(localServerComboBox, 0, 202, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(localServerButton))
                    .add(layout.createSequentialGroup()
                        .add(localServerInfoLabel)
                        .addContainerGap())
                    .add(encodingComboBox, 0, 297, Short.MAX_VALUE)))
            .add(separator, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .add(projectFolderPanel, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
        
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(projectNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(projectNameLabel))
                .add(7, 7, 7)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(localServerComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(localServerButton)
                    .add(sourcesLabel))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(localServerInfoLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(encodingComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(encodingLabel))
                .addPreferredGap(LayoutStyle.UNRELATED)
                .add(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.UNRELATED)
                .add(projectFolderPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        
        );

        projectNameLabel.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.projectNameLabel.AccessibleContext.accessibleName")); // NOI18N
        projectNameLabel.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.projectNameLabel.AccessibleContext.accessibleDescription")); // NOI18N
        projectNameTextField.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.projectNameTextField.AccessibleContext.accessibleName")); // NOI18N
        projectNameTextField.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.projectNameTextField.AccessibleContext.accessibleDescription")); // NOI18N
        sourcesLabel.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.sourcesLabel.AccessibleContext.accessibleName")); // NOI18N
        sourcesLabel.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.sourcesLabel.AccessibleContext.accessibleDescription")); // NOI18N
        localServerComboBox.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.localServerComboBox.AccessibleContext.accessibleName")); // NOI18N
        localServerComboBox.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.localServerComboBox.AccessibleContext.accessibleDescription")); // NOI18N
        localServerButton.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.localServerButton.AccessibleContext.accessibleName")); // NOI18N
        localServerButton.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.localServerButton.AccessibleContext.accessibleDescription")); // NOI18N
        localServerInfoLabel.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.localServerInfoLabel.AccessibleContext.accessibleName")); // NOI18N
        localServerInfoLabel.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.localServerInfoLabel.AccessibleContext.accessibleDescription")); // NOI18N
        encodingLabel.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.encodingLabel.AccessibleContext.accessibleName")); // NOI18N
        encodingLabel.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.encodingLabel.AccessibleContext.accessibleDescription")); // NOI18N
        encodingComboBox.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.encodingComboBox.AccessibleContext.accessibleName")); // NOI18N
        encodingComboBox.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.encodingComboBox.AccessibleContext.accessibleDescription")); // NOI18N
        separator.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.separator.AccessibleContext.accessibleName")); // NOI18N
        separator.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.separator.AccessibleContext.accessibleDescription")); // NOI18N
        projectFolderPanel.getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.projectFolderPanel.AccessibleContext.accessibleName")); // NOI18N
        projectFolderPanel.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.projectFolderPanel.AccessibleContext.accessibleDescription")); // NOI18N
        getAccessibleContext().setAccessibleName(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.AccessibleContext.accessibleName")); // NOI18N
        getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(ConfigureNewProjectPanelVisual.class, "ConfigureNewProjectPanelVisual.AccessibleContext.accessibleDescription")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JComboBox encodingComboBox;
    private JLabel encodingLabel;
    private JButton localServerButton;
    private JComboBox localServerComboBox;
    private JLabel localServerInfoLabel;
    private JPanel projectFolderPanel;
    private JLabel projectNameLabel;
    protected JTextField projectNameTextField;
    private JSeparator separator;
    private JLabel sourcesLabel;
    // End of variables declaration//GEN-END:variables

    public String getProjectName() {
        return projectNameTextField.getText().trim();
    }

    public void setProjectName(String projectName) {
        projectNameTextField.setText(projectName);
        projectNameTextField.selectAll();
    }

    public LocalServer getSourcesLocation() {
        return localServerComponent.getLocalServer();
    }

    public MutableComboBoxModel getLocalServerModel() {
        return localServerComponent.getLocalServerModel();
    }

    public void setLocalServerModel(MutableComboBoxModel localServers) {
        localServerComponent.setLocalServerModel(localServers);
    }

    public void selectSourcesLocation(LocalServer localServer) {
        localServerComponent.selectLocalServer(localServer);
    }

    public Charset getEncoding() {
        return (Charset) encodingComboBox.getSelectedItem();
    }

    public void setEncoding(Charset encoding) {
        encodingComboBox.setSelectedItem(encoding);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        String hint = localServerComponent.getLocalServer().getHint();
        localServerInfoLabel.setText(hint);
        super.stateChanged(e);
    }

    private static class BrowseSources implements LocalServerController.BrowseHandler {
        public File getCurrentDirectory() {
            return LastUsedFolders.getSources();
        }
        public void locationChanged(File location) {
            LastUsedFolders.setSources(location);
        }
    }
}

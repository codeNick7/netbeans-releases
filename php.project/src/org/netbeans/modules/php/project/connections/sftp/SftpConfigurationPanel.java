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

package org.netbeans.modules.php.project.connections.sftp;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.netbeans.modules.php.project.connections.ConfigManager.Configuration;
import org.netbeans.modules.php.project.connections.spi.RemoteConfigurationPanel;
import org.netbeans.modules.php.project.ui.customizer.RunAsValidator;
import org.openide.awt.Mnemonics;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;

/**
 * @author Tomas Mysik
 */
public class SftpConfigurationPanel extends JPanel implements RemoteConfigurationPanel {
    private static final long serialVersionUID = 62874685129730L;

    private static final int MINIMUM_PORT = 0;
    private static final int MAXIMUM_PORT = 65535;

    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private String error = null;

    public SftpConfigurationPanel() {
        initComponents();

        warningLabel.setText(" "); // NOI18N

        // listeners
        registerListeners();
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public JPanel getComponent() {
        return this;
    }

    private void setWarning(String msg) {
        warningLabel.setText(" "); // NOI18N
        warningLabel.setForeground(UIManager.getColor("nb.warningForeground")); // NOI18N
        warningLabel.setText(msg);
    }

    public boolean isValidConfiguration() {
        // remember password is dangerous
        // just warning - do it every time
        if (validateRememberPassword()) {
            setWarning(null);
        }

        if (!validateHost()) {
            return false;
        }

        if (!validatePort()) {
            return false;
        }

        if (!validateUser()) {
            return false;
        }

        // XXX known hosts + private key

        if (!validateInitialDirectory()) {
            return false;
        }

        if (!validateTimeout()) {
            return false;
        }
        return true;
    }

    public String getError() {
        return error;
    }

    protected void setError(String error) {
        this.error = error;
    }

    private void registerListeners() {
        DocumentListener documentListener = new DefaultDocumentListener();
        hostTextField.getDocument().addDocumentListener(documentListener);
        portTextField.getDocument().addDocumentListener(documentListener);
        userTextField.getDocument().addDocumentListener(documentListener);
        passwordTextField.getDocument().addDocumentListener(documentListener);
        knownHostsFileTextField.getDocument().addDocumentListener(documentListener);
        identityFileTextField.getDocument().addDocumentListener(documentListener);
        initialDirectoryTextField.getDocument().addDocumentListener(documentListener);
        timeoutTextField.getDocument().addDocumentListener(documentListener);
    }

    void fireChange() {
        changeSupport.fireChange();
    }

    private boolean validateRememberPassword() {
        if (getPassword().length() > 0) {
            setWarning(NbBundle.getMessage(SftpConfigurationPanel.class, "MSG_PasswordRememberDangerous"));
            return false;
        }
        return true;
    }

    private boolean validateHost() {
        if (getHostName().trim().length() == 0) {
            setError(NbBundle.getMessage(SftpConfigurationPanel.class, "MSG_NoHostName"));
            return false;
        }
        return true;
    }

    private boolean validatePort() {
        String err = null;
        try {
            int port = Integer.parseInt(getPort());
            if (port < MINIMUM_PORT || port > MAXIMUM_PORT) { // see InetSocketAddress
                err = NbBundle.getMessage(SftpConfigurationPanel.class, "MSG_PortInvalid", String.valueOf(MINIMUM_PORT), String.valueOf(MAXIMUM_PORT));
            }
        } catch (NumberFormatException nfe) {
            err = NbBundle.getMessage(SftpConfigurationPanel.class, "MSG_PortNotNumeric");
        }
        setError(err);
        return err == null;
    }

    private boolean validateUser() {
        if (getUserName().trim().length() == 0) {
            setError(NbBundle.getMessage(SftpConfigurationPanel.class, "MSG_NoUserName"));
            return false;
        }
        return true;
    }

    private boolean validateInitialDirectory() {
        String err = RunAsValidator.validateUploadDirectory(getInitialDirectory(), false);
        if (err != null) {
            setError(err);
            return false;
        }
        return true;
    }

    private boolean validateTimeout() {
        String err = null;
        try {
            int timeout = Integer.parseInt(getTimeout());
            if (timeout < 0) {
                err = NbBundle.getMessage(SftpConfigurationPanel.class, "MSG_TimeoutNotPositive");
            }
        } catch (NumberFormatException nfe) {
            err = NbBundle.getMessage(SftpConfigurationPanel.class, "MSG_TimeoutNotNumeric");
        }
        setError(err);
        return err == null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {




        hostLabel = new JLabel();
        hostTextField = new JTextField();
        portLabel = new JLabel();
        portTextField = new JTextField();
        userLabel = new JLabel();
        userTextField = new JTextField();
        identityFileLabel = new JLabel();
        identityFileTextField = new JTextField();
        identityFileBrowseButton = new JButton();
        passwordLabel = new JLabel();
        passwordTextField = new JPasswordField();
        passwordLabelInfo = new JLabel();
        initialDirectoryLabel = new JLabel();
        initialDirectoryTextField = new JTextField();
        timeoutLabel = new JLabel();
        timeoutTextField = new JTextField();
        knownHostsFileLabel = new JLabel();
        knownHostsFileTextField = new JTextField();
        knownHostsFileBrowseButton = new JButton();
        warningLabel = new JLabel();
        Mnemonics.setLocalizedText(hostLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.hostLabel.text")); // NOI18N
        hostTextField.setMinimumSize(new Dimension(150, 19));
        Mnemonics.setLocalizedText(portLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.portLabel.text"));
        Mnemonics.setLocalizedText(userLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.userLabel.text"));
        identityFileLabel.setLabelFor(identityFileTextField);




        Mnemonics.setLocalizedText(identityFileLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.identityFileLabel.text")); // NOI18N
        identityFileTextField.setText(NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.identityFileTextField.text")); // NOI18N
        Mnemonics.setLocalizedText(identityFileBrowseButton, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.identityFileBrowseButton.text"));
        Mnemonics.setLocalizedText(passwordLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.passwordLabel.text"));
        Mnemonics.setLocalizedText(passwordLabelInfo, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.passwordLabelInfo.text"));
        passwordLabelInfo.setEnabled(false);


        Mnemonics.setLocalizedText(initialDirectoryLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.initialDirectoryLabel.text"));
        Mnemonics.setLocalizedText(timeoutLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.timeoutLabel.text"));
        timeoutTextField.setMinimumSize(new Dimension(20, 19));

        knownHostsFileLabel.setLabelFor(knownHostsFileTextField);




        Mnemonics.setLocalizedText(knownHostsFileLabel, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.knownHostsFileLabel.text"));
        knownHostsFileTextField.setText(NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.knownHostsFileTextField.text")); // NOI18N
        Mnemonics.setLocalizedText(knownHostsFileBrowseButton, NbBundle.getMessage(SftpConfigurationPanel.class, "SftpConfigurationPanel.knownHostsFileBrowseButton.text"));
        Mnemonics.setLocalizedText(warningLabel, "warning");
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(warningLabel)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(hostLabel)
                    .add(userLabel)
                    .add(passwordLabel)
                    .add(initialDirectoryLabel)
                    .add(timeoutLabel)
                    .add(identityFileLabel)
                    .add(knownHostsFileLabel))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(passwordLabelInfo)
                    .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(GroupLayout.TRAILING)
                            .add(GroupLayout.LEADING, identityFileTextField, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .add(GroupLayout.LEADING, userTextField, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .add(GroupLayout.LEADING, hostTextField, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .add(GroupLayout.LEADING, passwordTextField, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .add(GroupLayout.LEADING, initialDirectoryTextField, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .add(GroupLayout.LEADING, timeoutTextField, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .add(knownHostsFileTextField, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(GroupLayout.LEADING)
                            .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                .add(layout.createSequentialGroup()
                                    .add(portLabel)
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(portTextField))
                                .add(identityFileBrowseButton))
                            .add(knownHostsFileBrowseButton))
                        .add(0, 0, 0)))
                .add(0, 0, 0))
        );

        layout.linkSize(new Component[] {identityFileBrowseButton, knownHostsFileBrowseButton}, GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(hostLabel)
                    .add(portLabel)
                    .add(portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(hostTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(userLabel)
                    .add(userTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(identityFileTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(identityFileLabel)
                    .add(identityFileBrowseButton))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(passwordLabel)
                    .add(passwordTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(passwordLabelInfo)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(initialDirectoryLabel)
                    .add(initialDirectoryTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(timeoutTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(timeoutLabel))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(knownHostsFileTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(knownHostsFileBrowseButton)
                    .add(knownHostsFileLabel))
                .addPreferredGap(LayoutStyle.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(warningLabel)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel hostLabel;
    private JTextField hostTextField;
    private JButton identityFileBrowseButton;
    private JLabel identityFileLabel;
    private JTextField identityFileTextField;
    private JLabel initialDirectoryLabel;
    private JTextField initialDirectoryTextField;
    private JButton knownHostsFileBrowseButton;
    private JLabel knownHostsFileLabel;
    private JTextField knownHostsFileTextField;
    private JLabel passwordLabel;
    private JLabel passwordLabelInfo;
    private JPasswordField passwordTextField;
    private JLabel portLabel;
    private JTextField portTextField;
    private JLabel timeoutLabel;
    private JTextField timeoutTextField;
    private JLabel userLabel;
    private JTextField userTextField;
    private JLabel warningLabel;
    // End of variables declaration//GEN-END:variables

    public String getHostName() {
        return hostTextField.getText();
    }

    public void setHostName(String hostName) {
        hostTextField.setText(hostName);
    }

    public String getPort() {
        return portTextField.getText();
    }

    public void setPort(String port) {
        portTextField.setText(port);
    }

    public String getUserName() {
        return userTextField.getText();
    }

    public void setUserName(String userName) {
        userTextField.setText(userName);
    }

    public String getPassword() {
        return new String(passwordTextField.getPassword());
    }

    public void setPassword(String password) {
        passwordTextField.setText(password);
    }

    public String getKnownHostsFile() {
        return knownHostsFileTextField.getText();
    }

    public void setKnownHostsFile(String knownHostsFile) {
        knownHostsFileTextField.setText(knownHostsFile);
    }

    public String getIdentityFile() {
        return identityFileTextField.getText();
    }

    public void setIdentityFile(String identityFile) {
        identityFileTextField.setText(identityFile);
    }

    public String getInitialDirectory() {
        return initialDirectoryTextField.getText();
    }

    public void setInitialDirectory(String initialDirectory) {
        initialDirectoryTextField.setText(initialDirectory);
    }

    public String getTimeout() {
        return timeoutTextField.getText();
    }

    public void setTimeout(String timeout) {
        timeoutTextField.setText(timeout);
    }

    public void read(Configuration cfg) {
        setHostName(cfg.getValue(SftpConnectionProvider.HOST));
        setPort(cfg.getValue(SftpConnectionProvider.PORT));
        setUserName(cfg.getValue(SftpConnectionProvider.USER));
        setPassword(cfg.getValue(SftpConnectionProvider.PASSWORD, true));
        setKnownHostsFile(cfg.getValue(SftpConnectionProvider.KNOWN_HOSTS_FILE));
        setIdentityFile(cfg.getValue(SftpConnectionProvider.IDENTITY_FILE));
        setInitialDirectory(cfg.getValue(SftpConnectionProvider.INITIAL_DIRECTORY));
        setTimeout(cfg.getValue(SftpConnectionProvider.TIMEOUT));
    }

    public void store(Configuration cfg) {
        cfg.putValue(SftpConnectionProvider.HOST, getHostName());
        cfg.putValue(SftpConnectionProvider.PORT, getPort());
        cfg.putValue(SftpConnectionProvider.USER, getUserName());
        cfg.putValue(SftpConnectionProvider.PASSWORD, getPassword(), true);
        cfg.putValue(SftpConnectionProvider.KNOWN_HOSTS_FILE, getKnownHostsFile());
        cfg.putValue(SftpConnectionProvider.IDENTITY_FILE, getIdentityFile());
        cfg.putValue(SftpConnectionProvider.INITIAL_DIRECTORY, getInitialDirectory());
        cfg.putValue(SftpConnectionProvider.TIMEOUT, getTimeout());
    }

    private final class DefaultDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            processUpdate();
        }
        public void removeUpdate(DocumentEvent e) {
            processUpdate();
        }
        public void changedUpdate(DocumentEvent e) {
            processUpdate();
        }
        private void processUpdate() {
            fireChange();
        }
    }
}

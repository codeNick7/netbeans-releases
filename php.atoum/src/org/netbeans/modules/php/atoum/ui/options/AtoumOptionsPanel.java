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
package org.netbeans.modules.php.atoum.ui.options;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.modules.php.api.util.FileUtils;
import org.netbeans.modules.php.api.util.UiUtils;
import org.netbeans.modules.php.atoum.commands.Atoum;
import org.openide.awt.HtmlBrowser;
import org.openide.awt.Mnemonics;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

public class AtoumOptionsPanel extends JPanel {

    private static final long serialVersionUID = -138100563143476L;

    private final ChangeSupport changeSupport = new ChangeSupport(this);


    public AtoumOptionsPanel() {
        initComponents();
        init();
    }

    @NbBundle.Messages({
        "# {0} - phar file name",
        "# {1} - atoum file name",
        "AtoumOptionsPanel.atoum.hint=Full path of atoum file (typically {0} or {1}).",
    })
    private void init() {
        errorLabel.setText(" "); // NOI18N
        atoumPathHintLabel.setText(Bundle.AtoumOptionsPanel_atoum_hint(Atoum.PHAR_FILE_NAME, Atoum.ATOUM_FILE_NAME));

        atoumPathTextField.getDocument().addDocumentListener(new DefaultDocumentListener());
    }

    public String getAtoumPath() {
        return atoumPathTextField.getText();
    }

    public void setAtoumPath(String path) {
        atoumPathTextField.setText(path);
    }

    public void setError(String message) {
        errorLabel.setText(" "); // NOI18N
        errorLabel.setForeground(UIManager.getColor("nb.errorForeground")); // NOI18N
        errorLabel.setText(message);
    }

    public void setWarning(String message) {
        errorLabel.setText(" "); // NOI18N
        errorLabel.setForeground(UIManager.getColor("nb.warningForeground")); // NOI18N
        errorLabel.setText(message);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    void fireChange() {
        changeSupport.fireChange();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form
     * Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        atoumPathLabel = new JLabel();
        atoumPathTextField = new JTextField();
        browseAtoumButton = new JButton();
        searchAtoumButton = new JButton();
        atoumPathHintLabel = new JLabel();
        noteLabel = new JLabel();
        installLabel = new JLabel();
        learnMoreLabel = new JLabel();
        errorLabel = new JLabel();

        atoumPathLabel.setLabelFor(atoumPathTextField);
        Mnemonics.setLocalizedText(atoumPathLabel, NbBundle.getMessage(AtoumOptionsPanel.class, "AtoumOptionsPanel.atoumPathLabel.text")); // NOI18N

        Mnemonics.setLocalizedText(browseAtoumButton, NbBundle.getMessage(AtoumOptionsPanel.class, "AtoumOptionsPanel.browseAtoumButton.text")); // NOI18N
        browseAtoumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                browseAtoumButtonActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(searchAtoumButton, NbBundle.getMessage(AtoumOptionsPanel.class, "AtoumOptionsPanel.searchAtoumButton.text")); // NOI18N
        searchAtoumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchAtoumButtonActionPerformed(evt);
            }
        });

        Mnemonics.setLocalizedText(atoumPathHintLabel, "HINT"); // NOI18N

        Mnemonics.setLocalizedText(noteLabel, NbBundle.getMessage(AtoumOptionsPanel.class, "AtoumOptionsPanel.noteLabel.text")); // NOI18N

        Mnemonics.setLocalizedText(installLabel, NbBundle.getMessage(AtoumOptionsPanel.class, "AtoumOptionsPanel.installLabel.text")); // NOI18N

        Mnemonics.setLocalizedText(learnMoreLabel, NbBundle.getMessage(AtoumOptionsPanel.class, "AtoumOptionsPanel.learnMoreLabel.text")); // NOI18N
        learnMoreLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                learnMoreLabelMouseEntered(evt);
            }
            public void mousePressed(MouseEvent evt) {
                learnMoreLabelMousePressed(evt);
            }
        });

        Mnemonics.setLocalizedText(errorLabel, "ERROR"); // NOI18N

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(atoumPathLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(atoumPathHintLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(atoumPathTextField)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseAtoumButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchAtoumButton))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(installLabel)
                    .addComponent(learnMoreLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(noteLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(errorLabel))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {browseAtoumButton, searchAtoumButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(atoumPathLabel)
                    .addComponent(atoumPathTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseAtoumButton)
                    .addComponent(searchAtoumButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(atoumPathHintLabel)
                .addGap(18, 18, 18)
                .addComponent(noteLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(installLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(learnMoreLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(errorLabel))
        );
    }// </editor-fold>//GEN-END:initComponents

    @NbBundle.Messages("AtoumOptionsPanel.atoum.browse.title=Select atoum")
    private void browseAtoumButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_browseAtoumButtonActionPerformed
        File file = new FileChooserBuilder(AtoumOptionsPanel.class)
                .setFilesOnly(true)
                .setTitle(Bundle.AtoumOptionsPanel_atoum_browse_title())
                .showOpenDialog();
        if (file != null) {
            atoumPathTextField.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_browseAtoumButtonActionPerformed

    @NbBundle.Messages({
        "AtoumOptionsPanel.atoum.search.title=atoum files",
        "AtoumOptionsPanel.atoum.search.files=&atoum files:",
        "AtoumOptionsPanel.atoum.search.pleaseWaitPart=atoum files",
        "AtoumOptionsPanel.atoum.search.notFound=No atoum files found."
    })
    private void searchAtoumButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_searchAtoumButtonActionPerformed
        String atoum = UiUtils.SearchWindow.search(new UiUtils.SearchWindow.SearchWindowSupport() {
            @Override
            public List<String> detect() {
                return FileUtils.findFileOnUsersPath(Atoum.PHAR_FILE_NAME, Atoum.ATOUM_FILE_NAME);
            }
            @Override
            public String getWindowTitle() {
                return Bundle.AtoumOptionsPanel_atoum_search_title();
            }
            @Override
            public String getListTitle() {
                return Bundle.AtoumOptionsPanel_atoum_search_files();
            }
            @Override
            public String getPleaseWaitPart() {
                return Bundle.AtoumOptionsPanel_atoum_search_pleaseWaitPart();
            }
            @Override
            public String getNoItemsFound() {
                return Bundle.AtoumOptionsPanel_atoum_search_notFound();
            }
        });
        if (atoum != null) {
            atoumPathTextField.setText(atoum);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_searchAtoumButtonActionPerformed

    private void learnMoreLabelMouseEntered(MouseEvent evt) {//GEN-FIRST:event_learnMoreLabelMouseEntered
        evt.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_learnMoreLabelMouseEntered

    private void learnMoreLabelMousePressed(MouseEvent evt) {//GEN-FIRST:event_learnMoreLabelMousePressed
        try {
            URL url = new URL("https://github.com/atoum/atoum"); // NOI18N
            HtmlBrowser.URLDisplayer.getDefault().showURL(url);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_learnMoreLabelMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel atoumPathHintLabel;
    private JLabel atoumPathLabel;
    private JTextField atoumPathTextField;
    private JButton browseAtoumButton;
    private JLabel errorLabel;
    private JLabel installLabel;
    private JLabel learnMoreLabel;
    private JLabel noteLabel;
    private JButton searchAtoumButton;
    // End of variables declaration//GEN-END:variables

    //~ Inner classes

    private final class DefaultDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            processUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            processUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            processUpdate();
        }

        private void processUpdate() {
            fireChange();
        }

    }

}

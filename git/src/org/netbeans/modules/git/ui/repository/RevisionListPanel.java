/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */

/*
 * RevisionsPanel.java
 *
 * Created on Dec 21, 2010, 5:14:40 PM
 */

package org.netbeans.modules.git.ui.repository;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.netbeans.modules.git.client.GitClient;
import org.netbeans.libs.git.GitException;
import org.netbeans.libs.git.GitFileInfo;
import org.netbeans.libs.git.GitObjectType;
import org.netbeans.libs.git.GitRevisionInfo;
import org.netbeans.libs.git.GitUser;
import org.netbeans.libs.git.SearchCriteria;
import org.netbeans.libs.git.progress.ProgressMonitor;
import org.netbeans.libs.git.progress.RevisionInfoListener;
import org.netbeans.modules.git.Git;
import org.netbeans.modules.git.client.GitClientExceptionHandler;
import org.netbeans.modules.git.client.GitProgressSupport;
import org.openide.util.Mutex;
import org.openide.util.NbBundle;

/**
 *
 * @author ondra
 */
public class RevisionListPanel extends javax.swing.JPanel implements ActionListener {

    private ProgressMonitor.DefaultProgressMonitor listHistoryMonitor;
    private final DefaultListModel revisionListModel;
    private GitProgressSupport supp;
    private final Object LOCK = new Object();
    private String lastHWRevision;
    private File lastHWRepository;
    private Revision currRevision;
    private File currRepository;
    private File[] currRoots;
    private int currLimit;
    private boolean addition;
    private final int DEFAULT_LIMIT = 10;
    
    /** Creates new form RevisionsPanel */
    public RevisionListPanel() {
        lstRevisions.setModel(revisionListModel = new DefaultListModel());
        lstRevisions.setFixedCellHeight(-1);
        lstRevisions.setCellRenderer(new RevisionRenderer());
        initComponents();
        attachListeners();
    }

    @Override
    public void removeNotify () {
        cancelBackgroundTasks();
        super.removeNotify();
    }

    private void attachListeners () {
        btnAll.addActionListener(this);
        btnNext10.addActionListener(this);
        btnRefresh.addActionListener(this);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        File repo;
        File[] roots;
        Revision revision;
        int limit;
        boolean add = true;
        synchronized (LOCK) {
            repo = currRepository;
            roots = currRoots;
            revision = currRevision;
            limit = currLimit;
        }
        if (btnAll == e.getSource()) {
            limit = -1;
        } else if (btnNext10 == e.getSource()) {
            limit += 10;
        } else if (btnRefresh == e.getSource()) {
            add = false;
            limit = DEFAULT_LIMIT;
        }
        updateHistory(repo, roots, revision, limit, add);
    }

    private static class RevisionRenderer extends JTextPane implements ListCellRenderer {

        private Style selectedStyle;
        private Style normalStyle;
        private Color selectionBackground;
        private Color selectionForeground;

        public RevisionRenderer () {
            selectionBackground = new JList().getSelectionBackground();
            selectionForeground = new JList().getSelectionForeground();

            selectedStyle = addStyle("selected", null); // NOI18N
            StyleConstants.setForeground(selectedStyle, selectionForeground); // NOI18N
            StyleConstants.setBackground(selectedStyle, selectionBackground); // NOI18N
            normalStyle = addStyle("normal", null); // NOI18N
            StyleConstants.setForeground(normalStyle, UIManager.getColor("List.foreground")); // NOI18N

            setLayout(new BorderLayout());
            setBorder(null);
        }
        
        @Override
        public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            GitRevisionInfo revision = (GitRevisionInfo) value;
            StyledDocument sd = getStyledDocument();

            Style style;
            Color backgroundColor;

            if (isSelected) {
                backgroundColor = selectionBackground;
                style = selectedStyle;
            } else {
                backgroundColor = UIManager.getColor("List.background"); // NOI18N
                style = normalStyle;
            }
            setBackground(backgroundColor);

            try {
                // clear document
                sd.remove(0, sd.getLength());
                String revStr = revision.getRevision();
                if (revStr.length() > 20) {
                    revStr = revStr.substring(0, 17) + "..."; //NOI18N
                }
                sd.insertString(0, revStr, style);
            } catch (BadLocationException e) {
                //
            }

            return this;
        }
        
    }
    
    private void enableButtons (final boolean enabled) {
        Mutex.EVENT.readAccess(new Runnable() {
            @Override
            public void run() {
                btnAll.setEnabled(enabled);
                btnNext10.setEnabled(enabled);
                btnRefresh.setEnabled(enabled);
            }
        });
    }
    
    private void cancelBackgroundTasks () {
        synchronized (LOCK) {
            if (supp != null) {
                supp.cancel();
            }
            if (listHistoryMonitor != null) {
                listHistoryMonitor.cancel();
            }
        }
    }
    
    void updateHistory (File repository, File[] roots, Revision revision) {
        updateHistory(repository, roots, revision, DEFAULT_LIMIT, null);
    }
    
    private void updateHistory (File repository, File[] roots, Revision revision, int limit, Boolean addition) {
        synchronized (LOCK) {
            if (addition == null && (repository == lastHWRepository || lastHWRepository != null && lastHWRepository.equals(repository))
                    && (revision == null && lastHWRevision == null || lastHWRevision != null && revision != null && lastHWRevision.equals(revision.getRevision()))) {
                // no change made (selected repository i the same and selected revision is the same)
                return;
            }
            this.addition = Boolean.TRUE.equals(addition);
            lastHWRepository = repository;
            lastHWRevision = revision == null ? null : revision.getRevision();
            currRepository = repository;
            currRevision = revision;
            currRoots = roots;
            currLimit = limit;
            cancelBackgroundTasks();
            supp = new ListHistoryProgressSupport();
            supp.start(Git.getInstance().getRequestProcessor(repository), repository, NbBundle.getMessage(RevisionListPanel.class, "LBL_RevisionList.LoadingRevisions")); //NOI18N
        }
    }
    
    private class ListHistoryProgressSupport extends GitProgressSupport.NoOutputLogging implements RevisionInfoListener, ListSelectionListener {
        
        private final List<GitRevisionInfo> revisions = new LinkedList<GitRevisionInfo>();
        private final Set<String> displayedRevisions = new HashSet<String>(10);
        private boolean reselected;
        private GitRevisionInfo selectedRevision;
        private int limit;
        
        @Override
        public void perform () {
            Revision rev;
            File repository;
            File[] roots;
            final boolean add;
            synchronized (LOCK) {
                rev = currRevision;
                repository = currRepository;
                roots = currRoots;
                limit = currLimit;
                add = addition;
            }
            synchronized (revisions) {
                revisions.clear();
            }
            if (repository != null) {
                listHistoryMonitor = new ProgressMonitor.DefaultProgressMonitor();
                if (isCanceled()) {
                    return;
                }
                boolean finished = false;
                try {
                    GitClient client = getClient();
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run () {
                            selectedRevision = (GitRevisionInfo) lstRevisions.getSelectedValue();
                            displayedRevisions.clear();
                            if (add) {
                                for (Object o : revisionListModel.toArray()) {
                                    displayedRevisions.add(((GitRevisionInfo) o).getRevision());
                                }
                            } else {
                                revisionListModel.clear();
                            }
                            reselected = false;
                            enableButtons(false);
                        }
                    });
                    lstRevisions.addListSelectionListener(this);
                    client.addNotificationListener(this);
                    SearchCriteria criteria = new SearchCriteria();
                    criteria.setFiles(roots);
                    if (limit > 0) {
                        criteria.setLimit(limit + 1); // get one extra revision to be able to enable/disable buttons
                    }
                    if (rev != null) {
                        criteria.setRevisionTo(rev.getRevision());
                    }
                    final GitRevisionInfo[] revs = client.log(criteria, listHistoryMonitor);
                    if (!isCanceled()) {
                        finished = true;
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run () {
                                btnRefresh.setEnabled(true);
                                if (limit > 0 && revs.length > limit) {
                                    btnAll.setEnabled(true);
                                    btnNext10.setEnabled(true);
                                }
                            }
                        });
                    }
                } catch (GitException.MissingObjectException ex) {
                    if (ex.getObjectType() != GitObjectType.HEAD) {
                        GitClientExceptionHandler.notifyException(ex, true);
                    }
                } catch (GitException ex) {
                    GitClientExceptionHandler.notifyException(ex, true);
                } finally {
                    final boolean isFinished = finished;
                    lstRevisions.removeListSelectionListener(this);
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run () {
                            if (!isFinished) {
                                enableButtons(true);
                            }
                            // do not keep the reference forever
                            selectedRevision = null;
                        }
                    });
                }
            }
        }

        @Override
        public void notifyRevisionInfo (GitRevisionInfo revisionInfo) {
            synchronized (revisions) {
                revisions.add(revisionInfo);
            }
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run () {
                    synchronized (revisions) {
                        while (!revisions.isEmpty()) {
                            GitRevisionInfo info = new GitRevisionInfoDelegate(revisions.remove(0));// override toString, so one can Ctrl+C the revision string
                            if ((limit < 0 || limit > revisionListModel.getSize()) && !displayedRevisions.contains(info.getRevision())) {
                                revisionListModel.addElement(info);
                                if (!reselected && selectedRevision != null && info.getRevision().equals(selectedRevision.getRevision())) {
                                    // has not yet been reselected or manually selected by user
                                    lstRevisions.setSelectedValue(info, false);
                                    reselected = true;
                                    selectedRevision = null;
                                }
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void valueChanged (ListSelectionEvent e) {
            if (!e.getValueIsAdjusting() && e.getLastIndex() > -1 && e.getFirstIndex() > -1) {
                reselected = true;
                selectedRevision = null;
            }
        }
    }
    
    private static class GitRevisionInfoDelegate implements GitRevisionInfo {
        private final GitRevisionInfo info;

        public GitRevisionInfoDelegate (GitRevisionInfo info) {
            this.info = info;
        }

        @Override
        public String getRevision () {
            return info.getRevision();
        }

        @Override
        public String getShortMessage () {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getFullMessage () {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long getCommitTime () {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public GitUser getAuthor () {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public GitUser getCommitter () {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Map<File, GitFileInfo> getModifiedFiles () throws GitException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String toString () {
            return getRevision();
        }

        @Override
        public String[] getParents () {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        btnNext10 = new org.netbeans.modules.versioning.history.LinkButton();
        jLabel2 = new javax.swing.JLabel();
        btnAll = new org.netbeans.modules.versioning.history.LinkButton();
        jLabel3 = new javax.swing.JLabel();
        btnRefresh = new org.netbeans.modules.versioning.history.LinkButton();

        lstRevisions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lstRevisions);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btnNext10, org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.btnNext10.text")); // NOI18N
        btnNext10.setToolTipText(org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.btnNext10.TTtext")); // NOI18N
        btnNext10.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btnAll, org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.btnAll.text")); // NOI18N
        btnAll.setToolTipText(org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.btnAll.TTtext")); // NOI18N
        btnAll.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btnRefresh, org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.btnRefresh.text")); // NOI18N
        btnRefresh.setToolTipText(org.openide.util.NbBundle.getMessage(RevisionListPanel.class, "RevisionListPanel.btnRefresh.toolTipText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel2)
                        .addGap(5, 5, 5)
                        .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3)
                        .addGap(5, 5, 5)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnNext10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.netbeans.modules.versioning.history.LinkButton btnAll;
    private org.netbeans.modules.versioning.history.LinkButton btnNext10;
    private org.netbeans.modules.versioning.history.LinkButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    final javax.swing.JList lstRevisions = new javax.swing.JList();
    // End of variables declaration//GEN-END:variables

}

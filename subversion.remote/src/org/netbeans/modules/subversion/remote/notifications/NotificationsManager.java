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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.modules.subversion.remote.notifications;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.netbeans.modules.subversion.remote.FileInformation;
import org.netbeans.modules.subversion.remote.FileStatusCache;
import org.netbeans.modules.subversion.remote.Subversion;
import org.netbeans.modules.subversion.remote.api.ISVNInfo;
import org.netbeans.modules.subversion.remote.api.ISVNStatus;
import org.netbeans.modules.subversion.remote.api.SVNClientException;
import org.netbeans.modules.subversion.remote.api.SVNRevision;
import org.netbeans.modules.subversion.remote.api.SVNStatusKind;
import org.netbeans.modules.subversion.remote.api.SVNUrl;
import org.netbeans.modules.subversion.remote.client.SvnClient;
import org.netbeans.modules.subversion.remote.client.SvnClientExceptionHandler;
import org.netbeans.modules.subversion.remote.kenai.SvnKenaiAccessor;
import org.netbeans.modules.subversion.remote.ui.diff.DiffAction;
import org.netbeans.modules.subversion.remote.ui.diff.Setup;
import org.netbeans.modules.subversion.remote.ui.history.SearchHistoryAction;
import org.netbeans.modules.subversion.remote.util.Context;
import org.netbeans.modules.subversion.remote.util.SvnUtils;
import org.netbeans.modules.subversion.remote.util.VCSFileProxySupport;
import org.netbeans.modules.subversion.remote.versioning.util.VCSNotificationDisplayer;
import org.netbeans.modules.versioning.core.api.VCSFileProxy;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.WeakSet;

/**
 * Notifies about external changes on the given files.
 * @author Ondra Vrabec
 */
public class NotificationsManager {

    private static NotificationsManager instance;
    private static final Logger LOG = Logger.getLogger(NotificationsManager.class.getName());
    private static final Set<VCSFileProxy> alreadySeen = Collections.synchronizedSet(new WeakSet<VCSFileProxy>());
    private static final String CMD_DIFF = "cmd.diff";                  //NOI18N

    private final HashSet<VCSFileProxy> files;
    private final RequestProcessor rp;
    private final RequestProcessor.Task notificationTask;
    private final FileStatusCache cache;
    private final SvnKenaiAccessor kenaiAccessor;
    private Boolean enabled;

    private final Map<VCSFileProxy, Long> notifiedFiles = Collections.synchronizedMap(new HashMap<VCSFileProxy, Long>());

    private NotificationsManager () {
        files = new HashSet<VCSFileProxy>();
        rp = new RequestProcessor("SubversionNotifications", 1, true);  //NOI18N
        notificationTask = rp.create(new NotificationTask());
        cache = Subversion.getInstance().getStatusCache();
        kenaiAccessor = SvnKenaiAccessor.getInstance();
    }

    /**
     * Returns an instance of the class
     * @return
     */
    public static synchronized NotificationsManager getInstance() {
        if (instance == null) {
            instance = new NotificationsManager();
        }
        return instance;
    }

    /**
     * Plans a task detecting if a notification for the file is needed and in that case displaying the notification.
     * The notification is displayed if the file is still up-to-date (during the time of the call) and there's an external change
     * in the repository.
     * @param file file to scan
     */
    public void scheduleFor(VCSFileProxy file) {
        if (isSeen(file) || !isUpToDate(file) || !isEnabled(file, false)) {
            if (LOG.isLoggable(Level.FINER)) {
                LOG.log(Level.FINER, "File {0} is {1} up to date, notifications enabled: {2}", new Object[]{file.getPath(), isUpToDate(file) ? "" : "not ", isEnabled(file, false)}); //NOI18N
            }
            return;
        }
        boolean refresh;
        // register the file for the scan
        synchronized (files) {
            int size = files.size();
            files.add(file);
            refresh = files.size() != size;
        }
        if (refresh) {
            notificationTask.schedule(1000);
        }
    }

    public void notfied(VCSFileProxy[] files, Long revision) {
        for (VCSFileProxy file : files) {
            notifiedFiles.put(file, revision);
        }
    }

    public void setupPane(JTextPane pane, final VCSFileProxy[] files, String fileNames, final VCSFileProxy projectDir, final String url, final String revision) {
         String msg = revision == null
                 ? NbBundle.getMessage(NotificationsManager.class, "MSG_NotificationBubble_DeleteDescription", fileNames, CMD_DIFF) //NOI18N
                 : NbBundle.getMessage(NotificationsManager.class, "MSG_NotificationBubble_Description", fileNames, url, CMD_DIFF); //NOI18N
        pane.setText(msg);

        pane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    if(CMD_DIFF.equals(e.getDescription())) {
                        Context ctx = new Context(files);
                        DiffAction.diff(ctx, Setup.DIFFTYPE_REMOTE, NbBundle.getMessage(NotificationsManager.class, "LBL_Remote_Changes", projectDir.getName()), false); //NOI18N
                    } else if (revision != null) {
                        try {
                            SearchHistoryAction.openSearch(new SVNUrl(url), projectDir, Long.parseLong(revision));
                        } catch (MalformedURLException ex) {
                            LOG.log(Level.WARNING, null, ex);
                        }
                    }
                }
            }
        });
    }

    /**
     * Notifications are enabled only for logged kenai users and unless disabled by a switch
     * @return
     */
    private boolean isEnabled (VCSFileProxy file, boolean checkUrl) {
        if (enabled == null) {
            // let's leave a possibility to disable the notifications
            enabled = !"false".equals(System.getProperty("subversion.notificationsEnabled", "true")); //NOI18N
        }
        boolean retval = false;
        if (enabled.booleanValue()) {
            if (checkUrl) {
                assert !EventQueue.isDispatchThread();
                SVNUrl url = null;
                try {
                    url = SvnUtils.getRepositoryRootUrl(file);
                } catch (SVNClientException ex) { }
                if (url != null) {
                    retval = kenaiAccessor.isLogged(url.toString());
                }
            } else {
                retval = true;
            }
        }
        return retval;
    }

    private boolean isUpToDate(VCSFileProxy file) {
        boolean upToDate = false;
        FileInformation info = cache.getCachedStatus(file);
        if (info == null || (info.getStatus() & FileInformation.STATUS_VERSIONED_UPTODATE) != 0 && !info.isDirectory()) {
            upToDate = true;
        }
        return upToDate;
    }

    private boolean isSeen(VCSFileProxy file) {
        return alreadySeen.contains(file) || notifiedFiles.containsKey(file);
    }

    private class NotificationTask extends VCSNotificationDisplayer implements Runnable {

        @Override
        public void run() {
            HashSet<VCSFileProxy> filesToScan;
            synchronized (files) {
                filesToScan = new HashSet<VCSFileProxy>(files);
                files.clear();
            }
            removeDirectories(filesToScan);
            removeSeenFiles(filesToScan);
            removeNotEnabled(filesToScan);
            if (!filesToScan.isEmpty()) {
                scanFiles(filesToScan);
            }
        }

        @Override
        protected void setupPane(JTextPane pane, final VCSFileProxy[] files, final VCSFileProxy projectDir, final String url, final String revision) {
            NotificationsManager.this.setupPane(pane, files, getFileNames(files), projectDir, url, revision);
        }

        private void removeDirectories (Collection<VCSFileProxy> filesToScan) {
            for (Iterator<VCSFileProxy> it = filesToScan.iterator(); it.hasNext();) {
                VCSFileProxy file = it.next();
                if (!file.isFile()) {
                    it.remove();
                }
            }
        }

        private void removeNotEnabled (Collection<VCSFileProxy> filesToScan) {
            for (Iterator<VCSFileProxy> it = filesToScan.iterator(); it.hasNext();) {
                VCSFileProxy file = it.next();
                if (!isEnabled(file, true)) {
                    if (LOG.isLoggable(Level.FINER)) {
                        LOG.log(Level.FINER, "File {0} is probably not from kenai, notifications disabled", new Object[] { file.getPath() } ); //NOI18N
                    }
                    it.remove();
                }
            }
        }

        private void removeSeenFiles (Collection<VCSFileProxy> filesToScan) {
            for (Iterator<VCSFileProxy> it = filesToScan.iterator(); it.hasNext();) {
                VCSFileProxy file = it.next();
                if (isSeen(file)) {
                    it.remove();
                }
            }
        }

        private void scanFiles (Collection<VCSFileProxy> filesToScan) {
            HashMap<SVNUrl, HashSet<VCSFileProxy>> filesPerRepository = sortByRepository(filesToScan);
            for (Map.Entry<SVNUrl, HashSet<VCSFileProxy>> entry : filesPerRepository.entrySet()) {
                SVNUrl repositoryUrl = entry.getKey();
                HashMap<Long, Notification> notifications = new HashMap<Long, Notification>();
                try {
                    HashSet<VCSFileProxy> files = entry.getValue();
                    VCSFileProxy[] roots = files.toArray(new VCSFileProxy[files.size()]);
                    final Context context = new Context(roots);
                    SvnClient client = Subversion.getInstance().getClient(context, repositoryUrl);
                    if (client != null) {
                        ISVNStatus[] statuses = client.getStatus(roots);
                        for (ISVNStatus status : statuses) {
                            if ((SVNStatusKind.UNVERSIONED.equals(status.getTextStatus())
                                    || SVNStatusKind.IGNORED.equals(status.getTextStatus()))) {
                                continue;
                            }
                            VCSFileProxy file = status.getFile();
                            SVNRevision.Number rev = status.getRevision();
                            // get repository info - last revision if possible
                            ISVNInfo info = null;
                            boolean removed = false;
                            try {
                                SVNUrl url = status.getUrl();
                                if (url == null) {
                                    String canonicalPath;
                                    try {
                                        // i suspect the file to be under a symlink folder
                                        canonicalPath = VCSFileProxySupport.getCanonicalPath(file);
                                    } catch (IOException ex) {
                                        canonicalPath = null;
                                    }
                                    LOG.log(Level.WARNING, "scanFiles: though versioned it has no svn url: {0}, {1}, {2}, {3}, {4}", //NOI18N
                                            new Object[] { file, status.getFile(), status.getTextStatus(), status.getUrlString(), canonicalPath });
                                } else {
                                    info = client.getInfo(context, url, SVNRevision.HEAD, rev);
                                }
                            } catch (SVNClientException ex) {
                                LOG.log(Level.FINE, null, ex);
                                // XXX or should we run remote status to determine if the file was deleted?
                                removed = SvnClientExceptionHandler.isFileNotFoundInRevision(ex.getMessage());
                            }
                            if (info != null || removed) {
                                Long repositoryRev = removed ? null : info.getLastChangedRevision().getNumber();
                                // XXX some hack to look up deleted file's last revision
                                if (isModifiedInRepository(rev.getNumber(), repositoryRev)) {
                                    addToMap(notifications, file, repositoryRev);
                                    // this will refresh versioning view as well
                                    cache.refresh(file, new FileStatusCache.RepositoryStatus(status, null));
                                }
                            }
                        }
                        alreadySeen.addAll(files);
                    }
                } catch (SVNClientException ex) {
                    LOG.log(Level.FINE, null, ex);
                }
                notifyChanges(notifications, repositoryUrl);
            }
        }

        private boolean isModifiedInRepository (long revision, Long repositoryRevision) {
            return repositoryRevision == null // file is probably remotely deleted
                    || revision < repositoryRevision; // base revision is lower than the repository revision
        }

        /**
         * Adds new notification or adds file to already existing notification
         * @param notifications sorted by revision number
         * @param file file to add to a notification
         * @param revision repository revision
         */
        private void addToMap(HashMap<Long, Notification> notifications, VCSFileProxy file, Long revision) {
            Notification revisionNotification = notifications.get(revision);
            if (revisionNotification == null) {
                revisionNotification = new Notification(revision);
                notifications.put(revision, revisionNotification);
            }
            revisionNotification.addFile(file);
        }

        private class Notification {
            private final Set<VCSFileProxy> files;
            private final Long revision;

            Notification(Long revision) {
                files = new HashSet<VCSFileProxy>();
                this.revision = revision;
            }

            void addFile(VCSFileProxy file) {
                files.add(file);
            }

            VCSFileProxy[] getFiles () {
                return files.toArray(new VCSFileProxy[files.size()]);
            }

            Long getRevision() {
                return revision;
            }
        }

        private HashMap<SVNUrl, HashSet<VCSFileProxy>> sortByRepository (Collection<VCSFileProxy> files) {
            HashMap<SVNUrl, HashSet<VCSFileProxy>> filesByRepository = new HashMap<SVNUrl, HashSet<VCSFileProxy>>();
            for (VCSFileProxy file : files) {
                SVNUrl repositoryUrl = getRepositoryRoot(file);
                if (repositoryUrl != null) {
                    HashSet<VCSFileProxy> filesPerRepository = filesByRepository.get(repositoryUrl);
                    if (filesPerRepository == null) {
                        filesPerRepository = new HashSet<VCSFileProxy>();
                        filesByRepository.put(repositoryUrl, filesPerRepository);
                    }
                    filesPerRepository.add(file);
                }
            }
            return filesByRepository;
        }

        private void notifyChanges (HashMap<Long, Notification> notifications, SVNUrl repositoryUrl) {
            for (Map.Entry<Long, Notification> e : notifications.entrySet()) {
                Notification notification = e.getValue();
                VCSFileProxy[] files = notification.getFiles();
                Long revision = notification.getRevision();
                notifyFileChange(files, files[0].getParentFile(), repositoryUrl.toString(), revision == null ? null : revision.toString());
            }
        }

        /**
         * Returns repository root url for the given file or null if the repository is unknown or does not belong to allowed urls.
         * Currently allowed repositories are kenai repositories.
         * @param file
         * @return
         */
        private SVNUrl getRepositoryRoot (VCSFileProxy file) {
            SVNUrl repositoryUrl = null;
            SVNUrl url = getRepositoryUrl(file);
            if (url != null && kenaiAccessor.isKenai(url.toString()) && kenaiAccessor.isLogged(url.toString())) {
                repositoryUrl = url;
            }
            return repositoryUrl;
        }

        private SVNUrl getRepositoryUrl (VCSFileProxy file) {
            SVNUrl url = null;
            try {
                url = SvnUtils.getRepositoryRootUrl(file);
            } catch (SVNClientException ex) {
                LOG.log(Level.FINE, "getRepositoryUrl: No repository root url found for managed file : [" + file + "]", ex); //NOI18N
                try {
                    url = SvnUtils.getRepositoryUrl(file); // try to falback
                } catch (SVNClientException ex1) {
                    LOG.log(Level.FINE, "getRepositoryUrl: No repository url found for managed file : [" + file + "]", ex1); //NOI18N
                }
            }
            return url;
        }
    }
}

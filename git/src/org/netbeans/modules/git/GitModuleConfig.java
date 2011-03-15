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

package org.netbeans.modules.git;

import java.awt.Color;
import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import org.netbeans.libs.git.utils.GitURI;
import org.netbeans.modules.git.FileInformation.Mode;
import org.netbeans.modules.versioning.util.KeyringSupport;
import org.netbeans.modules.versioning.util.Utils;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 *
 * @author ondra
 */
public final class GitModuleConfig {
    
    private static GitModuleConfig instance;
    private static final String AUTO_OPEN_OUTPUT_WINDOW = "autoOpenOutput";     // NOI18N
    private static final String PROP_COMMIT_EXCLUSIONS  = "commitExclusions";   // NOI18N
    private static final String PROP_LAST_USED_MODE     = "lastUsedMode";       // NOI18N
    private static final String EXCLUDE_NEW_FILES       = "excludeNewFiles";    // NOI18N
    private static final String RECENT_COMMIT_AUTHORS   = "recentCommitAuhtors";// NOI18N
    private static final String RECENT_COMMITERS        = "recentCommiters";    // NOI18N
    private static final String RECENT_GURI             = "recent_guri";    
    private static final String SIGN_OFF                = "signOff";            // NOI18N
    private static final String REVERT_ALL              = "revertAll";          // NOI18N
    private static final String REMOVE_ALL_NEW          = "removeAllNew";       // NOI18N
    private static final String REVERT_INDEX            = "revertIndex";        // NOI18N
    private static final String REVERT_WT               = "revertWT";           // NOI18N
    private static final String REMOVE_WT_NEW           = "removeWTNew";        // NOI18N
    private static final String PROP_LAST_USED_COMMIT_VIEW_MODE = "lastUsedCommitViewMode"; //NOI18N
    private static final String AUTO_IGNORE_FILES       = "autoIgnoreFiles"; //NOI18N
    private static final String SHOW_CLONE_COMPLETED    = "cloneCompleted.showCloneCompleted";        // NOI18N  
    private static final String GURI_PASSWORD           = "guri_password";
    
    private static final String DELIMITER               = "<=~=>";              // NOI18N
    
    private String lastCanceledCommitMessage;
    
    public static GitModuleConfig getDefault () {
        if (instance == null) {
            instance = new GitModuleConfig();
        }
        return instance;
    }
    private Set<String> exclusions;
    
    private GitModuleConfig() { }

    public boolean isExcludedFromCommit(String path) {
        Set<String> commitExclusions = getCommitExclusions();        
        return commitExclusions.contains(path);
    }

    public Color getColor(String colorName, Color defaultColor) {
         int colorRGB = getPreferences().getInt(colorName, defaultColor.getRGB());
         return new Color(colorRGB);
    }

    public void setColor(String colorName, Color value) {
         getPreferences().putInt(colorName, value.getRGB());
    }

    public Preferences getPreferences() {
        return NbPreferences.forModule(GitModuleConfig.class);
    }

    public boolean getExludeNewFiles() {
        return getPreferences().getBoolean(EXCLUDE_NEW_FILES, false);
    }

    public void setExcludeNewFiles(boolean value) {
        getPreferences().putBoolean(EXCLUDE_NEW_FILES, value);
    }
    
    public String getLastCanceledCommitMessage() {
        return lastCanceledCommitMessage == null ? "" : lastCanceledCommitMessage; //NOI18N
    }

    public void setLastCanceledCommitMessage(String message) {
        lastCanceledCommitMessage = message;
    }
    
    /**
     * @param paths collection of paths, of File.getAbsolutePath()
     */
    public void addExclusionPaths(Collection<String> paths) {
        Set<String> commitExclusions = getCommitExclusions();
        if (commitExclusions.addAll(paths)) {
            Utils.put(getPreferences(), PROP_COMMIT_EXCLUSIONS, new ArrayList<String>(commitExclusions));
        }
    }

    /**
     * @param paths collection of paths, File.getAbsolutePath()
     */
    public void removeExclusionPaths(Collection<String> paths) {
        Set<String> commitExclusions = getCommitExclusions();
        if (commitExclusions.removeAll(paths)) {
            Utils.put(getPreferences(), PROP_COMMIT_EXCLUSIONS, new ArrayList<String>(commitExclusions));
        }
    }   
    
    synchronized Set<String> getCommitExclusions() {
        if (exclusions == null) {
            exclusions = new HashSet<String>(Utils.getStringList(getPreferences(), PROP_COMMIT_EXCLUSIONS));
        }
        return exclusions;
    }  
    
    public Mode getLastUsedModificationContext () {
        Mode mode;
        try {
            mode = Mode.valueOf(getPreferences().get(PROP_LAST_USED_MODE, Mode.HEAD_VS_WORKING_TREE.name()));
        } catch (IllegalArgumentException ex) {
            mode = null;
        }
        return mode == null ? Mode.HEAD_VS_WORKING_TREE : mode;
    }

    public void setLastUsedModificationContext (Mode mode) {
        getPreferences().put(PROP_LAST_USED_MODE, mode.name());
    }    

    public Mode getLastUsedCommitViewMode () {
        Mode mode;
        try {
            mode = Mode.valueOf(getPreferences().get(PROP_LAST_USED_COMMIT_VIEW_MODE, Mode.HEAD_VS_WORKING_TREE.name()));
        } catch (IllegalArgumentException ex) {
            mode = null;
        }
        return mode == null ? Mode.HEAD_VS_WORKING_TREE : mode;
    }

    public void setLastUsedCommitViewMode (Mode mode) {
        getPreferences().put(PROP_LAST_USED_COMMIT_VIEW_MODE, mode.name());
    }
    
    public boolean getAutoOpenOutput() {
        return getPreferences().getBoolean(AUTO_OPEN_OUTPUT_WINDOW, true);
    }

    public void setAutoOpenOutput(boolean value) {
        getPreferences().putBoolean(AUTO_OPEN_OUTPUT_WINDOW, value);
    }

    public void putRecentCommitAuthors(String author) {
        if(author == null) return;
        author = author.trim();
        if(author.isEmpty()) return;
        Utils.insert(getPreferences(), RECENT_COMMIT_AUTHORS, author, 10);
    }
    
    public void putRecentCommiter(String commiter) {
        if(commiter == null) return;
        commiter = commiter.trim();
        if(commiter.isEmpty()) return;
        Utils.insert(getPreferences(), RECENT_COMMITERS, commiter, 10);
    }
    
    public List<String> getRecentCommitAuthors() {
        return Utils.getStringList(getPreferences(), RECENT_COMMIT_AUTHORS);
    }
    
    public List<String> getRecentCommiters() {
        return Utils.getStringList(getPreferences(), RECENT_COMMITERS);
    }

    public void setSignOff(boolean value) {
        getPreferences().putBoolean(SIGN_OFF, value);
    }
    
    public boolean getSignOff() {
        return getPreferences().getBoolean(SIGN_OFF, false);
    }
    
    public void putRevertAll(boolean value) {
        getPreferences().putBoolean(REVERT_ALL, value);        
    }
    
    public void putRevertIndex(boolean value) {
        getPreferences().putBoolean(REVERT_INDEX, value);        
    }
    
    public void putRevertWT(boolean value) {
        getPreferences().putBoolean(REVERT_WT, value);        
    }
    
    public void putRemoveAllNew(boolean value) {
        getPreferences().putBoolean(REMOVE_ALL_NEW, value);        
    }
    
    public void putRemoveWTNew(boolean value) {
        getPreferences().putBoolean(REMOVE_WT_NEW, value);        
    }
    
    public boolean getRevertAll() {
        return getPreferences().getBoolean(REVERT_ALL, true);        
    }
    
    public boolean getRevertIndex() {
        return getPreferences().getBoolean(REVERT_INDEX, false);        
    }
    
    public boolean getRevertWT() {
        return getPreferences().getBoolean(REVERT_WT, true);        
    }
        
    public boolean getRemoveWTNew() {
        return getPreferences().getBoolean(REMOVE_WT_NEW, false);                
    }
    
    public boolean getRemoveAllNew() {
        return getPreferences().getBoolean(REMOVE_ALL_NEW, false);                
    }

    public boolean getAutoIgnoreFiles () {
        return getPreferences().getBoolean(AUTO_IGNORE_FILES, true);
    }

    public void setAutoIgnoreFiles (boolean flag) {
        getPreferences().putBoolean(AUTO_IGNORE_FILES, flag);
    }

    public boolean getShowCloneCompleted() {
        return getPreferences().getBoolean(SHOW_CLONE_COMPLETED, true);
    }
    
    public void setShowCloneCompleted(boolean bl) {
        getPreferences().putBoolean(SHOW_CLONE_COMPLETED, bl);
    }
    
    public void insertRecentGitURI(GitURI guri, boolean savePassword) {
        if(guri == null) {
            return;
        }
        assert !EventQueue.isDispatchThread();
                
        Preferences prefs = getPreferences();

        List<String> urlValues = Utils.getStringList(prefs, RECENT_GURI);
        for (Iterator<String> it = urlValues.iterator(); it.hasNext();) {
            String rcOldString = it.next();
            GitURI guriOld = null;
            try {
                guriOld = new GitURI(rcOldString);
            } catch (URISyntaxException ex) {
                Git.LOG.log(Level.WARNING, rcOldString, ex);
            }
            if(guri.equals(guriOld)) {
                Utils.removeFromArray(prefs, RECENT_GURI, rcOldString);
            }
        }
        
        if(savePassword) {
            storeCredentials(guri);
        }
        
        String guriString = guri.toString();
        if (!"".equals(guriString)) {                                           //NOI18N
            Utils.insert(prefs, RECENT_GURI, new GitURIEntry(guriString, savePassword).toString() , -1);
        }
    }    

//    public void setRecentUrls(List<GitURI> recentGuris, handle save password) {
//        assert !EventQueue.isDispatchThread();
//        
//        List<String> urls = new ArrayList<String>(recentGuris.size());
//        
//        int idx = 0;
//        for (Iterator<GitURI> it = recentGuris.iterator(); it.hasNext();) {
//            idx++;
//            GitURI guri = it.next();
//            storeCredentials(guri);
//            String guriString = guri.toString();
//            if (!"".equals(guriString)) {                                       //NOI18N
//                urls.add(guriString);
//            }
//        }
//        Utils.put( getPreferences(), RECENT_GURI, urls);
//    }
    
    public List<GitURI> getRecentUrls() {
        assert !EventQueue.isDispatchThread();
        
        Preferences prefs = getPreferences();
        List<String> urls = Utils.getStringList(prefs, RECENT_GURI);
        List<GitURI> ret = new ArrayList<GitURI>(urls.size());
        for (String guriString : urls) {
            GitURIEntry entry = GitURIEntry.create(guriString);
            GitURI guri;
            try {
                guri = new GitURI(entry.guriString);
            } catch (URISyntaxException ex) {
                Git.LOG.log(Level.WARNING, guriString, ex);
                continue;
            }
            char[] password = entry.savePassword ? KeyringSupport.read(GURI_PASSWORD, guri.toString()) : null;
            if(password != null) {
                guri.setPass(new String(password));
            }
            ret.add(guri);
        }
        return ret;
    }

    private void storeCredentials (final GitURI guri) {
        if ((guri.getPass() != null)) {
            Runnable outOfAWT = new Runnable() {
                @Override
                public void run() {
                    String url = guri.toString();
                    String passwd = guri.getPass();
                    KeyringSupport.save(GURI_PASSWORD, url, passwd.toCharArray(), null);

                }
            };
            // keyring should be called only in a background thread
            if (EventQueue.isDispatchThread()) {
                Git.getInstance().getRequestProcessor().post(outOfAWT);
            } else {
                outOfAWT.run();
            }
        }
    }
    
    private static class GitURIEntry {
        final String guriString;
        final boolean savePassword;
        private String stringValue;
        static GitURIEntry create(String entryString) {
            String[] s = entryString.split(DELIMITER);
            assert s.length == 2;
            if(s.length < 2) {
                return null;
            }
            return new GitURIEntry(s[0], Boolean.parseBoolean(s[1]));
        }
        GitURIEntry(String guriString, boolean savePassword) {
            this.guriString = guriString;
            this.savePassword = savePassword;
        }
        @Override
        public String toString() {
            if(stringValue == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(guriString);
                sb.append(DELIMITER);
                sb.append(savePassword);
                stringValue = sb.toString();
            }
            return stringValue;
        }
    }
    
}

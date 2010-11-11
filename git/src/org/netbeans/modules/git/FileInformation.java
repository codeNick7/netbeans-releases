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

import java.io.File;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Set;
import org.netbeans.libs.git.GitConflictDescriptor.Type;
import org.netbeans.libs.git.GitStatus;
import org.netbeans.modules.versioning.util.common.VCSFileInformation;
import org.openide.util.NbBundle;

/**
 *
 * @author ondra
 */
public class FileInformation extends VCSFileInformation {
    private final EnumSet<Status> status;
    private boolean seenInUI;
    private final boolean directory;
    private final boolean renamed, copied;
    private final File oldFile;
    private Type conflictType;

    FileInformation (EnumSet<Status> status, boolean isDirectory) {
        this.status = status;
        this.directory = isDirectory;
        renamed = copied = false;
        oldFile = null;
    }

    FileInformation (GitStatus status) {
        directory = status.isFolder();
        seenInUI = true;
        renamed = status.isRenamed();
        copied = status.isCopied();
        oldFile = status.getOldPath();
        if (!status.isTracked()) {
            this.status = GitStatus.Status.STATUS_IGNORED.equals(status.getStatusIndexWC()) ? EnumSet.of(Status.NOTVERSIONED_EXCLUDED)
                    : EnumSet.of(Status.NEW_INDEX_WORKING_TREE, Status.NEW_HEAD_WORKING_TREE);
        } else if (status.isConflict()) {
            this.status = EnumSet.of(Status.IN_CONFLICT);
            this.conflictType = status.getConflictDescriptor().getType();
        } else {
            GitStatus.Status statusHeadIndex = status.getStatusHeadIndex();
            GitStatus.Status statusIndexWC = status.getStatusIndexWC();
            GitStatus.Status statusHeadWC = status.getStatusHeadWC();
            EnumSet<Status> s = EnumSet.noneOf(Status.class);
            if (GitStatus.Status.STATUS_ADDED.equals(statusHeadIndex)) {
                s.add(Status.NEW_HEAD_INDEX);
            } else if (GitStatus.Status.STATUS_MODIFIED.equals(statusHeadIndex)) {
                s.add(Status.MODIFIED_HEAD_INDEX);
            } else if (GitStatus.Status.STATUS_REMOVED.equals(statusHeadIndex)) {
                s.add(Status.REMOVED_HEAD_INDEX);
            }
            if (GitStatus.Status.STATUS_ADDED.equals(statusIndexWC)) {
                s.add(Status.NEW_INDEX_WORKING_TREE);
            } else if (GitStatus.Status.STATUS_MODIFIED.equals(statusIndexWC)) {
                s.add(Status.MODIFIED_INDEX_WORKING_TREE);
            } else if (GitStatus.Status.STATUS_REMOVED.equals(statusIndexWC)) {
                s.add(Status.REMOVED_INDEX_WORKING_TREE);
            }
            if (GitStatus.Status.STATUS_MODIFIED.equals(statusHeadWC)) {
                s.add(Status.MODIFIED_HEAD_WORKING_TREE);
            } else if (GitStatus.Status.STATUS_ADDED.equals(statusHeadWC)) {
                s.add(Status.NEW_HEAD_WORKING_TREE);
            }
            // file is removed in the WT, but is NOT in HEAD yet
            // or is removed both in WT and index
            if (GitStatus.Status.STATUS_REMOVED.equals(statusIndexWC) && !GitStatus.Status.STATUS_ADDED.equals(statusHeadIndex)
                    || GitStatus.Status.STATUS_REMOVED.equals(statusHeadIndex) && GitStatus.Status.STATUS_NORMAL.equals(statusIndexWC)) {
                s.add(Status.REMOVED_HEAD_WORKING_TREE);
            }
            if (s.isEmpty()) {
                s.add(Status.UPTODATE);
            }
            this.status = s;
        }
    }

    public boolean containsStatus (Set<Status> includeStatus) {
        EnumSet<Status> intersection = status.clone();
        intersection.retainAll(includeStatus);
        return !intersection.isEmpty();
    }

    public boolean containsStatus (Status includeStatus) {
        return containsStatus(EnumSet.of(includeStatus));
    }

    void setSeenInUI (boolean flag) {
        this.seenInUI = flag;
    }

    boolean seenInUI () {
        return seenInUI;
    }

    public Set<Status> getStatus() {
        return status;
    }
    
    public boolean isDirectory () {
        return this.directory;
    }

    /**
     * Gets integer status that can be used in comparators. The more important the status is for the user,
     * the lower value it has. Conflict is 0, unknown status is 100.
     *
     * @return status constant suitable for 'by importance' comparators
     */
    @Override
    public int getComparableStatus () {
        if (containsStatus(Status.IN_CONFLICT)) {
            return 0;
        } else if (containsStatus(Status.UPTODATE)) {
            return 850;
        } else if (containsStatus(Status.NOTVERSIONED_EXCLUDED)) {
            return 900;
        } else if (containsStatus(Status.NOTVERSIONED_NOTMANAGED)) {
            return 901;
        } else if (containsStatus(Status.UNKNOWN)) {
            return 902;
        }
        int value = 400;
        if (containsStatus(Status.REMOVED_HEAD_WORKING_TREE)) {
            value -= 100;
        }
        if (containsStatus(Status.MODIFIED_HEAD_WORKING_TREE)) {
            value -= 100;
        }
        if (containsStatus(Status.NEW_HEAD_WORKING_TREE)) {
            value -= 100;
        }
        if (containsStatus(Status.REMOVED_HEAD_INDEX)) {
            value -= 10;
        }
        if (containsStatus(Status.MODIFIED_HEAD_INDEX)) {
            value -= 10;
        }
        if (containsStatus(Status.NEW_HEAD_INDEX)) {
            value -= 10;
        }
        if (containsStatus(Status.REMOVED_INDEX_WORKING_TREE)) {
            value -= 1;
        }
        if (containsStatus(Status.MODIFIED_INDEX_WORKING_TREE)) {
            value -= 1;
        }
        if (containsStatus(Status.NEW_INDEX_WORKING_TREE)) {
            value -= 1;
        }
        if (value == 400) {
            throw new IllegalArgumentException("Uncomparable status: " + getStatus()); //NOI18N
        }
        return value;
    }

    public String getShortStatusText() {
        String sIndex = ""; //NOI18N
        String sWorkingTree = ""; //NOI18N
                
        if(containsStatus(Status.NEW_HEAD_INDEX)) {
            if (isRenamed()) {
                sIndex = "R"; //NOI18N
            } else if (isCopied()) {
                sIndex = "C"; //NOI18N
            } else {
                sIndex = "A"; //NOI18N
            }
        } else if(containsStatus(Status.MODIFIED_HEAD_INDEX)) {
            sIndex = "M"; //NOI18N
        } else if(containsStatus(Status.REMOVED_HEAD_INDEX)) {
            sIndex = "D"; //NOI18N
        } else {
            sIndex = "-"; //NOI18N
        }
        
        if(containsStatus(Status.NEW_INDEX_WORKING_TREE)) {
            sWorkingTree = "A"; //NOI18N
        } else if(containsStatus(Status.MODIFIED_INDEX_WORKING_TREE)) {
            sWorkingTree = "M"; //NOI18N
        } else if(containsStatus(Status.REMOVED_INDEX_WORKING_TREE)) {
            sWorkingTree = "D"; //NOI18N
        } else {
            sWorkingTree = "-"; //NOI18N
        }
        
        if (containsStatus(Status.NOTVERSIONED_EXCLUDED)) {
            return "I"; //NOI18N
        } else if (containsStatus(Status.IN_CONFLICT)) {
            switch (conflictType) {
                case ADDED_BY_THEM:
                    return "UA"; //NOI18N
                case ADDED_BY_US:
                    return "AU"; //NOI18N
                case BOTH_ADDED:
                    return "AA"; //NOI18N
                case BOTH_DELETED:
                    return "DD"; //NOI18N
                case BOTH_MODIFIED:
                    return "UU"; //NOI18N
                case DELETED_BY_THEM:
                    return "UD"; //NOI18N
                case DELETED_BY_US:
                    return "DU"; //NOI18N
                default:
                    throw new IllegalStateException("Unknown conflict type: " + conflictType.toString()); //NOI18N
            }
        } else if ("-".equals(sIndex) && "-".equals(sWorkingTree)) { //NOI18N
            return ""; //NOI18N
        } else {
            return new MessageFormat("{0}/{1}").format(new Object[] {sIndex, sWorkingTree}, new StringBuffer(), null).toString(); //NOI18N
        }
    }

    @Override
    public String getStatusText () {
        return getStatusText(Mode.HEAD_VS_WORKING_TREE);
    }

    public String getStatusText (Mode mode) {
        String sIndex = ""; //NOI18N
        String sWorkingTree = ""; //NOI18N

        if (containsStatus(Status.NEW_HEAD_INDEX)) {
            if (isRenamed()) {
                sIndex = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_AddedRenamed_Short"); //NOI18N
            } else if (isCopied()) {
                sIndex = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_AddedCopied_Short"); //NOI18N
            } else {
                sIndex = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_New_Short"); //NOI18N
            }
        } else if (containsStatus(Status.MODIFIED_HEAD_INDEX)) {
            sIndex = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Modified_Short"); //NOI18N
        } else if (containsStatus(Status.REMOVED_HEAD_INDEX)) {
            sIndex = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Removed_Short"); //NOI18N
        } else {
            sIndex = "-"; //NOI18N
        }

        if (containsStatus(Status.NEW_INDEX_WORKING_TREE)) {
            sWorkingTree = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_New_Short"); //NOI18N
        } else if (containsStatus(Status.MODIFIED_INDEX_WORKING_TREE)) {
            sWorkingTree = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Modified_Short"); //NOI18N
        } else if (containsStatus(Status.REMOVED_INDEX_WORKING_TREE)) {
            sWorkingTree = NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Removed_Short"); //NOI18N
        } else {
            sWorkingTree = "-"; //NOI18N
        }

        if (containsStatus(Status.NOTVERSIONED_EXCLUDED)) {
            return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Excluded_Short"); //NOI18N
        } else if (containsStatus(Status.IN_CONFLICT)) {
            switch (conflictType) {
                case ADDED_BY_THEM:
                    return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Conflict_AddedByThem"); //NOI18N
                case ADDED_BY_US:
                    return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Conflict_AddedByUs"); //NOI18N
                case BOTH_ADDED:
                    return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Conflict_BothAdded"); //NOI18N
                case BOTH_DELETED:
                    return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Conflict_BothDeleted"); //NOI18N
                case BOTH_MODIFIED:
                    return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Conflict_BothModified"); //NOI18N
                case DELETED_BY_THEM:
                    return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Conflict_DeletedByThem"); //NOI18N
                case DELETED_BY_US:
                    return NbBundle.getMessage(FileInformation.class, "CTL_FileInfoStatus_Conflict_DeletedByUs"); //NOI18N
                default:
                    throw new IllegalStateException("Unknown conflict type: " + conflictType.toString()); //NOI18N
            }
        } else if (Mode.HEAD_VS_INDEX.equals(mode)) {
            return new MessageFormat("{0}").format(new Object[] { sIndex }); //NOI18N
        } else if (Mode.INDEX_VS_WORKING_TREE.equals(mode)) {
            return new MessageFormat("{0}").format(new Object[] { sWorkingTree }); //NOI18N
        } else {
            return new MessageFormat("{0}/{1}").format(new Object[] { sIndex, sWorkingTree }, new StringBuffer(), null).toString(); //NOI18N
        }
    }

    public boolean isRenamed () {
        return renamed;
    }

    public boolean isCopied () {
        return copied;
    }

    public File getOldFile () {
        return oldFile;
    }

    @Override
    public String annotateNameHtml(String name) {
        return ((Annotator) Git.getInstance().getVCSAnnotator()).annotateNameHtml(name, this, null);
    }

    public static enum Mode {
        HEAD_VS_WORKING_TREE,
        HEAD_VS_INDEX,
        INDEX_VS_WORKING_TREE
    }
    
    public static enum Status {

        /**
         * There is nothing known about the file, it may not even exist.
         */
        UNKNOWN,
        /**
         * The file is not managed by the module, i.e. the user does not wish it to be under control of this
         * versioning system module. All files except files under versioned roots have this status.
         */
        NOTVERSIONED_NOTMANAGED,
        /**
         * The file exists locally but is NOT under version control because it should not be (i.e. is ignored or resides under an excluded folder).
         * The file itself IS under a versioned root.
         */
        NOTVERSIONED_EXCLUDED,
        /**
         * The file has been added to index but does not exist in repository yet.
         */
        NEW_HEAD_INDEX,
        /**
         * The file exists locally but is NOT in index.
         */
        NEW_INDEX_WORKING_TREE,
        /**
         * The file exists locally but is not in HEAD. Even though this is redundant, we need it to quickly get files for HEAD/WT mode.
         * Remember the state: removed in index and recreated in working tree.
         */
        NEW_HEAD_WORKING_TREE,
        /**
         * The file is under version control and is in sync with repository.
         */
        UPTODATE,
        /**
         * There's a modification between HEAD and index versions of the file
         */
        MODIFIED_HEAD_INDEX,
        /**
         * There's a modification between HEAD and working tree versions of the file
         */
        MODIFIED_HEAD_WORKING_TREE,
        /**
         * There's a modification between index and working tree versions of the file
         */
        MODIFIED_INDEX_WORKING_TREE,
        /**
         * Merging during update resulted in merge conflict. Conflicts in the local copy must be resolved before the file can be commited.
         */
        IN_CONFLICT,
        /**
         * The file does NOT exist in index but does in HEAD, it has beed removed from index, waits for commit.
         */
        REMOVED_HEAD_INDEX,
        /**
         * The file has been removed in the working tree
         */
        REMOVED_INDEX_WORKING_TREE,
        /**
         * The file does NOT exist in the working tree but does in HEAD.
         */
        REMOVED_HEAD_WORKING_TREE
    }
    public static final EnumSet<Status> STATUS_ALL = EnumSet.allOf(Status.class);
    public static final EnumSet<Status> STATUS_MANAGED = EnumSet.complementOf(EnumSet.of(Status.NOTVERSIONED_NOTMANAGED));
    public static final EnumSet<Status> STATUS_REMOVED = EnumSet.of(Status.REMOVED_HEAD_INDEX,
                                                                    Status.REMOVED_INDEX_WORKING_TREE);
    public static final EnumSet<Status> STATUS_LOCAL_CHANGES = EnumSet.of(
            Status.NEW_HEAD_INDEX,
            Status.NEW_INDEX_WORKING_TREE,
            Status.NEW_HEAD_WORKING_TREE,
            Status.IN_CONFLICT,
            Status.REMOVED_HEAD_INDEX,
            Status.REMOVED_INDEX_WORKING_TREE,
            Status.REMOVED_HEAD_WORKING_TREE,
            Status.MODIFIED_HEAD_INDEX,
            Status.MODIFIED_HEAD_WORKING_TREE,
            Status.MODIFIED_INDEX_WORKING_TREE);

    public static final EnumSet<Status> STATUS_MODIFIED_HEAD_VS_WORKING = EnumSet.of(
            Status.NEW_HEAD_WORKING_TREE,
            Status.IN_CONFLICT,
            Status.REMOVED_HEAD_WORKING_TREE,
            Status.MODIFIED_HEAD_WORKING_TREE);
    public static final EnumSet<Status> STATUS_MODIFIED_HEAD_VS_INDEX = EnumSet.of(
            Status.NEW_HEAD_INDEX,
            Status.IN_CONFLICT,
            Status.REMOVED_HEAD_INDEX,
            Status.MODIFIED_HEAD_INDEX);
    public static final EnumSet<Status> STATUS_MODIFIED_INDEX_VS_WORKING = EnumSet.of(
            Status.NEW_INDEX_WORKING_TREE,
            Status.IN_CONFLICT,
            Status.REMOVED_INDEX_WORKING_TREE,
            Status.MODIFIED_INDEX_WORKING_TREE);
}

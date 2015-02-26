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

package org.netbeans.libs.git.remote.jgit.commands;

import java.text.MessageFormat;
import org.netbeans.libs.git.remote.GitException;
import org.netbeans.libs.git.remote.jgit.GitClassFactory;
import org.netbeans.libs.git.remote.jgit.JGitRepository;
import org.netbeans.libs.git.remote.jgit.Utils;
import org.netbeans.libs.git.remote.progress.FileListener;
import org.netbeans.libs.git.remote.progress.ProgressMonitor;
import org.netbeans.modules.remotefs.versioning.api.ProcessUtils;
import org.netbeans.modules.versioning.core.api.VCSFileProxy;

/**
 *
 * @author ondra
 */
public class CopyCommand extends GitCommand {
    private final VCSFileProxy source;
    private final VCSFileProxy target;
    private final ProgressMonitor monitor;
    private final FileListener listener;

    public CopyCommand (JGitRepository repository, GitClassFactory gitFactory, VCSFileProxy source, VCSFileProxy target, ProgressMonitor monitor, FileListener listener) {
        super(repository, gitFactory, monitor);
        this.source = source;
        this.target = target;
        this.monitor = monitor;
        this.listener = listener;
    }
    
    @Override
    protected boolean prepareCommand() throws GitException {
        boolean retval = super.prepareCommand();
        if (retval) {
            VCSFileProxy workTree = getRepository().getLocation();
            String relPathToSource = Utils.getRelativePath(workTree, source);
            String relPathToTarget = Utils.getRelativePath(workTree, target);
            if (relPathToSource.startsWith(relPathToTarget + "/")) { //NOI18N
                monitor.preparationsFailed(MessageFormat.format(Utils.getBundle(CopyCommand.class).getString("MSG_Error_SourceFolderUnderTarget"), new Object[] { relPathToSource, relPathToTarget } )); //NOI18N
                throw new GitException(MessageFormat.format(Utils.getBundle(CopyCommand.class).getString("MSG_Error_SourceFolderUnderTarget"), new Object[] { relPathToSource, relPathToTarget } )); //NOI18N
            } else if (relPathToTarget.startsWith(relPathToSource + "/")) { //NOI18N
                monitor.preparationsFailed(MessageFormat.format(Utils.getBundle(CopyCommand.class).getString("MSG_Error_TargetFolderUnderSource"), new Object[] { relPathToTarget, relPathToSource } )); //NOI18N
                throw new GitException(MessageFormat.format(Utils.getBundle(CopyCommand.class).getString("MSG_Error_TargetFolderUnderSource"), new Object[] { relPathToTarget, relPathToSource } )); //NOI18N
            }
        }
        return retval;
    }
    
    @Override
    protected void prepare() throws GitException {
        setCommandsNumber(2);
        super.prepare();
        addArgument(0, "mv"); //NOI18N
        addArgument(0, "--verbose"); //NOI18N
        addArgument(0, "-f"); //NOI18N
        addArgument(0, Utils.getRelativePath(getRepository().getLocation(), source));
        addArgument(0, Utils.getRelativePath(getRepository().getLocation(), target));
        
        addArgument(1, "checkout"); //NOI18N
        addArgument(1, "HEAD");
        addArgument(1, "--");
        addArgument(1, Utils.getRelativePath(getRepository().getLocation(), source));
    }
    
    @Override
    protected void run() throws GitException {
        ProcessUtils.Canceler canceled = new ProcessUtils.Canceler();
        if (monitor != null) {
            monitor.setCancelDelegate(canceled);
        }
        try {
            new Runner(canceled, 0){

                @Override
                public void outputParser(String output) throws GitException {
                    parseMoveOutput(output);
                }

                @Override
                protected void errorParser(String error) throws GitException {
                    System.err.println(error);
                }
                
            }.runCLI();

            new Runner(canceled, 1){

                @Override
                public void outputParser(String output) throws GitException {
                    System.err.println(output);
                }

                @Override
                protected void errorParser(String error) throws GitException {
                    System.err.println(error);
                }
                
            }.runCLI();
        } catch (GitException t) {
            throw t;
        } catch (Throwable t) {
            if (canceled.canceled()) {
            } else {
                throw new GitException(t);
            }
        }
    }
    
    private void parseMoveOutput(String output) {
        //Renaming file to folder/file
        for (String line : output.split("\n")) { //NOI18N
            if (line.startsWith("Renaming")) {
                String[] s = line.split(" ");
                String file = s[s.length-1];
                listener.notifyFile(VCSFileProxy.createFileProxy(getRepository().getLocation(), file), file);
            }
        }
    }
}

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
package org.netbeans.modules.mercurial.remote.ui.pull;

import java.util.Set;
import org.netbeans.modules.mercurial.remote.HgException;
import org.netbeans.modules.mercurial.remote.HgProgressSupport;
import org.netbeans.modules.mercurial.remote.Mercurial;
import org.netbeans.modules.mercurial.remote.ui.actions.ContextAction;
import org.netbeans.modules.mercurial.remote.util.HgCommand;
import org.netbeans.modules.mercurial.remote.util.HgUtils;
import org.netbeans.modules.versioning.core.api.VCSFileProxy;
import org.netbeans.modules.versioning.core.spi.VCSContext;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Ondrej Vrabec
 */
@Messages({
    "CTL_MenuItem_PullBranchLocal=&Pull Current Branch",
    "# {0} - repository folder name",
    "CTL_MenuItem_PullBranchRoot=&Pull Current Branch - {0}"
})
@ActionID(id = "org.netbeans.modules.mercurial.remote.ui.pull.PullCurrentBranchAction", category = "MercurialRemote")
@ActionRegistration(lazy = false, displayName = "#CTL_MenuItem_PullBranchLocal")
public class PullCurrentBranchAction extends ContextAction {
    
    public static final String ICON_RESOURCE = "org/netbeans/modules/mercurial/remote/resources/icons/pull.png"; //NOI18N
    
    public PullCurrentBranchAction () {
        super(ICON_RESOURCE);
    }
    
    @Override
    protected boolean enable(Node[] nodes) {
        VCSContext context = HgUtils.getCurrentContext(nodes);
        return HgUtils.isFromHgRepository(context);
    }
    
    @Override
    protected String getBaseName(Node[] nodes) {
        return "CTL_MenuItem_PullBranchLocal"; //NOI18N
    }

    @Override
    protected String iconResource () {
        return ICON_RESOURCE;
    }

    @Override
    public String getName(String role, Node[] activatedNodes) {
        VCSContext ctx = HgUtils.getCurrentContext(activatedNodes);
        Set<VCSFileProxy> roots = HgUtils.getRepositoryRoots(ctx);
        return roots.size() == 1 
                ? NbBundle.getMessage(PullCurrentBranchAction.class, "CTL_MenuItem_PullBranchRoot", roots.iterator().next().getName()) //NOI18N
                : NbBundle.getMessage(PullCurrentBranchAction.class, "CTL_MenuItem_PullBranchLocal"); //NOI18N
    }

    @Override
    @Messages({
        "# {0} - branch name", "MSG_PULL_BRANCH_PROGRESS=Pulling {0}"
    })
    protected void performContextAction (Node[] nodes) {
        final VCSContext context = HgUtils.getCurrentContext(nodes);
        final Set<VCSFileProxy> repositoryRoots = HgUtils.getRepositoryRoots(context);
        // run the whole bulk operation in background
        Mercurial.getInstance().getRequestProcessor().post(new Runnable() {
            @Override
            public void run() {
                for (VCSFileProxy repositoryRoot : repositoryRoots) {
                    final VCSFileProxy root = repositoryRoot;
                    final String branch;
                    try {
                        branch = HgCommand.getBranch(root);
                        RequestProcessor rp = Mercurial.getInstance().getRequestProcessor(root);
                        // run every repository fetch in its own support with its own output window
                        HgProgressSupport support = new HgProgressSupport() {
                            @Override
                            public void perform() {
                                PullAction.getDefaultAndPerformPull(root, null, branch, this);
                            }
                        };
                        support.start(rp, root, Bundle.MSG_PULL_BRANCH_PROGRESS(branch)).waitFinished();
                        if (support.isCanceled()) {
                            break;
                        }
                    } catch (HgException.HgCommandCanceledException ex) {
                        // canceled by user, do nothing
                    } catch (HgException ex) {
                        HgUtils.notifyException(ex);
                    }
                }
            }
        });
    }
}

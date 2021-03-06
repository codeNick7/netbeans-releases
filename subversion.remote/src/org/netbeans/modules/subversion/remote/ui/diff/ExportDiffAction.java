/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2009 Sun
 * Microsystems, Inc. All Rights Reserved.
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
 */

package org.netbeans.modules.subversion.remote.ui.diff;

import org.netbeans.modules.remotefs.versioning.api.ExportDiffSupport;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import org.netbeans.modules.diff.builtin.visualizer.TextDiffVisualizer;
import org.netbeans.modules.versioning.util.Utils;
import org.netbeans.api.diff.Difference;
import org.netbeans.spi.diff.DiffProvider;
import org.openide.windows.TopComponent;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.awt.StatusDisplayer;
import java.util.*;
import java.util.List;
import org.netbeans.modules.proxy.Base64Encoder;
import org.netbeans.modules.subversion.remote.FileInformation;
import org.netbeans.modules.subversion.remote.Subversion;
import org.netbeans.modules.subversion.remote.SvnModuleConfig;
import org.netbeans.modules.subversion.remote.api.SVNClientException;
import org.netbeans.modules.subversion.remote.client.SvnClientExceptionHandler;
import org.netbeans.modules.subversion.remote.client.SvnProgressSupport;
import org.netbeans.modules.subversion.remote.ui.actions.ContextAction;
import org.netbeans.modules.subversion.remote.util.Context;
import org.netbeans.modules.subversion.remote.util.SvnUtils;
import org.netbeans.modules.remotefs.versioning.api.VCSFileProxySupport;
import org.netbeans.modules.versioning.core.api.VCSFileProxy;

/**
 * Exports diff to file:
 *
 * <ul>
 * <li>for components that implements {@link DiffSetupSource} interface
 * exports actually displayed diff.
 *
 * <li>for DataNodes <b>local</b> differencies between the current
 * working copy and BASE repository version.
 * </ul>
 *  
 * @author Petr Kuzel
 */
public class ExportDiffAction extends ContextAction {
    
    private static final int enabledForStatus =
            FileInformation.STATUS_VERSIONED_MERGE |
            FileInformation.STATUS_VERSIONED_MODIFIEDLOCALLY |
            FileInformation.STATUS_VERSIONED_DELETEDLOCALLY |
            FileInformation.STATUS_VERSIONED_REMOVEDLOCALLY |
            FileInformation.STATUS_NOTVERSIONED_NEWLOCALLY |
            FileInformation.STATUS_VERSIONED_ADDEDLOCALLY;

    @Override
    protected String getBaseName(Node [] activatedNodes) {
        return "CTL_MenuItem_ExportDiff";  // NOI18N
    }

    /**
     * First look for DiffSetupSource name then for super (context name).
     */
    @Override
    public String getName() {
        TopComponent activated = TopComponent.getRegistry().getActivated();
        if (activated instanceof DiffSetupSource) {
            String setupName = ((DiffSetupSource)activated).getSetupDisplayName();
            if (setupName != null) {
                return NbBundle.getMessage(this.getClass(), getBaseName(getActivatedNodes()) + "_Context",  // NOI18N
                                            setupName);
            }
        }
        return super.getName();
    }
    
    @Override
    public boolean enable(Node[] nodes) {
        if (super.enable(nodes)) {
            Context ctx = getCachedContext(nodes);
            if(!Subversion.getInstance().getStatusCache().containsFiles(ctx, enabledForStatus, true)) {
                return false;
            }  
            TopComponent activated = TopComponent.getRegistry().getActivated();
            if (activated instanceof DiffSetupSource) {
                return true;
            }
            return Lookup.getDefault().lookup(DiffProvider.class) != null;                
        } else {
            return false;
        }
    }

    @Override
    protected void performContextAction(Node[] nodes) {
        performContextAction(nodes, false);
    }

    void performContextAction (final Node[] nodes, final boolean singleDiffSetup) {
        // reevaluate fast enablement logic guess
        boolean noop;
        final Context context = getContext(nodes);
        if(!Subversion.getInstance().checkClientAvailable(context)) {            
            return;
        }
        TopComponent activated = TopComponent.getRegistry().getActivated();
        if (activated instanceof DiffSetupSource) {
            noop = ((DiffSetupSource) activated).getSetups().isEmpty();
        } else {
            noop = !Subversion.getInstance().getStatusCache().containsFiles(context, FileInformation.STATUS_LOCAL_CHANGE, true);
        }
        if (noop) {
            NotifyDescriptor msg = new NotifyDescriptor.Message(NbBundle.getMessage(ExportDiffAction.class, "BK3001"), NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(msg);
            return;
        }

        ExportDiffSupport exportDiffSupport = new ExportDiffSupport(context.getRootFiles(), SvnModuleConfig.getDefault(context.getFileSystem()).getPreferences()) {
            @Override
            public void writeDiffFile(final VCSFileProxy toFile) {
                RequestProcessor rp = Subversion.getInstance().getRequestProcessor();
                SvnProgressSupport ps = new SvnProgressSupport(context.getFileSystem()) {
                    @Override
                    protected void perform() {
                        async(this, nodes, toFile, singleDiffSetup);
                    }
                };
                ps.start(rp, null, getRunningName(nodes)).waitFinished();
            }
        };
        exportDiffSupport.export();
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    private void async(SvnProgressSupport progress, Node[] nodes, VCSFileProxy destination, boolean singleDiffSetup) {
        // prepare setups and common parent - root

        VCSFileProxy root;
        List<Setup> setups;

        TopComponent activated = TopComponent.getRegistry().getActivated();
        if (activated instanceof DiffSetupSource) {
            if (!singleDiffSetup) {
                setups = new ArrayList<>(((DiffSetupSource) activated).getSetups());
            } else {
                if (nodes.length > 0 && nodes[0] instanceof DiffNode) {
                    setups = new ArrayList<>(Collections.singletonList(((DiffNode)nodes[0]).getSetup()));
                } else {
                    return;
                }
            }
            List<VCSFileProxy> setupFiles = new ArrayList<>(setups.size());
            for (Iterator<Setup> i = setups.iterator(); i.hasNext();) {
                Setup setup = i.next();
                setupFiles.add(setup.getBaseFile()); 
            }
            root = getCommonParent(setupFiles.toArray(new VCSFileProxy[setupFiles.size()]));
        } else {
            Context context = getContext(nodes);
            VCSFileProxy [] files = SvnUtils.getModifiedFiles(context, FileInformation.STATUS_LOCAL_CHANGE);
            root = getCommonParent(context.getRootFiles());
            setups = new ArrayList<>(files.length);
            for (int i = 0; i < files.length; i++) {
                VCSFileProxy file = files[i];
                if (!Subversion.getInstance().getStatusCache().getStatus(file).isDirectory()) {
                    Setup setup = new Setup(file, null, Setup.DIFFTYPE_LOCAL);
                    setups.add(setup);
                }
            }
        }
        exportDiff(setups, destination, root, progress);
    }

    public void exportDiff (List<Setup> setups, VCSFileProxy destination, VCSFileProxy root, SvnProgressSupport progress) {
        if (root == null) {
            NotifyDescriptor nd = new NotifyDescriptor(
                    NbBundle.getMessage(ExportDiffAction.class, "MSG_BadSelection_Prompt"), 
                    NbBundle.getMessage(ExportDiffAction.class, "MSG_BadSelection_Title"), 
                    NotifyDescriptor.DEFAULT_OPTION, NotifyDescriptor.ERROR_MESSAGE, null, null);
            DialogDisplayer.getDefault().notify(nd);
            return;
        }
        boolean success = false;
        OutputStream out = null;
        int exportedFiles = 0;

        try {
            String sep = System.getProperty("line.separator"); // NOI18N
            out = new BufferedOutputStream(VCSFileProxySupport.getOutputStream(destination));
            // Used by PatchAction as MAGIC to detect right encoding
            out.write(("# This patch file was generated by NetBeans IDE" + sep).getBytes("utf8"));  // NOI18N
            out.write(("# Following Index: paths are relative to: " + root.getPath() + sep).getBytes("utf8"));  // NOI18N
            out.write(("# This patch can be applied using context Tools: Patch action on respective folder." + sep).getBytes("utf8"));  // NOI18N
            out.write(("# It uses platform neutral UTF-8 encoding and \\n newlines." + sep).getBytes("utf8"));  // NOI18N
            out.write(("# Above lines and this line are ignored by the patching process." + sep).getBytes("utf8"));  // NOI18N


            Collections.sort(setups, new Comparator<Setup>() {
                @Override
                public int compare(Setup o1, Setup o2) {
                    return o1.getBaseFile().getPath().compareTo(o2.getBaseFile().getPath());
                }
            });
            Iterator<Setup> it = setups.iterator();
            int i = 0;
            while (it.hasNext()) {
                Setup setup = it.next();
                VCSFileProxy file = setup.getBaseFile();                
                if (file.isDirectory()) {
                    continue;
                }
                try {            
                    progress.setRepositoryRoot(SvnUtils.getRepositoryRootUrl(file));
                } catch (SVNClientException ex) {
                    SvnClientExceptionHandler.notifyException(new Context(file), ex, true, true);
                    return;
                }                           
                progress.setDisplayName(file.getName());

                String index = "Index: ";   // NOI18N
                String rootPath = root.getPath();
                String filePath = file.getPath();
                String relativePath = filePath;
                if (filePath.startsWith(rootPath)) {
                    relativePath = filePath.substring(rootPath.length() + 1).replace('\\', '/');
                    index += relativePath + sep;
                    out.write(index.getBytes("utf8")); // NOI18N
                }
                exportDiff(setup, relativePath, out);
                i++;
            }

            exportedFiles = i;
            success = true;
        } catch (IOException ex) {
            SvnClientExceptionHandler.notifyException(null, new Exception(NbBundle.getMessage(ExportDiffAction.class, "BK3003", //NOI18N
                    ex.getLocalizedMessage()), ex), true, false);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException alreadyClsoed) {
                }
            }
            if (success) {
                StatusDisplayer.getDefault().setStatusText(NbBundle.getMessage(ExportDiffAction.class, "BK3004", exportedFiles));
                if (exportedFiles == 0) {
                    VCSFileProxySupport.delete(destination);
                } else {
                    VCSFileProxySupport.openFile(destination.normalizeFile());
                }
            } else {
                VCSFileProxySupport.delete(destination);
            }

        }
    }

    private static VCSFileProxy getCommonParent(VCSFileProxy [] files) {
        VCSFileProxy root = files[0];
        if (!root.exists() || root.isFile()) {
            root = root.getParentFile();
        }
        for (int i = 1; i < files.length; i++) {
            root = VCSFileProxySupport.getCommonParent(root, files[i]);
            if (root == null) {
                return null;
            }
        }
        return root;
    }

    /** Writes contextual diff into given stream.*/
    @org.netbeans.api.annotations.common.SuppressWarnings("DE") // ignore warning in finally block
    private void exportDiff(Setup setup, String relativePath, OutputStream out) throws IOException {
        setup.initSources();
        DiffProvider diff = Lookup.getDefault().lookup(DiffProvider.class);

        Reader r1 = null;
        Reader r2 = null;
        Difference[] differences;

        try {
            r1 = setup.getFirstSource().createReader();
            if (r1 == null) r1 = new StringReader("");  // NOI18N
            r2 = setup.getSecondSource().createReader();
            if (r2 == null) r2 = new StringReader("");  // NOI18N
            differences = diff.computeDiff(r1, r2);
        } finally {
            if (r1 != null) try { r1.close(); } catch (Exception e) {}
            if (r2 != null) try { r2.close(); } catch (Exception e) {}
        }

        r1 = null;
        r2 = null;
        VCSFileProxy file = setup.getBaseFile();
        try {
            InputStream is;
            if (!SvnUtils.getMimeType(file).startsWith("text/") && differences.length == 0) { //NOI18N
                // assume the file is binary 
                is = new ByteArrayInputStream(exportBinaryFile(file).getBytes("utf8"));  // NOI18N
            } else {
                r1 = setup.getFirstSource().createReader();
                if (r1 == null) r1 = new StringReader(""); // NOI18N
                r2 = setup.getSecondSource().createReader();
                if (r2 == null) r2 = new StringReader(""); // NOI18N
                TextDiffVisualizer.TextDiffInfo info = new TextDiffVisualizer.TextDiffInfo(
                    relativePath + " " + setup.getFirstSource().getTitle(), // NOI18N
                    relativePath + " " + setup.getSecondSource().getTitle(),  // NOI18N
                    null,
                    null,
                    r1,
                    r2,
                    differences
                );
                info.setContextMode(true, 3);
                String diffText = TextDiffVisualizer.differenceToUnifiedDiffText(info);
                is = new ByteArrayInputStream(diffText.getBytes("utf8"));  // NOI18N
            }
            while(true) {
                int i = is.read();
                if (i == -1) break;
                out.write(i);
            }
        } finally {
            if (r1 != null) try { r1.close(); } catch (Exception e) {}
            if (r2 != null) try { r2.close(); } catch (Exception e) {}
        }
    }
        
    private String exportBinaryFile(VCSFileProxy file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder((int) VCSFileProxySupport.length(file));
        if (VCSFileProxySupport.canRead(file)) {
            Utils.copyStreamsCloseAll(baos, file.getInputStream(false));
        }
        sb.append("MIME: application/octet-stream; encoding: Base64; length: ").append(VCSFileProxySupport.canRead(file) ? VCSFileProxySupport.length(file) : -1); // NOI18N
        sb.append(System.getProperty("line.separator")); // NOI18N
        sb.append(Base64Encoder.encode(baos.toByteArray(), true));
        sb.append(System.getProperty("line.separator")); // NOI18N
        return sb.toString();
    }
}

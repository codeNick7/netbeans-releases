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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.netbeans.modules.maven.hints.pom;

import java.awt.Image;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeSelectionModel;
import org.netbeans.api.project.Project;
import org.netbeans.modules.maven.api.NbMavenProject;
import org.netbeans.modules.maven.embedder.MavenEmbedder;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author mkleint
 */
public final class MoveToDependencyManagementPanel extends javax.swing.JPanel implements ExplorerManager.Provider, Runnable {

    private final BeanTreeView treeView;
    private final transient ExplorerManager explorerManager = new ExplorerManager();
    private final File current;
    private final Project currentProject;

    /** Creates new form MoveToDependencyManagementPanel */
    @SuppressWarnings("LeakingThisInConstructor")
    public MoveToDependencyManagementPanel(File file, Project prj) {
        assert file != null;
        initComponents();
        treeView = (BeanTreeView)jScrollPane1;
        treeView.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        current = file;
        currentProject = prj;
        showWaitNode();
        RequestProcessor.getDefault().post(this);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    /**
     *
     */
    public void showWaitNode() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               treeView.setRootVisible(true);
               explorerManager.setRootContext(createWaitNode());
            }
        });
    }
    private static Node createWaitNode() {
        AbstractNode an = new AbstractNode(Children.LEAF);
        an.setIconBaseWithExtension("org/netbeans/modules/maven/navigator/wait.gif");
        an.setDisplayName(NbBundle.getMessage(MoveToDependencyManagementPanel.class, "LBL_Wait"));
        return an;
    }


    @Override
    public void run() {
                NbMavenProject nbprj = currentProject.getLookup().lookup(NbMavenProject.class);
        List<MavenEmbedder.ModelDescription> lin = null;
        if (nbprj != null) {
            lin = MavenEmbedder.getModelDescriptors(nbprj.getMavenProject());
        }
        if (lin != null) {
                    final Children ch = new PomChildren(lin);
                    SwingUtilities.invokeLater(new Runnable() {
                @Override
                        public void run() {
                           treeView.setRootVisible(false);
                           explorerManager.setRootContext(new AbstractNode(ch));
                            try {
                                explorerManager.setSelectedNodes(new Node[]{
                                    explorerManager.getRootContext().getChildren().getNodes()[0]
                                });
                            } catch (PropertyVetoException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                    });
        }
    }

    private static Node createEmptyNode() {
        AbstractNode an = new AbstractNode(Children.LEAF);
        return an;
    }

    private static Node createErrorNode() {
        AbstractNode an = new AbstractNode(Children.LEAF);
        an.setDisplayName(NbBundle.getMessage(MoveToDependencyManagementPanel.class, "LBL_Error"));
        return an;
    }

    private static class PomChildren extends Children.Keys<List<MavenEmbedder.ModelDescription>> {

        public PomChildren(List<MavenEmbedder.ModelDescription> lineage) {
            setKeys(new List[] {lineage});
        }

        @Override
        protected Node[] createNodes(List<MavenEmbedder.ModelDescription> key) {
            List<POMNode> nds = new ArrayList<POMNode>();
            for (MavenEmbedder.ModelDescription mdl : key) {
                if(mdl.getLocation() != null ) {
                    File fl = mdl.getLocation();
                    FileObject fo = FileUtil.toFileObject(fl);
                    Lookup lookup;
                    if (fo != null && !"pom".equals(fo.getExt())) { //NOI18N
                        lookup = Lookups.singleton(fo);
                    } else {
                        lookup = Lookup.EMPTY;
                    }

                    nds.add(new POMNode(fl, mdl, lookup));
                }
            }
            return nds.toArray(new Node[0]);
        }

    }

    private static class POMNode extends AbstractNode {

        private final Image icon = ImageUtilities.loadImage("org/netbeans/modules/maven/navigator/Maven2Icon.gif"); // NOI18N
        private boolean readonly = false;
        private POMNode(File key, MavenEmbedder.ModelDescription mdl, Lookup lkp) {
            super( Children.LEAF, lkp);
            String artifact = mdl.getArtifactId();
            String version = mdl.getVersion();
            setDisplayName(NbBundle.getMessage(MoveToDependencyManagementPanel.class, "TITLE_PomNode", artifact, version));

            if (key.getName().endsWith("pom")) { //NOI18N
                //coming from repository
                readonly = true;
            }
            setShortDescription(key.getAbsolutePath());
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[0];
        }

        @Override
        public Action getPreferredAction() {
            return null;
        }

        @Override
        public String getHtmlDisplayName() {
            if (readonly) {
                return NbBundle.getMessage(MoveToDependencyManagementPanel.class, "HTML_TITLE_PomNode", getDisplayName());
            }
            return null;
        }

        @Override
        public Image getIcon(int type) {
             return icon;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }

    FileObject getSelectedPomFile() {
        Node[] nds = explorerManager.getSelectedNodes();
        if (nds.length > 0) {
            return nds[0].getLookup().lookup(FileObject.class);
        }
        return null;
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new BeanTreeView();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MoveToDependencyManagementPanel.class, "MoveToDependencyManagementPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}

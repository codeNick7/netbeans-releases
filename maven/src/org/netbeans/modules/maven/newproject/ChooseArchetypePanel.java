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

package org.netbeans.modules.maven.newproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import javax.swing.tree.TreeSelectionModel;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.util.FileUtils;
import org.netbeans.modules.maven.api.archetype.Archetype;
import org.netbeans.modules.maven.api.archetype.ArchetypeProvider;
import org.netbeans.modules.maven.embedder.EmbedderFactory;
import org.netbeans.modules.maven.indexer.api.NBVersionInfo;
import org.netbeans.modules.maven.indexer.api.RepositoryIndexer;
import org.netbeans.modules.maven.indexer.api.RepositoryInfo;
import org.netbeans.modules.maven.indexer.api.RepositoryPreferences;
import org.netbeans.modules.maven.indexer.api.RepositoryQueries;
import org.netbeans.modules.maven.indexer.api.RepositoryUtil;
import static org.netbeans.modules.maven.newproject.Bundle.*;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
import org.openide.awt.Mnemonics;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 *
 * @author  mkleint
 */
public class ChooseArchetypePanel extends javax.swing.JPanel implements ExplorerManager.Provider, Runnable {
    
    private static final RequestProcessor RP = new RequestProcessor(ChooseArchetypePanel.class.getName(),5);

    private static File getLocalCatalogFile() {
        return new File(RepositorySystem.userMavenConfigurationHome, "archetype-catalog.xml"); // NOI18N
    }

    private static URL getDefaultCatalogFile() throws IOException {
        List<NBVersionInfo> versions = RepositoryQueries.getVersions("org.apache.maven.archetype", "archetype-common", Collections.singletonList(RepositoryPreferences.getInstance().getLocalRepository())); // NOI18N
        if (versions.isEmpty()) {
            return null;
        }
        // any need to sort?
        NBVersionInfo newest = versions.get(0);
        Artifact art = RepositoryUtil.createArtifact(newest);
        File jar = art.getFile();
        if (!jar.isFile()) {
            return null;
        }
        return new URL("jar:" + jar.toURI() + "!/archetype-catalog.xml"); // NOI18N
    }

    private ExplorerManager manager;
    private ChooseWizardPanel wizardPanel;
    static final String PROP_ARCHETYPE = "archetype"; //NOI18N
    TreeView tv;
    private static Archetype REMOTE_PLACEHOLDER = new Archetype();
    private static Archetype CATALOGS_PLACEHOLDER = new Archetype();
    private static Archetype LOCAL_PLACEHOLDER = new Archetype();
    private static Archetype LOADING_ARCHETYPE = new Archetype();
    private static List<Archetype> prohibited = new ArrayList<Archetype>();
    static {
        //prevent equals..
        REMOTE_PLACEHOLDER.setGroupId("R"); //NOI18N
        LOADING_ARCHETYPE.setGroupId("L"); //NOI18N
        LOCAL_PLACEHOLDER.setGroupId("X"); //NOI18N
        CATALOGS_PLACEHOLDER.setGroupId("@"); //NOI18N
        prohibited.add(REMOTE_PLACEHOLDER);
        prohibited.add(LOCAL_PLACEHOLDER);
        prohibited.add(LOADING_ARCHETYPE);
        prohibited.add(CATALOGS_PLACEHOLDER);
    }

    /** Creates new form ChooseArchetypePanel */
    @Messages("TIT_CreateProjectStep=Select a Maven archetype as a template for your project.")
    public ChooseArchetypePanel(ChooseWizardPanel wizPanel) {
        initComponents();
        
        Mnemonics.setLocalizedText(jLabel2, TIT_CreateProjectStep());
        
        this.wizardPanel = wizPanel;
        tv = new BeanTreeView();
        tv.setMinimumSize(new Dimension(50, 50));
        manager = new ExplorerManager();
        pnlView.add(tv, BorderLayout.CENTER);
        tv.setBorder(jScrollPane1.getBorder());
        tv.setDefaultActionAllowed(false);
        tv.setPopupAllowed(false);
        tv.setRootVisible(false);
        tv.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        Childs childs = new Childs();
        childs.addArchetype(LOADING_ARCHETYPE);
        AbstractNode root = new AbstractNode(childs);
        manager.setRootContext(root);
        RP.post(this);
        manager.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateDescription();
                wizardPanel.fireChangeEvent();
            }
        });
        updateDescription();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        tv.requestFocusInWindow();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHint = new javax.swing.JLabel();
        pnlView = new javax.swing.JPanel();
        btnCustom = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        lblHint.setLabelFor(pnlView);
        org.openide.awt.Mnemonics.setLocalizedText(lblHint, org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "LBL_MavenArchetype")); // NOI18N

        pnlView.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(btnCustom, org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "LBL_AddArchetype")); // NOI18N
        btnCustom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnRemove, org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "LBL_RemoveArchetype")); // NOI18N
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        taDescription.setBackground(new java.awt.Color(238, 238, 238));
        taDescription.setColumns(20);
        taDescription.setEditable(false);
        taDescription.setLineWrap(true);
        taDescription.setRows(5);
        taDescription.setWrapStyleWord(true);
        jScrollPane1.setViewportView(taDescription);
        taDescription.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "ArchetypesPanel.taDescription.accessibledesc")); // NOI18N

        jLabel1.setLabelFor(taDescription);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "LBL_Description")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "TIT_CreateProjectStep", "")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
            .addComponent(lblHint)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlView, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCustom)
                    .addComponent(btnRemove)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCustom, btnRemove});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCustom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove))
                    .addComponent(pnlView, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlView.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "ArchetypesPanel.pnlView.accessiblename")); // NOI18N
        pnlView.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "ArchetypesPanel.pnlView.accessibledesc")); // NOI18N
        btnCustom.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "ArchetypesPanel.btnCustom.accessibledesc")); // NOI18N
        btnRemove.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(ChooseArchetypePanel.class, "ArchetypesPanel.btnRemove.accessibledesc")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    @Messages("Q_RemoveArch=Really remove {0} archetype from local repository?")
private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
    Node[] nds = getExplorerManager().getSelectedNodes();
    if (nds.length != 0) {
        final Archetype arch = (Archetype) nds[0].getValue(PROP_ARCHETYPE);
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
                Q_RemoveArch(arch.getArtifactId()), 
                NotifyDescriptor.YES_NO_OPTION);
        Object ret = DialogDisplayer.getDefault().notify(nd);
        if (ret != NotifyDescriptor.YES_OPTION) {
            return;
        }
        RP.post(new Runnable() {
            public void run() {
                try {
                    RepositoryInfo info = RepositoryPreferences.getInstance().getLocalRepository();
                    List<NBVersionInfo> rec = RepositoryQueries.getRecords(arch.getGroupId(),
                            arch.getArtifactId(), arch.getVersion(), Collections.singletonList(info));
                    for (NBVersionInfo record : rec) {
                        Artifact a = RepositoryUtil.createArtifact(record);
                        RepositoryIndexer.deleteArtifactFromIndex(info, a);
                    }
                    File path = new File(EmbedderFactory.getProjectEmbedder().getLocalRepository().getBasedir(),
                            arch.getGroupId().replace('.', File.separatorChar) + File.separator + arch.getArtifactId() + File.separator + arch.getVersion());
                    if (path.exists()) {
                        FileUtils.deleteDirectory(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ((Childs) getExplorerManager().getRootContext().getChildren()).removeArchetype(arch);
    }
}//GEN-LAST:event_btnRemoveActionPerformed

    @Messages({
        "TIT_Archetype_details=Specify archetype details",
        "LBL_Custom=Custom archetype - {0}"
    })
    private void btnCustomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomActionPerformed
        CustomArchetypePanel panel = new CustomArchetypePanel();
        DialogDescriptor dd = new DialogDescriptor(panel, TIT_Archetype_details());
        Object ret = DialogDisplayer.getDefault().notify(dd);
        if (ret == NotifyDescriptor.OK_OPTION) {
            Childs childs = (Childs)manager.getRootContext().getChildren();
            Archetype arch = new Archetype();
            arch.setArtifactId(panel.getArtifactId());
            arch.setGroupId(panel.getGroupId());
            arch.setVersion(panel.getVersion().length() == 0 ? "LATEST" : panel.getVersion()); //NOI18N
            
            arch.setName(LBL_Custom(panel.getArtifactId()));
            if (panel.getRepository().length() != 0) {
                arch.setRepository(panel.getRepository());
            }
            childs.addArchetype(arch);
            //HACK - the added one will be last..
            Node[] list =  getExplorerManager().getRootContext().getChildren().getNodes();
            try {
                getExplorerManager().setSelectedNodes(new Node[] {list[list.length - 1]});
            } catch (PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnCustomActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustom;
    private javax.swing.JButton btnRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHint;
    private javax.swing.JPanel pnlView;
    private javax.swing.JTextArea taDescription;
    // End of variables declaration//GEN-END:variables
    
    

    public ExplorerManager getExplorerManager() {
        return manager;
    }
    
    public @Override void run() {
        List<Archetype> archetypes = new ArrayList<Archetype>();
        // XXX currently there will be none here; should reconsider UI of this panel
        for (ArchetypeProvider provider : Lookup.getDefault().lookupAll(ArchetypeProvider.class)) {
            for (Archetype ar : provider.getArchetypes()) {
                if (!archetypes.contains(ar)) {
                    archetypes.add(ar);
                }
            }
        }
        archetypes.add(CATALOGS_PLACEHOLDER);
        archetypes.add(LOCAL_PLACEHOLDER);
        archetypes.add(REMOTE_PLACEHOLDER);
        Childs childs = (Childs)manager.getRootContext().getChildren();
        childs.setArchetypes(archetypes);

        try {
            manager.setSelectedNodes(new Node[] {manager.getRootContext().getChildren().getNodes()[0]});
        } catch (PropertyVetoException e) {
        }
    }

    void read(WizardDescriptor wizardDescriptor) {
    }

    void store(WizardDescriptor d) {
        if (manager.getSelectedNodes().length > 0) {
            d.putProperty(PROP_ARCHETYPE, manager.getSelectedNodes()[0].getValue(PROP_ARCHETYPE));
        }
    }

    void validate(WizardDescriptor wizardDescriptor) {
    }

    boolean valid(WizardDescriptor wizardDescriptor) {
        boolean isSelected = manager.getSelectedNodes().length > 0;
        if (isSelected) {
            Archetype arch = (Archetype)((AbstractNode)manager.getSelectedNodes()[0]).getValue(PROP_ARCHETYPE);
            isSelected = arch != null && !prohibited.contains(arch);
        }
        return isSelected;
    }

    @Messages({
        "MSG_Description_with_repo={0}\n\nGroup ID: {1}\nArtifact ID: {2}\nVersion: {3}\nRepository: {4}",
        "MSG_Description_no_repo={0}\n\nGroup ID: {1}\nArtifact ID: {2}\nVersion: {3}",
        "MSG_NoTemplate=<No template selected>"
    })
    private void updateDescription() {
        Node[] nds = manager.getSelectedNodes();
        if (nds.length > 0) {
            Archetype arch = (Archetype)((AbstractNode)nds[0]).getValue(PROP_ARCHETYPE);
            if (arch != null && !prohibited.contains(arch)) {
                String desc = arch.getDescription() == null ? "" : arch.getDescription().replaceAll("\\s+", " ").replaceAll("^ | $", "");
                if (arch.getRepository() != null) {
                    taDescription.setText(MSG_Description_with_repo(desc, arch.getGroupId(), arch.getArtifactId(), arch.getVersion(), arch.getRepository()));
                } else {
                    taDescription.setText(MSG_Description_no_repo(desc, arch.getGroupId(), arch.getArtifactId(), arch.getVersion()));
                }
                taDescription.setCaretPosition(0);
                btnRemove.setEnabled(arch.deletable);
                return;
            }
        }
        taDescription.setText(MSG_NoTemplate());
        btnRemove.setEnabled(false);
    }
    
    @Messages({
        "LBL_Loading=Loading more archetypes...",
        "TIT_Archetype_Node_Name={0} ({1})"
    })
    public static Node[] createNodes(Archetype arch, Children childs) {
        if (arch == LOADING_ARCHETYPE) {
            AbstractNode loading = new AbstractNode(Children.LEAF);
            loading.setName("loading"); //NOI18N
            loading.setDisplayName(LBL_Loading());
            return new Node[] {loading};
        }
        AbstractNode nd = new AbstractNode(childs);
        String dn = arch.getName() == null ? arch.getArtifactId() : arch.getName();
        nd.setName(dn);
        nd.setDisplayName(TIT_Archetype_Node_Name(dn, arch.getVersion()));
        nd.setIconBaseWithExtension("org/netbeans/modules/maven/resources/Maven2Icon.gif"); //NOI18N
        nd.setValue(PROP_ARCHETYPE, arch);
        return new Node[] { nd };
    }
    
    
    private static class Childs extends Children.Keys<Archetype> {
        private List<Archetype> keys;
        public Childs() {
            this.keys = new ArrayList<Archetype>();
        }
        @Override
        public void addNotify() {
            setKeys(keys);
        }
        
        @Override
        public void removeNotify() {
            setKeys(Collections.<Archetype>emptyList());
        }
        
        public void addArchetype(Archetype arch) {
            keys.add(Math.max(0, keys.size() - 1), arch);
            setKeys(keys);
            refresh();
        }
        
        public void removeArchetype(Archetype arch) {
            keys.remove(arch);
            setKeys(keys);
            refresh();
        }
        
        public Node[] createNodes(Archetype arch) {
            if (arch == REMOTE_PLACEHOLDER) {
                return new Node[] { createRemoteRepoNode() };
            }
            if (arch == LOCAL_PLACEHOLDER) {
                return new Node[] { createLocalRepoNode() };
            }
            if (arch == CATALOGS_PLACEHOLDER) {
                return new Node[] {createLocalCatalogNode(), createDefaultCatalogNode()};
            }
            return ChooseArchetypePanel.createNodes(arch, Children.LEAF);
        }

        private void addArchetypes(Collection<Archetype> archetypes) {
            keys.addAll(archetypes);
            setKeys(keys);
            refresh();
        }

        private void setArchetypes(Collection<Archetype> archetypes) {
            keys = new ArrayList<Archetype>(archetypes);
            setKeys(keys);
            refresh();
        }
    }

    @Messages("LBL_Remote=Archetypes from Remote Repositories")
    private static Node createRemoteRepoNode() {
        AbstractNode nd = new AbstractNode(new RepoProviderChildren(new RemoteRepoProvider()));
        nd.setName("remote-repo-content"); //NOI18N
        nd.setDisplayName(LBL_Remote());
        nd.setIconBaseWithExtension("org/netbeans/modules/maven/newproject/remoterepo.png");
        return nd;
    }
    
    @Messages("LBL_Local=Archetypes from Local Repository")
    private static Node createLocalRepoNode() {
        AbstractNode nd = new AbstractNode(new RepoProviderChildren(new LocalRepoProvider()));
        nd.setName("local-repo-content"); //NOI18N
        nd.setDisplayName(LBL_Local());
        nd.setIconBaseWithExtension("org/netbeans/modules/maven/newproject/remoterepo.png");
        return nd;
    }
    
    @Messages("LBL_LocalCatalog=Local Archetype Catalog")
    private static Node createLocalCatalogNode() {
        AbstractNode nd = new AbstractNode(new RepoProviderChildren(new CatalogRepoProvider() {
            protected @Override URL file() throws IOException {
                File f = getLocalCatalogFile();
                return f.isFile() ? f.toURI().toURL() : null;
            }
            protected @Override String repository() {
                return RepositorySystem.DEFAULT_LOCAL_REPO_ID;
            }
        }));
        nd.setName("local-catalog-content"); //NOI18N
        nd.setDisplayName(LBL_LocalCatalog());
        nd.setIconBaseWithExtension("org/netbeans/modules/maven/newproject/remoterepo.png");
        return nd;
    }

    @Messages("LBL_DefaultCatalog=Default Archetype Catalog")
    private static Node createDefaultCatalogNode() {
        AbstractNode nd = new AbstractNode(new RepoProviderChildren(new CatalogRepoProvider() {
            protected @Override URL file() throws IOException {
                return getDefaultCatalogFile();
            }
        }));
        nd.setName("default-catalog-content"); //NOI18N
        nd.setDisplayName(LBL_DefaultCatalog());
        nd.setIconBaseWithExtension("org/netbeans/modules/maven/newproject/remoterepo.png");
        return nd;
    }

    private static class RepoProviderChildren extends Children.Keys<Archetype> implements Runnable {
        private List<Archetype> keys;
        private TreeMap<String, TreeMap<DefaultArtifactVersion, Archetype>> res = new TreeMap<String, TreeMap<DefaultArtifactVersion, Archetype>>();
        private final ArchetypeProvider provider;

        private RepoProviderChildren(ArchetypeProvider prov) {
            keys = new ArrayList<Archetype>();
            this.provider = prov;
        }
        
        @Override
        public void addNotify() {
            keys.add(LOADING_ARCHETYPE);
            setKeys(keys);
            RP.post(this);
        }
        
        @Override
        public void removeNotify() {
            setKeys(Collections.<Archetype>emptyList());
        }
        
        @Override
        protected Node[] createNodes(Archetype key) {
            if (key == LOADING_ARCHETYPE) {
                return ChooseArchetypePanel.createNodes(key, LEAF);
            }
            String id = key.getGroupId() + "|" + key.getArtifactId(); //NOI18N
            TreeMap<DefaultArtifactVersion, Archetype> rs = res.get(id);
            Children childs = Children.LEAF;
            if (rs != null && rs.size() > 1) {
                //can it be actually null?
                childs = new Childs();
                List<Archetype> lst = new ArrayList<Archetype>();
                lst.addAll(rs.values());
                lst.remove(key);
                ((Childs)childs).addArchetypes(lst);
            } 
            return ChooseArchetypePanel.createNodes(key, childs);
        }

        public void run() {
            for (Archetype ar : provider.getArchetypes()) {
                String key = ar.getGroupId() + "|" + ar.getArtifactId(); //NOI18N
                TreeMap<DefaultArtifactVersion, Archetype> archs = res.get(key);
                if (archs == null) {
                    archs = new TreeMap<DefaultArtifactVersion, Archetype>(new VersionComparator());
                    res.put(key, archs);
                }
                if (!archs.containsValue(ar)) {
                    DefaultArtifactVersion ver = new DefaultArtifactVersion(ar.getVersion());
                    archs.put(ver, ar);
                }
            }
            List<Archetype> archetypes = new ArrayList<Archetype>();
            for (TreeMap<DefaultArtifactVersion, Archetype> map : res.values()) {
                archetypes.add(map.values().iterator().next());
            }
            keys = archetypes;
            setKeys(keys);
        }
        
    }
    
    private static class VersionComparator implements Comparator<DefaultArtifactVersion> {
        public int compare(DefaultArtifactVersion o1, DefaultArtifactVersion o2) {
            assert o1 != null;
            assert o2 != null;
            return o2.compareTo(o1);
        }
    }
}

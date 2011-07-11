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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
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
package org.netbeans.modules.localhistory.ui.actions;

import java.awt.Dialog;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import org.netbeans.modules.localhistory.ui.view.ShowLocalHistoryAction;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.netbeans.modules.localhistory.LocalHistory;
import org.netbeans.modules.localhistory.store.StoreEntry;
import org.netbeans.modules.localhistory.ui.actions.FileNode.PlainFileNode;
import org.netbeans.modules.localhistory.ui.actions.FileNode.StoreEntryNode;
import org.netbeans.modules.versioning.spi.VCSContext;
import org.netbeans.modules.versioning.spi.VersioningSupport;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

/**
 * @author Tomas Stupka
 */
public class RevertDeletedAction extends NodeAction {
    
    /** Creates a new instance of ShowLocalHistoryAction */
    public RevertDeletedAction() {
        setIcon(null);
        putValue("noIconInMenu", Boolean.TRUE); // NOI18N
    }
    
    @Override
    protected void performAction(final Node[] activatedNodes) {
                               
        LocalHistory.getInstance().getParallelRequestProcessor().post(new Runnable() {
            @Override
            public void run() {
                VCSContext ctx = VCSContext.forNodes(activatedNodes);
                Set<File> rootSet = ctx.getRootFiles();        
                if(rootSet == null || rootSet.size() < 1) { 
                    return;
                }                                        
                DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
                for (File root : rootSet) {            
                    PlainFileNode rfn = new PlainFileNode(root);
                    populateNode(rfn, root, !VersioningSupport.isFlat(root));
                    if(rfn.getChildCount() > 0) {
                        rootNode.add(rfn);
                    }
                }
                if(rootNode.getChildCount() > 0) {
                    revert(rootNode);
                } else {
                    DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(NbBundle.getMessage(RevertDeletedAction.class, "MSG_NO_FILES")));
                }
            }                                       
        });
    }

    private List<StoreEntryNode> getDeletedEntries(File file) {
        StoreEntry[] entries = LocalHistory.getInstance().getLocalHistoryStore().getDeletedFiles(file);
        if(entries.length == 0) {
            return new LinkedList<StoreEntryNode>();
                }            
        List<StoreEntryNode> l = new LinkedList<StoreEntryNode>();
        for (StoreEntry e : entries) {
            if(!e.getFile().exists()) { 
                // the files version was created by a delete &&
                // the file wasn't created again.  
                l.add(new StoreEntryNode(e));
            }
        }
        return l;                
    }
    
    private void revert(DefaultMutableTreeNode rootNode) {
        
        RevertPanel p = new RevertPanel();
        p.tree.setCellRenderer(new DeletedListRenderer());
        FileNodeListener l = new FileNodeListener();
        p.tree.addMouseListener(l);
        p.tree.addKeyListener(l);
                                
        p.setRoot(rootNode);
        DialogDescriptor dd = 
            new DialogDescriptor (
                p, 
                NbBundle.getMessage(RevertDeletedAction.class, "LBL_SELECT_FILES"), // NOI18N
                true, 
                DialogDescriptor.OK_CANCEL_OPTION, 
                DialogDescriptor.OK_OPTION, 
                null); 
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setVisible(true);

        if(dd.getValue() != DialogDescriptor.OK_OPTION) {
            return;
        }

        List<StoreEntryNode> nodes = getSelectedNodes(rootNode);

        for(StoreEntryNode sen : nodes) {
            revert(sen.getStoreEntry());
        }
    }
    
    private List<StoreEntryNode> getSelectedNodes(TreeNode node) {
        List<StoreEntryNode> ret = new LinkedList<StoreEntryNode>();
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            TreeNode child = node.getChildAt(i);
            if(child instanceof StoreEntryNode) {
                StoreEntryNode sen = (StoreEntryNode) child;
                if(sen.isSelected()) {
                    ret.add(sen);
                }
            }
            ret.addAll(getSelectedNodes(child));
        }
        return ret;
    }
    
    protected boolean enable(Node[] activatedNodes) {     
        VCSContext ctx = VCSContext.forNodes(activatedNodes);
        Set<File> rootSet = ctx.getRootFiles();        
        if(rootSet == null || rootSet.size() < 1) { 
            return false;
        }                        
        for (File file : rootSet) {            
            if(file != null && !file.isDirectory()) {
                return false;
            }
        }        
        return true;
    }
    
    public String getName() {
        return NbBundle.getMessage(this.getClass(), "CTL_ShowRevertDeleted");   // NOI18N      
    }
    
    public HelpCtx getHelpCtx() {
        return new HelpCtx(ShowLocalHistoryAction.class);
    }

    private static void revert(StoreEntry se) {        
        File file = se.getFile();
        if(file.exists()) {
            // created externaly?
            if(file.isFile()) {                
                LocalHistory.LOG.log(Level.WARNING, "Skipping revert for file {0} which already exists.", file.getAbsolutePath());    // NOI18N
            }  
            // fix history
            // XXX create a new entry vs. fixing the entry timestamp and deleted flag?
            LocalHistory.getInstance().getLocalHistoryStore().fileCreate(file, file.lastModified());
        }
        File storeFile = se.getStoreFile();
                
        InputStream is = null;
        OutputStream os = null;
        try {               
            if(!storeFile.isFile()) {
                FileUtil.createFolder(file);             
            } else {            
                FileObject fo = FileUtil.createData(file);                

                os = getOutputStream(fo);     
                is = se.getStoreFileInputStream();                    
                FileUtil.copy(is, os);            
            }
        } catch (Exception e) {            
            LocalHistory.LOG.log(Level.SEVERE, null, e);
            return;
        } finally {
            try {
                if(os != null) { os.close(); }
                if(is != null) { is.close(); }
            } catch (IOException e) {}
        } 
    }
    
    private static OutputStream getOutputStream(FileObject fo) throws FileAlreadyLockedException, IOException, InterruptedException {
        int retry = 0;
        while (true) {
            try {
                return fo.getOutputStream();                
            } catch (IOException ioe) {            
                retry++;
                if (retry > 7) {
                    throw ioe;
                }
                Thread.sleep(retry * 30);
            } 
        }                    
    }
    
    private void populateNode(FileNode node, File root, boolean recursively) {
        
        List<StoreEntryNode> deletedEntries = getDeletedEntries(root);
        if(!recursively) {
            for (StoreEntryNode sen : deletedEntries) {
                node.add(sen);
            }
            return;
        }
        
        // check all previosly deleted children files if they by chance 
        // also contain something deleted
        for (StoreEntryNode sen : deletedEntries.toArray(new StoreEntryNode[deletedEntries.size()])) {
            node.add(sen);
            if(!sen.getStoreEntry().representsFile()) {
                populateNode(sen, sen.getStoreEntry().getFile(), true);
            }
        }

        // check all existing children files if they contain anything deleted
        File[] files = root.listFiles();
        if(files != null) {
            for(File f : files) {
                if(f.isDirectory()) {
                    PlainFileNode pfn = new PlainFileNode(f);
                    populateNode(pfn, f, true);
                    if(pfn.getChildCount() > 0) {
                        node.add(pfn);
                    }
                }            
            }
        } else {
            LocalHistory.LOG.log(Level.WARNING, "listFiles() for directory {0} returned null", root);
        }
    }
    
    private class FileNodeListener implements MouseListener, KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JTree tree = (JTree) e.getSource();
            Point p = e.getPoint();
            int row = tree.getRowForLocation(e.getX(), e.getY());
            TreePath path = tree.getPathForRow(row);
            
            // if path exists and mouse is clicked exactly once
            if (path != null) {
                FileNode node = (FileNode) path.getLastPathComponent();
                Rectangle chRect = DeletedListRenderer.getCheckBoxRectangle();
                Rectangle rowRect = tree.getPathBounds(path);
                chRect.setLocation(chRect.x + rowRect.x, chRect.y + rowRect.y);
                if (e.getClickCount() == 1 && chRect.contains(p)) {
                    boolean isSelected = !(node.isSelected());
                    node.setSelected(isSelected);
                    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                    if (row == 0) {
                        tree.revalidate();
                    }
                    tree.repaint();
                }
            }
        }

        @Override public void keyTyped(KeyEvent e) { }
        @Override public void keyReleased(KeyEvent e) { }
        @Override public void mouseEntered(MouseEvent e) { }
        @Override public void mouseExited(MouseEvent e) { }
        @Override public void mouseReleased(MouseEvent e) { }
        @Override public void mousePressed(MouseEvent event) { }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_SPACE) {
                JTree tree = (JTree) e.getSource();
                TreePath path = tree.getSelectionPath();
                if (path != null) {
                    FileNode node = (FileNode) path.getLastPathComponent();
                    node.setSelected(!node.isSelected());
                    tree.repaint();
                    e.consume();
                }
            } 
        }
    } // end FileNodeListener    
}

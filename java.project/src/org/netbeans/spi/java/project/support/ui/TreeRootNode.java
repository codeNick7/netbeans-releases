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

package org.netbeans.spi.java.project.support.ui;

import java.awt.EventQueue;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.queries.AccessibilityQuery;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.queries.VisibilityQuery;
import org.netbeans.modules.java.project.PackageDisplayUtils;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.ChangeableDataFilter;
import org.openide.loaders.DataFilter;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeNotFoundException;
import org.openide.nodes.NodeOp;
import org.openide.util.ChangeSupport;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

// XXX need unit test

/**
 * Displays a package root in a tree.
 * @see "#42151"
 * @author Jesse Glick
 */
final class TreeRootNode extends FilterNode implements PropertyChangeListener {
    
    private final SourceGroup g;
    
    public TreeRootNode(SourceGroup g) {
        this(DataFolder.findFolder(g.getRootFolder()), g);
    }
    
    private TreeRootNode(DataFolder folder, SourceGroup g) {
        this(new FilterNode(folder.getNodeDelegate(), folder.createNodeChildren(new GroupDataFilter(g))), g);
    }
    
    private TreeRootNode (Node originalNode, SourceGroup g) {
        super(originalNode, new PackageFilterChildren(originalNode),
            new ProxyLookup(
                originalNode.getLookup(),
                Lookups.singleton(new PathFinder(g))
                // no need for explicit search info
            ));
        this.g = g;
        g.addPropertyChangeListener(WeakListeners.propertyChange(this, g));
    }

    /** Copied from PackageRootNode with modifications. */
    private Image computeIcon(boolean opened, int type) {
        Icon icon = g.getIcon(opened);
        if (icon == null) {
            Image image = opened ? super.getOpenedIcon(type) : super.getIcon(type);
            return ImageUtilities.mergeImages(image, ImageUtilities.loadImage(PackageRootNode.PACKAGE_BADGE), 7, 7);
        } else {
            return ImageUtilities.icon2Image(icon);
        }
    }
    
    public Image getIcon(int type) {
        return computeIcon(false, type);
    }

    public Image getOpenedIcon(int type) {
        return computeIcon(true, type);
    }

    public String getName() {
        return g.getName();
    }

    public String getDisplayName() {
        return g.getDisplayName();
    }

    public boolean canRename() {
        return false;
    }

    public boolean canDestroy() {
        return false;
    }

    public boolean canCut() {
        return false;
    }

    public void propertyChange(PropertyChangeEvent ev) {
        // XXX handle SourceGroup.rootFolder change too
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                fireNameChange(null, null);
                fireDisplayNameChange(null, null);
                fireIconChange();
                fireOpenedIconChange();
            }
        });
    }

    /** Copied from PhysicalView and PackageRootNode. */
    public static final class PathFinder {
        
        private final SourceGroup g;
        
        PathFinder(SourceGroup g) {
            this.g = g;
        }
        
        public Node findPath(Node rootNode, Object o) {
            FileObject fo;
            if (o instanceof FileObject) {
                fo = (FileObject) o;
            } else if (o instanceof DataObject) {
                fo = ((DataObject) o).getPrimaryFile();
            } else {
                return null;
            }
            FileObject groupRoot = g.getRootFolder();
            if (FileUtil.isParentOf(groupRoot, fo) /* && group.contains(fo) */) {
                FileObject folder = fo.isFolder() ? fo : fo.getParent();
                String relPath = FileUtil.getRelativePath(groupRoot, folder);
                List<String> path = new ArrayList<String>();
                StringTokenizer strtok = new StringTokenizer(relPath, "/"); // NOI18N
                while (strtok.hasMoreTokens()) {
                    String token = strtok.nextToken();
                   path.add(token);
                }
                try {
                    Node folderNode =  folder.equals(groupRoot) ? rootNode : NodeOp.findPath(rootNode, Collections.enumeration(path));
                    if (fo.isFolder()) {
                        return folderNode;
                    } else {
                        Node[] childs = folderNode.getChildren().getNodes(true);
                        for (int i = 0; i < childs.length; i++) {
                           DataObject dobj = childs[i].getLookup().lookup(DataObject.class);
                           if (dobj != null && dobj.getPrimaryFile().getNameExt().equals(fo.getNameExt())) {
                               return childs[i];
                           }
                        }
                    }
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (groupRoot.equals(fo)) {
                return rootNode;
            } 
            return null;
        }
    }
    
    private static final class GroupDataFilter implements ChangeListener, PropertyChangeListener,
            ChangeableDataFilter, DataFilter.FileBased {
        
        private static final long serialVersionUID = 1L; // in case a DataFolder.ClonedFilterHandle saves me
        
        private final ChangeSupport cs = new ChangeSupport(this);
        private final SourceGroup g;
        
        public GroupDataFilter(SourceGroup g) {
            this.g = g;
            VisibilityQuery.getDefault().addChangeListener(WeakListeners.change(this, VisibilityQuery.getDefault()));
            g.addPropertyChangeListener(WeakListeners.propertyChange(this, g));
        }
        
        public boolean acceptDataObject(DataObject obj) {
            return acceptFileObject(obj.getPrimaryFile());
        }
        
        public void stateChanged(ChangeEvent e) {
            cs.fireChange();
        }
        
        public void propertyChange(PropertyChangeEvent e) {
            if (SourceGroup.PROP_CONTAINERSHIP.equals(e.getPropertyName())) {
                cs.fireChange();
            }
        }

        public void addChangeListener(ChangeListener listener) {
            cs.addChangeListener(listener);
        }
        
        public void removeChangeListener(ChangeListener listener) {
            cs.removeChangeListener(listener);
        }

        public boolean acceptFileObject(FileObject fo) {
            return fo.isValid() && g.contains(fo) && VisibilityQuery.getDefault().isVisible(fo);
        }
        
    }
    
    
    private static final class PackageFilterChildren extends FilterNode.Children {
        
        public PackageFilterChildren (final Node originalNode) {
            super (originalNode);
        }       
                
        @Override
        protected Node copyNode(final Node originalNode) {
            FileObject fobj = originalNode.getLookup().lookup(FileObject.class);
            return fobj.isFolder() ? new PackageFilterNode (originalNode) : super.copyNode(originalNode);
        }
    }
    
    private static final class PackageFilterNode extends FilterNode {
        
        private static final @StaticResource String PUBLIC_PACKAGE_BADGE = "org/netbeans/spi/java/project/support/ui/publicBadge.gif";    //NOI18N
        private static final @StaticResource String PRIVATE_PACKAGE_BADGE = "org/netbeans/spi/java/project/support/ui/privateBadge.gif";  //NOI18N
        private static Image unlockBadge;
        private static Image lockBadge;
        
        public PackageFilterNode (final Node origNode) {
            super (origNode, new PackageFilterChildren (origNode));
        }
        
        @Override
        public void setName (final String name) {
            if (Utilities.isJavaIdentifier (name)) {
                super.setName (name);
            }
            else {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message (
                    NbBundle.getMessage(TreeRootNode.class,"MSG_InvalidPackageName"), NotifyDescriptor.INFORMATION_MESSAGE));
            }
        }
        
        @Override
        public Image getIcon (int type) {
            return accessibility(super.getIcon(type));
        }

        @Override
        public Image getOpenedIcon (int type) {
            return accessibility(super.getOpenedIcon(type)); 
        }
        
        private Image accessibility(final Image icon) {
            if (icon == null) {
                return icon;
            }
            final DataObject dobj = getLookup().lookup(DataObject.class);
            if (dobj == null) {
                return icon;
            }
            final FileObject fo = dobj.getPrimaryFile();
            if (fo == null) {
                return icon;
            }
            final Boolean pub = AccessibilityQuery.isPubliclyAccessible(fo);
            if (pub == Boolean.TRUE) {
                synchronized (PackageFilterNode.class) {
                    if (unlockBadge == null) {
                        unlockBadge = ImageUtilities.loadImage(PUBLIC_PACKAGE_BADGE); 
                    }
                }
                return ImageUtilities.mergeImages(icon, unlockBadge, 0, 0);
            } else if (pub == Boolean.FALSE) {
                synchronized (PackageFilterNode.class) {
                    if (lockBadge == null) {
                        lockBadge = ImageUtilities.loadImage(PRIVATE_PACKAGE_BADGE);
                    }
                }
                return ImageUtilities.mergeImages(icon, lockBadge, 0, 0);
            } else {
                return icon;
            }
        }

        @Override public String getShortDescription() {
            DataObject doj = getLookup().lookup(DataObject.class);
            if (doj != null) {
                FileObject f = doj.getPrimaryFile();
                ClassPath src = ClassPath.getClassPath(f, ClassPath.SOURCE);
                if (src != null) {
                    String pkg = src.getResourceName(f, '.', false);
                    if (pkg != null) {
                        return PackageDisplayUtils.getToolTip(f, pkg);
                    }
                }
            }
            return super.getShortDescription();
        }

    }
    
}

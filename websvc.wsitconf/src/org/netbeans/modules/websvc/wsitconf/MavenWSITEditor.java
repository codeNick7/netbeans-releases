/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 2006 Sun
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

package org.netbeans.modules.websvc.wsitconf;

import java.util.Collection;
import javax.swing.undo.UndoManager;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.websvc.wsitconf.ui.ErrorTopComponent;
import org.netbeans.modules.websvc.wsitconf.ui.service.ServiceTopComponent;
import org.netbeans.modules.websvc.wsitconf.ui.client.ClientTopComponent;
import org.netbeans.modules.websvc.wsitconf.util.UndoManagerHolder;
import org.netbeans.modules.websvc.wsitconf.wsdlmodelext.WSITModelSupport;
import org.netbeans.modules.xml.retriever.catalog.Utilities;
import org.netbeans.modules.xml.wsdl.model.*;
import org.netbeans.modules.xml.xam.locator.CatalogModelException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.modules.websvc.api.wseditor.WSEditor;
import org.netbeans.modules.websvc.jaxws.light.api.JAXWSLightSupport;
import org.netbeans.modules.websvc.jaxws.light.api.JaxWsService;
import org.netbeans.modules.websvc.wsitconf.spi.WsitProvider;
import org.netbeans.modules.websvc.wsitconf.wsdlmodelext.MavenWSITModelSupport;
import org.netbeans.modules.xml.xam.ModelSource;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author Martin Grebac
 */
public class MavenWSITEditor implements WSEditor, UndoManagerHolder {

    private static final Logger logger = Logger.getLogger(MavenWSITEditor.class.getName());

    private JAXWSLightSupport jaxWsSupport;
    private JaxWsService jaxWsService;
    private Project project;

    private UndoManager undoManager;
    private Collection<FileObject> createdFiles = new LinkedList<FileObject>();
            
    /**
     * Creates a new instance of MavenWSITEditor
     */
    public MavenWSITEditor(JAXWSLightSupport jaxWsSupport, JaxWsService jaxService, Project project) {
        this.jaxWsSupport = jaxWsSupport;
        this.jaxWsService = jaxService;
        this.project = project;
    }

    public String getTitle() {
        return NbBundle.getMessage(MavenWSITEditor.class, "EDITOR_TITLE"); //NOI18N
    }

    public JComponent createWSEditorComponent(Node node) {

        WSDLModel clientWsdlModel;
        WSDLModel wsdlModel;

        boolean isClient = jaxWsService.isServiceProvider();
        if (project != null) {
            try {
                wsdlModel = MavenWSITModelSupport.getModel(node, project, jaxWsSupport, jaxWsService, this, true, createdFiles);
                if (isClient) {
                    clientWsdlModel = MavenWSITModelSupport.getModel(node, project, jaxWsSupport, jaxWsService, this, true, createdFiles);
//                    wsdlModel = WSITModelSupport.getServiceModelForClient(wscs, client);
//                    return new ClientTopComponent(client, jaxWsModel, clientWsdlModel, wsdlModel, node);
                } else {
                    return new ServiceTopComponent(node, jaxWsService, wsdlModel, getUndoManager());
                }
            } catch(Exception e){
                logger.log(Level.SEVERE, null, e);
            }
        }
        return new ErrorTopComponent(NbBundle.getMessage(MavenWSITEditor.class, "TXT_WSIT_NotSupported"));
    }

    public void save(Node node) {
        if (node == null) return;
        try {
            WSDLModel model = MavenWSITModelSupport.getModel(node, project, jaxWsSupport, jaxWsService, this, false, createdFiles);
            if (model != null) {
                WSITModelSupport.save(model);
            }
        } catch (Exception e){
            logger.log(Level.SEVERE, null, e);
        }
    }

    public void cancel(Node node) {
        if (node == null) return;
        WSDLModel model = null;
        
        // remove imports from main config file if a new config file was imported
        FileObject srcRoot = node.getLookup().lookup(FileObject.class);
        if (srcRoot != null) {
            Project p = FileOwnerQuery.getOwner(srcRoot);
            if (p != null) {
                FileObject clientConfigFolder = getClientConfigFolder(p);
                WSDLModel mainModel = WSITModelSupport.getMainClientModel(clientConfigFolder);
                if (mainModel != null) {
                    Collection<Import> imports = mainModel.getDefinitions().getImports();
                    for (Import i : imports) {
                        try {
                            WSDLModel importedModel = i.getImportedWSDLModel();
                            ModelSource importedms = importedModel.getModelSource();
                            FileObject importedfo = org.netbeans.modules.xml.retriever.catalog.Utilities.getFileObject(importedms);
                            mainModel.startTransaction();
                            if (createdFiles.contains(importedfo)) {
                                mainModel.getDefinitions().removeImport(i);
                            }
                            mainModel.endTransaction();
                            FileObject mainFO = Utilities.getFileObject(mainModel.getModelSource());
                            if (mainFO == null) {
                                logger.log(Level.INFO, "Cannot find fileobject in lookup for: " + mainModel.getModelSource());
                            }
                            try {
                                DataObject mainDO = DataObject.find(mainFO);
                                if ((mainDO != null) && (mainDO.isModified())) {
                                    SaveCookie wsdlSaveCookie = mainDO.getCookie(SaveCookie.class);
                                    if(wsdlSaveCookie != null){
                                        wsdlSaveCookie.save();
                                    }
                                    mainDO.setModified(false);
                                }
                            } catch (IOException ioe) {
                                // ignore - just don't do anything
                            }
                        } catch (CatalogModelException ex) {
                            logger.log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        
        // now first undo all edits in the files
        try {
            model = MavenWSITModelSupport.getModel(node, project, jaxWsSupport, jaxWsService, this, false, createdFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (model != null) {
            try {
                if (getUndoManager() != null) {
                    while (getUndoManager().canUndo()) {
                        getUndoManager().undo();
                    }
                }
            } catch (Exception e){
                logger.log(Level.INFO, null, e);
            }
            FileObject fo = org.netbeans.modules.xml.retriever.catalog.Utilities.getFileObject(model.getModelSource());
            DataObject dO = null;
            try {
                dO = DataObject.find(fo);
            } catch (DataObjectNotFoundException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            if (dO != null) {
                try {
                    model.sync();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
                dO.setModified(false);dO.setModified(true);dO.setModified(false);
            }
        }
        
        // and remove all created files during this run
        if ((createdFiles != null) && (createdFiles.size() > 0)) {
            for (FileObject fo : createdFiles) {
                if (fo != null) {
                    try {
                        DataObject dO = DataObject.find(fo);
                        if (dO != null) {
                            dO.delete();
                        }
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static FileObject getClientConfigFolder(Project p) {
        WsitProvider wsitProvider = p.getLookup().lookup(WsitProvider.class);
        if (wsitProvider != null) {
            return wsitProvider.getConfigFilesFolder(true);
        }

        // proceed with default folder (META-INF) if the provider is not found
        FileObject folder = null;
        Sources sources = ProjectUtils.getSources(p);
        if (sources == null) return null;
        SourceGroup[] sourceGroups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);        
        if ((sourceGroups != null) && (sourceGroups.length > 0)) {
            folder = sourceGroups[0].getRootFolder();
            if (folder != null) {
                folder = folder.getFileObject("META-INF");
            }
            if ((folder == null) || (!folder.isValid())) {
                try {
                    folder = sourceGroups[0].getRootFolder().createFolder("META-INF");
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        return folder;
    }
    
    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    public String getDescription() {
        return NbBundle.getMessage(MavenWSITEditor.class, "WSIT_CONFIG_DESC");
    }
}

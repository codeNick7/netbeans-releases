/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.

 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.uml.integration.netbeans.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.SwingUtilities;

import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.java.JavaDataObject;

import org.netbeans.modules.uml.core.metamodel.structure.IProject;
import org.netbeans.modules.uml.core.reverseengineering.reintegration.ReverseEngineerTask;
import org.netbeans.modules.uml.core.support.umlsupport.IStrings;
import org.netbeans.modules.uml.core.support.umlsupport.Strings;
import org.netbeans.modules.uml.integration.netbeans.actions.ui.ReverseEngineerDescriptor;
import org.netbeans.modules.uml.integration.netbeans.actions.ui.ReverseEngineerPanel;
import org.netbeans.modules.uml.project.ProjectUtil;
import org.netbeans.modules.uml.project.UMLProject;
import org.netbeans.modules.uml.project.UMLProjectGenerator;
import org.netbeans.modules.uml.project.UMLProjectHelper;
import org.netbeans.modules.uml.project.ui.common.JavaSourceRootsUI;
import org.netbeans.modules.uml.project.ui.java.UMLJavaAssociationUtil;
import org.netbeans.modules.uml.project.ui.nodes.ModelRootNodeCookie;
import org.netbeans.modules.uml.project.ui.nodes.UMLPhysicalViewProvider;
import org.netbeans.modules.uml.project.ui.wizards.NewUMLProjectWizardIterator;
import org.netbeans.modules.uml.util.ITaskFinishListener;

import org.netbeans.spi.project.support.ant.AntProjectHelper;

import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.NotifyDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.CookieAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class ReverseEngineerAction extends CookieAction
    implements ITaskFinishListener
{
    Project sourceProject;
    ArrayList<String> sourceFiles;
    IProject umlIProject;
    
    protected void performAction(Node[] nodes)
    {
        sourceProject = (Project)nodes[0].getLookup().lookup(Project.class);
        sourceFiles = findJavaFiles(nodes);

        ReverseEngineerPanel rePanel;
        boolean doRE;
        
        // Java project node selected
        if (sourceProject != null)
        {
            rePanel = new ReverseEngineerPanel(
                sourceProject, 
                UMLJavaAssociationUtil.getAssociatedUMLProject(sourceProject));
            
            doRE = displayDialogDescriptor(rePanel);
        }
        
        // Java packages and/or classes selected
        else
        {
            sourceProject = getParentJavaProject(nodes);
            rePanel = new ReverseEngineerPanel(
                sourceProject,
                getNodeNames(nodes), 
                UMLJavaAssociationUtil.getAssociatedUMLProject(
                    getParentJavaProject(nodes)));
            
            doRE = displayDialogDescriptor(rePanel);
        }

        if (doRE)
            doReverseEngineer(rePanel);
    }


    private boolean displayDialogDescriptor(ReverseEngineerPanel rePanel)
    {
        ReverseEngineerDescriptor red = new ReverseEngineerDescriptor(
            rePanel, // inner pane
            NbBundle.getMessage(ReverseEngineerAction.class, 
                "LBL_ReverseEngineerDialog_Title"), // NOI18N
            true, // modal flag
            NotifyDescriptor.OK_CANCEL_OPTION, // button option type
            NotifyDescriptor.OK_OPTION, // default button
            DialogDescriptor.DEFAULT_ALIGN, // button alignment
            new HelpCtx("uml_reverse_eng_reverse_eng_dialog_box"), // NOI18N
            rePanel); // button action listener
  
        rePanel.getAccessibleContext().setAccessibleName(NbBundle
            .getMessage(ReverseEngineerAction.class, "ACSN_RevEngDialog")); // NOI18N
        rePanel.getAccessibleContext().setAccessibleDescription(NbBundle
            .getMessage(ReverseEngineerAction.class, "ACSD_RevEngDialog")); // NOI18N
        
        rePanel.requestFocus();
        
        return (DialogDisplayer.getDefault().notify(red) == 
            NotifyDescriptor.OK_OPTION);
    }
    
    
    protected int mode()
    {
        return CookieAction.MODE_ANY;
    }

    private String getLocalizedMessage(String key)
    {
        return NbBundle.getMessage(ReverseEngineerAction.class, key);
    }

    public String getName()
    {
        return getLocalizedMessage("CTL_ReverseEngineerAction"); // NOI18N
    }

    protected Class[] cookieClasses()
    {
        return new Class[] 
        {
            Project.class,
            DataFolder.class,
            JavaDataObject.class
        };
    }

    
// TODO: conover - fix this enable method
    /**
     * This operation is used to enable or disable the action in the menu.
     * It should be enabled for the Java projects nodes, and package or class
     * nodes in Java projects.
     */
    protected boolean enable(Node[] nodes)
    {
        boolean enabled = super.enable(nodes);

        if (!enabled)
            return false;

        Project project;
        
        // check for possible Project node selection
        if (nodes.length == 1)
        {
            project = (Project)nodes[0].getLookup().lookup(Project.class);

            if (project != null)
            {
                // project node was selected, now we just need to verify that
                // the project has source groups
                return hasSourceGroups(project);
            }
        }
        
        // if past this point, then the selection is more than one node,
        // or the single selected node is not a project node.
        
        // Because the active nodes can be multiple selected,
        // need make sure all the nodes are from the same project
        project = getParentJavaProject(nodes);

        if (project != null)
        {
            // selected nodes are from same Java project, otherwise project
            // var would have been null
            
            ArrayList javaFiles = findJavaFiles(nodes);
            
            // only enable if selection contains Java files 
            // or they are Java files
            return (javaFiles != null && javaFiles.size() > 0);
        }

        return false;
    }
    
    protected String iconResource()
    {
        return "org/netbeans/modules/uml/resources/toolbar_images/ReverseEngineer.png"; // NOI18N
    }


    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }


    protected boolean asynchronous()
    {
        return false;
    }
    
    
    // Helper Methods
    /////////////////
    
    private void doReverseEngineer(ReverseEngineerPanel rePanel)
    {
        if (rePanel.isCreateNewProject())
            reverseEngineerIntoNewProject(rePanel);
            
        else
            reverseEngineerIntoExistingProject(rePanel);
    }
    

    private void reverseEngineerIntoNewProject(
        final ReverseEngineerPanel rePanel)
    {
        Thread runner = new Thread()
        {
            public void run()
            {
                File prjDir = createNewUMLProject(rePanel);
                openUMLProject(prjDir);
            }
        };
        SwingUtilities.invokeLater(runner);
    }
    
    private void reverseEngineerIntoExistingProject(
        final ReverseEngineerPanel rePanel)
    {
        Thread runner = new Thread()
        {
            public void run()
            {
                useExistingUMLProject((UMLProject)rePanel.getUMLProject());
            }
        };
        SwingUtilities.invokeLater(runner);
    }
    
    private File createNewUMLProject(ReverseEngineerPanel rePanel)
    {
        try
        {
            JavaSourceRootsUI.JavaSourceRootsModel roots = null;
            ArrayList<String> sources = sourceFiles;
            
            if (rePanel.isShowSourceRoots())
            {
                sources = null;
                roots = rePanel.getSourceRoots();
            }
            
            AntProjectHelper helper = UMLProjectGenerator.createRevEngProject(
                new File(rePanel.getProjectFolder()),
                rePanel.getProjectName(),
                rePanel.getJavaProject(),
                roots,
                sourceFiles, // ArrayList of sources to be RE'ed
                NewUMLProjectWizardIterator.TYPE_REVERSE_ENGINEER);
            
            return FileUtil.toFile(helper.getProjectDirectory());
        }

        catch (IOException ex)
        {
            // TODO: conover - provide proper handling
            ex.printStackTrace();
        }
        
        return null;
    }

    private void useExistingUMLProject(UMLProject umlProject)
    {
        IStrings fileNames = new Strings();
        Iterator iter = sourceFiles.iterator();
        
        UMLProjectHelper umlPrjHelper = (UMLProjectHelper)(umlProject)
            .getLookup().lookup(UMLProjectHelper.class);
        
        umlIProject = (IProject)umlPrjHelper.getProject();

        while (iter.hasNext())
            fileNames.add(iter.next().toString());

        reverseEngineerIntoExistingUMLProject(umlIProject, fileNames);
    }

    private void reverseEngineerIntoExistingUMLProject(
        IProject targetProject, final IStrings javaFiles)
    {
        ReverseEngineerTask reTask = new ReverseEngineerTask(
            targetProject,
            javaFiles,
            false, false, true, true, this);
        
        RequestProcessor processor = 
            new RequestProcessor("uml/ReverseEngineer"); // NOI18N

        processor.post(reTask);
        
//        refreshUMLProject(targetProject);

//        final IUMLParsingIntegrator integrator = new UMLParsingIntegrator();
//        integrator.setFiles(javaFiles);
//        
//        integrator.reverseEngineer(
//            targetProject,
//            false, // this brings up the file chooser
//            false, // this should be false for now.
//            true,  // this will display the progress dialog
//            true); // this will cause all the classes to
//                   // be created in their own file. Not
//                   // currently enabled
    }
    
    
    public void taskFinished()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                Project proj = ProjectUtil
                    .findNetBeansProjectForModel(umlIProject);

                UMLPhysicalViewProvider provider =
                    (UMLPhysicalViewProvider)proj.getLookup().
                    lookup(UMLPhysicalViewProvider.class);

                ModelRootNodeCookie cookie =
                    provider.getModelRootNodeCookie();

                if (cookie!=null)
                    cookie.recalculateChildren();
            }
        });
    }
    
    private boolean hasSourceGroups(Project project)
    {
        if (project == null)
        {
            throw new IllegalArgumentException(
                "Parameter \"project\" cannot be null.");
        }
        
        Sources sources = (Sources)project
            .getLookup().lookup(Sources.class);

        if (sources != null)
        {
            SourceGroup[] srcGrps = sources.getSourceGroups(
                JavaProjectConstants.SOURCES_TYPE_JAVA);

            if (srcGrps != null && srcGrps.length > 0)
                return true;
        }
        
        return false;
    }
    
    
    
    private void openUMLProject(File projectDir)
    {
        final File pjrDir = projectDir;

        if (pjrDir == null)
            return;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    synchronized (getLock())
                    {
                        FileObject  dirFO = FileUtil.toFileObject(pjrDir);
                        final Project prj = 
                            ProjectManager.getDefault().findProject(dirFO);

                        if (prj instanceof UMLProject)
                        {
                            OpenProjects.getDefault().open(
                                new Project[] {prj}, // Put the project into OpenProjectList
                                false); // And optionaly open subprojects

                            SwingUtilities.invokeLater( new Runnable()
                            {
                                public void run()
                                {
                                    TopComponent tc = WindowManager.getDefault().
                                        findTopComponent("projectTabLogical_tc");

                                    if (tc==null)
                                        return;

                                    ExplorerManager explorerManager =
                                        ((ExplorerManager.Provider)tc).getExplorerManager();
                                    Node root = explorerManager.getRootContext();

                                    Node projNode = root.getChildren()
                                        .findChild(ProjectUtils
                                            .getInformation(prj).getName());

                                    if (projNode != null)
                                    {
                                        try
                                        {
                                            explorerManager.setSelectedNodes(
                                                new Node[] {projNode});
                                        }

                                        catch (Exception ignore)
                                        {
                                            // may ignore it
                                        }
                                    }
                                }
                            });
                        }
                    }
                }

                catch (IOException e)
                {
                    ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e );
                }
            }
        });
    }
    
    protected ArrayList<String> findJavaFiles(Node[] nodes)
    {
        HashSet<String> files = new HashSet<String>();
        ArrayList<String> ordered = new ArrayList<String>();
        
        for (int i = 0; i < nodes.length; ++i)
        {
            DataObject dobj = (DataObject)nodes[i].getCookie(DataObject.class);
            DataShadow dshw = (DataShadow)nodes[i].getCookie(DataShadow.class);
            
            if (dshw != null)
                dobj = dshw.getOriginal();
            
            if (dobj != null)
                findJavaFiles(dobj, files, ordered);
            
            // else valid only for 1st node and only one node selected
            else if (i == 0 && nodes.length == 1)
            {
                // selected node must be a Project type
                Project project = (Project)nodes[i]
                    .getLookup().lookup(Project.class);
                
                if (project != null)
                    findJavaFiles(nodes[0].getChildren(), files, ordered);
            }
        }
        
        return ordered;
    }

    protected void findJavaFiles(
        DataObject dobj, HashSet<String> files, ArrayList<String> ord)
    {
        if (dobj instanceof DataFolder)
        {
            DataFolder df = (DataFolder) dobj;
            DataObject[] obj = df.getChildren();
            
            for (int i = 0; i < obj.length; ++i)
                findJavaFiles(obj[i], files, ord);
        }
        
        else
        {
            FileObject fo = dobj.getPrimaryFile();
            
            if (isJavaFile(fo))
            {
                String filename = FileUtil.toFile(fo).toString();
                String downcased = filename.toLowerCase();
                
                if (!files.contains(downcased))
                {
                    files.add(downcased);
                    ord.add(filename);
                }
            }
        }
    }
    
    protected void findJavaFiles(
        Children prjChildren, HashSet<String> files, ArrayList<String> ord)
    {
        Node[] children = prjChildren.getNodes();
        
        for (Node child: children)
        {
            DataObject dobj = (DataObject)child.getCookie(DataObject.class);
            
            if (dobj != null)
                findJavaFiles(dobj, files, ord);
        }
    }
    
    public static boolean isJavaFile(FileObject file)
    {
        return file != null && file.isValid() &&
            file.isData() && file.getExt().equals("java"); // NOI18N
    }

    /** This method checks if all the active nodes are from the same project.
     * returns the project object if all the nodes have the same project;
     * otherwise, return null.
     */
    private Project getParentJavaProject(Node[] activatedNodes)
    {
        int total = activatedNodes.length;
        boolean sameOwner = true;
        DataObject dObj = null;
        FileObject fObj = null;
        Project projOwner = null;
        
        for (int i = 0; i < total; i++)
        {
            dObj = (DataObject)activatedNodes[i].getCookie(DataObject.class);
            
            if (dObj == null)
            {
                projOwner = (Project)activatedNodes[i]
                    .getLookup().lookup(Project.class);
                
                return projOwner != null && activatedNodes.length == 1
                    ? projOwner : null;
            }
            
            else
            {
                fObj = dObj.getPrimaryFile();
                fObj.refresh();
            }
            
            if (projOwner == null)
                // get the project owner of the 1st active node
                projOwner = FileOwnerQuery.getOwner(fObj);
            
            else
                // compare the project of the 1st node with that of subsequent nodes
                sameOwner = (projOwner.equals(FileOwnerQuery.getOwner(fObj)));
            
            // if any of the active nodes does not come from the same project,
            // no need to test further; get out of the loop.
            if (!sameOwner)
                break;
        }
        
        return  (sameOwner ? projOwner : null);
    }

    private ArrayList getNodeNames(Node[] nodes)
    {
        if (sourceFiles == null || sourceFiles.size() == 0)
            return null;
        
        ArrayList<String> nodeNames = new ArrayList<String>(nodes.length);
        
        for (Node node: nodes)
        {
            nodeNames.add(node.getName());
        }
        
        return nodeNames;
    }

}

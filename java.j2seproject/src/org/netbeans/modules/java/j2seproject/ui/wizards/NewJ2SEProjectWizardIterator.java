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

package org.netbeans.modules.java.j2seproject.ui.wizards;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.modules.java.api.common.project.ProjectProperties;
import org.netbeans.modules.java.j2seproject.J2SEProjectGenerator;
import org.netbeans.spi.java.project.support.ui.SharableLibrariesUtils;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.ErrorManager;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.modules.java.j2seproject.api.J2SEProjectBuilder;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 * Wizard to create a new J2SE project.
 */
public class NewJ2SEProjectWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator {

    enum WizardType {APP, LIB, EXT}
    
    static final String PROP_NAME_INDEX = "nameIndex";      //NOI18N
    static final String PROP_BUILD_SCRIPT_NAME = "buildScriptName"; //NOI18N
    static final String PROP_DIST_FOLDER = "distFolder";    //NOI18N

    private static final String MANIFEST_FILE = "manifest.mf"; // NOI18N

    private static final long serialVersionUID = 1L;
    
    private WizardType type;
    
    private NewJ2SEProjectWizardIterator(WizardType type) {
        this.type = type;
    }

    @TemplateRegistration(folder="Project/Standard", position=100, displayName="#template_app", iconBase="org/netbeans/modules/java/j2seproject/ui/resources/j2seProject.png", description="../resources/emptyProject.html")
    @Messages("template_app=Java Application")
    public static NewJ2SEProjectWizardIterator app() {
        return new NewJ2SEProjectWizardIterator(WizardType.APP);
    }

    @TemplateRegistration(folder="Project/Standard", position=200, displayName="#template_library", iconBase="org/netbeans/modules/java/j2seproject/ui/resources/j2seProject.png", description="../resources/emptyLibrary.html")
    @Messages("template_library=Java Class Library")
    public static NewJ2SEProjectWizardIterator library() {
        return new NewJ2SEProjectWizardIterator(WizardType.LIB);
    }

    @TemplateRegistration(folder="Project/Standard", position=300, displayName="#template_existing", iconBase="org/netbeans/modules/java/j2seproject/ui/resources/j2seProject.png", description="../resources/existingProject.html")
    @Messages("template_existing=Java Project with Existing Sources")
    public static NewJ2SEProjectWizardIterator existing() {
        return new NewJ2SEProjectWizardIterator(WizardType.EXT);
    }

    private WizardDescriptor.Panel[] createPanels() {
        switch (type) {
            case EXT:
                return new WizardDescriptor.Panel[] {
                    new PanelConfigureProject(type),
                    new PanelSourceFolders.Panel(),
                    new PanelIncludesExcludes(),
                };
            default:
                return new WizardDescriptor.Panel[] {
                    new PanelConfigureProject(type)
                };
        }
    }
    
    private String[] createSteps() {
        switch (type) {
            case EXT:
                return new String[] {
                    NbBundle.getMessage(NewJ2SEProjectWizardIterator.class,"LAB_ConfigureProject"),
                    NbBundle.getMessage(NewJ2SEProjectWizardIterator.class,"LAB_ConfigureSourceRoots"),
                    NbBundle.getMessage(NewJ2SEProjectWizardIterator.class,"LAB_PanelIncludesExcludes"),
                };
            default:
                return new String[] {
                    NbBundle.getMessage(NewJ2SEProjectWizardIterator.class,"LAB_ConfigureProject"),
                };
        }
    }
    
    
    public Set<?> instantiate() throws IOException {
        assert false : "Cannot call this method if implements WizardDescriptor.ProgressInstantiatingIterator.";
        return null;
    }
        
    public Set<FileObject> instantiate (ProgressHandle handle) throws IOException {
        handle.start (4);
        //handle.progress (NbBundle.getMessage (NewJ2SEProjectWizardIterator.class, "LBL_NewJ2SEProjectWizardIterator_WizardProgress_ReadingProperties"));
        Set<FileObject> resultSet = new HashSet<FileObject>();
        File dirF = (File)wiz.getProperty("projdir");        //NOI18N
        if (dirF == null) {
            throw new NullPointerException ("projdir == null, props:" + wiz.getProperties());
        }
        dirF = FileUtil.normalizeFile(dirF);
        String name = (String)wiz.getProperty("name");        //NOI18N
        String mainClass = (String)wiz.getProperty("mainClass");        //NOI18N
        String librariesDefinition = (String)wiz.getProperty(PanelOptionsVisual.SHARED_LIBRARIES);
        if (librariesDefinition != null) {
            if (!librariesDefinition.endsWith(File.separator)) {
                librariesDefinition += File.separatorChar;
            }
            librariesDefinition += SharableLibrariesUtils.DEFAULT_LIBRARIES_FILENAME;
        }
        handle.progress (NbBundle.getMessage (NewJ2SEProjectWizardIterator.class, "LBL_NewJ2SEProjectWizardIterator_WizardProgress_CreatingProject"), 1);
        switch (type) {
        case EXT:
            File[] sourceFolders = (File[])wiz.getProperty("sourceRoot");        //NOI18N
            File[] testFolders = (File[])wiz.getProperty("testRoot");            //NOI18N
            String buildScriptName = (String) wiz.getProperty(PROP_BUILD_SCRIPT_NAME);
            String distFolder = (String) wiz.getProperty(PROP_DIST_FOLDER);
            AntProjectHelper h = new J2SEProjectBuilder(dirF, name).
                addSourceRoots(sourceFolders).
                addTestRoots(testFolders).
                skipTests(testFolders.length == 0).
                setManifest(MANIFEST_FILE).
                setLibrariesDefinitionFile(librariesDefinition).
                setBuildXmlName(buildScriptName).
                setDistFolder(distFolder).
                build();
            EditableProperties ep = h.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
            String includes = (String) wiz.getProperty(ProjectProperties.INCLUDES);
            if (includes == null) {
                includes = "**"; // NOI18N
            }
            ep.setProperty(ProjectProperties.INCLUDES, includes);
            String excludes = (String) wiz.getProperty(ProjectProperties.EXCLUDES);
            if (excludes == null) {
                excludes = ""; // NOI18N
            }
            ep.setProperty(ProjectProperties.EXCLUDES, excludes);
            h.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, ep);
            handle.progress (2);
            for (File f : sourceFolders) {
                FileObject srcFo = FileUtil.toFileObject(f);
                if (srcFo != null) {
                    resultSet.add (srcFo);
                }
            }
            break;
        default:
            h = J2SEProjectGenerator.createProject(dirF, name, mainClass, type == WizardType.APP ? MANIFEST_FILE : null, librariesDefinition, true);
            handle.progress (2);
            if (mainClass != null && mainClass.length () > 0) {
                final FileObject sourcesRoot = h.getProjectDirectory ().getFileObject ("src");        //NOI18N
                if (sourcesRoot != null) {
                    final FileObject mainClassFo = getMainClassFO (sourcesRoot, mainClass);
                    if (mainClassFo != null) {
                        resultSet.add (mainClassFo);
                    }
                }
            }
            // if ( type == TYPE_LIB ) {
                // resultSet.add( h.getProjectDirectory ().getFileObject ("src") );        //NOI18N 
                // resultSet.add( h.getProjectDirectory() ); // Only expand the project directory
            // }
        }
        FileObject dir = FileUtil.toFileObject(dirF);
        switch (type) {
            case APP:
                createManifest(dir, false);
                break;
            case EXT:
                createManifest(dir, true);
                break;
        }
        handle.progress (3);

        // Returning FileObject of project diretory. 
        // Project will be open and set as main
        final Integer ind = (Integer) wiz.getProperty(PROP_NAME_INDEX);
        if (ind != null) {
            switch (type) {
                case APP:
                    WizardSettings.setNewApplicationCount(ind);
                    break;
                case LIB:
                    WizardSettings.setNewLibraryCount(ind);
                    break;
                case EXT:
                    WizardSettings.setNewProjectCount(ind);
                    break;
            }
        }
        resultSet.add (dir);
        handle.progress (NbBundle.getMessage (NewJ2SEProjectWizardIterator.class, "LBL_NewJ2SEProjectWizardIterator_WizardProgress_PreparingToOpen"), 4);
        dirF = (dirF != null) ? dirF.getParentFile() : null;
        if (dirF != null && dirF.exists()) {
            ProjectChooser.setProjectsFolder (dirF);    
        }
         
        SharableLibrariesUtils.setLastProjectSharable(librariesDefinition != null);
        return resultSet;
    }
    
        
    private transient int index;
    private transient WizardDescriptor.Panel[] panels;
    private transient WizardDescriptor wiz;
    
    public void initialize(WizardDescriptor wiz) {
        this.wiz = wiz;
        index = 0;
        panels = createPanels();
        // Make sure list of steps is accurate.
        String[] steps = createSteps();
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (steps[i] == null) {
                // Default step name to component name of panel.
                // Mainly useful for getting the name of the target
                // chooser to appear in the list of steps.
                steps[i] = c.getName();
            }
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent)c;
                // Step #.
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                // Step name (actually the whole list for reference).
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
            }
        }
        //set the default values of the sourceRoot and the testRoot properties
        this.wiz.putProperty("sourceRoot", new File[0]);    //NOI18N
        this.wiz.putProperty("testRoot", new File[0]);      //NOI18N
    }

    public void uninitialize(WizardDescriptor wiz) {
        if (this.wiz != null) {
            this.wiz.putProperty("projdir",null);           //NOI18N
            this.wiz.putProperty("name",null);          //NOI18N
            this.wiz.putProperty("mainClass",null);         //NOI18N
            switch (type) {
            case EXT:
                this.wiz.putProperty("sourceRoot",null);    //NOI18N
                this.wiz.putProperty("testRoot",null);      //NOI18N
            }
            this.wiz = null;
            panels = null;
        }
    }
    
    public String name() {
        return NbBundle.getMessage(NewJ2SEProjectWizardIterator.class, "LAB_IteratorName", index + 1, panels.length);
    }
    
    public boolean hasNext() {
        return index < panels.length - 1;
    }
    public boolean hasPrevious() {
        return index > 0;
    }
    public void nextPanel() {
        if (!hasNext()) throw new NoSuchElementException();
        index++;
    }
    public void previousPanel() {
        if (!hasPrevious()) throw new NoSuchElementException();
        index--;
    }
    public WizardDescriptor.Panel current () {
        return panels[index];
    }
    
    // If nothing unusual changes in the middle of the wizard, simply:
    public final void addChangeListener(ChangeListener l) {}
    public final void removeChangeListener(ChangeListener l) {}
    
    // helper methods, finds mainclass's FileObject
    private FileObject getMainClassFO (FileObject sourcesRoot, String mainClass) {
        // replace '.' with '/'
        mainClass = mainClass.replace ('.', '/'); // NOI18N
        
        // ignore unvalid mainClass ???
        
        return sourcesRoot.getFileObject (mainClass+ ".java"); // NOI18N
    }

    static String getPackageName (String displayName) {
        StringBuffer builder = new StringBuffer ();
        boolean firstLetter = true;
        for (int i=0; i< displayName.length(); i++) {
            char c = displayName.charAt(i);            
            if ((!firstLetter && Character.isJavaIdentifierPart (c)) || (firstLetter && Character.isJavaIdentifierStart(c))) {
                firstLetter = false;
                if (Character.isUpperCase(c)) {
                    c = Character.toLowerCase(c);
                }                    
                builder.append(c);
            }            
        }
        return builder.length() == 0 ? NbBundle.getMessage(NewJ2SEProjectWizardIterator.class,"TXT_DefaultPackageName") : builder.toString();
    }
    
    /**
     * Create a new application manifest file with minimal initial contents.
     * @param dir the directory to create it in
     * @throws IOException in case of problems
     */
    private static void createManifest(final FileObject dir, final boolean skeepIfExists) throws IOException {
        if (skeepIfExists && dir.getFileObject(MANIFEST_FILE) != null) {
            return;
        }
        else {
            FileObject manifest = dir.createData(MANIFEST_FILE);
            FileLock lock = manifest.lock();
            try {
                OutputStream os = manifest.getOutputStream(lock);
                try {
                    PrintWriter pw = new PrintWriter(os);
                    pw.println("Manifest-Version: 1.0"); // NOI18N
                    pw.println("X-COMMENT: Main-Class will be added automatically by build"); // NOI18N
                    pw.println(); // safest to end in \n\n due to JRE parsing bug
                    pw.flush();
                } finally {
                    os.close();
                }
            } finally {
                lock.releaseLock();
            }
        }
    }

}

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

package org.netbeans.modules.maven.samples;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.modules.j2ee.deployment.devmodules.api.Deployment;
import org.netbeans.modules.j2ee.deployment.devmodules.api.InstanceRemovedException;
import org.netbeans.modules.j2ee.deployment.devmodules.api.ServerInstance;
import org.netbeans.modules.javaee.project.api.JavaEEProjectSettings;
import org.netbeans.modules.maven.api.NbMavenProject;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

public class MavenSamplesWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator {
    
    private int index;
    private WizardDescriptor.Panel[] panels;
    protected WizardDescriptor wiz;
    private String title;

    public MavenSamplesWizardIterator() {
    }
    
    public MavenSamplesWizardIterator(String title) {
        this.title = title;
    }
    
    public static MavenSamplesWizardIterator createCalculatorIterator() {
        return new MavenSamplesWizardIterator(NbBundle.getMessage(MavenSamplesWizardIterator.class, "Templates/Project/Samples/Maven/MavenCalculator"));
    }
    
    public static MavenSamplesWizardIterator createCalculatorClientIterator() {
        return new MavenSamplesWizardIterator(NbBundle.getMessage(MavenSamplesWizardIterator.class, "Templates/Project/Samples/Maven/MavenCalculatorClient"));
    }
    
    public static MavenSamplesWizardIterator createScrumToysIterator() {
        return new MavenSamplesWizardIterator(NbBundle.getMessage(MavenSamplesWizardIterator.class, "Templates/Project/Samples/Maven/MavenScrumToys"));
    }
    
    public static MavenSamplesWizardIterator createIterator() {
        return new MavenSamplesWizardIterator();
    }
    
    protected WizardDescriptor.Panel[] createPanels() {
        return new WizardDescriptor.Panel[] {
            new MavenSamplesWizardPanel(true)
        };
    }
    
    protected String[] createSteps() {
        return new String[] {
            NbBundle.getMessage(MavenSamplesWizardIterator.class, "LBL_CreateProjectStep")
        };
    }
    
    @Override
    public Set instantiate(ProgressHandle handle) throws IOException {
        handle.start(5);
        Set<FileObject> resultSet = new LinkedHashSet<FileObject>();
        File dirF = FileUtil.normalizeFile((File) wiz.getProperty(WizardProperties.PROJ_DIR));
        createFolder(dirF);
        
        FileObject template = Templates.getTemplate(wiz);
        FileObject dir = FileUtil.toFileObject(dirF);
        unZipFile(template.getInputStream(), dir);
        configureProject(dir);
        ProjectManager.getDefault().clearNonProjectCache();
        
        // Always open top dir as a project:
        resultSet.add(dir);
        handle.progress(1);
        Project prj = ProjectManager.getDefault().findProject(dir);
        if (prj != null) {
            configureServer(prj);

            final NbMavenProject mvn = prj.getLookup().lookup(NbMavenProject.class);
            if (mvn != null) {
                mvn.downloadDependencyAndJavadocSource(false);
            }
        }
        handle.progress(3);

        // Look for nested projects to open as well:
        Enumeration e = dir.getFolders(true);
        while (e.hasMoreElements()) {
            FileObject subfolder = (FileObject) e.nextElement();
            if (ProjectManager.getDefault().isProject(subfolder)) {
                prj = ProjectManager.getDefault().findProject(subfolder);
                if (prj != null) {
                    NbMavenProject mvn = prj.getLookup().lookup(NbMavenProject.class);
                    if (mvn != null) {
                        mvn.downloadDependencyAndJavadocSource(false);
                    }
                    resultSet.add(subfolder);
                }
            }
        }
        handle.progress(4);
        
        File parent = dirF.getParentFile();
        if (parent != null && parent.exists()) {
            ProjectChooser.setProjectsFolder(parent);
        }
        
        return resultSet;
    }
    
    @Override
    public void initialize(WizardDescriptor wiz) {
        this.wiz = wiz;
        wiz.putProperty ("NewProjectWizard_Title", title); // NOI18N        
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
                JComponent jc = (JComponent) c;
                // Step #.
                jc.putClientProperty(WizardProperties.SELECTED_INDEX, new Integer(i));
                // Step name (actually the whole list for reference).
                jc.putClientProperty(WizardProperties.CONTENT_DATA, steps);
            }
        }
        
        FileObject template = Templates.getTemplate(wiz);
        
        wiz.putProperty(WizardProperties.NAME, template.getName());
    }
    
    @Override
    public void uninitialize(WizardDescriptor wiz) {
        this.wiz.putProperty(WizardProperties.PROJ_DIR, null);
        this.wiz.putProperty(WizardProperties.NAME, null);
        this.wiz = null;
        panels = null;
    }
    
    @Override
    public String name() {
        return NbBundle.getMessage(MavenSamplesWizardIterator.class, "LBL_Order",
                new Object[] {new Integer(index + 1), new Integer(panels.length)});
    }
    
    @Override
    public boolean hasNext() {
        return index < panels.length - 1;
    }
    
    @Override
    public boolean hasPrevious() {
        return index > 0;
    }
    
    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }
    
    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }
    
    @Override
    public WizardDescriptor.Panel current() {
        return panels[index];
    }
    
    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public final void addChangeListener(ChangeListener l) {}
    @Override
    public final void removeChangeListener(ChangeListener l) {}
    
    private static void unZipFile(InputStream source, FileObject projectRoot) throws IOException {
        try {
            ZipInputStream str = new ZipInputStream(source);
            ZipEntry entry;
            while ((entry = str.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    FileUtil.createFolder(projectRoot, entry.getName());
                } else {
                    FileObject fo = FileUtil.createData(projectRoot, entry.getName());
                    OutputStream out = fo.getOutputStream();
                    try {
                        FileUtil.copy(str, out);
                    } finally {
                        out.close();
                    }
                }
            }
        } finally {
            source.close();
        }
    }
    
    /** TODO: replace with FileUtil.createFolder(File) in trunk. */
    private static FileObject createFolder(File dir) throws IOException {
        Stack<String> stack = new Stack<String>();
        while (!dir.exists()) {
            stack.push(dir.getName());
            dir = dir.getParentFile();
        }
        FileObject dirFO = FileUtil.toFileObject(dir);
        if (dirFO == null) {
            refreshFileSystem(dir);
            dirFO = FileUtil.toFileObject(dir);
        }
        assert dirFO != null;
        while (!stack.isEmpty()) {
            dirFO = dirFO.createFolder(stack.pop());
        }
        return dirFO;
    }
    
    private static void refreshFileSystem(final File dir) throws FileStateInvalidException {
        File rootF = dir;
        while (rootF.getParentFile() != null) {
            rootF = rootF.getParentFile();
        }
        FileObject dirFO = FileUtil.toFileObject(rootF);
        assert dirFO != null : "At least disk roots must be mounted! " + rootF; // NOI18N
        dirFO.getFileSystem().refresh(false);
    }

    @Override
    public Set instantiate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // #230450
    protected void configureServer(Project project) {
        if (!isJavaEEProject(project)) {
            return;
        }

        Set<String> gfServers = new LinkedHashSet<String>();
        gfServers.add("gfv3ee6"); // NOI18N
        gfServers.add("gfv3ee6wc"); // NOI18N

        String[] registeredServers = Deployment.getDefault().getServerInstanceIDs();
        for (String serverInstanceID : registeredServers) {
            ServerInstance si = Deployment.getDefault().getServerInstance(serverInstanceID);
            if (si != null) {
                try {
                    String serverID = si.getServerID();

                    if (gfServers.contains(serverID)) {
                        JavaEEProjectSettings.setServerInstanceID(project, serverInstanceID);
                        return;
                    }
                } catch (InstanceRemovedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private boolean isJavaEEProject(Project project) {
        NbMavenProject mavenProject = project.getLookup().lookup(NbMavenProject.class);
        if (mavenProject != null) {
            String packagingType = mavenProject.getPackagingType();
            if ("war".equals(packagingType) ||
                "ejb".equals(packagingType) ||
                "ear".equals(packagingType) ||
                "app-client".equals(packagingType)) {

                return true;
            }
        }
        return false;
    }

    protected void configureProject(FileObject dir) throws IOException {}

}

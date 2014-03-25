/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2014 Sun Microsystems, Inc.
 */

package org.netbeans.modules.avatar_js.project;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectFactory2;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jaroslav Tulach <jtulach@netbeans.org>
 */
@ServiceProvider(service = ProjectFactory.class)
public final class AvatarJSProjectFactory implements ProjectFactory2 {
    private static final Logger LOG = Logger.getLogger(AvatarJSProjectFactory.class.getName());
    @StaticResource
    private static final String ICON = "org/netbeans/modules/avatar_js/project/resources/nodejs.png";
    
    
    @Override
    public ProjectManager.Result isProject2(FileObject projectDirectory) {
        FileObject pkgJson = projectDirectory.getFileObject("package.json");
        if (pkgJson == null) {
            return null;
        }
        ImageIcon img = ImageUtilities.loadImageIcon(ICON, false);
        return new ProjectManager.Result(img);
    }

    @Override
    public boolean isProject(FileObject projectDirectory) {
        return isProject2(projectDirectory) != null;
    }

    @Override
    public Project loadProject(FileObject projectDirectory, ProjectState state) throws IOException {
        return new PackageJSONPrj(projectDirectory);
    }

    @Override
    public void saveProject(Project project) throws IOException, ClassCastException {
        if (project instanceof PackageJSONPrj) {
            ((PackageJSONPrj)project).save();
        }
    }
    
    private static final class PackageJSONPrj implements Project, 
    ActionProvider {
        private final FileObject dir;
        private final Lookup lkp;
        private JSONObject pckg;

        public PackageJSONPrj(FileObject dir) {
            this.dir = dir;
            this.lkp = Lookups.singleton(this);
        }
        
        @Override
        public FileObject getProjectDirectory() {
            return dir;
        }

        @Override
        public Lookup getLookup() {
            return lkp;
        }
        
        public void save() throws IOException {
        }
        
        @Override
        public String[] getSupportedActions() {
            return new String[] {
                ActionProvider.COMMAND_RUN
            };
        }

        @Override
        public void invokeAction(String command, Lookup context) throws IllegalArgumentException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
            return getPackage().get("main") != null;
        }
        
        private JSONObject getPackage() {
            if (pckg != null) {
                return pckg;
            }
            FileObject fo = dir.getFileObject("package.json");
            if (fo != null) {
                try {
                    pckg = (JSONObject) new JSONParser().parse(fo.asText("UTF-8"));
                } catch (Exception ex) {
                    LOG.log(Level.WARNING, "Error parsing " + fo, ex);
                }
            }
            if (pckg == null) {
                pckg = new JSONObject();
            }
            return pckg;
        }
    }
}

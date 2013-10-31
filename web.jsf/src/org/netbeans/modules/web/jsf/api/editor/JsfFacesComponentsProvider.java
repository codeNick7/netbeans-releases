/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package org.netbeans.modules.web.jsf.api.editor;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.queries.SourceForBinaryQuery;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.SourceUtils;
import org.netbeans.api.project.Project;
import org.netbeans.modules.j2ee.metadata.model.api.MetadataModel;
import org.netbeans.modules.j2ee.metadata.model.api.MetadataModelAction;
import org.netbeans.modules.j2ee.metadata.model.api.MetadataModelException;
import org.netbeans.modules.web.api.webmodule.WebModule;
import org.netbeans.modules.web.jsf.JSFConfigUtilities;
import org.netbeans.modules.web.jsf.JSFUtils;
import org.netbeans.modules.web.jsf.api.metamodel.Component;
import org.netbeans.modules.web.jsf.api.metamodel.JsfModel;
import org.netbeans.modules.web.jsf.impl.metamodel.ComponentImpl;
import org.netbeans.modules.web.jsfapi.api.Attribute;
import org.netbeans.modules.web.jsfapi.api.Library;
import org.netbeans.modules.web.jsfapi.api.LibraryComponent;
import org.netbeans.modules.web.jsfapi.api.LibraryType;
import org.netbeans.modules.web.jsfapi.api.Tag;
import org.netbeans.modules.web.jsfapi.spi.LibraryUtils;
import org.openide.filesystems.FileObject;

/**
 * Provides libraries defined by @FacesComponents.
 *
 * @author Martin Fousek <marfous@netbeans.org>
 */
public class JsfFacesComponentsProvider {

    private static final Logger LOGGER = Logger.getLogger(JsfFacesComponentsProvider.class.getName());

    private JsfFacesComponentsProvider() {
    }

    /**
     * Gets list of component tags and libraries defined by them.
     * @param project project to examine
     * @return list of libraries with defined component tags.
     */
    public static Collection<? extends Library> getLibraries(Project project) {
        long start = System.currentTimeMillis();
        try {
            MetadataModel<JsfModel> model = JSFUtils.getModel(project);
            if (model == null) {
                return Collections.emptyList();
            }
            try {
                return model.runReadAction(new MetadataModelAction<JsfModel, Collection<? extends Library>>() {

                    @Override
                    public Collection<? extends Library> run(JsfModel metadata) throws Exception {
                        List<Component> facesComponents = metadata.getElements(Component.class);
                        Map<String, FacesComponentLibrary> libraries = new HashMap<>();
                        for (Component component : facesComponents) {
                            // @FacesComponent to be used as a tag in the facelet can be defined by annotation only for now.
                            if (component instanceof ComponentImpl) {
                                ComponentImpl facesComponent = (ComponentImpl) component;
                                includeComponentIntoLibraries(libraries, (ComponentImpl) facesComponent);
                            }
                        }
                        return libraries.values();
                    }
                });
            } catch (MetadataModelException ex) {
                LOGGER.log(Level.INFO, "Failed to read Faces Components for " + project, ex);
            } catch (IOException | IllegalStateException ex) {
                LOGGER.log(Level.INFO, "Failed to read Faces Components for " + project, ex);
            }
            return Collections.emptyList();
        } finally {
            LOGGER.log(Level.FINEST, "JsfFacesComponentsProvider parsed for elements in {0} ms.", System.currentTimeMillis()- start);
        }
    }

    private static void includeComponentIntoLibraries(Map<String, FacesComponentLibrary> libraries, ComponentImpl facesComponent) {
        if (!facesComponent.isCreateTag()) {
            return;
        }

        String namespace = facesComponent.getNamespace();
        FacesComponentLibrary library = libraries.get(namespace);
        if (library == null) {
            library = new FacesComponentLibrary(namespace);
            libraries.put(namespace, library);
        }
        library.addComponent(new FacesLibraryComponent(library, facesComponent));
    }

    public static final class FacesComponentLibrary implements Library {

        private static final String FACES_COMPONENT = "Faces Component";

        private final String namespace;
        private final Map<String, LibraryComponent> components;

        public FacesComponentLibrary(String namespace) {
            this.namespace = namespace;
            this.components = new HashMap<>(1);
        }

        public void addComponent(LibraryComponent component) {
            components.put(component.getName(), component);
        }

        @Override
        public String getDefaultNamespace() {
            return namespace;
        }

        @Override
        public LibraryType getType() {
            return LibraryType.COMPONENT;
        }

        @Override
        public Collection<? extends LibraryComponent> getComponents() {
            return components.values();
        }

        @Override
        public LibraryComponent getComponent(String componentName) {
            return components.get(componentName);
        }

        @Override
        public String getNamespace() {
            return namespace;
        }

        @Override
        public String getDefaultPrefix() {
            return LibraryUtils.generateDefaultPrefix(getNamespace());
        }

        @Override
        public String getDisplayName() {
            return FACES_COMPONENT;
        }

        @Override
        public String getLegacyNamespace() {
            return null;
        }
    }

    public static final class FacesLibraryComponent implements LibraryComponent {

        private final ElementHandle handle;
        private final String name;
        private final Library library;
        private final Tag tag;

        public FacesLibraryComponent(Library library, ComponentImpl component) {
            this.handle = component.getTypeElementHandle();
            this.name = component.getTagName();
            this.library = library;
            this.tag = new FacesComponentTag(component.getTagName());
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Tag getTag() {
            return tag;
        }

        @Override
        public Library getLibrary() {
            return library;
        }

        @Override
        public String[][] getDescription() {
            return new String[0][0];
        }

        /**
         * Gets the FQN of the class.
         * @return file
         */
        public FileObject getComponentFile(Project project) {
            WebModule webModule = WebModule.getWebModule(project.getProjectDirectory());
            if (webModule == null) {
                return null;
            }
            
            JavaSource js = JSFConfigUtilities.createJavaSource(webModule);
            if (js != null) {
                FileObject file = SourceUtils.getFile(handle, js.getClasspathInfo());
                if (file != null) {
                    return file;
                } else {
                    String qualifiedName = handle.getQualifiedName();
                    String relPath = qualifiedName.replace('.', '/') + ".class"; //NOI18N
                    ClassPath classPath = js.getClasspathInfo().getClassPath(ClasspathInfo.PathKind.COMPILE);
                    for (ClassPath.Entry entry : classPath.entries()) {
                        FileObject[] roots;
                        if (entry.isValid()) {
                            roots = new FileObject[]{entry.getRoot()};
                        } else {
                            SourceForBinaryQuery.Result res = SourceForBinaryQuery.findSourceRoots(entry.getURL());
                            roots = res.getRoots();
                        }
                        for (FileObject root : roots) {
                            file = root.getFileObject(relPath);
                            if (file != null) {
                                return file;
                            }
                        }
                    }
                }
            }
            return null;
        }

    }

    private static final class FacesComponentTag implements Tag {

        private final String tagName;

        public FacesComponentTag(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public String getName() {
            return tagName;
        }

        @Override
        public String getDescription() {
            return ""; //NOI18N
        }

        @Override
        public boolean hasNonGenenericAttributes() {
            return false;
        }

        @Override
        public Collection<Attribute> getAttributes() {
            return Collections.<Attribute>emptyList();
        }

        @Override
        public Attribute getAttribute(String name) {
            return null;
        }

    }
}

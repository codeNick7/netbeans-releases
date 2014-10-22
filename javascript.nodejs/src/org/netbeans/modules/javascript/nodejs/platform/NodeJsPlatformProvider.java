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
package org.netbeans.modules.javascript.nodejs.platform;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.modules.javascript.nodejs.file.PackageJson;
import org.netbeans.modules.javascript.nodejs.preferences.NodeJsPreferences;
import org.netbeans.modules.javascript.nodejs.ui.Notifications;
import org.netbeans.modules.javascript.nodejs.ui.customizer.NodeJsRunPanel;
import org.netbeans.modules.javascript.nodejs.util.NodeJsUtils;
import org.netbeans.modules.javascript.nodejs.util.StringUtils;
import org.netbeans.modules.web.clientproject.api.BadgeIcon;
import org.netbeans.modules.web.clientproject.api.platform.PlatformProviders;
import org.netbeans.modules.web.clientproject.spi.CustomizerPanelImplementation;
import org.netbeans.modules.web.clientproject.spi.platform.PlatformProviderImplementation;
import org.netbeans.modules.web.clientproject.spi.platform.PlatformProviderImplementationListener;
import org.netbeans.spi.project.ActionProvider;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;


@ServiceProvider(service = PlatformProviderImplementation.class, path = PlatformProviders.PLATFORM_PATH, position = 100)
public final class NodeJsPlatformProvider implements PlatformProviderImplementation, PropertyChangeListener {

    private static final Logger LOGGER = Logger.getLogger(NodeJsPlatformProvider.class.getName());

    public static final String IDENT = "node.js"; // NOI18N

    @StaticResource
    private static final String ICON_PATH = "org/netbeans/modules/javascript/nodejs/ui/resources/nodejs-badge.png"; // NOI18N

    private final BadgeIcon badgeIcon;
    private final PlatformProviderImplementationListener.Support listenerSupport = new PlatformProviderImplementationListener.Support();


    public NodeJsPlatformProvider() {
        badgeIcon = new BadgeIcon(
                ImageUtilities.loadImage(ICON_PATH),
                NodeJsPlatformProvider.class.getResource("/" + ICON_PATH)); // NOI18N
    }

    @Override
    public String getIdentifier() {
        return IDENT;
    }

    @NbBundle.Messages("NodeJsPlatformProvider.name=Node.js")
    @Override
    public String getDisplayName() {
        return Bundle.NodeJsPlatformProvider_name();
    }

    @Override
    public BadgeIcon getBadgeIcon() {
        return badgeIcon;
    }

    @Override
    public boolean isEnabled(Project project) {
        assert project != null;
        return NodeJsSupport.forProject(project).getPreferences().isEnabled();
    }

    @Override
    public List<URL> getSourceRoots(Project project) {
        assert project != null;
        assert isEnabled(project) : "Node.je support must be enabled in this project: " + project.getProjectDirectory().getNameExt();
        return NodeJsSupport.forProject(project).getSourceRoots();
    }

    @Override
    public ActionProvider getActionProvider(Project project) {
        assert project != null;
        return NodeJsSupport.forProject(project).getActionProvider();
    }

    @Override
    public List<CustomizerPanelImplementation> getRunCustomizerPanels(Project project) {
        return Collections.<CustomizerPanelImplementation>singletonList(new NodeJsRunPanel(project));
    }

    @Override
    public void projectOpened(Project project) {
        assert project != null;
        NodeJsSupport nodeJsSupport = NodeJsSupport.forProject(project);
        nodeJsSupport.addPropertyChangeListener(this);
        nodeJsSupport.projectOpened();
        detectNodeJs(project);
    }

    @Override
    public void projectClosed(Project project) {
        assert project != null;
        NodeJsSupport nodeJsSupport = NodeJsSupport.forProject(project);
        nodeJsSupport.projectClosed();
        nodeJsSupport.removePropertyChangeListener(this);
    }

    @Override
    public void notifyPropertyChanged(Project project, PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        if (PROP_ENABLED.equals(propertyName)) {
            NodeJsSupport.forProject(project).getPreferences().setEnabled((boolean) event.getNewValue());
        } else if (PROP_PROJECT_NAME.equals(propertyName)) {
            projectNameChanged(project, (String) event.getNewValue());
        } else if (PROP_RUN_CONFIGURATION.equals(propertyName)) {
            runConfigurationChanged(project, event.getNewValue());
        }
    }

    @Override
    public void addPlatformProviderImplementationListener(PlatformProviderImplementationListener listener) {
        listenerSupport.addPlatformProviderImplementationsListener(listener);
    }

    @Override
    public void removePlatformProviderImplementationListener(PlatformProviderImplementationListener listener) {
        listenerSupport.removePlatformProviderImplementationsListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        listenerSupport.firePropertyChanged((Project) evt.getSource(), this,
                new PropertyChangeEvent(this, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()));
    }

    private void detectNodeJs(Project project) {
        NodeJsSupport nodeJsSupport = NodeJsSupport.forProject(project);
        NodeJsPreferences preferences = nodeJsSupport.getPreferences();
        if (preferences.isEnabled()) {
            // already enabled => noop
            return;
        }
        PackageJson packageJson = nodeJsSupport.getPackageJson();
        if (!packageJson.exists()) {
            return;
        }
        Map<String, Object> content = packageJson.getContent();
        if (content == null) {
            // some error
            return;
        }
        Object engines = content.get(PackageJson.ENGINES);
        if (engines instanceof Map) {
            if (((Map<String, Object>) engines).containsKey(PackageJson.NODE)) {
                Notifications.notifyNodeJsDetected(project);
            }
        }
    }

    @NbBundle.Messages({
        "NodeJsPlatformProvider.sync.ask=Sync project name change to package.json?",
        "NodeJsPlatformProvider.sync.done=Project name change synced to package.json.",
    })
    private void projectNameChanged(Project project, String newName) {
        String projectName = project.getProjectDirectory().getNameExt();
        NodeJsSupport nodeJsSupport = NodeJsSupport.forProject(project);
        NodeJsPreferences preferences = nodeJsSupport.getPreferences();
        if (!preferences.isEnabled()) {
            LOGGER.log(Level.FINE, "Project name change ignored, node.js not enabled in project {0}", projectName);
            return;
        }
        if (!preferences.isSyncEnabled()) {
            LOGGER.log(Level.FINE, "Project name change ignored, sync not enabled in project {0}", projectName);
            return;
        }
        PackageJson packageJson = nodeJsSupport.getPackageJson();
        if (!packageJson.exists()) {
            LOGGER.log(Level.FINE, "Project name change ignored, package.json not exist in project {0}", projectName);
            return;
        }
        LOGGER.log(Level.FINE, "Processing project name change in project {0}", projectName);
        Map<String, Object> content = packageJson.getContent();
        if (content == null) {
            LOGGER.log(Level.FINE, "Project name change ignored, package.json has no content in project {0}", projectName);
            return;
        }
        if (!StringUtils.hasText(newName)) {
            LOGGER.log(Level.FINE, "Project name change ignored, new name is empty in project {0}", projectName);
            return;
        }
        String name = (String) content.get(PackageJson.NAME);
        if (Objects.equals(name, newName)) {
            LOGGER.log(Level.FINE, "Project name change ignored, new name same as current name in package.json in project {0}", projectName);
            return;
        }
        if (preferences.isAskSyncEnabled()) {
            if (!Notifications.askUser(NodeJsUtils.getProjectDisplayName(project), Bundle.NodeJsPlatformProvider_sync_ask())) {
                preferences.setSyncEnabled(false);
                LOGGER.log(Level.FINE, "Project name change ignored, cancelled by user in project {0}", projectName);
                return;
            }
        }
        content.put(PackageJson.NAME, newName);
        packageJson.setContent(content);
        Notifications.notifyUser(NodeJsUtils.getProjectDisplayName(project), Bundle.NodeJsPlatformProvider_sync_done());
    }

    private void runConfigurationChanged(Project project, Object activeRunConfig) {
        boolean runEnabled = false;
        for (CustomizerPanelImplementation panel : getRunCustomizerPanels(project)) {
            if (panel.getIdentifier().equals(activeRunConfig)) {
                runEnabled = true;
                break;
            }
        }
        NodeJsSupport.forProject(project).getPreferences().setRunEnabled(runEnabled);
    }

}

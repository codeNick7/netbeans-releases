/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
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

package org.netbeans.modules.cnd.makeproject.api;

import java.util.ResourceBundle;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.makeproject.api.configurations.MakeConfiguration;
import org.netbeans.modules.cnd.makeproject.api.runprofiles.RunProfile;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public final class ProjectActionEvent {

    public interface Type {
        int ordinal();
        String name();
        String getLocalizedName();
        void setLocalizedName(String name);
    }

    public static enum PredefinedType implements Type {
        BUILD("Build"), // NOI18N
        CLEAN("Clean"), // NOI18N
        RUN("Run"), // NOI18N
        DEBUG("Debug"), // NOI18N
        DEBUG_STEPINTO("Debug"), // NOI18N
        DEBUG_LOAD_ONLY("Debug"), // NOI18N
        CHECK_EXECUTABLE("CheckExecutable"), // NOI18N
        CUSTOM_ACTION("Custom"), // NOI18N
        TEST("Test"); // NOI18N

        private final String localizedName;

        private PredefinedType(String resourceNamePrefix) {
            localizedName = getString(resourceNamePrefix + "ActionName"); // NOI18N
        }

        @Override
        public String getLocalizedName() {
            return localizedName;
        }

        @Override
        public void setLocalizedName(String name) {
            // predefined events already have localized name
            throw new UnsupportedOperationException();
        }
    }

    private final Project project;
    private final Type type;
    private String executable;
    private final MakeConfiguration configuration;
    private final RunProfile profile;
    private final boolean wait;
    private final Lookup context;
    private boolean isFinalExecutable;

    public ProjectActionEvent(Project project, Type type, String executable, MakeConfiguration configuration, RunProfile profile, boolean wait) {
        this(project, type, executable, configuration, profile, wait, Lookup.EMPTY);
    }

    public ProjectActionEvent(Project project, Type type, String executable, MakeConfiguration configuration, RunProfile profile, boolean wait, Lookup context) {
        this.project = project;
        this.type = type;
	this.executable = executable;
	this.configuration = configuration;
	this.profile = profile;
	this.wait = wait;
        this.context = context;
    }
    
    public Project getProject() {
        return project;
    }

    public final Lookup getContext(){
        return context;
    }
    
    public Type getType() {
        return type;
    }

    // TODO: move method to ProjectActionHandlerFactory or ProjectActionHandler
    public String getActionName() {
        return type.getLocalizedName();
    }

    public String getExecutable() {
	return executable;
    }

    public MakeConfiguration getConfiguration() {
	return configuration;
    }

    public RunProfile getProfile() {
        if (profile != null) {
            return profile;
        } else {
            return configuration.getProfile();
        }
    }

    public boolean getWait() {
        return wait;
    }

    void setExecutable(String executable) {
        this.executable = executable;
    }

    void setFinalExecutable(){
        isFinalExecutable = true;
    }

    boolean isFinalExecutable(){
        return isFinalExecutable;
    }

    @Override
    public String toString() {
        return "PAE " + type + " " + getActionName() + " exec: " + getExecutable(); // NOI18N
    }

     /** Look up i18n strings here */
    private static ResourceBundle bundle;
    private static String getString(String s) {
        if (bundle == null) {
            bundle = NbBundle.getBundle(ProjectActionEvent.class);
        }
        return bundle.getString(s);
    }
}

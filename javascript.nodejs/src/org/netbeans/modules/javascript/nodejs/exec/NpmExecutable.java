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
package org.netbeans.modules.javascript.nodejs.exec;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.annotations.common.NullAllowed;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.api.project.Project;
import org.netbeans.modules.javascript.nodejs.file.PackageJson;
import org.netbeans.modules.javascript.nodejs.options.NodeJsOptions;
import org.netbeans.modules.javascript.nodejs.options.NodeJsOptionsValidator;
import org.netbeans.modules.javascript.nodejs.ui.options.NodeJsOptionsPanelController;
import org.netbeans.modules.javascript.nodejs.util.FileUtils;
import org.netbeans.modules.javascript.nodejs.util.NodeJsUtils;
import org.netbeans.modules.javascript.nodejs.util.StringUtils;
import org.netbeans.modules.javascript.nodejs.util.ValidationResult;
import org.netbeans.modules.web.common.api.ExternalExecutable;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;


public class NpmExecutable {

    private static final Logger LOGGER = Logger.getLogger(NpmExecutable.class.getName());

    public static final String NPM_NAME = "npm"; // NOI18N

    public static final String SAVE_PARAM = "--save"; // NOI18N
    public static final String SAVE_DEV_PARAM = "--save-dev"; // NOI18N
    public static final String SAVE_OPTIONAL_PARAM = "--save-optional"; // NOI18N

    private static final String INSTALL_PARAM = "install"; // NOI18N

    protected final Project project;
    protected final String npmPath;


    NpmExecutable(String npmPath, @NullAllowed Project project) {
        assert npmPath != null;
        this.npmPath = npmPath;
        this.project = project;
    }

    @CheckForNull
    public static NpmExecutable getDefault(@NullAllowed Project project, boolean showOptions) {
        ValidationResult result = new NodeJsOptionsValidator()
                .validateNpm()
                .getResult();
        if (validateResult(result) != null) {
            if (showOptions) {
                OptionsDisplayer.getDefault().open(NodeJsOptionsPanelController.OPTIONS_PATH);
            }
            return null;
        }
        return createExecutable(NodeJsOptions.getInstance().getNpm(), project);
    }

    private static NpmExecutable createExecutable(String npm, Project project) {
        if (Utilities.isMac()) {
            return new MacNpmExecutable(npm, project);
        }
        return new NpmExecutable(npm, project);
    }

    String getCommand() {
        return npmPath;
    }

    @NbBundle.Messages({
        "# {0} - project name",
        "NpmExecutable.install=npm ({0})",
    })
    @CheckForNull
    public Future<Integer> install(String... args) {
        assert !EventQueue.isDispatchThread();
        assert project != null;
        String projectName = NodeJsUtils.getProjectDisplayName(project);
        Future<Integer> task = getExecutable(Bundle.NpmExecutable_install(projectName))
                .additionalParameters(getInstallParams(args))
                .run(getDescriptor());
        assert task != null : npmPath;
        return task;
    }

    private ExternalExecutable getExecutable(String title) {
        assert title != null;
        return new ExternalExecutable(getCommand())
                .workDir(getWorkDir())
                .displayName(title)
                .noOutput(false);
    }

    private ExecutionDescriptor getDescriptor() {
        assert project != null;
        return new ExecutionDescriptor()
                .frontWindow(true)
                .frontWindowOnError(false)
                .controllable(true)
                .optionsPath(NodeJsOptionsPanelController.OPTIONS_PATH)
                .outLineBased(true)
                .errLineBased(true);
    }

    private File getWorkDir() {
        if (project == null) {
            return FileUtils.TMP_DIR;
        }
        PackageJson packageJson = new PackageJson(project.getProjectDirectory());
        if (packageJson.exists()) {
            return new File(packageJson.getPath()).getParentFile();
        }
        File sourceRoot = NodeJsUtils.getSourceRoot(project);
        if (sourceRoot != null) {
            return sourceRoot;
        }
        File workDir = FileUtil.toFile(project.getProjectDirectory());
        assert workDir != null : project.getProjectDirectory();
        return workDir;
    }

    private List<String> getInstallParams(String... args) {
        List<String> params = new ArrayList<>(args.length + 1);
        params.add(INSTALL_PARAM);
        params.addAll(getArgsParams(args));
        return getParams(params);
    }

    private List<String> getArgsParams(String[] args) {
        return Arrays.asList(args);
    }

    List<String> getParams(List<String> params) {
        assert params != null;
        return params;
    }

    @CheckForNull
    private static String validateResult(ValidationResult result) {
        if (result.isFaultless()) {
            return null;
        }
        if (result.hasErrors()) {
            return result.getErrors().get(0).getMessage();
        }
        return result.getWarnings().get(0).getMessage();
    }

    //~ Inner classes

    private static final class MacNpmExecutable extends NpmExecutable {

        private static final String BASH_COMMAND = "/bin/bash -lc"; // NOI18N


        MacNpmExecutable(String npmPath, Project project) {
            super(npmPath, project);
        }

        @Override
        String getCommand() {
            return BASH_COMMAND;
        }

        @Override
        List<String> getParams(List<String> params) {
            StringBuilder sb = new StringBuilder(200);
            sb.append("\""); // NOI18N
            sb.append(npmPath);
            sb.append("\" \""); // NOI18N
            sb.append(StringUtils.implode(super.getParams(params), "\" \"")); // NOI18N
            sb.append("\""); // NOI18N
            return Collections.singletonList(sb.toString());
        }

    }

}

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
package org.netbeans.modules.web.clientproject.createprojectapi;

import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.annotations.common.NullAllowed;
import org.openide.filesystems.FileObject;
import org.openide.util.Parameters;

/**
 * Project properties for creating new project in the given project directory with the given project name.
 * <p>
 * It contains usually relative paths of:
 * <ul>
 * <li>Source folder</li>
 * <li>Site Root folder</li>
 * <li>Unit Tests folder</li>
 * <li>Config folder</li>
 * </ul>
 * <p>
 * This class is not thread-safe.
 *
 * @since 1.37
 * @author Martin Janicek
 */
public final class CreateProjectProperties {

    private final FileObject projectDir;
    private final String projectName;

    private String sourceFolder;
    private String siteRootFolder;
    private String testFolder;
    private String testSeleniumFolder;
    private String jsTestingProvider;
    private String platformProvider;
    private boolean autoconfigured = false;
    private String startFile;
    private String projectUrl;


    /**
     * Creates new project properties.
     * @param projectDir project directory
     * @param projectName project name
     * @since 1.78
     */
    public CreateProjectProperties(FileObject projectDir, String projectName) {
        Parameters.notNull("projectDir", projectDir); // NOI18N
        Parameters.notEmpty("projectName", projectName); // NOI18N
        this.projectDir = projectDir;
        this.projectName = projectName;
    }

    /**
     * Get project directory.
     * @return project directory
     */
    @NonNull
    public FileObject getProjectDir() {
        return projectDir;
    }

    /**
     * Get project name.
     * @return project name
     */
    @NonNull
    public String getProjectName() {
        return projectName;
    }

    /**
     * Get Source folder, usually relative path.
     *
     * @return Source folder, usually relative path; can be {@code null} if no Source folder is present
     * @since 1.61
     */
    @CheckForNull
    public String getSourceFolder() {
        return sourceFolder;
    }

    /**
     * Set Source folder, usually relative path or empty string.
     *
     * @param sourceFolder Source folder, can be {@code null} if no Source folder is present
     * @return itself
     * @since 1.61
     */
    @NonNull
    public CreateProjectProperties setSourceFolder(@NullAllowed String sourceFolder) {
        this.sourceFolder = sourceFolder;
        return this;
    }

    /**
     * Get Site Root folder, usually relative path.
     *
     * @return Site Root folder, usually relative path; can be {@code null} if no Site Root is present
     */
    @CheckForNull
    public String getSiteRootFolder() {
        return siteRootFolder;
    }

    /**
     * Set Site Root folder, usually relative path or empty string.
     *
     * @param siteRootFolder Site Root folder, can be {@code null} if no Site Root is present
     * @return itself
     */
    @NonNull
    public CreateProjectProperties setSiteRootFolder(@NullAllowed String siteRootFolder) {
        this.siteRootFolder = siteRootFolder;
        return this;
    }

    /**
     * Get Test folder, usually relative path.
     *
     * @return Test folder, usually relative path; can be {@code null} if no Test folder is present
     */
    @CheckForNull
    public String getTestFolder() {
        return testFolder;
    }

    /**
     * Set Test folder, can be {@code null} if there are no tests available.
     *
     * @param testFolder Test folder, can be {@code null} if there is no test folder available
     * @return itself
     */
    @NonNull
    public CreateProjectProperties setTestFolder(@NullAllowed String testFolder) {
        this.testFolder = testFolder;
        return this;
    }

    /**
     * Get Test folder for Selenium tests, usually relative path.
     *
     * @return Test folder for Selenium tests, usually relative path; can be {@code null} if no Test folder is present
     * @since 1.80
     */
    @CheckForNull
    public String getTestSeleniumFolder() {
        return testSeleniumFolder;
    }

    /**
     * Set Test folder for Selenium tests, can be {@code null} if there are no tests available.
     *
     * @param testSeleniumFolder Test folder for Selenium tests, can be {@code null} if there is no test folder available
     * @return itself
     * @since 1.80
     */
    @NonNull
    public CreateProjectProperties setTestSeleniumFolder(@NullAllowed String testSeleniumFolder) {
        this.testSeleniumFolder = testSeleniumFolder;
        return this;
    }

    /**
     * Get JS testing provider (its identifier).
     * @return JS testing provider (its identifier); can be {@code null} if there is no default JS testing provider
     * @since 1.50
     */
    @CheckForNull
    public String getJsTestingProvider() {
        return jsTestingProvider;
    }

    /**
     * Set JS testing provider (its identifier), can be {@code null} if there is no default JS testing provider.
     * @param jsTestingProvider JS testing provider (its identifier), can be {@code null} if there is no default JS testing provider
     * @return itself
     * @since 1.50
     */
    public CreateProjectProperties setJsTestingProvider(@NullAllowed String jsTestingProvider) {
        this.jsTestingProvider = jsTestingProvider;
        return this;
    }

    /**
     * Get platform provider (its identifier).
     * @return platform provider (its identifier); can be {@code null} if there is no default platform provider
     * @since 1.62
     */
    @CheckForNull
    public String getPlatformProvider() {
        return platformProvider;
    }

    /**
     * Set platform provider (its identifier), can be {@code null} if there is no default platform provider.
     * @param platformProvider platform provider (its identifier), can be {@code null} if there is no default platform provider
     * @return itself
     * @since 1.62
     */
    public CreateProjectProperties setPlatformProvider(@NullAllowed String platformProvider) {
        this.platformProvider = platformProvider;
        return this;
    }

    /**
     * Return {@code true} if this project was automatically configured.
     * <p>
     * Such project shows notification on its first open.
     * @return {@code true} if this project was automatically configured
     * @since 1.72
     */
    public boolean isAutoconfigured() {
        return autoconfigured;
    }

    /**
     * Set {@code true} if the project is automatically configured.
     * <p>
     * Such project shows notification on its first open.
     * @param autoconfigured {@code true} if the project is automatically configured
     * @return itself
     * @since 1.72
     */
    public CreateProjectProperties setAutoconfigured(boolean autoconfigured) {
        this.autoconfigured = autoconfigured;
        return this;
    }

    /**
     * Get start file, usually relative path to {@link #getSiteRootFolder() site root}.
     * @return start file, usually relative path to {@link #getSiteRootFolder() site root};
     *         can be {@code null} if there is no start file
     * @since 1.78
     */
    @CheckForNull
    public String getStartFile() {
        return startFile;
    }

    /**
     * Set start file (usually relative path to {@link #getSiteRootFolder() site root}),
     * can be {@code null} if there is no start file.
     * @param startFile start file (usually relative path to {@link #getSiteRootFolder() site root}), can be {@code null}
     * @return itself
     * @since 1.78
     */
    public CreateProjectProperties setStartFile(@NullAllowed String startFile) {
        this.startFile = startFile;
        return this;
    }

    /**
     * Get project URL.
     * @return project URL; can be {@code null} if there is no project URL
     * @since 1.78
     */
    @CheckForNull
    public String getProjectUrl() {
        return projectUrl;
    }

    /**
     * Set project URL, can be {@code null} if there is no project URL.
     * @param projectUrl project URL, can be {@code null} if there is no project URL
     * @return itself
     * @since 1.78
     */
    public CreateProjectProperties setProjectUrl(@NullAllowed String projectUrl) {
        this.projectUrl = projectUrl;
        return this;
    }

}

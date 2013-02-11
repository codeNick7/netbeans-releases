/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.modules.php.project.ui.actions.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.php.api.phpmodule.PhpModule;
import org.netbeans.modules.php.api.util.FileUtils;
import org.netbeans.modules.php.project.PhpProject;
import org.netbeans.modules.php.project.PhpProjectValidator;
import org.netbeans.modules.php.project.ProjectPropertiesSupport;
import org.netbeans.modules.php.project.ui.actions.support.CommandUtils;
import org.netbeans.modules.php.project.ui.Utils;
import org.netbeans.modules.php.project.util.PhpProjectUtils;
import org.netbeans.modules.php.spi.testing.locate.Locations;
import org.netbeans.modules.php.spi.testing.PhpTestingProvider;
import org.netbeans.spi.gototest.TestLocator;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

/**
 * Support for PHP Unit.
 * @author Tomas Mysik
 */
@org.openide.util.lookup.ServiceProvider(service=org.netbeans.spi.gototest.TestLocator.class)
public class GoToTest implements TestLocator {
    private static final Logger LOGGER = Logger.getLogger(GoToTest.class.getName());
    private static final RequestProcessor RP = new RequestProcessor(GoToTest.class.getName(), 2);

    public GoToTest() {
    }

    @Override
    public boolean appliesTo(FileObject fo) {
        return FileUtils.isPhpFile(fo);
    }

    @Override
    public boolean asynchronous() {
        return true;
    }

    @Override
    public void findOpposite(final FileObject fo, final int caretOffset, final LocationListener callback) {
        RP.post(new Runnable() {
            @Override
            public void run() {
                callback.foundLocation(fo, findOpposite0(fo));
            }
        });
    }

    @Override
    public LocationResult findOpposite(FileObject fo, int caretOffset) {
        throw new UnsupportedOperationException("Go To Test is asynchronous");
    }

    private LocationResult findOpposite0(FileObject fo) {
        PhpProject project = PhpProjectUtils.getPhpProject(fo);
        if (project == null) {
            // XXX what to do now??
            LOGGER.log(Level.INFO, "PHP project was not found for file {0}", fo);
            return null;
        }
        if (PhpProjectValidator.isFatallyBroken(project)) {
            Utils.warnInvalidSourcesDirectory(project);
            return null;
        }

        if (CommandUtils.isUnderTests(project, fo, false)) {
            return findSource(project, fo);
        } else if (CommandUtils.isUnderSources(project, fo)) {
            return findTest(project, fo);
        }
        return null;
    }

    @Override
    public FileType getFileType(FileObject fo) {
        PhpProject project = PhpProjectUtils.getPhpProject(fo);
        if (project == null || PhpProjectValidator.isFatallyBroken(project)) {
            LOGGER.log(Level.INFO, "PHP project was not found for file {0}", fo);
            return FileType.NEITHER;
        }

        if (CommandUtils.isUnderTests(project, fo, false)) {
            PhpModule phpModule = project.getPhpModule();
            for (PhpTestingProvider testingProvider : project.getTestingProviders()) {
                if (testingProvider.isTestFile(phpModule, fo)) {
                    return FileType.TEST;
                }
            }
        } else if (CommandUtils.isUnderSources(project, fo)) {
            return FileType.TESTED;
        }
        return FileType.NEITHER;
    }

    private LocationResult findSource(PhpProject project, FileObject testFo) {
        return findFile(project, testFo, false);
    }

    public static LocationResult findTest(PhpProject project, FileObject srcFo) {
        return findFile(project, srcFo, true);
    }

    private static LocationResult findFile(PhpProject project, FileObject file, boolean searchTest) {
        final FileObject sourceRoot = searchTest ? getTests(project) : getSources(project);
        if (sourceRoot == null) {
            return null;
        }
        Set<Locations.Offset> phpFiles = Collections.emptySet();
        PhpModule phpModule = project.getPhpModule();
        for (PhpTestingProvider testingProvider : project.getTestingProviders()) {
            org.netbeans.modules.php.spi.testing.locate.TestLocator testLocator = testingProvider.getTestLocator(phpModule);
            if (searchTest) {
                phpFiles = testLocator.findTests(file);
            } else {
                phpFiles = testLocator.findSources(file);
            }
        }
        if (phpFiles.isEmpty()) {
            return new LocationResult(NbBundle.getMessage(GoToTest.class, searchTest ? "MSG_TestNotFound" : "MSG_SrcNotFound", file.getNameExt()));
        }
        if (phpFiles.size() == 1) {
            Locations.Offset source = phpFiles.iterator().next();
            return new LocationResult(source.getFile(), source.getOffset());
        }
        List<FileObject> files = new ArrayList<FileObject>(phpFiles.size());
        for (Locations.Offset location : phpFiles) {
            files.add(location.getFile());
        }
        FileObject selected = SelectFilePanel.open(sourceRoot, files);
        if (selected != null) {
            int offset = -1;
            for (Locations.Offset location : phpFiles) {
                if (selected.equals(location.getFile())) {
                    offset = location.getOffset();
                    break;
                }
            }
            return new LocationResult(selected, offset);
        }
        return null;
    }

    private static FileObject getSources(PhpProject project) {
        FileObject sources = ProjectPropertiesSupport.getSourcesDirectory(project);
        assert sources != null : "Project sources must be found for " + project;
        return sources;
    }

    private static FileObject getTests(PhpProject project) {
        return ProjectPropertiesSupport.getTestDirectory(project, false);
    }
}

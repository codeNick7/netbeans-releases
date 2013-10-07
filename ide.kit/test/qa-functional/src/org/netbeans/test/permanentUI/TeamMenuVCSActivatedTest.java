/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.test.permanentUI;

import junit.framework.Test;
import org.netbeans.jellytools.ProjectsTabOperator;
import org.netbeans.jellytools.WizardOperator;
import org.netbeans.jellytools.nodes.ProjectRootNode;
import org.netbeans.test.permanentUI.utils.ProjectContext;
import org.netbeans.test.permanentUI.utils.Utilities;

/**
 *
 * @author Lukas Hasik, Jan Peska, Marian.Mirilovic@oracle.com
 */
public class TeamMenuVCSActivatedTest extends MainMenuTest {

    /**
     * Need to be defined because of JUnit
     *
     * @param name
     */
    public TeamMenuVCSActivatedTest(String name) {
        super(name);
    }

    public static Test suite() {
        return TeamMenuVCSActivatedTest.emptyConfiguration().
                addTest(TeamMenuVCSActivatedTest.class, "testTeamMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_DiffSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_IgnoreSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_PatchesSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_BranchTagSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_QueuesSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_RemoteSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_RecoverSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_OtherVCSSubMenu").
                addTest(TeamMenuVCSActivatedTest.class, "testTeam_HistorySubMenu").
                clusters(".*").enableModules(".*").
                suite();
    }

    @Override
    public void initialize() {
        openVersioningJavaEEProject("SampleProject");
        this.context = ProjectContext.VERSIONING_ACTIVATED;
    }

    @Override
    protected void tearDown() throws Exception {}

    @Override
    public void testTeamMenu() {
        oneMenuTest("Team");
    }

    public void testTeam_DiffSubMenu() {
        oneSubMenuTest("Team|Diff", false);
    }

    public void testTeam_IgnoreSubMenu() {
        oneSubMenuTest("Team|Ignore", false);
    }

    public void testTeam_PatchesSubMenu() {
        oneSubMenuTest("Team|Patches", false);
    }

    public void testTeam_BranchTagSubMenu() {
        oneSubMenuTest("Team|Branch/Tag", false);
    }

    public void testTeam_QueuesSubMenu() {
        oneSubMenuTest("Team|Queues", false);
    }

    public void testTeam_RemoteSubMenu() {
        Utilities.projectName = "core-main";
        oneSubMenuTest("Team|Remote", false);
    }

    public void testTeam_RecoverSubMenu() {
        Utilities.projectName = "core-main";
        oneSubMenuTest("Team|Recover", false);
    }

    public void testTeam_OtherVCSSubMenu() {
        oneSubMenuTest("Team|Other VCS", false);
    }

    @Override
    public void testTeam_HistorySubMenu() {
        oneSubMenuTest("Team|History", true);
    }

    /**
     * Create new Java EE project in default path.
     *
     * @param projectName Name of project.
     * @return name of currently created project.
     */
    public boolean openVersioningJavaEEProject(String projectName) {
        ProjectRootNode projectNode = openProject(projectName);
        waitScanFinished();
        projectNode.select();
        if (!isVersioningProject(projectName)) {
            versioningActivation(projectName);
        }
        return true;
    }

    /**
     * Create repository (Mercurial) for given project.
     *
     * @param projectName
     */
    public void versioningActivation(String projectName) {
        ProjectRootNode projectNode = new ProjectsTabOperator().getProjectRootNode(projectName);
        projectNode.callPopup().showMenuItem("Versioning|Initialize Mercurial Repository...").push();
        WizardOperator wo2 = new WizardOperator("Initialize a Mercurial Repository");
        wo2.ok();
    }

    /**
     * Check if project has local repository activated.
     *
     * @param projectName
     * @return true - VCS activated, else false
     */
    public boolean isVersioningProject(String projectName) {
        int menuSize = getMainMenuItem("Team").getSubmenu().size();
        return menuSize > 8;
    }

}

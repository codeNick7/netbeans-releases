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
package org.netbeans.jellytools.actions;

import java.io.IOException;
import junit.framework.Test;
import junit.textui.TestRunner;
import org.netbeans.jellytools.FindInFilesOperator;
import org.netbeans.jellytools.JavaProjectsTabOperator;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jellytools.nodes.SourcePackagesNode;

/** Test org.netbeans.jellytools.actions.FindAction
 *
 * @author Adam Sotona
 * @author Jiri Skrivanek
 */
public class FindActionTest extends JellyTestCase {

    private static final String[] tests = new String[]{
        "testPerformPopup",
        "testPerformMenu",
        "testPerformAPI",
        "testPerformShortcut"
    };

    /** constructor required by JUnit
     * @param testName method name to be used as testcase
     */
    public FindActionTest(String testName) {
        super(testName);
    }

    /** method used for explicit testsuite definition
     */
    public static Test suite() {
        return createModuleTest(FindActionTest.class, tests);
    }

    @Override
    protected void setUp() throws IOException {
        openDataProjects("SampleProject");
    }

    /** Test performPopup */
    public void testPerformPopup() {
        Node node = new JavaProjectsTabOperator().getProjectRootNode("SampleProject"); // NOI18N
        new FindAction().performPopup(node);
        new FindInFilesOperator().close();
    }

    /** Test performMenu */
    public void testPerformMenu() {
        Node node = new Node(new SourcePackagesNode("SampleProject"), "sample1"); // NOI18N
        new FindAction().performMenu(node);
        new FindInFilesOperator().close();
    }

    /** Test performAPI */
    public void testPerformAPI() {
        new FindAction().performAPI();
        new FindInFilesOperator().close();
    }

    /** Test performShortcut */
    public void testPerformShortcut() {
        new FindAction().performShortcut();
        new FindInFilesOperator().close();
        // On some linux it may happen autorepeat is activated and it 
        // opens dialog multiple times. So, we need to close all modal dialogs.
        // See issue http://www.netbeans.org/issues/show_bug.cgi?id=56672.
        closeAllModal();
    }
}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
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

package org.netbeans.jellytools.nodes;

import java.io.IOException;
import junit.framework.Test;
import junit.textui.TestRunner;
import org.netbeans.jellytools.FindInFilesOperator;
import org.netbeans.jellytools.CNDProjectsTabOperator;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.MainWindowOperator;
import org.netbeans.jellytools.NbDialogOperator;

/**
 *  Test of CNDProjectRootNode. Mostly copied/moved from ProjectRootNodeTest,
 *  but did not extend ProjectRootNodeTest, because a test library dependency on 
 *  jellytools module would be neccessary.
 *
 * @author Vojtech.Sigler@sun.com
 */
public class CNDProjectRootNodeTest extends JellyTestCase
{

    /** constructor required by JUnit
     * @param testName method name to be used as testcase
     */
    public CNDProjectRootNodeTest(String testName) {
        super(testName);
    }
    
    /** method used for explicit testsuite definition */
    public static Test suite() {        
        return createModuleTest(CNDProjectRootNodeTest.class,
                "testVerifyPopup", "testFind",
                "testBuildProject", "testCleanProject",
                "testProperties");
    }
    
    /** Use for internal test execution inside IDE
     * @param args command line arguments
     */
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    private static CNDProjectRootNode projectRootNode;
    
    /** Find node. */
    protected void setUp() throws IOException {
        System.out.println("### "+getName()+" ###");
        openDataProjects("SampleCNDProject");
        if(projectRootNode == null) {
            projectRootNode = CNDProjectsTabOperator.invoke().getCNDProjectRootNode("SampleCNDProject"); // NOI18N
        }
    }
    
    /** Test verifyPopup */
    public void testVerifyPopup() {
        projectRootNode.verifyPopup();
    }
    
    /** Test find */
    public void testFind() {
        projectRootNode.find();
        new FindInFilesOperator().close();
    }
    
    /** Test buildProject */
    public void testBuildProject() {
        MainWindowOperator.StatusTextTracer statusTextTracer = MainWindowOperator.getDefault().getStatusTextTracer();
        statusTextTracer.start();
        projectRootNode.buildProject();
        // wait status text "SampleCNDProject (Build)"
        statusTextTracer.waitText("Build", false); // NOI18N
        // wait status text "Build successful.
        statusTextTracer.waitText("successful", false); // NOI18N
        statusTextTracer.stop();
    }
    
    /** Test cleanProject*/
    public void testCleanProject() {
        MainWindowOperator.StatusTextTracer statusTextTracer = MainWindowOperator.getDefault().getStatusTextTracer();
        statusTextTracer.start();
        projectRootNode.cleanProject();
        // wait status text "SampleCNDProject (Clean)"
        statusTextTracer.waitText("Clean", false); // NOI18N
        // wait status text "Clean successful."
        statusTextTracer.waitText("successful", false); // NOI18N
        statusTextTracer.stop();
    }
    
    /** Test properties */
    public void testProperties() {
        projectRootNode.properties();
        new NbDialogOperator("SampleCNDProject").close(); //NOI18N
    }
}

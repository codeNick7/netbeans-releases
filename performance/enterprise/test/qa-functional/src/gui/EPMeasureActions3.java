/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package gui;


import org.netbeans.junit.NbTestSuite;
import gui.actions.*;

/**
 * Measure UI-RESPONSIVENES and WINDOW_OPENING.
 *
 * @author  mmirilovic@netbeans.org, rashid@netbeans.org, mrkam@netbeans.org
 */
public class EPMeasureActions3  {

    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
            
        suite.addTest(new WatchProjects("testInitGCProjects"));
        suite.addTest(new SchemaNavigatorSchemaView("measureTime", "Schema Navigator Schema View"));
        suite.addTest(new NavigatorSchemaViewMode("measureTime","Schema Navigator Schema View mode"));
        

//TODO there is an password dialog solve before enable to run again        suite.addTest(new DeployProject("measureTime","Deploy Project"));
        suite.addTest(new OpenComplexDiagram("measureTime","Open Complex Diagram"));         
//        suite.addTest(new OpenComplexDiagram("testGC","Open Complex Diagram - Test GC"));         
        suite.addTest(new OpenBPELproject("measureTime","OpenBPELproject"));

        suite.addTest(new StartAppserver("measureTime","Start Appserver"));
        suite.addTest(new OpenSchemaView("testOpenComplexSchemaView", "Open Complex Schema View"));
        suite.addTest(new WatchProjects("testGCProjects"));
        return suite;
    }
    
}

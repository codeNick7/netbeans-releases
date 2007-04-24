/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 * 
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package gui;

import gui.window.MobilityDeploymentManagerDialog;
import gui.window.ProjectPropertiesDialog;
import gui.window.QuickRunDialog;
import gui.window.SecurityManagerDialog;
import org.netbeans.junit.NbTestSuite;

/**
 * Measure UI-RESPONSIVENES and WINDOW_OPENING.
 *
 * @author  mmirilovic@netbeans.org
 */
public class MPMeasureDialogs  {

    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
            
        // TODO add some test cases
        suite.addTest(new MobilityDeploymentManagerDialog("measureTime","Mobility Deployment Manager dialog time"));
        suite.addTest(new SecurityManagerDialog("measureTime","Security Manager dialog time"));
        suite.addTest(new QuickRunDialog("measureTime","Quick Run dialog time"));
        suite.addTest(new ProjectPropertiesDialog("measureTime","Mobility project properties dialog"));
        return suite;
    }
    
}

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

package org.netbeans.modules.projectimport.eclipse.core;

import org.netbeans.junit.NbTestCase;

/**
 * Tests equal and hashCode methods.
 *
 * @author mkrauskopf
 */
public class EqualityAndHashCodeTest extends NbTestCase {

    Link link2;
    Link theSameAsLink2;

    Workspace.Variable var2;
    Workspace.Variable theSameAsVar2;

    public EqualityAndHashCodeTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        link2 = new Link("link2", true, "/link2");
        
        theSameAsLink2 = new Link("link2", true, "/link2");
        
        var2 = new Workspace.Variable("/var2", "var2");
        
        theSameAsVar2 = new Workspace.Variable("/var2", "var2");
    }
    
    /** tests ClassPathContent.Link.equals() */
    public void testLinksEquality() {
        assertNotSame("link2 and theSameAsLink2 shouldn't be the same " +
                "(link2 == theSameAsLink2)", link2, theSameAsLink2);
        assertEquals("link2 should be equal to theSameAsLink2",
                link2, theSameAsLink2);
    }
    
    /** tests ClassPathContent.Link.hashCode() */
    public void testLinksHashCodes() {
        assertEquals("link2 and theSameAsLink2 should generate the same hashCode",
                link2.hashCode(), theSameAsLink2.hashCode());
    }
    /** tests ClassPathContent.Variable.equals() */
    public void testVariablesEquality() {
        assertNotSame("var2 and theSameAsVar2 shouldn't be the same " +
                "(var2 == theSameAsVar2)", var2, theSameAsVar2);
        assertEquals("var2 should be equal to theSameAsVar2",
                var2, theSameAsVar2);
    }
    
}

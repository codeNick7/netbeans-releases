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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.netbeans.modules.xml.text.syntax;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.xml.lexer.XMLTokenId;
import org.netbeans.editor.TokenItem;
import org.netbeans.modules.xml.text.AbstractTestCase;
import org.netbeans.modules.xml.text.api.dom.XMLSyntaxSupport;
import org.netbeans.modules.xml.text.api.dom.SyntaxElement;

/**
 *
 * @author Samaresh
 */
public class XMLSyntaxSupportTest extends AbstractTestCase {

    public XMLSyntaxSupportTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new XMLSyntaxSupportTest("testParse1"));
        suite.addTest(new XMLSyntaxSupportTest("testParse2"));
        suite.addTest(new XMLSyntaxSupportTest("testTokens"));
        return suite;
    }

    public void testParse1() throws Exception {
        XMLSyntaxSupport support = getSyntaxSupport("syntax/test.xml");
        SyntaxElement se = support.getElementChain(1);
        while( se != null) {
            System.out.println("Class: " + se.getClass().getSimpleName() + " Offset: " + se.getElementOffset() + " Length: "+ se.getElementLength());
            se = se.getNext();
        }
    }

    public void testParse2() throws Exception {
        XMLSyntaxSupport support = getSyntaxSupport("syntax/invalid.xml");
        SyntaxElement se = support.getElementChain(1);
        while( se != null) {
            System.out.println("Class: " + se.getClass().getSimpleName() + " Offset: " + se.getElementOffset() + " Length: "+ se.getElementLength());
            se = se.getNext();
        }
    }

    public void testTokens() throws Exception {
        XMLSyntaxSupport support = getSyntaxSupport("syntax/test.xml");
        Token<XMLTokenId> token = support.getPreviousToken(30);
        System.out.println("Token: " + token.id().name() + " Text: " + token.text());
        token = support.getPreviousToken(31);
        System.out.println("Token: " + token.id().name() + " Text: " + token.text());
        token = support.getPreviousToken(32);
        System.out.println("Token: " + token.id().name() + " Text: " + token.text());
    }
}

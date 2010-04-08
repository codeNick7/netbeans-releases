/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.editor.lexer;

import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.test.MockMimeLookup;
import org.netbeans.api.html.lexer.HTMLTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.css.lexer.api.CssTokenId;
import org.openide.filesystems.FileObject;

/**
 *
 * @author petr
 */
public class LexUtilitiesTest extends PHPLexerTestBase {

    public LexUtilitiesTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetMostEmbeddedTokenSequenceLocking() throws Exception {
        FileObject file = getTestFile("testfiles/embeddings.php");
        Document doc = getDocument(file);
        LexUtilities.getMostEmbeddedTokenSequence(doc, 0, true);
        LexUtilities.getMostEmbeddedTokenSequence(doc, 0, false);
    }
    
    public void testGetMostEmbeddedTokenSequence() throws Exception {
        FileObject file = getTestFile("testfiles/embeddings.php");
        //the file content:
        //<div style="color: red"><? echo "hello"; ?></div>
        //01234567890123456789012345678901234567890123456789
        //0         1         2         3         4
        Document doc = getDocument(file);
        
        check(doc, 0, HTMLTokenId.language());
        check(doc, 5, HTMLTokenId.language());
        check(doc, 44, HTMLTokenId.language());
        check(doc, 26, PHPTokenId.language());
        check(doc, 40, PHPTokenId.language());
        check(doc, 14, CssTokenId.language());

    }

    private void check(Document doc, int offset, Language l) {
        TokenSequence ts = LexUtilities.getMostEmbeddedTokenSequence(doc, offset, true);
        assertNotNull(ts);
        assertEquals(l, ts.language());
    }

}

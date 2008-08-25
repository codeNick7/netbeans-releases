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
package org.netbeans.modules.php.editor.nav;

import java.awt.Color;
import java.io.CharConversionException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.modules.gsfpath.api.classpath.ClassPath;
import org.netbeans.modules.gsfpath.api.classpath.GlobalPathRegistry;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.gsf.api.CompilationInfo;
import org.netbeans.modules.gsf.api.ParserResult;
import org.netbeans.modules.gsfpath.spi.classpath.support.ClassPathSupport;
import org.netbeans.modules.php.editor.lexer.LexUtilities;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.modules.php.editor.parser.PHPParseResult;
import org.netbeans.modules.php.editor.parser.astnodes.ASTNode;
import org.netbeans.modules.php.editor.parser.astnodes.Expression;
import org.netbeans.modules.php.editor.parser.astnodes.Include;
import org.netbeans.modules.php.editor.parser.astnodes.ParenthesisExpression;
import org.netbeans.modules.php.editor.parser.astnodes.Program;
import org.netbeans.modules.php.editor.parser.astnodes.Scalar;
import org.netbeans.modules.php.editor.parser.astnodes.Scalar.Type;
import org.netbeans.modules.php.editor.parser.astnodes.visitors.DefaultVisitor;
import org.netbeans.modules.php.project.api.PhpSourcePath;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.URLMapper;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.openide.xml.XMLUtil;

/**
 * Various utilies related to Php refactoring; the generic ones are based
 * on the ones from the Java refactoring module.
 * 
 * @author Jan Becicka, Tor Norbye, Jan Lahoda, Radek Matous
 */
public class RefactoringUtils {

    public static boolean isPhpFile(FileObject fo) {
        return PhpSourcePath.MIME_TYPE.equals(fo.getMIMEType());
    }

    public static Program getRoot(CompilationInfo info) {
        PHPParseResult result = getParseResult(info);
        return (result != null) ? result.getProgram() : null;
    }

    public static PHPParseResult getParseResult(CompilationInfo info) {
        ParserResult result = info.getEmbeddedResult(PhpSourcePath.MIME_TYPE, 0);
        return ((PHPParseResult) result);
    }



    public static String htmlize(String input) {
        try {
            return XMLUtil.toElementContent(input);
        } catch (CharConversionException cce) {
            Exceptions.printStackTrace(cce);
            return input;
        }
    }

    
    public static List<ASTNode> underCaret(CompilationInfo info, final int offset) {
        class Result extends Error {

            private Stack<ASTNode> result;

            public Result(Stack<ASTNode> result) {
                this.result = result;
            }

            @Override
            public Throwable fillInStackTrace() {
                return this;
            }
        }
        try {
            new DefaultVisitor() {

                private Stack<ASTNode> s = new Stack<ASTNode>();

                @Override
                public void scan(ASTNode node) {
                    if (node == null) {
                        return;
                    }

                    if (node.getStartOffset() <= offset && offset <= node.getEndOffset()) {
                        s.push(node);
                        super.scan(node);
                        throw new Result(s);
                    }
                }
            }.scan(RefactoringUtils.getRoot(info));
        } catch (Result r) {
            return new LinkedList<ASTNode>(r.result);
        }

        return Collections.emptyList();
    }

    public static boolean isQuoted(String value) {
        return value.length() >= 2 &&
                (value.startsWith("\"") || value.startsWith("'")) &&
                (value.endsWith("\"") || value.endsWith("'"));
    }

    public static String dequote(String value) {
        assert isQuoted(value);

        return value.substring(1, value.length() - 1);
    }

    public static FileObject resolveInclude(CompilationInfo info, Include include) {
        Expression e = include.getExpression();

        if (e instanceof ParenthesisExpression) {
            e = ((ParenthesisExpression) e).getExpression();
        }

        if (e instanceof Scalar) {
            Scalar s = (Scalar) e;

            if (Type.STRING == s.getScalarType()) {
                String fileName = s.getStringValue();
                fileName = fileName.length() >= 2 ? fileName.substring(1, fileName.length() - 1) : fileName;//TODO: not nice

                return resolveRelativeFile(info, fileName);
            }
        }

        return null;
    }

    private static FileObject resolveRelativeFile(CompilationInfo info, String name) {
        PhpSourcePath psp = null;
        Project p = FileOwnerQuery.getOwner(info.getFileObject());

        if (p != null) {
            psp = p.getLookup().lookup(PhpSourcePath.class);
        }

        while (true) {
            FileObject result;

            if (psp != null) {
                result = psp.resolveFile(info.getFileObject().getParent(), name);
            } else {
                result = info.getFileObject().getParent().getFileObject(name);
            }

            if (result != null) {
                return result;
            }

            //try to strip a directory from the "name":
            int slash = name.indexOf('/');

            if (slash != (-1)) {
                name = name.substring(slash + 1);
            } else {
                return null;
            }
        }
    }

    public static FileObject getFile(Document doc) {
        Object o = doc.getProperty(Document.StreamDescriptionProperty);

        if (o instanceof DataObject) {
            DataObject od = (DataObject) o;

            return od.getPrimaryFile();
        }

        return null;
    }
}

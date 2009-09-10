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

package org.netbeans.modules.web.core.syntax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.jsp.lexer.JspTokenId;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.lexer.TokenUtilities;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Embedding;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.web.jsps.parserapi.JspParserAPI;

import org.netbeans.modules.web.jsps.parserapi.PageInfo;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import static org.netbeans.api.jsp.lexer.JspTokenId.JavaCodeType;

/**
 * Utility class for generating a simplified <em>JSP servlet</em> class from a JSP file.
 * Using a full featured JSP parser would be too resource demanding,
 * we need a lightweight solution to be used with code completion.
 *
 * Inputs: original JSP document, caret offset within the original document
 * Outputs: a body of a simplified JSP servlet class, offset of the corresponding
 *          position in the servlet class
 *
 * @author Tomasz.Slota@Sun.COM
 */
public class SimplifiedJspServlet extends JSPProcessor {

    private static final String CLASS_HEADER = "\nclass SimplifiedJSPServlet extends %s {\n" + //NOI18N
            "\tprivate static final long serialVersionUID = 1L;\n"; //NOI18N
    private static final String METHOD_HEADER = "\n\tvoid mergedScriptlets(\n"
            + "\t\tHttpServletRequest request,\n"
            + "\t\tHttpServletResponse response,\n"
            + "\t\tHttpSession session,\n"
            + "\t\tServletContext application,\n"
            + "\t\tJspWriter out,\n"
            + "\t\tServletConfig config,\n"
            + "\t\tJspContext jspContext,\n"
            + "\t\tObject page,\n"
            + "\t\tPageContext pageContext,\n"
            + "\t\tThrowable exception\n"
            + "\t) throws Throwable {\n"; //NOI18N
    private static final String CLASS_FOOTER = "\n\t}\n}"; //NOI18N
    private CharSequence charSequence;
    private final Snapshot snapshot;
    private final ArrayList<Embedding> codeBlocks = new ArrayList<Embedding>();
    private List<String> localImportsFound = new ArrayList<String>();
    private List<String> localBeansFound = new ArrayList<String>();

    private Embedding header;
    private List<Embedding> scriptlets = new LinkedList<Embedding>();
    private List<Embedding> declarations = new LinkedList<Embedding>();
    private List<Embedding> localImports = new LinkedList<Embedding>();
    // keep bean declarations separate to avoid duplicating the declaration, see #130745
    List<Embedding> beanDeclarations = new LinkedList<Embedding>();;
    
    private List<Embedding> implicitImports = new LinkedList<Embedding>();;
    private int expressionIndex = 1;
    

    public SimplifiedJspServlet(Snapshot snapshot, Document doc){
        this(snapshot, doc, null);
    }

    public SimplifiedJspServlet(Snapshot snapshot, Document doc, CharSequence charSequence) {
        this.doc = doc;

        if (charSequence == null) {
            this.charSequence = snapshot.getText();
        } else {
            this.charSequence = charSequence;
        }

        if (doc != null){
            DataObject dobj = NbEditorUtilities.getDataObject(doc);
            fobj = (dobj != null) ? NbEditorUtilities.getDataObject(doc).getPrimaryFile(): null;
        } else {
            logger.log(Level.SEVERE, "Unable to find FileObject for document");
            fobj = null;
        }

        this.snapshot = snapshot;
    }

    /* process under document readlock */
    @Override
    protected void renderProcess() throws BadLocationException{
        //check servlet API on classpath
        if (!isServletAPIOnClasspath()){
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    displayServletAPIMissingWarning();
                }
            });
            processingSuccessful = false;
            return;
        }

        processIncludes();

        TokenHierarchy tokenHierarchy = null;

        if (doc != null){
            tokenHierarchy = TokenHierarchy.get(doc);
        } else {
            tokenHierarchy = TokenHierarchy.create(charSequence, JspTokenId.language());
        }

        TokenSequence tokenSequence = tokenHierarchy.tokenSequence(); //get top level token sequence
        if (!tokenSequence.moveNext()) {
            return; //no tokens in token sequence
        }

        /**
         * process java code blocks one by one
         * note: We count on the fact the scripting language in JSP is Java
         */
        do {
            Token token = tokenSequence.token();

            if (token.id() == JspTokenId.SCRIPTLET) {
                int blockStart = token.offset(tokenHierarchy);

                JavaCodeType blockType = (JavaCodeType) token.getProperty(JspTokenId.SCRIPTLET_TOKEN_TYPE_PROPERTY);
                List<Embedding> buff = blockType == JavaCodeType.DECLARATION ? declarations : scriptlets;

                if (blockType == JavaCodeType.EXPRESSION) {
                    // the "" + (...) construction is used to preserve compatibility with pre-autoboxing java
                    // see issue #116598
                    buff.add(snapshot.create(String.format("\t\tObject expr%1$d = \"\" + (", expressionIndex++), "text/x-java")); //NOI18N
                    buff.add(snapshot.create(blockStart, token.length(), "text/x-java"));
                    buff.add(snapshot.create(");\n", "text/x-java")); //NOI18N
                } else {
                    buff.add(snapshot.create(blockStart, token.length(), "text/x-java"));
                    buff.add(snapshot.create("\n", "text/x-java")); //NOI18N
                }
            }
        } while (tokenSequence.moveNext());

        processImportsAndBeanDeclarations();

        header = snapshot.create(getClassHeader(), "text/x-java");
        implicitImports.add(snapshot.create(createImplicitImportStatements(localImportsFound), "text/x-java"));
        beanDeclarations.add(snapshot.create("\n" + createBeanVarDeclarations(localBeansFound), "text/x-java"));
    }

    

    private boolean consumeWS(TokenSequence tokenSequence){
        if (tokenSequence.token().id() == JspTokenId.WHITESPACE){
            return tokenSequence.moveNext();
        }
        
        return true;
    }

    /**
     * The information about imports obtained from the JSP Parser
     * does not include data about offsets,
     * therefore it is necessary to some manual parsing.
     * 
     * This method creates embeddings and stores them in the
     * <code>localImports</code>
     *
     * additionaly it returns a list of imports found
     */
    private void processImportsAndBeanDeclarations() {
        TokenHierarchy tokenHierarchy = null;

        if (doc != null){
            tokenHierarchy = TokenHierarchy.get(doc);
        } else {
            tokenHierarchy = TokenHierarchy.create(charSequence, JspTokenId.language());
        }
        
        TokenSequence tokenSequence = tokenHierarchy.tokenSequence();
        tokenSequence.moveStart();

        while (tokenSequence.moveNext()) {
            PieceOfCode pieceOfCode = extractCodeFromTagAttribute(tokenSequence,
                    Arrays.asList("page", "tag"), //NOI18N
                    Arrays.asList("import"));     //NOI18N

            if (pieceOfCode != null){
                String importContent = pieceOfCode.getContent();
                int startOffset = 0;
                boolean moreToProcess = true;

                int endOffset;

                do {
                    // the JSP directive can take a comma separated list of imports
                    endOffset = importContent.indexOf(',', startOffset);

                    if (endOffset == -1) {
                        endOffset = importContent.length();
                        moreToProcess = false;
                    }

                    localImports.add(snapshot.create("import ", "text/x-java")); //NOI18N

                    localImports.add(snapshot.create(pieceOfCode.getStartOffset() + startOffset,
                            endOffset - startOffset,
                            "text/x-java")); //NOI18N

                    localImports.add(snapshot.create(";\n", "text/x-java")); //NOI18N

                    String singleImport = importContent.substring(startOffset, endOffset).trim();
                    localImportsFound.add(singleImport);
                    startOffset = endOffset + 1;
                    
                } while (moreToProcess);
            } else {
                pieceOfCode = extractCodeFromTagAttribute(tokenSequence,
                    Arrays.asList("jsp:useBean"), //NOI18N
                    Arrays.asList("class", "type"));     //NOI18N


                if (pieceOfCode != null){
                    PieceOfCode id = extractCodeFromTagAttribute(tokenSequence,
                        Arrays.asList("jsp:useBean"), //NOI18N
                        Arrays.asList("id"));     //NOI18N

                    // id may be null in broken (incomplete) code
                    if (id != null){
                        beanDeclarations.add(snapshot.create(
                                pieceOfCode.getStartOffset(),
                                pieceOfCode.getLength(), "text/x-java")); //NOI18N

                        beanDeclarations.add(snapshot.create(" ", "text/x-java")); //NOI18N

                        beanDeclarations.add(snapshot.create(
                                id.getStartOffset(),
                                id.getLength(), "text/x-java")); //NOI18N

                        beanDeclarations.add(snapshot.create(";\n", "text/x-java")); //NOI18N

                        String beanId = id.getContent();
                        localBeansFound.add(beanId);
                    }
                }

            }
        }
    }

    private PieceOfCode extractCodeFromTagAttribute(TokenSequence tokenSequence, List<String> tagName, List<String> attrName) {
        PieceOfCode pieceOfCode = null;
        
        if (tokenSequence.token().id() == JspTokenId.TAG && tagName.contains(tokenSequence.token().text().toString())) { //NOI18N

            int startPos = tokenSequence.offset();

            tokensearchloop:
            while (tokenSequence.moveNext() && consumeWS(tokenSequence)
                    && !(tokenSequence.token().id() == JspTokenId.SYMBOL
                    && TokenUtilities.equals(tokenSequence.token().text(), "%>"))) {

                if (tokenSequence.token().id() == JspTokenId.ATTRIBUTE && attrName.contains(tokenSequence.token().text().toString())) { //NOI18N

                    if (tokenSequence.moveNext() && consumeWS(tokenSequence) && tokenSequence.token().id() == JspTokenId.SYMBOL && TokenUtilities.equals("=", tokenSequence.token().text())) {

                        if (tokenSequence.moveNext() && consumeWS(tokenSequence) && tokenSequence.token().id() == JspTokenId.ATTR_VALUE) {

                            String val = tokenSequence.token().text().toString();

                            // extract the content of quoted string
                            if (val.length() > 2
                                    // attr values can be specified using double or single quotes
                                    && (val.charAt(0) == '"' || val.charAt(0) == '\'')
                                    && val.charAt(val.length() - 1) == val.charAt(0)) {

                                // a hack for compatibility with
                                // org.netbeans.modules.editor.java.Utilities.isJavaContext()
                                tokenSequence.createEmbedding(JavaTokenId.language(), 1, 1);

                                int startOffset = tokenSequence.offset() + 1;
                                int len = val.length() - 1;
                                String imprt = val.substring(1, len);
                                pieceOfCode = new PieceOfCode(imprt, startOffset, imprt.length());
                                break tokensearchloop;
                            }
                        }
                    }
                }
            }

            tokenSequence.move(startPos);
            tokenSequence.moveNext();
        }

        return pieceOfCode;
    }

    

    private boolean isServletAPIOnClasspath() {
        ClassPath cp = ClassPath.getClassPath(fobj, ClassPath.COMPILE);

        if (cp != null && cp.findResource("javax/servlet/http/HttpServlet.class") != null) { //NOI18N
            return true;
        }

        return false;
    }

    @Override
    protected void processIncludedFile(IncludedJSPFileProcessor includedJSPFileProcessor) {
        implicitImports.add(snapshot.create(includedJSPFileProcessor.getImports(), "text/x-java"));
        declarations.add(snapshot.create(includedJSPFileProcessor.getDeclarations(), "text/x-java"));
        //TODO: is it necessary?
        scriptlets.add(snapshot.create(includedJSPFileProcessor.getScriptlets(), "text/x-java"));
    }

    private void displayServletAPIMissingWarning() {
        if (fobj == null){
            return; //issue #160889
        }

        try {
            DataObject doJsp = DataObject.find(fobj);
            EditorCookie editor = doJsp.getCookie(EditorCookie.class);

            if (editor != null && editor.getOpenedPanes() != null) {

                JTextComponent component = editor.getOpenedPanes()[0];
                if (component != null) {
                    org.netbeans.editor.Utilities.setStatusBoldText(component,
                            NbBundle.getMessage(SimplifiedJspServlet.class, "MSG_MissingServletAPI"));
                }
            }
        } catch (DataObjectNotFoundException e) {
            // ignore
        }
    }


    
    

    private String getClassHeader() {
        String extendsClass = null; //NOI18N
        PageInfo pageInfo = getPageInfo();

        if (pageInfo != null) {
            extendsClass = pageInfo.getExtends();
        }

        if (extendsClass == null ||
                // workaround for issue #116314
                "org.apache.jasper.runtime.HttpJspBase".equals(extendsClass)){ //NOI18N
            extendsClass = "HttpServlet"; //NOI18N
        }

        return String.format(CLASS_HEADER, extendsClass);
    }

    @Override
    protected Collection<String> processedIncludes() {
        return Collections.emptyList();
    }

    public Embedding getSimplifiedServlet() {
        assureProcessCalled();

        if (!processingSuccessful){
            return null;
        }

        if (localImports.isEmpty() && declarations.isEmpty() && scriptlets.isEmpty()) {
            return null;
        }

        List<Embedding> content = new LinkedList<Embedding>();

        // debug code to find the root cause of #169924
        assert !implicitImports.contains(null) : "implicitImports contains null";
        assert !localImports.contains(null) : "localImports contains null";
        assert !declarations.contains(null) : "declarations contains null";
        assert !beanDeclarations.contains(null) : "beanDeclarations contains null";
        assert !scriptlets.contains(null) : "scriptlets contains null";
        
        content.addAll(implicitImports);
        content.addAll(localImports);
        content.add(header);
        content.addAll(declarations);
        content.addAll(beanDeclarations);
        content.add(snapshot.create(METHOD_HEADER, "text/x-java"));
        content.addAll(scriptlets);
        content.add(snapshot.create(CLASS_FOOTER, "text/x-java"));

        Embedding embedding = Embedding.create(content);

        if (logger.isLoggable(Level.FINEST)){
            String msg = "---\n" + embedding.getSnapshot().getText() + "\n---";
            logger.finest(msg);
        }
        
        return embedding;
    }

    @Deprecated
    public static abstract class VirtualJavaClass {

        public final void create(Document doc, String virtualClassBody) {
            FileObject fileDummyJava = null;
            List<? extends CompletionItem> javaCompletionItems = null;

            try {
                FileSystem memFS = FileUtil.createMemoryFileSystem();
                fileDummyJava = memFS.getRoot().createData("SimplifiedJSPServlet", "java"); //NOI18N
                PrintWriter writer = new PrintWriter(fileDummyJava.getOutputStream());
                writer.print(virtualClassBody);
                writer.close();

                Source source = Source.create(fileDummyJava);
                process(fileDummyJava, source);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        protected abstract void process(FileObject fileObject, Source javaEmbedding);
    }

    private class PieceOfCode{
        private String content;
        private int startOffset;
        private int length;

        public PieceOfCode(String content, int startOffset, int length) {
            this.content = content;
            this.startOffset = startOffset;
            this.length = length;
        }

        public String getContent() {
            return content;
        }

        public int getLength() {
            return length;
        }

        public int getStartOffset() {
            return startOffset;
        }
    }
}

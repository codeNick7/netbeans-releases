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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */
package org.netbeans.modules.web.el;

import com.sun.el.parser.AstIdentifier;
import com.sun.el.parser.AstString;
import com.sun.el.parser.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractElementVisitor6;
import javax.swing.text.Document;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.ui.ElementOpen;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.netbeans.modules.el.lexer.api.ELTokenId;
import org.netbeans.modules.parsing.api.ParserManager;
import org.netbeans.modules.parsing.api.ResultIterator;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.api.UserTask;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.web.common.api.LexerUtils;
import org.netbeans.modules.web.common.api.WebUtils;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * Go to declaration for Expression Language.
 *
 * @author Erno Mononen
 */
public final class ELHyperlinkProvider implements HyperlinkProviderExt {

    @Override
    public boolean isHyperlinkPoint(final Document doc, final int offset, HyperlinkType type) {
        final AtomicBoolean ret = new AtomicBoolean(false);
        doc.render(new Runnable() {

            @Override
            public void run() {
                ret.set(getELIdentifierSpan(doc, offset) != null);
            }
        });

        return ret.get();
    }

    @Override
    public int[] getHyperlinkSpan(final Document doc, final int offset, HyperlinkType type) {
        final AtomicReference<int[]> ret = new AtomicReference<int[]>();
        doc.render(new Runnable() {

            @Override
            public void run() {
                ret.set(getELIdentifierSpan(doc, offset));
            }
        });
        return ret.get();
    }

    @Override
    public void performClickAction(final Document doc, final int offset, HyperlinkType type) {
        final AtomicBoolean cancel = new AtomicBoolean();
        ProgressUtils.runOffEventDispatchThread(new Runnable() {
            @Override
            public void run() {
                performGoTo(doc, offset, cancel);
            }
        }, NbBundle.getMessage(ELHyperlinkProvider.class, "LBL_GoToDeclaration"), cancel, false);
    }

    @Override
    public Set<HyperlinkType> getSupportedHyperlinkTypes() {
        return EnumSet.of(HyperlinkType.GO_TO_DECLARATION);
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType type) {
        Pair<Node, ELElement> nodeAndElement = resolveNodeAndElement(doc, offset, new AtomicBoolean());
        if (nodeAndElement != null) {
            if (nodeAndElement.first instanceof AstString) {
                // could be a resource bundle key
                return getTooltipTextForBundleKey(nodeAndElement);
            } else {
                return getTooltipTextForElement(nodeAndElement);
            }
        }
        return null;
    }

    private String getTooltipTextForElement(Pair<Node, ELElement> pair) {
        FileObject context = pair.second.getSnapshot().getSource().getFileObject();
        final Element resolvedElement = ELTypeUtilities.create(context).resolveElement(pair.second, pair.first);
        if (resolvedElement == null) {
            return null;
        }
        final String[] result = new String[1];
        ClasspathInfo cp = ClasspathInfo.create(pair.second.getSnapshot().getSource().getFileObject());
        try {
            JavaSource.create(cp).runUserActionTask(new Task<CompilationController>() {

                @Override
                public void run(CompilationController cc) throws Exception {
                    DisplayNameElementVisitor dnev = new DisplayNameElementVisitor(cc);
                    dnev.visit(resolvedElement, Boolean.TRUE);
                    result[0] = "<html><body>" + dnev.result.toString(); //NOI18N
                }
            }, true);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return result[0];
    }
    
    private String getTooltipTextForBundleKey(Pair<Node, ELElement> pair) {
        FileObject context = pair.second.getSnapshot().getSource().getFileObject();
        ResourceBundles resourceBundles = ResourceBundles.get(context);
        if (!resourceBundles.canHaveBundles()) {
            return null;
        }
        for (Pair<AstIdentifier,AstString> each : resourceBundles.collectKeys(pair.second.getNode())) {
            if (each.second.equals(pair.first)) {
                StringBuilder result = new StringBuilder();
                String key = each.second.getString();
                String value = resourceBundles.getValue(each.first.getImage(), each.second.getString());
                String bundle = each.first.getImage();
                result.append("<html><body>")
                        /* displaying the bundle in the tooltip looks a bit strange,
                          so commented out for now - maybe using a smaller font would
                          help
                         append("<i>")
                        .append(NbBundle.getMessage(ELHyperlinkProvider.class, "ResourceBundle", bundle))
                        .append("</i><br>")
                         */
                        .append(key)
                        .append("=<font color='#ce7b00'>") // NOI18N
                        .append(value)
                        .append("</font>"); // NOI18N
                
                return result.toString();
            }
        }
        return null;
    }

    /**
     * Resolves the Node at the given offset.
     * @return the node and the ELElement containing it or null.
     */
    private Pair<Node, ELElement> resolveNodeAndElement(final Document doc, final int offset, final AtomicBoolean cancel) {
        final List<Pair<Node,ELElement>> result = new ArrayList<Pair<Node,ELElement>>(1);
        Source source = Source.create(doc);
        try {
            ParserManager.parse(Collections.singletonList(source), new UserTask() {
                @Override
                public void run(ResultIterator resultIterator) throws Exception {
                    ResultIterator elRi = WebUtils.getResultIterator(resultIterator, ELLanguage.MIME_TYPE);
                    if (cancel.get()) {
                        return;
                    }
                    ELParserResult parserResult = (ELParserResult) elRi.getParserResult();
                    ELElement elElement = parserResult.getElementAt(offset);
                    if (elElement == null || !elElement.isValid()) {
                        return;
                    }
                    Node node = elElement.findNodeAt(offset);
                    if (node == null) {
                        return;
                    }
                    result.add(Pair.<Node, ELElement>of(node, elElement));
                }
            });
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return result.isEmpty() ? null : result.get(0);
    }

    private void performGoTo(final Document doc, final int offset, final AtomicBoolean cancel) {
        Pair<Node, ELElement> nodeElem = resolveNodeAndElement(doc, offset, cancel);
        if (nodeElem == null) {
            return;
        }
        FileObject context = nodeElem.second.getSnapshot().getSource().getFileObject();
        Element javaElement = ELTypeUtilities.create(context).resolveElement(nodeElem.second, nodeElem.first);
        if (javaElement != null) {
            ElementOpen.open(ClasspathInfo.create(context), javaElement);
        }
    }

    private int[] getELIdentifierSpan(Document doc, int offset) {
        TokenSequence<?> elTokenSequence = LexerUtils.getTokenSequence(doc, offset, ELTokenId.language(), false);
        if (elTokenSequence == null) {
            return null;
        }

        elTokenSequence.move(offset);
        if (!elTokenSequence.moveNext()) {
            return null; //no token
        }

        if (elTokenSequence.token().id() == ELTokenId.IDENTIFIER
                || elTokenSequence.token().id() == ELTokenId.STRING_LITERAL) { // string for bundle keys

            return new int[]{elTokenSequence.offset(), elTokenSequence.offset() + elTokenSequence.token().length()};
        }

        return null;
    }

    /**
     * This is a copy of {@code org.netbeans.modules.editor.java.GoToSupport.DisplayNameElementVisitor}.
     * See #189669.
     */
    private static final class DisplayNameElementVisitor extends AbstractElementVisitor6<Void, Boolean> {

        private final CompilationInfo info;

        public DisplayNameElementVisitor(CompilationInfo info) {
            this.info = info;
        }

        private StringBuffer result        = new StringBuffer();

        private void boldStartCheck(boolean highlightName) {
            if (highlightName) {
                result.append("<b>");
            }
        }

        private void boldStopCheck(boolean highlightName) {
            if (highlightName) {
                result.append("</b>");
            }
        }

        public Void visitPackage(PackageElement e, Boolean highlightName) {
            boldStartCheck(highlightName);

            result.append(e.getQualifiedName());

            boldStopCheck(highlightName);

            return null;
        }

        public Void visitType(TypeElement e, Boolean highlightName) {
            return printType(e, null, highlightName);
        }

        Void printType(TypeElement e, DeclaredType dt, Boolean highlightName) {
            modifier(e.getModifiers());
            switch (e.getKind()) {
                case CLASS:
                    result.append("class ");
                    break;
                case INTERFACE:
                    result.append("interface ");
                    break;
                case ENUM:
                    result.append("enum ");
                    break;
                case ANNOTATION_TYPE:
                    result.append("@interface ");
                    break;
            }
            Element enclosing = e.getEnclosingElement();

            if (enclosing == info.getElementUtilities().enclosingTypeElement(e)) {
                result.append(((TypeElement) enclosing).getQualifiedName());
                result.append('.');
                boldStartCheck(highlightName);
                result.append(e.getSimpleName());
                boldStopCheck(highlightName);
            } else {
                result.append(e.getQualifiedName());
            }

            if (dt != null)
                dumpRealTypeArguments(dt.getTypeArguments());

            return null;
        }

        public Void visitVariable(VariableElement e, Boolean highlightName) {
            modifier(e.getModifiers());

            result.append(getTypeName(info, e.asType(), true));

            result.append(' ');

            boldStartCheck(highlightName);

            result.append(e.getSimpleName());

            boldStopCheck(highlightName);

            if (highlightName) {
                if (e.getConstantValue() != null) {
                    result.append(" = ");
                    result.append(e.getConstantValue().toString());
                }

                Element enclosing = e.getEnclosingElement();

                if (e.getKind() != ElementKind.PARAMETER && e.getKind() != ElementKind.LOCAL_VARIABLE && e.getKind() != ElementKind.EXCEPTION_PARAMETER) {
                    result.append(" in ");

                    //short typename:
                    result.append(getTypeName(info, enclosing.asType(), true));
                }
            }

            return null;
        }

        public Void visitExecutable(ExecutableElement e, Boolean highlightName) {
            return printExecutable(e, null, highlightName);
        }

        Void printExecutable(ExecutableElement e, DeclaredType dt, Boolean highlightName) {
            switch (e.getKind()) {
                case CONSTRUCTOR:
                    modifier(e.getModifiers());
                    dumpTypeArguments(e.getTypeParameters());
                    result.append(' ');
                    boldStartCheck(highlightName);
                    result.append(e.getEnclosingElement().getSimpleName());
                    boldStopCheck(highlightName);
                    if (dt != null) {
                        dumpRealTypeArguments(dt.getTypeArguments());
                        dumpArguments(e.getParameters(), ((ExecutableType) info.getTypes().asMemberOf(dt, e)).getParameterTypes());
                    } else {
                        dumpArguments(e.getParameters(), null);
                    }
                    dumpThrows(e.getThrownTypes());
                    break;
                case METHOD:
                    modifier(e.getModifiers());
                    dumpTypeArguments(e.getTypeParameters());
                    result.append(getTypeName(info, e.getReturnType(), true));
                    result.append(' ');
                    boldStartCheck(highlightName);
                    result.append(e.getSimpleName());
                    boldStopCheck(highlightName);
                    dumpArguments(e.getParameters(), null);
                    dumpThrows(e.getThrownTypes());
                    break;
                case INSTANCE_INIT:
                case STATIC_INIT:
                    //these two cannot be referenced anyway...
            }
            return null;
        }

        public Void visitTypeParameter(TypeParameterElement e, Boolean highlightName) {
            return null;
        }

        private void modifier(Set<Modifier> modifiers) {
            boolean addSpace = false;

            for (Modifier m : modifiers) {
                if (addSpace) {
                    result.append(' ');
                }
                addSpace = true;
                result.append(m.toString());
            }

            if (addSpace) {
                result.append(' ');
            }
        }

        private void dumpTypeArguments(List<? extends TypeParameterElement> list) {
            if (list.isEmpty())
                return ;

            boolean addSpace = false;

            result.append("&lt;");

            for (TypeParameterElement e : list) {
                if (addSpace) {
                    result.append(", ");
                }

                result.append(getTypeName(info, e.asType(), true));

                addSpace = true;
            }

            result.append("&gt;");
        }

        private void dumpRealTypeArguments(List<? extends TypeMirror> list) {
            if (list.isEmpty())
                return ;

            boolean addSpace = false;

            result.append("&lt;");

            for (TypeMirror t : list) {
                if (addSpace) {
                    result.append(", ");
                }

                result.append(getTypeName(info, t, true));

                addSpace = true;
            }

            result.append("&gt;");
        }

        private void dumpArguments(List<? extends VariableElement> list, List<? extends TypeMirror> types) {
            boolean addSpace = false;

            result.append('(');

            Iterator<? extends VariableElement> listIt = list.iterator();
            Iterator<? extends TypeMirror> typesIt = types != null ? types.iterator() : null;

            while (listIt.hasNext()) {
                if (addSpace) {
                    result.append(", ");
                }

                VariableElement ve = listIt.next();
                TypeMirror      type = typesIt != null ? typesIt.next() : ve.asType();

                result.append(getTypeName(info, type, true));
                result.append(" ");
                result.append(ve.getSimpleName());

                addSpace = true;
            }

            result.append(')');
        }

        private void dumpThrows(List<? extends TypeMirror> list) {
            if (list.isEmpty())
                return ;

            boolean addSpace = false;

            result.append(" throws ");

            for (TypeMirror t : list) {
                if (addSpace) {
                    result.append(", ");
                }

                result.append(getTypeName(info, t, true));

                addSpace = true;
            }
        }
    }

    private static String getTypeName(CompilationInfo info, TypeMirror t, boolean fqn) {
        return translate(info.getTypeUtilities().getTypeName(t).toString());
    }

    private static String[] c = new String[] {"&", "<", ">", "\n", "\""}; // NOI18N
    private static String[] tags = new String[] {"&amp;", "&lt;", "&gt;", "<br>", "&quot;"}; // NOI18N

    private static String translate(String input) {
        for (int cntr = 0; cntr < c.length; cntr++) {
            input = input.replaceAll(c[cntr], tags[cntr]);
        }

        return input;
    }

}

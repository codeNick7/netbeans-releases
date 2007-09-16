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
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 */
package org.netbeans.modules.java.editor.semantic;

import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import java.io.IOException;
import javax.lang.model.element.Element;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.ui.ElementHeaders;
import org.netbeans.spi.editor.highlighting.HighlightAttributeValue;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

import static org.netbeans.api.java.source.ui.ElementHeaders.*;

/**
 * @author Jan Lahoda
 */
final class UnusedTooltipResolver implements HighlightAttributeValue<String> {
    
    public String getValue(JTextComponent component, Document document, Object attributeKey, int startOffset, final int endOffset) {
        try {
            JavaSource js = JavaSource.forDocument(document);

            if (js == null) {
                return null;
            }
            final String[] result = new String[1];

            js.runUserActionTask(new Task<CompilationController>() {
                public void run(CompilationController cont) throws Exception {
                    cont.toPhase(Phase.RESOLVED);

                    TreePath tp = cont.getTreeUtilities().pathFor(endOffset);
                    
                    if (tp == null) {
                        return ;
                    }
                    
                    boolean isInImport = false;
                    
                    TreePath lookingFor = tp;
                    
                    OUTER: while (lookingFor != null) {
                        Tree t = lookingFor.getLeaf();
                        
                        switch (t.getKind()) {
                            case IMPORT:
                                isInImport = true;
                                break OUTER;
                            case CLASS:
                            case METHOD:
                            case BLOCK:
                                isInImport = false;
                                break OUTER;
                        }
                        
                        lookingFor = lookingFor.getParentPath();
                    }
                    
                    if (isInImport) {
                        result[0] = NbBundle.getMessage(SemanticHighlighter.class, "LBL_UnusedImport");
                        return;
                    }
                    
                    Element e = cont.getTrees().getElement(tp);

                    if (e == null) {
                        return;
                    }
                    
                    String elementDisplayName = null;
                    String key = null;
                    
                    switch (e.getKind()) {
                        case LOCAL_VARIABLE:
                        case EXCEPTION_PARAMETER:
                            key = "LBL_UnusedVariable";
                            elementDisplayName = e.getSimpleName().toString();
                            break;
                        case PARAMETER:
                            key = "LBL_UnusedParameter";
                            elementDisplayName = e.getSimpleName().toString();
                            break;
                        case FIELD:
                            key = "LBL_UnusedField";
                            elementDisplayName = e.getSimpleName().toString();
                            break;
                        case METHOD:
                            key = "LBL_UnusedMethod";
                            elementDisplayName = ElementHeaders.getHeader(e, cont, NAME + PARAMETERS);
                            break;
                        case CONSTRUCTOR:
                            key = "LBL_UnusedConstructor";
                            elementDisplayName = e.getEnclosingElement().getSimpleName().toString() + ElementHeaders.getHeader(e, cont, PARAMETERS);
                            break;
                        case CLASS:
                            key = "LBL_UnusedClass";
                            elementDisplayName = e.getSimpleName().toString();
                            break;
                        case INTERFACE:
                            key = "LBL_UnusedInterface";
                            elementDisplayName = e.getSimpleName().toString();
                            break;
                        case ANNOTATION_TYPE:
                            key = "LBL_UnusedAnnotationType";
                            elementDisplayName = e.getSimpleName().toString();
                            break;
                        case ENUM:
                            key = "LBL_UnusedEnum";
                            elementDisplayName = e.getSimpleName().toString();
                            break;
                    }
                    
                    if (elementDisplayName != null) {
                        result[0] = NbBundle.getMessage(UnusedTooltipResolver.class, key, elementDisplayName);
                    }
                }
            }, true);

            return result[0];
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
    
}

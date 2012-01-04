/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */

package org.netbeans.modules.refactoring.java.plugins;

import com.sun.source.tree.*;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.util.List;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import org.netbeans.api.java.source.WorkingCopy;
import org.netbeans.modules.refactoring.java.api.JavaRefactoringUtils;
import org.netbeans.modules.refactoring.java.spi.RefactoringVisitor;
import org.netbeans.modules.refactoring.java.spi.ToPhaseException;
import org.openide.util.Exceptions;

/**
 * @author Jan Becicka, Bharath Ravi Kumar
 */
class VarUsageVisitor extends RefactoringVisitor {

    private final TypeElement superTypeElement;
    private final TypeElement subTypeElement;
    private boolean isReplCandidate = true;

    VarUsageVisitor(TypeElement subTypeElement, WorkingCopy workingCopy, 
            TypeElement superTypeElem) {
        try {
            setWorkingCopy(workingCopy);
        } catch (ToPhaseException ex) {
            Exceptions.printStackTrace(ex);
        }
        this.superTypeElement = superTypeElem;
        this.subTypeElement = subTypeElement;
    }

    @Override
    public Tree visitReturn(ReturnTree node, Element p) {
        ExpressionTree expression = node.getExpression();
        checkReturnType(expression, p);
        return super.visitReturn(node, p);
    }

    private void checkReturnType(ExpressionTree expression, Element p) {
        if(expression == null || p == null) { return; }
        if(expression.getKind() == Tree.Kind.IDENTIFIER) {
            IdentifierTree ident = (IdentifierTree) expression;
            checkReturnType(ident, p);
        } else if(expression.getKind() == Tree.Kind.CONDITIONAL_EXPRESSION) {
            ConditionalExpressionTree cet = (ConditionalExpressionTree) expression;
            checkReturnType(cet.getFalseExpression(), p);
            checkReturnType(cet.getTrueExpression(), p);
        }
    }

    private void checkReturnType(IdentifierTree ident, Element p) {
        Element el = asElement(ident);
        if(el.equals(p)) {
            MethodTree method = (MethodTree) JavaPluginUtils.findMethod(getCurrentPath()).getLeaf();
            TypeMirror returnType = workingCopy.getTrees().getTypeMirror(workingCopy.getTrees().getPath(workingCopy.getCompilationUnit(), method.getReturnType()));
            if(!workingCopy.getTypes().isSubtype(superTypeElement.asType(), returnType)) {
                isReplCandidate = false;
            }
        }
    }

    @Override
    public Tree visitMemberSelect(MemberSelectTree memSelectTree, Element refVarElem) {

        Element methodElement = this.asElement(memSelectTree);
        Element varElement = asElement(memSelectTree.getExpression());
        if (!refVarElem.equals(varElement)) {
            return super.visitMemberSelect(memSelectTree, refVarElem);
        }

        boolean isAssgCmptble = isMemberAvailable(subTypeElement, methodElement, 
                superTypeElement);
        if (!isAssgCmptble) {
            isReplCandidate = false;
        }
        return super.visitMemberSelect(memSelectTree, refVarElem);
    }

    @Override
    public Tree visitMethodInvocation(MethodInvocationTree node, Element p) {
        List<? extends ExpressionTree> arguments = node.getArguments();
        for (int i = 0; i < arguments.size(); i++) {
            ExpressionTree argument = arguments.get(i);
            Element argElement = asElement(argument); // TODO: Slow and misses ternary expressions
            if(p.equals(argElement)) {
                ExecutableElement method = (ExecutableElement) asElement(node);
                VariableElement parameter = method.getParameters().get(i);
                Types types = workingCopy.getTypes();
                TypeMirror parameterType = parameter.asType();
                if(parameterType.getKind().equals(TypeKind.TYPEVAR)) {
                    TypeVariable typeVariable = (TypeVariable) parameterType;
                    TypeMirror upperBound = typeVariable.getUpperBound();
                    TypeMirror lowerBound = typeVariable.getLowerBound();
                    if(upperBound != null && !types.isSubtype(superTypeElement.asType(), upperBound)) {
                        isReplCandidate = false;
                    }
                    if(lowerBound != null && !types.isSubtype(lowerBound, superTypeElement.asType())) {
                        isReplCandidate = false;
                    }
                } else if(!types.isAssignable(superTypeElement.asType(), parameterType)) {
                    isReplCandidate = false;
                }
            }
        }
        return super.visitMethodInvocation(node, p);
    }

    @Override
    public Tree visitAssignment(AssignmentTree assgnTree, Element refVarElem) {

        ExpressionTree exprnTree = assgnTree.getExpression();
        Element exprElement = asElement(exprnTree);
        if (!refVarElem.equals(exprElement)) {
            return super.visitAssignment(assgnTree, refVarElem);
        }
        ExpressionTree varExprTree = assgnTree.getVariable();

        VariableElement varElement = (VariableElement) asElement(varExprTree);
        isReplCandidate = isReplacableAssgnmt(varElement) && isReplCandidate;
        return super.visitAssignment(assgnTree, refVarElem);
    }

    @Override
    public Tree visitVariable(VariableTree varTree, Element refVarElem) {
        ExpressionTree initTree = varTree.getInitializer();
        if (null == initTree) {
            return super.visitVariable(varTree, refVarElem);
        }
        Element exprElement = asElement(initTree);
        if (!refVarElem.equals(exprElement)) {
            return super.visitVariable(varTree, refVarElem);
        }
        VariableElement varElement = (VariableElement) asElement(varTree);
        isReplCandidate = isReplacableAssgnmt(varElement) && isReplCandidate;
        return super.visitVariable(varTree, refVarElem);
    }

    @Override
    public Tree visitMethod(MethodTree node, Element p) {
        List<? extends VariableTree> parameters = node.getParameters();
        for (VariableTree variableTree : parameters) {
            Element var = workingCopy.getTrees().getElement(new TreePath(getCurrentPath(), variableTree));
            if(p.equals(var)) {
                TypeElement classElement = (TypeElement) workingCopy.getTrees().getElement(JavaRefactoringUtils.findEnclosingClass(workingCopy, getCurrentPath(), true, true, true, true, false));
                List<ExecutableElement> methods = ElementFilter.methodsIn(workingCopy.getElements().getAllMembers(classElement));
                String methodName = node.getName().toString();
                for (ExecutableElement method : methods) {
                        if(methodName.equals(method.getSimpleName().toString())
                        && node.getParameters().size() == method.getParameters().size()) {
                    ExecutableElement element = (ExecutableElement) workingCopy.getTrees().getElement(getCurrentPath());
                    boolean sameParameters = true;
                    for (int j = 0; j < node.getParameters().size(); j++) {
                        TypeMirror exType = ((VariableElement)method.getParameters().get(j)).asType();
                        TypeMirror type;
                        VariableElement vari = (VariableElement)element.getParameters().get(j);
                        if(p.equals(vari)) {
                            type = superTypeElement.asType();
                        } else {
                            type = vari.asType();
                        }
                        if(!workingCopy.getTypes().isSameType(exType, type)) {
                            sameParameters = false;
                        }
                    }
                    if(sameParameters) {
                        isReplCandidate = false;
                    }
                    }
                }
            }
        }
        return super.visitMethod(node, p);
    }

    private boolean isMemberAvailable(TypeElement subTypeElement, Element methodElement, 
            TypeElement superTypeElement) {
        ElementKind memberKind = methodElement.getKind();
        if(ElementKind.METHOD.equals(memberKind)){
            return isMethodAvailable(subTypeElement, (ExecutableElement)methodElement, 
                    superTypeElement);
        }else{
            return isHidingMember(subTypeElement, methodElement, superTypeElement);
        }
    }

    private boolean isMethodAvailable(TypeElement subTypeElement, 
            ExecutableElement execElem, TypeElement superTypeElement) {
        Elements elements = workingCopy.getElements();
        List<? extends Element> memberElements = elements.getAllMembers(superTypeElement);
        for (Element elem : memberElements) {
            if(ElementKind.METHOD.equals(elem.getKind())){
                if(execElem.getModifiers().contains(Modifier.STATIC) && elements.hides(execElem, elem)){
                    return true;
                }else{
                    if(execElem.equals(elem) || elements.overrides(execElem, (ExecutableElement)elem, 
                            subTypeElement)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isHidingMember(TypeElement subTypeElement, Element variableElement, 
            TypeElement superTypeElement) {
        //TODO: We do not handle nested types yet (includes enums)
        Elements elements = workingCopy.getElements();
        List<? extends Element> memberElements = elements.getAllMembers(superTypeElement);
        for (Element elem : memberElements) {
            if(variableElement.equals(elem) || elements.hides(variableElement, elem)){
                return true;
            }
        }
        return false;
    }

    private boolean isReplacableAssgnmt(VariableElement varElement) {
        if (isDeclaredType(varElement.asType())) {
            DeclaredType declType = (DeclaredType) varElement.asType();
            TypeElement varType = (TypeElement) declType.asElement();
            if (isAssignable(superTypeElement, varType)) {
                return true;
            }
        }
        return false;
    }

    boolean isReplaceCandidate() {
        return isReplCandidate;
    }

    private boolean isAssignable(TypeElement typeFrom, TypeElement typeTo) {
        Types types = workingCopy.getTypes();
        return types.isAssignable(typeFrom.asType(), typeTo.asType());
    }

    private Element asElement(Tree tree) {
        Trees treeUtil = workingCopy.getTrees();
        TreePath treePath = treeUtil.getPath(workingCopy.getCompilationUnit(), tree);
        Element element = treeUtil.getElement(treePath);
        return element;
    }

    private boolean isDeclaredType(TypeMirror type) {
        return TypeKind.DECLARED.equals(type.getKind());
    }

}

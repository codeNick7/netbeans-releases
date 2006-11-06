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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.modules.j2ee.ejbcore.api.methodcontroller;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.util.Collections;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.netbeans.api.java.source.TreeUtilities;
import org.netbeans.api.java.source.WorkingCopy;
import org.netbeans.modules.j2ee.ejbcore.api.methodcontroller.AbstractMethodController;
import org.netbeans.modules.j2ee.ejbcore.api.methodcontroller.MethodType;
import org.netbeans.modules.j2ee.ejbcore.api.methodcontroller.MethodType.BusinessMethodType;
import org.netbeans.modules.j2ee.ejbcore.api.methodcontroller.MethodType.CreateMethodType;
import org.netbeans.modules.j2ee.ejbcore.api.methodcontroller.MethodType.FinderMethodType;
import org.netbeans.modules.j2ee.ejbcore.api.methodcontroller.MethodType.HomeMethodType;

/**
 *
 * @author Chris Webster
 * @author Martin Adamek
 */
class SessionGenerateFromIntfVisitor implements MethodType.MethodTypeVisitor, AbstractMethodController.GenerateFromIntf {

    private WorkingCopy workingCopy;
    private ExecutableElement implMethod;
    private static final String TODO = "//TODO implement "; //NOI18N
    
    public SessionGenerateFromIntfVisitor(WorkingCopy workingCopy) {
        this.workingCopy = workingCopy;
    }
    
    public void getInterfaceMethodFromImpl(MethodType m) {
        m.accept(this);
    }
    
    public ExecutableElement getImplMethod() {
        return implMethod;
    }
    
    public ExecutableElement getSecondaryMethod() {
        return null;
    }
    
    public void visit(BusinessMethodType bmt) {
        implMethod = bmt.getMethodElement();
        TypeMirror type= implMethod.getReturnType();

        SourcePositions[] positions = new SourcePositions[1];
        TreeUtilities tu = workingCopy.getTreeUtilities();
        String body = TODO + implMethod.getSimpleName() + EntityGenerateFromIntfVisitor.getReturnStatement(type);
        StatementTree bodyTree = tu.parseStatement(body, positions);
        
        MethodTree resultTree = AbstractMethodController.modifyMethod(
                workingCopy, 
                implMethod, 
                Collections.singleton(Modifier.PUBLIC), 
                null, null, null, null,
                workingCopy.getTreeMaker().Block(Collections.singletonList(bodyTree), false)
                );
        Trees trees = workingCopy.getTrees();
        TreePath treePath = trees.getPath(workingCopy.getCompilationUnit(), resultTree);
        implMethod = (ExecutableElement) trees.getElement(treePath);
    }
       
    public void visit(CreateMethodType cmt) {
        implMethod = cmt.getMethodElement();
        String origName = implMethod.getSimpleName().toString();
        String newName = prependAndUpper(origName,"ejb"); //NOI18N
        
        SourcePositions[] positions = new SourcePositions[1];
        TreeUtilities tu = workingCopy.getTreeUtilities();
        String body = TODO + newName;
        StatementTree bodyTree = tu.parseStatement(body, positions);
        
        MethodTree resultTree = AbstractMethodController.modifyMethod(
                workingCopy, 
                implMethod, 
                Collections.singleton(Modifier.PUBLIC), 
                newName, 
                workingCopy.getTreeMaker().PrimitiveType(TypeKind.VOID),
                null, null,
                workingCopy.getTreeMaker().Block(Collections.singletonList(bodyTree), false)
                );
        Trees trees = workingCopy.getTrees();
        TreePath treePath = trees.getPath(workingCopy.getCompilationUnit(), resultTree);
        implMethod = (ExecutableElement) trees.getElement(treePath);
    }
    
    public void visit(HomeMethodType hmt) {
        assert false: "session beans do not have home methods";
    }
    
    public void visit(FinderMethodType fmt) {
        assert false: "session beans do not have finder methods";
    }
    
    private String prependAndUpper(String fullName, String prefix) {
         StringBuffer sb = new StringBuffer(fullName);
         sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
         return prefix+sb.toString();
    }
}

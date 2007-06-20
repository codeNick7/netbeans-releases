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

package org.netbeans.modules.refactoring.java.plugins;

import org.netbeans.modules.refactoring.java.spi.RefactoringVisitor;
import com.sun.source.tree.*;
import javax.lang.model.element.*;
import org.netbeans.api.java.source.WorkingCopy;
import org.netbeans.modules.refactoring.java.spi.ToPhaseException;
import org.openide.util.Exceptions;

/**
 *
 * @author Jan Becicka
 */
public class CopyTransformer extends RefactoringVisitor {
    
    private String newName;
    private boolean insertImport;
    private String oldPackage;

    public CopyTransformer(WorkingCopy workingCopy, String newName, boolean insertImport, String oldPackage) {
        try {
            setWorkingCopy(workingCopy);
            this.newName = newName;
            this.insertImport = insertImport;
            this.oldPackage = oldPackage + ".*";
        } catch (ToPhaseException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Override
    public Tree visitCompilationUnit(CompilationUnitTree tree, Element p) {
        if (!workingCopy.getTreeUtilities().isSynthetic(getCurrentPath())) {
            if (insertImport) {
                Element el = workingCopy.getTrees().getElement(getCurrentPath());
                Tree tree2 = make.insertCompUnitImport(tree, 0, make.Import(make.Identifier(oldPackage), false));
                rewrite(tree, tree2);
            }
        }
        return super.visitCompilationUnit(tree, p);
    }         

    @Override
    public Tree visitClass(ClassTree tree, Element p) {
        if (!workingCopy.getTreeUtilities().isSynthetic(getCurrentPath())) {
            //if (tree.getSimpleName().toString().equals(workingCopy.getFileObject().getName())) {
            Tree nju = make.setLabel(tree, newName);
            rewrite(tree, nju);
            //}
        }
        return null;
    }
    
}

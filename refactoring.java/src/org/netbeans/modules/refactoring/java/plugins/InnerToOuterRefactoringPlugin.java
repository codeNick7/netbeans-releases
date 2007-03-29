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

import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.java.api.InnerToOuterRefactoring;
import org.netbeans.modules.refactoring.java.plugins.JavaRefactoringPlugin;
import org.netbeans.modules.refactoring.spi.RefactoringElementsBag;


/** Plugin that implements the core functionality of "inner to outer" refactoring.
 *
 * @author Martin Matula
 */
public class InnerToOuterRefactoringPlugin extends JavaRefactoringPlugin {
    /** Reference to the parent refactoring instance */
    private final InnerToOuterRefactoring refactoring;
    
    
    /** Creates a new instance of InnerToOuterRefactoringPlugin
     * @param refactoring Parent refactoring instance.
     */
    InnerToOuterRefactoringPlugin(InnerToOuterRefactoring refactoring) {
        this.refactoring = refactoring;
    }
    
//    private Collection outerReferences;

    public Problem preCheck() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Problem checkParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Problem fastCheckParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Problem prepare(RefactoringElementsBag refactoringElements) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
//    private Collection multipartIds;
//
//    /** Checks pre-conditions of the refactoring.
//     * @return Problems found or <code>null</code>.
//     */
//    public Problem preCheck() {
//        // fire operation start on the registered progress listeners (4 steps)
//        fireProgressListenerStart(AbstractRefactoring.PRE_CHECK, 4);
//        try {
//            JavaClass sourceType = refactoring.getSourceType();
//
//            if (sourceType == null) {
//                return new Problem(true, NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "ERR_InnerToOuter_MustBeInnerClass")); // NOI18N
//            }
//            
//            // check whether the element is valid
//            Problem result = isElementAvail(sourceType);
//            if (result != null) {
//                // fatal error -> don't continue with further checks
//                return result;
//            }
//            if (!CheckUtils.isElementInOpenProject(sourceType)) {
//                return new Problem(true, NbBundle.getMessage(JavaRefactoringPlugin.class, "ERR_ProjectNotOpened"));
//            }
//            
//            
//            // check whether the element is an unresolved class
//            if (sourceType instanceof UnresolvedClass) {
//                // fatal error -> return
//                return new Problem(true, NbBundle.getMessage(JavaRefactoringPlugin.class, "ERR_ElementNotAvailable")); // NOI18N
//            }
//            
//            refactoring.setClassName(sourceType.getSimpleName());
//            
//            // increase progress (step 1)
//            fireProgressListenerStep();
//            
//            // #1 - check if the class is an inner class
//            RefObject declCls = (RefObject) sourceType.refImmediateComposite();
//            if (!(declCls instanceof JavaClass) || !isNotLocal(declCls)) {
//                // fatal error -> return
//                return new Problem(true, NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "ERR_InnerToOuter_MustBeInnerClass")); // NOI18N
//            }
//            
//            // increase progress (step 2)
//            fireProgressListenerStep();
//            
//            if (Modifier.isStatic(sourceType.getModifiers())) {
//                fireProgressListenerStep();
//            } else {
//                // collect all features of the outer class
//                Set featureSet = new HashSet(), innerFeatures = new HashSet();
//                getFeatureSet((JavaClass) declCls, featureSet, new HashSet());
//                getFeatureSet(sourceType, innerFeatures, new HashSet());
//                featureSet.remove(sourceType);
//
//                // increase progress (step 3)
//                fireProgressListenerStep();
//
//                // check if any features of the outer class are referenced
//                // if so, create refactoring elements for them
//                multipartIds = new ArrayList();
//                Collection outerReferences = traverseForOuterReferences(sourceType, new ArrayList(), multipartIds, featureSet, innerFeatures, (JavaClass) declCls);
//                if (!outerReferences.isEmpty()) {
//                    this.outerReferences = outerReferences;
//                    refactoring.setReferenceName(firstLower(((JavaClass) declCls).getSimpleName()));
//                }
//            }
//
//            // all checks passed -> return null
//            return null;
//        } finally {
//            // fire operation end on the registered progress listeners
//            fireProgressListenerStop();
//        }
//    }
//    
//    public Problem fastCheckParameters() {
//        Problem result = null;
//        
//        JavaClass sourceType = refactoring.getSourceType();
//        String newName = refactoring.getClassName();
//        
//        if (!org.openide.util.Utilities.isJavaIdentifier(newName)) {
//            result = createProblem(result, true, NbBundle.getMessage(RenameRefactoring.class, "ERR_InvalidIdentifier", newName)); // NOI18N
//            return result;
//        }
//        
//        RefFeatured composite = getFutureParent();
//        
//        if (composite instanceof Resource) {
//            Resource resource = (Resource) composite;
//            FileObject primFile = JavaModel.getFileObject(resource);
//            FileObject folder = primFile.getParent();
//            FileObject[] children = folder.getChildren();
//            for (int x = 0; x < children.length; x++) {
//                if (children[x] != primFile && !children[x].isVirtual() && children[x].getName().equals(newName) && "java".equals(children[x].getExt())) { // NOI18N
//                    result = createProblem(result, true, NbBundle.getMessage(RenameRefactoring.class, "ERR_ClassClash", newName, resource.getPackageName())); // NOI18N
//                    return result;
//                }
//            }
//        } else {
//            if (((JavaClass) composite).getInnerClass(newName, false) != null) {
//                return createProblem(null, true, NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "ERR_InnerToOuter_ClassNameClash", newName));
//            }
//        }
//        
//        String fieldName = refactoring.getReferenceName();
//        if (fieldName != null) {
//            if (!org.openide.util.Utilities.isJavaIdentifier(fieldName)) {
//                result = createProblem(result, true, NbBundle.getMessage(RenameRefactoring.class, "ERR_InvalidIdentifier", fieldName)); // NOI18N
//                return result;
//            }
//            if (sourceType.getField(fieldName, false) != null) {
//                return createProblem(null, true, NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "ERR_InnerToOuter_FieldNameClash", fieldName));
//            }
//        }
//
//        return null;
//    }
//
//    public Problem checkParameters() {
//        Problem result = null;
//        fireProgressListenerStart(AbstractRefactoring.PARAMETERS_CHECK, 3);
//        try {
//            // #1 - check whether the field will not hide other field in a super class
//            String fieldName = refactoring.getReferenceName();
//            if (fieldName != null) {
//                if (refactoring.getSourceType().getField(fieldName, true) != null) {
//                    result = createProblem(result, false, NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "ERR_InnerToOuter_FieldHidden", fieldName));
//                }
//            }
//            
//            fireProgressListenerStep();
//            
//            // #2 - check if the reference to the outer class is necessary but not set
//            if (refactoring.getReferenceName() == null && outerReferences != null) {
//                result = createProblem(result, false, NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "ERR_InnerToOuter_OuterUsed"));
//            }
//            
//            fireProgressListenerStep();
//
//            // #3 - check if the class will not hide another inner class in the super of parent class (if applicable)
//            Element futureParent = getFutureParent();
//            if (futureParent instanceof JavaClass) {
//                if (((JavaClass) futureParent).getInnerClass(refactoring.getClassName(), true) != null) {
//                    result = createProblem(result, false, NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "ERR_InnerToOuter_InnerHidden", refactoring.getClassName()));
//                }
//            }
//            return result;
//        } finally {
//            fireProgressListenerStop();
//        }
//    }
//
//    public Problem prepare(RefactoringElementsBag refactoringElements) {
//        // rename class if necessary (if the new name is different from the original name)
//        if (!refactoring.getClassName().equals(refactoring.getSourceType().getSimpleName())) {
//            refactoringElements.add(refactoring, new RenameClassElement());
//        }
//        
//        // add declaration of the field pointing to the original class (if user chooses to do so)
//        // and fix all references to the outer class elements
//        if (refactoring.getReferenceName() != null) {
//            refactoringElements.add(refactoring, new AddReferenceElement());
//            if (outerReferences!=null) {
//                refactoringElements.addAll(refactoring, outerReferences);
//            }
//            // find all explicit constructor invocations and fix them
//            for (Iterator it = refactoring.getSourceType().getSubClasses().iterator(); it.hasNext();) {
//                JavaClass cls = (JavaClass) it.next();
//                Object[] contents = cls.getContents().toArray();
//                boolean constructorVisited = false;
//                for (int i = 0; i < contents.length; i++) {
//                    if (contents[i] instanceof Constructor) {
//                        constructorVisited = true;
//                        Iterator stmts = ((Constructor) contents[i]).getBody().getStatements().iterator();
//                        Object firstStatement = stmts.hasNext() ? stmts.next() : null;
//                        if (firstStatement instanceof ConstructorInvocation) {
//                            ConstructorInvocation ci = (ConstructorInvocation) firstStatement;
//                            if (ci.isHasSuper()) {
//                                refactoringElements.add(refactoring, new AddInvocationArgumentElement(ci));
//                            }
//                        } else {
//                            refactoringElements.add(refactoring, new AddConstructorInvocationElement((Constructor) contents[i]));
//                        }
//                    }
//                }
//                if (!constructorVisited) {
//                    refactoringElements.add(refactoring, new AddConstructorElement(cls));
//                }
//            }
//        }
//        
//        JavaClass origOuter = (JavaClass) refactoring.getSourceType().refImmediateComposite();
//        // move the class to the outer level
//        Element parent = getFutureParent();
//        if (parent instanceof JavaClass) {
//            refactoringElements.add(refactoring, new MoveInnerToOuterElement((JavaClass) parent));
//        } else {
//            refactoringElements.add(refactoring, new MoveInnerToTopElement(refactoring.getSourceType(), refactoring.getClassName()));
//        }
//        
//        // fix all references to other inner classes of the outer class from the class being moved
//        if (multipartIds != null) {
//            refactoringElements.addAll(refactoring, multipartIds);
//        }
//
//        // fix all the references to the inner class (including imports and new class expressions)
//        ElementReference[] classReferences = (ElementReference[]) refactoring.getSourceType().getReferences().toArray(new ElementReference[0]);
//        for (int i = 0; i < classReferences.length; i++) {
//            refactoringElements.add(refactoring, new ChangeClassReferenceElement(classReferences[i], origOuter));
//        }
//        
//        return null;
//    }
//    
//    // --- HELPER METHODS ------------------------------------------------------
//    
//    private static boolean isNotLocal(RefObject cls) {
//        while (cls instanceof JavaClass) {
//            cls = (RefObject) cls.refImmediateComposite();
//        }
//        return (cls instanceof Resource);
//    }
//    
//    private static String firstLower(String text) {
//        return text.substring(0, 1).toLowerCase() + text.substring(1);
//    }
//    
//    private static ClassDefinition getDeclaringClass(Element element) {
//        Feature result = JavaModelUtil.getDeclaringFeature(element);
//        if (!(result instanceof ClassDefinition)) {
//            return result.getDeclaringClass();
//        }
//        return (ClassDefinition) result;
//    }
//    
//    private static ClassDefinition getOuter(ClassDefinition classDefinition) {
//        Element composite = (Element) classDefinition.refImmediateComposite();
//        while (composite != null && !(composite instanceof ClassDefinition)) {
//            composite = (Element) composite.refImmediateComposite();
//        }
//        return (ClassDefinition) composite;
//    }
//
//    private Collection traverseForOuterReferences(Element element, Collection refactElements, Collection multipartIds, Set featureSet, Set innerFeatures, JavaClass outer) {
//        PrimaryExpression parentClass = null;
//        boolean check = true;
//
//        if (element instanceof MethodInvocation) {
//            parentClass = ((MethodInvocation) element).getParentClass();
//        } else if (element instanceof NewClassExpression) {
//            parentClass = ((NewClassExpression) element).getEnclosingClass();
//        } else if (element instanceof VariableAccess) {
//            parentClass = ((VariableAccess) element).getParentClass();
//        } else {
//            if (element instanceof MultipartId) {
//                MultipartId mpi = (MultipartId) element;
//                NamedElement e = mpi.getElement();
//                if (e instanceof JavaClass) {
//                    e = unwrap((JavaClass) e);
//                    if (featureSet.contains(e) && !innerFeatures.contains(e)) {
//                        multipartIds.add(new UpdateMultipartIdElement((MultipartId) element));
//                    }
//                }
//            }
//            check = false;
//        }
//        
//        if (check) {
//            NamedElement e = ((ElementReference) element).getElement();
//            if (e instanceof Constructor) {
//                e = (NamedElement) e.refImmediateComposite();
//            }
//            if (featureSet.contains(e)) {
//                Feature feature = (Feature) e;
//                ChangeElementReferenceElement re = null;
//                if (parentClass == null) {
//                    if (!innerFeatures.contains(feature)) {
//                        re = new ChangeElementReferenceElement(element);
//                    }
//                } else if (parentClass instanceof ThisExpression) {
//                    MultipartId className = ((ThisExpression) parentClass).getClassName();
//                    if (className != null && outer.equals(unwrap((JavaClass) className.getElement()))) {
//                        re = new ChangeElementReferenceElement(element);
//                    }
//                }
//                if (re != null) {
//                    refactElements.add(re);
//                    if (Modifier.isPrivate(feature.getModifiers())) {
//                        refactElements.add(new RelaxAccessRightsElement(feature));
//                    }
//                }
//            }
//        }
//
//        for (Iterator it = element.getChildren().iterator(); it.hasNext();) {
//            traverseForOuterReferences((Element) it.next(), refactElements, multipartIds, featureSet, innerFeatures, outer);
//        }
//        return refactElements;
//    }
//
//    private void getFeatureSet(JavaClass cls, Set features, Set visited) {
//        if (cls == null) return;
//        cls = unwrap(cls);
//        if (visited.add(cls)) {
//            features.addAll(cls.getFeatures());
//        }
//        getFeatureSet(cls.getSuperClass(), features, visited);
//        for (Iterator it = cls.getInterfaces().iterator(); it.hasNext();) {
//            getFeatureSet((JavaClass) it.next(), features, visited);
//        }
//    }
//
//    private static JavaClass unwrap(JavaClass cls) {
//        while (cls instanceof ParameterizedType) {
//            cls = ((ParameterizedType) cls).getDefinition();
//        }
//        return cls;
//    }
//    
//    private Element getFutureParent() {
//        return (Element) ((RefObject) refactoring.getSourceType().refImmediateComposite()).refImmediateComposite();
//    }
//    
//    private PrimaryExpression createConstrInvocArgument(ClassDefinition declCls) {
//        JavaClass sourceOuter = (JavaClass) refactoring.getSourceType().refImmediateComposite();
//        while (declCls != null && !declCls.isSubTypeOf(sourceOuter)) {
//            declCls = getOuter(declCls);
//        }
//        return ((JavaModelPackage) declCls.refImmediatePackage()).getThisExpression().createThisExpression(declCls != null && (declCls instanceof JavaClass) ? JavaModelUtil.resolveImportsForClass(declCls, (JavaClass) declCls) :  null);
//    }
//
//    private ConstructorInvocation createConstrInvoc(ClassDefinition javaClass) {
//        return ((JavaModelPackage) javaClass.refImmediatePackage()).getConstructorInvocation().createConstructorInvocation(
//            null,
//            Collections.singletonList(createConstrInvocArgument(javaClass)),
//            true,
//            null
//        );
//    }
//
//    // --- REFACTORING ELEMENTS ------------------------------------------------
//
//    private class AddInvocationArgumentElement extends WhereUsedElement {
//        AddInvocationArgumentElement(ConstructorInvocation ci) {
//            super(JavaModelUtil.getDeclaringFeature(ci), ci);
//        }
//        
//        public void performChange() {
//            JavaModelPackage jmp = (JavaModelPackage) feature.refImmediatePackage();
//            ConstructorInvocation ci = (ConstructorInvocation) feature;
//            PrimaryExpression pe = ci.getParentClass();
//            if (pe != null) {
//                ci.setParentClass(null);
//            } else {
//                pe = createConstrInvocArgument(getDeclaringClass(feature));
//            }
//            ci.getParameters().add(0, pe);
//        }
//    }
//    
//    private class UpdateMultipartIdElement extends WhereUsedElement {
//        UpdateMultipartIdElement(MultipartId mpi) {
//            super(JavaModelUtil.getDeclaringFeature(mpi), mpi);
//        }
//        
//        public void performChange() {
//            Element parent = (Element) feature.refImmediateComposite();
//            parent.replaceChild(feature, JavaModelUtil.resolveImportsForClass(feature, (JavaClass) ((MultipartId) feature).getElement()));
//        }
//    }
//    
//    private class ChangeElementReferenceElement extends WhereUsedElement {
//        ChangeElementReferenceElement(Element elementReference) {
//            super(JavaModelUtil.getDeclaringFeature(elementReference), elementReference);
//        }
//        
//        public void performChange() {
//            JavaModelPackage jmp = (JavaModelPackage) feature.refImmediatePackage();
//            VariableAccessClass vac = jmp.getVariableAccess();
//            
//            ClassDefinition cd = getDeclaringClass(feature);
//            ThisExpression te = jmp.getThisExpression().createThisExpression(refactoring.getSourceType().equals(cd) ? null : jmp.getMultipartId().createMultipartId(refactoring.getClassName(), null, null));
//            VariableAccess va = vac.createVariableAccess(refactoring.getReferenceName(), te, false);
//            
//            if (feature instanceof MethodInvocation) {
//                ((MethodInvocation) feature).setParentClass(va);
//            } else if (feature instanceof NewClassExpression) {
//                ((NewClassExpression) feature).setEnclosingClass(va);
//            } else {
//                ((VariableAccess) feature).setParentClass(va);
//            }
//        }
//    }
//    
//    private class ChangeClassReferenceElement extends WhereUsedElement {
//        private final JavaClass origOuter;
//        
//        ChangeClassReferenceElement(ElementReference elementReference, JavaClass origOuter) {
//            super(JavaModelUtil.getDeclaringFeature(elementReference), elementReference);
//            this.origOuter = origOuter;
//        }
//        
//        public void performChange() {
//            Element composite = (Element) feature.refImmediateComposite();
//            if (composite instanceof Import) {
//                composite.refDelete();
//            } else {
//                if ((composite instanceof NewClassExpression) && ((NewClassExpression) composite).getClassName().equals(feature)) {
//                    if (refactoring.getReferenceName() != null) {
//                        NewClassExpression nce = (NewClassExpression) composite;
//                        PrimaryExpression enclosing = nce.getEnclosingClass();
//                        if (enclosing != null) {
//                            nce.setEnclosingClass(null);
//                            nce.getParameters().add(0, enclosing);
//                        } else {
//                            // compute which outerclass to pass in the new instance expression:
//                            // #1 - for the current outer or its subtype pass "this"
//                            // #2 - for an inner class of the current outer (other than the inner class being moved) pass "outer.this"
//                            // #3 - for the class being moved pass "this.outerField"
//                            // #4 - for an inner class of the class being moved pass "className.this.outerField"
//                            // #5 - for an inner class of a subtype of outer class pass "outerSubtype.this"
//                            JavaModelPackage jmp = (JavaModelPackage) feature.refImmediatePackage();
//                            boolean needOuter = false;
//                            ClassDefinition outer = JavaModelUtil.getDeclaringFeature(nce).getDeclaringClass();
//                            while ((outer != null) && !isAcceptableClass(outer)) {
//                                needOuter = true;
//                                outer = getOuter(outer);
//                            }
//                            VariableAccessClass vac = jmp.getVariableAccess();
//                            ThisExpressionClass tec = jmp.getThisExpression();
//
//                            if (refactoring.getSourceType().equals(outer)) {
//                                // covers #3 and #4
//                                ThisExpression te = tec.createThisExpression(needOuter ? jmp.getMultipartId().createMultipartId(refactoring.getClassName(), null, null) : null);
//                                nce.getParameters().add(0, vac.createVariableAccess(refactoring.getReferenceName(), te, false));
//                            } else {
//                                // covers #1, #2, #5
//                                nce.getParameters().add(0, tec.createThisExpression(needOuter && (outer instanceof JavaClass) ? JavaModelUtil.resolveImportsForClass(nce, (JavaClass) outer) : null));
//                            }
//                        }
//                    }
//                }
//                composite.replaceChild(feature, JavaModelUtil.resolveImportsForClass(composite, refactoring.getSourceType()));
//            }
//        }
//
//        private boolean isAcceptableClass(ClassDefinition cls) {
//            return refactoring.getSourceType().equals(cls) || cls.isSubTypeOf(origOuter);
//        }
//    }
//
//    private class RelaxAccessRightsElement extends SimpleRefactoringElementImpl {
//        private final String text;
//        private final Feature feature;
//        
//        RelaxAccessRightsElement(Feature feature) {
//            this.feature = feature;
//            this.text = NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "TXT_InnerToOuter_RelaxAccessRights");
//        }
//        
//        public void performChange() {
//            feature.setModifiers(feature.getModifiers() & ~Modifier.PRIVATE);
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getDisplayText() {
//            return text;
//        }
//
//        public FileObject getParentFile() {
//            return JavaMetamodel.getManager().getFileObject(feature.getResource());
//        }
//
//        public Element getJavaElement() {
//            return feature;
//        }
//
//        public PositionBounds getPosition() {
//            return JavaMetamodel.getManager().getElementPosition(feature);
//        }
//    }
//
//    private class AddConstructorInvocationElement extends SimpleRefactoringElementImpl {
//        private final String text;
//        private final Constructor constr;
//        
//        AddConstructorInvocationElement(Constructor constr) {
//            this.constr = constr;
//            this.text = NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "TXT_InnerToOuter_AddConstructorInvocation");
//        }
//        
//        public void performChange() {
//            constr.getBody().getStatements().add(0, createConstrInvoc(constr.getDeclaringClass()));
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getDisplayText() {
//            return text;
//        }
//
//        public FileObject getParentFile() {
//            return JavaMetamodel.getManager().getFileObject(constr.getResource());
//        }
//
//        public Element getJavaElement() {
//            return constr;
//        }
//
//        public PositionBounds getPosition() {
//            return JavaMetamodel.getManager().getElementPosition(constr);
//        }
//    }
//
//    private class AddConstructorElement extends SimpleRefactoringElementImpl {
//        private final String text;
//        private final JavaClass parent;
//        
//        AddConstructorElement(JavaClass parent) {
//            this.parent = parent;
//            this.text = NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "TXT_InnerToOuter_AddConstructor");
//        }
//        
//        public void performChange() {
//            JavaModelPackage jmp = (JavaModelPackage) parent.refImmediatePackage();
//            Constructor constructor = jmp.getConstructor().createConstructor(null, null, Modifier.PUBLIC, null, null,
//                    jmp.getStatementBlock().createStatementBlock(Collections.singletonList(createConstrInvoc(parent))),
//                    null, null, null, null);
//            int constructorIndex = -1;
//            Object contents[] = parent.getContents().toArray();
//            for (int i = 0; i < contents.length; i++) {
//                if ((constructorIndex == -1) && !((contents[i] instanceof Field) || (contents[i] instanceof FieldGroup))) {
//                    constructorIndex = i;
//                    break;
//                }
//            }
//            parent.getContents().add(constructorIndex < 0 ? 0 : constructorIndex, constructor);
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getDisplayText() {
//            return text;
//        }
//
//        public FileObject getParentFile() {
//            return JavaMetamodel.getManager().getFileObject(parent.getResource());
//        }
//
//        public Element getJavaElement() {
//            return parent;
//        }
//
//        public PositionBounds getPosition() {
//            return JavaMetamodel.getManager().getElementPosition(parent);
//        }
//    }
//
//    private class MoveInnerToOuterElement extends SimpleRefactoringElementImpl {
//        private final String text;
//        private final JavaClass parent;
//        
//        MoveInnerToOuterElement(JavaClass parent) {
//            this.parent = parent;
//            this.text = NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "TXT_InnerToOuter_MoveToOuter", UIUtilities.getDisplayText(parent));
//        }
//        
//        public void performChange() {
//            JavaClass cls = refactoring.getSourceType();
//            ((Element) cls.refImmediateComposite()).replaceChild(cls, null);
//            parent.getContents().add(cls);
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getDisplayText() {
//            return text;
//        }
//
//        public FileObject getParentFile() {
//            return JavaMetamodel.getManager().getFileObject(refactoring.getSourceType().getResource());
//        }
//
//        public Element getJavaElement() {
//            return (Element) JavaModelUtil.getDeclaringFeature(refactoring.getSourceType());
//        }
//
//        public PositionBounds getPosition() {
//            return JavaMetamodel.getManager().getElementPosition(refactoring.getSourceType());
//        }
//    }
//
//    private static class MoveInnerToTopElement extends SimpleRefactoringElementImpl {
//        private final String text;
//        private final JavaClass sourceType;
//        private final String className;
//        private FileObject newClsFO;
//        
//        MoveInnerToTopElement(JavaClass sourceType, String className) {
//            this.text = NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "TXT_InnerToOuter_MoveToTop");
//            this.sourceType = sourceType;
//            this.className = className;
//        }
//        
//        public void performChange() {
//            ExternalChange ec = new ExternalChange() {
//                private FileSystem fs;
//                private String folderName;
//                
//                public void performExternalChange() {
//                    try {
//                        FileObject tempFO = Repository.getDefault().getDefaultFileSystem().findResource("Templates/Classes/Empty.java"); // NOI18N
//                        
//                        FileObject folderFO;
//                        if (fs == null) {
//                            FileObject sourceFO = JavaModel.getFileObject(sourceType.getResource());
//                            folderFO = sourceFO.getParent();
//                            folderName = folderFO.getPath();
//                            fs = folderFO.getFileSystem();
//                        } else {
//                            folderFO = fs.findResource(folderName);
//                        }
//                            
//                        DataFolder folder = (DataFolder) DataObject.find(folderFO);
//                        DataObject template = DataObject.find(tempFO);
//                        DataObject newClsDO = template.createFromTemplate(folder, className);
//                        newClsFO = newClsDO.getPrimaryFile();
//                    } catch (DataObjectNotFoundException e) {
//                        ErrorManager.getDefault().notify(e);
//                    } catch (IOException e) {
//                        ErrorManager.getDefault().notify(e);
//                    }
//                }
//                
//                public void undoExternalChange() {
//                    try {
//                        DataObject newClsDO = DataObject.find(newClsFO);
//                        newClsDO.delete();
//                    } catch (DataObjectNotFoundException e) {
//                        ErrorManager.getDefault().notify(e);
//                    } catch (IOException e) {
//                        ErrorManager.getDefault().notify(e);
//                    }
//                    newClsFO = null;
//                }
//            };
//            
//            // create the new file
//            ec.performExternalChange();
//            
//            // move the class into it
//            String packageName = sourceType.getResource().getPackageName();
//            Resource clsResource = JavaModel.getResource(newClsFO);
//            if (packageName != null && packageName.length() > 0) {
//                clsResource.setPackageName(packageName);
//            }
//            ((Element) sourceType.refImmediateComposite()).replaceChild(sourceType, null);
//            sourceType.setModifiers(sourceType.getModifiers() & ~(Modifier.STATIC | Modifier.PRIVATE));
//            clsResource.getClassifiers().add(sourceType);
//            ((MetadataElement) sourceType).fixImports(sourceType, sourceType);
//            
//            // register undo element
//            JavaMetamodel.getManager().registerUndoElement(ec);
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getDisplayText() {
//            return text;
//        }
//
//        public FileObject getParentFile() {
//            return JavaMetamodel.getManager().getFileObject(sourceType.getResource());
//        }
//
//        public Element getJavaElement() {
//            return (Element) JavaModelUtil.getDeclaringFeature(sourceType);
//        }
//
//        public PositionBounds getPosition() {
//            return JavaMetamodel.getManager().getElementPosition(sourceType);
//        }
//    }
//
//    private class RenameClassElement extends SimpleRefactoringElementImpl {
//        private final String text;
//        
//        RenameClassElement() {
//            this.text = NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "TXT_InnerToOuter_Rename", refactoring.getClassName());
//        }
//        
//        public void performChange() {
//            refactoring.getSourceType().setSimpleName(refactoring.getClassName());
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getDisplayText() {
//            return text;
//        }
//
//        public FileObject getParentFile() {
//            return JavaMetamodel.getManager().getFileObject(refactoring.getSourceType().getResource());
//        }
//
//        public Element getJavaElement() {
//            return (Element) JavaModelUtil.getDeclaringFeature(refactoring.getSourceType());
//        }
//
//        public PositionBounds getPosition() {
//            return JavaMetamodel.getManager().getElementPosition(refactoring.getSourceType());
//        }
//    }
//
//    private class AddReferenceElement extends SimpleRefactoringElementImpl {
//        private final String text;
//        private final JavaClass parent;
//        
//        AddReferenceElement() {
//            this.text = NbBundle.getMessage(InnerToOuterRefactoringPlugin.class, "TXT_InnerToOuter_AddReference", refactoring.getReferenceName());
//            this.parent = (JavaClass) refactoring.getSourceType().refImmediateComposite();
//        }
//        
//        public void performChange() {
//            JavaClass cls = refactoring.getSourceType();
//            String paramName = refactoring.getReferenceName();
//            JavaModelPackage jmp = (JavaModelPackage) cls.refImmediatePackage();
//            TypeReference typeName = JavaModelUtil.resolveImportsForClass(cls, parent);
//            Field field = jmp.getField().createField(paramName, null, Modifier.PRIVATE | Modifier.FINAL,
//                    null, null, true, typeName, 0, null, null);
//            cls.getContents().add(0, field);
//            
//            Object[] contents = (Object[]) cls.getContents().toArray(new ClassMember[0]);
//            int constructorIndex = -1;
//            Parameter param = jmp.getParameter().createParameter(paramName, null, false, 
//                                (TypeReference) typeName.duplicate(), 0, false);
//            ExpressionStatement assignment = jmp.getExpressionStatement().createExpressionStatement(
//                    jmp.getAssignment().createAssignment(
//                        jmp.getVariableAccess().createVariableAccess(paramName, jmp.getThisExpression().createThisExpression(), false),
//                        OperatorEnum.ASSIGN,
//                        jmp.getVariableAccess().createVariableAccess(paramName, null, false)
//                        )
//                    );
//            boolean constructorVisited = false;
//            JavaClass superType = cls.getSuperClass();
//            boolean superIsInner = (superType.refImmediateComposite() instanceof JavaClass) && !Modifier.isStatic(parent.getModifiers());
//            for (int i = 0; i < contents.length; i++) {
//                if (contents[i] instanceof Constructor) {
//                    constructorVisited = true;
//                    Constructor constr = (Constructor) contents[i];
//                    constr.getParameters().add(0, duplicateIfNecessary(param));
//                    Iterator it = constr.getBody().getStatements().iterator();
//                    Object firstStatement = it.hasNext() ? it.next() : null;
//                    if ((firstStatement instanceof ConstructorInvocation) && (((ConstructorInvocation) firstStatement).getParentClass() == null)) {
//                        ConstructorInvocation ci = (ConstructorInvocation) firstStatement;
//                        if (ci.isHasSuper()) {
//                            if (superIsInner) {
//                                ci.setParentClass(jmp.getVariableAccess().createVariableAccess(paramName, null, false));
//                            }
//                            constr.getBody().getStatements().add(1, duplicateIfNecessary(assignment));
//                        } else {
//                            ci.getParameters().add(0, jmp.getVariableAccess().createVariableAccess(paramName, null, false));
//                        }
//                    } else {
//                        constr.getBody().getStatements().add(0, duplicateIfNecessary(assignment));
//                        if (superIsInner) {
//                            constr.getBody().getStatements().add(0, jmp.getConstructorInvocation().createConstructorInvocation(
//                                    null, null, true, jmp.getVariableAccess().createVariableAccess(paramName, null, false)));
//                        }
//                    }
//                } else if ((param == null) && (constructorIndex == -1) && !((contents[i] instanceof Field) || (contents[i] instanceof FieldGroup))) {
//                    constructorIndex = i;
//                }
//            }
//            if (!constructorVisited) {
//                Constructor constr = jmp.getConstructor().createConstructor(
//                        null, null, Modifier.PUBLIC, null, null,
//                        jmp.getStatementBlock().createStatementBlock(Collections.singletonList(assignment)),
//                        null,
//                        null,
//                        Collections.singletonList(param),
//                        null
//                        );
//                cls.getContents().add(constructorIndex < 0 ? 0 : constructorIndex, constr);
//            }
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getDisplayText() {
//            return text;
//        }
//
//        public FileObject getParentFile() {
//            return JavaMetamodel.getManager().getFileObject(refactoring.getSourceType().getResource());
//        }
//
//        public Element getJavaElement() {
//            return (Element) JavaModelUtil.getDeclaringFeature(refactoring.getSourceType());
//        }
//
//        public PositionBounds getPosition() {
//            return JavaMetamodel.getManager().getElementPosition(refactoring.getSourceType());
//        }
//
//        private Element duplicateIfNecessary(Element element) {
//            if (element.refImmediateComposite() != null) {
//                element = element.duplicate();
//            }
//            return element;
//        }
//    }
}

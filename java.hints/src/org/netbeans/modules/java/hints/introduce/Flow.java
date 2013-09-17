/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2011 Sun
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

package org.netbeans.modules.java.hints.introduce;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.util.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.CompilationInfo.CacheClearPolicy;
import org.netbeans.api.java.source.support.CancellableTreePathScanner;
import org.netbeans.modules.java.hints.errors.Utilities;
import org.netbeans.spi.java.hints.HintContext;

/**
 *
 * @author lahvac
 */
public class Flow {

    public static FlowResult assignmentsForUse(CompilationInfo info, AtomicBoolean cancel) {
        return assignmentsForUse(info, new AtomicBooleanCancel(cancel));
    }

    public static FlowResult assignmentsForUse(CompilationInfo info, TreePath from, AtomicBoolean cancel) {
        return assignmentsForUse(info, from, new AtomicBooleanCancel(cancel));
    }

    public static FlowResult assignmentsForUse(final HintContext ctx) {
        return Flow.assignmentsForUse(ctx.getInfo(), new Cancel() {
            @Override
            public boolean isCanceled() {
                return ctx.isCanceled();
            }
        });
    }
    
    private static final Object KEY_FLOW = new Object();
    
    public static FlowResult assignmentsForUse(CompilationInfo info, Cancel cancel) {
        FlowResult result = (FlowResult) info.getCachedValue(KEY_FLOW);
        
        if (result == null) {
            result = assignmentsForUse(info, new TreePath(info.getCompilationUnit()), cancel);
            
            if (result != null && !cancel.isCanceled()) {
                info.putCachedValue(KEY_FLOW, result, CacheClearPolicy.ON_TASK_END);
            }
        }
        
        return result;
    }
    
    public static FlowResult assignmentsForUse(CompilationInfo info, TreePath from, Cancel cancel) {
        Map<Tree, Iterable<? extends TreePath>> result = new HashMap<Tree, Iterable<? extends TreePath>>();
        VisitorImpl v = new VisitorImpl(info, null, cancel);

        v.scan(from, null);

        if (cancel.isCanceled()) return null;

        for (Entry<Tree, State> e : v.use2Values.entrySet()) {
            result.put(e.getKey(), e.getValue() != null ? e.getValue().assignments : Collections.<TreePath>emptyList());
        }

        v.deadBranches.remove(null);
        
        Set<Element> fc = v.finalCandidates;
        // PENDING: optimize case with empty fc
        Set<VariableElement> finalCandidates = new HashSet<VariableElement>(fc.size());
        for (Element e : fc) {
            if (SUPPORTED_VARIABLES.contains(e.getKind())) {
                finalCandidates.add((VariableElement)e);
            }
        }
        
        finalCandidates.removeAll(v.usedWhileUndefined);

        return new FlowResult(Collections.unmodifiableMap(result), Collections.unmodifiableSet(v.deadBranches), Collections.unmodifiableSet(finalCandidates));
    }
    
    public static final class FlowResult {
        private final Map<Tree, Iterable<? extends TreePath>> assignmentsForUse;
        private final Set<? extends Tree> deadBranches;
        private final Set<VariableElement> finalCandidates;
        private FlowResult(Map<Tree, Iterable<? extends TreePath>> assignmentsForUse, Set<Tree> deadBranches, Set<VariableElement> finalCandidates) {
            this.assignmentsForUse = assignmentsForUse;
            this.deadBranches = deadBranches;
            this.finalCandidates = finalCandidates;
        }
        public Map<Tree, Iterable<? extends TreePath>> getAssignmentsForUse() {
            return assignmentsForUse;
        }
        public Set<? extends Tree> getDeadBranches() {
            return deadBranches;
        }
        public Set<VariableElement> getFinalCandidates() {
            return finalCandidates;
        }
    }

    public interface Cancel {
        public boolean isCanceled();
    }

    public static final class AtomicBooleanCancel implements Cancel {

        private final AtomicBoolean cancel;

        public AtomicBooleanCancel(AtomicBoolean cancel) {
            this.cancel = cancel;
        }

        @Override
        public boolean isCanceled() {
            return cancel.get();
        }

    }

    public static boolean definitellyAssigned(CompilationInfo info, VariableElement var, Iterable<? extends TreePath> trees, AtomicBoolean cancel) {
        return definitellyAssigned(info, var, trees, new AtomicBooleanCancel(cancel));
    }

    /**
     * This is a variant of {@link #definitellyAssigned(org.netbeans.api.java.source.CompilationInfo, javax.lang.model.element.VariableElement, java.lang.Iterable, java.util.concurrent.atomic.AtomicBoolean)},
     * that allows to inspect a pseudo-variable that corresponds to an undefined symbol.
     * 
     * @param scope scope where the undefined symbol will be created. It's used to identify
     * aliases in nested classes.
     * @return true, if the 'var' symbol is definitely assigned.
     */
    public static boolean unknownSymbolFinalCandidate(CompilationInfo info, 
            Element var, TypeElement scope, Iterable<? extends TreePath> trees, AtomicBoolean cancel) {
        return definitellyAssignedImpl(info, var, scope, trees, false, new AtomicBooleanCancel(cancel));
    }
    
    private static boolean definitellyAssignedImpl(CompilationInfo info, 
            Element var, TypeElement scope, 
            Iterable<? extends TreePath> trees, boolean reassignAllowed, Cancel cancel) {
        VisitorImpl v = new VisitorImpl(info, scope, cancel);
        if (scope != null) {
            v.canonicalUndefined(var);
        }
        v.variable2State.put(var, State.create(null, false));

        for (TreePath tp : trees) {
            if (cancel.isCanceled()) return false;
            
            v.scan(tp, null);
            
            TreePath toResume = tp;
            
            while (toResume != null) {
                v.resume(toResume.getLeaf(), v.resumeAfter);
                toResume = toResume.getParentPath();
            }

            State s = v.variable2State.get(var);
            if (!s.assignments.contains(null)) {
                return reassignAllowed || !s.reassigned;
            }
        }

        return false;
    }

    public static boolean definitellyAssigned(CompilationInfo info, VariableElement var, Iterable<? extends TreePath> trees, Cancel cancel) {
        return definitellyAssignedImpl(info, var, null, trees, true, cancel);
    }

    /**
     * Unresolved variable analysis: currently only the definitive assignment analysis can actually work
     * with undefined symbols. First the {@link #undefinedSymbolScope} is set up. Each L-value unresolved symbol
     * without a qualifier is thought of as belonging to that scope - symbols are aliased by name. Unresolved symbols
     * that are qualified to belong to other scopes are ignored at the moment.
     * 
     */
    private static final class VisitorImpl extends CancellableTreePathScanner<Boolean, ConstructorData> {
        
        private final CompilationInfo info;
        private final TypeElement undefinedSymbolScope;
        // just an optimization for comparisons
        private final Name thisName;
        /**
         * Undefined variables found in the {@link #undefinedSymbolScope}. Does not contain entries
         * for undefined variables in other scopes.
         */
        private Map<Name, Element> undefinedVariables = new HashMap<Name, Element>();
        private Map<Element, State> variable2State = new HashMap<Element, Flow.State>();
        private Map<Tree, State> use2Values = new IdentityHashMap<Tree, State>();
        private Map<Tree, Collection<Map<Element, State>>> resumeBefore = new IdentityHashMap<Tree, Collection<Map<Element, State>>>();
        private Map<Tree, Collection<Map<Element, State>>> resumeAfter = new IdentityHashMap<Tree, Collection<Map<Element, State>>>();
        private Map<TypeMirror, Collection<Map<Element, State>>> resumeOnExceptionHandler = new IdentityHashMap<TypeMirror, Collection<Map<Element, State>>>();
        private boolean inParameters;
        private Tree nearestMethod;
        private Set<Element> currentMethodVariables = Collections.newSetFromMap(new IdentityHashMap<Element, Boolean>());
        private final Set<Tree> deadBranches = new HashSet<Tree>();
        private final List<TreePath> pendingFinally = new LinkedList<TreePath>();
        private final Cancel cancel;
        private boolean doNotRecord;
        private /*Map<ClassTree, */Set<Element> finalCandidates = new ReluctantSet<>();
        private final Set<Element> usedWhileUndefined = new HashSet<Element>();

        /**
         * The target type for a qualified L-value reference. Null for unqualified symbols.
         * Undefined (null or non-null) in other cases.
         */
        private TypeElement referenceTarget;

        /**
         * Blocks 'use' marks for visited variables; indicates that lValue is being processed.
         */
        private boolean lValueDereference;

        public VisitorImpl(CompilationInfo info, TypeElement undefinedSymbolScope, Cancel cancel) {
            super();
            this.info = info;
            this.cancel = cancel;
            this.undefinedSymbolScope = undefinedSymbolScope;
            this.thisName = info.getElements().getName("this");
        }

        @Override
        protected boolean isCanceled() {
            return cancel.isCanceled();
        }

        @Override
        public Boolean scan(Tree tree, ConstructorData p) {
            resume(tree, resumeBefore);
            
            Boolean result = super.scan(tree, p);

            resume(tree, resumeAfter);

            return result;
        }

        private void resume(Tree tree, Map<Tree, Collection<Map<Element, State>>> resume) {
            Collection<Map<Element, State>> toResume = resume.remove(tree);

            if (toResume != null) {
                for (Map<Element, State> s : toResume) {
                    variable2State = mergeOr(variable2State, s);
                }
            }
        }

        @Override
        public Boolean visitAssignment(AssignmentTree node, ConstructorData p) {
            TypeElement oldQName = this.referenceTarget;
            this.referenceTarget = null;
            lValueDereference = true;
            scan(node.getVariable(), null);
            lValueDereference = false;

            Boolean constVal = scan(node.getExpression(), p);

            Element e = info.getTrees().getElement(new TreePath(getCurrentPath(), node.getVariable()));
            
            if (e != null) {
                if (SUPPORTED_VARIABLES.contains(e.getKind())) {
                    variable2State.put(e, State.create(new TreePath(getCurrentPath(), node.getExpression()), variable2State.get(e)));
                } else if (shouldProcessUndefined(e)) {
                    Element cv = canonicalUndefined(e);
                    variable2State.put(cv, State.create(new TreePath(getCurrentPath(), node.getExpression()), variable2State.get(cv)));
                }
            }
            this.referenceTarget = oldQName;
            return constVal;
        }
        
        /**
         * Returns an alias for the Element, if the undefined name was already found. Returns e
         * and causes further names like 'e' to be aliased to e instance. This cannonicalization is
         * used to support collection operations throughout the flow
         */
        private Element canonicalUndefined(Element e) {
            Name n = e.getSimpleName();
            Element prev = undefinedVariables.get(n);
            if (prev != null) {
                return prev;
            } else {
                undefinedVariables.put(n, e);
                return e;
            }
        }
        
        /**
         * Determines if the Element should be processed as an undefined variable. Currently the implementation
         * only returns true if an undefinedSymbolScope is set, AND the undefined symbol might belong to that scope.
         * If the undefined symbol is qualified (by this. or class.this. or whatever else manner), the method will
         * only return true if the qualifier corresponds to the undefinedSymbolScope.
         * 
         */
        private boolean shouldProcessUndefined(Element e) {
            if (e == null || undefinedSymbolScope == null ||
                e.asType().getKind() != TypeKind.ERROR) {
                return false;
            }
            if (e.getKind() == ElementKind.CLASS) {
                return referenceTarget == null || 
                    referenceTarget == undefinedSymbolScope;
            }
            return false;
        }
        
        @Override
        public Boolean visitCompoundAssignment(CompoundAssignmentTree node, ConstructorData p) {
            TypeElement oldQName = this.referenceTarget;
            this.referenceTarget = null;

            lValueDereference = true;
            scan(node.getVariable(), null);
            lValueDereference = false;

            Boolean constVal = scan(node.getExpression(), p);

            Element e = info.getTrees().getElement(new TreePath(getCurrentPath(), node.getVariable()));

            if (e != null) {
                if (SUPPORTED_VARIABLES.contains(e.getKind())) {
                    VariableElement ve = (VariableElement) e;
                    State prevState = variable2State.get(ve);
                    if (LOCAL_VARIABLES.contains(e.getKind())) {
                        use2Values.put(node.getVariable(), prevState); //XXX
                    } else if (e.getKind() == ElementKind.FIELD && prevState != null && prevState.hasUnassigned() && !finalCandidates.contains(ve)) {
                        usedWhileUndefined.add(ve);
                    }
                    variable2State.put(ve, State.create(getCurrentPath(), prevState));
                } else if (shouldProcessUndefined(e)) {
                    Element cv = canonicalUndefined(e);
                    State prevState = variable2State.get(cv);
                    variable2State.put(cv, State.create(getCurrentPath(), prevState));
                }
            }

            this.referenceTarget = oldQName;
            return constVal;
        }

        @Override
        public Boolean visitVariable(VariableTree node, ConstructorData p) {
            super.visitVariable(node, p);

            Element e = info.getTrees().getElement(getCurrentPath());
            
            if (e != null && SUPPORTED_VARIABLES.contains(e.getKind())) {
                variable2State.put((Element) e, State.create(node.getInitializer() != null ? new TreePath(getCurrentPath(), node.getInitializer()) : inParameters ? getCurrentPath() : null, variable2State.get(e)));
                currentMethodVariables.add((Element) e);
            }
            
            return null;
        }
        
        @Override
        public Boolean visitMemberSelect(MemberSelectTree node, ConstructorData p) {
            boolean lVal = lValueDereference;
            // selector is only read despite the member select is on the assignment's left,
            // but keep for recursive MEMBER_SELECT to reach the innermost tree.
            if (node.getExpression().getKind() != Tree.Kind.MEMBER_SELECT) {
                lValueDereference = false;
            }
            if (lVal) {
                // reset the target in top-down direction, the innermost select will populate it.
                referenceTarget = null;
            }
            super.visitMemberSelect(node, p);
            lValueDereference = lVal;
            
            // for LValue reference, note the target class' Name
            if (lVal && referenceTarget == null) {
                // TODO: intern Name in constructor, compare using ==
                Element e = null;
                
                if (node.getIdentifier() == thisName) {
                    // class.this
                    e = info.getTrees().getElement(getCurrentPath());
                } else if (node.getExpression().getKind() == Tree.Kind.IDENTIFIER) {
                    e = info.getTrees().getElement(
                            new TreePath(getCurrentPath(), node.getExpression()));
                    if (((IdentifierTree)node.getExpression()).getName() == thisName) {
                        // this.whatever
                        if (e != null) {
                            e = e.getEnclosingElement();
                        }
                    }
                }
                if (e != null && (e.getKind().isClass() || e.getKind().isInterface())) {
                    referenceTarget = (TypeElement)e;
                }
            }
            handleCurrentAccess();
            return null;
        }
        
        private void handleCurrentAccess() {
            if (lValueDereference) {
                return;
            }
            Element e = info.getTrees().getElement(getCurrentPath());

            if (e != null && SUPPORTED_VARIABLES.contains(e.getKind())) {
                VariableElement ve = (VariableElement) e;
                State prevState = variable2State.get(ve);
                
                if (LOCAL_VARIABLES.contains(e.getKind())) {
                    use2Values.put(getCurrentPath().getLeaf(), prevState);
                } else if (e.getKind() == ElementKind.FIELD && (prevState == null || prevState.hasUnassigned()) && !finalCandidates.contains(ve)) {
                    usedWhileUndefined.add(ve);
                }
            }
        }

        @Override
        public Boolean visitLiteral(LiteralTree node, ConstructorData p) {
            Object val = node.getValue();

            if (val instanceof Boolean) {
                return (Boolean) val;
            } else {
                return null;
            }
        }

        @Override
        public Boolean visitIf(IfTree node, ConstructorData p) {
            generalizedIf(node.getCondition(), node.getThenStatement(), node.getElseStatement() != null ? Collections.singletonList(node.getElseStatement()) : Collections.<Tree>emptyList(), true);
            return null;
        }
        
        public void generalizedIf(Tree condition, Tree thenSection, Iterable<? extends Tree> elseSection, boolean realElse) {
            Boolean result = scan(condition, null);

            if (result != null) {
                if (result) {
                    scan(thenSection, null);
                    if (realElse && elseSection.iterator().hasNext())
                        deadBranches.add(elseSection.iterator().next());
                } else {
                    scan(elseSection, null);
                    deadBranches.add(thenSection);
                }

                return ;
            }

            Map<Element, State> oldVariable2State = variable2State;
            
            variable2State = new HashMap<Element, Flow.State>(oldVariable2State);

            scan(thenSection, null);
            
            Map<Element, State> variableStatesAfterThen = new HashMap<Element, Flow.State>(variable2State);

            variable2State = new HashMap<Element, Flow.State>(oldVariable2State);

            scan(elseSection, null);

            variable2State = mergeOr(variable2State, variableStatesAfterThen);
        }

        @Override
        public Boolean visitBinary(BinaryTree node, ConstructorData p) {
            Boolean left = scan(node.getLeftOperand(), p);

            if (left != null && (node.getKind() == Kind.CONDITIONAL_AND || node.getKind() == Kind.CONDITIONAL_OR)) {
                if (left) {
                    if (node.getKind() == Kind.CONDITIONAL_AND) {
                        return scan(node.getRightOperand(), p);
                    } else {
                        return true;
                    }
                } else {
                    if (node.getKind() == Kind.CONDITIONAL_AND) {
                        return false;
                    } else {
                        return scan(node.getRightOperand(), p);
                    }
                }
            }

            Map<Element, State> oldVariable2State = variable2State;

            variable2State = new HashMap<Element, Flow.State>(oldVariable2State);
            
            Boolean right = scan(node.getRightOperand(), p);

            variable2State = mergeOr(variable2State, oldVariable2State);

            if (left == null || right == null) {
                return null;
            }

            switch (node.getKind()) {
                case AND: case CONDITIONAL_AND: return left && right;
                case OR: case CONDITIONAL_OR: return left || right;
                case EQUAL_TO: return left == right;
                case NOT_EQUAL_TO: return left != right;
            }
            
            return null;
        }

        @Override
        public Boolean visitConditionalExpression(ConditionalExpressionTree node, ConstructorData p) {
            Boolean result = scan(node.getCondition(), p);

            if (result != null) {
                if (result) {
                    scan(node.getTrueExpression(), null);
                } else {
                    scan(node.getFalseExpression(), null);
                }

                return null;
            }

            Map<Element, State> oldVariable2State = variable2State;

            variable2State = new HashMap<Element, Flow.State>(oldVariable2State);

            scan(node.getTrueExpression(), null);

            if (node.getFalseExpression() != null) {
                Map<Element, State> variableStatesAfterThen = new HashMap<Element, Flow.State>(variable2State);

                variable2State = new HashMap<Element, Flow.State>(oldVariable2State);

                scan(node.getFalseExpression(), null);

                variable2State = mergeOr(variable2State, variableStatesAfterThen);
            } else {
                variable2State = mergeOr(variable2State, oldVariable2State);
            }

            return null;
        }

        @Override
        public Boolean visitIdentifier(IdentifierTree node, ConstructorData p) {
            super.visitIdentifier(node, p);
            handleCurrentAccess();
            return null;
        }

        @Override
        public Boolean visitUnary(UnaryTree node, ConstructorData p) {
            Boolean val = super.visitUnary(node, p);

            if (val != null && node.getKind() == Kind.LOGICAL_COMPLEMENT) {
                return !val;
            }

            if (    node.getKind() == Kind.PREFIX_DECREMENT
                 || node.getKind() == Kind.PREFIX_INCREMENT
                 || node.getKind() == Kind.POSTFIX_DECREMENT
                 || node.getKind() == Kind.POSTFIX_INCREMENT) {
                Element e = info.getTrees().getElement(new TreePath(getCurrentPath(), node.getExpression()));

                if (e != null) {
                    if (SUPPORTED_VARIABLES.contains(e.getKind())) {
                        VariableElement ve = (VariableElement) e;
                        State prevState = variable2State.get(ve);

                        if (LOCAL_VARIABLES.contains(e.getKind())) {
                            use2Values.put(node.getExpression(), prevState);
                        } else if (e.getKind() == ElementKind.FIELD && prevState != null && prevState.hasUnassigned() && !finalCandidates.contains(ve)) {
                            usedWhileUndefined.add(ve);
                        }
                        variable2State.put(ve, State.create(getCurrentPath(), prevState));
                    } else if (shouldProcessUndefined(e)) {
                        Element cv = canonicalUndefined(e);
                        State prevState = variable2State.get(cv);
                        variable2State.put(cv, State.create(getCurrentPath(), prevState));
                    }
                }
            }


            return null;
        }

        @Override
        public Boolean visitMethod(MethodTree node, ConstructorData p) {
            Tree oldNearestMethod = nearestMethod;
            Set<Element> oldCurrentMethodVariables = currentMethodVariables;
            Map<TypeMirror, Collection<Map<Element, State>>> oldResumeOnExceptionHandler = resumeOnExceptionHandler;
            Map<Element, State> oldVariable2State = variable2State;

            nearestMethod = node;
            currentMethodVariables = Collections.newSetFromMap(new IdentityHashMap<Element, Boolean>());
            resumeOnExceptionHandler = new IdentityHashMap<TypeMirror, Collection<Map<Element, State>>>();
            variable2State = new HashMap<>(variable2State);
            
            for (Iterator<Entry<Element, State>> it = variable2State.entrySet().iterator(); it.hasNext();) {
                Entry<Element, State> e = it.next();
                
                if (e.getKey().getKind().isField()) it.remove();
            }
            
            try {
                scan(node.getModifiers(), null);
                scan(node.getReturnType(), null);
                scan(node.getTypeParameters(), null);

                inParameters = true;

                try {
                    scan(node.getParameters(), null);
                } finally {
                    inParameters = false;
                }

                scan(node.getThrows(), null);
                
                List<Tree> additionalTrees = p != null ? p.initializers : Collections.<Tree>emptyList();
                handleInitializers(additionalTrees);
                
                scan(node.getBody(), null);
                scan(node.getDefaultValue(), null);
            
                //constructor check:
                boolean isConstructor = isConstructor(getCurrentPath());
                Set<Element> definitellyAssignedOnce = new HashSet<Element>();
                Set<Element> assigned = new HashSet<Element>();
                Element methodEl = info.getTrees().getElement(getCurrentPath());
                Element classEl = methodEl != null ? methodEl.getEnclosingElement() : null;

                for (Iterator<Entry<Element, State>> it = variable2State.entrySet().iterator(); it.hasNext();) {
                    Entry<Element, State> e = it.next();

                    if (e.getKey().getKind() == ElementKind.FIELD) {
                        if (isConstructor && !e.getValue().hasUnassigned() && !e.getValue().reassigned && !e.getKey().getModifiers().contains(Modifier.STATIC) && e.getKey().getEnclosingElement().equals(classEl)) {
                            definitellyAssignedOnce.add(e.getKey());
                        }

                        assigned.add(e.getKey());

                        it.remove();
                    }
                }

                if (isConstructor && p != null/*IntroduceHint may bypass visitClass - TODO: test in FlowTest missing for this*/) {
                    if (p.first) {
                        finalCandidates.addAll(definitellyAssignedOnce);
                    } else {
                        finalCandidates.retainAll(definitellyAssignedOnce);
                    }
                    
                    for (Element var : assigned) {
                        if (var.getModifiers().contains(Modifier.STATIC) || !definitellyAssignedOnce.contains(var)) {
                            finalCandidates.remove(var);
                        }
                    }
                } else {
                    finalCandidates.removeAll(assigned);
                }
            } finally {
                nearestMethod = oldNearestMethod;
                currentMethodVariables = oldCurrentMethodVariables;
                resumeOnExceptionHandler = oldResumeOnExceptionHandler;
                variable2State = mergeOr(variable2State, oldVariable2State, false);
            }
            
            return null;
        }
        
        private boolean isConstructor(TreePath what) {
            return what.getLeaf().getKind() == Kind.METHOD && ((MethodTree) what.getLeaf()).getReturnType() == null; //TODO: not really a proper way to detect constructors
        }

        @Override
        public Boolean visitWhileLoop(WhileLoopTree node, ConstructorData p) {
            return handleGeneralizedForLoop(null, node.getCondition(), null, node.getStatement(), node.getCondition(), p);
        }

        @Override
        public Boolean visitDoWhileLoop(DoWhileLoopTree node, ConstructorData p) {
            Map< Element, State> beforeLoop = variable2State;

            variable2State = new HashMap< Element, Flow.State>(beforeLoop);

            scan(node.getStatement(), null);
            Boolean condValue = scan(node.getCondition(), null);

            if (condValue != null) {
                if (condValue) {
                    //XXX: handle possibly infinite loop
                } else {
                    //will not run more than once, skip:
                    return null;
                }
            }

            variable2State = mergeOr(beforeLoop, variable2State);

            if (!doNotRecord) {
                boolean oldDoNotRecord = doNotRecord;
                doNotRecord = true;
                
                beforeLoop = new HashMap< Element, State>(variable2State);
                scan(node.getStatement(), null);
                scan(node.getCondition(), null);
                
                doNotRecord = oldDoNotRecord;
                variable2State = beforeLoop;
            }

            return null;
        }

        @Override
        public Boolean visitForLoop(ForLoopTree node, ConstructorData p) {
            return handleGeneralizedForLoop(node.getInitializer(), node.getCondition(), node.getUpdate(), node.getStatement(), node.getCondition(), p);
        }
        
        private Boolean handleGeneralizedForLoop(Iterable<? extends Tree> initializer, Tree condition, Iterable<? extends Tree> update, Tree statement, Tree resumeOn, ConstructorData p) {
            scan(initializer, null);
            
            Map< Element, State> beforeLoop = variable2State;

            variable2State = new HashMap< Element, Flow.State>(beforeLoop);

            Boolean condValue = scan(condition, null);

            if (condValue != null) {
                if (condValue) {
                    //XXX: handle possibly infinite loop
                } else {
                    //will not run at all, skip:
                    return null;
                }
            }
            
            if (!doNotRecord) {
                boolean oldDoNotRecord = doNotRecord;
                doNotRecord = true;

                scan(statement, null);
                scan(update, null);

                variable2State = mergeOr(beforeLoop, variable2State);
                resume(resumeOn, resumeBefore);
                beforeLoop = new HashMap< Element, State>(variable2State);
                scan(condition, null);
                doNotRecord = oldDoNotRecord;
            }

            scan(statement, null);
            scan(update, null);

            variable2State = mergeOr(beforeLoop, variable2State);

            return null;
        }

        public Boolean visitTry(TryTree node, ConstructorData p) {
            if (node.getFinallyBlock() != null) {
                pendingFinally.add(0, new TreePath(getCurrentPath(), node.getFinallyBlock()));
            }
            
            scan(node.getResources(), null);

            Map< Element, State> oldVariable2State = variable2State;

            variable2State = new HashMap< Element, Flow.State>(oldVariable2State);

            scan(node.getBlock(), null);

            HashMap< Element, State> afterBlockVariable2State = new HashMap< Element, Flow.State>(variable2State);

            for (CatchTree ct : node.getCatches()) {
                Map< Element, State> variable2StateBeforeCatch = variable2State;

                variable2State = new HashMap< Element, Flow.State>(oldVariable2State);

                if (ct.getParameter() != null) {
                    TypeMirror caught = info.getTrees().getTypeMirror(new TreePath(getCurrentPath(), ct.getParameter()));

                    if (caught != null && caught.getKind() != TypeKind.ERROR) {
                        for (Iterator<Entry<TypeMirror, Collection<Map< Element, State>>>> it = resumeOnExceptionHandler.entrySet().iterator(); it.hasNext();) {
                            Entry<TypeMirror, Collection<Map< Element, State>>> e = it.next();

                            if (info.getTypes().isSubtype(e.getKey(), caught)) {
                                for (Map< Element, State> s : e.getValue()) {
                                    variable2State = mergeOr(variable2State, s);
                                }

                                it.remove();
                            }
                        }
                    }
                }
                
                scan(ct, null);

                variable2State = mergeOr(variable2StateBeforeCatch, variable2State);
            }

            if (node.getFinallyBlock() != null) {
                pendingFinally.remove(0);
                variable2State = mergeOr(mergeOr(oldVariable2State, variable2State), afterBlockVariable2State);

                scan(node.getFinallyBlock(), null);
            }
            
            return null;
        }

        public Boolean visitReturn(ReturnTree node, ConstructorData p) {
            super.visitReturn(node, p);
            variable2State = new HashMap< Element, State>(variable2State);

            if (pendingFinally.isEmpty()) {
                //performance: limit amount of held variables and their mapping:
                for ( Element ve : currentMethodVariables) {
                    variable2State.remove(ve);
                }
            }
            
            resumeAfter(nearestMethod, variable2State);
            
            variable2State = new HashMap< Element, State>(variable2State);
            for (Iterator< Element> it = variable2State.keySet().iterator(); it.hasNext();) {
                 Element k = it.next();
                
                if (!k.getKind().isField()) it.remove();
            }
            
            return null;
        }

        public Boolean visitBreak(BreakTree node, ConstructorData p) {
            super.visitBreak(node, p);

            StatementTree target = info.getTreeUtilities().getBreakContinueTarget(getCurrentPath());
            
            resumeAfter(target, variable2State);

            variable2State = new HashMap< Element, State>();
            
            return null;
        }

        public Boolean visitSwitch(SwitchTree node, ConstructorData p) {
            scan(node.getExpression(), null);

            Map< Element, State> origVariable2State = new HashMap< Element, State>(variable2State);

            variable2State = new HashMap< Element, State>();

            boolean exhaustive = false;

            for (CaseTree ct : node.getCases()) {
                variable2State = mergeOr(variable2State, origVariable2State);

                if (ct.getExpression() == null) {
                    exhaustive = true;
                }

                scan(ct, null);
            }

            if (!exhaustive) {
                variable2State = mergeOr(variable2State, origVariable2State);
            }
            
            return null;
        }

        public Boolean visitEnhancedForLoop(EnhancedForLoopTree node, ConstructorData p) {
            return handleGeneralizedForLoop(Arrays.asList(node.getVariable(), node.getExpression()), null, null, node.getStatement(), node.getStatement(), p);
        }

        @Override
        public Boolean visitAssert(AssertTree node, ConstructorData p) {
            Map< Element, State> oldVariable2State = variable2State;

            variable2State = new HashMap< Element, Flow.State>(oldVariable2State);

            scan(node.getCondition(), null);

            if (node.getDetail() != null) {
                Map< Element, State> beforeDetailState = new HashMap< Element, Flow.State>(variable2State);

                scan(node.getDetail(), null);

                variable2State = mergeOr(variable2State, beforeDetailState);
            }
            
            variable2State = mergeOr(variable2State, oldVariable2State);

            recordResumeOnExceptionHandler("java.lang.AssertionError");
            return null;
        }

        public Boolean visitContinue(ContinueTree node, ConstructorData p) {
            StatementTree loop = info.getTreeUtilities().getBreakContinueTarget(getCurrentPath());

            if (loop == null) {
                super.visitContinue(node, p);
                return null;
            }

            Tree resumePoint;

            if (loop.getKind() == Kind.LABELED_STATEMENT) {
                loop = ((LabeledStatementTree) loop).getStatement();
            }
            
            switch (loop.getKind()) {
                case WHILE_LOOP:
                    resumePoint = ((WhileLoopTree) loop).getCondition();
                    break;
                case FOR_LOOP:
                    resumePoint = ((ForLoopTree) loop).getCondition();
                    if (resumePoint == null) {
                        resumePoint = ((ForLoopTree) loop).getStatement();
                    }
                    break;
                case DO_WHILE_LOOP:
                    resumePoint = ((DoWhileLoopTree) loop).getCondition();
                    break;
                case ENHANCED_FOR_LOOP:
                    resumePoint = ((EnhancedForLoopTree) loop).getStatement();
                    break;
                default:
                    resumePoint = null;
                    break;
            }

            if (resumePoint != null) {
                recordResume(resumeBefore, resumePoint, variable2State);
            }

            variable2State = new HashMap< Element, State>();

            super.visitContinue(node, p);
            return null;
        }

        public Boolean visitThrow(ThrowTree node, ConstructorData p) {
            super.visitThrow(node, p);

            if (node.getExpression() != null) {
                TypeMirror thrown = info.getTrees().getTypeMirror(new TreePath(getCurrentPath(), node.getExpression()));

                recordResumeOnExceptionHandler(thrown);
            }

            return null;
        }

        public Boolean visitMethodInvocation(MethodInvocationTree node, ConstructorData p) {
            super.visitMethodInvocation(node, p);

            Element invoked = info.getTrees().getElement(getCurrentPath());

            if (invoked != null && invoked.getKind() == ElementKind.METHOD) {
                recordResumeOnExceptionHandler((ExecutableElement) invoked);
            }

            return null;
        }

        @Override
        public Boolean visitLambdaExpression(LambdaExpressionTree node, ConstructorData p) {
            TypeElement oldTarget = referenceTarget;
            Boolean b = super.visitLambdaExpression(node, p);
            referenceTarget = oldTarget;
            return b;
        }

        public Boolean visitNewClass(NewClassTree node, ConstructorData p) {
            TypeElement oldTarget = referenceTarget;
            super.visitNewClass(node, p);

            Element invoked = info.getTrees().getElement(getCurrentPath());

            if (invoked != null && invoked.getKind() == ElementKind.CONSTRUCTOR) {
                recordResumeOnExceptionHandler((ExecutableElement) invoked);
            }
            referenceTarget = oldTarget;
            return null;
        }

        public Boolean visitClass(ClassTree node, ConstructorData p) {
            TypeElement oldTarget = referenceTarget;
            List<Tree> staticInitializers = new ArrayList<Tree>(node.getMembers().size());
            List<Tree> instanceInitializers = new ArrayList<Tree>(node.getMembers().size());
            List<MethodTree> constructors = new ArrayList<MethodTree>(node.getMembers().size());
            List<Tree> others = new ArrayList<Tree>(node.getMembers().size());
            
            for (Tree member : node.getMembers()) {
                if (member.getKind() == Kind.BLOCK) {
                    if (((BlockTree) member).isStatic()) {
                        staticInitializers.add(member);
                    } else {
                        instanceInitializers.add(member);
                    }
                } else if (member.getKind() == Kind.VARIABLE && ((VariableTree) member).getInitializer() != null) {
                    if (((VariableTree) member).getModifiers().getFlags().contains(Modifier.STATIC)) {
                        staticInitializers.add((VariableTree) member);
                    } else {
                        instanceInitializers.add((VariableTree) member);
                    }
                } else if (isConstructor(new TreePath(getCurrentPath(), member))) {
                    constructors.add((MethodTree) member);
                } else {
                    others.add(member);
                }
            }
            
            Map< Element, State> oldVariable2State = variable2State;

            variable2State = new HashMap<>(variable2State);
            
            for (Iterator<Entry< Element, State>> it = variable2State.entrySet().iterator(); it.hasNext();) {
                Entry< Element, State> e = it.next();
                
                if (e.getKey().getKind().isField()) it.remove();
            }
            
            try {
                handleInitializers(staticInitializers);
            
                //constructor check:
                Set< Element> definitellyAssignedOnce = new HashSet< Element>();
                Set< Element> assigned = new HashSet< Element>();

                for (Iterator<Entry< Element, State>> it = variable2State.entrySet().iterator(); it.hasNext();) {
                    Entry< Element, State> e = it.next();

                    if (e.getKey().getKind() == ElementKind.FIELD) {
                        if (!e.getValue().hasUnassigned() && !e.getValue().reassigned && e.getKey().getModifiers().contains(Modifier.STATIC)) {
                            definitellyAssignedOnce.add(e.getKey());
                        }

                        assigned.add(e.getKey());

                        it.remove();
                    }
                }

                finalCandidates.addAll(definitellyAssignedOnce);
                //TODO: support for erroneous source code, we should prevent marking instance fields written in static blocks as final-able (i.e. none of "assigned" - "definitellyAssignedOnce" should ever be final candidates
            } finally {
                variable2State = mergeOr(variable2State, oldVariable2State, false);
            }

            boolean first = true;
            
            for (MethodTree constructor : constructors) {
                scan(constructor, new ConstructorData(first, instanceInitializers));
                first = false;
            }
            
            scan(others, p);

            referenceTarget = oldTarget;
            return null;
        }

        public Boolean visitBlock(BlockTree node, ConstructorData p) {
            List<? extends StatementTree> statements = new ArrayList<StatementTree>(node.getStatements());
            
            for (int i = 0; i < statements.size(); i++) {
                StatementTree st = statements.get(i);
                
                if (st.getKind() == Kind.IF) {
                    IfTree it = (IfTree) st; 
                    if (it.getElseStatement() == null && Utilities.exitsFromAllBranchers(info, new TreePath(new TreePath(getCurrentPath(), it), it.getThenStatement()))) {
                        generalizedIf(it.getCondition(), it.getThenStatement(), statements.subList(i + 1, statements.size()), false);
                        break;
                    }
                }
                
                scan(st, null);
            }
            
            return null;
        }

        private void recordResumeOnExceptionHandler(ExecutableElement invoked) {
            for (TypeMirror tt : invoked.getThrownTypes()) {
                recordResumeOnExceptionHandler(tt);
            }

            recordResumeOnExceptionHandler("java.lang.RuntimeException");
            recordResumeOnExceptionHandler("java.lang.Error");
        }

        private void recordResumeOnExceptionHandler(String exceptionTypeFQN) {
            TypeElement exc = info.getElements().getTypeElement(exceptionTypeFQN);

            if (exc == null) return;

            recordResumeOnExceptionHandler(exc.asType());
        }

        private void recordResumeOnExceptionHandler(TypeMirror thrown) {
            if (thrown == null || thrown.getKind() == TypeKind.ERROR) return;
            
            Collection<Map< Element, State>> r = resumeOnExceptionHandler.get(thrown);

            if (r == null) {
                resumeOnExceptionHandler.put(thrown, r = new ArrayList<Map< Element, State>>());
            }

            r.add(new HashMap< Element, State>(variable2State));
        }

        public Boolean visitParenthesized(ParenthesizedTree node, ConstructorData p) {
            return super.visitParenthesized(node, p);
        }

        private void resumeAfter(Tree target, Map< Element, State> state) {
            for (TreePath tp : pendingFinally) {
                boolean shouldBeRun = false;

                for (Tree t : tp) {
                    if (t == target) {
                        shouldBeRun = true;
                        break;
                    }
                }

                if (shouldBeRun) {
                    recordResume(resumeBefore, tp.getLeaf(), state);
                } else {
                    break;
                }
            }

            recordResume(resumeAfter, target, state);
        }

        private static void recordResume(Map<Tree, Collection<Map< Element, State>>> resume, Tree target, Map< Element, State> state) {
            Collection<Map< Element, State>> r = resume.get(target);

            if (r == null) {
                resume.put(target, r = new ArrayList<Map< Element, State>>());
            }

            r.add(new HashMap< Element, State>(state));
        }

        public Boolean visitWildcard(WildcardTree node, ConstructorData p) {
            super.visitWildcard(node, p);
            return null;
        }

        public Boolean visitUnionType(UnionTypeTree node, ConstructorData p) {
            super.visitUnionType(node, p);
            return null;
        }

        public Boolean visitTypeParameter(TypeParameterTree node, ConstructorData p) {
            super.visitTypeParameter(node, p);
            return null;
        }

        public Boolean visitTypeCast(TypeCastTree node, ConstructorData p) {
            super.visitTypeCast(node, p);
            return null;
        }

        public Boolean visitSynchronized(SynchronizedTree node, ConstructorData p) {
            super.visitSynchronized(node, p);
            return null;
        }

        public Boolean visitPrimitiveType(PrimitiveTypeTree node, ConstructorData p) {
            super.visitPrimitiveType(node, p);
            return null;
        }

        public Boolean visitParameterizedType(ParameterizedTypeTree node, ConstructorData p) {
            super.visitParameterizedType(node, p);
            return null;
        }

        public Boolean visitOther(Tree node, ConstructorData p) {
            super.visitOther(node, p);
            return null;
        }

        public Boolean visitNewArray(NewArrayTree node, ConstructorData p) {
            super.visitNewArray(node, p);
            return null;
        }

        public Boolean visitModifiers(ModifiersTree node, ConstructorData p) {
            super.visitModifiers(node, p);
            return null;
        }

        public Boolean visitLabeledStatement(LabeledStatementTree node, ConstructorData p) {
            super.visitLabeledStatement(node, p);
            return null;
        }

        public Boolean visitInstanceOf(InstanceOfTree node, ConstructorData p) {
            super.visitInstanceOf(node, p);
            return null;
        }

        public Boolean visitImport(ImportTree node, ConstructorData p) {
            super.visitImport(node, p);
            return null;
        }

        public Boolean visitExpressionStatement(ExpressionStatementTree node, ConstructorData p) {
            super.visitExpressionStatement(node, p);
            return null;
        }

        public Boolean visitErroneous(ErroneousTree node, ConstructorData p) {
            super.visitErroneous(node, p);
            return null;
        }

        public Boolean visitEmptyStatement(EmptyStatementTree node, ConstructorData p) {
            super.visitEmptyStatement(node, p);
            return null;
        }

        public Boolean visitCompilationUnit(CompilationUnitTree node, ConstructorData p) {
            super.visitCompilationUnit(node, p);
            return null;
        }

        public Boolean visitCatch(CatchTree node, ConstructorData p) {
            super.visitCatch(node, p);
            return null;
        }

        public Boolean visitCase(CaseTree node, ConstructorData p) {
            super.visitCase(node, p);
            return null;
        }

        public Boolean visitArrayType(ArrayTypeTree node, ConstructorData p) {
            super.visitArrayType(node, p);
            return null;
        }

        public Boolean visitArrayAccess(ArrayAccessTree node, ConstructorData p) {
            boolean lv = lValueDereference;
            super.visitArrayAccess(node, p);
            this.lValueDereference = lv;
            return null;
        }

        public Boolean visitAnnotation(AnnotationTree node, ConstructorData p) {
            super.visitAnnotation(node, p);
            return null;
        }

        private Map<Element, State> mergeOr(Map< Element, State> into, Map< Element, State> what) {
            return mergeOr(into, what, true);
        }
        
        private Map<Element, State> mergeOr(Map< Element, State> into, Map< Element, State> what, boolean markMissingAsUnassigned) {
            for (Entry< Element, State> e : what.entrySet()) {
                State stt = into.get(e.getKey());

                if (stt != null) {
                    into.put(e.getKey(), stt.merge(e.getValue()));
                } else if (e.getKey().getKind() == ElementKind.FIELD && markMissingAsUnassigned) {
                    into.put(e.getKey(), e.getValue().merge(UNASSIGNED));
                } else {
                    into.put(e.getKey(), e.getValue());
                }
            }
            
            if (markMissingAsUnassigned) {
                for (Entry< Element, State> e : into.entrySet()) {
                    if (e.getKey().getKind() == ElementKind.FIELD && !what.containsKey(e.getKey())) {
                        into.put(e.getKey(), e.getValue().merge(UNASSIGNED));
                    }
                }
            }

            return into;
        }

        private void handleInitializers(List<Tree> additionalTrees) {
            for (Tree additionalTree : additionalTrees) {
                switch (additionalTree.getKind()) {
                    case BLOCK:
                        Tree oldNearestMethod = nearestMethod;
                        Set< Element> oldCurrentMethodVariables = currentMethodVariables;
                        Map<TypeMirror, Collection<Map< Element, State>>> oldResumeOnExceptionHandler = resumeOnExceptionHandler;

                        nearestMethod = additionalTree;
                        currentMethodVariables = Collections.newSetFromMap(new IdentityHashMap< Element, Boolean>());
                        resumeOnExceptionHandler = new IdentityHashMap<TypeMirror, Collection<Map< Element, State>>>();

                        try {
                            scan(((BlockTree) additionalTree).getStatements(), null);
                        } finally {
                            nearestMethod = oldNearestMethod;
                            currentMethodVariables = oldCurrentMethodVariables;
                            resumeOnExceptionHandler = oldResumeOnExceptionHandler;
                        }
                        break;
                    case VARIABLE: scan(additionalTree, null); break;
                    default: assert false : additionalTree.getKind(); break;
                }
            }
        }
    }
    
    private static final Set<ElementKind> SUPPORTED_VARIABLES = EnumSet.of(ElementKind.EXCEPTION_PARAMETER, ElementKind.LOCAL_VARIABLE, ElementKind.PARAMETER, ElementKind.FIELD);
    private static final Set<ElementKind> LOCAL_VARIABLES = EnumSet.of(ElementKind.EXCEPTION_PARAMETER, ElementKind.LOCAL_VARIABLE, ElementKind.PARAMETER);
    
    static class State {
        private final Set<TreePath> assignments;
        private final boolean reassigned;
        private State(Set<TreePath> assignments, boolean reassigned) {
            this.assignments = assignments;
            this.reassigned = reassigned;
        }
        public static State create(TreePath assignment, boolean reassigned) {
            return new State(Collections.singleton(assignment), reassigned);
        }
        public static State create(TreePath assignment, State previous) {
            return new State(Collections.singleton(assignment), previous != null && (previous.assignments.size() > 1 || !previous.assignments.contains(null)));
        }
        public State merge(State value) {
            @SuppressWarnings("LocalVariableHidesMemberVariable")
            Set<TreePath> assignments = new HashSet<TreePath>(this.assignments);

            assignments.addAll(value.assignments);

            return new State(assignments, this.reassigned || value.reassigned);
        }
        
        public boolean hasUnassigned() {
            return assignments.contains(null);
        }
    }
    
    static class ConstructorData {
        final boolean first;
        final List<Tree> initializers;
        public ConstructorData(boolean first, List<Tree> initializers) {
            this.first = first;
            this.initializers = initializers;
        }
    }
    
    private static final class ReluctantSet<T> implements Set<T> {
        private final Set<T> included = new HashSet<>();
        private final Set<Object> removed = new HashSet<>();
        @Override public int size() {
            return included.size();
        }
        @Override public boolean isEmpty() {
            return included.isEmpty();
        }
        @Override public boolean contains(Object o) {
            return included.contains(o);
        }
        @Override public Iterator<T> iterator() {
            return Collections.synchronizedSet(included).iterator();
        }
        @Override public Object[] toArray() {
            return included.toArray();
        }
        @Override public <T> T[] toArray(T[] a) {
            return included.toArray(a);
        }
        @Override public boolean add(T e) {
            if (removed.contains(e)) {
                return false;
            }
            return included.add(e);
        }
        @Override public boolean remove(Object o) {
            removed.add(o);
            return included.remove(o);
        }
        @Override public boolean containsAll(Collection<?> c) {
            return included.containsAll(c);
        }
        @Override public boolean addAll(Collection<? extends T> c) {
            boolean result = false;
            for (T t : c) result |= add(t);
            return result;
        }
        @Override public boolean retainAll(Collection<?> c) {
            return included.retainAll(c);
        }
        @Override public boolean removeAll(Collection<?> c) {
            boolean result = false;
            for (Object o : c) result |= remove(o);
            return result;
        }
        @Override public void clear() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    private static final State UNASSIGNED = State.create(null, false);

}

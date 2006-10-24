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
package org.netbeans.api.java.source;

import com.sun.source.tree.*;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.tools.javac.api.JavacScope;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.jackpot.model.CommentHandler;
import org.netbeans.jackpot.model.CommentSet;
import org.netbeans.modules.java.builder.CommentHandlerService;

/**
 *
 * @author Jan Lahoda, Dusan Balek
 */
public final class TreeUtilities {
    
    private CompilationInfo info;
    private CommentHandler handler;
    
    /** Creates a new instance of CommentUtilities */
    TreeUtilities(CompilationInfo info) {
        this.info = info;
        this.handler = CommentHandlerService.instance(info.getJavacTask().getContext());
    }
    
    public boolean isInterface(ClassTree tree) {
        return (((JCTree.JCModifiers)tree.getModifiers()).flags & Flags.INTERFACE) != 0;
    }
    
    public boolean isEnum(ClassTree tree) {
        return (((JCTree.JCModifiers)tree.getModifiers()).flags & Flags.ENUM) != 0;
    }
    
    public boolean isAnnotation(ClassTree tree) {
        return (((JCTree.JCModifiers)tree.getModifiers()).flags & Flags.ANNOTATION) != 0;
    }
    
    /**Returns list of comments attached to a given tree. Can return either
     * preceding or trailing comments.
     *
     * @param tree for which comments should be returned
     * @param preceding true if preceding comments should be returned, false if trailing comments should be returned.
     * @return list of preceding/trailing comments attached to the given tree
     */
    public List<Comment> getComments(Tree tree, boolean preceding) {
        CommentSet set = handler.getComments(tree);
        
        if (set == null)
            return Collections.<Comment>emptyList();
        
        List<Comment> comments = preceding ? set.getPrecedingComments() : set.getTrailingComments();
        
        return Collections.unmodifiableList(comments);
    }
    
    public TreePath pathFor(int pos) {
        return pathFor(new TreePath(info.getCompilationUnit()), pos);
    }
    
    public TreePath pathFor(TreePath path, int pos) {
        return pathFor(path, pos, info.getTrees().getSourcePositions());
    }

    public TreePath pathFor(TreePath path, int pos, SourcePositions sourcePositions) {
        if (info == null || path == null || sourcePositions == null)
            throw new IllegalArgumentException();

        class Result extends Error {
	    TreePath path;
	    Result(TreePath path) {
		this.path = path;
	    }
	}
        
	class PathFinder extends TreePathScanner<Void,Void> {
            private int pos;
            private SourcePositions sourcePositions;
            
            private PathFinder(int pos, SourcePositions sourcePositions) {
                this.pos = pos;
                this.sourcePositions = sourcePositions;
            }
        
	    public Void scan(Tree tree, Void p) {
                if (tree != null) {
                    switch(tree.getKind()) {
                        case ERRONEOUS:
                            return tree.accept(this, p);
                        default:
                            if (sourcePositions.getStartPosition(getCurrentPath().getCompilationUnit(), tree) < pos && sourcePositions.getEndPosition(getCurrentPath().getCompilationUnit(), tree) >= pos) {
                                super.scan(tree, p);
                                throw new Result(new TreePath(getCurrentPath(), tree));
                            }
                    }
                }
                return null;
            }
	}
	
	try {
	    new PathFinder(pos, sourcePositions).scan(path, null);
	} catch (Result result) {
	    path = result.path;
	}
        
        if (path.getLeaf() == path.getCompilationUnit())
            return path;
        
        TokenSequence<JavaTokenId> tokenList = tokensFor(path.getLeaf(), sourcePositions);
        if (tokenList.moveLast() && tokenList.offset() < pos) {
            switch (tokenList.token().id()) {
                case GTGTGT:
                case GTGT:
                case GT:
                    if (path.getLeaf().getKind() == Tree.Kind.MEMBER_SELECT || path.getLeaf().getKind() == Tree.Kind.CLASS)
                        break;
                case RPAREN:
                    if (path.getLeaf().getKind() == Tree.Kind.ENHANCED_FOR_LOOP || path.getLeaf().getKind() == Tree.Kind.FOR_LOOP ||
                            path.getLeaf().getKind() == Tree.Kind.IF || path.getLeaf().getKind() == Tree.Kind.WHILE_LOOP ||
                            path.getLeaf().getKind() == Tree.Kind.DO_WHILE_LOOP)
                        break;
                case SEMICOLON:
                    if (path.getLeaf().getKind() == Tree.Kind.FOR_LOOP &&
                            tokenList.offset() <= sourcePositions.getStartPosition(path.getCompilationUnit(), ((ForLoopTree)path.getLeaf()).getUpdate().get(0)))
                        break;
                case RBRACE:
                    path = path.getParentPath();
                    switch (path.getLeaf().getKind()) {
                        case CATCH:
                            path = path.getParentPath();
                        case METHOD:
                        case TRY:
                            path = path.getParentPath();
                    }
                    break;
            }
        }        
        return path;
    }
    
    public TypeMirror parseType(String expr, TypeElement scope) {
	return info.getJavacTask().parseType(expr, scope);
    }
    
    public StatementTree parseStatement(String stmt, SourcePositions[] sourcePositions) {
	return (StatementTree)info.getJavacTask().parseStatement(stmt, sourcePositions);
    }
    
    public ExpressionTree parseExpression(String expr, SourcePositions[] sourcePositions) {
	return (ExpressionTree)info.getJavacTask().parseExpression(expr, sourcePositions);
    }
    
    public ExpressionTree parseVariableInitializer(String init, SourcePositions[] sourcePositions) {
	return (ExpressionTree)info.getJavacTask().parseVariableInitializer(init, sourcePositions);
    }

    public BlockTree parseStaticBlock(String block, SourcePositions[] sourcePositions) {
	return (BlockTree)info.getJavacTask().parseStaticBlock(block, sourcePositions);
    }

    public Scope scopeFor(int pos) {
        List<? extends StatementTree> stmts = null;
        TreePath path = pathFor(pos);
        switch (path.getLeaf().getKind()) {
            case BLOCK:
                stmts = ((BlockTree)path.getLeaf()).getStatements();
                break;
            case FOR_LOOP:
                stmts = ((ForLoopTree)path.getLeaf()).getInitializer();
                break;
            case ENHANCED_FOR_LOOP:
                stmts = Collections.singletonList(((EnhancedForLoopTree)path.getLeaf()).getStatement());
                break;
            case METHOD:
                stmts = ((MethodTree)path.getLeaf()).getParameters();
                break;
        }
        if (stmts != null) {
            Tree tree = null;
            SourcePositions sourcePositions = info.getTrees().getSourcePositions();
            for (StatementTree st : stmts) {
                if (sourcePositions.getStartPosition(path.getCompilationUnit(), st) < pos)
                    tree = st;
            }
            if (tree != null)
                path = new TreePath(path, tree);
        }
        return info.getTrees().getScope(path);
    }
    
    public TypeMirror attributeTree(Tree tree, Scope scope) {
        return info.getJavacTask().attributeTree((JCTree)tree, ((JavacScope)scope).getEnv());
    }
    
    public Scope attributeTreeTo(Tree tree, Scope scope, Tree to) {
        return info.getJavacTask().attributeTreeTo((JCTree)tree, ((JavacScope)scope).getEnv(), (JCTree)to);
    }
    
    public TokenSequence<JavaTokenId> tokensFor(Tree tree) {
        return tokensFor(tree, info.getTrees().getSourcePositions());
    }
    
    public TokenSequence<JavaTokenId> tokensFor(Tree tree, SourcePositions sourcePositions) {
        int start = (int)sourcePositions.getStartPosition(info.getCompilationUnit(), tree);
        int end   = (int)sourcePositions.getEndPosition(info.getCompilationUnit(), tree);
        
        return info.getTokenHiearchy().tokenSequence(JavaTokenId.language()).subSequence(start, end);
    }
    
    public boolean isAccessible(Scope scope, Element member, TypeMirror type) {
        if (scope instanceof JavacScope 
                && member instanceof Symbol 
                && type instanceof Type) {
            Resolve resolve = Resolve.instance(info.getJavacTask().getContext());
	    return resolve.isAccessible(((JavacScope)scope).getEnv(), (Type)type, (Symbol)member);  
        } else 
            return false;
    }
    
    public boolean isStaticContext(Scope scope) {
        return Resolve.instance(info.getJavacTask().getContext()).isStatic(((JavacScope)scope).getEnv());
    }
    
    public Set<TypeMirror> getUncaughtExceptions(TreePath path) {
        HashSet<TypeMirror> set = new HashSet<TypeMirror>();
        new UncaughtExceptionsVisitor(info).scan(path, set);
        return set;
    }

    private static class UncaughtExceptionsVisitor extends TreePathScanner<Void, Set<TypeMirror>> {
        
        private CompilationInfo info;
        
        private UncaughtExceptionsVisitor(CompilationInfo info) {
            this.info = info;
        }
    
        public Void visitMethodInvocation(MethodInvocationTree node, Set<TypeMirror> p) {
            super.visitMethodInvocation(node, p);
            Element el = info.getTrees().getElement(getCurrentPath());
            if (el != null && el.getKind() == ElementKind.METHOD)
                p.addAll(((ExecutableElement)el).getThrownTypes());
            return null;
        }

        public Void visitNewClass(NewClassTree node, Set<TypeMirror> p) {
            super.visitNewClass(node, p);
            Element el = info.getTrees().getElement(getCurrentPath());
            if (el != null && el.getKind() == ElementKind.CONSTRUCTOR)
                p.addAll(((ExecutableElement)el).getThrownTypes());
            return null;
        }

        public Void visitTry(TryTree node, Set<TypeMirror> p) {
            Set<TypeMirror> s = new HashSet<TypeMirror>();
            scan(node.getBlock(), s);
            for (CatchTree ct : node.getCatches()) {
                TypeMirror t = info.getTrees().getTypeMirror(new TreePath(getCurrentPath(), ct.getParameter().getType()));
                for (Iterator<TypeMirror> it = s.iterator(); it.hasNext();)
                    if (info.getTypes().isSubtype(it.next(), t))
                        it.remove();
            }
            p.addAll(s);
            scan(node.getCatches(), p);
            scan(node.getFinallyBlock(), p);
            return null;            
        }

        public Void visitMethod(MethodTree node, Set<TypeMirror> p) {
            Set<TypeMirror> s = new HashSet<TypeMirror>();
            scan(node.getBody(), s);
            for (ExpressionTree et : node.getThrows()) {
                TypeMirror t = info.getTrees().getTypeMirror(new TreePath(getCurrentPath(), et));
                for (Iterator<TypeMirror> it = s.iterator(); it.hasNext();)
                    if (info.getTypes().isSubtype(it.next(), t))
                        it.remove();
            }
            p.addAll(s);
            return null;
        }
    }
}

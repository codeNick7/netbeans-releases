/**
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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.api.java.source.gen;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import com.sun.source.tree.*;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import org.netbeans.api.java.source.*;
import org.netbeans.junit.NbTestSuite;
import static org.netbeans.api.java.source.JavaSource.*;

/**
 *
 * @author Pavel Flaska
 */
public class RefactoringRegressionsTest extends GeneratorTestMDRCompat {

    public RefactoringRegressionsTest(String aName) {
        super(aName);
    }
    
    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTestSuite(RefactoringRegressionsTest.class);
//        suite.addTest(new RefactoringRegressionsTest("testRenameTypeParameterInInvocation"));
//        suite.addTest(new RefactoringRegressionsTest("testRenameInNewClassExpressionWithSpaces"));
//        suite.addTest(new RefactoringRegressionsTest("testMoveEmptyReturnStatement"));
        return suite;
    }

    /**
     * http://www.netbeans.org/issues/show_bug.cgi?id=111981
     */
    public void testRenameTypeParameterInInvocation() throws Exception {
        testFile = new File(getWorkDir(), "Test.java");
        TestUtilities.copyStringToFile(testFile, 
            "package javaapplication1;\n" +
            "\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.Arrays;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public class Mnozina {\n" +
            "    \n" +
            "    static enum Prvek {\n" +
            "        PrvniPrvek,\n" +
            "        DruhyPrvek;\n" +
            "    }\n" +
            "    \n" +
            "    void metoda() {\n" +
            "        List<Prvek> required = new ArrayList<Prvek>();\n" +
            "        required.addAll(Arrays.<Prvek>asList());\n" +
            "    }\n" +
            "}\n"
            );
        String golden =
            "package javaapplication1;\n" +
            "\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.Arrays;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public class Mnozina {\n" +
            "    \n" +
            "    static enum Unit {\n" +
            "        PrvniPrvek,\n" +
            "        DruhyPrvek;\n" +
            "    }\n" +
            "    \n" +
            "    void metoda() {\n" +
            "        List<Unit> required = new ArrayList<Unit>();\n" +
            "        required.addAll(Arrays.<Unit>asList());\n" +
            "    }\n" +
            "}\n";

        JavaSource src = getJavaSource(testFile);
        Task<WorkingCopy> task = new Task<WorkingCopy>() {

            public void run(WorkingCopy workingCopy) throws IOException {
                workingCopy.toPhase(Phase.RESOLVED);
                CompilationUnitTree cut = workingCopy.getCompilationUnit();
                TreeMaker make = workingCopy.getTreeMaker();
                ClassTree clazz = (ClassTree) cut.getTypeDecls().get(0);
                ClassTree innerClazz = (ClassTree) clazz.getMembers().get(1);
                MethodTree method = (MethodTree) clazz.getMembers().get(2);
                workingCopy.rewrite(innerClazz, make.setLabel(innerClazz, "Unit"));
                
                VariableTree var = (VariableTree) method.getBody().getStatements().get(0);
                ParameterizedTypeTree ptt = (ParameterizedTypeTree) var.getType();
                IdentifierTree ident = (IdentifierTree) ptt.getTypeArguments().get(0);
                workingCopy.rewrite(ident, make.Identifier("Unit"));
                
                NewClassTree nct = (NewClassTree) var.getInitializer();
                ptt = (ParameterizedTypeTree) nct.getIdentifier();
                ident = (IdentifierTree) ptt.getTypeArguments().get(0);
                workingCopy.rewrite(ident, make.Identifier("Unit"));
                
                ExpressionStatementTree stat = (ExpressionStatementTree) method.getBody().getStatements().get(1);
                MethodInvocationTree mit = (MethodInvocationTree) stat.getExpression();
                mit = (MethodInvocationTree) mit.getArguments().get(0);
                ident = (IdentifierTree) mit.getTypeArguments().get(0);
                workingCopy.rewrite(ident, make.Identifier("Unit"));
            }
            
        };
        src.runModificationTask(task).commit();
        String res = TestUtilities.copyFileToString(testFile);
        System.err.println(res);
        assertEquals(golden, res);
    }

    /**
     * http://www.netbeans.org/issues/show_bug.cgi?id=111966
     */
    public void testRenameInNewClassExpressionWithSpaces() throws Exception {
        testFile = new File(getWorkDir(), "Test.java");
        TestUtilities.copyStringToFile(testFile, 
            "package javaapplication1;\n" +
            "\n" +
            "public class A{\n" +
            "	A	( ){};\n" +
            "};\n" +
            "\n" +
            "class C{\n" +
            "	void s(){\n" +
            "	new javaapplication1 . A ( );\n" +
            "	}\n" +
            "};\n"
            );
        String golden =
            "package javaapplication1;\n" +
            "\n" +
            "public class B{\n" +
            "	B	( ){};\n" +
            "};\n" +
            "\n" +
            "class C{\n" +
            "	void s(){\n" +
            "	new javaapplication1 . B ( );\n" +
            "	}\n" +
            "};\n";

        JavaSource src = getJavaSource(testFile);
        Task<WorkingCopy> task = new Task<WorkingCopy>() {

            public void run(WorkingCopy workingCopy) throws IOException {
                workingCopy.toPhase(Phase.RESOLVED);
                CompilationUnitTree cut = workingCopy.getCompilationUnit();
                TreeMaker make = workingCopy.getTreeMaker();
                ClassTree clazz = (ClassTree) cut.getTypeDecls().get(0);
                MethodTree method = (MethodTree) clazz.getMembers().get(0);
                workingCopy.rewrite(clazz, make.setLabel(clazz, "B"));
                workingCopy.rewrite(method, make.setLabel(method, "B"));
                
                method = (MethodTree) ((ClassTree) cut.getTypeDecls().get(2)).getMembers().get(1);
                ExpressionStatementTree est = (ExpressionStatementTree) method.getBody().getStatements().get(0);
                NewClassTree nct = (NewClassTree) est.getExpression();
                MemberSelectTree mst = (MemberSelectTree) nct.getIdentifier();
                workingCopy.rewrite(mst, make.setLabel(mst, "B"));
            }
            
        };
        src.runModificationTask(task).commit();
        String res = TestUtilities.copyFileToString(testFile);
        System.err.println(res);
        assertEquals(golden, res);
    }
    
    /**
     * http://www.netbeans.org/issues/show_bug.cgi?id=111769
     */
    public void testMoveEmptyReturnStatement() throws Exception {
        testFile = new File(getWorkDir(), "Test.java");
        TestUtilities.copyStringToFile(testFile, 
            "package javaapplication1;\n" +
            "\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.Arrays;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public class Mnozina {\n" +
            "    \n" +
            "    void metoda() {\n" +
            "        List<Prvek> required = new ArrayList<Prvek>();\n" +
            "        return;\n" +
            "    }\n" +
            "}\n"
            );
        String golden =
            "package javaapplication1;\n" +
            "\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.Arrays;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public class Mnozina {\n" +
            "    \n" +
            "    void metoda() {\n" +
            "    }\n" +
            "\n" +
            "    void m() {\n" +
            "        List<Prvek> required = new ArrayList<Prvek>();\n" +
            "        return;\n" +
            "    }\n" +
            "}\n";

        JavaSource src = getJavaSource(testFile);
        Task<WorkingCopy> task = new Task<WorkingCopy>() {

            public void run(WorkingCopy workingCopy) throws IOException {
                workingCopy.toPhase(Phase.RESOLVED);
                CompilationUnitTree cut = workingCopy.getCompilationUnit();
                TreeMaker make = workingCopy.getTreeMaker();
                ClassTree clazz = (ClassTree) cut.getTypeDecls().get(0);
                MethodTree method = (MethodTree) clazz.getMembers().get(1);
                MethodTree nju = make.Method(
                        make.Modifiers(Collections.<Modifier>emptySet()),
                        "m",
                        make.PrimitiveType(TypeKind.VOID), // return type - void
                        Collections.<TypeParameterTree>emptyList(),
                        Collections.<VariableTree>emptyList(),
                        Collections.<ExpressionTree>emptyList(),
                        make.Block(method.getBody().getStatements(), false),
                        null // default value - not applicable
                );
                workingCopy.rewrite(clazz, make.addClassMember(clazz, nju));
                workingCopy.rewrite(method.getBody(), make.Block(Collections.<StatementTree>singletonList(make.Return(null)), false));
            }
            
        };
        src.runModificationTask(task).commit();
        String res = TestUtilities.copyFileToString(testFile);
        System.err.println(res);
        assertEquals(golden, res);
    }
    
    String getGoldenPckg() {
        return "";
    }

    String getSourcePckg() {
        return "";
    }
}

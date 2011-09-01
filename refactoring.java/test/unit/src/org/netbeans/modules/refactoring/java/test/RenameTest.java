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
package org.netbeans.modules.refactoring.java.test;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.TreePathHandle;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.modules.refactoring.java.ui.JavaRenameProperties;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Jan Becicka
 */
public class RenameTest extends RefactoringTestBase {

    public RenameTest(String name) {
        super(name);
    }

    public void testRenameProp() throws Exception {
        writeFilesAndWaitForScan(src,
                new File("t/A.java", "package t;\n"
                + "public class A {\n"
                + "    private int property;\n"
                + "    public void setProperty(int property) {\n"
                + "        this.property = property;\n"
                + "    }\n"
                + "    public int getProperty() {\n"
                + "        return property;\n"
                + "    }\n"
                + "    public int foo() {\n"
                + "        A a = new A();\n"
                + "        a.setProperty(1);\n"
                + "        return a.getProperty();\n"
                + "    }\n"
                + "}"));
        JavaRenameProperties props = new JavaRenameProperties();
        props.setIsRenameGettersSetters(true);
        performRename(src.getFileObject("t/A.java"), 1, "renamed", props);
        verifyContent(src,
                new File("t/A.java", "package t;\n"
                + "public class A {\n"
                + "    private int renamed;\n"
                + "    public void setRenamed(int renamed) {\n"
                + "        this.renamed = renamed;\n"
                + "    }\n"
                + "    public int getRenamed() {\n"
                + "        return renamed;\n"
                + "    }\n"
                + "    public int foo() {\n"
                + "        A a = new A();\n"
                + "        a.setRenamed(1);\n"
                + "        return a.getRenamed();\n"
                + "    }\n"
                + "}"));

    }
    
    public void test200224() throws Exception {
        writeFilesAndWaitForScan(src,
                new File("t/A.java", "package t;\n"
                + "public class A {\n"
                + "}"));
        writeFilesAndWaitForScan(test,
                new File("t/ATest.java", "package t;\n"
                + "import junit.framework.TestCase;\n"
                + "\n"
                + "public class ATest extends TestCase {\n"
                + "}"));
        JavaRenameProperties props = new JavaRenameProperties();
        props.setIsRenameTestClass(true);
        performRename(src.getFileObject("t/A.java"), -1, "B", props);
        verifyContent(src,
                new File("t/A.java", "package t;\n" // XXX: Why use old filename, is it not renamed?
                + "public class B {\n"
                + "}"));
        verifyContent(test,
                new File("t/BTest", "package t;\n" // XXX: Why is there no java extension?
                + "import junit.framework.TestCase;\n"
                + "\n"
                + "public class BTest extends TestCase {\n"
                + "}"));
    }
    
    public void test111953() throws Exception {
        writeFilesAndWaitForScan(src, new File("t/B.java", "class B { public void m(){};}"),
                new File("t/A.java", "class A extends B implements I{ public void m(){};}"),
                new File("t/I.java", "interface I { void m();}"),
                new File("t/J.java", "interface J { void m();}"),
                new File("t/C.java", "class C extends D implements I, J{ public void m(){};}"),
                new File("t/D.java", "class D { public void m(){};}"));
        performRename(src.getFileObject("t/B.java"), 1, "k", null, new Problem(false, "ERR_IsOverridden"), new Problem(false, "ERR_IsOverriddenOverrides"));
        verifyContent(src, new File("t/B.java", "class B { public void k(){};}"),
                new File("t/A.java", "class A extends B implements I{ public void k(){};}"),
                new File("t/I.java", "interface I { void m();}"),
                new File("t/J.java", "interface J { void m();}"),
                new File("t/C.java", "class C extends D implements I, J{ public void m(){};}"),
                new File("t/D.java", "class D { public void m(){};}"));
    }
    
    public void test195070() throws Exception { // #195070 - refactor/rename works wrong with override
        writeFilesAndWaitForScan(src, new File("t/A.java", "class A { public void bindSuper(){}}"),
                new File("t/B.java", "class B extends A { public void bind(){ bindSuper();}}"));
        performRename(src.getFileObject("t/A.java"), 1, "bind", null);
        verifyContent(src, new File("t/A.java", "class A { public void bind(){}}"),
                new File("t/B.java", "class B extends A { public void bind(){ super.bind();}}"));
        
        writeFilesAndWaitForScan(src, new File("t/A.java", "class A { public void bindSuper(){}}"),
                new File("t/B.java", "class B extends A { public void bind(){ bindSuper();}}"));
        performRename(src.getFileObject("t/A.java"), 1, "binding", null);
        verifyContent(src, new File("t/A.java", "class A { public void binding(){}}"),
                new File("t/B.java", "class B extends A { public void bind(){ binding();}}"));
    }

    private void performRename(FileObject source, final int position, final String newname, final JavaRenameProperties props, Problem... expectedProblems) throws Exception {
        final RenameRefactoring[] r = new RenameRefactoring[1];

        JavaSource.forFileObject(source).runUserActionTask(new Task<CompilationController>() {

            @Override
            public void run(CompilationController javac) throws Exception {
                javac.toPhase(JavaSource.Phase.RESOLVED);
                CompilationUnitTree cut = javac.getCompilationUnit();

                Tree method = cut.getTypeDecls().get(0);
                if (position >= 0) {
                    method = ((ClassTree) method).getMembers().get(position);
                }

                TreePath tp = TreePath.getPath(cut, method);
                r[0] = new RenameRefactoring(Lookups.singleton(TreePathHandle.create(tp, javac)));
                r[0].setNewName(newname);
                if(props != null) {
                    r[0].getContext().add(props);
                }
            }
        }, true);

        RefactoringSession rs = RefactoringSession.create("Rename");
        List<Problem> problems = new LinkedList<Problem>();

        addAllProblems(problems, r[0].preCheck());
        if (!problemIsFatal(problems)) {
            addAllProblems(problems, r[0].prepare(rs));
        }
        if (!problemIsFatal(problems)) {
            addAllProblems(problems, rs.doRefactoring(true));
        }

        assertProblems(Arrays.asList(expectedProblems), problems);
    }
    
    private boolean problemIsFatal(List<Problem> problems) {
        for (Problem problem : problems) {
            if (problem.isFatal()) {
                return true;
            }
        }
        return false;
    }
}

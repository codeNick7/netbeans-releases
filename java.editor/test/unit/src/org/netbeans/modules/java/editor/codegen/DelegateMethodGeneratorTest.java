/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
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
package org.netbeans.modules.java.editor.codegen;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;

import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.ScanUtils;
import org.netbeans.api.java.source.SourceUtilsTestUtil;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.TreePathHandle;
import org.netbeans.api.java.source.WorkingCopy;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.java.editor.codegen.ui.ElementNode;
import org.netbeans.modules.java.source.TreeLoader;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Jan Lahoda
 */
public class DelegateMethodGeneratorTest extends NbTestCase {
    
    public DelegateMethodGeneratorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        TreeLoader.DISABLE_CONFINEMENT_TEST = true;
    }

    public void testFindUsableFields() throws Exception {
        prepareTest("FindUsableFieldsTest");

        CompilationInfo info = SourceUtilsTestUtil.getCompilationInfo(source, Phase.RESOLVED);
        TypeElement clazz = info.getElements().getTypeElement("test.FindUsableFieldsTest");

        List<ElementNode.Description> variables = DelegateMethodGenerator.computeUsableFieldsDescriptions(info, info.getTrees().getPath(clazz));

        assertEquals(1, variables.size());
        
        variables = variables.get(0).getSubs();
        
        assertEquals(2, variables.size());
        assertTrue("s".contentEquals(variables.get(0).getName()));
        assertTrue("l".contentEquals(variables.get(1).getName()));
    }

    public void testMethodProposals1() throws Exception {
        performMethodProposalsTest("a");
    }

    public void testMethodProposals2() throws Exception {
        performMethodProposalsTest("l");
    }

    public void testMethodProposals3() throws Exception {
        performMethodProposalsTest("s");
    }

    public void testGenerate129140() throws Exception {
        performTestGenerateTest("ll", "toArray", 1);
    }

    public void testGenerate133625a() throws Exception {
        performTestGenerateTest("lext", "add", 1);
    }

    public void testGenerate133625b() throws Exception {
        performTestGenerateTest("lsup", "add", 1);
    }

    public void testGenerate133625c() throws Exception {
        performTestGenerateTest("lsup", "addAll", 1);
    }

    public void testGenerate133625d() throws Exception {
        performTestGenerateTest("lub", "add", 1);
    }

    private void performTestGenerateTest(String field, final String methodName, final int paramCount) throws Exception {
        prepareTest("MethodProposals");

        CompilationInfo info = SourceUtilsTestUtil.getCompilationInfo(source, Phase.RESOLVED);
        TypeElement clazz = info.getElements().getTypeElement("test.MethodProposals");
        VariableElement variable = null;

        for (VariableElement v : ElementFilter.fieldsIn(clazz.getEnclosedElements())) {
            if (field.contentEquals(v.getSimpleName())) {
                variable = v;
            }
        }

        assertNotNull(variable);

        final VariableElement variableFinal = variable;

        final TreePath ct = info.getTrees().getPath(clazz);

        source.runModificationTask(new Task<WorkingCopy>() {
            public void run(WorkingCopy wc) throws Exception {
                wc.toPhase(Phase.RESOLVED);
                VariableElement var = ElementHandle.create(variableFinal).resolve(wc);
                TreePath tp = TreePathHandle.create(ct, wc).resolve(wc);
                TypeElement type = (TypeElement) ((DeclaredType) var.asType()).asElement();
                for (ExecutableElement ee : ElementFilter.methodsIn(type.getEnclosedElements())) {
                    if (methodName.equals(ee.getSimpleName().toString()) && ee.getParameters().size() == paramCount) {
                        DelegateMethodGenerator.generateDelegatingMethods(wc, tp, var, Collections.singletonList(ee), -1);
                    }
                }
            }
        }).commit();

        source.runUserActionTask(new Task<CompilationController>() {
            public void run(CompilationController parameter) throws Exception {
                getRef().print(parameter.getText());
            }
        }, true);

        String version = System.getProperty("java.specification.version") + "/";
        compareReferenceFiles(this.getName()+".ref",version+this.getName()+".pass",this.getName()+".diff");
    }
     
    private void performMethodProposalsTest(final String name) throws Exception {
        prepareTest("MethodProposals");

        ScanUtils.waitUserActionTask(source, new Task<CompilationController>() {

            @Override
            public void run(CompilationController info) throws Exception {
                info.toPhase(Phase.RESOLVED);
                TypeElement clazz = info.getElements().getTypeElement("test.MethodProposals");
                VariableElement variable = null;

                for (VariableElement v : ElementFilter.fieldsIn(clazz.getEnclosedElements())) {
                    if (name.contentEquals(v.getSimpleName())) {
                        variable = v;
                    }
                }

                assertNotNull(variable);

                ClassTree ct = info.getTrees().getTree(clazz);
                int offset = (int) (info.getTrees().getSourcePositions().getEndPosition(info.getCompilationUnit(), ct) - 1);
                compareMethodProposals(info, DelegateMethodGenerator.getAvailableMethods(info, offset,
                        ElementHandle.create(clazz), ElementHandle.create(variable)));
            }
        });
    }
     
    private void compareMethodProposals(CompilationInfo info, ElementNode.Description proposal) {
        List<ElementNode.Description> proposals = new LinkedList<ElementNode.Description>();
        Queue<ElementNode.Description> q = new LinkedList<ElementNode.Description>();
        
        q.offer(proposal);
        
        while (!q.isEmpty()) {
            ElementNode.Description en = q.remove();
            
            if (en == null) {
                continue;
            }
            if (en.getSubs() == null || en.getSubs().isEmpty()) {
                proposals.add(en);
                continue;
            }
            
            if (en.getElementHandle() != null && en.getElementHandle().getKind() == ElementKind.CLASS && "java.lang.Object".equals(en.getElementHandle().getBinaryName())) {
                continue;
            }
            
            for (ElementNode.Description d : en.getSubs()) {
                q.offer(d);
            }
        }
        
        List<String> result = new ArrayList<String>();
        
        for (ElementNode.Description d : proposals) {
            ExecutableElement resolved = (ExecutableElement) d.getElementHandle().resolve(info);
            result.add(dump(resolved));
        }
        
        Collections.sort(result);
        
        for (String s : result) {
            ref(s);
        }
        
        String version = System.getProperty("java.specification.version") + "/";
        compareReferenceFiles(this.getName()+".ref",version+this.getName()+".pass",this.getName()+".diff");
    }
    
    private String dump(ExecutableElement ee) {
        StringBuilder result = new StringBuilder();
        
        for (Modifier m : ee.getModifiers()) {
            result.append(m.toString());
            result.append(' ');
        }
        
        result.append(ee.getReturnType().toString() + " " + ee.toString());
        
        return result.toString();
    }

    private FileObject testSourceFO;
    private JavaSource source;
    
    private void copyToWorkDir(File resource, File toFile) throws IOException {
        //TODO: finally:
        InputStream is = new FileInputStream(resource);
        OutputStream outs = new FileOutputStream(toFile);
        
        int read;
        
        while ((read = is.read()) != (-1)) {
            outs.write(read);
        }
        
        outs.close();
        
        is.close();
    }
    
    private void prepareTest(String fileName) throws Exception {
        SourceUtilsTestUtil.prepareTest(new String[0], new Object[0]);
        
        FileObject scratch = SourceUtilsTestUtil.makeScratchDir(this);
        FileObject cache   = scratch.createFolder("cache");
        
        File wd         = getWorkDir();
        File testSource = new File(wd, "test/" + fileName + ".java");
        
        testSource.getParentFile().mkdirs();
        
        File dataFolder = new File(getDataDir(), "org/netbeans/modules/java/editor/codegen/data/");
        
        for (File f : dataFolder.listFiles()) {
            copyToWorkDir(f, new File(wd, "test/" + f.getName()));
        }
        
        testSourceFO = FileUtil.toFileObject(testSource);
        
        assertNotNull(testSourceFO);
        
        File testBuildTo = new File(wd, "test-build");
        
        testBuildTo.mkdirs();
        
        SourceUtilsTestUtil.prepareTest(FileUtil.toFileObject(dataFolder), FileUtil.toFileObject(testBuildTo), cache);
        SourceUtilsTestUtil.compileRecursively(FileUtil.toFileObject(dataFolder));
        
        source = JavaSource.forFileObject(testSourceFO);
    }
}

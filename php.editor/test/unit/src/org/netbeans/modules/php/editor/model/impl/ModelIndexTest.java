/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.editor.model.impl;

import java.util.ArrayList;
import org.netbeans.modules.php.editor.index.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.text.Document;
import org.netbeans.modules.gsf.api.CancellableTask;
import org.netbeans.modules.gsf.api.CompilationInfo;
import org.netbeans.modules.gsfpath.api.classpath.ClassPath;
import org.netbeans.modules.gsfpath.spi.classpath.support.ClassPathSupport;
import org.netbeans.modules.gsfret.source.usages.RepositoryUpdater;
import org.netbeans.modules.php.editor.PHPLanguage;
import org.netbeans.modules.php.editor.model.ClassScope;
import org.netbeans.modules.php.editor.model.FileScope;
import org.netbeans.modules.php.editor.model.IndexScope;
import org.netbeans.modules.php.editor.model.InterfaceScope;
import org.netbeans.modules.php.editor.model.Model;
import org.netbeans.modules.php.editor.model.ModelElement;
import org.netbeans.modules.php.editor.model.ModelFactory;
import org.netbeans.modules.php.editor.model.ModelUtils;
import org.netbeans.modules.php.editor.model.TypeScope;
import org.netbeans.modules.php.editor.model.impl.ModelTestBase;
import org.netbeans.modules.php.editor.nav.TestUtilities;
import org.netbeans.napi.gsfret.source.ClasspathInfo;
import org.netbeans.napi.gsfret.source.CompilationController;
import org.netbeans.napi.gsfret.source.Phase;
import org.netbeans.napi.gsfret.source.Source;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Radek Matous
 */
public class ModelIndexTest extends ModelTestBase {

    public ModelIndexTest(String testName) {
        super(testName);
    }
    private static final String FOLDER = "GsfPlugins";

    @Override
    public void setUp() throws Exception {
        FileObject f = FileUtil.getConfigFile(FOLDER + "/text/html");

        if (f != null) {
            f.delete();
        }

        FileUtil.setMIMEType("php", PHPLanguage.PHP_MIME_TYPE);
        Logger.global.setFilter(new Filter() {

            public boolean isLoggable(LogRecord record) {
                Throwable t = record.getThrown();

                if (t == null) {
                    return true;
                }

                for (StackTraceElement e : t.getStackTrace()) {
                    if ("org.netbeans.modules.php.editor.index.GsfUtilities".equals(e.getClassName()) && "getBaseDocument".equals(e.getMethodName()) && t instanceof ClassNotFoundException) {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public void testModelFile1() throws Exception {
        final String[] clsNames = {"clsModelTest1", "clsModelTest2"};
        final String[] ifaceNames = {"ifaceModelTest1", "ifaceModelTest2"};
        TestModelTask task = new TestModelTask() {

            @Override
            void testFileScope(Model model, FileScope fScope, IndexScope iScope) {
                assertExactElements(fScope.getDeclaredClasses(), clsNames);
                assertExactElements(fScope.getDeclaredInterfaces(), ifaceNames);
            }

            void testClassScope(Model model, FileScope fileScope, ClassScope clsScope, IndexScope indexScope) {
                assertAnyElement(Collections.singletonList(clsScope), clsNames);
                String name = clsScope.getName();
                if (name.equals(clsNames[0])) {
                    assertExactDeclaredMethods(clsScope, "methodClsModelTest1");
                    assertExactMethods(clsScope, "methodClsModelTest1",
                            "methodIfaceModelTest1");
                } else if (name.equals(clsNames[1])) {
                    assertExactDeclaredMethods(clsScope, "methodClsModelTest2");
                    assertAllMethods(clsScope, "methodClsModelTest1", "methodClsModelTest2",
                            "methodIfaceModelTest1", "methodIfaceModelTest2");
                }
            }

            void testIfaceScope(Model model, FileScope fileScope, InterfaceScope ifaceScope, IndexScope indexScope) {
                assertAnyElement(Collections.singletonList(ifaceScope), ifaceNames);
                String name = ifaceScope.getName();
                if (name.equals(ifaceNames[0])) {
                    assertExactDeclaredMethods(ifaceScope, "methodIfaceModelTest1");
                    assertExactMethods(ifaceScope, "methodIfaceModelTest1");
                } else if (name.equals(ifaceNames[1])) {
                    assertExactDeclaredMethods(ifaceScope, "methodIfaceModelTest2");
                    assertAllMethods(ifaceScope, "methodIfaceModelTest1", "methodIfaceModelTest2");
                }
            }
        };
        performModelTest(task, "testfiles/model/modelfile1.php");
    }

    static <T extends ModelElement> T getFirst(Collection<T> allElements,
            final String... elementName) {
        return ModelUtils.getFirst(allElements, elementName);
    }

    void performModelTest(TestModelTask task, String testFilePath) throws Exception {
        performTest(task, prepareTestFile(testFilePath));
    }

    private static <T extends ModelElement> void assertAnyElement(Collection<? extends ModelElement> allElements, String... elemNames) {
        assertNotEmpty(allElements);
        ModelElement anyElement = null;
        for (int i = 0; i < elemNames.length; i++) {
            ModelElement elem = getFirst(allElements, elemNames[i]);
            if (elem != null) {
                anyElement = elem;
                break;
            }
        }
        assertNotNull(anyElement);
    }

    private static <T extends ModelElement> void assertAllElements(Collection<? extends ModelElement> allElements, String... elemNames) {
        assertNotEmpty(allElements);
        for (int i = 0; i < elemNames.length; i++) {
            ModelElement elem = getFirst(allElements, elemNames[i]);
            assertNotNull(elemNames[i], elem);
        }
    }

    private static <T extends ModelElement> void assertAllMethods(TypeScope typeScope, String... elemNames) {
        assertAllElements(typeScope.getMethods(), elemNames);
    }

    private static <T extends ModelElement> void assertExactMethods(TypeScope typeScope, String... elemNames) {
        assertExactElements(typeScope.getMethods(), elemNames);
    }

    private static <T extends ModelElement> void assertExactDeclaredMethods(TypeScope typeScope, String... elemNames) {
        assertExactElements(typeScope.getDeclaredMethods(), elemNames);
    }

    private static <T extends ModelElement> void assertExactElements(Collection<? extends ModelElement> allElements, String... elemNames) {
        assertNotEmpty(allElements);
        List<ModelElement> testedElems = new ArrayList<ModelElement>();
        for (int i = 0; i < elemNames.length; i++) {
            ModelElement elem = getFirst(allElements, elemNames[i]);
            assertNotNull(elemNames[i], elem);
            testedElems.add(elem);
        }
        List<ModelElement> notTestedElems = new ArrayList<ModelElement>(allElements);
        notTestedElems.removeAll(testedElems);
        assertTrue(notTestedElems.toString(), notTestedElems.isEmpty());
        assertEquals(allElements.size(), elemNames.length);
    }

    private static <T> void assertNotEmpty(final Collection<? extends T> collection) {
        assertNotNull(collection);
        assertFalse(collection.isEmpty());
    }

    abstract class TestModelTask implements CancellableTask<CompilationInfo> {

        public void cancel() {
        }

        public void run(CompilationInfo parameter) throws Exception {
            Model model = ModelFactory.getModel(parameter);
            assertNotNull(model);
            FileScope fileScope = model.getFileScope();
            assertNotNull(fileScope);
            IndexScope indexScope = fileScope.getIndexScope();
            assertNotNull(indexScope);
            testFileScope(model, fileScope, indexScope);
            for (ClassScope classScope : fileScope.getDeclaredClasses()) {
                testClassScope(model, fileScope, classScope, indexScope);
            }
            for (InterfaceScope ifaceScope : fileScope.getDeclaredInterfaces()) {
                testIfaceScope(model, fileScope, ifaceScope, indexScope);
            }
        }

        abstract void testFileScope(Model model, FileScope fileScope, IndexScope indexScope);

        void testClassScope(Model model, FileScope fileScope, ClassScope classScope, IndexScope indexScope) {
        }

        void testIfaceScope(Model model, FileScope fileScope, InterfaceScope classScope, IndexScope indexScope) {
        }
    }

    protected static String computeFileName(int index) {
        return "test" + (index == (-1) ? "" : (char) ('a' + index)) + ".php";
    }

    protected void performTest(final CancellableTask<CompilationInfo> task, String... code) throws Exception {
        clearWorkDir();
        FileUtil.refreshAll();

        FileObject workDir = FileUtil.toFileObject(getWorkDir());
        FileObject cache = workDir.createFolder("cache");
        FileObject folder = workDir.createFolder("src");
        FileObject cluster = workDir.createFolder("cluster");
        int index = -1;

        for (String c : code) {
            FileObject f = FileUtil.createData(folder, computeFileName(index));
            TestUtilities.copyStringToFile(f, c);
            index++;
        }

        System.setProperty("netbeans.user", FileUtil.toFile(cache).getAbsolutePath());
        PHPIndex.setClusterUrl(cluster.getURL().toExternalForm());
        CountDownLatch l = RepositoryUpdater.getDefault().scheduleCompilationAndWait(folder, folder);

        l.await();

        final FileObject test = folder.getFileObject("test.php");

        Document doc = openDocument(test);

        ClassPath empty = ClassPathSupport.createClassPath(new FileObject[0]);
        ClassPath source = ClassPathSupport.createClassPath(folder);
        ClasspathInfo info = ClasspathInfo.create(empty, empty, source);
        Source s = Source.create(info, test);

        s.runUserActionTask(new CancellableTask<CompilationController>() {

            public void cancel() {
            }

            public void run(CompilationController parameter) throws Exception {
                parameter.toPhase(Phase.UP_TO_DATE);

                task.run(parameter);
            }
        }, true);
    }

    private static Document openDocument(FileObject fileObject) throws Exception {
        DataObject dobj = DataObject.find(fileObject);

        EditorCookie ec = dobj.getCookie(EditorCookie.class);

        assertNotNull(ec);

        return ec.openDocument();
    }
}

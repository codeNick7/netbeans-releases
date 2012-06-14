/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.netbeans.api.java.source;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.java.source.parsing.JavacParserResult;
import org.netbeans.modules.parsing.api.ResultIterator;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.api.UserTask;
import org.netbeans.modules.parsing.api.indexing.IndexingManager;
import org.netbeans.modules.parsing.impl.Utilities;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author sdedic
 */
public class ScanUtilsTest extends NbTestCase {
    
    public ScanUtilsTest(String s) {
        super(s);
    }
    
    private static final String REPLACE_PATTERN = "/*TODO:Changed-by-test*/";
    private static final String TEST_FILE_CONTENT=
                "public class {0} '{\n"+
                "   public static void main (String[] args) {\n"+
                "       javax.swing.JTable table = new javax.swing.JTable ();\n"+
                "       Class c = table.getModel().getClass();\n"+
                "       "+REPLACE_PATTERN+"\n"+
                "   }'\n"+
                "   public {1} getOtherClass() '{ return null; }'\n" +
                "}//end'\n";


    private FileObject createTestFile (String className, String otherClassName) {
        try {
            File workdir = this.getWorkDir();
            File root = new File (workdir, "src");
            root.mkdir();
            File data = new File (root, className+".java");

            PrintWriter out = new PrintWriter (new FileWriter (data));
            try {
                out.println(MessageFormat.format(TEST_FILE_CONTENT, new Object[] {className, otherClassName}));
            } finally {
                out.close ();
            }
            return FileUtil.toFileObject(data);
        } catch (IOException ioe) {
            return null;
        }
    }

    private ClassPath createBootPath () throws MalformedURLException {
        String bootPath = System.getProperty ("sun.boot.class.path");
        String[] paths = bootPath.split(File.pathSeparator);
        List<URL>roots = new ArrayList<URL> (paths.length);
        for (String path : paths) {
            File f = new File (path);
            if (!f.exists()) {
                continue;
            }
            URL url = org.openide.util.Utilities.toURI(f).toURL();
            if (FileUtil.isArchiveFile(url)) {
                url = FileUtil.getArchiveRoot(url);
            }
            roots.add (url);
        }
        return ClassPathSupport.createClassPath(roots.toArray(new URL[roots.size()]));
    }

    private ClassPath createCompilePath () {
        return ClassPathSupport.createClassPath(Collections.EMPTY_LIST);
    }

    private ClassPath createSourcePath () throws IOException {
        File workdir = this.getWorkDir();
        File root = new File (workdir, "src");
        if (!root.exists()) {
            root.mkdirs();
        }
        return ClassPathSupport.createClassPath(new URL[] {org.openide.util.Utilities.toURI(root).toURL()});
    }
    
    static class ScannerBlock implements Runnable {
            private final CountDownLatch start;
            private final CountDownLatch latch;

            public ScannerBlock(final CountDownLatch start, final CountDownLatch latch) {
                assert start != null;
                assert latch != null;
                this.start = start;
                this.latch = latch;
            }


            public void run() {
                try {
                    this.start.countDown();
                    this.latch.await();
                    doInScanning();
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
            public void doInScanning() {}
    }
    
    private JavaSource testSource;
    private FileObject  testFile1;
    private FileObject  testFile2;
    
    private void setupTestFile(boolean wait) throws Exception {
        setupTestFile("Test1", wait);
    }
    
    private void setupTestFile(String filename, boolean wait) throws Exception {
        String fn2 = filename + "Other";
        testFile1 = createTestFile(filename, fn2);
        testFile2 = createTestFile(fn2, filename);
        if (wait) {
            IndexingManager.getDefault().refreshIndexAndWait(testFile1.getParent().getURL(), null);
        }

        Thread.sleep (1000); //Indexing task already finished, but we want to wait until JS working thread is waiting on task to dispatch
        final ClassPath bootPath = createBootPath();
        final ClassPath compilePath = createCompilePath();
        final ClassPath srcPath = createSourcePath();
        final ClasspathInfo cpInfo = ClasspathInfo.create(bootPath,compilePath,srcPath);
        testSource = JavaSource.create(cpInfo,testFile1);
    }
    
    public void testRunWhenScanFinished () throws Exception {
        setupTestFile(true);
        
        class T implements Task<CompilationController> {

            private final CountDownLatch latch;

            public T (final CountDownLatch latch) {
                assert latch != null;
                this.latch = latch;
            }

            public void run(CompilationController parameter) throws Exception {
                
                this.latch.countDown();
            }

        };

        CountDownLatch latch = new CountDownLatch (1);
        
        Future res = ScanUtils.postUserActionTask(testSource, new T(latch));
        assertEquals(0,latch.getCount());
        res.get(1,TimeUnit.SECONDS);
        assertTrue(res.isDone());
        assertFalse (res.isCancelled());
    }

    /**
     * Checks that errors raised in a task immediately run by post* method are catched
     * and not propagated to the caller.
     * 
     * @throws Exception 
     */
    public void testIgnoreErrorsInstantPostJava() throws Exception {
        setupTestFile(true);

        Future res = ScanUtils.postUserActionTask(testSource, new Task<CompilationController>() {

            @Override
            public void run(CompilationController parameter) throws Exception {
                throw new RuntimeException("Must be catched and logged");
            }
        });
        // the action should be finished, the error logged, but not thrown
        assertTrue(res.isDone());
    }

    /**
     * Checks that task, which signals incomplete data is deferred and is retried again
     * 
     * @throws Exception 
     */
    public void testSignalPostJava() throws Exception {
        setupTestFile(false);
        
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean passed = new AtomicBoolean(false);
        
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(1);
        
        final ScannerBlock block = new ScannerBlock(start, latch);
        
        Utilities.runAsScanWork(block);
        IndexingManager.getDefault().refreshIndex(testFile1.getParent().getURL(), null);
        
        // wait for the parsing to begin, stop it
        block.start.await();

        Future res = ScanUtils.postUserActionTask(testSource, new Task<CompilationController>() {
            @Override
            public void run(CompilationController parameter) throws Exception {
                count.incrementAndGet();
                ScanUtils.signalIncompleteData(parameter, null);
                passed.set(true);
            }
        });
        // the action should be finished, the error logged, but not thrown
        assertFalse(res.isDone());
        // the task was invoked ONCE
        assertEquals(1, count.intValue());
        
        // free the parser
        block.latch.countDown();
        
        // should not fail !
        res.get(100, TimeUnit.SECONDS);
        
        assertEquals(2, count.intValue());
        assertTrue(passed.get());
    }
    
    /**
     * Checks that wait* method completes immediately if no signal is raised.
     * 
     * @throws Exception 
     */
    public void testInstantWaitJava() throws Exception {
    }
    
    /**
     * Checks that wait() method will wait until scanning finishes on signal raise
     * @throws Exception 
     */
    public void testDeferredWaitJava() throws Exception {
        setupTestFile(false);
        
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AtomicReference<String> error = new AtomicReference<String>();
        
        CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch latch = new CountDownLatch(1);
        
        final ScannerBlock block = new ScannerBlock(start, latch) {

            @Override
            public void doInScanning() {
                // check that the counter is still 1:
                if (count.get() != 1) {
                    error.set("Task was re-run before scan finish");
                }
                try {
                    // block enough for the user task to be rescheduled
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        };
        
        Utilities.runAsScanWork(block);
        IndexingManager.getDefault().refreshIndex(testFile1.getParent().getURL(), null);
        
        // wait for the parsing to begin, stop it
        block.start.await();

        ScanUtils.waitUserActionTask(testSource, new Task<CompilationController>() {
            @Override
            public void run(CompilationController parameter) throws Exception {
                count.incrementAndGet();
                // wake up scanning work
                latch.countDown();
                ScanUtils.signalIncompleteData(parameter, null);
                passed.set(true);
            }
        });
        // the action should be finished, the error logged, but not thrown
        // the task was invoked ONCE
        assertEquals(2, count.intValue());
        assertTrue(passed.get());
        assertNull(error.get(), error.get());
    }
    
    /**
     * Checks that exceptions thrown by user task are propagated to the caller.
     * 
     * @throws Exception 
     */
    public void testDeferredWaitJavaError() throws Exception {
        setupTestFile(false);
        
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AtomicReference<String> error = new AtomicReference<String>();
        
        CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch latch = new CountDownLatch(1);
        
        final ScannerBlock block = new ScannerBlock(start, latch) {

            @Override
            public void doInScanning() {
                // check that the counter is still 1:
                try {
                    // block enough for the user task to be rescheduled
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        };
        
        Utilities.runAsScanWork(block);
        IndexingManager.getDefault().refreshIndex(testFile1.getParent().getURL(), null);
        
        // wait for the parsing to begin, stop it
        block.start.await();

        try {
            ScanUtils.waitUserActionTask(testSource, new Task<CompilationController>() {
                @Override
                public void run(CompilationController parameter) throws Exception {
                    count.incrementAndGet();
                    // wake up scanning work
                    latch.countDown();
                    ScanUtils.signalIncompleteData(parameter, null);
                    passed.set(true);
                    throw new UnsupportedOperationException("Caller should get this one");
                }
            });
            fail("Expected IOException from the failed user task");
        } catch (IOException ex) {
            assertEquals(2, count.intValue());
            assertTrue(passed.get());
            assertNull(error.get(), error.get());
        }
    }
    
    /**
     * Checks that errors are propagated from the restarted task
     * 
     * @throws Exception 
     */
    public void testJavaPostResolveError() throws Exception {
        setupTestFile("TestResolve", false);
        
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AtomicReference<String> error = new AtomicReference<String>();
        
        CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch latch = new CountDownLatch(1);
        
        final ScannerBlock block = new ScannerBlock(start, latch) {

            @Override
            public void doInScanning() {
                // check that the counter is still 1:
                try {
                    // block enough for the user task to be rescheduled
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        };
        
        Utilities.runAsScanWork(block);
        IndexingManager.getDefault().refreshIndex(testFile1.getParent().getURL(), null);
        
        // wait for the parsing to begin, stop it
        block.start.await();

        Future f = ScanUtils.postUserActionTask(testSource, new Task<CompilationController>() {
            @Override
            public void run(CompilationController parameter) throws Exception {
                parameter.toPhase(JavaSource.Phase.RESOLVED);
                count.incrementAndGet();

                // FIXME - re-run on NPE ? Shouldn't !
                TypeElement el = parameter.getElements().getTypeElement("TestResolve");
                ScanUtils.checkElement(parameter, el);

                List<? extends Element> members = el.getEnclosedElements();
                Element e = null;
                for (Element m : members) {
                    if ("getOtherClass".equals(m.getSimpleName())) {
                        e = m;
                        break;
                    }
                }

                ScanUtils.checkElement(parameter, e);

                passed.set(true);
                throw new UnsupportedOperationException("Caller should get this one");
            }
        });
        
        assertEquals(1, count.get());
        assertFalse(f.isDone());
        
        // wake up scanning work
        latch.countDown();

        f.get(30, TimeUnit.SECONDS);
        
        assertEquals(2, count.get());
        assertTrue(passed.get());
    }
    
    /**
     * Checks that errors are propagated from the restarted task
     * 
     * @throws Exception 
     */
    public void testJavaRepeatResolveError() throws Exception {
        setupTestFile("TestResolve", false);
        
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AtomicReference<String> error = new AtomicReference<String>();
        
        CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch latch = new CountDownLatch(1);
        
        final ScannerBlock block = new ScannerBlock(start, latch) {

            @Override
            public void doInScanning() {
                // check that the counter is still 1:
                try {
                    // block enough for the user task to be rescheduled
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        };
        
        Utilities.runAsScanWork(block);
        IndexingManager.getDefault().refreshIndex(testFile1.getParent().getURL(), null);
        
        // wait for the parsing to begin, stop it
        block.start.await();

        try {
            ScanUtils.waitUserActionTask(testSource, new Task<CompilationController>() {
                @Override
                public void run(CompilationController parameter) throws Exception {
                    parameter.toPhase(JavaSource.Phase.RESOLVED);
                    count.incrementAndGet();
                    
                    // FIXME - re-run on NPE ? Shouldn't !
                    TypeElement el = parameter.getElements().getTypeElement("TestResolve");
                    ScanUtils.checkElement(parameter, el);
                    
                    // wake up scanning work
                    latch.countDown();

                    List<? extends Element> members = el.getEnclosedElements();
                    Element e = null;
                    for (Element m : members) {
                        if ("getOtherClass".equals(m.getSimpleName())) {
                            e = m;
                            break;
                        }
                    }
                    
                    ScanUtils.checkElement(parameter, e);
                    
                    passed.set(true);
                    throw new UnsupportedOperationException("Caller should get this one");
                }
            });
            fail("Expected IOException from the failed user task");
        } catch (IOException ex) {
            assertEquals(2, count.intValue());
            assertTrue(passed.get());
            assertNull(error.get(), error.get());
        }
    }
    
    /**
     * Checks that errors are propagated from the restarted task
     * 
     * @throws Exception 
     */
    public void testParsingRepeatResolveError() throws Exception {
        setupTestFile("TestResolve", false);
        
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AtomicReference<String> error = new AtomicReference<String>();
        
        CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch latch = new CountDownLatch(1);
        
        final ScannerBlock block = new ScannerBlock(start, latch) {

            @Override
            public void doInScanning() {
                // check that the counter is still 1:
                try {
                    // block enough for the user task to be rescheduled
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        };
        
        Utilities.runAsScanWork(block);
        IndexingManager.getDefault().refreshIndex(testFile1.getParent().getURL(), null);
        
        // wait for the parsing to begin, stop it
        block.start.await();
        
        Source src = Source.create(testFile1);

        try {
            ScanUtils.waitUserTask(src, new UserTask() {

                @Override
                public void run(ResultIterator resultIterator) throws Exception {

                    count.incrementAndGet();
                    
                    JavacParserResult r = (JavacParserResult)resultIterator.getParserResult();
                    
                    CompilationController parameter = CompilationController.get(r);
                    parameter.toPhase(JavaSource.Phase.RESOLVED);
                    
                    // wake up scanning work
                    latch.countDown();

                    // FIXME - re-run on NPE ? Shouldn't !
                    TypeElement el = parameter.getElements().getTypeElement("TestResolve");
                    
                    ScanUtils.checkElement(parameter, el);
                    
                    List<? extends Element> members = el.getEnclosedElements();
                    
                    Element e = null;
                    for (Element m : members) {
                        if ("getOtherClass".equals(m.getSimpleName())) {
                            e = m;
                            break;
                        }
                    }
                    
                    ScanUtils.checkElement(parameter, e);
                    
                    passed.set(true);
                    throw new UnsupportedOperationException("Caller should get this one");
                }
            });
            fail("Expected IOException from the failed user task");
        } catch (ParseException ex) {
            assertEquals(2, count.intValue());
            assertTrue(passed.get());
            assertNull(error.get(), error.get());
            assertTrue(ex.getCause() instanceof UnsupportedOperationException);
        }
    }
    
    /**
     * Checks that the 'retry context' is properly restored when ScanUtils is invoked
     * recursively.
     * 
     * @throws Exception 
     */
    public void testJavaNestedInvocation() throws Exception {
        setupTestFile("TestResolve", false);
        
        final AtomicInteger count = new AtomicInteger(0);
        final AtomicBoolean passed = new AtomicBoolean(false);
        final AtomicReference<String> error = new AtomicReference<String>();
        
        CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch latch = new CountDownLatch(1);
        
        final ScannerBlock block = new ScannerBlock(start, latch) {

            @Override
            public void doInScanning() {
                // check that the counter is still 1:
                try {
                    // block enough for the user task to be rescheduled
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        };
        
        Utilities.runAsScanWork(block);
        IndexingManager.getDefault().refreshIndex(testFile1.getParent().getURL(), null);
        
        // wait for the parsing to begin, stop it
        block.start.await();

        try {
            ScanUtils.waitUserActionTask(testSource, new Task<CompilationController>() {
                @Override
                public void run(CompilationController parameter) throws Exception {
                    parameter.toPhase(JavaSource.Phase.RESOLVED);
                    count.incrementAndGet();
                    
                    ScanUtils.waitUserActionTask(testSource, new Task<CompilationController>() {
                        @Override
                        public void run(CompilationController parameter) throws Exception {
                            // not important
                        }
                    });
                    
                    // FIXME - re-run on NPE ? Shouldn't !
                    TypeElement el = parameter.getElements().getTypeElement("TestResolve");
                    
                    // this should not fail
                    ScanUtils.checkElement(parameter, el);
                    
                    // wake up scanning work
                    latch.countDown();

                    List<? extends Element> members = el.getEnclosedElements();
                    Element e = null;
                    for (Element m : members) {
                        if ("getOtherClass".equals(m.getSimpleName())) {
                            e = m;
                            break;
                        }
                    }
                    
                    ScanUtils.checkElement(parameter, e);
                    
                    passed.set(true);
                    throw new UnsupportedOperationException("Caller should get this one");
                }
            });
            fail("Expected IOException from the failed user task");
        } catch (IOException ex) {
            assertEquals(2, count.intValue());
            assertTrue(passed.get());
            assertNull(error.get(), error.get());
        }
    }
}

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

package org.netbeans;

import java.io.*;
import java.util.logging.Level;
import org.netbeans.junit.*;
import java.util.*;
import java.util.logging.Logger;
import org.netbeans.CLIHandler.Status;
import org.openide.util.RequestProcessor;

/**
 * Test the command-line-interface handler.
 * @author Jaroslav Tulach
 */
public class CLIHandlerTest extends NbTestCase {

    final static ByteArrayInputStream nullInput = new ByteArrayInputStream(new byte[0]);
    final static ByteArrayOutputStream nullOutput = new ByteArrayOutputStream();
    
    private Logger LOG;

    public CLIHandlerTest(String name) {
        super(name);
    }
    
    protected @Override void setUp() throws Exception {
        LOG = Logger.getLogger("TEST-" + getName());
        
        super.setUp();

        // all handlers shall be executed immediatelly
        CLIHandler.finishInitialization (false);
        
        // setups a temporary file
        String p = getWorkDirPath ();
        if (p == null) {
            p = System.getProperty("java.io.tmpdir");
        }
        File tmp = new File(new File(p), "wd");
        tmp.mkdirs();
        System.getProperties().put("netbeans.user", tmp.getPath());
        
        File f = new File(tmp, "lock");
        if (f.exists()) {
            assertTrue("Clean up previous mess", f.delete());
            assertTrue(!f.exists());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        CLIHandler.stopServer();
    }
    
    protected @Override Level logLevel() {
        return Level.FINEST;
    }

    protected @Override int timeOut() {
        return 50000;
    }
    
    public void testFileExistsButItCannotBeRead() throws Exception {
        // just creates the file and blocks
        InitializeRunner runner = new InitializeRunner(10);
        
        // blocks when operation fails
        InitializeRunner second = new InitializeRunner(85);
        
        for (int i = 0; i < 3; i++) {
            second.next();
        }
        
        // finishes the code
        runner.next();
        
        assertNotNull("Runner succeeded to allocate the file", runner.resultFile());
        
        // let the second code go on as well
        second.next();
        
        assertEquals("The same file has been allocated", runner.resultFile(), second.resultFile());
    }
    
    public void testFileExistsButTheServerCannotBeContacted() throws Exception {
        // start the server and block
        InitializeRunner runner = new InitializeRunner(65);
        
        assertNotNull("File created", runner.resultFile());
        assertTrue("Port allocated", runner.resultPort() != 0);
        
        // blocks when operation fails
        InitializeRunner second = new InitializeRunner(85);
        assertTrue("Fails quickly", second.hasResult());
        assertEquals("Already running, but not replying", Status.CANNOT_CONNECT, second.getExitCode());
    }
    
    public void testFileExistsHasPortButNotTheKey() throws Exception {
        // start the server and block
        Integer block = new Integer(97);
        InitializeRunner runner;
        synchronized (block) {
            runner = new InitializeRunner(block, true);
            // the initialization code can finish without reaching 97
            runner.waitResult();
        }
        
        assertTrue("Port allocated", runner.resultPort() != 0);
        
        // blocks after read the keys from the file
        InitializeRunner second = new InitializeRunner(94);

        // let the CLI Secure Handler finish
        synchronized (block) {
            block.notifyAll();
        }
        // let the test go beyond 97 to the end of file
        assertNotNull("File created", runner.resultFile());
        
        
        // let the second finish
        second.next();
        
        assertEquals("Still the same file", runner.resultFile(), second.resultFile());
        assertEquals("Another port allocated", second.resultPort(), runner.resultPort());
    }

    public void testFileExistsHasPortButAppIsNotActive() throws Exception {
        String tmp = System.getProperty("netbeans.user");

        File f = new File(tmp, "lock");
        if (f.exists()) {
            assertTrue("Clean up previous mess", f.delete());
            assertTrue(!f.exists());
        }

        // write down stupid port number
        FileOutputStream os = new FileOutputStream(f);
        os.write(0);
        os.write(0);
        os.write(80);
        os.write(26);
        os.close();

        // blocks after read the keys from the file
        InitializeRunner second = new InitializeRunner(-1);
        assertTrue("Detects stalled file and initializes the port", second.waitResult());
        assertEquals("Still the same file", f, second.resultFile());
    }
    
    public void testHelpIsPrinted() throws Exception {
        class UserDir extends CLIHandler {
            private int cnt;
            private boolean doCheck;
            
            public UserDir() {
                super(WHEN_BOOT);
            }
            
            protected int cli(Args args) {
                if (!doCheck) {
                    return 0;
                }
                
                cnt++;
                
                for (String a : args.getArguments()) {
                    if ("--help".equals(a)) {
                        return 0;
                    }
                }
                return 5;
            }
            
            protected void usage(PrintWriter w) {
                w.println("this is a help");
            }
        }
        UserDir ud = new UserDir();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        CLIHandler.Status res = cliInitialize(new String[] { "--help" }, new CLIHandler[] { ud }, nullInput, os, nullOutput);
        assertEquals("Help returns 2", 2, res.getExitCode());
        
        if (os.toString().indexOf("help") == -1) {
            fail("There should be some help text:\n" + os);
        }
    }

    public void testHelpIsPassedToRunningServer() throws Exception {
        class UserDir extends CLIHandler implements Runnable {
            private int cnt;
            private int usage;
            private boolean doCheck;
            private CLIHandler.Status res;
            
            public UserDir() {
                super(WHEN_BOOT);
            }
            
            protected int cli(Args args) {
                if (!doCheck) {
                    return 0;
                }
                
                cnt++;
                
                for (String a : args.getArguments()) {
                    if ("--help".equals(a)) {
                        return 0;
                    }
                }
                return 5;
            }
            
            protected void usage(PrintWriter w) {
                usage++;
            }
            
            public void run() {
                res = cliInitialize(new String[] { }, new CLIHandler[] { this }, nullInput, nullOutput, nullOutput);
            }
        }
        UserDir ud = new UserDir();
        
        RequestProcessor.getDefault().post(ud).waitFinished();
        assertNotNull(ud.res);
        
        assertNotNull("File created", ud.res.getLockFile());
        assertTrue("Port allocated", ud.res.getServerPort() != 0);
        
        ud.doCheck = true;
        CLIHandler.Status res = cliInitialize(new String[] { "--help" }, new CLIHandler[0], nullInput, nullOutput, nullOutput);
        
        assertEquals("Ok exec of help", 2, res.getExitCode());
        
        assertEquals("No cli called", 0, ud.cnt);
        assertEquals("Usage called", 1, ud.usage);
        
    }
    
    public void testFileExistsButTheServerCannotBeContactedAndWeDoNotWantToCleanTheFileOnOtherHost() throws Exception {
        // start the server and block
        InitializeRunner runner = new InitializeRunner(65);
        
        assertNotNull("File created", runner.resultFile());
        assertTrue("Port allocated", runner.resultPort() != 0);
        
        File f = runner.resultFile();
        byte[] arr = new byte[(int)f.length()];
        int len = arr.length;
        assertTrue("We know that the size of the file should be int + key_length + 4 for ip address: ", len >=14 && len <= 18);
        FileInputStream is = new FileInputStream(f);
        assertEquals("Fully read", arr.length, is.read(arr));
        is.close();
        
        byte[] altarr = new byte[18];
        for (int i = 0; i < 18; i++) {
            altarr[i] = i<14? arr[i]: 1;
        }
        
        // change the IP at the end of the file
        FileOutputStream os = new FileOutputStream(f);
        os.write(altarr);
        os.close();
        
        CLIHandler.Status res = CLIHandler.initialize(
            new CLIHandler.Args(new String[0], nullInput, nullOutput, nullOutput, ""), 
            null, Collections.<CLIHandler>emptyList(), false, false, null
        );
        
        assertEquals ("Cannot connect because the IP is different", CLIHandler.Status.CANNOT_CONNECT, res.getExitCode());
    }
    
    public void testFileExistsButTheKeyIsNotRecognized() throws Exception {
        // start the server be notified when it accepts connection
        InitializeRunner runner = new InitializeRunner(65);
        
        assertNotNull("File created", runner.resultFile());
        assertTrue("Port allocated", runner.resultPort() != 0);
        
        int s = (int)runner.resultFile().length();
        byte[] copy = new byte[s];
        FileInputStream is = new FileInputStream(runner.resultFile());
        assertEquals("Read fully", s, is.read(copy));
        is.close();
        
        // change one byte in the key
        copy[4 + 2]++;
        
        FileOutputStream os = new FileOutputStream(runner.resultFile());
        os.write(copy);
        os.close();
        
        // try to connect to previous server be notified as soon as it
        // sends request
        InitializeRunner second = new InitializeRunner(30);
        
        // handle the request, say NO
        runner.next();
        
        // read the reply and allocate new port
        second.next();
        
        assertTrue("Execution finished", second.waitResult());
        assertEquals("Cannot connect to server", Status.CANNOT_CONNECT, second.getExitCode());
    }
    
    public void testCLIHandlersCanChangeLocationOfLockFile() throws Exception {
        clearWorkDir();
        final File dir = getWorkDir();
        
        class UserDir extends CLIHandler {
            private int cnt;
            
            public UserDir() {
                super(WHEN_BOOT);
            }
            
            protected int cli(Args args) {
                cnt++;
                System.setProperty("netbeans.user", dir.toString());
                return 0;
            }
            
            protected void usage(PrintWriter w) {}
        }
        UserDir ud = new UserDir();
        
        CLIHandler.Status res = cliInitialize(new String[0], ud, nullInput, nullOutput, nullOutput, null);
        assertNotNull("res: ", res);
        assertEquals("Our command line handler is called once", 1, ud.cnt);
        assertEquals("Lock file is created in dir", dir, res.getLockFile().getParentFile());
    }
    
    public void testCLIHandlerCanStopEvaluation() throws Exception {
        class H extends CLIHandler {
            private int cnt;
            
            public H() {
                super(WHEN_INIT);
            }
            
            protected int cli(Args args) {
                cnt++;
                return 1;
            }
            
            protected void usage(PrintWriter w) {}
        }
        H h1 = new H();
        H h2 = new H();
        
        
        CLIHandler.Status res = cliInitialize(new String[0], new H[] {
            h1, h2
        }, nullInput, nullOutput, nullOutput);
        
        assertEquals("CLI evaluation failed with return code of h1", 1, res.getExitCode());
        assertEquals("First one executed", 1, h1.cnt);
        assertEquals("Not the second one", 0, h2.cnt);
    }

    public void testCannotWrite() throws Exception {
        File tmp = new File(System.getProperty("netbeans.user"));
        tmp.setReadOnly();
        try {
            CLIHandler.Args args = new CLIHandler.Args(new String[0], nullInput, nullOutput, nullOutput, System.getProperty("user.dir"));
            Status res = CLIHandler.initialize(args, null, Collections.<CLIHandler>emptyList(), false, false, null);

            assertEquals("CLI evaluation failed with return code of h1", CLIHandler.Status.CANNOT_WRITE, res.getExitCode());
        } finally {
            cleanUpReadOnly(tmp);
        }
    }

    private void cleanUpReadOnly(File tmp) {
        tmp.setWritable(true);
        for (File f : tmp.listFiles()) {
            if (!f.delete()) {
                f.deleteOnExit();
            }
        }
        if (!tmp.delete()) {
            tmp.deleteOnExit();
        }
    }
    public void testCannotWriteLockFile() throws Exception {
        File tmp = new File(System.getProperty("netbeans.user"));
        File f = new File(tmp, "lock");
        f.createNewFile();
        f.setReadOnly();
        try {
            CLIHandler.Args args = new CLIHandler.Args(new String[0], nullInput, nullOutput, nullOutput, System.getProperty("user.dir"));
            Status res = CLIHandler.initialize(args, null, Collections.<CLIHandler>emptyList(), false, false, null);

            assertEquals("CLI evaluation failed with return code of h1", CLIHandler.Status.CANNOT_WRITE, res.getExitCode());
        } finally {
            cleanUpReadOnly(tmp);
        }
    }
    
    public void testWhenInvokedTwiceParamsGoToTheFirstHandler() throws Exception {
        final String[] template = { "Ahoj", "Hello" };
        final String currentDir = "MyDir";
        
        class H extends CLIHandler {
            private int cnt;
            
            public H() {
                super(WHEN_INIT);
            }
            
            protected int cli(Args args) {
                String[] a = args.getArguments();
                String[] t = template;
                
                assertEquals("Same length", t.length, a.length);
                assertEquals("First is same", t[0], a[0]);
                assertEquals("Second is same", t[1], a[1]);
                assertEquals("Current dir is fine", currentDir, args.getCurrentDirectory().toString());
                return ++cnt;
            }
            
            protected void usage(PrintWriter w) {}
        }
        H h1 = new H();
        
        
        CLIHandler.Status res = cliInitialize(template, h1, nullInput, nullOutput, nullOutput, null, currentDir);
        
        assertEquals("First one executed", 1, h1.cnt);
        assertEquals("CLI evaluation failed with return code of h1", 1, res.getExitCode());
        
        CLIHandler.waitSecureCLIOver();
        
        res = cliInitialize(template, java.util.Collections.<CLIHandler>emptyList(), nullInput, nullOutput, nullOutput, null, currentDir);
        assertEquals("But again executed h1: " + res, 2, h1.cnt);
        assertEquals("Now the result is 2 as cnt++ was increased", 2, res.getExitCode());
    }

    //
    // Utility methods
    //
    
    static CLIHandler.Status cliInitialize(String[] args, CLIHandler handler, InputStream is, OutputStream os, OutputStream err, Integer lock) {
        return cliInitialize(args, handler, is, os, err, lock, System.getProperty ("user.dir"));
    }
    static CLIHandler.Status cliInitialize(String[] args, CLIHandler handler, InputStream is, OutputStream os, OutputStream err, Integer lock, String currentDir) {
        return cliInitialize(args, Collections.nCopies(1, handler), is, os, err, lock, currentDir);
    }
    static CLIHandler.Status cliInitialize(String[] args, CLIHandler[] arr, InputStream is, OutputStream os, OutputStream err) {
        return cliInitialize(args, Arrays.asList(arr), is, os, err, null);
    }
    static CLIHandler.Status cliInitialize(String[] args, List<? extends CLIHandler> coll, InputStream is, OutputStream os, java.io.OutputStream err, Integer lock) {
        return cliInitialize (args, coll, is, os, err, lock, System.getProperty ("user.dir"));
    }
    static CLIHandler.Status cliInitialize(String[] args, List<? extends CLIHandler> coll, InputStream is, OutputStream os, java.io.OutputStream err, Integer lock, String currentDir) {
        return CLIHandler.initialize(new CLIHandler.Args(args, is, os, err, currentDir), lock, coll, false, true, null);
    }
    
    private static final class InitializeRunner extends Object implements Runnable {
        private final Integer block;
        private String[] args;
        private CLIHandler handler;
        private CLIHandler.Status result;
        private boolean noEnd;
        
        public InitializeRunner(int till) throws InterruptedException {
            this(new String[0], null, till);
        }

        public InitializeRunner(int till, boolean noEnd) throws InterruptedException {
            this(new String[0], null, till, noEnd);
        }
        
        public InitializeRunner(Integer till, boolean noEnd) throws InterruptedException {
            this(new String[0], null, till, noEnd);
        }
        public InitializeRunner(String[] args, CLIHandler h, int till) throws InterruptedException {
            this(args, h, new Integer(till));
        }
        public InitializeRunner(String[] args, CLIHandler h, Integer till) throws InterruptedException {
            this(args, h, till, false);
        }

        private InitializeRunner(String[] args, CLIHandler h, Integer till, boolean noEnd) throws InterruptedException {
            this.args = args;
            this.block = till;
            this.handler = h;
            this.noEnd = noEnd;
            
            synchronized (block) {
                new RequestProcessor("InitializeRunner blocks on " + till).post(this);
                block.wait();
            }
        }
        
        public void run() {
            synchronized (block) {
                result = CLIHandler.initialize(
                    new CLIHandler.Args(args, nullInput, nullOutput, nullOutput, ""),
                    block,
                    handler == null ? Collections.<CLIHandler>emptyList() : Collections.nCopies(1, handler),
                    false,
                    true,
                    null
                );
                if (!noEnd) {
                    // we are finished, wake up guys in next() if any
                    block.notifyAll();
                }
            }
            synchronized (this) {
                notifyAll();
            }
        }
        
        /** Executes the code to next invocation */
        public void next() throws InterruptedException {
            synchronized (block) {
                block.notify();
                block.wait();
            }
        }
        
        /** Has already the resutl?
         */
        public boolean hasResult() {
            return result != null;
        }
        
        public int getExitCode() {
            if (result == null) {
                fail("No result produced");
            }
            return result.getExitCode();
        }
        
        public boolean waitResult() throws InterruptedException {
            synchronized (this) {
                for (int i = 0; i < 10; i++) {
                    if (result != null) {
                        return true;
                    }
                    wait(1000);
                }
            }
            fail("No result produced: " + result);
            return true;
        }
        
        /** Gets the resultFile, if there is some,
         */
        public File resultFile() {
            if (result == null) {
                fail("No result produced");
            }
            return result.getLockFile();
        }
        
        /** Gets the port, if there is some,
         */
        public int resultPort() {
            if (result == null) {
                fail("No result produced");
            }
            return result.getServerPort();
        }
    } // end of InitializeRunner
    
}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2014 Sun Microsystems, Inc.
 */
package org.netbeans.modules.masterfs.filebasedfs.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.junit.MockServices;
import org.netbeans.modules.masterfs.providers.Notifier;
import org.netbeans.modules.masterfs.watcher.Watcher;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author jhavlin
 */
public class FileChangedManagerDeadlockTest {

    @Before
    public void setUp() {
        MockServices.setServices(MockNotifier.class);
    }

    @Test
    public void testDeadlockWhenNotifierCallsSecurityManager() throws
            InterruptedException {

        // permits - progress
        // 1 - Priority IO can enter priority section (lock taken by idle IO)
        // 2 - Idle IO can ask for some IO operation (security manager blocked)
        Semaphore s = new Semaphore(0);

        Lookup.getDefault().lookup(MockNotifier.class).setSemaphore(s);

        IdleIO idleIO = new IdleIO(s);
        PriorityIO priorityIO = new PriorityIO(s);

        // Thread 1 - Enter idleIO, add a watch -> wait for priority
        Thread idle = new Thread(idleIO, "Idle IO Thread");

        // Thread 2 - Enter priority IO, wait for watch's lock.
        Thread priority = new Thread(priorityIO, "Priority IO Thread");

        idle.start();
        priority.start();

        idle.join(2000);
        priority.join(2000);

        assertTrue("Idle IO should be finished", idleIO.finished.get());
        assertTrue("Priority IO should be finished", priorityIO.finished.get());
    }

    private static abstract class IORunnable implements Runnable {

        protected Semaphore semaphore;
        final AtomicBoolean finished = new AtomicBoolean(false);

        public IORunnable(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                runInner();
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            finished.set(true);
        }

        public abstract void runInner() throws Exception;

        public void addSomeWatch() {
            String homeDir = System.getProperty("user.home");
            Watcher.register(FileUtil.toFileObject(
                    new File(homeDir).getAbsoluteFile()));
        }
    }

    private static class IdleIO extends IORunnable {

        public IdleIO(Semaphore semaphore) {
            super(semaphore);
        }

        @Override
        public void runInner() {
            FileChangedManager.idleIO(50, new Runnable() {

                @Override
                public void run() {
                    // Add watch in idle thread. This cannot finish, because
                    // it takes Watcher lock and then waits for the priority
                    // thread, which waits for the watcher lock.
                    addSomeWatch();
                }
            }, null, new AtomicBoolean(true));
        }
    }

    private static class PriorityIO extends IORunnable {

        public PriorityIO(Semaphore semaphore) {
            super(semaphore);
        }

        @Override
        public void runInner() throws Exception {

            // Start priority thread after the lock is taken by the idle thread.
            semaphore.acquire(1);
            FileChangedManager.priorityIO(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    // Continue the idle thread. FileChangedManager will now
                    // wait for this priority thread to complete.
                    semaphore.release(2);
                    // But this thread cannot complete, because it needs the
                    // watcher lock which is held by the idle thread.
                    addSomeWatch();
                    return true;
                }
            });
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static class MockNotifier extends Notifier<String> {

        private Semaphore semaphore = null;

        public void setSemaphore(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        /**
         * Simulate a notifier that invokes SecurityManager when adding a watch.
         *
         * @param path
         * @return
         * @throws IOException
         */
        @Override
        protected String addWatch(String path) throws IOException {
            if (semaphore != null) {
                // Idle thread has watcher lock. Priority thread can start.
                semaphore.release(1);
            }
            try {
                // Priority thread has entered the priority section. Idle thread
                // can continue.
                semaphore.acquire(2);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
            FileChangedManager.getInstance().checkRead(path);
            return "path";
        }

        @Override
        protected void removeWatch(String key) throws IOException {
        }

        @Override
        protected synchronized String nextEvent() throws IOException,
                InterruptedException {
            wait(); // wait forever
            return null;
        }

        @Override
        protected void start() throws IOException {
        }
    }
}

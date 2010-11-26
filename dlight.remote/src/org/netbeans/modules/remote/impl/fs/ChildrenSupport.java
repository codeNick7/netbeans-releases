/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.modules.remote.impl.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import org.netbeans.api.extexecution.input.LineProcessor;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.netbeans.modules.nativeexecution.api.util.CommonTasksSupport;
import org.netbeans.modules.nativeexecution.api.util.ConnectionManager;
import org.netbeans.modules.nativeexecution.api.util.ProcessUtils;
import org.netbeans.modules.nativeexecution.api.util.ProcessUtils.ExitStatus;
import org.netbeans.modules.nativeexecution.api.util.ShellScriptRunner;
import org.netbeans.modules.remote.support.RemoteLogger;
import org.openide.util.Exceptions;
import org.openide.util.Parameters;

/**
 * Responsible for copying files from remote host.
 * Each instance of the RemoteFileSupport class corresponds to a remote server
 * 
 * @author Vladimir Kvashin
 */
public class ChildrenSupport {

    /** File transfer statistics */
    private static int fileCopyCount;

    /** Directory synchronization statistics */
    private static int dirSyncCount;

    private final Object mainLock = new Object();
    private Map<File, Object> locks = new HashMap<File, Object>();

    public static final String FLAG_FILE_NAME = ".rfs"; // NOI18N

    private final ExecutionEnvironment execEnv;
    private final RemoteFileSystem fileSystem;

    public ChildrenSupport(RemoteFileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.execEnv = fileSystem.getExecutionEnvironment();
        resetStatistic();
    }

    private Object getLock(File file) {
        synchronized(mainLock) {
            Object lock = locks.get(file);
            if (lock == null) {
                lock = new Object();
                locks.put(file, lock);
            }
            return lock;
        }
    }

    private void removeLock(File file) {
        synchronized(mainLock) {
            locks.remove(file);
        }
    }

    public void ensureFileSync(File file, String remotePath) throws IOException, InterruptedException, ExecutionException, ConnectException {
        if (!file.exists() || file.length() == 0) {
            synchronized (getLock(file)) {
                // dbl check is ok here since it's file-based
                if (!file.exists() || file.length() == 0) {
                    syncFile(file, remotePath); // fromFixedCaseSensitivePathIfNeeded(remotePath));
                    removeLock(file);
                }
            }
        }
    }

    private void syncFile(File file, String remotePath) throws IOException, InterruptedException, ExecutionException, ConnectException {
        RemoteLogger.assertTrue(!file.exists() || file.isFile(), "not a file " + file.getAbsolutePath());
        checkConnection(file, remotePath, false);
        Future<Integer> task = CommonTasksSupport.downloadFile(remotePath, execEnv, file.getAbsolutePath(), null);
        try {
            int rc = task.get().intValue();
            if (rc == 0) {
                fileCopyCount++;
            } else {
                throw new IOException("Can't copy file " + file.getAbsolutePath() + // NOI18N
                        " from " + execEnv + ':' + remotePath + ": rc=" + rc); //NOI18N
            }
        } catch (InterruptedException ex) {
            truncate(file);
            throw ex;
        } catch (ExecutionException ex) {
            truncate(file);
            throw ex;
        }
    }

    private void truncate(File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        os.close();

    }

    private boolean isValidLocalFile(File base, String name) {
        File file = new File(base, name);
        if (file.isAbsolute()) {
            return file.exists();
        } else {
            return false;
        }
    }

    /**
     * Ensured that the directory is synchronized
     */
    public final DirectoryAttributes ensureDirSync(File dir, String remoteDir) throws IOException, ConnectException {
        DirectoryAttributes attrs = null;
        if( ! dir.exists() || ! isValidLocalFile(dir, FLAG_FILE_NAME)) {
            synchronized (getLock(dir)) {
                // dbl check is ok here since it's file-based
                File flagFile = new File(dir, FLAG_FILE_NAME);
                if( ! dir.exists() || ! isValidLocalFile(dir, FLAG_FILE_NAME)) {
                    attrs = syncDirStruct(dir, /*fromFixedCaseSensitivePathIfNeeded(*/remoteDir/*)*/, flagFile);
                    removeLock(dir);
                }
            }
        }
        return attrs;
    }

    @org.netbeans.api.annotations.common.SuppressWarnings("RV") // it's ok to ignore File.createNewFile() return value
    private DirectoryAttributes syncDirStruct(final File dir, final String remoteDir, final File flagFile) throws IOException, ConnectException {
        Parameters.notNull("null remote dir", remoteDir);
        if (dir.exists()) {
            RemoteLogger.assertTrue(dir.isDirectory(), dir.getAbsolutePath() + " is not a directory"); //NOI18N
        }

        final String rdir = remoteDir.length() == 0 ? "/" : remoteDir; // NOI18N
        checkConnection(dir, rdir, true);

        final String script = "test -d \"" + rdir + "\" && " + // NOI18N
                "cd \"" + rdir + "\" &&" + // NOI18N
                "for D in `/bin/ls`; do " + // NOI18N
                "if [ -d \"$D\" ]; then echo D \"$D\"; else if [ -w \"$D\" ]; then echo w \"$D\"; else echo r \"$D\"; fi; fi; done"; // NOI18N

        final AtomicReference<IOException> criticalException = new AtomicReference<IOException>();
        final AtomicReference<IOException> nonCriticalException = new AtomicReference<IOException>();
        final AtomicBoolean dirCreated = new AtomicBoolean(false);
        final DirectoryAttributes attrs = new DirectoryAttributes(flagFile);

        LineProcessor outputProcessor = new LineProcessor() {
            @Override
            public void processLine(String inputLine) {
                if (!dirCreated.get()) {
                    dirCreated.set(true);
                    if (!dir.mkdirs() && !dir.exists()) {
                        criticalException.set(new IOException("Can not create directory " + dir.getAbsolutePath())); //NOI18N
                        return;
                    }
                }
                RemoteLogger.assertTrueInConsole(inputLine.length() > 2, "unexpected file information " + inputLine); // NOI18N
                char mode = inputLine.charAt(0);
                boolean directory = (mode == 'D');
                String fileName = inputLine.substring(2);
                if (directory) {
                    //fileName = fixCaseSensitivePathIfNeeded(fileName);
                    attrs.setWritable(fileName, true);
                } else {
                    attrs.setWritable(fileName, mode == 'w');
                }
                File file = new File(dir, fileName);
                try {
                    RemoteLogger.getInstance().log(Level.FINEST, "\tcreating {0}", fileName); // NOI18N
                    if (directory) {
                        if (!file.mkdirs() && !file.exists()) {
                            throw new IOException("can't create directory " + file.getAbsolutePath()); // NOI18N
                        }
                    } else {
                        file.createNewFile();
                    }
                } catch (IOException ioex) {
                    RemoteLogger.getInstance().log(Level.WARNING,
                            "Error creating {0}{1}{2}: {3}", // NOI18N
                            new Object[]{directory ? "directory" : "file", ' ', file.getAbsolutePath(), ioex.getMessage()}); // NOI18N
                    nonCriticalException.set(ioex);
                }
            }

            @Override
            public void reset() {
            }

            @Override
            public void close() {
            }
        };

        LineProcessor errorProcessor = new LineProcessor() {

            @Override
            public void processLine(String line) {
                RemoteLogger.getInstance().log(Level.FINEST,
                        "Error [{0}]\n\ton Synchronizing dir {1} with {2}{3}{4}", // NOI18N
                        new Object[]{line, dir.getAbsolutePath(), execEnv, ':', rdir});
            }

            @Override
            public void reset() {
            }

            @Override
            public void close() {
            }
        };

        ShellScriptRunner scriptRunner = new ShellScriptRunner(execEnv, script, outputProcessor);
        scriptRunner.setErrorProcessor(errorProcessor);

        RemoteLogger.getInstance().log(Level.FINEST, "Synchronizing dir {0} with {1}{2}{3}", // NOI18N
                new Object[]{dir.getAbsolutePath(), execEnv, ':', rdir});

        int rc = scriptRunner.execute();

        if (nonCriticalException.get() != null) {
            IOException e = nonCriticalException.get();
            IOException ioe = new IOException("Error synchronizing " + rdir + " at " + execEnv.getDisplayName() + ": " + e.getMessage(), e); //NOI18N;
            Exceptions.printStackTrace(ioe);
        }
        if (criticalException.get() != null) {
            IOException e = criticalException.get();
            IOException ioe = new IOException("Error synchronizing " + rdir + " at " + execEnv.getDisplayName() + ": " + e.getMessage(), e); //NOI18N;
            throw ioe;
        }

        if (dirCreated.get()) {
            File flag = new File(dir, FLAG_FILE_NAME);
            RemoteLogger.getInstance().log(Level.FINEST, "Creating Flag file {0}", flag.getAbsolutePath());
            try {
                flag.createNewFile(); // TODO: error processing
            } catch (IOException ie) {
                RemoteLogger.getInstance().log(Level.FINEST, "FAILED creating Flag file {0}", flag.getAbsolutePath());
                criticalException.set(ie);
            }
            dirSyncCount++;
        }
        if (rc == 0) {
            RemoteLogger.assertTrue(flagFile.getParentFile().exists(), "File " + flagFile.getParentFile().getAbsolutePath() + " should exist"); //NOI18N
            attrs.store();
            return attrs;
        } else {
            if (RemoteLogger.getInstance().isLoggable(Level.FINE)) {
                ExitStatus ls = ProcessUtils.execute(execEnv, "/bin/ls", rdir); // NOI18N
                RemoteLogger.getInstance().log(Level.FINE, "Error running script\n{0}\non {1}.\nContent of directory {2}:\n{3}\n",
                        new Object[]{script, execEnv, rdir, ls.output});
            }
            return null;
        }
    }

    /*package-local test method*/ final void resetStatistic() {
        dirSyncCount = 0;
        fileCopyCount = 0;
    }

    /*package-local test method*/ int getDirSyncCount() {
        return dirSyncCount;
    }

    /*package-local test method*/ int getFileCopyCount() {
        return fileCopyCount;
    }

    private void checkConnection(File localFile, String remotePath, boolean isDirectory) throws ConnectException {
        if (!ConnectionManager.getInstance().isConnectedTo(execEnv)) {
            fileSystem.getRemoteFileSupport().addPendingFile(localFile, remotePath, isDirectory);
            throw new ConnectException();
        }
    }
}

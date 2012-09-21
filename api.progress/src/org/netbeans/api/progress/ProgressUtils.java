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

package org.netbeans.api.progress;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.netbeans.modules.progress.spi.RunOffEDTProvider;
import org.netbeans.modules.progress.spi.RunOffEDTProvider.Progress;
import org.netbeans.modules.progress.spi.RunOffEDTProvider.Progress2;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.RequestProcessor.Task;

/**
 * Useful static methods
 * @author Tomas Holy
 * @since 1.16
 */
public final class ProgressUtils {
    private static final RunOffEDTProvider PROVIDER = getProvider();
    private static final int DISPLAY_DIALOG_MS = 9450;
    private static final int DISPLAY_WAIT_CURSOR_MS = 50;

    private ProgressUtils() {
    }

    private static RunOffEDTProvider getProvider() {
        RunOffEDTProvider p = Lookup.getDefault().lookup(RunOffEDTProvider.class);
        return p != null ? p : new Trivial();
    }

    /**
     * Runs operation out of event dispatch thread, blocks UI while operation is in progress. First it shows
     * wait cursor after ~50ms elapses, if operation takes longer than ~10s a dialog with Cancel button is shown.
     * <p>
     * This method is supposed to be used by user invoked foreground actions, that are expected to run very fast in vast majority of cases.
     * However, in some rather rare cases (e.g. extensive IO operations in progress), supplied operation may need longer time. In such case
     * this method first displays wait cursor and if operation takes even more time it displays dialog allowing to cancel operation.
     * DO NOT use this method for operations that may take long time under normal circumstances!
     * @param operation operation to perform
     * @param operationDescr text shown in dialog
     * @param cancelOperation set to true if user canceled the operation
     * @param waitForCanceled true if method should wait until canceled task is finished (if it is not finished in 1s ISE is thrown)
     */
    public static void runOffEventDispatchThread(Runnable operation, String operationDescr, AtomicBoolean cancelOperation, boolean waitForCanceled) {
        PROVIDER.runOffEventDispatchThread(operation, operationDescr, cancelOperation, waitForCanceled, DISPLAY_WAIT_CURSOR_MS, DISPLAY_DIALOG_MS);
    }

    /**
     * Runs operation out of event dispatch thread, blocks UI while operation is in progress. First it shows
     * wait cursor after <i>waitCursorAfter</i> elapses, if operation takes longer than <i>dialogAfter</i> a dialog with Cancel button is shown.
     * <p>
     * This method is supposed to be used by user invoked foreground actions, that are expected to run very fast in vast majority of cases.
     * However, in some rather rare cases (e.g. extensive IO operations in progress), supplied operation may need longer time. In such case
     * this method first displays wait cursor and if operation takes even more time it displays dialog allowing to cancel operation.
     * DO NOT use this method for operations that may take long time under normal circumstances!
     * @param operation operation to perform
     * @param operationDescr text shown in dialog
     * @param cancelOperation set to true if user canceled the operation
     * @param waitForCanceled true if method should wait until canceled task is finished (if it is not finished in 1s ISE is thrown)
     * @param waitCursorAfter time in ms after which wait cursor is shown
     * @param dialogAfter time in ms after which dialog with "Cancel" button is shown
     * @since 1.19
     */
    public static void runOffEventDispatchThread(Runnable operation, String operationDescr, AtomicBoolean cancelOperation, boolean waitForCanceled, int waitCursorAfter, int dialogAfter) {
        PROVIDER.runOffEventDispatchThread(operation, operationDescr, cancelOperation, waitForCanceled, waitCursorAfter, dialogAfter);
    }

    /**
     * Show a modal progress dialog that blocks the main window, while running
     * the passed runnable on a background thread.
     * <p/>
     * This method is thread-safe, and will block until the operation has
     * completed, regardless of what thread calls this method.
     * <p/>
     * The resulting progress UI should show a cancel button if the passed
     * runnable implements org.openide.util.Cancellable.
     * <p/>
     * Unless you are being passed the runnable or progress handle from foreign
     * code (such as in WizardDescriptor.progressInstantiatingIterator), it
     * is usually simpler to use {@link #showProgressDialogAndRun(ProgressRunnable, String, boolean)}.
     *
     * @param operation A runnable to run in the background
     * @param progress A progress handle to create a progress bar for
     * @param includeDetailLabel True if the caller will use
     * ProgressHandle.progress (String, int), false if not.  If true, the
     * created dialog will include a label that shows progress details.
     * @since 1.19
     */
    public static void showProgressDialogAndRun(Runnable operation, ProgressHandle progress, boolean includeDetailLabel) {
        if (PROVIDER instanceof Progress) {
            Progress p = (Progress) PROVIDER;
            p.showProgressDialogAndRun(operation, progress, includeDetailLabel);
        } else {
            PROVIDER.runOffEventDispatchThread(operation, progress.getDisplayName(), new AtomicBoolean(false), false, 0, 0);
        }
    }

    /**
     * Runs operation out of the event thread, blocking the whole UI. When
     * operation takes more than 1s, the method first displays wait cursor.
     * If operation will not end in 3s interval, modal dialog with
     * progress is shown up.
     * If operation is marked with {@link org.openide.util.Cancellable}
     * interface, cancel button is part of dialog and can be used 
     * to interrupt the operation.
     * 
     * @param operation  task to perform in the background
     * @param dialogTitle dialog title
     * @param progress  progress handle. Do not invoke any methods before
     *                  passing to this method. Start/progress/finish it
     *                  only in {@code operation}
     * @param includeDetailLabel  show progress detail label in the dialog
     * @param waitCursorAfter amount of time, in milliseconds, after which wait
     *                        cursor is shown
     * @param dialogAfter amount of time, in milliseconds, after which dialog
     *                    is shown
     * 
     * @since 1.30
     */
    public static void runOffEventThreadWithProgressDialog(
            final Runnable operation,
            final String dialogTitle, 
            final ProgressHandle progress, 
            final boolean includeDetailLabel,
            int waitCursorAfter,
            int dialogAfter)
    {
        if (PROVIDER instanceof Progress2) {
            Progress2 p = (Progress2) PROVIDER;
            p.runOffEventThreadWithProgressDialog(operation, dialogTitle, progress, includeDetailLabel, waitCursorAfter, dialogAfter);
        } else {
            PROVIDER.runOffEventDispatchThread(operation, progress.getDisplayName(),
                    new AtomicBoolean(false),
                    true,
                    DISPLAY_WAIT_CURSOR_MS,
                    DISPLAY_DIALOG_MS);
        }
    }

    /**
     * Runs operation out of the event thread, blocking the whole UI. When
     * operation takes more than 1s, the method first displays wait cursor.
     * If operation will not end up in 3s interval, modal dialog with
     * {@code content} panel is shown.
     * If operation is marked with {@link org.openide.util.Cancellable} 
     * interface, cancel button is part of dialog and can be used to 
     * interrupt the operation.
     * 
     * @param operation  task to perform in the background
     * @param dialogTitle dialog title
     * @param content  panel to be shown in the dialog
     * @param waitCursorAfter amount of time, in milliseconds, after which wait
     *                        cursor is shown
     * @param dialogAfter amount of time, in milliseconds, after which dialog
     *                    is shown
     * 
     * @since 1.30
     */
    public static void runOffEventThreadWithCustomDialogContent(
            final Runnable operation,
            final String dialogTitle,
            final JPanel content,
            int waitCursorAfter,
            int dialogAfter)
    {
        if (PROVIDER instanceof Progress2) {
            Progress2 p = (Progress2) PROVIDER;
            p.runOffEventThreadWithCustomDialogContent(operation, dialogTitle, content, waitCursorAfter, dialogAfter);
        } else {
            PROVIDER.runOffEventDispatchThread(operation, 
                    dialogTitle, 
                    new AtomicBoolean(false),
                    true, 
                    DISPLAY_WAIT_CURSOR_MS,
                    DISPLAY_DIALOG_MS);
        }
    }
    
    /**
     * Show a modal progress dialog that blocks the main window, while running
     * the passed runnable on a background thread.
     * <p/>
     * This method is thread-safe, and will block until the operation has
     * completed, regardless of what thread calls this method.
     * <p/>
     * The resulting progress UI should show a cancel button if the passed
     * runnable implements org.openide.util.Cancellable.
     *
     * @param <T> The result type - use Void if no return type needed
     * @param operation A runnable-like object which performs work in the
     * background, and is passed a ProgressHandle to update progress
     * @param displayName The display name for this operation
     * @param includeDetailLabel If true, include a lable to show progress
     * details (needed only if you plan to call ProgressHandle.setProgress(String, int)
     * @return The result of the operation.
     * @since 1.19
     */
    public static <T> T showProgressDialogAndRun(final ProgressRunnable<T> operation, final String displayName, boolean includeDetailLabel) {
        if (PROVIDER instanceof Progress) {
            Progress p = (Progress) PROVIDER;
            return p.showProgressDialogAndRun(operation, displayName, includeDetailLabel);
        } else {
            final AtomicReference<T> ref = new AtomicReference<T>();
            PROVIDER.runOffEventDispatchThread(new Runnable() {
                @Override
                public void run() {
                    ProgressHandle handle = ProgressHandleFactory.createHandle(displayName);
                    handle.start();
                    handle.switchToIndeterminate();
                    try {
                        ref.set(operation.run(handle));
                    } finally {
                        handle.finish();
                    }
                }
            }, displayName, new AtomicBoolean(false), true, 0, 0);
            return ref.get();
        }
    }



    /**
     * Show a modal progress dialog that blocks the main window, while running
     * the passed runnable on a background thread with an indeterminate-state
     * progress bar.
     * <p/>
     * This method is thread-safe, and will block until the operation has
     * completed, regardless of what thread calls this method.
     * <p/>
     * The resulting progress UI should show a cancel button if the passed
     * runnable implements org.openide.util.Cancellable.
     * .
     * @param operation A runnable to run
     * @param displayName The display name of the operation, to show in the dialog
     * @since 1.19
     */
    public static void showProgressDialogAndRun(Runnable operation, String displayName) {
        showProgressDialogAndRun(new RunnableWrapper(operation), displayName, false);
    }

    /**
     * Show a modal progress dialog that blocks the main window while running
     * a background process.  This call should block until the work is
     * started, and then return a task which can be monitored for completion
     * or cancellation.  This method will not block while the work is run,
     * only until the progress UI is initialized.
     * <p/>
     * The resulting progress UI should show a cancel button if the passed
     * runnable implements org.openide.util.Cancellable.
     *
     * @param operation
     * @param handle
     * @param includeDetailLabel
     * @return
     */
    public static <T> Future<T> showProgressDialogAndRunLater (final ProgressRunnable<T> operation, final ProgressHandle handle, boolean includeDetailLabel) {
        if (PROVIDER instanceof Progress) {
            Progress p = (Progress) PROVIDER;
            return p.showProgressDialogAndRunLater(operation, handle, includeDetailLabel);
        } else {
            FutureTask<T> result = new FutureTask<T>(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return operation.run(handle);
                }
            });
            PROVIDER.runOffEventDispatchThread(result, handle.getDisplayName(), new AtomicBoolean(false), true, 0, 0);
            return result;
        }
    }

    private static final class RunnableWrapper implements ProgressRunnable<Void> {
        private final Runnable toRun;
        RunnableWrapper(Runnable toRun) {
            this.toRun = toRun;
        }

        @Override
        public Void run(ProgressHandle handle) {
            toRun.run();
            return null;
        }
    }

    private static class Trivial implements RunOffEDTProvider {
        private static final RequestProcessor WORKER = new RequestProcessor(ProgressUtils.class.getName());

        @Override
        public void runOffEventDispatchThread(Runnable operation, String operationDescr, AtomicBoolean cancelOperation, boolean waitForCanceled, int waitCursorAfter, int dialogAfter) {
            if (SwingUtilities.isEventDispatchThread()) {
                Task t = WORKER.post(operation);
                t.waitFinished();
            } else {
                operation.run();
            }
        }
    }
}

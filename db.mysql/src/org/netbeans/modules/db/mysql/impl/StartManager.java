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

package org.netbeans.modules.db.mysql.impl;

import org.netbeans.modules.db.mysql.impl.ConnectManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import org.netbeans.api.db.explorer.DatabaseException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.modules.db.mysql.DatabaseServer;
import org.netbeans.modules.db.mysql.impl.MySQLOptions;
import org.netbeans.modules.db.mysql.ui.PropertiesDialog;
import org.netbeans.modules.db.mysql.ui.PropertiesDialog.Tab;
import org.netbeans.modules.db.mysql.util.Utils;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.Mnemonics;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Cancellable;
import org.openide.util.Mutex;
import org.openide.util.Mutex.Action;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

/**
 * Manages starting of a server, including posting messages if start fails
 * and allowing users to edit properties or keep waiting as needed.
 * 
 * @author David Van Couvering
 */
public class StartManager {
    private static final StartManager DEFAULT = new StartManager();
    private static final Logger LOGGER = Logger.getLogger(StartManager.class.getName());

    private PropertyChangeListener listener = new StartPropertyChangeListener();
    private volatile boolean isStarting = false;
    private volatile boolean startRequested = false;
    private DatabaseServer server;

    private StartManager() {
    }

    public static StartManager getDefault() {
        return DEFAULT;
    }

    public PropertyChangeListener getStartListener() {
        return listener;
    }

    private synchronized void setIsStarting(boolean isStarting) {
        this.isStarting = isStarting;
    }

    public void start(final DatabaseServer server) {
        this.server = server;

        RequestProcessor.getDefault().post(new Runnable() {
            public void run() {
                try {
                    startRequested = true;
                    synchronized(this) {
                        if (isStarting) {
                            LOGGER.log(Level.INFO, "Server is already starting");
                            return;
                        }
                        isStarting = true;
                    }
                    StatusDisplayer.getDefault().setStatusText(NbBundle.getMessage(StartManager.class, "MSG_StartingMySQL"));

                    server.start();

                    waitForStartAndConnect();
                } catch (DatabaseException dbe) {
                    Utils.displayError(Utils.getMessage("MSG_UnableToStartServer"), dbe);
                } finally {
                    setIsStarting(false);
                }
            }
            
        });
    }

    private void waitForStartAndConnect() {
        RequestProcessor.getDefault().post(new Runnable() {
            public void run() {
                ProgressHandle handle = ProgressHandleFactory.createHandle(
                        NbBundle.getMessage(StartManager.class, "MSG_WaitingForServerToStart"));
                handle.start();
                handle.switchToIndeterminate();

                try {
                    for ( ; ; ) {
                        if (waitForStart()) {
                            startRequested = false;
                            return;
                        }

                        boolean keepTrying = displayServerNotRunning();
                        if (! keepTrying) {
                            break;
                        }
                    }
                } finally {
                    handle.finish();
                }
            }
        });
    }
    private boolean displayServerNotRunning() {
        JButton cancelButton = new JButton();
        Mnemonics.setLocalizedText(cancelButton, NbBundle.getMessage(StartManager.class, "StartManager.CancelButton")); // NOI18N
        cancelButton.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(StartManager.class, "StartManager.CancelButtonA11yDesc")); //NOI18N

        JButton keepWaitingButton = new JButton();
        Mnemonics.setLocalizedText(keepWaitingButton, NbBundle.getMessage(StartManager.class, "StartManager.KeepWaitingButton")); // NOI18N
        keepWaitingButton.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(StartManager.class, "StartManager.KeepWaitingButtonA11yDesc")); //NOI18N

        JButton propsButton = new JButton();
        Mnemonics.setLocalizedText(propsButton, NbBundle.getMessage(StartManager.class, "StartManager.PropsButton")); // NOI18N
        propsButton.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(StartManager.class, "StartManager.PropsButtonA11yDesc")); //NOI18N

        String message = NbBundle.getMessage(StartManager.class, "MSG_ServerDoesNotAppearToHaveStarted");
        final NotifyDescriptor ndesc = new NotifyDescriptor(message,
                NbBundle.getMessage(StartManager.class, "StartManager.ServerNotRunningTitle"),
                NotifyDescriptor.YES_NO_CANCEL_OPTION,
                NotifyDescriptor.QUESTION_MESSAGE,
                new Object[] {keepWaitingButton, propsButton, cancelButton},
                NotifyDescriptor.CANCEL_OPTION); //NOI18N

        Object ret = Mutex.EVENT.readAccess(new Action<Object>() {
            public Object run() {
                return DialogDisplayer.getDefault().notify(ndesc);
            }

        });

        if (cancelButton.equals(ret)) {
            startRequested = false;
            return false;
        } else if (keepWaitingButton.equals(ret)) {
            return true;
        } else {
            displayAdminProperties(server);
            return false;
        }
    }

    private void displayAdminProperties(final DatabaseServer server)  {
        Mutex.EVENT.postReadRequest(new Runnable() {
            public void run() {
                PropertiesDialog dlg = new PropertiesDialog(server);
                dlg.displayDialog(Tab.ADMIN);
            }
        });
    }

    private boolean waitForStart() {
        int tries = 0;
        while (tries <= 10) {
            tries++;
            if (server.checkRunning()) {
                ConnectManager.getDefault().reconnect(server);
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                return false;
            }
        }

        return false;
    }

    public class StartPropertyChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            final DatabaseServer server = (DatabaseServer)evt.getSource();
            if ((MySQLOptions.PROP_START_ARGS.equals(evt.getPropertyName()) ||
                    MySQLOptions.PROP_START_PATH.equals(evt.getPropertyName()))) {
                RequestProcessor.getDefault().post(new Runnable() {
                    public void run() {
                        // Run in task because checkRunning can't be run on AWT thread
                        if (! server.checkRunning() && startRequested) {
                            start(server);
                        }
                    }
                });
            }
        }
    }
}

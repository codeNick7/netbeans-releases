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
package org.netbeans.modules.javascript.gulp.file;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.util.ChangeSupport;

@MIMEResolver.Registration(displayName = "Gulpfile", resource = "../resources/gulpfile-resolver.xml", position = 122)
public final class Gulpfile {

    private static final Logger LOGGER = Logger.getLogger(Gulpfile.class.getName());

    public static final String FILE_NAME = "gulpfile.js"; // NOI18N

    private final FileObject directory;
    private final FileChangeListener directoryListener = new DirectoryListener();
    private final FileChangeListener gulpfileListener = new GulpfileListener();
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    // @GuardedBy("this")
    private File gulpfile;


    public Gulpfile(FileObject directory) {
        assert directory != null;
        assert directory.isFolder() : "Must be folder: " + directory;
        this.directory = directory;
        this.directory.addFileChangeListener(directoryListener);
    }

    public boolean exists() {
        return getGulpfile().isFile();
    }

    public File getFile() {
        return getGulpfile();
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    private synchronized File getGulpfile() {
        if (gulpfile == null) {
            gulpfile = new File(FileUtil.toFile(directory), FILE_NAME);
            try {
                FileUtil.addFileChangeListener(gulpfileListener, gulpfile);
                LOGGER.log(Level.FINE, "Started listening to {0}", gulpfile);
            } catch (IllegalArgumentException ex) {
                // ignore, already listening
                LOGGER.log(Level.FINE, "Already listening to {0}", gulpfile);
            }
        }
        return gulpfile;
    }

    void reset(boolean newFile) {
        if (newFile) {
            synchronized (this) {
                if (gulpfile != null) {
                    try {
                        FileUtil.removeFileChangeListener(gulpfileListener, gulpfile);
                        LOGGER.log(Level.FINE, "Stopped listening to {0}", gulpfile);
                    } catch (IllegalArgumentException ex) {
                        // not listeneing yet, ignore
                        LOGGER.log(Level.FINE, "Not listening yet to {0}", gulpfile);
                    }
                    gulpfile = null;
                }
            }
        }
        // fire change
        changeSupport.fireChange();
    }

    //~ Inner classes

    private final class DirectoryListener extends FileChangeAdapter {

        @Override
        public void fileRenamed(FileRenameEvent fe) {
            reset(true);
        }

    }

    private final class GulpfileListener extends FileChangeAdapter {

        @Override
        public void fileDataCreated(FileEvent fe) {
            reset(true);
        }

        @Override
        public void fileChanged(FileEvent fe) {
            reset(false);
        }

        @Override
        public void fileDeleted(FileEvent fe) {
            reset(true);
        }

        @Override
        public void fileRenamed(FileRenameEvent fe) {
            reset(true);
        }

    }

}

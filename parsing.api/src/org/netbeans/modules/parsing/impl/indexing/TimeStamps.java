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

package org.netbeans.modules.parsing.impl.indexing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.URLMapper;

/**
 *
 * @author Tomas Zezula
 */
public class TimeStamps {

    private static String TIME_STAMPS_FILE = "timestamps.properties";           //NOI18N

    private Properties props = new Properties();
    private Set<String> unseen = new HashSet<String>();
    private final URL root;
    private FileObject rootFoCache;
    private boolean changed;

    private TimeStamps (final URL root) throws IOException {
        assert root != null;
        this.root = root;
        load ();
    }
    //where
    private void load () throws IOException {
        FileObject cacheDir = getCacheDir();
        FileObject f = cacheDir.getFileObject(TIME_STAMPS_FILE);
        if (f != null) {
            try {
                final InputStream in = f.getInputStream();
                try {
                    props.load(in);
                } finally {
                    in.close();
                }
                for (Object k : props.keySet()) {
                    unseen.add((String)k);
                }
            } catch (IOException e) {
                //In case of IOException props are empty, everything is scanned
                e.printStackTrace();
            }
        }
    }

    public Set<String> store () throws IOException {
        if (true) {
            FileObject cacheDir = getCacheDir();
            FileObject f = FileUtil.createData(cacheDir, TIME_STAMPS_FILE);
            assert f != null;
            try {
                final OutputStream out = f.getOutputStream();
                try {
                    props.keySet().removeAll(unseen);
                    props.store(out, "");
                } finally {
                    out.close();
                }
            } catch (IOException e) {
                //In case of IOException props are not stored, everything is scanned next time
                e.printStackTrace();
            }
        }
        changed = false;
        return this.unseen;
    }

    private FileObject getCacheDir () throws IOException {
        return CacheFolder.getDataFolder(root);
    }

//    public boolean isUpToDate (final File f) {
//        String relative = null;
//        long fts = f.lastModified();
//        String value = (String) props.setProperty(relative,Long.toString(fts));
//        if (value == null) {
//            return false;
//        }
//        unseen.remove(relative);
//        long lts = Long.parseLong(value);
//        return lts >= fts;
//    }

    public boolean isUpToDate (final FileObject f) {
        if (rootFoCache == null) {
            rootFoCache = URLMapper.findFileObject(root);
        }
        String relative = FileUtil.getRelativePath(rootFoCache, f);
        String fileId = relative != null ? relative : URLMapper.findURL(f, URLMapper.EXTERNAL).toExternalForm();
        long fts = f.lastModified().getTime();
        String value = (String) props.setProperty(fileId, Long.toString(fts));
        if (value == null) {
            changed|=true;
            return false;
        }
        unseen.remove(fileId);
        long lts = Long.parseLong(value);
        boolean isUpToDate = lts >= fts;
        changed|=!isUpToDate;
        return isUpToDate;
    }    

    public static TimeStamps forRoot (final URL root) throws IOException {
        assert root != null;
        return new TimeStamps(root);
    }

}

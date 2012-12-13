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
package org.netbeans.modules.masterfs.filebasedfs.fileobjects;

import org.netbeans.modules.masterfs.filebasedfs.*;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.masterfs.filebasedfs.children.ChildrenCache;
import org.netbeans.modules.masterfs.filebasedfs.naming.FileNaming;
import org.netbeans.modules.masterfs.filebasedfs.naming.NamingFactory;
import org.netbeans.modules.masterfs.filebasedfs.utils.FileChangedManager;
import org.netbeans.modules.masterfs.filebasedfs.utils.FileInfo;
import org.netbeans.modules.masterfs.filebasedfs.utils.Utils;
import org.netbeans.modules.masterfs.watcher.Watcher;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Mutex;
import org.openide.util.Utilities;
import org.openide.util.WeakSet;

/**
 * @author Radek Matous
 */
public final class FileObjectFactory {
    public static final Map<File, FileObjectFactory> AllFactories = new HashMap<File, FileObjectFactory>();
    public static boolean WARNINGS = true;
    final Map<Integer, Object> allIBaseFileObjects = Collections.synchronizedMap(new WeakHashMap<Integer, Object>());
    private BaseFileObj root;
    private static final Logger LOG_REFRESH = Logger.getLogger("org.netbeans.modules.masterfs.REFRESH"); // NOI18N

    public static enum Caller {
        ToFileObject, GetFileObject, GetChildern, GetParent, Others
    }
        
    private FileObjectFactory(final File rootFile) {
        this(new FileInfo(rootFile));
    }

    private FileObjectFactory(final FileInfo fInfo) {
        this(fInfo, null);
    }
    
    private FileObjectFactory(FileInfo fInfo, Object msg) {
        final BaseFileObj realRoot = create(fInfo);
        assert realRoot != null : "No fo for " + fInfo + " queried for " + msg;
        root = realRoot;
    }
    
    public static FileObjectFactory getInstance(final File file) {
        return getInstance(file, true);
    }

    public static FileObjectFactory getInstance(final File file, boolean addMising) {
        FileObjectFactory retVal = null;
        final FileInfo rootInfo = new FileInfo(file).getRoot();
        final File rootFile = rootInfo.getFile();

        synchronized (FileObjectFactory.AllFactories) {
            retVal = FileObjectFactory.AllFactories.get(rootFile);
        }
        if (retVal == null && addMising) {
            if (rootInfo.isConvertibleToFileObject()) {
                synchronized (FileObjectFactory.AllFactories) {
                    retVal = FileObjectFactory.AllFactories.get(rootFile);
                    if (retVal == null) {
                        retVal = new FileObjectFactory(new FileInfo(rootFile), file);
                        FileObjectFactory.AllFactories.put(rootFile, retVal);
                    }
                }
            }
        }
        return retVal;
    }

    public static Collection<FileObjectFactory> getInstances() {
        synchronized (FileObjectFactory.AllFactories) {
            return new ArrayList<FileObjectFactory>(AllFactories.values());
        }
    }
    

    public final BaseFileObj getRoot() {
        return root;
    }

    public static int getFactoriesSize() {
        synchronized (FileObjectFactory.AllFactories) {
            return AllFactories.size();
        }
    }

    public int getSize() {
        int retval = 0;

        List<Object> list = new ArrayList<Object>();
        synchronized (allIBaseFileObjects) {
            list.addAll(allIBaseFileObjects.values());
        }
        List<Object> list2 = new ArrayList<Object>();


        for (Object obj : list) {
            if (obj instanceof Reference<?>) {
                list2.add(obj);
            } else {
                list2.addAll((List<?>) obj);
            }
        }

        for (Iterator<Object> it = list2.iterator(); it.hasNext();) {
            @SuppressWarnings("unchecked")
            Reference<FileObject> ref = (Reference<FileObject>) it.next();
            FileObject fo = (ref != null) ? ref.get() : null;
            if (fo != null) {
                retval++;
            }
        }

        return retval;
    }
    
    
    public BaseFileObj getFileObject(FileInfo fInfo, Caller caller) {
        File file = fInfo.getFile();
        FolderObj parent = BaseFileObj.getExistingParentFor(file, this);
        FileNaming child = null;
        boolean isInitializedCache = true;
        if (parent != null) {
            final ChildrenCache childrenCache = parent.getChildrenCache();
            final Mutex.Privileged mutexPrivileged = childrenCache.getMutexPrivileged();
            mutexPrivileged.enterReadAccess();
            try {
                final String nameExt = BaseFileObj.getNameExt(file);
                isInitializedCache = childrenCache.isCacheInitialized();
                child = childrenCache.getChild(nameExt, false);
            } finally {
                mutexPrivileged.exitReadAccess();
            }
        }
        int initTouch = (isInitializedCache) ? -1 : (child != null ? 1 : 0);        
        if (initTouch == -1  && FileBasedFileSystem.isModificationInProgress()) {
            initTouch = file.exists() ? 1 : 0;
        }
        return issueIfExist(file, caller, parent, child, initTouch, !caller.equals(Caller.Others));
    }


    /** 
     * Check cache consistence. It is called only from assertion, so it can be disabled
     * from command line. Given file should or shouldn't exist according to given 
     * expectedExists parameter. If the state is different than expected, it prints
     * warning to console. It can happen when some module doesn't use filesystems API
     * to handle files but directly uses java.io.File.
     * @param expectedExists whether given file is expected to exist
     * @param file checked file
     * @param caller caller of this method
     * @return always true.
     */
    private boolean checkCacheState(boolean expectedExists, File file, Caller caller) {
        if (!expectedExists && (caller.equals(Caller.GetParent) || caller.equals(Caller.ToFileObject))) {
            return true;
        }
        if (isWarningEnabled() && caller != null && !caller.equals(Caller.GetChildern)) {
            boolean realExists = file.exists();
            boolean notsame = expectedExists != realExists;
            if (notsame) {
                if (!realExists) {
                    //test for broken symlinks
                    File p = file.getParentFile();
                    if (p != null) {
                        File[] children = p.listFiles();
                        if (children != null && Arrays.asList(children).contains(file)) {
                            return true;
                        }
                    }                    
                }
                printWarning(file);
            }
        }
        return true;
    }

    private Integer initRealExists(int initTouch) {
        final Integer retval = new Integer(initTouch);
        return retval;
    }

    private void printWarning(File file) {
        StringBuilder sb = new StringBuilder("WARNING(please REPORT):  Externally ");
        sb.append(file.exists() ? "created " : "deleted "); //NOI18N
        sb.append(file.isDirectory() ? "folder: " : "file: "); //NOI18N
        sb.append(file.getAbsolutePath());
        sb.append(" (Possibly not refreshed FileObjects when external command finished.");//NOI18N
        sb.append(" For additional information see: http://wiki.netbeans.org/wiki/view/FileSystems)");//NOI18N
        IllegalStateException ise = new IllegalStateException(sb.toString());
        // set to INFO level to be notified about possible problems.
        Logger.getLogger(getClass().getName()).log(Level.FINE, ise.getMessage(), ise);
    }

    private BaseFileObj issueIfExist(File file, Caller caller, final FileObject parent, FileNaming child, int initTouch,boolean asyncFire) {
        boolean exist = false;
        BaseFileObj foForFile = null;
        Integer realExists = initRealExists(initTouch);
        final FileChangedManager fcb = FileChangedManager.getInstance();

        //use cached info as much as possible + do refresh if something is wrong
        //exist = (parent != null) ? child != null : (((foForFile = get(file)) != null && foForFile.isValid()) || touchExists(file, realExists));
        foForFile = getCachedOnly(file);
        if (parent != null && parent.isValid()) {
            if (child != null) {
                if (foForFile == null) {
                    exist = (realExists == -1) ? true : touchExists(file, realExists);
                    if (fcb.impeachExistence(file, exist)) {
                        exist = touchExists(file, realExists);
                        if (!exist) {
                            refreshFromGetter(parent,asyncFire);
                        }
                    }
                    assert checkCacheState(true, file, caller);
                } else if (foForFile.isValid()) {
                    exist = (realExists == -1) ? true : touchExists(file, realExists);
                    if (fcb.impeachExistence(file, exist)) {
                        exist = touchExists(file, realExists);
                        if (!exist) {
                            refreshFromGetter(parent,asyncFire);
                        }
                    }
                    assert checkCacheState(exist, file, caller);
                } else {
                    //!!!!!!!!!!!!!!!!! inconsistence
                    exist = touchExists(file, realExists);
                    if (!exist) {
                        refreshFromGetter(parent,asyncFire);
                    }
                }
            } else {
                if (foForFile == null) {
                    exist = (realExists == -1) ? false : touchExists(file, realExists);
                    if (fcb.impeachExistence(file, exist)) {
                        if (exist != touchExists(file, realExists)) {
                            exist = !exist;
                            refreshFromGetter(parent, asyncFire);
                        }
                    }
                    assert checkCacheState(exist, file, caller);
                } else if (foForFile.isValid()) {
                    //!!!!!!!!!!!!!!!!! inconsistence
                    exist = touchExists(file, realExists);
                    if (!exist) {
                        refreshFromGetter(foForFile,asyncFire);
                    }
                } else {
                    exist = touchExists(file, realExists);
                    if (exist) {
                        refreshFromGetter(parent,asyncFire);
                    }
                }
            }
        } else {
            if (foForFile == null) {
                if (caller == Caller.GetParent) {
                    exist = true;
                } else {
                    exist = touchExists(file, realExists);
                }
            } else if (foForFile.isValid()) {
                if (parent == null) {
                    exist = (realExists == -1) ? true : touchExists(file, realExists);
                    if (fcb.impeachExistence(file, exist)) {
                        exist = touchExists(file, realExists);
                        if (!exist) {
                            refreshFromGetter(foForFile,asyncFire);
                        }
                    }
                    assert checkCacheState(exist, file, caller);
                } else {
                    //!!!!!!!!!!!!!!!!! inconsistence
                    exist = touchExists(file, realExists);
                    if (!exist) {
                        refreshFromGetter(foForFile,asyncFire);
                    }
                }
            } else {
                exist = (realExists == -1) ? false : touchExists(file, realExists);
                if (fcb.impeachExistence(file, exist)) {
                    exist = touchExists(file, realExists);
                }
                assert checkCacheState(exist, file, caller);
            }
        }
        if (!exist) {
            switch (caller) {
                case GetParent:
                    //guarantee issuing parent
                    BaseFileObj retval = null;
                    if (foForFile != null && !foForFile.isRoot()) {
                        retval = foForFile;
                    } else {
                        retval = getOrCreate(new FileInfo(file, 1));
                    }
                    if (retval instanceof BaseFileObj && retval.isValid()) {
                        exist = touchExists(file, realExists);
                        if (!exist) {
                            //parent is exception must be issued even if not valid
                            retval.setValid(false);
                        }
                    }
                    assert checkCacheState(exist, file, caller);
                    return retval;
                case ToFileObject:
                    //guarantee issuing for existing file
                    exist = touchExists(file, realExists);
                    if (exist && parent != null && parent.isValid()) {
                        refreshFromGetter(parent,asyncFire);
                    }
                    assert checkCacheState(exist, file, caller);
                    break;
            }
        }
        //ratio 59993/507 (means 507 touches for 59993 calls)
        return (exist) ? getOrCreate(new FileInfo(file, 1)) : null;
    }

    private static boolean touchExists(File f, Integer state) {
        if (state == -1) {
            state = FileChangedManager.getInstance().exists(f) ? 1 : 0;
        }
        assert state != -1;
        return (state == 1) ? true : false;
    }

    private BaseFileObj getOrCreate(final FileInfo fInfo) {
        BaseFileObj retVal = null;
        File f = fInfo.getFile();

        boolean issue45485 = fInfo.isWindows() && f.getName().endsWith(".") && !f.getName().matches("[.]{1,2}");//NOI18N
        if (issue45485) {
            File f2 = FileUtil.normalizeFile(f);
            issue45485 = !f2.getName().endsWith(".");
            if (issue45485) {
                return null;
            }
        }
        synchronized (allIBaseFileObjects) {
            retVal = this.getCachedOnly(f);
            if (retVal == null || !retVal.isValid()) {
                final File parent = f.getParentFile();
                if (parent != null) {
                    retVal = this.create(fInfo);
                } else {
                    retVal = this.getRoot();
                }

            }
            return retVal;
        }
    }

    private BaseFileObj create(final FileInfo fInfo) {
        if (!fInfo.isConvertibleToFileObject()) {
            return null;
        }

        final File file = fInfo.getFile();
        FileNaming name = fInfo.getFileNaming();
        name = (name == null) ? NamingFactory.fromFile(file) : name;

        if (name == null) {
            return null;
        }

        if (name.isFile() && !name.isDirectory()) {
            final FileObj realRoot = new FileObj(file, name);
            return putInCache(realRoot, realRoot.getFileName().getId());
        }

        if (!name.isFile() && name.isDirectory()) {
            final FolderObj realRoot = new FolderObj(file, name);
            return putInCache(realRoot, realRoot.getFileName().getId());
        }

        assert false;
        return null;
    }

    final void refreshAll(RefreshSlow slow, boolean ignoreRecursiveListeners, boolean expected) {
        Set<BaseFileObj> all2Refresh = collectForRefresh(ignoreRecursiveListeners);
        refresh(all2Refresh, slow, expected);
    }

    private Set<BaseFileObj> collectForRefresh(boolean noRecListeners) {
        final Set<BaseFileObj> all2Refresh;
        synchronized (allIBaseFileObjects) {
            all2Refresh = new WeakSet<BaseFileObj>(allIBaseFileObjects.size() * 3 + 11);
            final Iterator<Object> it = allIBaseFileObjects.values().iterator();
            while (it.hasNext()) {
                final Object obj = it.next();
                if (obj instanceof List<?>) {
                    for (Iterator<?> iterator = ((List<?>) obj).iterator(); iterator.hasNext();) {
                        @SuppressWarnings("unchecked")
                        WeakReference<BaseFileObj> ref = (WeakReference<BaseFileObj>) iterator.next();
                        BaseFileObj fo = shallBeChecked(
                            ref != null ? ref.get() : null, noRecListeners
                        );
                        if (fo != null) {
                            all2Refresh.add(fo);
                        }
                    }
                } else {
                    @SuppressWarnings("unchecked")
                    final WeakReference<BaseFileObj> ref = (WeakReference<BaseFileObj>) obj;
                    BaseFileObj fo = shallBeChecked(
                        ref != null ? ref.get() : null, noRecListeners
                    );
                    if (fo != null) {
                        all2Refresh.add(fo);
                    }
                }
            }
        }
        all2Refresh.remove(root); // #182793
        return all2Refresh;
    }

    private BaseFileObj shallBeChecked(BaseFileObj fo, boolean noRecListeners) {
        if (fo != null && noRecListeners) {
            FolderObj p = (FolderObj) (fo instanceof FolderObj ? fo : fo.getExistingParent());
            if (p != null && Watcher.isWatched(fo)) {
                LOG_REFRESH.log(Level.FINER, "skip: {0}", fo);
                fo = null;
            }
        }
        return fo;
    }

    private boolean refresh(final Set<BaseFileObj> all2Refresh, RefreshSlow slow, File... files) {
        return refresh(all2Refresh, slow, true, files);
    }

    private static boolean isInFiles(BaseFileObj fo, File[] files) {
        if (fo == null) {
            return false;
        }
        if (files == null) {
            return true;
        }
        for (File file : files) {
            if (isParentOf(file, fo.getFileName().getFile())) {
                return true;
            }
        }
        return false;
    }

    private boolean refresh(final Set<BaseFileObj> all2Refresh, RefreshSlow slow, final boolean expected) {
        return refresh(all2Refresh, slow, expected, null);
    }

    private boolean refresh(final Set<BaseFileObj> all2Refresh, RefreshSlow slow, final boolean expected, File[] files) {
        int add = 0;
        Iterator<BaseFileObj> it = all2Refresh.iterator();
        while (it.hasNext()) {
            BaseFileObj fo = null;
            if (slow != null) {
                BaseFileObj pref = slow.preferrable();
                if (pref != null && all2Refresh.remove(pref)) {
                    LOG_REFRESH.log(Level.FINER, "Preferring {0}", pref);
                    fo = pref;
                    it = all2Refresh.iterator();
                }
            }
            if (fo == null) {
                fo = it.next();
                it.remove();
            }
            add++;
            if (!isInFiles(fo, files)) {
                continue;
            }
            if (slow != null) {
                if (!slow.refreshFileObject(fo, expected, add)) {
                    return false;
                }
                add = 0;
            } else {
                fo.refresh(expected);
            }
        }
        return true;
    }

    public static boolean isParentOf(final File dir, final File file) {
        File tempFile = file;
        while (tempFile != null && !Utils.equals(tempFile, dir)) {
            tempFile = tempFile.getParentFile();
        }
        return tempFile != null;
    }

    public final void rename(Set<BaseFileObj> changeId) {
        final Map<Integer, Object> toRename = new HashMap<Integer, Object>();
        synchronized (allIBaseFileObjects) {
            final Iterator<Map.Entry<Integer, Object>> it = allIBaseFileObjects.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Object> entry = it.next();
                final Object obj = entry.getValue();
                final Integer key = entry.getKey();
                if (!(obj instanceof List<?>)) {
                    @SuppressWarnings("unchecked")
                    final WeakReference<BaseFileObj> ref = (WeakReference<BaseFileObj>) obj;

                    final BaseFileObj fo = (ref != null) ? ref.get() : null;
                    if (changeId.contains(fo)) {
                        toRename.put(key, fo);
                    }
                } else {
                    for (Iterator<?> iterator = ((List<?>) obj).iterator(); iterator.hasNext();) {
                        @SuppressWarnings("unchecked")
                        WeakReference<BaseFileObj> ref = (WeakReference<BaseFileObj>) iterator.next();
                        final BaseFileObj fo = (ref != null) ? ref.get() : null;
                        if (changeId.contains(fo)) {
                            toRename.put(key, ref);
                        }
                    }

                }
            }

            for (Map.Entry<Integer, Object> entry : toRename.entrySet()) {
                Integer key = entry.getKey();
                Object previous = allIBaseFileObjects.remove(key);
                if (previous instanceof List<?>) {
                    List<?> list = (List<?>) previous;
                    list.remove(entry.getValue());
                    allIBaseFileObjects.put(key, previous);
                } else {
                    BaseFileObj bfo = (BaseFileObj) entry.getValue();
                    putInCache(bfo, bfo.getFileName().getId());
                }
            }
        }
    }

    public final BaseFileObj getCachedOnly(final File file) {
        return getCachedOnly(file, true);
    }
    public final BaseFileObj getCachedOnly(final File file, boolean checkExtension) {
        BaseFileObj retval;
        synchronized (allIBaseFileObjects) {
            final Object value = allIBaseFileObjects.get(NamingFactory.createID(file));
            if (value instanceof Reference<?>) {
                retval = getReference(Collections.nCopies(1, value), file);
            } else {
                retval = getReference((List<?>) value, file);
            }
        }
        if (retval != null && checkExtension) {
            if (!file.getName().equals(retval.getNameExt())) {
                if (!Utils.equals(file, retval.getFileName().getFile())) {
                    retval = null;
                }
            }
        }
        return retval;
    }

    private static BaseFileObj getReference(final List<?> list, final File file) {
        BaseFileObj retVal = null;
        if (list != null) {
            for (int i = 0; retVal == null && i < list.size(); i++) {
                Object item = list.get(i);
                if (!(item instanceof Reference<?>)) {
                    continue;
                }
                final Object ce = ((Reference<?>)item).get();
                if (!(ce instanceof BaseFileObj)) {
                    continue;
                }
                final BaseFileObj cachedElement = (BaseFileObj)ce;
                if (cachedElement != null && cachedElement.getFileName().getFile().compareTo(file) == 0) {
                    retVal = cachedElement;
                }
            }
        }
        return retVal;
    }

    private BaseFileObj putInCache(final BaseFileObj newValue, final Integer id) {
        synchronized (allIBaseFileObjects) {
            final WeakReference<BaseFileObj> newRef = new WeakReference<BaseFileObj>(newValue);
            final Object listOrReference = allIBaseFileObjects.put(id, newRef);

            if (listOrReference != null) {
                if (listOrReference instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<Reference<BaseFileObj>> list = (List<Reference<BaseFileObj>>) listOrReference;
                    list.add(newRef);
                    allIBaseFileObjects.put(id, listOrReference);
                } else {
                    assert (listOrReference instanceof WeakReference<?>);
                    @SuppressWarnings("unchecked")
                    final Reference<BaseFileObj> oldRef = (Reference<BaseFileObj>) listOrReference;
                    BaseFileObj oldValue = (oldRef != null) ? oldRef.get() : null;

                    if (oldValue != null && !newValue.getFileName().equals(oldValue.getFileName())) {
                        final List<Reference<BaseFileObj>> l = new ArrayList<Reference<BaseFileObj>>();
                        l.add(oldRef);
                        l.add(newRef);
                        allIBaseFileObjects.put(id, l);
                    }
                }
            }
        }

        return newValue;
    }

    @Override
    public String toString() {
        List<Object> list = new ArrayList<Object>();
        synchronized (allIBaseFileObjects) {
            list.addAll(allIBaseFileObjects.values());
        }
        List<String> l2 = new ArrayList<String>();
        for (Iterator<Object> it = list.iterator(); it.hasNext();) {
            @SuppressWarnings("unchecked")
            Reference<FileObject> ref = (Reference<FileObject>) it.next();
            FileObject fo = (ref != null) ? ref.get() : null;
            if (fo != null) {
                l2.add(fo.getPath());
            }
        }
        return l2.toString();
    }

    public static synchronized Map<File, FileObjectFactory> factories() {
        return new HashMap<File, FileObjectFactory>(AllFactories);
    }

    public boolean isWarningEnabled() {
        return WARNINGS && !Utilities.isMac();
    }

    //only for tests purposes
    public static void reinitForTests() {
        FileObjectFactory.AllFactories.clear();
    }



    public final BaseFileObj getValidFileObject(final File f, FileObjectFactory.Caller caller) {
        final BaseFileObj retVal = (getFileObject(new FileInfo(f), caller));
        return (retVal != null && retVal.isValid()) ? retVal : null;
    }

    public void refresh(boolean expected) {
        refresh(null, expected);
    }
    void refresh(RefreshSlow slow, boolean expected) {
        refresh(slow, false, expected);
    }
    final void refresh(final RefreshSlow slow, final boolean ignoreRecursiveListeners, final boolean expected) {
        Statistics.StopWatch stopWatch = Statistics.getStopWatch(Statistics.REFRESH_FS);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                refreshAll(slow, ignoreRecursiveListeners, expected);
            }            
        };        
        
        stopWatch.start();
        try {
            if (slow != null) {
                FileBasedFileSystem.runAsInconsistent(r);
            } else {
                FileBasedFileSystem.getInstance().runAtomicAction(new FileSystem.AtomicAction() {
                    @Override
                    public void run() throws IOException {
                        FileBasedFileSystem.runAsInconsistent(r);
                    }
                });
            }
        } catch (IOException iex) {/*method refreshAll doesn't throw IOException*/

        }
        stopWatch.stop();

        // print refresh stats unconditionally in trunk
        if (LOG_REFRESH.isLoggable(Level.FINE)) {
            LOG_REFRESH.log(
                Level.FINE,
                "FS.refresh statistics ({0}FileObjects):\n  {1}\n  {2}\n  {3}\n  {4}\n",
                new Object[]{
                    Statistics.fileObjects(),
                    Statistics.REFRESH_FS.toString(),
                    Statistics.LISTENERS_CALLS.toString(),
                    Statistics.REFRESH_FOLDER.toString(),
                    Statistics.REFRESH_FILE.toString()
                }
            );
        }

        Statistics.REFRESH_FS.reset();
        Statistics.LISTENERS_CALLS.reset();
        Statistics.REFRESH_FOLDER.reset();
        Statistics.REFRESH_FILE.reset();
    }

    final void refreshFor(final RefreshSlow slow, final boolean ignoreRecursiveListeners, final File... files) {
        Statistics.StopWatch stopWatch = Statistics.getStopWatch(Statistics.REFRESH_FS);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                Set<BaseFileObj> all2Refresh = collectForRefresh(ignoreRecursiveListeners);
                refresh(all2Refresh, slow, files);
                if (LOG_REFRESH.isLoggable(Level.FINER)) {
                    LOG_REFRESH.log(Level.FINER, "Refresh for {0} objects", all2Refresh.size());
                    for (BaseFileObj baseFileObj : all2Refresh) {
                        LOG_REFRESH.log(Level.FINER, "  {0}", baseFileObj);
                    }
                }
            }
        };        
        stopWatch.start();
        try {
            if (slow != null) {
                FileBasedFileSystem.runAsInconsistent(r);
            } else {
                FileBasedFileSystem.getInstance().runAtomicAction(new FileSystem.AtomicAction() {
                    @Override
                    public void run() throws IOException {
                        FileBasedFileSystem.runAsInconsistent(r);
                    }
                });
            }
        } catch (IOException iex) {/*method refreshAll doesn't throw IOException*/

        }
        stopWatch.stop();

        // print refresh stats unconditionally in trunk
        if (LOG_REFRESH.isLoggable(Level.FINE)) {
            LOG_REFRESH.log(
                Level.FINE,
                "FS.refresh statistics ({0}FileObjects):\n  {1}\n  {2}\n  {3}\n  {4}\n",
                new Object[]{
                    Statistics.fileObjects(),
                    Statistics.REFRESH_FS.toString(),
                    Statistics.LISTENERS_CALLS.toString(),
                    Statistics.REFRESH_FOLDER.toString(),
                    Statistics.REFRESH_FILE.toString()
                }
            );
        }

        Statistics.REFRESH_FS.reset();
        Statistics.LISTENERS_CALLS.reset();
        Statistics.REFRESH_FOLDER.reset();
        Statistics.REFRESH_FILE.reset();
    }
    
    private static class AsyncRefreshAtomicAction implements  FileSystem.AtomicAction {
        private FileObject fo;
        AsyncRefreshAtomicAction(FileObject fo) {
            this.fo = fo;
        }
        @Override
        public void run() throws IOException {
            this.fo.refresh();
        }
        
    }

    private void refreshFromGetter(final FileObject parent,boolean asyncFire)  {
        try {
            if (asyncFire) {
                FileUtil.runAtomicAction(new AsyncRefreshAtomicAction(parent));
            } else {
                parent.refresh();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}

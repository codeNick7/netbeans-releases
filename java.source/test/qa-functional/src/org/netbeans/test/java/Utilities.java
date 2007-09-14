/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

/*
 * Utilities.java
 *
 * Created on September 30, 2002, 4:34 PM
 */

package org.netbeans.test.java;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import javax.swing.text.StyledDocument;
import org.netbeans.junit.AssertionFailedErrorException;
import org.openide.actions.SaveAllAction;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;


/**
 *
 * @author  jb105785
 */
public class Utilities {
    
    public static void saveAll() {
        ((SaveAllAction) SaveAllAction.findObject(SaveAllAction.class, true)).performAction();
    }
    
    public static void delete(File file) {
        try {
            DataObject.find(FileUtil.toFileObject(file)).delete();
        } catch (IOException e) {
        }
    }
    
    public static File copyTo(File file, File destination) {
        try {
            FileObject src=FileUtil.toFileObject(file);
            FileObject dest=FileUtil.toFileObject(destination);
            DataObject.find(src).copy(DataFolder.findFolder(dest));
            return new File(destination, file.getName());
        } catch (IOException e) {
            throw new AssertionFailedErrorException(e);
        }
    }
    
    public static String getAsString(File file) {
        String result;
        try {
            FileObject testFile = FileUtil.toFileObject(file);
            DataObject DO = DataObject.find(testFile);
            
            EditorCookie ec=(EditorCookie)(DO.getCookie(EditorCookie.class));
            StyledDocument doc=ec.openDocument();
            result=doc.getText(0, doc.getLength());
        } catch (Exception e){
            throw new AssertionFailedErrorException(e);
        }
        return result;
    }
    
    private static String[] allFileNames(final File f, final Vector v, boolean comp, final boolean recurse, final boolean fullName, final FilenameFilter filter) {
        String[] files;
        if(filter != null) {
            files = f.list(filter);
        }
        else {
            files = f.list();
        }
        
        String path = f.getPath();
        if(!path.endsWith(File.separator)) {
            path += File.separatorChar;
        }
        for(int i = 0; i < files.length; i++) {
            String addElement;
            if(fullName) {
                addElement = path + files[i];
            }
            else {
                addElement = files[i];
            }
            
            v.addElement(addElement);
        }
        if(recurse) {
            String[] dirs = f.list(new FilenameFilter()	{
                public boolean accept(File f, String name){
                    return (new File(f.getPath() + File.separatorChar + name).isDirectory());
                }
            });
            for(int i = 0; i < dirs.length; i++) {
                File newF = new File(path + dirs[i]);
                allFileNames(newF, v, false, true, fullName, filter);
            }
        }
        else {
            comp = true;
        }
        if(comp) {
            String[] strs = new String[v.size()];
            v.copyInto(strs);
            return strs;
        }
        return null;
    }
    
    public static String[] getAllFilenames(File initialDirectory, boolean recurse, final String filter) {
        FilenameFilter f = new FilenameFilter() {
            public boolean accept(File f, String name) {
                return (name.indexOf(filter) > 0);
            }};
            
            if(!initialDirectory.isDirectory()) {
                return new String[0];
            }
            return allFileNames(initialDirectory, new Vector(), true, recurse, true, f);
    }
    
    /**
     * Deletes a directory recursively
     * @param path path to directory for deletion
     * @return was the direcotory deleted?
     */
    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
    
     /**
     * Sleeps for waitTimeout miliseconds to avoid incorrect test failures.
     */
    public static void takeANap(int waitTimeout) {
        new org.netbeans.jemmy.EventTool().waitNoEvent(waitTimeout);
    }    
        
}

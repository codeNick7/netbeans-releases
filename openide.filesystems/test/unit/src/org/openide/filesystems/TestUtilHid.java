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

package org.openide.filesystems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.openide.util.BaseUtilities;

/**
 * @author  rm111737
 */
public class TestUtilHid {
    private static int cnt = 0;
    private static NumberFormat cntFormat = new DecimalFormat("000");
    public  static final File locationOfTempFolder (String name) throws IOException {
        name += cntFormat.format(cnt++);
        String property = System.getProperty("workdir");

        File workdir = (property == null) ? null: new File (property);
        File tmpdir = (workdir != null) ? workdir : new File(System.getProperty("java.io.tmpdir"), "fstests");
        tmpdir.mkdirs();
        if (!tmpdir.isDirectory()) throw new IOException("Could not make: " + tmpdir);
        File tmp = File.createTempFile(name,null, tmpdir);
        tmp.delete();
        tmp = new File(tmp.getParent(),name);
        return tmp;
    }

    /** @return  URL to folder where should be placed tested data */
    public final static URL getResourceContext () {
        //System.out.println("getResourceContext: " + FileSystemFactoryHid.class.getResource("../data"));
        return FileSystemFactoryHid.class.getResource("../data/");
    }
    
    /** It may be helpful to delete resursively Files */
    public final static boolean  deleteFolder (File file)  throws IOException{
        boolean ret = file.delete();
        
        if (ret) {
            return true;
        }
        
        if (! file.exists()) {
            return false;
        }
        
        if (file.isDirectory()) {
            // first of all delete whole content
            File[] arr = file.listFiles();
            for (int i = 0; i < arr.length; i++) {
                if (deleteFolder (arr[i]) != true) {
                    throw new IOException ("Cannot delete: "+ arr[i]);
                    //return false;
                }
            }
        }
        
        return (file.delete() ? true : false);
    }    
    
    /**
     * XXX this method should be package-private: non-FS-testing code should instead use
     * {@link NbTestCase#getWorkDir} and call either
     * {@link #createLocalFileSystem(File,String[])}
     * or use FileUtil.toFileObject with masterfs in CP
     */
    public final static FileSystem createLocalFileSystem(String testName, String[] resources) throws IOException {
        File mountPoint = locationOfLFSTempFolder(testName);
        return createLocalFileSystem(mountPoint, resources);

    }

    public static FileSystem createLocalFileSystem(File mountPoint, String[] resources) throws IOException {
        mountPoint.mkdir();
        
        for (int i = 0; i < resources.length; i++) {                        
            File f = new File (mountPoint,resources[i]);
            if (f.isDirectory() || resources[i].endsWith("/")) {
                f.mkdirs();
            }
            else {
                f.getParentFile().mkdirs();
                try {
                    f.createNewFile();
                } catch (IOException iex) {
                    throw new IOException ("While creating " + resources[i] + " in " + mountPoint.getAbsolutePath() + ": " + iex.toString() + ": " + f.getAbsolutePath() + " with resource list: " + Arrays.asList(resources));
                }
            }
        }
        
        LocalFileSystem lfs = new StatusFileSystem();
        try {
        lfs.setRootDirectory(mountPoint);
        } catch (Exception ex) {}
        
        return lfs;
    }

    public static File locationOfLFSTempFolder(String testName) throws IOException {        
        File mountPoint = TestUtilHid.locationOfTempFolder("lfstest");
        return mountPoint;
    }

    public final static  void destroyLocalFileSystem (String testName) throws IOException {            
        File mountPoint = TestUtilHid.locationOfTempFolder("lfstest");
        
        if (mountPoint.exists()) {
                if (TestUtilHid.deleteFolder(mountPoint) == false)
                    throw new IOException("Cannot delete test folder: " + mountPoint.toString());
        }
        
    }

    public final static void destroyXMLFileSystem(String testName) throws IOException {    
        File tempFile = TestUtilHid.locationOfTempFolder("xfstest");
        File xmlFile = new File (tempFile,"xfstest.xml");
        if (xmlFile.exists()) 
            xmlFile.delete();                            
    }
    
    
    public final static FileSystem createXMLFileSystem(String testName, String[] resources) throws IOException{
        File xmlFile = createXMLLayer(testName, resources);

        XMLFileSystem xfs = new XMLFileSystem  ();
        try {
            xfs.setXmlUrl(BaseUtilities.toURI(xmlFile).toURL());
        } catch (Exception ex) {}
        
        return xfs;
    }
    
    public final static FileSystem createXMLFileSystem(String testName, Resource rootResource) throws IOException {
        File xmlFile = createXMLLayer(testName, rootResource);
        
        XMLFileSystem xfs = new XMLFileSystem  ();
        try {
            xfs.setXmlUrl(BaseUtilities.toURI(xmlFile).toURL());
        } catch (Exception ex) {}
        
        return xfs;
    }
    
    public static File createXMLLayer(String testName, Resource rootResource) throws IOException {
        File tempFile = TestUtilHid.locationOfTempFolder("xfstest");
        tempFile.mkdir();
        
        File xmlFile = new File (tempFile,"xfstest.xml");
        if (!xmlFile.exists()) {
            xmlFile.getParentFile().mkdirs();
            xmlFile.createNewFile();
        } 
        FileOutputStream xos = new FileOutputStream (xmlFile);        
        PrintWriter pw = new PrintWriter (xos); 
        pw.println("<filesystem>");
        rootResource.serialize("", "  ", pw, xmlFile.getParentFile());
        pw.println("</filesystem>");       
        pw.close();
        return xmlFile;
    }

    public static File createXMLLayer(String testName, String[] resources) throws IOException {
        Resource root =  createRoot();
        
        for (int i = 0; i < resources.length; i++)                         
            root.add (resources[i]);
        
        return createXMLLayer(testName, root);
    }
    
    public static Resource createRoot() {
        return new Resource("");
    }

    private  static void testStructure (PrintWriter pw,ResourceElement[] childern,String tab) {
        for (int i = 0; i < childern.length;i++) {
            ResourceElement[] sub = childern[i].getChildren ();
            if (sub.length != 0)
                pw.println(tab+"<folder name=\""+childern[i].getName ()+"\">" );            
            else
                pw.println(tab+"<file name=\""+childern[i].getName ()+"\">" );                            
            
            testStructure (pw,sub, tab+"  ");            
            
            if (sub.length != 0)
                pw.println(tab+"</folder>" );            
            else
                pw.println(tab+"</file>" );                            
        }
    }
    
    static class ResourceElement {
        String element;
        ResourceElement (String element) {
            //System.out.println(element);
            this.element = element;
        }
        Map<String,ResourceElement> children = new HashMap<String,ResourceElement> ();
        Resource add (String resource) {
            return add (new StringTokenizer (resource,"/"));
        }
        
        Resource add (Enumeration en) {
            //StringTokenizer tokens = StringTokenizer (resource);
            if (en.hasMoreElements()) {
                String chldElem = (String)en.nextElement();
                Resource child = (Resource)children.get(chldElem);
                if (child == null)
                    child = new Resource(chldElem);
                children.put (chldElem,child);
                return child.add (en);                
            } else {
                return (Resource)this;
            }
        }
        ResourceElement[] getChildren () {
            int i = 0;
            ResourceElement[] retVal =  new ResourceElement[children.entrySet().size()];
            Iterator it = children.entrySet().iterator();
            while (it.hasNext()) {
                retVal[i++] = (ResourceElement)((Map.Entry)it.next()).getValue();
            }
                        
            return retVal;
        }
        
        String getName () {
            return element;
        }
        
        boolean isLeaf() {
            return children.isEmpty();
        }
        
        public void serialize(String path, String tab, PrintWriter pw, File baseFolder) throws IOException {
            
        }
    }    

    static class StatusFileSystem extends LocalFileSystem {
        StatusDecorator status = new StatusDecorator () {
            public String annotateName (String name, java.util.Set files) {
                return name;
            }

            @Override
            public String annotateNameHtml(String name, Set<? extends FileObject> files) {
                return name;
            }

        };        
        
        public StatusDecorator getDecorator() {
            return status;
        }
        
    }
    
    public static class Resource extends ResourceElement {
        private Map<String, String> attributeTypes = new HashMap<String, String>();
        private Map<String, String> attributeContents = new HashMap<String, String>();
        private CharSequence fileContents = null;
        private URL contentURL = null;
        private boolean forceFolder;
        
        public Resource(String element) {
            super(element);
        }
        
        public Resource addAttribute(String name, String type, String valueContent) {
            attributeTypes.put(name, type);
            attributeContents.put(name, valueContent);
            
            return this;
        }
        
        public Resource withContent(CharSequence seq) {
            this.fileContents = seq;
            
            return this;
        }
        
        public Resource withContentAt(URL contentURL) {
            this.contentURL = contentURL;
            return this;
        }
        
        public Resource forceFolder() {
            this.forceFolder = true;
            return this;
        }
        
        public boolean isLeaf() {
            return !forceFolder && super.isLeaf();
        }
        
        public void serialize(String path, String tab, PrintWriter pw, File baseFolder) throws IOException {
            if (!getName().isEmpty()) {
                String n = getName();
                if (isLeaf()) {
                    pw.print(tab+"<file name=\""+n + "\"");
                } else {
                    pw.print(tab+"<folder name=\""+n + "\"");
                }
                String urlVal = null;

                if (fileContents != null) {
                    urlVal = (path + getName()).replaceAll("/", "-");
                    File f = new File(baseFolder, urlVal);
                    FileWriter wr = new FileWriter(f);
                    wr.append(fileContents);
                    wr.close();
                } else if (contentURL != null) {
                    urlVal = contentURL.toExternalForm();
                }
                if (urlVal != null) {
                    pw.print(" url=\"" + urlVal + "\"");
                }
                pw.print(">");

                for (String s : attributeContents.keySet()) {
                    pw.print("\n" + tab + "    <attr name=\"" + s + "\" " + 
                            attributeTypes.get(s) + "=\"" + attributeContents.get(s) + "\"/>");
                }
                pw.println();
            }
            String newPath = path + getName();
            if (!newPath.isEmpty()) {
                newPath = newPath + "/";
            }
            for (ResourceElement res : children.values()) {
                res.serialize(newPath, tab + "  ", pw, baseFolder);
            }
            if (!getName().isEmpty()) {
                if (isLeaf()) {
                    pw.println(tab+"</file>" );                            
                } else {
                    pw.println(tab+"</folder>" );            
                }
            }
        }
    }
}

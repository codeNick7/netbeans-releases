/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2002 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.xml.api.model;

import java.beans.FeatureDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import junit.framework.*;
import org.netbeans.core.modules.ProxyClassLoader;
import org.netbeans.junit.*;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.Repository;
import org.openide.filesystems.XMLFileSystem;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.FolderLookup;
import org.openide.util.Lookup;
import org.openide.util.enum.ArrayEnumeration;
import org.openide.util.enum.EmptyEnumeration;
import org.xml.sax.InputSource;

/**
 * The test always fails because you cannot register
 * test class in lookup using layer. I would like to
 * see FolderLookup tests for system FS.
 * <p>
 * Last test version passes but kills system class loader.
 *
 * @author Petr Kuzel
 */
public class GrammarQueryManagerTest extends NbTestCase {
    
    public GrammarQueryManagerTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new NbTestSuite(GrammarQueryManagerTest.class);
        
        return suite;
    }
    
    /** Test of getDefault method, of class org.netbeans.modules.xml.text.completion.api.GrammarQueryManager. */
    public void testGetDefault() throws Exception {
        System.out.println("testGetDefault");

        FileSystem def = Repository.getDefault().getDefaultFileSystem();
        org.netbeans.core.projects.SystemFileSystem system =
            (org.netbeans.core.projects.SystemFileSystem) def;
        FileSystem[] original = system.getLayers();
        ProxyClassLoader testLoader = new ProxyClassLoader(
            new ClassLoader[] {SampleGrammarQueryManager.class.getClassLoader()}
        );
                
        try {
            
            // modify default FS content
            
            URL source = getClass().getResource("data/filesystem.xml");
            XMLFileSystem xmlfs = new XMLFileSystem(source);        
            List layers = new ArrayList(Arrays.asList(original));            
            layers.add(xmlfs);
            FileSystem[] fss = (FileSystem[]) layers.toArray(new FileSystem[0]);
            system.setLayers(fss);
            
            // adjust system classloader
            
            ClassLoader loader = (ClassLoader) Lookup.getDefault().lookup(ClassLoader.class);
            ProxyClassLoader systemLoader = (ProxyClassLoader) loader;
            systemLoader.append(new ClassLoader[] {testLoader});
            
            // test
            
            GrammarQueryManager manager = GrammarQueryManager.getDefault();
            GrammarEnvironment env = new GrammarEnvironment(EmptyEnumeration.EMPTY, new InputSource(), null);
            Enumeration trigger = manager.enabled(env);
            assertTrue("No grammar found!", trigger!=null);
        } finally {
            
            // rollback
            
            system.setLayers(original);
            
            testLoader.destroy();
        }        
    }
    
}

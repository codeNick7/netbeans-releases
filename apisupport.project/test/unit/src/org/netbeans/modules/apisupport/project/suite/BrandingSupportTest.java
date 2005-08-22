/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2005 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.apisupport.project.suite;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.modules.apisupport.project.ManifestManager;
import org.netbeans.modules.apisupport.project.TestBase;
import org.netbeans.modules.apisupport.project.suite.SuiteProject;
import org.netbeans.modules.apisupport.project.suite.SuiteProjectGenerator;
import org.netbeans.modules.apisupport.project.suite.BrandingSupport.BundleKey;
import org.netbeans.modules.apisupport.project.universe.ModuleEntry;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Radek Matous
 */
public class BrandingSupportTest extends TestBase {
    private BrandingSupport instance = null;
    
    public BrandingSupportTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        File suiteDir  = new File(getWorkDir(), "testSuite");
        SuiteProjectGenerator.createSuiteModule(suiteDir, "default");
        FileObject fo = FileUtil.toFileObject(suiteDir);
        SuiteProject suitePrj = (SuiteProject) ProjectManager.getDefault().findProject(fo);
        assertNotNull(suitePrj);
        
        instance = BrandingSupport.getInstance(suitePrj);
        
    }
            
    public void testBranding1() throws IOException {
        assertFalse(instance.getBrandingRoot().exists());
        Set keys = new HashSet(Arrays.asList(new String[]{"CTL_About_Title"}));
        implOfBundleKeyTest("org.netbeans.core.startup", "org/netbeans/core/startup/Bundle.properties",keys, "About");
    }
    
    public void testBranding2() throws IOException {
        assertFalse(instance.getBrandingRoot().exists());
        Set keys = new HashSet(Arrays.asList(new String[]{"CTL_MainWindow_Title"}));
        implOfBundleKeyTest("org.netbeans.core.windows","org/netbeans/core/windows/view/ui/Bundle.properties", keys, "NetBeans Platform {0}");
    }
    

    private void implOfBundleKeyTest(final String moduleCodeNameBase, final String bundleEntry, final Set keys, String expectedValue) throws IOException {
        Set bKeys = instance.getBundleKeys(moduleCodeNameBase,bundleEntry,keys);

        assertNotNull(bKeys);
        assertEquals(1, bKeys.size());

        BrandingSupport.BundleKey bKey = (BrandingSupport.BundleKey) bKeys.toArray()[0];
        assertFalse(instance.isBranded(bKey));
        assertFalse(instance.isBranded(bKey.getModuleEntry()));
        assertFalse(instance.getBrandingRoot().exists());
        assertFalse(instance.getModuleEntryDirectory(bKey.getModuleEntry()).exists());
        assertNotNull(instance.getListOfBrandedBundleKeys(bKey.getModuleEntry()));
        assertFalse(instance.getListOfBrandedBundleKeys(bKey.getModuleEntry()).contains(bKey));
        assertEquals(expectedValue, bKey.getValue());

        instance.brandBundleKeys(bKeys);
        assertFalse(instance.isBranded(bKey));
        assertFalse(instance.isBranded(bKey.getModuleEntry()));
        assertFalse(instance.getBrandingRoot().exists());
        assertFalse(instance.getModuleEntryDirectory(bKey.getModuleEntry()).exists());        
        assertNotNull(instance.getListOfBrandedBundleKeys(bKey.getModuleEntry()));
        assertFalse(instance.getListOfBrandedBundleKeys(bKey.getModuleEntry()).contains(bKey));        
        assertEquals(expectedValue, bKey.getValue());


        bKey.setValue("brandedValue");
        instance.brandBundleKeys(bKeys);
        assertTrue(instance.isBranded(bKey));
        assertTrue(instance.isBranded(bKey.getModuleEntry()));
        assertTrue(instance.getBrandingRoot().exists());
        assertTrue(instance.getModuleEntryDirectory(bKey.getModuleEntry()).exists());        
        assertNotNull(instance.getListOfBrandedBundleKeys(bKey.getModuleEntry()));
        assertTrue(instance.getListOfBrandedBundleKeys(bKey.getModuleEntry()).contains(bKey));        
        assertEquals("brandedValue", bKey.getValue());
    }
        
}

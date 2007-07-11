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
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 */
package org.netbeans.test.subversion.main.properties;

import java.io.File;
import java.io.PrintStream;
import junit.textui.TestRunner;
import org.netbeans.jellytools.JellyTestCase;
import org.netbeans.jellytools.NbDialogOperator;
import org.netbeans.jellytools.OutputTabOperator;
import org.netbeans.jellytools.nodes.Node;
import org.netbeans.jellytools.nodes.SourcePackagesNode;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.junit.NbTestSuite;
import org.netbeans.test.subversion.operators.CheckoutWizardOperator;
import org.netbeans.test.subversion.operators.RepositoryStepOperator;
import org.netbeans.test.subversion.operators.SvnPropertiesOperator;
import org.netbeans.test.subversion.operators.VersioningOperator;
import org.netbeans.test.subversion.operators.WorkDirStepOperator;
import org.netbeans.test.subversion.utils.RepositoryMaintenance;
import org.netbeans.test.subversion.utils.TestKit;

/**
 *
 * @author novakm
 */
public class SvnPropertiesTest extends JellyTestCase {

    public static final String TMP_PATH = "/tmp";
    public static final String REPO_PATH = "repo";
    public static final String WORK_PATH = "work";
    public static final String PROJECT_NAME = "JavaApp";
    public File projectPath;
    public PrintStream stream;
    String os_name;

    public SvnPropertiesTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        os_name = System.getProperty("os.name");
        //System.out.println(os_name);
        System.out.println("### " + getName() + " ###");
    }

    protected boolean isUnix() {
        boolean unix = false;
        if (os_name.indexOf("Windows") == -1) {
            unix = true;
        }
        return unix;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        TestRunner.run(suite());
    }

    public static NbTestSuite suite() {
        NbTestSuite suite = new NbTestSuite();
        suite.addTest(new SvnPropertiesTest("SvnPropertiesTest"));
        return suite;
    }

    public void SvnPropertiesTest() throws Exception {
        try {
            TestKit.closeProject(PROJECT_NAME);

            stream = new PrintStream(new File(getWorkDir(), getName() + ".log"));
            VersioningOperator vo = VersioningOperator.invoke();
            CheckoutWizardOperator co = CheckoutWizardOperator.invoke();
            RepositoryStepOperator rso = new RepositoryStepOperator();

            //create repository...
            File work = new File(TMP_PATH + File.separator + WORK_PATH + File.separator + "w" + System.currentTimeMillis());
            new File(TMP_PATH).mkdirs();
            work.mkdirs();
            RepositoryMaintenance.deleteFolder(new File(TMP_PATH + File.separator + REPO_PATH));
            RepositoryMaintenance.createRepository(TMP_PATH + File.separator + REPO_PATH);
            RepositoryMaintenance.loadRepositoryFromFile(TMP_PATH + File.separator + REPO_PATH, getDataDir().getCanonicalPath() + File.separator + "repo_dump");
            rso.setRepositoryURL(RepositoryStepOperator.ITEM_FILE + RepositoryMaintenance.changeFileSeparator(TMP_PATH + File.separator + REPO_PATH, false));

            rso.next();
            WorkDirStepOperator wdso = new WorkDirStepOperator();
            wdso.setRepositoryFolder("trunk/" + PROJECT_NAME);
            wdso.setLocalFolder(work.getCanonicalPath());
            wdso.checkCheckoutContentOnly(false);
            wdso.finish();
            
            //open project
            OutputTabOperator oto = new OutputTabOperator("file:///tmp/repo");
            oto.getTimeouts().setTimeout("ComponentOperator.WaitStateTimeout", 30000);
            oto.waitText("Checking out... finished.");
            NbDialogOperator nbdialog = new NbDialogOperator("Checkout Completed");
            JButtonOperator open = new JButtonOperator(nbdialog, "Open Project");
            open.push();

            TestKit.waitForScanFinishedAndQueueEmpty();

            oto = new OutputTabOperator("file:///tmp/repo");
            oto.getTimeouts().setTimeout("ComponentOperator.WaitStateTimeout", 30000);
            oto.clear();
            
            // set svnProperty for file
            Node node = new Node(new SourcePackagesNode(PROJECT_NAME), "javaapp|Main.java");
            SvnPropertiesOperator spo = SvnPropertiesOperator.invoke(node);
            spo.typePropertyName("fileName");
            spo.typePropertyValue("fileValue");
            spo.add();
            Thread.sleep(500);
            assertEquals("Wrong row count of table.", 1, spo.propertiesTable().getRowCount());
            assertFalse("Recursively checkbox should be disabled on file! ", spo.cbRecursively().isEnabled());
            Thread.sleep(500);
            spo.cancel();
            Thread.sleep(500);
            
            //  set svnProperty for folder - one recursive and one nonrecursive
            node = new Node(new SourcePackagesNode(PROJECT_NAME), "javaapp");
            spo = SvnPropertiesOperator.invoke(node);
            assertTrue("Recursively checkbox should be enabled on package! ", spo.cbRecursively().isEnabled());
            spo.checkRecursively(false);
            spo.typePropertyName("nonrecursiveName");
            spo.typePropertyValue("nonrecursiveValue");
            spo.add();
            Thread.sleep(500);
            spo.checkRecursively(true);
            spo.typePropertyName("recursiveName");
            spo.typePropertyValue("recursiveValue");
            spo.add();
            Thread.sleep(500);
            assertEquals("Wrong row count of table.", 2, spo.propertiesTable().getRowCount());
            spo.cancel();
            
            //  verify whether the recursive property is present on file
            node = new Node(new SourcePackagesNode(PROJECT_NAME), "javaapp|Main.java");
            spo = SvnPropertiesOperator.invoke(node);
            Thread.sleep(500);
            assertEquals("Wrong row count of table.", 2, spo.propertiesTable().getRowCount());
            assertEquals("Expected file is missing.", "recursiveName", spo.propertiesTable().getModel().getValueAt(1, 0).toString());
            spo.propertiesTable().selectCell(1, 0);
            spo.remove();
            Thread.sleep(500);
            assertEquals("Wrong row count of table.", 1, spo.propertiesTable().getRowCount());
            spo.cancel();
        } catch (Exception e) {
            throw new Exception("Test failed: " + e);
        } finally {
            TestKit.closeProject(PROJECT_NAME);
        }
    }
}

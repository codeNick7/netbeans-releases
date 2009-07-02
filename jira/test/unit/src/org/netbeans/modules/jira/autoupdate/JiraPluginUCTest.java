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

package org.netbeans.modules.jira.autoupdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import org.eclipse.mylyn.internal.jira.core.model.JiraVersion;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;
import org.netbeans.core.startup.MainLookup;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.autoupdate.updateprovider.AutoupdateCatalogProvider;
import org.openide.util.Lookup;

/**
 *
 * @author tomas
 */
public class JiraPluginUCTest extends NbTestCase {
    
    private static File catalogFile;
    private static URL catalogURL;
    protected boolean modulesOnly = true;
    protected List<UpdateUnit> keepItNotToGC;
    private static String CATALOG_CONTENTS_FORMAT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<!DOCTYPE module_updates PUBLIC \"-//NetBeans//DTD Autoupdate Catalog 2.6//EN\" \"http://www.netbeans.org/dtds/autoupdate-catalog-2_6.dtd\">" +
                "<module_updates timestamp=\"20/02/13/01/07/2009\">" +
                    "<module " +
                        "autoload=\"false\" " +
                        "codenamebase=\"{0}\" " +
                        "distribution=\"modules/extra/org-netbeans-libs-jira.nbm\" " +
                        "downloadsize=\"2546568\" " +
                        "eager=\"false\" " +
                        "homepage=\"http://www.netbeans.org/\" " +
                        "license=\"BE94B573\" " +
                        "moduleauthor=\"\" " +
                        "needsrestart=\"false\" " +
                        "releasedate=\"2009/05/27\">" +
                    "<manifest " +
                        "AutoUpdate-Show-In-Client=\"true\" " +
                        "OpenIDE-Module=\"org.netbeans.libs.jira\" " +
                        "OpenIDE-Module-Display-Category=\"Libraries\" " +
                        "OpenIDE-Module-Implementation-Version=\"090527\" " +
                        "OpenIDE-Module-Java-Dependencies=\"Java &gt; 1.5\" " +
                        "OpenIDE-Module-Long-Description=\"This module bundles the JIRA connector implementation\" " +
                        "OpenIDE-Module-Module-Dependencies=\"org.jdesktop.layout/1 &gt; 1.6, " +
                                                             "org.netbeans.libs.bugtracking &gt; 1.0, " +
                                                             "org.netbeans.libs.commons_logging/1 &gt; 1.7, " +
                                                             "org.openide.awt &gt; 7.3, " +
                                                             "org.openide.dialogs &gt; 7.8, " +
                                                             "org.openide.modules &gt; 6.0, " +
                                                             "org.openide.nodes &gt; 7.7, " +
                                                             "org.openide.util &gt; 7.18, " +
                                                             "org.openide.windows &gt; 6.24\" " +
                        "OpenIDE-Module-Name=\"JIRA Libraries\" " +
                        "OpenIDE-Module-Requires=\"org.openide.modules.ModuleFormat1\" " +
                        "OpenIDE-Module-Short-Description=\"Bundles JIRA Libraries\" " +
                        "OpenIDE-Module-Specification-Version=\"{1}\"/>" +
                    "</module>" +
                "</module_updates>";

    public JiraPluginUCTest(String testName) {
        super(testName);
    }

    public static class MyProvider extends AutoupdateCatalogProvider {
        public MyProvider () {
            super ("test-updates-provider", "test-updates-provider", catalogURL, UpdateUnitProvider.CATEGORY.STANDARD);
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.clearWorkDir ();
        catalogFile = new File(getWorkDir(), "updates.xml");
        if (!catalogFile.exists()) {
            catalogFile.createNewFile();
        }
        catalogURL = catalogFile.toURI().toURL();

        setUserDir (getWorkDirPath ());
        MainLookup.register(new MyProvider());
        assert Lookup.getDefault().lookup(MyProvider.class) != null;
    }


    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNewJIRAvailable() throws Throwable {
        String contents = MessageFormat.format(CATALOG_CONTENTS_FORMAT, JiraAutoupdate.JIRA_MODULE_CODE_NAME, "9.9.9");
        populateCatalog(contents);

        JiraAutoupdate jau = new JiraAutoupdate();
        assertTrue(jau.checkHigherJiraVersion());
    }

    public void testNewJIRANotAvailable() throws Throwable {
        String contents = MessageFormat.format(CATALOG_CONTENTS_FORMAT, JiraAutoupdate.JIRA_MODULE_CODE_NAME, "1.0.0");
        populateCatalog(contents);

        JiraAutoupdate jau = new JiraAutoupdate();
        assertFalse(jau.checkHigherJiraVersion());
    }

    public void testJIRAIsNotAtUCAvailable() throws Throwable {
        String contents = MessageFormat.format(CATALOG_CONTENTS_FORMAT, "org.netbeans.modules.ketchup", "1.0.0");
        populateCatalog(contents);

        JiraAutoupdate jau = new JiraAutoupdate();
        assertFalse(jau.checkHigherJiraVersion());
    }

    public void testIsSupported() {
        JiraAutoupdate jau = new JiraAutoupdate();
        assertTrue(jau.isSupportedVersion(JiraVersion.MIN_VERSION));
        assertTrue(jau.isSupportedVersion(JiraVersion.JIRA_3_3));
        assertTrue(jau.isSupportedVersion(new JiraVersion("3.3.1")));
        assertTrue(jau.isSupportedVersion(JiraVersion.JIRA_3_4));
        assertTrue(jau.isSupportedVersion(JiraVersion.JIRA_3_7));
        assertTrue(jau.isSupportedVersion(new JiraVersion("3.8.0")));
        assertTrue(jau.isSupportedVersion(JiraVersion.JIRA_3_13));
        assertTrue(jau.isSupportedVersion(new JiraVersion("3.13.1")));        
        assertTrue(jau.isSupportedVersion(getLower(JiraAutoupdate.SUPPORTED_JIRA_VERSION.toString())));
    }

    public void testIsNotSupported() {
        JiraAutoupdate jau = new JiraAutoupdate();
        assertFalse(jau.isSupportedVersion(getHigherMicro(JiraAutoupdate.SUPPORTED_JIRA_VERSION.toString())));
        assertFalse(jau.isSupportedVersion(getHigherMinor(JiraAutoupdate.SUPPORTED_JIRA_VERSION.toString())));
        assertFalse(jau.isSupportedVersion(getHigherMajor(JiraAutoupdate.SUPPORTED_JIRA_VERSION.toString())));
    }

    public static void setUserDir(String path) {
        System.setProperty ("netbeans.user", path);
    }

    private void populateCatalog(String contents) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(catalogFile);
        try {
            os.write(contents.getBytes());
        } finally {
            os.close();
        }
        UpdateUnitProviderFactory.getDefault().refreshProviders (null, true);
    }

    private JiraVersion getHigherMicro(String version) {
        String[] segments = version == null ? new String[0] : version.split("\\."); //$NON-NLS-1$
        int major = segments.length > 0 ? toInt(segments[0]) : 0;
        int minor = segments.length > 1 ? toInt(segments[1]) : 0;
        int micro = segments.length > 2 ? toInt(segments[2]) : 0;
        return new JiraVersion(new String("" + major + "." + minor + "." + ++micro));
    }

    private JiraVersion getHigherMinor(String version) {
        String[] segments = version == null ? new String[0] : version.split("\\."); //$NON-NLS-1$
        int major = segments.length > 0 ? toInt(segments[0]) : 0;
        int minor = segments.length > 1 ? toInt(segments[1]) : 0;
        int micro = segments.length > 2 ? toInt(segments[2]) : 0;
        return new JiraVersion(new String("" + major + "." + ++minor + "." + micro));
    }

    private JiraVersion getHigherMajor(String version) {
        String[] segments = version == null ? new String[0] : version.split("\\."); //$NON-NLS-1$
        int major = segments.length > 0 ? toInt(segments[0]) : 0;
        int minor = segments.length > 1 ? toInt(segments[1]) : 0;
        int micro = segments.length > 2 ? toInt(segments[2]) : 0;
        return new JiraVersion(new String("" + ++major + "." + minor + "." + micro));
    }

    private JiraVersion getLower(String version) {
        String[] segments = version == null ? new String[0] : version.split("\\."); //$NON-NLS-1$
        int major = segments.length > 0 ? toInt(segments[0]) : 0;
        int minor = segments.length > 1 ? toInt(segments[1]) : 0;
        int micro = segments.length > 2 ? toInt(segments[2]) : 0;
        if(micro > 0) {
            micro--;
        } else {
            if(minor > 0) {
                minor--;
            } else {
                major--;
            }
        }
        return new JiraVersion(new String("" + major + "." + minor + "." + ++micro));
    }

    private int toInt(String segment) {
        try {
            return segment.length() == 0 ? 0 : Integer.parseInt(getVersion(segment));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String getVersion(String segment) {
        int n = segment.indexOf('-');
        return n == -1 ? segment : segment.substring(0, n);
    }

}

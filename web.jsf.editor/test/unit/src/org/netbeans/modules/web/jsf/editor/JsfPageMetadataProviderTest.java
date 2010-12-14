/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */
package org.netbeans.modules.web.jsf.editor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.project.Sources;
import org.netbeans.editor.ext.html.parser.SyntaxAnalyzer;
import org.netbeans.editor.ext.html.parser.api.HtmlSource;
import org.netbeans.editor.ext.html.parser.api.SyntaxAnalyzerResult;
import org.netbeans.lib.lexer.test.TestLanguageProvider;
import org.netbeans.modules.parsing.api.indexing.IndexingManager;
import org.netbeans.modules.projectapi.SimpleFileOwnerQueryImplementation;
import org.netbeans.modules.web.common.api.WebPageMetadata;
import org.netbeans.spi.java.classpath.ClassPathProvider;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.test.MockLookup;

/**
 *
 * @author marekfukala
 */
public class JsfPageMetadataProviderTest extends TestBase {

    public JsfPageMetadataProviderTest(String testName) {
        super(testName);
    }

    public static Test Xsuite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new JsfPageMetadataProviderTest("testX"));
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        //disable info exceptions from j2eeserver
        Logger.getLogger("org.netbeans.modules.j2ee.deployment.impl.ServerRegistry").setLevel(Level.SEVERE);

        //so InstalledFileLocatorImpl finds the jsf "modules/ext/jsf-2_0/jsf-impl.jar"
        System.setProperty("netbeans.dirs", "/Volumes/Mercurial/web-main/nbbuild/netbeans/enterprise");

        FileObject srcFo = getTestFile("testWebProject/src");

        //create classpath
        Map<String, ClassPath> cps = new HashMap<String, ClassPath>();
        cps.put(ClassPath.COMPILE, createServletAPIClassPath());
        cps.put(ClassPath.EXECUTE, createServletAPIClassPath());
        cps.put(ClassPath.SOURCE, ClassPathSupport.createClassPath(new FileObject[]{srcFo}));
        cps.put(ClassPath.BOOT, createBootClassPath());

        ClassPathProvider classpathProvider = new TestClassPathProvider(cps);
        Sources sources = new TestSources(srcFo, getTestFile("testWebProject/web"));

        MockLookup.setInstances(
                new OpenProject(),
                new TestUserCatalog(),
                new TestProjectFactory(classpathProvider, sources),
                new SimpleFileOwnerQueryImplementation(),
                classpathProvider,
                new TestLanguageProvider(),
                new FakeWebModuleProvider(getTestFile("testWebProject")));

        IndexingManager.getDefault().refreshIndexAndWait(srcFo.getURL(), null);
    }

    public void testInstanceInGlobalLookup() {
        JsfPageMetadataProvider instance = Lookup.getDefault().lookup(JsfPageMetadataProvider.class);
        assertNotNull(instance);
    }

    public void testGetPageMetadata() {
        FileObject file = getTestFile("testWebProject/web/index.xhtml");
        assertNotNull(file);

        SyntaxAnalyzerResult result = SyntaxAnalyzer.create(new HtmlSource(file)).analyze();
        InstanceContent ic = new InstanceContent();
        ic.add(result);
        Lookup lookup = new AbstractLookup(ic);

        WebPageMetadata meta = WebPageMetadata.getMetadata(lookup);
        assertNotNull(meta);
        
        Collection<String> namespaces = (Collection<String>) meta.value(JsfPageMetadataProvider.JSF_LIBRARIES_KEY);
        assertNotNull(namespaces);
        assertTrue(namespaces.contains("http://java.sun.com/jsf/html"));
        assertTrue(namespaces.contains("http://java.sun.com/jsp/jstl/core"));
        assertFalse(namespaces.contains("http://www.w3.org/1999/xhtml"));

        //test mimetype
        assertEquals(JsfPageMetadataProvider.JSF_PAGE_MIMETYPE, meta.value(WebPageMetadata.MIMETYPE));
    }

    public void testNoPageMetadataForPlainXHTML() {
        FileObject file = getTestFile("testWebProject/web/nofacelets.xhtml");
        assertNotNull(file);

        SyntaxAnalyzerResult result = SyntaxAnalyzer.create(new HtmlSource(file)).analyze();
        InstanceContent ic = new InstanceContent();
        ic.add(result);
        Lookup lookup = new AbstractLookup(ic);

        WebPageMetadata meta = WebPageMetadata.getMetadata(lookup);
        assertNull(meta);
    }

}

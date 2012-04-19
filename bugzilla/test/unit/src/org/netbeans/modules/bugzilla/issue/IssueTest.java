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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.netbeans.modules.bugzilla.issue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.netbeans.modules.bugzilla.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaCorePlugin;
import org.eclipse.mylyn.internal.bugzilla.core.BugzillaRepositoryConnector;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.bugtracking.BugtrackingManager;
import org.netbeans.modules.bugtracking.util.BugtrackingUtil;
import org.netbeans.modules.bugzilla.repository.BugzillaRepository;
import org.netbeans.modules.bugzilla.repository.IssueField;
import org.openide.util.test.MockLookup;

/**
 *
 * @author tomas
 */
public class IssueTest extends NbTestCase implements TestConstants {

    private static String REPO_NAME = "Beautiful";
    private Method getSeenAttributes;

    public IssueTest(String arg0) {
        super(arg0);
    }

    @Override
    protected Level logLevel() {
        return Level.ALL;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("netbeans.user", getWorkDir().getAbsolutePath());
        MockLookup.setLayersAndInstances();
        BugtrackingUtil.getBugtrackingConnectors(); // ensure conector
    }

//    public void testStatusOpenIssue() throws MalformedURLException, CoreException, InterruptedException, IOException, Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//
//        setSeen(issue);
//
//        String keyword = getKeyword(issue);
//        issue.setFieldValue(IssueField.KEYWORDS, keyword);
//        submit(issue);
//        assertStatus(BugzillaIssue.FIELD_STATUS_NEW, issue, IssueField.KEYWORDS);
//
//        issue.open();
//
//        for (int i = 0; i < 100; i++) {
//            if(BugzillaIssue.FIELD_STATUS_NEW != issue.getFieldStatus(IssueField.KEYWORDS)) {
//                break;
//            }
//            Thread.sleep(500);
//        }
//
//        keyword = getKeyword(issue);
//        issue.setFieldValue(IssueField.KEYWORDS, keyword);
//        submit(issue);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.KEYWORDS);
//    }

//    public void testFields() throws Throwable {
//        // WARNING: the test assumes that there is more than one value
//        // for atributes like platform, versions etc.
//
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        final BugzillaRepository repository = getRepository();
//        String id = TestUtil.createIssue( repository, summary);
//        BugzillaIssue issue = (BugzillaIssue) repository.getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//
//        issue.setSeen(true);
//        for (IssueField f : IssueField.values()) {
//            // is seen , everything's uptodate
//            assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, f);
//        }
//
//        String keyword = getOtherKeyword(issue);
////        String milestone = getMilestone(issue);
//        String platform = getOtherPlatform(issue);
//        String priority = getOtherPriority(issue);
//        String resolution = getOtherResolution(issue);
//        String version = getOtherVersion(issue);
//        String assignee = REPO_USER;
//        String reporter = REPO_USER;
//        String qaContact = REPO_USER;
//        String assigneeName = REPO_USER_NAME;
//        String qaContactName = REPO_USER_NAME;
//        String blocks = "1";
//        String depends = "2";
//        String newcc = REPO_USER;
//        String cc = REPO_USER;
//        String url = "http://new.ulr";
//        String component = getOtherComponent(issue);
//        String severity = getOtherSeverity(issue);
//
////        issue.setFieldValue(IssueField.ASSIGNED_TO, assignee);
////        issue.setFieldValue(IssueField.ASSIGNED_TO_NAME, assigneeName);
//        issue.setFieldValue(IssueField.BLOCKS, blocks);
//        issue.setFieldValue(IssueField.COMPONENT, component);
//        issue.setFieldValue(IssueField.DEPENDS_ON, depends);
//        issue.setFieldValue(IssueField.KEYWORDS, keyword);
////        issue.setFieldValue(IssueField.MILESTONE,milestone);
//        issue.setFieldValue(IssueField.PLATFORM,platform);
//        issue.setFieldValue(IssueField.PRIORITY, priority);
//        issue.setFieldValue(IssueField.QA_CONTACT, qaContact);
//        issue.setFieldValue(IssueField.QA_CONTACT_NAME, qaContactName);
//        issue.setFieldValue(IssueField.SEVERITY, getOtherSeverity(issue));
//        issue.setFieldValue(IssueField.SUMMARY, summary + ".new");
//        issue.setFieldValue(IssueField.URL, url);
//        issue.setFieldValue(IssueField.VERSION, version);
//        issue.setFieldValue(IssueField.NEWCC, newcc);
//
//        // can't be changed
////      DESCRIPTION
////      PRODUCT
//        // won't test those too
////      CREATION
////      MODIFICATION
//        // handled in separate tests
////      STATUS
////      RESOLUTION
////      REMOVECC
////      CC
////      ASSIGNED_TO
////      ASSIGNED_TO_NAME
//
//        submit(issue);
//
//        // assert values
//        assertEquals(reporter, issue.getFieldValue(IssueField.REPORTER));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals(assignee, issue.getFieldValue(IssueField.ASSIGNED_TO));
//        assertEquals(assigneeName, issue.getFieldValue(IssueField.ASSIGNED_TO_NAME));
//        assertEquals(blocks, issue.getFieldValue(IssueField.BLOCKS));
//        assertEquals(component, issue.getFieldValue(IssueField.COMPONENT));
//        assertEquals(depends, issue.getFieldValue(IssueField.DEPENDS_ON));
//        assertEquals(keyword, issue.getFieldValue(IssueField.KEYWORDS));
////        assertEquals(milestone, issue.getFieldValue(IssueField.MILESTONE));
//        assertEquals(platform, issue.getFieldValue(IssueField.PLATFORM));
//        assertEquals(priority, issue.getFieldValue(IssueField.PRIORITY));
//        assertEquals(qaContact, issue.getFieldValue(IssueField.QA_CONTACT));
//        assertEquals(qaContactName, issue.getFieldValue(IssueField.QA_CONTACT_NAME));
//        assertEquals(severity, issue.getFieldValue(IssueField.SEVERITY));
//        assertEquals(summary + ".new", issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals(url, issue.getFieldValue(IssueField.URL));
//        assertEquals(version, issue.getFieldValue(IssueField.VERSION));
//        assertEquals(cc, issue.getFieldValue(IssueField.CC));
//
//        assertEquals(ISSUE_DESCRIPTION, issue.getFieldValue(IssueField.DESCRIPTION));
//        assertEquals(TEST_PROJECT, issue.getFieldValue(IssueField.PRODUCT));
//
//        // assert status
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.REPORTER);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.STATUS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.ASSIGNED_TO);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.ASSIGNED_TO_NAME);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.BLOCKS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.COMPONENT);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.DEPENDS_ON);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.KEYWORDS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.MILESTONE);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.PLATFORM);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.PRIORITY);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.QA_CONTACT);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.QA_CONTACT_NAME);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.SEVERITY);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.SUMMARY);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.URL);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.VERSION);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.CC);
//
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.DESCRIPTION);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.PRODUCT);
//
//        setSeen(issue); // reset status
//
//        BugzillaRepositoryConnector brc = new BugzillaRepositoryConnector();
//        TaskData data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
//        BugzillaIssue modIssue = new BugzillaIssue(data, repository);
//
//        keyword = keyword + "," + getOtherKeyword(issue);
////        milestone = getMilestone(issue);
//        platform = getOtherPlatform(issue);
//        priority = getOtherPriority(issue);
//        resolution = getOtherResolution(issue);
//        version = getOtherVersion(issue);
//        String product = getOtherProduct(issue);
////        String milestone = getMilestone(issue);
//        qaContact = REPO_USER2;
//        qaContactName = REPO_USER2_NAME;
//        blocks = "1,3";
//        depends = "2,4";
//        newcc = REPO_USER2;
//        url = "http://evennewer.ulr";
//        component = getOtherComponent(issue);
//        severity = getOtherSeverity(issue);
////        summary =  issue.getFieldValue(IssueField.SUMMARY) + "modified";
////        String description = issue.getFieldValue(IssueField.DESCRIPTION) + ".modified";
//
////        modIssue.setFieldValue(IssueField.PRODUCT, product);
////        modIssue.setFieldValue(IssueField.SUMMARY, summary);
////        modIssue.setFieldValue(IssueField.DESCRIPTION, description); can't change desc?
//        modIssue.setFieldValue(IssueField.BLOCKS, blocks);
//        modIssue.setFieldValue(IssueField.COMPONENT, component);
//        modIssue.setFieldValue(IssueField.DEPENDS_ON, depends);
//        modIssue.setFieldValue(IssueField.KEYWORDS, keyword);
////        issue.setFieldValue(IssueField.MILESTONE, milestone);
//        modIssue.setFieldValue(IssueField.PLATFORM, platform);
//        modIssue.setFieldValue(IssueField.PRIORITY, priority);
//        modIssue.setFieldValue(IssueField.QA_CONTACT, qaContact);
//        modIssue.setFieldValue(IssueField.QA_CONTACT_NAME, qaContactName);
//        modIssue.setFieldValue(IssueField.SEVERITY, getOtherSeverity(issue));
//        modIssue.setFieldValue(IssueField.SUMMARY, summary + ".new");
//        modIssue.setFieldValue(IssueField.URL, url);
//        modIssue.setFieldValue(IssueField.VERSION, version);
//        modIssue.setFieldValue(IssueField.NEWCC, newcc);
//
//        submit(modIssue);
//        issue.refresh();
//
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.REPORTER);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.STATUS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.ASSIGNED_TO);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.ASSIGNED_TO_NAME);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.BLOCKS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.COMPONENT);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.DEPENDS_ON);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.KEYWORDS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.MILESTONE);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.PLATFORM);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.PRIORITY);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.QA_CONTACT);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.QA_CONTACT_NAME);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.SEVERITY);
////        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.SUMMARY);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.URL);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.VERSION);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.CC);
//
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.DESCRIPTION);
//        assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, IssueField.PRODUCT);
//
//        resetStatusValues(issue);
//
//        qaContact = "";
//        qaContactName = "";
//        blocks = "";
//        depends = "";
//        url = "";
//        keyword = "";
//
//        data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
//        modIssue = new BugzillaIssue(data, repository);
//
//        modIssue.setFieldValue(IssueField.BLOCKS, blocks);
//        modIssue.setFieldValue(IssueField.DEPENDS_ON, depends);
//        modIssue.setFieldValue(IssueField.KEYWORDS, keyword);
//        modIssue.setFieldValue(IssueField.QA_CONTACT, qaContact);
//        modIssue.setFieldValue(IssueField.QA_CONTACT_NAME, qaContactName);
//        modIssue.setFieldValue(IssueField.URL, url);
//
//        submit(modIssue);
//        issue.refresh();
//
//        assertEquals(blocks, issue.getFieldValue(IssueField.BLOCKS));
//        assertEquals(depends, issue.getFieldValue(IssueField.DEPENDS_ON));
//        assertEquals(keyword, issue.getFieldValue(IssueField.KEYWORDS));
//        assertEquals(qaContact, issue.getFieldValue(IssueField.QA_CONTACT));
//        assertEquals(qaContactName, issue.getFieldValue(IssueField.QA_CONTACT_NAME));
//        assertEquals(url, issue.getFieldValue(IssueField.URL));
//
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.BLOCKS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.DEPENDS_ON);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.KEYWORDS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.QA_CONTACT);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.QA_CONTACT_NAME);
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.URL);
//
//        data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
//        modIssue = new BugzillaIssue(data, repository);
//
//        modIssue.resolve("FIXED");
//        submit(modIssue);
//        issue.refresh();
//
//        assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("FIXED", issue.getFieldValue(IssueField.RESOLUTION));
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.STATUS);
//        assertStatus(BugzillaIssue.FIELD_STATUS_NEW, issue, IssueField.RESOLUTION);
//
//
////        XXX changing a product might also imply the change of other fields!!!
////
////        String product = getProduct(issue);
////        issue.setFieldValue(IssueField.PRODUCT, product);
////        try {
////            issue.submit();
////        } catch (CoreException ex) {
////            TestUtil.handleException(ex);
////        }
////
////        issue.refresh();
////        assertEquals(product, issue.getFieldValue(IssueField.PRODUCT));
//
//    }

    public void testIsFinished() throws Throwable {
        BugzillaRepository br = TestUtil.getRepository("test", REPO_URL, REPO_USER, REPO_PASSWD);
        String id = TestUtil.createIssue(br, "testIsFinished ");
        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
        assertFalse(issue.isFinished());
        issue.resolve("FIXED");
        issue.submitAndRefresh();
        assertTrue(issue.isFinished());
    }

    public void testCC() throws Throwable {
        // WARNING: the test assumes that there are more than one value
        // for atributes like platform, versions etc.

        long ts = System.currentTimeMillis();
        String summary = "somary" + ts;
        String id = TestUtil.createIssue(getRepository(), summary);
        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));

        resetStatusValues(issue);

        BugzillaRepository repository = getRepository();
        BugzillaRepositoryConnector brc = new BugzillaRepositoryConnector(new File(getWorkDir().getAbsolutePath(), "bugzillaconfiguration"));
        TaskData data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
        BugzillaIssue modIssue = new BugzillaIssue(data, repository);

        // add a cc
        assertNotSame(REPO_USER, issue.getFieldValue(IssueField.CC));
        modIssue.setFieldValue(IssueField.NEWCC, REPO_USER);
        submit(modIssue);
        issue.refresh();

        assertEquals(REPO_USER, issue.getFieldValue(IssueField.CC));
        assertStatus(BugzillaIssue.FIELD_STATUS_NEW, issue, IssueField.CC);

        resetStatusValues(issue);

        data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
        modIssue = new BugzillaIssue(data, repository);

        // add new cc
        modIssue.setFieldValue(IssueField.NEWCC, REPO_USER2);
        submit(modIssue);
        issue.refresh();

        List<String> ccs = issue.getFieldValues(IssueField.CC);
        assertEquals(2, ccs.size());
        assertTrue(ccs.contains(REPO_USER));
        assertTrue(ccs.contains(REPO_USER2));
        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.CC);

        data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
        modIssue = new BugzillaIssue(data, repository);

        // add two cc-s at once
        modIssue.setFieldValue(IssueField.NEWCC, REPO_USER3 + ", " + REPO_USER4);
        submit(modIssue);
        issue.refresh();

        ccs = issue.getFieldValues(IssueField.CC);
        assertEquals(4, ccs.size());
        assertTrue(ccs.contains(REPO_USER));
        assertTrue(ccs.contains(REPO_USER2));
        assertTrue(ccs.contains(REPO_USER3));
        assertTrue(ccs.contains(REPO_USER4));
        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.CC);

        resetStatusValues(issue);

        data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
        modIssue = new BugzillaIssue(data, repository);

        // remove a cc
        ccs = new ArrayList<String>();
        ccs.add(REPO_USER4);
        ccs.add(REPO_USER);
        modIssue.setFieldValues(IssueField.REMOVECC, ccs);

        submit(modIssue);
        issue.refresh();
        ccs = issue.getFieldValues(IssueField.CC);
        assertEquals(2, ccs.size());
        assertTrue(ccs.contains(REPO_USER2));
        assertTrue(ccs.contains(REPO_USER3));
        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.CC);

        resetStatusValues(issue);

        data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
        modIssue = new BugzillaIssue(data, repository);

        // remove all
        ccs = new ArrayList<String>();
        ccs.add(REPO_USER3);
        ccs.add(REPO_USER2);        
        modIssue.setFieldValues(IssueField.REMOVECC, ccs);
        submit(modIssue);
        issue.refresh();
        ccs = issue.getFieldValues(IssueField.CC);
        assertEquals(0, ccs.size());
        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.CC);

    }
//    public void testStartResolveFixedVerifiedClosedReopen() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("", issue.getFieldValue(IssueField.RESOLUTION));
//
//        issue.setFieldValue(IssueField.ASSIGNED_TO_NAME, getRepository().getUsername());
//        issue.accept();
//        submit(issue);
//        assertEquals("ASSIGNED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("", issue.getFieldValue(IssueField.RESOLUTION));
//
//        issue.resolve("FIXED");
//        submit(issue);
//        assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("FIXED", issue.getFieldValue(IssueField.RESOLUTION));
//
//        issue.verify();
//        submit(issue);
//        assertEquals("VERIFIED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("FIXED", issue.getFieldValue(IssueField.RESOLUTION));
//
//        issue.close();
//        submit(issue);
//        assertEquals("CLOSED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("FIXED", issue.getFieldValue(IssueField.RESOLUTION));
//
//        // we do not support this yet - DUPLICATE RESOLVED
////        issue.duplicate("1");
////        submit(issue);
////        assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
////        assertEquals("DUPLICATE", issue.getFieldValue(IssueField.RESOLUTION));
//
//        issue.reopen();
//        submit(issue);
//        assertEquals("REOPENED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("", issue.getFieldValue(IssueField.RESOLUTION));
//    }
//
//    public void testResolveNew() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//
//        List<String> rs = getRepository().getConfiguration().getResolutions();
//        for (String r : rs) {
//            if(r.equals("DUPLICATE") ||
//               r.equals("MOVED"))
//            {
//                continue;  // used and tested in a different way
//            }
//            String id = TestUtil.createIssue(getRepository(), summary);
//            BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//            assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//            assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//            assertEquals("", issue.getFieldValue(IssueField.RESOLUTION));
//
//            issue.resolve(r);
//            submit(issue);
//            assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
//            assertEquals(r, issue.getFieldValue(IssueField.RESOLUTION));
//        }
//    }
//
//    //    we do not support this yet
////    public void testResolveClosed() throws Throwable {
////        long ts = System.currentTimeMillis();
////        String summary = "somary" + ts;
////
////        String id = TestUtil.createIssue(getRepository(), summary);
////        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
////        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
////        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
////        assertEquals("", issue.getFieldValue(IssueField.RESOLUTION));
////
////        // fix
////        issue.resolve("FIXED");
////        submit(issue);
////        assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
////        assertEquals("FIXED", issue.getFieldValue(IssueField.RESOLUTION));
////
////        // try to change to another resolution
////        List<String> rs = getRepository().getConfiguration().getResolutions();
////        for (String r : rs) {
////            if(r.equals("DUPLICATE") ||
////               r.equals("MOVED") ||
////               r.equals("FIXED")) // already fixed
////            {
////                continue;  // used and tested in a different way
////            }
////
////            issue.resolve(r);
////            submit(issue);
////            assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
////            assertEquals(r, issue.getFieldValue(IssueField.RESOLUTION));
////        }
////    }
//
//
//    public void testResolveDuplicateReopen() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//
//        issue.duplicate("1");
//        submit(issue);
//
//        assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("DUPLICATE", issue.getFieldValue(IssueField.RESOLUTION));
//
//        issue.reopen();
//        submit(issue);
//        assertEquals("REOPENED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("", issue.getFieldValue(IssueField.RESOLUTION));
//
//        // XXX get dupl ID
//
//    }
//
//    public void testReassign() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals(REPO_USER, issue.getFieldValue(IssueField.ASSIGNED_TO));
//
//        issue.reassign(REPO_USER2);
//        submit(issue);
//
//        assertEquals(REPO_USER2, issue.getFieldValue(IssueField.ASSIGNED_TO));
//    }
//
//    public void testComment() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals(REPO_USER, issue.getFieldValue(IssueField.ASSIGNED_TO));
//
//        resetStatusValues(issue);
//
//        BugzillaRepository repository = getRepository();
//        BugzillaRepositoryConnector brc = new BugzillaRepositoryConnector();
//        TaskData data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
//        BugzillaIssue modIssue = new BugzillaIssue(data, repository);
//
//        // add comment
//        String comment = "koment";
//        modIssue.addComment(comment);
//        submit(modIssue);
//        issue.refresh();
//        assertStatus(BugzillaIssue.FIELD_STATUS_NEW, issue, IssueField.COMMENT_COUNT);
//
//        // get comment
//        Comment[] comments = issue.getComments();
//        assertEquals(1, comments.length);
//        assertEquals(comment, issue.getComments()[0].getText());
//
//        resetStatusValues(issue);
//        data = brc.getTaskData(repository.getTaskRepository(), issue.getID(), new NullProgressMonitor());
//        modIssue = new BugzillaIssue(data, repository);
//
//        // one more comment
//        comment = "1 more koment";
//        modIssue.addComment(comment);
//        submit(modIssue);
//        issue.refresh();
//        assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.COMMENT_COUNT);
//    }
//
//    public void testAddCommentClose() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals(REPO_USER, issue.getFieldValue(IssueField.ASSIGNED_TO));
//
//        String comment = "koment";
//        issue.addComment(comment, true);
//        issue.refresh();
//
//        Comment[] comments = issue.getComments();
//        assertEquals(1, comments.length);
//        assertEquals(comment, issue.getComments()[0].getText());
//
//        assertEquals("RESOLVED", issue.getFieldValue(IssueField.STATUS));
//        assertEquals("FIXED", issue.getFieldValue(IssueField.RESOLUTION));
//    }
//
//
//    public void testAttachment() throws Throwable {
//        try {
//            long ts = System.currentTimeMillis();
//            String summary = "somary" + ts;
//            String id = TestUtil.createIssue(getRepository(), summary);
//            BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//            assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//            assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//            assertEquals(REPO_USER, issue.getFieldValue(IssueField.ASSIGNED_TO));
//
//            resetStatusValues(issue);
//
//            // add attachment
//            String atttext = "my first attachement";
//            String attcomment = "my first attachement";
//            String attdesc = "file containing text";
//            File f = getAttachmentFile(atttext);
//            issue.addAttachment(f, attcomment, attdesc, "text/plain", false);
//            issue.refresh();
//
//            // get attachment
//            Attachment[] atts = issue.getAttachments();
//            assertEquals(1, atts.length);
//            assertEquals(attdesc, atts[0].getDesc());
//            assertStatus(BugzillaIssue.FIELD_STATUS_NEW, issue, IssueField.ATTACHEMENT_COUNT);
//
//            // get attachment data
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            atts[0].getAttachementData(os);
//            String fileConttents = os.toString();
//            assertEquals(atttext, fileConttents);
//
//            resetStatusValues(issue);
//
//            // one more  attachment
//            atttext = "my second attachement";
//            attcomment = "my second attachement";
//            attdesc = "file containing text";
//            f = getAttachmentFile(atttext);
//            issue.addAttachment(f, attcomment, attdesc, "text/plain", false);
//            issue.refresh();
//
//            // get attachment
//            atts = issue.getAttachments();
//            assertEquals(2, atts.length);
//            assertEquals(attdesc, atts[0].getDesc());
//            assertStatus(BugzillaIssue.FIELD_STATUS_MODIFIED, issue, IssueField.ATTACHEMENT_COUNT);
//
//        } catch (Exception e) {
//            TestUtil.handleException(e);
//        }
//    }
//
//    public void testRecentChanges() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        BugzillaRepository repository = getRepository();
//        String id = TestUtil.createIssue( repository, summary);
//        BugzillaIssue issue = (BugzillaIssue) repository.getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals(REPO_USER, issue.getFieldValue(IssueField.ASSIGNED_TO));
//
//        issue.setSeen(true);
//        String comment = "koment";
//        RepositoryResponse rr = TestUtil.addComment(repository.getTaskRepository(), issue.getID(), comment);
//        assertEquals(RepositoryResponse.ResponseKind.TASK_UPDATED, rr.getReposonseKind());
//        issue.refresh();
//
//        String rc = issue.getRecentChanges();
//        assertEquals("1 new comment(s)", rc);
//    }
//
//    public void testChangeProduct() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals(REPO_USER, issue.getFieldValue(IssueField.ASSIGNED_TO));
//
//        resetStatusValues(issue);
//
//        issue.setFieldValue(IssueField.PRODUCT, TEST_PROJECT2);
//        issue.setFieldValue(IssueField.COMPONENT, getOtherComponent(issue, TEST_PROJECT2));
//        issue.setFieldValue(IssueField.VERSION, getOtherVersion(issue, TEST_PROJECT2));
//        issue.setFieldValue(IssueField.MILESTONE, getOtherMilestone(issue, TEST_PROJECT2));
//
//        issue.submitAndRefresh();
//        assertEquals(TEST_PROJECT2, issue.getFieldValue(IssueField.PRODUCT));
//    }
//
//    public void testSetResetProduct() throws Throwable {
//        long ts = System.currentTimeMillis();
//        String summary = "somary" + ts;
//        String id = TestUtil.createIssue(getRepository(), summary);
//        BugzillaIssue issue = (BugzillaIssue) getRepository().getIssue(id);
//        assertEquals(summary, issue.getFieldValue(IssueField.SUMMARY));
//        assertEquals("NEW", issue.getFieldValue(IssueField.STATUS));
//        assertEquals(REPO_USER, issue.getFieldValue(IssueField.ASSIGNED_TO));
//
//        resetStatusValues(issue);
//
//        // set new product
//        issue.setFieldValue(IssueField.PRODUCT, TEST_PROJECT2);
//        issue.setFieldValue(IssueField.COMPONENT, getOtherComponent(issue, TEST_PROJECT2));
//        issue.setFieldValue(IssueField.VERSION, getOtherVersion(issue, TEST_PROJECT2));
//        issue.setFieldValue(IssueField.MILESTONE, getOtherMilestone(issue, TEST_PROJECT2));
//
//        // reset back
//        issue.setFieldValue(IssueField.PRODUCT, TEST_PROJECT);
//        issue.setFieldValue(IssueField.COMPONENT, getOtherComponent(issue, TEST_PROJECT));
//        issue.setFieldValue(IssueField.VERSION, getOtherVersion(issue, TEST_PROJECT));
//        issue.setFieldValue(IssueField.MILESTONE, getOtherMilestone(issue, TEST_PROJECT));
//
//        issue.submitAndRefresh();
//        assertEquals(TEST_PROJECT, issue.getFieldValue(IssueField.PRODUCT));
//    }

    // XXX test new issue

    private void addHandler(LogHandler lh) {
        BugtrackingManager.LOG.addHandler(lh);
    }

    private void assertStatus(int expectedStatus, BugzillaIssue issue, IssueField f) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        int status = issue.getFieldStatus(f);
        //System.out.println(issue.getID() + " " + f + " " + getName(expectedStatus) + " " + getName(status) + " " + "[" + issue.getFieldValue(f) + "] vs [" + getSeenValue(issue, f) + "]");
        if(status != expectedStatus) {
            fail("expected [" + getName(expectedStatus) + "], " +
                 "was [" + getName(status)+ "] " +
                 "because of value [" + issue.getFieldValue(f) + "] " +
                 "vs [" + getSeenValue(issue, f) + "] in field " + f);
        }
    }

    private String getName(int s) {
        switch(s) {
            case BugzillaIssue.FIELD_STATUS_IRELEVANT:
                return "Irelevant";
            case BugzillaIssue.FIELD_STATUS_NEW :
                return "New";
            case BugzillaIssue.FIELD_STATUS_MODIFIED :
                return "Modified";
            case BugzillaIssue.FIELD_STATUS_UPTODATE :
                return "Uptodate";
            default :
                throw new IllegalStateException("Wrong status " + s);
        }
    }

    private String getSeenValue(BugzillaIssue issue, IssueField f) throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        if(getSeenAttributes ==  null) {
            getSeenAttributes = issue.getClass().getDeclaredMethod("getSeenAttributes");
            getSeenAttributes.setAccessible(true);
        }
        Map<String, String> m = (Map<String, String>) getSeenAttributes.invoke(issue);
        if(m == null) {
            return "";
        }
        String ret = m.get(f.getKey());
        return ret != null ? ret : "";
    }

    private BugzillaRepository getRepository() {
        return TestUtil.getRepository(REPO_NAME, REPO_URL, REPO_USER, REPO_PASSWD);
    }

    private String getOtherKeyword(BugzillaIssue issue) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getKeywords();
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.KEYWORDS), IssueField.KEYWORDS.getKey());
    }

    private String getOtherMilestone(BugzillaIssue issue, String product) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getTargetMilestones(product);
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.MILESTONE), IssueField.MILESTONE.getKey());
    }

    private String getOtherMilestone(BugzillaIssue issue) throws IOException, CoreException {
        return getOtherMilestone(issue, TEST_PROJECT);
    }

    private String getOtherPlatform(BugzillaIssue issue) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getPlatforms();
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.PLATFORM), IssueField.PLATFORM.getKey());
    }

    private String getOtherProduct(BugzillaIssue issue) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getProducts();
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.PRODUCT), IssueField.PRODUCT.getKey());
    }

    private String getOtherPriority(BugzillaIssue issue) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getPriorities();
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.PRIORITY), IssueField.PRIORITY.getKey());
    }

    private String getOtherVersion(BugzillaIssue issue, String product) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getVersions(product);
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.VERSION), IssueField.VERSION.getKey());
    }

    private String getOtherVersion(BugzillaIssue issue) throws IOException, CoreException {
        return getOtherVersion(issue, TEST_PROJECT);
    }

    private String getOtherSeverity(BugzillaIssue issue) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getSeverities();
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.SEVERITY), IssueField.SEVERITY.getKey());
    }

    private String getOtherResolution(BugzillaIssue issue) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getResolutions();
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.RESOLUTION), IssueField.RESOLUTION.getKey());
    }

    private String getOtherComponent(BugzillaIssue issue, String project) throws IOException, CoreException {
        List<String> l = getRepository().getConfiguration().getComponents(project);
        return getDifferentServerValue(l, issue.getFieldValue(IssueField.COMPONENT), IssueField.COMPONENT.getKey());
    }

    private String getOtherComponent(BugzillaIssue issue) throws IOException, CoreException {
        return getOtherComponent(issue, TEST_PROJECT);
    }

    private String getDifferentServerValue(List<String> l, String v, String field) {
        if(v != null) {
            for (String s : l) {
                if(!s.equals(v)) {
                    return s;
                }
            }
            fail("there is no different value for field " + field + "  then [" + v + "] on the server.");
        }
        return l.get(0);
    }

    private void resetStatusValues(BugzillaIssue issue) throws InterruptedException, SecurityException, IOException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        issue.closed(); // resets the status values
        setSeen(issue);
    }

    private void setSeen(BugzillaIssue issue) throws SecurityException, InterruptedException, IOException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        LogHandler lh = new LogHandler("finished storing issue");
        addHandler(lh);
        issue.setSeen(true);
        while (!lh.done) {
            Thread.sleep(100);
        }
        for (IssueField f : issue.getRepository().getConfiguration().getFields()) {
            // seen -> everything's uptodate
            assertStatus(BugzillaIssue.FIELD_STATUS_UPTODATE, issue, f);
        }
    }

    private void submit(BugzillaIssue issue) throws Throwable {
        issue.submitAndRefresh();
    }

    private File getAttachmentFile(String content) throws Exception {
        FileWriter fw = null;
        File f = null;
        try {
            f = File.createTempFile("bugzillatest", null);
            f.deleteOnExit();
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // ignore
            }
            fw = new FileWriter(f);
            fw.write(content);
            fw.flush();
            return f;
        } finally {
            try { if (fw != null) fw.close(); } catch (IOException iOException) { }
        }
    }

    private class LogHandler extends Handler {
        private final String msg;
        private boolean done = false;
        public LogHandler(String msg) {
            this.msg = msg;
        }

        @Override
        public void publish(LogRecord record) {
            if(!done) done = record.getMessage().startsWith(msg);
        }
        @Override
        public void flush() { }
        @Override
        public void close() throws SecurityException { }
    }
}

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

package org.netbeans.modules.cnd.modelimpl.csm.core;

import java.io.File;
import org.netbeans.modules.cnd.modelimpl.trace.TraceModelTestBase;

/**
 * A suite for testing smart header parsing (iz #142107)
 * @author Vladimir Kvashin
 */
public class SmartParseHeaderTest extends TraceModelTestBase {

    
    public SmartParseHeaderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        System.setProperty("parser.report.errors", "true");
        System.setProperty("cnd.smart.parse", "true");
        System.setProperty("antlr.exceptions.hideExpectedTokens", "true");
        System.setProperty("cnd.modelimpl.parser.threads", "1");
        ParseStatistics.getInstance().setEnabled(true);
        super.setUp();
    }

    @Override
    protected void postSetUp() {
        // init flags needed for file model tests
        getTraceModel().setDumpModel(true);
        getTraceModel().setDumpPPState(true);
        ParseStatistics.getInstance().clear();
    }

    private FileImpl findFile(String name) {
        ProjectBase project = this.getProject();
        if (project != null) {
            String toCompare = File.separator + name;
            for (FileImpl file : project.getAllFileImpls()) {
                if (file.getAbsolutePath().toString().endsWith(toCompare)) {
                    return file;
                }
            }
        }
        return null;
    }

    private void assertParseCount(String fileName, int expectedParseCount) throws Exception {
        FileImpl fileImpl = findFile(fileName);
        assertNotNull("file " + fileName + " not found", fileImpl);
        int actualParseCount = ParseStatistics.getInstance().getParseCount(fileImpl);
        assertEquals("Unexpected parse count for " + fileName, expectedParseCount, actualParseCount);
    }

    public void testSimple_1a() throws Exception {
        performTest("smart_headers_simple_1a.cc");
        assertParseCount("smart_headers_simple_1.h", 3);
    }

    public void testSimple_1b() throws Exception {
        performTest("smart_headers_simple_1b.cc");
        assertParseCount("smart_headers_simple_1.h", 1);
    }

    public void testSimple_1c() throws Exception {
        performTest("smart_headers_simple_1c.cc");
        assertParseCount("smart_headers_simple_1.h", 1);
    }

    public void testSimple_1d() throws Exception {
        performTest("smart_headers_simple_1d.cc");
        assertParseCount("smart_headers_simple_1.h", 1);
    }

    public void testSimple_1e() throws Exception {
        performTest("smart_headers_simple_1e.cc");
        assertParseCount("smart_headers_simple_1.h", 3);
    }

    public void testSimple_1f() throws Exception {
        performTest("smart_headers_simple_1f.cc");
        assertParseCount("smart_headers_simple_1.h", 1);
    }

    /////////////////////////////////////////////////////////////////////
    // FAILURES
    
    public static class Failed extends TraceModelTestBase {
	
        public Failed(String testName) {
            super(testName);
        }

	@Override
	protected void setUp() throws Exception {
	    System.setProperty("parser.report.errors", "true");
	    super.setUp();
	}
	
        @Override
	protected Class getTestCaseDataClass() {
	    return SmartParseHeaderTest.class;
	}
	
        @Override
	protected void postSetUp() {
	    // init flags needed for file model tests
	    getTraceModel().setDumpModel(true);
	    getTraceModel().setDumpPPState(true);
	}
   }
    
}

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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */
package org.netbeans.modules.java.hints.bugs;

import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.java.hints.test.api.HintTest;

/**
 *
 * @author lahvac
 */
public class TinyTest extends NbTestCase {

    public TinyTest(String name) {
        super(name);
    }

    public void testPositive1() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "    public void test(String[] args) {\n" +
                       "        \"a\".replaceAll(\".\", \"/\");\n" +
                       "    }\n" +
                       "}\n")
                .run(Tiny.class)
                .findWarning("3:23-3:26:verifier:ERR_string-replace-all-dot")
                .applyFix("FIX_string-replace-all-dot")
                .assertCompilable()
                .assertOutput("package test;\n" +
                              "public class Test {\n" +
                              "    public void test(String[] args) {\n" +
                              "        \"a\".replaceAll(\"\\\\.\", \"/\");\n" +
                              "     }\n" +
                              "}\n");
    }

    public void testNegative1() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "    public void test(String[] args) {\n" +
                       "        \"a\".replaceAll(\",\", \"/\");\n" +
                       "    }\n" +
                       "}\n")
                .run(Tiny.class)
                .assertWarnings();
    }

    public void testIgnoredNewObject1() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "    public void test(String[] args) {\n" +
                       "        new Object();\n" +
                       "    }\n" +
                       "}\n")
                .run(Tiny.class)
                .assertWarnings("3:8-3:21:verifier:new Object");
    }

    public void testIgnoredNewObject2() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "        public static void test() {\n" +
                       "            new Test().new T(1, 3);\n" +
                       "        }\n" +
                       "        private class T {\n" +
                       "            public T(int i, int j) {}" +
                       "        }\n" +
                       "}\n")
                .run(Tiny.class)
                .assertWarnings("3:12-3:35:verifier:new Object");
    }

    public void testSystemArrayCopy() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "        public static void test(Object o1, Object[] o2, Object o3) {\n" +
                       "            System.arraycopy(o1, 0, o2, 0, 1);\n" +
                       "            System.arraycopy(o2, 0, o3, 0, 1);\n" +
                       "            System.arraycopy(o2, 0 - 1, o2, 0 + 2 - 4, -1);\n" +
                       "        }\n" +
                       "}\n")
                .run(Tiny.class)
                .assertWarnings("3:29-3:31:verifier:...o1 not an instance of an array type",
                                "4:36-4:38:verifier:...o3 not an instance of an array type",
                                "5:33-5:38:verifier:0-1 is negative",
                                "5:44-5:53:verifier:0+2-4 is negative",
                                "5:55-5:57:verifier:-1 is negative");
    }

    public void testEqualsNull() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "    public boolean test(String arg) {\n" +
                       "        return arg.equals(null);\n" +
                       "    }\n" +
                       "}\n")
                .run(Tiny.class)
                .findWarning("3:15-3:31:verifier:ERR_equalsNull")
                .applyFix("FIX_equalsNull")
                .assertCompilable()
                .assertOutput("package test;\n" +
                              "public class Test {\n" +
                              "    public boolean test(String arg) {\n" +
                              "        return arg == null;\n" +
                              "    }\n" +
                              "}\n");
    }

    public void testResultSet1() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "    public Object test(java.sql.ResultSet set) throws java.sql.SQLException{\n" +
                       "        return set.getBoolean(0);\n" +
                       "    }\n" +
                       "}\n")
                .run(Tiny.class)
                .assertWarnings("3:30-3:31:verifier:ERR_ResultSetZero");
    }

    public void testResultSet2() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "    public Object test(R set) {\n" +
                       "        return set.getBoolean(0);\n" +
                       "    }" +
                       "    private interface R extends java.sql.ResultSet {" +
                       "        public boolean getBoolean(int i);" +
                       "    }\n" +
                       "}\n")
                .run(Tiny.class)
                .assertWarnings("3:30-3:31:verifier:ERR_ResultSetZero");
    }

    public void testResultSet180027() throws Exception {
        HintTest
                .create()
                .input("package test;\n" +
                       "public class Test {\n" +
                       "    public Object test(R set, int i) {\n" +
                       "        set.getBoolean(0);\n" +
                       "        return set.getBoolean(i + 1);\n" +
                       "    }" +
                       "    private interface R extends java.sql.ResultSet {" +
                       "        public boolean getBoolean(int i);" +
                       "    }\n" +
                       "}\n")
                .run(Tiny.class)
                .assertWarnings("3:23-3:24:verifier:ERR_ResultSetZero");
    }
}
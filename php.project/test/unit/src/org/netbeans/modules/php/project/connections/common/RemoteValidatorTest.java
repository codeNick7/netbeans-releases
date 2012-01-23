/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.project.connections.common;

import org.netbeans.junit.NbTestCase;

public class RemoteValidatorTest extends NbTestCase {

    public RemoteValidatorTest(String name) {
        super(name);
    }

    public void testValidateHost() {
        assertNull(RemoteValidator.validateHost("host"));
        assertNull(RemoteValidator.validateHost("a"));
        assertNull(RemoteValidator.validateHost(" b "));
        // errors
        assertNotNull(RemoteValidator.validateHost(null));
        assertNotNull(RemoteValidator.validateHost(""));
        assertNotNull(RemoteValidator.validateHost(" "));
    }

    public void testValidateUser() {
        assertNull(RemoteValidator.validateUser("user"));
        assertNull(RemoteValidator.validateUser("a"));
        assertNull(RemoteValidator.validateUser(" b "));
        // errors
        assertNotNull(RemoteValidator.validateUser(null));
        assertNotNull(RemoteValidator.validateUser(""));
        assertNotNull(RemoteValidator.validateUser(" "));
    }

    public void testValidatePort() {
        assertNull(RemoteValidator.validatePort("10"));
        assertNull(RemoteValidator.validatePort(String.valueOf(RemoteValidator.MINIMUM_PORT)));
        assertNull(RemoteValidator.validatePort(String.valueOf(RemoteValidator.MAXIMUM_PORT)));
        // errors
        assertNotNull(RemoteValidator.validatePort(null));
        assertNotNull(RemoteValidator.validatePort(""));
        assertNotNull(RemoteValidator.validatePort(" "));
        assertNotNull(RemoteValidator.validatePort("a"));
        assertNotNull(RemoteValidator.validatePort(" a "));
        assertNotNull(RemoteValidator.validatePort(String.valueOf(RemoteValidator.MINIMUM_PORT - 1)));
        assertNotNull(RemoteValidator.validatePort(String.valueOf(RemoteValidator.MAXIMUM_PORT + 1)));
    }

    public void testValidateUploadDirectory() {
        assertNull(RemoteValidator.validateUploadDirectory("/upload/path/to/project"));
        // errors
        assertNotNull(RemoteValidator.validateUploadDirectory(null));
        assertNotNull(RemoteValidator.validateUploadDirectory(""));
        assertNotNull(RemoteValidator.validateUploadDirectory(RemoteValidator.INVALID_SEPARATOR));
        assertNotNull(RemoteValidator.validateUploadDirectory("a" + RemoteValidator.INVALID_SEPARATOR + "b"));
        assertNotNull(RemoteValidator.validateUploadDirectory("no/slash/"));
    }

    public void testValidatePositiveNumber() {
        assertNull(RemoteValidator.validatePositiveNumber("10", null, null));
        assertNull(RemoteValidator.validatePositiveNumber("0", null, null));
        // errors
        final String errorNotPositive = "errorNotPositive";
        final String errorNotNumeric = "errorNotNumeric";
        assertEquals(errorNotNumeric, RemoteValidator.validatePositiveNumber(null, errorNotPositive, errorNotNumeric));
        assertEquals(errorNotNumeric, RemoteValidator.validatePositiveNumber("", errorNotPositive, errorNotNumeric));
        assertEquals(errorNotNumeric, RemoteValidator.validatePositiveNumber(" ", errorNotPositive, errorNotNumeric));
        assertEquals(errorNotNumeric, RemoteValidator.validatePositiveNumber("a", errorNotPositive, errorNotNumeric));
        assertEquals(errorNotPositive, RemoteValidator.validatePositiveNumber("-1", errorNotPositive, errorNotNumeric));
        assertEquals(errorNotPositive, RemoteValidator.validatePositiveNumber("-100", errorNotPositive, errorNotNumeric));
    }

}

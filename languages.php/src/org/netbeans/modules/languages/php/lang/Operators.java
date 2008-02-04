/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package org.netbeans.modules.languages.php.lang;

/**
 * Operators.
 * 
 * @author Victor G. Vasilyev
 */
public enum Operators {
    /**
     * The Scope Resolution Operator (<code>::</code>).
     * 
     * @see <a href="http://www.php.net/manual/en/language.oop5.paamayim-nekudotayim.php">
     * PHP Manual / Part III. Language Reference / 
     * Chapter 19. Classes and Objects (PHP 5) / Scope Resolution Operator (::)</a>
     */
    SCOPE_RESOLUTION("::") {
        boolean equals(String value) {
            return value().equalsIgnoreCase(value);
        }
    },
    
    /**
     * The Object Member Access Operator (<code>-&gt;</code>).
     * <p>
     * Unfortunately, the PHP manual doesn't define this operator exactly.
     * Nevertheless, the section "List of Parser Tokens" referes to 
     * the section <a href="http://www.php.net/manual/en/language.oop.php">
     * Classes and Objects (PHP 4)</a> where there are many occurences of the 
     * operator, but there aren't a definition and descriptions.
     * It seems the section <a href="http://www.php.net/manual/en/language.oop.php">
     * Classes and Objects (PHP 5)</a> assumes that the operator has been 
     * completely described in the previous sections.
     * </p>
     */
    OBJECT_MEMBER_ACCESS("->") {
        boolean equals(String value) {
            return value().equalsIgnoreCase(value);
        }
    },
    ;
    
   Operators(String value) { this.value = value; }
    private final String value;
    public String value() { return value; }

    /**
     * Tests if the specified <code>value</code> is the underlying 
     * operator.
     * 
     * @param value any string, including <code>null</code> that will be tested. 
     * @return <code>true</code> if the specified string is the underlying 
     * <code>Constants</code>, otherwise - <code>false</code>.
     */
    abstract boolean equals(String value);

}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */
package org.netbeans.modules.cnd.dwarfdump.dwarfconsts;

import java.util.HashMap;

/**
 * Symbol table types (st_info)
 *
 * @author Alexander Simon
 */
public enum STT {
    STT_NOTYPE(0, "NOTYPE"), /* Type not specified */ // NOI18N
    STT_OBJECT(1, "OBJECT"), /* Associated with a data object */ // NOI18N
    STT_FUNC(2, "FUNC"), /* Associated with a function */ // NOI18N
    STT_SECTION(3, "SECTION"), /* Associated with a section */ // NOI18N
    STT_FILE(4, "FILE"), /* Associated with a file name */ // NOI18N
    STT_COMMON(5, "COMMON"), /* Uninitialised common block */ // NOI18N
    STT_TLS(6, "TLS"), /* Thread local data object */ // NOI18N
    STT_NUM(7, "NUM"), // NOI18N
    STT_LOOS(10, "LOOS"), /* Operating system specific range */ // NOI18N
    STT_HIOS(12, "HIOS"), // NOI18N
    STT_LOPROC(13, "LOPROC"), /* Processor-specific range */// NOI18N
    STT_HIPROC(15, "HIPROC"); // NOI18N
    
    private static final HashMap<Integer, STT> hashmap = new HashMap<Integer, STT>();
    private final int value;
    private final String name;
    
    static {
        for (STT elem : values()) {
            hashmap.put(elem.value, elem);
        }
    }
    
    STT(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public static STT get(int val) {
        return hashmap.get(val);
    }
    
    public int value() {
        return value;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}

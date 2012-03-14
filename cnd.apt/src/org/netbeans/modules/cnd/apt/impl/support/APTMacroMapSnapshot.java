/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
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

package org.netbeans.modules.cnd.apt.impl.support;

import org.netbeans.modules.cnd.antlr.TokenStream;
import java.io.IOException;
import java.util.*;
import org.netbeans.modules.cnd.apt.structure.APTDefine;
import org.netbeans.modules.cnd.apt.support.APTMacro;
import org.netbeans.modules.cnd.apt.support.APTToken;
import org.netbeans.modules.cnd.apt.utils.APTSerializeUtils;
import org.netbeans.modules.cnd.apt.utils.APTUtils;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataInput;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataOutput;
import org.netbeans.modules.cnd.utils.cache.TinyMaps;
import org.openide.util.CharSequences;

/**
 *
 * @author gorrus
 */
public final class APTMacroMapSnapshot {
    private static final Map<CharSequence, APTMacro> NO_MACROS = Collections.unmodifiableMap(new HashMap<CharSequence, APTMacro>(0));
    private Map<CharSequence, APTMacro> macros;

    /*package*/ final APTMacroMapSnapshot parent;
    
    public APTMacroMapSnapshot(APTMacroMapSnapshot parent) {
        macros = createMacroMap(0);
        assert (parent == null || parent.parent == null || !parent.parent.isEmtpy()) : "how grand father could be empty " + parent;
        // optimization to prevent chaining of empty snapshots
        while (parent != null && parent.isEmtpy()) {
            parent = parent.parent;
        }
        this.parent = parent;
    }

    private Map<CharSequence, APTMacro> createMacroMap(int prefferedSize) {
        if (prefferedSize == 0) {
            return NO_MACROS;
        }
        return TinyMaps.createMap(prefferedSize);
    }

    private void prepareMacroMapToAddMacro(CharSequence name) {
        if (macros == NO_MACROS) {
            // create LW map
            macros = createMacroMap(1);
        } else {
            // expand map if needed based on expected next key
            macros = TinyMaps.expandForNextKey(macros, name);
        }
    }

    /*package*/ final void putMacro(CharSequence name, APTMacro macro) {
        prepareMacroMapToAddMacro(name);
        macros.put(name, macro);
    }

    public final APTMacro getMacro(APTToken token) {
        return getMacro(token.getTextID());
    }
    
    /*package*/ final APTMacro getMacro(CharSequence key) {
        assert CharSequences.isCompact(key) : "string can't be here " + key;
        APTMacroMapSnapshot currentSnap = this;
        while (currentSnap != null) {
            APTMacro macro = currentSnap.macros.get(key);
            if (macro != null) {
                return macro;
            }
            currentSnap = currentSnap.parent;
        }
        return null;
    }
    
    @Override
    public String toString() {
        Map<CharSequence, APTMacro> tmpMap = addAllMacros(this, null);
        return APTUtils.macros2String(tmpMap);
    }
    
    public static Map<CharSequence, APTMacro> addAllMacros(APTMacroMapSnapshot snap, Map<CharSequence, APTMacro> out) {
        if (snap != null) {
            int i = 0;
            LinkedList<APTMacroMapSnapshot> stack = new LinkedList<APTMacroMapSnapshot>();
            while(snap != null) {
                i += snap.macros.size();
                stack.add(snap);
                snap = snap.parent;
            }
            if (out == null) {
                out = new HashMap<CharSequence, APTMacro>(i);
            }
            while(!stack.isEmpty()) {
                snap = stack.removeLast();
                for (Map.Entry<CharSequence, APTMacro> cur : snap.macros.entrySet()) {
                    if (cur.getValue() != UNDEFINED_MACRO) {
                        out.put(cur.getKey(), cur.getValue());
                    } else {
                        out.remove(cur.getKey());
                    }
                }
            }
        }
        if (out == null) {
            out = new HashMap<CharSequence, APTMacro>();
        }
        return out;
    }

    public static int getMacroSize(APTMacroMapSnapshot snap) {
        int size = 0;
        while (snap != null) {
            size += snap.macros.size();
            snap = snap.parent;
        }
        return size;
    }

    public boolean isEmtpy() {
        return macros.isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////
    // persistence support
    
    public void write(RepositoryDataOutput output) throws IOException {
        APTSerializeUtils.writeSnapshot(this.parent, output);
        APTSerializeUtils.writeStringToMacroMap(this.macros, output);
    }
    
    public APTMacroMapSnapshot(RepositoryDataInput input) throws IOException {
        this.parent = APTSerializeUtils.readSnapshot(input);
        int collSize = input.readInt();
        macros = createMacroMap(collSize);
        APTSerializeUtils.readStringToMacroMap(collSize, this.macros, input);
    }  
        
    //This is a single instance of a class to indicate that macro is undefined,
    //not a child of APTMacro to track errors more easily
    public static final APTMacro UNDEFINED_MACRO = new UndefinedMacro();

    private static final class UndefinedMacro implements APTMacro {
        @Override
        public String toString() {
            return "Macro undefined"; // NOI18N
        }

        @Override
        public CharSequence getFile() {
            return CharSequences.empty();
        }
        
        @Override
        public Kind getKind() {
            return Kind.USER_SPECIFIED;
        }

        @Override
        public boolean isFunctionLike() {
            throw new UnsupportedOperationException("Not supported in fake impl"); // NOI18N
        }

        @Override
        public APTToken getName() {
            throw new UnsupportedOperationException("Not supported in fake impl"); // NOI18N
        }

        @Override
        public Collection<APTToken> getParams() {
            throw new UnsupportedOperationException("Not supported in fake impl"); // NOI18N
        }

        @Override
        public TokenStream getBody() {
            throw new UnsupportedOperationException("Not supported in fake impl"); // NOI18N
        }

        @Override
        public APTDefine getDefineNode() {
            throw new UnsupportedOperationException("Not supported in fake impl."); // NOI18N
        }

    }
}

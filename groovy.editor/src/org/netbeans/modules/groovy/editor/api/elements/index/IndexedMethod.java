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
package org.netbeans.modules.groovy.editor.api.elements.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.groovy.editor.api.elements.common.MethodElement;
import org.netbeans.modules.parsing.spi.indexing.support.IndexResult;

/**
 * A class describing a Groovy method that is in "textual form" (signature, filename, etc.)
 * obtained from the code index.
 *
 * @author Tor Norbye
 * @author Martin Adamek
 */
public final class IndexedMethod extends IndexedElement implements MethodElement {
    /** This method takes a (possibly optional, see BLOCK_OPTIONAL) block */
    public static final int BLOCK = 1 << 6;
    /** This method takes an optional block */
    public static final int BLOCK_OPTIONAL = 1 << 7;
    /** Deprecated? */
    /** Parenthesis or space delimited? */

    protected final String signature;
    private String[] args;
    private List<MethodParameter> parameters;
    private boolean smart;
    private final String returnType;
    
    private IndexedMethod(String signature, String returnType, IndexResult result, String clz, String attributes, int flags) {
        super(result, clz, attributes, flags);
        this.signature = signature;
        this.returnType = returnType;
    }

    public static IndexedMethod create(String signature, String returnType,
            String clz, IndexResult result, String attributes, int flags) {
        IndexedMethod m = new IndexedMethod(signature, returnType, result, clz, attributes, flags);

        return m;
    }

    @Override
    public String toString() {
        return getSignature();
    }

    @Override
    public String getName() {
        if (name == null) {
            int parenIndex = signature.indexOf('(');

            if (parenIndex == -1) {
                name = signature;
            } else {
                name = signature.substring(0, parenIndex);
            }
        }

        return name;
    }

    @Override
    public String getReturnType() {
        return returnType;
    }

    @Override
    public String getSignature() {
        return in + "#" + signature;
    }

    private String[] getArgs() {
        if (args == null) {
            // Parse signature
            int parenIndex = signature.indexOf('(');

            if (parenIndex == -1) {
                return new String[0];
            }

            String argsPortion = signature.substring(parenIndex + 1, signature.length() - 1);
            args = argsPortion.split(","); // NOI18N
        }

        return args;
    }

    @Override
    public List<MethodParameter> getParameters() {
        if (parameters == null) {
            String[] argArray = getArgs();

            if ((argArray != null) && (argArray.length > 0)) {
                parameters = new ArrayList<>(argArray.length);
                for (String paramType : argArray) {
                    parameters.add(new MethodParameter(paramType, null, null));
                }
            } else {
                parameters = Collections.emptyList();
            }
        }

        return parameters;
    }

    @Override
    public List<String> getParameterTypes() {
        List<String> paramTypes = new ArrayList<>();

        for (MethodParameter parameter : getParameters()) {
            paramTypes.add(parameter.getType());
        }
        return paramTypes;
    }

    @Override
    public ElementKind getKind() {
        if (((name == null) && signature.startsWith("initialize(")) || // NOI18N
                ((name != null) && name.equals("initialize"))) { // NOI18N

            return ElementKind.CONSTRUCTOR;
        } else {
            return ElementKind.METHOD;
        }
    }
    
    public boolean isSmart() {
        return smart;
    }
    
    public void setSmart(boolean smart) {
        this.smart = smart;
    }

    public boolean hasBlock() {
        return (flags & BLOCK) != 0;
    }

    public boolean isBlockOptional() {
        return (flags & BLOCK_OPTIONAL) != 0;
    }

    public String getEncodedAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IndexedMethod other = (IndexedMethod) obj;
        if (this.signature != other.signature && (this.signature == null || !this.signature.equals(other.signature))) {
            return false;
        }
        if (this.in != other.in && (this.in == null || !this.in.equals(other.in))) {
            return false;
        }
        if (this.flags != other.flags) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.signature != null ? this.signature.hashCode() : 0);
        hash = 53 * hash + (this.in != null ? this.in.hashCode() : 0);
        hash = 53 * hash + flags;
        return hash;
    }
}

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


package org.netbeans.modules.cnd.modelimpl.csm;

import org.netbeans.modules.cnd.api.model.*;
import org.netbeans.modules.cnd.antlr.collections.AST;
import java.io.IOException;
import org.netbeans.modules.cnd.modelimpl.csm.deep.ExpressionBase;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataInput;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataOutput;

/**
 * Implements CsmParameter
 * @author Vladimir Kvashin
 */
public class ParameterImpl extends VariableImpl<CsmParameter> implements CsmParameter {

    protected ParameterImpl(AST ast, CsmFile file, CsmType type, NameHolder name, CsmScope scope) {
        super(ast, file, type, name, scope, false, false);
    }

    protected ParameterImpl(CsmType type, CharSequence name, CsmScope scope, ExpressionBase initExpr, CsmFile file, int startOffset, int endOffset) {
        super(type, name, scope, false, false, initExpr, file, startOffset, endOffset);
    }
    
    public static ParameterImpl create(AST ast, CsmFile file, CsmType type, NameHolder name, CsmScope scope) {
        ParameterImpl parameterImpl = new ParameterImpl(ast, file, type, name, scope);
        return parameterImpl;
    }

    @Override
    protected CsmUID<? extends CsmOffsetableDeclaration> createUID() {
        assert false;
        return super.createUID();
    }

    @Override
    protected boolean registerInProject() {
        return false;
    }

    @Override
    protected boolean unregisterInProject() {
        return false;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }
    
    
    public static class ParameterBuilder extends SimpleDeclarationBuilder {

        @Override
        public ParameterImpl create() {
            ParameterImpl param = new ParameterImpl(getType(), getName(), getScope(), null, getFile(), getStartOffset(), getEndOffset());
            return param;
        }
    }      
    
    ////////////////////////////////////////////////////////////////////////////
    // impl of SelfPersistent
    
    @Override
    public void write(RepositoryDataOutput output) throws IOException {
        super.write(output);      
    }

    @Override
    protected boolean isScopePersistent() {
        return false;
    }

    public ParameterImpl(RepositoryDataInput input, CsmScope scope) throws IOException {
        super(input, scope);
    } 
}

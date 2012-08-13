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

package org.netbeans.modules.cnd.modelimpl.csm.core;

import java.io.IOException;
import java.util.List;
import org.netbeans.modules.cnd.antlr.collections.AST;
import org.netbeans.modules.cnd.api.model.CsmFile;
import org.netbeans.modules.cnd.api.model.CsmOffsetableDeclaration;
import org.netbeans.modules.cnd.api.model.CsmProject;
import org.netbeans.modules.cnd.api.model.CsmScope;
import org.netbeans.modules.cnd.api.model.CsmTemplate;
import org.netbeans.modules.cnd.api.model.CsmTemplateParameter;
import org.netbeans.modules.cnd.api.model.CsmUID;
import org.netbeans.modules.cnd.api.model.util.CsmKindUtilities;
import org.netbeans.modules.cnd.modelimpl.content.file.FileContent;
import org.netbeans.modules.cnd.modelimpl.csm.CsmObjectBuilder;
import org.netbeans.modules.cnd.modelimpl.csm.TemplateDescriptor;
import org.netbeans.modules.cnd.modelimpl.csm.TemplateUtils;
import org.netbeans.modules.cnd.modelimpl.debug.TraceFlags;
import org.netbeans.modules.cnd.modelimpl.parser.generated.CPPTokenTypes;
import org.netbeans.modules.cnd.modelimpl.repository.RepositoryUtils;
import org.netbeans.modules.cnd.modelimpl.uid.UIDUtilities;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataInput;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataOutput;
import org.openide.util.CharSequences;

/**
 *
 * @author Vladimir Kvashin
 */
public abstract class OffsetableDeclarationBase<T> extends OffsetableIdentifiableBase<T> implements CsmOffsetableDeclaration {
    
    public static final char UNIQUE_NAME_SEPARATOR = ':';
    
    protected OffsetableDeclarationBase(CsmFile file, int startOffset, int endOffset) {
        super(file, startOffset, endOffset);
    }

    @Override
    public CharSequence getUniqueName() {
        return CharSequences.create(Utils.getCsmDeclarationKindkey(getKind()) + UNIQUE_NAME_SEPARATOR + getUniqueNameWithoutPrefix());
    }
    
    public CharSequence getUniqueNameWithoutPrefix() {
        return getQualifiedName();
    }
    
    protected final CsmProject getProject() {
        CsmFile file = this.getContainingFile();
        assert file != null;
        return file != null ? file.getProject() : null;
    }    
    
    public CharSequence getQualifiedNamePostfix() {
        if (TraceFlags.SET_UNNAMED_QUALIFIED_NAME && (getName().length() == 0)) {
            return getOffsetBasedName();
        } else {
            return getName();
        }
    }

    protected CharSequence toStringName() {
        CharSequence name;
        if (CsmKindUtilities.isTemplate(this)) {
            name = ((CsmTemplate)this).getDisplayName();
        } else {
            name = getName();
        }
        if (this instanceof RawNamable) {
            StringBuilder out = new StringBuilder(name);
            out.append('(');
            boolean first = true;
            for (CharSequence part : ((RawNamable)this).getRawName()) {
                if (first) {
                    first = false;
                } else {
                    out.append("::"); //NOI18N
                }
                out.append(part);
            }
            out.append(')');
            name = out.toString();
        }
        return name;
    }
    
    private String getOffsetBasedName() {
        return "[" + this.getContainingFile().getName() + ":" + this.getStartOffset() + "-" + this.getEndOffset() + "]"; // NOI18N
    }   

    @Override
    protected CsmUID<? extends CsmOffsetableDeclaration> createUID() {
        return UIDUtilities.<CsmOffsetableDeclaration>createDeclarationUID(this);
    }

    protected static TemplateDescriptor createTemplateDescriptor(AST node, CsmFile file, CsmScope scope, StringBuilder classTemplateSuffix, boolean global) {
        boolean _template = false, specialization = false;
        switch(node.getType()) {
            case CPPTokenTypes.CSM_FUNCTION_TEMPLATE_DECLARATION: 
            case CPPTokenTypes.CSM_FUNCTION_TEMPLATE_DEFINITION: 
            case CPPTokenTypes.CSM_CTOR_TEMPLATE_DECLARATION: 
            case CPPTokenTypes.CSM_CTOR_TEMPLATE_DEFINITION:  
            case CPPTokenTypes.CSM_TEMPL_FWD_CL_OR_STAT_MEM:
            case CPPTokenTypes.CSM_USER_TYPE_CAST_TEMPLATE_DECLARATION:
            case CPPTokenTypes.CSM_USER_TYPE_CAST_TEMPLATE_DEFINITION:
                _template = true;
                break;
            case CPPTokenTypes.CSM_TEMPLATE_FUNCTION_DEFINITION_EXPLICIT_SPECIALIZATION:
            case CPPTokenTypes.CSM_TEMPLATE_CTOR_DEFINITION_EXPLICIT_SPECIALIZATION:
            case CPPTokenTypes.CSM_TEMPLATE_DTOR_DEFINITION_EXPLICIT_SPECIALIZATION:
            case CPPTokenTypes.CSM_TEMPLATE_EXPLICIT_SPECIALIZATION:
                _template = true;
                specialization = true;
                break;
        }
        if (_template) {
            boolean templateClass = false;
            List<CsmTemplateParameter> templateParams = null;
            AST templateNode = node.getFirstChild();
            AST templateClassNode = templateNode;            
            if (templateNode == null || templateNode.getType() != CPPTokenTypes.LITERAL_template) {
                return null;
            }
            // 0. our grammar can't yet differ template-class's method from template-method
            // so we need to check here if we has template-class or not
            AST qIdToken = AstUtil.findChildOfType(node, CPPTokenTypes.CSM_QUALIFIED_ID);
            // 1. check for definition of template class's method
            // like template<class A> C<A>:C() {}
            AST startTemplateSign = qIdToken != null ? AstUtil.findChildOfType(qIdToken, CPPTokenTypes.LESSTHAN) : null;
            if (startTemplateSign != null) {
                // TODO: fix parsing of inline definition of template operator <
                // like template<class T, class P> bool operator<(T x, P y) {return x<y};
                // workaround is next validation
                AST endTemplateSign = null;//( startTemplateSign.getNextSibling() != null ? startTemplateSign.getNextSibling().getNextSibling() : null);
                for( AST sibling = startTemplateSign.getNextSibling(); sibling != null; sibling = sibling.getNextSibling() ) {
                    if( sibling.getType() == CPPTokenTypes.GREATERTHAN ) {
                        endTemplateSign = sibling;
                        break;
                    }
                }
                if (endTemplateSign != null) {
                    AST scopeSign = endTemplateSign.getNextSibling();
                    if (scopeSign != null && scopeSign.getType() == CPPTokenTypes.SCOPE) {
                        // 2. we have template class, we need to determine, is it specialization definition or not
                        if (specialization && classTemplateSuffix != null) { 
                            // we need to initialize classTemplateSuffix in this case
                            // to avoid mixing different specialization (IZ92138)
                            classTemplateSuffix.append(TemplateUtils.getSpecializationSuffix(qIdToken, null));
                        }     
                        // but there is still a chance to have template-method of template-class
                        // e.g.: template<class A> template<class B> C<A>::C(B b) {}
                        AST templateSiblingNode = templateNode.getNextSibling();
                        if ( templateSiblingNode != null && templateSiblingNode.getType() == CPPTokenTypes.LITERAL_template ) {
                            // it is template-method of template-class
                            templateNode = templateSiblingNode;
                            templateClass = true;
                        } else {
                            // we have no template-method at all
                            templateClass = true;
                            _template = false;
                        }
                    }
                }
            }
            int inheritedTemplateParametersNumber = 0;
            if(templateClass){
                templateParams = TemplateUtils.getTemplateParameters(templateClassNode,
                    file, scope, global);
                inheritedTemplateParametersNumber = templateParams.size();
            }
            CharSequence templateSuffix = "";
            if (_template) {                
                // 3. We are sure now what we have template-method, 
                // let's check is it specialization template or not
                if (specialization) {
                    // 3a. specialization
                    if (qIdToken == null) {
                        // malformed template specification
                        templateSuffix = "<>"; //NOI18N
                    } else {
                        templateSuffix = TemplateUtils.getSpecializationSuffix(qIdToken, null);
                    }
                } else {
                    // 3b. no specialization, plain and simple template-method
                    StringBuilder sb  = new StringBuilder();
                    TemplateUtils.addSpecializationSuffix(templateNode.getFirstChild(), sb, null);
                    templateSuffix = '<' + sb.toString() + '>';
                }                
                if(templateParams != null) {
                    templateParams.addAll(TemplateUtils.getTemplateParameters(templateNode,
                        file, scope, global));
                } else {
                    templateParams = TemplateUtils.getTemplateParameters(templateNode,
                        file, scope, global);
                }
            }            
            return new TemplateDescriptor(
                templateParams, templateSuffix, inheritedTemplateParametersNumber, specialization, global);
        }
        return null;
    }
    
    public static class SimpleDeclarationBuilder implements CsmObjectBuilder {
        
        private boolean typedefSpecifier = false;
        private boolean typeSpecifier = false;
        private boolean inDeclSpecifiers = false;
        private DeclaratorBuilder declaratorBuilder;
        
        
        public void setTypedefSpecifier() {
            this.typedefSpecifier = true;
        }

        public boolean hasTypedefSpecifier() {
            return typedefSpecifier;
        }

        public void setTypeSpecifier() {
            this.typeSpecifier = true;
        }

        public boolean hasTypeSpecifier() {
            return typeSpecifier && inDeclSpecifiers;
        }
        
        public void declSpecifiers() {
            inDeclSpecifiers = true;
        }

        public void endDeclSpecifiers() {
            inDeclSpecifiers = false;
        }
        
        public boolean isInDeclSpecifiers() {
            return inDeclSpecifiers;
        }

        public void setDeclarator(DeclaratorBuilder declaratorBuilder) {
            this.declaratorBuilder = declaratorBuilder;
        }

        public DeclaratorBuilder getDeclaratorBuilder() {
            return declaratorBuilder;
        }
        
    }
    
    public static class DeclaratorBuilder implements CsmObjectBuilder {

        private int level = 0;
        private CharSequence name;
        
        public void setName(CharSequence name) {
            this.name = name;
        }

        public CharSequence getName() {
            return name;
        }
        
        public void enterDeclarator() {
            level++;
        }
        
        public void leaveDeclarator() {
            level--;
        }
        
        public boolean isTopDeclarator() {
            return level == 0;
        }
        
    }    
    
    ////////////////////////////////////////////////////////////////////////////
    // impl of SelfPersistent
    
    @Override
    public void write(RepositoryDataOutput output) throws IOException {
        super.write(output);
    }  
    
    protected OffsetableDeclarationBase(RepositoryDataInput input) throws IOException {
        super(input);
    }    

    @Override
    public String toString() {
        CharSequence name = toStringName();
        return "" + getKind() + ' ' + name  + getOffsetString() + getPositionString(); // NOI18N
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)){
            return false;
        }
        return getName().equals(((OffsetableDeclarationBase<?>)obj).getName());
    }

    @Override
    public int hashCode() {
        return  31*super.hashCode() + getName().hashCode();
    }

    protected boolean registerInProject() {
        //do nothing by default
        return false;
    }

    protected static<T> void postObjectCreateRegistration(boolean register, OffsetableDeclarationBase<T> obj) {
        if (register) {
            if (!obj.registerInProject()) {
                RepositoryUtils.put(obj);
            }
        } else {
            Utils.setSelfUID(obj);
        }
    }

    protected static<T> void temporaryRepositoryRegistration(boolean global, OffsetableDeclarationBase<T> obj) {
        if (global) {
            RepositoryUtils.hang(obj); // "hang" now and then "put" in "register()"
        } else {
            Utils.setSelfUID(obj);
        }
    }
}

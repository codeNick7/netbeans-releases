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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */
package org.netbeans.modules.cnd.modelimpl.impl.services.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.modules.cnd.antlr.TokenStream;
import org.netbeans.modules.cnd.antlr.collections.AST;
import org.netbeans.modules.cnd.api.model.CsmClass;
import org.netbeans.modules.cnd.api.model.CsmClassifier;
import org.netbeans.modules.cnd.api.model.CsmExpressionBasedSpecializationParameter;
import org.netbeans.modules.cnd.api.model.CsmField;
import org.netbeans.modules.cnd.api.model.CsmFile;
import org.netbeans.modules.cnd.api.model.CsmFunctional;
import org.netbeans.modules.cnd.api.model.CsmInstantiation;
import org.netbeans.modules.cnd.api.model.CsmMember;
import org.netbeans.modules.cnd.api.model.CsmObject;
import org.netbeans.modules.cnd.api.model.CsmOffsetableDeclaration;
import org.netbeans.modules.cnd.api.model.CsmScope;
import org.netbeans.modules.cnd.api.model.CsmSpecializationParameter;
import org.netbeans.modules.cnd.api.model.CsmTemplate;
import org.netbeans.modules.cnd.api.model.CsmTemplateParameter;
import org.netbeans.modules.cnd.api.model.CsmTemplateParameterType;
import org.netbeans.modules.cnd.api.model.CsmType;
import org.netbeans.modules.cnd.api.model.CsmTypeBasedSpecializationParameter;
import org.netbeans.modules.cnd.api.model.deep.CsmExpression;
import org.netbeans.modules.cnd.api.model.services.CsmClassifierResolver;
import org.netbeans.modules.cnd.api.model.services.CsmEntityResolver;
import org.netbeans.modules.cnd.api.model.util.CsmBaseUtilities;
import org.netbeans.modules.cnd.api.model.util.CsmKindUtilities;
import org.netbeans.modules.cnd.apt.support.APTTokenStreamBuilder;
import org.netbeans.modules.cnd.apt.support.lang.APTLanguageFilter;
import org.netbeans.modules.cnd.apt.support.lang.APTLanguageSupport;
import org.netbeans.modules.cnd.modelimpl.csm.Instantiation;
import org.netbeans.modules.cnd.modelimpl.csm.TemplateUtils;
import org.netbeans.modules.cnd.modelimpl.csm.TypeFactory;
import org.netbeans.modules.cnd.modelimpl.csm.core.Utils;
import org.netbeans.modules.cnd.modelimpl.csm.resolver.Resolver;
import org.netbeans.modules.cnd.modelimpl.csm.resolver.Resolver3;
import org.netbeans.modules.cnd.modelimpl.csm.resolver.ResolverFactory;
import org.netbeans.modules.cnd.modelimpl.debug.TraceFlags;
import org.netbeans.modules.cnd.modelimpl.impl.services.ExpressionEvaluator;
import org.netbeans.modules.cnd.modelimpl.impl.services.InstantiationProviderImpl;
import org.netbeans.modules.cnd.modelimpl.impl.services.MemberResolverImpl;
import org.netbeans.modules.cnd.modelimpl.parser.CPPParserEx;
import org.netbeans.modules.cnd.modelimpl.util.MapHierarchy;
import org.netbeans.modules.cnd.utils.Antiloop;

/**
 *
 * @author nk220367
 */
public class VariableProvider {
    private static final Logger LOG = Logger.getLogger(VariableProvider.class.getSimpleName());
    public static final int INFINITE_RECURSION = 16;

    private final int level;
    private CsmOffsetableDeclaration decl;
    private MapHierarchy<CsmTemplateParameter, CsmSpecializationParameter> mapping;
    private CsmFile variableFile; 
    private int variableStartOffset;
    private int variableEndOffset;
    
    
    public VariableProvider(int level) {
        this.level = level;
        if (LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "\nVARIABLE PROVIDER CREATED WITHOUT MAP HIERARCHY\n"); // NOI18N
        }
    }
    
    public VariableProvider(CsmOffsetableDeclaration decl, MapHierarchy<CsmTemplateParameter, CsmSpecializationParameter> mapping, CsmFile variableFile, int variableStartOffset, int variableEndOffset, int level) {
        this.decl = decl;
        this.mapping = mapping;
        this.variableFile = variableFile != null ? variableFile : (decl != null ? decl.getContainingFile() : null);
        this.variableStartOffset = variableStartOffset;
        this.variableEndOffset = variableEndOffset;
        this.level = level;
        if (LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "\nVARIABLE PROVIDER CREATED WITH MAP HIERARCHY:\n{0}\n", mapping); // NOI18N
        }
    }    

    public int getValue(String variableName) {
        if(level > INFINITE_RECURSION) {
            return 0;
        }
        long time = System.currentTimeMillis();
        try {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "GetValue for {0}:{1}\n", new Object[]{variableName, decl});
            }
            if(variableName.equals("true")) { // NOI18N
                return 1;
            }
            if(variableName.equals("false")) { // NOI18N
                return 0;
            }
            if (decl != null) {
                for (Map.Entry<CsmTemplateParameter, CsmSpecializationParameter> entry : mapping.entries()) {
                    CsmTemplateParameter param = entry.getKey();
                    if (variableName.equals(param.getQualifiedName().toString()) ||
                            (decl.getQualifiedName() + "::" + variableName).equals(param.getQualifiedName().toString())) { // NOI18N
                        CsmSpecializationParameter spec = entry.getValue();
                        if (CsmKindUtilities.isExpressionBasedSpecalizationParameter(spec)) {
                            CsmExpressionBasedSpecializationParameter specParam = (CsmExpressionBasedSpecializationParameter) spec;
                            Object eval = new ExpressionEvaluator(level+1).eval(
                                    specParam.getText().toString(), 
                                    decl, 
                                    specParam.getContainingFile(),
                                    specParam.getStartOffset(),
                                    specParam.getEndOffset(),
                                    mapping
                            );
                            if (eval instanceof Integer) {
                                return (Integer) eval;
                            }
                        } else if (CsmKindUtilities.isTypeBasedSpecalizationParameter(spec)) {
                            CsmTypeBasedSpecializationParameter specParameter = (CsmTypeBasedSpecializationParameter) spec;
                            
// TODO: think about param decl and appropriate mapping (could be necessary if parameter is instantiation itself)
//                            CsmOffsetableDeclaration paramContextDecl = decl;
//                            MapHierarchy<CsmTemplateParameter, CsmSpecializationParameter> paramMapping = mapping;
//                            
//                            CsmType specParamType = specParameter.getType();
//                            CsmClassifier specParamCls = specParamType.getClassifier();
//                            if (CsmKindUtilities.isInstantiation(specParamCls) && CsmKindUtilities.isOffsetableDeclaration(specParamCls)) {
//                                paramContextDecl = (CsmOffsetableDeclaration) specParamCls;
//                                paramMapping = TemplateUtils.gatherMapping((CsmInstantiation) paramContextDecl);
//                            }
                            
                            Object eval = new ExpressionEvaluator(level+1).eval(
                                    specParameter.getText().toString(), 
                                    decl, 
                                    specParameter.getContainingFile(), 
                                    specParameter.getStartOffset(), 
                                    specParameter.getEndOffset(), 
                                    mapping
                            );
                            
                            if (eval instanceof Integer) {
                                return (Integer) eval;
                            }
                        }
                    }
                }
                if(CsmKindUtilities.isClass(decl)) {
                    final CsmClass clazz = (CsmClass) decl;
                    MemberResolverImpl r = new MemberResolverImpl();
                    final Iterator<CsmMember> classMembers = r.getDeclarations(clazz, variableName);
                    if (classMembers.hasNext()) {
                        CsmMember member = classMembers.next();
                        if(member.isStatic() && CsmKindUtilities.isField(member) && member.getName().toString().equals(variableName)) {
                            CsmExpression expr = ((CsmField)member).getInitialValue();
                            if(CsmKindUtilities.isInstantiation(member)) {
                                Object eval = new ExpressionEvaluator(level+1).eval(
                                        expr.getExpandedText().toString(), 
                                        member.getContainingClass(), 
                                        expr.getContainingFile(),
                                        expr.getStartOffset(),
                                        expr.getEndOffset(),
                                        getMapping((CsmInstantiation) member)
                                );
                                if (eval instanceof Integer) {
                                    return (Integer) eval;
                                }
                            } else if (expr != null) {
                                Object eval = new ExpressionEvaluator(level+1).eval(
                                        expr.getExpandedText().toString(), 
                                        member.getContainingClass(), 
                                        expr.getContainingFile(),
                                        expr.getStartOffset(),
                                        expr.getEndOffset(),
                                        Collections.<CsmTemplateParameter, CsmSpecializationParameter>emptyMap()
                                );
                                if (eval instanceof Integer) {
                                    return (Integer) eval;
                                }                            
                            }
                        }
                    }
                }

                boolean executeSimpleResolution = !(TraceFlags.EXPRESSION_EVALUATOR_DEEP_VARIABLE_PROVIDER && variableName.contains("<")); // NOI18N

                if (executeSimpleResolution) {
                    CsmObject o = null;
                    Resolver aResolver = ResolverFactory.createResolver(decl);            
                    try {
                        o = aResolver.resolve(Utils.splitQualifiedName(variableName.replaceAll("(.*)::.*", "$1")), Resolver3.ALL); // NOI18N
                    } finally {
                        ResolverFactory.releaseResolver(aResolver);
                    }
                    if (CsmKindUtilities.isClassifier(o)) {
                        CsmClassifier cls = (CsmClassifier) o;
                        CsmClassifier originalClassifier = CsmClassifierResolver.getDefault().getOriginalClassifier(cls, decl.getContainingFile());
                        if(CsmKindUtilities.isInstantiation(originalClassifier)) {
                            Object eval = new ExpressionEvaluator(level+1).eval(variableName.replaceAll(".*::(.*)", "$1"), (CsmInstantiation) originalClassifier); // NOI18N
                            if (eval instanceof Integer) {
                                return (Integer) eval;
                            }
                        } else if (CsmKindUtilities.isOffsetableDeclaration(originalClassifier)) {
                            Object eval = new ExpressionEvaluator(level+1).eval(variableName.replaceAll(".*::(.*)", "$1"), (CsmOffsetableDeclaration) originalClassifier, Collections.<CsmTemplateParameter, CsmSpecializationParameter>emptyMap()); // NOI18N
                            if (eval instanceof Integer) {
                                return (Integer) eval;
                            }                    
                        }
                    }
                }

                if (TraceFlags.EXPRESSION_EVALUATOR_DEEP_VARIABLE_PROVIDER) {
                    // it works but does it too slow

                    int flags = CPPParserEx.CPP_CPLUSPLUS;
                    flags |= CPPParserEx.CPP_SUPPRESS_ERRORS;
                    // TODO get flavor from variableFile?
                    try {
                        // use cached TS
                        TokenStream buildTokenStream = APTTokenStreamBuilder.buildTokenStream(variableName.replaceAll("(.*)::.*", "$1"), APTLanguageSupport.GNU_CPP); // NOI18N
                        if (buildTokenStream != null) {
                            if (variableStartOffset > 0) {
                                buildTokenStream = new ShiftedTokenStream(buildTokenStream, variableStartOffset);
                            }

                            CPPParserEx parser = CPPParserEx.getInstance(variableFile, buildTokenStream, flags);
                            parser.type_name();
                            AST ast = parser.getAST();

                            CsmType type = TypeFactory.createType(ast, variableFile, null, 0, decl.getScope()); // TODO: decl.getScope() is a wrong scope
                            if(CsmKindUtilities.isInstantiation(decl)) {
                                type = checkTemplateType(type, (Instantiation)decl);
                            }
                            for (CsmTemplateParameter csmTemplateParameter : mapping.keys()) {
                                type = TemplateUtils.checkTemplateType(type, csmTemplateParameter.getScope());
                            }

                            if (CsmKindUtilities.isTemplateParameterType(type)) {
                                CsmSpecializationParameter instantiatedType = mapping.get(((CsmTemplateParameterType) type).getParameter());
                                int iteration = 15;
                                while (CsmKindUtilities.isTypeBasedSpecalizationParameter(instantiatedType) &&
                                        CsmKindUtilities.isTemplateParameterType(((CsmTypeBasedSpecializationParameter) instantiatedType).getType()) && iteration != 0) {
                                    CsmSpecializationParameter nextInstantiatedType = mapping.get(((CsmTemplateParameterType) ((CsmTypeBasedSpecializationParameter) instantiatedType).getType()).getParameter());
                                    if (nextInstantiatedType != null) {
                                        instantiatedType = nextInstantiatedType;
                                    } else {
                                        break;
                                    }
                                    iteration--;
                                }
                                if (instantiatedType != null && instantiatedType instanceof CsmTypeBasedSpecializationParameter) {
                                    type = ((CsmTypeBasedSpecializationParameter) instantiatedType).getType();
                                }
                            }                      

                            // TODO: think about differences between decl and mapping!!!
                            
//                            if (CsmKindUtilities.isInstantiation(decl)) {
//                                type = instantiateType(type, (Instantiation)decl);
//                            }

                            CsmClassifier originalClassifier = CsmClassifierResolver.getDefault().getOriginalClassifier(type.getClassifier(), decl.getContainingFile());
                            
                            // TODO:
                            // This block should be deleted - type should be instantiated with the right parameters
                            // (Or type should not be instantiated at all, but in such case type.getClassifier() shouldn't specialize classifier)
                            if (CsmKindUtilities.isInstantiation(originalClassifier) && !CsmKindUtilities.isSpecialization(originalClassifier)) {
                                CsmObject instantiation = originalClassifier;
                                
                                InstantiationProviderImpl ip = (InstantiationProviderImpl) InstantiationProviderImpl.getDefault();
                                
                                while (CsmKindUtilities.isInstantiation(((CsmInstantiation) instantiation).getTemplateDeclaration())) {
                                    instantiation = (CsmClassifier) ((CsmInstantiation) instantiation).getTemplateDeclaration();
                                }

                                List<Map<CsmTemplateParameter, CsmSpecializationParameter>> maps = mapping.getMaps(new MapHierarchy.NonEmptyFilter());
                                
                                for (int i = maps.size() - 1; i > 0; i--) {
                                    instantiation = ip.instantiate((CsmTemplate) instantiation, variableFile, variableStartOffset, maps.get(i), false);
                                }
                                if (!maps.isEmpty()) {
                                    instantiation = ip.instantiate((CsmTemplate) instantiation, variableFile, variableStartOffset, maps.get(0));
                                }
                                
                                if (CsmKindUtilities.isClassifier(instantiation)) {
                                    originalClassifier = (CsmClassifier) instantiation;
                                }
                            }
                            
                            if (CsmKindUtilities.isInstantiation(originalClassifier)) {
                                Object eval = new ExpressionEvaluator(level+1).eval(variableName.replaceAll(".*::(.*)", "$1"), (CsmInstantiation) originalClassifier); // NOI18N
                                if (eval instanceof Integer) {
                                    return (Integer) eval;
                                }
                            } else if (CsmKindUtilities.isOffsetableDeclaration(originalClassifier)) {
                                Object eval = new ExpressionEvaluator(level+1).eval(variableName.replaceAll(".*::(.*)", "$1"), (CsmOffsetableDeclaration) originalClassifier, mapping); // NOI18N
                                if (eval instanceof Integer) {
                                    return (Integer) eval;
                                }                                
                            }
                        }
                    } catch (Throwable ex) {
                    }
                }
            }

            return Integer.MAX_VALUE;
        } finally {
            if (LOG.isLoggable(Level.FINE)) {
                time = System.currentTimeMillis() - time;
                LOG.log(Level.FINE, "getValue {0} took {1}ms\n", new Object[]{variableName, time}); // NOI18N
            }
        }
    }
    
    public int getFunCallValue(String funCall) {
        return Integer.MAX_VALUE; // Not supported yet
    }
    
    public int getSizeOfValue(String obj) {
        if (true) {
            return Integer.MAX_VALUE; // Not supported yet
        }
        
        List<CsmInstantiation> instantiations = null;
        
        if (CsmKindUtilities.isInstantiation(decl)) {
            instantiations = new ArrayList<>();
            CsmInstantiation inst = (CsmInstantiation) decl;
            instantiations.add(inst);
            while (CsmKindUtilities.isInstantiation(inst.getTemplateDeclaration())) {
                inst = (CsmInstantiation) inst.getTemplateDeclaration();
                instantiations.add(inst);
            }
        } else if (CsmKindUtilities.isTemplate(decl) && !mapping.isEmpty()) {
            List<Map<CsmTemplateParameter, CsmSpecializationParameter>> maps = mapping.getMaps(new MapHierarchy.NonEmptyFilter());
            
            InstantiationProviderImpl ip = (InstantiationProviderImpl) InstantiationProviderImpl.getDefault();
            
            CsmObject instantiation = decl;
            
            instantiations = new ArrayList<>();
            
            for (int i = maps.size() - 1; i >= 0; i--) {
                instantiation = ip.instantiate((CsmTemplate) instantiation, variableFile, variableStartOffset, maps.get(i), false);
                instantiations.add(0, (CsmInstantiation) instantiation);
            }
        }
        
        CsmScope objScope = CsmKindUtilities.isScope(decl) ? (CsmScope) decl : null;
        
        CsmType objType = CsmEntityResolver.resolveType(obj, variableFile, variableEndOffset, objScope, instantiations);
        
        // This is necessary to resolve classifiers defined in macroses
        int counter = Antiloop.MAGIC_PLAIN_TYPE_RESOLVING_CONST;
        while (objType != null && !CsmBaseUtilities.isValid(objType.getClassifier()) && counter > 0) {
            objType = CsmEntityResolver.resolveType(objType.getText(), variableFile, variableEndOffset, objScope, instantiations);
            counter--;
        }
        
        return Utils.getSizeOfType(objType, variableFile);
    }
    
    private CsmType instantiateType(CsmType type, CsmInstantiation inst) {
        if (CsmKindUtilities.isInstantiation(inst.getTemplateDeclaration())) {
            type = instantiateType(type, (CsmInstantiation) inst.getTemplateDeclaration());
        }
        return Instantiation.createType(type, inst);
    }

    private CsmType checkTemplateType(CsmType type, CsmInstantiation inst) {
        for (CsmTemplateParameter csmTemplateParameter : inst.getMapping().keySet()) {
            type = TemplateUtils.checkTemplateType(type, csmTemplateParameter.getScope());
        }
        if(CsmKindUtilities.isInstantiation(inst.getTemplateDeclaration())) {
            type = checkTemplateType(type, (Instantiation)inst.getTemplateDeclaration());
        }
        return type;
    }

    private MapHierarchy<CsmTemplateParameter, CsmSpecializationParameter> getMapping(CsmInstantiation inst) {
        MapHierarchy<CsmTemplateParameter, CsmSpecializationParameter> mapping2 = new MapHierarchy<>(inst.getMapping());
        while (CsmKindUtilities.isInstantiation(inst.getTemplateDeclaration())) {
            inst = (CsmInstantiation) inst.getTemplateDeclaration();
            mapping2.push(inst.getMapping());
        }
        return mapping2;
    }

    @Override
    public String toString() {
        return "VariableProvider{" + "level=" + level + ", decl=" + decl + ", mapping=" + mapping + ", variableFile=" + variableFile + ", variableStartOffset=" + variableStartOffset + ", variableEndOffset=" + variableEndOffset + '}'; // NOI18N
    }        
}

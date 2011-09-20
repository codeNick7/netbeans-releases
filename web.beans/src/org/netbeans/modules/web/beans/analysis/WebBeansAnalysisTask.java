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
package org.netbeans.modules.web.beans.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.j2ee.metadata.model.api.MetadataModel;
import org.netbeans.modules.j2ee.metadata.model.api.MetadataModelAction;
import org.netbeans.modules.j2ee.metadata.model.api.MetadataModelException;
import org.netbeans.modules.web.beans.MetaModelSupport;
import org.netbeans.modules.web.beans.analysis.analyzer.AnnotationModelAnalyzer;
import org.netbeans.modules.web.beans.analysis.analyzer.ClassModelAnalyzer;
import org.netbeans.modules.web.beans.analysis.analyzer.FieldModelAnalyzer;
import org.netbeans.modules.web.beans.analysis.analyzer.MethodModelAnalyzer;
import org.netbeans.modules.web.beans.analysis.analyzer.ModelAnalyzer;
import org.netbeans.modules.web.beans.analysis.analyzer.ModelAnalyzer.Result;
import org.netbeans.modules.web.beans.api.model.WebBeansModel;
import org.netbeans.modules.web.beans.hints.EditorAnnotationsHelper;
import org.netbeans.spi.editor.hints.ErrorDescription;


/**
 * @author ads
 *
 */
public class WebBeansAnalysisTask extends AbstractAnalysisTask {
    
    private final static Logger LOG = Logger.getLogger( 
            WebBeansAnalysisTask.class.getName());
    
    protected Result createResult( CompilationInfo compInfo  ){
        return new Result( compInfo );
    }
    
    /* (non-Javadoc)
     * @see org.netbeans.modules.web.beans.analysis.AbstractAnalysisTask#getResult()
     */
    @Override
    protected Result getResult() {
        return (Result)super.getResult();
    }
    
    protected MetadataModel<WebBeansModel> getModel(CompilationInfo compInfo){
        Project project = FileOwnerQuery.getOwner( compInfo.getFileObject() );
        if ( project == null ){
            return null;
        }
        MetaModelSupport support = new MetaModelSupport(project);
        return support.getMetaModel();
    }
    
    @Override
    List<ErrorDescription> getProblems(){
        return getResult().getProblems();
    }

    /* (non-Javadoc)
     * @see org.netbeans.modules.web.beans.analysis.AbstractAnalysisTask#run(org.netbeans.api.java.source.CompilationInfo)
     */
    @Override
    protected void run( final CompilationInfo compInfo ) {
        setResult( createResult( compInfo ) );
        List<? extends TypeElement> types = compInfo.getTopLevelElements();
        final List<ElementHandle<TypeElement>> handles = 
            new ArrayList<ElementHandle<TypeElement>>(1);
        for (TypeElement typeElement : types) {
            if ( isCancelled() ){
                break;
            }
            handles.add(ElementHandle.create(typeElement));
        }
        MetadataModel<WebBeansModel> metaModel = getModel(compInfo);
        if ( metaModel == null ){
            return;
        }
        try {
            metaModel.runReadAction( 
                    new MetadataModelAction<WebBeansModel, Void>() 
            {
                @Override
                public Void run( WebBeansModel model ) throws Exception {
                    CompilationController controller = model.getCompilationController();
                    for (ElementHandle<TypeElement> handle : handles) {
                        TypeElement type = handle.resolve( controller );
                        if ( type == null ){
                            continue;
                        }
                        analyzeType( type , null , model , compInfo );
                    }
                    return null;
                }
            });
        }
        catch (MetadataModelException e) {
            LOG.log( Level.INFO , null , e);
        }
        catch (IOException e) {
            LOG.log( Level.INFO , null , e);
        }
        finally {
            EditorAnnotationsHelper helper = EditorAnnotationsHelper.getInstance(getResult());
            if ( helper == null ){
                return;
            }
            helper.publish( getResult() );
        }
    }
    
    private void analyzeType(TypeElement typeElement , TypeElement parent ,
            WebBeansModel model , CompilationInfo info )
    {
        ElementKind kind = typeElement.getKind();
        ModelAnalyzer analyzer = ANALIZERS.get( kind );
        if ( analyzer != null ){
            analyzer.analyze(typeElement, parent, model, getCancel(), getResult());
        }
        if ( isCancelled() ){
            return;
        }
        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
        List<TypeElement> types = ElementFilter.typesIn(enclosedElements);
        for (TypeElement innerType : types) {
            if ( innerType == null ){
                continue;
            }
            analyzeType(innerType, typeElement , model , info );
        }
        Set<Element> enclosedSet = new HashSet<Element>( enclosedElements );
        enclosedSet.removeAll( types );
        for(Element element : enclosedSet ){
            if ( element == null ){
                continue;
            }
            analyze(typeElement, model, element, info );
        }
    }

    private void analyze( TypeElement typeElement, WebBeansModel model,
             Element element, CompilationInfo info  )
    {
        ModelAnalyzer analyzer;
        if ( isCancelled() ){
            return;
        }
        analyzer = ANALIZERS.get( element.getKind() );
        if ( analyzer == null ){
            return;
        }
        analyzer.analyze(element, typeElement, model, getCancel(), getResult());
    }
    
    private static final Map<ElementKind,ModelAnalyzer> ANALIZERS = 
        new HashMap<ElementKind, ModelAnalyzer>();

    static {
        ANALIZERS.put(ElementKind.CLASS, new ClassModelAnalyzer());
        ANALIZERS.put(ElementKind.FIELD, new FieldModelAnalyzer());
        ANALIZERS.put(ElementKind.METHOD, new MethodModelAnalyzer());
        ANALIZERS.put(ElementKind.ANNOTATION_TYPE, new AnnotationModelAnalyzer());
    }

}

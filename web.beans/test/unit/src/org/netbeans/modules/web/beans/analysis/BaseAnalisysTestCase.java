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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import org.netbeans.modules.j2ee.metadata.model.support.JavaSourceTestCase;
import org.netbeans.modules.parsing.api.indexing.IndexingManager;
import org.netbeans.modules.web.beans.testutilities.CdiTestUtilities;
import org.openide.filesystems.FileObject;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;


/**
 * @author ads
 *
 */
public abstract class BaseAnalisysTestCase extends JavaSourceTestCase {
    
    protected static final ResultProcessor NO_ERRORS_PROCESSOR = new ResultProcessor (){

        @Override
        public void process( TestProblems result ) {
            Set<Element> elements = result.getErrors().keySet();
            String msg = "";
            if ( !elements.isEmpty()) {
                msg = result.getErrors().values().iterator().next();
            }
            assertTrue(  "Expected no errors, but found :" +msg , elements.isEmpty() );
            elements = result.getWarings().keySet();
            if ( !elements.isEmpty()) {
                msg = result.getWarings().values().iterator().next();
            }
            assertTrue(  "Expected no warnings, but found :" +msg , elements.isEmpty() );
        }
        
    };
    
    protected static final ResultProcessor WARNINGS_PROCESSOR = new ResultProcessor (){

        @Override
        public void process( TestProblems result ) {
            Set<Element> elements = result.getErrors().keySet();
            String msg = "";
            if ( !elements.isEmpty()) {
                msg = result.getErrors().values().iterator().next();
            }
            assertTrue(  "Expected no errors, but found :" +msg , elements.isEmpty() );
        }
        
    };

    public BaseAnalisysTestCase( String testName ) {
        super(testName);
    }
    
    /* (non-Javadoc)
     * @see org.netbeans.modules.j2ee.metadata.model.support.JavaSourceTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        GlobalPathRegistry.getDefault().register(ClassPath.SOURCE, 
                new ClassPath[] { ClassPath.getClassPath(srcFO, ClassPath.SOURCE) });
        GlobalPathRegistry.getDefault().register(ClassPath.BOOT, 
                new ClassPath[] { bootCP });
        myClassPathInfo = ClasspathInfo.create(srcFO);
        myUtilities = new CdiTestUtilities( srcFO );
        myUtilities.initAnnotations();
    }

    /* (non-Javadoc)
     * @see org.netbeans.modules.j2ee.metadata.model.support.JavaSourceTestCase#tearDown()
     */
    @Override
    protected void tearDown() {
    }
    
    protected void runAnalysis( FileObject fileObject , 
            final ResultProcessor processor ) throws IOException
    {
        IndexingManager.getDefault().refreshIndexAndWait(srcFO.getURL(), null);
        JavaSource js = JavaSource.create(myClassPathInfo, fileObject  );
        final AbstractAnalysisTask task  = createTask();
        js.runWhenScanFinished( new Task<CompilationController>() {
            
            @Override
            public void run( CompilationController controller ) throws Exception {
                controller.toPhase( Phase.ELEMENTS_RESOLVED );
                
                task.run( controller );
                processor.process((TestProblems)task.getResult());
            }
        }, true);
    }
    
    protected CdiTestUtilities getUtilities(){
        return myUtilities;
    }
    
    protected void checkFieldElement(TestProblems result , String enclosingClass, 
            String expectedName )
    {
        checkFieldElement(result, enclosingClass, expectedName, false );
    }
    
    protected void checkFieldElement(TestProblems result , String enclosingClass, 
            String expectedName , boolean checkOnlyFields )
    {
        checkElement(result, enclosingClass, expectedName, VariableElement.class, 
                checkOnlyFields);
    }
    
    protected void checkMethodElement(TestProblems result , String enclosingClass, 
            String expectedName , boolean checkOnlyFields)
    {
        checkElement(result, enclosingClass, expectedName, ExecutableElement.class, 
                checkOnlyFields);
    }
    
    protected void checkMethodElement(Map<Element,String> map , String enclosingClass, 
            String expectedName )
    {
        ElementMatcher matcher = new SimpleNameMatcher( expectedName );
        checkElement(map, enclosingClass, matcher, ExecutableElement.class, 
                false);
    }
    
    protected void checkMethodElement(TestProblems result , String enclosingClass, 
            String expectedName )
    {
        checkMethodElement(result, enclosingClass, expectedName, false );
    }
    
    protected void checkCtor(TestProblems result , String enclosingClass)
    {
        ElementMatcher matcher = new CtorMatcher();
        checkElement(result, enclosingClass, matcher, ExecutableElement.class, false);
    }
    
    protected <T extends Element> void checkElement(TestProblems result , String enclosingClass, 
            String expectedName , Class<T> elementClass, boolean checkOnlyFields )
    {
        ElementMatcher matcher = new SimpleNameMatcher( expectedName );
        checkElement(result, enclosingClass, matcher, elementClass, checkOnlyFields);
    }
    
    protected <T extends Element> void checkElement(TestProblems result , String enclosingClass, 
            ElementMatcher matcher, Class<T> elementClass, boolean checkOnlyFields )
    {
        checkElement(result.getErrors(), enclosingClass, matcher, elementClass, 
                checkOnlyFields);
    }
    
    protected <T extends Element> void checkElement(Map<Element,String> map , String enclosingClass, 
            ElementMatcher matcher, Class<T> elementClass, boolean checkOnlyFields )
    {
        Set<Element> elements = map.keySet();
        Set<Element> classElements = new HashSet<Element>();
        TypeElement enclosingClazz = null;
        for( Element element : elements ){
            Element enclosingElement = element.getEnclosingElement();
            TypeElement clazz = null;
            boolean forAdd = false ;
            if ( enclosingElement instanceof TypeElement ){
                forAdd = true;
                clazz = (TypeElement) enclosingElement;
            }
            else if ( element instanceof TypeElement ){
                if ( !checkOnlyFields ){
                    forAdd = true;
                }
                clazz = (TypeElement)element;
            }
            else {
                assertTrue("Found element which parent is not a type definition " +
                        "and is not a definition itself ", false);
            }
            if (  forAdd && clazz.getQualifiedName().contentEquals( enclosingClass )){
                enclosingClazz = clazz;
                //System.out.println( "Found element : "+element);
                classElements.add( element );
            }
        }
        assertNotNull("Expected enclosing class doesn't contain errors", enclosingClazz );
        assertEquals(  "Expected exactly one error element", 1 , classElements.size());
        Element element = classElements.iterator().next();
        assertTrue( "Element has a class "+element.getClass(), 
                elementClass.isAssignableFrom( element.getClass() ) );
        boolean match = matcher.matches( element );
        if ( !match ){
            assertTrue(  matcher.getMessage(), match);
        }
    }
    
    protected void checkParamElement(TestProblems result , String enclosingClass, 
            String methodName , String paramName )
    {
        Set<Element> elements = result.getErrors().keySet();
        Set<Element> classElements = new HashSet<Element>();
        TypeElement enclosingClazz = null;
        ExecutableElement method = null;
        for( Element element : elements ){
            Element enclosingElement = element.getEnclosingElement();
            TypeElement clazz = null;
            ExecutableElement methodElement = null;
            boolean forAdd = false ;
            if ( enclosingElement instanceof TypeElement ){
                forAdd = true;
                clazz = (TypeElement) enclosingElement;
            }
            else if ( element instanceof TypeElement ){
                clazz = (TypeElement)element;
            }
            else if ( enclosingElement instanceof ExecutableElement ) {
                forAdd = true;
                methodElement = (ExecutableElement)enclosingElement;
            }
            else {
                assertTrue("Found element which parent is not a type definition, " +
                        "not a definition itself and not method", false);
            }
            if (  forAdd && methodElement != null && 
                    methodElement.getSimpleName().contentEquals( methodName ))
            {
                method = methodElement;
                enclosingClazz = (TypeElement)method.getEnclosingElement();
                classElements.add( element );
            }
        }
        assertNotNull("Expected enclosing class doesn't contain errors", enclosingClazz );
        assertNotNull("Expected enclosing method doesn't contain errors", method );
        assertEquals(  "Expected exactly one error element", 1 , classElements.size());
        Element element = classElements.iterator().next();
        assertEquals(paramName, element.getSimpleName().toString());
    }
    
    protected void checkTypeElement( TestProblems result , String expectedName ){
        checkTypeElement(result.getErrors(), expectedName);
        assertEquals( "Found unexpected warnings" , 0, result.getWarings().size() );
    }
    
    protected void checkTypeElement( Map<Element,String> map, String expectedName ){
        Set<Element> elements = map.keySet();
        if ( elements.size() > 1 ){
            for( Element element : elements ){
                System.out.println( "Found element : "+element.toString());
            }
        }
        assertEquals(  "Expected exactly one error element", 1 , elements.size());
        Element element = elements.iterator().next();
        assertTrue( element instanceof TypeElement );
        String fqn = ((TypeElement)element).getQualifiedName().toString();
        assertEquals(expectedName, fqn);
    }
    
    interface ElementMatcher {
        boolean matches(Element element);
        String getMessage();
    }
    
    class SimpleNameMatcher implements ElementMatcher {
        
        SimpleNameMatcher( String simpleName ){
            myName = simpleName; 
        }
        
        public boolean matches(Element element){
            myMessage = "Found "+element.getSimpleName()+" element, expected "+myName;
            return myName.contentEquals( element.getSimpleName());
        }
        
        public String getMessage() {
            return myMessage;
        }
        
        private String myName;
        private String myMessage;
    }
    
    class CtorMatcher implements ElementMatcher {
        
        public boolean matches(Element element){
            myKind = element.getKind();
            return  myKind== ElementKind.CONSTRUCTOR;
        }
        
        public String getMessage() {
            return "Found element has "+myKind+" , not CTOR";
        }
        
        private ElementKind myKind;
    }
    
    protected abstract AbstractAnalysisTask createTask();
    
    private ClasspathInfo myClassPathInfo;
    private CdiTestUtilities myUtilities;
    
    protected static interface ResultProcessor {
        void process ( TestProblems result );
    }
    
}

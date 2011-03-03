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
package org.netbeans.modules.web.beans.impl.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;


/**
 * @author ads
 *
 */
public class RestrictedTypedFilter extends Filter<TypeElement> {

    void filter( Set<TypeElement> elements ){
        Set<TypeElement> allImplementors = new HashSet<TypeElement>( elements );
        for (Iterator<TypeElement> iterator = allImplementors.iterator() ; 
            iterator.hasNext() ; ) 
        {
            TypeElement typeElement = iterator.next();
            Collection<TypeMirror> restrictedTypes = getRestrictedTypes(typeElement,
                    getImplementation());
            if ( restrictedTypes == null ){
                continue;
            }
            boolean hasBeanType = false;
            TypeElement element = getElement();
            TypeMirror type = element.asType();
            Types types= getImplementation().getHelper().getCompilationController().
                getTypes();
            for (TypeMirror restrictedType : restrictedTypes) {
                if ( types.isSameType( types.erasure( type), 
                        types.erasure( restrictedType)))
                {
                    hasBeanType = true;
                    break;
                }
            }
            if ( !hasBeanType ){
                iterator.remove();
            }
        }
    }
    
    static Collection<TypeMirror> getRestrictedTypes( Element element , 
            WebBeansModelImplementation implementation )
    {
        if ( element == null ){
            return null;
        }
        List<? extends AnnotationMirror> annotationMirrors = 
                element.getAnnotationMirrors();
        Map<String, ? extends AnnotationMirror> annotations = 
            implementation.getHelper().getAnnotationsByType( annotationMirrors );
        AnnotationMirror typedAnnotation = annotations.get( 
                WebBeansModelProviderImpl.TYPED_RESTRICTION );
        if ( typedAnnotation == null ){
            return null;
        }
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = 
                typedAnnotation.getElementValues();
        if ( elementValues == null ){
            return Collections.emptyList();
        }
        AnnotationValue restrictedTypes  = null;
        for( Entry<? extends ExecutableElement, ? extends AnnotationValue> entry: 
            elementValues.entrySet())
        {
            ExecutableElement key = entry.getKey();
            AnnotationValue value = entry.getValue();
            if ( key.getSimpleName().contentEquals("value")){  // NOI18N
                restrictedTypes = value;
                break;
            }
        }
        if ( restrictedTypes == null ){
            return Collections.emptyList();
        }
        Object value = restrictedTypes.getValue();
        Collection<TypeMirror> result = new LinkedList<TypeMirror>();
        if ( value instanceof List<?> ){
            for( Object type : (List<?>)value){
                AnnotationValue annotationValue = (AnnotationValue)type;
                type = annotationValue.getValue();
                if (type instanceof TypeMirror){
                    result.add((TypeMirror) type );
                }
            }
        }
        return result;
    }
    
    void init( TypeElement element, WebBeansModelImplementation modelImpl ) {
        myImpl = modelImpl;
        myElement = element;
    }
    
    private WebBeansModelImplementation getImplementation() {
        return myImpl;
    }
    
    private TypeElement getElement(){
        return myElement;
    }

    private TypeElement myElement;
    private WebBeansModelImplementation myImpl;
}

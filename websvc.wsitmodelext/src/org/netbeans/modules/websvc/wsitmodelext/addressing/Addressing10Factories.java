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
 * Software is Sun Microsystems, Inc. Portions Copyright 2006 Sun
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

package org.netbeans.modules.websvc.wsitmodelext.addressing;

import org.netbeans.modules.websvc.wsitmodelext.addressing.impl.Address10Impl;
import org.netbeans.modules.websvc.wsitmodelext.addressing.impl.Addressing10EndpointReferenceImpl;
import org.netbeans.modules.websvc.wsitmodelext.addressing.impl.Addressing10MetadataImpl;
import org.netbeans.modules.websvc.wsitmodelext.addressing.impl.Addressing10ReferencePropertiesImpl;
import org.netbeans.modules.xml.wsdl.model.WSDLComponent;
import org.netbeans.modules.xml.wsdl.model.spi.ElementFactory;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Set;
import org.netbeans.modules.websvc.wsitmodelext.addressing.impl.Addressing10AnonymousImpl;


public class Addressing10Factories {

    @org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.xml.wsdl.model.spi.ElementFactory.class)
    public static class EndpointReferenceFactory extends ElementFactory {
        @Override
        public Set<QName> getElementQNames() {
            return Collections.singleton(Addressing10QName.ENDPOINTREFERENCE.getQName());
        }
        @Override
        public WSDLComponent create(WSDLComponent context, Element element) {
            return new Addressing10EndpointReferenceImpl(context.getModel(), element);
        }
    }

    @org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.xml.wsdl.model.spi.ElementFactory.class)
    public static class AnonymousFactory extends ElementFactory {
        @Override
        public Set<QName> getElementQNames() {
            return Collections.singleton(Addressing10QName.ANONYMOUS.getQName());
        }
        @Override
        public WSDLComponent create(WSDLComponent context, Element element) {
            return new Addressing10AnonymousImpl(context.getModel(), element);
        }
    }
    
    @org.openide.util.lookup.ServiceProviders({@org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.xml.wsdl.model.spi.ElementFactory.class), @org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.xml.wsdl.model.spi.ElementFactory.class)})
    public static class Address10Factory extends ElementFactory {
        @Override
        public Set<QName> getElementQNames() {
            return Collections.singleton(Addressing10QName.ADDRESS.getQName());
        }
        @Override
        public WSDLComponent create(WSDLComponent context, Element element) {
            return new Address10Impl(context.getModel(), element);
        }
    }
    
    @org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.xml.wsdl.model.spi.ElementFactory.class)
    public static class Addressing10MetadataFactory extends ElementFactory {
        @Override
        public Set<QName> getElementQNames() {
            return Collections.singleton(Addressing10QName.ADDRESSINGMETADATA.getQName());
        }
        @Override
        public WSDLComponent create(WSDLComponent context, Element element) {
            return new Addressing10MetadataImpl(context.getModel(), element);
        }
    }
    
    @org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.xml.wsdl.model.spi.ElementFactory.class)
    public static class Addressing10ReferencePropertiesFactory extends ElementFactory {
        @Override
        public Set<QName> getElementQNames() {
            return Collections.singleton(Addressing10QName.REFERENCEPROPERTIES.getQName());
        }
        @Override
        public WSDLComponent create(WSDLComponent context, Element element) {
            return new Addressing10ReferencePropertiesImpl(context.getModel(), element);
        }
    }    
}

/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.modules.vmd.midp.general;

import org.netbeans.modules.vmd.api.model.common.AcceptPresenter;
import org.netbeans.modules.vmd.api.model.common.AcceptSuggestion;
import org.netbeans.modules.vmd.api.model.*;

/**
 * @author David Kaspar
 */
public class AcceptTypePresenter extends AcceptPresenter {
    
    private TypeID typeID;
    
    public AcceptTypePresenter(TypeID typeID) {
        super(Kind.COMPONENT_PRODUCER);
        assert typeID != null;
        this.typeID = typeID;
    }
    
    public final boolean isAcceptable (ComponentProducer producer, AcceptSuggestion suggestion) {
        DescriptorRegistry registry = getComponent().getDocument().getDescriptorRegistry();
        TypeID producerTypeID = producer.getComponentTypeID ();
        return registry.isInHierarchy(typeID, producerTypeID)  &&  notifyAccepting (producerTypeID);
    }

    public final ComponentProducer.Result accept (ComponentProducer producer, AcceptSuggestion suggestion) {
        ComponentProducer.Result result = producer.createComponent (getComponent ().getDocument ());
        DesignComponent component = result.getMainComponent();
        if (component != null)
            notifyCreated(component);
        return result;
    }
    
    protected boolean notifyAccepting (TypeID producerTypeID) {
        return true;
    }

    protected void notifyCreated(DesignComponent component) {
        getComponent().addComponent(component);
    }
    
}

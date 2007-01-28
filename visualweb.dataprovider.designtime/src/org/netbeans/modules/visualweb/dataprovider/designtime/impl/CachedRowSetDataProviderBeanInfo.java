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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */


package org.netbeans.modules.visualweb.dataprovider.designtime.impl;

import org.netbeans.modules.visualweb.dataprovider.designtime.BeanInfoSupport;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.rave.designtime.Constants;
import java.util.List;
import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;

public class CachedRowSetDataProviderBeanInfo extends CachedRowSetDataProviderBeanInfoBase {
    
    private EventSetDescriptor esDescriptors[] = null;

    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor beanDescriptor = super.getBeanDescriptor();
        beanDescriptor.setValue(Constants.BeanDescriptor.CLEANUP_METHOD,
                "close");
	beanDescriptor.setValue(Constants.BeanDescriptor.HELP_KEY, "projrave_ui_elements_palette_dataproviders_cachedrowset_dp");
        beanDescriptor.setValue(Constants.BeanDescriptor.PROPERTIES_HELP_KEY, "projrave_ui_elements_palette_dataproviders_propsheets_cachedrowset_dp_props");
        return beanDescriptor;
    }

    public EventSetDescriptor[] getEventSetDescriptors() {

        if (esDescriptors == null) {
            List results =
              BeanInfoSupport.getDataProviderEventSetDescriptors
                (CachedRowSetDataProvider.class);
            results.addAll(BeanInfoSupport.getRefreshableDataProviderEventSetDescriptors
              (CachedRowSetDataProvider.class));
            results.addAll(BeanInfoSupport.getTableDataProviderEventSetDescriptors
              (CachedRowSetDataProvider.class));
            results.addAll(BeanInfoSupport.getTransactionalDataProviderEventSetDescriptors
              (CachedRowSetDataProvider.class));
            esDescriptors = (EventSetDescriptor[])
              results.toArray(new EventSetDescriptor[results.size()]);
        }
        return esDescriptors;

    }

}

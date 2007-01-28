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
import com.sun.data.provider.impl.FilteredTableDataProvider;
import java.util.List;
import java.beans.EventSetDescriptor;

public class FilteredTableDataProviderBeanInfo extends FilteredTableDataProviderBeanInfoBase {

    private EventSetDescriptor esDescriptors[] = null;

    public EventSetDescriptor[] getEventSetDescriptors() {

        if (esDescriptors == null) {
            List results =
              BeanInfoSupport.getDataProviderEventSetDescriptors
                (FilteredTableDataProvider.class);
            results.addAll(BeanInfoSupport.getTableDataProviderEventSetDescriptors
              (FilteredTableDataProvider.class));
            esDescriptors = (EventSetDescriptor[])
              results.toArray(new EventSetDescriptor[results.size()]);
        }
        return esDescriptors;

    }

}

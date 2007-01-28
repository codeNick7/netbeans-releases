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
package org.netbeans.modules.visualweb.faces.dt.validator;

import com.sun.rave.propertyeditors.LongPropertyEditor;
import org.netbeans.modules.visualweb.faces.dt.HtmlNonGeneratedBeanInfoBase;
import java.beans.*;
import com.sun.rave.designtime.*;
import org.netbeans.modules.visualweb.faces.dt.util.ComponentBundle;
import javax.faces.validator.LongRangeValidator;

public class LongRangeValidatorBeanInfo extends HtmlNonGeneratedBeanInfoBase {

    private static final ComponentBundle bundle = ComponentBundle.getBundle(LongRangeValidatorBeanInfo.class);

    /**
     * Construct a <code>LongRangeValidatorBeanInfo</code> instance
     */
    public LongRangeValidatorBeanInfo() {
        beanClass = LongRangeValidator.class;
        iconFileName_C16 = "LongRangeValidator_C16";    //NOI18N
        iconFileName_C32 = "LongRangeValidator_C32";    //NOI18N
        iconFileName_M16 = "LongRangeValidator_M16";    //NOI18N
        iconFileName_M32 = "LongRangeValidator_M32";    //NOI18N
    }

    private BeanDescriptor beanDescriptor;

    /**
     * @return The BeanDescriptor
     */
    public BeanDescriptor getBeanDescriptor() {
        if (beanDescriptor == null) {
            beanDescriptor = new BeanDescriptor(beanClass);
            //beanDescriptor.setValue(Constants.BeanDescriptor.TAGLIB_URI, "http://java.sun.com/jsf/core");    //NOI18N
            //beanDescriptor.setValue(Constants.BeanDescriptor.TAG_NAME, "validateLongRange");    //NOI18N
            beanDescriptor.setValue(Constants.BeanDescriptor.INSTANCE_NAME, "longRangeValidator");    //NOI18N
            beanDescriptor.setDisplayName(bundle.getMessage("lrValid"));    //NOI18N
            beanDescriptor.setShortDescription(bundle.getMessage("lrValidShortDesc"));    //NOI18N
            beanDescriptor.setValue(Constants.BeanDescriptor.HELP_KEY, "projrave_ui_elements_palette_jsf-val-conv_long_range_valdtr");

        }
        return beanDescriptor;
    }

    private PropertyDescriptor[] propDescriptors;

    /**
     * Returns the PropertyDescriptor array which describes
     * the property meta-data for this JavaBean
     *
     * @return An array of PropertyDescriptor objects
     */
    public PropertyDescriptor[] getPropertyDescriptors() {

        if (propDescriptors == null) {
            try {

                PropertyDescriptor _minimum = new PropertyDescriptor("minimum", beanClass, "getMinimum", "setMinimum");    //NOI18N
                _minimum.setPropertyEditorClass(LongPropertyEditor.class);

                PropertyDescriptor _maximum = new PropertyDescriptor("maximum", beanClass, "getMaximum", "setMaximum");    //NOI18N
                _maximum.setPropertyEditorClass(LongPropertyEditor.class);

                propDescriptors = new PropertyDescriptor[] {
                    _minimum,
                    _maximum,
                };
            }
            catch (IntrospectionException ix) {
                ix.printStackTrace();
            }
        }

        return propDescriptors;
    }
}

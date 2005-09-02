/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2004 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.j2ee.dd.impl.common;

import org.netbeans.modules.schema2beans.BaseBean;
import org.netbeans.modules.schema2beans.BaseProperty;
import org.netbeans.modules.j2ee.dd.api.web.*;

/**
 * Customized comparator for web.xml
 *
 * @author  Milan Kuchtiak
 */
public class Comparator extends org.netbeans.modules.schema2beans.BeanComparator
{
    public BaseBean compareBean(String 		beanName,
				BaseBean 	curBean,
				BaseBean 	newBean) {
        if (curBean!=null && newBean!= null) {
            if (curBean instanceof EnclosingBean && newBean instanceof EnclosingBean) {
                if (((EnclosingBean) curBean).getOriginal() == ((EnclosingBean) newBean).getOriginal()) {
                    return curBean;
                }
            }
            if (curBean instanceof KeyBean) {
                String prop = ((KeyBean) curBean).getKeyProperty();
                Object key1 = curBean.getValue(prop);
                Object key2 = newBean.getValue(prop);
                if (key1 != null) {
                    if (key1.equals(key2)) {
                        return curBean;
                    }
                }
            } else {
                if (beanName.equals("SessionConfig")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("WelcomeFileList")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("LoginConfig")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("FormLoginConfig")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("FilterMapping")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("Listener")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("RunAs")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("AuthConstraint")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("UserDataConstraint")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("JspConfig")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("JspPropertyGroup")) { //NOI18N
                    return curBean;
                } else if (beanName.equals("LocaleEncodingMappingList")) { //NOI18N
                    return curBean;
                }
            }
        }
        return super.compareBean(beanName, curBean, newBean);
    }
    
    public Object compareProperty(String 	propertyName,
                                  BaseBean 	curBean,
                                  Object 	curValue,
                                  int		curIndex,
                                  BaseBean	newBean,
                                  Object 	newValue,
                                  int		newIndex) {
        return super.compareProperty(propertyName, curBean,curValue,curIndex,newBean,newValue, newIndex);
    }
}

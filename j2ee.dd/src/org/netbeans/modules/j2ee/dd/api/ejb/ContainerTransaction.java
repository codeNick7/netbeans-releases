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

package org.netbeans.modules.j2ee.dd.api.ejb;

// 
// This interface has all of the bean info accessor methods.
// 
import org.netbeans.modules.j2ee.dd.api.common.CommonDDBean;
import org.netbeans.modules.j2ee.dd.api.common.DescriptionInterface;

public interface ContainerTransaction extends CommonDDBean, DescriptionInterface {
    
    public static final String METHOD = "Method";	// NOI18N
    public static final String TRANS_ATTRIBUTE = "TransAttribute";	// NOI18N
        
    public void setMethod(int index, Method value);
    
    public void setMethod(Method[] value);
    
    public Method getMethod(int index);
    
    public Method[] getMethod();
    
    public int addMethod(org.netbeans.modules.j2ee.dd.api.ejb.Method value);
    
    public int sizeMethod();
    
    public int removeMethod(org.netbeans.modules.j2ee.dd.api.ejb.Method value);
    
    public Method newMethod();

    public void setTransAttribute(String value);

    public String getTransAttribute();
}


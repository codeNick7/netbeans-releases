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

package org.netbeans.modules.j2ee.dd.api.common;

// 
// This interface has all of the bean info accessor methods.
// 
import org.netbeans.modules.j2ee.dd.api.common.CommonDDBean;
import org.netbeans.modules.j2ee.dd.api.common.DescriptionInterface;

/**
 * Generated interface for EjbRef element.
 *
 *<p><b><font color="red"><em>Important note: Do not provide an implementation of this interface unless you are a DD API provider!</em></font></b>
 *</p>
 */
public interface EjbRef extends CommonDDBean, DescriptionInterface {
    
        public static final String EJB_REF_NAME = "EjbRefName";	// NOI18N
	public static final String EJB_REF_TYPE = "EjbRefType";	// NOI18N
	public static final String HOME = "Home";	// NOI18N
	public static final String REMOTE = "Remote";	// NOI18N
	public static final String EJB_LINK = "EjbLink";	// NOI18N
        public static final String EJB_REF_TYPE_ENTITY = "Entity"; // NOI18N
        public static final String EJB_REF_TYPE_SESSION = "Session"; // NOI18N
        /** Setter for ejb-ref-name property.
         * @param value property value
         */
	public void setEjbRefName(java.lang.String value);
        /** Getter for ejb-ref-name property.
         * @return property value 
         */
	public java.lang.String getEjbRefName();
        /** Setter for ejb-ref-type property.
         * @param value property value
         */
	public void setEjbRefType(java.lang.String value);
        /** Getter for ejb-ref-type property.
         * @return property value 
         */
	public java.lang.String getEjbRefType();
        /** Setter for home property.
         * @param value property value
         */
	public void setHome(java.lang.String value);
        /** Getter for home property.
         * @return property value 
         */
	public java.lang.String getHome();
        /** Setter for remote property.
         * @param value property value
         */
	public void setRemote(java.lang.String value);
        /** Getter for remote property.
         * @return property value 
         */
	public java.lang.String getRemote();
        /** Setter for ejb-link property.
         * @param value property value
         */
	public void setEjbLink(java.lang.String value);
        /** Getter for ejb-link property.
         * @return property value 
         */
	public java.lang.String getEjbLink();
}


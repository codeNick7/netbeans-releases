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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.12.09 at 06:25:55 PM PST 
//


package org.netbeans.modules.compapp.javaee.sunresources.generated.sunejb30;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "maxCacheSize",
    "resizeQuantity",
    "isCacheOverflowAllowed",
    "cacheIdleTimeoutInSeconds",
    "removalTimeoutInSeconds",
    "victimSelectionPolicy"
})
@XmlRootElement(name = "bean-cache")
public class BeanCache {

    @XmlElement(name = "max-cache-size")
    protected String maxCacheSize;
    @XmlElement(name = "resize-quantity")
    protected String resizeQuantity;
    @XmlElement(name = "is-cache-overflow-allowed")
    protected String isCacheOverflowAllowed;
    @XmlElement(name = "cache-idle-timeout-in-seconds")
    protected String cacheIdleTimeoutInSeconds;
    @XmlElement(name = "removal-timeout-in-seconds")
    protected String removalTimeoutInSeconds;
    @XmlElement(name = "victim-selection-policy")
    protected String victimSelectionPolicy;

    /**
     * Gets the value of the maxCacheSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
     * Sets the value of the maxCacheSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxCacheSize(String value) {
        this.maxCacheSize = value;
    }

    /**
     * Gets the value of the resizeQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResizeQuantity() {
        return resizeQuantity;
    }

    /**
     * Sets the value of the resizeQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResizeQuantity(String value) {
        this.resizeQuantity = value;
    }

    /**
     * Gets the value of the isCacheOverflowAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsCacheOverflowAllowed() {
        return isCacheOverflowAllowed;
    }

    /**
     * Sets the value of the isCacheOverflowAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsCacheOverflowAllowed(String value) {
        this.isCacheOverflowAllowed = value;
    }

    /**
     * Gets the value of the cacheIdleTimeoutInSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCacheIdleTimeoutInSeconds() {
        return cacheIdleTimeoutInSeconds;
    }

    /**
     * Sets the value of the cacheIdleTimeoutInSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCacheIdleTimeoutInSeconds(String value) {
        this.cacheIdleTimeoutInSeconds = value;
    }

    /**
     * Gets the value of the removalTimeoutInSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemovalTimeoutInSeconds() {
        return removalTimeoutInSeconds;
    }

    /**
     * Sets the value of the removalTimeoutInSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemovalTimeoutInSeconds(String value) {
        this.removalTimeoutInSeconds = value;
    }

    /**
     * Gets the value of the victimSelectionPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVictimSelectionPolicy() {
        return victimSelectionPolicy;
    }

    /**
     * Sets the value of the victimSelectionPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVictimSelectionPolicy(String value) {
        this.victimSelectionPolicy = value;
    }

}

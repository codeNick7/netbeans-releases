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
// Generated on: 2006.12.09 at 06:26:06 PM PST 
//


package org.netbeans.modules.compapp.javaee.sunresources.generated.jaxrpcmapping11;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 
 * 	The exception-mapping element defines the mapping between the
 * 	service specific exception types and wsdl faults and
 * 	SOAP headerfaults.
 * 
 * 	This element should be interpreted with respect to the
 * 	mapping between a method and an operation which provides the
 * 	mapping context.
 * 
 * 	Used in: service-endpoint-method-mapping
 * 
 *       
 * 
 * <p>Java class for exception-mappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exception-mappingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="exception-type" type="{http://java.sun.com/xml/ns/j2ee}fully-qualified-classType"/>
 *         &lt;element name="wsdl-message" type="{http://java.sun.com/xml/ns/j2ee}wsdl-messageType"/>
 *         &lt;element name="wsdl-message-part-name" type="{http://java.sun.com/xml/ns/j2ee}wsdl-message-part-nameType" minOccurs="0"/>
 *         &lt;element name="constructor-parameter-order" type="{http://java.sun.com/xml/ns/j2ee}constructor-parameter-orderType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exception-mappingType", propOrder = {
    "exceptionType",
    "wsdlMessage",
    "wsdlMessagePartName",
    "constructorParameterOrder"
})
public class ExceptionMappingType {

    @XmlElement(name = "exception-type", required = true)
    protected FullyQualifiedClassType exceptionType;
    @XmlElement(name = "wsdl-message", required = true)
    protected WsdlMessageType wsdlMessage;
    @XmlElement(name = "wsdl-message-part-name")
    protected WsdlMessagePartNameType wsdlMessagePartName;
    @XmlElement(name = "constructor-parameter-order")
    protected ConstructorParameterOrderType constructorParameterOrder;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected java.lang.String id;

    /**
     * Gets the value of the exceptionType property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getExceptionType() {
        return exceptionType;
    }

    /**
     * Sets the value of the exceptionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setExceptionType(FullyQualifiedClassType value) {
        this.exceptionType = value;
    }

    /**
     * Gets the value of the wsdlMessage property.
     * 
     * @return
     *     possible object is
     *     {@link WsdlMessageType }
     *     
     */
    public WsdlMessageType getWsdlMessage() {
        return wsdlMessage;
    }

    /**
     * Sets the value of the wsdlMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link WsdlMessageType }
     *     
     */
    public void setWsdlMessage(WsdlMessageType value) {
        this.wsdlMessage = value;
    }

    /**
     * Gets the value of the wsdlMessagePartName property.
     * 
     * @return
     *     possible object is
     *     {@link WsdlMessagePartNameType }
     *     
     */
    public WsdlMessagePartNameType getWsdlMessagePartName() {
        return wsdlMessagePartName;
    }

    /**
     * Sets the value of the wsdlMessagePartName property.
     * 
     * @param value
     *     allowed object is
     *     {@link WsdlMessagePartNameType }
     *     
     */
    public void setWsdlMessagePartName(WsdlMessagePartNameType value) {
        this.wsdlMessagePartName = value;
    }

    /**
     * Gets the value of the constructorParameterOrder property.
     * 
     * @return
     *     possible object is
     *     {@link ConstructorParameterOrderType }
     *     
     */
    public ConstructorParameterOrderType getConstructorParameterOrder() {
        return constructorParameterOrder;
    }

    /**
     * Sets the value of the constructorParameterOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConstructorParameterOrderType }
     *     
     */
    public void setConstructorParameterOrder(ConstructorParameterOrderType value) {
        this.constructorParameterOrder = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}

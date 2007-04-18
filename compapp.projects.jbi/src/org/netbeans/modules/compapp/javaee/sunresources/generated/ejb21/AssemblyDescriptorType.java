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
// Generated on: 2006.12.09 at 06:25:52 PM PST 
//


package org.netbeans.modules.compapp.javaee.sunresources.generated.ejb21;

import java.util.ArrayList;
import java.util.List;
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
 * 	The assembly-descriptorType defines
 * 	application-assembly information.
 * 
 * 	The application-assembly information consists of the
 * 	following parts: the definition of security roles, the
 * 	definition of method permissions, the definition of
 * 	transaction attributes for enterprise beans with
 * 	container-managed transaction demarcation and a list of
 * 	methods to be excluded from being invoked.
 * 
 * 	All the parts are optional in the sense that they are
 * 	omitted if the lists represented by them are empty.
 * 
 * 	Providing an assembly-descriptor in the deployment
 * 	descriptor is optional for the ejb-jar file producer.
 * 
 *       
 * 
 * <p>Java class for assembly-descriptorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="assembly-descriptorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="security-role" type="{http://java.sun.com/xml/ns/j2ee}security-roleType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="method-permission" type="{http://java.sun.com/xml/ns/j2ee}method-permissionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="container-transaction" type="{http://java.sun.com/xml/ns/j2ee}container-transactionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="message-destination" type="{http://java.sun.com/xml/ns/j2ee}message-destinationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="exclude-list" type="{http://java.sun.com/xml/ns/j2ee}exclude-listType" minOccurs="0"/>
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
@XmlType(name = "assembly-descriptorType", propOrder = {
    "securityRole",
    "methodPermission",
    "containerTransaction",
    "messageDestination",
    "excludeList"
})
public class AssemblyDescriptorType {

    @XmlElement(name = "security-role")
    protected List<SecurityRoleType> securityRole;
    @XmlElement(name = "method-permission")
    protected List<MethodPermissionType> methodPermission;
    @XmlElement(name = "container-transaction")
    protected List<ContainerTransactionType> containerTransaction;
    @XmlElement(name = "message-destination")
    protected List<MessageDestinationType> messageDestination;
    @XmlElement(name = "exclude-list")
    protected ExcludeListType excludeList;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected java.lang.String id;

    /**
     * Gets the value of the securityRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityRoleType }
     * 
     * 
     */
    public List<SecurityRoleType> getSecurityRole() {
        if (securityRole == null) {
            securityRole = new ArrayList<SecurityRoleType>();
        }
        return this.securityRole;
    }

    /**
     * Gets the value of the methodPermission property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the methodPermission property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMethodPermission().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MethodPermissionType }
     * 
     * 
     */
    public List<MethodPermissionType> getMethodPermission() {
        if (methodPermission == null) {
            methodPermission = new ArrayList<MethodPermissionType>();
        }
        return this.methodPermission;
    }

    /**
     * Gets the value of the containerTransaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the containerTransaction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContainerTransaction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContainerTransactionType }
     * 
     * 
     */
    public List<ContainerTransactionType> getContainerTransaction() {
        if (containerTransaction == null) {
            containerTransaction = new ArrayList<ContainerTransactionType>();
        }
        return this.containerTransaction;
    }

    /**
     * Gets the value of the messageDestination property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageDestination property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageDestination().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageDestinationType }
     * 
     * 
     */
    public List<MessageDestinationType> getMessageDestination() {
        if (messageDestination == null) {
            messageDestination = new ArrayList<MessageDestinationType>();
        }
        return this.messageDestination;
    }

    /**
     * Gets the value of the excludeList property.
     * 
     * @return
     *     possible object is
     *     {@link ExcludeListType }
     *     
     */
    public ExcludeListType getExcludeList() {
        return excludeList;
    }

    /**
     * Sets the value of the excludeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExcludeListType }
     *     
     */
    public void setExcludeList(ExcludeListType value) {
        this.excludeList = value;
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

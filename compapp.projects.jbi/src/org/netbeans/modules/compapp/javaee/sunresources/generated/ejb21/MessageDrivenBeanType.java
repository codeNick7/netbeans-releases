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
 * 	The message-driven element declares a message-driven
 * 	bean. The declaration consists of:
 * 
 * 	    - an optional description
 * 	    - an optional display name
 * 	    - an optional icon element that contains a small and a large
 * 	      icon file name.
 * 	    - a name assigned to the enterprise bean in
 * 	      the deployment descriptor
 * 	    - the message-driven bean's implementation class
 * 	    - an optional declaration of the bean's messaging
 * 	      type
 * 	    - the message-driven bean's transaction management type
 * 	    - an optional declaration of the bean's
 * 	      message-destination-type
 * 	    - an optional declaration of the bean's
 * 	      message-destination-link
 * 	    - an optional declaration of the message-driven bean's
 * 	      activation configuration properties
 * 	    - an optional declaration of the bean's environment
 * 	      entries
 * 	    - an optional declaration of the bean's EJB references
 * 	    - an optional declaration of the bean's local EJB
 * 	      references
 * 	    - an optional declaration of the bean's web service
 * 	      references
 * 	    - an optional declaration of the security
 * 	      identity to be used for the execution of the bean's
 * 	      methods
 * 	    - an optional declaration of the bean's
 * 	      resource manager connection factory
 * 	      references
 * 	    - an optional declaration of the bean's resource
 * 	      environment references.
 * 	    - an optional declaration of the bean's message
 * 	      destination references
 * 
 *       
 * 
 * <p>Java class for message-driven-beanType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="message-driven-beanType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://java.sun.com/xml/ns/j2ee}descriptionGroup"/>
 *         &lt;element name="ejb-name" type="{http://java.sun.com/xml/ns/j2ee}ejb-nameType"/>
 *         &lt;element name="ejb-class" type="{http://java.sun.com/xml/ns/j2ee}ejb-classType"/>
 *         &lt;element name="messaging-type" type="{http://java.sun.com/xml/ns/j2ee}fully-qualified-classType" minOccurs="0"/>
 *         &lt;element name="transaction-type" type="{http://java.sun.com/xml/ns/j2ee}transaction-typeType"/>
 *         &lt;element name="message-destination-type" type="{http://java.sun.com/xml/ns/j2ee}message-destination-typeType" minOccurs="0"/>
 *         &lt;element name="message-destination-link" type="{http://java.sun.com/xml/ns/j2ee}message-destination-linkType" minOccurs="0"/>
 *         &lt;element name="activation-config" type="{http://java.sun.com/xml/ns/j2ee}activation-configType" minOccurs="0"/>
 *         &lt;group ref="{http://java.sun.com/xml/ns/j2ee}jndiEnvironmentRefsGroup"/>
 *         &lt;element name="security-identity" type="{http://java.sun.com/xml/ns/j2ee}security-identityType" minOccurs="0"/>
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
@XmlType(name = "message-driven-beanType", propOrder = {
    "description",
    "displayName",
    "icon",
    "ejbName",
    "ejbClass",
    "messagingType",
    "transactionType",
    "messageDestinationType",
    "messageDestinationLink",
    "activationConfig",
    "envEntry",
    "ejbRef",
    "ejbLocalRef",
    "serviceRef",
    "resourceRef",
    "resourceEnvRef",
    "messageDestinationRef",
    "securityIdentity"
})
public class MessageDrivenBeanType {

    protected List<DescriptionType> description;
    @XmlElement(name = "display-name")
    protected List<DisplayNameType> displayName;
    protected List<IconType> icon;
    @XmlElement(name = "ejb-name", required = true)
    protected EjbNameType ejbName;
    @XmlElement(name = "ejb-class", required = true)
    protected EjbClassType ejbClass;
    @XmlElement(name = "messaging-type")
    protected FullyQualifiedClassType messagingType;
    @XmlElement(name = "transaction-type", required = true)
    protected TransactionTypeType transactionType;
    @XmlElement(name = "message-destination-type")
    protected MessageDestinationTypeType messageDestinationType;
    @XmlElement(name = "message-destination-link")
    protected MessageDestinationLinkType messageDestinationLink;
    @XmlElement(name = "activation-config")
    protected ActivationConfigType activationConfig;
    @XmlElement(name = "env-entry")
    protected List<EnvEntryType> envEntry;
    @XmlElement(name = "ejb-ref")
    protected List<EjbRefType> ejbRef;
    @XmlElement(name = "ejb-local-ref")
    protected List<EjbLocalRefType> ejbLocalRef;
    @XmlElement(name = "service-ref")
    protected List<ServiceRefType> serviceRef;
    @XmlElement(name = "resource-ref")
    protected List<ResourceRefType> resourceRef;
    @XmlElement(name = "resource-env-ref")
    protected List<ResourceEnvRefType> resourceEnvRef;
    @XmlElement(name = "message-destination-ref")
    protected List<MessageDestinationRefType> messageDestinationRef;
    @XmlElement(name = "security-identity")
    protected SecurityIdentityType securityIdentity;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected java.lang.String id;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DescriptionType }
     * 
     * 
     */
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the displayName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisplayName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DisplayNameType }
     * 
     * 
     */
    public List<DisplayNameType> getDisplayName() {
        if (displayName == null) {
            displayName = new ArrayList<DisplayNameType>();
        }
        return this.displayName;
    }

    /**
     * Gets the value of the icon property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the icon property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIcon().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IconType }
     * 
     * 
     */
    public List<IconType> getIcon() {
        if (icon == null) {
            icon = new ArrayList<IconType>();
        }
        return this.icon;
    }

    /**
     * Gets the value of the ejbName property.
     * 
     * @return
     *     possible object is
     *     {@link EjbNameType }
     *     
     */
    public EjbNameType getEjbName() {
        return ejbName;
    }

    /**
     * Sets the value of the ejbName property.
     * 
     * @param value
     *     allowed object is
     *     {@link EjbNameType }
     *     
     */
    public void setEjbName(EjbNameType value) {
        this.ejbName = value;
    }

    /**
     * Gets the value of the ejbClass property.
     * 
     * @return
     *     possible object is
     *     {@link EjbClassType }
     *     
     */
    public EjbClassType getEjbClass() {
        return ejbClass;
    }

    /**
     * Sets the value of the ejbClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link EjbClassType }
     *     
     */
    public void setEjbClass(EjbClassType value) {
        this.ejbClass = value;
    }

    /**
     * Gets the value of the messagingType property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getMessagingType() {
        return messagingType;
    }

    /**
     * Sets the value of the messagingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setMessagingType(FullyQualifiedClassType value) {
        this.messagingType = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionTypeType }
     *     
     */
    public TransactionTypeType getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionTypeType }
     *     
     */
    public void setTransactionType(TransactionTypeType value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the messageDestinationType property.
     * 
     * @return
     *     possible object is
     *     {@link MessageDestinationTypeType }
     *     
     */
    public MessageDestinationTypeType getMessageDestinationType() {
        return messageDestinationType;
    }

    /**
     * Sets the value of the messageDestinationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDestinationTypeType }
     *     
     */
    public void setMessageDestinationType(MessageDestinationTypeType value) {
        this.messageDestinationType = value;
    }

    /**
     * Gets the value of the messageDestinationLink property.
     * 
     * @return
     *     possible object is
     *     {@link MessageDestinationLinkType }
     *     
     */
    public MessageDestinationLinkType getMessageDestinationLink() {
        return messageDestinationLink;
    }

    /**
     * Sets the value of the messageDestinationLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDestinationLinkType }
     *     
     */
    public void setMessageDestinationLink(MessageDestinationLinkType value) {
        this.messageDestinationLink = value;
    }

    /**
     * Gets the value of the activationConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ActivationConfigType }
     *     
     */
    public ActivationConfigType getActivationConfig() {
        return activationConfig;
    }

    /**
     * Sets the value of the activationConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivationConfigType }
     *     
     */
    public void setActivationConfig(ActivationConfigType value) {
        this.activationConfig = value;
    }

    /**
     * Gets the value of the envEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the envEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvEntryType }
     * 
     * 
     */
    public List<EnvEntryType> getEnvEntry() {
        if (envEntry == null) {
            envEntry = new ArrayList<EnvEntryType>();
        }
        return this.envEntry;
    }

    /**
     * Gets the value of the ejbRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ejbRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEjbRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EjbRefType }
     * 
     * 
     */
    public List<EjbRefType> getEjbRef() {
        if (ejbRef == null) {
            ejbRef = new ArrayList<EjbRefType>();
        }
        return this.ejbRef;
    }

    /**
     * Gets the value of the ejbLocalRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ejbLocalRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEjbLocalRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EjbLocalRefType }
     * 
     * 
     */
    public List<EjbLocalRefType> getEjbLocalRef() {
        if (ejbLocalRef == null) {
            ejbLocalRef = new ArrayList<EjbLocalRefType>();
        }
        return this.ejbLocalRef;
    }

    /**
     * Gets the value of the serviceRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceRefType }
     * 
     * 
     */
    public List<ServiceRefType> getServiceRef() {
        if (serviceRef == null) {
            serviceRef = new ArrayList<ServiceRefType>();
        }
        return this.serviceRef;
    }

    /**
     * Gets the value of the resourceRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceRefType }
     * 
     * 
     */
    public List<ResourceRefType> getResourceRef() {
        if (resourceRef == null) {
            resourceRef = new ArrayList<ResourceRefType>();
        }
        return this.resourceRef;
    }

    /**
     * Gets the value of the resourceEnvRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceEnvRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceEnvRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceEnvRefType }
     * 
     * 
     */
    public List<ResourceEnvRefType> getResourceEnvRef() {
        if (resourceEnvRef == null) {
            resourceEnvRef = new ArrayList<ResourceEnvRefType>();
        }
        return this.resourceEnvRef;
    }

    /**
     * Gets the value of the messageDestinationRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageDestinationRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageDestinationRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageDestinationRefType }
     * 
     * 
     */
    public List<MessageDestinationRefType> getMessageDestinationRef() {
        if (messageDestinationRef == null) {
            messageDestinationRef = new ArrayList<MessageDestinationRefType>();
        }
        return this.messageDestinationRef;
    }

    /**
     * Gets the value of the securityIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityIdentityType }
     *     
     */
    public SecurityIdentityType getSecurityIdentity() {
        return securityIdentity;
    }

    /**
     * Sets the value of the securityIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityIdentityType }
     *     
     */
    public void setSecurityIdentity(SecurityIdentityType value) {
        this.securityIdentity = value;
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

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
/*
 * WebservicesProxy.java
 *
 * Created on August 27, 2004, 2:47 PM
 */

package org.netbeans.modules.j2ee.dd.impl.webservices;

/**
 *
 * @author  Nitya Doraisamy
 */
import org.netbeans.modules.j2ee.dd.api.webservices.Webservices;

public class WebServicesProxy implements Webservices {
    private Webservices webSvc;
    private String version;
    private java.util.List listeners;
    public boolean writing = false;
    private OutputProvider outputProvider;
    private org.xml.sax.SAXParseException error;
    private int ddStatus;
    
    /** Creates a new instance of WebservicesProxy */
    public WebServicesProxy(Webservices webService, String version) {
        this.webSvc = webService;
        this.version = version;
        listeners = new java.util.ArrayList();
    }

    public void setOriginal(Webservices webSvc) {
        if (this.webSvc != webSvc) {
            for (int i=0;i<listeners.size();i++) {
                java.beans.PropertyChangeListener pcl = 
                    (java.beans.PropertyChangeListener)listeners.get(i);
                if (this.webSvc != null) this.webSvc.removePropertyChangeListener(pcl);
                if (webSvc != null) webSvc.addPropertyChangeListener(pcl);
                
            }
            this.webSvc = webSvc;
            if (webSvc != null) setProxyVersion(webSvc.getVersion().toString());
        }
    }
    
    public Webservices getOriginal() { 
        return webSvc;
    }
    
    public void setProxyVersion(java.lang.String value) {
        if ((version == null && value != null) || !version.equals(value)) {
            java.beans.PropertyChangeEvent evt = 
                new java.beans.PropertyChangeEvent(this, PROPERTY_VERSION, version, value);
            version = value;
            for (int i=0;i<listeners.size();i++) {
                ((java.beans.PropertyChangeListener)listeners.get(i)).propertyChange(evt);
            }
        }
    }
    
    public org.xml.sax.SAXParseException getError() {
        return error;
    }

    public void setError(org.xml.sax.SAXParseException error) {
        this.error = error;
    } 
     
    public int getStatus() {
        return ddStatus;
    }

    public void setStatus(int value) {
        if (ddStatus != value) {
            java.beans.PropertyChangeEvent evt = 
                new java.beans.PropertyChangeEvent(this, PROPERTY_STATUS, new Integer(ddStatus), new Integer(value));
            ddStatus = value;
            for (int i=0;i<listeners.size();i++) {
                ((java.beans.PropertyChangeListener)listeners.get(i)).propertyChange(evt);
            }
        }
    }
    
    public java.math.BigDecimal getVersion() {
        return new java.math.BigDecimal(version);
    }

    public int addWebserviceDescription(org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription value) {
        if(webSvc == null)
            return -1;
        else
            return webSvc.addWebserviceDescription(value);
    } 

     
    public org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription[] getWebserviceDescription() {
        if(webSvc == null)
            return new org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription[0];
        else
            return webSvc.getWebserviceDescription();
    }

    public org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription getWebserviceDescription(int index) {
        if(webSvc == null)
            return null;
        else
            return webSvc.getWebserviceDescription(index);
    }

    public org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription newWebserviceDescription() {
        if(webSvc == null)
            return null;
        else
            return webSvc.newWebserviceDescription();
    }

    public int removeWebserviceDescription(org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription value) {
        if(webSvc == null)
            return -1;
        else
            return webSvc.removeWebserviceDescription(value);
    }

    public void setWebserviceDescription(org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription[] value) {
        if(webSvc != null){
            webSvc.setWebserviceDescription(value);
        }    
    }

    public void setWebserviceDescription(int index, org.netbeans.modules.j2ee.dd.api.webservices.WebserviceDescription value) {
        if(webSvc != null){
            webSvc.setWebserviceDescription(index, value);
        }  
    }

    public int sizeWebserviceDescription() {
        if(webSvc == null)
            return 0;
        else
            return webSvc.sizeWebserviceDescription();
    }
    
    
    public Object getValue(String name) {
        if(webSvc == null)
            return null;
        else
            return webSvc.getValue(name);
    }
    
    public java.lang.String getId() {
        if(webSvc == null)
            return null;
        else
            return webSvc.getId();
    }
    
    public void setId(java.lang.String value) {
        if(webSvc != null) {
            webSvc.setId(value);
        }    
    }
   
    public java.util.Map getAllDescriptions() {
        return webSvc==null?new java.util.HashMap():webSvc.getAllDescriptions();
    }
    
    public String getDescription(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        return webSvc==null?null:webSvc.getDescription(locale);
    }
     
    public String getDefaultDescription() {
        return webSvc==null?null:webSvc.getDefaultDescription();
    }
    
    public java.util.Map getAllDisplayNames() {
        return webSvc==null?new java.util.HashMap():webSvc.getAllDisplayNames();
    }
    
    public String getDefaultDisplayName() {
        return webSvc==null?null:webSvc.getDefaultDisplayName();
    }
    
    public String getDisplayName(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        return webSvc==null?null:webSvc.getDisplayName(locale);
    }
    
    public org.netbeans.modules.j2ee.dd.api.common.Icon getDefaultIcon() {
        return webSvc==null?null:webSvc.getDefaultIcon();
    }
    
    public java.util.Map getAllIcons() {
        return webSvc==null?new java.util.HashMap():webSvc.getAllIcons();
    }
    
    public String getLargeIcon() {
        return webSvc==null?null:webSvc.getLargeIcon();
    }
    
    public String getLargeIcon(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        return webSvc==null?null:webSvc.getLargeIcon(locale);
    }
    
    public String getSmallIcon() {
        return webSvc==null?null:webSvc.getSmallIcon();
    }
    
    public String getSmallIcon(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        return webSvc==null?null:webSvc.getSmallIcon(locale);
    }
    
    public void removeAllDescriptions() {
        if (webSvc!=null) webSvc.removeAllDescriptions();
    }
    
    public void removeDescription() {
        if (webSvc!=null) webSvc.removeDescription();
    }
    
    public void removeDescriptionForLocale(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.removeDescriptionForLocale(locale);
    }
    
    public void removeAllDisplayNames() {
        if (webSvc!=null) webSvc.removeAllDisplayNames();
    }
    
    public void removeDisplayName() {
        if (webSvc!=null) webSvc.removeDisplayName();
    }
    
    public void removeDisplayNameForLocale(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.removeDisplayNameForLocale(locale);
    }
    
    public void removeAllIcons() {
        if (webSvc!=null) webSvc.removeAllIcons();
    }
    
    public void removeIcon() {
        if (webSvc!=null) webSvc.removeIcon();
    }
    
    public void removeIcon(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.removeIcon(locale);
    }
    
    public void removeLargeIcon() {
        if (webSvc!=null) webSvc.removeLargeIcon();
    }
    
    public void removeLargeIcon(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.removeLargeIcon(locale);
    }
    
    public void removeSmallIcon() {
        if (webSvc!=null) webSvc.removeSmallIcon();
    }
    
    public void removeSmallIcon(String locale) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.removeSmallIcon(locale);
    }
    
    public void setAllDescriptions(java.util.Map descriptions) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.setAllDescriptions(descriptions);
    }
    
    public void setDescription(String description) {
        if (webSvc!=null) webSvc.setDescription(description);
    }
    
    public void setDescription(String locale, String description) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.setDescription(locale, description);
    }
    
    public void setAllDisplayNames(java.util.Map displayNames) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.setAllDisplayNames(displayNames);
    }
    
    public void setDisplayName(String displayName) {
        if (webSvc!=null) webSvc.setDisplayName(displayName);
    }
    
    public void setDisplayName(String locale, String displayName) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.setDisplayName(locale, displayName);
    }
    
    public void setAllIcons(String[] locales, String[] smallIcons, String[] largeIcons) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.setAllIcons(locales, smallIcons, largeIcons);
    }
    
    public void setLargeIcon(String icon) {
        if (webSvc!=null) webSvc.setLargeIcon(icon);
    }
    
    public void setLargeIcon(String locale, String icon) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.setLargeIcon(locale, icon);
    }
    
    public void setSmallIcon(String icon) {
        if (webSvc!=null) webSvc.setSmallIcon(icon);
    }
    
    public void setSmallIcon(String locale, String icon) throws org.netbeans.modules.j2ee.dd.api.common.VersionNotSupportedException {
        if (webSvc!=null) webSvc.setSmallIcon(locale, icon);
    }
    
    public void setIcon(org.netbeans.modules.j2ee.dd.api.common.Icon icon) {
        if (webSvc!=null) webSvc.setIcon(icon);
    }
    
    public org.netbeans.modules.j2ee.dd.api.common.CommonDDBean addBean(String beanName, String[] propertyNames, Object[] propertyValues, String keyProperty) throws ClassNotFoundException, org.netbeans.modules.j2ee.dd.api.common.NameAlreadyUsedException {
        return webSvc==null?null:webSvc.addBean(beanName, propertyNames, propertyValues, keyProperty);
    }
    
    public org.netbeans.modules.j2ee.dd.api.common.CommonDDBean addBean(String beanName) throws ClassNotFoundException {
        return webSvc==null?null:webSvc.addBean(beanName);
    }
    
    public org.netbeans.modules.j2ee.dd.api.common.CommonDDBean createBean(String beanName) throws ClassNotFoundException {
        return webSvc==null?null:webSvc.createBean(beanName);
    }
    
    public org.netbeans.modules.j2ee.dd.api.common.CommonDDBean findBeanByName(String beanName, String propertyName, String value) {
        return webSvc==null?null:webSvc.findBeanByName(beanName, propertyName, value);
    }
        
    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        if (webSvc != null) {
            webSvc.addPropertyChangeListener(pcl);
        }    
        listeners.add(pcl); 
    }
    
    public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        if (webSvc != null) {
            webSvc.removePropertyChangeListener(pcl);
        }
        listeners.remove(pcl);
    }
    
    public void merge(org.netbeans.modules.j2ee.dd.api.common.RootInterface bean, int mode) {
        if (webSvc != null) {
            if (bean instanceof WebServicesProxy)
                webSvc.merge(((WebServicesProxy)bean).getOriginal(), mode);
            else webSvc.merge(bean, mode);
        }
    }
    
    public void write(java.io.OutputStream os) throws java.io.IOException {
        if (webSvc != null) {
            writing = true;
            webSvc.write(os);
        }
    }
    
    public void write(org.openide.filesystems.FileObject fo) throws java.io.IOException {
        if (webSvc != null) {
            try {
                org.openide.filesystems.FileLock lock = fo.lock();
                try {
                    java.io.OutputStream os = fo.getOutputStream(lock);
                    try {
                        writing = true;
                        write(os);
                    } finally {
                        os.close();
                    }
                } 
                finally {
                    lock.releaseLock();
                }
            } catch (org.openide.filesystems.FileAlreadyLockedException ex) {
                // trying to use OutputProvider for writing changes
                org.openide.loaders.DataObject dobj = org.openide.loaders.DataObject.find(fo);
                if (dobj != null && dobj instanceof WebServicesProxy.OutputProvider)
                    ((WebServicesProxy.OutputProvider)dobj).write(this);
                else 
                    throw ex;
            }
        }
    }    
    
    public Object clone() {
        WebServicesProxy proxy = null;
        if (webSvc == null)
            proxy = new WebServicesProxy(null, version);
        else {
            Webservices clonedWebSvc = (Webservices)webSvc.clone();
            proxy = new WebServicesProxy(clonedWebSvc, version);
            if (Webservices.VERSION_1_1.equals(version)) {
                ((org.netbeans.modules.j2ee.dd.impl.webservices.model_1_1.Webservices)clonedWebSvc)._setSchemaLocation
                    ("http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/j2ee_web_services_1_1.xsd");
            }
        }
        proxy.setError(error);
        proxy.setStatus(ddStatus);
        return proxy;
    }
    
    /** Contract between friend modules that enables 
    * a specific handling of write(FileObject) method for targeted FileObject
    */
    public static interface OutputProvider {
        public void write(Webservices webSvc) throws java.io.IOException;
        public org.openide.filesystems.FileObject getTarget();
    }
}

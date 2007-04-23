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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.websvc.wsitconf.design;

import java.awt.Component;
import java.awt.Image;
import java.util.Collection;
import java.util.LinkedList;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.websvc.api.jaxws.project.config.Service;
import org.netbeans.modules.websvc.design.configuration.WSConfiguration;
import org.netbeans.modules.websvc.wsitconf.wsdlmodelext.TransportModelHelper;
import org.netbeans.modules.websvc.wsitconf.wsdlmodelext.WSITModelSupport;
import org.netbeans.modules.xml.wsdl.model.Binding;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;

/**
 *
 * @author Martin Grebac
*/
public class MtomConfiguration  implements WSConfiguration{
  
    private Service service;
    private FileObject implementationFile;
    private Project project;

    private Collection<FileObject> createdFiles = new LinkedList();
    
    /** Creates a new instance of WSITWsConfiguration */

    public MtomConfiguration(Service service, FileObject implementationFile) {
        this.service = service;
        this.implementationFile = implementationFile;
        this.project = FileOwnerQuery.getOwner(implementationFile);
    }
    
    public Component getComponent() {
        return null;
    }

    public String getDescription() {
        return "MTOM";
    }

    public Image getIcon() {
        return Utilities.loadImage
                ("org/netbeans/modules/websvc/wsitconf/resources/designer-mtom.gif"); // NOI18N   
    }

    public String getDisplayName() {
        return "MTOM";
    }
  
    public boolean isSet() {
        Binding binding = WSITModelSupport.getBinding(service, implementationFile, project, false, createdFiles);
        if (binding != null) {
            return TransportModelHelper.isMtomEnabled(binding);
        }
        return false;
    }
    
    public void set() {
        Binding binding = WSITModelSupport.getBinding(service, implementationFile, project, true, createdFiles);
        if (binding == null) return;
        if (!(TransportModelHelper.isMtomEnabled(binding))) {
            TransportModelHelper.enableMtom(binding);
            WSITModelSupport.save(binding);
        }
    }

    public void unset() {
        Binding binding = WSITModelSupport.getBinding(service, implementationFile, project, false, createdFiles);
        if (binding == null) return;
        if (TransportModelHelper.isMtomEnabled(binding)) {
            TransportModelHelper.disableMtom(binding);
            WSITModelSupport.save(binding);
        }
    }
}

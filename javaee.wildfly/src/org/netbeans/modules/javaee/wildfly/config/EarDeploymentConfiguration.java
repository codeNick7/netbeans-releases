/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.netbeans.modules.javaee.wildfly.config;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.netbeans.modules.j2ee.deployment.common.api.ConfigurationException;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eeModule;
import org.netbeans.modules.j2ee.deployment.plugins.spi.config.DeploymentPlanConfiguration;
import org.netbeans.modules.j2ee.deployment.plugins.spi.config.ModuleConfiguration;
import org.netbeans.modules.javaee.wildfly.config.gen.JbossApp;
import org.netbeans.modules.javaee.wildfly.ide.ui.JBPluginUtils;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;


/**
 * EAR application deployment configuration handles jboss-app.xml configuration
 * file creation.
 *
 * @author sherold
 */
public class EarDeploymentConfiguration extends WildflyDeploymentConfiguration 
implements ModuleConfiguration, DeploymentPlanConfiguration {
    
    private File jbossAppFile;
    private JbossApp jbossApp;

    public EarDeploymentConfiguration(J2eeModule j2eeModule) {
        this(j2eeModule, null);
    }

    /**
     * Creates a new instance of EarDeploymentConfiguration 
     */
    public EarDeploymentConfiguration(J2eeModule j2eeModule, JBPluginUtils.Version version) {
        super(j2eeModule, version);
        jbossAppFile = j2eeModule.getDeploymentConfigurationFile("META-INF/jboss-app.xml"); // NOI18N
        getJbossApp();
        if (deploymentDescriptorDO == null) {
            try {
                deploymentDescriptorDO = deploymentDescriptorDO.find(FileUtil.toFileObject(jbossAppFile));
            } catch(DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
            }
        }
    }
    
    @Override
    public void dispose() {
    }
    
    @Override
    public Lookup getLookup() {
        return Lookups.fixed(this);
    }
       
    /**
     * Return jbossApp graph. If it was not created yet, load it from the file
     * and cache it. If the file does not exist, generate it.
     *
     * @return jbossApp graph or null if the jboss-app.xml file is not parseable.
     */
    public synchronized JbossApp getJbossApp() {
        if (jbossApp == null) {
            try {
                if (jbossAppFile.exists()) {
                    // load configuration if already exists
                    try {
                        jbossApp = jbossApp.createGraph(jbossAppFile);
                    } catch (IOException ioe) {
                        Exceptions.printStackTrace(ioe);
                    } catch (RuntimeException re) {
                        // jboss-app.xml is not parseable, do nothing
                    }
                } else {
                    // create jboss-app.xml if it does not exist yet
                    jbossApp = genereatejbossApp();
                    ResourceConfigurationHelper.writeFile(jbossAppFile, jbossApp);
                }
            } catch (ConfigurationException ce) {
                Exceptions.printStackTrace(ce);
            }
        }
        return jbossApp;
    }
    
    @Override
    public void save(OutputStream os) throws ConfigurationException {
        JbossApp jbossApp = getJbossApp();
        if (jbossApp == null) {
            String msg = NbBundle.getMessage(EarDeploymentConfiguration.class, "MSG_cannotSaveNotParseableConfFile", jbossAppFile.getAbsolutePath());
            throw new ConfigurationException(msg);
        }
        try {
            jbossApp.write(os);
        } catch (IOException ioe) {
            String msg = NbBundle.getMessage(EarDeploymentConfiguration.class, "MSG_CannotUpdateFile", jbossAppFile.getAbsolutePath());
            throw new ConfigurationException(msg, ioe);
        }
    }
    
    // private helper methods -------------------------------------------------
    
    /**
     * Genereate Context graph.
     */
    private JbossApp genereatejbossApp() {
        return new JbossApp();
    }    
}

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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
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

package org.netbeans.modules.j2ee.ddloaders.client;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.UniFileLoader;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.util.NbBundle;


/** Recognizes deployment descriptors of ejb modules.
 *
 * @author Ludovic Champenois
 */
public class ClientDataLoader extends UniFileLoader {
    
    private static final long serialVersionUID = 3616780278434213886L;
    private static final String REQUIRED_MIME_PREFIX_1 = "text/x-dd-client1.3"; // NOI18N
    private static final String REQUIRED_MIME_PREFIX_2 = "text/x-dd-client1.4"; // NOI18N
    private static final String REQUIRED_MIME_PREFIX_3 = "text/x-dd-client5.0"; // NOI18N
    private static final String REQUIRED_MIME_PREFIX_4 = "text/x-dd-client6.0"; // NOI18N
    private static final String REQUIRED_MIME_PREFIX_5 = "text/x-dd-client7.0"; // NOI18N
    
    public ClientDataLoader() {
        super("org.netbeans.modules.j2ee.ddloaders.client.ClientDataObject");  // NOI18N
    }
    
    protected String defaultDisplayName() {
        return NbBundle.getMessage(ClientDataLoader.class, "LBL_loaderName");
    }
    
    protected String actionsContext() {
        return "Loaders/text/x-dd/Actions/"; // NOI18N
    }
    
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME_PREFIX_1);
        getExtensions().addMimeType(REQUIRED_MIME_PREFIX_2);
        getExtensions().addMimeType(REQUIRED_MIME_PREFIX_3);
        getExtensions().addMimeType(REQUIRED_MIME_PREFIX_4);
        getExtensions().addMimeType(REQUIRED_MIME_PREFIX_5);
    }
    
    protected MultiDataObject createMultiObject(FileObject primaryFile)
    throws DataObjectExistsException, IOException {
        return new ClientDataObject(primaryFile, this);
    }
}

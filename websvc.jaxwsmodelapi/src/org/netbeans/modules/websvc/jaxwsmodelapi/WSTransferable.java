/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
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
/*
 * WebServiceTransferable.java
 *
 * Created on August 23, 2006, 1:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.netbeans.modules.websvc.jaxwsmodelapi;

import org.netbeans.modules.websvc.jaxwsmodelapi.WSReference;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author ayubskhan
 */
public class WSTransferable  extends ExTransferable.Single {
    private WSReference ref;
    
    public static final DataFlavor WS_FLAVOR =
        new DataFlavor(WSReference.class, "webservice ref");
    
    /** Creates a new instance of WebServiceTransferable */
    public WSTransferable(WSReference ref) {
        super(WS_FLAVOR);
        this.ref = ref;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return WS_FLAVOR.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (WS_FLAVOR.equals(flavor)) {
            return ref;
        } 
        throw new UnsupportedFlavorException(flavor);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[] {
            WS_FLAVOR
        };
    }

    @Override
    protected Object getData() throws IOException, UnsupportedFlavorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

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
 * 
 * Contributor(s):
 * 
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.modules.websvc.saas.ui.nodes;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.netbeans.modules.websvc.jaxwsmodelapi.WSOperation;
import org.netbeans.modules.websvc.jaxwsmodelapi.WSParameter;
import org.netbeans.modules.websvc.jaxwsmodelapi.java.JavaMethod;
import org.netbeans.modules.websvc.jaxwsmodelapi.java.JavaParameter;
import org.netbeans.modules.websvc.saas.model.Saas;
import org.netbeans.modules.websvc.saas.model.WsdlSaasMethod;
import org.netbeans.modules.websvc.saas.spi.MethodNodeActionsProvider;
import org.netbeans.modules.websvc.saas.util.SaasTransferable;
import org.netbeans.modules.websvc.saas.util.SaasUtil;
import org.netbeans.modules.websvc.saas.util.TypeUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport.Reflection;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author nam
 */
public class WsdlMethodNode extends AbstractNode {
    private WsdlSaasMethod method;
    private Transferable transferable;

    public WsdlMethodNode(WsdlSaasMethod method) {
        this(method, new InstanceContent());
    }

    protected WsdlMethodNode(WsdlSaasMethod method, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        this.method = method;
        content.add(method);
        transferable = ExTransferable.create(
                new SaasTransferable<WsdlSaasMethod>(method, SaasTransferable.WSDL_METHOD_FLAVORS));
    }

    @Override
    public String getDisplayName() {
        return method.getName();
    }

    @Override
    public String getShortDescription() {
        JavaMethod javaMethod = method.getJavaMethod();
        String signature;
        if (javaMethod != null) {
            signature = javaMethod.getReturnType().getFormalName() + " " + javaMethod.getName() + "(";
            Iterator parameterIterator = javaMethod.getParametersList().iterator();
            while (parameterIterator.hasNext()) {
                JavaParameter currentParam = (JavaParameter) parameterIterator.next();
                String parameterType = TypeUtil.getParameterType(currentParam);
                signature += parameterType + " " + currentParam.getName();
                if (parameterIterator.hasNext()) {
                    signature += ", ";
                }
            }

        }
        else{
            WSOperation wsOperation = method.getWsdlOperation();
            signature = wsOperation.getName() + "(";
            ListIterator<? extends WSParameter> iterator = wsOperation.getParameters().listIterator();
            while(iterator.hasNext()){
                WSParameter parameter =iterator.next();
                signature +=  parameter.getName();
                if(iterator.hasNext()){
                    signature += ", ";
                }
            }
        }
        signature += ")";
        return signature;
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = SaasNode.getActions(getLookup());
        for (MethodNodeActionsProvider ext : SaasUtil.getMethodNodeActionsProviders()) {
            actions.addAll(Arrays.asList(ext.getMethodActions(getLookup())));
        }
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Action getPreferredAction() {
        Action[] actions = getActions(true);
        return actions.length > 0 ? actions[0] : null;
    }

    @Override
    public Image getIcon(int type) {
        return getMethodIcon();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getMethodIcon();
    }

    private Image getMethodIcon() {
        JavaMethod javaMethod = method.getJavaMethod();
        if (javaMethod != null && !"void".equals(javaMethod.getReturnType().getRealName())) { // NOI18N
            Image image1 = ImageUtilities.loadImage("org/netbeans/modules/websvc/manager/resources/methodicon.png"); // NOI18N
            Image image2 = ImageUtilities.loadImage("org/netbeans/modules/websvc/manager/resources/table_dp_badge.png"); // NOI18N
            int x = image1.getWidth(null) - image2.getWidth(null);
            int y = image1.getHeight(null) - image2.getHeight(null);
            return ImageUtilities.mergeImages(image1, image2, x, y);
        } else {
            return ImageUtilities.loadImage("org/netbeans/modules/websvc/saas/ui/resources/methodicon.png"); // NOI18N
        }
    }

    /**
     * Create a property sheet for the individual method node
     * @return property sheet for the data source nodes
     */
    @Override
    protected Sheet createSheet() {
        JavaMethod javaMethod = method.getJavaMethod();
        Sheet sheet = super.createSheet();
        Set ss = sheet.get("data"); // NOI18N

        if (ss == null) {
            ss = new Set();
            ss.setName("data");  // NOI18N
            ss.setDisplayName(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_INFO")); // NOI18N
            ss.setShortDescription(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_INFO")); // NOI18N
            sheet.put(ss);
        }

        if (javaMethod == null) {
            return sheet;
        }

        try {
            Reflection p;

            p = new Reflection(javaMethod, String.class, "getName", null); // NOI18N
            p.setName("name"); // NOI18N
            p.setDisplayName(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_NAME")); // NOI18N
            p.setShortDescription(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_NAME")); // NOI18N
            ss.put(p);
            String signature = javaMethod.getReturnType().getRealName() + " " +
                    javaMethod.getName() + "(";

            Iterator tempIterator = javaMethod.getParametersList().iterator();
            while (tempIterator.hasNext()) {
                JavaParameter currentparam = (JavaParameter) tempIterator.next();
                signature += currentparam.getType().getRealName() + " " + currentparam.getName();
                if (tempIterator.hasNext()) {
                    signature += ", ";
                }
            }

            signature += ")";

            Iterator excpIterator = javaMethod.getExceptions();
            if (excpIterator.hasNext()) {
                signature += " throws";
                while (excpIterator.hasNext()) {
                    String currentExcp = (String) excpIterator.next();
                    signature += " " + currentExcp;
                    if (excpIterator.hasNext()) {
                        signature += ",";
                    }
                }


            }

            p = new Reflection(signature, String.class, "toString", null); // NOI18N
            p.setName("signature"); // NOI18N
            p.setDisplayName(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_SIGNATURE")); // NOI18N
            p.setShortDescription(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_SIGNATURE")); // NOI18N
            ss.put(p);

            p = new Reflection(javaMethod.getReturnType(), String.class, "getRealName", null); // NOI18N
            p.setName("returntype"); // NOI18N
            p.setDisplayName(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_RETURNTYPE")); // NOI18N
            p.setShortDescription(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_RETURNTYPE")); // NOI18N
            ss.put(p);

            Set paramSet = sheet.get("parameters"); // NOI18N
            if (paramSet == null) {
                paramSet = new Sheet.Set();
                paramSet.setName("parameters"); // NOI18N
                paramSet.setDisplayName(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_PARAMDIVIDER")); // NOI18N
                paramSet.setShortDescription(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_PARAMDIVIDER")); // NOI18N
                sheet.put(paramSet);
            }
            Iterator paramIterator = javaMethod.getParametersList().iterator();
            if (paramIterator.hasNext()) {
                p = new Reflection(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_PARAMTYPE"), // NOI18N
                        String.class,
                        "toString", // NOI18N
                        null);
                p.setName("paramdivider2"); // NOI18N

                for (int ii = 0; paramIterator.hasNext(); ii++) {
                    JavaParameter currentParameter = (JavaParameter) paramIterator.next();
                    if (currentParameter.getType().isHolder()) {
                        p = new Reflection(TypeUtil.getParameterType(currentParameter), String.class, "toString", null); // NOI18N
                    } else {
                        p = new Reflection(currentParameter.getType(), String.class, "getRealName", null); // NOI18N
                    }
                    p.setName("paramname" + ii); // NOI18N
                    p.setDisplayName(currentParameter.getName());
                    p.setShortDescription(currentParameter.getName() + "-" +
                            currentParameter.getType().getRealName());
                    paramSet.put(p);
                }
            }
            Set exceptionSet = sheet.get("exceptions"); // NOI18N
            if (exceptionSet == null) {
                exceptionSet = new Sheet.Set();
                exceptionSet.setName("exceptions"); // NOI18N
                exceptionSet.setDisplayName(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_EXCEPTIONDIVIDER")); // NOI18N
                exceptionSet.setShortDescription(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_EXCEPTIONDIVIDER")); // NOI18N
                sheet.put(exceptionSet);
            }

            Iterator exceptionIterator = javaMethod.getExceptions();
            for (int ii = 0; exceptionIterator.hasNext(); ii++) {
                String currentException = (String) exceptionIterator.next();
                p = new Reflection(currentException, String.class, "toString", null); // NOI18N
                p.setName("exception" + ii); // NOI18N
                p.setDisplayName(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_PARAMTYPE")); // NOI18N
                p.setShortDescription(NbBundle.getMessage(WsdlMethodNode.class, "METHOD_PARAMTYPE")); // NOI18N
                exceptionSet.put(p);
            }
        } catch (NoSuchMethodException nsme) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, nsme.getLocalizedMessage(), nsme);
        }

        return sheet;
    }
    // Handle copying and cutting specially:
    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        if (method.getSaas().getState() == Saas.State.READY) {
            return SaasTransferable.addFlavors(transferable);
        } else {
            method.getSaas().toStateReady(false);
            return super.clipboardCopy();
        }

    }
}

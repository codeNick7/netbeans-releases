/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
package org.netbeans.modules.swingapp.actiontable;

import org.netbeans.modules.swingapp.*;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

public class ProxyActionNode extends AbstractNode {
    
    public ProxyActionNode() {
        super(Children.LEAF);
        setDisplayName("Root");
    }
    
    public ProxyActionNode(Lookup lk) {
        super(Children.LEAF,lk);
    }
    
    public ProxyActionNode(ProxyActionChildren children, Lookup lk) {
        super(children,lk);
    }
    
    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        ProxyAction act = getLookup().lookup(ProxyAction.class);
        try {
            Property prop;
            prop = new PropertySupport.Reflection<String>(act, String.class, "getMethodName",null) { // NOI18N
                @Override
                public boolean canWrite() {
                    return false;
                }
            };
            prop.setName("methodName"); // NOI18N
            
            prop = new PropertySupport.ReadOnly("methodName",String.class,"Method Name","A Method Name") {
                public Object getValue() {
                    return "asdf";
                }
            };
            
            set.put(prop);
            prop = new PropertySupport.Reflection<String>(act, String.class, "getClassname",null); // NOI18N
            prop.setName("classname"); // NOI18N
            set.put(prop);
            
            prop = new PropertySupport.Reflection<String>(act, String.class, "getId",null); // NOI18N
            prop.setName("id"); // NOI18N
            set.put(prop);
            
            prop = new PropertySupport.Reflection<Boolean>(act, Boolean.class, "isTaskEnabled",null); // NOI18N
            prop.setName("task"); // NOI18N
            set.put(prop);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sheet.put(set);
        return sheet;
    }
    
}

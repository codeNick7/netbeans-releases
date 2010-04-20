/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */

package org.netbeans.modules.php.editor.codegen.ui;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import org.netbeans.modules.php.editor.api.elements.TypeElement;
import org.netbeans.modules.php.editor.api.elements.TypeTreeElement;
import org.netbeans.modules.php.editor.codegen.CGSGenerator;
import org.netbeans.modules.php.editor.codegen.CGSInfo;
import org.netbeans.modules.php.editor.codegen.MethodProperty;
import org.netbeans.modules.php.editor.codegen.Property;
import org.netbeans.modules.php.editor.codegen.ui.CheckNode.CGSClassNode;

/**
 * @author Tomas Mysik
 */
public class MethodPanel extends ConstructorPanel {

    public MethodPanel(CGSInfo cgsInfo) {
        super(CGSGenerator.GenType.METHODS, cgsInfo);
    }

    @Override
    protected MutableTreeNode getRootNode() {
        // get the enclosing type
        TypeTreeElement enclosingType = null;
        Map<TypeElement, List<MethodProperty>> data = new LinkedHashMap<TypeElement, List<MethodProperty>>();
        for (Property property : properties) {
            MethodProperty methodProperty = (MethodProperty) property;
            enclosingType = methodProperty.getEnclosingType();
            break;
        }

        // init tree
        CheckNode root = new CheckNode.CGSClassNode(className);

        LinkedList<TypeTreeElement> queue = new LinkedList<TypeTreeElement>();
        queue.offer(enclosingType);
        while (!queue.isEmpty()) {
            TypeTreeElement type = queue.poll();
            final CGSClassNode classNode = new CheckNode.CGSClassNode(type.getName());
            for (Property property : properties) {
                MethodProperty methodProperty = (MethodProperty) property;
                // XXX fix this
                if (methodProperty.getMethod().getType().equals(type)) {
                    classNode.add(new CheckNode.MethodPropertyNode(methodProperty));
                }
            }
            root.add(classNode);

            for (TypeTreeElement e : type.getDirectlyInherited()) {
                queue.offer(e);
            }
        }

        return root;
    }

    @Override
    protected void initTree(JTree tree) {
        tree.setRootVisible(false);
    }
}

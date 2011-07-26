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

package org.netbeans.modules.profiler.j2ee.selector.nodes.web.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.lib.profiler.results.cpu.cct.nodes.RuntimeCPUCCTNode.Children;
import org.netbeans.modules.j2ee.dd.api.web.Listener;
import org.netbeans.modules.j2ee.dd.api.web.WebAppMetadata;
import org.netbeans.modules.profiler.selector.spi.nodes.SelectorNode;
import org.openide.util.NbBundle;
import org.netbeans.modules.profiler.api.icons.Icons;
import org.netbeans.modules.profiler.j2ee.impl.icons.JavaEEIcons;
import org.netbeans.modules.profiler.j2ee.selector.nodes.web.AbstractWebContainerNode;
import org.netbeans.modules.profiler.selector.spi.nodes.ContainerNode;
import org.netbeans.modules.profiler.selector.spi.nodes.SelectorChildren;


/**
 *
 * @author Jaroslav Bachorik
 */
public class ListenersNode extends AbstractWebContainerNode {
    //~ Static fields/initializers -----------------------------------------------------------------------------------------------

    // -----
    // I18N String constants
    private static final String LISTENERS_STRING = NbBundle.getMessage(ListenersNode.class, "ListenersNode_ListenersString"); // NOI18N
                                                                                                                              // -----

    //~ Constructors -------------------------------------------------------------------------------------------------------------

    /** Creates a new instance of ServletsNode */
    public ListenersNode(ContainerNode parent) {
        super(LISTENERS_STRING, parent);
    }

    //~ Methods ------------------------------------------------------------------------------------------------------------------

    @Override
    protected Collection<SelectorNode> collectChildren(ClasspathInfo cpInfo, CompilationController cc, WebAppMetadata md) {
        Collection<SelectorNode> fNodes = new ArrayList<SelectorNode>();
        for(Listener li : md.getRoot().getListener()) {
            TypeElement sType = cc.getElements().getTypeElement(li.getListenerClass());

            fNodes.add(new ListenerNode(cpInfo, sType, this));
        }
        return fNodes;
    }
}

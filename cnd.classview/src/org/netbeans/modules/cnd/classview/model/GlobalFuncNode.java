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

package org.netbeans.modules.cnd.classview.model;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import org.openide.nodes.*;

import  org.netbeans.modules.cnd.api.model.*;
import org.netbeans.modules.cnd.modelutil.CsmImageLoader;

/**
 * @author Vladimir Kvasihn
 */
public class GlobalFuncNode extends ObjectNode {
    private CharSequence text;
    public GlobalFuncNode(CsmFunction fun) {
        super(fun, Children.LEAF);
        init(fun);
    }
    
    private void init(final CsmFunction fun){
        final CharSequence old = text;
        text = CVUtil.getSignature(fun);
        final Runnable runnable = new Runnable() {
            
            @Override
            public void run() {
                if (SwingUtilities.isEventDispatchThread()) {
                    if ((old == null) || !old.equals(text)) {
                        fireNameChange(old == null ? null : old.toString(),
                                text == null ? null : text.toString());
                        fireDisplayNameChange(old == null ? null : old.toString(),
                                text == null ? null : text.toString());
                        fireShortDescriptionChange(old == null ? null : old.toString(),
                                text == null ? null : text.toString());
                    }
                    fireIconChange();
                    fireOpenedIconChange();
                } else {
                    resetIcon(CsmImageLoader.getImage(fun));
                    SwingUtilities.invokeLater(this);
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            RP.post(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public String getName() {
        return text.toString();
    }

    @Override
    public String getDisplayName() {
        return text.toString();
    }

    @Override
    public String getShortDescription() {
        return text.toString();
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        Object o = e.getSource();
        if (o instanceof CsmFunction){
            CsmFunction cls = (CsmFunction)o;
            setObject(cls);
            init(cls);
        } else if (o != null) {
            System.err.println("Expected CsmFunction. Actually event contains "+o.toString());
        }
    }
}

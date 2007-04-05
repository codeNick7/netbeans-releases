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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.debugger.jpda.heapwalk.views;

import java.awt.BorderLayout;
import org.netbeans.modules.profiler.heapwalk.HeapFragmentWalker;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Martin Entlicher
 */
public class InstancesView extends TopComponent {
    
    /** Creates a new instance of InstancesView */
    public InstancesView() {
        setIcon (Utilities.loadImage ("org/netbeans/modules/debugger/jpda/resources/root.gif")); // NOI18N
    }

    protected void componentShowing() {
        super.componentShowing ();
        ClassesCountsView cc = (ClassesCountsView) WindowManager.getDefault().findTopComponent("classesCounts");
        HeapFragmentWalker hfw = cc.getCurrentFragmentWalker();
        if (hfw != null) {
            setLayout (new BorderLayout ());
            add(hfw.getInstancesController().getPanel(), "Center");
        }
    }
    
    public String getName () {
        return "Instances";
        //return NbBundle.getMessage (ClassesView.class, "CTL_Classes_view");
    }
    
    public String getToolTipText () {
        return "Instances";
        //return NbBundle.getMessage (ClassesView.class, "CTL_Classes_tooltip");// NOI18N
    }

    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }
    
}

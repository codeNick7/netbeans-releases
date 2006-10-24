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
package org.netbeans.modules.refactoring.spi.impl;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.refactoring.api.ui.RefactoringActionsFactory;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

/** 
 * @author Jan Becicka
 */
public class SafeDeleteAction extends RefactoringGlobalAction {

    /**
     * Creates a new instance of RenameAction
     */
    public SafeDeleteAction() {
        super("SafeDelete", null);
        putValue("noIconInMenu", Boolean.TRUE); // NOI18N
    }
    
    public final void performAction(final Node[] n) {
        RefactoringActionsFactory.deleteImpl(getLookup(n)).run();
    }
    
    public org.openide.util.HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected boolean asynchronous() {
        return false;
    }

    protected boolean enable(Node[] activatedNodes) {
        if (RefactoringActionsFactory.canDelete(getLookup(activatedNodes))) 
            return true;
        return false;
    }
}

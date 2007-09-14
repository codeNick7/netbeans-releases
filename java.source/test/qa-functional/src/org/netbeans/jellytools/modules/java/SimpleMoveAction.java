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

package org.netbeans.jellytools.modules.java;

import org.netbeans.jellytools.Bundle;
import org.netbeans.jellytools.MainWindowOperator;
import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.operators.Operator;

/**
 *
 * @author Jiri Prox
 */
public class SimpleMoveAction {
    
    protected static final String POPUP = Bundle.getStringTrimmed("org.openide.actions.Bundle", "Paste");
    protected static final String MENU = Bundle.getStringTrimmed("org.netbeans.core.Bundle", "Menu/Edit")
            + "|" + POPUP+ "|Move";
    
    public void perform() {
        new EventTool().waitNoEvent(500);
        MainWindowOperator.getDefault().menuBar().pushMenu(MENU, "|", new Operator.DefaultStringComparator(true, true));
    }
    
}

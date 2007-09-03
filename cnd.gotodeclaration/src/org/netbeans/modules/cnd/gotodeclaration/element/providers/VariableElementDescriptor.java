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
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 */
package org.netbeans.modules.cnd.gotodeclaration.element.providers;

import javax.swing.Icon;
import org.netbeans.modules.cnd.api.model.*;
import org.netbeans.modules.cnd.gotodeclaration.element.spi.ElementDescriptor;
import org.netbeans.modules.cnd.gotodeclaration.util.ContextUtil;

import org.netbeans.modules.cnd.modelutil.CsmImageLoader;


/**
 * An ElementDescriptor for variables (CsmVariable)
 * @author Vladimir Kvashin
 */

/* package */ 
class VariableElementDescriptor extends BaseElementDescriptor implements ElementDescriptor {

    private final CsmUID<CsmVariable> uid;
    private String displayName = null;
    private String contextName = null;
    
    @SuppressWarnings("unchecked")
    VariableElementDescriptor(CsmVariable var) {
	uid = var.getUID();
	displayName = var.getName();
	contextName = ContextUtil.getContextName(var);
    }
    
    protected CsmOffsetable getElement() {
	return uid.getObject();
    }

    protected String getContextNameImpl() {
	return contextName;
    }

    public String getDisplayName() {
	return displayName;
    }

    public Icon getIcon() {
	return CsmImageLoader.getIcon(CsmDeclaration.Kind.VARIABLE, 0);
    }

}

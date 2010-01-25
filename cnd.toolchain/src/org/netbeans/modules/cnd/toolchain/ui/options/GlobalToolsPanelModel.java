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


package org.netbeans.modules.cnd.toolchain.ui.options;

import org.netbeans.modules.cnd.toolchain.ui.api.ToolsPanelModel;

/** Manage the data for the ToolsPanel */
/*package-local*/ final class GlobalToolsPanelModel extends ToolsPanelModel {
    
    @Override
    public void setMakeRequired(boolean value) {
        
    }
    
    @Override
    public boolean isMakeRequired() {
        return false;
    }
    
    @Override
    public boolean isDebuggerRequired() {
        return false;
    }
    
    @Override
    public void setDebuggerRequired(boolean value) {
//        CppSettings.getDefault().setGdbRequired(value);
    }
    
    @Override
    public boolean isCRequired() {
        return false; //return CppSettings.getDefault().isCRequired();
    }
    
    @Override
    public void setCRequired(boolean value) {
//        CppSettings.getDefault().setCRequired(value);
    }
    
    @Override
    public boolean isCppRequired() {
        return false; //return CppSettings.getDefault().isCppRequired();
    }
    
    @Override
    public void setCppRequired(boolean value) {
//        CppSettings.getDefault().setCppRequired(value);
    }
    
    @Override
    public boolean isFortranRequired() {
        return false;
        //return CppSettings.getDefault().isFortranEnabled();
        //return CppSettings.getDefault().isFortranRequired();
    }
    
    @Override
    public void setFortranRequired(boolean value) {
//        CppSettings.getDefault().setFortranRequired(value);
    }

    @Override
    public boolean isAsRequired() {
        return false;
    }

    @Override
    public void setAsRequired(boolean value) {
    }
    
    @Override
    public boolean showRequiredTools() {
        return false;
    }
    
    public void setRequiredBuildTools(boolean enabled) {
 
    }
    
    @Override
    public void setShowRequiredBuildTools(boolean enabled) {
        
    }
    
    @Override
    public boolean showRequiredBuildTools() {
        return false;
    }
    
    @Override
    public void setShowRequiredDebugTools(boolean enabled) {
        
    }
    
    @Override
    public boolean showRequiredDebugTools() {
        return false;
    }

    @Override
    public void setEnableDevelopmentHostChange(boolean value) {
    }

    @Override
    public boolean getEnableDevelopmentHostChange() {
        return true;
    }
}

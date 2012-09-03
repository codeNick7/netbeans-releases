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
package org.netbeans.modules.web.browser.api;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.web.browser.api.BrowserSupport;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 * Reloads page in browser using BrowserSupport from project's lookup.
 */
public class BrowserReloadTask extends Task {

    
    public void execute() throws BuildException {
        ClassLoader originalLoader = null;

        try {
            // see issue #62448
            ClassLoader current = (ClassLoader)Lookup.getDefault().lookup(ClassLoader.class);
            if (current == null) {
                current = ClassLoader.getSystemClassLoader();
            }
            if (current != null) {
                originalLoader = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(current);
            }

            FileObject fileObject = FileUtil.toFileObject( FileUtil.normalizeFile( 
                    new File(filePath)));
            
            BrowserSupport bs = null;
            Project p = FileOwnerQuery.getOwner(fileObject);
            if (p != null) {
                bs = (BrowserSupport)p.getLookup().lookup(BrowserSupport.class);
            }
            if (bs == null) {
                bs = BrowserSupport.getDefault(); 
            }
            try {
                URL url = new URL(getUrl());
                bs.load(url, fileObject);
            } catch (MalformedURLException ex) {
                Exceptions.printStackTrace(ex);
            }
        } finally {
            if (originalLoader != null) {
                Thread.currentThread().setContextClassLoader(originalLoader);
            }
        }
    }

    /**
     * Project's file object to associate with URL being opened in the browser.
     * FileObject will be used to evaluate which project changes have impact
     * on the URL in order to decide whether URL needs to be refreshed in browser 
     * or not.
     */
    public String getFilePath(){
        return filePath;
    }
    
    /**
     * URL to open in browser.
     */
    public String getUrl(){
        return url;
    }
    
    public void setUrl(String url ){
        this.url = url;
    }
    
    public void setFilePath( String path ){
        filePath = path;
    }

    private String filePath;
    private String url;
    
}

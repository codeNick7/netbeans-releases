/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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

package org.netbeans.modules.bugtracking.vcs;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import org.netbeans.modules.bugtracking.TestIssue;
import org.netbeans.modules.bugtracking.TestQuery;
import org.netbeans.modules.bugtracking.TestRepository;
import org.netbeans.modules.bugtracking.api.Issue;
import org.netbeans.modules.bugtracking.api.Query;
import org.netbeans.modules.bugtracking.spi.*;
import org.openide.util.Lookup;

/**
 *
 * @author Tomas Stupka
 */
public class HookRepository extends TestRepository {
    private RepositoryInfo info = new RepositoryInfo("HookRepository", "HookRepository", "http://url", "HookRepository", "HookRepository", null, null, null, null);

    @Override
    public RepositoryInfo getInfo() {
        return info;
    }

    @Override
    public Image getIcon() {
        return null;
    }

    @Override
    public TestIssue[] getIssues(String[] id) {
        return new TestIssue[] {HookIssue.instance};
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RepositoryController getController() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TestQuery createQuery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TestIssue createIssue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<TestQuery> getQueries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<TestIssue> simpleSearch(String criteria) {
        return Arrays.asList(new TestIssue[] {HookIssue.instance});
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) { }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) { }

    @Override
    public void refreshQueries(TestQuery... queries) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshIssues(TestIssue... issues) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

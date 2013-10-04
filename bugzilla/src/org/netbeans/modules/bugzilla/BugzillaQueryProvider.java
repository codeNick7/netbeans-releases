/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * <p/>
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
 * <p/>
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 * <p/>
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development and
 * Distribution License("CDDL") (collectively, the "License"). You may not use
 * this file except in compliance with the License. You can obtain a copy of
 * the License at http://www.netbeans.org/cddl-gplv2.html or
 * nbbuild/licenses/CDDL-GPL-2-CP. See the License for the specific language
 * governing permissions and limitations under the License. When distributing
 * the software, include this License Header Notice in each file and include
 * the License file at nbbuild/licenses/CDDL-GPL-2-CP. Oracle designates this
 * particular file as subject to the "Classpath" exception as provided by
 * Oracle in the GPL Version 2 section of the License file that accompanied
 * this code. If applicable, add the following below the License Header, with
 * the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 * <p/>
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you do not indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to its
 * licensees as provided above. However, if you add GPL Version 2 code and
 * therefore, elected the GPL Version 2 license, then the option applies only
 * if the new code is made subject to such option by the copyright holder.
 * <p/>
 * Contributor(s):
 * <p/>
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */
package org.netbeans.modules.bugzilla;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.netbeans.modules.bugtracking.team.spi.TeamQueryProvider;
import org.netbeans.modules.bugtracking.team.spi.OwnerInfo;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.netbeans.modules.bugzilla.issue.BugzillaIssue;
import org.netbeans.modules.bugzilla.kenai.KenaiRepository;
import org.netbeans.modules.bugzilla.query.BugzillaQuery;
import org.netbeans.modules.bugzilla.repository.BugzillaRepository;

/**
 *
 * @author Tomas Stupka
 */
public class BugzillaQueryProvider extends TeamQueryProvider<BugzillaQuery, BugzillaIssue> {

    @Override
    public String getDisplayName(BugzillaQuery query) {
        return query.getDisplayName();
    }

    @Override
    public String getTooltip(BugzillaQuery query) {
        return query.getTooltip();
    }

    @Override
    public QueryController getController(BugzillaQuery query) {
        return query.getController();
    }

    @Override
    public void remove(BugzillaQuery q) {
        q.remove();
    }
    
    @Override
    public boolean isSaved(BugzillaQuery query) {
        return query.isSaved();
    }

    @Override
    public Collection<BugzillaIssue> getIssues(BugzillaQuery query) {
        return query.getIssues();
    }

    @Override
    public void removePropertyChangeListener(BugzillaQuery query, PropertyChangeListener listener) {
        query.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(BugzillaQuery query, PropertyChangeListener listener) {
        query.addPropertyChangeListener(listener);
    }

    @Override
    public boolean contains(BugzillaQuery query, String id) {
        return query.contains(id);
    }

    @Override
    public void refresh(BugzillaQuery query) {
        query.getController().refresh(true);
    }


    /************************************************************************************
     * Kenai
     ************************************************************************************/
    
    @Override
    public void setOwnerInfo(BugzillaQuery q, OwnerInfo info) {
        q.setOwnerInfo(info);
    }
    
    @Override
    public boolean needsLogin(BugzillaQuery query) {
        BugzillaRepository repository = query.getRepository();
        return query == ((KenaiRepository) repository).getMyIssuesQuery();
    }

    @Override
    public boolean canRename(BugzillaQuery q) {
        return true;
    }

    @Override
    public void rename(BugzillaQuery q, String displayName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

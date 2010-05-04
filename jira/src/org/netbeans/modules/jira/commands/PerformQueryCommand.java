/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.modules.jira.commands;

import com.atlassian.connector.eclipse.internal.jira.core.JiraRepositoryConnector;
import com.atlassian.connector.eclipse.internal.jira.core.model.JiraFilter;
import com.atlassian.connector.eclipse.internal.jira.core.service.JiraException;
import com.atlassian.connector.eclipse.internal.jira.core.util.JiraUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tasks.core.RepositoryQuery;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.netbeans.libs.bugtracking.BugtrackingRuntime;
import org.netbeans.modules.jira.Jira;
import org.netbeans.modules.jira.repository.JiraRepository;

/**
 * Perfoms a repository query
 * 
 * @author Tomas Stupka
 */
public class PerformQueryCommand extends JiraCommand {

    private final JiraRepository repository;
    private final JiraFilter jiraFilter;
    private final TaskDataCollector collector;

    public PerformQueryCommand(JiraRepository repository, JiraFilter jiraFilter, TaskDataCollector collector) {
        this.repository = repository;
        this.jiraFilter = jiraFilter;
        this.collector = collector;
    }

    @Override
    public void execute() throws CoreException, JiraException {
        JiraRepositoryConnector rc = Jira.getInstance().getRepositoryConnector();
        RepositoryQuery repositoryQuery = new RepositoryQuery(rc.getConnectorKind(), "query"); // NOI18N
        JiraUtil.setQuery(repository.getTaskRepository(), repositoryQuery, jiraFilter);
        rc.performQuery(
                repository.getTaskRepository(),
                repositoryQuery,
                collector,
                BugtrackingRuntime.getInstance().getSynchronizationSession(),
                new NullProgressMonitor());
    }

}

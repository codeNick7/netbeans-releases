/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.netbeans.modules.odcs.ui.project.activity;

import java.awt.event.MouseEvent;
import java.util.MissingResourceException;
import javax.swing.Icon;
import javax.swing.JComponent;
import oracle.clouddev.server.profile.activity.client.api.Activity;
import org.netbeans.modules.bugtracking.commons.TextUtils;
import org.netbeans.modules.odcs.api.ODCSProject;
import org.netbeans.modules.odcs.ui.project.LinkLabel;
import org.netbeans.modules.odcs.ui.utils.Utils;
import org.netbeans.modules.team.server.ui.spi.ProjectHandle;
import org.openide.util.ImageUtilities;

public final class WikiActivityDisplayer extends ActivityDisplayer {

    private static final String PROP_PAGE = "page"; // NOI18N
    private static final String PROP_TYPE = "type"; // NOI18N

    private final Activity activity;
    private final ProjectHandle<ODCSProject> projectHandle;

    public WikiActivityDisplayer(Activity activity, ProjectHandle<ODCSProject> projectHandle, int maxWidth) {
        super(activity.getTimestamp(), maxWidth);
        this.activity = activity;
        this.projectHandle = projectHandle;
    }

    @Override
    public JComponent getShortDescriptionComponent() {
        final String page = activity.getProperty(PROP_PAGE);
        LinkLabel pageLink = new LinkLabel(page) {
            @Override
            public void mouseClicked(MouseEvent e) {
                Utils.openBrowser(getPageUrl(page));
            }
        };
        String bundleKey = "FMT_Wiki_" + activity.getProperty(PROP_TYPE); // NOI18N
        try {
            return createMultipartTextComponent(bundleKey, pageLink);
        } catch (MissingResourceException ex) {
            return createMultipartTextComponent("FMT_Wiki_UPDATED", pageLink); // NOI18N
        }
    }

    @Override
    public JComponent getDetailsComponent() {
        return null;
    }

    @Override
    String getUserName() {
        return activity.getAuthor().getFullname();
    }

    @Override
    public Icon getActivityIcon() {
        return ImageUtilities.loadImageIcon("org/netbeans/modules/odcs/ui/resources/activity_wiki.png", true); //NOI18N
    }

    private String getPageUrl(String page) {
        page = TextUtils.encodeURL(page);
        return projectHandle.getTeamProject().getWebUrl() + "/wiki/p/" + page; // NOI18N
    }
}

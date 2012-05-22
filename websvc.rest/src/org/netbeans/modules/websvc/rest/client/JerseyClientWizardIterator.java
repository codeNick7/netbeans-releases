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
package org.netbeans.modules.websvc.rest.client;

import java.awt.Component;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.modules.websvc.api.support.SourceGroups;
import org.netbeans.spi.java.project.support.ui.templates.JavaTemplates;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.RequestProcessor;

public final class JerseyClientWizardIterator implements WizardDescriptor.InstantiatingIterator {

    private int index;
    private WizardDescriptor wizard;
    private WizardDescriptor.Panel[] panels;
    private transient WizardDescriptor.Panel<WizardDescriptor> firstPanel;
    private transient JerseyClientWizardPanel bottomPanel;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        return panels;
    }

    @Override
    public Set<DataObject> instantiate() throws IOException {
        FileObject template = Templates.getTemplate(wizard);
        template.setAttribute("REST_RESOURCE_NAME", bottomPanel.getResourceName());
        DataObject dTemplate = DataObject.find( template );
        org.openide.filesystems.FileObject dir = Templates.getTargetFolder(wizard);
        DataFolder df = DataFolder.findFolder( dir );
        final DataObject dobj = dTemplate.createFromTemplate(df, Templates.getTargetName(wizard ));
        // generating client
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                ClientJavaSourceHelper.generateJerseyClient(bottomPanel.getResourceNode(), dobj.getPrimaryFile(), null, bottomPanel.getSecurity());
            }
        });
        return Collections.<DataObject>singleton(dobj);
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        Project project = Templates.getProject(wizard);

        SourceGroup[] sourceGroups = SourceGroups.getJavaSourceGroups(project);
        bottomPanel = new JerseyClientWizardPanel();
        //WizardDescriptor.Panel firstPanel; //special case: use Java Chooser
        if (sourceGroups.length == 0) {
            SourceGroup[] genericSourceGroups = ProjectUtils.
                    getSources(project).getSourceGroups(Sources.TYPE_GENERIC);
            firstPanel = new FinishableProxyWizardPanel(Templates.
                    createSimpleTargetChooser(project, genericSourceGroups, bottomPanel), 
                    sourceGroups, false);
        } else {
            firstPanel = new FinishableProxyWizardPanel(
                    JavaTemplates.createPackageChooser(project, sourceGroups, 
                    bottomPanel, true));
        }
        panels = new WizardDescriptor.Panel[] { firstPanel };
        String[] steps = createSteps();
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (steps[i] == null) {
                // Default step name to component name of panel. Mainly
                // useful for getting the name of the target chooser to
                // appear in the list of steps.
                steps[i] = c.getName();
            }
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                // Sets step number of a component
                // TODO if using org.openide.dialogs >= 7.8, can use
                // WizardDescriptor.PROP_*:
                jc.putClientProperty("WizardPanel_contentSelectedIndex",
                        new Integer(i));
                // Sets steps names for a panel
                jc.putClientProperty("WizardPanel_contentData", steps);
                // Turn on subtitle creation on each step
                jc.putClientProperty("WizardPanel_autoWizardStyle",
                        Boolean.TRUE);
                // Show steps on the left side with the image on the background
                jc.putClientProperty("WizardPanel_contentDisplayed",
                        Boolean.TRUE);
                // Turn on numbering of all steps
                jc.putClientProperty("WizardPanel_contentNumbered",
                        Boolean.TRUE);
            }
        }

    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels()[index];
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = null;
        Object prop = wizard.getProperty("WizardPanel_contentData");
        if (prop != null && prop instanceof String[]) {
            beforeSteps = (String[]) prop;
        }

        if (beforeSteps == null) {
            beforeSteps = new String[0];
        }

        String[] res = new String[(beforeSteps.length - 1) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
            }
        }
        return res;
    }

}

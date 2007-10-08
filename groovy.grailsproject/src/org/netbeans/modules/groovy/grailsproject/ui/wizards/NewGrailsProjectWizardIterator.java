/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 */

package org.netbeans.modules.groovy.grailsproject.ui.wizards;

import java.awt.Component;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import java.util.NoSuchElementException;
import javax.swing.JComponent;
import org.openide.util.NbBundle;
import java.io.BufferedReader;
import org.netbeans.modules.groovy.grails.api.GrailsServer;
import org.netbeans.modules.groovy.grails.api.GrailsServerFactory;
import org.openide.util.Exceptions;
import javax.swing.JTextArea;
import java.io.File;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileObject;



/**
 *
 * @author schmidtm
 */
public class NewGrailsProjectWizardIterator implements WizardDescriptor.InstantiatingIterator {
    
    private transient int index;
    private transient WizardDescriptor.Panel[] panels;
    private transient WizardDescriptor wiz;
    
    ServerOutputStep sos = null;
    
    private WizardDescriptor.Panel[] createPanels () {
        
        sos = new ServerOutputStep();
        
        return new WizardDescriptor.Panel[] {
                new GetProjectLocationStep(), sos };
    }
    
     private String[] createSteps() {
            return new String[] {
                NbBundle.getMessage(NewGrailsProjectWizardIterator.class,"LAB_ConfigureProject"), 
                NbBundle.getMessage(NewGrailsProjectWizardIterator.class,"LAB_CaptureOutput")
            };
    }
    
    
    
    
    public Set instantiate() throws IOException {
        Set<FileObject> resultSet = new HashSet<FileObject> ();
        File dirF = new File((String)wiz.getProperty("projectFolder"));
        if (dirF != null) {
            dirF = FileUtil.normalizeFile(dirF);
        }
        
        FileObject dir = FileUtil.toFileObject(dirF);
        resultSet.add (dir);
        
        return resultSet;
    }

    public void initialize(WizardDescriptor wizard) {
        this.wiz = wizard;
        index = 0;
        panels = createPanels();
        String[] steps = createSteps();
        
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (steps[i] == null) {
                // Default step name to component name of panel.
                // Mainly useful for getting the name of the target
                // chooser to appear in the list of steps.
                steps[i] = c.getName();
            }
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent)c;
                // Step #.
                jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i)); // NOI18N
                // Step name (actually the whole list for reference).
                jc.putClientProperty("WizardPanel_contentData", steps); // NOI18N
            }
        }
    }

    public void uninitialize(WizardDescriptor wizard) {
        


        
    }

    public Panel current() {
        return panels[index];
    }

    public String name() {
        return MessageFormat.format (NbBundle.getMessage(NewGrailsProjectWizardIterator.class,"LAB_IteratorName"),
            new Object[] {new Integer (index + 1), new Integer (panels.length) });      
    }

    public boolean hasNext() {
        return index < panels.length - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) throw new NoSuchElementException();
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) throw new NoSuchElementException();
        index--;
    }

    public void addChangeListener(ChangeListener l) {}

    public void removeChangeListener(ChangeListener l) {}

}

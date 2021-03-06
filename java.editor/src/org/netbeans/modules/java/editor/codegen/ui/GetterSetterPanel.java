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
 *
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

package org.netbeans.modules.java.editor.codegen.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import javax.lang.model.element.Element;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.modules.java.editor.codegen.GeneratorUtils;
import org.netbeans.modules.java.editor.codegen.GetterSetterGenerator;
import org.openide.explorer.ExplorerManager;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

/**
 *
 * @author  Dusan Balek
 */
public class GetterSetterPanel extends JPanel implements PropertyChangeListener {
    
    private ElementSelectorPanel elementSelector;
    private HashSet<ElementHandle<? extends Element>> thisFields;
    private RequestProcessor.Task currentTask;
    private final RequestProcessor RP = new RequestProcessor(GetterSetterPanel.class);
    
    
    /** Creates new form GetterSetterPanel */
    public GetterSetterPanel(ElementNode.Description description, int type) {
        initComponents();
        elementSelector = new ElementSelectorPanel(description, false);
        elementSelector.getExplorerManager().addPropertyChangeListener(this);
        thisFields = new HashSet<ElementHandle<? extends Element>>();
        for (ElementNode.Description desc:description.getSubs().get(0).getSubs()) {
            thisFields.add(desc.getElementHandle());
        }
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        add(elementSelector, gridBagConstraints);
        if (type == GeneratorUtils.GETTERS_ONLY)
            selectorLabel.setText(NbBundle.getMessage(GetterSetterGenerator.class, "LBL_getter_field_select")); //NOI18N
        else if (type == GeneratorUtils.SETTERS_ONLY)
            selectorLabel.setText(NbBundle.getMessage(GetterSetterGenerator.class, "LBL_setter_field_select")); //NOI18N
        else
            selectorLabel.setText(NbBundle.getMessage(GetterSetterGenerator.class, "LBL_getter_and_setter_field_select")); //NOI18N
        selectorLabel.setLabelFor(elementSelector);
        
        elementSelector.doInitialExpansion(1);
	
	this.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(GetterSetterGenerator.class, "A11Y_Generate_GetterSetter"));
    }
    
    public List<ElementHandle<? extends Element>> getVariables() {
        return ((ElementSelectorPanel)elementSelector).getSelectedElements();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        selectorLabel = new javax.swing.JLabel();
        performEncapsulate = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 6, 12);
        add(selectorLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(performEncapsulate, org.openide.util.NbBundle.getMessage(GetterSetterGenerator.class, "GetterSetterPanel.performEncapsulate.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        add(performEncapsulate, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox performEncapsulate;
    private javax.swing.JLabel selectorLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ExplorerManager.PROP_NODE_CHANGE.equals(evt.getPropertyName()))
            updateEncapsulateCheckBox();
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    private void updateEncapsulateCheckBox() {
        if (currentTask!=null) {
            currentTask.cancel();
        }
        currentTask = RP.post(new Runnable() {

            @Override
            public void run() {
                final List<ElementHandle<? extends Element>> selected = elementSelector.getSelectedElements();
                selected.removeAll(thisFields);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        performEncapsulate.setEnabled(selected.isEmpty());
                    }
                });
            }
        });
    }
    
    public boolean isPerformEnsapsulate() {
        return performEncapsulate.isEnabled() && performEncapsulate.isSelected();
    }
}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2015 Oracle and/or its affiliates. All rights reserved.
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
package org.netbeans.lib.profiler.ui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import org.netbeans.lib.profiler.ui.UIUtils;
import org.netbeans.lib.profiler.ui.components.CloseButton;
import org.netbeans.modules.profiler.api.icons.GeneralIcons;
import org.netbeans.modules.profiler.api.icons.Icons;

/**
 *
 * @author Jiri Sedlacek
 */
public final class FilterUtils {
    
    public static boolean filterContains(ProfilerTable table, final String text) {
        final int mainColumn = table.getMainColumn();
        
        Filter filter = new Filter() {
            public boolean include(RowFilter.Entry entry) {
                return (entry.getValue(mainColumn)).toString().contains(text);
            }
        };
        
        if (text == null || text.isEmpty()) {
            table.removeRowFilter(filter);
            return false;
        } else {
            table.addRowFilter(filter);
            return table.getRowCount() > 0;
        }
    }
    
    public static JComponent createFilterPanel(final ProfilerTable table) {
        JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
        if (UIUtils.isGTKLookAndFeel() || UIUtils.isNimbusLookAndFeel())
                toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
        toolbar.setBorder(BorderFactory.createEmptyBorder(1, 2, 0, 2));
        toolbar.setBorderPainted(false);
        toolbar.setRollover(true);
        toolbar.setFloatable(false);
        toolbar.setOpaque(false);
        
        toolbar.add(Box.createHorizontalStrut(6));
        toolbar.add(new JLabel("Filter:"));
        toolbar.add(Box.createHorizontalStrut(3));
        
        final EditableHistoryCombo combo = new EditableHistoryCombo();        
        final JTextComponent textC = combo.getTextComponent();
        
        toolbar.add(combo);
        
        toolbar.add(Box.createHorizontalStrut(5));
        
        KeyStroke escKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        KeyStroke filterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        
        final String[] activeFilter = new String[1];
        
        final JButton filter = new JButton("Filter", Icons.getIcon(GeneralIcons.FILTER)) {
            protected void fireActionPerformed(ActionEvent e) {
                super.fireActionPerformed(e);
                final JButton _filter = this;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        String filterString = getFilterString(combo);
                        if (filterContains(table, filterString)) combo.addItem(filterString);
                        activeFilter[0] = filterString;
                        updateFilterButton(_filter, activeFilter, filterString);
                    }
                });
            }
        };
        String filterAccelerator = UIUtils.keyAcceleratorString(filterKey);
        filter.setToolTipText("Filter results (" + filterAccelerator + ")");
        toolbar.add(filter);
        
        updateFilterButton(filter, activeFilter, getFilterString(combo));
        
        toolbar.add(Box.createHorizontalStrut(2));
        
        combo.setOnTextChangeHandler(new Runnable() {
            public void run() {
                updateFilterButton(filter, activeFilter, getFilterString(combo));
            }
        });
        
        final JPanel panel = new JPanel(new BorderLayout()) {
            public boolean requestFocusInWindow() {
                if (textC != null) textC.selectAll();
                return combo.requestFocusInWindow();
            }
        };
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIManager.getColor("controlShadow"))); // NOI18N
        panel.add(toolbar, BorderLayout.CENTER);
        
        final Runnable hider = new Runnable() {
            public void run() {
                activeFilter[0] = null;
                updateFilterButton(filter, activeFilter, getFilterString(combo));
                filterContains(table, activeFilter[0]);
                panel.setVisible(false);
            }
        };
        JButton closeButton = CloseButton.create(hider);
        String escAccelerator = UIUtils.keyAcceleratorString(escKey);
        closeButton.setToolTipText("Close Filter sidebar (" + escAccelerator + ")");
        panel.add(closeButton, BorderLayout.EAST);
        
        String HIDE = "hide-action"; // NOI18N
        InputMap map = panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        Action hiderAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) { hider.run(); }
        };
        panel.getActionMap().put(HIDE, hiderAction);
        map.put(escKey, HIDE);
        
        if (textC != null) {
            map = textC.getInputMap();
            String FILTER = "filter-action"; // NOI18N
            Action nextAction = new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() { if (filter.isEnabled()) filter.doClick(); }
                    });
                }
            };
            textC.getActionMap().put(FILTER, nextAction);
            map.put(filterKey, FILTER);
        }
        
        return panel;
    }
    
    private static String getFilterString(EditableHistoryCombo combo) {
        String filter = combo.getText();
        return filter == null ? null : filter.trim();
    }
    
    private static void updateFilterButton(JButton button, String[] activeFilter, String currentFilter) {
        String active = activeFilter[0];
        if (active == null) active = ""; // NOI18N
        String current = currentFilter == null ? "" : currentFilter; // NOI18N
        button.setEnabled(!current.equals(active));
    }
    
    private static abstract class Filter extends RowFilter {

        public boolean equals(Object o) {
            return o instanceof Filter;
        }
        
        public int hashCode() {
            return Integer.MAX_VALUE;
        }
    
    }
    
}

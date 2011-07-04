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

package org.netbeans.modules.form.editors;

import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.awt.Mnemonics;


/**
 * A custom editor for array of Strings.
 *
 * @author  Jiri Vagner
 */
public class StringArrayCustomEditor extends javax.swing.JPanel {
    private static String EMPTY_STRING = "";  // NOI18N
    private static String SPACER = " "; // NOI18N
    private static String LINE_SEP = "\n";  // NOI18N
    
    static final long serialVersionUID =-4347656479280614636L;
    private StringArrayCustomizable editor;

    /** Initializes the Form with customized label text */
    public StringArrayCustomEditor(StringArrayCustomizable sac,
            String labelContent) {
        this(sac);
        Mnemonics.setLocalizedText(label, labelContent);        
    }

    /** Initializes the Form */
    public StringArrayCustomEditor(StringArrayCustomizable sac) {
        initComponents ();

        editor = sac;
        String[] sourceArr = editor.getStringArray();
        StringBuilder textBuffer = new StringBuilder();
        if (sourceArr != null) {
            for (int i = 0; i < sourceArr.length; i++) {
                String actValue = sourceArr[i];
                boolean lastRow = (i == (sourceArr.length - 1));
                
                // convert default " " spacer into empty line
                if (actValue != null && actValue.trim().length() == 0) {
                    actValue = EMPTY_STRING;
                }
                textBuffer.append(actValue);
                
                // not last row, add line separator
                if (!lastRow) {
                    textBuffer.append(LINE_SEP);
                }
            }
        }
        textArea.setText(textBuffer.toString());
        
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateValue();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateValue();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateValue();
            }
        });
    };

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        label = new javax.swing.JLabel();

        textArea.setColumns(20);
        textArea.setRows(5);
        scrollPane.setViewportView(textArea);
        textArea.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(StringArrayCustomEditor.class, "StringArrayCustomEditor.textArea.AccessibleContext.accessibleName")); // NOI18N
        textArea.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(StringArrayCustomEditor.class, "StringArrayCustomEditor.textArea.AccessibleContext.accessibleDescription")); // NOI18N

        label.setLabelFor(textArea);
        org.openide.awt.Mnemonics.setLocalizedText(label, org.openide.util.NbBundle.getMessage(StringArrayCustomEditor.class, "StringArrayCustomEditor.label.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );

        label.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(StringArrayCustomEditor.class, "StringArrayCustomEditor.label.AccessibleContext.accessibleName")); // NOI18N
        label.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(StringArrayCustomEditor.class, "StringArrayCustomEditor.label.AccessibleContext.accessibleDescription")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents


    public void updateValue () {
        String lastToken = EMPTY_STRING;
        ArrayList<String> list = new ArrayList<String>();
        String text = textArea.getText();
        
        // input ends with empty line > spacer added
        if (text.endsWith(LINE_SEP)) {
            text += SPACER;
        }
        
        // fill list from text area content
        // note: tokenizer returns separator as tokens, this setting doesn't
        //       ignore empty text lines at the end of text
        StringTokenizer st = new StringTokenizer(text, LINE_SEP, true);
        while (st.hasMoreTokens()) {
            String actToken = st.nextToken();
            
            if (!actToken.equals(LINE_SEP)) {
                list.add(actToken);
            } else {
                // line contains only new line char 
                // and previuos ends with new line char > spacer added
                if (lastToken.equals(LINE_SEP)) {
                    list.add(SPACER);
                }
            }
            lastToken = actToken;
        }
        
        editor.setStringArray(list.toArray(new String[list.size()]));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables

}

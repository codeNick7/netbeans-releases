/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 */
package org.netbeans.modules.java.hints.introduce;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.netbeans.modules.java.hints.introduce.IntroduceFieldPanel;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.lang.model.element.Modifier;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.openide.util.NbPreferences;

/**
 *
 * @author Jan Lahoda
 */
public class IntroduceMethodPanel extends javax.swing.JPanel {
    
    public static final int INIT_METHOD = 1;
    public static final int INIT_FIELD = 2;
    public static final int INIT_CONSTRUCTORS = 4;
    
    private static final int ACCESS_PUBLIC = 1;
    private static final int ACCESS_PROTECTED = 2;
    private static final int ACCESS_DEFAULT = 3;
    private static final int ACCESS_PRIVATE = 4;
    
    private JButton btnOk;
    
    public IntroduceMethodPanel(String name) {
        initComponents();
        
        this.name.setText(name);
        
        Preferences pref = getPreferences();
        
        int accessModifier = pref.getInt( "accessModifier", ACCESS_PRIVATE ); //NOI18N
        switch( accessModifier ) {
        case ACCESS_PUBLIC:
            accessPublic.setSelected( true );
            break;
        case ACCESS_PROTECTED:
            accessProtected.setSelected( true );
            break;
        case ACCESS_DEFAULT:
            accessDefault.setSelected( true );
            break;
        case ACCESS_PRIVATE:
            accessPrivate.setSelected( true );
            break;
        }
    }
    
    private Preferences getPreferences() {
        return NbPreferences.forModule( IntroduceFieldPanel.class ).node( "introduceField" ); //NOI18N
    }
    
    public void setOkButton( JButton btn ) {
        this.btnOk = btn;
    }
    
    private JLabel createErrorLabel() {
        ErrorLabel.Validator validator = new ErrorLabel.Validator() {

            public String validate(String text) {
                if( null == text 
                    || text.length() == 0
                    || text.indexOf( ' ' ) >= 0 )
                    return getDefaultErrorMessage( text );
                return null;
            }
        };
        
        final ErrorLabel errorLabel = new ErrorLabel( name.getDocument(), validator );
        errorLabel.addPropertyChangeListener(  ErrorLabel.PROP_IS_VALID, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                btnOk.setEnabled( errorLabel.isInputTextValid() );
            }
        });
        return errorLabel;
    }
    
    String getDefaultErrorMessage( String inputText ) {
        return "'" + inputText +"' is not a valid identifier";
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        initilizeIn = new javax.swing.ButtonGroup();
        accessGroup = new javax.swing.ButtonGroup();
        lblName = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        lblAccess = new javax.swing.JLabel();
        accessPublic = new javax.swing.JRadioButton();
        accessProtected = new javax.swing.JRadioButton();
        accessDefault = new javax.swing.JRadioButton();
        accessPrivate = new javax.swing.JRadioButton();
        errorLabel = createErrorLabel();

        org.openide.awt.Mnemonics.setLocalizedText(lblName, org.openide.util.NbBundle.getBundle(IntroduceMethodPanel.class).getString("LBL_Name")); // NOI18N

        name.setColumns(20);

        lblAccess.setLabelFor(accessPublic);
        org.openide.awt.Mnemonics.setLocalizedText(lblAccess, org.openide.util.NbBundle.getMessage(IntroduceMethodPanel.class, "LBL_Access")); // NOI18N

        accessGroup.add(accessPublic);
        org.openide.awt.Mnemonics.setLocalizedText(accessPublic, org.openide.util.NbBundle.getMessage(IntroduceMethodPanel.class, "LBL_public")); // NOI18N
        accessPublic.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accessPublic.setMargin(new java.awt.Insets(0, 0, 0, 0));

        accessGroup.add(accessProtected);
        org.openide.awt.Mnemonics.setLocalizedText(accessProtected, org.openide.util.NbBundle.getMessage(IntroduceMethodPanel.class, "LBL_protected")); // NOI18N
        accessProtected.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accessProtected.setMargin(new java.awt.Insets(0, 0, 0, 0));

        accessGroup.add(accessDefault);
        org.openide.awt.Mnemonics.setLocalizedText(accessDefault, org.openide.util.NbBundle.getMessage(IntroduceMethodPanel.class, "LBL_Default")); // NOI18N
        accessDefault.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accessDefault.setMargin(new java.awt.Insets(0, 0, 0, 0));

        accessGroup.add(accessPrivate);
        accessPrivate.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(accessPrivate, org.openide.util.NbBundle.getMessage(IntroduceMethodPanel.class, "LBL_private")); // NOI18N
        accessPrivate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        accessPrivate.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.openide.awt.Mnemonics.setLocalizedText(errorLabel, "jLabel1");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblAccess)
                            .add(lblName))
                        .add(21, 21, 21)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(name, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(accessPublic)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(accessProtected)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(accessDefault)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(accessPrivate))))
                    .add(errorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(name, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblAccess)
                    .add(accessPublic)
                    .add(accessProtected)
                    .add(accessDefault)
                    .add(accessPrivate))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 123, Short.MAX_VALUE)
                .add(errorLabel)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton accessDefault;
    private javax.swing.ButtonGroup accessGroup;
    private javax.swing.JRadioButton accessPrivate;
    private javax.swing.JRadioButton accessProtected;
    private javax.swing.JRadioButton accessPublic;
    private javax.swing.JLabel errorLabel;
    private javax.swing.ButtonGroup initilizeIn;
    private javax.swing.JLabel lblAccess;
    private javax.swing.JLabel lblName;
    private javax.swing.JTextField name;
    // End of variables declaration//GEN-END:variables
    
    public String getMethodName() {
        if (methodNameTest != null) return methodNameTest;
        return this.name.getText();
    }
    
    public Set<Modifier> getAccess() {
        if (accessTest != null) return accessTest;
        Set<Modifier> set;
        int val;
        if( accessPublic.isSelected() ) {
            val = ACCESS_PUBLIC;
            set = EnumSet.of(Modifier.PUBLIC);
        } else if( accessProtected.isSelected() ) {
            val = ACCESS_PROTECTED;
            set = EnumSet.of(Modifier.PROTECTED);
        } else if( accessDefault.isSelected() ) {
            val = ACCESS_DEFAULT;
            set = Collections.emptySet();
        } else {
            val = ACCESS_PRIVATE;
            set = EnumSet.of(Modifier.PRIVATE);
        }
        getPreferences().putInt( "accessModifier", val ); //NOI18N
        return set;
    }
    
    //For tests:
    private String methodNameTest;
    private Set<Modifier> accessTest;
    
    void setAccess(Set<Modifier> access) {
        this.accessTest = access;
    }

    void setMethodName(String methodName) {
        this.methodNameTest = methodName;
    }

}

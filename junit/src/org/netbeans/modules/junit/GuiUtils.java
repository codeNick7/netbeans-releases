/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 2004 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.junit;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import org.openide.awt.Mnemonics;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.util.NbBundle;

/**
 * Various utility method for creating and manipulating with GUI elements.
 *
 * @author  Marian Petras
 */
public final class GuiUtils {
    
    /** */
    public static final String TEMPLATES_DIR = "Templates/JUnit";       //NOI18N
    
    /** */
    public static final String CHK_PUBLIC = "Public";                   //NOI18N
    /** */
    public static final String CHK_PROTECTED = "Protected";             //NOI18N
    /** */
    public static final String CHK_PACKAGE = "Package";                 //NOI18N
    /** */
    public static final String CHK_PACKAGE_PRIVATE_CLASSES
                               = "PackagePrivateClasses";               //NOI18N
    /** */
    public static final String CHK_ABSTRACT_CLASSES
                               = "AbstractImpl";                        //NOI18N
    /** */
    public static final String CHK_EXCEPTION_CLASSES
                               = "Exceptions";                          //NOI18N
    /** */
    public static final String CHK_SUITES = "GenerateSuites";           //NOI18N
    /** */
    public static final String CHK_SETUP = "SetUp";                     //NOI18N
    /** */
    public static final String CHK_TEARDOWN = "TearDown";               //NOI18N
    /** */
    public static final String CHK_METHOD_BODIES = "Content";           //NOI18N
    /** */
    public static final String CHK_JAVADOC = "JavaDoc";                 //NOI18N
    /** */
    public static final String CHK_HINTS = "Comments";                  //NOI18N
    
    /**
     * Creates a combo-box for choosing a template.
     * The combo-box will contain <code>FileObject</code>s representing
     * available JUnit templates, wrapped using class {@link NamedObject},
     * so that the file's names are displayed in the combo-box.
     * <p>
     * To get the currently selected template from the combo-box, use:
     * <blockquote><pre>
     * NamedObject namedObject = (NamedObject) comboBox.getSelectedItem();
     * FileObject template = (FileObject) namedObject.object;
     * </pre></blockquote>
     * 
     *
     * @param  defaultTemplate  path to the default template
     * @return  non-editable combo-box displaying names of templates
     */
    public static JComboBox createTemplateChooser(String defaultTemplate) {
        FileObject templatesDir = Repository.getDefault().getDefaultFileSystem()
                                  .findResource(TEMPLATES_DIR);
        if (templatesDir == null) {
            throw new RuntimeException("Not found: " + TEMPLATES_DIR);  //NOI18N
        }
        FileObject templates[] = templatesDir.getChildren();
        
        /*
         * collect a list of templates and identify the default template
         * among them:
         */
        List itemList = new ArrayList(templates.length);
        int defaultItemIndex = -1;
        int itemIndex = 0;
        
        for (int i = 0; i < templates.length; i++) {
            FileObject template = templates[i];
            
            if (!template.getExt().equals("java")) {                    //NOI18N
                continue;
            }
            
            itemList.add(new NamedObject(template, template.getName()));
            
            if ((defaultItemIndex == -1)
                    && (defaultTemplate != null)
                    && template.getPath().equals(defaultTemplate)) {
                defaultItemIndex = itemIndex;
            }
            
            itemIndex++;
        }
        
        /* create the combo-box and select the default template: */
        JComboBox comboBox;
        if (itemList.isEmpty()) {
            comboBox = new JComboBox();
        } else {
            comboBox = new JComboBox(itemList.toArray());
            if (defaultItemIndex != -1) {
                comboBox.setSelectedIndex(defaultItemIndex);
            }
        }
        comboBox.setEditable(false);
        return comboBox;
    }
    
    /**
     * Creates a specified set of checkboxes.
     * The checkboxes are specified by unique identifiers.
     * The identifiers are given by this class's constants <code>CHK_xxx</code>.
     * <p>
     * The array of strings passed as the argument may also contain
     * <code>null</code> items. In such a case, the resulting array
     * of check-boxes will contain <code>null</code>s on the corresponding
     * possitions.
     *
     * @param  ids  identifiers of the checkboxes to be created
     * @return  array of checkboxes corresponding to the array of identifiers
     *          passed as the argument
     */
    public static JCheckBox[] createCheckBoxes(String[] ids) {
        JCheckBox[] chkBoxes = new JCheckBox[ids.length];
        
        if (chkBoxes.length == 0) {
            return chkBoxes;
        }
        
        ResourceBundle bundle = NbBundle.getBundle(JUnitCfgOfCreate.class);
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            
            if (id == null) {
                chkBoxes[i] = null;
                continue;
            }
            
            JCheckBox chkBox = new JCheckBox();
            String baseName = "JUnitCfgOfCreate.chk" + id;              //NOI18N
            AccessibleContext accessCtx = chkBox.getAccessibleContext();
            Mnemonics.setLocalizedText(
                    chkBox,
                    bundle.getString(baseName + ".text"));              //NOI18N
            chkBox.setToolTipText(
                    bundle.getString(baseName + ".toolTip"));           //NOI18N
            accessCtx.setAccessibleName(
                    bundle.getString(baseName + ".AN"));                //NOI18N
            accessCtx.setAccessibleDescription(
                    bundle.getString(baseName + ".AD"));                //NOI18N
            
            chkBoxes[i] = chkBox;
        }
        return chkBoxes;
    }
    
    /**
     * Creates a labelled group of checkboxes.
     *
     * @param  title  title for the group of checkboxes
     * @param  elements  checkboxes - members of the group
     * @return  visual component representing the group
     */
    public static JComponent createChkBoxGroup(String title,
                                               JCheckBox[] elements) {
        
        /* create a component representing the group without title: */
        JComponent content;
        if (elements.length == 1) {
            content = elements[0];
        } else {
            content = new JPanel(new GridLayout(0, 1, 0, 5));
            for (int i = 0; i < elements.length; i++) {
                content.add(elements[i]);
            }
        }
        
        /* add the title and insets to the group: */
        JPanel result = new SizeRestrictedPanel(new BorderLayout(), true, true);
        result.add(new JLabel(title), BorderLayout.NORTH);
        addBorder(content, BorderFactory.createEmptyBorder(6, 12, 0, 0));
        result.add(content, BorderLayout.CENTER);
        
        return result;
    }
    
    /**
     * Creates a text component to be used as a multi-line, automatically
     * wrapping label.
     * <p>
     * <strong>Restriction:</strong><br>
     * The component may have its preferred size very wide.
     *
     * @param  text  text of the label
     * @return  created multi-line text component
     */
    public static JComponent createMultilineLabel(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setDisabledTextColor(textArea.getForeground());
        textArea.setEnabled(false);
        textArea.setOpaque(false);
        textArea.setPreferredSize(new java.awt.Dimension(16379,0));
        return textArea;
    }
    
    /**
     * Adds a given border to a given component.
     * If the component already has some border, the given border is put
     * around the existing border.
     *
     * @param  component  component the border should be added to
     * @param  border  the border to be added
     */
    private static void addBorder(JComponent component,
                                  Border newBorder) {
        Border currentBorder = component.getBorder();
        if (currentBorder == null) {
            component.setBorder(newBorder);
        } else {
            component.setBorder(BorderFactory.createCompoundBorder(
                    newBorder,          //outside
                    currentBorder));    //inside
        }
    }
    
}

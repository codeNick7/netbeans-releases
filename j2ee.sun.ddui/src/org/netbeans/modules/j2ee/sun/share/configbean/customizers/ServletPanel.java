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
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.modules.j2ee.sun.share.configbean.customizers;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.netbeans.modules.j2ee.sun.dd.api.ASDDVersion;
import org.netbeans.modules.j2ee.sun.dd.api.CommonDDBean;
import org.netbeans.modules.j2ee.sun.dd.api.VersionNotSupportedException;
import org.netbeans.modules.j2ee.sun.dd.api.web.Servlet;
import org.netbeans.modules.j2ee.sun.ddloaders.SunDescriptorDataObject;
import org.netbeans.modules.j2ee.sun.ddloaders.Utils;
import org.netbeans.modules.j2ee.sun.ddloaders.multiview.DDTextFieldEditorModel;
import org.netbeans.modules.j2ee.sun.ddloaders.multiview.common.DDBinding;
import org.netbeans.modules.j2ee.sun.ddloaders.multiview.web.ServletNode;
import org.netbeans.modules.xml.multiview.ItemEditorHelper;
import org.netbeans.modules.xml.multiview.XmlMultiViewDataSynchronizer;
import org.netbeans.modules.xml.multiview.ui.SectionNodeInnerPanel;
import org.netbeans.modules.xml.multiview.ui.SectionNodeView;
import org.openide.util.Utilities;


/**
 *
 * @author Peter Williams
 */
public class ServletPanel extends SectionNodeInnerPanel {
	
	private static final ResourceBundle customizerBundle = ResourceBundle.getBundle(
		"org.netbeans.modules.j2ee.sun.share.configbean.customizers.Bundle");	// NOI18N
    
    public static final String ATTR_CLASSNAME = "ClassName";

    // data model & version
    private ServletNode servletNode;
    private ASDDVersion version;
    
    // true if standard DD is servlet version 2.4 or newer
	private boolean servlet24FeaturesVisible;

    // true if AS 9.0+ fields are visible.
    private boolean as90FeaturesVisible;
    
	public ServletPanel(SectionNodeView sectionNodeView, final ServletNode servletNode, final ASDDVersion version) {
        super(sectionNodeView);
        this.servletNode = servletNode;
        this.version = version;
        this.servlet24FeaturesVisible = true;
        this.as90FeaturesVisible = true;
        
		initComponents();
		initUserComponents(sectionNodeView);
	}

	private void initUserComponents(SectionNodeView sectionNodeView) {
        if(ASDDVersion.SUN_APPSERVER_9_0.compareTo(version) <= 0) {
            showAS90Fields();
        } else {
            hideAS90Fields();
        }

//		if(theBean.getJ2EEModuleVersion().compareTo(ServletVersion.SERVLET_2_4) >= 0) {
//			showWebServiceEndpointInformation();
//		} else {
			hideWebServiceEndpointInformation();
//		}
        
        SunDescriptorDataObject dataObject = (SunDescriptorDataObject) sectionNodeView.getDataObject();
        XmlMultiViewDataSynchronizer synchronizer = dataObject.getModelSynchronizer();
        addRefreshable(new ItemEditorHelper(jTxtName, new ServletTextFieldEditorModel(synchronizer, Servlet.SERVLET_NAME)));
        addRefreshable(new ItemEditorHelper(jTxtPrincipalName, new ServletTextFieldEditorModel(synchronizer, Servlet.PRINCIPAL_NAME)));
        addRefreshable(new ItemEditorHelper(jTxtClassName, new ServletTextFieldEditorModel(synchronizer, Servlet.PRINCIPAL_NAME, ATTR_CLASSNAME)));

        handleRoleFields(servletNode.getBinding());

        updateOrigin();
	}
    
    private void updateOrigin() {
        DDBinding binding = servletNode.getBinding();
        String origin = (binding.getAnnotatedBean() != null) ? "Annotation" : 
                (binding.getStandardBean() != null ? "Standard DD" : "Unbound");
        if(binding.isVirtual()) {
            origin += " (virtual)";
        }
        jTxtOrigin.setText(origin);
        
        Image originImage = getOriginImage(binding);
        jLblOriginImage.setIcon(new ImageIcon(originImage));
    }
    
    public static final String UNBOUND_ICON = "org/netbeans/modules/j2ee/sun/ddloaders/resources/UnBoundDD.gif";
    public static final String BOUND_STANDARD_DD_ICON = "org/netbeans/modules/j2ee/sun/ddloaders/resources/BoundStandardDD.gif";
    public static final String BOUND_ANNOTATION_ICON = "org/netbeans/modules/j2ee/sun/ddloaders/resources/BoundAnnotation.gif";
    
    public Image getOriginImage(DDBinding binding) {
        String iconRef = binding.isBound() ? (binding.isAnnotated() ? BOUND_ANNOTATION_ICON : BOUND_STANDARD_DD_ICON) : UNBOUND_ICON;
        return Utilities.loadImage(iconRef);
    }

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPnlServlet = new javax.swing.JPanel();
        jLblOriginImage = new javax.swing.JLabel();
        jLblName = new javax.swing.JLabel();
        jTxtName = new javax.swing.JTextField();
        jLblOrigin = new javax.swing.JLabel();
        jTxtOrigin = new javax.swing.JLabel();
        jLblRoleUsageDescription = new javax.swing.JLabel();
        jLblRunAsRoleName = new javax.swing.JLabel();
        jTxtRunAsRoleName = new javax.swing.JTextField();
        jLblPrincipalName = new javax.swing.JLabel();
        jTxtPrincipalName = new javax.swing.JTextField();
        jLblClassNameUsageDesc = new javax.swing.JLabel();
        jLblClassName = new javax.swing.JLabel();
        jTxtClassName = new javax.swing.JTextField();
        jLblEndpointHelp = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jPnlServlet.setOpaque(false);
        jPnlServlet.setLayout(new java.awt.GridBagLayout());

        jLblOriginImage.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPnlServlet.add(jLblOriginImage, gridBagConstraints);

        jLblName.setLabelFor(jTxtName);
        jLblName.setText(customizerBundle.getString("LBL_ServletName_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPnlServlet.add(jLblName, gridBagConstraints);

        jTxtName.setText("asdfasf");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPnlServlet.add(jTxtName, gridBagConstraints);
        jTxtName.getAccessibleContext().setAccessibleName(customizerBundle.getString("ACSN_ServletName")); // NOI18N
        jTxtName.getAccessibleContext().setAccessibleDescription(customizerBundle.getString("ACSD_ServletName")); // NOI18N

        jLblOrigin.setText("Origin:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 0);
        jPnlServlet.add(jLblOrigin, gridBagConstraints);

        jTxtOrigin.setText("(unknown)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPnlServlet.add(jTxtOrigin, gridBagConstraints);

        jLblRoleUsageDescription.setLabelFor(jTxtPrincipalName);
        jLblRoleUsageDescription.setText(customizerBundle.getString("LBL_ServletRunAsDescription")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        jPnlServlet.add(jLblRoleUsageDescription, gridBagConstraints);

        jLblRunAsRoleName.setLabelFor(jTxtRunAsRoleName);
        jLblRunAsRoleName.setText(customizerBundle.getString("LBL_RunAsRole_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPnlServlet.add(jLblRunAsRoleName, gridBagConstraints);

        jTxtRunAsRoleName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPnlServlet.add(jTxtRunAsRoleName, gridBagConstraints);
        jTxtRunAsRoleName.getAccessibleContext().setAccessibleName(customizerBundle.getString("ACSN_RunAsRole")); // NOI18N
        jTxtRunAsRoleName.getAccessibleContext().setAccessibleDescription(customizerBundle.getString("ACSD_RunAsRole")); // NOI18N

        jLblPrincipalName.setLabelFor(jTxtPrincipalName);
        jLblPrincipalName.setText(customizerBundle.getString("LBL_PrincipalName_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPnlServlet.add(jLblPrincipalName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPnlServlet.add(jTxtPrincipalName, gridBagConstraints);
        jTxtPrincipalName.getAccessibleContext().setAccessibleName(customizerBundle.getString("ACSN_PrincipalName")); // NOI18N
        jTxtPrincipalName.getAccessibleContext().setAccessibleDescription(customizerBundle.getString("ACSD_PrincipalName")); // NOI18N

        jLblClassNameUsageDesc.setLabelFor(jTxtClassName);
        jLblClassNameUsageDesc.setText(customizerBundle.getString("LBL_PrincipalClassNameDesc")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        jPnlServlet.add(jLblClassNameUsageDesc, gridBagConstraints);

        jLblClassName.setLabelFor(jTxtClassName);
        jLblClassName.setText(customizerBundle.getString("LBL_ClassName_1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPnlServlet.add(jLblClassName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPnlServlet.add(jTxtClassName, gridBagConstraints);
        jTxtClassName.getAccessibleContext().setAccessibleName(customizerBundle.getString("ACSN_ClassName")); // NOI18N
        jTxtClassName.getAccessibleContext().setAccessibleDescription(customizerBundle.getString("ACSD_ClassName")); // NOI18N

        jLblEndpointHelp.setText(customizerBundle.getString("LBL_EndpointHelp")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        jPnlServlet.add(jLblEndpointHelp, gridBagConstraints);
        jLblEndpointHelp.getAccessibleContext().setAccessibleName(customizerBundle.getString("ACSN_EndpointHelp")); // NOI18N
        jLblEndpointHelp.getAccessibleContext().setAccessibleDescription(customizerBundle.getString("ACSD_EndpointHelp")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 5, 5);
        add(jPnlServlet, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLblClassName;
    private javax.swing.JLabel jLblClassNameUsageDesc;
    private javax.swing.JLabel jLblEndpointHelp;
    private javax.swing.JLabel jLblName;
    private javax.swing.JLabel jLblOrigin;
    private javax.swing.JLabel jLblOriginImage;
    private javax.swing.JLabel jLblPrincipalName;
    private javax.swing.JLabel jLblRoleUsageDescription;
    private javax.swing.JLabel jLblRunAsRoleName;
    private javax.swing.JPanel jPnlServlet;
    private javax.swing.JTextField jTxtClassName;
    private javax.swing.JTextField jTxtName;
    private javax.swing.JLabel jTxtOrigin;
    private javax.swing.JTextField jTxtPrincipalName;
    private javax.swing.JTextField jTxtRunAsRoleName;
    // End of variables declaration//GEN-END:variables

//	protected void initFields() {
//		jTxtName.setText(theBean.getServletName());
//
//        if(ASDDVersion.SUN_APPSERVER_9_0.compareTo(theBean.getAppServerVersion()) <= 0) {
//            showAS90Fields();
//        } else {
//            hideAS90Fields();
//        }
//        
//        handleRoleFields();
//		
//		if(theBean.getJ2EEModuleVersion().compareTo(ServletVersion.SERVLET_2_4) >= 0) {
//			showWebServiceEndpointInformation();
//		} else {
//			hideWebServiceEndpointInformation();
//		}
//	}

    private void handleRoleFields(final DDBinding binding) {
        org.netbeans.modules.j2ee.dd.api.web.Servlet ddServlet = (org.netbeans.modules.j2ee.dd.api.web.Servlet) binding.getStandardBean();
        org.netbeans.modules.j2ee.dd.api.common.RunAs ddRunAs = (ddServlet != null) ? ddServlet.getRunAs() : null;
        String runAsRole = (ddRunAs != null) ? ddRunAs.getRoleName() : null;

        Servlet sunServlet = (Servlet) binding.getSunBean();
        String principalName = sunServlet.getPrincipalName();
        principalName = (principalName != null) ? principalName.trim() : null;
        String principalClassName = null;
        
        try {
            principalClassName = sunServlet.getPrincipalNameClassName();
            principalClassName = (principalClassName != null) ? principalClassName.trim() : null;
        } catch(VersionNotSupportedException ex) {
        }

        // Disable the role specific fields for bound servlets that have not 
        // specified run-as.  Unbound servlets can edit here because we can't
        // tell for sure (because they're unbound!)
        //
        enableRoleFields(!binding.isBound() || Utils.notEmpty(runAsRole), 
                runAsRole, principalName, principalClassName);
//        if(Utils.notEmpty(runAsRole)) {
//            enableRoleFields(true, runAsRole, principalName, principalClassName);
//        } else {
//            enableRoleFields(false, "", "", "");
//        }
    }

    private void enableRoleFields(boolean enabled, String runAs, String pn, String cn) {
        jLblRunAsRoleName.setEnabled(enabled);
        jTxtRunAsRoleName.setText(runAs);
        jLblPrincipalName.setEnabled(enabled);
        jTxtPrincipalName.setEditable(enabled);
        jTxtPrincipalName.setEnabled(enabled);
        jTxtPrincipalName.setText(pn);
        jLblClassName.setEnabled(enabled);
        jTxtClassName.setEditable(enabled);
        jTxtClassName.setEnabled(enabled);
        jTxtClassName.setText(cn);
    }

    private void showWebServiceEndpointInformation() {
        if(!servlet24FeaturesVisible) {
            jLblEndpointHelp.setVisible(true);
            servlet24FeaturesVisible = true;
        }
    }

    private void hideWebServiceEndpointInformation() {
        if(servlet24FeaturesVisible) {
            jLblEndpointHelp.setVisible(false);
            servlet24FeaturesVisible = false;
        }
    }

    private void showAS90Fields() {
        if(!as90FeaturesVisible) {
            jLblClassNameUsageDesc.setVisible(true);
            jLblClassName.setVisible(true);
            jTxtClassName.setVisible(true);
            as90FeaturesVisible = true;
        }
    }

    private void hideAS90Fields() {
        if(as90FeaturesVisible) {
            jLblClassNameUsageDesc.setVisible(false);
            jLblClassName.setVisible(false);
            jTxtClassName.setVisible(false);
            as90FeaturesVisible = false;
        }
    }

//	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
//		String eventName = propertyChangeEvent.getPropertyName();
//		
//		if(ServletRef.SERVLET_NAME.equals(eventName)) {
//			jTxtName.setText(theBean.getServletName());
//		} else if(ServletRef.RUN_AS_ROLE_NAME.equals(eventName)) {
//			handleRoleFields();
//		}
//	}
	
	public String getHelpId() {
		return "AS_CFG_Servlet";	// NOI18N
	}
    
    public void setValue(JComponent source, Object value) {
    }

    public void linkButtonPressed(Object ddBean, String ddProperty) {
    }

    public JComponent getErrorComponent(String errorId) {
        return null;
    }
    
    /** Return correct preferred size.  The multiline JLabels in this panel cause
     *  the default preferred size behavior to be incorrect (too wide).
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getMinimumSize().width, super.getPreferredSize().height);
    }
    
    // Model class for handling updates to the text fields
    private class ServletTextFieldEditorModel extends DDTextFieldEditorModel {

        public ServletTextFieldEditorModel(XmlMultiViewDataSynchronizer synchronizer, String propertyName) {
            super(synchronizer, propertyName);
        }
        
        public ServletTextFieldEditorModel(XmlMultiViewDataSynchronizer synchronizer, String propertyName, String attributeName) {
            super(synchronizer, propertyName, attributeName);
        }

        protected CommonDDBean getBean() {
            return servletNode.getBinding().getSunBean();
        }
        
        @Override
        protected void setValue(String value) {
            super.setValue(value);

            // If this was a virtual bean, commit it to the graph.
            if(servletNode.addVirtualBean()) {
                updateOrigin();
            }
        }
        
    }
    
}

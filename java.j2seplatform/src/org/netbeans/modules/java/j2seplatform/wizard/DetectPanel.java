/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2002 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.java.j2seplatform.wizard;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.java.classpath.ClassPath;

import org.openide.filesystems.*;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.HelpCtx;

import org.netbeans.api.java.platform.JavaPlatform;
import org.netbeans.api.java.platform.JavaPlatformManager;
import org.netbeans.modules.java.j2seplatform.platformdefinition.J2SEPlatformImpl;
import org.netbeans.modules.java.j2seplatform.platformdefinition.Util;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.ErrorManager;
import org.openide.WizardDescriptor;

/**
 * This Panel launches autoconfiguration during the New J2SE Platform sequence.
 * The UI views properties of the platform, reacts to the end of detection by
 * updating itself. It triggers the detection task when the button is pressed.
 * The inner class WizardPanel acts as a controller, reacts to the UI completness
 * (jdk name filled in) and autoconfig result (passed successfully) - and manages
 * Next/Finish button (valid state) according to those.
 *
 * @author Svata Dedic
 */
public class DetectPanel extends javax.swing.JPanel {

    private JavaPlatform platform;
    private ArrayList listeners;

    /**
     * Creates a detect panel
     * start the task and update on its completion
     * @param p the platform being customized.
     */
    public DetectPanel(JavaPlatform p) {
        initComponents();
        postInitComponents ();
        putClientProperty("WizardPanel_contentData",
            new String[] {
                NbBundle.getMessage(DetectPanel.class,"TITLE_PlatformName"),
        });
        this.platform = p;
        this.setName (NbBundle.getMessage(DetectPanel.class,"TITLE_PlatformName"));
    }

    public void addNotify() {
        super.addNotify();        
    }    

    private void postInitComponents () {
        this.jdkName.getDocument().addDocumentListener (new DocumentListener () {

            public void insertUpdate(DocumentEvent e) {
                handleNameChange ();
            }

            public void removeUpdate(DocumentEvent e) {
                handleNameChange ();
            }

            public void changedUpdate(DocumentEvent e) {
                handleNameChange ();
            }
        });       
    }

    private void handleNameChange () {
        this.fireChange();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel3 = new javax.swing.JLabel();
        jdkName = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        javadoc = new javax.swing.JTextField();
        sources = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("AD_DetectPanel"));
        jLabel3.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("MNE_PlatformName").charAt(0));
        jLabel3.setText(NbBundle.getBundle(DetectPanel.class).getString("LBL_DetailsPanel_Name"));
        jLabel3.setLabelFor(jdkName);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        add(jdkName, gridBagConstraints);
        jdkName.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("AD_PlatformName"));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("MNE_PlatformSources").charAt(0));
        jLabel1.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("TXT_Sources"));
        jLabel1.setLabelFor(sources);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel4.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("MNE_PlatformJavadoc").charAt(0));
        jLabel4.setText(NbBundle.getBundle(DetectPanel.class).getString("TXT_JavaDoc"));
        jLabel4.setLabelFor(javadoc);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 0);
        jPanel1.add(javadoc, gridBagConstraints);
        javadoc.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("AD_PlatformJavadoc"));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel1.add(sources, gridBagConstraints);
        sources.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("AD_PlatformSources"));

        jButton1.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("LBL_Browse"));
        jButton1.setMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("MNE_BrowseSources").charAt(0));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectSources(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel1.add(jButton1, gridBagConstraints);
        jButton1.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("AD_SelectSources"));

        jButton2.setText(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("LBL_Browse"));
        jButton2.setMnemonic(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("MNE_BrowseJavadoc").charAt(0));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectJavadoc(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 0);
        jPanel1.add(jButton2, gridBagConstraints);
        jButton2.getAccessibleContext().setAccessibleDescription(java.util.ResourceBundle.getBundle("org/netbeans/modules/java/j2seplatform/wizard/Bundle").getString("AD_SelectJavadoc"));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(24, 0, 0, 0);
        add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);

    }//GEN-END:initComponents

    private void selectJavadoc(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectJavadoc
        String newValue = this.browse(this.javadoc.getText(),NbBundle.getMessage(DetectPanel.class,"TXT_SelectJavadoc"));
        if (newValue != null) {
            this.javadoc.setText(newValue);
        }
        
    }//GEN-LAST:event_selectJavadoc

    private void selectSources(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectSources
        String newValue = this.browse(this.sources.getText(),NbBundle.getMessage(DetectPanel.class,"TXT_SelectSources"));
        if (newValue != null) {
            this.sources.setText(newValue);
        }
    }//GEN-LAST:event_selectSources
    
    public final synchronized void addChangeListener (ChangeListener listener) {
        if (this.listeners == null)
            this.listeners = new ArrayList ();
        this.listeners.add (listener);
    }

    public final synchronized void removeChangeListener (ChangeListener listener) {
        if (this.listeners == null)
            return;
        this.listeners.remove (listener);
    }

    public String getPlatformName() {
	    return jdkName.getText();
    }
    
    String getSources () {
        String val = this.sources.getText();
        return val.length() == 0 ? null : val;
    }

    void setSources (String sources) {
        this.sources.setText (sources == null ? "" : sources);      //NOI18N
    }

    String getJavadoc () {
        String val = this.javadoc.getText();
        return val.length() == 0 ? null : val;
    }

    void setJavadoc (String jdoc) {
        this.javadoc.setText(jdoc == null ? "" : jdoc);             //NOI18N
    }

    protected final void fireChange () {
        Iterator it = null;
        synchronized (this) {
            if (this.listeners == null)
                return;
            it = ((ArrayList)this.listeners.clone()).iterator();
        }
        ChangeEvent event = new ChangeEvent (this);
        while (it.hasNext()) {
            ((ChangeListener)it.next()).stateChanged(event);
        }
    }

    /**
     * Updates static information from the detected platform's properties
     */
    void updateData() {
        Map m = platform.getSystemProperties();        
        // if the name is empty, fill something in:
        if ("".equals(jdkName.getText())) {
            jdkName.setText(getInitialName (m));
            this.jdkName.selectAll();
        }
    }


    private static String getInitialName (Map m) {
        String vmName = (String)m.get("java.vm.name");              //NOI18N
        String vmVersion = (String)m.get("java.vm.version");        //NOI18N
        StringBuffer result = new StringBuffer();
        if (vmName != null)
            result.append(vmName);
        if (vmVersion != null) {
            if (result.length()>0) {
                result.append (" ");
            }
            result.append (vmVersion);
        }
        return result.toString();
    }
    
    
    private String browse (String oldValue, String title) {
        JFileChooser chooser = new JFileChooser ();
        FileUtil.preventFileChooserSymlinkTraversal(chooser, null);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setFileFilter (new FileFilter () {
            public boolean accept(File f) {
                return (f.exists() && f.canRead() && (f.isDirectory() || (f.getName().endsWith(".zip") || f.getName().endsWith(".jar"))));
            }

            public String getDescription() {
                return NbBundle.getMessage(DetectPanel.class,"TXT_ZipFilter");
            }
        });
        File f = new File (oldValue);
        chooser.setSelectedFile(f);
        chooser.setDialogTitle (title);
        if (chooser.showOpenDialog (this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField javadoc;
    private javax.swing.JTextField jdkName;
    private javax.swing.JTextField sources;
    // End of variables declaration//GEN-END:variables

    /**
     * Controller for the outer class: manages wizard panel's valid state
     * according to the user's input and detection state.
     */
    static class WizardPanel implements WizardDescriptor.Panel, TaskListener, ChangeListener {
        private DetectPanel         component;
        private RequestProcessor.Task task;
        private final J2SEWizardIterator  iterator;
        private Collection          changeList = new ArrayList();
        private boolean             detected;
	private J2SEPlatformImpl    platform;
        private boolean             valid;
        private boolean             firstPass=true;
        private WizardDescriptor    wiz;

        WizardPanel(J2SEWizardIterator iterator) {
            this.iterator = iterator;
            JavaPlatform platform = iterator.getPlatform();
            assert platform instanceof J2SEPlatformImpl;
	        this.platform = (J2SEPlatformImpl) iterator.getPlatform();
        }

        public void addChangeListener(ChangeListener l) {
            changeList.add(l);
        }

        public java.awt.Component getComponent() {
            if (component == null) {
                component = new DetectPanel(iterator.getPlatform());
                component.addChangeListener (this);
                task = RequestProcessor.getDefault().create(iterator);
                task.addTaskListener(this);
            }
            return component;
        }

        void setValid(boolean v) {
            if (v == valid)
                return;
            valid = v;
            fireStateChange();
        }

        public HelpCtx getHelp() {
            return HelpCtx.DEFAULT_HELP;
        }

        public boolean isValid() {
            return valid;
        }

        public void readSettings(Object settings) {           
            this.wiz = (WizardDescriptor) settings;
            JavaPlatform platform = this.iterator.getPlatform();
            String srcPath = null;
            String jdocPath = null;
            ClassPath src = platform.getSourceFolders();
            if (src.entries().size()>0) {
                URL folderRoot = ((ClassPath.Entry)src.entries().get(0)).getURL();
                if ("jar".equals(folderRoot.getProtocol())) {   //NOI18N
                    folderRoot = FileUtil.getArchiveFile (folderRoot);
                }
                srcPath = new File(URI.create(folderRoot.toExternalForm())).getAbsolutePath();
            }
            else if (firstPass) {
                Iterator il = platform.getInstallFolders().iterator();
                if (il.hasNext()) {
                    File base = FileUtil.toFile ((FileObject)il.next());
                    if (base!=null) {
                        File f = new File (base,"src.zip"); //NOI18N
                        if (f.canRead()) {
                            srcPath = f.getAbsolutePath();
                        }
                        else {
                            f = new File (base,"src.jar");  //NOI18N
                            if (f.canRead()) {
                                srcPath = f.getAbsolutePath();
                            }
                        }
                    }
                }                
            }
            List jdoc = platform.getJavadocFolders();
            if (jdoc.size()>0) {
                URL folderRoot = (URL)jdoc.get(0);
                if ("jar".equals(folderRoot.getProtocol())) {
                    folderRoot = FileUtil.getArchiveFile (folderRoot);
                }
                jdocPath = new File (URI.create(folderRoot.toExternalForm())).getAbsolutePath();
            }
            else if (firstPass) {
                Iterator il = platform.getInstallFolders().iterator();
                if (il.hasNext()) {
                    File base = FileUtil.toFile ((FileObject)il.next());
                    if (base!=null) {
                        File f = new File (base,"docs"); //NOI18N
                        if (f.isDirectory() && f.canRead()) {
                            jdocPath = f.getAbsolutePath();
                        }                        
                    }
                }
                firstPass = false;
            }
            this.component.setSources (srcPath);
            this.component.setJavadoc (jdocPath);
            this.component.jdkName.setEditable(false);
            task.schedule(0);
        }

        void fireStateChange() {
            ChangeListener[] ll;
            synchronized (this) {
                if (changeList.isEmpty())
                    return;
                ll = (ChangeListener[])changeList.toArray(new ChangeListener[0]);
            }
            ChangeEvent ev = new ChangeEvent(this);
            for (int i = 0; i < ll.length; i++)
                ll[i].stateChanged(ev);
        }

        public void removeChangeListener(ChangeListener l) {
            changeList.remove(l);
        }

	/**
	 Updates the Platform's display name with the one the user
	 has entered. Stores user-customized display name into the Platform.
	 */
        public void storeSettings(Object settings) {
            if (isValid()) {
                assert platform instanceof J2SEPlatformImpl;
                String name = component.getPlatformName();
                platform.setDisplayName (name);
                String antName = createAntName (name);
                platform.setAntName (antName);
                List src = new ArrayList ();
                List jdoc = new ArrayList ();
                String srcPath = this.component.getSources();
                if (srcPath!=null) {
                    File f = new File (srcPath);
                    try {
                        URL url = f.toURI().toURL();
                        if (FileUtil.isArchiveFile(url)) {
                            url = FileUtil.getArchiveRoot(url);
                            FileObject fo = URLMapper.findFileObject(url);
                            if (fo != null) {
                                fo = fo.getFileObject("src");   //NOI18N
                                if (fo != null) {
                                    url = fo.getURL();
                                }
                            }
                            src.add (ClassPathSupport.createResource(url));
                        }
                        else {
                            src.add (ClassPathSupport.createResource(url));
                        }
                    } catch (MalformedURLException mue) {
                        ErrorManager.getDefault().notify (mue);
                    }
                    catch (FileStateInvalidException e) {
                        ErrorManager.getDefault().notify(e);
                    }
                }
                String jdocPath = this.component.getJavadoc();
                if (jdocPath!=null) {
                    File f = new File (jdocPath);
                    try {
                        URL url = f.toURI().toURL();
                        if (FileUtil.isArchiveFile(url)) {
                            jdoc.add (FileUtil.getArchiveRoot(url));
                        }
                        else {
                            jdoc.add (url);
                        }
                    } catch (MalformedURLException mue) {
                        ErrorManager.getDefault().notify (mue);
                    }
                }
                ((J2SEPlatformImpl)platform).setSourceFolders (ClassPathSupport.createClassPath(src));
                ((J2SEPlatformImpl)platform).setJavadocFolders (jdoc);
            }
        }

        /**
         * Revalidates the Wizard Panel
         */
        public void taskFinished(Task task) {
            SwingUtilities.invokeLater( new Runnable () {
                public void run () {
                    component.updateData ();
                    component.jdkName.setEditable(true);
                    detected = iterator.isValid();
                    checkValid ();
                }
            });            
        }


        public void stateChanged(ChangeEvent e) {
             this.checkValid();
        }

        private void checkValid () {
            String name = this.component.getPlatformName ();            
            boolean validDisplayName = name.length() > 0;
            if (!detected) {
                this.wiz.putProperty( "WizardPanel_errorMessage",NbBundle.getMessage(DetectPanel.class,"ERROR_NoSDKRegistry"));         //NOI18N
            }
            else if (!validDisplayName) {
                this.wiz.putProperty( "WizardPanel_errorMessage",NbBundle.getMessage(DetectPanel.class,"ERROR_InvalidDisplayName"));    //NOI18N
            }
            else {
                this.wiz.putProperty( "WizardPanel_errorMessage", "");                                                                  //NOI18N
            }
            boolean v = detected && validDisplayName;
            setValid(v);            
        }

        private static String createAntName (String name) {
            StringBuffer baseName = new StringBuffer ();
            if (name == null || name.length() == 0) {
                throw new IllegalArgumentException ();
            }
            
            if (!Character.isJavaIdentifierStart (name.charAt(0))) {
                baseName.append('_');
            }
            for (int i=0; i< name.length(); i++) {
                char c = name.charAt(i);
                if (!Character.isJavaIdentifierPart(c) && c !='-' && c!='.') {
                    c = '_';
                }
                baseName.append (c);
            }
            String antName = baseName.toString();
            if (platformExists (antName)) {
                int index = 1;
                antName = baseName.toString () + Integer.toString (index);
                while (platformExists (antName)) {
                    index ++;
                    antName = baseName.toString () + Integer.toString (index);
                }
            }
            return antName;
        }
        
        private static boolean platformExists (String antName) {
            JavaPlatformManager mgr = JavaPlatformManager.getDefault();
            JavaPlatform[] platforms = mgr.getInstalledPlatforms();
            for (int i=0; i < platforms.length; i++) {
                if (platforms[i] instanceof J2SEPlatformImpl) {
                    String val = ((J2SEPlatformImpl)platforms[i]).getAntName();
                    if (antName.equals(val)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
    }    
}

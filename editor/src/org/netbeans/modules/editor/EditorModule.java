/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2003 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
    

package org.netbeans.modules.editor;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.text.EditorKit;

import org.netbeans.editor.AnnotationType;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.DialogSupport;
import org.netbeans.editor.ext.java.JavaSettingsNames;
import org.netbeans.editor.LocaleSupport;
import org.netbeans.modules.editor.options.AllOptions;
import org.netbeans.modules.editor.options.AnnotationTypesFolder;
import org.netbeans.modules.editor.options.JavaPrintOptions;
import org.netbeans.modules.editor.options.HTMLPrintOptions;
import org.netbeans.modules.editor.options.PlainPrintOptions;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.RepositoryListener;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.modules.ModuleInstall;
import org.openide.nodes.Node;
import org.openide.options.SystemOption;
import org.openide.text.PrintSettings;
import org.openide.util.RequestProcessor;
import org.openide.util.SharedClassObject;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.editor.AnnotationTypes;
import org.netbeans.modules.editor.options.BaseOptions;
import org.netbeans.editor.ImplementationProvider;
import org.netbeans.modules.editor.NbImplementationProvider;
import java.util.Iterator;
import org.openide.text.CloneableEditor;
import java.util.HashSet;
import javax.swing.SwingUtilities;
import org.netbeans.modules.editor.java.JCStorage;
import org.netbeans.modules.editor.options.BasePrintOptions;
import org.openide.loaders.OperationEvent;
import org.openide.loaders.OperationListener;
import org.openide.loaders.OperationAdapter;
import org.openide.cookies.SourceCookie;
import org.netbeans.modules.editor.java.JCUpdater;
import org.netbeans.modules.editor.java.JavaKit;
import org.netbeans.modules.editor.java.ParserThread;
import org.netbeans.modules.editor.options.AllOptionsFolder;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.RepositoryEvent;
import org.openide.filesystems.RepositoryReorderedEvent;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataLoaderPool;
import org.openide.util.Lookup;


/**
 * Module installation class for editor.
 *
 * @author Miloslav Metelka
 */

public class EditorModule extends ModuleInstall {

    private static final boolean debug = Boolean.getBoolean("netbeans.debug.editor.kits");
    private RequestProcessor ccUpdateProcessor;
    private RepositOperations operationListener;


    /** PrintOptions to be installed */
    static Class[] printOpts = new Class[] {
        PlainPrintOptions.class,
        JavaPrintOptions.class,
        HTMLPrintOptions.class
    };
    
    static boolean inited = false;

    
    public static void init(){
        if (inited) return;

        inited = true; // moved here to fix #27418
        
        NbEditorSettingsInitializer.init();
        PrintSettings ps = (PrintSettings) SharedClassObject.findObject(PrintSettings.class, true);

        // Start listening on addition/removal of print options
        BasePrintOptions bpo = (BasePrintOptions) BasePrintOptions.findObject(BasePrintOptions.class, true);
        bpo.init();
        
        for (int i = 0; i < printOpts.length; i++) {
            ps.addOption((SystemOption)SharedClassObject.findObject(printOpts[i], true));
        }
    }
    
    /** Module installed again. */
    public void restored () {
        LocaleSupport.addLocalizer(new NbLocalizer(AllOptions.class));
        LocaleSupport.addLocalizer(new NbLocalizer(BaseKit.class));
        LocaleSupport.addLocalizer(new NbLocalizer(JavaSettingsNames.class));

        // Initializations
        DialogSupport.setDialogFactory( new NbDialogSupport() );
        
        ImplementationProvider.registerDefault(new NbImplementationProvider());
        
        // register loader for annotation types
        AnnotationTypes.getTypes().registerLoader( new AnnotationTypes.Loader() {
                public void loadTypes() {
                    AnnotationTypesFolder.getAnnotationTypesFolder();
                }
                public void loadSettings() {
                    // AnnotationType properties are stored in BaseOption, so let's read them now
                    BaseOptions bo = (BaseOptions)BaseOptions.findObject(BaseOptions.class, true);

                    Integer i = (Integer)bo.getSettingValue(AnnotationTypes.PROP_BACKGROUND_GLYPH_ALPHA);
                    if (i != null)
                        AnnotationTypes.getTypes().setBackgroundGlyphAlpha(i.intValue());
                    Boolean b = (Boolean)bo.getSettingValue(AnnotationTypes.PROP_BACKGROUND_DRAWING);
                    if (b != null)
                        AnnotationTypes.getTypes().setBackgroundDrawing(b);
                    b = (Boolean)bo.getSettingValue(AnnotationTypes.PROP_COMBINE_GLYPHS);
                    if (b != null)
                        AnnotationTypes.getTypes().setCombineGlyphs(b);
                    b = (Boolean)bo.getSettingValue(AnnotationTypes.PROP_GLYPHS_OVER_LINE_NUMBERS);
                    if (b != null)
                        AnnotationTypes.getTypes().setGlyphsOverLineNumbers(b);
                    b = (Boolean)bo.getSettingValue(AnnotationTypes.PROP_SHOW_GLYPH_GUTTER);
                    if (b != null)
                        AnnotationTypes.getTypes().setShowGlyphGutter(b);
                }
                public void saveType(AnnotationType type) {
                    AnnotationTypesFolder.getAnnotationTypesFolder().saveAnnotationType(type);
                }
                public void saveSetting(String settingName, Object value) {
                    // AnnotationType properties are stored to BaseOption
                    BaseOptions bo = (BaseOptions)BaseOptions.findObject(BaseOptions.class, true);
                    bo.setSettingValue(settingName, value);
                }
            } );

        // Settings
        //NbEditorSettingsInitializer.init(); moving to NbEditorKit in accordance with the bug #21976


	// defer the rest of initialization, but enable a bit of paralelism
//        org.openide.util.RequestProcessor.postRequest (this, 0, Thread.MIN_PRIORITY);

        // Options
            /*
        PrintSettings ps = (PrintSettings) SharedClassObject.findObject(PrintSettings.class, true);

        // Start listening on addition/removal of print options
        BasePrintOptions bpo = (BasePrintOptions) BasePrintOptions.findObject(BasePrintOptions.class, true);
        bpo.init();
        
        for (int i = 0; i < printOpts.length; i++) {
            ps.addOption((SystemOption)SharedClassObject.findObject(printOpts[i], true));
        }
*/
        // Autoregistration
        try {
            Field keyField = JEditorPane.class.getDeclaredField("kitRegistryKey");  // NOI18N
            keyField.setAccessible(true);
            Object key = keyField.get(JEditorPane.class);
            Hashtable kitMapping = (Hashtable)sun.awt.AppContext.getAppContext().get(key);
            sun.awt.AppContext.getAppContext().put(key, new HackMap(kitMapping));
        } catch (Throwable t) {
            t.printStackTrace();
        }

        // Registration of the editor kits to JEditorPane
//        for (int i = 0; i < replacements.length; i++) {
//            JEditorPane.registerEditorKitForContentType(
//                replacements[i].contentType,
//                replacements[i].newKitClassName,
//                getClass().getClassLoader()
//            );
//        }

        operationListener = new RepositOperations();
        ((DataLoaderPool)Lookup.getDefault().lookup(DataLoaderPool.class)).addOperationListener(operationListener);

    }

    /** Called when module is uninstalled. Overrides superclass method. */
    public void uninstalled() {

        AllOptionsFolder.unregisterModuleRegListener();
        
        ((DataLoaderPool)Lookup.getDefault().lookup(DataLoaderPool.class)).removeOperationListener(operationListener);
        operationListener = null;
        
        // Options
        PrintSettings ps = (PrintSettings) SharedClassObject.findObject(PrintSettings.class, true);

        for (int i = 0; i < printOpts.length; i++) {
            ps.removeOption((SystemOption)SharedClassObject.findObject(printOpts[i], true));
        }

        // unregister our registry
        try {
            Field keyField = JEditorPane.class.getDeclaredField("kitRegistryKey");  // NOI18N
            keyField.setAccessible(true);
            Object key = keyField.get(JEditorPane.class);
            HackMap kitMapping = (HackMap)sun.awt.AppContext.getAppContext().get(key);
            if (kitMapping.getOriginal() != null) {
                sun.awt.AppContext.getAppContext().put(key, kitMapping.getOriginal());
            } else {
                sun.awt.AppContext.getAppContext().remove(key);
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        // #42970 - Possible closing of opened editor top components must happen in AWT thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // issue #16110
                // close all TopComponents which contain editor based on BaseKit
                HashSet set = new HashSet();
                set.addAll(TopComponent.getRegistry().getOpened());

                for (Iterator it = set.iterator(); it.hasNext(); ) {
                    TopComponent topComp = (TopComponent)it.next();
                    // top components in which we are interested must be of type CloneableEditor
                    if (!(topComp instanceof CloneableEditor))
                        continue;
                    Node[] arr = topComp.getActivatedNodes();
                    if (arr == null)
                        continue;
                    for (int i=0; i<arr.length; i++) {
                        EditorCookie ec = (EditorCookie)arr[i].getCookie(EditorCookie.class);
                        if (ec == null)
                            continue;
                        JEditorPane[] pane = ec.getOpenedPanes();
                        if (pane == null) 
                            continue;
                        for (int j=0; j<pane.length; j++) {
                            if (pane[j].getEditorKit() instanceof BaseKit) {
                                topComp.setCloseOperation(TopComponent.CLOSE_EACH);
                                topComp.close();
                            }
                        }
                    }
                }

            }
        });

        // stop any queued updates of parser DB
        ParserThread.getParserThread().stopParserThread();
        
        inited = false; // moved here as part of fix of #27418
    }

    private synchronized RequestProcessor getCCUpdateProcessor() {
        if (ccUpdateProcessor == null) {
            ccUpdateProcessor = new RequestProcessor("Code Completion Database Updater"); //NOI18N
        }
        return ccUpdateProcessor;
    }
    
    private static class HackMap extends Hashtable {
        
	private Hashtable delegate;

        HackMap(Hashtable h) {
            delegate = h;
            
            if (debug) {
                if (h != null) {
                    System.err.println("Original kit mappings: " + h);
                }

                try {
                    Field keyField = JEditorPane.class.getDeclaredField("kitTypeRegistryKey");  // NOI18N
                    keyField.setAccessible(true);
                    Object key = keyField.get(JEditorPane.class);
                    Hashtable kitTypeMapping = (Hashtable)sun.awt.AppContext.getAppContext().get(key);
                    sun.awt.AppContext.getAppContext().put(key, new DebugHashtable(kitTypeMapping));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        private Object findKit(String type) {
            FileObject fo = Repository.getDefault().getDefaultFileSystem().findResource("Editors/" + type + "/EditorKit.instance");
            if (fo == null) return null;

            DataObject dobj;
            try {
                dobj = DataObject.find(fo);
                InstanceCookie cookie = (InstanceCookie)dobj.getCookie(InstanceCookie.class);
                Class kitClass = cookie.instanceClass();
                if(EditorKit.class.isAssignableFrom(kitClass)) {
                    return BaseKit.getKit(kitClass);
                }
            }
            catch (DataObjectNotFoundException e) {}
            catch (IOException e) {}
            catch (ClassNotFoundException e) {}

            return null;
        }
        
        private String getKitClassName(String type) {
            try {
                Field keyField = JEditorPane.class.getDeclaredField("kitTypeRegistryKey");  // NOI18N
                keyField.setAccessible(true);
                Object key = keyField.get(JEditorPane.class);
                Hashtable kitTypeMapping = (Hashtable)sun.awt.AppContext.getAppContext().get(key);
                if (kitTypeMapping != null) {
                    return (String)kitTypeMapping.get(type);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            
            return null;
        }
            
        
        public synchronized Object get(Object key) {
            Object retVal = null;
            
            if (delegate != null) {
                retVal = delegate.get(key);
                if (debug && retVal != null) {
                    System.err.println("Found cached instance kit=" + retVal + " for mimeType=" + key);
                }
            }

	    if ((retVal == null || retVal.getClass().getName().startsWith("javax.swing."))
                && key instanceof String
            ) {
                // first check the type registry
                String kitClassName = getKitClassName((String)key);
                if (debug) {
                    System.err.println("Found kitClassName=" + kitClassName + " for mimeType=" + key);
                }
                
                if (kitClassName == null || kitClassName.startsWith("javax.swing.")) { // prefer layers
                    Object kit = findKit((String)key);
                    if (kit != null) {
                        retVal = kit;
                        if (debug) {
                            System.err.println("Found kit=" + retVal + " in xml layers for mimeType=" + key);
                        }
                    }
                }
	    }

            return retVal;
        }
        
        public synchronized Object put(Object key, Object value) {
            if (delegate == null) {
                delegate = new Hashtable();
            }

            Object ret = delegate.put(key,value);
            
            if (debug) {
                System.err.println("registering mimeType=" + key
                    + " -> kitInstance=" + value
                    + " original was " + ret);
            }
             
            return ret;
        }

        public synchronized Object remove(Object key) {
            Object ret = (delegate != null) ? delegate.remove(key) : null;
            
            if (debug) {
                System.err.println("removing kitInstance=" + ret
                    + " for mimeType=" + key);
            }
            
            return ret;
        }
        
        Hashtable getOriginal() {
            return delegate;
        }

    }
    
    private static final class DebugHashtable extends Hashtable {
        
        DebugHashtable(Hashtable h) {
            if (h != null) {
                putAll(h);
                System.err.println("Existing kit classNames mappings: " + this);
            }
        }
        
        public Object put(Object key, Object value) {
            Object ret = super.put(key, value);
            System.err.println("registering mimeType=" + key
                + " -> kitClassName=" + value
                + " original was " + ret);
            return ret;
        }
        
        public Object remove(Object key) {
            Object ret = super.remove(key);
            System.err.println("removing kitClassName=" + ret
                + " for mimeType=" + key);
            return ret;
        }
        
    }

    /** Listens to repository operations like move, delete and rename of the 
        classes or packages and updates Code Completion DB */
    private final class RepositOperations extends OperationAdapter {

        // for DataFolder move you will receive move events
        // for individual DataObjects in folder.
        public void operationMove(OperationEvent.Move ev){
            final DataObject dobj = ev.getObject();
            if (dobj == null) {
                return;
            }
            SourceCookie sc = (SourceCookie)dobj.getCookie(SourceCookie.class);
            if (sc == null) {
                return;
            }
            // Remove old class from parser DB. The moved file will
            // be added into parser DB automatically because of package
            // rename and consequent save operation.
            JCStorage.getStorage().removeClass(ev.getOriginalPrimaryFile());
        }
        
        // for DataFolder delete you will receive delete events
        // for individual DataObjects in folder.
        public void operationDelete(OperationEvent ev){
            DataObject dobj = ev.getObject();
            if (dobj == null) {
                return;
            }
            SourceCookie sc = (SourceCookie)dobj.getCookie(SourceCookie.class);
            if (sc == null) {
                return;
            }
            // Remove old class from parser DB
            JCStorage.getStorage().removeClass(dobj.getPrimaryFile());
        }
        
        public void operationRename(OperationEvent.Rename ev){
            DataObject dobj = ev.getObject();
            if (dobj == null) {
                return;  
            }

            // create path (including the file name) of the renamed file
            String oldPath = dobj.getPrimaryFile().getParent().getPath();
            if (oldPath.length() > 0) {
                oldPath += '/';
            }
            oldPath += ev.getOriginalName();
            
            DataFolder df = (DataFolder)dobj.getCookie(DataFolder.class);
            if (df != null) {
                // DataFolder was renamed. It is necessary to remove all classes
                // from renamed package from parser DB
                JCStorage.getStorage().removePackage(oldPath);
            } else {
                SourceCookie sc = (SourceCookie)dobj.getCookie(SourceCookie.class);
                if (sc == null) {
                    return;
                }
                // Remove the renamed class from parser DB
                JCStorage.getStorage().removeClass(oldPath);
            }
        }

    }
    
}

/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2005 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.image;

import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.openide.actions.*;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.ExtensionList;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.actions.SystemAction;
import org.openide.util.NbBundle;
import org.openide.ErrorManager;
import java.util.Iterator;

/** 
 * Data loader which recognizes image files.
 * @author Petr Hamernik, Jaroslav Tulach
 * @author Marian Petras
 */
public class ImageDataLoader extends UniFileLoader {

    /** Generated serial version UID. */
    static final long serialVersionUID =-8188309025795898449L;
    
    /** holds information about supported and unsupported MIME-types */
    private static final Map imageTypeSupport = new HashMap(7, .75f);
    
    /** Creates new image loader. */
    public ImageDataLoader() {
        // Set the representation class.
        super("org.netbeans.modules.image.ImageDataObject"); // NOI18N
    }
    
    protected FileObject findPrimaryFile(FileObject fo){
        if (fo.isFolder()) {
            return null;
        }
        
        final String mimeType = fo.getMIMEType();
        if (mimeType.startsWith("image/") && mimeType.length() > 6) {   //NOI18N
            final String imgType = mimeType.substring(6);
            Object supported = imageTypeSupport.get(imgType);
            if (supported != null) {
                return (supported == Boolean.TRUE) ? fo : null;
            } else {
                boolean hasReader = ImageIO.getImageReadersByMIMEType(mimeType)
                                    .hasNext();
                imageTypeSupport.put(new String(imgType),
                                     Boolean.valueOf(hasReader));
                return hasReader ? fo : null;
            }
        } else {
            return null;
        }
    }
    
    /** Gets default display name. Overrides superclass method. */
    protected String defaultDisplayName() {
        return NbBundle.getBundle(ImageDataLoader.class).getString("PROP_ImageLoader_Name");
    }
    
    /**
     * This methods uses the layer action context so it returns
     * a non-<code>null</code> value.
     *
     * @return  name of the context on layer files to read/write actions to
     */
    protected String actionsContext () {
        return "Loaders/image/png-gif-jpeg-bmp/Actions/";               //NOI18N
    }
    
    /**
     * This method returns <code>null</code> because it uses method
     * {@link #actionsContext}.
     *
     * @return  <code>null</code>
     */
    protected SystemAction[] defaultActions() {
        return null;
    }

    /** Create the image data object.
     * @param primaryFile the primary file (e.g. <code>*.gif</code>)
     * @return the data object for this file
     * @exception DataObjectExistsException if the primary file already has a data object
     * @exception java.io.IOException should not be thrown
     */
    protected MultiDataObject createMultiObject (FileObject primaryFile)
    throws DataObjectExistsException, java.io.IOException {
        return new ImageDataObject(primaryFile, this);
    }

}

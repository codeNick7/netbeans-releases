/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2004 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.spi.project;

import java.net.URI;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

/**
 * Knowledge of which project some files belong to.
 * @see org.netbeans.api.project.FileOwnerQuery
 * @author Jesse Glick
 */
public interface FileOwnerQueryImplementation {
    
    /**
     * Decide which project, if any, "owns" a given file.
     * @param file an absolute URI to some file (typically on disk; need not currently exist)
     * @return a project which owns it, or null for no response
     */
    Project getOwner(URI file);
    
    /**
     * Decide which project, if any, "owns" a given file.
     * @param file FileObject of an existing file
     * @return a project which owns it, or null for no response
     */
    Project getOwner(FileObject file);
    
    // XXX int getPriority();
    
}

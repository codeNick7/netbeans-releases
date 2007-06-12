/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.cnd.repository.disk.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.netbeans.modules.cnd.repository.sfs.ConcurrentFileRWAccess;
import org.netbeans.modules.cnd.repository.spi.Key;


/**
 *
 * @author Nickolay Dalmatov
 */
public interface RepFilesAccessStrategy {
    
    /** Returns the file access for the object identified by the id */
    ConcurrentFileRWAccess getFileForObj(Key id,  boolean read)  throws FileNotFoundException, IOException;
    
    /** Remove the record */
    void removeObjForKey (Key id);
    
    /** Sets the path to the repository */
    void setRepositoryBase(String aBaseDir);

    void setOpenFilesLimit(int limit);
    
    void closeUnit(String unitName);
}

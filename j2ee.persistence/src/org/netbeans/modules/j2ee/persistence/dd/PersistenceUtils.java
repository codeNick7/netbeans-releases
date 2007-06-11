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

package org.netbeans.modules.j2ee.persistence.dd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.j2ee.persistence.api.PersistenceScope;
import org.netbeans.modules.j2ee.persistence.api.PersistenceScopes;
import org.netbeans.modules.j2ee.persistence.api.metadata.orm.Entity;
import org.netbeans.modules.j2ee.persistence.api.metadata.orm.EntityMappings;
import org.netbeans.modules.j2ee.persistence.dd.persistence.model_1_0.Persistence;
import org.netbeans.modules.j2ee.persistence.dd.persistence.model_1_0.PersistenceUnit;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Marek Fukala, Andrei Badea
 */
public class PersistenceUtils {
    
    // TODO multiple mapping files
    
    private PersistenceUtils() {
    }
    
    /**
     * Returns the persistence unit(s) the given entity class belongs to. Since
     * an entity class can belong to any persistence unit, this returns all
     * persistence units in all persistence.xml files in the project which owns
     * the given entity class.
     *
     * @return an array of PersistenceUnit's; never null.
     * @throws NullPointerException if <code>sourceFile</code> is null.
     */
    public static PersistenceUnit[] getPersistenceUnits(FileObject sourceFile) throws IOException {
        if (sourceFile == null) {
            throw new NullPointerException("The sourceFile parameter cannot be null"); // NOI18N
        }
        
        Project project = FileOwnerQuery.getOwner(sourceFile);
        if (project == null) {
            return new PersistenceUnit[0];
        }
        
        List<PersistenceUnit> result = new ArrayList<PersistenceUnit>();
        for (PersistenceScope persistenceScope : getPersistenceScopes(project)) {
            Persistence persistence = PersistenceMetadata.getDefault().getRoot(persistenceScope.getPersistenceXml());
            for (PersistenceUnit persistenceUnit : persistence.getPersistenceUnit()) {
                result.add(persistenceUnit);
            }
        }
        
        return (PersistenceUnit[])result.toArray(new PersistenceUnit[result.size()]);
    }
    
    /**
     * Searches the given entity mappings for the specified entity class.
     *
     * @param  className the Java class to search for.
     * @param  entityMappings the entity mappings to be searched.
     * @return the entity class or null if it could not be found.
     * @throws NullPointerException if <code>className</code> or
     *         <code>entityMappings</code> were null.
     */
    public static Entity getEntity(String className, EntityMappings entityMappings) {
        if (className == null) {
            throw new NullPointerException("The javaClass parameter cannot be null"); // NOI18N
        }
        if (entityMappings == null) {
            throw new NullPointerException("The entityMappings parameter cannot be null"); // NOI18N
        }
        
        for (Entity entity : entityMappings.getEntity()) {
            if (className.equals(entity.getClass2())) {
                return entity;
            }
        }
        return null;
    }
    
    /**
     * Returns an array containing all persistence scopes provided by the
     * given project. This is just an utility method which does:
     *
     * <pre>
     * PersistenceScopes.getPersistenceScopes(project).getPersistenceScopes();
     * </pre>
     *
     * <p>with all the necessary checks for null (returning an empty
     * array in this case).</p>
     *
     * @param  project the project to retrieve the persistence scopes from.
     * @return the list of persistence scopes provided by <code>project</code>;
     *         or an empty array if the project provides no persistence
     *         scopes; never null.
     * @throws NullPointerException if <code>project</code> was null.
     */
    public static PersistenceScope[] getPersistenceScopes(Project project) {
        if (project == null) {
            throw new NullPointerException("The project parameter cannot be null"); // NOI18N
        }
        
        PersistenceScopes persistenceScopes = PersistenceScopes.getPersistenceScopes(project);
        if (persistenceScopes != null) {
            return persistenceScopes.getPersistenceScopes();
        }
        return new PersistenceScope[0];
    }
    
    /**
     * Compares entity classes lexicographically by fully qualified names.
     */
    private static final class EntityComparator implements Comparator<Entity> {
        
        public int compare(Entity o1, Entity o2) {
            String name1 = o1.getClass2();
            String name2 = o2.getClass2();
            if (name1 == null) {
                return name2 == null ? 0 : -1;
            } else {
                return name2 == null ? 1 : name1.compareTo(name2);
            }
        }
    }
}

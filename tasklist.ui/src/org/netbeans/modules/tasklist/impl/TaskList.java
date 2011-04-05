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

package org.netbeans.modules.tasklist.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.netbeans.modules.tasklist.filter.TaskFilter;
import org.netbeans.modules.tasklist.trampoline.TaskGroup;
import org.netbeans.spi.tasklist.FileTaskScanner;
import org.netbeans.spi.tasklist.PushTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;
import org.openide.util.WeakSet;

/**
 * @author S. Aubrecht
 * @author Tomas Stupka
 */
public class TaskList {
    
    private TreeSet<Task> sortedTasks;
    private ArrayList<Task> tasksList;
    
    private Map<PushTaskScanner, List<Task>> pushScanner2tasks = new HashMap<PushTaskScanner, List<Task>>( 10 );
    private Map<FileTaskScanner, List<Task>> fileScanner2tasks = new HashMap<FileTaskScanner, List<Task>>( 10 );
    
    private Map<TaskGroup, List<Task>> group2tasks = new HashMap<TaskGroup,List<Task>>( 10 );
    
    private final WeakSet<Listener> listeners = new WeakSet<Listener>( 2 );
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    private Comparator<Task> comparator;
    
    /** Creates a new instance of TaskList */
    public TaskList() {
        sortedTasks = new TreeSet<Task>(getComparator());
    }
    
    void setTasks( PushTaskScanner scanner, FileObject resource, List<? extends Task> tasks, TaskFilter filter ) throws IOException {
        lock.writeLock().lock();
        
        List<Task> removed = clear( scanner, resource );
        
        List<Task> tasksToAdd = null;
        int currentCount = countTasks( scanner );
        for( Task t : tasks ) {
            if( filter.accept( t ) && !filter.isTaskCountLimitReached(currentCount) ) {
                currentCount++;
                
                if( sortedTasks.contains( t ) || (tasksToAdd != null && tasksToAdd.contains( t ) ) )
                    continue;
                
                if( null == tasksToAdd )
                    tasksToAdd = new ArrayList<Task>( tasks.size() );
                
                List<Task> scannerTasks = pushScanner2tasks.get( scanner );
                if( null == scannerTasks ) {
                    scannerTasks = new LinkedList<Task>();
                    pushScanner2tasks.put( scanner, scannerTasks );
                }
                
                TaskGroup group = Accessor.getGroup( t );
                List<Task> groupTasks = group2tasks.get( group );
                if( null == groupTasks ) {
                    groupTasks = new LinkedList<Task>();
                    group2tasks.put( group, groupTasks );
                }
                
                tasksToAdd.add( t );
                scannerTasks.add( t );
                groupTasks.add( t );
            }
        }
        if( null != tasksToAdd ) {
            addTasks( tasksToAdd );
        }
        
        lock.writeLock().unlock();
        
        if( null != removed && !removed.isEmpty() )
            fireTasksRemoved( removed );
        if( null != tasksToAdd && !tasksToAdd.isEmpty() )
            fireTasksAdded( tasksToAdd );
    }
    
    void clear( PushTaskScanner scanner ) {
        lock.writeLock().lock();
        List<Task> toRemove = pushScanner2tasks.get( scanner );
        pushScanner2tasks.remove( scanner );
        if( null != toRemove ) {
            for( List<Task> groupTasks : group2tasks.values() ) {
                groupTasks.removeAll( toRemove );
            }
            removeTasks( toRemove );
        }
        lock.writeLock().unlock();
        
        if( null != toRemove && !toRemove.isEmpty() ) {
            fireTasksRemoved( toRemove );
        }
    }
    
    private int countTasks( PushTaskScanner scanner ) {
        List<Task> tasks = pushScanner2tasks.get( scanner );
        return null == tasks ? 0 : tasks.size();
    }
    
    private List<Task> clear( PushTaskScanner scanner, FileObject resource ) {
        List<Task> toRemove = null;
        List<Task> tasks = pushScanner2tasks.get( scanner );
        if( null != tasks ) {
            if( null == resource ) {
                toRemove = new LinkedList<Task>();
                toRemove.addAll(tasks);
            } else {
                for( Task t : tasks ) {
                    if( resource.equals( Accessor.getFile( t ) ) ) {
                        if( null == toRemove )
                            toRemove = new LinkedList<Task>();
                        toRemove.add( t );
                    }
                }
            }
        }
        
        if( null != toRemove ) {
            removeTasks( toRemove );
            tasks.removeAll( toRemove );
            for( List<Task> groupTasks : group2tasks.values() ) {
                groupTasks.removeAll( toRemove );
            }
        }
        return toRemove;
    }
    
    void update( FileTaskScanner scanner, FileObject resource, List<Task> newTasks, TaskFilter filter ) {
        lock.writeLock().lock();
        
        List<Task> removed = clear( scanner, resource );

        ArrayList<Task> tasksToAdd = new ArrayList<Task>( newTasks.size() );
        for( Task t : newTasks ) {
            if( sortedTasks.contains( t ) || tasksToAdd.contains( t ) )
                continue;
            if( !filter.isTaskCountLimitReached( countTasks( scanner ) ) && filter.accept( t ) ) {
                List<Task> scannerTasks = fileScanner2tasks.get( scanner );
                if( null == scannerTasks ) {
                    scannerTasks = new LinkedList<Task>();
                    fileScanner2tasks.put( scanner, scannerTasks );
                }
                TaskGroup group = Accessor.getGroup( t );
                List<Task> groupTasks = group2tasks.get( group );
                if( null == groupTasks ) {
                    groupTasks = new LinkedList<Task>();
                    group2tasks.put( group, groupTasks );
                }
                scannerTasks.add( t );
                groupTasks.add( t );
                tasksToAdd.add( t );
            }
        }
        if( !tasksToAdd.isEmpty() ) {
            addTasks( tasksToAdd );
        }

        lock.writeLock().unlock();

        if( null != removed && !removed.isEmpty() )
            fireTasksRemoved( removed );
        if( !tasksToAdd.isEmpty() )
            fireTasksAdded( tasksToAdd );
    }
    
    public int size() {
        int retValue = 0;
        lock.readLock().lock();
        retValue = sortedTasks.size();
        lock.readLock().unlock();
        return retValue;
    }
    
    public List<? extends Task> getTasks() {
        lock.readLock().lock();
        try {
            return new ArrayList<Task>( sortedTasks );
        } finally {
            lock.readLock().unlock();
        }
    }
    
    int countTasks( FileTaskScanner scanner ) {
        List<Task> tasks = fileScanner2tasks.get( scanner );
        return null == tasks ? 0 : tasks.size();
    }
    
    public int countTasks( TaskGroup group ) {
        List<Task> groupTasks = group2tasks.get( group );
        return null == groupTasks ? 0 : groupTasks.size();
    }
    
    public Task getTask( int index ) {
        Task retValue = null;
        lock.readLock().lock();
        if( index >= 0 && index < sortedTasks.size() )
            retValue = getTasksList().get( index );
        lock.readLock().unlock();
        return retValue;
    }

    void clear( FileTaskScanner scanner ) {
        lock.writeLock().lock();
        List<Task> toRemove = fileScanner2tasks.get( scanner );
        fileScanner2tasks.remove( scanner );
        if( null != toRemove ) {
            for( List<Task> groupTasks : group2tasks.values() ) {
                groupTasks.removeAll( toRemove );
            }
            removeTasks( toRemove );
        }
        lock.writeLock().unlock();
        
        if( null != toRemove && !toRemove.isEmpty() ) {
            fireTasksRemoved( toRemove );
        }
    }
    
    void clear( FileTaskScanner scanner, FileObject... resources ) throws IOException {
        lock.readLock().lock();
        ArrayList<Task> toRemove = null;
        List<Task> tasks = fileScanner2tasks.get( scanner );
        if( null != tasks ) {
            for( Task t : tasks ) {
                for( FileObject rc : resources ) {
                    if( rc.equals( Accessor.getFile( t ) ) ) {
                        if( null == toRemove )
                            toRemove = new ArrayList<Task>( resources.length );
                        toRemove.add( t );
                    }
                }
            }
        }
        lock.readLock().unlock();
        
        if( null != toRemove && !toRemove.isEmpty() ) {
            lock.writeLock().lock();
            removeTasks( toRemove );
            tasks.removeAll( toRemove );
            for( List<Task> groupTasks : group2tasks.values() ) {
                groupTasks.removeAll( toRemove );
            }
            lock.writeLock().unlock();
            
            fireTasksRemoved( toRemove );
        }
    }
    
    private List<Task> clear( FileTaskScanner scanner, FileObject resource ) {
        List<Task> tasks = fileScanner2tasks.get( scanner );
        if( null == tasks )
            return null;
        List<Task> toRemove = null;
        for( Task t : tasks ) {
            if( resource.equals( Accessor.getFile( t ) ) ) {
                if( null == toRemove )
                    toRemove = new LinkedList<Task>();
                toRemove.add( t );
            }
        }
        
        if( null != toRemove ) {
            removeTasks( toRemove );
            tasks.removeAll( toRemove );
            for( List<Task> groupTasks : group2tasks.values() ) {
                groupTasks.removeAll( toRemove );
            }
        }
        return toRemove;
    }
    
    void clear( FileObject resource ) {
        List<Task> toRemove = null;

        lock.writeLock().lock();

        for( List<Task> scannerTasks : fileScanner2tasks.values() ) {
            for( Task t : scannerTasks ) {
                if( resource.equals( Accessor.getFile(t) ) ) {
                    if( null == toRemove )
                        toRemove = new LinkedList<Task>();
                    toRemove.add( t );
                }
            }
        }

        if( null != toRemove ) {
            removeTasks( toRemove );
            for( List<Task> scannerTasks : fileScanner2tasks.values() ) {
                scannerTasks.removeAll( toRemove );
            }
            for( List<Task> groupTasks : group2tasks.values() ) {
                groupTasks.removeAll( toRemove );
            }
        }

        lock.writeLock().unlock();

        if( null != toRemove ) {
            fireTasksRemoved( toRemove );
        }
    }

    void clear() {
        lock.writeLock().lock();
        sortedTasks.clear();
        tasksList = null; 
        fileScanner2tasks.clear();
        pushScanner2tasks.clear();
        group2tasks.clear();
        lock.writeLock().unlock();
        fireCleared();
    }

    void clearDeletedFiles() {
        lock.writeLock().lock();
        LinkedList<Task> toRemove = new LinkedList<Task>();
        for( Task t : sortedTasks ) {
            FileObject fo = Accessor.getFile(t);
            if( null != fo && !fo.isValid() )
                toRemove.add(t);
            }

        if( !toRemove.isEmpty() ) {
            removeTasks( toRemove );
            for( List<Task> scannerTasks : fileScanner2tasks.values() ) {
                scannerTasks.removeAll( toRemove );
            }
            for( List<Task> groupTasks : group2tasks.values() ) {
                groupTasks.removeAll( toRemove );
            }
        }

        lock.writeLock().unlock();

        if( !toRemove.isEmpty() ) {
            fireTasksRemoved( toRemove );
        }
    }
    
    public void addListener( Listener l ) {
        synchronized( listeners ) {
            listeners.add( l );
        }
    }
    
    public void removeListener( Listener l ) {
        synchronized( listeners ) {
            listeners.remove( l );
        }
    }
    
    public int indexOf( Task t ) {
        lock.readLock().lock();
        int idx = getTasksList().indexOf(t);
        lock.readLock().unlock();
        return idx;
    }
    
    private Comparator<Task> getComparator() {
        if( null == comparator )
            comparator = TaskComparator.getDefault();
        return comparator;
    }
    
    public void setComparator( Comparator<Task> comparator ) {
        if( getComparator().equals( comparator ) ) {
            return;
        }
        
        lock.writeLock().lock();
        
        this.comparator = comparator;
        TreeSet<Task> s = sortedTasks;
        sortedTasks = new TreeSet<Task>(comparator);
        addTasks(s);
        
        lock.writeLock().unlock();
    }
    
    private void fireTasksAdded( List<Task> tasks ) {
        TaskList.Listener[] tmp; 
        synchronized( listeners ) {
            tmp = listeners.toArray(new TaskList.Listener[listeners.size()]);
        }
        for ( Listener l : tmp ) {
            l.tasksAdded( tasks );
        }
    }
    
    private void fireTasksRemoved( List<Task> tasks ) {
        TaskList.Listener[] tmp; 
        synchronized( listeners ) {
            tmp = listeners.toArray(new TaskList.Listener[listeners.size()]);
        }
        for ( Listener l : tmp ) {
            l.tasksRemoved( tasks );
        }
    }
    
    private void fireCleared() {
        TaskList.Listener[] tmp; 
        synchronized( listeners ) {
            tmp = listeners.toArray(new TaskList.Listener[listeners.size()]);
        }
        for( Listener l : tmp) {
            l.cleared();
        }
    }
    
    private List<Task> getTasksList() {
        if(tasksList == null) {
            tasksList = new ArrayList<Task>(sortedTasks);
        }
        return tasksList;
    }
    
    private void addTasks(Collection<Task> tasksToAdd) {
        sortedTasks.addAll( tasksToAdd );
        tasksList = null; 
    }
    
    private void removeTasks(List<Task> toRemove) {
        sortedTasks.removeAll( toRemove );
        tasksList = null; 
    }    
    
    public static interface Listener {
        void tasksAdded( List<? extends Task> tasks );
        void tasksRemoved( List<? extends Task> tasks );
        void cleared();
    }
}

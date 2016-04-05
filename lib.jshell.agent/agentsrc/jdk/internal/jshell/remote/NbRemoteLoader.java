/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2016 Oracle and/or its affiliates. All rights reserved.
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
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2016 Sun Microsystems, Inc.
 */
package jdk.internal.jshell.remote;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 *
 * @author sdedic
 */
class NbRemoteLoader extends RemoteClassLoader {
    private final NbRemoteLoader oldDelegate;

    public NbRemoteLoader(ClassLoader parent, ClassLoader oldDelegate) {
        if (oldDelegate != null && !(oldDelegate instanceof NbRemoteLoader)) {
            throw new IllegalArgumentException("Invalid classloader: " + oldDelegate);
        }
        this.oldDelegate = (NbRemoteLoader)oldDelegate;
    }

    /**
     * Finds the class.
     * The order should be:
     * - the delegate classloader
     * 
     * @param name
     * @return
     * @throws ClassNotFoundException 
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class c;
        
        try {
            // contact the original classloader for the application; it should not load REPL classes.
            c = getParent().loadClass(name);
            return c;
        } catch (ClassNotFoundException ex) {
        }
        if (oldDelegate != null) {
            try {
                return oldDelegate.findClass(name);
            } catch (ClassNotFoundException ex) {
            }
        }
        return super.findClass(name);
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        Enumeration<URL> origResources = getParent().getResources(name);
        Enumeration<URL> deleResources = oldDelegate != null ? oldDelegate.findResources(name) : Collections.emptyEnumeration();
        
        return new CompoundEnumeration<>(new Enumeration[] {
                origResources,
                deleResources
        });
    }

    @Override
    public URL findResource(String name) {
        try {
            Enumeration<URL> res = findResources(name);
            return res.hasMoreElements() ? res.nextElement() : null;
        } catch (IOException ex) {
            return null;
        }
    }

    
    private static class CompoundEnumeration<T> implements Enumeration<T> {
        private final Enumeration<T>[] enums;
        private final HashSet<T>  enumerated = new HashSet<>();
        
        private int index;
        private T   nextItem;

        public CompoundEnumeration(Enumeration<T>[] enums) {
            this.enums = enums;
        }
        
        @Override
        public boolean hasMoreElements() {
            if (nextItem != null) {
                return true;
            }
            while (index < enums.length) {
                Enumeration<T> del = enums[index];
                if (del.hasMoreElements()) {
                    T item = del.nextElement();
                    if (!enumerated.add(item)) {
                        continue;
                    }
                    nextItem = item;
                    break;
                } else {
                    index++;
                }
            }
            return nextItem != null;
        }

        @Override
        public T nextElement() {
            if (!hasMoreElements()) {
                throw new NoSuchElementException();
            }
            return nextItem;
        }
    
    }
}

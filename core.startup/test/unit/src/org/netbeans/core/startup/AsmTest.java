/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */

package org.netbeans.core.startup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import org.netbeans.PatchByteCode;
import org.netbeans.junit.NbTestCase;
import org.openide.modules.ConstructorDelegate;
import org.openide.modules.PatchFor;
import org.openide.modules.PatchedPublic;

public class AsmTest extends NbTestCase {

    public AsmTest(String n) {
        super(n);
    }

    public static class C {
        static final long x = 123L; // test CONSTANT_Long, tricky!
        private C(boolean _) {}
        @PatchedPublic
        private C(int _) {}
        private void m1() {}
        @PatchedPublic
        private void m2() {}
    }
    
    public static class Superclazz {
        public int val;
    }
    
    public static class CAPI extends Superclazz {
        public int otherVal;
        
        public CAPI() {
            otherVal = 1;
        }
    }
    
    @PatchFor(CAPI.class)
    public static class CompatAPI extends Superclazz {
        @ConstructorDelegate
        protected static void createAPI(CompatAPI inst, int val2) {
            inst.val = val2;
        }
    }

    public static class CAPI2 {
        CAPI2() {}
    }

    @PatchFor(CAPI2.class)
    public static class CompatAPI2 {
        @ConstructorDelegate
        public static void createAPI(CAPI2 inst, String[] param) {
        }
    }

    public void testPatchingPublic() throws Exception {
        Class<?> c = new L().loadClass(C.class.getName());
        assertNotSame(c, C.class);
        Member m;
        m = c.getDeclaredConstructor(boolean.class);
        assertEquals(0, m.getModifiers() & Modifier.PUBLIC);
        assertEquals(Modifier.PRIVATE, m.getModifiers() & Modifier.PRIVATE);
        m = c.getDeclaredConstructor(int.class);
        assertEquals(Modifier.PUBLIC, m.getModifiers() & Modifier.PUBLIC);
        assertEquals(0, m.getModifiers() & Modifier.PRIVATE);
        m = c.getDeclaredMethod("m1");
        assertEquals(0, m.getModifiers() & Modifier.PUBLIC);
        assertEquals(Modifier.PRIVATE, m.getModifiers() & Modifier.PRIVATE);
        m = c.getDeclaredMethod("m2");
        assertEquals(Modifier.PUBLIC, m.getModifiers() & Modifier.PUBLIC);
        assertEquals(0, m.getModifiers() & Modifier.PRIVATE);
    }
    
    public void testPatchSuperclass() throws Exception {
        Class<?> c = new L().loadClass(CAPI.class.getName());
        assertNotSame(c, CAPI.class);
        
        Class s = c.getSuperclass();
        assertNotSame(s, Object.class);
        assertEquals(CompatAPI.class.getName(), s.getName());
    }

    public void testPatchSuperclassWithArray() throws Exception {
        Class<?> c = new L().loadClass(CAPI2.class.getName());
        assertNotSame(c, CAPI2.class);
        Class s = c.getSuperclass();
        assertEquals(CompatAPI2.class.getName(), s.getName());
        Constructor<?> init = c.getConstructor(String[].class);
        init.newInstance((Object)new String[] {"a","b"});
    }
    
    public void testGeneratedConstructor() throws Exception {
        Class<?> c = new L().loadClass(CAPI.class.getName());
        Member m = c.getDeclaredConstructor(int.class);
        assertEquals(Modifier.PROTECTED, m.getModifiers() & (Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PRIVATE));
    }
    
    public void testExecutingConstructor() throws Exception {
        ClassLoader l = new L();
        Class<?> c = l.loadClass(CAPI.class.getName());        
        Member m = c.getDeclaredConstructor(int.class);
        Constructor ctor = (Constructor)m;
        ctor.setAccessible(true);
        
        Object o = ctor.newInstance(5);
        assertSame(c, o.getClass());
        assertTrue("Invalid API superclass", Superclazz.class.isInstance(o));
        
        assertEquals("@ConstructorDelegate method did not execute", 5, ((Superclazz)o).val);
        
        Field f = o.getClass().getField("otherVal");
        Object v = f.get(o);
        assertEquals("Patched API constructor did not execute", v, 1);
    }
    
    

    private static class L extends ClassLoader {

        L() {
            super(L.class.getClassLoader());
        }

        @Override
        protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.startsWith(AsmTest.class.getName() + "$C")) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream in = getResourceAsStream(name.replace('.', '/') + ".class");
                int r;
                try {
                    while ((r = in.read()) != -1) {
                        baos.write(r);
                    }
                } catch (IOException x) {
                    throw new ClassNotFoundException(name, x);
                }
                byte[] data;
                try {
                    Enumeration<URL> res = getResources("META-INF/.bytecodePatched"); // NOI18N
                    Method fs = PatchByteCode.class.getDeclaredMethod("fromStream", Enumeration.class, ClassLoader.class);
                    Method apply = PatchByteCode.class.getDeclaredMethod("apply", String.class, byte[].class);
                    fs.setAccessible(true);
                    apply.setAccessible(true);
                    PatchByteCode pbc = (PatchByteCode) fs.invoke(null, res, this);
                    data = (byte[]) apply.invoke(pbc, name, baos.toByteArray());
                } catch (Exception x) {
                    throw new ClassNotFoundException(name, x);
                }
                Class c = defineClass(name, data, 0, data.length);
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            } else {
                return super.loadClass(name, resolve);
            }
        }
    }

}

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
package org.openide.filesystems;

import java.net.URL;
import java.net.URLClassLoader;
import junit.framework.*;
import org.openide.ErrorManager;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.ref.*;
import java.util.*;

/**
 *
 * @author Jaroslav Tulach
 */
public class MIMESupportResolversTest extends TestCase {
    static {
        System.setProperty("org.openide.util.Lookup", "org.openide.filesystems.MIMESupportResolversTest$Lkp");
    }
    
    
    public MIMESupportResolversTest (String testName) {
        super (testName);
    }

    protected void setUp () throws Exception {
    }

    protected void tearDown () throws Exception {
    }

    public static Test suite () {
        TestSuite suite = new TestSuite(MIMESupportResolversTest.class);
        return suite;
    }
    
    
    public void testWrongImplOfGetResolvers() throws Exception {
        MIMEResolver[] all = MIMESupport.getResolvers();
        assertTrue("Error manager race condition activated", ErrMgr.switchDone);
        assertEquals("there is one", 1, all.length);
        assertEquals("c1 the original one", Lkp.c1, all[0]);
        
        all = MIMESupport.getResolvers();
        assertEquals("there is one", 1, all.length);
        assertEquals("c2 the new one", Lkp.c2, all[0]);
    }
    
    
    
    public static final class Lkp extends org.openide.util.lookup.AbstractLookup {
        private ErrMgr err = new ErrMgr();
        private org.openide.util.lookup.InstanceContent ic;
        static MIMEResolver c1 = new MIMEResolver() {
            public String findMIMEType(FileObject fo) {
                return null;
            }
            
            public String toString() {
                return "C1";
            }
        };
        static MIMEResolver c2 = new MIMEResolver() {
            public String findMIMEType(FileObject fo) {
                return null;
            }
            public String toString() {
                return "C2";
            }
        };
        
        public Lkp () {
            this (new org.openide.util.lookup.InstanceContent ());
        }
        
        private Lkp (org.openide.util.lookup.InstanceContent ic) {
            super (ic);
            this.ic = ic;
            
            turn(c1);
        }
        
        public void turn (MIMEResolver c) {
            ArrayList l = new ArrayList();
            l.add(err);
            l.add(c);
            ic.set (l, null);
        }
    }
    
    
    private static class ErrMgr extends ErrorManager {
        public static boolean switchDone;
        
        public Throwable attachAnnotations (Throwable t, ErrorManager.Annotation[] arr) {
            return null;
        }

        public ErrorManager.Annotation[] findAnnotations (Throwable t) {
            return null;
        }

        public Throwable annotate (Throwable t, int severity, String message, String localizedMessage, Throwable stackTrace, Date date) {
            return null;
        }

        public void notify (int severity, Throwable t) {
        }

        public void log (int severity, String s) {
            if (s.startsWith ("Resolvers computed")) {
                switchDone = true;
                Lkp lkp = (Lkp)org.openide.util.Lookup.getDefault ();
                lkp.turn (Lkp.c2);
            }
        }

        public ErrorManager getInstance (String name) {
            return this;
        }
        
    }
    
}

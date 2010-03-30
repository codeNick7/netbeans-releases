/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */

package org.netbeans.modules.terminal;

import java.awt.BorderLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.terminal.api.TerminalContainer;
import org.openide.util.Exceptions;
import org.openide.windows.IOContainer;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author ivan
 */
public class TestSupport extends NbTestCase {

    private JComponent actualContainer;

    private JFrame frame;

    protected IOProvider ioProvider;
    protected IOContainer ioContainer;
    protected InputOutput io;
    protected boolean defaultContainer = false;
    // LATER: my IOContainer doesn't do well with output2 IOProvider
    protected boolean defaultProvider = false;

    public TestSupport(String testName) {
	super(testName);
    }

    @Override
    protected void setUp() throws Exception {
	System.out.printf("TestSupport.setUp()\n");
	System.out.printf("setUp()\n");

	if (defaultContainer) {
	    ioContainer = IOContainer.getDefault();
	    actualContainer = defaultContainer(ioContainer);
	} else {
	    TerminalContainer tc = TerminalContainer.create(null, "Test");
	    actualContainer = tc;
	    ioContainer = tc.ioContainer();
	}

        SwingUtilities.invokeAndWait(new Runnable() {
	    @Override
            public void run() {

		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(actualContainer, BorderLayout.CENTER);
		frame.setBounds(20, 20, 700, 300);
		frame.setVisible(true);

	    }
	});

	if (defaultProvider) {
	    ioProvider = IOProvider.getDefault();
	    assertNotNull ("Could not find IOProvider", ioProvider);
	} else {
	    ioProvider = IOProvider.get("Terminal");
	    assertNotNull ("Could not find IOProvider", ioProvider);
	    assertTrue("Got default IOProvider", ioProvider != IOProvider.getDefault());
	}
	io = ioProvider.getIO("test", null, ioContainer);
	assertNotNull ("Could not get InputOutput", io);
	io.select();
    }

    @Override
    protected void tearDown() throws Exception {
	System.out.printf("TestSupport.tearDown()\n");

	io.closeInputOutput();
	io = null;
	ioProvider = null;
	ioContainer = null;
	actualContainer = null;

        SwingUtilities.invokeAndWait(new Runnable() {
	    @Override
            public void run() {
		frame.dispose();
		frame = null;
	    }
	});
    }

    protected static void sleep(int seconds) {
	try {
	    Thread.sleep(seconds * 1000);
	} catch(InterruptedException x) {
	    fail("sleep interrupted");
	}
    }

    /**
     * Use reflection to extract private IOWindow instance so
     * we can embed it in a JFrame.
     */
    private static JComponent defaultContainer(IOContainer ioContainer) {
        JComponent comp = null;
        try {
            try {
                Field f = ioContainer.getClass().getDeclaredField("provider");
                f.setAccessible(true);
                IOContainer.Provider prov = (IOContainer.Provider) f.get(ioContainer);
                Method m = prov.getClass().getDeclaredMethod("impl", new Class[0]);
                m.setAccessible(true);
                comp = (JComponent) m.invoke(prov);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            } catch (NoSuchMethodException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        return comp;
    }
}
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

package org.netbeans.modules.form;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.openide.nodes.*;
import org.openide.cookies.*;
import org.openide.actions.*;
import org.openide.util.actions.SystemAction;
import org.netbeans.modules.form.actions.*;

/**
 * A common superclass for nodes used in Form Editor.
 *
 * @author Tomas Pavek
 */

public class FormNode extends AbstractNode implements FormCookie {

    private FormModel formModel;

    protected FormNode(Children children, FormModel formModel) {
        super(children);
        this.formModel = formModel;
        getCookieSet().add(this);
    }

    // FormCookie implementation
    public final FormModel getFormModel() {
        return formModel;
    }

    // FormCookie implementation
    public final Node getOriginalNode() {
        return this;
    }

    public Node.Cookie getCookie(Class type) {
        Node.Cookie cookie = super.getCookie(type);
        if (cookie == null
            && (CompilerCookie.class.isAssignableFrom(type)
                || SaveCookie.class.isAssignableFrom(type)
                || ExecCookie.class.isAssignableFrom(type)
                || type.getName().equals("org.openide.cookies.DebuggerCookie") // NOI18N
                || CloseCookie.class.isAssignableFrom(type)
                || ArgumentsCookie.class.isAssignableFrom(type)
                || PrintCookie.class.isAssignableFrom(type)))
        {
            FormDataObject fdo = FormEditorSupport.getFormDataObject(formModel);
            if (fdo != null)
                cookie = fdo.getCookie(type);
        }
        return cookie;
    }

    protected SystemAction[] createActions() {
        return new SystemAction[] {
            SystemAction.get(GotoEditorAction.class),
            null,
            SystemAction.get(PropertiesAction.class) 
        };
    }

    public Component getCustomizer() {
        Component customizer = createCustomizer();
        if (customizer instanceof Window) {
            // register the customizer window (probably a dialog) to be closed
            // automatically when the form is closed
            FormEditorSupport fes = FormEditorSupport.getSupport(formModel);
            if (fes != null) {
                Window customizerWindow = (Window) customizer;
                fes.registerFloatingWindow(customizerWindow);
                // attach a listener to unregister the window when it is closed
                customizerWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (e.getSource() instanceof Window) {
                            Window window = (Window) e.getSource();
                            FormEditorSupport fes =
                                FormEditorSupport.getSupport(formModel);
                            if (fes != null)
                                fes.unregisterFloatingWindow(window);
                            window.removeWindowListener(this);
                        }
                    }
                });
            }
        }
        return customizer;
    }

    // to be implemented in FormNode descendants (instead of getCustomizer)
    protected Component createCustomizer() {
        return null;
    }

    // ----------
    // automatic children updates

    void updateChildren() {
        Children children = getChildren();
        if (children instanceof FormNodeChildren)
            ((FormNodeChildren)children).updateKeys();
    }

    // Special children class - to be implemented in FormNode descendants (if
    // they know their set of children nodes and can update them).
    protected abstract static class FormNodeChildren extends Children.Keys {
        protected void updateKeys() {}
    }

    // ----------
    // Persistence hacks - for the case the node is selected in some
    // (standalone) properties window when IDE exits. We don't restore the
    // original node after IDE restarts (would require to load the form), but
    // provide a fake node which destroys itself immediately - closing the
    // properties window. [Would be nice to find some better solution...]

    public Node.Handle getHandle() {
        return new Handle();
    }

    static class Handle implements Node.Handle {
        public Node getNode() throws java.io.IOException {
            return new ClosingNode();
        }
    }

    static class ClosingNode extends AbstractNode implements Runnable {
        ClosingNode() {
            super(Children.LEAF);
        }
        public String getName() {
            java.awt.EventQueue.invokeLater(this);
            return super.getName();
        }
        public Node.PropertySet[] getPropertySets() {
            java.awt.EventQueue.invokeLater(this);
            return super.getPropertySets();
        }
        public void run() {
            this.fireNodeDestroyed();
        }
    }
}

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
package org.netbeans.modules.java.hints.introduce;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.InvocationTargetException;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.TreePathHandle;
import org.openide.NotificationLineSupport;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

/**
 * Provides support for name changes in Introduce dialogs. Will delay validation
 * so that user can fast type. In case of focus lost event, performs validation
 * synchronously if the doc was changed from the last validation.
 * 
 * @author sdedic
 */
class NameChangeSupport extends FocusAdapter implements DocumentListener, Runnable {
    private static final RequestProcessor CHANGE_RP = new RequestProcessor(NameChangeSupport.class);
    
    private final JTextField    control;
    private MemberValidator  validator;
    private RequestProcessor.Task   validateTask = CHANGE_RP.create(this, true);
    private ChangeListener  listener;
    private TreePathHandle  target;
    private Modifier minAccess;
    private boolean valid;
    private String validateName;
    
    public NameChangeSupport(JTextField control) {
        this.control = control;
        control.getDocument().addDocumentListener(this);
        control.addFocusListener(this);
    }
    
    public synchronized void setChangeListener(ChangeListener l) {
        assert listener == null;
        this.listener = l;
    }
    
    public synchronized void setValidator(MemberValidator val) {
        this.validator = val;
    }

    @Override
    public void focusLost(FocusEvent e) {
        // must revalidate immediately
        if (validateTask.cancel()) {
            this.validateName = control.getText().trim();
            run();
        }
    }
    
    public void setTarget(TreePathHandle target) {
        this.validateName = control.getText().trim();
        this.target = target;
        validateTask.run();
    }
    
    public synchronized Modifier getMinAccess() {
        return minAccess;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.validateName = control.getText().trim();
        validateTask.schedule(200);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}

    public boolean isValid() {
        return validator == null ? true : valid;
    }
    
    @NbBundle.Messages({
        "ERR_NameIsEmpty=Name is empty",
        "ERR_NameIsNotValid=Name is not a vallid java identifier"
    })
    @Override
    public void run() {
        TreePathHandle t;
        MemberValidator v;
        synchronized (this) {
            if (validateName == null) {
                return;
            }
            if (validator == null) {
                return;
            }
            t = target;
            v = validator;
        }
        boolean nv = false;
        Modifier mod = null;
        final ChangeListener l;
        
        if (validateName.isEmpty()) {
            notifyNameError(Bundle.ERR_NameIsEmpty());
            nv = false;
        } else if (!Utilities.isJavaIdentifier(validateName)) {
            notifyNameError(Bundle.ERR_NameIsNotValid());
            nv = false;
        } else {
            MemberSearchResult res = v.validateName(t, control.getText().trim());
            nv = updateUI(res);
            mod = res == null ? null : res.getRequiredModifier();
        }
        synchronized (this) {
            if (minAccess == mod && nv == valid) {
                return;
            }
            this.valid = nv;
            if (nv) {
                minAccess = mod;
            }
            l = listener;
        }
        if (l != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    l.stateChanged(new ChangeEvent(this));
                }
            });
        }
    }
    
    /**
     * Should update the UI with messages appropriate for the search. The return value
     * informs whether the entry is valid
     * @param result
     * @return 
     */
    protected boolean updateUI(MemberSearchResult result) {
        return true;
    }
    
    protected void notifyNameError(String msg) {
    }
    
}

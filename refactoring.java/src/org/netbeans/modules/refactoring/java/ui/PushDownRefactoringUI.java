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
package org.netbeans.modules.refactoring.java.ui;

import com.sun.source.util.TreePath;
import java.util.HashSet;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.swing.event.ChangeListener;
import org.netbeans.api.fileinfo.NonRecursiveFolder;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.TreePathHandle;
import org.netbeans.api.java.source.ui.ElementHeaders;
import org.netbeans.modules.refactoring.api.AbstractRefactoring;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.java.RefactoringUtils;
import org.netbeans.modules.refactoring.java.api.MemberInfo;
import org.netbeans.modules.refactoring.java.api.PushDownRefactoring;
import org.netbeans.modules.refactoring.spi.ui.CustomRefactoringPanel;
import org.netbeans.modules.refactoring.spi.ui.RefactoringUI;
import org.openide.filesystems.FileObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;


/** Refactoring UI object for Push Down refactoring.
 *
 * @author Pavel Flaska, Jan Becicka
 */
public class PushDownRefactoringUI implements RefactoringUI, JavaRefactoringUIFactory {
    // reference to pull up refactoring this UI object corresponds to
    private PushDownRefactoring refactoring;
    // initially selected members
    private Set initialMembers;
    // UI panel for collecting parameters
    private PushDownPanel panel;
    
    private String description;
    
    /** Creates a new instance of PushDownRefactoringUI
     * @param selectedElements Elements the refactoring action was invoked on.
     */
    private PushDownRefactoringUI(TreePathHandle selectedElements, CompilationInfo info) {
        initialMembers = new HashSet();
        TreePathHandle selectedPath = resolveSelection(selectedElements, info);

        if (selectedPath != null) {
            Element selected = selectedPath.resolveElement(info);
            initialMembers.add(MemberInfo.create(selected, info));
            // compute source type and members that should be pre-selected from the
            // set of elements the action was invoked on

           // create an instance of push down refactoring object
            if (!(selected instanceof TypeElement)) {
                selected = info.getElementUtilities().enclosingTypeElement(selected);
            }
            TreePath tp = info.getTrees().getPath(selected);
            if(tp != null) {
                TreePathHandle sourceType = TreePathHandle.create(tp, info);
                description = ElementHeaders.getHeader(tp, info, ElementHeaders.NAME);
                refactoring = new PushDownRefactoring(sourceType);
                refactoring.getContext().add(RefactoringUtils.getClasspathInfoFor(sourceType));
            } else {
                // put the unresolvable selection to refactoring,
                // user notification is provided by PushDownRefactoringPlugin.preCheck
                refactoring = new PushDownRefactoring(selectedElements);
            }
        } else {
            // put the unresolvable selection to refactoring,
            // user notification is provided by PushDownRefactoringPlugin.preCheck
            refactoring = new PushDownRefactoring(selectedElements);
        }
    }

    private PushDownRefactoringUI() {
    }
    
    // --- IMPLEMENTATION OF RefactoringUI INTERFACE ---------------------------
    
    @Override
    public boolean isQuery() {
        return false;
    }

    @Override
    public CustomRefactoringPanel getPanel(ChangeListener parent) {
        if (panel == null) {
            panel = new PushDownPanel(refactoring, initialMembers, parent);
        }
        return panel;
    }

    @Override
    public Problem setParameters() {
        captureParameters();
        return refactoring.checkParameters();
    }
    
    @Override
    public Problem checkParameters() {
        captureParameters();
        return refactoring.fastCheckParameters();
    }

    @Override
    public AbstractRefactoring getRefactoring() {
        return refactoring;
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(PushDownRefactoringUI.class, "DSC_PushDown", description); // NOI18N
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(PushDownRefactoringUI.class, "LBL_PushDown"); // NOI18N
    }

    @Override
    public boolean hasParameters() {
        return true;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("org.netbeans.modules.refactoring.java.ui.PushDownRefactoringUI"); // NOI18N
    }
    
    // --- PRIVATE HELPER METHODS ----------------------------------------------
    
    /** Gets parameters from the refactoring panel and sets them
     * to the refactoring object.
     */
    private void captureParameters() {
        refactoring.setMembers(panel.getMembers());
    }

    static TreePathHandle resolveSelection(TreePathHandle source, CompilationInfo javac) {
        TreePath resolvedPath = source.resolve(javac);
        TreePath path = resolvedPath;
        Element resolvedElement = source.resolveElement(javac);
        while (path != null && resolvedElement == null) {
            path = path.getParentPath();
            if (path == null) {
                return null;
            }
            resolvedElement = javac.getTrees().getElement(path);
        }

        return path == resolvedPath ? source : TreePathHandle.create(path, javac);
    }

    @Override
    public RefactoringUI create(CompilationInfo info, TreePathHandle[] handles, FileObject[] files, NonRecursiveFolder[] packages) {
        assert handles.length == 1;
        TreePathHandle selectedElement = PullUpRefactoringUI.findSelectedClassMemberDeclaration(handles[0], info);
                    return selectedElement != null
                            ? new PushDownRefactoringUI(selectedElement, info)
                            : null;
    }
    
    public static JavaRefactoringUIFactory factory() {
        return new PushDownRefactoringUI();
    }

}

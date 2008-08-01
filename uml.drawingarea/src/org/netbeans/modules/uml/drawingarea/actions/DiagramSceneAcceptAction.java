/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
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
package org.netbeans.modules.uml.drawingarea.actions;

import java.awt.Cursor;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;


public final class DiagramSceneAcceptAction extends WidgetAction.Adapter {

    private AcceptProvider provider;

    public DiagramSceneAcceptAction (AcceptProvider provider) {
        this.provider = provider;
    }

    @Override
    public State mouseClicked(Widget widget, WidgetMouseEvent event)
    {
        State returnState = State.REJECTED;
        
        if ((CopyPasteSupport.PasteCursor).equals(widget.getCursor()))
        {
            if (event.getButton() != MouseEvent.BUTTON1)
            {
                widget.getScene().setCursor(Cursor.getDefaultCursor());
                return State.CONSUMED;
            }
            
            ConnectorState acceptable = provider.isAcceptable(widget, event.getPoint(), 
                    CopyPasteSupport.getTransferable());

            if (acceptable == ConnectorState.ACCEPT)
            {
                provider.accept(widget, event.getPoint(), 
                        CopyPasteSupport.getTransferable());
                returnState = State.CONSUMED;
            } else if (acceptable == ConnectorState.REJECT_AND_STOP)
            {
                returnState = State.CONSUMED;
            }
            widget.getScene().setCursor(Cursor.getDefaultCursor());
            widget.getScene().getView().setCursor(Cursor.getDefaultCursor());
        }
        return returnState;
    }
    
    @Override
    public State mouseMoved (Widget widget, WidgetMouseEvent event) {
        // to do, enable/disable paste cursor based on the hit widget
            return State.REJECTED;
        }
    
    @Override
    public State dragOver (Widget widget, WidgetDropTargetDragEvent event) {
        ConnectorState acceptable = provider.isAcceptable (widget, event.getPoint (), 
                event.getTransferable());

        if (acceptable == ConnectorState.ACCEPT) {
            event.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
            return State.CONSUMED;
        } else if (acceptable == ConnectorState.REJECT_AND_STOP) {
            event.rejectDrag ();
            return State.CONSUMED;
        }
        return State.REJECTED;
    }

    @Override
    public State dropActionChanged (Widget widget, WidgetDropTargetDragEvent event) {
        ConnectorState acceptable = provider.isAcceptable (widget, event.getPoint (), event.getTransferable ());

        if (acceptable == ConnectorState.ACCEPT) {
            event.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
            return State.CONSUMED;
        } else if (acceptable == ConnectorState.REJECT_AND_STOP) {
            event.rejectDrag ();
            return State.CONSUMED;
        }
        return State.REJECTED;
    }

    @Override
    public State drop (Widget widget, WidgetDropTargetDropEvent event) {
        ConnectorState acceptable = provider.isAcceptable (widget, event.getPoint (), event.getTransferable ());

        if (acceptable == ConnectorState.ACCEPT) {
            event.acceptDrop (DnDConstants.ACTION_COPY_OR_MOVE);
            provider.accept (widget, event.getPoint (), event.getTransferable ());
            return State.CONSUMED;
        } else if (acceptable == ConnectorState.REJECT_AND_STOP) {
            event.rejectDrop ();
            return State.CONSUMED;
        }
        return State.REJECTED;
    }

}
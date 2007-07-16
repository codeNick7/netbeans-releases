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
package org.netbeans.modules.subversion.ui.history;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import org.openide.util.NbBundle;

/**
 * @author Maros Sandor
 */
class Divider extends JPanel {

    public static final int DIVIDER_CLICKED = 1;
    public static final int DOWN = 0;
    public static final int UP = 1;

    private Color bkg;
    private Color sbkg;
    private Color arrowColor;
    private Color selectedArrowColor;
    private ActionListener listener;

    private int arrowDirection;

    public Divider(ActionListener listener) {
        this.listener = listener;
        enableEvents(MouseEvent.MOUSE_ENTERED | MouseEvent.MOUSE_EXITED | MouseEvent.MOUSE_CLICKED);
        bkg = getBackground();
        sbkg = UIManager.getColor("TextField.selectionBackground"); // NOI18N
        selectedArrowColor = UIManager.getColor("TextField.selectionForeground"); // NOI18N
        arrowColor = UIManager.getColor("TextField.inactiveForeground"); // NOI18N
        getAccessibleContext().setAccessibleName(NbBundle.getMessage(Divider.class, "ACSN_Divider"));
        getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(Divider.class, "ACSD_Divider"));
    }

    public Dimension getPreferredSize() {
        return new Dimension(Integer.MAX_VALUE, 6);
    }

    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, 6);
    }

    public void setArrowDirection(int direction) {
        arrowDirection = direction;
    }

    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getID() == MouseEvent.MOUSE_ENTERED) {
            setBackground(sbkg);
            repaint();
        }
        if (e.getID() == MouseEvent.MOUSE_EXITED) {
            setBackground(bkg);
            repaint();
        }
        if (e.getID() == MouseEvent.MOUSE_CLICKED) {
            listener.actionPerformed(new ActionEvent(this, DIVIDER_CLICKED, "")); // NOI18N
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = getSize();
        if (getBackground().equals(bkg)) {
            g.setColor(arrowColor);
        } else {
            g.setColor(selectedArrowColor);
        }

        int mid = dim.width / 2;
        if (arrowDirection == DOWN) {
            g.drawLine(mid - 4, 1, mid + 4, 1);
            g.drawLine(mid - 3, 2, mid + 3, 2);
            g.drawLine(mid - 2, 3, mid + 2, 3);
            g.drawLine(mid - 1, 4, mid + 1, 4);
        }
        else if (arrowDirection == UP) {
            g.drawLine(mid - 4, 4, mid + 4, 4);
            g.drawLine(mid - 3, 3, mid + 3, 3);
            g.drawLine(mid - 2, 2, mid + 2, 2);
            g.drawLine(mid - 1, 1, mid + 1, 1);
        }
    }
}

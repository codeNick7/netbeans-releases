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
/*
 * OutputPane.java
 *
 * Created on May 14, 2004, 6:45 PM
 */

package org.netbeans.core.output2.ui;

import java.awt.Rectangle;
import javax.swing.plaf.TextUI;
import org.netbeans.core.output2.Controller;
import org.openide.ErrorManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.openide.util.Lookup;

/**
 * A scroll pane containing an editor pane, with special handling of the caret
 * and scrollbar - until a keyboard or mouse event, after a call to setDocument(),
 * the caret and scrollbar are locked to the last line of the document.  This avoids
 * "jumping" scrollbars as the position of the caret (and thus the scrollbar) get updated
 * to reposition them at the bottom of the document on every document change.
 *
 * @author  Tim Boudreau
 */
public abstract class AbstractOutputPane extends JScrollPane implements DocumentListener, MouseListener, MouseMotionListener, KeyListener, ChangeListener, MouseWheelListener, Runnable {
    private boolean locked = true;
    
    private int fontHeight = -1;
    private int fontWidth = -1;
    protected JEditorPane textView;
    int lastCaretLine = 0;
    boolean hadSelection = false;
    boolean recentlyReset = false;

    public AbstractOutputPane() {
        textView = createTextView();
        init();
    }
    
    public int getFontWidth() {
        return fontWidth;
    }
    
    public int getFontHeight() {
        return fontHeight;
    }
    
    public void requestFocus() {
        textView.requestFocus();
    }
    
    public boolean requestFocusInWindow() {
        return textView.requestFocusInWindow();
    }
    
    protected abstract JEditorPane createTextView();

    protected void documentChanged() {
        lastLength = -1;
        if (pendingCaretLine != -1) {
            if (!sendCaretToLine (pendingCaretLine, pendingCaretSelect)) {
                ensureCaretPosition();
            }
        } else {
            ensureCaretPosition();
        }
        if (recentlyReset && isShowing()) {
            recentlyReset = false;
        }
        if (locked) {
            setMouseLine(-1);
        }
        if (isWrapped()) {
            //Saves having OutputEditorKit have to do its own listening
            getViewport().revalidate();
            getViewport().repaint();
        }
    }
    
    public abstract boolean isWrapped();
    public abstract void setWrapped (boolean val);

    public boolean hasSelection() {
        return textView.getSelectionStart() != textView.getSelectionEnd();
    }

    /**
     * Ensure that the document is scrolled all the way to the bottom (unless
     * some user event like scrolling or placing the caret has unlocked it).
     * <p>
     * Note that this method is always called on the event queue, since 
     * OutputDocument only fires changes on the event queue.
     */
    public final void ensureCaretPosition() {
        if (locked) {           
            //Make sure the scrollbar is updated *after* the document change
            //has been processed and the scrollbar model's maximum updated
            if (!enqueued) {
                SwingUtilities.invokeLater(this);
                enqueued = true;
            }
        }
    }
    
    /** True when invokeLater has already been called on this instance */
    private boolean enqueued = false;
    /**
     * Scrolls the pane to the bottom, invokeLatered to ensure all state has
     * been updated, so the bottom really *is* the bottom.
     */
    public void run() {
        enqueued = false;
        getVerticalScrollBar().setValue(getVerticalScrollBar().getModel().getMaximum());
    }

    public int getSelectionStart() {
        return textView.getSelectionStart();
    }

    public void setSelection (int start, int end) {
        int rstart = Math.min (start, end);
        int rend = Math.max (start, end);
        if (rstart == rend) {
            getCaret().setDot(rstart);
        } else {
            textView.setSelectionStart(rstart);
            textView.setSelectionEnd(rend);
        }
    }

    public void selectAll() {
        unlockScroll();
        getCaret().setVisible(true);
        textView.setSelectionStart(0);
        textView.setSelectionEnd(getLength());
    }

    public boolean isAllSelected() {
        return textView.getSelectionStart() == 0 && textView.getSelectionEnd() == getLength();
    }

    protected void init() {
        setViewportView(textView);
        textView.setEditable(false);

        textView.addMouseListener(this);
        textView.addMouseWheelListener(this);
        textView.addMouseMotionListener(this);
        textView.addKeyListener(this);
        textView.setCaret (new OCaret());
        
        getCaret().setVisible(true);
        getCaret().setBlinkRate(0);
        getCaret().setSelectionVisible(true);
        
        getVerticalScrollBar().getModel().addChangeListener(this);
        getVerticalScrollBar().addMouseMotionListener(this);
        
        getViewport().addMouseListener(this);
        getVerticalScrollBar().addMouseListener(this);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        addMouseListener(this);

        getCaret().addChangeListener(this);
        Integer i = (Integer) UIManager.get("customFontSize"); //NOI18N
        int size = 11;
        if (i != null) {
            size = i.intValue();
        }
        textView.setFont (new Font ("Monospaced", Font.PLAIN, size)); //NOI18N
        setBorder (BorderFactory.createEmptyBorder());
        setViewportBorder (BorderFactory.createEmptyBorder());
    }

    public final Document getDocument() {
        return textView.getDocument();
    }
    
    /**
     * This method is here for use *only* by unit tests.
     */
    public final JTextComponent getTextView() {
        return textView;
    }

    public final void copy() {
        if (getCaret().getDot() != getCaret().getMark()) {
            textView.copy();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    protected void setDocument (Document doc) {
        if (hasSelection()) {
            hasSelectionChanged(false);
        }
        hadSelection = false;
        lastCaretLine = 0;
        lastLength = -1;
        Document old = textView.getDocument();
        old.removeDocumentListener(this);
        textView.setDocument(doc);
        doc.addDocumentListener(this);
        lockScroll();
        recentlyReset = true;
        pendingCaretLine = -1;
    }
    
    protected void setEditorKit(EditorKit kit) {
        Document doc = textView.getDocument();
        
        textView.setEditorKit(kit);
        textView.setDocument(doc);
        updateKeyBindings();
        getCaret().setVisible(true);
        getCaret().setBlinkRate(0);
    }
    
    /**
     * Setting the editor kit will clear the action map/key map connection
     * to the TopComponent, so we reset it here.
     */
    protected final void updateKeyBindings() {
        Keymap keymap = textView.getKeymap();
        keymap.removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
    }
    
    protected EditorKit getEditorKit() {
        return textView.getEditorKit();
    }
    
    public final int getLineCount() {
        return textView.getDocument().getDefaultRootElement().getElementCount();
    }

    private int lastLength = -1;
    public final int getLength() {
        if (lastLength == -1) {
            lastLength = textView.getDocument().getLength();
        }
        return lastLength;
    }
    
    /**
     * If we are sending the caret to a hyperlinked line, but it is < 3 lines
     * from the bottom, we will hold the line number in this field until there
     * are enough lines that it will be semi-centered.
     */
    private int pendingCaretLine = -1;
    private boolean pendingCaretSelect = false;
    private boolean inSendCaretToLine = false;
    
    public final boolean sendCaretToLine(int idx, boolean select) {
        int count = getLineCount();
        if (count - idx < 3) {
            pendingCaretLine = idx;
            pendingCaretSelect = select;
            return false;
        } else {
            inSendCaretToLine = true;
            pendingCaretLine = -1;
            unlockScroll();
            getCaret().setVisible(true);
            getCaret().setSelectionVisible(true);
            Element el = textView.getDocument().getDefaultRootElement().getElement(Math.min(idx, getLineCount() - 1));
            int position = el.getStartOffset();
            if (select) {
                getCaret().setDot (el.getEndOffset()-1);
                getCaret().moveDot (position);
                getCaret().setSelectionVisible(true);
                textView.repaint();
            } else {            
                if (idx + 3 < getLineCount()) {
                    getCaret().setDot(position);
                    //Ensure a little more than the requested line is in view
                    try {
                        Rectangle r = textView.modelToView(textView.getDocument().getDefaultRootElement().getElement (idx + 3).getStartOffset());
                        if (Controller.log) Controller.log ("Trying to ensure some lines below the new caret line are visible - scrolling into view " + r);
                        if (r != null) { //Will be null if maximized - no parent, no coordinate space
                            textView.scrollRectToVisible(r);
                        }
                    } catch (BadLocationException ble) {
                        ErrorManager.getDefault().notify(ble);
                    }
                }
            }
            inSendCaretToLine = false;
            return true;
        }
    }
    
    
    
    protected abstract int getWrappedHeight();
    
    public final void lockScroll() {
        if (!locked) {
            locked = true;
        }
    }
    
    public final void unlockScroll() {
        if (locked) {
            locked = false;
        }
    }

    protected abstract boolean shouldRelock(int dot);

    protected abstract void caretEnteredLine (int line);
    
    protected abstract void lineClicked (int line);
    
    protected abstract void postPopupMenu (Point p, Component src);
    
    public final int getCaretLine() {
        int result = -1;
        int charPos = getCaret().getDot();
        if (charPos > 0) {
            result = textView.getDocument().getDefaultRootElement().getElementIndex(charPos);
        }
        return result;
    }

    public final int getCaretPos() {
        return getCaret().getDot();
    }

    public final void paint (Graphics g) {
        if (fontHeight == -1) {
            fontHeight = g.getFontMetrics(textView.getFont()).getHeight();
            fontWidth = g.getFontMetrics(textView.getFont()).charWidth('m'); //NOI18N
        }
        super.paint(g);
    }


    protected abstract boolean shouldRelockScrollBar(int currVal);


//***********************Listener implementations*****************************

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JViewport) {
            if (locked) {
                ensureCaretPosition();
            }
        } else if (e.getSource() == getVerticalScrollBar().getModel()) {
            if (!locked) { //XXX check if doc is still being written?
                BoundedRangeModel mdl = getVerticalScrollBar().getModel();
                if (mdl.getValue() == mdl.getMaximum()) {
                    Thread.dumpStack();
                    lockScroll();
                }
            }
        } else {
            if (!locked) {
                maybeSendCaretEnteredLine();
            }
        }
    }

    private boolean caretLineChanged() {
        int line = getCaretLine();
        boolean result = line != lastCaretLine;
        lastCaretLine = line;
        return result;
    }

    private void maybeSendCaretEnteredLine() {
        //Don't message the controller if we're programmatically setting
        //the selection, or if the caret moved because output was written - 
        //it can cause the controller to send events to OutputListeners which
        //should only happen for user events
        if ((!locked && caretLineChanged()) && !inSendCaretToLine) {
            int line = getCaretLine();
            boolean sel = textView.getSelectionStart() != textView.getSelectionEnd();
            if (line != -1 && !sel) {
                caretEnteredLine(getCaretLine());
            }
            if (isWrapped()) {
                //We need to force a repaint to erase all of the old selection
                //if we're doing our own painting
                int dot = getCaret().getDot();
                int mark = getCaret().getMark();
                if ((((dot > mark) != (lastKnownDot > lastKnownMark)) && !(lastKnownDot == lastKnownMark)) || ((lastKnownDot == lastKnownMark) != (dot == mark))){
                    int begin = Math.min (Math.min(lastKnownDot, lastKnownMark), Math.min(dot, mark));
                    int end = Math.max (Math.max(lastKnownDot, lastKnownMark), Math.max (dot, mark));
                }
            }
            if (sel != hadSelection) {
                hadSelection = sel;
                hasSelectionChanged (sel);
            }
        }
        lastKnownMark = getCaret().getMark();
        lastKnownDot = getCaret().getDot();
    }
    
    private int lastKnownMark = -1;
    private int lastKnownDot = -1;

    private void hasSelectionChanged(boolean sel) {
        ((AbstractOutputTab) getParent()).hasSelectionChanged(sel);
    }

    public final void changedUpdate(DocumentEvent e) {
        //Ensure it is consumed
        e.getLength();
        documentChanged();
    }

    public final void insertUpdate(DocumentEvent e) {
        //Ensure it is consumed
        e.getLength();
        documentChanged();
    }

    public final void removeUpdate(DocumentEvent e) {
        //Ensure it is consumed
        e.getLength();
        documentChanged();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        setMouseLine (-1);
    }

    private int mouseLine = -1;
    public void setMouseLine (int line) {
        if (mouseLine != line) {
            mouseLine = line;
        }
    }

    public int getMouseLine() {
        return mouseLine;
    }


    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        int pos = textView.viewToModel(p);
        if (pos < getLength()) {
            int line = getDocument().getDefaultRootElement().getElementIndex(pos);
            int lineStart = getDocument().getDefaultRootElement().getElement(line).getStartOffset();
            int lineLength = getDocument().getDefaultRootElement().getElement(line).getEndOffset() -
                    lineStart;

            int left = getInsets().left;
            int maxX = left + (fontWidth * lineLength);
//            System.err.println ("maxX " + maxX + " x " + p.x + " lineLength " + lineLength + " pos " + pos + " lineStart " + lineStart);

            if (p.x <= maxX) {
                setMouseLine (line);
            } else {
                setMouseLine(-1);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getSource() == getVerticalScrollBar()) {
            int y = e.getY();
            if (y > getVerticalScrollBar().getHeight()) {
                lockScroll();
            }
        }
    }

    public final void mousePressed(MouseEvent e) {
        if (locked) {
            Element el = getDocument().getDefaultRootElement().getElement(getLineCount()-1);
            getCaret().setDot(el.getStartOffset());
            unlockScroll();
            //We should now set the caret position so the caret doesn't
            //seem to ignore the first click
            if (e.getSource() == textView) {
                getCaret().setDot (textView.viewToModel(e.getPoint()));
            }
        }
        if (e.isPopupTrigger()) {
            //Convert immediately to our component space - if the 
            //text view scrolls before the component is opened, popup can
            //appear above the top of the screen
            Point p = SwingUtilities.convertPoint((Component) e.getSource(), 
                e.getPoint(), this);
            
            postPopupMenu (p, this);
        }
    }

    public final void mouseReleased(MouseEvent e) {
        if (e.getSource() == textView && SwingUtilities.isLeftMouseButton(e)) {
            int pos = textView.viewToModel(e.getPoint());
            if (pos != -1) {
                int line = textView.getDocument().getDefaultRootElement().getElementIndex(pos);
                if (line >= 0) {
                    lineClicked(line);
                }
            }
        }
        if (e.isPopupTrigger()) {
            Point p = SwingUtilities.convertPoint((Component) e.getSource(), 
            //Convert immediately to our component space - if the 
            //text view scrolls before the component is opened, popup can
            //appear above the top of the screen
                e.getPoint(), this);
            
            postPopupMenu (p, this);
        }
    }
    
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_END) {
            lockScroll();
        } else {
            unlockScroll();
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public final void mouseWheelMoved(MouseWheelEvent e) {
        BoundedRangeModel sbmodel = getVerticalScrollBar().getModel();
        int currPosition = sbmodel.getValue();
        unlockScroll();
        if (e.getSource() == textView) {
            int newPosition = Math.max (0, Math.min (sbmodel.getMaximum(),
                currPosition + (e.getUnitsToScroll() * (sbmodel.getExtent() / 4))));
            sbmodel.setValue (newPosition);
        }
    }

    Caret getCaret() {
        return textView.getCaret();
    }
    
    private class OCaret extends DefaultCaret {
        public void setSelectionVisible(boolean val) {
            super.setSelectionVisible(true);
            super.setBlinkRate(0);
        }
        public boolean isSelectionVisible() {
            return true;
        }
        public void setBlinkRate(int rate) {
            super.setBlinkRate(0);
        }
        public void setVisible(boolean b) {
            super.setVisible(true);
        }
 
        public boolean isVisible() { return true; }
        
        public void paint(Graphics g) {
            JTextComponent component = textView;
            if(isVisible()) {
                try {
                    TextUI mapper = component.getUI();
                    Rectangle r = mapper.modelToView(component, getDot(), Position.Bias.Forward);

                    if ((r == null) || ((r.width == 0) && (r.height == 0))) {
                        return;
                    }
                    if (width > 0 && height > 0 &&
                                    !this._contains(r.x, r.y, r.width, r.height)) {
                        // We seem to have gotten out of sync and no longer
                        // contain the right location, adjust accordingly.
                        Rectangle clip = g.getClipBounds();

                        if (clip != null && !clip.contains(this)) {
                            // Clip doesn't contain the old location, force it
                            // to be repainted lest we leave a caret around.
                            repaint();
                        }
 //                       System.err.println("WRONG! Caret dot m2v = " + r + " but my bounds are " + x + "," + y + "," + width + "," + height);
                        
                        // This will potentially cause a repaint of something
                        // we're already repainting, but without changing the
                        // semantics of damage we can't really get around this.
                        damage(r);
                    }
                    g.setColor(component.getCaretColor());
                    g.drawLine(r.x, r.y, r.x, r.y + r.height - 1);

                } catch (BadLocationException e) {
                    // can't render I guess
                    //System.err.println("Can't render cursor");
                }
            }
        }    
        
        private boolean _contains(int X, int Y, int W, int H) {
            int w = this.width;
            int h = this.height;
            if ((w | h | W | H) < 0) {
                // At least one of the dimensions is negative...
                return false;
            }
            // Note: if any dimension is zero, tests below must return false...
            int x = this.x;
            int y = this.y;
            if (X < x || Y < y) {
                return false;
            }
            if (W > 0) {
                w += x;
                W += X;
                if (W <= X) {
                    // X+W overflowed or W was zero, return false if...
                    // either original w or W was zero or
                    // x+w did not overflow or
                    // the overflowed x+w is smaller than the overflowed X+W
                    if (w >= x || W > w) {
                        return false;
                    }
                } else {
                    // X+W did not overflow and W was not zero, return false if...
                    // original w was zero or
                    // x+w did not overflow and x+w is smaller than X+W
                    if (w >= x && W > w) {
                        //This is the bug in DefaultCaret - returns false here
                        return true;
                    }
                }
            }
            else if ((x + w) < X) {
                return false;
            }
            if (H > 0) {
                h += y;
                H += Y;
                if (H <= Y) {
                    if (h >= y || H > h) return false;
                } else {
                    if (h >= y && H > h) return false;
                }
            }
            else if ((y + h) < Y) {
                return false;
            }
            return true;
        }        
    }
}

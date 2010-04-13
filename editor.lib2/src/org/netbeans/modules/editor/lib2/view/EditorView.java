/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package org.netbeans.modules.editor.lib2.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.View;

/**
 * Base class for views in editor view hierarchy.
 * <br/>
 * In general there are three types of views:<ul>
 * <li>Document view</li>
 * <li>Paragraph views</li>
 * <li>Children of paragraph views which include highlights view, newline view and others.</li>
 * </ul>
 * <br/>
 * Paragraph views have their start offset based over a swing text position. Their end offset
 * is based on last child's end offset.
 * <br/>
 * Children of paragraph views have their start offset based over a relative distance
 * to their parent's paragraph view's start offset. Therefore their start offset does not mutate
 * upon modification unless the whole paragraph's start offset mutates.
 * Their {@link #getLength()} method should remain stable upon document mutations
 * (this way the view builder can iterate over them when calculating last affected view
 * once the new views become created).
 *
 * @author Miloslav Metelka
 */

public abstract class EditorView extends View {

    // -J-Dorg.netbeans.modules.editor.lib2.view.EditorView.level=FINE
    private static final Logger LOG = Logger.getLogger(EditorView.class.getName());

    /**
     * Raw offset along the parent's major axis (axis along which the children are laid out).
     */
    private double rawVisualOffset; // 16 + 8 = 24 bytes

    public EditorView(Element element) {
        super(element);
    }

    /**
     * Get raw start offset of the view which may transform to real start offset
     * when post-processed by parent view.
     * <br/>
     * <b>Note:</b> Typical clients should NOT call this method (they should call
     * {@link #getStartOffset()} method instead).
     *
     * @return raw start offset of the view or -1 if the view does not support
     * storage of the raw offsets (e.g. a ParagraphView).
     */
    public abstract int getRawOffset();

    /**
     * Set raw start offset of the view.
     *
     * @param rawOffset raw start offset of the view. This method will not be called
     *  if {@link #getRawOffset()} returns -1.
     */
    public abstract void setRawOffset(int rawOffset);

    /**
     * Textual length of the view.
     *
     * @return &gt;=0 length of the view.
     */
    public int getLength() {
        return getEndOffset() - getStartOffset();
    }

    /**
     * Set textual length of the view.
     *
     * @param length &gt;=0 new textual length of the view.
     * @return true if length change is supported or false if the view must be recreated.
     */
    public boolean setLength(int length) {
        return false;
    }

    /**
     * @return raw visual offset along parent's major axis. It must be post-processed
     *   by parent (if it uses gap-based storage) to become a real visual offset.
     */
    public final double getRawVisualOffset() {
        return rawVisualOffset;
    }
    
    /**
     * Parent can set the view's raw offset along the parent view's
     * major axis (axis along which the children are laid out) by using this method.
     *
     * @param rawVisualOffset raw offset value along the major axis of parent view.
     *  It is not particularly useful without postprocessing by the parent.
     */
    public final void setRawVisualOffset(double rawVisualOffset) {
        this.rawVisualOffset = rawVisualOffset;
    }

    /**
     * Paint into the given bounds.
     *
     * @param g non-null graphics to render into.
     * @param alloc non-null bounds allocated to this view. It can be mutated if necessary.
     */
    public abstract void paint(Graphics2D g, Shape alloc, Rectangle clipBounds);

    /**
     * {@inheritDoc}
     */
    @Override
    public final void paint(Graphics g, Shape alloc) {
        if (alloc != null) {
            if (g instanceof Graphics2D) {
                paint((Graphics2D)g, alloc, g.getClipBounds());
            }
        }
    }

    /**
     * Provides a way to determine the next visually represented model
     * location at which one might place a caret.
     * Some views may not be visible,
     * they might not be in the same order found in the model, or they just
     * might not allow access to some of the locations in the model.
     *
     * @param offset the position to convert >= 0
     * @param bias bias for the offset.
     * @param alloc non-null bounds allocated to this view.
     * @param direction the direction from the current position that can
     *  be thought of as the arrow keys typically found on a keyboard.
     *  This will be one of the following values:
     * <ul>
     * <li>SwingConstants.WEST
     * <li>SwingConstants.EAST
     * <li>SwingConstants.NORTH
     * <li>SwingConstants.SOUTH
     * </ul>
     * @return the location within the model that best represents the next
     *  location visual position
     * @exception IllegalArgumentException if <code>direction</code>
     *		doesn't have one of the legal values above
     */
    public int getNextVisualPositionFromChecked(int offset, Position.Bias bias, Shape alloc,
            int direction, Position.Bias[] biasRet)
    {
        try {
            return super.getNextVisualPositionFrom(offset, bias, alloc, direction, biasRet);
        } catch (BadLocationException e) {
            return getStartOffset(); // Should not happen
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getNextVisualPositionFrom(int offset, Position.Bias bias, Shape alloc,
            int direction, Position.Bias[] biasRet) throws BadLocationException
    {
        if (offset != -1) { // -1 is allowed as a special case; although javadoc in View prohibits it!
            checkBounds(offset);
        }
        checkBias(bias);
        if (alloc != null) {
            offset = getNextVisualPositionFromChecked(offset, bias, alloc, direction, biasRet);
        }
        return offset;
    }

    /**
     * Provides a mapping, for a given character,
     * from the document model coordinate space
     * to the view coordinate space.
     *
     * @param offset the position of the desired character (>=0)
     * @param alloc the area of the view, which encompasses the requested character
     * @param bias the bias toward the previous character or the
     *  next character represented by the offset, in case the
     *  position is a boundary of two views; <code>b</code> will have one
     *  of these values:
     * <ul>
     * <li> <code>Position.Bias.Forward</code>
     * <li> <code>Position.Bias.Backward</code>
     * </ul>
     * @return the bounding box, in view coordinate space,
     *		of the character at the specified position
     * @exception IllegalArgumentException if <code>b</code> is not one of the
     *		legal <code>Position.Bias</code> values listed above
     * @see View#viewToModel
     */
    public abstract Shape modelToViewChecked(int offset, Shape alloc, Position.Bias bias);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Shape modelToView(int offset, Shape alloc, Position.Bias bias) throws BadLocationException {
        checkBounds(offset);
        checkBias(bias);
        if (alloc != null) {
            return modelToViewChecked(offset, alloc, bias);
        } else {
            return null;
        }
    }

    /**
     * Provides a mapping, for a given region,
     * from the document model coordinate space
     * to the view coordinate space. The specified region is
     * created as a union of the first and last character positions.
     *
     * @param offset0 the position of the first character (>=0)
     * @param bias0 the bias of the first character position,
     *  toward the previous character or the
     *  next character represented by the offset, in case the
     *  position is a boundary of two views; <code>b0</code> will have one
     *  of these values:
     * <ul>
     * <li> <code>Position.Bias.Forward</code>
     * <li> <code>Position.Bias.Backward</code>
     * </ul>
     * @param offset1 the position of the last character (>=0)
     * @param bias1 the bias for the second character position, defined
     *		one of the legal values shown above
     * @param alloc bounds allocated to this view. It should be modified
     *  to contain the resulting bounding box which is a union of the region specified
     *		by the first and last character positions.
     * @see View#viewToModel
     */
    public Shape modelToViewChecked(int offset0, Position.Bias bias0, int offset1, Position.Bias bias1,
            Shape alloc)
    {
        Shape start = modelToViewChecked(offset0, alloc, bias0);
        Shape end = modelToViewChecked(offset1, alloc, bias1);
        Rectangle2D.Double mutableBounds = null;
        if (start != null) {
            mutableBounds = ViewUtils.shape2Bounds(start);
            if (end != null) {
                Rectangle2D endRect = ViewUtils.shapeAsRect(end);
                if (mutableBounds.getY() != endRect.getY()) {
                    Rectangle2D allocRect = ViewUtils.shapeAsRect(alloc);
                    // If it spans lines, force it to be the width of the view.
                    mutableBounds.x = allocRect.getX();
                    mutableBounds.width = allocRect.getWidth();
                }
                mutableBounds.add(endRect);
            }
        }
        return mutableBounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Shape modelToView(int offset0, Position.Bias bias0, int offset1, Position.Bias bias1,
            Shape alloc) throws BadLocationException
    {
        checkBounds(offset0);
        checkBias(bias0);
        checkBounds(offset1);
        checkBias(bias1);
        if (alloc != null) {
            return modelToViewChecked(offset0, bias0, offset1, bias1, alloc);
        } else {
            return null;
        }
    }

    /**
     * Provides a mapping from the view coordinate space to the logical
     * coordinate space of the model. The <code>biasReturn</code>
     * argument will be filled in to indicate that the point given is
     * closer to the next character in the model or the previous
     * character in the model.
     *
     * @param x the X coordinate >= 0
     * @param y the Y coordinate >= 0
     * @param alloc bounds allocated to this view.
     * @return the location within the model that best represents the
     *  given point in the view >= 0.  The <code>biasReturn</code>
     *  argument will be
     * filled in to indicate that the point given is closer to the next
     * character in the model or the previous character in the model.
     */
    public abstract int viewToModelChecked(double x, double y, Shape alloc, Position.Bias[] biasReturn);

    /**
     * {@inheritDoc}
     */
    @Override
    public final int viewToModel(float x, float y, Shape alloc, Position.Bias[] biasReturn) {
        if (alloc != null) {
            return viewToModelChecked((double)x, (double)y, alloc, biasReturn);
        } else {
            return getStartOffset();
        }
    }

    /**
     * Returns the child view index representing the given position in
     * the view. This iterates over all the children returning the
     * first with a bounds that contains <code>x</code>, <code>y</code>.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param alloc current allocation of the View.
     * @return  index of the view representing the given location, or
     *   -1 if no view represents that position
     * @since 1.4
     */
    public int getViewIndexChecked(double x, double y, Shape alloc) {
        return -1; // No subviews by default
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getViewIndex(float x, float y, Shape alloc) {
        if (alloc != null) {
            return getViewIndexChecked((double)x, (double)y, alloc);
        } else {
            return -1;
        }
    }

    @Override
    public Document getDocument() {
        View parent = getParent();
        // By default do not assume non-null element for view construction => return null
        // Possibly use getElement().getDocument() in descendants
        return (parent != null) ? parent.getDocument() : null;
    }

    protected String getDumpName() {
        return "EV"; // EditorView abbrev; NOI18N
    }

    public final String getDumpId() {
        return getDumpName() + "@" + ViewUtils.toStringId(this);
    }

    public void checkIntegrity() {
        if (LOG.isLoggable(Level.FINE)) {
            String err = findTreeIntegrityError(); // Check integrity of the document view
            if (err != null) {
                String msg = "View hierarchy INTEGRITY ERROR! - " + err;
                LOG.fine(msg + "\nErrorneous view hierarchy:\n");
                StringBuilder sb = new StringBuilder(200);
                appendViewInfo(sb, 0, -2); // -2 means detailed info
                LOG.fine(sb.toString());
                // For finest level stop throw real ISE otherwise just log the stack
                if (LOG.isLoggable(Level.FINEST)) {
                    throw new IllegalStateException(msg);
                } else {
                    LOG.log(Level.INFO, msg, new Exception());
                }
            }
        }
    }

    public String findIntegrityError() {
        return null;
    }

    public String findTreeIntegrityError() {
        String err = findIntegrityError();
        if (err == null) { // Check children
            int viewCount = getViewCount();
            int lastOffset = getStartOffset();
            int endOffset = getEndOffset();
            for (int i = 0; i < viewCount; i++) {
                EditorView child = (EditorView) getView(i);
                if (child.getParent() != this) {
                    err = "child.getParent() != this";
                }
                if (err == null) {
                    // Check proper children parenting since e.g. paragraph view may derive
                    // its end offset from last child
                    int childViewCount = child.getViewCount();
                    for (int j = 0; j < childViewCount; j++) {
                        EditorView childChild = (EditorView) child.getView(j);
                        EditorView childChildParent = (EditorView) childChild.getParent();
                        if (childChildParent != child) {
                            String ccpStr = (childChildParent != null) ? childChildParent.getDumpId() : "<NULL>";
                            err = "childChild[" + j + "].getParent()=" + ccpStr + // NOI18N
                                    " != child=" + child.getDumpId(); // NOI18N
                            break;
                        }
                    }
                }
                int childStartOffset = child.getStartOffset();
                int childEndOffset = child.getEndOffset();
                boolean noChildInfo = false;
                if (err == null) {
                    if (childStartOffset != lastOffset) {
                        err = "childStartOffset=" + childStartOffset + ", lastOffset=" + lastOffset; // NOI18N
                    } else if (childStartOffset < 0) {
                        err = "childStartOffset=" + childStartOffset + " < 0"; // NOI18N
                    } else if (childStartOffset > childEndOffset) {
                        err = "childStartOffset=" + childStartOffset + " > childEndOffset=" + childEndOffset; // NOI18N
                    } else if (childEndOffset > endOffset) {
                        err = "childEndOffset=" + childEndOffset + " > parentEndOffset=" + endOffset; // NOI18N
                    } else {
                        err = child.findTreeIntegrityError();
                        noChildInfo = true;
                    }
                }

                if (err != null) {
                    return getDumpId() + "[" + i + "]=" + (noChildInfo ? "" : child.getDumpId() + ": ") + err + '\n';
                }
                lastOffset = childEndOffset;
            }
        }
        return err;
    }

    protected StringBuilder appendViewInfo(StringBuilder sb, int indent, int importantChildIndex) {
        sb.append(getDumpId()).append(':');
        sb.append('<').append(getStartOffset());
        sb.append(',');
        sb.append(getEndOffset()).append('>');
        View parent = getParent();
        if (parent instanceof EditorBoxView) {
            EditorBoxView boxView = (EditorBoxView) parent;
            String axis = (boxView.getMajorAxis() == X_AXIS) ? "X" : "Y";
            sb.append(' ').append(axis).append('=').append(boxView.getViewVisualOffset(this));
            // Also append raw visual offset value
            sb.append("(R").append(getRawVisualOffset()).append(')');
        }
        return sb;
    }

    private void checkBounds(int offset) throws BadLocationException {
        Document doc = getDocument();
        if (offset < 0 || offset > doc.getLength()) {
            throw new BadLocationException("Invalid offset=" + offset + ", docLen=" + doc.getLength(), offset); // NOI18N
        }
    }

    private void checkBias(Position.Bias bias) {
        if (bias == null) { // Position.Bias is final class so only null value is invalid
            throw new IllegalArgumentException("Null bias prohibited.");
        }
    }

    public interface Parent {

        /**
         * Get start offset of a child view based on view's raw offset.
         * @param rawOffset relative child's raw offset.
         * @return real offset.
         */
        int getViewOffset(int rawOffset);

        /**
         * Get cached text layout for the given child view.
         *
         * @param textLayoutView non-null text layout view.
         * @return cached (or created) text layout.
         */
        TextLayout getTextLayout(TextLayoutView textLayoutView);

        /**
         * Get font rendering context that for example may be used for text layout creation.
         * @return font rendering context.
         */
        FontRenderContext getFontRenderContext();

    }

}

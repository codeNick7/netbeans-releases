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
 * Portions Copyrighted 2010 Sun Microsystems, Inc.
 */

package org.netbeans.modules.editor.lib2.highlighting;

import java.awt.Font;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import org.netbeans.lib.editor.util.ArrayUtilities;
import org.netbeans.modules.editor.lib2.view.ViewUtils;

/**
 * List of highlights that can dynamically add/remove existing highlights fed from a highlights sequence.
 *
 * @author Miloslav Metelka
 */
public final class HighlightsList {
    
    // -J-Dorg.netbeans.modules.editor.lib2.highlighting.HighlightsList.level=FINE
    private static final Logger LOG = Logger.getLogger(HighlightsList.class.getName());

    /**
     * List of highlight items. First highlights item starts at startOffset.
     * <br>
     * GapList is used due to its copyElements() method.
     */
    private HighlightItem[] highlightItems;
    
    private int startIndex;
    
    private int endIndex;
    
    /**
     * Start offset of the first highlight item.
     */
    private int startOffset;
    
    public HighlightsList(int startOffset) {
        this.highlightItems = new HighlightItem[4];
        this.startOffset = startOffset;
    }
    
    public HighlightsList(int startOffset, HighlightItem[] items) {
        this.highlightItems = items;
        this.endIndex = items.length;
        this.startOffset = startOffset;
    }
    
    public int startOffset() {
        return startOffset;
    }
    
    public int endOffset() {
        return (endIndex - startIndex > 0)
                ? highlightItems[endIndex - 1].getEndOffset()
                : startOffset;
    }
    
    public int endSplitOffset() {
        return (endIndex - startIndex > 0)
                ? highlightItems[endIndex - 1].getEndSplitOffset()
                : 0;
    }
    
    public int size() {
        return endIndex - startIndex;
    }
    
    public HighlightItem get(int index) {
        if (startIndex + index >= endIndex) {
            throw new IndexOutOfBoundsException("index=" + index + " >= size=" + size() + ", " + this); // NOI18N
        }
        return highlightItems[startIndex + index];
    }

    public void add(HighlightItem item) {
        if (endIndex == highlightItems.length) {
            if (startIndex == 0) {
                HighlightItem[] tmp = new HighlightItem[highlightItems.length << 1];
                System.arraycopy(highlightItems, 0, tmp, 0, highlightItems.length);
                highlightItems = tmp;
            } else { // Make startIndex == 0
                System.arraycopy(highlightItems, startIndex, highlightItems, 0, size());
                endIndex -= startIndex;
                startIndex = 0;
            }
        }
        highlightItems[endIndex++] = item;
    }

    /**
     * Create attribute set covering {@link #startOffset()} till maxEndOffset
     * or lower offset if font would differ for the particular attribute set
     * of an item.
     * <br>
     * The list must cover cutEndOffset otherwise the behavior is undefined.
     *
     * @param defaultFont default font to which the attributes in highlight items
     *  are added to get the resulting font.
     * @param maxEndOffset maximum end offset where the cutting must end.
     * @param wsEndOffset whitespace end offset must be lower than or equal to maxEndOffset
     *  and when exceeded a first whitespace char in docText means that the cutting will end there.
     * @param docText document text in order properly handle wsEndOffset parameter.
     * @return either simple or compound attribute set.
     */
    public AttributeSet cutSameFont(Font defaultFont, int maxEndOffset, int wsEndOffset, CharSequence docText) {
        assert (maxEndOffset <= endOffset()) :
                "maxEndOffset=" + maxEndOffset + " > endOffset()=" + endOffset() + ", " + this; // NOI18N
        HighlightItem item = get(0);
        AttributeSet firstAttrs = item.getAttributes();
        int itemEndOffset = item.getEndOffset();
        if (wsEndOffset <= itemEndOffset) {
            if (wsEndOffset == maxEndOffset) {
                if (maxEndOffset == itemEndOffset) {
                    cutStartItems(1);
                }
                startOffset = maxEndOffset;
                return firstAttrs;

            } else { // wsEndOffset < maxEndOffset (see javadoc above)
                // Search for whitespace starting from wsEndOffset
                int limitOffset = Math.min(maxEndOffset, itemEndOffset);
                for (int offset = wsEndOffset; offset < limitOffset; offset++) {
                    if (Character.isWhitespace(docText.charAt(offset))) {
                        startOffset = offset;
                        return firstAttrs;
                    }
                }
                if ((maxEndOffset > itemEndOffset && Character.isWhitespace(docText.charAt(itemEndOffset))) ||
                    maxEndOffset == itemEndOffset)
                {
                    cutStartItems(1);
                    startOffset = itemEndOffset;
                    return firstAttrs;
                } else if (maxEndOffset < itemEndOffset) {
                    startOffset = maxEndOffset;
                    return firstAttrs;
                } // else: for non-WS at itemEndOffset continue to next hItem
            }
        }

        // Extends beyond first highlight
        Font firstFont = ViewUtils.getFont(firstAttrs, defaultFont);
        int index = 1;
        while (true) {
            item = get(index);
            AttributeSet attrs = item.getAttributes();
            Font font = ViewUtils.getFont(attrs, defaultFont);
            if (!font.equals(firstFont)) { // Stop at itemEndOffset
                if (index == 1) { // Just single attribute set
                    cutStartItems(1);
                    startOffset = itemEndOffset; // end offset of first item
                    return firstAttrs;
                }
                // Index > 1
                return cutCompound(index, itemEndOffset); // end offset of first item
            }
            int itemStartOffset = itemEndOffset;
            itemEndOffset = item.getEndOffset();
            if (wsEndOffset <= itemEndOffset) {
                if (wsEndOffset == maxEndOffset) {
                    if (maxEndOffset == itemEndOffset) {
                        return cutCompound(index + 1, itemEndOffset);
                    }
                    return cutCompoundAndPart(index, maxEndOffset, attrs);

                } else { // wsEndOffset < maxEndOffset (see javadoc above)
                    int offset = Math.max(itemStartOffset, wsEndOffset);
                    int limitOffset = Math.min(maxEndOffset, itemEndOffset);
                    for (; offset < limitOffset; offset++) {
                        if (Character.isWhitespace(docText.charAt(offset))) {
                            return cutCompoundAndPart(index, offset, attrs);
                        }
                    }
                    if ((maxEndOffset > itemEndOffset && Character.isWhitespace(docText.charAt(itemEndOffset)))
                            || maxEndOffset == itemEndOffset)
                    {
                        return cutCompound(index + 1, itemEndOffset);
                    } else if (maxEndOffset < itemEndOffset) {
                        return cutCompoundAndPart(index, maxEndOffset, attrs);
                    } // else: for non-WS at itemEndOffset continue to next hItem
                }
            }
            index++;
        }
    }
    
    public AttributeSet cut(int endOffset) {
        assert (endOffset <= endOffset()) :
                "endOffset=" + endOffset + " > endOffset()=" + endOffset() + ", " + this; // NOI18N
        HighlightItem item = get(0);
        AttributeSet attrs = item.getAttributes();
        int itemEndOffset = item.getEndOffset();
        if (endOffset <= itemEndOffset) {
            if (endOffset == itemEndOffset) {
                cutStartItems(1);
            }
            startOffset = endOffset;
            return attrs;
        }
        // Span two or more highlights
        int index = 1;
        while (true) {
            item = get(index);
            itemEndOffset = item.getEndOffset();
            if (endOffset <= itemEndOffset) {
                if (endOffset == itemEndOffset) {
                    return cutCompound(index + 1, itemEndOffset);
                }
                return cutCompoundAndPart(index, endOffset, item.getAttributes());
            }
            index++;
        }
    }
    
    /**
     * Create attribute set covering single character at {@link #startOffset()}.
     * <br>
     * The list must cover {@link #startOffset()} otherwise the behavior is undefined.
     *
     * @return attribute set.
     */
    public AttributeSet cutSingleChar() {
        HighlightItem item = get(0);
        startOffset++;
        if (startOffset == item.getEndOffset()) {
            cutStartItems(1);
        }
        return item.getAttributes();
    }
    
    public void skip(int newStartOffset) {
        HighlightItem item = get(0);
        int itemEndOffset = item.getEndOffset();
        if (newStartOffset <= itemEndOffset) {
            if (newStartOffset == itemEndOffset) {
                cutStartItems(1);
            }
        } else {
            int index = 1;
            while (true) {
                item = get(index);
                itemEndOffset = item.getEndOffset();
                if (newStartOffset <= itemEndOffset) {
                    if (newStartOffset == itemEndOffset) {
                        cutStartItems(index + 1);
                    } else {
                        cutStartItems(index);
                    }
                    break;
                }
                index++;
            }
        }
        startOffset = newStartOffset;
    }

    private void cutStartItems(int count) {
        startIndex += count;
    }
    
    private CompoundAttributes cutCompound(int count, int lastItemEndOffset) {
        HighlightItem[] cutItems = new HighlightItem[count];
        System.arraycopy(highlightItems, startIndex, cutItems, 0, count);
        cutStartItems(count);
        CompoundAttributes cAttrs = new CompoundAttributes(startOffset, cutItems);
        startOffset = lastItemEndOffset;
        return cAttrs;
    }
    
    private CompoundAttributes cutCompoundAndPart(int count, int cutEndOffset, AttributeSet lastAttrs) {
        HighlightItem[] cutItems = new HighlightItem[count + 1];
        cutItems[count] = new HighlightItem(cutEndOffset, lastAttrs);
        System.arraycopy(highlightItems, startIndex, cutItems, 0, count);
        cutStartItems(count);
        CompoundAttributes cAttrs = new CompoundAttributes(startOffset, cutItems);
        startOffset = cutEndOffset;
        return cAttrs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append("HL:<").append(startOffset()).append(",").append(endOffset()).append(">");
        sb.append(", Items#").append(size()).append("\n");
        int size = size();
        int digitCount = ArrayUtilities.digitCount(size);
        int lastOffset = startOffset;
        for (int i = 0; i < size; i++) {
            ArrayUtilities.appendBracketedIndex(sb, i, digitCount);
            HighlightItem item = get(i);
            sb.append(item.toString(lastOffset));
            sb.append('\n');
            lastOffset = item.getEndOffset();
        }
        return sb.toString();
    }

}

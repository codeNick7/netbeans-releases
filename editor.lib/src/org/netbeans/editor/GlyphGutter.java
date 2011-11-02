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

package org.netbeans.editor;

import java.awt.Shape;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.text.BadLocationException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.accessibility.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;
import org.netbeans.api.editor.fold.FoldHierarchy;
import org.netbeans.api.editor.fold.FoldHierarchyEvent;
import org.netbeans.api.editor.fold.FoldHierarchyListener;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.settings.EditorStyleConstants;
import org.netbeans.api.editor.settings.FontColorNames;
import org.netbeans.api.editor.settings.FontColorSettings;
import org.netbeans.modules.editor.lib.ColoringMap;
import org.netbeans.modules.editor.lib2.view.LockedViewHierarchy;
import org.netbeans.modules.editor.lib2.view.ParagraphViewDescriptor;
import org.netbeans.modules.editor.lib2.view.ViewHierarchy;
import org.openide.ErrorManager;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.RequestProcessor.Task;
import org.openide.util.WeakListeners;

/** GlyphGutter is component for displaying line numbers and annotation
 * glyph icons. Component also allow to "cycle" through the annotations. It
 * means that if there is more than one annotation on the line, only one of them
 * might be visible. And clicking the special cycling button in the gutter the user
 * can cycle through the annotations.
 *
 * @author  David Konecny
 * @since 07/2001
 */

public class GlyphGutter extends JComponent implements Annotations.AnnotationsListener, Accessible, SideBarFactory {

    private static final Logger LOG = Logger.getLogger(GlyphGutter.class.getName());
    
    private static final String TEXT_ZOOM_PROPERTY = "text-zoom"; // Maintained by DocumentView

    /** EditorUI which part this gutter is */
    private volatile EditorUI editorUI;
    
    /** Annotations manager responsible for annotations for this line */
    private Annotations annos;
    
    /** Cycling button image */
    private Image gutterButton;
    
    /** Background color of the gutter */
    private Color backgroundColor;
    
    /** Foreground color of the gutter. Used for drawing line numbers. */
    private Color foreColor;
    
    /** Font used for drawing line numbers */
    private Font font;
    
    /** Flag whether the gutter was initialized or not. The painting is disabled till the
     * gutter is not initialized */
    private boolean init;
    
    /** Width of the glyph gutter*/ 
    private int glyphGutterWidth;

    /** Predefined width of the glyph icons */
    private final static int glyphWidth = 16;

    /** Predefined width of the cycling button */
    private final static int glyphButtonWidth = 9;
    
    /** Predefined left area width - area between left border of the number
     *  and the left border of the glyphgutter
     */
    private final static int leftGap= 10;

    /** Predefined right area width - area between right border of the number
     *  and the right border of the glyphgutter
     */
    private final static int rightGap= 4;
    
    /** Whether the line numbers are shown or not */
    private boolean showLineNumbers = true;
    
    /** The gutter height is enlarged by number of lines which specifies this constant */
    private static final int ENLARGE_GUTTER_HEIGHT = 300;
    
    /** The highest line number. This value is used for calculating width of the gutter */
    private int highestLineNumber = 0;
    
    /** Whether the annotation glyph can be drawn over the line numbers */
    private boolean drawOverLineNumbers = false;

    /* These two variables are used for caching of count of line annos 
     * on the line over which is the mouse caret. Just for sake of optimalization. */
    private volatile int cachedCountOfAnnos = -1;
    private int cachedCountOfAnnosForLine = -1;

    /** Property change listener on AnnotationTypes changes */
    private PropertyChangeListener annoTypesListener;
    private PropertyChangeListener editorUIListener;
    private GlyphGutter.GlyphGutterFoldHierarchyListener glyphGutterFoldHierarchyListener;
    private GutterMouseListener gutterMouseListener;
    private FoldHierarchy foldHierarchy;

    private ColoringMap coloringMap;
    private final PropertyChangeListener coloringMapListener = new PropertyChangeListener() {
        public @Override void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName() == null || ColoringMap.PROP_COLORING_MAP.equals(evt.getPropertyName())) {
                update();
            }
        }
    };
    
    private Preferences prefs = null;
    private final PreferenceChangeListener prefsListener = new PreferenceChangeListener() {
        public @Override void preferenceChange(PreferenceChangeEvent evt) {
            EditorUI eui = editorUI;
            JTextComponent c = eui == null ? null : eui.getComponent();
            Rectangle rect = c == null ? null : c.getVisibleRect();
            if (rect != null && rect.width == 0) {
                if (SwingUtilities.isEventDispatchThread()) {
                    resize();
                } else {
                    SwingUtilities.invokeLater(
                        new Runnable() {
                            public @Override void run() {
                                resize();
                            }
                        }
                    );
                }
            }
        }
    };
    
    public GlyphGutter(){}
    
    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public GlyphGutter(EditorUI eui) {
        super();
        this.editorUI = eui;
        init = false;
        annos = eui.getDocument().getAnnotations();
        
        // Annotations class is model for this view, so the listener on changes in
        // Annotations must be added here
        annos.addAnnotationsListener(this);

        // do initialization
        init();
        update();
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        foldHierarchy = FoldHierarchy.get(eui.getComponent());
        glyphGutterFoldHierarchyListener = new GlyphGutterFoldHierarchyListener();
        foldHierarchy.addFoldHierarchyListener(glyphGutterFoldHierarchyListener);
        editorUIListener = new EditorUIListener();
        eui.addPropertyChangeListener(editorUIListener);
        eui.getComponent().addPropertyChangeListener(editorUIListener);
        setOpaque (true);
        
        String mimeType = org.netbeans.lib.editor.util.swing.DocumentUtilities.getMimeType(eui.getComponent());
        coloringMap = ColoringMap.get(mimeType);
        coloringMap.addPropertyChangeListener(WeakListeners.propertyChange(coloringMapListener, coloringMap));

        prefs = MimeLookup.getLookup(mimeType).lookup(Preferences.class);
        prefs.addPreferenceChangeListener(WeakListeners.create(PreferenceChangeListener.class, prefsListener, prefs));
        prefsListener.preferenceChange(null);
    }
    
    /* Read accessible context
     * @return - accessible context
     */
    public @Override AccessibleContext getAccessibleContext () {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleJComponent() {
                public @Override AccessibleRole getAccessibleRole() {
                    return AccessibleRole.PANEL;
                }
            };
        }
        return accessibleContext;
    }

    /** Do initialization of the glyph gutter*/
    protected void init() {
        if (editorUI == null)
            return ;

        gutterButton = ImageUtilities.loadImage("org/netbeans/editor/resources/glyphbutton.gif");

        setToolTipText ("");
        getAccessibleContext().setAccessibleName(NbBundle.getBundle(BaseKit.class).getString("ACSN_Glyph_Gutter")); // NOI18N
        getAccessibleContext().setAccessibleDescription(NbBundle.getBundle(BaseKit.class).getString("ACSD_Glyph_Gutter")); // NOI18N

        // add mouse listener for cycling button
        // TODO: clicking the line number should select whole line
        // TODO: clicking the line number abd dragging the mouse should select block of lines
        gutterMouseListener = new GutterMouseListener ();
        addMouseListener (gutterMouseListener);
        addMouseMotionListener (gutterMouseListener);

        AnnotationTypes.getTypes().addPropertyChangeListener( annoTypesListener = new PropertyChangeListener() {
            public @Override void propertyChange (PropertyChangeEvent evt) {
                if (evt.getPropertyName() == null ||
                    evt.getPropertyName().equals(AnnotationTypes.PROP_GLYPHS_OVER_LINE_NUMBERS) ||
                    evt.getPropertyName().equals(AnnotationTypes.PROP_SHOW_GLYPH_GUTTER))
                {
                    update();
                }
            }
        });
        
    }
    
    /** Update colors, fonts, sizes and invalidate itself. This method is
     * called from EditorUI.update() */
    public void update() {
        EditorUI eui = editorUI;
        if (eui == null)
            return ;
        Coloring lineColoring = eui.getColoringMap().get(FontColorNames.LINE_NUMBER_COLORING);
        Coloring defaultColoring = eui.getDefaultColoring();
        
        // fix for issue #16940
        // the real cause of this problem is that closed document is not garbage collected, 
        // because of *some* references (see #16072) and so any change in AnnotationTypes.PROP_*
        // properties is fired which must update this component although it is not visible anymore
        if (lineColoring == null)
            return;


        final Color backColor = lineColoring.getBackColor();
        // set to white by o.n.swing.plaf/src/org/netbeans/swing/plaf/aqua/AquaLFCustoms
        if (org.openide.util.Utilities.isMac()) {
            backgroundColor = backColor;
        } else {
            backgroundColor = UIManager.getColor("NbEditorGlyphGutter.background"); //NOI18N
        }
        if( null == backgroundColor ) {
            if (backColor != null)
                backgroundColor = backColor;
            else
                backgroundColor = defaultColoring.getBackColor();
        }

        if (lineColoring.getForeColor() != null)
            foreColor = lineColoring.getForeColor();
        else
            foreColor = defaultColoring.getForeColor();
    
        if (lineColoring.getFont() != null) {
            Font lineFont = lineColoring.getFont();
            font = (lineFont != null) ? lineFont.deriveFont((float)lineFont.getSize()-1) : null;
        } else {
            font = defaultColoring.getFont();
            font = new Font("Monospaced", Font.PLAIN, font.getSize()-1); //NOI18N
        }
        
        JTextComponent tc = eui.getComponent();
        if (tc != null) {
            Integer textZoom = (Integer) tc.getClientProperty(TEXT_ZOOM_PROPERTY);
            if (textZoom != null && textZoom != 0) {
                font = new Font(font.getFamily(), font.getStyle(), Math.max(font.getSize() + textZoom, 2));
            }
        }

        showLineNumbers = eui.lineNumberVisibleSetting;
        drawOverLineNumbers = AnnotationTypes.getTypes().isGlyphsOverLineNumbers().booleanValue();
        
        init = true;

        // initialize the value with current number of lines
        highestLineNumber = getLineCount();
        
        repaint();
        resize();
    }
    
   
    protected void resize() {
        EditorUI eui = editorUI;
        if (eui != null) {
            Dimension dim = new Dimension();
            glyphGutterWidth = getWidthDimension();
            dim.width = glyphGutterWidth;
            dim.height = getHeightDimension();


            // enlarge the gutter so that inserting new lines into
            // document does not cause resizing too often
            dim.height += ENLARGE_GUTTER_HEIGHT * eui.getLineHeight();

            setPreferredSize(dim);

            revalidate();
            putDimensionForPrinting();
        }
    }

    /** Return number of lines in the document */
    protected int getLineCount() {
        int lineCnt;
        EditorUI eui = editorUI;
        try {
            BaseDocument document = eui != null ? eui.getDocument() : null;
            if (document != null) {
                document.readLock();
                try {
                    lineCnt = Utilities.getLineOffset(document, document.getLength()) + 1;
                } finally {
                    document.readUnlock();
                }
            } else { // deactivated
                lineCnt = 1;
            }
        } catch (BadLocationException e) {
            lineCnt = 1;
        }
        return lineCnt;
    }

    /** Gets number of digits in the number */
    protected int getDigitCount(int number) {
        return Integer.toString(number).length();
    }

    protected int getLineNumberWidth() {
        int newWidth = 0;
        EditorUI eui = editorUI;
        if (eui != null) {
            /*
            Insets insets = eui.getLineNumberMargin();
            if (insets != null) {
                newWidth += insets.left + insets.right;
            }
             */
            JTextComponent tc = eui.getComponent();
            if (font != null && tc != null) {
                Graphics g;
                FontRenderContext frc;
                FontMetrics fm;
                if ((g = tc.getGraphics()) != null && (g instanceof Graphics2D) &&
                    (frc = ((Graphics2D)g).getFontRenderContext()) != null)
                {
                    newWidth += new TextLayout(String.valueOf(highestLineNumber), font, frc).getAdvance();
                } else if ((fm = getFontMetrics(font)) != null) {
                    // Use FontMetrics.stringWidth() as best approximation
                    newWidth += fm.stringWidth(String.valueOf(highestLineNumber));
                }
            }
        }
        
        return newWidth;
    }
    
    protected int getWidthDimension() {
        int newWidth = 0;
        
        if (showLineNumbers) {
            int lineNumberWidth = getLineNumberWidth();
            newWidth = leftGap + lineNumberWidth + rightGap;
        } else {
            if (editorUI != null) {
                if (annos.isGlyphColumn() || 
                        AnnotationTypes.getTypes().isShowGlyphGutter().booleanValue()){
                    newWidth += glyphWidth;
                }

                if (annos.isGlyphButtonColumn()){
                             newWidth += glyphButtonWidth;
                }
            }
        }

        return newWidth;
    }
    
    protected int getHeightDimension() {
        EditorUI eui = editorUI;
        if (eui == null)
            return 0;
        JComponent comp = eui.getComponent();
        if (comp == null)
            return 0;
        return (int)comp.getSize().getHeight(); // + highestLineNumber * eui.getLineHeight()
    }
    
    private static final Color DEFAULT_GUTTER_LINE = new Color(184, 184, 184);
    
    /** Paint the gutter itself */
    public @Override void paintComponent(final Graphics g) {
        super.paintComponent(g);
        EditorUI eui = editorUI;
        if (eui == null)
            return ;

        // Possibly apply the rendering hints
        String mimeType = org.netbeans.lib.editor.util.swing.DocumentUtilities.getMimeType(eui.getComponent());
        FontColorSettings fcs = MimeLookup.getLookup(mimeType).lookup(FontColorSettings.class);
        Map hints = (Map) fcs.getFontColors(FontColorNames.DEFAULT_COLORING).getAttribute(EditorStyleConstants.RenderingHints);
        if (!hints.isEmpty()) {
            ((java.awt.Graphics2D)g).addRenderingHints(hints);
        }
        
        // if the gutter was not initialized yet, skip the painting        
        if (!init) return;
        
        final Rectangle clip = g.getClipBounds();   

        final JTextComponent component = eui.getComponent();
        if (component == null) return;
        
        BaseTextUI textUI = (BaseTextUI)component.getUI();
        View rootView = Utilities.getDocumentView(component);
        if (rootView == null) return;
      
        g.setColor(backgroundColor);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        
        //painting gutter line
        g.setColor(DEFAULT_GUTTER_LINE);
        g.drawLine(glyphGutterWidth-1, clip.y, glyphGutterWidth-1, clip.height + clip.y);

        final Document doc = component.getDocument();
        doc.render(new Runnable() {
            @Override
            public void run() {
                ViewHierarchy vh = ViewHierarchy.get(component);
                LockedViewHierarchy lockedVH = vh.lock();
                try {
                    int pViewIndex = lockedVH.yToParagraphViewIndex(clip.y);
                    if (pViewIndex >= 0) {
                        int pViewCount = lockedVH.getParagraphViewCount();
                        int repaintWidth = (int) getSize().getWidth();
                        int endRepaintY = clip.y + clip.height;
                        Element lineElementRoot = doc.getDefaultRootElement();
                        ParagraphViewDescriptor pViewDesc = lockedVH.getParagraphViewDescriptor(pViewIndex);
                        int pViewStartOffset = pViewDesc.getStartOffset();
                        int lineIndex = lineElementRoot.getElementIndex(pViewStartOffset);;
                        int lineEndOffset = lineElementRoot.getElement(lineIndex).getEndOffset();
                        int lineWithAnno = -1;
                        float rowHeight = lockedVH.getDefaultRowHeight();
                        int lineNumberMaxWidth = getLineNumberWidth();
                        g.setFont(font);
                        g.setColor(foreColor);
                        FontMetrics fm = FontMetricsCache.getFontMetrics(font, g);

                        while (true) {
                            Shape pViewAlloc = pViewDesc.getAllocation();
                            Rectangle pViewRect = pViewAlloc.getBounds();
                            pViewRect.width = repaintWidth;
                            if (pViewRect.y >= endRepaintY) {
                                break;
                            }
                            while (pViewStartOffset >= lineEndOffset) {
                                lineIndex++;
                                lineEndOffset = lineElementRoot.getElement(lineIndex).getEndOffset();
                                lineWithAnno = -1;
                            }
                            String lineNumberString = String.valueOf(lineIndex + 1);
                            int lineNumberWidth = fm.stringWidth(lineNumberString);
                            if (lineWithAnno == -1) {
                                lineWithAnno = annos.getNextLineWithAnnotation(lineIndex);
                            }
                            int annoCount;
                            AnnotationDesc annoDesc;
                            Image annoGlyph;
                            if (lineWithAnno == lineIndex) {
                                annoCount = annos.getNumberOfAnnotations(lineIndex);
                                annoDesc = annos.getActiveAnnotation(lineIndex);
                                annoGlyph = annoDesc != null ? annoDesc.getGlyph() : null;
                            } else {
                                annoCount = 0;
                                annoDesc = null;
                                annoGlyph = null;
                            }

                            if (showLineNumbers) {
                                boolean glyphHasIcon = false;
                                if (lineIndex == lineWithAnno) {
                                    if (annoDesc != null && !(annoDesc.isDefaultGlyph() &&
                                            annoCount == 1) && annoGlyph != null)
                                    {
                                        glyphHasIcon = true;
                                    }
                                }
                                if ((!glyphHasIcon)
                                        || (!drawOverLineNumbers)
                                        || (drawOverLineNumbers && lineIndex != lineWithAnno))
                                {
                                    // Use simplified painting (non-TextLayout) since TextLayout creation
                                    // is rather expensive and with line numbers there is no RTL issue
                                    // or other complexities where TextLayout would be useful.
                                    g.drawString(lineNumberString,
                                            glyphGutterWidth - lineNumberWidth - rightGap,
                                            (int) Math.round(pViewRect.y + pViewDesc.getAscent())
                                    );
                                }
                            }

                            // draw anotation if we get to the line with some annotation
                            if (lineIndex == lineWithAnno) {
                                int xPos = (showLineNumbers) ? lineNumberMaxWidth : 0;
                                if (drawOverLineNumbers) {
                                    xPos = getWidth() - glyphWidth;
                                    if (annoCount > 1) {
                                        xPos -= glyphButtonWidth;
                                    }
                                }

                                if (annoGlyph != null) {
                                    int glyphHeight = annoGlyph.getHeight(null);
                                    // draw the glyph only when the annotation type has its own icon (no the default one)
                                    // or in case there is more than one annotations on the line
                                    if (!(annoCount == 1 && annoDesc.isDefaultGlyph())) {
                                        g.drawImage(
                                                annoGlyph,
                                                xPos,
                                                (int) Math.round(pViewRect.y + (rowHeight - glyphHeight) / 2 + 1),
                                                null
                                        );
                                    }

                                    // draw cycling button if there is more than one annotations on the line
                                    if (annoCount > 1) {
                                        g.drawImage(
                                                gutterButton,
                                                xPos + glyphWidth - 1,
                                                (int) Math.round(pViewRect.y + (rowHeight - glyphHeight) / 2),
                                                null
                                        );
                                    }
                                }
                                lineWithAnno = -1;
                            }

                            // Go to next paragraph view
                            pViewIndex++;
                            if (pViewIndex >= pViewCount) {
                                break;
                            }
                            pViewDesc = lockedVH.getParagraphViewDescriptor(pViewIndex);
                            pViewStartOffset = pViewDesc.getStartOffset();

                        }
                    }
                } finally {
                    lockedVH.unlock();
                }
            }
        });
    }

    private Rectangle toRepaint = null;
    private final Object toRepaintLock = new Object();
    private static final int REPAINT_TASK_DELAY = 50;
    private final Task repaintTask = new RequestProcessor(GlyphGutter.class.getName(), 1, false, false).create(new Runnable() {
        @Override public void run() {
            Rectangle repaint;
            
            synchronized (toRepaintLock) {
                repaint = toRepaint;
                toRepaint = null;
            }
            
            if (repaint != null) {
                doRepaint(repaint);
            }
        }
    });
    
    private void doRepaint(Rectangle r) {
        repaint(r);
        checkSize();
    }
    
    /** Data for the line has changed and the line must be redraw. */
    public @Override void changedLine(final int lineIndex) {
        EditorUI eui = editorUI;
        if (!init || eui == null)
            return;

        // reset cache if there was some change
        cachedCountOfAnnos = -1;
        
        // redraw also lines around - three lines will be redrawn
        final JTextComponent component = eui.getComponent();
        if (component!=null){
            final Document doc = component.getDocument();
            doc.render(new Runnable() {
                @Override
                public void run() {
                    Element rootElem = doc.getDefaultRootElement();
                    if (lineIndex >= rootElem.getElementCount()) { // #42504
                        return;
                    }
                    Element lineElem = rootElem.getElement(lineIndex);
                    ViewHierarchy vh = ViewHierarchy.get(component);
                    LockedViewHierarchy lvh = vh.lock();
                    try {
                        int pViewIndex = lvh.modelToParagraphViewIndex(lineElem.getStartOffset());
                        if (pViewIndex >= 0) {
                            ParagraphViewDescriptor pViewDesc = lvh.getParagraphViewDescriptor(pViewIndex);
                            Shape pViewAlloc = pViewDesc.getAllocation();
                            Rectangle repaintRect = pViewAlloc.getBounds();
                            repaintRect.width = (int) getSize().getWidth();
                            if (LOG.isLoggable(Level.FINE)) {
                                LOG.fine("GlyphGutter.changedLine() lineIndex=" + lineIndex + // NOI18N
                                        ", repaintRect=" + repaintRect + "\n"); // NOI18N
                            }
                            //ensuring that the actual repaint will happen in the AWT thread, to prevent deadlocks inside repaint(...)
                            //coalesce the events, so that the AWT thread is not hogged by these requests:
                            if (SwingUtilities.isEventDispatchThread()) {
                                doRepaint(repaintRect);
                            } else {
                                synchronized (toRepaintLock) {
                                    toRepaint = toRepaint != null ? toRepaint.union(repaintRect) : repaintRect;
                                    repaintTask.schedule(REPAINT_TASK_DELAY);
                                }
                            }
                        }
                    } finally {
                        lvh.unlock();
                    }
                }
            });
        }
    }

    /** Repaint whole gutter.*/
    public @Override void changedAll() {

        if (!init || editorUI == null)
            return;

        // reset cache if there was some change
        cachedCountOfAnnos = -1;
        
        // This method is called from the same thread as doc.insertString/remove() is done.
        // Ensure the following runs in EDT.
        Utilities.runInEventDispatchThread(new Runnable() {
            public @Override void run() {
                repaint();
                checkSize();
            }
        });
    }

    /** Check whether it is not necessary to resize the gutter */
    protected void checkSize() {
        int count = getLineCount();
        if (count != highestLineNumber) {
            highestLineNumber = count;
        }
        Dimension dim = getPreferredSize();
        if (getWidthDimension() != dim.width ||
            getHeightDimension() > dim.height) {
            resize();
        }
        putDimensionForPrinting();
    }

    private void putDimensionForPrinting() {
        // This code doesn't affect on drawning of this component
        // and doesn't change any functionality. The purpose is to
        // pass actual width and height of the component to Print
        // Preview dialog when sources are printed, see issue #178357.
        // More details of Print API can be found in print module.
        putClientProperty("print.size", new Dimension(getWidthDimension(), getHeightDimension())); // NOI18N
    }

    /** Get tooltip text for the mouse position */
    // TODO: does not work for asynchronous tooltip texts
    public @Override String getToolTipText (MouseEvent e) {
        if (editorUI == null)
            return null;
        int line = getLineFromMouseEvent(e);
        if (annos.getNumberOfAnnotations(line) == 0)
            return null;
        if (isMouseOverCycleButton(e) && annos.getNumberOfAnnotations(line) > 1) {
            return java.text.MessageFormat.format (
                NbBundle.getBundle(BaseKit.class).getString ("cycling-glyph_tooltip"), //NOI18N
                new Object[] { new Integer (annos.getNumberOfAnnotations(line)) });
        }
        else if (isMouseOverGlyph(e)) {
            return annos.getActiveAnnotation(line).getShortDescription();
        }
        else
            return null;
    }

    /** Count the X position of the glyph on the line. */
    private int getXPosOfGlyph(int line) {
        if (editorUI == null) {
            return -1;
        }

        if (cachedCountOfAnnos == -1 || cachedCountOfAnnosForLine != line) {
            cachedCountOfAnnos = annos.getNumberOfAnnotations(line);
            cachedCountOfAnnosForLine = line;
        }

        if (cachedCountOfAnnos > 0) {
            int xPos = (showLineNumbers) ? getLineNumberWidth() : 0;
            if (drawOverLineNumbers) {
                xPos = getWidth() - glyphWidth;
                if (cachedCountOfAnnos > 1) {
                     xPos -= glyphButtonWidth;
                }
            }
            return xPos;
        } else {
            return -1;
        }
    }

    /** Check whether the mouse is over some glyph icon or not */
    private boolean isMouseOverGlyph(MouseEvent e) {
        int line = getLineFromMouseEvent(e);
        int xPos = getXPosOfGlyph(line);
        if (xPos != -1 && e.getX() >= xPos && e.getX() <= xPos + glyphWidth) {
            return true;
        } else {
            return false;
        }
    }
    
    /** Check whether the mouse is over the cycling button or not */
    private boolean isMouseOverCycleButton(MouseEvent e) {
        int line = getLineFromMouseEvent(e);
        int xPos = getXPosOfGlyph(line);
        if (xPos != -1 && e.getX() >= xPos + glyphWidth && e.getX() <= xPos + glyphWidth + glyphButtonWidth) {
            return true;
        } else {
            return false;
        }
    }

    public @Override JComponent createSideBar(JTextComponent target) {
        EditorUI eui = Utilities.getEditorUI(target);
        if (eui == null){
            return null;
        }
        GlyphGutter glyph = new GlyphGutter(eui);
        eui.setGlyphGutter(glyph);
        return glyph;
    }
    
    private int getLineFromMouseEvent(MouseEvent e){
        int line = -1;
        EditorUI eui = editorUI;
        if (eui != null) {
            try{
                JTextComponent component = eui.getComponent();
                BaseDocument document = eui.getDocument();
                BaseTextUI textUI = (BaseTextUI)component.getUI();
                int clickOffset = textUI.viewToModel(component, new Point(0, e.getY()));
                line = Utilities.getLineOffset(document, clickOffset);
            }catch (BadLocationException ble) {
                LOG.log(Level.WARNING, null, ble);
            }
        }
        return line;
    }
    
    class GutterMouseListener extends MouseAdapter implements MouseMotionListener {
        
        /** start line of the dragging. */
        private int dragStartOffset = -1;
        /** end line of the dragging. */
        private int dragEndOffset;

        public @Override void mouseClicked(MouseEvent e) {
            EditorUI eui = editorUI;
            if (eui==null)
                return;
            // cycling button was clicked by left mouse button
            if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
                if (isMouseOverCycleButton(e)) {
                    int line = getLineFromMouseEvent(e);
                    e.consume();
                    annos.activateNextAnnotation(line);
                } else {
                    Action actions[] = ImplementationProvider.getDefault().getGlyphGutterActions(eui.getComponent());
                    if (actions != null && actions.length >0) {
                        Action a = actions[0]; //TODO - create GUI chooser
                        if (a!=null && a.isEnabled()){
                            int currentLine = -1;
                            int line = getLineFromMouseEvent(e);
                            if (line == -1) return;
                            BaseDocument document = eui.getDocument();
                            try {
                                currentLine = Utilities.getLineOffset(document, eui.getComponent().getCaret().getDot());
                            } catch (BadLocationException ex) {
                                return;
                            }
                            if (line != currentLine) {
                                int offset = Utilities.getRowStartFromLineOffset(document, line);
                                JumpList.checkAddEntry();
                                eui.getComponent().getCaret().setDot(offset);
                            }
                            e.consume();
                            a.actionPerformed(new ActionEvent(eui.getComponent(), 0, ""));
                            repaint();
                        }
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        }

        private void showPopup(MouseEvent e) {
            final EditorUI eui = editorUI;
            if (eui == null)
                return;
            // annotation glyph was clicked by right mouse button
            if (e.isPopupTrigger()) {
                int line = getLineFromMouseEvent(e);
                int offset;
                if (annos.getActiveAnnotation(line) != null) {
                    offset = annos.getActiveAnnotation(line).getOffset();
                } else {
                    BaseDocument document = eui.getDocument();
                    offset = Utilities.getRowStartFromLineOffset(document, line);
                }
                if (eui.getComponent().getCaret().getDot() != offset)
                    JumpList.checkAddEntry();
                eui.getComponent().getCaret().setDot(offset);
                JPopupMenu pm = annos.createPopupMenu(Utilities.getKit(eui.getComponent()), line);
                if (pm != null) {
                    e.consume();
                    pm.show(GlyphGutter.this, e.getX(), e.getY());
                    pm.addPopupMenuListener( new PopupMenuListener() {
                            public @Override void popupMenuCanceled(PopupMenuEvent e2) {
                                eui.getComponent().requestFocus();
                            }
                            public @Override void popupMenuWillBecomeInvisible(PopupMenuEvent e2) {
                                eui.getComponent().requestFocus();
                            }
                            public @Override void popupMenuWillBecomeVisible(PopupMenuEvent e2) {
                            }
                        });
                }
            }
        }
        
        public @Override void mouseReleased(MouseEvent e) {
            showPopup(e);
            if (!e.isConsumed() && (isMouseOverGlyph(e) || isMouseOverCycleButton(e))) {
                e.consume();
            }
            dragStartOffset = -1;
        }
        
        public @Override void mousePressed (MouseEvent e) {
            showPopup(e);
            if (!e.isConsumed() && (isMouseOverGlyph(e) || isMouseOverCycleButton(e))) {
                e.consume();
            }
        }
        
        public @Override void mouseDragged(MouseEvent e) {
            EditorUI eui = editorUI;
            if (eui == null) {
                return;
            }
            JTextComponent component = eui.getComponent();
            BaseTextUI textUI = (BaseTextUI)component.getUI();
            AbstractDocument aDoc = (AbstractDocument)component.getDocument();
            aDoc.readLock();
            try {
                // The drag must be extended to a next line in order to perform any selection
                int lineStartOffset = textUI.getPosFromY(e.getY());
                boolean updateDragEndOffset = false;
                if (dragStartOffset == -1) { // Drag starts now
                    dragStartOffset = lineStartOffset;
                    dragEndOffset = lineStartOffset;
                } else if (dragStartOffset == dragEndOffset) {
                    if (lineStartOffset != dragStartOffset) {
                        updateDragEndOffset = true;
                    }
                } else {
                    updateDragEndOffset = true;
                }
                if (updateDragEndOffset) {
                    // Extend selection to active line's end or begining depending on dragStartOffset
                    Caret caret = component.getCaret();
                    if (lineStartOffset >= dragStartOffset) {
                        if (caret.getMark() != dragStartOffset) {
                            caret.setDot(dragStartOffset);
                        }
                        // Check if the sele
                        // Extend to next line's begining
                        dragEndOffset = Math.min(Utilities.getRowEnd((BaseDocument) aDoc, lineStartOffset) + 1, aDoc.getLength());
                    } else { // Backward selection
                        // Check if the selection is already reverted i.e. it starts at dragStartOffset's line end
                        if (caret.getMark() == dragStartOffset) {
                            caret.setDot(Utilities.getRowEnd((BaseDocument)aDoc, dragStartOffset) + 1);
                        }
                        dragEndOffset = lineStartOffset;
                    }
                    component.moveCaretPosition(dragEndOffset);
                }
            } catch (BadLocationException ble) {
                // Ignore rather than notify
            } finally {
                aDoc.readUnlock();
            }
        }
        
        public @Override void mouseMoved(MouseEvent e) {}
        
    }

    class GlyphGutterFoldHierarchyListener implements FoldHierarchyListener, Runnable {
    
        public GlyphGutterFoldHierarchyListener(){
        }
        
        public @Override void foldHierarchyChanged(FoldHierarchyEvent evt) {
            SwingUtilities.invokeLater(this);
        }

        @Override
        public void run() {
            repaint();
        }
    }
    
    /** Listening to EditorUI to properly deinstall attached listeners */
    class EditorUIListener implements PropertyChangeListener{
        public @Override void propertyChange (PropertyChangeEvent evt) {
            if (evt.getSource() instanceof EditorUI) {
                if (evt.getPropertyName() == null || EditorUI.COMPONENT_PROPERTY.equals(evt.getPropertyName())) {
                    if (evt.getNewValue() == null){
                        // component deinstalled, lets uninstall all listeners
                        editorUI.removePropertyChangeListener(this);
                        if (evt.getOldValue() instanceof JTextComponent) {
                            ((JTextComponent) evt.getOldValue()).removePropertyChangeListener(this);
                        }
                        annos.removeAnnotationsListener(GlyphGutter.this);
                        foldHierarchy.removeFoldHierarchyListener(glyphGutterFoldHierarchyListener);
                        if (gutterMouseListener!=null){
                            removeMouseListener(gutterMouseListener);
                            removeMouseMotionListener(gutterMouseListener);
                        }
                        if (annoTypesListener !=null){
                            AnnotationTypes.getTypes().removePropertyChangeListener(annoTypesListener);
                        }
                        foldHierarchy.removeFoldHierarchyListener(glyphGutterFoldHierarchyListener);
                        foldHierarchy = null;
                        editorUI = null;
                        annos = null;
                    }
                }
            } else if (evt.getSource() instanceof JTextComponent) {
                if (evt.getPropertyName() == null || "document".equals(evt.getPropertyName())) { //NOI18N
                    annos.removeAnnotationsListener(GlyphGutter.this);
                    EditorUI eui = editorUI;
                    if (eui != null && eui.getDocument() != null) {
                        annos = eui.getDocument().getAnnotations();
                    }
                    annos.addAnnotationsListener(GlyphGutter.this);

                    update();
                } else if (TEXT_ZOOM_PROPERTY.equals(evt.getPropertyName())) {
                    update();
                } else if ("font".equals(evt.getPropertyName())) {
                    update();
                }
            }
        }
    }

}

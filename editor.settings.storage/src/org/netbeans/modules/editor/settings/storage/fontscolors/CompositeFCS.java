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

package org.netbeans.modules.editor.settings.storage.fontscolors;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.netbeans.api.editor.settings.EditorStyleConstants;
import org.netbeans.api.editor.settings.FontColorNames;
import org.netbeans.api.editor.settings.FontColorSettings;
import org.netbeans.modules.editor.settings.storage.api.EditorSettings;
import org.openide.util.Utilities;

/**
 *
 * @author Vita Stejskal
 */
public final class CompositeFCS extends FontColorSettings {

    // -J-Dorg.netbeans.modules.editor.settings.storage.fontscolors.CompositeFCS.level=FINE
    private static final Logger LOG = Logger.getLogger(CompositeFCS.class.getName());
    
    public static final String TEXT_ANTIALIASING_PROP = "textAntialiasing"; // NOI18N
    
    // Special instance to mark 'no attributes' for a token. This should never
    // be passed outside of this class, use SimpleAttributeSet.EMPTY instead. There
    // might be other code doing 'attribs == SAS.EMPTY'.
    private static final AttributeSet NULL = new SimpleAttributeSet();
    
    private final FontColorSettingsImpl[] allFcsi;
    /* package */ final String profile;
    private final Map<String, AttributeSet> tokensCache = new HashMap<String, AttributeSet>();
    
    private final Preferences preferences;

    /** Creates a new instance of CompositeFCS */
    public CompositeFCS(MimePath mimePath, String profile, Preferences preferences) {
        super();

        assert mimePath != null : "The parameter allPaths should not be null"; //NOI18N
        assert profile != null : "The parameter profile should not be null"; //NOI18N

        // Skip all mime types from the end that do not define any colorings.
        // This is here to support dummy languages like text/x-java/text/x-java-string that
        // inherit all colorings from the outer language.
        while (mimePath.size() > 1) {
            String lastMimeType = mimePath.getMimeType(mimePath.size() - 1);
            boolean empty = FontColorSettingsImpl.get(MimePath.parse(lastMimeType)).getColorings(profile).isEmpty();
            if (!empty) {
                break;
            }
            mimePath = mimePath.getPrefix(mimePath.size() - 1);
        }

        List<MimePath> allPaths = mimePath.getIncludedPaths();
        assert allPaths.size() > 0 : "allPaths should always contain at least MimePath.EMPTY"; //NOI18N

        this.allFcsi = new FontColorSettingsImpl[allPaths.size()];
        for (int i = 0; i < allPaths.size(); i++) {
            allFcsi[i] = FontColorSettingsImpl.get(allPaths.get(i));
        }

        this.profile = profile;
        this.preferences = preferences;
    }

    /**
     * Gets the coloring for a highlight. Highlights are used for highlighting
     * important things in editor such as a caret row, text selection, marking
     * text found by the last search peration, etc. They are not bound to any
     * tokens and therefore are mime type independent.
     */
    @Override
    public AttributeSet getFontColors(String highlightName) {
        assert highlightName != null : "The parameter highlightName must not be null."; //NOI18N

        AttributeSet attribs = null;
        Map<String, AttributeSet> coloringsMap = EditorSettings.getDefault().getHighlightings(profile);
        if (coloringsMap != null) {
            attribs = coloringsMap.get(highlightName);
        }

        if (highlightName.equals(FontColorNames.DEFAULT_COLORING) && (attribs == null || attribs.getAttribute(StyleConstants.FontFamily) == null) ) {
            ArrayList<AttributeSet> colorings = new ArrayList<AttributeSet>();
            String name = highlightName;

            for (FontColorSettingsImpl fcsi : allFcsi) {
                name = processLayer(fcsi, name, colorings);
            }

            colorings.add(getHardcodedDefaultColoring());
            colorings.add(AttributesUtilities.createImmutable(
                    EditorStyleConstants.RenderingHints, getRenderingHints()));

            return AttributesUtilities.createImmutable(colorings.toArray(new AttributeSet[colorings.size()]));

        }

//        dumpAttribs(attribs, highlightName, false);
        return attribs;
    }

    @Override
    public AttributeSet getTokenFontColors(String tokenName) {
        assert tokenName != null : "The parameter tokenName must not be null."; //NOI18N

        synchronized (tokensCache) {
            AttributeSet attribs = tokensCache.get(tokenName);

            if (attribs == null) {
                attribs = findColoringForToken(tokenName);
//                dumpAttribs(attribs, tokenName, true);
                tokensCache.put(tokenName, attribs);
//            } else {
//                System.out.println("Using cached value for token '" + tokenName + "' CompoundFCS.this = " + this);
            }

            return attribs == NULL ? null : attribs;
        }
    }

    public boolean isDerivedFromMimePath(MimePath mimePath) {
        for (FontColorSettingsImpl fcsi : allFcsi) {
            if (fcsi.getMimePath() == mimePath) {
                return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    // private implementation
    //-----------------------------------------------------------------------
    
    private AttributeSet findColoringForToken(String tokenName) {
        ArrayList<AttributeSet> colorings = new ArrayList<AttributeSet>();
        String name = tokenName;

        for (FontColorSettingsImpl fcsi : allFcsi) {
            name = processLayer(fcsi, name, colorings);
        }

        if (colorings.size() > 0) {
            return AttributesUtilities.createImmutable(colorings.toArray(new AttributeSet[colorings.size()]));
        } else {
            return NULL;
        }
    }

    //side effect colorings
    private String processLayer(FontColorSettingsImpl fcsi, String name, ArrayList<AttributeSet> colorings) {
        // Try colorings first
        AttributeSet as = fcsi.getColorings(profile).get(name);
        if (as == null) {
            // If not found, try the layer's default colorings
            as = fcsi.getDefaultColorings(profile).get(name);
        }

        // If we found a coloring then process it
        if (as != null) {
            colorings.add(as);

            String nameOfColoring = (String) as.getAttribute(StyleConstants.NameAttribute);
            String nameOfDelegate = (String) as.getAttribute(EditorStyleConstants.Default);
            if (nameOfDelegate != null && !nameOfDelegate.equals(FontColorNames.DEFAULT_COLORING)) {
                if (!nameOfDelegate.equals(nameOfColoring)) {
                    // Find delegate on the same layer
                    nameOfDelegate = processLayer(fcsi, nameOfDelegate, colorings);
                }
            } else {
                // Use the coloring's name as the default name of a delegate
                nameOfDelegate = nameOfColoring;
            }

            name = nameOfDelegate;
        }

        // Return updated name - either the name of the coloring or the name of
        // the coloring's delegate
        return name;
    }

    private void dumpAttribs(AttributeSet attribs, String name, boolean tokenColoring) {
//        if (!allFcsi[0].getMimePath().getPath().equals("text/x-java")) { //NOI18N
//            return;
//        }

        StringBuilder sb = new StringBuilder();
        sb.append("Attribs for base mime path '"); //NOI18N
        sb.append(allFcsi[0].getMimePath().getPath());
        sb.append("' and "); //NOI18N
        if (tokenColoring) {
            sb.append("token '"); //NOI18N
        } else {
            sb.append("highlight '"); //NOI18N
        }
        sb.append(name);
        sb.append("' = {"); //NOI18N

        Enumeration<?> keys = attribs.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = attribs.getAttribute(key);

            sb.append("'").append(key).append("' = '").append(value).append("'"); //NOI18N
            if (keys.hasMoreElements()) {
                sb.append(", "); //NOI18N
            }
        }

        sb.append("} CompoundFCS.this = "); //NOI18N
        sb.append(this.toString());

        System.out.println(sb.toString());
    }

    private Map<?, ?> getRenderingHints() {
        // This property was introduced in JDK1.6, see http://java.sun.com/javase/6/docs/api/java/awt/doc-files/DesktopProperties.html
        // We should probably also listen on the default toolkit for changes in this
        // property and refresh FontColorSettings in MimeLookup. As per JDK docs java apps
        // should pick up changes in OS AA Font Settings automatically without restart.
        Map<?, ?> desktopHints = (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints"); //NOI18N
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("System provided desktop hints:"); //NOI18N
            if (desktopHints != null) {
                for (Object key : desktopHints.keySet()) {
                    Object value = desktopHints.get(key);
                    String humanReadableKey = translateRenderingHintsConstant(key);
                    String humanReadableValue = translateRenderingHintsConstant(value);
                    LOG.fine("  " + humanReadableKey + " = " + humanReadableValue); //NOI18N
                }
            } else {
                LOG.fine("There are no desktop hints");
            }
            LOG.fine("----------------"); //NOI18N
        }

        Boolean aaOn = null;
        String reason = null;

// Since there is no way how to control TEXT_ANTIALIASING_PROP from Tools-Options
// we will ignore the property. Also see #144516.
//
//        String aaOnString = preferences.get(TEXT_ANTIALIASING_PROP, null);
//        if (aaOnString != null) {
//            aaOn = Boolean.valueOf(aaOnString);
//            reason = "editor preferences property '" + TEXT_ANTIALIASING_PROP + "'"; //NOI18N
//        } else {

        // These two properties are questionable. I haven't found any oficial docs
        // saying that JVM recognizes them. However, we have been using them in Netbeans
        // for a long time and so keep supporting them. But they most likely have absolutely
        // no effect on JVM.
        //
        // There is another 'officially unsupported' property called awt.useSystemAAFontSetings
        // introduced in JDK1.6, please http://java.sun.com/javase/6/docs/technotes/guides/2d/flags.html#aaFonts
        //
        String systemProperty = System.getProperty("javax.aatext"); //NOI18N
        if (systemProperty == null) {
            systemProperty = System.getProperty("swing.aatext"); //NOI18N
        }

        if (systemProperty != null) {
            aaOn = Boolean.valueOf(systemProperty);
            reason = "system property 'javax.aatext' or 'swing.aatext'"; //NOI18N
        } else {
            // Traditionally we turn text AA on when on Mac OS X
            if (Utilities.isMac()) {
                aaOn = Boolean.TRUE;
                reason = "running on Mac OSX";//NOI18N
            }
        }
//        }

        Map<Object, Object> hints;
        if (aaOn == null) {
            LOG.fine("Text Antialiasing setting was not determined, using defaults."); //NOI18N
            if (desktopHints != null) {
                LOG.fine("Using system provided desktop hints"); //NOI18N
                hints = new HashMap<Object, Object>(desktopHints);
            } else {
                LOG.fine("No system provided desktop hints available, using hardcoded defaults"); //NOI18N
                hints = Collections.<Object, Object>singletonMap(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
            }
        } else {
            LOG.fine("Text Antialiasing was set " + (aaOn.booleanValue() ? "ON" : "OFF") + " by " + reason + "."); //NOI18N
            if (desktopHints != null) {
                LOG.fine("Using system provided desktop hints"); //NOI18N
                hints = new HashMap<Object, Object>(desktopHints);
            } else {
                LOG.fine("No system provided desktop hints available, using hardcoded defaults"); //NOI18N
                hints = new HashMap<Object, Object>();
            }
            if (aaOn.booleanValue()) {
                // aaOn == true normally means that we should use system defaults,
                // but if there are none we will turn text antialiasing on manually. This
                // may not provide the best results, but should be better than nothing.
                if (!hints.containsKey(RenderingHints.KEY_TEXT_ANTIALIASING)) {
                    hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                }
            } else {
                hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }
        }

        if (Boolean.getBoolean("org.netbeans.editor.aa.extra.hints")) {
            // Get "bolder" Liberation Mono
            hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            // Extra hints
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }

        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Editor Rendering hints:"); //NOI18N
            for (Object key : hints.keySet()) {
                Object value = hints.get(key);
                String humanReadableKey = translateRenderingHintsConstant(key);
                String humanReadableValue = translateRenderingHintsConstant(value);
                LOG.fine("  " + humanReadableKey + " = " + humanReadableValue); //NOI18N
            }
            LOG.fine("----------------"); //NOI18N
        }

        return hints;
    }
    
    private static final Map<Object, String> renderingHintsConstants = new HashMap<Object, String>();
    private static synchronized String translateRenderingHintsConstant(Object c) {
        String s = null;

        if (c != null) {
            s = renderingHintsConstants.get(c);
            if (s == null) {
                for (Field f : RenderingHints.class.getFields()) {
                    try {
                        f.setAccessible(true);
                        if ((f.getModifiers() & Modifier.STATIC) != 0 && f.get(null) == c) {
                            s = f.getName();
                            break;
                        }
                    } catch (IllegalAccessException iae) {
                        // ignore
                    }
                }

                if (s != null) {
                    renderingHintsConstants.put(c, s);
                }
            }
        }

        return s != null ? s : c != null ? c.toString() : null;
    }

    private static AttributeSet hardCodedDefaultColoring = null;
    private static final int DEFAULT_FONTSIZE = 13;
    private static AttributeSet getHardcodedDefaultColoring() {
        if (hardCodedDefaultColoring == null) {
            int defaultFontSize;
            if (GraphicsEnvironment.isHeadless()) {
                defaultFontSize = DEFAULT_FONTSIZE;
            } else {
                Integer customFontSize = (Integer) UIManager.get("customFontSize"); //NOI18N
                defaultFontSize = (customFontSize != null) ? customFontSize : (UIManager.getFont("TextField.font") != null ? UIManager.getFont("TextField.font").getSize() : DEFAULT_FONTSIZE); //NOI18N
            }

            hardCodedDefaultColoring = AttributesUtilities.createImmutable(
                    StyleConstants.NameAttribute, FontColorNames.DEFAULT_COLORING,
                    StyleConstants.Foreground, Color.black,
                    StyleConstants.Background, Color.white,
                    StyleConstants.FontFamily, "Monospaced", //NOI18N
                    StyleConstants.FontSize, Math.max(defaultFontSize, DEFAULT_FONTSIZE));
        }
        assert hardCodedDefaultColoring != null;
        return hardCodedDefaultColoring;
    }
}

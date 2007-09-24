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
package org.netbeans.modules.visualweb.xhtml;
/**
 * <b>Body</b> is generated from xhtml.rng by Relaxer.
 * This class is derived from:
 *
 * <!-- for programmer
 * <element name="body">
 *     <ref name="body.attlist"/>
 *     <ref name="Block.model"/>
 *   </element>-->
 * <!-- for javadoc -->
 * <pre> &lt;element name="body"&gt;
 *     &lt;ref name="body.attlist"/&gt;
 *     &lt;ref name="Block.model"/&gt;
 *   &lt;/element&gt;</pre>
 *
 * @version xhtml.rng (Tue Apr 20 01:31:08 PDT 2004)
 * @author  Relaxer 1.1b (http://www.relaxer.org)
 */
public class Body {
    public static final String DIR_LTR = "ltr";
    public static final String DIR_RTL = "rtl";
    private String id_;
    private String classValue_;
    private String title_;
    private String style_;
    private String xmlLang_;
    private String lang_;
    private String dir_;
    private String onclick_;
    private String ondblclick_;
    private String onmousedown_;
    private String onmouseup_;
    private String onmouseover_;
    private String onmousemove_;
    private String onmouseout_;
    private String onkeypress_;
    private String onkeydown_;
    private String onkeyup_;
    private String onload_;
    private String onunload_;
    private String background_;
    private String bgcolor_;
    private String text_;
    private String link_;
    private String vlink_;
    private String alink_;
    /**
     * Creates a <code>Body</code>.
     *
     */
    public Body() {
    }
    /**
     * Gets the String property <b>id</b>.
     *
     * @return String
     */
    public String getId() {
        return (id_);
    }
    /**
     * Sets the String property <b>id</b>.
     *
     * @param id
     */
    public void setId(String id) {
        this.id_ = id;
    }
    /**
     * Gets the String property <b>classValue</b>.
     *
     * @return String
     */
    public String getClassValue() {
        return classValue_;
    }
    /**
     * Sets the String property <b>classValue</b>.
     *
     * @param classValue
     */
    public void setClassValue(String classValue) {
        this.classValue_ = classValue;
    }
    /**
     * Gets the String property <b>title</b>.
     *
     * @return String
     */
    public String getTitle() {
        return (title_);
    }
    /**
     * Sets the String property <b>title</b>.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title_ = title;
    }
    /**
     * Gets the String property <b>style</b>.
     *
     * @return String
     */
    public String getStyle() {
        return (style_);
    }
    /**
     * Sets the String property <b>style</b>.
     *
     * @param style
     */
    public void setStyle(String style) {
        this.style_ = style;
    }
    /**
     * Gets the java.util.Locale property <b>xmlLang</b>.
     *
     * @return java.util.Locale
     */
    public String getXmlLang() {
        return (xmlLang_);
    }
    /**
     * Sets the java.util.Locale property <b>xmlLang</b>.
     *
     * @param xmlLang
     */
    public void setXmlLang(String xmlLang) {
        this.xmlLang_ = xmlLang;
    }
    /**
     * Gets the java.util.Locale property <b>lang</b>.
     *
     * @return java.util.Locale
     */
    public String getLang() {
        return (lang_);
    }
    /**
     * Sets the java.util.Locale property <b>lang</b>.
     *
     * @param lang
     */
    public void setLang(String lang) {
        this.lang_ = lang;
    }
    /**
     * Gets the String property <b>dir</b>.
     *
     * @return String
     */
    public String getDir() {
        return (dir_);
    }
    /**
     * Sets the String property <b>dir</b>.
     *
     * @param dir
     */
    public void setDir(String dir) {
        this.dir_ = dir;
    }
    /**
     * Gets the String property <b>onclick</b>.
     *
     * @return String
     */
    public String getOnclick() {
        return (onclick_);
    }
    /**
     * Sets the String property <b>onclick</b>.
     *
     * @param onclick
     */
    public void setOnclick(String onclick) {
        this.onclick_ = onclick;
    }
    /**
     * Gets the String property <b>ondblclick</b>.
     *
     * @return String
     */
    public String getOndblclick() {
        return (ondblclick_);
    }
    /**
     * Sets the String property <b>ondblclick</b>.
     *
     * @param ondblclick
     */
    public void setOndblclick(String ondblclick) {
        this.ondblclick_ = ondblclick;
    }
    /**
     * Gets the String property <b>onmousedown</b>.
     *
     * @return String
     */
    public String getOnmousedown() {
        return (onmousedown_);
    }
    /**
     * Sets the String property <b>onmousedown</b>.
     *
     * @param onmousedown
     */
    public void setOnmousedown(String onmousedown) {
        this.onmousedown_ = onmousedown;
    }
    /**
     * Gets the String property <b>onmouseup</b>.
     *
     * @return String
     */
    public String getOnmouseup() {
        return (onmouseup_);
    }
    /**
     * Sets the String property <b>onmouseup</b>.
     *
     * @param onmouseup
     */
    public void setOnmouseup(String onmouseup) {
        this.onmouseup_ = onmouseup;
    }
    /**
     * Gets the String property <b>onmouseover</b>.
     *
     * @return String
     */
    public String getOnmouseover() {
        return (onmouseover_);
    }
    /**
     * Sets the String property <b>onmouseover</b>.
     *
     * @param onmouseover
     */
    public void setOnmouseover(String onmouseover) {
        this.onmouseover_ = onmouseover;
    }
    /**
     * Gets the String property <b>onmousemove</b>.
     *
     * @return String
     */
    public String getOnmousemove() {
        return (onmousemove_);
    }
    /**
     * Sets the String property <b>onmousemove</b>.
     *
     * @param onmousemove
     */
    public void setOnmousemove(String onmousemove) {
        this.onmousemove_ = onmousemove;
    }
    /**
     * Gets the String property <b>onmouseout</b>.
     *
     * @return String
     */
    public String getOnmouseout() {
        return (onmouseout_);
    }
    /**
     * Sets the String property <b>onmouseout</b>.
     *
     * @param onmouseout
     */
    public void setOnmouseout(String onmouseout) {
        this.onmouseout_ = onmouseout;
    }
    /**
     * Gets the String property <b>onkeypress</b>.
     *
     * @return String
     */
    public String getOnkeypress() {
        return (onkeypress_);
    }
    /**
     * Sets the String property <b>onkeypress</b>.
     *
     * @param onkeypress
     */
    public void setOnkeypress(String onkeypress) {
        this.onkeypress_ = onkeypress;
    }
    /**
     * Gets the String property <b>onkeydown</b>.
     *
     * @return String
     */
    public String getOnkeydown() {
        return (onkeydown_);
    }
    /**
     * Sets the String property <b>onkeydown</b>.
     *
     * @param onkeydown
     */
    public void setOnkeydown(String onkeydown) {
        this.onkeydown_ = onkeydown;
    }
    /**
     * Gets the String property <b>onkeyup</b>.
     *
     * @return String
     */
    public String getOnkeyup() {
        return (onkeyup_);
    }
    /**
     * Sets the String property <b>onkeyup</b>.
     *
     * @param onkeyup
     */
    public void setOnkeyup(String onkeyup) {
        this.onkeyup_ = onkeyup;
    }
    /**
     * Gets the String property <b>onload</b>.
     *
     * @return String
     */
    public String getOnload() {
        return (onload_);
    }
    /**
     * Sets the String property <b>onload</b>.
     *
     * @param onload
     */
    public void setOnload(String onload) {
        this.onload_ = onload;
    }
    /**
     * Gets the String property <b>onunload</b>.
     *
     * @return String
     */
    public String getOnunload() {
        return (onunload_);
    }
    /**
     * Sets the String property <b>onunload</b>.
     *
     * @param onunload
     */
    public void setOnunload(String onunload) {
        this.onunload_ = onunload;
    }
    /**
     * Gets the String property <b>background</b>.
     *
     * @return String
     */
    public String getBackground() {
        return (background_);
    }
    /**
     * Sets the String property <b>background</b>.
     *
     * @param background
     */
    public void setBackground(String background) {
        this.background_ = background;
    }
    /**
     * Gets the String property <b>bgcolor</b>.
     *
     * @return String
     */
    public String getBgcolor() {
        return (bgcolor_);
    }
    /**
     * Sets the String property <b>bgcolor</b>.
     *
     * @param bgcolor
     */
    public void setBgcolor(String bgcolor) {
        this.bgcolor_ = bgcolor;
    }
    /**
     * Gets the String property <b>text</b>.
     *
     * @return String
     */
    public String getText() {
        return (text_);
    }
    /**
     * Sets the String property <b>text</b>.
     *
     * @param text
     */
    public void setText(String text) {
        this.text_ = text;
    }
    /**
     * Gets the String property <b>link</b>.
     *
     * @return String
     */
    public String getLink() {
        return (link_);
    }
    /**
     * Sets the String property <b>link</b>.
     *
     * @param link
     */
    public void setLink(String link) {
        this.link_ = link;
    }
    /**
     * Gets the String property <b>vlink</b>.
     *
     * @return String
     */
    public String getVlink() {
        return (vlink_);
    }
    /**
     * Sets the String property <b>vlink</b>.
     *
     * @param vlink
     */
    public void setVlink(String vlink) {
        this.vlink_ = vlink;
    }
    /**
     * Gets the String property <b>alink</b>.
     *
     * @return String
     */
    public String getAlink() {
        return (alink_);
    }
    /**
     * Sets the String property <b>alink</b>.
     *
     * @param alink
     */
    public void setAlink(String alink) {
        this.alink_ = alink;
    }
}

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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.visualweb.faces.dt_1_1.component.html;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.sun.rave.designtime.CategoryDescriptor;
import com.sun.rave.designtime.Constants;
import com.sun.rave.designtime.faces.FacetDescriptor;
import com.sun.rave.designtime.markup.AttributeDescriptor;
import org.netbeans.modules.visualweb.faces.dt.BeanDescriptorBase;
import org.netbeans.modules.visualweb.faces.dt.PropertyDescriptorBase;
import org.netbeans.modules.visualweb.faces.dt_1_1.component.UIGraphicBeanInfoBase;


public class HtmlGraphicImageBeanInfo extends UIGraphicBeanInfoBase {

    protected static ResourceBundle resources =
            ResourceBundle.getBundle("org.netbeans.modules.visualweb.faces.dt_1_1.component.html.Bundle-JSF", Locale.getDefault(), HtmlGraphicImageBeanInfo.class.getClassLoader());


    public HtmlGraphicImageBeanInfo() {
        beanClass = javax.faces.component.html.HtmlGraphicImage.class;
        defaultPropertyName = "value";
        iconFileName_C16 = "/org/netbeans/modules/visualweb/faces/dt_1_1/component/html/HtmlGraphicImage_C16";
        iconFileName_C32 = "/org/netbeans/modules/visualweb/faces/dt_1_1/component/html/HtmlGraphicImage_C32";
        iconFileName_M16 = "/org/netbeans/modules/visualweb/faces/dt_1_1/component/html/HtmlGraphicImage_M16";
        iconFileName_M32 = "/org/netbeans/modules/visualweb/faces/dt_1_1/component/html/HtmlGraphicImage_M32";
    }


    private BeanDescriptor beanDescriptor;

    public BeanDescriptor getBeanDescriptor() {

        if (beanDescriptor != null) {
            return beanDescriptor;
        }

        beanDescriptor = new BeanDescriptorBase(beanClass);
        beanDescriptor.setDisplayName(resources.getString("HtmlGraphicImage_DisplayName"));
        beanDescriptor.setShortDescription(resources.getString("HtmlGraphicImage_Description"));
        beanDescriptor.setExpert(false);
        beanDescriptor.setHidden(false);
        beanDescriptor.setPreferred(false);
        beanDescriptor.setValue(Constants.BeanDescriptor.FACET_DESCRIPTORS,getFacetDescriptors());
        beanDescriptor.setValue(Constants.BeanDescriptor.HELP_KEY,"projrave_ui_elements_palette_jsfstd_image");
        beanDescriptor.setValue(Constants.BeanDescriptor.INSTANCE_NAME,"image");
        beanDescriptor.setValue(Constants.BeanDescriptor.IS_CONTAINER,Boolean.FALSE);
        beanDescriptor.setValue(Constants.BeanDescriptor.PROPERTIES_HELP_KEY,"projrave_ui_elements_propsheets_jsfstd_image_props");
        beanDescriptor.setValue(Constants.BeanDescriptor.PROPERTY_CATEGORIES,getCategoryDescriptors());
        beanDescriptor.setValue(Constants.BeanDescriptor.TAG_NAME,"graphicImage");
        beanDescriptor.setValue(Constants.BeanDescriptor.TAGLIB_PREFIX,"h");
        beanDescriptor.setValue(Constants.BeanDescriptor.TAGLIB_URI,"http://java.sun.com/jsf/html");
        beanDescriptor.setValue(Constants.BeanDescriptor.RESIZE_CONSTRAINTS,
                new Integer(Constants.ResizeConstraints.MAINTAIN_ASPECT_RATIO|Constants.ResizeConstraints.ANY));

        return beanDescriptor;
    }


    private PropertyDescriptor[] propertyDescriptors;

    public PropertyDescriptor[] getPropertyDescriptors() {

        if (propertyDescriptors != null) {
            return propertyDescriptors;
        }
        AttributeDescriptor attrib = null;

        try {

            PropertyDescriptor prop_alt = new PropertyDescriptorBase("alt",beanClass,"getAlt","setAlt");
            prop_alt.setDisplayName(resources.getString("HtmlGraphicImage_alt_DisplayName"));
            prop_alt.setShortDescription(resources.getString("HtmlGraphicImage_alt_Description"));
            prop_alt.setExpert(false);
            prop_alt.setHidden(false);
            prop_alt.setPreferred(false);
            attrib = new AttributeDescriptor("alt",false,null,true);
            prop_alt.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_alt.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.APPEARANCE);

            PropertyDescriptor prop_dir = new PropertyDescriptorBase("dir",beanClass,"getDir","setDir");
            prop_dir.setDisplayName(resources.getString("HtmlGraphicImage_dir_DisplayName"));
            prop_dir.setShortDescription(resources.getString("HtmlGraphicImage_dir_Description"));
            prop_dir.setPropertyEditorClass(loadClass("com.sun.jsfcl.std.property.ChooseOneReferenceDataPropertyEditor"));
            prop_dir.setExpert(false);
            prop_dir.setHidden(false);
            prop_dir.setPreferred(false);
            attrib = new AttributeDescriptor("dir",false,null,true);
            prop_dir.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_dir.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.ADVANCED);
            prop_dir.setValue("referenceDataDefiner", com.sun.jsfcl.std.reference.ReferenceDataManager.TEXT_DIRECTIONS);

            PropertyDescriptor prop_height = new PropertyDescriptorBase("height",beanClass,"getHeight","setHeight");
            prop_height.setDisplayName(resources.getString("HtmlGraphicImage_height_DisplayName"));
            prop_height.setShortDescription(resources.getString("HtmlGraphicImage_height_Description"));
            prop_height.setPropertyEditorClass(loadClass("com.sun.rave.propertyeditors.LengthPropertyEditor"));
            prop_height.setExpert(false);
            prop_height.setHidden(false);
            prop_height.setPreferred(false);
            attrib = new AttributeDescriptor("height",false,null,true);
            prop_height.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_height.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.APPEARANCE);

            PropertyDescriptor prop_ismap = new PropertyDescriptorBase("ismap",beanClass,"isIsmap","setIsmap");
            prop_ismap.setDisplayName(resources.getString("HtmlGraphicImage_ismap_DisplayName"));
            prop_ismap.setShortDescription(resources.getString("HtmlGraphicImage_ismap_Description"));
            prop_ismap.setExpert(false);
            prop_ismap.setHidden(false);
            prop_ismap.setPreferred(false);
            attrib = new AttributeDescriptor("ismap",false,null,true);
            prop_ismap.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_ismap.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.ADVANCED);

            PropertyDescriptor prop_lang = new PropertyDescriptorBase("lang",beanClass,"getLang","setLang");
            prop_lang.setDisplayName(resources.getString("HtmlGraphicImage_lang_DisplayName"));
            prop_lang.setShortDescription(resources.getString("HtmlGraphicImage_lang_Description"));
            prop_lang.setPropertyEditorClass(loadClass("com.sun.rave.propertyeditors.SelectOneDomainEditor"));
            prop_lang.setExpert(false);
            prop_lang.setHidden(false);
            prop_lang.setPreferred(false);
            attrib = new AttributeDescriptor("lang",false,null,true);
            prop_lang.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_lang.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.ADVANCED);
            prop_lang.setValue("com.sun.rave.propertyeditors.DOMAIN_CLASS", com.sun.rave.propertyeditors.domains.LanguagesDomain.class);

            PropertyDescriptor prop_longdesc = new PropertyDescriptorBase("longdesc",beanClass,"getLongdesc","setLongdesc");
            prop_longdesc.setDisplayName(resources.getString("HtmlGraphicImage_longdesc_DisplayName"));
            prop_longdesc.setShortDescription(resources.getString("HtmlGraphicImage_longdesc_Description"));
            prop_longdesc.setExpert(false);
            prop_longdesc.setHidden(false);
            prop_longdesc.setPreferred(false);
            attrib = new AttributeDescriptor("longdesc",false,null,true);
            prop_longdesc.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_longdesc.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.APPEARANCE);

            PropertyDescriptor prop_title = new PropertyDescriptorBase("title",beanClass,"getTitle","setTitle");
            prop_title.setDisplayName(resources.getString("HtmlGraphicImage_title_DisplayName"));
            prop_title.setShortDescription(resources.getString("HtmlGraphicImage_title_Description"));
            prop_title.setExpert(false);
            prop_title.setHidden(false);
            prop_title.setPreferred(false);
            attrib = new AttributeDescriptor("title",false,null,true);
            prop_title.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_title.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.APPEARANCE);

            PropertyDescriptor prop_usemap = new PropertyDescriptorBase("usemap",beanClass,"getUsemap","setUsemap");
            prop_usemap.setDisplayName(resources.getString("HtmlGraphicImage_usemap_DisplayName"));
            prop_usemap.setShortDescription(resources.getString("HtmlGraphicImage_usemap_Description"));
            prop_usemap.setPropertyEditorClass(loadClass("com.sun.rave.propertyeditors.StringPropertyEditor"));
            prop_usemap.setExpert(false);
            prop_usemap.setHidden(false);
            prop_usemap.setPreferred(false);
            attrib = new AttributeDescriptor("usemap",false,null,true);
            prop_usemap.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_usemap.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.ADVANCED);

            PropertyDescriptor prop_width = new PropertyDescriptorBase("width",beanClass,"getWidth","setWidth");
            prop_width.setDisplayName(resources.getString("HtmlGraphicImage_width_DisplayName"));
            prop_width.setShortDescription(resources.getString("HtmlGraphicImage_width_Description"));
            prop_width.setPropertyEditorClass(loadClass("com.sun.rave.propertyeditors.LengthPropertyEditor"));
            prop_width.setExpert(false);
            prop_width.setHidden(false);
            prop_width.setPreferred(false);
            attrib = new AttributeDescriptor("width",false,null,true);
            prop_width.setValue(Constants.PropertyDescriptor.ATTRIBUTE_DESCRIPTOR,attrib);
            prop_width.setValue(Constants.PropertyDescriptor.CATEGORY,com.sun.rave.designtime.base.CategoryDescriptors.APPEARANCE);

            List<PropertyDescriptor> propertyDescriptorList = new ArrayList<PropertyDescriptor>();
            propertyDescriptorList.add(prop_alt);
            propertyDescriptorList.add(prop_dir);
            propertyDescriptorList.add(prop_height);
            propertyDescriptorList.add(prop_ismap);
            propertyDescriptorList.add(prop_lang);
            propertyDescriptorList.add(prop_longdesc);
            propertyDescriptorList.add(prop_title);
            propertyDescriptorList.add(prop_usemap);
            propertyDescriptorList.add(prop_width);

            propertyDescriptorList.addAll(Properties.getVisualPropertyList(beanClass));
            propertyDescriptorList.addAll(Properties.getKeyEventPropertyList(beanClass));
            propertyDescriptorList.addAll(Properties.getMouseEventPropertyList(beanClass));
            propertyDescriptorList.addAll(Properties.getClickEventPropertyList(beanClass));
            propertyDescriptorList.addAll(Arrays.asList(super.getPropertyDescriptors()));
            propertyDescriptors = propertyDescriptorList.toArray(new PropertyDescriptor[propertyDescriptorList.size()]);
            return propertyDescriptors;

        } catch (IntrospectionException e) {
            e.printStackTrace();
            return null;
        }

    }

}


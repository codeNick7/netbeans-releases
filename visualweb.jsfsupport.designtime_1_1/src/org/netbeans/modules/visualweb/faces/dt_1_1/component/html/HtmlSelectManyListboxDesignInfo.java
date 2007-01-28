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

import com.sun.rave.designtime.*;
import javax.faces.component.html.HtmlSelectManyListbox;

/**
 * DesignInfo for the HtmlSelectManyListbox
 */
public class HtmlSelectManyListboxDesignInfo extends HtmlSelectDesignInfoBase {

    public Class getBeanClass() { return HtmlSelectManyListbox.class; }

    public Result beanCreatedSetup(DesignBean bean) {
        return selectManyBeanCreated(bean);
    }

    public Result beanDeletedCleanup(DesignBean bean) {
        modifyVirtualFormsOnBeanDeletedCleanup(bean);
        return selectManyBeanDeleted(bean);
    }

    public Result beanPastedSetup(DesignBean bean) {
        return selectManyBeanPasted(bean);
    }

    public DisplayAction[] getContextItems(DesignBean bean) {
        return selectManyGetContextItems(bean);
    }

    public Result linkBeans(DesignBean targetBean, DesignBean sourceBean) {

        try {
            if (canLinkConverterOrValidatorBeans(targetBean, sourceBean)) {
                linkConverterOrValidatorBeans(targetBean, sourceBean);
                return Result.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILURE;
        }
        return selectManyLinkBeans(targetBean, sourceBean);
    }
}

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
package org.netbeans.modules.form.layoutdesign;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.openide.filesystems.FileUtil;

public class ALT_SeqResizing14Test extends LayoutTestCase {

    public ALT_SeqResizing14Test(String name) {
        super(name);
        try {
            className = this.getClass().getName();
            className = className.substring(className.lastIndexOf('.') + 1, className.length());
            startingFormFile = FileUtil.toFileObject(new File(url.getFile() + goldenFilesPath + className + "-StartingForm.form").getCanonicalFile());
        } catch (IOException ioe) {
            fail(ioe.toString());
        }
    }

    /**
     * Resize jButton1 up by its bottom edges so it goes above the other buttons.
     * In vertical dimension the buttons should end up in one sequence.
     */
    public void doChanges0() {
        ld.externalSizeChangeHappened();
// > UPDATE CURRENT STATE
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        contInterior.put("Form", new Rectangle(0, 0, 400, 178));
        compBounds.put("jTextField1", new Rectangle(10, 11, 380, 20));
        baselinePosition.put("jTextField1-380-20", new Integer(14));
        compBounds.put("jScrollPane1", new Rectangle(37, 37, 35, 130));
        baselinePosition.put("jScrollPane1-35-130", new Integer(0));
        compBounds.put("jButton1", new Rectangle(125, 37, 73, 130));
        baselinePosition.put("jButton1-73-130", new Integer(69));
        compBounds.put("jButton3", new Rectangle(228, 115, 73, 23));
        baselinePosition.put("jButton3-73-23", new Integer(15));
        compBounds.put("jButton2", new Rectangle(228, 144, 73, 23));
        baselinePosition.put("jButton2-73-23", new Integer(15));
        compMinSize.put("Form", new Dimension(376, 178));
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        compPrefSize.put("jTextField1", new Dimension(59, 20));
        prefPadding.put("jButton1-jButton3-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPadding.put("jButton1-jButton2-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        compPrefSize.put("jButton1", new Dimension(73, 23));
        ld.updateCurrentState();
// < UPDATE CURRENT STATE
        prefPaddingInParent.put("Form-jTextField1-1-0", new Integer(11)); // parentId-compId-dimension-compAlignment
        prefPaddingInParent.put("Form-jButton1-1-1", new Integer(11)); // parentId-compId-dimension-compAlignment
        prefPaddingInParent.put("Form-jButton2-1-1", new Integer(11)); // parentId-compId-dimension-compAlignment
        prefPaddingInParent.put("Form-jTextField1-0-0", new Integer(10)); // parentId-compId-dimension-compAlignment
        prefPaddingInParent.put("Form-jTextField1-0-1", new Integer(10)); // parentId-compId-dimension-compAlignment
        prefPadding.put("jButton1-jButton3-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPadding.put("jButton1-jButton2-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPaddingInParent.put("Form-jButton1-1-1", new Integer(11)); // parentId-compId-dimension-compAlignment
        prefPaddingInParent.put("Form-jButton2-1-1", new Integer(11)); // parentId-compId-dimension-compAlignment
        prefPadding.put("jButton1-jButton3-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPadding.put("jButton1-jButton2-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
// > START RESIZING
        baselinePosition.put("jButton1-73-130", new Integer(69));
        compPrefSize.put("jButton1", new Dimension(73, 23));
        {
            String[] compIds = new String[]{
                "jButton1"
            };
            Rectangle[] bounds = new Rectangle[]{
                new Rectangle(125, 37, 73, 130)
            };
            Point hotspot = new Point(162, 170);
            int[] resizeEdges = new int[]{
                -1,
                1
            };
            boolean inLayout = true;
            ld.startResizing(compIds, bounds, hotspot, resizeEdges, inLayout);
        }
// < START RESIZING
        prefPaddingInParent.put("Form-jButton1-1-1", new Integer(11)); // parentId-compId-dimension-compAlignment
// > MOVE
        {
            Point p = new Point(170, 79);
            String containerId = "Form";
            boolean autoPositioning = true;
            boolean lockDimension = false;
            Rectangle[] bounds = new Rectangle[]{
                new Rectangle(125, 37, 73, 39)
            };
            ld.move(p, containerId, autoPositioning, lockDimension, bounds);
        }
// < MOVE
        prefPaddingInParent.put("Form-jButton1-1-1", new Integer(11)); // parentId-compId-dimension-compAlignment
// > MOVE
        {
            Point p = new Point(170, 78);
            String containerId = "Form";
            boolean autoPositioning = true;
            boolean lockDimension = false;
            Rectangle[] bounds = new Rectangle[]{
                new Rectangle(125, 37, 73, 38)
            };
            ld.move(p, containerId, autoPositioning, lockDimension, bounds);
        }
// < MOVE
// > END MOVING
        compPrefSize.put("jTextField1", new Dimension(59, 20));
        prefPadding.put("jScrollPane1-jButton3-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPadding.put("jScrollPane1-jButton2-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPadding.put("jButton1-jButton3-1-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPaddingInParent.put("Form-jButton1-0-1", new Integer(10)); // parentId-compId-dimension-compAlignment
        ld.endMoving(true);
// < END MOVING
        ld.externalSizeChangeHappened();
// > UPDATE CURRENT STATE
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        contInterior.put("Form", new Rectangle(0, 0, 400, 178));
        compBounds.put("jTextField1", new Rectangle(10, 11, 380, 20));
        baselinePosition.put("jTextField1-380-20", new Integer(14));
        compBounds.put("jScrollPane1", new Rectangle(37, 37, 35, 130));
        baselinePosition.put("jScrollPane1-35-130", new Integer(0));
        compBounds.put("jButton1", new Rectangle(125, 37, 73, 38));
        baselinePosition.put("jButton1-73-38", new Integer(23));
        compBounds.put("jButton3", new Rectangle(228, 115, 73, 23));
        baselinePosition.put("jButton3-73-23", new Integer(15));
        compBounds.put("jButton2", new Rectangle(228, 144, 73, 23));
        baselinePosition.put("jButton2-73-23", new Integer(15));
        compMinSize.put("Form", new Dimension(250, 178));
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        compPrefSize.put("jTextField1", new Dimension(59, 20));
        prefPadding.put("jScrollPane1-jButton3-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPadding.put("jScrollPane1-jButton2-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPaddingInParent.put("Form-jButton1-0-1", new Integer(10)); // parentId-compId-dimension-compAlignment
        prefPadding.put("jButton1-jButton3-1-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        contInterior.put("Form", new Rectangle(0, 0, 400, 178));
        compBounds.put("jTextField1", new Rectangle(10, 11, 380, 20));
        baselinePosition.put("jTextField1-380-20", new Integer(14));
        compBounds.put("jScrollPane1", new Rectangle(37, 37, 35, 130));
        baselinePosition.put("jScrollPane1-35-130", new Integer(0));
        compBounds.put("jButton1", new Rectangle(125, 37, 73, 38));
        baselinePosition.put("jButton1-73-38", new Integer(23));
        compBounds.put("jButton3", new Rectangle(228, 115, 73, 23));
        baselinePosition.put("jButton3-73-23", new Integer(15));
        compBounds.put("jButton2", new Rectangle(228, 144, 73, 23));
        baselinePosition.put("jButton2-73-23", new Integer(15));
        compMinSize.put("Form", new Dimension(250, 178));
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        compPrefSize.put("jTextField1", new Dimension(59, 20));
        prefPadding.put("jScrollPane1-jButton3-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPadding.put("jScrollPane1-jButton2-0-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPaddingInParent.put("Form-jButton1-0-1", new Integer(10)); // parentId-compId-dimension-compAlignment
        prefPadding.put("jButton1-jButton3-1-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        ld.updateCurrentState();
// < UPDATE CURRENT STATE
        compBounds.put("Form", new Rectangle(0, 0, 400, 178));
        prefPadding.put("jButton1-jButton3-1-0-0", new Integer(6)); // comp1Id-comp2Id-dimension-comp2Alignment-paddingType
        prefPaddingInParent.put("Form-jButton1-0-1", new Integer(10)); // parentId-compId-dimension-compAlignment
    }
}

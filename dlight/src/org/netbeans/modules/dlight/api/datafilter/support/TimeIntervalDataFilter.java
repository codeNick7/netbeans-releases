/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */
package org.netbeans.modules.dlight.api.datafilter.support;

import org.netbeans.modules.dlight.util.Range;
import org.netbeans.modules.dlight.util.Util;

/**
 *
 * @author mt154047
 */
public final class TimeIntervalDataFilter implements NumericDataFilter<Range<Long>> {

    private final Range<Long> timeInterval;

    /**
     * Null <code>timeInterval.start</code> is replaced with <code>Long.MIN_VALUE</code>,
     * null <code>timeInterval.end</code> is replaced with <code>Long.MAX_VALUE</code>.
     *
     * @param timeInterval
     */
    TimeIntervalDataFilter(Range<Long> timeInterval) {
        if (timeInterval == null) {
            throw new NullPointerException("timeInterval is null"); // NOI18N
        }
        if (timeInterval.getStart() != null && timeInterval.getEnd() != null) {
            this.timeInterval = timeInterval;
        } else {
            this.timeInterval = new Range<Long>(
                    Util.maskNull(timeInterval.getStart(), Long.MIN_VALUE),
                    Util.maskNull(timeInterval.getEnd(), Long.MAX_VALUE));
        }
    }

    /**
     * @return  time interval with non-null <code>start</code> and <code>end</code>
     */
    public Range<Long> getInterval() {
        return timeInterval;
    }
}

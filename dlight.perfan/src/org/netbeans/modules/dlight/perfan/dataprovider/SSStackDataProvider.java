/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.modules.dlight.perfan.dataprovider;

import java.text.DecimalFormat;
import java.text.ParseException;
import org.netbeans.modules.dlight.perfan.util.TasksCachedProcessor;
import org.netbeans.modules.dlight.perfan.util.Computable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.netbeans.modules.dlight.api.storage.DataTableMetadata.Column;
import org.netbeans.modules.dlight.core.stack.dataprovider.FunctionCallTreeTableNode;
import org.netbeans.modules.dlight.core.stack.dataprovider.StackDataProvider;
import org.netbeans.modules.dlight.core.stack.api.Function;
import org.netbeans.modules.dlight.core.stack.api.FunctionCall;
import org.netbeans.modules.dlight.core.stack.api.FunctionMetric;
import org.netbeans.modules.dlight.perfan.stack.impl.FunctionCallImpl;
import org.netbeans.modules.dlight.perfan.stack.impl.FunctionImpl;
import org.netbeans.modules.dlight.perfan.storage.impl.Metrics;
import org.netbeans.modules.dlight.perfan.storage.impl.PerfanDataStorage;
import org.netbeans.modules.dlight.spi.storage.DataStorage;
import org.netbeans.modules.dlight.util.DLightLogger;

/**
 * This class suppose to be thread-safe.
 * This means that it's methods (getHotSpotFunctions) can be called from any
 * thread at any moment.
 *
 * As any operation with er_print takes a while (could be significant time),
 * not all requests can be served in time.
 * So, the policy here is:
 *    - when request is arrives, it is stored in a queue and it's processing
 *      starts;
 *
 *    - CALLER THREAD IS BLOCKED until after result fetching.
 *
 *    - in case when new request arrives as long as it is not THE SAME request,
 *      it is stored to the queue
 *
 *    - in case when THE SAME request arrives prior to processing for the previous
 *      one is done, the result will be the one, received from the ORIGINAL (FIRST)
 *      request.
 *
 */
class SSStackDataProvider implements StackDataProvider {

    private static final Logger log = DLightLogger.getLogger(SSStackDataProvider.class);
    private final Computable<HotSpotFunctionsFetcherParams, List<FunctionCall>> hotSpotFunctionsFetcher =
            new TasksCachedProcessor<HotSpotFunctionsFetcherParams, List<FunctionCall>>(new HotSpotFunctionsFetcher(), true);
    private List<FunctionMetric> metricsList = Arrays.asList(
            TimeMetric.UserFuncTimeExclusive,
            TimeMetric.UserFuncTimeInclusive,
            TimeMetric.SyncWaitCallInclusive,
            TimeMetric.SyncWaitTimeInclusive,
            MemoryMetric.LeakBytesMetric,
            MemoryMetric.LeaksCountMetric);
    private PerfanDataStorage storage;

    private static enum CC_MODE {

        CALLEES,
        CALLERS
    };

    public SSStackDataProvider() {
    }

    public synchronized List<FunctionCall> getCallers(FunctionCall[] path, boolean aggregate) {
        return getCallersCallees(CC_MODE.CALLERS, path, aggregate);
    }

    public synchronized List<FunctionCall> getCallees(FunctionCall[] path, boolean aggregate) {
        return getCallersCallees(CC_MODE.CALLEES, path, aggregate);
    }

    public List<FunctionCallTreeTableNode> getTableView(List<Column> columns, List<Column> orderBy, int limit) {
        return FunctionCallTreeTableNode.getFunctionCallTreeTableNodes(getHotSpotFunctions(columns, orderBy, limit));
    }

    public List<FunctionCallTreeTableNode> getChildren(List<FunctionCallTreeTableNode> path) {
        return FunctionCallTreeTableNode.getFunctionCallTreeTableNodes(getCallers(FunctionCallTreeTableNode.getFunctionCalls(path).toArray(new FunctionCall[0]), false));
    }

    public FunctionCallTreeTableNode getValueAt(int row) {
        return null;
    }

    public String getTableValueAt(Column column, int row) {
        return null;
    }

    // TODO: implement
    private synchronized List<FunctionCall> getCallersCallees(CC_MODE mode, FunctionCall[] path, boolean aggregate) {
        //TODO: Now just take the last from the path...
//        FunctionCall parent_fc = path[path.length - 1];
//
//        Object[] raw_data = mode == CC_MODE.CALLEES ? storage.getCallees(((FunctionImpl) parent_fc.getFunction()).getRef()) : storage.getCallers(((FunctionImpl) parent_fc.getFunction()).getRef());
//
//        if (raw_data == null || raw_data.length == 0) {
//            return Collections.emptyList();
//        }
//
//        SortedSet<FunctionCall> result = rawDataToResult(raw_data, NaturalFunctionCallComparator.getInstance(TimeMetric.UserFuncTimeInclusive));
//
//        List<FunctionCall> callees = new ArrayList<FunctionCall>(10);
//        Iterator<FunctionCall> it = result.iterator();
//        while (it.hasNext()) {
//            FunctionCall fc = it.next();
//            callees.add(fc);
//        }
//
//        return callees;
        return Collections.emptyList();
    }

    public List<FunctionCall> getHotSpotFunctions(
            final List<Column> columns, final List<Column> orderBy, final int limit) {

        try {
            return hotSpotFunctionsFetcher.compute(new HotSpotFunctionsFetcherParams(columns, orderBy, limit));
        } catch (InterruptedException ex) {
            log.fine("HotSpotFunctionsFetcher interrupted."); // NOI18N
        }

        return Collections.emptyList();
    }

    // TODO: !!!
    private FunctionMetric getMetricInstance(String name) {
        for (FunctionMetric metric : metricsList) {
            if (metric.getMetricID().equals(name)) {
                return metric;
            }
        }

        return null;
    }

    public List<FunctionMetric> getMetricsList() {
        return metricsList;
    }

    public void attachTo(DataStorage storage) {
        if (storage instanceof PerfanDataStorage) {
            this.storage = (PerfanDataStorage) storage;
        } else {
            String msg = "Attempt to attach SSStackDataProvider to storage " +
                    "'" + storage + "'"; // NOI18N

            throw new IllegalArgumentException(msg);
        }
    }

    private static class HotSpotFunctionsFetcherParams {

        private final List<Column> columns;
        private final List<Column> orderBy;
        private final int limit;
        private final Metrics metrics;

        public HotSpotFunctionsFetcherParams(
                final List<Column> columns,
                final List<Column> orderBy,
                final int limit) {

            if (columns == null) {
                throw new NullPointerException();
            }

            if (columns.isEmpty()) {
                throw new IllegalArgumentException("HotSpotFunctionsFetcherParams: empty columns list!"); // NOI18N
            }

            this.columns = columns;
            this.orderBy = orderBy == null ? Arrays.asList(columns.get(0)) : orderBy;
            this.limit = limit;
            this.metrics = Metrics.constructFrom(columns, orderBy);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof HotSpotFunctionsFetcherParams)) {
                throw new IllegalArgumentException();
            }
            HotSpotFunctionsFetcherParams o = (HotSpotFunctionsFetcherParams) obj;
            return o.metrics.equals(metrics) && o.limit == limit;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + this.limit;
            hash = 71 * hash + (this.metrics != null ? this.metrics.hashCode() : 0);
            return hash;
        }
    }

    private class HotSpotFunctionsFetcher
            implements Computable<HotSpotFunctionsFetcherParams, List<FunctionCall>> {

        private final DecimalFormat df = new DecimalFormat();

        public HotSpotFunctionsFetcher() {
            df.getDecimalFormatSymbols().setDecimalSeparator(',');
        }

        public List<FunctionCall> compute(HotSpotFunctionsFetcherParams taskArguments) throws InterruptedException {
            log.fine("Started to fetch Hot Spot Functions"); // NOI18N

            Metrics metrics = taskArguments.metrics;

            String[] er_result = storage.getTopFunctions(
                    metrics, taskArguments.limit);

            if (er_result == null || er_result.length == 0) {
                return Collections.emptyList();
            }

            int limit = Math.min(er_result.length, taskArguments.limit);
            ArrayList<FunctionCall> result = new ArrayList<FunctionCall>(limit);

            int colCount = taskArguments.columns.size();
            Column primarySortColumn = taskArguments.orderBy.get(0);

            for (int i = 0; i < limit; i++) {
                String[] info = er_result[i].split("[ \t]+", colCount); // NOI18N
                String name = info[colCount - 1];
                Function f = new FunctionImpl(name, name.hashCode());

                Map<FunctionMetric, Object> metricsValues =
                        new HashMap<FunctionMetric, Object>();

                // Will skip function if value of primary sorting metric == 0
                boolean skipFunction = false;
                
                for (int midx = 0; midx < colCount - 1; midx++) {
                    Column col = taskArguments.columns.get(midx);
                    String colName = col.getColumnName();
                    Class colClass = col.getColumnClass();
                    FunctionMetric metric = getMetricInstance(colName);

                    Object value = info[midx];
                    try {
                        Number nvalue = df.parse(info[midx]);

                        if (Integer.class == colClass) {
                            value = new Integer(nvalue.intValue());
                        } else if (Double.class == colClass) {
                            value = new Double(nvalue.doubleValue());
                        } else if (Float.class == colClass) {
                            value = new Float(nvalue.floatValue());
                        }

                    } catch (ParseException ex) {
                        // use plain info[midx]
                    }
                    metricsValues.put(metric, value);
                }

                if (!skipFunction) {
                    FunctionCallImpl fc = new FunctionCallImpl(f, metricsValues);
                    result.add(fc);
                }
            }

            log.fine("Done with Hot Spot Functions fetching"); // NOI18N

            return result;
        }
    }
}

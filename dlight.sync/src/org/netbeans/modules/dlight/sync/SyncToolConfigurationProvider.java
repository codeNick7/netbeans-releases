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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.modules.dlight.sync;

import java.awt.Color;
import java.beans.FeatureDescriptor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.modules.dlight.api.collector.DataCollectorConfiguration;
import org.netbeans.modules.dlight.api.indicator.IndicatorConfiguration;
import org.netbeans.modules.dlight.api.indicator.IndicatorDataProviderConfiguration;
import org.netbeans.modules.dlight.api.indicator.IndicatorMetadata;
import org.netbeans.modules.dlight.api.storage.DataRow;
import org.netbeans.modules.dlight.api.storage.DataTableMetadata;
import org.netbeans.modules.dlight.api.storage.DataTableMetadata.Column;
import org.netbeans.modules.dlight.api.storage.DataUtil;
import org.netbeans.modules.dlight.api.storage.StandardColumns;
import org.netbeans.modules.dlight.api.storage.types.Time;
import org.netbeans.modules.dlight.api.tool.DLightToolConfiguration;
import org.netbeans.modules.dlight.api.visualizer.VisualizerConfiguration;
import org.netbeans.modules.dlight.collector.stdout.CLIODCConfiguration;
import org.netbeans.modules.dlight.collector.stdout.CLIOParser;
import org.netbeans.modules.dlight.core.stack.api.support.FunctionDatatableDescription;
import org.netbeans.modules.dlight.dtrace.collector.DTDCConfiguration;
import org.netbeans.modules.dlight.indicators.DataRowToTimeSeries;
import org.netbeans.modules.dlight.indicators.TimeSeriesIndicatorConfiguration;
import org.netbeans.modules.dlight.indicators.TimeSeriesDescriptor;
import org.netbeans.modules.dlight.perfan.SunStudioDCConfiguration;
import org.netbeans.modules.dlight.perfan.SunStudioDCConfiguration.CollectedInfo;
import org.netbeans.modules.dlight.spi.tool.DLightToolConfigurationProvider;
import org.netbeans.modules.dlight.tools.LLDataCollectorConfiguration;
import org.netbeans.modules.dlight.tools.ProcDataProviderConfiguration;
import org.netbeans.modules.dlight.util.DLightLogger;
import org.netbeans.modules.dlight.visualizers.api.ColumnsUIMapping;
import org.netbeans.modules.dlight.visualizers.api.FunctionName;
import org.netbeans.modules.dlight.visualizers.api.FunctionsListViewVisualizerConfiguration;
import org.openide.util.NbBundle;

/**
 *
 * @author Vladimir Kvashin
 */
public final class SyncToolConfigurationProvider implements DLightToolConfigurationProvider {

    private static final int INDICATOR_POSITION = 300;
    private static final String ID = "dlight.tool.sync"; // NOI18N
    private static final String TOOL_NAME = loc("SyncTool.ToolName"); // NOI18N
    private static final String TOOL_DESCRIPTION = loc("SyncTool.ToolDescription");//NOI18N
    private static final Column waiterColumn =
        new Column("waiter", Integer.class, loc("SyncTool.ColumnName.waiter"), null); // NOI18N
    private static final Column mutexColumn =
        new Column("mutex", Long.class, loc("SyncTool.ColumnName.mutex"), null); // NOI18N
    private static final Column blockerColumn =
        new Column("blocker", Integer.class, loc("SyncTool.ColumnName.blocker"), null); // NOI18N
    private static final Column timeColumn =
        new Column("time", Time.class, loc("SyncTool.ColumnName.time"), null); // NOI18N
    private static final Column locksColumn =
        new Column("locks", Float.class, loc("SyncTool.ColumnName.locks"), null); // NOI18N
    private static final Column threadsColumn =
        new Column("threads", Integer.class, loc("SyncTool.ColumnName.threads"), null); // NOI18N
    private static final DataTableMetadata rawTableMetadata;


    static {
        List<Column> rawColumns = Arrays.asList(
            StandardColumns.TIMESTAMP_COLUMN,
            waiterColumn,
            mutexColumn,
            blockerColumn,
            timeColumn,
            StandardColumns.STACK_COLUMN);

        rawTableMetadata = new DataTableMetadata(
                "sync", rawColumns, null); // NOI18N
    }

    public SyncToolConfigurationProvider() {
    }

    public DLightToolConfiguration create() {
        DLightToolConfiguration toolConfiguration = new DLightToolConfiguration(ID, TOOL_NAME);
        toolConfiguration.setLongName(TOOL_DESCRIPTION);
        toolConfiguration.setDescription(loc("SyncTool.ToolDescription.Details"));//NOI18N
        toolConfiguration.setIcon("org/netbeans/modules/dlight/sync/resources/threads.png");//NOI18N
        FeatureDescriptor descriptor = new FeatureDescriptor();
        descriptor.setValue(DTDCConfiguration.DSCRIPT_TOOL_PROPERTY, getScriptUrl());
        toolConfiguration.setFeatureDescriptor(descriptor);
        List<DataCollectorConfiguration> dcConfigurations = initDataCollectorConfigurations();
        for (DataCollectorConfiguration dc : dcConfigurations) {
            toolConfiguration.addDataCollectorConfiguration(dc);
        }
        List<IndicatorDataProviderConfiguration> idpcs = initIndicatorDataProviderConfigurations();
        for (IndicatorDataProviderConfiguration idpc : idpcs) {
            toolConfiguration.addIndicatorDataProviderConfiguration(idpc);
        }
        IndicatorConfiguration ic = initIndicatorConfiguration();
        toolConfiguration.addIndicatorConfiguration(ic);

        return toolConfiguration;
    }

    private URL getScriptUrl() {
        return getClass().getResource("resources/sync.d"); // NOI18N
    }

    private List<DataCollectorConfiguration> initDataCollectorConfigurations() {
        List<DataCollectorConfiguration> result = new ArrayList<DataCollectorConfiguration>();
        result.add(new SunStudioDCConfiguration(CollectedInfo.SYNCHRONIZATION));
        result.add(new LLDataCollectorConfiguration(LLDataCollectorConfiguration.CollectedData.SYNC));
        DTDCConfiguration dataCollectorConfiguration =
            new DTDCConfiguration(getScriptUrl(), Arrays.asList(rawTableMetadata));

        dataCollectorConfiguration.setStackSupportEnabled(true);
        dataCollectorConfiguration.setIndicatorFiringFactor(1);
        dataCollectorConfiguration.setOutputPrefix("sync:"); // NOI18N
        result.add(dataCollectorConfiguration);

        return result;
    }

    private IndicatorConfiguration initIndicatorConfiguration() {
        //VisualizerConfiguration vc = null;

        IndicatorMetadata indicatorMetadata = null;
        List<Column> indicatorColumns = new ArrayList<Column>();
        indicatorColumns.add(locksColumn);
        indicatorColumns.add(threadsColumn);
        indicatorColumns.add(SunStudioDCConfiguration.c_ulockSummary);
        indicatorColumns.add(ProcDataProviderConfiguration.THREADS);
        indicatorColumns.addAll(LLDataCollectorConfiguration.SYNC_TABLE.getColumns());
        indicatorMetadata = new IndicatorMetadata(indicatorColumns);

        TimeSeriesIndicatorConfiguration indicatorConfiguration = new TimeSeriesIndicatorConfiguration(
                indicatorMetadata, INDICATOR_POSITION);
        indicatorConfiguration.setPersistencePrefix("dlight_sync"); // NOI18N
        indicatorConfiguration.setTitle(loc("indicator.title")); // NOI18N
        indicatorConfiguration.setGraphScale(2);
        final TimeSeriesDescriptor threadsDesciptor = new TimeSeriesDescriptor("threads", loc("graph.description.threads"), new Color(0xB2, 0xBC, 0x00), TimeSeriesDescriptor.Kind.ABS_SURFACE); // NOI18N
        final TimeSeriesDescriptor syncDescriptor = new TimeSeriesDescriptor("locks", loc("graph.description.locks"), new Color(231, 111, 0), TimeSeriesDescriptor.Kind.ABS_SURFACE); // NOI18N
        final List<Column> threadColumnsList = Arrays.asList(threadsColumn, ProcDataProviderConfiguration.THREADS, LLDataCollectorConfiguration.threads_count);
        final List<Column> lockColumnsList = Arrays.asList(locksColumn, SunStudioDCConfiguration.c_ulockSummary, LLDataCollectorConfiguration.LOCKS_COUNT);
        threadsDesciptor.setSourceColumns(threadColumnsList);
        syncDescriptor.setSourceColumns(lockColumnsList);
        indicatorConfiguration.addTimeSeriesDescriptors(
                threadsDesciptor,syncDescriptor); // NOI18N
        indicatorConfiguration.setDataRowHandler(
                new DataRowToSync( threadColumnsList,lockColumnsList));
        indicatorConfiguration.setActionDisplayName(loc("indicator.action")); // NOI18N
        indicatorConfiguration.setActionTooltip(loc("indicator.action.tooltip"));//NOI18N

        indicatorConfiguration.addVisualizerConfiguration(getDetails(rawTableMetadata));

        // Then configure what happens when user clicks on the indicator...
        // configure metadata for the detailed view ...
        DataTableMetadata detailedViewTableMetadata =
            SunStudioDCConfiguration.getSyncTableMetadata(
            SunStudioDCConfiguration.c_name,
            SunStudioDCConfiguration.c_eSync,
            SunStudioDCConfiguration.c_eSyncn);
        FunctionDatatableDescription functionDesc = new FunctionDatatableDescription(
                SunStudioDCConfiguration.c_name.getColumnName(), 
                SunStudioDCConfiguration.c_name.getColumnName(),
                null, /* offset */
                null /* context */);
//        indicatorConfiguration.addVisualizerConfiguration(new AdvancedTableViewVisualizerConfiguration(detailedViewTableMetadata,
//            SunStudioDCConfiguration.c_name.getColumnName(), SunStudioDCConfiguration.c_name.getColumnName()));

        FunctionsListViewVisualizerConfiguration tableVisualizerConfiguration =
            new FunctionsListViewVisualizerConfiguration(detailedViewTableMetadata, functionDesc, Arrays.asList(SunStudioDCConfiguration.c_eSync, SunStudioDCConfiguration.c_eSyncn));
        ColumnsUIMapping uiMapping = new ColumnsUIMapping();
        uiMapping.setColumnUI(SunStudioDCConfiguration.c_name.getColumnName(), loc("SyncTool.ColumnName.func_name"), loc("SyncTool.ColumnTooltip.func_name")); // NOI18N
        uiMapping.setColumnUI(SunStudioDCConfiguration.c_eSync.getColumnName(), loc("SyncTool.ColumnName.e_sync"), loc("SyncTool.ColumnTooltip.e_sync")); // NOI18N
        uiMapping.setColumnUI(SunStudioDCConfiguration.c_eSyncn.getColumnName(), loc("SyncTool.ColumnName.syncn"), loc("SyncTool.ColumnTooltip.syncn")); // NOI18N
        tableVisualizerConfiguration.setColumnsUIMapping(uiMapping);
        tableVisualizerConfiguration.setEmptyAnalyzeMessage(loc("DetailedView.EmptyAnalyzeMessage"));//NOI18N
        tableVisualizerConfiguration.setEmptyRunningMessage(loc("DetailedView.EmptyRunningMessage"));//NOI18N
        indicatorConfiguration.addVisualizerConfiguration(tableVisualizerConfiguration);

        return indicatorConfiguration;
    }

    private List<IndicatorDataProviderConfiguration> initIndicatorDataProviderConfigurations() {

        List<IndicatorDataProviderConfiguration> lockIndicatorDataProviders = new ArrayList<IndicatorDataProviderConfiguration>();
        final DataTableMetadata indicatorTableMetadata = new DataTableMetadata(
                "locks", Arrays.asList(locksColumn, threadsColumn), null); // NOI18N
        List<DataTableMetadata> indicatorTablesMetadata = Arrays.asList(indicatorTableMetadata);
        CLIODCConfiguration lockConf = new CLIODCConfiguration(
            "/bin/prstat", "-mv -p @PID -c 1", // NOI18N
            new SyncCLIOParser(locksColumn, threadsColumn), indicatorTablesMetadata);
        lockConf.setName("prstat");//NOI18N
        lockIndicatorDataProviders.add(lockConf);

        lockIndicatorDataProviders.add(new SunStudioDCConfiguration(CollectedInfo.SYNCSUMMARY));
        lockIndicatorDataProviders.add(new ProcDataProviderConfiguration());

        lockIndicatorDataProviders.add(new LLDataCollectorConfiguration(
            LLDataCollectorConfiguration.CollectedData.SYNC));

        return lockIndicatorDataProviders;
    }

    private static class SyncCLIOParser implements CLIOParser {

        private static final Pattern TOTAL = Pattern.compile("^Total: \\d+ processes, (\\d+) lwps"); // NOI18N
        private final List<String> colnames;
        private float locks;
        private boolean nextLineShouldBeTotal;

        public SyncCLIOParser(Column locksColumn, Column threadsColumn) {
            colnames = Arrays.asList(locksColumn.getColumnName(), threadsColumn.getColumnName());
        }

        public DataRow process(String line) {
            if (DLightLogger.instance.isLoggable(Level.FINE)) {
                DLightLogger.instance.fine("SyncCLIOParser: " + line); //NOI18N
            }

            /*----- Below is an example of output: -------------------------
            PID USERNAME USR SYS TRP TFL DFL LCK SLP LAT VCX ICX SCL SIG PROCESS/NLWP
            2732 vk155633 0.0 0.0 0.0 0.0 0.0 0.0 100 0.0   0   0   0   0 dlight_simpl/1
            Total: 1 processes, 1 lwps, load averages: 0.30, 0.27, 0.34
            ---------------------------------------------------------------- */

            if (line == null || line.length() == 0) {
                return null;
            }

            line = line.trim();

            if (Character.isDigit(line.charAt(0))) {
                line = line.replaceAll(",", ".");//NOI18N
                String[] tokens = line.split("[ \t]+");//NOI18N
                if (tokens.length < 8) {
                    return null;
                }
                try {
                    locks = Float.parseFloat(tokens[7]);
                    nextLineShouldBeTotal = true;
                } catch (NumberFormatException ex) {
                }
            } else if (nextLineShouldBeTotal) {
                Matcher m = TOTAL.matcher(line);
                if (m.find()) {
                    try {
                        int threads = Integer.parseInt(m.group(1));
                        return new DataRow(colnames, Arrays.asList(locks, threads));
                    } catch (NumberFormatException ex) {
                    } finally {
                        nextLineShouldBeTotal = false;
                    }
                }
            }
            return null;
        }
    }

    private VisualizerConfiguration getDetails(DataTableMetadata rawTableMetadata) {
        DataTableMetadata viewTableMetadata = null;
        Column syncTimeColumn = new Column("time", Time.class, loc("SyncTool.ColumnName.e_sync"), null);//NOI18N
        Column syncCountColumn = new Column("count", Long.class, loc("SyncTool.ColumnName.syncn"), null);//NOI18N
        List<Column> viewColumns = Arrays.asList(
            new Column("id", Integer.class, "id", null),// NOI18N
            new Column("func_name", FunctionName.class, "Function", null),// NOI18N
            syncTimeColumn,
            syncCountColumn);
        
        String sql = "select stacknode.func_id as id, fname as func_name, offset, sum(sync.time) as time, count(*) as count" // NOI18N
                + " from sync inner join stacknode on stacknode.id = sync.stack_id" // NOI18N
                + " inner join funcnames on funcnames.id = stacknode.func_id" // NOI18N
                + " group by stacknode.func_id, offset " // NOI18N
                + " order by time desc"; // NOI18N

        viewTableMetadata = new DataTableMetadata("sync", viewColumns, sql, Arrays.asList(rawTableMetadata)); // NOI18N
        FunctionDatatableDescription functionDesc = new FunctionDatatableDescription("id", "func_name", "offset", null); // NOI18N
        FunctionsListViewVisualizerConfiguration tableVisualizerConfiguration =
            new FunctionsListViewVisualizerConfiguration(viewTableMetadata, functionDesc, Arrays.asList(syncTimeColumn, syncCountColumn));
        ColumnsUIMapping uiMapping = new ColumnsUIMapping();
        uiMapping.setColumnUI("func_name", loc("SyncTool.ColumnName.func_name"), loc("SyncTool.ColumnTooltip.func_name")); // NOI18N
        uiMapping.setColumnUI("time", loc("SyncTool.ColumnName.e_sync"), loc("SyncTool.ColumnTooltip.e_sync")); // NOI18N
        uiMapping.setColumnUI("count", loc("SyncTool.ColumnName.syncn"), loc("SyncTool.ColumnTooltip.syncn")); // NOI18N
        tableVisualizerConfiguration.setColumnsUIMapping(uiMapping);
        tableVisualizerConfiguration.setEmptyAnalyzeMessage(loc("DetailedView.EmptyAnalyzeMessage"));//NOI18N
        tableVisualizerConfiguration.setEmptyRunningMessage(loc("DetailedView.EmptyRunningMessage"));//NOI18N
        return tableVisualizerConfiguration;
    }

    private static String loc(String key, String... params) {
        return NbBundle.getMessage(SyncToolConfigurationProvider.class, key, params);
    }

    private static final class DataRowToSync implements DataRowToTimeSeries {

        private final List<Column> threadColumns;
        private final List<Column> lockColumns;
        private int locks;

        public DataRowToSync(List<Column> threadColumns, List<Column> lockColumns) {
            this.threadColumns = new ArrayList<Column>(threadColumns);
            this.lockColumns = new ArrayList<Column>(lockColumns);
        }

        @Override
        public float[] getData(DataRow row) {
            // Warning! Threads and locks can come in different data rows!
            boolean success = false;
            int threads = 0;
            for (String columnName : row.getColumnNames()) {
                for (Column threadColumn : threadColumns) {
                    if (threadColumn.getColumnName().equals(columnName)) {
                        success = true;
                        threads = DataUtil.toInt(row.getData(columnName));
                    }
                }
                for (Column lockColumn : lockColumns) {
                    if (lockColumn.getColumnName().equals(columnName)) {
                        locks = DataUtil.toInt(row.getData(columnName));
                    }
                }
            }
            if (success) {
                return new float[] { threads, threads * Math.min(locks, 100) / 100.0f };
            } else {
                return null;
            }
        }

        @Override
        public Map<String, String> getDetails() {
            return Collections.emptyMap();
        }
    }
}

package com.appdynamics.monitors.voltdb.responseprocessor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;

public class VoltDBResponseProcessor implements VoltResponseProcessor {

    private static final Logger LOG = Logger.getLogger(VoltDBResponseProcessor.class);

    private final String metricPath;
    private final List<String> appenderColumns;

    public VoltDBResponseProcessor(String metricPath, List<String> appenderColumns) {
        this.metricPath = metricPath;
        this.appenderColumns = appenderColumns;
    }

    public VoltDBResponseProcessor(String metricPath) {
        this(metricPath, Lists.<String>newArrayList());
    }

    /**
     * @param clientResponse
     * @return
     */
    public Map<String, Number> process(ClientResponse clientResponse) {
        Map<String, Number> statsMap = Maps.newHashMap();
        if (clientResponse.getStatus() == 1) {
            VoltTable[] tables = clientResponse.getResults();
            VoltTable table = tables[0];
            statsMap = processResult(table);
        } else {
            LOG.error("Received failed response [" + clientResponse.getStatusString() + "] from voltDB");
        }
        return statsMap;
    }


    /**
     * Extracts stats From the given {@code VoltTable}
     *
     * @param table
     * @return
     */
    private Map<String, Number> processResult(VoltTable table) {
        Map<String, Number> statsMap = new TreeMap<String, Number>();
        for (int rows = 0; rows < table.getRowCount(); rows++) {
            if (table.advanceRow()) {
                int columns = table.getColumnCount();
                for (int i = 0; i < columns; i++) {
                    VoltType voltType = table.getColumnType(i);
                    String columnName = table.getColumnName(i);
                    if ((!columnName.equalsIgnoreCase("timestamp")) && (!columnName.equalsIgnoreCase("site_id")) &&
                            (!columnName.equalsIgnoreCase("partition_id")) && (!columnName.equalsIgnoreCase("weighted_perc")) &&
                            (!columnName.equalsIgnoreCase("host_id")) && (!columnName.equalsIgnoreCase("hostname"))) {

                        Number value = null;
                        switch (voltType.ordinal()) {
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                value = table.getLong(i);
                                if (table.wasNull()) {
                                    value = null;
                                }
                                break;
                            case 7:
                                value = table.getDouble(i);
                                if (table.wasNull()) {
                                    value = null;
                                }
                                break;
                            case 11:
                                value = (BigDecimal) table.get(i, voltType);
                                if (table.wasNull()) {
                                    value = null;
                                }
                        }
                        if (value != null) {
                            String key = buildMetricKey(table, columnName);
                            statsMap.put(key, value);
                        }
                    }
                }
            }
        }
        return statsMap;
    }

    private String buildMetricKey(VoltTable table, String columnName) {
        StringBuilder sb = new StringBuilder(metricPath);
        if (appenderColumns != null && !appenderColumns.isEmpty()) {
            for (String appenderColumnName : appenderColumns) {
                int appenderColumnIndex = table.getColumnIndex(appenderColumnName);
                VoltType appenderColumnType = table.getColumnType(appenderColumnIndex);
                sb.append("|").append(String.valueOf(table.get(appenderColumnName, appenderColumnType)));
            }
        }

        sb.append("|").append(columnName);

        return sb.toString();
    }
}

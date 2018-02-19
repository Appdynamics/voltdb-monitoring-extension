/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb.responseprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class VoltDBJsonResponseProcessor implements JsonResponseProcessor {
    private static final Logger LOG = Logger.getLogger(VoltDBJsonResponseProcessor.class);
    private final String metricPath;
    private final List<String> appenderColumns;

    public VoltDBJsonResponseProcessor(String metricPath, List<String> appenderColumns) {
        this.metricPath = metricPath;
        this.appenderColumns = appenderColumns;
    }

    public Map<String, Number> parseJson(String json) throws TaskExecutionException {
        try {
            Table table = buildTable(json);
            Map<String, Number> stats = getStats(table);
            return stats;
        } catch (IOException e) {
            LOG.error("Error processing response", e);
            throw new TaskExecutionException("Error processing response", e);
        }
    }

    private Table buildTable(String json) throws IOException {
        ObjectMapper m = new ObjectMapper();
        JsonNode rootNode = m.readTree(json);
        JsonNode schema = rootNode.findValue("schema");

        List<JsonNode> fieldNamesJson = schema.findValues("name");
        List<JsonNode> typesJson = schema.findValues("type");

        Iterator<JsonNode> data = rootNode.findValue("data").elements();
        Table table = new Table();
        while (data.hasNext()) {
            JsonNode rowValues = data.next();
            Iterator<JsonNode> elements = rowValues.elements();
            ArrayList<JsonNode> values = Lists.newArrayList(elements);

            Row row = new Row();
            table.addRow(row);

            int i = 0;
            for (JsonNode fieldName : fieldNamesJson) {
                String fieldNameString = fieldName.asText();
                int type = typesJson.get(i).intValue();
                switch (type) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        Column<Long> longColumn = new Column<Long>();
                        longColumn.setName(fieldNameString);
                        longColumn.setValue(values.get(i).asLong());
                        row.addColumn(longColumn);
                        break;
                    case 7:
                        Column<Double> doubleColumn = new Column<Double>();
                        doubleColumn.setName(fieldNameString);
                        doubleColumn.setValue(values.get(i).asDouble());
                        row.addColumn(doubleColumn);
                        break;
                    case 11:
                        Column<BigDecimal> decimalColumn = new Column<BigDecimal>();
                        decimalColumn.setName(fieldNameString);
                        decimalColumn.setValue(values.get(i).decimalValue());
                        row.addColumn(decimalColumn);
                        break;
                    default:
                        Column<String> stringColumn = new Column<String>();
                        stringColumn.setName(fieldNameString);
                        stringColumn.setValue(values.get(i).asText());
                        row.addColumn(stringColumn);
                }
                i++;
            }
        }

        return table;
    }

    private Map<String, Number> getStats(Table table) {
        Map<String, Number> stats = new LinkedHashMap<String, Number>();
        List<Row> rows = table.getRows();
        for (Row row : rows) {
            List<Column> columns = row.getColumns();
            StringBuilder sb = new StringBuilder(metricPath).append("|");
            for (String col : appenderColumns) {
                sb.append(row.getColumnValue(col)).append("|");
            }
            for (Column column : columns) {
                if (!ignoreColumn(column)) {
                    if (!(column.getValue() instanceof String)) {
                        stats.put(sb.toString() + column.getName(), (Number) column.getValue());
                    }
                }
            }
        }
        return stats;
    }

    private boolean ignoreColumn(Column column) {
        String columnName = column.getName();
        if (columnName.equalsIgnoreCase("timestamp") || columnName.equalsIgnoreCase("site_id") ||
                columnName.equalsIgnoreCase("partition_id") || columnName.equalsIgnoreCase("weighted_perc") ||
                columnName.equalsIgnoreCase("host_id") || columnName.equalsIgnoreCase("hostname")) {
            return true;
        }
        return false;
    }
}
/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb.responseprocessor;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

public class Row {

    private List<Column> columns = new ArrayList<Column>();

    public <T> void addColumn(Column<T> column) {
        columns.add(column);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Object getColumnValue(String columnName) {
        Preconditions.checkArgument(columns != null, "No columns found");
        for (Column column : columns) {
            if (column.getName().toLowerCase().equals(columnName.toLowerCase())) {
                return column.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Row " + columns;
    }
}
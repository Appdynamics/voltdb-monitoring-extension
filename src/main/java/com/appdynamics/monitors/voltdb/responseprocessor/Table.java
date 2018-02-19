/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb.responseprocessor;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private List<Row> rows = new ArrayList<Row>();

    public void addRow(Row row) {
        rows.add(row);
    }

    public List<Row> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "Table " + rows;
    }
}
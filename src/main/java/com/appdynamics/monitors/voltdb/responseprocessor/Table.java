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
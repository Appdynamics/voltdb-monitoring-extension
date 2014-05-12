package com.appdynamics.monitors.voltdb.responseprocessor;

public class Column<T> {

    private String name;
    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Column[Name : " + name + ", Value : " + value + "]";
    }
}

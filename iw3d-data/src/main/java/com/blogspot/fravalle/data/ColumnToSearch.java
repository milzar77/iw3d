package com.blogspot.fravalle.data;

import org.apache.cayenne.exp.Property;

public class ColumnToSearch {
    private Property columnProperty;
    private Object columnValue;

    public ColumnToSearch() {}


    public Property getColumnProperty() {
        return columnProperty;
    }

    public void setColumnProperty(Property columnProperty) {
        this.columnProperty = columnProperty;
    }


    public Object getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Object columnValue) {
        this.columnValue = columnValue;
    }

}

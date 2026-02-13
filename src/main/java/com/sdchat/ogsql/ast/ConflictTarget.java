package com.sdchat.ogsql.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the target of an ON CONFLICT clause.
 * Can be a list of columns or a constraint name.
 */
public class ConflictTarget {
    private List<String> columns;
    private String constraintName;

    public ConflictTarget() {
        this.columns = new ArrayList<>();
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public boolean isColumnList() {
        return columns != null && !columns.isEmpty();
    }

    public boolean isConstraintName() {
        return constraintName != null;
    }
}

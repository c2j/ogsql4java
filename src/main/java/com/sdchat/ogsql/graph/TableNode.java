package com.sdchat.ogsql.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a table node in the relationship graph.
 * Contains table name, optional alias, and column information.
 */
public class TableNode {
    private final String name;
    private final String alias;
    private final List<ColumnInfo> columns;

    public TableNode(String name) {
        this(name, null);
    }

    public TableNode(String name, String alias) {
        this.name = Objects.requireNonNull(name, "Table name cannot be null");
        this.alias = alias;
        this.columns = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public boolean hasAlias() {
        return alias != null && !alias.isEmpty();
    }

    public List<ColumnInfo> getColumns() {
        return new ArrayList<>(columns);
    }

    public void addColumn(ColumnInfo column) {
        columns.add(Objects.requireNonNull(column, "Column cannot be null"));
    }

    public String getDisplayName() {
        return hasAlias() ? name + " (" + alias + ")" : name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableNode tableNode = (TableNode) o;
        return name.equals(tableNode.name) && 
               Objects.equals(alias, tableNode.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, alias);
    }

    @Override
    public String toString() {
        return "TableNode{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", columns=" + columns.size() +
                '}';
    }
}

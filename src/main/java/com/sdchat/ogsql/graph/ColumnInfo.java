package com.sdchat.ogsql.graph;

import java.util.Objects;

/**
 * Represents column information within a table.
 */
public class ColumnInfo {
    private final String name;
    private final String qualifiedName;

    public ColumnInfo(String name) {
        this(name, null);
    }

    public ColumnInfo(String name, String qualifiedName) {
        this.name = Objects.requireNonNull(name, "Column name cannot be null");
        this.qualifiedName = qualifiedName;
    }

    public String getName() {
        return name;
    }

    public String getQualifiedName() {
        return qualifiedName != null ? qualifiedName : name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnInfo that = (ColumnInfo) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "name='" + name + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                '}';
    }
}

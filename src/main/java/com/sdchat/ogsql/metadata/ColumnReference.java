package com.sdchat.ogsql.metadata;

import java.util.Objects;

/**
 * Represents a reference to a database column in a SQL query.
 */
public class ColumnReference {
    
    private final String columnName;
    private final String tableName;
    private final String alias;
    
    /**
     * Creates a new column reference.
     * 
     * @param columnName The name of the column
     * @param tableName The name of the table (can be null)
     */
    public ColumnReference(String columnName, String tableName) {
        this(columnName, tableName, null);
    }
    
    /**
     * Creates a new column reference with alias.
     * 
     * @param columnName The name of the column
     * @param tableName The name of the table (can be null)
     * @param alias The alias for the column (can be null)
     */
    public ColumnReference(String columnName, String tableName, String alias) {
        if (columnName == null || columnName.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be null or empty");
        }
        this.columnName = columnName.trim();
        this.tableName = tableName != null ? tableName.trim() : null;
        this.alias = alias != null ? alias.trim() : null;
    }
    
    /**
     * Gets the column name.
     * 
     * @return The column name
     */
    public String getColumnName() {
        return columnName;
    }
    
    /**
     * Gets the table name.
     * 
     * @return The table name, or null if not specified
     */
    public String getTableName() {
        return tableName;
    }
    
    /**
     * Gets the column alias.
     * 
     * @return The alias, or null if not specified
     */
    public String getAlias() {
        return alias;
    }
    
    /**
     * Checks if this reference has a table qualifier.
     * 
     * @return true if table name is specified
     */
    public boolean hasTableQualifier() {
        return tableName != null && !tableName.isEmpty();
    }
    
    /**
     * Checks if this reference has an alias.
     * 
     * @return true if alias is specified
     */
    public boolean hasAlias() {
        return alias != null && !alias.isEmpty();
    }
    
    /**
     * Gets the qualified column name (table.column or just column).
     * 
     * @return The qualified name
     */
    public String getQualifiedName() {
        if (hasTableQualifier()) {
            return tableName + "." + columnName;
        }
        return columnName;
    }
    
    /**
     * Gets the display name (alias if present, otherwise column name).
     * 
     * @return The display name
     */
    public String getDisplayName() {
        if (hasAlias()) {
            return alias;
        }
        return columnName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ColumnReference that = (ColumnReference) obj;
        return Objects.equals(columnName, that.columnName) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(alias, that.alias);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(columnName, tableName, alias);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (hasTableQualifier()) {
            sb.append(tableName).append(".");
        }
        sb.append(columnName);
        if (hasAlias()) {
            sb.append(" AS ").append(alias);
        }
        return sb.toString();
    }
}
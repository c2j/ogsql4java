package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a CREATE FOREIGN TABLE statement for external data source mapping.
 * 
 * Foreign tables allow access to data stored in external systems through
 * foreign data wrappers and foreign servers.
 */
public class ExternalTable implements SQLStatement {

    private String tableName;
    private String serverName;
    private Map<String, String> serverOptions;
    private List<Column> columns = new ArrayList<>();
    private Map<String, String> tableOptions;

    /**
     * Creates a new external table definition.
     * 
     * @param tableName The name of the foreign table
     * @param serverName The name of the foreign server
     */
    public ExternalTable(String tableName, String serverName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        if (serverName == null || serverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Server name cannot be null or empty");
        }
        this.tableName = tableName.trim();
        this.serverName = serverName.trim();
        this.serverOptions = new HashMap<>();
        this.tableOptions = new HashMap<>();
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.CREATE_FOREIGN_TABLE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Gets the table name.
     * 
     * @return The table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the table name.
     * 
     * @param tableName The table name
     */
    public void setTableName(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        this.tableName = tableName.trim();
    }

    /**
     * Gets the server name.
     * 
     * @return The server name
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Sets the server name.
     * 
     * @param serverName The server name
     */
    public void setServerName(String serverName) {
        if (serverName == null || serverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Server name cannot be null or empty");
        }
        this.serverName = serverName.trim();
    }

    /**
     * Gets the server options (connection parameters).
     * 
     * @return The server options map
     */
    public Map<String, String> getServerOptions() {
        return serverOptions;
    }

    /**
     * Sets the server options.
     * 
     * @param serverOptions The server options map
     */
    public void setServerOptions(Map<String, String> serverOptions) {
        if (serverOptions == null) {
            throw new IllegalArgumentException("Server options cannot be null");
        }
        this.serverOptions = new HashMap<>(serverOptions);
    }

    /**
     * Adds a server option.
     * 
     * @param key The option key
     * @param value The option value
     */
    public void addServerOption(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Option key cannot be null or empty");
        }
        this.serverOptions.put(key.trim(), value);
    }

    /**
     * Gets the table options.
     * 
     * @return The table options map
     */
    public Map<String, String> getTableOptions() {
        return tableOptions;
    }

    /**
     * Sets the table options.
     * 
     * @param tableOptions The table options map
     */
    public void setTableOptions(Map<String, String> tableOptions) {
        if (tableOptions == null) {
            throw new IllegalArgumentException("Table options cannot be null");
        }
        this.tableOptions = new HashMap<>(tableOptions);
    }

    /**
     * Adds a table option.
     * 
     * @param key The option key
     * @param value The option value
     */
    public void addTableOption(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Option key cannot be null or empty");
        }
        this.tableOptions.put(key.trim(), value);
    }

    /**
     * Gets the columns.
     * 
     * @return The list of columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Sets the columns.
     * 
     * @param columns The list of columns
     */
    public void setColumns(List<Column> columns) {
        if (columns == null) {
            throw new IllegalArgumentException("Columns cannot be null");
        }
        this.columns = new ArrayList<>(columns);
    }

    /**
     * Adds a column.
     * 
     * @param column The column to add
     */
    public void addColumn(Column column) {
        if (column == null) {
            throw new IllegalArgumentException("Column cannot be null");
        }
        this.columns.add(column);
    }

    /**
     * Checks if server options are specified.
     * 
     * @return true if server options exist
     */
    public boolean hasServerOptions() {
        return !serverOptions.isEmpty();
    }

    /**
     * Checks if table options are specified.
     * 
     * @return true if table options exist
     */
    public boolean hasTableOptions() {
        return !tableOptions.isEmpty();
    }

    /**
     * Checks if columns are specified.
     * 
     * @return true if columns exist
     */
    public boolean hasColumns() {
        return !columns.isEmpty();
    }

    /**
     * Gets the number of columns.
     * 
     * @return The column count
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Gets the number of server options.
     * 
     * @return The server option count
     */
    public int getServerOptionCount() {
        return serverOptions.size();
    }

    /**
     * Gets the number of table options.
     * 
     * @return The table option count
     */
    public int getTableOptionCount() {
        return tableOptions.size();
    }

    /**
     * Validates the external table configuration.
     * 
     * @throws IllegalStateException if the configuration is invalid
     */
    public void validate() {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalStateException("Table name cannot be null or empty");
        }
        if (serverName == null || serverName.trim().isEmpty()) {
            throw new IllegalStateException("Server name cannot be null or empty");
        }
        // At least one column is typically required, but some foreign data wrappers may support columnless tables
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExternalTable{tableName='").append(tableName).append('\'');
        sb.append(", serverName='").append(serverName).append('\'');
        if (hasServerOptions()) {
            sb.append(", serverOptions=").append(serverOptions);
        }
        if (hasTableOptions()) {
            sb.append(", tableOptions=").append(tableOptions);
        }
        sb.append(", columns=").append(columns.size()).append(" columns");
        sb.append('}');
        return sb.toString();
    }
}
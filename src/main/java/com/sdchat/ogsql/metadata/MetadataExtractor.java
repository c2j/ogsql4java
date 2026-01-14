package com.sdchat.ogsql.metadata;

import com.sdchat.ogsql.ast.*;
import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.*;

/**
 * Extracts structured metadata from parsed SQL statements.
 * Provides methods to extract tables, columns, functions, and WHERE conditions.
 * 
 * Note: This implementation works with the current minimal AST structure.
 * It will be enhanced as the AST implementation becomes more complete.
 */
public class MetadataExtractor implements ASTVisitor<Void> {

    private Set<String> tables = new HashSet<>();
    private Set<ColumnReference> columns = new HashSet<>();
    private Set<FunctionCall> functions = new HashSet<>();
    private Set<Condition> whereConditions = new HashSet<>();

    /**
     * Creates a new metadata extractor.
     */
    public MetadataExtractor() {
    }

    /**
     * Extracts metadata from a SQL statement.
     * 
     * @param statement The SQL statement to analyze
     * @return This extractor instance for method chaining
     */
    public MetadataExtractor extract(SQLStatement statement) {
        reset();
        if (statement != null) {
            statement.accept(this);
        }
        return this;
    }

    /**
     * Resets all collected metadata.
     */
    private void reset() {
        tables.clear();
        columns.clear();
        functions.clear();
        whereConditions.clear();
    }

    /**
     * Gets the extracted table names.
     * 
     * @return Set of table names
     */
    public Set<String> getTables() {
        return new HashSet<>(tables);
    }

    /**
     * Gets the extracted column references.
     * 
     * @return Set of column references
     */
    public Set<ColumnReference> getColumns() {
        return new HashSet<>(columns);
    }

    /**
     * Gets the extracted function calls.
     * 
     * @return Set of function calls
     */
    public Set<FunctionCall> getFunctions() {
        return new HashSet<>(functions);
    }

    /**
     * Gets the extracted WHERE conditions.
     * 
     * @return Set of WHERE conditions
     */
    public Set<Condition> getWhereConditions() {
        return new HashSet<>(whereConditions);
    }

    @Override
    public Void visit(SelectQuery query) {
        if (query == null) return null;

        // Extract FROM clause tables
        if (query.getFromClause() != null) {
            extractFromClause(query.getFromClause());
        }

        return null;
    }

    @Override
    public Void visit(CreateStatement statement) {
        if (statement == null) return null;

        // Extract table name
        if (statement.getObjectName() != null) {
            tables.add(statement.getObjectName());
        }

        // Extract column definitions
        if (statement.getColumns() != null) {
            for (Column column : statement.getColumns()) {
                extractColumn(column);
            }
        }

        return null;
    }

    @Override
    public Void visit(InsertStatement statement) {
        if (statement == null) return null;

        // Current InsertStatement implementation is minimal
        // This would need to be enhanced when the AST is more complete

        return null;
    }

    @Override
    public Void visit(UpdateStatement statement) {
        if (statement == null) return null;

        // Current UpdateStatement implementation is minimal
        // This would need to be enhanced when the AST is more complete

        return null;
    }

    @Override
    public Void visit(DeleteStatement statement) {
        if (statement == null) return null;

        // Current DeleteStatement implementation is minimal
        // This would need to be enhanced when the AST is more complete

        return null;
    }

    @Override
    public Void visit(AlterStatement statement) {
        // ALTER statements can reference tables but implementation depends on specific ALTER type
        return null;
    }

    @Override
    public Void visit(DropStatement statement) {
        if (statement == null) return null;

        // Current DropStatement implementation is minimal
        // This would need to be enhanced when the AST is more complete

        return null;
    }

    @Override
    public Void visit(ExternalTable externalTable) {
        if (externalTable == null) return null;

        // Extract foreign table name
        if (externalTable.getTableName() != null) {
            tables.add(externalTable.getTableName());
        }

        // Extract column definitions
        if (externalTable.getColumns() != null) {
            for (Column column : externalTable.getColumns()) {
                extractColumn(column);
            }
        }

        return null;
    }

    @Override
    public Void visit(PartitioningInformation partitioning) {
        // Partitioning information doesn't directly reference additional tables/columns
        return null;
    }

    @Override
    public Void visit(PartitionDefinition partition) {
        // Partition definitions don't directly reference additional tables/columns
        return null;
    }

    /**
     * Extracts tables from a FROM clause string.
     * 
     * @param fromClause The FROM clause string
     */
    private void extractFromClause(String fromClause) {
        if (fromClause == null || fromClause.trim().isEmpty()) {
            return;
        }

        // Simple table extraction - split by commas and handle aliases
        String[] tableParts = fromClause.split(",");
        for (String tablePart : tableParts) {
            tablePart = tablePart.trim();
            if (!tablePart.isEmpty()) {
                // Handle table aliases (table_name alias)
                String[] parts = tablePart.split("\\s+");
                if (parts.length > 0) {
                    tables.add(parts[0]);
                }
            }
        }
    }

    /**
     * Extracts metadata from a column definition.
     * 
     * @param column The column to analyze
     */
    private void extractColumn(Column column) {
        if (column == null) return;

        // Extract column reference
        if (column.getName() != null) {
            columns.add(new ColumnReference(column.getName(), null)); // Table name will be set by context
        }
    }

    /**
     * Creates a summary of extracted metadata.
     * 
     * @return String summary of metadata
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Metadata Summary:\n");
        sb.append("  Tables: ").append(tables.size()).append(" (").append(tables).append(")\n");
        sb.append("  Columns: ").append(columns.size()).append("\n");
        sb.append("  Functions: ").append(functions.size()).append(" (").append(functions).append(")\n");
        sb.append("  WHERE Conditions: ").append(whereConditions.size()).append("\n");
        return sb.toString();
    }
}
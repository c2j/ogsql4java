package com.sdchat.ogsql.metadata;

import com.sdchat.ogsql.ast.*;
import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.*;

/**
 * Extracts structured metadata from parsed SQL statements.
 * Provides methods to extract tables, columns, functions, and WHERE conditions.
 * 
 * Note: This implementation works with the current minimal AST structure.
 * It will be enhanced as AST implementation becomes more complete.
 */
public class MetadataExtractor implements ASTVisitor<Void> {

    private Set<String> tables = new HashSet<>();
    private Set<String> procedures = new HashSet<>();
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
        procedures.clear();
        columns.clear();
        functions.clear();
        whereConditions.clear();
    }

    /**
     * Gets extracted table names.
     * 
     * @return Set of table names
     */
    public Set<String> getTables() {
        return new HashSet<>(tables);
    }

    /**
     * Gets extracted procedure names.
     * 
     * @return Set of procedure names
     */
    public Set<String> getProcedures() {
        return new HashSet<>(procedures);
    }

    /**
     * Gets extracted function calls.
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
    public Void visitSelectQuery(SelectQuery query) {
        if (query == null) return null;

        // P2 optimization: Structured dataSources available
        if (query.getDataSources() != null && !query.getDataSources().isEmpty()) {
            for (DataSource ds : query.getDataSources()) {
                if (ds.getName() != null && !ds.getName().isEmpty()) {
                    tables.add(ds.getName());
                }
            }
        }

        // Fallback to string-based extraction when dataSources is empty
        if (tables.isEmpty() && query.getFromClause() != null) {
            extractFromClause(query.getFromClause());
        }

        return null;
    }

    @Override
    public Void visitCreateStatement(CreateStatement statement) {
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
    public Void visitCreateProcedureStmt(CreateProcedureStmt statement) {
        if (statement == null) return null;

        // Extract procedure name
        if (statement.getProcedureName() != null) {
            procedures.add(statement.getProcedureName());
        }

        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatement statement) {
        if (statement == null) return null;

        if (statement.getTableName() != null) {
            tables.add(statement.getTableName());
        }

        return null;
    }

    @Override
    public Void visitUpdateStatement(UpdateStatement statement) {
        if (statement == null) return null;

        if (statement.getTableName() != null) {
            tables.add(statement.getTableName());
        }

        return null;
    }

    @Override
    public Void visitDeleteStatement(DeleteStatement statement) {
        if (statement == null) return null;

        if (statement.getTableName() != null) {
            tables.add(statement.getTableName());
        }

        return null;
    }

    @Override
    public Void visitAlterStatement(AlterStatement statement) {
        // ALTER statements can reference tables but implementation depends on specific ALTER type
        return null;
    }

    @Override
    public Void visitAlterProcedureStmt(AlterProcedureStmt statement) {
        if (statement == null) return null;

        // Extract procedure name
        if (statement.getProcedureName() != null) {
            procedures.add(statement.getProcedureName());
        }

        return null;
    }

    @Override
    public Void visitDropStatement(DropStatement statement) {
        if (statement == null) return null;

        if (statement.getObjectName() != null) {
            tables.add(statement.getObjectName());
        }

        return null;
    }

    @Override
    public Void visitCallFuncStmt(CallFuncStmt statement) {
        if (statement == null) return null;

        // Extract procedure name
        if (statement.getProcedureName() != null) {
            procedures.add(statement.getProcedureName());
        }

        return null;
    }

    @Override
    public Void visitExternalTable(ExternalTable externalTable) {
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
    public Void visitPartitioningInformation(PartitioningInformation partitioning) {
        // Partitioning information doesn't directly reference additional tables/columns
        return null;
    }

    @Override
    public Void visitPartitionDefinition(PartitionDefinition partition) {
        // Partition definitions don't directly reference additional tables/columns
        return null;
    }

    /**
     * Extracts tables from a FROM clause string.
     * Handles nested parentheses in JOIN conditions and subqueries.
     *
     * @param fromClause The FROM clause string
     */
    private void extractFromClause(String fromClause) {
        if (fromClause == null || fromClause.trim().isEmpty()) {
            return;
        }

        // Process: FROM clause while tracking parenthesis nesting levels
        // Extract table names from subqueries before skipping them
        StringBuilder result = new StringBuilder();
        int parenLevel = 0;
        boolean inOnOrUsingClause = false;

        for (int i = 0; i < fromClause.length(); i++) {
            char c = fromClause.charAt(i);

            // Track parenthesis nesting for subqueries
            if (c == '(') {
                parenLevel++;
                // Only treat as subquery if it contains SELECT (not JOIN conditions or function calls)
                if (isSubquery(fromClause, i)) {
                    // Extract table names from subquery before skipping it
                    extractTablesFromSubquery(fromClause, i);
                    // Skip entire subquery (until matching closing parenthesis at original level)
                    int startLevel = parenLevel;
                    int nestedLevel = 1;
                    i++;
                    while (i < fromClause.length() && nestedLevel > 0) {
                        if (fromClause.charAt(i) == '(') nestedLevel++;
                        else if (fromClause.charAt(i) == ')') nestedLevel--;
                        i++;
                    }
                    i--; // Back up to closing parenthesis at original level
                }
                continue;
            } else if (c == ')') {
                parenLevel--;
            }

            // Only process keywords when we're not inside parentheses (subqueries)
            if (parenLevel == 0) {
                // Check for JOIN keywords (case-insensitive)
                if (i <= fromClause.length() - 5) {
                    String fiveChars = fromClause.substring(i, Math.min(i + 5, fromClause.length()));
                    if (fiveChars.equalsIgnoreCase("JOIN ")) {
                        inOnOrUsingClause = false;
                        result.append(", ");
                        i += 4; // Skip "JOIN"
                        continue;
                    }
                }

                // Check for LEFT, RIGHT, FULL, INNER, CROSS keywords
                if (i <= fromClause.length() - 6) {
                    String sixChars = fromClause.substring(i, Math.min(i + 6, fromClause.length()));
                    if (sixChars.equalsIgnoreCase("LEFT ") ||
                        sixChars.equalsIgnoreCase("RIGHT ") ||
                        sixChars.equalsIgnoreCase("FULL ")) {
                        inOnOrUsingClause = false;
                        result.append(sixChars);
                        i += 5; // Skip keyword and space
                        continue;
                    }
                }

                // Check for OUTER
                if (i <= fromClause.length() - 6 && fromClause.substring(i, Math.min(i + 6, fromClause.length())).equalsIgnoreCase("OUTER")) {
                    result.append("OUTER ");
                    i += 5;
                    continue;
                }

                // Check for INNER
                if (i <= fromClause.length() - 6 && fromClause.substring(i, Math.min(i + 6, fromClause.length())).equalsIgnoreCase("INNER")) {
                    result.append("INNER ");
                    i += 5;
                    continue;
                }

                // Check for CROSS
                if (i <= fromClause.length() - 6 && fromClause.substring(i, Math.min(i + 6, fromClause.length())).equalsIgnoreCase("CROSS")) {
                    result.append("CROSS ");
                    i += 5;
                    continue;
                }

                // Check for AS keyword
                if (i + 3 <= fromClause.length() && fromClause.substring(i, i + 3).equalsIgnoreCase("AS ")) {
                    inOnOrUsingClause = true;
                    i += 3;
                    continue;
                }

                // Check for ON keyword
                if (i + 3 <= fromClause.length() && fromClause.substring(i, i + 3).equalsIgnoreCase("ON ")) {
                    inOnOrUsingClause = true;
                    i += 2;
                    continue;
                }

                // Check for USING keyword
                if (i + 6 <= fromClause.length() && fromClause.substring(i, i + 6).equalsIgnoreCase("USING ")) {
                    inOnOrUsingClause = true;
                    i += 5;
                    continue;
                }
            }

            // Skip characters in ON/USING clauses
            if (inOnOrUsingClause) {
                // Exit ON/USING clause when we reach comma at parenLevel 0 or another keyword
                if (parenLevel == 0 && c == ',') {
                    inOnOrUsingClause = false;
                }
                continue;
            }

            // Add character to result (table name, not subquery content)
            if (parenLevel == 0) {
                result.append(c);
            }
        }

        // Extract table names from the normalized result
        String normalized = result.toString();
        String[] tokens = normalized.split(",");
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty()) {
                // Extract first word (table name) before any alias
                // This handles schema-qualified names (e.g., schema.table)
                String[] parts = token.split("\\s+");
                if (parts.length > 0 && !parts[0].isEmpty()) {
                    tables.add(parts[0]);
                }
            }
        }
    }

    /**
     * Checks if parentheses at the given index represent a subquery.
     * A subquery is identified by the presence of a SELECT keyword.
     *
     * @param fromClause The full FROM clause string
     * @param startIndex The index where opening parenthesis was found
     * @return true if the parentheses contain a subquery, false otherwise
     */
    private boolean isSubquery(String fromClause, int startIndex) {
        // Check if there's enough content after the opening parenthesis
        if (startIndex + 1 >= fromClause.length()) {
            return false;
        }

        // Look for SELECT keyword after the opening parenthesis (with optional whitespace)
        int contentStart = startIndex + 1;
        int maxLength = Math.min(contentStart + 20, fromClause.length());
        String content = fromClause.substring(contentStart, maxLength).trim().toUpperCase();

        // Check if it starts with SELECT
        return content.startsWith("SELECT");
    }

    /**
     * Extracts table names from a subquery (text inside parentheses).
     * Handles multiple tables, JOINs, and schema-qualified names.
     *
     * @param fromClause The full FROM clause string
     * @param startIndex The index where opening parenthesis was found
     */
    private void extractTablesFromSubquery(String fromClause, int startIndex) {
        // Find the closing parenthesis at the same level
        int parenLevel = 1;
        int endIndex = startIndex + 1;

        while (endIndex < fromClause.length() && parenLevel > 0) {
            if (fromClause.charAt(endIndex) == '(') parenLevel++;
            else if (fromClause.charAt(endIndex) == ')') parenLevel--;
            endIndex++;
        }

        // Extract the subquery content
        String subqueryContent = fromClause.substring(startIndex + 1, endIndex);

        // Look for "FROM" keyword in subquery to extract table names
        String upperContent = subqueryContent.toUpperCase();
        int fromIndex = upperContent.indexOf(" FROM ");

        if (fromIndex > 0) {
            // Extract the FROM clause portion after "FROM"
            int fromClauseStart = fromIndex + 6;
            String subFromClause = subqueryContent.substring(fromClauseStart);

            // Extract table names from the subquery's FROM clause
            extractTableNamesFromClause(subFromClause);
        }
    }

    /**
     * Extracts table names from a FROM clause string.
     * Handles comma-separated tables, JOINs, and schema-qualified names.
     *
     * @param fromClause The FROM clause string
     */
    private void extractTableNamesFromClause(String fromClause) {
        if (fromClause == null || fromClause.trim().isEmpty()) {
            return;
        }

        String normalized = fromClause.trim();
        StringBuilder result = new StringBuilder();
        int parenLevel = 0;

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);

            if (c == '(') {
                parenLevel++;
                // Skip subqueries and function calls
                int startLevel = parenLevel;
                int nestedLevel = 1;
                i++;
                while (i < normalized.length() && nestedLevel > 0) {
                    if (normalized.charAt(i) == '(') nestedLevel++;
                    else if (normalized.charAt(i) == ')') nestedLevel--;
                    i++;
                }
                i--;
                continue;
            } else if (c == ')') {
                parenLevel--;
                continue;
            }

            // Skip JOIN keywords and related clauses
            if (i <= normalized.length() - 6) {
                String sixChars = normalized.substring(i, Math.min(i + 6, normalized.length()));
                if (sixChars.equalsIgnoreCase("LEFT ") ||
                    sixChars.equalsIgnoreCase("RIGHT ") ||
                    sixChars.equalsIgnoreCase("FULL ")) {
                    i += 5;
                    continue;
                }
            }

            if (i <= normalized.length() - 6 && normalized.substring(i, Math.min(i + 6, normalized.length())).equalsIgnoreCase("OUTER")) {
                i += 5;
                continue;
            }

            if (i <= normalized.length() - 6 && normalized.substring(i, Math.min(i + 6, normalized.length())).equalsIgnoreCase("INNER")) {
                i += 5;
                continue;
            }

            if (i <= normalized.length() - 6 && normalized.substring(i, Math.min(i + 6, normalized.length())).equalsIgnoreCase("CROSS")) {
                i += 5;
                continue;
            }

            if (i <= normalized.length() - 5 && normalized.substring(i, Math.min(i + 5, normalized.length())).equalsIgnoreCase("JOIN ")) {
                i += 4;
                continue;
            }

            // Skip AS keyword
            if (i + 3 <= normalized.length() && normalized.substring(i, i + 3).equalsIgnoreCase("AS ")) {
                i += 3;
                continue;
            }

            // Skip ON and USING clauses
            if (i + 3 <= normalized.length() && normalized.substring(i, i + 3).equalsIgnoreCase("ON ")) {
                // Skip until next comma or end
                i += 2;
                while (i < normalized.length() && normalized.charAt(i) != ',') {
                    i++;
                }
                continue;
            }

            if (i + 6 <= normalized.length() && normalized.substring(i, i + 6).equalsIgnoreCase("USING ")) {
                // Skip until next comma or end
                i += 5;
                while (i < normalized.length() && normalized.charAt(i) != ',') {
                    i++;
                }
                continue;
            }

            // Keep characters for table name extraction
            result.append(c);
        }

        // Split by commas and extract table names
        String[] tokens = result.toString().split(",");
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty()) {
                // Extract first word (table name) before any alias
                String[] parts = token.split("\\s+");
                if (parts.length > 0 && !parts[0].isEmpty()) {
                    // Handle schema-qualified names (schema.table)
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

    @Override
    public Void visitExplainStatement(ExplainStatement statement) {
        return null;
    }

    @Override
    public Void visitShowStatement(ShowStatement statement) {
        return null;
    }

    @Override
    public Void visitBeginStatement(BeginStatement statement) {
        return null;
    }

    @Override
    public Void visitCommitStatement(CommitStatement statement) {
        return null;
    }

    @Override
    public Void visitRollbackStatement(RollbackStatement statement) {
        return null;
    }

    @Override
    public Void visitAnalyzeStatement(AnalyzeStatement statement) {
        return null;
    }

    @Override
    public Void visitSetStatement(SetStatement statement) {
        return null;
    }

    @Override
    public Void visitCreateSchemaStatement(CreateSchemaStatement statement) {
        return null;
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
        sb.append("  Procedures: ").append(procedures.size()).append(" (").append(procedures).append(")\n");
        sb.append("  Columns: ").append(columns.size()).append("\n");
        sb.append("  Functions: ").append(functions.size()).append(" (").append(functions).append(")\n");
        sb.append("  WHERE Conditions: ").append(whereConditions.size()).append("\n");
        return sb.toString();
    }
}

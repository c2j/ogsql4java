package com.sdchat.ogsql.graph;

import com.sdchat.ogsql.ast.*;
import com.sdchat.ogsql.visitor.ASTVisitor;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts table relationships from SQL AST.
 * Implements Visitor pattern to traverse the AST and build a relationship graph.
 */
public class TableRelationshipExtractor implements ASTVisitor<TableRelationshipGraph> {
    private static final Logger LOGGER = Logger.getLogger(TableRelationshipExtractor.class.getName());
    
    private final TableRelationshipGraph graph;
    private final Map<String, TableNode> aliasToTable;
    private final List<String> warnings;
    
    // Pattern to parse join conditions like "users.id = orders.user_id"
    private static final Pattern JOIN_CONDITION_PATTERN = 
        Pattern.compile("(\\w+)\\.(\\w+)\\s*=\\s*(\\w+)\\.(\\w+)");

    public TableRelationshipExtractor() {
        this.graph = new TableRelationshipGraph();
        this.aliasToTable = new HashMap<>();
        this.warnings = new ArrayList<>();
    }

    @Override
    public TableRelationshipGraph visitSelectQuery(SelectQuery query) {
        if (query == null) {
            return graph;
        }

        LOGGER.fine("Processing SELECT query");
        
        // Process data sources (tables and joins)
        if (query.hasDataSources()) {
            processDataSources(query.getDataSources());
        }
        
        return graph;
    }

    /**
     * Process data sources to extract tables and relationships.
     */
    private void processDataSources(List<DataSource> dataSources) {
        TableNode previousTable = null;
        
        for (DataSource dataSource : dataSources) {
            TableNode currentTable = extractTableNode(dataSource);
            
            if (currentTable != null) {
                graph.addTable(currentTable);
                
                // Register alias mapping
                if (dataSource.hasAlias()) {
                    aliasToTable.put(dataSource.getAlias(), currentTable);
                }
                aliasToTable.put(dataSource.getName(), currentTable);
                
                // If this is a JOIN, create relationship with previous table
                if (dataSource.hasJoin() && previousTable != null) {
                    createRelationship(previousTable, currentTable, dataSource);
                }
                
                previousTable = currentTable;
            }
        }
    }

    /**
     * Extract TableNode from DataSource.
     */
    private TableNode extractTableNode(DataSource dataSource) {
        if (dataSource == null || dataSource.getName() == null) {
            return null;
        }
        
        String tableName = dataSource.getName();
        String alias = dataSource.getAlias();
        
        return new TableNode(tableName, alias);
    }

    /**
     * Create relationship edge between two tables.
     */
    private void createRelationship(TableNode source, TableNode target, DataSource joinSource) {
        JoinType joinType = JoinType.fromSql(joinSource.getJoinType());
        RelationshipEdge edge = new RelationshipEdge(source, target, joinType);
        
        // Parse join conditions
        String joinCondition = joinSource.getJoinCondition();
        if (joinCondition != null && !joinCondition.isEmpty()) {
            parseJoinConditions(edge, joinCondition);
        }
        
        graph.addRelationship(edge);
        LOGGER.fine(String.format("Created relationship: %s -[%s]-> %s", 
            source.getName(), joinType.getDisplayName(), target.getName()));
    }

    /**
     * Parse join condition string and extract column pairs.
     */
    private void parseJoinConditions(RelationshipEdge edge, String condition) {
        Matcher matcher = JOIN_CONDITION_PATTERN.matcher(condition);
        
        while (matcher.find()) {
            String leftTable = matcher.group(1);
            String leftColumn = matcher.group(2);
            String rightTable = matcher.group(3);
            String rightColumn = matcher.group(4);
            
            String leftQualified = leftTable + "." + leftColumn;
            String rightQualified = rightTable + "." + rightColumn;
            
            edge.addCondition(leftQualified, rightQualified);
            
            // Also add column info to respective tables
            addColumnToTable(leftTable, leftColumn);
            addColumnToTable(rightTable, rightColumn);
        }
        
        // If no conditions were parsed, log a warning
        if (edge.getConditions().isEmpty()) {
            warnings.add("Could not parse join condition: " + condition);
            LOGGER.warning("Could not parse join condition: " + condition);
        }
    }

    /**
     * Add column information to a table.
     */
    private void addColumnToTable(String tableIdentifier, String columnName) {
        TableNode table = aliasToTable.get(tableIdentifier);
        if (table != null) {
            String qualifiedName = tableIdentifier + "." + columnName;
            ColumnInfo column = new ColumnInfo(columnName, qualifiedName);
            table.addColumn(column);
        }
    }

    /**
     * Extract columns from SELECT clause (if available in the query).
     * This is a placeholder for future enhancement.
     */
    public void extractSelectColumns(SelectQuery query, List<String> columns) {
        if (columns == null || columns.isEmpty()) {
            return;
        }
        
        for (String column : columns) {
            extractColumnReference(column);
        }
    }

    /**
     * Extract column reference and associate with table.
     */
    private void extractColumnReference(String columnRef) {
        if (columnRef == null || columnRef.equals("*")) {
            return;
        }
        
        // Handle qualified column references like "users.id"
        if (columnRef.contains(".")) {
            String[] parts = columnRef.split("\\.", 2);
            if (parts.length == 2) {
                String tableRef = parts[0];
                String columnName = parts[1];
                addColumnToTable(tableRef, columnName);
            }
        }
    }

    /**
     * Get the extracted graph.
     */
    public TableRelationshipGraph getGraph() {
        return graph;
    }

    /**
     * Get any warnings that occurred during extraction.
     */
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }

    /**
     * Check if any warnings occurred.
     */
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    // Unsupported statement types - return empty graph
    @Override
    public TableRelationshipGraph visitCreateStatement(CreateStatement statement) {
        LOGGER.fine("CREATE statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitCreateProcedureStmt(CreateProcedureStmt statement) {
        LOGGER.fine("CREATE PROCEDURE statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitInsertStatement(InsertStatement statement) {
        LOGGER.fine("INSERT statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitUpdateStatement(UpdateStatement statement) {
        LOGGER.fine("UPDATE statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitDeleteStatement(DeleteStatement statement) {
        LOGGER.fine("DELETE statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitAlterStatement(AlterStatement statement) {
        LOGGER.fine("ALTER statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitAlterProcedureStmt(AlterProcedureStmt statement) {
        LOGGER.fine("ALTER PROCEDURE statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitDropStatement(DropStatement statement) {
        LOGGER.fine("DROP statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitCallFuncStmt(CallFuncStmt statement) {
        LOGGER.fine("CALL statement not supported for relationship extraction");
        return graph;
    }

    @Override
    public TableRelationshipGraph visitPartitioningInformation(PartitioningInformation partitioning) {
        return graph;
    }

    @Override
    public TableRelationshipGraph visitPartitionDefinition(PartitionDefinition partition) {
        return graph;
    }

    @Override
    public TableRelationshipGraph visitExternalTable(ExternalTable externalTable) {
        return graph;
    }
}

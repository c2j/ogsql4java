package com.sdchat.ogsql.graph.formatter;

import com.sdchat.ogsql.graph.*;

import java.util.*;

/**
 * Formats table relationship graph to JSON format.
 * JSON output is suitable for programmatic consumption and web visualization.
 */
public class JsonFormatter implements GraphFormatter<String> {
    
    private static final String INDENT = "  ";
    
    @Override
    public String format(TableRelationshipGraph graph) {
        if (graph == null || graph.isEmpty()) {
            return "{\"nodes\": [], \"edges\": []}";
        }
        
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Nodes array
        json.append(INDENT).append("\"nodes\": [\n");
        Set<TableNode> tables = graph.getTables();
        boolean firstNode = true;
        for (TableNode table : tables) {
            if (!firstNode) {
                json.append(",\n");
            }
            json.append(formatTableNode(table, 2));
            firstNode = false;
        }
        json.append("\n").append(INDENT).append("],\n");
        
        // Edges array
        json.append(INDENT).append("\"edges\": [\n");
        Set<RelationshipEdge> relationships = graph.getRelationships();
        boolean firstEdge = true;
        for (RelationshipEdge edge : relationships) {
            if (!firstEdge) {
                json.append(",\n");
            }
            json.append(formatRelationshipEdge(edge, 2));
            firstEdge = false;
        }
        json.append("\n").append(INDENT).append("]\n");
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Format a table node as JSON object.
     */
    private String formatTableNode(TableNode table, int indentLevel) {
        String indent = repeat(INDENT, indentLevel);
        StringBuilder json = new StringBuilder();
        
        json.append(indent).append("{\n");
        
        // Name
        json.append(indent).append(INDENT).append("\"name\": \"").append(escapeJson(table.getName())).append("\"");
        
        // Alias
        if (table.hasAlias()) {
            json.append(",\n");
            json.append(indent).append(INDENT).append("\"alias\": \"").append(escapeJson(table.getAlias())).append("\"");
        }
        
        // Columns
        json.append(",\n");
        json.append(indent).append(INDENT).append("\"columns\": [");
        List<ColumnInfo> columns = table.getColumns();
        if (!columns.isEmpty()) {
            json.append("\n");
            boolean first = true;
            for (ColumnInfo column : columns) {
                if (!first) {
                    json.append(",\n");
                }
                json.append(indent).append(INDENT).append(INDENT).append("\"").append(escapeJson(column.getName())).append("\"");
                first = false;
            }
            json.append("\n").append(indent).append(INDENT);
        }
        json.append("]");
        
        json.append("\n").append(indent).append("}");
        return json.toString();
    }
    
    /**
     * Format a relationship edge as JSON object.
     */
    private String formatRelationshipEdge(RelationshipEdge edge, int indentLevel) {
        String indent = repeat(INDENT, indentLevel);
        StringBuilder json = new StringBuilder();
        
        json.append(indent).append("{\n");
        
        // Source
        String sourceId = edge.getSource().hasAlias() ? 
            edge.getSource().getAlias() : edge.getSource().getName();
        json.append(indent).append(INDENT).append("\"source\": \"").append(escapeJson(sourceId)).append("\"");
        
        // Target
        String targetId = edge.getTarget().hasAlias() ? 
            edge.getTarget().getAlias() : edge.getTarget().getName();
        json.append(",\n");
        json.append(indent).append(INDENT).append("\"target\": \"").append(escapeJson(targetId)).append("\"");
        
        // Type
        json.append(",\n");
        json.append(indent).append(INDENT).append("\"type\": \"").append(edge.getJoinType().name()).append("\"");
        
        // Join conditions
        json.append(",\n");
        json.append(indent).append(INDENT).append("\"joinConditions\": [");
        List<JoinCondition> conditions = edge.getConditions();
        if (!conditions.isEmpty()) {
            json.append("\n");
            boolean first = true;
            for (JoinCondition condition : conditions) {
                if (!first) {
                    json.append(",\n");
                }
                json.append(indent).append(INDENT).append(INDENT).append("{\n");
                json.append(indent).append(INDENT).append(INDENT).append(INDENT)
                    .append("\"leftColumn\": \"").append(escapeJson(condition.getLeftColumn())).append("\"");
                json.append(",\n");
                json.append(indent).append(INDENT).append(INDENT).append(INDENT)
                    .append("\"rightColumn\": \"").append(escapeJson(condition.getRightColumn())).append("\"");
                json.append("\n").append(indent).append(INDENT).append(INDENT).append("}");
                first = false;
            }
            json.append("\n").append(indent).append(INDENT);
        }
        json.append("]");
        
        json.append("\n").append(indent).append("}");
        return json.toString();
    }
    
    /**
     * Escape special characters in JSON strings.
     */
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * Repeat a string n times.
     */
    private String repeat(String str, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            result.append(str);
        }
        return result.toString();
    }
    
    @Override
    public String getFormatType() {
        return "application/json";
    }
    
    @Override
    public String getFileExtension() {
        return "json";
    }
}

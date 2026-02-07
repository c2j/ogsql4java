package com.sdchat.ogsql.graph.formatter;

import com.sdchat.ogsql.graph.*;

import java.util.Set;

/**
 * Formats table relationship graph to Graphviz DOT format.
 * DOT format can be rendered by Graphviz tools to produce visual diagrams.
 */
public class DotFormatter implements GraphFormatter<String> {
    
    private static final String INDENT = "  ";
    
    @Override
    public String format(TableRelationshipGraph graph) {
        if (graph == null || graph.isEmpty()) {
            return "digraph TableRelationships {\n}";
        }
        
        StringBuilder dot = new StringBuilder();
        dot.append("digraph TableRelationships {\n");
        dot.append(INDENT).append("rankdir=LR;\n");
        dot.append(INDENT).append("node [shape=record, style=filled, fillcolor=lightblue];\n");
        dot.append(INDENT).append("edge [color=gray];\n");
        dot.append("\n");
        
        // Generate table nodes
        Set<TableNode> tables = graph.getTables();
        for (TableNode table : tables) {
            dot.append(formatTableNode(table));
        }
        
        dot.append("\n");
        
        // Generate relationship edges
        Set<RelationshipEdge> relationships = graph.getRelationships();
        for (RelationshipEdge edge : relationships) {
            dot.append(formatRelationshipEdge(edge));
        }
        
        dot.append("}\n");
        return dot.toString();
    }
    
    /**
     * Format a single table node as DOT record.
     */
    private String formatTableNode(TableNode table) {
        StringBuilder node = new StringBuilder();
        node.append(INDENT);
        
        // Node ID (use alias if available, otherwise name)
        String nodeId = getNodeId(table);
        node.append("\"").append(nodeId).append("\"");
        
        // Node label as record
        node.append(" [label=\"");
        node.append(table.getName());
        
        // Add columns if present
        if (!table.getColumns().isEmpty()) {
            node.append(" | {");
            boolean first = true;
            for (ColumnInfo column : table.getColumns()) {
                if (!first) {
                    node.append(" | ");
                }
                node.append(column.getName());
                first = false;
            }
            node.append("}");
        }
        
        node.append("\"");
        
        // Add alias as tooltip if present
        if (table.hasAlias()) {
            node.append(", tooltip=\"Alias: ").append(table.getAlias()).append("\"");
        }
        
        node.append("];\n");
        return node.toString();
    }
    
    /**
     * Format a relationship edge as DOT edge.
     */
    private String formatRelationshipEdge(RelationshipEdge edge) {
        StringBuilder edgeStr = new StringBuilder();
        edgeStr.append(INDENT);
        
        // Source and target
        edgeStr.append("\"").append(getNodeId(edge.getSource())).append("\"");
        edgeStr.append(" -> ");
        edgeStr.append("\"").append(getNodeId(edge.getTarget())).append("\"");
        
        // Edge attributes
        edgeStr.append(" [");
        
        // Label with join type
        edgeStr.append("label=\"").append(edge.getJoinType().getDisplayName()).append("\"");
        
        // Add join conditions as tooltip
        if (!edge.getConditions().isEmpty()) {
            edgeStr.append(", tooltip=\"");
            boolean first = true;
            for (JoinCondition condition : edge.getConditions()) {
                if (!first) {
                    edgeStr.append("\\n");
                }
                edgeStr.append(condition.toString());
                first = false;
            }
            edgeStr.append("\"");
        }
        
        // Style based on join type
        switch (edge.getJoinType()) {
            case LEFT_JOIN:
                edgeStr.append(", style=dashed");
                break;
            case RIGHT_JOIN:
                edgeStr.append(", style=dashed");
                break;
            case FULL_JOIN:
                edgeStr.append(", style=dashed, dir=both");
                break;
            case CROSS_JOIN:
                edgeStr.append(", style=dotted");
                break;
            default:
                // INNER_JOIN - solid line (default)
                break;
        }
        
        edgeStr.append("];\n");
        return edgeStr.toString();
    }
    
    /**
     * Get node ID for a table (prefer alias if available).
     */
    private String getNodeId(TableNode table) {
        return table.hasAlias() ? table.getAlias() : table.getName();
    }
    
    @Override
    public String getFormatType() {
        return "text/vnd.graphviz";
    }
    
    @Override
    public String getFileExtension() {
        return "dot";
    }
}

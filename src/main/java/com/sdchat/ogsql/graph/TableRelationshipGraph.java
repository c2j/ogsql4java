package com.sdchat.ogsql.graph;

import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;

/**
 * Container for table relationship graph.
 * Uses JGraphT as the underlying graph implementation.
 */
public class TableRelationshipGraph {
    private final DirectedMultigraph<TableNode, RelationshipEdge> graph;
    private final Map<String, TableNode> tableByAlias;
    private final Map<String, TableNode> tableByName;

    public TableRelationshipGraph() {
        this.graph = new DirectedMultigraph<>(RelationshipEdge.class);
        this.tableByAlias = new HashMap<>();
        this.tableByName = new HashMap<>();
    }

    /**
     * Add a table node to the graph.
     */
    public void addTable(TableNode table) {
        Objects.requireNonNull(table, "Table cannot be null");
        
        if (!graph.containsVertex(table)) {
            graph.addVertex(table);
            tableByName.put(table.getName(), table);
            
            if (table.hasAlias()) {
                tableByAlias.put(table.getAlias(), table);
            }
        }
    }

    /**
     * Add a relationship edge between two tables.
     */
    public void addRelationship(RelationshipEdge edge) {
        Objects.requireNonNull(edge, "Edge cannot be null");
        
        addTable(edge.getSource());
        addTable(edge.getTarget());
        graph.addEdge(edge.getSource(), edge.getTarget(), edge);
    }

    /**
     * Get all table nodes in the graph.
     */
    public Set<TableNode> getTables() {
        return new HashSet<>(graph.vertexSet());
    }

    /**
     * Get all relationship edges in the graph.
     */
    public Set<RelationshipEdge> getRelationships() {
        return new HashSet<>(graph.edgeSet());
    }

    /**
     * Get table by name.
     */
    public Optional<TableNode> getTableByName(String name) {
        return Optional.ofNullable(tableByName.get(name));
    }

    /**
     * Get table by alias.
     */
    public Optional<TableNode> getTableByAlias(String alias) {
        return Optional.ofNullable(tableByAlias.get(alias));
    }

    /**
     * Get table by name or alias.
     */
    public Optional<TableNode> findTable(String identifier) {
        Optional<TableNode> byName = getTableByName(identifier);
        if (byName.isPresent()) {
            return byName;
        }
        return getTableByAlias(identifier);
    }

    /**
     * Get relationships for a specific table.
     */
    public Set<RelationshipEdge> getRelationshipsForTable(TableNode table) {
        if (!graph.containsVertex(table)) {
            return Collections.emptySet();
        }
        
        Set<RelationshipEdge> edges = new HashSet<>();
        edges.addAll(graph.outgoingEdgesOf(table));
        edges.addAll(graph.incomingEdgesOf(table));
        return edges;
    }

    /**
     * Get the number of tables (nodes).
     */
    public int getTableCount() {
        return graph.vertexSet().size();
    }

    /**
     * Get the number of relationships (edges).
     */
    public int getRelationshipCount() {
        return graph.edgeSet().size();
    }

    /**
     * Check if graph is empty.
     */
    public boolean isEmpty() {
        return graph.vertexSet().isEmpty();
    }

    /**
     * Clear the graph.
     */
    public void clear() {
        graph.removeAllVertices(new HashSet<>(graph.vertexSet()));
        tableByAlias.clear();
        tableByName.clear();
    }

    @Override
    public String toString() {
        return "TableRelationshipGraph{" +
                "tables=" + getTableCount() +
                ", relationships=" + getRelationshipCount() +
                '}';
    }
}

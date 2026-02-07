package com.sdchat.ogsql.graph.formatter;

import com.sdchat.ogsql.graph.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for graph formatters.
 */
public class GraphFormatterTest {
    
    private TableRelationshipGraph graph;
    private TableNode usersTable;
    private TableNode ordersTable;
    
    @BeforeEach
    void setUp() {
        graph = new TableRelationshipGraph();
        
        // Create tables
        usersTable = new TableNode("users", "u");
        usersTable.addColumn(new ColumnInfo("id", "users.id"));
        usersTable.addColumn(new ColumnInfo("name", "users.name"));
        
        ordersTable = new TableNode("orders", "o");
        ordersTable.addColumn(new ColumnInfo("id", "orders.id"));
        ordersTable.addColumn(new ColumnInfo("user_id", "orders.user_id"));
        ordersTable.addColumn(new ColumnInfo("amount", "orders.amount"));
        
        graph.addTable(usersTable);
        graph.addTable(ordersTable);
        
        // Create relationship
        RelationshipEdge edge = new RelationshipEdge(usersTable, ordersTable, JoinType.INNER_JOIN);
        edge.addCondition("users.id", "orders.user_id");
        graph.addRelationship(edge);
    }
    
    @Test
    void testDotFormatterNotNull() {
        DotFormatter formatter = new DotFormatter();
        String dot = formatter.format(graph);
        
        assertNotNull(dot);
        assertTrue(dot.contains("digraph TableRelationships"));
    }
    
    @Test
    void testDotFormatterContainsTables() {
        DotFormatter formatter = new DotFormatter();
        String dot = formatter.format(graph);
        
        assertTrue(dot.contains("users"));
        assertTrue(dot.contains("orders"));
    }
    
    @Test
    void testDotFormatterContainsJoin() {
        DotFormatter formatter = new DotFormatter();
        String dot = formatter.format(graph);
        
        assertTrue(dot.contains("INNER JOIN"));
        assertTrue(dot.contains("->"));
    }
    
    @Test
    void testDotFormatterEmptyGraph() {
        DotFormatter formatter = new DotFormatter();
        String dot = formatter.format(new TableRelationshipGraph());
        
        assertEquals("digraph TableRelationships {\n}", dot);
    }
    
    @Test
    void testJsonFormatterNotNull() {
        JsonFormatter formatter = new JsonFormatter();
        String json = formatter.format(graph);
        
        assertNotNull(json);
        assertTrue(json.contains("\"nodes\""));
        assertTrue(json.contains("\"edges\""));
    }
    
    @Test
    void testJsonFormatterContainsTables() {
        JsonFormatter formatter = new JsonFormatter();
        String json = formatter.format(graph);
        
        assertTrue(json.contains("\"name\": \"users\""));
        assertTrue(json.contains("\"name\": \"orders\""));
    }
    
    @Test
    void testJsonFormatterContainsAliases() {
        JsonFormatter formatter = new JsonFormatter();
        String json = formatter.format(graph);
        
        assertTrue(json.contains("\"alias\": \"u\""));
        assertTrue(json.contains("\"alias\": \"o\""));
    }
    
    @Test
    void testJsonFormatterContainsColumns() {
        JsonFormatter formatter = new JsonFormatter();
        String json = formatter.format(graph);
        
        assertTrue(json.contains("\"columns\""));
        assertTrue(json.contains("\"id\""));
        assertTrue(json.contains("\"name\""));
    }
    
    @Test
    void testJsonFormatterContainsJoinConditions() {
        JsonFormatter formatter = new JsonFormatter();
        String json = formatter.format(graph);
        
        assertTrue(json.contains("\"type\": \"INNER_JOIN\""));
        assertTrue(json.contains("\"joinConditions\""));
        assertTrue(json.contains("\"leftColumn\""));
        assertTrue(json.contains("\"rightColumn\""));
    }
    
    @Test
    void testJsonFormatterEmptyGraph() {
        JsonFormatter formatter = new JsonFormatter();
        String json = formatter.format(new TableRelationshipGraph());

        // The formatter produces formatted JSON with newlines and indentation
        assertNotNull(json);
        assertTrue(json.contains("\"nodes\""));
        assertTrue(json.contains("\"edges\""));
        assertTrue(json.contains("[]"));
    }
    
    @Test
    void testFormatterFactoryGetDotFormatter() {
        GraphFormatter<String> formatter = FormatterFactory.getDotFormatter();
        
        assertNotNull(formatter);
        assertTrue(formatter instanceof DotFormatter);
    }
    
    @Test
    void testFormatterFactoryGetJsonFormatter() {
        GraphFormatter<String> formatter = FormatterFactory.getJsonFormatter();
        
        assertNotNull(formatter);
        assertTrue(formatter instanceof JsonFormatter);
    }
    
    @Test
    void testFormatterFactoryByExtension() {
        GraphFormatter<String> dotFormatter = FormatterFactory.getFormatter("dot");
        GraphFormatter<String> jsonFormatter = FormatterFactory.getFormatter("json");
        
        assertNotNull(dotFormatter);
        assertNotNull(jsonFormatter);
        assertTrue(dotFormatter instanceof DotFormatter);
        assertTrue(jsonFormatter instanceof JsonFormatter);
    }
    
    @Test
    void testFormatterFactoryFormatMethod() {
        String dot = FormatterFactory.format(graph, "dot");
        String json = FormatterFactory.format(graph, "json");
        
        assertNotNull(dot);
        assertNotNull(json);
        assertTrue(dot.contains("digraph"));
        assertTrue(json.contains("\"nodes\""));
    }
    
    @Test
    void testFormatterFactoryHasFormatter() {
        assertTrue(FormatterFactory.hasFormatter("dot"));
        assertTrue(FormatterFactory.hasFormatter("json"));
        assertFalse(FormatterFactory.hasFormatter("xml"));
    }
    
    @Test
    void testFormatterFactoryAvailableFormats() {
        String[] formats = FormatterFactory.getAvailableFormats();
        
        assertNotNull(formats);
        assertEquals(2, formats.length);
        assertTrue(java.util.Arrays.asList(formats).contains("dot"));
        assertTrue(java.util.Arrays.asList(formats).contains("json"));
    }
    
    @Test
    void testDotFormatterFormatType() {
        DotFormatter formatter = new DotFormatter();
        assertEquals("text/vnd.graphviz", formatter.getFormatType());
        assertEquals("dot", formatter.getFileExtension());
    }
    
    @Test
    void testJsonFormatterFormatType() {
        JsonFormatter formatter = new JsonFormatter();
        assertEquals("application/json", formatter.getFormatType());
        assertEquals("json", formatter.getFileExtension());
    }
}

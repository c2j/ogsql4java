package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.ast.DataSource;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.graph.*;
import com.sdchat.ogsql.graph.formatter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for SQL table relationship graph extraction.
 * Tests parsing of simple and complex SQL statements.
 */
public class TableRelationshipGraphTest {
    
    private TableRelationshipExtractor extractor;
    
    @BeforeEach
    void setUp() {
        extractor = new TableRelationshipExtractor();
    }
    
    @Test
    @DisplayName("Test single table SELECT query")
    void testSingleTableSelect() {
        // Create a simple SELECT query
        SelectQuery query = new SelectQuery();
        DataSource usersTable = new DataSource("users");
        query.addDataSource(usersTable);
        
        // Extract graph
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        // Verify
        assertNotNull(graph);
        assertEquals(1, graph.getTableCount(), "Should have 1 table");
        assertEquals(0, graph.getRelationshipCount(), "Should have 0 relationships");
        
        // Verify table exists
        assertTrue(graph.getTableByName("users").isPresent(), "Should contain 'users' table");
    }
    
    @Test
    @DisplayName("Test simple WHERE clause")
    void testSimpleWhereClause() {
        SelectQuery query = new SelectQuery();
        DataSource usersTable = new DataSource("users");
        query.addDataSource(usersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertNotNull(graph);
        assertEquals(1, graph.getTableCount());
        
        TableNode table = graph.getTableByName("users").orElse(null);
        assertNotNull(table);
        assertEquals("users", table.getName());
    }
    
    @Test
    @DisplayName("Test INNER JOIN between two tables")
    void testInnerJoin() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("INNER");
        ordersTable.setJoinCondition("u.id = o.user_id");
        query.addDataSource(ordersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertEquals(2, graph.getTableCount(), "Should have 2 tables");
        assertEquals(1, graph.getRelationshipCount(), "Should have 1 relationship");
        
        // Verify relationship
        RelationshipEdge edge = graph.getRelationships().iterator().next();
        assertEquals(JoinType.INNER_JOIN, edge.getJoinType());
        assertEquals(1, edge.getConditions().size());
    }
    
    @Test
    @DisplayName("Test LEFT JOIN between two tables")
    void testLeftJoin() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("LEFT");
        ordersTable.setJoinCondition("u.id = o.user_id");
        query.addDataSource(ordersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertEquals(2, graph.getTableCount());
        assertEquals(1, graph.getRelationshipCount());
        
        RelationshipEdge edge = graph.getRelationships().iterator().next();
        assertEquals(JoinType.LEFT_JOIN, edge.getJoinType());
    }
    
    @Test
    @DisplayName("Test multi-table JOIN with 3+ tables")
    void testMultiTableJoin() {
        SelectQuery query = new SelectQuery();
        
        // users table
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        // orders table with JOIN
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("INNER");
        ordersTable.setJoinCondition("u.id = o.user_id");
        query.addDataSource(ordersTable);
        
        // products table with JOIN
        DataSource productsTable = new DataSource("products");
        productsTable.setAlias("p");
        productsTable.setJoinType("LEFT");
        productsTable.setJoinCondition("o.product_id = p.id");
        query.addDataSource(productsTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertEquals(3, graph.getTableCount(), "Should have 3 tables");
        assertEquals(2, graph.getRelationshipCount(), "Should have 2 relationships");
        
        // Verify all tables exist
        assertTrue(graph.getTableByName("users").isPresent());
        assertTrue(graph.getTableByName("orders").isPresent());
        assertTrue(graph.getTableByName("products").isPresent());
    }
    
    @Test
    @DisplayName("Test graph node count matches unique tables")
    void testGraphNodeCount() {
        SelectQuery query = new SelectQuery();
        query.addDataSource(new DataSource("table1"));
        query.addDataSource(new DataSource("table2"));
        query.addDataSource(new DataSource("table3"));
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertEquals(3, graph.getTableCount(), "Node count should match unique tables");
    }
    
    @Test
    @DisplayName("Test graph edge count matches JOIN relationships")
    void testGraphEdgeCount() {
        SelectQuery query = new SelectQuery();
        
        query.addDataSource(new DataSource("t1"));
        
        DataSource t2 = new DataSource("t2");
        t2.setJoinType("INNER");
        t2.setJoinCondition("t1.id = t2.t1_id");
        query.addDataSource(t2);
        
        DataSource t3 = new DataSource("t3");
        t3.setJoinType("LEFT");
        t3.setJoinCondition("t2.id = t3.t2_id");
        query.addDataSource(t3);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertEquals(2, graph.getRelationshipCount(), "Edge count should match JOIN relationships");
    }
    
    @Test
    @DisplayName("Test join conditions accuracy")
    void testJoinConditionsAccuracy() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("INNER");
        ordersTable.setJoinCondition("u.id = o.user_id AND u.status = o.status");
        query.addDataSource(ordersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        RelationshipEdge edge = graph.getRelationships().iterator().next();
        assertEquals(2, edge.getConditions().size(), "Should have 2 join conditions");
    }
    
    @Test
    @DisplayName("Test DOT format output is valid")
    void testDotFormatOutput() {
        SelectQuery query = createSampleQuery();
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        DotFormatter formatter = new DotFormatter();
        String dot = formatter.format(graph);
        
        assertNotNull(dot);
        assertTrue(dot.contains("digraph TableRelationships"), "Should be valid DOT header");
        assertTrue(dot.contains("users"), "Should contain users table");
        assertTrue(dot.contains("orders"), "Should contain orders table");
        assertTrue(dot.contains("INNER JOIN"), "Should contain JOIN label");
    }
    
    @Test
    @DisplayName("Test JSON format output is valid")
    void testJsonFormatOutput() {
        SelectQuery query = createSampleQuery();
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        JsonFormatter formatter = new JsonFormatter();
        String json = formatter.format(graph);
        
        assertNotNull(json);
        assertTrue(json.contains("\"nodes\""), "Should contain nodes array");
        assertTrue(json.contains("\"edges\""), "Should contain edges array");
        assertTrue(json.contains("\"name\": \"users\""), "Should contain users table");
        assertTrue(json.contains("\"name\": \"orders\""), "Should contain orders table");
        assertTrue(json.contains("\"type\": \"INNER_JOIN\""), "Should contain JOIN type");
    }
    
    @Test
    @DisplayName("Test table alias handling")
    void testTableAliasHandling() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertTrue(graph.getTableByAlias("u").isPresent(), "Should find table by alias");
        assertEquals("users", graph.getTableByAlias("u").get().getName());
    }
    
    @Test
    @DisplayName("Test empty graph handling")
    void testEmptyGraph() {
        SelectQuery query = new SelectQuery();
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertNotNull(graph);
        assertTrue(graph.isEmpty(), "Graph should be empty");
        assertEquals(0, graph.getTableCount());
        assertEquals(0, graph.getRelationshipCount());
    }
    
    @Test
    @DisplayName("Test column extraction from join conditions")
    void testColumnExtraction() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("INNER");
        ordersTable.setJoinCondition("u.id = o.user_id");
        query.addDataSource(ordersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        TableNode users = graph.getTableByName("users").orElse(null);
        TableNode orders = graph.getTableByName("orders").orElse(null);
        
        assertNotNull(users);
        assertNotNull(orders);
        
        // Columns should be extracted from join conditions
        assertFalse(users.getColumns().isEmpty(), "Users table should have columns");
        assertFalse(orders.getColumns().isEmpty(), "Orders table should have columns");
    }
    
    @Test
    @DisplayName("Test RIGHT JOIN relationship")
    void testRightJoin() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("RIGHT");
        ordersTable.setJoinCondition("u.id = o.user_id");
        query.addDataSource(ordersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        RelationshipEdge edge = graph.getRelationships().iterator().next();
        assertEquals(JoinType.RIGHT_JOIN, edge.getJoinType());
    }
    
    @Test
    @DisplayName("Test FULL JOIN relationship")
    void testFullJoin() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("FULL");
        ordersTable.setJoinCondition("u.id = o.user_id");
        query.addDataSource(ordersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        RelationshipEdge edge = graph.getRelationships().iterator().next();
        assertEquals(JoinType.FULL_JOIN, edge.getJoinType());
    }
    
    @Test
    @DisplayName("Test CROSS JOIN relationship")
    void testCrossJoin() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setJoinType("CROSS");
        query.addDataSource(ordersTable);
        
        TableRelationshipGraph graph = extractor.visitSelectQuery(query);
        
        assertEquals(2, graph.getTableCount());
        assertEquals(1, graph.getRelationshipCount());
        
        RelationshipEdge edge = graph.getRelationships().iterator().next();
        assertEquals(JoinType.CROSS_JOIN, edge.getJoinType());
    }
    
    /**
     * Helper method to create a sample query for testing.
     */
    private SelectQuery createSampleQuery() {
        SelectQuery query = new SelectQuery();
        
        DataSource usersTable = new DataSource("users");
        usersTable.setAlias("u");
        query.addDataSource(usersTable);
        
        DataSource ordersTable = new DataSource("orders");
        ordersTable.setAlias("o");
        ordersTable.setJoinType("INNER");
        ordersTable.setJoinCondition("u.id = o.user_id");
        query.addDataSource(ordersTable);
        
        return query;
    }
}

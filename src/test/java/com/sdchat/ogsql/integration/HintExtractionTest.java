package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.ast.PerformanceHint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for hint extraction.
 */
class HintExtractionTest {

    @Test
    void testExtractHintType() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) */ id FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
        assertEquals("NestLoop", select.getHints().get(0).getHintType());
    }

    @Test
    void testExtractHintTables() throws Exception {
        String sql = "SELECT /*+ HashJoin(users, orders, products) */ * FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
        
        PerformanceHint hint = select.getHints().get(0);
        assertNotNull(hint.getTables());
        assertEquals(3, hint.getTables().size());
        assertTrue(hint.getTables().contains("users"));
        assertTrue(hint.getTables().contains("orders"));
        assertTrue(hint.getTables().contains("products"));
    }

    @Test
    void testExtractMultipleHints() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) MergeJoin(orders) */ id FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(2, select.getHints().size());
        assertEquals("NestLoop", select.getHints().get(0).getHintType());
        assertEquals("MergeJoin", select.getHints().get(1).getHintType());
    }

    @Test
    void testExtractNoHints() throws Exception {
        String sql = "SELECT id FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(0, select.getHints().size());
    }

    @Test
    void testExtractHintWithWhereClause() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) */ id, name FROM users WHERE active = true";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
        assertEquals("NestLoop", select.getHints().get(0).getHintType());
    }
}

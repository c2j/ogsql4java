package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.MultiParseResult;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for hints with SELECT queries.
 */
class HintIntegrationTest {

    @Test
    void testHintParsingWithComplexQuery() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) MergeJoin(orders) */ u.id, o.order_date, u.name, o.total " +
                    "FROM users u " +
                    "JOIN orders o ON u.id = o.user_id " +
                    "WHERE u.active = true " +
                    "ORDER BY o.order_date DESC " +
                    "LIMIT 10";
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
    void testHintParsingWithSubquery() throws Exception {
        String sql = "SELECT /*+ HashJoin(orders) */ * FROM orders WHERE user_id IN (SELECT id FROM users WHERE active = true)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
    }

    @Test
    void testMultipleStatementsWithHints() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) */ id FROM users; " +
                    "SELECT /*+ MergeJoin(orders) */ * FROM orders";
        SQLParser parser = new SQLParser();
        
        MultiParseResult result = parser.parseMultiple(sql);
        
        assertNotNull(result);
        assertEquals(2, result.getStatements().size());
        
        assertInstanceOf(SelectQuery.class, result.getStatements().get(0));
        assertInstanceOf(SelectQuery.class, result.getStatements().get(1));
        
        SelectQuery select1 = (SelectQuery) result.getStatements().get(0);
        SelectQuery select2 = (SelectQuery) result.getStatements().get(1);
        
        assertNotNull(select1.getHints());
        assertNotNull(select2.getHints());
        assertEquals(1, select1.getHints().size());
        assertEquals(1, select2.getHints().size());
    }

    @Test
    void testHintParsingWithAggregation() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) */ COUNT(*), AVG(salary) FROM users GROUP BY department";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
    }

    @Test
    void testHintParsingWithDistinct() throws Exception {
        String sql = "SELECT /*+ HashJoin(orders) */ DISTINCT user_id FROM orders";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
    }
}

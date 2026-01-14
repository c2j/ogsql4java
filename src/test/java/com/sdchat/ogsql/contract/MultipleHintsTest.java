package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for multiple hints in single comment.
 */
class MultipleHintsTest {

    @Test
    void testParseSelectWithMultipleHints() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) MergeJoin(orders) */ * FROM users JOIN orders ON users.id = orders.user_id";
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
    void testParseSelectWithMultipleTableHint() throws Exception {
        String sql = "SELECT /*+ HashJoin(users, orders, products) */ * FROM users JOIN orders JOIN products";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
        assertEquals(3, select.getHints().get(0).getTables().size());
    }

    @Test
    void testParseSelectWithThreeHints() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) MergeJoin(orders) HashJoin(products) */ * FROM users, orders, products";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(3, select.getHints().size());
    }

    @Test
    void testParseSelectWithHintAndWhereClause() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) */ id FROM users WHERE active = true";
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

package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for hint parsing with single hint.
 */
class HintParseTest {

    @Test
    void testParseSelectWithNestLoopHint() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) */ id, name FROM users WHERE id = 1";
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
    void testParseSelectWithMergeJoinHint() throws Exception {
        String sql = "SELECT /*+ MergeJoin(users, orders) */ * FROM users JOIN orders ON users.id = orders.user_id";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
        assertEquals("MergeJoin", select.getHints().get(0).getHintType());
    }

    @Test
    void testParseSelectWithHashJoinHint() throws Exception {
        String sql = "SELECT /*+ HashJoin(products, categories) */ p.name, c.name FROM products p JOIN categories c ON p.category_id = c.id";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
        assertEquals("HashJoin", select.getHints().get(0).getHintType());
    }

    @Test
    void testParseSelectWithSingleTableHint() throws Exception {
        String sql = "SELECT /*+ NestLoop(users) */ * FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(1, select.getHints().size());
        assertEquals(1, select.getHints().get(0).getTables().size());
        assertEquals("users", select.getHints().get(0).getTables().get(0));
    }
}

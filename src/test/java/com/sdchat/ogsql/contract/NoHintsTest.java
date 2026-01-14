package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for query without hints.
 */
class NoHintsTest {

    @Test
    void testParseSelectWithoutHints() throws Exception {
        String sql = "SELECT id, name FROM users WHERE active = true";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(0, select.getHints().size());
    }

    @Test
    void testParseSelectWithEmptyHintsComment() throws Exception {
        String sql = "SELECT /*+ */ id FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
        assertEquals(0, select.getHints().size());
    }

    @Test
    void testParseSimpleSelectReturnsEmptyHintsList() throws Exception {
        String sql = "SELECT * FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        assertNotNull(select.getHints());
    }
}

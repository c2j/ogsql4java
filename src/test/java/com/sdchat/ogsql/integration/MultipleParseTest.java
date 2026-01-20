package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.MultiParseResult;
import com.sdchat.ogsql.ast.SQLStatement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for parsing multiple SQL statements.
 */
class MultipleParseTest {

    @Test
    void testParseMultipleSelectStatements() throws Exception {
        String sql = "SELECT id FROM users; SELECT name FROM products;";
        SQLParser parser = new SQLParser();
        
        MultiParseResult result = parser.parseMultiple(sql);
        
        assertNotNull(result);
        assertEquals(2, result.getStatements().size());
        assertInstanceOf(com.sdchat.ogsql.ast.SelectQuery.class, result.getStatements().get(0));
        assertInstanceOf(com.sdchat.ogsql.ast.SelectQuery.class, result.getStatements().get(1));
        assertFalse(result.hasErrors());
    }

    @Test
    void testParseMixedStatements() throws Exception {
        String sql = "SELECT id FROM users; INSERT INTO logs VALUES (1);";
        SQLParser parser = new SQLParser();
        
        MultiParseResult result = parser.parseMultiple(sql);
        
        assertNotNull(result);
        assertEquals(2, result.getStatements().size());
        assertInstanceOf(com.sdchat.ogsql.ast.SelectQuery.class, result.getStatements().get(0));
        assertInstanceOf(com.sdchat.ogsql.ast.InsertStatement.class, result.getStatements().get(1));
        assertFalse(result.hasErrors());
    }

    @Test
    void testParseThreeStatements() throws Exception {
        String sql = "CREATE TABLE users (id INT); INSERT INTO users VALUES (1); SELECT * FROM users;";
        SQLParser parser = new SQLParser();
        
        MultiParseResult result = parser.parseMultiple(sql);
        
        assertNotNull(result);
        assertEquals(3, result.getStatements().size());
        assertInstanceOf(com.sdchat.ogsql.ast.CreateStatement.class, result.getStatements().get(0));
        assertInstanceOf(com.sdchat.ogsql.ast.InsertStatement.class, result.getStatements().get(1));
        assertInstanceOf(com.sdchat.ogsql.ast.SelectQuery.class, result.getStatements().get(2));
        assertFalse(result.hasErrors());
    }
}

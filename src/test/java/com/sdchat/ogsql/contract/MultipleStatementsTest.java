package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.MultiParseResult;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for multiple statements parsing.
 */
class MultipleStatementsTest {

    @Test
    void testParseMultipleStatements() throws ParseException {
        String sql = "CREATE TABLE users (id INT); INSERT INTO users VALUES (1);";
        SQLParser parser = new SQLParser();
        
        MultiParseResult result = parser.parseMultiple(sql);
        
        assertNotNull(result);
        assertEquals(2, result.getStatements().size());
        assertFalse(result.hasErrors());
    }

    @Test
    void testParseMultipleSelectStatements() throws ParseException {
        String sql = "SELECT id FROM users; SELECT name FROM users;";
        SQLParser parser = new SQLParser();
        
        MultiParseResult result = parser.parseMultiple(sql);
        
        assertNotNull(result);
        assertEquals(2, result.getStatements().size());
        assertFalse(result.hasErrors());
    }

    @Test
    void testParseEmptyStatements() throws ParseException {
        String sql = "";
        SQLParser parser = new SQLParser();
        
        MultiParseResult result = parser.parseMultiple(sql);
        
        assertNotNull(result);
        assertEquals(0, result.getStatements().size());
        assertFalse(result.hasErrors());
    }
}
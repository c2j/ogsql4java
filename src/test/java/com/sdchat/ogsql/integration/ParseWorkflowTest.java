package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for end-to-end parsing workflow.
 */
class ParseWorkflowTest {

    @Test
    void testParseToAstWorkflow() throws ParseException {
        String sql = "SELECT id, name FROM users WHERE active = true";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement, "Should parse SQL statement");
        assertNotNull(statement.getStatementType(), "Should have statement type");
    }

    @Test
    void testParseMultipleStatementsWorkflow() throws ParseException {
        String sql = "CREATE TABLE users (id INT); INSERT INTO users VALUES (1); SELECT * FROM users;";
        SQLParser parser = new SQLParser();
        
        var statements = parser.parseMultiple(sql);
        
        assertNotNull(statements, "Should parse multiple statements");
        assertEquals(3, statements.size(), "Should parse 3 statements");
    }

    @Test
    void testErrorHandlingWorkflow() {
        String sql = "INVALID SQL STATEMENT";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql), 
                     "Should throw ParseException for invalid SQL");
    }

    @Test
    void testNullInputHandling() {
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(null), 
                     "Should throw ParseException for null input");
    }

    @Test
    void testEmptyInputHandling() {
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(""), 
                     "Should throw ParseException for empty input");
    }
}
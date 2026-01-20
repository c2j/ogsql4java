package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.exception.SyntaxErrorException;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for error handling in parser.
 */
class ErrorHandlingTest {

    @Test
    void testInvalidKeywordThrowsException() {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }

    @Test
    void testUnterminatedStringLiteralThrowsException() {
        String sql = "SELECT * FROM users WHERE name = 'John";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }

    @Test
    void testSyntaxErrorHasLineAndColumn() {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
        
        if (ex instanceof SyntaxErrorException) {
            SyntaxErrorException syntaxError = (SyntaxErrorException) ex;
            assertNotNull(syntaxError.getLine());
            assertNotNull(syntaxError.getColumn());
        }
    }

    @Test
    void testMismatchedParenthesesThrowsException() {
        String sql = "SELECT * FROM users WHERE (id = 1";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }

    @Test
    void testMissingRequiredClauseThrowsException() {
        String sql = "CREATE TABLE";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }
}

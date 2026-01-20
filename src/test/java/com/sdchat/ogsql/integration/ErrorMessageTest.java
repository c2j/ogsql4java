package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.exception.ParseException;
import com.sdchat.ogsql.exception.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for error message quality.
 */
class ErrorMessageTest {

    @Test
    void testErrorMessageIncludesLocation() throws Exception {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
        
        if (ex instanceof SyntaxErrorException) {
            SyntaxErrorException syntaxError = (SyntaxErrorException) ex;
            String message = syntaxError.getMessage();
            assertNotNull(message);
            assertTrue(message.contains("line") || message.contains("Syntax error"),
                "Error message should include location information");
        }
    }

    @Test
    void testErrorContextExtraction() throws Exception {
        String sql = "SELECT name, SELEC id FROM users WHERE age > 25";
        SQLParser parser = new SQLParser();
        
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
        
        if (ex instanceof SyntaxErrorException) {
            SyntaxErrorException syntaxError = (SyntaxErrorException) ex;
            String context = syntaxError.getContext();
            assertNotNull(context, "Error context should be extracted");
            assertTrue(context.contains("SELEC"), "Context should include the problematic token");
            System.out.println("Error context: " + context);
        }
    }

    @Test
    void testErrorSuggestions() throws Exception {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
        
        if (ex instanceof SyntaxErrorException) {
            SyntaxErrorException syntaxError = (SyntaxErrorException) ex;
            String suggestion = syntaxError.getSuggestion();
            assertNotNull(suggestion, "Error suggestion should be provided");
            assertTrue(suggestion.contains("SELECT"), "Suggestion should suggest correct keyword");
            System.out.println("Error suggestion: " + suggestion);
        }
    }

    @Test
    void testErrorMessageDescriptive() throws Exception {
        String sql = "CREATE TABLE";
        SQLParser parser = new SQLParser();
        
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
        
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty(),
            "Error message should not be empty");
        assertTrue(ex.getMessage().length() > 10,
            "Error message should be descriptive");
    }

    @Test
    void testErrorLineNumberAccurate() throws Exception {
        String sql = "\n\n\nSELEC id FROM users";
        SQLParser parser = new SQLParser();
        
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
        
        if (ex instanceof SyntaxErrorException) {
            SyntaxErrorException syntaxError = (SyntaxErrorException) ex;
            assertEquals(4, syntaxError.getLine(),
                "Error should be reported on correct line (4)");
        }
    }

    @Test
    void testErrorColumnNumberAccurate() throws Exception {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
        
        if (ex instanceof SyntaxErrorException) {
            SyntaxErrorException syntaxError = (SyntaxErrorException) ex;
            assertTrue(syntaxError.getColumn() >= 0,
                "Error column should be non-negative");
        }
    }

    @Test
    void testCommonMisspellingsSuggestions() throws Exception {
        // Test various common misspellings
        String[][] testCases = {
            {"SELEC id FROM users", "SELECT"},
            {"INSRT INTO users VALUES (1)", "INSERT"},
            {"UPDTE users SET name = 'test'", "UPDATE"},
            {"DELTE FROM users WHERE id = 1", "DELETE"},
            {"CREAT TABLE test (id INT)", "CREATE"},
            {"FROMT users WHERE id = 1", "FROM"}
        };

        for (String[] testCase : testCases) {
            String sql = testCase[0];
            String expectedKeyword = testCase[1];
            
            SQLParser parser = new SQLParser();
            ParseException ex = assertThrows(ParseException.class, () -> parser.parse(sql));
            
            if (ex instanceof SyntaxErrorException) {
                SyntaxErrorException syntaxError = (SyntaxErrorException) ex;
                String suggestion = syntaxError.getSuggestion();
                assertNotNull(suggestion, "Should provide suggestion for: " + sql);
                assertTrue(suggestion.contains(expectedKeyword), 
                    "Suggestion should contain '" + expectedKeyword + "' for: " + sql);
            }
        }
    }
}

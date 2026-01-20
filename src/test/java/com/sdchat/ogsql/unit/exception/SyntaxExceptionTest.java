package com.sdchat.ogsql.unit.exception;

import com.sdchat.ogsql.exception.ErrorSeverity;
import com.sdchat.ogsql.exception.SyntaxErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SyntaxErrorException class.
 */
class SyntaxErrorExceptionTest {

    @Test
    void testSyntaxErrorWithMessage() {
        String message = "Syntax error: expected FROM keyword";
        int line = 5;
        int column = 12;

        SyntaxErrorException exception = new SyntaxErrorException(message, line, column);

        assertEquals(message, exception.getMessage());
        assertEquals(line, exception.getLine());
        assertEquals(column, exception.getColumn());
        assertNull(exception.getContext());
        assertNull(exception.getSuggestion());
    }

    @Test
    void testSyntaxErrorWithContext() {
        String message = "Syntax error: expected FROM keyword";
        String context = "SELECT id FROMT users WHERE";
        int line = 5;
        int column = 12;

        SyntaxErrorException exception = new SyntaxErrorException(message, line, column, context);

        assertEquals(message, exception.getMessage());
        assertEquals(context, exception.getContext());
        assertEquals(line, exception.getLine());
        assertEquals(column, exception.getColumn());
        assertNull(exception.getSuggestion());
    }

    @Test
    void testSyntaxErrorWithSuggestion() {
        String message = "Syntax error: expected FROM keyword";
        String suggestion = "Did you mean FROM?";
        int line = 5;
        int column = 12;

        SyntaxErrorException exception = new SyntaxErrorException(message, line, column, null, suggestion);

        assertEquals(message, exception.getMessage());
        assertEquals(suggestion, exception.getSuggestion());
        assertEquals(line, exception.getLine());
        assertEquals(column, exception.getColumn());
    }

    @Test
    void testSyntaxErrorIsRuntimeException() {
        SyntaxErrorException exception = new SyntaxErrorException("Error", 1, 1);

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should report CREATE PROCEDURE syntax error for missing closing parenthesis")
    void testCreateProcedureMissingParenthesis() {
        String message = "Syntax error at line 5, column 12: expected ')' but found ','";
        int line = 5;
        int column = 12;
        String context = "CREATE PROCEDURE test_proc(\n  p1 IN INTEGER,\n  p2 IN INTEGER,  <-- Error\n);";

        SyntaxErrorException exception = new SyntaxErrorException(message, line, column, context);

        assertEquals(message, exception.getMessage());
        assertEquals(line, exception.getLine());
        assertEquals(column, exception.getColumn());
        assertEquals(context, exception.getContext());
        assertNull(exception.getSuggestion());
    }

    @Test
    @DisplayName("Should report CALL statement with mixed parameter styles")
    void testCallMixedParameterStyles() {
        String message = "Syntax error at line 3, column 25: cannot mix positional and named parameters";
        int line = 3;
        int column = 25;
        String context = "CALL test_proc(p1 => 1, p2, 3)";

        SyntaxErrorException exception = new SyntaxErrorException(message, line, column, context);

        assertEquals(message, exception.getMessage());
        assertEquals(line, exception.getLine());
        assertEquals(column, exception.getColumn());
        assertEquals(context, exception.getContext());
    }

    @Test
    void testSyntaxErrorWithAllFields() {
        String message = "Syntax error at line 8, column 12: invalid parameter";
        int line = 8;
        int column = 12;
        String context = "CREATE PROCEDURE test_proc(VARIADIC)";
        String suggestion = "VARIADIC parameters must be last";
        
        SyntaxErrorException exception = new SyntaxErrorException(message, line, column, context, suggestion);
        
        assertEquals(message, exception.getMessage());
        assertEquals(line, exception.getLine());
        assertEquals(column, exception.getColumn());
        assertEquals(context, exception.getContext());
        assertEquals(suggestion, exception.getSuggestion());
    }

    @Test
    @DisplayName("Should report DROP PROCEDURE with invalid options")
    void testDropProcedureInvalidOptions() {
        String message = "Syntax error at line 1, column 24: cannot specify both IF EXISTS and CASCADE simultaneously";
        int line = 1;
        int column = 24;
        String context = "DROP PROCEDURE IF EXISTS CASCADE test_proc";
        
        SyntaxErrorException exception = new SyntaxErrorException(message, line, column, context);
        
        assertEquals(message, exception.getMessage());
        assertEquals(line, exception.getLine());
        assertEquals(column, exception.getColumn());
        assertEquals(context, exception.getContext());
        assertNull(exception.getSuggestion());
    }
}

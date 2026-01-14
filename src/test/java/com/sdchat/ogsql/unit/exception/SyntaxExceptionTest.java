package com.sdchat.ogsql.unit.exception;

import com.sdchat.ogsql.exception.ErrorSeverity;
import com.sdchat.ogsql.exception.SyntaxErrorException;
import org.junit.jupiter.api.Test;
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
}

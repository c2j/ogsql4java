package com.sdchat.ogsql.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ParseException class.
 */
class ParseExceptionTest {

    @Test
    void testParseExceptionWithMessage() {
        String message = "Failed to parse SQL";

        ParseException exception = new ParseException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testParseExceptionWithCause() {
        String message = "Failed to parse SQL";
        Throwable cause = new RuntimeException("Underlying error");

        ParseException exception = new ParseException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testParseExceptionIsRuntimeException() {
        ParseException exception = new ParseException("Error");

        assertTrue(exception instanceof RuntimeException);
    }
}

package com.sdchat.ogsql.unit.exception;

import com.sdchat.ogsql.exception.InputValidationException;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InputValidationException class.
 */
class InputValidationExceptionTest {

    @Test
    void testInputValidationErrorWithMessage() {
        String message = "File too large: exceeds 100MB limit";

        InputValidationException exception = new InputValidationException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getInputType());
        assertEquals(-1, exception.getActualSize());
        assertEquals(-1, exception.getMaxSize());
    }

    @Test
    void testInputValidationErrorWithDetails() {
        String message = "File too large";
        String inputType = "SQL_FILE";
        long actualSize = 150 * 1024 * 1024;
        long maxSize = 100 * 1024 * 1024;

        InputValidationException exception = new InputValidationException(message, inputType, actualSize, maxSize);

        assertEquals(message, exception.getMessage());
        assertEquals(inputType, exception.getInputType());
        assertEquals(actualSize, exception.getActualSize());
        assertEquals(maxSize, exception.getMaxSize());
    }

    @Test
    void testInputValidationErrorIsRuntimeException() {
        InputValidationException exception = new InputValidationException("Input error");

        assertTrue(exception instanceof RuntimeException);
    }
}

package com.sdchat.ogsql.exception;

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
    }

    @Test
    void testInputValidationErrorIsRuntimeException() {
        InputValidationException exception = new InputValidationException("Input error");

        assertTrue(exception instanceof RuntimeException);
    }
}

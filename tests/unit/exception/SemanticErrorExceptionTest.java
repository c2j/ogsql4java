package com.sdchat.ogsql.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SemanticErrorException class.
 */
class SemanticErrorExceptionTest {

    @Test
    void testSemanticErrorWithMessage() {
        String message = "Invalid partition type: XYZ";

        SemanticErrorException exception = new SemanticErrorException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testSemanticErrorIsRuntimeException() {
        SemanticErrorException exception = new SemanticErrorException("Semantic error");

        assertTrue(exception instanceof RuntimeException);
    }
}

package com.sdchat.ogsql.unit.exception;

import com.sdchat.ogsql.exception.ParseException;
import com.sdchat.ogsql.exception.SemanticErrorException;
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
        assertNull(exception.getQueryPart());
    }

    @Test
    void testSemanticErrorWithQueryPart() {
        String message = "Invalid partition type";
        String queryPart = "PARTITION BY XYZ";

        SemanticErrorException exception = new SemanticErrorException(message, queryPart);

        assertEquals(message, exception.getMessage());
        assertEquals(queryPart, exception.getQueryPart());
    }

    @Test
    void testSemanticErrorIsRuntimeException() {
        SemanticErrorException exception = new SemanticErrorException("Semantic error");

        assertTrue(exception instanceof RuntimeException);
    }
}

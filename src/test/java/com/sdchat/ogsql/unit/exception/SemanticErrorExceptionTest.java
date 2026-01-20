package com.sdchat.ogsql.unit.exception;

import com.sdchat.ogsql.exception.ParseException;
import com.sdchat.ogsql.exception.SemanticErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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
        assertNull(exception.getSemanticIssue());
    }

    @Test
    void testSemanticErrorWithSemanticIssue() {
        String message = "Invalid partition type";
        String semanticIssue = "Partition type XYZ not supported";

        SemanticErrorException exception = new SemanticErrorException(message, semanticIssue);

        assertEquals(message, exception.getMessage());
        assertEquals(semanticIssue, exception.getSemanticIssue());
    }

    @Test
    void testSemanticErrorIsRuntimeException() {
        SemanticErrorException exception = new SemanticErrorException("Semantic error");

        assertTrue(exception instanceof RuntimeException);
    }
}

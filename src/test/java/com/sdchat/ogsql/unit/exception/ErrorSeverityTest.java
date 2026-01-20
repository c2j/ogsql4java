package com.sdchat.ogsql.unit.exception;

import com.sdchat.ogsql.exception.ErrorSeverity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ErrorSeverity enum.
 */
class ErrorSeverityTest {

    @Test
    void testErrorSeverityValues() {
        assertEquals("ERROR", ErrorSeverity.ERROR.name());
        assertEquals("WARNING", ErrorSeverity.WARNING.name());
        assertEquals("INFO", ErrorSeverity.INFO.name());
    }

    @Test
    void testErrorSeverityCount() {
        assertEquals(3, ErrorSeverity.values().length);
    }
}

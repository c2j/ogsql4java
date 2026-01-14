package com.sdchat.ogsql.unit.parser;

import com.sdchat.ogsql.parser.ParsingError;
import com.sdchat.ogsql.exception.ErrorSeverity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ParsingError class.
 */
class ParsingErrorTest {

    @Test
    void testParsingErrorBasicConstructor() {
        ParsingError error = new ParsingError("Syntax error", 5, 12, ErrorSeverity.ERROR);

        assertEquals("Syntax error", error.getMessage());
        assertEquals(5, error.getLine());
        assertEquals(12, error.getColumn());
        assertEquals(ErrorSeverity.ERROR, error.getSeverity());
        assertNull(error.getContext());
        assertNull(error.getSuggestion());
    }

    @Test
    void testParsingErrorWithContext() {
        String context = "SELECT id FROMT users";
        ParsingError error = new ParsingError("Syntax error", 5, 12, ErrorSeverity.ERROR, context);

        assertEquals("Syntax error", error.getMessage());
        assertEquals(5, error.getLine());
        assertEquals(12, error.getColumn());
        assertEquals(ErrorSeverity.ERROR, error.getSeverity());
        assertEquals(context, error.getContext());
        assertNull(error.getSuggestion());
    }

    @Test
    void testParsingErrorWithSuggestion() {
        String context = "SELECT id FROMT users";
        String suggestion = "Did you mean FROM?";
        ParsingError error = new ParsingError("Syntax error", 5, 12, ErrorSeverity.ERROR, context, suggestion);

        assertEquals("Syntax error", error.getMessage());
        assertEquals(5, error.getLine());
        assertEquals(12, error.getColumn());
        assertEquals(ErrorSeverity.ERROR, error.getSeverity());
        assertEquals(context, error.getContext());
        assertEquals(suggestion, error.getSuggestion());
    }

    @Test
    void testParsingErrorWithWarningSeverity() {
        ParsingError error = new ParsingError("Warning", 1, 1, ErrorSeverity.WARNING);

        assertEquals(ErrorSeverity.WARNING, error.getSeverity());
    }

    @Test
    void testParsingErrorWithInfoSeverity() {
        ParsingError error = new ParsingError("Info", 1, 1, ErrorSeverity.INFO);

        assertEquals(ErrorSeverity.INFO, error.getSeverity());
    }
}
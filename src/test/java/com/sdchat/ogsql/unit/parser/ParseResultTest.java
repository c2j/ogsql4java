package com.sdchat.ogsql.unit.parser;

import com.sdchat.ogsql.parser.ParseResult;
import com.sdchat.ogsql.parser.ParsingError;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.exception.ErrorSeverity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ParseResult class.
 */
class ParseResultTest {

    @Test
    void testParseResultSuccessConstructor() {
        SQLStatement statement = new SelectQuery();

        ParseResult result = new ParseResult(statement);

        assertTrue(result.isSuccess());
        assertEquals(statement, result.getStatement());
        assertNull(result.getError());
    }

    @Test
    void testParseResultErrorConstructor() {
        ParsingError error = new ParsingError("Parse failed", 1, 1, ErrorSeverity.ERROR);

        ParseResult result = new ParseResult(error);

        assertFalse(result.isSuccess());
        assertEquals(error, result.getError());
        assertNull(result.getStatement());
    }

    @Test
    void testGetStatementOnSuccessReturnsStatement() {
        SQLStatement statement = new SelectQuery();

        ParseResult result = new ParseResult(statement);

        assertSame(statement, result.getStatement());
    }

    @Test
    void testGetErrorOnSuccessReturnsNull() {
        SQLStatement statement = new SelectQuery();

        ParseResult result = new ParseResult(statement);

        assertNull(result.getError());
    }

    @Test
    void testGetStatementOnErrorReturnsNull() {
        ParsingError error = new ParsingError("Parse failed", 1, 1, ErrorSeverity.ERROR);

        ParseResult result = new ParseResult(error);

        assertNull(result.getStatement());
    }

    @Test
    void testGetErrorOnErrorReturnsError() {
        ParsingError error = new ParsingError("Parse failed", 1, 1, ErrorSeverity.ERROR);

        ParseResult result = new ParseResult(error);

        assertSame(error, result.getError());
    }
}
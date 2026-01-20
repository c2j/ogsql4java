package com.sdchat.ogsql.unit.parser;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.MultiParseResult;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SQLParser class.
 */
class SQLParserTest {

    @Test
    void testParseNullThrowsException() {
        SQLParser parser = new SQLParser();

        assertThrows(ParseException.class, () -> parser.parse(null));
    }

    @Test
    void testParseEmptyThrowsException() {
        SQLParser parser = new SQLParser();

        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    void testParseWhitespaceThrowsException() {
        SQLParser parser = new SQLParser();

        assertThrows(ParseException.class, () -> parser.parse("   "));
    }

    @Test
    void testParseSimpleSelectReturnsStatement() throws ParseException {
        String sql = "SELECT id FROM users";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
    }

    @Test
    void testParseMultipleReturnsEmptyListForNull() {
        SQLParser parser = new SQLParser();

        MultiParseResult result = parser.parseMultiple(null);

        assertNotNull(result);
        assertEquals(0, result.getStatements().size());
        assertFalse(result.hasErrors());
    }

    @Test
    void testParseMultipleReturnsEmptyListForEmpty() {
        SQLParser parser = new SQLParser();

        MultiParseResult result = parser.parseMultiple("");

        assertNotNull(result);
        assertEquals(0, result.getStatements().size());
        assertFalse(result.hasErrors());
    }

    @Test
    void testParseMultipleReturnsStatementsForValidSQL() throws ParseException {
        String sql = "SELECT id FROM users; INSERT INTO users VALUES (1);";
        SQLParser parser = new SQLParser();

        MultiParseResult result = parser.parseMultiple(sql);

        assertNotNull(result);
        assertEquals(2, result.getStatements().size());
        assertFalse(result.hasErrors());
    }
}
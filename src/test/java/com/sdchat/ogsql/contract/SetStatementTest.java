package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for SET statement parsing.
 */
class SetStatementTest {

    @Test
    void testSetSimple() throws ParseException {
        String sql = "SET enable_seqscan = OFF";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertEquals(com.sdchat.ogsql.ast.StatementType.SET, statement.getStatementType());
    }

    @Test
    void testSetWithBooleanOn() throws ParseException {
        String sql = "SET enable_indexscan = ON";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertEquals(com.sdchat.ogsql.ast.StatementType.SET, statement.getStatementType());
    }

    @Test
    void testSetWithBooleanOff() throws ParseException {
        String sql = "SET enable_bitmapscan = OFF";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertEquals(com.sdchat.ogsql.ast.StatementType.SET, statement.getStatementType());
    }

    @Test
    void testSetUsingTOKeyword() throws ParseException {
        String sql = "SET myvar TO true";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertEquals(com.sdchat.ogsql.ast.StatementType.SET, statement.getStatementType());
    }

    @Test
    void testSetWithNumber() throws ParseException {
        String sql = "SET work_mem = 100";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertEquals(com.sdchat.ogsql.ast.StatementType.SET, statement.getStatementType());
    }

    @Test
    void testSetWithComplexBoolean() throws ParseException {
        String sql = "SET ENABLE_STREAM_OPERATOR = ON";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertEquals(com.sdchat.ogsql.ast.StatementType.SET, statement.getStatementType());
    }
}

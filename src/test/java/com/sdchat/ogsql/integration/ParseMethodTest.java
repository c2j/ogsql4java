package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Integration tests for parser methods")
class ParseMethodTest {

    @Test
    @DisplayName("Should parse simple SELECT statement")
    void testParseSelect() {
        SQLParser parser = new SQLParser();
        String sql = "SELECT id FROM users";
        System.out.println("Parsing: " + sql);
        SQLStatement stmt = parser.parse(sql);
        System.out.println("Result: " + (stmt != null ? stmt.getClass().getSimpleName() : "null"));

        assertNotNull(stmt, "Statement should not be null");
        if (stmt != null) {
            assertEquals(StatementType.SELECT, stmt.getStatementType());
            assertTrue(stmt instanceof SelectQuery, "Statement should be SelectQuery");
            
            SelectQuery select = (SelectQuery) stmt;
            assertEquals("users", select.getFromClause());
        }
    }

    @Test
    @DisplayName("Should parse UPDATE statement")
    void testParseUpdate() {
        SQLParser parser = new SQLParser();
        SQLStatement stmt = parser.parse("UPDATE users SET name = 'Jane'");

        assertNotNull(stmt, "Statement should not be null");
        if (stmt != null) {
            assertEquals(StatementType.UPDATE, stmt.getStatementType());
            assertTrue(stmt instanceof UpdateStatement, "Statement should be UpdateStatement");
        }
    }

    @Test
    @DisplayName("Should parse CREATE TABLE statement")
    void testParseCreate() {
        SQLParser parser = new SQLParser();
        SQLStatement stmt = parser.parse("CREATE TABLE users (id INT)");

        assertNotNull(stmt, "Statement should not be null");
        if (stmt != null) {
            assertEquals(StatementType.CREATE_TABLE, stmt.getStatementType());
            assertTrue(stmt instanceof CreateStatement, "Statement should be CreateStatement");
        }
    }

    @Test
    @DisplayName("Should parse DROP TABLE statement")
    void testParseDrop() {
        SQLParser parser = new SQLParser();
        SQLStatement stmt = parser.parse("DROP TABLE users");

        assertNotNull(stmt, "Statement should not be null");
        if (stmt != null) {
            assertEquals(StatementType.DROP_TABLE, stmt.getStatementType());
            assertTrue(stmt instanceof DropStatement, "Statement should be DropStatement");
        }
    }
}

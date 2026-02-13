package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.InsertStatement;
import com.sdchat.ogsql.ast.UpdateStatement;
import com.sdchat.ogsql.ast.DeleteStatement;
import com.sdchat.ogsql.ast.ReturningClause;
import com.sdchat.ogsql.ast.ReturningExpression;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for RETURNING clause in INSERT/UPDATE/DELETE statements.
 */
class ReturningClauseTest {

    @Test
    void testParseInsertReturningSingleColumn() throws ParseException {
        String sql = "INSERT INTO users (name) VALUES ('John') RETURNING id";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        assertTrue(insert.hasReturningClause());

        ReturningClause returning = insert.getReturningClause();
        assertNotNull(returning);
        assertEquals(1, returning.getExpressions().size());
        assertEquals("id", returning.getExpressions().get(0).getExpression());
    }

    @Test
    void testParseInsertReturningMultipleColumns() throws ParseException {
        String sql = "INSERT INTO users (name) VALUES ('John') RETURNING id, name, created_at";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        ReturningClause returning = insert.getReturningClause();
        assertNotNull(returning);
        assertEquals(3, returning.getExpressions().size());
    }

    @Test
    void testParseInsertReturningWildcard() throws ParseException {
        String sql = "INSERT INTO users (name) VALUES ('John') RETURNING *";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        assertTrue(insert.hasReturningClause());

        ReturningClause returning = insert.getReturningClause();
        assertNotNull(returning);
        assertEquals(1, returning.getExpressions().size());
        assertEquals("*", returning.getExpressions().get(0).getExpression());
    }

    @Test
    void testParseInsertReturningWithQualifiedColumn() throws ParseException {
        String sql = "INSERT INTO users (name) VALUES ('John') RETURNING users.id";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        ReturningClause returning = insert.getReturningClause();
        assertNotNull(returning);
        assertEquals("users.id", returning.getExpressions().get(0).getExpression());
    }

    @Test
    void testParseUpdateReturning() throws ParseException {
        String sql = "UPDATE users SET name = 'Jane' WHERE id = 1 RETURNING id, name";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(UpdateStatement.class, statement);

        UpdateStatement update = (UpdateStatement) statement;
        assertTrue(update.hasReturningClause());

        ReturningClause returning = update.getReturningClause();
        assertNotNull(returning);
        assertEquals(2, returning.getExpressions().size());
    }

    @Test
    void testParseUpdateReturningWithExpression() throws ParseException {
        String sql = "UPDATE users SET counter = counter + 1 WHERE id = 1 RETURNING counter AS new_counter";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(UpdateStatement.class, statement);

        UpdateStatement update = (UpdateStatement) statement;
        ReturningClause returning = update.getReturningClause();
        assertNotNull(returning);
        assertTrue(returning.getExpressions().get(0).hasAlias());
    }

    @Test
    void testParseDeleteReturning() throws ParseException {
        String sql = "DELETE FROM users WHERE id = 1 RETURNING *";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(DeleteStatement.class, statement);

        DeleteStatement delete = (DeleteStatement) statement;
        assertTrue(delete.hasReturningClause());

        ReturningClause returning = delete.getReturningClause();
        assertNotNull(returning);
    }

    @Test
    void testParseDeleteReturningWithColumns() throws ParseException {
        String sql = "DELETE FROM users WHERE id = 1 RETURNING id, name";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(DeleteStatement.class, statement);

        DeleteStatement delete = (DeleteStatement) statement;
        ReturningClause returning = delete.getReturningClause();
        assertNotNull(returning);
        assertEquals(2, returning.getExpressions().size());
    }
}

package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.InsertStatement;
import com.sdchat.ogsql.ast.OnConflictClause;
import com.sdchat.ogsql.ast.ConflictAction;
import com.sdchat.ogsql.ast.ConflictTarget;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for INSERT ON CONFLICT (Upsert) statements.
 */
class InsertOnConflictTest {

    @Test
    void testParseDoNothing() throws ParseException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT DO NOTHING";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        assertTrue(insert.hasOnConflictClause());

        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);
        assertEquals(ConflictAction.DO_NOTHING, clause.getConflictAction());
    }

    @Test
    void testParseDoNothingWithConflictTarget() throws ParseException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT (id) DO NOTHING";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);

        ConflictTarget target = clause.getConflictTarget();
        assertNotNull(target);
        assertTrue(target.isColumnList());
        assertFalse(target.isConstraintName());
        assertEquals(1, target.getColumns().size());
        assertEquals("id", target.getColumns().get(0));
    }

    @Test
    void testParseDoNothingWithConstraintName() throws ParseException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT ON CONSTRAINT users_pkey DO NOTHING";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);

        ConflictTarget target = clause.getConflictTarget();
        assertNotNull(target);
        assertTrue(target.isConstraintName());
        assertFalse(target.isColumnList());
        assertEquals("users_pkey", target.getConstraintName());
    }

    @Test
    void testParseDoUpdateWithSingleColumn() throws ParseException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);
        assertEquals(ConflictAction.DO_UPDATE, clause.getConflictAction());
        assertTrue(clause.getUpdateAssignments().containsKey("name"));
    }

    @Test
    void testParseDoUpdateWithMultipleColumns() throws ParseException {
        String sql = "INSERT INTO users (id, name, email) VALUES (1, 'John', 'john@example.com') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, email = EXCLUDED.email";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);
        assertEquals(2, clause.getUpdateAssignments().size());
        assertTrue(clause.getUpdateAssignments().containsKey("name"));
        assertTrue(clause.getUpdateAssignments().containsKey("email"));
    }

    @Test
    void testParseDoUpdateWithWhere() throws ParseException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name WHERE users.updated_at < EXCLUDED.updated_at";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);
        assertNotNull(clause.getWhereClause());
    }

    @Test
    void testParseOnConflictWithMultipleColumns() throws ParseException {
        String sql = "INSERT INTO users (id, email, name) VALUES (1, 'john@example.com', 'John') ON CONFLICT (id, email) DO NOTHING";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);

        ConflictTarget target = clause.getConflictTarget();
        assertNotNull(target);
        assertEquals(2, target.getColumns().size());
        assertEquals("id", target.getColumns().get(0));
        assertEquals("email", target.getColumns().get(1));
    }

    @Test
    void testParseDoNothingWithoutConflictTarget() throws ParseException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT DO NOTHING";
        SQLParser parser = new SQLParser();

        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);

        InsertStatement insert = (InsertStatement) statement;
        OnConflictClause clause = insert.getOnConflictClause();
        assertNotNull(clause);

        ConflictTarget target = clause.getConflictTarget();
        assertNotNull(target);
        assertFalse(target.isColumnList());
        assertFalse(target.isConstraintName());
    }
}

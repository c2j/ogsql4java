package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.ast.StatementType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SQLStatement interface.
 */
class SQLStatementTest {

    @Test
    void testSelectQueryImplementsSQLStatement() {
        SQLStatement statement = new SelectQuery();

        assertEquals(StatementType.SELECT, statement.getStatementType());
    }

    @Test
    void testSQLStatementIsInterface() {
        assertTrue(SQLStatement.class.isInterface());
    }

    @Test
    void testSQLStatementHasGetStatementTypeMethod() throws NoSuchMethodException {
        assertTrue(SQLStatement.class.getMethod("getStatementType") != null);
    }

    @Test
    void testSQLStatementHasAcceptMethod() throws NoSuchMethodException {
        assertTrue(SQLStatement.class.getMethod("accept", com.sdchat.ogsql.visitor.ASTVisitor.class) != null);
    }
}
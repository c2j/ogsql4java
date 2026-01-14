package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.StatementType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StatementType enum.
 */
class StatementTypeTest {

    @Test
    void testStatementTypeValues() {
        assertEquals(9, StatementType.values().length);
    }

    @Test
    void testStatementTypeHasSelect() {
        assertEquals("SELECT", StatementType.SELECT.name());
    }

    @Test
    void testStatementTypeHasInsert() {
        assertEquals("INSERT", StatementType.INSERT.name());
    }

    @Test
    void testStatementTypeHasUpdate() {
        assertEquals("UPDATE", StatementType.UPDATE.name());
    }

    @Test
    void testStatementTypeHasDelete() {
        assertEquals("DELETE", StatementType.DELETE.name());
    }

    @Test
    void testStatementTypeHasCreateTable() {
        assertEquals("CREATE_TABLE", StatementType.CREATE_TABLE.name());
    }

    @Test
    void testStatementTypeHasAlterTable() {
        assertEquals("ALTER_TABLE", StatementType.ALTER_TABLE.name());
    }

    @Test
    void testStatementTypeHasDropTable() {
        assertEquals("DROP_TABLE", StatementType.DROP_TABLE.name());
    }

    @Test
    void testStatementTypeHasCreateForeignTable() {
        assertEquals("CREATE_FOREIGN_TABLE", StatementType.CREATE_FOREIGN_TABLE.name());
    }

    @Test
    void testStatementTypeHasUnknown() {
        assertEquals("UNKNOWN", StatementType.UNKNOWN.name());
    }
}
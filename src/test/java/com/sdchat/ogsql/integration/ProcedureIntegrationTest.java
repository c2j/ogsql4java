package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.MultiParseResult;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateProcedureStmt;
import com.sdchat.ogsql.ast.AlterProcedureStmt;
import com.sdchat.ogsql.ast.CallFuncStmt;
import com.sdchat.ogsql.ast.DropStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ProcedureIntegrationTest {

    @Test
    @DisplayName("Should parse CREATE PROCEDURE successfully")
    void testCreateProcedureParsing() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "CREATE PROCEDURE simple_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CreateProcedureStmt, "Should be CreateProcedureStmt");
    }

    @Test
    @DisplayName("Should parse ALTER PROCEDURE successfully")
    void testAlterProcedureParsing() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "ALTER PROCEDURE test_proc RENAME TO new_proc;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof AlterProcedureStmt, "Should be AlterProcedureStmt");
    }

    @Test
    @DisplayName("Should parse CALL statement successfully")
    void testCallStatementParsing() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "CALL test_proc(1, 2, 3);";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CallFuncStmt, "Should be CallFuncStmt");
    }

    @Test
    @DisplayName("Should parse DROP PROCEDURE successfully")
    void testDropProcedureParsing() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "DROP PROCEDURE test_proc;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof DropStatement, "Should be DropStatement");
    }

    @Test
    @DisplayName("Should handle complex procedure with multiple parameters")
    void testComplexProcedureParsing() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = """
            CREATE OR REPLACE PROCEDURE complex_proc(
                p1 IN INTEGER DEFAULT 0,
                p2 INOUT NUMERIC,
                p3 OUT VARCHAR
            )
            LANGUAGE plpgsql
            SECURITY DEFINER
            AS $$
            BEGIN
                IF p1 > 0 THEN
                    p2 := p1 * 2;
                END IF;
                p3 := 'Result: ' || p2;
            END;
            $$;
            """;

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CreateProcedureStmt, "Should be CreateProcedureStmt");
    }

    @Test
    @DisplayName("Should handle CALL with named parameters")
    void testCallWithNamedParameters() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "CALL test_proc(p1 => 1, p2 => 2, p3 => 3);";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CallFuncStmt, "Should be CallFuncStmt");

        CallFuncStmt callStmt = (CallFuncStmt) stmt;
        assertNotNull(callStmt.getNamedArguments(), "Named arguments should not be null");
        assertEquals(3, callStmt.getNamedArguments().size(), "Should have 3 named arguments");
    }

    @Test
    @DisplayName("Should handle DROP PROCEDURE IF EXISTS")
    void testDropProcedureIfExistsParsing() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "DROP PROCEDURE IF EXISTS test_proc;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof DropStatement, "Should be DropStatement");

        DropStatement dropStmt = (DropStatement) stmt;
        assertTrue(dropStmt.isIfExists(), "IF EXISTS should be set");
    }

    @Test
    @DisplayName("Should handle DROP PROCEDURE CASCADE")
    void testDropProcedureCascadeParsing() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "DROP PROCEDURE test_proc CASCADE;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof DropStatement, "Should be DropStatement");

        DropStatement dropStmt = (DropStatement) stmt;
        assertTrue(dropStmt.isCascade(), "CASCADE should be set");
    }

    @Test
    @DisplayName("Should parse multiple procedure statements in sequence")
    void testMultipleProcedureStatements() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = """
            CREATE PROCEDURE proc1(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;
            ALTER PROCEDURE proc1 RENAME TO proc2;
            CALL proc2(1);
            DROP PROCEDURE proc2;
            """;

        MultiParseResult result = parser.parseMultiple(sql);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.getStatements().size() >= 3, "Should parse at least 3 statements");
    }

    @Test
    @DisplayName("Should parse procedure within performance time limit")
    void testParsingPerformance() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = """
            CREATE OR REPLACE PROCEDURE performance_test(
                p1 IN INTEGER DEFAULT 0,
                p2 IN VARCHAR(100) DEFAULT 'test',
                p3 INOUT NUMERIC,
                p4 OUT VARCHAR(200)
            )
            LANGUAGE plpgsql
            SECURITY DEFINER
            AS $$
            BEGIN
                IF p1 > 0 THEN
                    p3 := p1 * 100;
                END IF;
                p4 := 'Processed: ' || p3;
            END;
            $$;
            """;

        long startTime = System.nanoTime();
        SQLStatement stmt = parser.parse(sql);
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CreateProcedureStmt, "Should be CreateProcedureStmt");
        assertTrue(durationMs < 50.0, "Parsing should complete in under 50ms, actual: " + durationMs + "ms");
    }

    @Test
    @DisplayName("Should handle large procedure bodies efficiently")
    void testMemoryEfficiency() throws Exception {
        SQLParser parser = new SQLParser();
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE PROCEDURE memory_test(");
        for (int i = 0; i < 50; i++) {
            if (i > 0) sql.append(", ");
            sql.append("p").append(i).append(" IN VARCHAR DEFAULT 'test_data_").append(i).append("'");
        }
        sql.append(") LANGUAGE plpgsql AS $$ BEGIN ");
        for (int i = 0; i < 50; i++) {
            if (i > 0) sql.append("; ");
            sql.append("DECLARE v").append(i).append(" VARCHAR; ");
        }
        sql.append("NULL; END; $$;");
        
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        SQLStatement stmt = parser.parse(sql.toString());
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoryUsed = endMemory - startMemory;

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CreateProcedureStmt, "Should be CreateProcedureStmt");
    }

    @Test
    @DisplayName("Should parse single statement successfully")
    void testErrorRecovery() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "CREATE PROCEDURE recovery_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CreateProcedureStmt, "Should be CreateProcedureStmt");
    }

    @Test
    @DisplayName("Should handle procedure compatibility modes")
    void testCompatibilityModes() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = """
            CREATE PROCEDURE compat_test(p1 IN INTEGER)
            LANGUAGE plpgsql
            AS $$
            BEGIN
                NULL;
            END;
            $$;
            """;

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof CreateProcedureStmt, "Should be CreateProcedureStmt");
    }
}

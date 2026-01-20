package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.AlterProcedureStmt;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrammarTest {

    @Test
    public void testAlterProcedureRenameGrammar() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "ALTER PROCEDURE test_proc RENAME TO new_proc;";
        
        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof com.sdchat.ogsql.ast.AlterProcedureStmt, "Statement should be AlterProcedureStmt");

        AlterProcedureStmt alterStmt = (AlterProcedureStmt) stmt;
        assertEquals("test_proc", alterStmt.getProcedureName());
        assertEquals("new_proc", alterStmt.getNewName());
        assertNull(alterStmt.getNewOwner(), "RENAME action should not have owner");
        assertNull(alterStmt.getNewSchema(), "RENAME action should not have schema");
        assertNull(alterStmt.getSecurityInvoker(), "RENAME action should not have security invoker");
    }

    @Test
    public void testAlterProcedureOwnerGrammar() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "ALTER PROCEDURE test_proc OWNER TO new_owner;";
        
        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        AlterProcedureStmt alterStmt = (AlterProcedureStmt) stmt;
        assertEquals("test_proc", alterStmt.getProcedureName());
        assertEquals("new_owner", alterStmt.getNewOwner());
        assertNull(alterStmt.getNewName(), "OWNER action should not have new name");
        assertNull(alterStmt.getNewSchema(), "OWNER action should not have schema");
        assertNull(alterStmt.getSecurityInvoker(), "OWNER action should not have security invoker");
    }

    @Test
    public void testAlterProcedureSchemaGrammar() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "ALTER PROCEDURE test_proc SET SCHEMA new_schema;";
        
        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        AlterProcedureStmt alterStmt = (AlterProcedureStmt) stmt;
        assertEquals("test_proc", alterStmt.getProcedureName());
        assertEquals("new_schema", alterStmt.getNewSchema());
        assertNull(alterStmt.getNewName(), "SET SCHEMA action should not have new name");
        assertNull(alterStmt.getNewOwner(), "SET SCHEMA action should not have owner");
        assertNull(alterStmt.getSecurityInvoker(), "SET SCHEMA action should not have security invoker");
    }

    @Test
    public void testAlterProcedureSecurityInvokerGrammar() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "ALTER PROCEDURE test_proc SECURITY INVOKER;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        AlterProcedureStmt alterStmt = (AlterProcedureStmt) stmt;
        assertEquals("test_proc", alterStmt.getProcedureName());
        assertNull(alterStmt.getNewName(), "SECURITY INVOKER action should not have new name");
        assertNull(alterStmt.getNewOwner(), "SECURITY INVOKER action should not have owner");
        assertNull(alterStmt.getNewSchema(), "SECURITY INVOKER action should not have schema");
        assertTrue(alterStmt.getSecurityInvoker(), "SECURITY INVOKER should be true");
    }

    @Test
    public void testCallStatementGrammar() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "CALL test_proc(1, 2, 3);";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof com.sdchat.ogsql.ast.CallFuncStmt, "Statement should be CallFuncStmt");

        com.sdchat.ogsql.ast.CallFuncStmt callStmt = (com.sdchat.ogsql.ast.CallFuncStmt) stmt;
        assertEquals("test_proc", callStmt.getProcedureName());
        assertNotNull(callStmt.getArguments(), "Arguments should not be null");
        assertEquals(3, callStmt.getArguments().size());
    }

    @Test
    public void testCallWithNamedParameters() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "CALL test_proc(p1 => 1, p2 => 2, p3 => 3);";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof com.sdchat.ogsql.ast.CallFuncStmt, "Statement should be CallFuncStmt");

        com.sdchat.ogsql.ast.CallFuncStmt callStmt = (com.sdchat.ogsql.ast.CallFuncStmt) stmt;
        assertEquals("test_proc", callStmt.getProcedureName());
        assertNotNull(callStmt.getNamedArguments(), "Named arguments should not be null");
        assertEquals(3, callStmt.getNamedArguments().size());
        assertTrue(callStmt.getNamedArguments().containsKey("p1"), "Should have p1 parameter");
        assertTrue(callStmt.getNamedArguments().containsKey("p2"), "Should have p2 parameter");
        assertTrue(callStmt.getNamedArguments().containsKey("p3"), "Should have p3 parameter");
    }

    @Test
    public void testDropProcedureGrammar() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "DROP PROCEDURE test_proc;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof com.sdchat.ogsql.ast.DropStatement, "Statement should be DropStatement");

        com.sdchat.ogsql.ast.DropStatement dropStmt = (com.sdchat.ogsql.ast.DropStatement) stmt;
        assertEquals("test_proc", dropStmt.getObjectName());
    }

    @Test
    public void testDropProcedureIfExists() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "DROP PROCEDURE IF EXISTS test_proc;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof com.sdchat.ogsql.ast.DropStatement, "Statement should be DropStatement");

        com.sdchat.ogsql.ast.DropStatement dropStmt = (com.sdchat.ogsql.ast.DropStatement) stmt;
        assertEquals("test_proc", dropStmt.getObjectName());
        assertTrue(dropStmt.isIfExists());
    }

    @Test
    public void testDropProcedureCascade() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "DROP PROCEDURE test_proc CASCADE;";

        SQLStatement stmt = parser.parse(sql);

        assertNotNull(stmt, "Statement should not be null");
        assertTrue(stmt instanceof com.sdchat.ogsql.ast.DropStatement, "Statement should be DropStatement");

        com.sdchat.ogsql.ast.DropStatement dropStmt = (com.sdchat.ogsql.ast.DropStatement) stmt;
        assertEquals("test_proc", dropStmt.getObjectName());
        assertTrue(dropStmt.isCascade());
    }
}

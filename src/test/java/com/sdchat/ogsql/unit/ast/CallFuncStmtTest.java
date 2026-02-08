package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.CallFuncStmt;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.ValueExpression;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.visitor.ASTVisitor;
import com.sdchat.ogsql.ast.ExplainStatement;
import com.sdchat.ogsql.ast.ShowStatement;
import com.sdchat.ogsql.ast.BeginStatement;
import com.sdchat.ogsql.ast.CommitStatement;
import com.sdchat.ogsql.ast.RollbackStatement;
import com.sdchat.ogsql.ast.AnalyzeStatement;
import com.sdchat.ogsql.ast.SetStatement;
import com.sdchat.ogsql.ast.CreateSchemaStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CallFuncStmt class.
 * 
 * Tests cover constructor, getters/setters, argument handling (positional and named),
 * return value flag, and visitor pattern acceptance.
 */
public class CallFuncStmtTest {

    private CallFuncStmt stmt;
    private ValueExpression mockExpr1;
    private ValueExpression mockExpr2;

    @BeforeEach
    void setUp() {
        stmt = new CallFuncStmt("test_procedure");
        mockExpr1 = createMockExpression("value1");
        mockExpr2 = createMockExpression("value2");
    }

    private ValueExpression createMockExpression(String value) {
        ValueExpression expr = new ValueExpression();
        expr.setLiteralValue(value);
        return expr;
    }

    @Test
    @DisplayName("Constructor with procedure name should initialize all fields")
    void testConstructorWithProcedureName() {
        CallFuncStmt testStmt = new CallFuncStmt("my_procedure");
        
        assertEquals("my_procedure", testStmt.getProcedureName());
        assertNotNull(testStmt.getArguments());
        assertTrue(testStmt.getArguments().isEmpty());
        assertNotNull(testStmt.getNamedArguments());
        assertTrue(testStmt.getNamedArguments().isEmpty());
        assertFalse(testStmt.hasReturnValue());
    }

    @Test
    @DisplayName("Adding positional arguments should work correctly")
    void testAddPositionalArguments() {
        stmt.addArgument(mockExpr1);
        stmt.addArgument(mockExpr2);
        
        assertEquals(2, stmt.getArguments().size());
        assertEquals(mockExpr1, stmt.getArguments().get(0));
        assertEquals(mockExpr2, stmt.getArguments().get(1));
    }

    @Test
    @DisplayName("Adding named arguments should work correctly")
    void testAddNamedArguments() {
        stmt.addNamedArgument("param1", mockExpr1);
        stmt.addNamedArgument("param2", mockExpr2);
        
        assertEquals(2, stmt.getNamedArguments().size());
        assertTrue(stmt.getNamedArguments().containsKey("param1"));
        assertTrue(stmt.getNamedArguments().containsKey("param2"));
        assertEquals(mockExpr1, stmt.getNamedArguments().get("param1"));
        assertEquals(mockExpr2, stmt.getNamedArguments().get("param2"));
    }

    @Test
    @DisplayName("Setting all positional arguments should replace existing")
    void testSetAllPositionalArguments() {
        stmt.addArgument(mockExpr1);
        
        List<ValueExpression> newArgs = new ArrayList<>();
        newArgs.add(mockExpr2);
        stmt.setArguments(newArgs);
        
        assertEquals(1, stmt.getArguments().size());
        assertEquals(mockExpr2, stmt.getArguments().get(0));
    }

    @Test
    @DisplayName("Setting all named arguments should replace existing")
    void testSetAllNamedArguments() {
        stmt.addNamedArgument("param1", mockExpr1);
        
        Map<String, ValueExpression> newArgs = new HashMap<>();
        newArgs.put("param2", mockExpr2);
        stmt.setNamedArguments(newArgs);
        
        assertEquals(1, stmt.getNamedArguments().size());
        assertTrue(stmt.getNamedArguments().containsKey("param2"));
        assertEquals(mockExpr2, stmt.getNamedArguments().get("param2"));
    }

    @Test
    @DisplayName("Setting procedure name should update correctly")
    void testSetProcedureName() {
        stmt.setProcedureName("new_procedure");
        
        assertEquals("new_procedure", stmt.getProcedureName());
    }

    @Test
    @DisplayName("Setting hasReturnValue flag should work correctly")
    void testSetHasReturnValue() {
        assertFalse(stmt.hasReturnValue());
        
        stmt.setHasReturnValue(true);
        assertTrue(stmt.hasReturnValue());
        
        stmt.setHasReturnValue(false);
        assertFalse(stmt.hasReturnValue());
    }

    @Test
    @DisplayName("Setting null arguments list should result in empty list")
    void testSetNullArguments() {
        stmt.setArguments(null);
        
        assertNotNull(stmt.getArguments());
        assertTrue(stmt.getArguments().isEmpty());
    }

    @Test
    @DisplayName("Setting null named arguments map should result in empty map")
    void testSetNullNamedArguments() {
        stmt.setNamedArguments(null);
        
        assertNotNull(stmt.getNamedArguments());
        assertTrue(stmt.getNamedArguments().isEmpty());
    }

    @Test
    @DisplayName("Should accept visitor pattern")
    void testAcceptVisitor() {
        MockVisitor visitor = new MockVisitor();
        stmt.accept(visitor);
        
        assertTrue(visitor.callFuncStmtVisited);
    }

    @Test
    @DisplayName("Should have correct statement type")
    void testStatementType() {
        assertEquals(StatementType.CALL_PROCEDURE,
                     stmt.getStatementType());
    }

    @Test
    @DisplayName("Multiple positional arguments should maintain order")
    void testMultiplePositionalArgumentsOrder() {
        ValueExpression expr3 = createMockExpression("value3");
        stmt.addArgument(mockExpr1);
        stmt.addArgument(mockExpr2);
        stmt.addArgument(expr3);
        
        assertEquals(3, stmt.getArguments().size());
        assertEquals(mockExpr1, stmt.getArguments().get(0));
        assertEquals(mockExpr2, stmt.getArguments().get(1));
        assertEquals(expr3, stmt.getArguments().get(2));
    }

    @Test
    @DisplayName("Multiple named arguments should all be accessible")
    void testMultipleNamedArguments() {
        ValueExpression expr3 = createMockExpression("value3");
        stmt.addNamedArgument("p1", mockExpr1);
        stmt.addNamedArgument("p2", mockExpr2);
        stmt.addNamedArgument("p3", expr3);
        
        assertEquals(3, stmt.getNamedArguments().size());
        assertTrue(stmt.getNamedArguments().containsKey("p1"));
        assertTrue(stmt.getNamedArguments().containsKey("p2"));
        assertTrue(stmt.getNamedArguments().containsKey("p3"));
    }

    @Test
    @DisplayName("Setting procedure name to null should not throw")
    void testSetNullProcedureName() {
        assertDoesNotThrow(() -> stmt.setProcedureName(null));
        assertNull(stmt.getProcedureName());
    }

    /**
     * Mock visitor for testing visitor pattern acceptance
     */
    private static class MockVisitor implements ASTVisitor<Void> {
        boolean callFuncStmtVisited = false;

        @Override
        public Void visitCallFuncStmt(CallFuncStmt stmt) {
            callFuncStmtVisited = true;
            return null;
        }

        @Override
        public Void visitCreateProcedureStmt(com.sdchat.ogsql.ast.CreateProcedureStmt stmt) {
            return null;
        }

        @Override
        public Void visitAlterProcedureStmt(com.sdchat.ogsql.ast.AlterProcedureStmt stmt) {
            return null;
        }

        @Override
        public Void visitSelectQuery(com.sdchat.ogsql.ast.SelectQuery query) {
            return null;
        }

        @Override
        public Void visitInsertStatement(com.sdchat.ogsql.ast.InsertStatement stmt) {
            return null;
        }

        @Override
        public Void visitUpdateStatement(com.sdchat.ogsql.ast.UpdateStatement stmt) {
            return null;
        }

        @Override
        public Void visitDeleteStatement(com.sdchat.ogsql.ast.DeleteStatement stmt) {
            return null;
        }

        @Override
        public Void visitAlterStatement(com.sdchat.ogsql.ast.AlterStatement stmt) {
            return null;
        }

        @Override
        public Void visitDropStatement(com.sdchat.ogsql.ast.DropStatement stmt) {
            return null;
        }

        @Override
        public Void visitCreateStatement(com.sdchat.ogsql.ast.CreateStatement stmt) {
            return null;
        }

        @Override
        public Void visitPartitioningInformation(com.sdchat.ogsql.ast.PartitioningInformation partitioning) {
            return null;
        }

        @Override
        public Void visitPartitionDefinition(com.sdchat.ogsql.ast.PartitionDefinition partition) {
            return null;
        }

        @Override
        public Void visitExternalTable(com.sdchat.ogsql.ast.ExternalTable externalTable) {
            return null;
        }

        @Override
        public Void visitExplainStatement(ExplainStatement statement) {
            return null;
        }

        @Override
        public Void visitShowStatement(ShowStatement statement) {
            return null;
        }

        @Override
        public Void visitBeginStatement(BeginStatement statement) {
            return null;
        }

        @Override
        public Void visitCommitStatement(CommitStatement statement) {
            return null;
        }

        @Override
        public Void visitRollbackStatement(RollbackStatement statement) {
            return null;
        }

        @Override
        public Void visitAnalyzeStatement(AnalyzeStatement statement) {
            return null;
        }

        @Override
        public Void visitSetStatement(SetStatement statement) {
            return null;
        }

        @Override
        public Void visitCreateSchemaStatement(CreateSchemaStatement statement) {
            return null;
        }
    }
}

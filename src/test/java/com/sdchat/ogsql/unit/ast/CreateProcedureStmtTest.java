package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.*;
import com.sdchat.ogsql.visitor.ASTVisitor;
import com.sdchat.ogsql.visitor.ASTVisitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateProcedureStmt AST node class.
 * Tests constructors, getters, setters, and visitor pattern acceptance.
 */
@DisplayName("CreateProcedureStmt Tests")
class CreateProcedureStmtTest {

    @Test
    @DisplayName("Constructor with name should set properties correctly")
    void testConstructorWithName() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");

        assertEquals("test_proc", stmt.getProcedureName());
        assertFalse(stmt.isOrReplace());
        assertNotNull(stmt.getParameters());
        assertTrue(stmt.getParameters().isEmpty());
    }

    @Test
    @DisplayName("Constructor with OR REPLACE should set properties correctly")
    void testConstructorWithOrReplace() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc", true);

        assertEquals("test_proc", stmt.getProcedureName());
        assertTrue(stmt.isOrReplace());
    }

    @Test
    @DisplayName("Set procedure name should update property")
    void testSetProcedureName() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        stmt.setProcedureName("new_proc");

        assertEquals("new_proc", stmt.getProcedureName());
    }

    @Test
    @DisplayName("Add parameter should add to parameters list")
    void testAddParameter() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        ProcedureParameter param = new ProcedureParameter("p1", ProcedureParameter.ParameterMode.IN, "INTEGER", null, 0);

        stmt.addParameter(param);

        assertEquals(1, stmt.getParameters().size());
        assertEquals("p1", stmt.getParameters().get(0).getName());
    }

    @Test
    @DisplayName("Add multiple parameters should maintain order")
    void testAddMultipleParameters() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        ProcedureParameter p1 = new ProcedureParameter("p1", ProcedureParameter.ParameterMode.IN, "INTEGER", null, 0);
        ProcedureParameter p2 = new ProcedureParameter("p2", ProcedureParameter.ParameterMode.OUT, "VARCHAR", null, 1);

        stmt.addParameter(p1);
        stmt.addParameter(p2);

        assertEquals(2, stmt.getParameters().size());
        assertEquals("p1", stmt.getParameters().get(0).getName());
        assertEquals("p2", stmt.getParameters().get(1).getName());
    }

    @Test
    @DisplayName("Set parameters should replace existing list")
    void testSetParameters() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        java.util.List<ProcedureParameter> params = new java.util.ArrayList<>();
        params.add(new ProcedureParameter("p1", ProcedureParameter.ParameterMode.IN, "INTEGER", null, 0));
        params.add(new ProcedureParameter("p2", ProcedureParameter.ParameterMode.OUT, "VARCHAR", null, 1));

        stmt.setParameters(params);

        assertEquals(2, stmt.getParameters().size());
    }

    @Test
    @DisplayName("Set body should update property")
    void testSetBody() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");

        stmt.setBody(body);

        assertEquals("plpgsql", body.getLanguage());
        assertEquals(body, stmt.getBody());
    }

    @Test
    @DisplayName("Set security should update property")
    void testSetSecurity() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        ProcedureSecurity security = new ProcedureSecurity();

        stmt.setSecurity(security);

        assertEquals(security, stmt.getSecurity());
    }

    @Test
    @DisplayName("Set language should update property")
    void testSetLanguage() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");

        stmt.setLanguage("plpgsql");

        assertEquals("plpgsql", stmt.getLanguage());
    }

    @Test
    @DisplayName("Set compatibility mode should update property")
    void testSetCompatibilityMode() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");

        stmt.setCompatibilityMode("A");

        assertEquals("A", stmt.getCompatibilityMode());
    }

    @Test
    @DisplayName("Visitor accept should call visitor method")
    void testVisitorAccept() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        MockVisitor visitor = new MockVisitor();

        Object result = stmt.accept(visitor);

        assertTrue(visitor.createProcedureVisited);
    }

    // Mock visitor for testing visitor pattern
    private static class MockVisitor implements ASTVisitor<Object> {
        boolean createProcedureVisited = false;

        @Override
        public Object visitSelectQuery(SelectQuery query) {
            return null;
        }

        @Override
        public Object visitCreateStatement(CreateStatement statement) {
            return null;
        }

        @Override
        public Object visitCreateProcedureStmt(CreateProcedureStmt statement) {
            createProcedureVisited = true;
            return null;
        }

        @Override
        public Object visitInsertStatement(InsertStatement statement) {
            return null;
        }

        @Override
        public Object visitUpdateStatement(UpdateStatement statement) {
            return null;
        }

        @Override
        public Object visitDeleteStatement(DeleteStatement statement) {
            return null;
        }

        @Override
        public Object visitAlterStatement(AlterStatement statement) {
            return null;
        }

        @Override
        public Object visitAlterProcedureStmt(AlterProcedureStmt statement) {
            return null;
        }

        @Override
        public Object visitDropStatement(DropStatement statement) {
            return null;
        }

        @Override
        public Object visitCallFuncStmt(CallFuncStmt statement) {
            return null;
        }

        @Override
        public Object visitPartitioningInformation(PartitioningInformation partitioning) {
            return null;
        }

        @Override
        public Object visitPartitionDefinition(PartitionDefinition partition) {
            return null;
        }

        @Override
        public Object visitExternalTable(ExternalTable externalTable) {
            return null;
        }

        @Override
        public Object visitExplainStatement(ExplainStatement statement) {
            return null;
        }

        @Override
        public Object visitShowStatement(ShowStatement statement) {
            return null;
        }

        @Override
        public Object visitBeginStatement(BeginStatement statement) {
            return null;
        }

        @Override
        public Object visitCommitStatement(CommitStatement statement) {
            return null;
        }

        @Override
        public Object visitRollbackStatement(RollbackStatement statement) {
            return null;
        }

        @Override
        public Object visitAnalyzeStatement(AnalyzeStatement statement) {
            return null;
        }

        @Override
        public Object visitSetStatement(SetStatement statement) {
            return null;
        }

        @Override
        public Object visitCreateSchemaStatement(CreateSchemaStatement statement) {
            return null;
        }

        @Override
        public Object visitSavepointStatement(SavepointStatement statement) {
            return null;
        }

        @Override
        public Object visitReleaseSavepointStatement(ReleaseSavepointStatement statement) {
            return null;
        }

        @Override
        public Object visitRollbackToSavepointStatement(RollbackToSavepointStatement statement) {
            return null;
        }

        @Override
        public Object visitOnConflictClause(OnConflictClause clause) {
            return null;
        }

        @Override
        public Object visitReturningClause(ReturningClause clause) {
            return null;
        }

        @Override
        public Object visitReturningExpression(ReturningExpression expression) {
            return null;
        }
    }
}

package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import com.sdchat.ogsql.ast.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ASTVisitor interface.
 */
class ASTVisitorTest {

    @Test
    void testASTVisitorIsInterface() {
        assertTrue(ASTVisitor.class.isInterface());
    }

    @Test
    void testASTVisitorIsGeneric() {
        ASTVisitor<?> visitor = new ASTVisitor<String>() {
            @Override
            public String visit(SelectQuery query) { return null; }
            @Override
            public String visit(CreateStatement statement) { return null; }
            @Override
            public String visit(InsertStatement statement) { return null; }
            @Override
            public String visit(UpdateStatement statement) { return null; }
            @Override
            public String visit(DeleteStatement statement) { return null; }
            @Override
            public String visit(AlterStatement statement) { return null; }
            @Override
            public String visit(DropStatement statement) { return null; }
            @Override
            public String visit(PartitioningInformation partitioning) { return null; }
            @Override
            public String visit(PartitionDefinition partition) { return null; }
            @Override
            public String visit(ExternalTable externalTable) { return null; }
        };
        assertNotNull(visitor);
    }

    @Test
    void testASTVisitorHasVisitMethodForSelectQuery() throws NoSuchMethodException {
        assertNotNull(ASTVisitor.class.getMethod("visit", SelectQuery.class));
    }

    @Test
    void testASTVisitorHasVisitMethodForCreateStatement() throws NoSuchMethodException {
        assertNotNull(ASTVisitor.class.getMethod("visit", CreateStatement.class));
    }

    @Test
    void testASTVisitorHasVisitMethodForInsertStatement() throws NoSuchMethodException {
        assertNotNull(ASTVisitor.class.getMethod("visit", InsertStatement.class));
    }

    @Test
    void testASTVisitorHasVisitMethodForUpdateStatement() throws NoSuchMethodException {
        assertNotNull(ASTVisitor.class.getMethod("visit", UpdateStatement.class));
    }

    @Test
    void testASTVisitorHasVisitMethodForDeleteStatement() throws NoSuchMethodException {
        assertNotNull(ASTVisitor.class.getMethod("visit", DeleteStatement.class));
    }

    @Test
    void testASTVisitorHasVisitMethodForAlterStatement() throws NoSuchMethodException {
        assertNotNull(ASTVisitor.class.getMethod("visit", AlterStatement.class));
    }

    @Test
    void testASTVisitorHasVisitMethodForDropStatement() throws NoSuchMethodException {
        assertNotNull(ASTVisitor.class.getMethod("visit", DropStatement.class));
    }
}
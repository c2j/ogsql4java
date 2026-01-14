package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.InsertStatement;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.visitor.ASTVisitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for InsertStatement")
class InsertStatementTest {

    @Test
    @DisplayName("InsertStatement should have INSERT statement type")
    void testStatementType() {
        InsertStatement statement = new InsertStatement();
        assertEquals(StatementType.INSERT, statement.getStatementType());
    }

    @Test
    @DisplayName("InsertStatement should accept visitor")
    void testAcceptVisitor() {
        InsertStatement statement = new InsertStatement();
        ASTVisitor<String> visitor = new ASTVisitor<String>() {
            @Override
            public String visit(com.sdchat.ogsql.ast.SelectQuery query) {
                return null;
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.CreateStatement statement) {
                return null;
            }

            @Override
            public String visit(InsertStatement statement) {
                return "visited";
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.UpdateStatement statement) {
                return null;
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.DeleteStatement statement) {
                return null;
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.AlterStatement statement) {
                return null;
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.DropStatement statement) {
                return null;
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.PartitioningInformation partitioning) {
                return null;
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.PartitionDefinition partition) {
                return null;
            }

            @Override
            public String visit(com.sdchat.ogsql.ast.ExternalTable externalTable) {
                return null;
            }
        };
        assertEquals("visited", statement.accept(visitor));
    }
}

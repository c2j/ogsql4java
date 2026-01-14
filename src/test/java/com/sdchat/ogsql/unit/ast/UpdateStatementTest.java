package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.UpdateStatement;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.visitor.ASTVisitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for UpdateStatement")
class UpdateStatementTest {

    @Test
    @DisplayName("UpdateStatement should have UPDATE statement type")
    void testStatementType() {
        UpdateStatement statement = new UpdateStatement();
        assertEquals(StatementType.UPDATE, statement.getStatementType());
    }

    @Test
    @DisplayName("UpdateStatement should accept visitor")
    void testAcceptVisitor() {
        UpdateStatement statement = new UpdateStatement();
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
            public String visit(com.sdchat.ogsql.ast.InsertStatement statement) {
                return null;
            }

            @Override
            public String visit(UpdateStatement statement) {
                return "visited";
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

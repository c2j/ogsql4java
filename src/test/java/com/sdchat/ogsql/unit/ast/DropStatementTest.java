package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.DropStatement;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.visitor.ASTVisitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for DropStatement")
class DropStatementTest {

    @Test
    @DisplayName("DropStatement should have DROP_TABLE statement type")
    void testStatementType() {
        DropStatement statement = new DropStatement();
        assertEquals(StatementType.DROP_TABLE, statement.getStatementType());
    }

    @Test
    @DisplayName("DropStatement should accept visitor")
    void testAcceptVisitor() {
        DropStatement statement = new DropStatement();
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
            public String visit(DropStatement statement) {
                return "visited";
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

package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.visitor.ASTVisitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for SelectQuery")
class SelectQueryTest {

    @Test
    @DisplayName("SelectQuery should have SELECT statement type")
    void testStatementType() {
        SelectQuery query = new SelectQuery();
        assertEquals(StatementType.SELECT, query.getStatementType());
    }

    @Test
    @DisplayName("SelectQuery should accept visitor")
    void testAcceptVisitor() {
        SelectQuery query = new SelectQuery();
        ASTVisitor<String> visitor = new ASTVisitor<String>() {
            @Override
            public String visit(SelectQuery query) {
                return "visited";
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
        assertEquals("visited", query.accept(visitor));
    }

    @Test
    @DisplayName("SelectQuery should set and get fromClause")
    void testFromClause() {
        SelectQuery query = new SelectQuery();
        query.setFromClause("users");
        assertEquals("users", query.getFromClause());
    }
}

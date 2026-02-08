package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a CREATE SCHEMA statement.
 * Example: CREATE SCHEMA my_schema;
 */
public class CreateSchemaStatement implements SQLStatement {

    private String schemaName;

    public CreateSchemaStatement() {
    }

    public CreateSchemaStatement(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitCreateSchemaStatement(this);
    }
}

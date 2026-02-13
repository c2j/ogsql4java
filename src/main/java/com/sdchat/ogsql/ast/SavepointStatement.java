package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a SAVEPOINT statement.
 * Example: SAVEPOINT my_savepoint;
 */
public class SavepointStatement implements SQLStatement {

    private String identifier;

    public SavepointStatement() {
    }

    public SavepointStatement(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SAVEPOINT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitSavepointStatement(this);
    }
}

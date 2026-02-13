package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a RELEASE SAVEPOINT statement.
 * Example: RELEASE SAVEPOINT my_savepoint;
 * Example: RELEASE my_savepoint;
 */
public class ReleaseSavepointStatement implements SQLStatement {

    private String identifier;

    public ReleaseSavepointStatement() {
    }

    public ReleaseSavepointStatement(String identifier) {
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
        return StatementType.RELEASE_SAVEPOINT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitReleaseSavepointStatement(this);
    }
}

package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a ROLLBACK TO SAVEPOINT statement.
 * Example: ROLLBACK TO SAVEPOINT my_savepoint;
 * Example: ROLLBACK WORK TO my_savepoint;
 */
public class RollbackToSavepointStatement implements SQLStatement {

    private String identifier;

    public RollbackToSavepointStatement() {
    }

    public RollbackToSavepointStatement(String identifier) {
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
        return StatementType.ROLLBACK_TO_SAVEPOINT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitRollbackToSavepointStatement(this);
    }
}

package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a ROLLBACK statement (without TO SAVEPOINT).
 * Example: ROLLBACK;
 * Example: ROLLBACK WORK;
 */
public class RollbackStatement implements SQLStatement {

    public RollbackStatement() {
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.ROLLBACK;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitRollbackStatement(this);
    }
}

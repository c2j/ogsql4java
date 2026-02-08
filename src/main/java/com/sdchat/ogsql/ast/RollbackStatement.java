package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a ROLLBACK statement.
 * Example: ROLLBACK;
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

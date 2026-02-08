package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a COMMIT statement.
 * Example: COMMIT;
 */
public class CommitStatement implements SQLStatement {

    public CommitStatement() {
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.COMMIT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitCommitStatement(this);
    }
}

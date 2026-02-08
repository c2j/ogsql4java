package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a BEGIN statement.
 * Example: BEGIN;
 */
public class BeginStatement implements SQLStatement {

    public BeginStatement() {
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.BEGIN;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBeginStatement(this);
    }
}

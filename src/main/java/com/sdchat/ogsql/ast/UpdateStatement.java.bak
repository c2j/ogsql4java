package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

public class UpdateStatement implements SQLStatement {

    @Override
    public StatementType getStatementType() {
        return StatementType.UPDATE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
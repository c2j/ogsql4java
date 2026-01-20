package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

public class InsertStatement implements SQLStatement {

    @Override
    public StatementType getStatementType() {
        return StatementType.INSERT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
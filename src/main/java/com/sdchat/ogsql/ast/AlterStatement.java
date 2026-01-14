package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

public class AlterStatement implements SQLStatement {

    @Override
    public StatementType getStatementType() {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
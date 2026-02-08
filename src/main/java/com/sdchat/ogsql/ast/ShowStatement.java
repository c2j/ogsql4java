package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a SHOW statement.
 * Example: SHOW TABLES;
 */
public class ShowStatement implements SQLStatement {

    private String objectName;

    public ShowStatement() {
    }

    public ShowStatement(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SHOW;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitShowStatement(this);
    }
}

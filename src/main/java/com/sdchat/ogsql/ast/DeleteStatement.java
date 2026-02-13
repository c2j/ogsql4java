package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

public class DeleteStatement implements SQLStatement {

    private String tableName;
    private ValueExpression whereClause;
    private ReturningClause returningClause;

    public DeleteStatement() {
    }

    public DeleteStatement(String tableName) {
        this.tableName = tableName;
    }

    public DeleteStatement(String tableName, ValueExpression whereClause) {
        this.tableName = tableName;
        this.whereClause = whereClause;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ValueExpression getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(ValueExpression whereClause) {
        this.whereClause = whereClause;
    }

    public ReturningClause getReturningClause() {
        return returningClause;
    }

    public void setReturningClause(ReturningClause returningClause) {
        this.returningClause = returningClause;
    }

    public boolean hasReturningClause() {
        return returningClause != null;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.DELETE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitDeleteStatement(this);
    }
}
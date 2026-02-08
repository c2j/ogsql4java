package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents an ANALYZE statement.
 * Example: ANALYZE table_name;
 */
public class AnalyzeStatement implements SQLStatement {

    private String tableName;

    public AnalyzeStatement() {
    }

    public AnalyzeStatement(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.ANALYZE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitAnalyzeStatement(this);
    }
}

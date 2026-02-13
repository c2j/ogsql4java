package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.List;
import java.util.Collections;

public class InsertStatement implements SQLStatement {

    private String tableName;
    private List<String> columns;
    private List<ValueExpression> values;
    private OnConflictClause onConflictClause;
    private ReturningClause returningClause;

    public InsertStatement() {
    }

    public InsertStatement(String tableName) {
        this.tableName = tableName;
    }

    public InsertStatement(String tableName, List<String> columns, List<ValueExpression> values) {
        this.tableName = tableName;
        this.columns = columns;
        this.values = values;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns == null ? Collections.emptyList() : Collections.unmodifiableList(columns);
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<ValueExpression> getValues() {
        return values == null ? Collections.emptyList() : Collections.unmodifiableList(values);
    }

    public void setValues(List<ValueExpression> values) {
        this.values = values;
    }

    public OnConflictClause getOnConflictClause() {
        return onConflictClause;
    }

    public void setOnConflictClause(OnConflictClause onConflictClause) {
        this.onConflictClause = onConflictClause;
    }

    public boolean hasOnConflictClause() {
        return onConflictClause != null;
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
        return StatementType.INSERT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitInsertStatement(this);
    }
}
package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class UpdateStatement implements SQLStatement {

    private String tableName;
    private Map<String, ValueExpression> setClauses;
    private ValueExpression whereClause;
    private ReturningClause returningClause;

    public UpdateStatement() {
        this.setClauses = new HashMap<>();
    }

    public UpdateStatement(String tableName) {
        this();
        this.tableName = tableName;
    }

    public UpdateStatement(String tableName, Map<String, ValueExpression> setClauses, ValueExpression whereClause) {
        this.tableName = tableName;
        this.setClauses = setClauses != null ? new HashMap<>(setClauses) : new HashMap<>();
        this.whereClause = whereClause;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, ValueExpression> getSetClauses() {
        return Collections.unmodifiableMap(setClauses);
    }

    public void addSetClause(String column, ValueExpression value) {
        this.setClauses.put(column, value);
    }

    public void setSetClauses(Map<String, ValueExpression> setClauses) {
        this.setClauses = setClauses != null ? new HashMap<>(setClauses) : new HashMap<>();
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
        return StatementType.UPDATE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitUpdateStatement(this);
    }
}
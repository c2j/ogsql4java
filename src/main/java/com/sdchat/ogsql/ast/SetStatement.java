package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents a SET statement.
 * Example: SET enable_seqscan = OFF;
 */
public class SetStatement implements SQLStatement {

    private String parameterName;
    private ValueExpression value;

    public SetStatement() {
    }

    public SetStatement(String parameterName, ValueExpression value) {
        this.parameterName = parameterName;
        this.value = value;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public ValueExpression getValue() {
        return value;
    }

    public void setValue(ValueExpression value) {
        this.value = value;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SET;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitSetStatement(this);
    }
}

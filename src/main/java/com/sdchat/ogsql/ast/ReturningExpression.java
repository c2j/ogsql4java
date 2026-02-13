package com.sdchat.ogsql.ast;

/**
 * Represents a RETURNING expression with optional alias.
 */
public class ReturningExpression {
    private String expression;
    private String alias;

    public ReturningExpression() {
    }

    public ReturningExpression(String expression) {
        this.expression = expression;
    }

    public ReturningExpression(String expression, String alias) {
        this.expression = expression;
        this.alias = alias;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean hasAlias() {
        return alias != null && !alias.isEmpty();
    }
}

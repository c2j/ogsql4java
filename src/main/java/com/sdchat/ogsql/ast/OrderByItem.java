package com.sdchat.ogsql.ast;

/**
 * Represents an ORDER BY clause item with sort direction.
 */
public class OrderByItem {
    private String expression;
    private boolean ascending = true;

    public OrderByItem() {
    }

    public OrderByItem(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}

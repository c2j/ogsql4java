package com.sdchat.ogsql.ast;

/**
 * Represents a table, view, or subquery in FROM clause.
 */
public class DataSource {
    private String name;
    private String alias;
    private String joinType;
    private String joinCondition;

    public DataSource() {
    }

    public DataSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(String joinCondition) {
        this.joinCondition = joinCondition;
    }

    public boolean hasJoin() {
        return joinType != null && !joinType.trim().isEmpty();
    }

    public boolean hasAlias() {
        return alias != null && !alias.trim().isEmpty();
    }
}

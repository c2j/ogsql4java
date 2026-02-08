package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Represents an EXPLAIN statement.
 * Example: EXPLAIN (costs off) SELECT * FROM table;
 */
public class ExplainStatement implements SQLStatement {

    private SelectQuery query;
    private boolean verbose;
    private boolean costs;

    public ExplainStatement() {
    }

    public ExplainStatement(SelectQuery query) {
        this.query = query;
    }

    public SelectQuery getQuery() {
        return query;
    }

    public void setQuery(SelectQuery query) {
        this.query = query;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isCosts() {
        return costs;
    }

    public void setCosts(boolean costs) {
        this.costs = costs;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.EXPLAIN;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitExplainStatement(this);
    }
}

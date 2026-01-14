package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SelectQuery implements SQLStatement {
    private String fromClause;
    private List<PerformanceHint> hints;

    public SelectQuery() {
        this.hints = new ArrayList<>();
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public String getFromClause() {
        return fromClause;
    }

    public List<PerformanceHint> getHints() {
        return hints;
    }

    public void setHints(List<PerformanceHint> hints) {
        this.hints = hints != null ? new ArrayList<>(hints) : new ArrayList<>();
    }

    public void addHint(PerformanceHint hint) {
        if (hint != null) {
            this.hints.add(hint);
        }
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SELECT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
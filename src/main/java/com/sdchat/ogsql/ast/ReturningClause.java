package com.sdchat.ogsql.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a RETURNING clause in INSERT/UPDATE/DELETE statements.
 */
public class ReturningClause {
    private List<ReturningExpression> expressions;

    public ReturningClause() {
        this.expressions = new ArrayList<>();
    }

    public List<ReturningExpression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<ReturningExpression> expressions) {
        this.expressions = expressions != null ? new ArrayList<>(expressions) : new ArrayList<>();
    }

    public void addExpression(ReturningExpression expression) {
        this.expressions.add(expression);
    }

    public boolean isEmpty() {
        return expressions == null || expressions.isEmpty();
    }
}

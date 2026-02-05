package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectQuery implements SQLStatement {
    private String fromClause;
    private List<DataSource> dataSources;
    private List<PerformanceHint> hints;
    private int withClauseCount = 0;
    private int nestedWithCount = 0;

    public SelectQuery() {
        this.hints = new ArrayList<>();
        this.dataSources = new ArrayList<>();
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public String getFromClause() {
        return fromClause;
    }

    public List<DataSource> getDataSources() {
        return Collections.unmodifiableList(dataSources);
    }

    public boolean hasDataSources() {
        return !dataSources.isEmpty();
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources != null ? new ArrayList<>(dataSources) : new ArrayList<>();
    }

    public void addDataSource(DataSource dataSource) {
        if (dataSource != null) {
            this.dataSources.add(dataSource);
        }
    }

    public List<PerformanceHint> getHints() {
        return Collections.unmodifiableList(hints);
    }

    public boolean hasHints() {
        return !hints.isEmpty();
    }

    public void setHints(List<PerformanceHint> hints) {
        this.hints = hints != null ? new ArrayList<>(hints) : new ArrayList<>();
    }

    public void addHint(PerformanceHint hint) {
        if (hint != null) {
            this.hints.add(hint);
        }
    }

    public int getWithClauseCount() {
        return withClauseCount;
    }

    public void setWithClauseCount(int withClauseCount) {
        this.withClauseCount = withClauseCount;
    }

    public int getNestedWithCount() {
        return nestedWithCount;
    }

    public void setNestedWithCount(int nestedWithCount) {
        this.nestedWithCount = nestedWithCount;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.SELECT;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitSelectQuery(this);
    }
}
package com.sdchat.ogsql.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a relationship edge between two tables.
 * Contains join type and join conditions.
 */
public class RelationshipEdge {
    private final TableNode source;
    private final TableNode target;
    private final JoinType joinType;
    private final List<JoinCondition> conditions;

    public RelationshipEdge(TableNode source, TableNode target, JoinType joinType) {
        this.source = Objects.requireNonNull(source, "Source table cannot be null");
        this.target = Objects.requireNonNull(target, "Target table cannot be null");
        this.joinType = Objects.requireNonNull(joinType, "Join type cannot be null");
        this.conditions = new ArrayList<>();
    }

    public TableNode getSource() {
        return source;
    }

    public TableNode getTarget() {
        return target;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public List<JoinCondition> getConditions() {
        return new ArrayList<>(conditions);
    }

    public void addCondition(JoinCondition condition) {
        conditions.add(Objects.requireNonNull(condition, "Condition cannot be null"));
    }

    public void addCondition(String leftColumn, String rightColumn) {
        conditions.add(new JoinCondition(leftColumn, rightColumn));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationshipEdge that = (RelationshipEdge) o;
        return source.equals(that.source) && 
               target.equals(that.target) && 
               joinType == that.joinType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, joinType);
    }

    @Override
    public String toString() {
        return "RelationshipEdge{" +
                "source=" + source.getName() +
                ", target=" + target.getName() +
                ", joinType=" + joinType +
                ", conditions=" + conditions.size() +
                '}';
    }
}

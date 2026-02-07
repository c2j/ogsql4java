package com.sdchat.ogsql.graph;

import java.util.Objects;

/**
 * Represents a join condition between two columns.
 */
public class JoinCondition {
    private final String leftColumn;
    private final String rightColumn;

    public JoinCondition(String leftColumn, String rightColumn) {
        this.leftColumn = Objects.requireNonNull(leftColumn, "Left column cannot be null");
        this.rightColumn = Objects.requireNonNull(rightColumn, "Right column cannot be null");
    }

    public String getLeftColumn() {
        return leftColumn;
    }

    public String getRightColumn() {
        return rightColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinCondition that = (JoinCondition) o;
        return leftColumn.equals(that.leftColumn) && 
               rightColumn.equals(that.rightColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftColumn, rightColumn);
    }

    @Override
    public String toString() {
        return leftColumn + " = " + rightColumn;
    }
}

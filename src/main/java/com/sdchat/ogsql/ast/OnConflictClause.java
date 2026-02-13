package com.sdchat.ogsql.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an ON CONFLICT clause in an INSERT statement.
 */
public class OnConflictClause {
    private ConflictTarget conflictTarget;
    private ConflictAction conflictAction;
    private Map<String, ValueExpression> updateAssignments;
    private ValueExpression whereClause;

    public OnConflictClause() {
        this.updateAssignments = new HashMap<>();
    }

    public ConflictTarget getConflictTarget() {
        return conflictTarget;
    }

    public void setConflictTarget(ConflictTarget conflictTarget) {
        this.conflictTarget = conflictTarget;
    }

    public ConflictAction getConflictAction() {
        return conflictAction;
    }

    public void setConflictAction(ConflictAction conflictAction) {
        this.conflictAction = conflictAction;
    }

    public Map<String, ValueExpression> getUpdateAssignments() {
        return updateAssignments;
    }

    public void setUpdateAssignments(Map<String, ValueExpression> updateAssignments) {
        this.updateAssignments = updateAssignments != null ? new HashMap<>(updateAssignments) : new HashMap<>();
    }

    public void addUpdateAssignment(String column, ValueExpression value) {
        this.updateAssignments.put(column, value);
    }

    public ValueExpression getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(ValueExpression whereClause) {
        this.whereClause = whereClause;
    }
}

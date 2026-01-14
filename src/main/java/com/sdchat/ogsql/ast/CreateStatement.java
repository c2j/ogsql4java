package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CREATE TABLE/INDEX/VIEW statement.
 */
public class CreateStatement implements SQLStatement {

    private String objectType = "TABLE";
    private String objectName;
    private List<Column> columns = new ArrayList<>();
    private List<Constraint> constraints = new ArrayList<>();
    private PartitioningInformation partitioning;

    @Override
    public StatementType getStatementType() {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Gets the object type (TABLE, INDEX, VIEW, etc.).
     * 
     * @return The object type
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the object type.
     * 
     * @param objectType The object type
     */
    public void setObjectType(String objectType) {
        if (objectType == null || objectType.trim().isEmpty()) {
            throw new IllegalArgumentException("Object type cannot be null or empty");
        }
        this.objectType = objectType.trim();
    }

    /**
     * Gets the object name.
     * 
     * @return The object name
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Sets the object name.
     * 
     * @param objectName The object name
     */
    public void setObjectName(String objectName) {
        if (objectName == null || objectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Object name cannot be null or empty");
        }
        this.objectName = objectName.trim();
    }

    /**
     * Gets the list of column definitions.
     * 
     * @return The list of columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Sets the list of column definitions.
     * 
     * @param columns The list of columns
     */
    public void setColumns(List<Column> columns) {
        if (columns == null) {
            throw new IllegalArgumentException("Columns cannot be null");
        }
        this.columns = new ArrayList<>(columns);
    }

    /**
     * Adds a column definition.
     * 
     * @param column The column to add
     */
    public void addColumn(Column column) {
        if (column == null) {
            throw new IllegalArgumentException("Column cannot be null");
        }
        this.columns.add(column);
    }

    /**
     * Gets the list of table constraints.
     * 
     * @return The list of constraints
     */
    public List<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * Sets the list of table constraints.
     * 
     * @param constraints The list of constraints
     */
    public void setConstraints(List<Constraint> constraints) {
        if (constraints == null) {
            throw new IllegalArgumentException("Constraints cannot be null");
        }
        this.constraints = new ArrayList<>(constraints);
    }

    /**
     * Adds a constraint.
     * 
     * @param constraint The constraint to add
     */
    public void addConstraint(Constraint constraint) {
        if (constraint == null) {
            throw new IllegalArgumentException("Constraint cannot be null");
        }
        this.constraints.add(constraint);
    }

    /**
     * Gets the partitioning information.
     * 
     * @return The partitioning information, or null if not partitioned
     */
    public PartitioningInformation getPartitioning() {
        return partitioning;
    }

    /**
     * Sets the partitioning information.
     * 
     * @param partitioning The partitioning information
     */
    public void setPartitioning(PartitioningInformation partitioning) {
        this.partitioning = partitioning;
    }

    /**
     * Checks if this table has partitioning.
     * 
     * @return true if partitioning is specified
     */
    public boolean hasPartitioning() {
        return partitioning != null;
    }
}
package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * AST node representing a DROP statement for database objects.
 * 
 * <p>This statement supports dropping various database objects including tables and procedures.
 * It provides options for conditional dropping (IF EXISTS) and cascade behavior.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * DROP TABLE IF EXISTS old_table;
 * DROP PROCEDURE test_proc CASCADE;
 * }</pre>
 * 
 * @see SQLStatement
 * @see ASTVisitor
 */
public class DropStatement implements SQLStatement {

    private String objectName;
    private boolean ifExists;
    private boolean cascade;

    /**
     * Constructs an empty DropStatement.
     */
    public DropStatement() {
    }

    /**
     * Constructs a DropStatement with the specified object name.
     * 
     * @param objectName the name of the object to drop
     */
    public DropStatement(String objectName) {
        this.objectName = objectName;
    }

    /**
     * Returns the name of the object to drop.
     * 
     * @return the object name
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Sets the name of the object to drop.
     * 
     * @param objectName the object name
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * Returns whether the IF EXISTS clause is specified.
     * 
     * @return true if IF EXISTS is set, false otherwise
     */
    public boolean isIfExists() {
        return ifExists;
    }

    /**
     * Sets whether the IF EXISTS clause is specified.
     * 
     * @param ifExists true to use IF EXISTS, false otherwise
     */
    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    /**
     * Returns whether the CASCADE option is specified.
     * 
     * @return true if CASCADE is set, false otherwise
     */
    public boolean isCascade() {
        return cascade;
    }

    /**
     * Sets whether the CASCADE option is specified.
     * 
     * @param cascade true to use CASCADE, false otherwise
     */
    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.DROP_TABLE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitDropStatement(this);
    }
}
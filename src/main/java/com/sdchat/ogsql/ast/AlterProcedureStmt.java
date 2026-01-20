package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * AST node representing an ALTER PROCEDURE statement.
 * 
 * <p>ALTER PROCEDURE modifies an existing procedure's attributes.
 * Exactly one modification action must be specified: RENAME, OWNER TO,
 * SET SCHEMA, SECURITY INVOKER, or DEPENDS ON.</p>
 * 
 * <p>Example:</p>
 * <pre>{@code
 * ALTER PROCEDURE old_name RENAME TO new_name;
 * ALTER PROCEDURE my_proc OWNER TO new_owner;
 * ALTER PROCEDURE my_proc SECURITY INVOKER;
 * }</pre>
 * 
 * @see SQLStatement
 * @see ASTVisitor
 */
public class AlterProcedureStmt implements SQLStatement {

    private String procedureName;
    private String newName;
    private String newOwner;
    private String newSchema;
    private Boolean securityInvoker;

    public AlterProcedureStmt(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(String newOwner) {
        this.newOwner = newOwner;
    }

    public String getNewSchema() {
        return newSchema;
    }

    public void setNewSchema(String newSchema) {
        this.newSchema = newSchema;
    }

    public Boolean getSecurityInvoker() {
        return securityInvoker;
    }

    public void setSecurityInvoker(Boolean securityInvoker) {
        this.securityInvoker = securityInvoker;
    }

    public boolean hasNewName() {
        return newName != null && !newName.trim().isEmpty();
    }

    public boolean hasNewOwner() {
        return newOwner != null && !newOwner.trim().isEmpty();
    }

    public boolean hasNewSchema() {
        return newSchema != null && !newSchema.trim().isEmpty();
    }

    public boolean isSecurityInvokerSet() {
        return securityInvoker != null;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.ALTER_PROCEDURE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitAlterProcedureStmt(this);
    }
}

package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AST node representing a CREATE PROCEDURE statement.
 * 
 * <p>CREATE PROCEDURE defines a new stored procedure with parameters,
 * security attributes, and a procedural body. The procedure can optionally
 * replace an existing procedure with OR REPLACE.</p>
 * 
 * <p>Example:</p>
 * <pre>{@code
 * CREATE OR REPLACE PROCEDURE add_user(p_name IN VARCHAR, p_age IN INTEGER)
 *     SECURITY DEFINER
 * AS $$
 * BEGIN
 *     INSERT INTO users(name, age) VALUES(p_name, p_age);
 * END;
 * $$ LANGUAGE plpgsql;
 * }</pre>
 * 
 * @see SQLStatement
 * @see ProcedureParameter
 * @see ProcedureBody
 * @see ProcedureSecurity
 * @see ASTVisitor
 */
public class CreateProcedureStmt implements SQLStatement {

    private String procedureName;
    private boolean orReplace;
    private List<ProcedureParameter> parameters;
    private ProcedureBody body;
    private ProcedureSecurity security;
    private String language;
    private String compatibilityMode;

    public CreateProcedureStmt(String procedureName) {
        this.procedureName = procedureName;
        this.orReplace = false;
        this.parameters = new ArrayList<>();
        this.language = "plpgsql";
        this.compatibilityMode = "PG";
    }

    public CreateProcedureStmt(String procedureName, boolean orReplace) {
        this.procedureName = procedureName;
        this.orReplace = orReplace;
        this.parameters = new ArrayList<>();
        this.language = "plpgsql";
        this.compatibilityMode = "PG";
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public boolean isOrReplace() {
        return orReplace;
    }

    public void setOrReplace(boolean orReplace) {
        this.orReplace = orReplace;
    }

    public List<ProcedureParameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    public void addParameter(ProcedureParameter parameter) {
        this.parameters.add(parameter);
    }

    public void setParameters(List<ProcedureParameter> parameters) {
        this.parameters = parameters != null ? new ArrayList<>(parameters) : new ArrayList<>();
    }

    public ProcedureBody getBody() {
        return body;
    }

    public void setBody(ProcedureBody body) {
        this.body = body;
    }

    public ProcedureSecurity getSecurity() {
        return security;
    }

    public void setSecurity(ProcedureSecurity security) {
        this.security = security;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCompatibilityMode() {
        return compatibilityMode;
    }

    public void setCompatibilityMode(String compatibilityMode) {
        this.compatibilityMode = compatibilityMode;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.CREATE_PROCEDURE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitCreateProcedureStmt(this);
    }
}

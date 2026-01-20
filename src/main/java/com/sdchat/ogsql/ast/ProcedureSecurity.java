package com.sdchat.ogsql.ast;

/**
 * Represents security attributes for a stored procedure.
 * 
 * <p>Security attributes control execution context and ownership
 * of the procedure. These include DEFINER vs INVOKER security model
 * and AUTHID specification for the procedure's execution context.</p>
 * 
 * <p>Security modes:</p>
 * <ul>
 *   <li>SECURITY DEFINER - Execute with privileges of the procedure's owner</li>
 *   <li>SECURITY INVOKER - Execute with privileges of the caller</li>
 * </ul>
 * 
 * @see CreateProcedureStmt
 */
public class ProcedureSecurity {

    /**
     * Authentication ID types for procedure execution context.
     */
    public enum AuthidType {
        DEFINER, CURRENT_USER
    }

    private boolean definers;
    private AuthidType authid;
    private boolean securityInvoker;

    public ProcedureSecurity() {
        this.definers = false;
        this.authid = AuthidType.DEFINER;
        this.securityInvoker = false;
    }

    public boolean isDefiner() {
        return definers;
    }

    public void setDefiner(boolean definers) {
        this.definers = definers;
    }

    public AuthidType getAuthid() {
        return authid;
    }

    public void setAuthid(AuthidType authid) {
        this.authid = authid;
    }

    public boolean isSecurityInvoker() {
        return securityInvoker;
    }

    public void setSecurityInvoker(boolean securityInvoker) {
        this.securityInvoker = securityInvoker;
    }
}

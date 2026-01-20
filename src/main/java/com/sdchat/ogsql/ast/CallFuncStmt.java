package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AST node representing a CALL statement that invokes a stored procedure.
 * 
 * <p>CALL statements support both positional and named parameter styles for passing
 * arguments to procedures. Positional arguments are passed in order (e.g., <code>CALL proc(1, 2, 3)</code>),
 * while named arguments use the <code>name => value</code> syntax (e.g., <code>CALL proc(p1 => 1, p2 => 2)</code>).</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * CALL calculate_bonus(emp_id, 0.15);
 * CALL process_data(input => 'data', mode => 'full');
 * }</pre>
 * 
 * @see SQLStatement
 * @see ASTVisitor
 */
public class CallFuncStmt implements SQLStatement {

    private String procedureName;
    private List<ValueExpression> arguments;
    private Map<String, ValueExpression> namedArguments;
    private boolean hasReturnValue;

    /**
     * Constructs a CallFuncStmt with the specified procedure name.
     * 
     * @param procedureName the name of the procedure to call
     */
    public CallFuncStmt(String procedureName) {
        this.procedureName = procedureName;
        this.arguments = new ArrayList<>();
        this.namedArguments = new HashMap<>();
        this.hasReturnValue = false;
    }

    /**
     * Returns the name of the procedure being called.
     * 
     * @return the procedure name
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * Sets the name of the procedure being called.
     * 
     * @param name the procedure name
     */
    public void setProcedureName(String name) {
        this.procedureName = name;
    }

    /**
     * Returns the list of positional arguments passed to the procedure.
     * 
     * @return an unmodifiable list of positional arguments
     */
    public List<ValueExpression> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    public boolean hasArguments() {
        return !arguments.isEmpty();
    }

    /**
     * Adds a positional argument to the argument list.
     * 
     * @param arg the value expression to add as a positional argument
     */
    public void addArgument(ValueExpression arg) {
        this.arguments.add(arg);
    }

    /**
     * Sets all positional arguments for this procedure call.
     * 
     * @param args the list of positional arguments
     */
    public void setArguments(List<ValueExpression> args) {
        this.arguments = args != null ? new ArrayList<>(args) : new ArrayList<>();
    }

    /**
     * Returns the map of named arguments passed to the procedure.
     * 
     * @return an unmodifiable map of named arguments (parameter names to values)
     */
    public Map<String, ValueExpression> getNamedArguments() {
        return Collections.unmodifiableMap(namedArguments);
    }

    public boolean hasNamedArguments() {
        return !namedArguments.isEmpty();
    }

    /**
     * Adds a named argument to the argument map.
     * 
     * @param name the parameter name
     * @param arg the value expression for this named parameter
     */
    public void addNamedArgument(String name, ValueExpression arg) {
        this.namedArguments.put(name, arg);
    }

    /**
     * Sets all named arguments for this procedure call.
     * 
     * @param args the map of named arguments (parameter names to values)
     */
    public void setNamedArguments(Map<String, ValueExpression> args) {
        this.namedArguments = args != null ? new HashMap<>(args) : new HashMap<>();
    }

    /**
     * Returns whether this procedure call returns a value.
     * 
     * @return true if the procedure returns a value, false otherwise
     */
    public boolean hasReturnValue() {
        return hasReturnValue;
    }

    /**
     * Sets whether this procedure call returns a value.
     * 
     * @param hasReturnValue true if the procedure returns a value
     */
    public void setHasReturnValue(boolean hasReturnValue) {
        this.hasReturnValue = hasReturnValue;
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.CALL_PROCEDURE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitCallFuncStmt(this);
    }
}

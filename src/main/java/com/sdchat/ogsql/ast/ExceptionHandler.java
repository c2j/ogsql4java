package com.sdchat.ogsql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an exception handler within a stored procedure.
 * 
 * <p>Exception handlers define what statements execute when specific
 * exceptions occur during procedure execution. Multiple exception types
 * can be handled by a single handler.</p>
 * 
 * <p>Example:</p>
 * <pre>{@code
 * EXCEPTION
 *     WHEN division_by_zero THEN
 *         RAISE NOTICE 'Division by zero occurred';
 *     WHEN no_data_found THEN
 *         RAISE NOTICE 'No matching data found';
 * END;
 * }</pre>
 * 
 * @see ProcedureBody
 */
public class ExceptionHandler {
    private List<String> exceptionTypes;
    private List<Statement> handlerStatements;

    public ExceptionHandler(List<String> exceptionTypes, List<Statement> handlerStatements) {
        this.exceptionTypes = exceptionTypes != null ? new ArrayList<>(exceptionTypes) : new ArrayList<>();
        this.handlerStatements = handlerStatements != null ? new ArrayList<>(handlerStatements) : new ArrayList<>();
    }

    public List<String> getExceptionTypes() {
        return Collections.unmodifiableList(exceptionTypes);
    }

    public boolean hasExceptionTypes() {
        return !exceptionTypes.isEmpty();
    }

    public void setExceptionTypes(List<String> exceptionTypes) {
        this.exceptionTypes = exceptionTypes != null ? new ArrayList<>(exceptionTypes) : new ArrayList<>();
    }

    public List<Statement> getHandlerStatements() {
        return Collections.unmodifiableList(handlerStatements);
    }

    public boolean hasHandlerStatements() {
        return !handlerStatements.isEmpty();
    }

    public void setHandlerStatements(List<Statement> handlerStatements) {
        this.handlerStatements = handlerStatements != null ? new ArrayList<>(handlerStatements) : new ArrayList<>();
    }
}

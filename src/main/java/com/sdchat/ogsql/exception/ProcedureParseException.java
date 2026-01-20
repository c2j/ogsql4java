package com.sdchat.ogsql.exception;

/**
 * Exception thrown for parsing errors in stored procedures.
 * Extends ParseException to add procedure-specific context.
 */
public class ProcedureParseException extends ParseException {

    private final String procedureName;
    private final String procedurePart;

    /**
     * Creates a new ProcedureParseException with the specified message.
     *
     * @param message The error message
     */
    public ProcedureParseException(String message) {
        super(message);
        this.procedureName = null;
        this.procedurePart = null;
    }

    /**
     * Creates a new ProcedureParseException with message and cause.
     *
     * @param message The error message
     * @param cause The underlying cause
     */
    public ProcedureParseException(String message, Throwable cause) {
        super(message, cause);
        this.procedureName = null;
        this.procedurePart = null;
    }

    /**
     * Creates a new ProcedureParseException with procedure context.
     *
     * @param message The error message
     * @param procedureName Name of the procedure being parsed
     * @param procedurePart The part of the procedure (e.g., "parameters", "body", "security")
     */
    public ProcedureParseException(String message, String procedureName, String procedurePart) {
        super(message);
        this.procedureName = procedureName;
        this.procedurePart = procedurePart;
    }

    /**
     * Creates a new ProcedureParseException with procedure context and cause.
     *
     * @param message The error message
     * @param cause The underlying cause
     * @param procedureName Name of the procedure being parsed
     * @param procedurePart The part of the procedure (e.g., "parameters", "body", "security")
     */
    public ProcedureParseException(String message, Throwable cause, String procedureName, String procedurePart) {
        super(message, cause);
        this.procedureName = procedureName;
        this.procedurePart = procedurePart;
    }

    /**
     * Gets the name of the procedure being parsed (if available).
     *
     * @return Procedure name, or null if not available
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * Gets the part of the procedure where the error occurred (if available).
     *
     * @return Procedure part (e.g., "parameters", "body", "security"), or null if not available
     */
    public String getProcedurePart() {
        return procedurePart;
    }
}

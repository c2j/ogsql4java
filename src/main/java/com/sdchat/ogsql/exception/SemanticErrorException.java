package com.sdchat.ogsql.exception;

/**
 * Exception thrown for semantic errors in SQL statements.
 * Semantic errors occur when SQL syntax is correct but meaning is invalid.
 */
public class SemanticErrorException extends ParseException {

    private final String semanticIssue;
    private final int line;
    private final String context;

    public SemanticErrorException(String message) {
        super(message);
        this.semanticIssue = null;
        this.line = 0;
        this.context = null;
    }

    public SemanticErrorException(String message, String semanticIssue) {
        super(message);
        this.semanticIssue = semanticIssue;
        this.line = 0;
        this.context = null;
    }

    public SemanticErrorException(String message, Throwable cause) {
        super(message, cause);
        this.semanticIssue = null;
        this.line = 0;
        this.context = null;
    }

    public SemanticErrorException(String message, String semanticIssue, Throwable cause) {
        super(message, cause);
        this.semanticIssue = semanticIssue;
        this.line = 0;
        this.context = null;
    }

    /**
     * Constructor for semantic error with line and context (for error reporting).
     * 
     * @param message Error message
     * @param line Line number where error occurred
     * @param context SQL context that caused error
     */
    public SemanticErrorException(String message, int line, String context) {
        super(message);
        this.semanticIssue = null;
        this.line = line;
        this.context = context;
    }

    /**
     * Returns the specific semantic issue that caused this exception.
     * 
     * @return semantic issue description, or null if not applicable
     */
    public String getSemanticIssue() {
        return semanticIssue;
    }

    /**
     * Returns the line number where the error occurred.
     * 
     * @return line number, or 0 if not available
     */
    public int getLine() {
        return line;
    }

    /**
     * Returns the SQL context where the error occurred.
     * 
     * @return SQL context, or null if not available
     */
    public String getContext() {
        return context;
    }
}

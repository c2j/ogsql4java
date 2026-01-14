package com.sdchat.ogsql.exception;

/**
 * Exception thrown for semantic errors in SQL statements.
 * Semantic errors occur when the SQL syntax is correct but the meaning is invalid.
 */
public class SemanticErrorException extends ParseException {

    private final String queryPart;

    public SemanticErrorException(String message) {
        super(message);
        this.queryPart = null;
    }

    public SemanticErrorException(String message, String queryPart) {
        super(message);
        this.queryPart = queryPart;
    }

    public SemanticErrorException(String message, Throwable cause) {
        super(message, cause);
        this.queryPart = null;
    }

    public SemanticErrorException(String message, String queryPart, Throwable cause) {
        super(message, cause);
        this.queryPart = queryPart;
    }

    public String getQueryPart() {
        return queryPart;
    }
}
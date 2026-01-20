package com.sdchat.ogsql.exception;

/**
 * Error severity levels for parsing errors.
 */
public enum ErrorSeverity {
    /**
     * Critical error that prevents parsing from completing.
     */
    ERROR,

    /**
     * Issue that doesn't prevent parsing but may indicate a problem.
     */
    WARNING,

    /**
     * Informational message about the parsing process.
     */
    INFO
}
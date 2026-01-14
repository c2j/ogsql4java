package com.sdchat.ogsql.parser;

/**
 * Error handling strategy for the SQL parser.
 */
public enum ErrorStrategy {
    /**
     * Default error strategy with detailed error messages and recovery.
     * Best for development and debugging.
     */
    DEFAULT,
    
    /**
     * Bail error strategy that stops parsing immediately on first error.
     * Best for production performance.
     */
    BAIL
}
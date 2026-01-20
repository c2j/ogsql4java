package com.sdchat.ogsql.parser;

import com.sdchat.ogsql.exception.ErrorSeverity;

/**
 * Class representing a parsing error with detailed information.
 */
public class ParsingError {
    private final String message;
    private final int line;
    private final int column;
    private final ErrorSeverity severity;
    private final String context;
    private final String suggestion;

    public ParsingError(String message, int line, int column, ErrorSeverity severity) {
        this(message, line, column, severity, null, null);
    }

    public ParsingError(String message, int line, int column, ErrorSeverity severity, String context) {
        this(message, line, column, severity, context, null);
    }

    public ParsingError(String message, int line, int column, ErrorSeverity severity, String context, String suggestion) {
        this.message = message;
        this.line = line;
        this.column = column;
        this.severity = severity;
        this.context = context;
        this.suggestion = suggestion;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public String getContext() {
        return context;
    }

    public String getSuggestion() {
        return suggestion;
    }
}
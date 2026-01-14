package com.sdchat.ogsql.exception;

public class SyntaxErrorException extends ParseException {

    private final int line;
    private final int column;
    private final String context;
    private final String suggestion;

    public SyntaxErrorException(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
        this.context = null;
        this.suggestion = null;
    }

    public SyntaxErrorException(String message, int line, int column, String context) {
        super(message);
        this.line = line;
        this.column = column;
        this.context = context;
        this.suggestion = null;
    }

    public SyntaxErrorException(String message, int line, int column, String context, String suggestion) {
        super(message);
        this.line = line;
        this.column = column;
        this.context = context;
        this.suggestion = suggestion;
    }

    public SyntaxErrorException(String message, int line, int column, Throwable cause) {
        super(message, cause);
        this.line = line;
        this.column = column;
        this.context = null;
        this.suggestion = null;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getContext() {
        return context;
    }

    public String getSuggestion() {
        return suggestion;
    }
}

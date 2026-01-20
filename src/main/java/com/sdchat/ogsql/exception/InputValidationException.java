package com.sdchat.ogsql.exception;

/**
 * Exception thrown for input validation errors.
 * Input validation errors include file size limits, memory limits, encoding issues, etc.
 */
public class InputValidationException extends ParseException {

    private final String inputType;
    private final long actualSize;
    private final long maxSize;

    public InputValidationException(String message) {
        super(message);
        this.inputType = null;
        this.actualSize = -1;
        this.maxSize = -1;
    }

    public InputValidationException(String message, String inputType, long actualSize, long maxSize) {
        super(message);
        this.inputType = inputType;
        this.actualSize = actualSize;
        this.maxSize = maxSize;
    }

    public InputValidationException(String message, Throwable cause) {
        super(message, cause);
        this.inputType = null;
        this.actualSize = -1;
        this.maxSize = -1;
    }

    public String getInputType() {
        return inputType;
    }

    public long getActualSize() {
        return actualSize;
    }

    public long getMaxSize() {
        return maxSize;
    }
}
package com.sdchat.ogsql.hint;

/**
 * Hint Validation Result
 *
 * Represents the result of validating a hint against the hint knowledge base.
 * Contains information about whether the hint is valid and any error messages.
 */
public class HintValidationResult {

    private final boolean valid;
    private final String hintType;
    private final String errorMessage;

    /**
     * Creates a successful validation result.
     *
     * @param hintType The hint type that was validated
     * @return A valid validation result
     */
    public static HintValidationResult valid(String hintType) {
        return new HintValidationResult(true, hintType, null);
    }

    /**
     * Creates a failed validation result.
     *
     * @param hintType The hint type that failed validation
     * @param errorMessage The error message explaining why validation failed
     * @return An invalid validation result
     */
    public static HintValidationResult invalid(String hintType, String errorMessage) {
        return new HintValidationResult(false, hintType, errorMessage);
    }

    private HintValidationResult(boolean valid, String hintType, String errorMessage) {
        this.valid = valid;
        this.hintType = hintType;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return valid;
    }

    public String getHintType() {
        return hintType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (valid) {
            return "HintValidationResult{valid=true, hintType='" + hintType + "'}";
        } else {
            return "HintValidationResult{valid=false, hintType='" + hintType +
                   "', error='" + errorMessage + "'}";
        }
    }
}

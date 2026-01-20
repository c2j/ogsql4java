package com.sdchat.ogsql.parser;

/**
 * Configuration options for SQL parser behavior.
 * Allows customization of limits and compatibility modes for parsing.
 */
public class ParserConfiguration {

    private long maxProcedureBodySize = 1048576; // 1MB default
    private int maxProcedureNestingDepth = 100; // 100 levels default
    private String defaultCompatibilityMode = "PG"; // PostgreSQL compatibility default

    /**
     * Creates a new ParserConfiguration with default settings.
     */
    public ParserConfiguration() {
    }

    /**
     * Gets the maximum size for procedure body in bytes.
     *
     * @return Maximum body size in bytes
     */
    public long getMaxProcedureBodySize() {
        return maxProcedureBodySize;
    }

    /**
     * Sets the maximum size for procedure body in bytes.
     * Prevents denial-of-service attacks via excessively large procedures.
     *
     * @param maxProcedureBodySize Maximum body size in bytes
     */
    public void setMaxProcedureBodySize(long maxProcedureBodySize) {
        if (maxProcedureBodySize <= 0) {
            throw new IllegalArgumentException("Max procedure body size must be positive");
        }
        this.maxProcedureBodySize = maxProcedureBodySize;
    }

    /**
     * Gets the maximum nesting depth for procedure blocks.
     *
     * @return Maximum nesting depth
     */
    public int getMaxProcedureNestingDepth() {
        return maxProcedureNestingDepth;
    }

    /**
     * Sets the maximum nesting depth for procedure blocks.
     * Prevents stack overflow via excessive nesting.
     *
     * @param maxProcedureNestingDepth Maximum nesting depth
     */
    public void setMaxProcedureNestingDepth(int maxProcedureNestingDepth) {
        if (maxProcedureNestingDepth <= 0) {
            throw new IllegalArgumentException("Max procedure nesting depth must be positive");
        }
        this.maxProcedureNestingDepth = maxProcedureNestingDepth;
    }

    /**
     * Gets the default compatibility mode for parsing.
     *
     * @return Compatibility mode (A, B, C, PG, D)
     */
    public String getDefaultCompatibilityMode() {
        return defaultCompatibilityMode;
    }

    /**
     * Sets the default compatibility mode for parsing.
     * OpenGauss supports multiple compatibility levels:
     * - A: Oracle compatibility
     * - B: MySQL compatibility
     * - C: TD compatibility
     * - PG: PostgreSQL compatibility
     * - D: Other compatibility
     *
     * @param defaultCompatibilityMode Compatibility mode
     */
    public void setDefaultCompatibilityMode(String defaultCompatibilityMode) {
        if (defaultCompatibilityMode == null || defaultCompatibilityMode.trim().isEmpty()) {
            throw new IllegalArgumentException("Compatibility mode cannot be null or empty");
        }
        String mode = defaultCompatibilityMode.toUpperCase().trim();
        if (!mode.matches("[ABCPGD]")) {
            throw new IllegalArgumentException("Invalid compatibility mode: " + mode + ". Must be one of: A, B, C, PG, D");
        }
        this.defaultCompatibilityMode = mode;
    }
}

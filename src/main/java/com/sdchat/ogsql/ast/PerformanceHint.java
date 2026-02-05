package com.sdchat.ogsql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an OpenGauss query optimizer hint.
 * Hints are special directives embedded in SQL comments to guide the query optimizer.
 */
public class PerformanceHint {
    private String hintType;
    private List<String> tables;
    private String rawHint;
    private HintLocation location;
    private boolean valid;
    private String errorMessage;
    private String tableReferences;

    /**
     * Creates a new performance hint.
     *
     * @param hintType The type of hint (e.g., NestLoop, MergeJoin, HashJoin)
     */
    public PerformanceHint(String hintType) {
        this.hintType = hintType;
        this.tables = new ArrayList<>();
        this.rawHint = hintType;
        this.valid = true;
    }

    /**
     * Creates a new performance hint with table references.
     *
     * @param hintType The type of hint
     * @param tables List of table names affected by this hint
     */
    public PerformanceHint(String hintType, List<String> tables) {
        this.hintType = hintType;
        this.tables = tables != null ? new ArrayList<>(tables) : new ArrayList<>();
        this.rawHint = hintType;
        this.valid = true;
    }

    /**
     * Gets the hint type.
     *
     * @return The hint type (e.g., NestLoop, MergeJoin, HashJoin)
     */
    public String getHintType() {
        return hintType;
    }

    /**
     * Sets the hint type.
     *
     * @param hintType The hint type
     */
    public void setHintType(String hintType) {
        this.hintType = hintType;
    }

    /**
     * Gets the list of tables affected by this hint.
     *
     * @return List of table names
     */
    public List<String> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public boolean hasTables() {
        return !tables.isEmpty();
    }

    /**
     * Sets the list of tables affected by this hint.
     *
     * @param tables List of table names
     */
    public void setTables(List<String> tables) {
        this.tables = tables != null ? new ArrayList<>(tables) : new ArrayList<>();
    }

    /**
     * Adds a table to this hint.
     *
     * @param table Table name
     */
    public void addTable(String table) {
        if (table != null) {
            this.tables.add(table);
        }
    }

    /**
     * Gets the raw hint string.
     *
     * @return Raw hint string
     */
    public String getRawHint() {
        return rawHint;
    }

    /**
     * Sets raw hint string.
     *
     * @param rawHint Raw hint string
     */
    public void setRawHint(String rawHint) {
        this.rawHint = rawHint;
    }

    /**
     * Gets hint location in original SQL.
     *
     * @return Hint location, or null if not available
     */
    public HintLocation getLocation() {
        return location;
    }

    /**
     * Sets hint location in original SQL.
     *
     * @param location Hint location
     */
    public void setLocation(HintLocation location) {
        this.location = location;
    }

    /**
     * Checks if hint is valid (exists in knowledge base).
     *
     * @return true if hint is valid, false otherwise
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets hint validation status.
     *
     * @param valid true if hint is valid, false otherwise
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Gets error message if hint is invalid.
     *
     * @return Error message, or null if hint is valid
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error message for invalid hint.
     *
     * @param errorMessage Error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceHint that = (PerformanceHint) o;
        return Objects.equals(hintType, that.hintType) &&
               Objects.equals(tables, that.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hintType, tables);
    }

    @Override
    public String toString() {
        return "PerformanceHint{" +
               "type='" + hintType + '\'' +
               ", tables=" + tables +
               '}';
    }
}

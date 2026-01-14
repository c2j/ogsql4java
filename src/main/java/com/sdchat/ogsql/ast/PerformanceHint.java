package com.sdchat.ogsql.ast;

import java.util.ArrayList;
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

    /**
     * Creates a new performance hint.
     *
     * @param hintType The type of hint (e.g., NestLoop, MergeJoin, HashJoin)
     */
    public PerformanceHint(String hintType) {
        this.hintType = hintType;
        this.tables = new ArrayList<>();
        this.rawHint = hintType;
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
        return tables;
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
     * Sets the raw hint string.
     *
     * @param rawHint Raw hint string
     */
    public void setRawHint(String rawHint) {
        this.rawHint = rawHint;
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

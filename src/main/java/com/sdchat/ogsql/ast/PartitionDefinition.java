package com.sdchat.ogsql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single partition definition within a partitioned table.
 * 
 * Contains the partition name, value specifications, and optional tablespace information.
 */
public class PartitionDefinition {

    private String name;
    private String minValue;
    private String maxValue;
    private List<String> values;
    private String tablespace;

    /**
     * Creates a new partition definition with the specified name.
     * 
     * @param name The name of the partition (e.g., "p2023", "p_north_america")
     */
    public PartitionDefinition(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Partition name cannot be null or empty");
        }
        this.name = name.trim();
    }

    /**
     * Gets the partition name.
     * 
     * @return The partition name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the partition name.
     * 
     * @param name The partition name
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Partition name cannot be null or empty");
        }
        this.name = name.trim();
    }

    /**
     * Gets the minimum value for RANGE partitions.
     * 
     * @return The minimum value, or null if not specified
     */
    public String getMinValue() {
        return minValue;
    }

    /**
     * Sets the minimum value for RANGE partitions.
     * 
     * @param minValue The minimum value
     */
    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    /**
     * Gets the maximum value for RANGE partitions.
     * 
     * @return The maximum value, or null if not specified
     */
    public String getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the maximum value for RANGE partitions.
     * 
     * @param maxValue The maximum value
     */
    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Gets the list of discrete values for LIST partitions.
     * 
     * @return The list of values, or null if not specified
     */
    public List<String> getValues() {
        return values != null ? Collections.unmodifiableList(values) : null;
    }

    /**
     * Sets the list of discrete values for LIST partitions.
     * 
     * @param values The list of values
     */
    public void setValues(List<String> values) {
        this.values = values != null ? new ArrayList<>(values) : null;
    }

    /**
     * Gets the tablespace for this partition.
     * 
     * @return The tablespace name, or null if using default tablespace
     */
    public String getTablespace() {
        return tablespace;
    }

    /**
     * Sets the tablespace for this partition.
     * 
     * @param tablespace The tablespace name
     */
    public void setTablespace(String tablespace) {
        this.tablespace = tablespace;
    }

    /**
     * Checks if this partition has a specific tablespace.
     * 
     * @return true if a tablespace is specified, false otherwise
     */
    public boolean hasTablespace() {
        return tablespace != null && !tablespace.trim().isEmpty();
    }

    /**
     * Checks if this partition is a RANGE partition with min/max values.
     * 
     * @return true if minValue or maxValue is specified
     */
    public boolean isRangePartition() {
        return minValue != null || maxValue != null;
    }

    /**
     * Checks if this partition is a LIST partition with discrete values.
     * 
     * @return true if values list is specified and not empty
     */
    public boolean isListPartition() {
        return values != null && !values.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PartitionDefinition{name='").append(name).append('\'');
        
        if (isRangePartition()) {
            sb.append(", minValue='").append(minValue).append('\'');
            sb.append(", maxValue='").append(maxValue).append('\'');
        }
        
        if (isListPartition()) {
            sb.append(", values=").append(values);
        }
        
        if (hasTablespace()) {
            sb.append(", tablespace='").append(tablespace).append('\'');
        }
        
        sb.append('}');
        return sb.toString();
    }
}
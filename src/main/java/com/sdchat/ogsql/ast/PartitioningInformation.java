package com.sdchat.ogsql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents table partitioning strategy and configuration.
 * 
 * Contains the partition type, partition keys, individual partition definitions,
 * and optional subpartitioning information.
 */
public class PartitioningInformation {

    private PartitionType type;
    private List<String> partitionKeys;
    private List<PartitionDefinition> partitions;
    private PartitioningInformation subpartitioning;

    /**
     * Creates a new partitioning information with the specified type.
     * 
     * @param type The partition type (RANGE, LIST, or HASH)
     */
    public PartitioningInformation(PartitionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Partition type cannot be null");
        }
        this.type = type;
        this.partitionKeys = new ArrayList<>();
        this.partitions = new ArrayList<>();
    }

    /**
     * Gets the partition type.
     * 
     * @return The partition type
     */
    public PartitionType getType() {
        return type;
    }

    /**
     * Sets the partition type.
     * 
     * @param type The partition type
     */
    public void setType(PartitionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Partition type cannot be null");
        }
        this.type = type;
    }

    /**
     * Gets the list of partition key column names.
     * 
     * @return The list of partition keys
     */
    public List<String> getPartitionKeys() {
        return Collections.unmodifiableList(partitionKeys);
    }

    /**
     * Sets the list of partition key column names.
     * 
     * @param partitionKeys The list of partition keys
     */
    public void setPartitionKeys(List<String> partitionKeys) {
        if (partitionKeys == null) {
            throw new IllegalArgumentException("Partition keys cannot be null");
        }
        this.partitionKeys = new ArrayList<>(partitionKeys);
    }

    /**
     * Adds a partition key column name.
     * 
     * @param key The partition key column name
     */
    public void addPartitionKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Partition key cannot be null or empty");
        }
        this.partitionKeys.add(key.trim());
    }

    /**
     * Gets the list of partition definitions.
     * 
     * @return The list of partition definitions
     */
    public List<PartitionDefinition> getPartitions() {
        return Collections.unmodifiableList(partitions);
    }

    /**
     * Sets the list of partition definitions.
     * 
     * @param partitions The list of partition definitions
     */
    public void setPartitions(List<PartitionDefinition> partitions) {
        if (partitions == null) {
            throw new IllegalArgumentException("Partitions cannot be null");
        }
        this.partitions = new ArrayList<>(partitions);
    }

    /**
     * Adds a partition definition.
     * 
     * @param partition The partition definition to add
     */
    public void addPartition(PartitionDefinition partition) {
        if (partition == null) {
            throw new IllegalArgumentException("Partition definition cannot be null");
        }
        this.partitions.add(partition);
    }

    /**
     * Gets the subpartitioning information.
     * 
     * @return The subpartitioning information, or null if no subpartitioning
     */
    public PartitioningInformation getSubpartitioning() {
        return subpartitioning;
    }

    /**
     * Sets the subpartitioning information.
     * 
     * @param subpartitioning The subpartitioning information
     */
    public void setSubpartitioning(PartitioningInformation subpartitioning) {
        this.subpartitioning = subpartitioning;
    }

    /**
     * Checks if this table has subpartitioning.
     * 
     * @return true if subpartitioning is specified
     */
    public boolean hasSubpartitioning() {
        return subpartitioning != null;
    }

    /**
     * Checks if partition keys are specified.
     * 
     * @return true if partition keys are specified
     */
    public boolean hasPartitionKeys() {
        return !partitionKeys.isEmpty();
    }

    /**
     * Checks if partition definitions are specified.
     * 
     * @return true if partition definitions are specified
     */
    public boolean hasPartitions() {
        return !partitions.isEmpty();
    }

    /**
     * Validates the partitioning configuration based on partition type.
     * 
     * @throws IllegalStateException if the configuration is invalid for the partition type
     */
    public void validate() {
        if (!hasPartitionKeys()) {
            throw new IllegalStateException("Partition keys cannot be empty");
        }
        
        // For RANGE and LIST partitions, partition definitions are required
        if ((type == PartitionType.RANGE || type == PartitionType.LIST) && !hasPartitions()) {
            throw new IllegalStateException(type + " partitions must have partition definitions");
        }
        
        // For HASH partitions, partition definitions can be auto-generated
        
        // Validate subpartitioning
        if (hasSubpartitioning()) {
            if (!hasPartitions()) {
                throw new IllegalStateException("Subpartitioning requires primary partitioning");
            }
            subpartitioning.validate();
        }
    }

    /**
     * Gets the number of partitions.
     * 
     * @return The number of partition definitions
     */
    public int getPartitionCount() {
        return partitions.size();
    }

    /**
     * Gets the number of partition keys.
     * 
     * @return The number of partition keys
     */
    public int getPartitionKeyCount() {
        return partitionKeys.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PartitioningInformation{type=").append(type);
        sb.append(", partitionKeys=").append(partitionKeys);
        sb.append(", partitions=").append(partitions);
        
        if (hasSubpartitioning()) {
            sb.append(", subpartitioning=").append(subpartitioning);
        }
        
        sb.append('}');
        return sb.toString();
    }
}
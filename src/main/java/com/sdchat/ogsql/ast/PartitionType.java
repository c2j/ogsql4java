package com.sdchat.ogsql.ast;

/**
 * Enumeration of supported partition types in OpenGauss.
 * 
 * RANGE: Partitions based on value ranges
 * LIST: Partitions based on discrete value lists  
 * HASH: Partitions based on hash of partition keys
 */
public enum PartitionType {
    RANGE,
    LIST,
    HASH
}
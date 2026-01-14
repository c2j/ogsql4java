package com.sdchat.ogsql.ast;

/**
 * Enumeration of SQL statement types.
 */
public enum StatementType {
    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    CREATE_TABLE,
    ALTER_TABLE,
    DROP_TABLE,
    CREATE_FOREIGN_TABLE,
    UNKNOWN
}
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
    CREATE_PROCEDURE,
    ALTER_PROCEDURE,
    CALL_PROCEDURE,
    EXPLAIN,
    SHOW,
    BEGIN,
    COMMIT,
    ROLLBACK,
    SAVEPOINT,
    RELEASE_SAVEPOINT,
    ROLLBACK_TO_SAVEPOINT,
    ANALYZE,
    SET,
    CREATE_SCHEMA,
    UNKNOWN
}
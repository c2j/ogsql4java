package com.sdchat.ogsql.ast;

/**
 * Enumeration of conflict actions in INSERT ON CONFLICT clause.
 * 
 * DO_NOTHING: Skip the insert if there is a conflict
 * DO_UPDATE: Update the conflicting row instead of inserting
 */
public enum ConflictAction {
    DO_NOTHING,
    DO_UPDATE
}

package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;

/**
 * Base interface for all SQL statement types.
 */
public interface SQLStatement {
    StatementType getStatementType();

    <T> T accept(ASTVisitor<T> visitor);
}
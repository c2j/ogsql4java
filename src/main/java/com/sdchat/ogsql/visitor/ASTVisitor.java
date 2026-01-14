package com.sdchat.ogsql.visitor;

import com.sdchat.ogsql.ast.*;

/**
 * Visitor interface for traversing AST nodes.
 * @param <T> Return type of visitor methods
 */
public interface ASTVisitor<T> {
    T visit(SelectQuery query);
    T visit(CreateStatement statement);
    T visit(InsertStatement statement);
    T visit(UpdateStatement statement);
    T visit(DeleteStatement statement);
    T visit(AlterStatement statement);
    T visit(DropStatement statement);
    
    // Partitioning support
    T visit(PartitioningInformation partitioning);
    T visit(PartitionDefinition partition);
    
    // Foreign table support
    T visit(ExternalTable externalTable);
}
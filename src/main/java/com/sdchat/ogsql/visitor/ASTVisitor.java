package com.sdchat.ogsql.visitor;

import com.sdchat.ogsql.ast.*;

/**
 * Visitor interface for traversing SQL AST nodes.
 * Supports both generic visit() for legacy code and specific visit methods.
 * 
 * <p>Implement this interface to perform operations on the parsed SQL AST,
 * such as validation, transformation, code generation, or analysis.</p>
 * 
 * @param <T> Return type of visitor methods
 */
public interface ASTVisitor<T> {
    T visitSelectQuery(SelectQuery query);
    T visitCreateStatement(CreateStatement statement);
    
    /**
     * Visits a CREATE PROCEDURE AST node.
     * 
     * @param statement the CREATE PROCEDURE statement to visit
     * @return visitor-specific result
     */
    T visitCreateProcedureStmt(CreateProcedureStmt statement);
    
    T visitInsertStatement(InsertStatement statement);
    T visitUpdateStatement(UpdateStatement statement);
    T visitDeleteStatement(DeleteStatement statement);
    T visitAlterStatement(AlterStatement statement);
    
    /**
     * Visits an ALTER PROCEDURE AST node.
     * 
     * @param statement the ALTER PROCEDURE statement to visit
     * @return visitor-specific result
     */
    T visitAlterProcedureStmt(AlterProcedureStmt statement);
    
    T visitDropStatement(DropStatement statement);
    
    /**
     * Visits a CALL statement AST node.
     * 
     * @param statement the CALL statement to visit
     * @return visitor-specific result
     */
    T visitCallFuncStmt(CallFuncStmt statement);

    T visitPartitioningInformation(PartitioningInformation partitioning);
    T visitPartitionDefinition(PartitionDefinition partition);
    T visitExternalTable(ExternalTable externalTable);
    T visitExplainStatement(ExplainStatement statement);
    T visitShowStatement(ShowStatement statement);
    T visitBeginStatement(BeginStatement statement);
    T visitCommitStatement(CommitStatement statement);
    T visitRollbackStatement(RollbackStatement statement);
    T visitAnalyzeStatement(AnalyzeStatement statement);
    T visitSetStatement(SetStatement statement);
    T visitCreateSchemaStatement(CreateSchemaStatement statement);
}

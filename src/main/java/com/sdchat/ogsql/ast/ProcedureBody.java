package com.sdchat.ogsql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the body of a stored procedure containing procedural code.
 * 
 * <p>The procedure body includes variable declarations, executable statements,
 * and exception handlers. It can be written in various procedural languages
 * (PL/pgSQL, PL/Python, etc.) specified by the language attribute.</p>
 * 
 * <p>The body structure includes:</p>
 * <ul>
 *   <li>Variable declarations (DECLARE section)</li>
 *   <li>Executable statements (BEGIN/END blocks)</li>
 *   <li>Exception handlers for error handling</li>
 *   <li>Original source code for reference</li>
 * </ul>
 * 
 * @see CreateProcedureStmt
 * @see VariableDeclaration
 * @see ExceptionHandler
 */
public class ProcedureBody {

    private String language;
    private List<VariableDeclaration> declarations;
    private List<Statement> statements;
    private List<ExceptionHandler> exceptionHandlers;
    private String sourceCode;

    public ProcedureBody(String language, String sourceCode) {
        this.language = language;
        this.sourceCode = sourceCode;
        this.declarations = new ArrayList<>();
        this.statements = new ArrayList<>();
        this.exceptionHandlers = new ArrayList<>();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<VariableDeclaration> getDeclarations() {
        return Collections.unmodifiableList(declarations);
    }

    public boolean hasDeclarations() {
        return !declarations.isEmpty();
    }

    public void addDeclaration(VariableDeclaration declaration) {
        this.declarations.add(declaration);
    }

    public void setDeclarations(List<VariableDeclaration> declarations) {
        this.declarations = declarations != null ? new ArrayList<>(declarations) : new ArrayList<>();
    }

    public List<Statement> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    public boolean hasStatements() {
        return !statements.isEmpty();
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements != null ? new ArrayList<>(statements) : new ArrayList<>();
    }

    public List<ExceptionHandler> getExceptionHandlers() {
        return Collections.unmodifiableList(exceptionHandlers);
    }

    public boolean hasExceptionHandlers() {
        return !exceptionHandlers.isEmpty();
    }

    public void addExceptionHandler(ExceptionHandler handler) {
        this.exceptionHandlers.add(handler);
    }

    public void setExceptionHandlers(List<ExceptionHandler> handlers) {
        this.exceptionHandlers = handlers != null ? new ArrayList<>(handlers) : new ArrayList<>();
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}

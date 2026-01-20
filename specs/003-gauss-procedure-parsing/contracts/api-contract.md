# API Contract: Gauss Stored Procedure Parser

**Feature**: Gauss Stored Procedure Parsing Enhancement
**Version**: 1.0.0
**Date**: 2025-01-15

## Overview

This contract defines the public API for parsing Gauss stored procedures in the OpenGauss SQL Parser. The API follows the existing parser patterns and extends the visitor pattern for procedure-specific AST nodes.

---

## Core Parser API

### SQLParser Class Extensions

**Package**: `com.sdchat.ogsql.parser`

**Existing Class**: `SQLParser`

**New Method Signatures**:

```java
/**
 * Parses a CREATE PROCEDURE statement and returns the corresponding AST node.
 *
 * @param sql The SQL statement to parse
 * @return CreateProcedureStmt representing the procedure definition
 * @throws ParseException if the SQL cannot be parsed
 * @throws SyntaxErrorException if there is a syntax error
 * @throws SemanticErrorException if there is a semantic error
 * @throws InputValidationException if input exceeds size limits
 */
public CreateProcedureStmt parseCreateProcedure(String sql) throws ParseException;
```

```java
/**
 * Parses an ALTER PROCEDURE statement and returns the corresponding AST node.
 *
 * @param sql The SQL statement to parse
 * @return AlterProcedureStmt representing the procedure modification
 * @throws ParseException if the SQL cannot be parsed
 * @throws SyntaxErrorException if there is a syntax error
 * @throws SemanticErrorException if there is a semantic error
 */
public AlterProcedureStmt parseAlterProcedure(String sql) throws ParseException;
```

```java
/**
 * Parses a CALL statement and returns the corresponding AST node.
 *
 * @param sql The SQL statement to parse
 * @return CallFuncStmt representing the procedure call
 * @throws ParseException if the SQL cannot be parsed
 * @throws SyntaxErrorException if there is a syntax error
 */
public CallFuncStmt parseCallStatement(String sql) throws ParseException;
```

```java
/**
 * Parses a DROP PROCEDURE statement and returns the corresponding AST node.
 *
 * @param sql The SQL statement to parse
 * @return DropStmt representing the procedure drop
 * @throws ParseException if the SQL cannot be parsed
 * @throws SyntaxErrorException if there is a syntax error
 */
public DropStmt parseDropProcedure(String sql) throws ParseException;
```

### ParseResult Class Extensions

**Package**: `com.sdchat.ogsql.parser`

**Existing Class**: `ParseResult`

**New Fields**:

```java
public class ParseResult {
    // ... existing fields ...

    /**
     * Indicates if the parsed SQL contains a stored procedure statement.
     */
    private boolean containsProcedure;

    /**
     * The parsed procedure statement if present, null otherwise.
     */
    private SQLStatement procedureStatement;
}
```

**New Methods**:

```java
/**
 * Returns true if the parsed SQL contains a stored procedure statement.
 */
public boolean containsProcedure();

/**
 * Returns the parsed procedure statement if present, null otherwise.
 */
public SQLStatement getProcedureStatement();
```

---

## AST Node Classes

### CreateProcedureStmt

**Package**: `com.sdchat.ogsql.ast`

**Signature**:

```java
public class CreateProcedureStmt extends SQLStatement {
    public CreateProcedureStmt(String procedureName);
    public CreateProcedureStmt(String procedureName, boolean orReplace);

    public String getProcedureName();
    public void setProcedureName(String name);

    public boolean isOrReplace();
    public void setOrReplace(boolean orReplace);

    public List<ProcedureParameter> getParameters();
    public void addParameter(ProcedureParameter parameter);
    public void setParameters(List<ProcedureParameter> parameters);

    public ProcedureBody getBody();
    public void setBody(ProcedureBody body);

    public ProcedureSecurity getSecurity();
    public void setSecurity(ProcedureSecurity security);

    public String getLanguage();
    public void setLanguage(String language);

    public String getCompatibilityMode();
    public void setCompatibilityMode(String mode);

    @Override
    public <T> T accept(ASTVisitor<T> visitor);
}
```

### AlterProcedureStmt

**Package**: `com.sdchat.ogsql.ast`

**Signature**:

```java
public class AlterProcedureStmt extends SQLStatement {
    public AlterProcedureStmt(String procedureName);

    public String getProcedureName();
    public void setProcedureName(String name);

    public String getNewName();
    public void setNewName(String newName);

    public String getNewOwner();
    public void setNewOwner(String newOwner);

    public String getNewSchema();
    public void setNewSchema(String newSchema);

    public Boolean getSecurityInvoker();
    public void setSecurityInvoker(Boolean securityInvoker);

    public String getDependsOnExtension();
    public void setDependsOnExtension(String extension);

    public boolean isNoDependsOnExtension();
    public void setNoDependsOnExtension(boolean noDepends);

    @Override
    public <T> T accept(ASTVisitor<T> visitor);
}
```

### CallFuncStmt

**Package**: `com.sdchat.ogsql.ast`

**Signature**:

```java
public class CallFuncStmt extends SQLStatement {
    public CallFuncStmt(String procedureName);

    public String getProcedureName();
    public void setProcedureName(String name);

    public List<ValueExpression> getArguments();
    public void addArgument(ValueExpression arg);
    public void setArguments(List<ValueExpression> args);

    public Map<String, ValueExpression> getNamedArguments();
    public void addNamedArgument(String name, ValueExpression arg);
    public void setNamedArguments(Map<String, ValueExpression> args);

    public boolean hasReturnValue();
    public void setHasReturnValue(boolean hasReturnValue);

    @Override
    public <T> T accept(ASTVisitor<T> visitor);
}
```

### ProcedureParameter

**Package**: `com.sdchat.ogsql.ast`

**Signature**:

```java
public class ProcedureParameter {
    public enum ParameterMode {
        IN, OUT, INOUT, VARIADIC
    }

    public ProcedureParameter(String name, ParameterMode mode, String dataType);
    public ProcedureParameter(String name, ParameterMode mode, String dataType, ValueExpression defaultValue, int position);

    public String getName();
    public void setName(String name);

    public ParameterMode getMode();
    public void setMode(ParameterMode mode);

    public String getDataType();
    public void setDataType(String dataType);

    public ValueExpression getDefaultValue();
    public void setDefaultValue(ValueExpression defaultValue);

    public int getPosition();
    public void setPosition(int position);

    @Override
    public boolean equals(Object obj);
    @Override
    public int hashCode();
}
```

### ProcedureBody

**Package**: `com.sdchat.ogsql.ast`

**Signature**:

```java
public class ProcedureBody {
    public ProcedureBody(String language, String sourceCode);

    public String getLanguage();
    public void setLanguage(String language);

    public List<VariableDeclaration> getDeclarations();
    public void addDeclaration(VariableDeclaration declaration);
    public void setDeclarations(List<VariableDeclaration> declarations);

    public List<Statement> getStatements();
    public void addStatement(Statement statement);
    public void setStatements(List<Statement> statements);

    public List<ExceptionHandler> getExceptionHandlers();
    public void addExceptionHandler(ExceptionHandler handler);
    public void setExceptionHandlers(List<ExceptionHandler> handlers);

    public String getSourceCode();
    public void setSourceCode(String sourceCode);
}
```

### ProcedureSecurity

**Package**: `com.sdchat.ogsql.ast`

**Signature**:

```java
public class ProcedureSecurity {
    public enum AuthidType {
        DEFINER, CURRENT_USER
    }

    public ProcedureSecurity();

    public boolean isDefiner();
    public void setDefiner(boolean definier);

    public AuthidType getAuthid();
    public void setAuthid(AuthidType authid);

    public boolean isSecurityInvoker();
    public void setSecurityInvoker(boolean securityInvoker);
}
```

---

## Visitor Pattern Extensions

### ASTVisitor Interface

**Package**: `com.sdchat.ogsql.ast.visitor`

**Existing Interface**: `ASTVisitor<T>`

**New Method Signatures**:

```java
public interface ASTVisitor<T> {
    // ... existing visit methods ...

    /**
     * Visit a CREATE PROCEDURE statement node.
     */
    T visitCreateProcedureStmt(CreateProcedureStmt stmt);

    /**
     * Visit an ALTER PROCEDURE statement node.
     */
    T visitAlterProcedureStmt(AlterProcedureStmt stmt);

    /**
     * Visit a CALL statement node.
     */
    T visitCallFuncStmt(CallFuncStmt stmt);
}
```

---

## Error Handling

### Exception Hierarchy

**Package**: `com.sdchat.ogsql.exception`

**Existing Classes** (extended for procedures):

```java
public class ParseException extends Exception {
    private int lineNumber;
    private int columnNumber;
    private String errorContext;

    public ParseException(String message, int lineNumber, int columnNumber, String errorContext);
    // ... existing methods ...
}

public class SyntaxErrorException extends ParseException {
    private String expectedToken;
    private String foundToken;

    public SyntaxErrorException(String message, int lineNumber, int columnNumber, String expectedToken, String foundToken, String errorContext);
}

public class SemanticErrorException extends ParseException {
    private String semanticIssue;

    public SemanticErrorException(String message, int lineNumber, int columnNumber, String semanticIssue, String errorContext);
}

public class InputValidationException extends ParseException {
    private String validationType; // e.g., "SIZE_LIMIT", "NESTING_DEPTH"

    public InputValidationException(String message, int lineNumber, int columnNumber, String validationType, String errorContext);
}
```

### Error Message Format

**Syntax Error Example**:
```
Syntax error at line 5, column 12: expected ')' but found ','
CREATE PROCEDURE test_proc(
  p1 IN INTEGER,
  p2 IN INTEGER,  <-- Error: Unexpected comma before closing parenthesis
);
```

**Semantic Error Example**:
```
Semantic error at line 8: VARIADIC parameter must be the last parameter
CREATE PROCEDURE test_proc(
  p1 VARIADIC INTEGER[],
  p2 IN VARCHAR  <-- Error: Cannot have parameter after VARIADIC
);
```

**Input Validation Error Example**:
```
Input validation error: procedure body exceeds maximum size limit (1MB)
```

---

## Configuration API

### ParserConfiguration Class Extensions

**Package**: `com.sdchat.ogsql.parser`

**Existing Class**: `ParserConfiguration` (if exists) or new

**New Configuration Options**:

```java
public class ParserConfiguration {
    // ... existing configuration ...

    /**
     * Sets the maximum size for procedure body in bytes.
     * Default: 1048576 (1MB)
     */
    public void setMaxProcedureBodySize(long sizeInBytes);

    public long getMaxProcedureBodySize();

    /**
     * Sets the maximum nesting depth for procedure blocks.
     * Default: 100
     */
    public void setMaxProcedureNestingDepth(int depth);

    public int getMaxProcedureNestingDepth();

    /**
     * Sets the default compatibility mode for procedure parsing.
     * Default: "PG"
     */
    public void setDefaultCompatibilityMode(String mode);

    public String getDefaultCompatibilityMode();
}
```

---

## Backward Compatibility

### Versioning

- **Current Version**: 1.0.0
- **API Stability**: Public API changes require MINOR version bump (per Constitution Principle IV)
- **Backward Compatibility**: All existing API methods remain unchanged

### Migration Path

Existing code using `SQLParser.parse()` will continue to work. The new procedure-specific methods are optional conveniences for explicit procedure parsing.

---

## Examples

### Example 1: Parse CREATE PROCEDURE

```java
SQLParser parser = new SQLParser();
String sql = """
    CREATE OR REPLACE PROCEDURE calculate_employee_bonus(
        p_employee_id IN INTEGER,
        p_bonus_percent IN NUMERIC DEFAULT 0.10,
        p_bonus_amount OUT NUMERIC
    )
    LANGUAGE plpgsql
    SECURITY DEFINER
    AS $$
    BEGIN
        SELECT salary * p_bonus_percent INTO p_bonus_amount
        FROM employees
        WHERE employee_id = p_employee_id;
    END;
    $$;
""";

try {
    CreateProcedureStmt stmt = parser.parseCreateProcedure(sql);
    System.out.println("Procedure: " + stmt.getProcedureName());
    System.out.println("Parameters: " + stmt.getParameters().size());
} catch (ParseException e) {
    System.err.println("Parse error at line " + e.getLineNumber() + ": " + e.getMessage());
}
```

### Example 2: Parse CALL Statement

```java
SQLParser parser = new SQLParser();
String sql = "CALL calculate_employee_bonus(12345, 0.15, NULL);";

try {
    CallFuncStmt stmt = parser.parseCallStatement(sql);
    System.out.println("Calling: " + stmt.getProcedureName());
    System.out.println("Arguments: " + stmt.getArguments().size());
} catch (ParseException e) {
    System.err.println("Parse error: " + e.getMessage());
}
```

### Example 3: Use Visitor Pattern

```java
public class ProcedurePrinter implements ASTVisitor<String> {
    @Override
    public String visitCreateProcedureStmt(CreateProcedureStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE");
        if (stmt.isOrReplace()) {
            sb.append(" OR REPLACE");
        }
        sb.append(" PROCEDURE ").append(stmt.getProcedureName());
        sb.append("(");
        // ... parameter printing ...
        sb.append(")");
        sb.append(" LANGUAGE ").append(stmt.getLanguage());
        return sb.toString();
    }

    // ... other visit methods ...
}

// Usage:
SQLParser parser = new SQLParser();
CreateProcedureStmt stmt = parser.parseCreateProcedure(sql);
ProcedurePrinter printer = new ProcedurePrinter();
String procedureSQL = stmt.accept(printer);
System.out.println(procedureSQL);
```

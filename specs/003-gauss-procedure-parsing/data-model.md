# Data Model: Gauss Stored Procedure Parsing

**Feature**: Gauss Stored Procedure Parsing Enhancement
**Date**: 2025-01-15
**Status**: Phase 1 - Design

## Overview

This document defines the data model for Gauss stored procedure parsing. The model includes AST node types, parameter definitions, and supporting entities required to represent stored procedures in the parser's abstract syntax tree.

---

## Core Entities

### CreateProcedureStmt

Represents a CREATE [OR REPLACE] PROCEDURE statement.

**Package**: `com.sdchat.ogsql.ast`

**Extends**: `SQLStatement`

**Fields**:
| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| `procedureName` | `String` | Fully-qualified procedure name (schema.name or just name) | Required, non-empty, valid identifier |
| `orReplace` | `boolean` | True if OR REPLACE clause specified | Default: false |
| `parameters` | `List<ProcedureParameter>` | Procedure parameter list | Can be empty, valid parameter definitions |
| `body` | `ProcedureBody` | Procedural code block | Required, non-null |
| `security` | `ProcedureSecurity` | Security attributes | Required, non-null |
| `language` | `String` | Procedural language (e.g., "plpgsql") | Default: "plpgsql", validated against supported languages |
| `compatibilityMode` | `String` | OpenGauss compatibility mode (A/B/C/PG/D) | Default: "PG" or system default |

**Relationships**:
- `contains` → many `ProcedureParameter`
- `contains` → one `ProcedureBody`
- `contains` → one `ProcedureSecurity`

**Validation Rules** (from FR-001, FR-003, FR-011):
- `procedureName` must be a valid SQL identifier
- If `orReplace` is true and procedure already exists, replacement logic handled during AST traversal (not validation)
- `language` must be a supported procedural language (validation warning if unsupported)
- `compatibilityMode` must be one of: "A", "B", "C", "PG", "D"

---

### AlterProcedureStmt

Represents an ALTER PROCEDURE statement with modification actions.

**Package**: `com.sdchat.ogsql.ast`

**Extends**: `SQLStatement`

**Fields**:
| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| `procedureName` | `String` | Fully-qualified procedure name | Required, non-empty, valid identifier |
| `newName` | `String` | New procedure name (for RENAME TO) | Optional, valid identifier if present |
| `newOwner` | `String` | New owner name (for OWNER TO) | Optional, valid identifier if present |
| `newSchema` | `String` | New schema name (for SET SCHEMA) | Optional, valid identifier if present |
| `securityInvoker` | `Boolean` | SECURITY INVOKER setting (null = no change) | Optional, null-allowed |
| `dependsOnExtension` | `String` | Extension name for DEPENDS ON EXTENSION | Optional, valid identifier if present |
| `noDependsOnExtension` | `boolean` | True if NO DEPENDS ON EXTENSION specified | Default: false |

**Validation Rules** (from FR-005):
- Exactly one modification action must be specified (RENAME, OWNER, SET SCHEMA, SECURITY, or DEPENDS)
- `procedureName` must be a valid SQL identifier
- Optional fields must be null if the corresponding clause is not specified

---

### CallFuncStmt

Represents a CALL statement to invoke a stored procedure.

**Package**: `com.sdchat.ogsql.ast`

**Extends**: `SQLStatement`

**Fields**:
| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| `procedureName` | `String` | Fully-qualified procedure name | Required, non-empty, valid identifier |
| `arguments` | `List<ValueExpression>` | Positional argument expressions | Can be empty, non-null list |
| `namedArguments` | `Map<String, ValueExpression>` | Named parameter arguments (param => value) | Can be empty, non-null map |
| `hasReturnValue` | `boolean` | True if procedure returns a value used in expression | Default: false |

**Validation Rules** (from FR-006):
- `procedureName` must be a valid SQL identifier
- Either `arguments` or `namedArguments` may be populated, but not both
- If `namedArguments` is used, all parameter names must be valid identifiers
- Argument count must match procedure parameter count (if available in metadata)

---

### ProcedureParameter

Represents a single procedure parameter definition.

**Package**: `com.sdchat.ogsql.ast`

**Fields**:
| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| `name` | `String` | Parameter name | Required, non-empty, valid identifier |
| `mode` | `ParameterMode` | Parameter mode (IN/OUT/INOUT/VARIADIC) | Required, non-null |
| `dataType` | `String` | Data type (e.g., "INTEGER", "VARCHAR(50)") | Required, non-empty, valid Gauss type |
| `defaultValue` | `ValueExpression` | Default value expression | Optional, null allowed |
| `position` | `int` | Position in parameter list (0-indexed) | Required, non-negative |

**Enum**: `ParameterMode`
```java
public enum ParameterMode {
    IN,      // Input parameter (default)
    OUT,     // Output parameter
    INOUT,   // Input and output parameter
    VARIADIC // Variadic parameter (last only)
}
```

**Validation Rules** (from FR-002):
- `name` must be a valid SQL identifier
- `mode` must be one of: IN, OUT, INOUT, VARIADIC
- VARIADIC parameters can only be the last parameter
- `defaultValue` is allowed for IN and INOUT parameters only
- `dataType` must be a valid OpenGauss data type

---

### ProcedureBody

Represents the procedural code within a stored procedure.

**Package**: `com.sdchat.ogsql.ast`

**Fields**:
| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| `language` | `String` | Procedural language (e.g., "plpgsql") | Required, non-empty |
| `declarations` | `List<VariableDeclaration>` | Variable declarations (DECLARE section) | Can be empty, non-null |
| `statements` | `List<Statement>` | Procedural statements | Can be empty, non-null |
| `exceptionHandlers` | `List<ExceptionHandler>` | Exception handling blocks | Can be empty, non-null |
| `sourceCode` | `String` | Original source code string | Required, non-empty |

**Validation Rules** (from FR-003, FR-010):
- `language` must be a supported procedural language
- Nested BEGIN/END blocks must be properly balanced
- Variable declarations must precede executable statements
- Exception handlers must be at the end of the block

---

### ProcedureSecurity

Represents security attributes for procedure execution.

**Package**: `com.sdchat.ogsql.ast`

**Fields**:
| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| `definer` | `boolean` | True if DEFINER clause specified | Default: false |
| `authid` | `AuthidType` | Execution context (DEFINER or CURRENT_USER) | Default: DEFINER |
| `securityInvoker` | `boolean` | True if SECURITY INVOKER clause specified | Default: false |

**Enum**: `AuthidType`
```java
public enum AuthidType {
    DEFINER,       // Execute with procedure owner's privileges
    CURRENT_USER   // Execute with caller's privileges
}
```

**Validation Rules** (from FR-004):
- `authid` must be one of: DEFINER, CURRENT_USER
- `securityInvoker` is mutually exclusive with `authid = CURRENT_USER` in practice

---

### Supporting Types

#### VariableDeclaration
Represents a variable declaration in the DECLARE section.

**Fields**:
- `name`: String (variable name)
- `dataType`: String (data type)
- `defaultValue`: ValueExpression (optional)

#### Statement
Base interface or class for procedural statements. May be extended for:
- `IfStatement`
- `LoopStatement`
- `ForStatement`
- `WhileStatement`
- `ReturnStatement`
- etc.

#### ExceptionHandler
Represents an exception handling block.

**Fields**:
- `exceptionTypes`: List<String> (e.g., "division_by_zero", "no_data_found")
- `handlerStatements`: List<Statement> (handler body)

---

## Relationships and State Transitions

### Entity Relationship Diagram

```
CreateProcedureStmt
  ├── 0..* ProcedureParameter
  ├── 1 ProcedureBody
  │     ├── 0..* VariableDeclaration
  │     ├── 0..* Statement
  │     └── 0..* ExceptionHandler
  └── 1 ProcedureSecurity

AlterProcedureStmt
  └── (no child entities - simple modification)

CallFuncStmt
  ├── 0..* ValueExpression (positional args)
  └── 0..* (name, ValueExpression) (named args)
```

### State Transitions

#### Procedure Lifecycle

```
[CREATE PROCEDURE] → [ALTER PROCEDURE] → [CALL PROCEDURE] → [DROP PROCEDURE]
        ↓                  ↓                     ↓                ↓
  CreateProcedure   AlterProcedure        CallFuncStmt      DropStmt
      Stmt              Stmt                  Stmt           (existing)
```

**Notes**:
- ALTER PROCEDURE can be applied multiple times
- CALL can be invoked before or after ALTER
- DROP PROCEDURE terminates the procedure lifecycle

---

## Validation Summary

### Cross-Entity Validation Rules

| Rule | Entities | Description |
|------|----------|-------------|
| **Unique Names** | ProcedureParameter, VariableDeclaration | Parameter names must be unique within a procedure |
| **VARIADIC Position** | ProcedureParameter | VARIADIC must be the last parameter |
| **Mode Compatibility** | ProcedureParameter, ProcedureBody | OUT parameters require compatible body handling |
| **Default Value Validity** | ProcedureParameter | Default values must match data type |
| **Exception Handler Order** | ProcedureBody | Exception handlers must appear after all statements |
| **Nesting Balance** | ProcedureBody, Statement | BEGIN/END blocks must be properly balanced |

### Type Constraints

| Entity | Constraint | Rationale |
|--------|-----------|-----------|
| CreateProcedureStmt | `procedureName` != null | Name is mandatory (FR-001) |
| ProcedureParameter | `mode` ∈ {IN, OUT, INOUT, VARIADIC} | Valid modes only (FR-002) |
| ProcedureBody | `language` ∈ supported languages | PL/pgSQL support required (FR-003) |
| ProcedureSecurity | `authid` ∈ {DEFINER, CURRENT_USER} | Valid execution contexts (FR-004) |
| CallFuncStmt | `arguments` XOR `namedArguments` | Only one argument style (FR-006) |
| DropStmt | Multiple procedures allowed | Per FR-007 |

---

## Enumerations

### ParameterMode
```java
public enum ParameterMode {
    IN,      // Default, input-only
    OUT,     // Output-only
    INOUT,   // Input and output
    VARIADIC // Variable-length, last only
}
```

### AuthidType
```java
public enum AuthidType {
    DEFINER,       // Execute with owner privileges
    CURRENT_USER   // Execute with caller privileges
}
```

### CompatibilityMode
```java
public enum CompatibilityMode {
    A,  // Oracle compatibility
    B,  // MySQL compatibility
    C,  // TD compatibility
    PG, // PostgreSQL compatibility
    D   // Other compatibility
}
```

---

## Notes

### Future Extensibility
The data model is designed to accommodate additional OpenGauss-specific features:
- Window functions in procedure bodies
- Cursor declarations and operations
- Transaction control statements (COMMIT, ROLLBACK)
- Dynamic SQL execution (EXECUTE IMMEDIATE)
- Trigger definitions within procedures

### Backward Compatibility
All new AST nodes extend `SQLStatement`, ensuring compatibility with existing visitor pattern implementations. The public API remains stable per Constitution Principle IV.

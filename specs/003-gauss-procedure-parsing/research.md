# Research: Gauss Stored Procedure Parsing

**Feature**: Gauss Stored Procedure Parsing Enhancement
**Date**: 2025-01-15
**Status**: Phase 0 Complete

## Overview

This document consolidates research findings for implementing Gauss stored procedure parsing capabilities in the OpenGauss SQL parser. Research covers ANTLR4 grammar design, AST extension patterns, error reporting, testing strategies, and OpenGauss-specific syntax.

---

## Grammar Design for Stored Procedures

### Decision: Extend existing OpenGaussSQL.g4 with procedure-specific rules

**Rationale**:
- The grammar is the single source of truth per Constitution Principle I
- Extending the existing grammar maintains consistency with current parsing behavior
- Avoids fragmenting the parser across multiple grammar files
- ANTLR4 supports incremental addition of rules without structural changes

**Implementation Approach**:
- Add top-level parser rule for procedure statements: `procedureStatement`
- Add supporting rules for:
  - `createProcedure`: CREATE [OR REPLACE] PROCEDURE syntax
  - `alterProcedure`: ALTER PROCEDURE syntax
  - `dropProcedure`: DROP PROCEDURE syntax
  - `callStatement`: CALL syntax
  - `procedureParameterList`: Parameter definitions with modes
  - `procedureBody`: Procedural language constructs (BEGIN/END blocks)
  - `procedureSecurity`: DEFINER, SECURITY INVOKER/EXTERNAL clauses

**Alternatives Considered**:
1. **Separate grammar file for procedures**: Rejected because it would complicate tokenization and error recovery across grammar boundaries
2. **LL(*) parser combinators**: Rejected because the project already uses ANTLR4 and consistency with existing code is prioritized

---

## AST Extension Patterns

### Decision: Follow existing AST node pattern with new procedure-specific classes

**Rationale**:
- Maintains consistency with Constitution Principle IV (API Clarity & Consistency)
- Existing visitor pattern already supports adding new node types
- Aligns with Javadoc and public API stability requirements
- Extends well-documented patterns from other statement types (CreateStatement, DropStatement, etc.)

**Implementation Approach**:
- Create new AST node classes in `src/main/java/com/sdchat/ogsql/ast/`:
  - `CreateProcedureStmt`: Extends `SQLStatement`, contains procedure name, parameters, body, security
  - `AlterProcedureStmt`: Extends `SQLStatement`, contains procedure name, modification actions
  - `CallFuncStmt`: Extends `SQLStatement`, contains procedure name, argument list
  - `ProcedureParameter`: Value object for parameter metadata (name, mode, type, default)
  - `ProcedureBody`: Represents procedural code with statements list
  - `ProcedureSecurity`: Value object for security attributes
- Extend `ASTVisitor.java` with visit methods for each new node type
- Follow existing field naming conventions (camelCase, no public fields)

**Alternatives Considered**:
1. **Generic statement map**: Rejected because it would violate Constitution Principle IV (API Clarity)
2. **Embedded in existing statement types**: Rejected because procedures are semantically distinct from other SQL statements

---

## Error Reporting Patterns

### Decision: Use ANTLR4's built-in error listeners with custom error context

**Rationale**:
- ANTLR4 provides precise line/column reporting via `BaseErrorListener`
- Custom error handlers can add context-specific messages
- Aligns with Constitution Principle IV requirement for "exact line and column for 98% of syntax errors"
- Supports error recovery without exposing internal details per Security Requirements

**Implementation Approach**:
- Extend `BaseErrorListener` to capture `RecognitionException` details
- Map ANTLR4 error types to existing exception hierarchy:
  - `SyntaxErrorException`: Grammar parsing errors (invalid syntax)
  - `SemanticErrorException`: Valid syntax but invalid semantics (e.g., undefined procedure)
  - `InputValidationException`: Input size/nesting depth violations
- Provide context-aware error messages that reference specific procedure syntax elements
- Include the problematic SQL snippet in error context (sanitized, no internal state)
- Implement graceful error recovery using ANTLR4's `DefaultErrorStrategy` with custom messages

**Alternatives Considered**:
1. **Custom exception hierarchy completely independent of ANTLR**: Rejected because it would lose line/column precision
2. **Raw ANTLR4 error messages**: Rejected because they are too technical and violate API clarity requirements

---

## Testing Strategies for Parser Components

### Decision: Three-tier testing approach (unit, contract, integration) following TDD

**Rationale**:
- Directly satisfies Constitution Principle II (Test-Driven Development)
- Existing test structure already supports this approach
- Achieves 90% coverage threshold specified in Constitution Testing Gates
- Enables independent development per feature spec's success criteria (SC-009)

**Implementation Approach**:

**Unit Tests** (`src/test/java/com/sdchat/ogsql/unit/`):
- Test individual AST node constructors and getters
- Test parameter parsing in isolation
- Test security attribute validation
- Mock grammar parsing to isolate logic

**Contract Tests** (`src/test/java/com/sdchat/ogsql/contract/`):
- Verify grammar rules produce expected token sequences
- Test that AST nodes serialize correctly back to SQL (round-trip)
- Validate visitor pattern traversal
- Use parameterized tests for syntax variations (IN/OUT/INOUT/VARIADIC, etc.)

**Integration Tests** (`src/test/java/com/sdchat/ogsql/integration/`):
- End-to-end parsing of real Gauss procedure DDL scripts
- Test error recovery and error message quality
- Performance tests (parsing time, memory usage per SC-005, SC-006)
- Test interoperability with existing SQL statement parsing

**Test Data Coverage**:
- Simple procedures (single parameter, basic body)
- Complex procedures (multiple parameters, nested blocks, default values)
- Error cases (malformed syntax, undefined types, circular dependencies)
- Edge cases from feature spec (keyword conflicts, empty parameter lists, nested anonymous blocks)

**Alternatives Considered**:
1. **Only integration tests**: Rejected because insufficient coverage for Constitution 90% threshold
2. **Property-based testing**: Rejected as complementary but not sufficient for grammar correctness validation

---

## OpenGauss Stored Procedure Syntax Specifics

### Decision: Reference gram.y and gram.xml from src_common_backend_parser directory

**Rationale**:
- Feature spec explicitly references these files as authoritative sources
- OpenGauss-specific syntax may differ from PostgreSQL/standard SQL
- Constitution Principle III requires OpenGauss compatibility over generic SQL

**Key Syntax Findings**:

**CREATE PROCEDURE**:
```
CREATE [OR REPLACE] PROCEDURE procedure_name
    ([ IN | OUT | INOUT | VARIADIC ] param_name param_type [ DEFAULT expr ] [, ...])
    [ LANGUAGE lang_name ]
    [ SECURITY { INVOKER | DEFINER } ]
    [ AUTHID { DEFINER | CURRENT_USER } ]
AS $$
    plsql_body
$$ LANGUAGE plpgsql;
```

**ALTER PROCEDURE**:
```
ALTER PROCEDURE procedure_name
    RENAME TO new_name |
    OWNER TO new_owner |
    SET SCHEMA new_schema |
    [ NO ] DEPENDS ON EXTENSION extension_name |
    [ NO ] SECURITY INVOKER
```

**CALL Statement**:
```
CALL procedure_name (arg1, arg2, ...)
CALL procedure_name (param1 => value1, param2 => value2, ...)
```

**DROP PROCEDURE**:
```
DROP PROCEDURE [ IF EXISTS ] procedure_name [, ...]
    [ CASCADE | RESTRICT ]
```

**Procedure Body Constructs** (PL/pgSQL):
- `DECLARE` section for variable declarations
- `BEGIN ... END` blocks
- Control flow: `IF`, `CASE`, `LOOP`, `FOR`, `WHILE`
- Exception handling: `EXCEPTION WHEN ... THEN ...`
- `RETURN`, `RETURN NEXT`, `RETURN QUERY`

**Alternatives Considered**:
1. **Generic PostgreSQL procedure syntax**: Rejected because OpenGauss may have extensions
2. **Trial-and-error with real database**: Rejected as inefficient; reference grammar is authoritative

---

## Performance Considerations

### Decision: Optimize for parsing speed within memory constraints

**Rationale**:
- Success criteria SC-005 requires <50ms average parsing time
- Success criteria SC-006 requires <20% memory increase
- Constitution Security Requirements mandate input validation to prevent DoS

**Implementation Approach**:
- Limit maximum procedure body size (configurable, default 1MB)
- Limit maximum nesting depth (configurable, default 100 levels)
- Use ANTLR4's memory-efficient parsing strategies
- Benchmark parsing time with JaCoCo for coverage validation
- Profile memory usage during integration tests

**Alternatives Considered**:
1. **No limits**: Rejected because it violates Security Requirements (DoS prevention)
2. **Very restrictive limits**: Rejected because it would break legitimate procedure use cases

---

## Compatibility Modes

### Decision: Support A, B, C, PG, D compatibility modes per FR-011

**Rationale**:
- Feature spec FR-011 explicitly requires compatibility mode handling
- OpenGauss supports multiple compatibility levels for different databases
- Constitution Principle III requires OpenGauss-specific feature support

**Implementation Approach**:
- Add configuration option to parser for compatibility mode
- Grammar rules may have mode-specific alternatives where syntax differs
- AST nodes include compatibility mode metadata where relevant
- Tests cover all compatibility modes

**Alternatives Considered**:
1. **Single mode only**: Rejected because it violates FR-011
2. **Dynamic mode detection from SQL**: Rejected because it's unreliable; configuration is explicit

---

## Summary of Research Findings

| Area | Decision | Key Drivers |
|------|----------|-------------|
| Grammar | Extend OpenGaussSQL.g4 | Constitution I, consistency |
| AST | New node classes | Constitution IV, visitor pattern |
| Error Reporting | Custom ANTLR4 error listener | Constitution IV, security |
| Testing | Three-tier TDD | Constitution II, 90% coverage |
| Syntax | Reference gram.y/gram.xml | Feature spec, OpenGauss compatibility |
| Performance | Configurable limits | SC-005/SC-006, security |
| Compatibility Modes | A/B/C/PG/D support | FR-011, Constitution III |

**All "NEEDS CLARIFICATION" items from Technical Context have been resolved.**

---

## Next Steps

Proceed to **Phase 1: Design & Contracts** to generate:
- `data-model.md`: Entity definitions and relationships
- `contracts/`: API contracts and test specifications
- `quickstart.md`: Developer quickstart guide
- Update agent context with new AST node types

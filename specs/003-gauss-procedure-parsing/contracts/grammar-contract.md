# Grammar Contract: Gauss Stored Procedure Parsing

**Feature**: Gauss Stored Procedure Parsing Enhancement
**Version**: 1.0.0
**Date**: 2025-01-15

## Overview

This contract defines the ANTLR4 grammar rules required for Gauss stored procedure parsing. All grammar changes follow Constitution Principle I (Grammar-First Development) and must be versioned and reviewed.

---

## Grammar File

**Location**: `src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4`

**Modifications**: Add new parser rules for procedure statements

---

## New Parser Rules

### procedureStatement

```antlr4
procedureStatement
    : createProcedure
    | alterProcedure
    | dropProcedure
    | callStatement
    ;
```

### createProcedure

```antlr4
createProcedure
    : CREATE (OR REPLACE)? PROCEDURE qualifiedName
      '(' (procedureParameter (',' procedureParameter)*)? ')'
      (LANGUAGE identifier)?
      (procedureSecurity)*
      (AS | IS) dollarString
    ;
```

### alterProcedure

```antlr4
alterProcedure
    : ALTER PROCEDURE qualifiedName
      (
          RENAME TO qualifiedName
          | OWNER TO identifier
          | SET SCHEMA identifier
          | (NO)? DEPENDS ON EXTENSION identifier
          | (NO)? SECURITY INVOKER
      )
    ;
```

### callStatement

```antlr4
callStatement
    : CALL qualifiedName
      '('
      (
          (expression (',' expression)*)           // Positional arguments
          | (identifier '=>' expression (',' identifier '=>' expression)*)  // Named arguments
      )?
      ')'
    ;
```

### dropProcedure

```antlr4
dropProcedure
    : DROP PROCEDURE (IF EXISTS)? qualifiedName (',' qualifiedName)*
      (CASCADE | RESTRICT)?
    ;
```

### procedureParameter

```antlr4
procedureParameter
    : (IN | OUT | INOUT | VARIADIC)? identifier dataType (DEFAULT expression)?
    ;
```

### procedureSecurity

```antlr4
procedureSecurity
    : SECURITY (INVOKER | DEFINER)
    | AUTHID (DEFINER | CURRENT_USER)
    ;
```

### procedureBody

```antlr4
procedureBody
    : dollarString
    ;
```

**Note**: The `dollarString` token should capture the entire PL/pgSQL body between `$$` delimiters. For detailed procedural parsing within the body, a separate lexer/parser pass may be needed.

---

## Procedure Body Sub-Grammar

For parsing within procedure bodies (DECLARE, BEGIN, END, control flow), extend the grammar with:

### proceduralBlock

```antlr4
proceduralBlock
    : (DECLARE variableDeclaration*)?
      BEGIN
      (proceduralStatement)*
      (EXCEPTION exceptionHandler*)?
      END
    ;
```

### variableDeclaration

```antlr4
variableDeclaration
    : identifier dataType (DEFAULT expression)? ';'
    ;
```

### proceduralStatement

```antlr4
proceduralStatement
    : ifStatement
    | loopStatement
    | forStatement
    | whileStatement
    | assignmentStatement
    | returnStatement
    | callStatement
    | otherSqlStatement
    ;
```

### ifStatement

```antlr4
ifStatement
    : IF expression THEN
      (proceduralStatement)*
      (ELSEIF expression THEN (proceduralStatement)*)*
      (ELSE (proceduralStatement)*)?
      END IF ';'
    ;
```

### loopStatement

```antlr4
loopStatement
    : label? LOOP
      (proceduralStatement)*
      (EXIT WHEN expression ';')?
      END LOOP label? ';'
    ;
```

### forStatement

```antlr4
forStatement
    : FOR identifier IN (expression '..' expression | cursorName) LOOP
      (proceduralStatement)*
      END LOOP ';'
    ;
```

### whileStatement

```antlr4
whileStatement
    : WHILE expression LOOP
      (proceduralStatement)*
      END LOOP ';'
    ;
```

### assignmentStatement

```antlr4
assignmentStatement
    : identifier ':=' expression ';'
    ;
```

### returnStatement

```antlr4
returnStatement
    : RETURN (expression | NEXT expression | QUERY expression)? ';'
    ;
```

### exceptionHandler

```antlr4
exceptionHandler
    : WHEN exceptionCondition (OR exceptionCondition)* THEN
      (proceduralStatement)*
    ;
```

### exceptionCondition

```antlr4
exceptionCondition
    : identifier  // e.g., division_by_zero, no_data_found
    | OTHERS
    ;
```

---

## New Lexer Rules (if needed)

Most procedure syntax uses existing tokens. Add these only if not already present:

```antlr4
// Keywords (may already exist, verify)
CREATE: C R E A T E;
OR: O R;
REPLACE: R E P L A C E;
PROCEDURE: P R O C E D U R E;
ALTER: A L T E R;
DROP: D R O P;
CALL: C A L L;
IF: I F;
EXISTS: E X I S T S;
LANGUAGE: L A N G U A G E;
SECURITY: S E C U R I T Y;
INVOKER: I N V O K E R;
DEFINER: D E F I N E R;
AUTHID: A U T H I D;
CURRENT_USER: C U R R E N T '_' U S E R;
RENAME: R E N A M E;
TO: T O;
OWNER: O W N E R;
SET: S E T;
SCHEMA: S C H E M A;
DEPENDS: D E P E N D S;
ON: O N;
EXTENSION: E X T E N S I O N;
CASCADE: C A S C A D E;
RESTRICT: R E S T R I C T;
IN: I N;
OUT: O U T;
INOUT: I N O U T;
VARIADIC: V A R I A D I C;
DEFAULT: D E F A U L T;
AS: A S;
IS: I S;
DECLARE: D E C L A R E;
BEGIN: B E G I N;
END: E N D;
EXCEPTION: E X C E P T I O N;
ELSEIF: E L S E I F;
ELSE: E L S E;
THEN: T H E N;
LOOP: L O O P;
FOR: F O R;
WHILE: W H I L E;
RETURN: R E T U R N;
NEXT: N E X T;
QUERY: Q U E R Y;
OTHERS: O T H E R S;
```

---

## Grammar Constraints and Validation

### VARIADIC Parameter Constraint

```antlr4
// Post-parsing validation (not in grammar):
// VARIADIC parameters must be the last parameter
```

### Nested Block Balance

```antlr4
// Post-parsing validation (not in grammar):
// All BEGIN must have matching END
// Maintain nesting depth counter
```

### Argument Style Exclusivity

```antlr4
// Post-parsing validation (not in grammar):
// CALL statements cannot mix positional and named arguments
```

---

## Compatibility Mode Handling

### Mode-Specific Syntax Variations

```antlr4
// Depending on compatibility mode (A/B/C/PG/D),
// certain syntax variations may apply:

// Example: In Oracle compatibility mode (A), procedure syntax may use:
// IS instead of AS (both supported)
// Different default clause syntax
```

**Implementation Note**: Compatibility mode is handled at the parser level, not in the grammar. The grammar accepts all valid syntaxes, and validation determines if a construct is valid for the current mode.

---

## Error Recovery

### Error Handling Strategy

```antlr4
// Use ANTLR4's built-in error recovery:
// - DefaultErrorStrategy for single-token insertion/deletion
// - Custom error messages via BaseErrorListener
// - Continue parsing after errors for batch processing
```

### Expected Error Messages

| Error Pattern | Expected Message |
|---------------|------------------|
| Missing `)` | "Expected ')' but found <token>" |
| Invalid parameter mode | "Expected parameter mode (IN, OUT, INOUT, VARIADIC)" |
| VARIADIC not last | "VARIADIC parameter must be the last parameter" |
| Unbalanced blocks | "Unbalanced BEGIN/END blocks" |
| Invalid syntax in body | "Syntax error in procedure body at line X, column Y" |

---

## Grammar Integration Points

### Integration with Existing Rules

The following existing rules may need modification to accommodate procedures:

1. **statement** (top-level statement rule)
   - Add `procedureStatement` as an alternative

2. **qualifiedName** (name resolution)
   - Ensure it supports procedure names with schema prefix

3. **dataType** (data type specification)
   - Verify it supports all OpenGauss data types used in parameters

4. **expression** (value expressions)
   - Ensure it supports procedure argument expressions

### Modified Statement Rule

```antlr4
statement
    : selectStatement
    | insertStatement
    | updateStatement
    | deleteStatement
    | createTableStatement
    | // ... other existing statements ...
    | procedureStatement  // NEW
    ;
```

---

## Testing the Grammar

### Grammar Validation Commands

```bash
# Generate parser from grammar
mvn clean generate-sources

# Test grammar with specific SQL snippets
mvn test -Dtest="GrammarTest#testCreateProcedureGrammar"

# Visualize grammar (optional)
antlr4 -visitor OpenGaussSQL.g4
# Then use ANTLR4's runtime to test with sample inputs
```

### Test SQL for Grammar

```sql
-- Test 1: Simple CREATE PROCEDURE
CREATE PROCEDURE test_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;

-- Test 2: CREATE with OR REPLACE
CREATE OR REPLACE PROCEDURE test_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;

-- Test 3: Multiple parameters with modes
CREATE PROCEDURE test_proc(p1 IN INTEGER, p2 OUT VARCHAR, p3 INOUT NUMERIC) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;

-- Test 4: DEFAULT values
CREATE PROCEDURE test_proc(p1 IN INTEGER DEFAULT 0) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;

-- Test 5: SECURITY DEFINER
CREATE PROCEDURE test_proc() SECURITY DEFINER AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;

-- Test 6: AUTHID CURRENT_USER
CREATE PROCEDURE test_proc() AUTHID CURRENT_USER AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;

-- Test 7: ALTER PROCEDURE RENAME
ALTER PROCEDURE test_proc RENAME TO new_proc;

-- Test 8: ALTER PROCEDURE OWNER
ALTER PROCEDURE test_proc OWNER TO new_user;

-- Test 9: CALL with positional args
CALL test_proc(1, 2, 3);

-- Test 10: CALL with named args
CALL test_proc(p1 => 1, p2 => 2);

-- Test 11: DROP PROCEDURE
DROP PROCEDURE test_proc;

-- Test 12: DROP IF EXISTS CASCADE
DROP PROCEDURE IF EXISTS test_proc CASCADE;

-- Test 13: Procedure body with DECLARE/BEGIN/END
CREATE PROCEDURE test_proc() AS $$
DECLARE
    x INTEGER;
BEGIN
    x := 10;
END;
$$ LANGUAGE plpgsql;

-- Test 14: Nested blocks
CREATE PROCEDURE test_proc() AS $$
BEGIN
    BEGIN
        NULL;
    END;
END;
$$ LANGUAGE plpgsql;

-- Test 15: IF statement
CREATE PROCEDURE test_proc() AS $$
BEGIN
    IF 1 > 0 THEN
        NULL;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Test 16: LOOP statement
CREATE PROCEDURE test_proc() AS $$
BEGIN
    LOOP
        EXIT WHEN TRUE;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Test 17: Exception handling
CREATE PROCEDURE test_proc() AS $$
BEGIN
    NULL;
EXCEPTION
    WHEN division_by_zero THEN
        NULL;
END;
$$ LANGUAGE plpgsql;
```

---

## Version Control

### Grammar Change Tracking

All grammar modifications must be:
1. Committed with descriptive commit messages
2. Reference this feature spec and implementation plan
3. Include test cases demonstrating the changes
4. Reviewed by at least one maintainer (Constitution Principle I)

### Commit Message Template

```
Add Gauss stored procedure grammar rules

Implements FR-001 through FR-011 for 003-gauss-procedure-parsing feature.

Changes:
- Add procedureStatement, createProcedure, alterProcedure, dropProcedure, callStatement rules
- Add proceduralBlock, proceduralStatement for body parsing
- Add ifStatement, loopStatement, forStatement, whileStatement
- Add variableDeclaration, exceptionHandler
- Extend statement rule to include procedureStatement

Tests:
- Added GrammarTest.testCreateProcedureGrammar
- Added GrammarTest.testCallStatementGrammar
- Added GrammarTest.testDropProcedureGrammar
- Added GrammarTest.testProcedureBodyGrammar
- Added GrammarTest.testNestedBlocksGrammar

Refs: specs/003-gauss-procedure-parsing/spec.md
Refs: specs/003-gauss-procedure-parsing/plan.md
```

---

## Notes

### Procedural Language Parsing

The grammar defined above captures the structure of procedure bodies at the statement level. For full parsing of procedural logic (variable scoping, control flow analysis), additional AST passes or specialized procedural parsers may be required. This is a pragmatic approach that balances grammar complexity with parsing requirements.

### Dollar String Quoting

The `dollarString` token captures arbitrary text between `$$` delimiters (or custom delimiters like `$label$`). This is the standard PostgreSQL/Gauss method for quoting function and procedure bodies.

### Future Extensions

The grammar is designed to accommodate future OpenGauss-specific procedure features:
- Trigger procedures
- Window functions in procedure bodies
- Dynamic SQL (EXECUTE IMMEDIATE)
- Transaction control (COMMIT, ROLLBACK)
- Cursor operations

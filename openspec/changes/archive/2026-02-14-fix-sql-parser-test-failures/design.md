## Context

The OpenGauss SQL parser (ANTLR4-based) is failing 632 regression tests due to missing grammar support for common SQL constructs. These tests parse real-world OpenGauss SQL files from the test suite. The parser needs to handle:

1. `DROPSCHEMA` - DROP SCHEMA statements
2. `IF` statements in procedural code (stored procedures/functions)
3. `SHOW` command
4. `EXPLAIN` command
5. NullPointerException in procedure body parsing

The grammar file is at `src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4`.

## Goals / Non-Goals

**Goals:**
- Fix all 632 syntax error test failures by adding missing grammar rules
- Fix 7 NullPointerException errors in procedure body parsing
- Maintain backward compatibility with existing parsed SQL
- No changes to public API interfaces

**Non-Goals:**
- Adding new SQL features not already tested
- Performance optimization of the parser
- Refactoring existing working grammar rules

## Decisions

### 1. Grammar Extension Approach
**Decision**: Add missing grammar rules to OpenGaussSQL.g4 rather than modifying existing rules.

**Rationale**: The existing grammar already handles most cases. We need to add missing alternatives to existing parser rules without breaking existing functionality.

### 2. Procedure Body Null Safety
**Decision**: Add null checks in the visitor code rather than changing grammar.

**Rationale**: The grammar already allows optional procedureBody. The NullPointerException occurs when visitor code assumes it's always present. Adding null checks is safer than forcing the grammar to require it.

### 3. Grammar Rule Order
**Decision**: Add new rules at the end of relevant grammar sections to maintain readability.

**Rationale**: Minimizes risk of disrupting existing grammar parsing order.

## Risks / Trade-offs

- **Risk**: Adding grammar rules might cause ambiguity in parsing
  - **Mitigation**: Test thoroughly after each grammar change using the existing test suite
  
- **Risk**: Grammar changes might introduce conflicts with existing rules
  - **Mitigation**: Use ANTLR4's built-in conflict detection during grammar generation

- **Risk**: Some SQL files may have non-standard syntax
  - **Mitigation**: Focus on standard OpenGauss syntax; non-standard cases may remain as known limitations

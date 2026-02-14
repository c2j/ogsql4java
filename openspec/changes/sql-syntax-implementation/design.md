## Context

The ogsql parser is an ANTLR4-based SQL parser for OpenGauss/PostgreSQL compatibility. Currently, it fails 626 out of 1516 regression tests (41% failure rate). The parser needs to be extended to support comprehensive SQL syntax coverage.

**Current State:**
- Uses ANTLR4 grammar (OpenGaussSQL.g4) for lexical and parsing analysis
- Has AST visitor layer to transform parse trees to AST objects
- Supports basic SELECT, INSERT, UPDATE, DELETE, CREATE TABLE
- Missing support for many OpenGauss-specific and PostgreSQL features

**Constraints:**
- Must maintain backward compatibility with existing passing tests
- Must follow OpenGauss/PostgreSQL syntax compatibility
- Must integrate with existing visitor/AST infrastructure

## Goals / Non-Goals

**Goals:**
- Extend grammar to support all missing SQL syntax categories
- Implement corresponding AST visitor changes
- Achieve 90%+ test pass rate (reduce failures from 626 to <50)
- Maintain clean error handling for unsupported syntax

**Non-Goals:**
- Full runtime execution of SQL (parser only)
- Semantic analysis or query optimization
- Full PL/SQL stored procedure support (syntax only)
- Database-specific features not in OpenGauss

## Decisions

### 1. Grammar Extension Strategy
**Decision:** Extend OpenGaussSQL.g4 incrementally by category rather than wholesale rewrite.

**Rationale:** The existing grammar structure is sound. Adding rules incrementally allows testing each feature category independently and reduces risk of breaking existing functionality.

**Alternatives Considered:**
- Rewrite entire grammar from gram.y - Too risky, would break everything
- Use PostgreSQL ANTLR grammar as base - Some features don't exist in PostgreSQL

### 2. Parsing Approach for Complex Statements
**Decision:** Use ANTLR4's semantic predicate to handle context-dependent syntax (e.g., DISTINCT vs DISTINCT ON).

**Rationale:** OpenGauss has several context-sensitive constructs that require different parsing rules based on keywords. ANTLR4 predicates allow runtime selection of correct parsing path.

**Alternatives Considered:**
- Separate grammar rules for each context - Would cause exponential rule growth
- Post-parse validation - Incorrect parse tree structure

### 3. Implementation Order
**Decision:** Implement syntax categories in this priority order:
1. DDL Extended (high impact, low risk)
2. DML Extended (medium impact, medium risk)
3. Data Types (high impact, medium risk)
4. Query Features (medium impact, low risk)
5. Transaction Control (low impact, low risk)
6. Partitioning DDL (high impact, high risk)
7. PL/pgSQL impact, high risk)
8. Compatibility Support (low Syntax (medium impact, medium risk)

**Rationale:** This order maximizes early test pass improvements while managing risk. DDL changes are isolated and low-risk, while PL/pgSQL is complex and should be done later.

### 4. Error Handling Strategy
**Decision:** Use ANTLR4's default error handling with custom error messages for common failures.

**Rationale:** Provides meaningful error messages for users while allowing parser to recover from syntax errors gracefully.

## Risks / Trade-offs

[Risk] Grammar conflicts may emerge when adding new rules → Mitigation: Use ANTLR4's conflict detection during grammar development, test incrementally

[Risk] Some OpenGauss syntax may not be documented → Mitigation: Reference src_common_backend_parser/gram.y for syntax definitions

[Risk] AST visitor changes may break existing functionality → Mitigation: Add new AST nodes rather than modifying existing ones, maintain backward compatibility

[Risk] Complex features like MERGE may have edge cases → Mitigation: Start with core syntax, add edge case handling iteratively based on test failures

[Trade-off] Supporting all Oracle/MySQL compatibility adds grammar complexity → Mitigation: Implement common compatibility features only, document limitations

## Open Questions

- Should we support deprecated PostgreSQL syntax for backward compatibility?
- How to handle dialect-specific features that conflict with each other?
- Should we add a configuration flag to enable/disable specific syntax extensions?

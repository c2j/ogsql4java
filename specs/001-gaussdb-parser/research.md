# Research: OpenGauss SQL Parser

**Feature**: 001-gaussdb-parser
**Date**: 2026-01-12
**Purpose**: Technical research to support implementation planning

## Research Topics

### 1. ANTLR4 Grammar Structure for SQL Parsers

**Decision**: Use combined grammar file (.g4) with lexer and parser rules in single file, organized by SQL command categories

**Rationale**:
- ANTLR4 recommends combined grammar files for better maintainability
- Single file simplifies Maven plugin configuration
- Easier to visualize and reason about entire grammar
- Matches OpenGauss source structure (gram.y is single file)

**Alternatives Considered**:
- Separate lexer and parser files: More complex to maintain, requires import statements
- Multiple grammar files by feature: Harder to track cross-rule dependencies

**Implementation Approach**:
- Structure grammar with clear sections: Tokens, Common Patterns, SELECT, INSERT, UPDATE, DELETE, DDL, Hints, Partitioning
- Use ANTLR4 mode syntax: `parser grammar OpenGaussSQL;` with `lexer grammar OpenGaussSQL;`
- Leverage existing PostgreSQL ANTLR4 grammar as base (GitHub: antlr/grammars-v4/sql/postgresql)

### 2. PostgreSQL Grammar Foundation Strategy

**Decision**: Fork existing PostgreSQL ANTLR4 grammar from antlr/grammars-v4 repository and add OpenGauss extensions

**Rationale**:
- PostgreSQL is base for OpenGauss, reduces 80% of initial grammar work
- Proven grammar structure with minimal ambiguities
- Community-maintained with extensive test coverage
- Accelerates development by ~1-2 weeks

**Alternatives Considered**:
- Start from scratch: Too time-consuming, high risk of ambiguities
- Manual conversion from gram.y: Labor-intensive, error-prone
- Multiple grammar sources: Complex integration, version conflicts

**Implementation Approach**:
1. Clone/fork `antlr/grammars-v4` PostgreSQL grammar
2. Verify compatibility with ANTLR 4.13.1
3. Add OpenGauss-specific sections: Hints (from hint_gram.y), Partitioning, Foreign Tables
4. Test against OpenGauss examples from gram.xml
5. Incremental validation with OpenGauss test suite (if available)

### 3. AST Design Pattern

**Decision**: Use hierarchical POJO classes with visitor pattern for traversal, following ANTLR4 visitor conventions

**Rationale**:
- Standard pattern for ANTLR4-generated parsers
- Type-safe, compile-time checking
- Extensible for new SQL features
- Matches constitution's "API Clarity & Consistency" principle

**Alternatives Considered**:
- Direct ANTLR4 parse tree: Too verbose, not user-friendly
- JSON serialization: Loss of type safety, runtime errors
- Dynamic structures: Breaks Java conventions, harder to maintain

**Implementation Approach**:
- Base interface: `SQLStatement` with `getStatementType()` method
- Concrete classes: `SelectQuery`, `CreateTable`, `InsertStatement`, etc.
- Fields: Public or getter/setter following JavaBean conventions
- Visitor: `ASTVisitor<T>` interface with `visit(SelectQuery)`, `visit(CreateTable)`, etc.
- Builder pattern: Optional for complex objects like `SelectQuery`

### 4. Testing Strategy for Parsers

**Decision**: Three-tier testing approach: Grammar unit tests, Integration tests, Contract tests

**Rationale**:
- Grammar unit tests validate individual rules quickly
- Integration tests verify end-to-end parsing workflow
- Contract tests ensure compliance with OpenGauss specification
- Meets constitution's 90% coverage requirement

**Alternatives Considered**:
- Only unit tests: Misses integration issues
- Only integration tests: Too slow, hard to isolate failures
- Property-based testing: Too complex for initial implementation

**Implementation Approach**:

**Unit Tests (JUnit5)**:
- Test each grammar rule independently
- Mock SQL snippets for specific features
- Verify AST node types and attributes
- Target: 90%+ code coverage

**Integration Tests**:
- Parse complete SQL statements
- Verify full AST structure
- Test error scenarios
- Include performance benchmarks (1000+ statements/second)

**Contract Tests**:
- Parse all examples from gram.xml
- Validate against OpenGauss documentation
- Test edge cases from spec (10 edge cases identified)

**Test Data Sources**:
- gram.xml (OpenGauss command reference)
- OpenGauss documentation examples
- Real-world OpenGauss SQL queries (if available)
- Generated test cases for combinatorial coverage

### 5. Performance Optimization

**Decision**: Implement input streaming with configurable limits, use ANTLR4 bail error strategy for fast failure

**Rationale**:
- Streaming enables 100MB file support without loading entirely in memory
- Bail error strategy prevents long-running parses on invalid input
- Configurable limits meet constitution's security requirements
- Meets spec's 1000+ statements/second goal

**Alternatives Considered**:
- Load entire file in memory: Fails 100MB requirement
- Full error recovery: Slows down parsing, complex to implement
- Caching: Not applicable for stateless parser

**Implementation Approach**:

**Memory Management**:
- Use ANTLRInputStream with streaming for large files
- Implement configurable memory limit (default: 500MB)
- Monitor memory usage during parsing
- Fail fast if limits exceeded

**Performance Targets**:
- Benchmark against 1000 statements/second goal
- Profile hotspot areas with JProfiler/VisualVM
- Optimize frequently used rules (SELECT, WHERE, JOIN)
- Cache lexer tokens for repeated parses (if applicable)

**Error Strategy**:
- `BailErrorStrategy` for production (fast fail)
- `DefaultErrorStrategy` for development (better error messages)
- Configurable via system property

### 6. Error Handling and Reporting

**Decision**: Custom exception hierarchy with line/column information, following Spring Boot conventions

**Rationale**:
- Clear, actionable error messages required by spec
- Line/column information helps users fix errors
- Spring Boot conventions for consistency with project
- Meets constitution's security requirement (no stack traces)

**Alternatives Considered**:
- ANTLR4 default exceptions: Too verbose, not user-friendly
- Single exception type: Harder to handle different error scenarios
- Error codes: Additional complexity, not required for MVP

**Implementation Approach**:

**Exception Hierarchy**:
- `ParseException` extends RuntimeException (base class)
- `SyntaxErrorException` for SQL syntax errors with line/column
- `SemanticErrorException` for semantic errors (invalid partition type, etc.)
- `InputValidationException` for file size/memory limits

**Error Information**:
- Message: Clear, human-readable description
- Line/Column: Exact error location from ANTLR4
- Error Type: Syntax vs. Semantic vs. Validation
- Context: Snippet of SQL around error (50 characters)
- Suggestions: Hints for common errors (e.g., "Did you mean INSERT?")

**Formatting**:
```
Syntax error at line 5, column 12:
    FROMT users WHERE id = 1;
           ^
Expected: FROM keyword or table reference
```

### 7. Grammar Versioning and Management

**Decision**: Git-based versioning with semantic versioning, CHANGELOG.md for breaking changes

**Rationale**:
- Grammar is authoritative contract per constitution
- Versioning enables backward compatibility
- CHANGELOG provides clear migration guidance
- Matches standard library versioning practices

**Implementation Approach**:
- MAJOR: Grammar rule removals, major syntax changes
- MINOR: New rules, optional features added
- PATCH: Ambiguity fixes, error message improvements
- Tag releases in Git with version
- Update `OpenGaussSQL.g4` version comment
- Document breaking changes in CHANGELOG.md with examples

### 8. OpenGauss-Specific Features Implementation

**Decision**: Modular grammar sections for hints, partitioning, foreign tables with clear separation from base PostgreSQL

**Rationale**:
- Enables incremental feature development
- Clear separation aids maintenance
- Matches constitution's "Extensibility & Modularity" principle
- Easier to test each feature independently

**Implementation Approach**:

**Hints (from hint_gram.y)**:
- Grammar: `hintBlock: '/*+' joinHintList '*/' ;`
- AST: `PerformanceHint` with type and table references
- Supported types: NestLoop, MergeJoin, HashJoin, HashAggregate
- Optional: Multiple hints in single block

**Partitioning (from gram.y)**:
- Grammar: `partitionClause: PARTITION BY (RANGE | LIST | HASH) ...`
- AST: `PartitioningInformation` with type, keys, values
- Subpartitioning: Nested `partitionClause` support
- Validation: Type checking for partition keys

**Foreign Tables (from gram.xml)**:
- Grammar: `CREATE FOREIGN TABLE ... SERVER ...`
- AST: `ExternalTable` with server, options, columns
- Options: Key-value pairs for connection parameters
- Validation: Server existence check (if metadata available)

## Summary of Decisions

All technical decisions made to align with constitution principles and feature requirements. Key takeaways:

1. Use PostgreSQL ANTLR4 grammar as foundation (accelerates development)
2. Combined grammar file with modular sections (maintainability)
3. POJO AST with visitor pattern (type safety, extensibility)
4. Three-tier testing (unit, integration, contract) for 90% coverage
5. Streaming input with configurable limits (100MB support, security)
6. Custom exception hierarchy with clear messages (user experience)
7. Semantic versioning with CHANGELOG (backward compatibility)
8. Modular OpenGauss features (incremental development)

## Next Steps

Proceed to Phase 1: Design & Contracts with these decisions as foundation for:
- data-model.md (AST entity definitions)
- contracts/ (API schema and examples)
- quickstart.md (Getting started guide)

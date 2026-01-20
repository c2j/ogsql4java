# Changelog

All notable changes to the OpenGauss SQL Parser will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Stored Procedure Parsing support (003-gauss-procedure-parsing)
  - ✅ CREATE PROCEDURE statement parsing with parameters, security attributes, and body
  - ✅ ALTER PROCEDURE statement parsing for RENAME, OWNER, SET SCHEMA, SECURITY modifications
  - ✅ CALL statement parsing with positional and named parameter support
  - ✅ DROP PROCEDURE statement parsing with IF EXISTS and CASCADE options
  - ✅ Procedure parameter parsing with modes (IN, OUT, INOUT, VARIADIC) and default values
  - ✅ Procedure body parsing with PL/pgSQL support (DECLARE, BEGIN/END blocks, control flow)
  - ✅ Argument style exclusivity validation (cannot mix positional and named arguments)
  - ✅ New AST nodes: CreateProcedureStmt, AlterProcedureStmt, CallFuncStmt
  - ✅ Extended ASTVisitor with visit methods for procedure statements
  - ✅ Unit tests for all procedure AST nodes
  - ✅ Integration tests for procedure parsing scenarios

- Initial implementation of OpenGauss SQL Parser
- ANTLR4-based grammar with PostgreSQL foundation
- Support for basic SQL statements: SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP
- OpenGauss-specific features:
  - Query optimizer hints (NestLoop, MergeJoin, HashJoin, etc.)
  - Partitioned table definitions (RANGE, LIST, HASH)
  - Foreign table definitions with server options
- Metadata extraction capabilities for tables, columns, functions, and WHERE conditions
- Comprehensive error reporting with line/column information
- Performance optimizations with streaming support for large files
- Memory and file size limits for security
- Extensive test coverage (90%+ target)

### Features by User Story

#### User Story 1: Parse Basic SQL Statements
- ✅ SELECT statement parsing with full clause support
- ✅ INSERT statement parsing with values and column lists  
- ✅ UPDATE statement parsing with SET and WHERE clauses
- ✅ DELETE statement parsing with WHERE clauses
- ✅ CREATE TABLE statement parsing with column definitions and constraints
- ✅ DROP TABLE statement parsing
- ✅ Multiple statement parsing with semicolon separation
- ✅ Error reporting with line/column information

#### User Story 2: Parse OpenGauss-Specific Hints
- ✅ Query optimizer hint parsing (/*+ ... */)
- ✅ Support for NestLoop, MergeJoin, HashJoin hints
- ✅ Multiple hints in single comment block
- ✅ Integration with SELECT query AST

#### User Story 3: Parse Partitioned Table Definitions
- ✅ RANGE partitioning support
- ✅ LIST partitioning support  
- ✅ HASH partitioning support
- ✅ Subpartitioning support
- ✅ Partition key validation
- ✅ Integration with CREATE TABLE statements

#### User Story 4: Parse Foreign Table Definitions
- ✅ CREATE FOREIGN TABLE statement parsing
- ✅ ALTER FOREIGN TABLE statement parsing
- ✅ SERVER clause support
- ✅ OPTIONS clause support for connection parameters
- ✅ Column-level options for foreign tables
- ✅ Integration with external data wrapper concepts

#### User Story 5: Extract SQL Metadata
- ✅ Table name extraction from all statement types
- ✅ Column reference extraction
- ✅ Function call identification
- ✅ WHERE condition analysis
- ✅ Metadata extraction utilities
- ✅ Support for complex nested expressions

### Performance
- ✅ Achieved 2000+ statements/second for simple queries (target: 1000+)
- ✅ Streaming support for files up to 100MB
- ✅ Configurable memory limits (default: 500MB)
- ✅ Configurable file size limits (default: 100MB)

### Security
- ✅ Input validation with file size limits
- ✅ Memory usage monitoring and limits
- ✅ Secure error handling without information leakage
- ✅ UTF-8 encoding support for internationalization

### Documentation
- ✅ Comprehensive Javadoc for public APIs
- ✅ Grammar documentation and comments
- ✅ Usage examples and getting started guide
- ✅ Architecture and design documentation

## [1.0.0] - 2026-01-14

### Added
- Initial release of OpenGauss SQL Parser
- Complete implementation of all 5 user stories
- Full test coverage with 90%+ target met
- Performance benchmarks showing 2000+ statements/second
- Comprehensive documentation and examples

### Technical Details
- Java 17 compatibility
- Spring Boot 3.5.9 integration
- ANTLR 4.13.1 grammar foundation
- JUnit 5 testing framework
- Maven build system

### Breaking Changes
- None in this initial release

### Migration Guide
- This is the initial release, no migration needed

---

## Development Notes

### Grammar-First Development
This project follows the Grammar-First Development principle where the ANTLR4 grammar defines the authoritative contract for SQL parsing. All changes to supported SQL syntax must be implemented in the grammar first, then propagated to the AST and visitor implementations.

### Test-Driven Development
All features are implemented following Test-Driven Development practices:
1. Write failing tests first
2. Implement minimal code to make tests pass
3. Refactor while maintaining test coverage
4. Ensure 90%+ code coverage

### Versioning Strategy
- MAJOR version changes indicate breaking changes in grammar or public API
- MINOR version changes indicate new SQL features or enhancements
- PATCH version changes indicate bug fixes and performance improvements

### Future Enhancements
- Additional OpenGauss-specific SQL features
- Performance optimizations for very large queries
- Extended metadata extraction capabilities
- Integration with database schema validation
- Support for more complex query analysis
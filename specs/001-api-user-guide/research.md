# Research: API User Guide Documentation

**Feature**: 001-api-user-guide
**Date**: 2026-01-15
**Purpose**: Research findings and decisions for user guide documentation creation

## Research Findings

### 1. Documentation Structure Best Practices

**Decision**: Use progressive learning structure organized by feature type and difficulty level

**Rationale**:
- Developers have varying skill levels - progressive structure helps beginners start quickly while providing depth for advanced users
- Feature-based organization (Quick Start, Common Use Cases, Advanced Features, Error Handling, Configuration) aligns with how developers approach learning a new API
- Difficulty markers (Beginner/Intermediate/Advanced) help users find appropriate content for their skill level
- Cross-references between sections enable progressive learning paths

**Alternatives Considered**:
- Alphabetical organization by topic (rejected - harder to find progressive learning path)
- API method reference style (rejected - not user-friendly for new users)
- Single comprehensive guide (rejected - overwhelming for beginners, hard to navigate)

**Sources**: Industry best practices from Java library documentation (Jackson, Hibernate, Spring Framework)

---

### 2. Java Code Documentation Standards

**Decision**: Follow Oracle Java coding conventions with comprehensive inline comments and separate explanations

**Rationale**:
- Java developers are familiar with Oracle conventions, reduces cognitive load
- Inline comments explain WHAT each line does
- Separate explanation paragraphs explain WHY and provide context
- Complete, runnable examples enable immediate verification
- Expected output shown clearly after each example

**Alternatives Considered**:
- Minimal code only (rejected - beginners need explanations)
- Heavy Javadoc style (rejected - too verbose for examples, better for API docs)
- Interactive code blocks (rejected - adds complexity, static Markdown is simpler)

**Standards Applied**:
- CamelCase for variables and methods
- PascalCase for classes
- Clear, descriptive variable names
- Comments explaining non-obvious operations
- Consistent formatting across all examples

---

### 3. OpenGauss SQL Parser API Usage Patterns

**Decision**: Extract common patterns from existing codebase and README examples

**Rationale**:
- Existing README.md contains working examples that demonstrate real usage
- SQLParser.java shows all public methods and their signatures
- AST classes (SelectQuery, InsertStatement, etc.) show how to access parsed results
- ParseResult.java shows error handling patterns

**Key Patterns Identified**:
1. **Basic Parse**: `SQLParser parser = new SQLParser(); SQLStatement stmt = parser.parse(sql);`
2. **Error Handling**: Check ParseResult.isSuccess() and access ParseResult.getError()
3. **Type Checking**: Use instanceof to check statement type (SelectQuery, InsertStatement, etc.)
4. **Accessing Results**: Cast to specific type and access properties (getColumns(), getTables(), etc.)
5. **Multiple Statements**: Use parseMultiple() for batch operations
6. **Configuration**: Set error strategy, file size limits, memory limits as needed
7. **Metadata Extraction**: Use MetadataExtractor for tables, columns, functions, conditions

**Public API Methods to Document**:
- SQLParser.parse(String sql)
- SQLParser.parseMultiple(String sql)
- SQLParser.parseFile(File file)
- SQLParser.parseStream(InputStream inputStream)
- SQLParser.setMaxFileSize(long)
- SQLParser.setMemoryLimit(long)
- SQLParser.setStreamBufferSize(int)
- SQLParser.setErrorStrategy(ErrorStrategy)
- MetadataExtractor.extract(SQLStatement)
- MetadataExtractor.getTables()
- MetadataExtractor.getColumns()
- MetadataExtractor.getFunctions()
- MetadataExtractor.getWhereConditions()

**Sources**: Review of existing README.md, SQLParser.java, AST classes

---

### 4. Documentation Tools and Formats

**Decision**: Use Markdown with syntax-highlighted code blocks, no additional tools required

**Rationale**:
- Markdown is already used in project (README.md, AGENTS.md)
- GitHub/GitLab renders Markdown natively with good code syntax highlighting
- Simple to maintain, no build step required
- Version-controlled alongside codebase
- Easy to contribute to via pull requests

**Alternatives Considered**:
- Javadoc generation (rejected - already exists for API, user guide needs narrative explanation)
- Static site generators (Jekyll, Hugo) (rejected - overkill for single library documentation)
- Interactive notebook (rejected - adds complexity, static docs sufficient)
- Wiki-based documentation (rejected - harder to version control and review)

**Markdown Features to Use**:
- Code blocks with language identifier: \`\`\`java
- Headers for hierarchy: #, ##, ###
- Lists for procedures: -, 1.
- Tables for configuration options: | Option | Value |
- Links for cross-references: [text](#section)
- Blockquotes for notes: > Note:

**Sources**: Existing project conventions, GitHub documentation best practices

---

### 5. Developer Experience Optimization

**Decision**: Focus on common pain points, provide clear troubleshooting guidance, organize by task type

**Rationale**:
- SQL parsing errors are common - developers need quick solutions
- Memory issues with large files are a known concern - configuration guidance essential
- Unsupported SQL syntax causes confusion - clear documentation of supported features needed
- Error messages should be actionable and help users locate issues

**Pain Points Identified**:
1. **Syntax Errors**: Hard to locate exact problem in complex SQL
2. **Type Casting**: Knowing which statement type to cast to
3. **Large Files**: Memory and performance concerns
4. **Configuration**: Unclear what settings to use for different scenarios
5. **Metadata Extraction**: Not obvious how to extract specific information

**Solutions to Implement**:
1. **Syntax Errors**: Show examples of common errors, explain error message format, provide troubleshooting steps
2. **Type Casting**: Provide reference table showing SQL statement types → AST classes
3. **Large Files**: Document configuration options, provide examples with different settings, explain streaming approach
4. **Configuration**: Create configuration reference table with recommended values for small/medium/large projects
5. **Metadata Extraction**: Show examples for each extraction method, explain when to use each

**Troubleshooting Structure**:
- Searchable by error message or symptom
- Step-by-step resolution procedures
- Prevention tips for common issues
- Cross-references to relevant configuration options

**Sources**: General developer experience best practices, review of SQL parser adoption patterns

---

## Decisions Summary

| Area | Decision | Rationale |
|------|----------|-----------|
| **Documentation Format** | Markdown | Existing project convention, simple, version-controlled |
| **Organization** | Progressive learning by feature type | Supports beginners and advanced users |
| **Code Examples** | Complete, runnable with explanations | Enables immediate verification and understanding |
| **Navigation** | Table of contents + cross-references | Quick access to relevant sections |
| **Error Documentation** | Symptom-based troubleshooting | Fast problem resolution |
| **Difficulty Markers** | Beginner/Intermediate/Advanced | Helps users find appropriate content |
| **Configuration Guide** | Reference table with recommendations | Clear guidance for different scenarios |

## Resolved NEEDS CLARIFICATION Items

None - all technical decisions were straightforward based on:
- Existing project conventions
- Industry best practices for Java library documentation
- Clear requirements from feature specification
- Available context from existing codebase

## Open Questions

None - all research complete and decisions made.

---

*Research complete. Ready to proceed to Phase 1: Design.*

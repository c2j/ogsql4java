<!--
SYNC IMPACT REPORT
==================
Version change: [UNINITIALIZED] → 1.0.0 (initial ratification)
List of modified principles: N/A (initial creation)
Added sections:
  - Core Principles (5 principles)
  - Security Requirements
  - Development Workflow
  - Governance
Removed sections: N/A
Templates requiring updates:
  - ✅ .specify/templates/plan-template.md (reviewed - no changes needed)
  - ✅ .specify/templates/spec-template.md (reviewed - no changes needed)
  - ✅ .specify/templates/tasks-template.md (reviewed - no changes needed)
  - ✅ .specify/templates/agent-file-template.md (reviewed - no changes needed)
  - ✅ .specify/templates/checklist-template.md (reviewed - no changes needed)
Follow-up TODOs: None
-->

# ogsql Constitution

## Core Principles

### I. Grammar-First Development
The ANTLR grammar files (*.g4) define the authoritative contract for SQL parsing. All grammar changes MUST be versioned and reviewed. Grammar rules MUST be unambiguous and deterministic. No parser implementation may deviate from defined grammar without explicit amendment.

**Rationale**: The grammar is the single source of truth for what SQL syntax is supported. Implementation bugs must be fixed to match grammar, not the reverse.

### II. Test-Driven Development
All parser components MUST have corresponding tests written before implementation. Tests MUST cover valid syntax, invalid syntax, edge cases, and error conditions. Red-Green-Refactor cycle MUST be strictly enforced. Grammar changes MUST include test coverage for all affected rules.

**Rationale**: SQL parsing correctness is non-negotiable. TDD ensures every grammar rule and parser logic has test coverage preventing regressions.

### III. OpenGauss Compatibility
The parser MUST accurately support OpenGauss SQL dialect features as defined by official documentation. Deviations from OpenGauss behavior MUST be explicitly documented with justification. Priority is given to OpenGauss-specific syntax over generic SQL where they differ.

**Rationale**: The project's purpose is OpenGauss SQL parsing. Generic SQL support is valuable but secondary to OpenGauss-specific behavior.

### IV. API Clarity & Consistency
Parser API methods MUST follow Java naming conventions. Error messages MUST be clear and actionable, indicating the exact syntax error and location. The AST (Abstract Syntax Tree) structure MUST be well-documented and stable across patch versions. Public API changes require MINOR or MAJOR version bump.

**Rationale**: Users need predictable, clear interfaces to integrate the parser into their applications.

### V. Extensibility & Modularity
The grammar and parser structure MUST be designed to easily accommodate new OpenGauss SQL features. New SQL constructs MUST be added without requiring wholesale grammar restructuring. Parser components SHOULD be independently testable and maintainable.

**Rationale**: OpenGauss continues to evolve. The architecture must support incremental additions without technical debt accumulation.

## Security Requirements

### Input Validation
All SQL input MUST be parsed within reasonable memory and time limits. The parser MUST prevent denial-of-service attacks via pathological SQL constructs (e.g., excessively deep nesting, recursion limits). Input size limits MUST be enforced and configurable.

### Error Handling
Parser errors MUST NOT expose internal implementation details or stack traces to callers. Error messages MUST be sanitized to prevent information leakage. Exception handling MUST follow Spring Boot conventions.

### Dependency Management
All dependencies MUST be regularly updated for security patches. Vulnerability scanning MUST be part of the CI/CD pipeline. No dependencies with known CVEs above severity threshold are permitted in releases.

## Development Workflow

### Code Review Standards
All grammar changes MUST be reviewed by at least one maintainer. All public API changes require explicit approval. Reviewers MUST verify test coverage meets standards. Reviewers MUST validate compliance with all Core Principles.

### Testing Gates
Grammar changes MUST pass all existing tests. New grammar features MUST include integration tests demonstrating usage. Parser MUST handle all test cases from the OpenGauss test suite (if available). Code coverage threshold for parser components is 90%.

### Documentation Requirements
Public API changes MUST be documented in Javadoc. Grammar rules MUST include comments explaining purpose and syntax. Breaking changes MUST be documented in CHANGELOG.md. New OpenGauss SQL features MUST include examples in documentation.

## Governance

The Constitution supersedes all other development practices. Conflicts between this Constitution and other guidelines MUST be resolved in favor of the Constitution.

### Amendment Procedure
1. Proposed amendments MUST be documented with rationale and impact analysis
2. Amendments MUST be reviewed and approved by project maintainers
3. Amendments MUST include a migration plan for existing code if backward-incompatible
4. Amendment commits MUST reference the Constitution version being changed

### Versioning Policy
Constitution versions follow semantic versioning (MAJOR.MINOR.PATCH):
- MAJOR: Removal or redefinition of principles, backward-incompatible governance changes
- MINOR: New principle or section added, materially expanded guidance
- PATCH: Clarifications, wording improvements, non-semantic refinements

### Compliance Review
All pull requests MUST verify compliance with Constitution principles. Violations MUST be explicitly justified in the PR description. Complexity or deviations from principles MUST be documented and approved. Use `.specify/templates/plan-template.md` Constitution Check section for feature-level validation.

**Version**: 1.0.0 | **Ratified**: 2026-01-12 | **Last Amended**: 2026-01-12

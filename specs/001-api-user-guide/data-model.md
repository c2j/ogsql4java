# Data Model: API User Guide Documentation

**Feature**: 001-api-user-guide
**Date**: 2026-01-15
**Type**: Documentation Structure (not database schema)

## Overview

This data model describes the structure and organization of the user guide documentation. Since this is a documentation feature, there is no persistent data storage. The "entities" represent the conceptual structure of the documentation content.

## Documentation Structure

### Documentation Node

**Purpose**: Base abstraction for all documentation content

**Attributes**:
- `id`: Unique identifier (section name or file path)
- `title`: Display title
- `description`: Brief description of content
- `difficulty_level`: Beginner | Intermediate | Advanced
- `prerequisites`: List of prerequisite knowledge or sections
- `related_sections`: Cross-references to other documentation

**Validation Rules**:
- Title must be descriptive and concise
- Difficulty level must be appropriate for target audience
- Prerequisites must reference existing sections
- Related sections must exist in documentation

---

### Section

**Purpose**: Top-level documentation division (Quick Start, Common Use Cases, etc.)

**Attributes** (inherits from Documentation Node):
- `section_id`: Unique section identifier
- `section_title`: Section display title
- `subsection_ids`: List of subsections in this section

**Validation Rules**:
- Must contain at least one subsection
- Subsections must be logically related
- Difficulty level should be consistent or progressively harder

---

### Subsection

**Purpose**: Specific topic within a section (e.g., SELECT Queries within Common Use Cases)

**Attributes** (inherits from Documentation Node):
- `subsection_id`: Unique subsection identifier
- `parent_section_id`: Parent section reference
- `example_ids`: List of code examples in this subsection

**Validation Rules**:
- Must belong to a valid parent section
- Must contain at least one code example
- Examples should be ordered by complexity

---

### Code Example

**Purpose**: Complete, runnable Java code demonstrating specific functionality

**Attributes**:
- `example_id`: Unique identifier (e.g., "select-simple")
- `title`: Example title
- `difficulty_level`: Beginner | Intermediate | Advanced
- `prerequisites`: List of knowledge requirements (e.g., "Basic Java", "SQL SELECT")
- `code`: Complete Java source code
- `expected_output`: Output when code is executed
- `explanation`: Narrative explanation of what the code does and why
- `related_example_ids`: References to related examples
- `subsection_id`: Parent subsection reference

**Validation Rules**:
- Code must be complete and compilable without modification
- Code must follow Java 17 syntax and conventions
- Expected output must match actual code execution
- Explanation must be clear and concise
- Must use consistent formatting and naming conventions
- Comments in code should explain non-obvious operations
- Example should be runnable standalone (no external dependencies beyond parser)

---

### Error Pattern

**Purpose**: Common error scenario with solution and prevention

**Attributes**:
- `error_pattern_id`: Unique identifier
- `error_type`: Type of error (Syntax, Validation, Memory, etc.)
- `symptoms`: Observable symptoms user experiences
- `common_scenarios`: Situations where this error occurs
- `solution_steps`: Step-by-step resolution procedure
- `prevention_tips`: How to avoid this error in the future
- `related_example_ids`: Examples demonstrating proper usage to avoid error

**Validation Rules**:
- Symptoms must be accurate and observable
- Solution steps must be tested and verified
- Prevention tips must be actionable
- Must reference at least one working example

---

### Configuration Option

**Purpose**: Parser configuration setting with recommendations

**Attributes**:
- `option_name`: Configuration option name
- `default_value`: Default setting value
- `allowed_range`: Valid range or values
- `description`: What this option controls
- `recommended_values`: Suggested values for different scenarios (small, medium, large projects)
- `usage_example_id`: Code example showing how to configure this option

**Validation Rules**:
- Default value must be safe and reasonable
- Recommended values must be tested
- Must reference at least one usage example

---

## Entity Relationships

```
Documentation Node (abstract)
    ├── Section
    │   └── Subsection (1 to N)
    │       └── Code Example (1 to N)
    │
    ├── Code Example
    │   └── Error Pattern (0 to N) [related]
    │
    └── Error Pattern (0 to N)
```

**Relationship Rules**:
- Each Section contains 1 or more Subsections
- Each Subsection contains 1 or more Code Examples
- Code Examples can reference related Examples and Error Patterns
- Error Patterns are standalone but can reference Examples
- Configuration Options are standalone but can reference Examples

---

## Data Flow

### Documentation Creation Flow

1. **Create Section** → Define section topic and subsections
2. **Create Subsection** → Define specific topic and examples needed
3. **Create Code Example** → Write complete, runnable Java code
4. **Verify Example** → Execute code and capture expected output
5. **Write Explanation** → Document what code does and why
6. **Add Cross-References** → Link to related examples and sections
7. **Identify Common Errors** → Document error patterns with solutions

### Developer Usage Flow

1. **Navigate to Section** → Find relevant feature area
2. **Locate Subsection** → Identify specific topic
3. **Review Code Example** → Read code and explanation
4. **Execute Example** (optional) → Verify understanding
5. **Adapt Code** → Modify for specific use case
6. **Handle Errors** → Reference error patterns if issues occur

---

## Validation Rules Summary

| Entity | Required Fields | Validation Rules |
|--------|----------------|------------------|
| Documentation Node | id, title, difficulty_level | Title descriptive, difficulty appropriate |
| Section | section_id, subsection_ids | 1+ subsections, logical ordering |
| Subsection | subsection_id, parent_section_id, example_ids | Valid parent, 1+ examples |
| Code Example | example_id, code, expected_output, explanation | Compilable, matches output, clear explanation |
| Error Pattern | error_pattern_id, error_type, solution_steps | Tested solutions, actionable tips |
| Configuration Option | option_name, default_value, description | Safe defaults, tested values |

---

## Index Structure

For quick reference, documentation will include:

### By SQL Statement Type
- SELECT → SelectExamples.java
- INSERT → InsertExamples.java
- UPDATE → UpdateExamples.java
- DELETE → DeleteExamples.java
- CREATE, ALTER, DROP → DdlExamples.java

### By Feature
- Hints → HintsExamples.java
- Partitioning → PartitioningExamples.java
- Foreign Tables → ForeignTableExamples.java
- Metadata Extraction → MetadataExtractionExamples.java

### By Difficulty
- Beginner → Quick Start, simple SELECT/INSERT/UPDATE/DELETE
- Intermediate → Joins, subqueries, complex WHERE clauses
- Advanced → Hints, partitioning, metadata extraction

### By Problem Type
- Setup → Quick Start guide
- Common usage → Common Use Cases guide
- Errors → Error Handling & Troubleshooting guide
- Configuration → Configuration & Performance guide

---

*Data model complete. Ready for implementation.*

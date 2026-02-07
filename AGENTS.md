# ogsql4java Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-01-12

## Active Technologies
- Markdown documentation (examples use Java 17) + OpenGauss SQL Parser library (com.sdchat.ogsql) (001-api-user-guide)
- Markdown files in documentation directory (001-api-user-guide)
- Java 17 + ANTLR4 4.13.1, Spring Boot 3.5.9, JUnit 5 (for testing) (003-gauss-procedure-parsing)
- N/A (parser library, no persistence) (003-gauss-procedure-parsing)

- Java 17
- Spring Boot 3.5.9
- ANTLR4 4.13.1

## Project Structure

```text
src/
├── main/java/com/sdchat/ogsql/
│   ├── grammar/
│   │   ├── OpenGaussSQL.g4
│   │   └── README.md
│   ├── parser/
│   │   ├── SQLParser.java
│   │   ├── ParseResult.java
│   │   └── ParsingError.java
│   ├── ast/
│   │   ├── SQLStatement.java
│   │   ├── SelectQuery.java
│   │   ├── CreateStatement.java
│   │   ├── InsertStatement.java
│   │   ├── UpdateStatement.java
│   │   ├── DeleteStatement.java
│   │   ├── DropStatement.java
│   │   ├── Column.java
│   │   ├── DataSource.java
│   │   ├── ValueExpression.java
│   │   ├── Constraint.java
│   │   ├── OrderByItem.java
│   │   ├── PerformanceHint.java
│   │   ├── PartitioningInformation.java
│   │   ├── PartitionDefinition.java
│   │   ├── ExternalTable.java
│   │   ├── AlterStatement.java
│   │   └── visitor/
│   │       └── ASTVisitor.java
│   ├── metadata/
│   │   ├── MetadataExtractor.java
│   │   ├── ColumnReference.java
│   │   ├── FunctionCall.java
│   │   ├── Condition.java
│   │   └── WhereExtractor.java
│   └── exception/
│       ├── ParseException.java
│       ├── SyntaxErrorException.java
│       ├── SemanticErrorException.java
│       ├── InputValidationException.java
│       └── ErrorSeverity.java
└── test/java/com/sdchat/ogsql/
    ├── contract/
    │   ├── GrammarTest.java
    │   ├── HintTest.java
    │   └── [additional contract tests]
    ├── integration/
    │   ├── ParseTest.java
    │   ├── MetadataTest.java
    │   └── ErrorHandlingTest.java
    └── unit/
        ├── exception/
        ├── ast/
        ├── parser/
        ├── grammar/
        ├── statementtests/
        ├── expressiontests/
        └── utilities/

target/generated-sources/antlr4/
    └── com/sdchat/ogsql/grammar/
```

## Commands

```bash
# Build project
mvn clean compile

# Run ANTLR4 grammar generation
mvn clean generate-sources

# Run tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report

# Package application
mvn clean package
```

## Code Style

- Follow standard Java conventions (camelCase for methods, PascalCase for classes)
- Use proper Javadoc for all public API
- Follow Spring Boot configuration conventions
- ANTLR4 grammar: Lowercase for lexer rules, lowercase for parser rules
- Package structure: `com.sdchat.ogsql.{package}`

## Recent Changes
- 003-gauss-procedure-parsing: Added Java 17 + ANTLR4 4.13.1, Spring Boot 3.5.9, JUnit 5 (for testing)
- 001-api-user-guide: Added Markdown documentation (examples use Java 17) + OpenGauss SQL Parser library (com.sdchat.ogsql)
- sql-table-relationship-graph-tests: Added JGraphT 1.5.2 for graph data structures

<!-- MANUAL ADDITIONS START -->
- 001-gaussdb-parser: Added Java 17 + Spring Boot 3.5.9, ANTLR4 4.13.1
- sql-table-relationship-graph-tests: Added JGraphT 1.5.2
<!-- MANUAL ADDITIONS END -->

---

description: "Task list for feature implementation"
---

# Tasks: OpenGauss SQL Parser

**Input**: Design documents from `/specs/001-gaussdb-parser/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Test-Driven Development required per constitution. All parser components MUST have tests written before implementation.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story?] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Single project**: `src/`, `tests/` at repository root
- Grammar files: `src/main/java/com/sdchat/ogsql/grammar/`
- AST entities: `src/main/java/com/sdchat/ogsql/ast/`
- Parser implementation: `src/main/java/com/sdchat/ogsql/parser/`
- Metadata extraction: `src/main/java/com/sdchat/ogsql/metadata/`
- Exceptions: `src/main/java/com/sdchat/ogsql/exception/`
- Test structure: `tests/contract/`, `tests/integration/`, `tests/unit/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [X] T001 Create directory structure per implementation plan in src/main/java/com/sdchat/ogsql/
- [X] T002 Create test directory structure in tests/contract/, tests/integration/, tests/unit/
- [X] T003 [P] Configure Spring Boot 3.5.9 dependency in pom.xml
- [X] T004 [P] Configure ANTLR4 4.13.1 dependency in pom.xml
- [X] T005 [P] Configure JUnit5 and spring-boot-starter-test dependencies in pom.xml
- [X] T006 [P] Configure ANTLR4 Maven plugin in pom.xml build section
- [X] T007 [P] Create AGENTS.md file documenting Java 17, Spring Boot 3.5.9, ANTLR4 4.13.1

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

### Exception Hierarchy (Shared by All Stories)

- [X] T008 [P] Create ParseException base class in src/main/java/com/sdchat/ogsql/exception/ParseException.java
- [X] T009 [P] Create SyntaxErrorException with line/column info in src/main/java/com/sdchat/ogsql/exception/SyntaxException.java
- [X] T010 [P] Create SemanticErrorException in src/main/java/com/sdchat/ogsql/exception/SemanticErrorException.java
- [X] T011 [P] Create InputValidationException in src/main/java/com/sdchat/ogsql/exception/InputValidationException.java
- [X] T012 [P] Create ErrorSeverity enum in src/main/java/com/sdchat/ogsql/exception/ErrorSeverity.java

### Test T001-T012
- [X] T013 [P] Write unit test for ParseException in tests/unit/exception/ParseExceptionTest.java
- [X] T014 [P] Write unit test for SyntaxErrorException with line/column in tests/unit/exception/SyntaxExceptionTest.java
- [X] T015 [P] Write unit test for SemanticErrorException in tests/unit/exception/SemanticErrorExceptionTest.java
- [X] T016 [P] Write unit test for InputValidationException in tests/unit/exception/InputValidationExceptionTest.java
- [X] T017 [P] Write unit test for ErrorSeverity enum in tests/unit/exception/ErrorSeverityTest.java

### AST Base Classes (Shared by All Stories)

- [X] T018 [P] Create SQLStatement base interface in src/main/java/com/sdchat/ogsql/ast/SQLStatement.java
- [X] T019 [P] Create StatementType enum (SELECT, INSERT, UPDATE, DELETE, etc.) in src/main/java/com/sdchat/ogsql/ast/StatementType.java
- [X] T020 [P] Create ASTVisitor<T> interface in src/main/java/com/sdchat/ogsql/ast/visitor/ASTVisitor.java

### Test T018-T020
- [X] T021 [P] Write unit test for SQLStatement interface in tests/unit/ast/SQLStatementTest.java
- [X] T022 [P] Write unit test for StatementType enum in tests/unit/ast/StatementTypeTest.java
- [X] T023 [P] Write unit test for ASTVisitor interface in tests/unit/ast/ASTVisitorTest.java

### Parser Entry Points (Shared by All Stories)

- [X] T024 [P] Create SQLParser main entry point class in src/main/java/com/sdchat/ogsql/parser/SQLParser.java
- [X] T025 [P] Create ParseResult wrapper class in src/main/java/com/sdchat/ogsql/parser/ParseResult.java
- [X] T026 [P] Create ParsingError class with context/suggestion in src/main/java/com/sdchat/ogsql/parser/ParsingError.java

### Test T024-T026
- [X] T027 [P] Write unit test for SQLParser in tests/unit/parser/SQLParserTest.java
- [X] T028 [P] Write unit test for ParseResult in tests/unit/parser/ParseResultTest.java
- [X] T029 [P] Write unit test for ParsingError in tests/unit/parser/ParsingErrorTest.java

### Grammar Setup (Foundation for All Stories)

- [X] T030 Clone/fork PostgreSQL ANTLR4 grammar from antlr/grammars-v4 repository
- [X] T031 Copy PostgreSQL grammar file to src/main/java/com/sdchat/ogsql/grammar/PostgreSQLParser.g4 as reference
- [X] T032 Create OpenGaussSQL.g4 grammar skeleton in src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4
- [X] T033 Configure ANTLR4 package declarations in OpenGaussSQL.g4 (@header and @lexer::header)
- [X] T034 Add version comment to OpenGaussSQL.g4 for tracking (v1.0.0)
- [X] T035 Create grammar README.md in src/main/java/com/sdchat/ogsql/grammar/README.md

### Test Grammar Setup
- [X] T036 Run `mvn clean generate-sources` to verify ANTLR4 plugin generates parser code
- [X] T037 Verify generated parser code compiles in target/generated-sources/antlr4/
- [X] T038 Write contract test verifying ANTLR4 setup in tests/contract/GrammarSetupTest.java

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Parse Basic SQL Statements (Priority: P1) 🎯 MVP

**Goal**: Parse standard OpenGauss SQL statements (SELECT, INSERT, UPDATE, DELETE, CREATE) and generate structured AST representations.

**Independent Test**: Parse standard SQL queries and verify AST structure contains correct elements and relationships.

### Tests for User Story 1 ⚠️ (REQUIRED by Constitution)

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T039 [P] [US1] Write contract test for SELECT parsing in tests/contract/SelectParseTest.java
- [X] T040 [P] [US1] Write contract test for INSERT parsing in tests/contract/InsertParseTest.java
- [X] T041 [P] [US1] Write contract test for UPDATE parsing in tests/contract/UpdateParseTest.java
- [X] T042 [P] [US1] Write contract test for DELETE parsing in tests/contract/DeleteParseTest.java
- [X] T043 [P] [US1] Write contract test for CREATE TABLE parsing in tests/contract/CreateTableParseTest.java
- [X] T044 [P] [US1] Write contract test for multiple statements separated by semicolons in tests/contract/MultipleStatementsTest.java
- [X] T045 [P] [US1] Write contract test for invalid SQL syntax error reporting in tests/contract/SyntaxErrorTest.java
- [X] T046 [P] [US1] Write integration test for end-to-end parsing in tests/integration/ParseWorkflowTest.java

### Implementation for User Story 1

#### AST Entities for Basic SQL

- [X] T047 [P] [US1] Create SelectQuery class in src/main/java/com/sdchat/ogsql/ast/SelectQuery.java
- [X] T048 [P] [US1] Create CreateStatement class in src/main/java/com/sdchat/ogsql/ast/CreateStatement.java
- [X] T049 [P] [US1] Create InsertStatement class in src/main/java/com/sdchat/ogsql/ast/InsertStatement.java
- [X] T050 [P] [US1] Create UpdateStatement class in src/main/java/com/sdchat/ogsql/ast/UpdateStatement.java
- [X] T051 [P] [US1] Create DeleteStatement class in src/main/java/com/sdchat/ogsql/ast/DeleteStatement.java
- [X] T052 [P] [US1] Create DropStatement class in src/main/java/com/sdchat/ogsql/ast/DropStatement.java
- [X] T053 [P] [US1] Create Column class in src/main/java/com/sdchat/ogsql/ast/Column.java
- [X] T054 [P] [US1] Create DataSource class in src/main/java/com/sdchat/ogsql/ast/DataSource.java
- [X] T055 [P] [US1] Create ValueExpression class in src/main/java/com/sdchat/ogsql/ast/ValueExpression.java
- [X] T056 [P] [US1] Create Constraint class in src/main/java/com/sdchat/ogsql/ast/Constraint.java
- [X] T057 [P] [US1] Create OrderByItem class in src/main/java/com/sdchat/ogsql/ast/OrderByItem.java

#### Test T047-T057
- [X] T058 [P] [US1] Write unit test for SelectQuery in tests/unit/ast/SelectQueryTest.java
- [X] T059 [P] [US1] Write unit test for CreateStatement in tests/unit/ast/CreateStatementTest.java
- [X] T060 [P] [US1] Write unit test for InsertStatement in tests/unit/ast/InsertStatementTest.java
- [X] T061 [P] [US1] Write unit test for UpdateStatement in tests/unit/ast/UpdateStatementTest.java
- [X] T062 [P] [US1] Write unit test for DeleteStatement in tests/unit/ast/DeleteStatementTest.java
- [X] T063 [P] [US1] Write unit test for DropStatement in tests/unit/ast/DropStatementTest.java
- [X] T064 [P] [US1] Write unit test for Column in tests/unit/ast/ColumnTest.java
- [X] T065 [P] [US1] Write unit test for DataSource in tests/unit/ast/DataSourceTest.java
- [X] T066 [P] [US1] Write unit test for ValueExpression in tests/unit/ast/ValueExpressionTest.java
- [X] T067 [P] [US1] Write unit test for Constraint in tests/unit/ast/ConstraintTest.java
- [X] T068 [P] [US1] Write unit test for OrderByItem in tests/unit/ast/OrderByItemTest.java

#### Grammar Rules for Basic SQL

- [X] T069 [US1] Add SELECT statement rules to OpenGaussSQL.g4 (selectStmt, distinct, target list, from, where, group by, having, order by, limit)
- [X] T070 [US1] Add INSERT statement rules to OpenGaussSQL.g4 (insertStmt, column list, values)
- [X] T071 [US1] Add UPDATE statement rules to OpenGaussSQL.g4 (updateStmt, set clause, where)
- [X] T072 [US1] Add DELETE statement rules to OpenGaussSQL.g4 (deleteStmt, from, where)
- [X] T073 [US1] Add CREATE TABLE statement rules to OpenGaussSQL.g4 (createStmt, column definitions, constraints)
- [X] T074 [US1] Add DROP statement rules to OpenGaussSQL.g4 (dropStmt)
- [X] T075 [US1] Add common expression rules to OpenGaussSQL.g4 (literals, identifiers, operators, functions)

#### Test Grammar Rules T069-T075
- [X] T076 [P] [US1] Write unit test for SELECT grammar rules in tests/unit/grammar/SelectGrammarTest.java
- [X] T077 [P] [US1] Write unit test for INSERT grammar rules in tests/unit/grammar/InsertGrammarTest.java
- [X] T078 [P] [US1] Write unit test for UPDATE grammar rules in tests/unit/grammar/UpdateGrammarTest.java
- [X] T079 [P] [US1] Write unit test for DELETE grammar rules in tests/unit/grammar/DeleteGrammarTest.java
- [X] T080 [P] [US1] Write unit test for CREATE TABLE grammar rules in tests/unit/grammar/CreateTableGrammarTest.java
- [X] T081 [P] [US1] Write unit test for expression grammar rules in tests/unit/grammar/ExpressionGrammarTest.java

#### Test Parser Implementation (TDD: Tests BEFORE Implementation)
- [X] T088 [P] [US1] Write integration test for parse() method in tests/integration/ParseMethodTest.java
- [X] T089 [P] [US1] Write integration test for parseMultiple() method in tests/integration/MultipleParseTest.java
- [X] T090 [P] [US1] Write integration test for error handling in tests/integration/ErrorHandlingTest.java

#### Parser Implementation for Basic SQL

- [X] T091 [US1] Implement parse() method in SQLParser.java for single statement
- [X] T092 [US1] Implement parseMultiple() method in SQLParser.java for multiple statements
- [X] T093 [US1] Implement error handling in SQLParser.java with line/column information
- [X] T094 [US1] Implement statement type detection in SQLParser.java
- [X] T095 [US1] Implement ANTLR4 AST visitor to build custom AST in src/main/java/com/sdchat/ogsql/parser/ASTBuilder.java
- [X] T096 [US1] Wire ASTBuilder into SQLParser parse() methods

#### Visitor Implementation for Basic SQL

- [X] T097 [US1] Implement visit(SelectQuery) in ASTBuilder.java
- [X] T098 [US1] Implement visit(CreateStatement) in ASTBuilder.java
- [X] T099 [US1] Implement visit(InsertStatement) in ASTBuilder.java
- [X] T100 [US1] Implement visit(UpdateStatement) in ASTBuilder.java
- [X] T101 [US1] Implement visit(DeleteStatement) in ASTBuilder.java
- [X] T102 [US1] Implement visit(DropStatement) in ASTBuilder.java

#### Test Visitor Implementation T097-T102
- [X] T103 [P] [US1] Write unit test for ASTBuilder visitor in tests/unit/parser/ASTBuilderTest.java

#### Error Handling for Basic SQL

- [X] T104 [US1] Add error context extraction (50 chars around error) in SyntaxErrorException
- [X] T105 [US1] Add error suggestions (e.g., "Did you mean FROM?") in SyntaxErrorException
- [X] T106 [US1] Configure default error strategy in SQLParser for development
- [X] T107 [US1] Configure bail error strategy in SQLParser for production

#### Test Error Handling T104-T107
- [X] T108 [P] [US1] Write integration test for error messages in tests/integration/ErrorMessageTest.java
- [X] T109 [P] [US1] Write integration test for error strategies in tests/integration/ErrorStrategyTest.java

#### Visitor Implementation for Basic SQL

- [X] T091 [US1] Implement visit(SelectQuery) in ASTBuilder.java
- [X] T092 [US1] Implement visit(CreateStatement) in ASTBuilder.java
- [X] T093 [US1] Implement visit(InsertStatement) in ASTBuilder.java
- [X] T094 [US1] Implement visit(UpdateStatement) in ASTBuilder.java
- [X] T095 [US1] Implement visit(DeleteStatement) in ASTBuilder.java
- [X] T096 [US1] Implement visit(DropStatement) in ASTBuilder.java

#### Test Visitor Implementation T091-T096
- [X] T097 [P] [US1] Write unit test for ASTBuilder visitor in tests/unit/parser/ASTBuilderTest.java

#### Error Handling for Basic SQL

- [X] T098 [US1] Add error context extraction (50 chars around error) in SyntaxErrorException
- [X] T099 [US1] Add error suggestions (e.g., "Did you mean FROM?") in SyntaxErrorException
- [X] T100 [US1] Configure default error strategy in SQLParser for development
- [X] T101 [US1] Configure bail error strategy in SQLParser for production

#### Test Error Handling T098-T101
- [X] T102 [P] [US1] Write integration test for error messages in tests/integration/ErrorMessageTest.java
- [X] T103 [P] [US1] Write integration test for error strategies in tests/integration/ErrorStrategyTest.java

**Checkpoint**: User Story 1 complete - can parse SELECT, INSERT, UPDATE, DELETE, CREATE statements

---

## Phase 3b: Common Table Expressions (CTE) Support

**Goal**: Parse Common Table Expressions (WITH clauses) in SELECT statements as required by FR-010.

**Independent Test**: Parse SELECT statements with CTEs and verify AST contains CTE definitions and references.

### Tests for CTE Support ⚠️ (REQUIRED by Constitution)

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] TCTE-01 [P] [US1b] Write contract test for single CTE in WITH clause in tests/contract/SingleCTETest.java
- [ ] TCTE-02 [P] [US1b] Write contract test for multiple CTEs in WITH clause in tests/contract/MultipleCTETest.java
- [ ] TCTE-03 [P] [US1b] Write contract test for CTE referencing other CTE in tests/contract/CTEChainTest.java
- [ ] TCTE-04 [P] [US1b] Write contract test for CTE in subquery in tests/contract/CTESubqueryTest.java
- [ ] TCTE-05 [P] [US1b] Write integration test for CTE parsing in tests/integration/CTEIntegrationTest.java

### Implementation for CTE Support

#### AST Entity for CTEs

- [ ] TCTE-06 [P] [US1b] Create CommonTableExpression class in src/main/java/com/sdchat/ogsql/ast/CommonTableExpression.java
- [ ] TCTE-07 [P] [US1b] Update SelectQuery AST to include CTEs list

#### Test TCTE-06-TCTE-07

- [ ] TCTE-08 [P] [US1b] Write unit test for CommonTableExpression in tests/unit/ast/CommonTableExpressionTest.java
- [ ] TCTE-09 [P] [US1b] Write unit test for SelectQuery CTEs in tests/unit/ast/SelectQueryCTETest.java

#### Grammar Rules for CTEs

- [ ] TCTE-10 [US1b] Add WITH clause grammar rules to OpenGaussSQL.g4
- [ ] TCTE-11 [US1b] Add CTE definition rules (cte_name AS subquery) to OpenGaussSQL.g4
- [ ] TCTE-12 [US1b] Update selectStmt to accept optional WITH clause

#### Test Grammar Rules TCTE-10-TCTE-12

- [ ] TCTE-13 [P] [US1b] Write unit test for CTE grammar rules in tests/unit/grammar/CTEGrammarTest.java

#### Parser Implementation for CTEs

- [ ] TCTE-14 [US1b] Update ASTBuilder to extract CTE information
- [ ] TCTE-15 [US1b] Implement visit(CommonTableExpression) in ASTBuilder.java

#### Test CTE Parser Implementation TCTE-14-TCTE-15

- [ ] TCTE-16 [P] [US1b] Write integration test for CTE extraction in tests/integration/CTEExtractionTest.java

**Checkpoint**: CTE support complete - can parse WITH clauses and CTE references

---

## Phase 4: User Story 2 - Parse OpenGauss-Specific Hints (Priority: P2)

**Goal**: Parse OpenGauss query optimizer hints (NestLoop, MergeJoin, HashJoin) embedded in SQL comments.

**Independent Test**: Parse SQL queries with embedded hints and verify AST contains correct hint information.

### Tests for User Story 2 ⚠️ (REQUIRED by Constitution)

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T110 [P] [US2] Write contract test for hint parsing with single hint in tests/contract/HintParseTest.java
- [X] T111 [P] [US2] Write contract test for multiple hints in single comment in tests/contract/MultipleHintsTest.java
- [X] T112 [P] [US2] Write contract test for query without hints in tests/contract/NoHintsTest.java
- [X] T113 [P] [US2] Write contract test for invalid hint syntax in tests/contract/InvalidHintTest.java
- [X] T114 [P] [US2] Write integration test for hints with SELECT queries in tests/integration/HintIntegrationTest.java

### Implementation for User Story 2

#### AST Entity for Hints

- [X] T115 [P] [US2] Create PerformanceHint class in src/main/java/com/sdchat/ogsql/ast/PerformanceHint.java

#### Test T115

- [X] T116 [P] [US2] Write unit test for PerformanceHint in tests/unit/ast/PerformanceHintTest.java

#### Grammar Rules for Hints

- [X] T117 [US2] Add hint block grammar rules to OpenGaussSQL.g4 (hintBlock: '/*+' hintList '*/')
- [X] T118 [US2] Add hint type rules to OpenGaussSQL.g4 (NestLoop, MergeJoin, HashJoin, HashAggregate, BitmapScan, IndexScan)
- [X] T119 [US2] Add hint table reference rules to OpenGaussSQL.g4
- [X] T120 [US2] Add hint parameter rules to OpenGaussSQL.g4

#### Test Grammar Rules T117-T120

- [X] T121 [P] [US2] Write unit test for hint grammar rules in tests/unit/grammar/HintGrammarTest.java

#### Parser Implementation for Hints

- [X] T122 [US2] Update ASTBuilder to extract hints from ANTLR4 parse tree
- [X] T123 [US2] Update SelectQuery AST to include hints list

#### Test Hint Parser Implementation T122-T123

- [X] T124 [P] [US2] Write integration test for hint extraction in tests/integration/HintExtractionTest.java

**Checkpoint**: User Story 2 complete - can parse OpenGauss hints

---

## Phase 5: User Story 3 - Parse Partitioned Table Definitions (Priority: P2)

**Goal**: Parse OpenGauss partitioned table CREATE TABLE statements (RANGE, LIST, HASH).

**Independent Test**: Parse CREATE TABLE statements with various partitioning clauses and verify AST contains correct partition type, keys, and values.

### Tests for User Story 3 ⚠️ (REQUIRED by Constitution)

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T125 [P] [US3] Write contract test for RANGE partitioning in tests/contract/RangePartitionTest.java
- [X] T126 [P] [US3] Write contract test for LIST partitioning in tests/contract/ListPartitionTest.java
- [X] T127 [P] [US3] Write contract test for HASH partitioning in tests/contract/HashPartitionTest.java
- [X] T128 [P] [US3] Write integration test for partitioned table parsing in tests/integration/PartitionIntegrationTest.java

### Implementation for User Story 3

#### AST Entities for Partitioning

- [X] T129 [P] [US3] Create PartitioningInformation class in src/main/java/com/sdchat/ogsql/ast/PartitioningInformation.java
- [X] T130 [P] [US3] Create PartitionDefinition class in src/main/java/com/sdchat/ogsql/ast/PartitionDefinition.java
- [X] T131 [P] [US3] Create PartitionType enum (RANGE, LIST, HASH) in src/main/java/com/sdchat/ogsql/ast/PartitionType.java
- [X] T132 [P] [US3] Write unit test for PartitioningInformation in tests/unit/ast/PartitioningInformationTest.java
- [X] T133 [P] [US3] Write unit test for PartitionDefinition in tests/unit/ast/PartitionDefinitionTest.java
- [X] T134 [P] [US3] Write unit test for PartitionType enum in tests/unit/ast/PartitionTypeTest.java

#### Grammar Rules for Partitioning

- [X] T135 [US3] Add PARTITION BY clause rules to OpenGaussSQL.g4
- [X] T136 [US3] Add partition type rules (RANGE, LIST, HASH) to OpenGaussSQL.g4
- [X] T137 [US3] Add partition definition rules (PARTITION name VALUES) to OpenGaussSQL.g4
- [X] T138 [US3] Add subpartitioning support rules to OpenGaussSQL.g4 (SUBPARTITION BY clause, subpartition definitions)
- [X] T139 [US3] Add unit test for subpartitioning grammar in src/test/java/com/sdchat/ogsql/unit/grammar/SubpartitionGrammarTest.java

#### Test Grammar Rules T135-T138

- [X] T140 [P] [US3] Write unit test for partitioning grammar rules in tests/unit/grammar/PartitionGrammarTest.java
- [X] T141 [US3] Update ASTBuilder to extract partitioning information
- [X] T142 [US3] Update CreateStatement AST to include partitioning field
- [X] T143 [US3] Implement partition key validation in ASTBuilder
- [X] T144 [P] [US3] Write integration test for partitioning extraction in tests/integration/PartitionExtractionTest.java
- [X] T145 [P] [US3] Write integration test for partition key validation in tests/integration/PartitionValidationTest.java

**Checkpoint**: User Story 3 complete - can parse partitioned tables

---

## Phase 6: User Story 4 - Parse Foreign Table Definitions (Priority: P3)

**Goal**: Parse OpenGauss foreign table definitions (CREATE FOREIGN TABLE, ALTER FOREIGN TABLE).

**Independent Test**: Parse CREATE FOREIGN TABLE statements and verify AST includes server name, options, and column mappings.

### Tests for User Story 4 ⚠️ (REQUIRED by Constitution)

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T146 [P] [US4] Write contract test for CREATE FOREIGN TABLE in tests/contract/ForeignTableTest.java
- [ ] T147 [P] [US4] Write contract test for ALTER FOREIGN TABLE in tests/contract/AlterForeignTableTest.java
- [ ] T148 [P] [US4] Write contract test for foreign table with column options in tests/contract/ForeignTableColumnTest.java
- [ ] T149 [P] [US4] Write integration test for foreign table parsing in tests/integration/ForeignTableIntegrationTest.java

### Implementation for User Story 4

#### AST Entity for Foreign Tables

- [ ] T150 [P] [US4] Create ExternalTable class in src/main/java/com/sdchat/ogsql/ast/ExternalTable.java

#### Test T150

- [ ] T151 [P] [US4] Write unit test for ExternalTable in tests/unit/ast/ExternalTableTest.java

#### Grammar Rules for Foreign Tables

- [X] T152 [US4] Add CREATE FOREIGN TABLE grammar rules to OpenGaussSQL.g4
- [X] T153 [US4] Add SERVER clause rules to OpenGaussSQL.g4
- [X] T154 [US4] Add OPTIONS clause rules to OpenGaussSQL.g4
- [X] T155 [US4] Add ALTER FOREIGN TABLE grammar rules to OpenGaussSQL.g4

#### Test Grammar Rules T152-T155

- [X] T156 [P] [US4] Write unit test for foreign table grammar rules in tests/unit/grammar/ForeignTableGrammarTest.java

#### Parser Implementation for Foreign Tables

- [X] T157 [US4] Update ASTBuilder to extract foreign table information
- [X] T158 [US4] Update CreateStatement AST to support external table type
- [X] T159 [US4] Implement server options parsing in ASTBuilder

#### Test Foreign Table Parser Implementation T157-T159

- [X] T160 [P] [US4] Write integration test for foreign table extraction in tests/integration/ForeignTableExtractionTest.java
- [X] T161 [P] [US4] Write integration test for server options parsing in tests/integration/ServerOptionsTest.java

#### Additional AST Entities Needed for US4

- [X] T162 [P] [US4] Update AlterStatement class in src/main/java/com/sdchat/ogsql/ast/AlterStatement.java

#### Test T162

- [X] T163 [P] [US4] Write unit test for AlterStatement in src/test/java/com/sdchat/ogsql/unit/ast/AlterStatementTest.java

#### Grammar Rules for ALTER

- [X] T164 [US4] Add ALTER TABLE grammar rules to OpenGaussSQL.g4 (already exists)
- [X] T165 [US4] Update ASTBuilder visit(AlterStatement) method (already exists)

#### Test ALTER Grammar T164-T165

- [X] T166 [P] [US4] Write unit test for ALTER grammar rules in src/test/java/com/sdchat/ogsql/unit/grammar/AlterGrammarTest.java

**Checkpoint**: User Story 4 complete - can parse foreign tables

---

## Phase 7: User Story 5 - Extract SQL Metadata (Priority: P3)

**Goal**: Extract structured information from parsed SQL queries (table names, column references, functions, WHERE conditions).

**Independent Test**: Parse various SQL statements and query structured representation to extract specific metadata elements.

### Tests for User Story 5 ⚠️ (REQUIRED by Constitution)

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T167 [P] [US5] Write contract test for table name extraction in src/test/java/com/sdchat/ogsql/contract/TableExtractionTest.java
- [ ] T168 [P] [US5] Write contract test for column reference extraction in tests/contract/ColumnExtractionTest.java
- [ ] T169 [P] [US5] Write contract test for function call extraction in tests/contract/FunctionExtractionTest.java
- [ ] T170 [P] [US5] Write contract test for WHERE condition extraction in tests/contract/WhereExtractionTest.java
- [ ] T171 [P] [US5] Write integration test for metadata extraction in tests/integration/MetadataIntegrationTest.java

### Implementation for User Story 5

#### Metadata Extraction Utilities

- [X] T172 [P] [US5] Create MetadataExtractor class in src/main/java/com/sdchat/ogsql/metadata/MetadataExtractor.java (already exists)
- [X] T173 [P] [US5] Create ColumnReference class in src/main/java/com/sdchat/ogsql/metadata/ColumnReference.java (already exists)
- [X] T174 [P] [US5] Create FunctionCall class in src/main/java/com/sdchat/ogsql/metadata/FunctionCall.java (already exists)
- [X] T175 [P] [US5] Create Condition class in src/main/java/com/sdchat/ogsql/metadata/Condition.java (already exists)
- [X] T176 [P] [US5] Create WhereExtractor utility class in src/main/java/com/sdchat/ogsql/metadata/WhereExtractor.java (already exists)

#### Test Metadata Extraction T172-T176

- [ ] T177 [P] [US5] Write unit test for MetadataExtractor in tests/unit/metadata/MetadataExtractorTest.java
- [ ] T178 [P] [US5] Write unit test for ColumnReference in tests/unit/metadata/ColumnReferenceTest.java
- [ ] T179 [P] [US5] Write unit test for FunctionCall in tests/unit/metadata/FunctionCallTest.java
- [ ] T180 [P] [US5] Write unit test for Condition in tests/unit/metadata/ConditionTest.java
- [ ] T181 [P] [US5] Write unit test for WhereExtractor in tests/unit/metadata/WhereExtractorTest.java

#### Metadata Extraction Implementation

- [X] T182 [US5] Implement getTables() method in MetadataExtractor (already implemented)
- [X] T183 [US5] Implement getColumns() method in MetadataExtractor (already implemented)
- [X] T184 [US5] Implement getFunctions() method in MetadataExtractor (already implemented)
- [X] T185 [US5] Implement getWhereConditions() method in MetadataExtractor (already implemented)
- [X] T186 [US5] Create visitor-based metadata traversal in MetadataExtractor (already implemented)

#### Test Metadata Extraction Implementation T182-T186

- [X] T187 [P] [US5] Write integration test for table extraction in src/test/java/com/sdchat/ogsql/contract/TableExtractionTest.java
- [ ] T188 [P] [US5] Write integration test for column extraction in tests/integration/ColumnExtractionTest.java
- [ ] T189 [P] [US5] Write integration test for function extraction in tests/integration/FunctionExtractionTest.java
- [ ] T190 [P] [US5] Write integration test for WHERE condition extraction in tests/integration/WhereExtractionTest.java

**Checkpoint**: User Story 5 complete - can extract metadata from parsed SQL

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

### Performance Optimization

- [ ] T199 [P] Implement streaming input support in SQLParser for large files (100MB)
- [ ] T200 [P] Implement configurable memory limit (default 500MB) in SQLParser
- [ ] T201 [P] Implement file size validation (100MB limit) in SQLParser
- [ ] T202 [P] Add performance benchmark tests in tests/integration/PerformanceBenchmarkTest.java

#### Test Performance T199-T201
- [ ] T203 [P] Write integration test for streaming large files in tests/integration/StreamingTest.java
- [ ] T204 [P] Write integration test for memory limit enforcement in tests/integration/MemoryLimitTest.java
- [ ] T205 [P] Write integration test for file size validation in tests/integration/FileSizeValidationTest.java

**NOTE**: These edge case tests MUST be executed before release. They are not optional polish tasks - they validate critical error handling scenarios defined in spec.md.

### Edge Cases

- [ ] T191 [P] Write integration test for Unicode/multi-byte string handling in tests/integration/UnicodeTest.java
- [ ] T192 [P] Write integration test for nested comments in tests/integration/NestedCommentsTest.java
- [ ] T193 [P] Write integration test for identifier case sensitivity in tests/integration/CaseSensitivityTest.java
- [ ] T194 [P] Write integration test for semicolons in string literals in tests/integration/SemicolonInStringTest.java
- [ ] T195 [P] Write integration test for operator precedence in tests/integration/OperatorPrecedenceTest.java
- [ ] T196 [P] Write integration test for standard escape sequences in tests/integration/EscapeSequenceTest.java
  - Test cases: `\'`, `\"`, `\\`, `\n`, `\t`, `\r`, `\0`
- [ ] T197 [P] Write integration test for octal escapes in tests/integration/OctalEscapeTest.java
- [ ] T198 [P] Write integration test for hex escapes in tests/integration/HexEscapeTest.java

### Documentation

- [ ] T206 [P] Add Javadoc to all public AST classes in src/main/java/com/sdchat/ogsql/ast/
- [ ] T207 [P] Add Javadoc to parser classes in src/main/java/com/sdchat/ogsql/parser/
- [ ] T208 [P] Add Javadoc to metadata extraction in src/main/java/com/sdchat/ogsql/metadata/
- [ ] T209 [P] Add comments to OpenGaussSQL.g4 grammar rules
- [ ] T210 [P] Create CHANGELOG.md in root documenting breaking changes and version history
- [ ] T211 [P] Update README.md with getting started guide linking to quickstart.md
- [ ] T212 [P] Add example usage code snippets in README.md

### Code Coverage

- [ ] T213 [P] Run all tests with coverage reporting
- [ ] T214 [P] Verify 90% code coverage threshold is met
- [ ] T215 [P] Add coverage report integration to build (Jacoco or similar)

### Final Validation

- [ ] T216 [P] Run all contract tests against examples from contracts/examples/sql-examples.md
- [ ] T217 [P] Run performance benchmarks to verify 1000+ statements/second target
- [ ] T218 [P] Run full test suite (unit + integration + contract)
- [ ] T219 [P] Validate all acceptance scenarios from spec.md pass
- [ ] T220 [P] Verify all edge cases from spec.md are handled

**Checkpoint**: All user stories complete and polished - ready for release

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-7)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Phase 8)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 (hints in SelectQuery) but should be independently testable
- **User Story 3 (P3)**: Can start after Foundational (Phase 2) - May integrate with US1 (partitioning in CreateStatement) but should be independently testable
- **User Story 4 (P3)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 5 (P3)**: Can start after Foundational (Phase 2) - Depends on US1 (metadata extraction needs SelectQuery, CreateStatement, etc.)

### Within Each User Story

- Tests MUST be written and FAIL before implementation (TDD per constitution)
- AST entities before grammar rules
- Grammar rules before parser implementation
- Parser implementation before visitor implementation
- Story complete before moving to next priority

### Parallel Opportunities

- **Setup phase (Phase 1)**: All tasks T001-T007 can run in parallel
- **Foundational phase (Phase 2)**:
  - Exception hierarchy (T008-T012): All parallel
  - AST base classes (T018-T020): All parallel
  - Parser entry points (T024-T026): All parallel
  - Grammar setup (T030-T038): Sequential (requires Maven plugin first)
- **User Story 1 (Phase 3)**:
  - Tests (T039-T046): All parallel
  - AST entities (T047-T057): All parallel
  - Grammar rules (T069-T081): All parallel
- **User Story 2 (Phase 4)**:
  - Tests (T104-T108): All parallel
  - AST entity (T109): Independent
- **User Story 3 (Phase 5)**:
  - Tests (T119-T123): All parallel
  - AST entities (T124-T126): All parallel
- **User Story 4 (Phase 6)**:
  - Tests (T140-T143): All parallel
  - AST entity (T144): Independent
- **User Story 5 (Phase 7)**:
  - Tests (T161-T165): All parallel
  - Metadata utilities (T166-T170): All parallel
- **Polish (Phase 8)**:
  - Performance (T185-T191): All parallel
  - Edge cases (T192-T196): All parallel
  - Documentation (T197-T203): All parallel

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together:
Task: "Write contract test for SELECT parsing in tests/contract/SelectParseTest.java"
Task: "Write contract test for INSERT parsing in tests/contract/InsertParseTest.java"
Task: "Write contract test for UPDATE parsing in tests/contract/UpdateParseTest.java"
Task: "Write contract test for DELETE parsing in tests/contract/DeleteParseTest.java"
Task: "Write contract test for CREATE TABLE parsing in tests/contract/CreateTableParseTest.java"
Task: "Write contract test for multiple statements separated by semicolons in tests/contract/MultipleStatementsTest.java"
Task: "Write contract test for invalid SQL syntax error reporting in tests/contract/SyntaxErrorTest.java"
Task: "Write integration test for end-to-end parsing in tests/integration/ParseWorkflowTest.java"

# Launch all AST entities for User Story 1 together:
Task: "Create SelectQuery class in src/main/java/com/sdchat/ogsql/ast/SelectQuery.java"
Task: "Create CreateStatement class in src/main/java/com/sdchat/ogsql/ast/CreateStatement.java"
Task: "Create InsertStatement class in src/main/java/com/sdchat/ogsql/ast/InsertStatement.java"
Task: "Create UpdateStatement class in src/main/java/com/sdchat/ogsql/ast/UpdateStatement.java"
Task: "Create DeleteStatement class in src/main/java/com/sdchat/ogsql/ast/DeleteStatement.java"
Task: "Create DropStatement class in src/main/java/com/sdchat/ogsql/ast/DropStatement.java"
Task: "Create Column class in src/main/java/com/sdchat/ogsql/ast/Column.java"
Task: "Create DataSource class in src/main/java/com/sdchat/ogsql/ast/DataSource.java"
Task: "Create ValueExpression class in src/main/java/com/sdchat/ogsql/ast/ValueExpression.java"
Task: "Create Constraint class in src/main/java/com/sdchat/ogsql/ast/Constraint.java"
Task: "Create OrderByItem class in src/main/java/com/sdchat/ogsql/ast/OrderByItem.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (T001-T007)
2. Complete Phase 2: Foundational (T008-T038) - CRITICAL: BLOCKS all stories
3. Complete Phase 3: User Story 1 (T039-T103)
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

**MVP Scope**: Parse basic SQL statements (SELECT, INSERT, UPDATE, DELETE, CREATE) with error reporting and AST generation.

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Add User Story 4 → Test independently → Deploy/Demo
6. Add User Story 5 → Test independently → Deploy/Demo
7. Add Polish → Full feature complete → Release

Each story adds value without breaking previous stories.

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 (Basic SQL)
   - Developer B: User Story 2 (Hints) - Can start after US1 SELECTQuery is ready
   - Developer C: User Story 3 (Partitioning) - Can start after US1 CreateStatement is ready
   - Developer D: User Story 4 (Foreign Tables)
3. Developer E: User Story 5 (Metadata) - Wait for US1-US4 AST classes
4. Stories complete and integrate independently

---

## Notes

- **[P] tasks** = different files, no dependencies, can run in parallel
- **[Story] label** = maps task to specific user story for traceability
- **Test-Driven Development** = Tests MUST be written FIRST and FAIL before implementation (constitution requirement)
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
- Constitution requirement: 90% code coverage threshold
- Constitution requirement: Grammar-First Development (grammar defines authoritative contract)
- Constitution requirement: All grammar changes require maintainer review

## 1. Transaction Control Extended (SAVEPOINT/RELEASE/ROLLBACK TO)

### 1.1 Grammar Rules
- [x] 1.1.1 Add SAVEPOINT token to lexer
- [x] 1.1.2 Add RELEASE token to lexer
- [x] 1.1.3 Create savepointStmt parser rule: `SAVEPOINT identifier`
- [x] 1.1.4 Create releaseSavepointStmt parser rule: `RELEASE [SAVEPOINT] identifier`
- [x] 1.1.5 Extend rollbackStmt parser rule to support `ROLLBACK [WORK] TO [SAVEPOINT] identifier`
- [x] 1.1.6 Add new statement types to stmt rule

### 1.2 AST Nodes
- [x] 1.2.1 Create SavepointStatement.java with identifier field
- [x] 1.2.2 Create ReleaseSavepointStatement.java with identifier field
- [x] 1.2.3 Create RollbackToSavepointStatement.java with identifier field
- [x] 1.2.4 Add SAVEPOINT, RELEASE_SAVEPOINT, ROLLBACK_TO_SAVEPOINT to StatementType enum

### 1.3 Visitor Pattern
- [x] 1.3.1 Add visitSavepointStatement to ASTVisitor interface
- [x] 1.3.2 Add visitReleaseSavepointStatement to ASTVisitor interface
- [x] 1.3.3 Add visitRollbackToSavepointStatement to ASTVisitor interface
- [x] 1.3.4 Implement visitSavepointStatement in TableRelationshipExtractor
- [x] 1.3.5 Implement visitReleaseSavepointStatement in TableRelationshipExtractor
- [x] 1.3.6 Implement visitRollbackToSavepointStatement in TableRelationshipExtractor
- [x] 1.3.7 Implement visitSavepointStatement in MetadataExtractor
- [x] 1.3.8 Implement visitReleaseSavepointStatement in MetadataExtractor
- [x] 1.3.9 Implement visitRollbackToSavepointStatement in MetadataExtractor
- [x] 1.3.10 Implement visitors in ASTBuilder

### 1.4 Testing
- [x] 1.4.1 Create SavepointStatementTest with testParseSimpleSavepoint scenario
- [x] 1.4.2 Add testParseSavepointWithQuotedIdentifier scenario
- [x] 1.4.3 Add testParseReleaseSavepoint scenario
- [x] 1.4.4 Add testParseRollbackToSavepoint scenario
- [x] 1.4.5 Add testSavepointStatementType scenario
- [x] 1.4.6 Add validation test for missing identifier

## 2. INSERT ON CONFLICT (Upsert)

### 2.1 Grammar Rules
- [x] 2.1.1 Add ON token to lexer (if not exists)
- [x] 2.1.2 Add CONFLICT token to lexer
- [x] 2.1.3 Add EXCLUDED token to lexer
- [x] 2.1.4 Create onConflictClause parser rule supporting DO NOTHING and DO UPDATE
- [x] 2.1.5 Create conflictTarget parser rule for column lists and constraint names
- [x] 2.1.6 Extend insertstmt rule to include optional onConflictClause

### 2.2 AST Nodes
- [x] 2.2.1 Create OnConflictClause class with conflictTarget and conflictAction fields
- [x] 2.2.2 Create ConflictTarget class supporting columns and constraint name
- [x] 2.2.3 Create ConflictAction enum (DO_NOTHING, DO_UPDATE)
- [x] 2.2.4 Extend InsertStatement with onConflictClause field and getter/setter

### 2.3 Visitor Pattern
- [x] 2.3.1 Add visitOnConflictClause to ASTVisitor interface
- [x] 2.3.2 Implement visitOnConflictClause in TableRelationshipExtractor
- [x] 2.3.3 Implement visitOnConflictClause in MetadataExtractor
- [x] 2.3.4 Update ASTBuilder visitInsertstmt to handle onConflictClause

### 2.4 Testing
- [x] 2.4.1 Create InsertOnConflictTest with testParseDoNothing scenario
- [x] 2.4.2 Add testParseDoNothingWithConflictTarget scenario
- [x] 2.4.3 Add testParseDoUpdate scenario
- [x] 2.4.4 Add testParseDoUpdateWithWhere scenario
- [x] 2.4.5 Add testParseDoUpdateWithMultipleAssignments scenario
- [x] 2.4.6 Add validation test for DO UPDATE without SET
- [x] 2.4.7 Add validation test for DO UPDATE without conflict target

## 3. RETURNING Clause (INSERT/UPDATE/DELETE)

### 3.1 Grammar Rules
- [x] 3.1.1 Add RETURNING token to lexer
- [x] 3.1.2 Create returningClause parser rule supporting column list, expressions, and wildcard
- [x] 3.1.3 Extend insertstmt rule to include optional returningClause
- [x] 3.1.4 Extend updatestmt rule to include optional returningClause
- [x] 3.1.5 Extend deletestmt rule to include optional returningClause

### 3.2 AST Nodes
- [x] 3.2.1 Create ReturningClause class with list of output expressions
- [x] 3.2.2 Create ReturningExpression class supporting column refs, expressions, and aliases
- [x] 3.2.3 Extend InsertStatement with returningClause field and getter/setter
- [x] 3.2.4 Extend UpdateStatement with returningClause field and getter/setter
- [x] 3.2.5 Extend DeleteStatement with returningClause field and getter/setter

### 3.3 Visitor Pattern
- [x] 3.3.1 Add visitReturningClause to ASTVisitor interface
- [x] 3.3.2 Add visitReturningExpression to ASTVisitor interface
- [x] 3.3.3 Implement visitReturningClause in TableRelationshipExtractor
- [x] 3.3.4 Implement visitReturningExpression in TableRelationshipExtractor
- [x] 3.3.5 Implement visitReturningClause in MetadataExtractor
- [x] 3.3.6 Implement visitReturningExpression in MetadataExtractor
- [x] 3.3.7 Update ASTBuilder to handle RETURNING in INSERT/UPDATE/DELETE

### 3.4 Testing
- [x] 3.4.1 Create ReturningClauseTest with testParseInsertReturningSingleColumn scenario
- [x] 3.4.2 Add testParseInsertReturningMultipleColumns scenario
- [x] 3.4.3 Add testParseInsertReturningWildcard scenario
- [x] 3.4.4 Add testParseInsertReturningWithExpression scenario
- [x] 3.4.5 Add testParseUpdateReturning scenario
- [x] 3.4.6 Add testParseDeleteReturning scenario
- [x] 3.4.7 Add validation test for RETURNING without expressions

## 4. Integration and Regression Testing

### 4.1 Mock Visitors Update
- [x] 4.1.1 Update CallFuncStmtTest.MockVisitor with new visitor methods
- [x] 4.1.2 Update CreateProcedureStmtTest.MockVisitor with new visitor methods
- [x] 4.1.3 Add any other test MockVisitors that need updating

### 4.2 Regression Testing
- [x] 4.2.1 Run full test suite to ensure no regressions
- [x] 4.2.2 Verify existing INSERT tests still pass
- [x] 4.2.3 Verify existing transaction tests (BEGIN/COMMIT/ROLLBACK) still pass
- [x] 4.2.4 Check for any compilation warnings or errors

### 4.3 Documentation
- [x] 4.3.1 Update grammar README with new syntax support
- [x] 4.3.2 Document any known limitations or edge cases
- [x] 4.3.3 Update API documentation for new AST nodes

## 5. Performance Validation

### 5.1 Benchmark Tests
- [x] 5.1.1 Run PerformanceBenchmarkTest to establish baseline
- [x] 5.1.2 Add benchmark for SAVEPOINT parsing
- [x] 5.1.3 Add benchmark for INSERT ON CONFLICT parsing
- [x] 5.1.4 Add benchmark for RETURNING clause parsing
- [x] 5.1.5 Verify no significant performance degradation (>10%)


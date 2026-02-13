## ADDED Requirements

### Requirement: Parser supports SAVEPOINT statement
The parser SHALL support parsing the SAVEPOINT statement to establish a new savepoint within the current transaction.

#### Scenario: Parse simple SAVEPOINT
- **WHEN** the parser receives "SAVEPOINT my_savepoint"
- **THEN** it SHALL return a SavepointStatement AST node with identifier "my_savepoint"

#### Scenario: Parse SAVEPOINT with quoted identifier
- **WHEN** the parser receives "SAVEPOINT \"My SavePoint\""
- **THEN** it SHALL return a SavepointStatement AST node with identifier "My SavePoint"

### Requirement: Parser supports RELEASE SAVEPOINT statement
The parser SHALL support parsing the RELEASE SAVEPOINT statement to destroy a savepoint.

#### Scenario: Parse RELEASE SAVEPOINT
- **WHEN** the parser receives "RELEASE SAVEPOINT my_savepoint"
- **THEN** it SHALL return a ReleaseSavepointStatement AST node with identifier "my_savepoint"

#### Scenario: Parse RELEASE without SAVEPOINT keyword
- **WHEN** the parser receives "RELEASE my_savepoint"
- **THEN** it SHALL return a ReleaseSavepointStatement AST node with identifier "my_savepoint"

### Requirement: Parser supports ROLLBACK TO SAVEPOINT statement
The parser SHALL support parsing the ROLLBACK TO SAVEPOINT statement to roll back to a savepoint.

#### Scenario: Parse ROLLBACK TO SAVEPOINT
- **WHEN** the parser receives "ROLLBACK TO SAVEPOINT my_savepoint"
- **THEN** it SHALL return a RollbackToSavepointStatement AST node with identifier "my_savepoint"

#### Scenario: Parse ROLLBACK WORK TO SAVEPOINT
- **WHEN** the parser receives "ROLLBACK WORK TO SAVEPOINT my_savepoint"
- **THEN** it SHALL return a RollbackToSavepointStatement AST node with identifier "my_savepoint"

#### Scenario: Parse ROLLBACK TO without SAVEPOINT keyword
- **WHEN** the parser receives "ROLLBACK TO my_savepoint"
- **THEN** it SHALL return a RollbackToSavepointStatement AST node with identifier "my_savepoint"

### Requirement: Parser distinguishes transaction statement types
The parser SHALL correctly distinguish between different transaction control statements and return the appropriate StatementType.

#### Scenario: SAVEPOINT statement type
- **WHEN** the parser parses a SAVEPOINT statement
- **THEN** the returned statement's getStatementType() SHALL return StatementType.SAVEPOINT

#### Scenario: RELEASE SAVEPOINT statement type
- **WHEN** the parser parses a RELEASE SAVEPOINT statement
- **THEN** the returned statement's getStatementType() SHALL return StatementType.RELEASE_SAVEPOINT

#### Scenario: ROLLBACK TO SAVEPOINT statement type
- **WHEN** the parser parses a ROLLBACK TO SAVEPOINT statement
- **THEN** the returned statement's getStatementType() SHALL return StatementType.ROLLBACK_TO_SAVEPOINT

### Requirement: Parser validates savepoint identifier
The parser SHALL validate that savepoint statements include a valid identifier.

#### Scenario: Reject SAVEPOINT without identifier
- **WHEN** the parser receives "SAVEPOINT" without an identifier
- **THEN** it SHALL throw a SyntaxErrorException with appropriate error message

#### Scenario: Reject invalid savepoint identifiers
- **WHEN** the parser receives "SAVEPOINT 123invalid" (identifier starting with number)
- **THEN** it SHALL throw a SyntaxErrorException with appropriate error message

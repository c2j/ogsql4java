## ADDED Requirements

### Requirement: Parser supports INSERT with RETURNING clause
The parser SHALL support parsing INSERT statements with RETURNING clause to return values from inserted rows.

#### Scenario: Parse INSERT RETURNING with single column
- **WHEN** the parser receives "INSERT INTO users (name) VALUES ('John') RETURNING id"
- **THEN** it SHALL return an InsertStatement with returningClause containing column "id"

#### Scenario: Parse INSERT RETURNING with multiple columns
- **WHEN** the parser receives "INSERT INTO users (name) VALUES ('John') RETURNING id, name, created_at"
- **THEN** it SHALL return an InsertStatement with returningClause containing columns "id", "name", and "created_at"

#### Scenario: Parse INSERT RETURNING with asterisk
- **WHEN** the parser receives "INSERT INTO users (name) VALUES ('John') RETURNING *"
- **THEN** it SHALL return an InsertStatement with returningClause containing wildcard "*"

### Requirement: Parser supports RETURNING with expressions
The parser SHALL support RETURNING clause with expressions and aliasing.

#### Scenario: Parse RETURNING with expression
- **WHEN** the parser receives "INSERT INTO users (first_name, last_name) VALUES ('John', 'Doe') RETURNING first_name || ' ' || last_name AS full_name"
- **THEN** it SHALL return an InsertStatement with returningClause containing expression and alias "full_name"

#### Scenario: Parse RETURNING with function call
- **WHEN** the parser receives "INSERT INTO users (name) VALUES ('John') RETURNING upper(name) AS upper_name"
- **THEN** it SHALL return an InsertStatement with returningClause containing function call expression

#### Scenario: Parse RETURNING with qualified column
- **WHEN** the parser receives "INSERT INTO users (name) VALUES ('John') RETURNING users.id"
- **THEN** it SHALL return an InsertStatement with returningClause containing qualified column "users.id"

### Requirement: Parser supports RETURNING with other DML statements
The parser SHALL support RETURNING clause with UPDATE and DELETE statements.

#### Scenario: Parse UPDATE with RETURNING
- **WHEN** the parser receives "UPDATE users SET name = 'Jane' WHERE id = 1 RETURNING id, name"
- **THEN** it SHALL return an UpdateStatement with returningClause containing columns "id" and "name"

#### Scenario: Parse DELETE with RETURNING
- **WHEN** the parser receives "DELETE FROM users WHERE id = 1 RETURNING *"
- **THEN** it SHALL return a DeleteStatement with returningClause containing wildcard "*"

#### Scenario: Parse UPDATE RETURNING with expression
- **WHEN** the parser receives "UPDATE users SET counter = counter + 1 WHERE id = 1 RETURNING counter AS new_counter"
- **THEN** it SHALL return an UpdateStatement with returningClause containing expression and alias

### Requirement: Parser validates RETURNING syntax
The parser SHALL validate RETURNING clause syntax.

#### Scenario: Reject RETURNING without output expression
- **WHEN** the parser receives "INSERT INTO users (name) VALUES ('John') RETURNING"
- **THEN** it SHALL throw a SyntaxErrorException indicating output expression is required

#### Scenario: Reject empty RETURNING list
- **WHEN** the parser receives "INSERT INTO users (name) VALUES ('John') RETURNING ()"
- **THEN** it SHALL throw a SyntaxErrorException

### Requirement: Parser exposes RETURNING in AST
The InsertStatement, UpdateStatement, and DeleteStatement AST nodes SHALL expose RETURNING clause information.

#### Scenario: Check if RETURNING clause exists
- **WHEN** accessing an InsertStatement with RETURNING clause
- **THEN** hasReturningClause() SHALL return true

#### Scenario: Check if RETURNING clause absent
- **WHEN** accessing an InsertStatement without RETURNING clause
- **THEN** hasReturningClause() SHALL return false

#### Scenario: Access RETURNING expressions
- **WHEN** accessing an InsertStatement with RETURNING id, name
- **THEN** getReturningExpressions() SHALL return list containing expressions for "id" and "name"

#### Scenario: Access RETURNING expression count
- **WHEN** accessing an InsertStatement with RETURNING id, name, email
- **THEN** getReturningExpressions().size() SHALL equal 3

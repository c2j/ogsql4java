## ADDED Requirements

### Requirement: Parser supports INSERT ON CONFLICT DO NOTHING
The parser SHALL support parsing INSERT statements with ON CONFLICT DO NOTHING clause for upsert operations.

#### Scenario: Parse INSERT ON CONFLICT DO NOTHING
- **WHEN** the parser receives "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT DO NOTHING"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing DO NOTHING action

#### Scenario: Parse INSERT with conflict target and DO NOTHING
- **WHEN** the parser receives "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT (id) DO NOTHING"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing conflict target column "id" and DO NOTHING action

#### Scenario: Parse INSERT with unique constraint name
- **WHEN** the parser receives "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT ON CONSTRAINT users_pkey DO NOTHING"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing constraint name "users_pkey" and DO NOTHING action

### Requirement: Parser supports INSERT ON CONFLICT DO UPDATE
The parser SHALL support parsing INSERT statements with ON CONFLICT DO UPDATE clause.

#### Scenario: Parse INSERT ON CONFLICT DO UPDATE with single column
- **WHEN** the parser receives "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing DO UPDATE action with SET clause

#### Scenario: Parse INSERT ON CONFLICT DO UPDATE with multiple columns
- **WHEN** the parser receives "INSERT INTO users (id, name, email) VALUES (1, 'John', 'john@example.com') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, email = EXCLUDED.email"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing DO UPDATE with multiple SET assignments

#### Scenario: Parse INSERT ON CONFLICT DO UPDATE with WHERE condition
- **WHEN** the parser receives "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name WHERE users.updated_at < EXCLUDED.updated_at"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing DO UPDATE with WHERE condition

### Requirement: Parser supports complex conflict targets
The parser SHALL support parsing various conflict target specifications.

#### Scenario: Parse ON CONFLICT with multiple columns
- **WHEN** the parser receives "INSERT INTO users (id, email, name) VALUES (1, 'john@example.com', 'John') ON CONFLICT (id, email) DO NOTHING"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing multiple conflict target columns

#### Scenario: Parse ON CONFLICT with index predicate
- **WHEN** the parser receives "INSERT INTO users (id, name) VALUES (1, 'John') ON CONFLICT (id) WHERE id > 0 DO NOTHING"
- **THEN** it SHALL return an InsertStatement with onConflictClause containing index predicate

### Requirement: Parser validates ON CONFLICT syntax
The parser SHALL validate the syntax of ON CONFLICT clauses.

#### Scenario: Reject DO UPDATE without SET
- **WHEN** the parser receives "INSERT INTO users (id) VALUES (1) ON CONFLICT DO UPDATE"
- **THEN** it SHALL throw a SyntaxErrorException indicating SET clause is required

#### Scenario: Reject DO UPDATE without conflict target
- **WHEN** the parser receives "INSERT INTO users (id) VALUES (1) ON CONFLICT DO UPDATE SET name = 'test'"
- **THEN** it SHALL throw a SyntaxErrorException indicating conflict target is required for DO UPDATE

### Requirement: Parser exposes ON CONFLICT in AST
The InsertStatement AST node SHALL expose ON CONFLICT clause information through appropriate methods.

#### Scenario: Access conflict action type
- **WHEN** accessing an InsertStatement with ON CONFLICT DO NOTHING
- **THEN** getConflictAction() SHALL return ConflictAction.DO_NOTHING

#### Scenario: Access conflict action type for UPDATE
- **WHEN** accessing an InsertStatement with ON CONFLICT DO UPDATE
- **THEN** getConflictAction() SHALL return ConflictAction.DO_UPDATE

#### Scenario: Access conflict target columns
- **WHEN** accessing an InsertStatement with ON CONFLICT (id, email)
- **THEN** getConflictTargetColumns() SHALL return list containing "id" and "email"

#### Scenario: Access DO UPDATE assignments
- **WHEN** accessing an InsertStatement with DO UPDATE SET name = EXCLUDED.name
- **THEN** getConflictUpdateAssignments() SHALL contain the assignment for column "name"

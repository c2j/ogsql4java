## ADDED Requirements

### Requirement: START TRANSACTION support
The parser SHALL support START TRANSACTION:
- START TRANSACTION
- START TRANSACTION isolation level
- BEGIN (alias for START TRANSACTION)

#### Scenario: START TRANSACTION
- **WHEN** "START TRANSACTION"
- **THEN** parser SHALL recognize as transaction start

#### Scenario: START TRANSACTION with isolation
- **WHEN** "START TRANSACTION ISOLATION LEVEL SERIALIZABLE"
- **THEN** parser SHALL handle isolation level

### Requirement: SAVEPOINT support
The parser SHALL support SAVEPOINT:
- SAVEPOINT name
- ROLLBACK TO SAVEPOINT
- RELEASE SAVEPOINT

#### Scenario: SAVEPOINT
- **WHEN** "SAVEPOINT sp1"
- **THEN** parser SHALL handle SAVEPOINT

#### Scenario: ROLLBACK TO SAVEPOINT
- **WHEN** "ROLLBACK TO SAVEPOINT sp1"
- **THEN** parser SHALL handle ROLLBACK TO

### Requirement: SET TRANSACTION support
The parser SHALL support SET TRANSACTION:
- SET TRANSACTION isolation level
- SET SESSION characteristics

#### Scenario: SET TRANSACTION
- **WHEN** "SET TRANSACTION ISOLATION LEVEL READ COMMITTED"
- **THEN** parser SHALL handle SET TRANSACTION

### Requirement: COMMIT/ROLLBACK options
The parser SHALL support:
- COMMIT AND CHAIN
- ROLLBACK AND CHAIN

#### Scenario: COMMIT AND CHAIN
- **WHEN** "COMMIT AND CHAIN"
- **THEN** parser SHALL handle AND CHAIN

### Requirement: SET with multiple variables
The parser SHALL support setting multiple variables

#### Scenario: SET multiple
- **WHEN** "SET a = 1, b = 2"
- **THEN** parser SHALL handle multiple SET

### Requirement: SHOW ALL support
The parser SHALL support SHOW command

#### Scenario: SHOW
- **WHEN** "SHOW work_mem"
- **THEN** parser SHALL handle SHOW

### Requirement: EXPLAIN support
The parser SHALL support EXPLAIN:
- EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT/JSON/XML/YAML) query
- EXPLAIN ANALYZE

#### Scenario: EXPLAIN ANALYZE
- **WHEN** "EXPLAIN ANALYZE SELECT * FROM t"
- **THEN** parser SHALL handle EXPLAIN ANALYZE

### Requirement: PREPARE/EXECUTE support
The parser SHALL support:
- PREPARE statement
- EXECUTE statement
- DEALLOCATE

#### Scenario: PREPARE
- **WHEN** "PREPARE p AS SELECT * FROM t WHERE id = $1"
- **THEN** parser SHALL handle PREPARE

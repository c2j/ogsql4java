## ADDED Requirements

### Requirement: EXPLAIN command parsing
The parser SHALL successfully parse EXPLAIN statements in OpenGauss SQL format.

#### Scenario: Basic EXPLAIN
- **WHEN** parser receives "EXPLAIN SELECT * FROM table_name;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: EXPLAIN ANALYZE
- **WHEN** parser receives "EXPLAIN ANALYZE SELECT * FROM table_name;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: EXPLAIN with options
- **WHEN** parser receives "EXPLAIN (FORMAT JSON) SELECT * FROM table_name;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: EXPLAIN ANALYZE with buffers
- **WHEN** parser receives "EXPLAIN (ANALYZE, BUFFERS) SELECT * FROM table_name;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: EXPLAIN for INSERT/UPDATE/DELETE
- **WHEN** parser receives "EXPLAIN UPDATE table_name SET col = value;"
- **THEN** parser SHALL return valid parse tree without syntax errors

## ADDED Requirements

### Requirement: Oracle compatibility syntax
The parser SHALL support Oracle-compatible syntax:
- CONNECT BY prior
- START WITH
- SYS_GUID()
- DECODE()
- NVL()/NVL2()
- ROWNUM
- ROWID

#### Scenario: CONNECT BY
- **WHEN** "SELECT * FROM t START WITH id = 1 CONNECT BY PRIOR parent_id = id"
- **THEN** parser SHALL handle CONNECT BY

#### Scenario: ROWNUM
- **WHEN** "SELECT * FROM t WHERE ROWNUM <= 10"
- **THEN** parser SHALL recognize ROWNUM

### Requirement: MySQL compatibility syntax
The parser SHALL support MySQL-compatible syntax:
- LIMIT offset, count
- INSERT IGNORE
- REPLACE INTO
- ON DUPLICATE KEY UPDATE

#### Scenario: LIMIT with offset
- **WHEN** "SELECT * FROM t LIMIT 10, 20"
- **THEN** parser SHALL handle offset,count LIMIT

#### Scenario: INSERT IGNORE
- **WHEN** "INSERT IGNORE INTO t VALUES (1)"
- **THEN** parser SHALL handle INSERT IGNORE

### Requirement: SET variable syntax
The parser SHALL support session/system variables:
- SET variable = value
- SET variable TO value
- SHOW variable

#### Scenario: SET variable
- **WHEN** "SET work_mem = '64MB'"
- **THEN** parser SHALL handle SET statement

### Requirement: TEMPLATE clause support
The parser SHALL support CREATE DATABASE with TEMPLATE

#### Scenario: CREATE DATABASE with TEMPLATE
- **WHEN** "CREATE DATABASE db TEMPLATE template0"
- **THEN** parser SHALL handle TEMPLATE clause

### Requirement: ANALYZE support
The parser SHALL support ANALYZE command

#### Scenario: ANALYZE
- **WHEN** "ANALYZE t"
- **THEN** parser SHALL recognize ANALYZE

### Requirement: CLUSTER support
The parser SHALL support CLUSTER command

#### Scenario: CLUSTER
- **WHEN** "CLUSTER t USING idx"
- **THEN** parser SHALL handle CLUSTER

### Requirement: COPY support
The parser SHALL support COPY command

#### Scenario: COPY
- **WHEN** "COPY t (a, b) FROM '/file.csv'"
- **THEN** parser SHALL handle COPY

### Requirement: TRUNCATE support
The parser SHALL support TRUNCATE with options:
- CASCADE
- RESTART IDENTITY

#### Scenario: TRUNCATE CASCADE
- **WHEN** "TRUNCATE t1, t2 CASCADE"
- **THEN** parser SHALL handle TRUNCATE CASCADE

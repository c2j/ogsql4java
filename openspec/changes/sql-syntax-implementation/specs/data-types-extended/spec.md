## ADDED Requirements

### Requirement: ENCODING clause support
The parser SHALL support ENCODING clause in CREATE TABLE and column definitions:
- ENCODING 'UTF8'
- ENCODING 'gbk'
- ENCODING 'utf8mb4'
- Column-level ENCODING

#### Scenario: CREATE TABLE with ENCODING
- **WHEN** parsing "CREATE TABLE t (a TEXT) ENCODING 'UTF8'"
- **THEN** parser SHALL recognize ENCODING clause

#### Scenario: Column with ENCODING
- **WHEN** parsing "CREATE TABLE t (a TEXT ENCODING 'gbk')"
- **THEN** parser SHALL handle column-level ENCODING

### Requirement: DBCOMPATIBILITY clause support
The parser SHALL support DBCOMPATIBILITY for Oracle/MySQL/PostgreSQL compatibility:
- DBCOMPATIBILITY 'A' (Oracle)
- DBCOMPATIBILITY 'B' (MySQL)
- DBCOMPATIBILITY 'C' (PostgreSQL)
- DBCOMPATIBILITY 'P'

#### Scenario: CREATE DATABASE with DBCOMPATIBILITY
- **WHEN** parsing "CREATE DATABASE db DBCOMPATIBILITY 'A'"
- **THEN** parser SHALL recognize DBCOMPATIBILITY clause

#### Scenario: CREATE TABLE with DBCOMPATIBILITY
- **WHEN** parsing "CREATE TABLE t (a INT) DBCOMPATIBILITY 'B'"
- **THEN** parser SHALL handle table-level DBCOMPATIBILITY

### Requirement: INTERVAL type support
The parser SHALL support INTERVAL type with various specifications:
- INTERVAL
- INTERVAL YEAR
- INTERVAL MONTH
- INTERVAL DAY
- INTERVAL HOUR
- INTERVAL SECOND
- INTERVAL TO YEAR
- INTERVAL TO MONTH
- INTERVAL 'value'

#### Scenario: INTERVAL column
- **WHEN** parsing "CREATE TABLE t (a INTERVAL)"
- **THEN** parser SHALL recognize INTERVAL as valid type

#### Scenario: INTERVAL with precision
- **WHEN** parsing "INTERVAL DAY(3) TO SECOND(2)"
- **THEN** parser SHALL handle interval qualifiers

### Requirement: ORIENTATION clause support
The parser SHALL support ORIENTATION clause for columnar/row storage:
- ORIENTATION ROW
- ORIENTATION COLUMN
- ORIENTATION PARALLEL

#### Scenario: CREATE TABLE with ORIENTATION
- **WHEN** parsing "CREATE TABLE t (a INT) ORIENTATION COLUMN"
- **THEN** parser SHALL recognize ORIENTATION clause

### Requirement: PASSWORD support in CREATE/ALTER
The parser SHALL support PASSWORD clause in user statements:
- CREATE USER ... PASSWORD
- ALTER USER ... PASSWORD
- CREATE ROLE ... PASSWORD

#### Scenario: CREATE USER with PASSWORD
- **WHEN** parsing "CREATE USER u PASSWORD 'pass123'"
- **THEN** parser SHALL recognize PASSWORD clause

### Requirement: STORAGE clause support
The parser SHALL support STORAGE clause:
- STORAGE (type)
- STORAGE OFF
- STORAGE ON

#### Scenario: Column with STORAGE
- **WHEN** parsing "CREATE TABLE t (a TEXT STORAGE (EXTENDED))"
- **THEN** parser SHALL handle STORAGE clause

### Requirement: LIKE...INCLUDING support
The parser SHALL support LIKE ... INCLUDING options:
- INCLUDING IDENTITY
- INCLUDING DEFAULTS
- INCLUDING CONSTRAINTS
- INCLUDING INDEXES
- INCLUDING STORAGE
- INCLUDING COMMENTS

#### Scenario: CREATE TABLE LIKE INCLUDING
- **WHEN** parsing "CREATE TABLE t2 (LIKE t1 INCLUDING DEFAULTS INCLUDING INDEXES)"
- **THEN** parser SHALL handle INCLUDING options

## ADDED Requirements

### Requirement: MERGE INTO support
The parser SHALL support MERGE INTO syntax for conditional upsert operations:
- MERGE INTO target table
- USING source table or subquery
- ON join condition
- WHEN MATCHED THEN UPDATE
- WHEN NOT MATCHED THEN INSERT

#### Scenario: Basic MERGE INTO
- **WHEN** parsing "MERGE INTO tgt USING src ON tgt.id = src.id WHEN MATCHED THEN UPDATE SET val = src.val WHEN NOT MATCHED THEN INSERT (id, val) VALUES (src.id, src.val)"
- **THEN** parser SHALL produce valid MergeStatement AST node

#### Scenario: MERGE WITH multiple conditions
- **WHEN** parsing "MERGE ... WHEN MATCHED AND condition THEN DELETE ..."
- **THEN** parser SHALL support MATCHED AND condition

### Requirement: UPSERT (INSERT ON CONFLICT) support
The parser SHALL support UPSERT syntax:
- INSERT ... ON CONFLICT (col) DO UPDATE/SET
- ON CONFLICT DO NOTHING
- ON CONFLICT (col) WHERE condition

#### Scenario: INSERT ON CONFLICT DO UPDATE
- **WHEN** parsing "INSERT INTO t (a, b) VALUES (1, 2) ON CONFLICT (a) DO UPDATE SET b = EXCLUDED.b"
- **THEN** parser SHALL produce valid Upsert AST node

#### Scenario: INSERT ON CONFLICT DO NOTHING
- **WHEN** parsing "INSERT ... ON CONFLICT DO NOTHING"
- **THEN** parser SHALL handle DO NOTHING clause

### Requirement: RETURNING clause support
The parser SHALL support RETURNING clause in:
- INSERT ... RETURNING *
- UPDATE ... RETURNING *
- DELETE ... RETURNING *

#### Scenario: INSERT RETURNING
- **WHEN** parsing "INSERT INTO t VALUES (1) RETURNING *"
- **THEN** parser SHALL recognize RETURNING clause

### Requirement: FOR UPDATE/SHARE support
The parser SHALL support row-level locking:
- FOR UPDATE
- FOR SHARE
- FOR UPDATE OF table
- FOR UPDATE NOWAIT
- FOR UPDATE SKIP LOCKED

#### Scenario: SELECT FOR UPDATE
- **WHEN** parsing "SELECT * FROM t FOR UPDATE"
- **THEN** parser SHALL add locking modifier to SelectQuery

#### Scenario: SELECT FOR UPDATE NOWAIT
- **WHEN** parsing "SELECT * FROM t FOR UPDATE NOWAIT"
- **THEN** parser SHALL recognize NOWAIT option

### Requirement: Multi-table DELETE support
The parser SHALL support DELETE from multiple tables using alias

#### Scenario: Multi-table DELETE
- **WHEN** parsing "DELETE FROM t1, t2 USING t1 JOIN t2 ON ..."
- **THEN** parser SHALL handle multi-table syntax

### Requirement: Multi-table UPDATE support
The parser SHALL support UPDATE affecting multiple tables

#### Scenario: Multi-table UPDATE
- **WHEN** parsing "UPDATE t1 SET a = 1 FROM t2 WHERE t1.id = t2.id"
- **THEN** parser SHALL handle FROM clause in UPDATE

### Requirement: ORDER BY in subqueries
The parser SHALL support ORDER BY in subqueries with LIMIT

#### Scenario: ORDER BY in subquery
- **WHEN** parsing "SELECT * FROM (SELECT * FROM t ORDER BY a LIMIT 10) sub"
- **THEN** parser SHALL handle ORDER BY in subquery

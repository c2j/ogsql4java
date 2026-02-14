## 1. Grammar Infrastructure Setup

- [ ] 1.1 Analyze existing OpenGaussSQL.g4 structure and identify extension points
- [ ] 1.2 Review src_common_backend_parser/gram.y for reference syntax
- [ ] 1.3 Set up grammar testing framework for incremental validation

## 2. DDL Extended Statements

- [ ] 2.1 Add CREATE FUNCTION/REPLACE FUNCTION grammar rules
- [ ] 2.2 Add CREATE PROCEDURE grammar rules  
- [ ] 2.3 Add CREATE/DROP TRIGGER grammar rules
- [ ] 2.4 Add CREATE VIEW/REPLACE VIEW grammar rules
- [ ] 2.5 Add CREATE TYPE (enum, composite) grammar rules
- [ ] 2.6 Add CREATE SEQUENCE grammar rules
- [ ] 2.7 Add CREATE MATERIALIZED VIEW grammar rules
- [ ] 2.8 Add ALTER TABLE extended grammar rules
- [ ] 2.9 Add AST visitor support for new DDL nodes
- [ ] 2.10 Run regression tests and fix failures

## 3. DML Extended Statements

- [ ] 3.1 Add MERGE INTO grammar rules
- [ ] 3.2 Add INSERT ON CONFLICT (UPSERT) grammar rules
- [ ] 3.3 Add RETURNING clause grammar rules to INSERT/UPDATE/DELETE
- [ ] 3.4 Add FOR UPDATE/SHARE grammar rules
- [ ] 3.5 Add multi-table DELETE grammar rules
- [ ] 3.6 Add multi-table UPDATE grammar rules
- [ ] 3.7 Add AST visitor support for DML extensions
- [ ] 3.8 Run regression tests and fix failures

## 4. Data Types Extended

- [ ] 4.1 Add ENCODING clause grammar rules
- [ ] 4.2 Add DBCOMPATIBILITY clause grammar rules
- [ ] 4.3 Add INTERVAL type grammar rules
- [ ] 4.4 Add ORIENTATION clause grammar rules
- [ ] 4.5 Add PASSWORD clause grammar rules
- [ ] 4.6 Add STORAGE clause grammar rules
- [ ] 4.7 Add LIKE INCLUDING grammar rules
- [ ] 4.8 Add AST support for new type options
- [ ] 4.9 Run regression tests and fix failures

## 5. Query Features

- [ ] 5.1 Add WITH RECURSIVE (recursive CTE) grammar rules
- [ ] 5.2 Add Window function grammar rules (OVER, PARTITION BY)
- [ ] 5.3 Add FETCH WITH TIES grammar rules
- [ ] 5.4 Add NULLS FIRST/LAST grammar rules
- [ ] 5.5 Add GROUP BY ROLLUP/CUBE grammar rules
- [ ] 5.6 Add DISTINCT ON grammar rules
- [ ] 5.7 Add AST visitor support for query features
- [ ] 5.8 Run regression tests and fix failures

## 6. Transaction Control

- [ ] 6.1 Add START TRANSACTION grammar rules
- [ ] 6.2 Add SAVEPOINT grammar rules
- [ ] 6.3 Add SET TRANSACTION grammar rules
- [ ] 6.4 Add COMMIT/ROLLBACK options (AND CHAIN)
- [ ] 6.5 Add SET variable grammar rules
- [ ] 6.6 Add EXPLAIN/PREPARE/EXECUTE grammar rules
- [ ] 6.7 Run regression tests and fix failures

## 7. Compatibility Syntax

- [ ] 7.1 Add Oracle CONNECT BY grammar rules
- [ ] 7.2 Add MySQL LIMIT offset,count grammar rules
- [ ] 7.3 Add TEMPLATE clause grammar rules
- [ ] 7.4 Add ANALYZE grammar rules
- [ ] 7.5 Add CLUSTER grammar rules
- [ ] 7.6 Add COPY grammar rules
- [ ] 7.7 Add TRUNCATE CASCADE grammar rules
- [ ] 7.8 Run regression tests and fix failures

## 8. Partitioning DDL

- [ ] 8.1 Add PARTITION BY RANGE grammar rules
- [ ] 8.2 Add PARTITION BY LIST grammar rules
- [ ] 8.3 Add PARTITION BY HASH grammar rules
- [ ] 8.4 Add subpartition grammar rules
- [ ] 8.5 Add ALTER TABLE partition operations grammar
- [ ] 8.6 Add partition storage options grammar
- [ ] 8.7 Add AST visitor support for partitions
- [ ] 8.8 Run regression tests and fix failures

## 9. PL/pgSQL Support

- [ ] 9.1 Add stored procedure body block grammar (BEGIN/END)
- [ ] 9.2 Add cursor declaration grammar (DECLARE CURSOR)
- [ ] 9.3 Add exception handling grammar (EXCEPTION WHEN)
- [ ] 9.4 Add control structures grammar (IF/LOOP/FOR)
- [ ] 9.5 Add INOUT/OUT parameter grammar
- [ ] 9.6 Add function body grammar
- [ ] 9.7 Add AST visitor support for PL/pgSQL
- [ ] 9.8 Run regression tests and fix failures

## 10. Final Validation

- [ ] 10.1 Run full regression test suite
- [ ] 10.2 Verify target failure count (< 50)
- [ ] 10.3 Document any known limitations
- [ ] 10.4 Update AGENTS.md with implementation notes

## Why

The ogsql parser currently fails 626 out of 1516 regression tests (41% failure rate). These failures prevent the parser from supporting many OpenGauss/PostgreSQL SQL features that users rely on. The parser needs comprehensive SQL syntax coverage to be production-ready for OpenGauss compatibility.

## What Changes

Implement comprehensive SQL syntax support by extending the ANTLR4 grammar (OpenGaussSQL.g4) and updating the AST visitor layer. This is a phased implementation starting with the most critical syntax categories:

- **DDL Commands**: Add support for CREATE/DROP/ALTER statements for functions, procedures, views, triggers, types, sequences, materialized views, and extended table options
- **DML Extensions**: Add MERGE INTO, UPSERT (INSERT ON CONFLICT), RETURNING clause, and FOR UPDATE/SHARE
- **Data Types**: Add ENCODING, DBCOMPATIBILITY, INTERVAL, ORIENTATION, and database compatibility types
- **Query Features**: Add Recursive CTE, Window Functions, FETCH WITH TIES, NULLS FIRST/LAST, GROUP BY extensions
- **PL/pgSQL**: Add stored procedure bodies, cursors, exception handling, control structures
- **Partitioning**: Add partition DDL, subpartitioning, and partition operations
- **Compatibility**: Add Oracle/MySQL compatibility syntax
- **Transaction Control**: Add START TRANSACTION, SAVEPOINT, SET TRANSACTION

## Capabilities

### New Capabilities
- `ddl-extended-statements`: Support CREATE/DROP/ALTER for functions, procedures, views, triggers, types, sequences, materialized views
- `dml-extended-statements`: Support MERGE INTO, UPSERT, RETURNING, FOR UPDATE/SHARE, multi-table operations
- `data-types-extended`: Support ENCODING, DBCOMPATIBILITY, INTERVAL, ORIENTATION, compatibility types
- `query-features`: Support Recursive CTE, WITH TIES, NULLS FIRST/ Window Functions, FETCHLAST, GROUP BY ROLLUP/CUBE
- `pl-pgsql-support`: Support stored procedure bodies, cursors, exception handling, control structures (IF/LOOP)
- `partitioning-ddl`: Support PARTITION BY, subpartitioning, partition operations
- `compatibility-syntax`: Support Oracle/MySQL compatibility syntax (DBCOMPATIBILITY, PASSWORD, etc.)
- `transaction-control`: Support START TRANSACTION, SAVEPOINT, SET TRANSACTION

### Modified Capabilities
None - initial implementation of SQL syntax capabilities.

## Impact

- **Code**: Extend src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4 and visitor classes
- **Tests**: Enable 626+ regression tests to pass
- **Parser**: Achieve near-complete OpenGauss SQL syntax coverage

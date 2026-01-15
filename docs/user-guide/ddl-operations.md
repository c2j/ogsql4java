# DDL Operations: CREATE, ALTER, DROP Tables

**Feature**: 001-api-user-guide
**Document Type**: DDL Operations Reference
**Target Audience**: Java developers managing database schema
**Prerequisites**: Completed [Common Use Cases](common-use-cases.md), SQL DDL knowledge
**Difficulty**: Intermediate

## Table of Contents

1. [CREATE TABLE](#create-table)
2. [Partitioned Tables](#partitioned-tables)
3. [ALTER TABLE](#alter-table)
4. [DROP TABLE](#drop-table)
5. [Next Steps](#next-steps)

---

## CREATE TABLE

### Basic Table Creation

Create a regular table with constraints:

```sql
-- See DdlExamples.java for complete parsing code
CREATE TABLE users (
  id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(255) UNIQUE,
  active BOOLEAN DEFAULT true
);
```

**When to use**: Creating new tables for persistent data storage
**Key concepts**: Primary keys, NOT NULL constraints, DEFAULT values, UNIQUE constraints
**Related**: [Common Use Cases - INSERT](common-use-cases.md#insert-statements)

---

## Partitioned Tables

### RANGE Partitioning

Partition large tables by date range for better query performance:

```sql
-- See DdlExamples.java for complete parsing code
CREATE TABLE sales (
  id BIGINT,
  sale_date DATE NOT NULL,
  region VARCHAR(50),
  amount DECIMAL(12,2)
) PARTITION BY RANGE (sale_date) (
  PARTITION p2023 VALUES LESS THAN ('2024-01-01'),
  PARTITION p2024 VALUES LESS THAN ('2025-01-01')
);
```

**When to use**: Large tables with time-series data (millions+ rows)
**Key concepts**: Partition key, partition definitions, RANGE partitioning
**Related**: [Advanced Features - Hints](advanced-features.md#query-optimizer-hints)

### LIST Partitioning

Partition by discrete values:

```sql
CREATE TABLE orders (
  id BIGINT,
  region VARCHAR(50),
  amount DECIMAL(12,2)
) PARTITION BY LIST (region) (
  PARTITION p_north VALUES ('North', 'Northeast'),
  PARTITION p_south VALUES ('South', 'Southeast'),
  PARTITION p_west VALUES ('West', 'Southwest')
);
```

**When to use**: Regional or categorical data with clear boundaries
**Key concepts**: LIST partitioning, value lists, partition pruning

### HASH Partitioning

Distribute data evenly across partitions:

```sql
CREATE TABLE events (
  id BIGINT,
  event_type VARCHAR(50),
  event_data JSONB
) PARTITION BY HASH (event_type) (
  PARTITION p0,
  PARTITION p1,
  PARTITION p2,
  PARTITION p3
);
```

**When to use**: High-throughput tables needing even distribution
**Key concepts**: Hash partitioning, partition count, uniformity

---

## ALTER TABLE

### Add Column

Modify existing table structure:

```sql
-- See DdlExamples.java for complete parsing code
ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
```

**When to use**: Adding new columns without recreating table
**Key concepts**: DDL operations, default values, backward compatibility
**Related**: [Error Handling - Schema Changes](error-handling.md#schema-evolution)

### Drop Column

Remove column from table:

```sql
ALTER TABLE users DROP COLUMN temp_field;
```

**When to use**: Removing deprecated or unused columns
**Key concepts**: Data loss prevention, index recreation

---

## DROP TABLE

### Basic DROP

Remove table completely:

```sql
-- See DdlExamples.java for complete parsing code
DROP TABLE temp_data;
```

**When to use**: Removing temporary or obsolete tables
**Key concepts**: Permanent deletion, data loss risk

### Conditional DROP

Drop only if table exists:

```sql
DROP TABLE IF EXISTS temp_data;
```

**When to use**: Scripts that may run multiple times
**Key concepts**: Safety checks, conditional DDL

---

## DDL Statement Reference

| DDL Operation | Java Class | Example File |
|----------------|-------------|--------------|
| CREATE TABLE | `CreateStatement` | [DdlExamples.java](../examples/DdlExamples.java) |
| ALTER TABLE | `AlterStatement` | [DdlExamples.java](../examples/DdlExamples.java) |
| DROP TABLE | `DropStatement` | [DdlExamples.java](../examples/DdlExamples.java) |
| CREATE PARTITIONED | `CreateStatement` | [DdlExamples.java](../examples/DdlExamples.java) |

---

## Best Practices

1. **Always use IF EXISTS** in DROP operations in scripts
2. **Test partitioning strategy** on similar data before applying to production
3. **Consider partition maintenance** for RANGE partitions (add new partitions periodically)
4. **Document partitioning key** - changing it requires recreating table
5. **Use transactional DDL** where possible for atomic changes

---

## Next Steps

Continue with advanced OpenGauss features:

- **[Advanced Features](advanced-features.md)** - Query optimizer hints, foreign tables, metadata extraction
- **[Error Handling](error-handling.md)** - DDL error scenarios and troubleshooting
- **[Configuration](configuration.md)** - Performance tuning for partitioned tables

---

**Return to**: [Quick Start Guide](quick-start.md) | [Common Use Cases](common-use-cases.md) | [Documentation Index](README.md)

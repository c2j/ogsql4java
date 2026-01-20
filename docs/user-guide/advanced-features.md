# Advanced Features: OpenGauss-Specific Capabilities

**Feature**: 001-api-user-guide
**Document Type**: Advanced Features Reference
**Target Audience**: Experienced Java developers needing OpenGauss-specific features
**Prerequisites**: Completed [Common Use Cases](common-use-cases.md), [DDL Operations](ddl-operations.md)
**Difficulty**: Advanced

## Table of Contents

1. [Query Optimizer Hints](#query-optimizer-hints)
2. [Partitioning](#partitioning)
3. [Foreign Tables](#foreign-tables)
4. [Metadata Extraction](#metadata-extraction)
5. [Next Steps](#next-steps)

---

## Query Optimizer Hints

Control query execution plans for better performance:

```sql
/*+ NestLoop(u o) */
SELECT * FROM users u
JOIN orders o ON u.id = o.user_id
WHERE o.status = 'completed';
```

**Example file**: [HintsExamples.java](../examples/HintsExamples.java)

**When to use**: Fine-tuning performance for specific query patterns
**Available hints**:
- `NestLoop` - Force nested loop join
- `MergeJoin` - Use merge join algorithm
- `HashJoin` - Use hash join algorithm
- `SeqScan` - Sequential table scan
- `IndexScan` - Use index scan

**Key concepts**: Hint syntax, table aliases, join control
**Related**: [Configuration - Performance](configuration.md#performance-tuning)

---

## Partitioning

### RANGE Partitioning

Time-based data distribution:

```sql
-- See PartitioningExamples.java for complete code
CREATE TABLE sales (
  id BIGINT,
  sale_date DATE NOT NULL,
  amount DECIMAL(12,2)
) PARTITION BY RANGE (sale_date) (
  PARTITION p2023 VALUES LESS THAN ('2024-01-01'),
  PARTITION p2024 VALUES LESS THAN ('2025-01-01')
);
```

**Example file**: [PartitioningExamples.java](../examples/PartitioningExamples.java)

**When to use**: Time-series data with natural date boundaries
**Key concepts**: Partition pruning, range queries, maintenance

### LIST Partitioning

Categorical data distribution:

```sql
CREATE TABLE orders (
  id BIGINT,
  region VARCHAR(50),
  amount DECIMAL(12,2)
) PARTITION BY LIST (region) (
  PARTITION p_north VALUES ('North', 'Northeast'),
  PARTITION p_south VALUES ('South', 'Southeast')
);
```

**When to use**: Regional or categorical data with clear values
**Key concepts**: Partition elimination, value lists

### HASH Partitioning

Uniform data distribution:

```sql
CREATE TABLE events (
  id BIGINT,
  event_type VARCHAR(50)
) PARTITION BY HASH (event_type) (
  PARTITION p0,
  PARTITION p1,
  PARTITION p2,
  PARTITION p3
);
```

**When to use**: High-throughput tables needing even distribution
**Key concepts**: Partition count, load balancing

**Related**: [DDL Operations - Partitioned Tables](ddl-operations.md#partitioned-tables)

---

## Foreign Tables

Access external data sources:

```sql
-- See ForeignTableExamples.java for complete code
CREATE FOREIGN TABLE remote_users (
  id INTEGER,
  name VARCHAR(100)
) SERVER postgres_server
OPTIONS (
  host 'remote.db.com',
  port '5432',
  database 'users_db',
  fetch_size '1000'
);
```

**Example file**: [ForeignTableExamples.java](../examples/ForeignTableExamples.java)

**When to use**: Integrating with external PostgreSQL databases
**Key concepts**: Foreign data wrappers, server options, security
**Use cases**:
- Data migration scenarios
- Cross-database queries
- Legacy system integration

---

## Metadata Extraction

Extract structured information from SQL statements:

### Tables Extraction

Get all tables referenced in a query:

```java
// See MetadataExtractionExamples.java for complete code
import com.sdchat.ogsql.metadata.MetadataExtractor;

MetadataExtractor extractor = new MetadataExtractor();
extractor.extract(statement);
java.util.Set<String> tables = extractor.getTables();
```

### Columns Extraction

Get all columns in SELECT statement:

```java
java.util.Set<Column> columns = extractor.getColumns();
```

### Functions Extraction

Identify all function calls:

```java
java.util.Set<FunctionCall> functions = extractor.getFunctions();
```

### WHERE Conditions Extraction

Extract filter conditions:

```java
List<Condition> conditions = extractor.getWhereConditions();
```

**Example file**: [MetadataExtractionExamples.java](../examples/MetadataExtractionExamples.java)

**When to use**:
- SQL query analysis tools
- Audit logging
- Query optimization
- Data lineage tracking

**Related**: [Common Use Cases - Subqueries](common-use-cases.md#subqueries)

---

## Advanced Feature Reference

| Feature | Example File | Difficulty |
|----------|--------------|------------|
| Query Optimizer Hints | [HintsExamples.java](../examples/HintsExamples.java) | Advanced |
| RANGE Partitioning | [PartitioningExamples.java](../examples/PartitioningExamples.java) | Intermediate |
| LIST Partitioning | [PartitioningExamples.java](../examples/PartitioningExamples.java) | Intermediate |
| HASH Partitioning | [PartitioningExamples.java](../examples/PartitioningExamples.java) | Advanced |
| Foreign Tables | [ForeignTableExamples.java](../examples/ForeignTableExamples.java) | Advanced |
| Metadata Extraction | [MetadataExtractionExamples.java](../examples/MetadataExtractionExamples.java) | Advanced |

---

## Next Steps

Continue with production-ready features:

- **[Error Handling](error-handling.md)** - Comprehensive error scenarios and troubleshooting
- **[Configuration](configuration.md)** - Performance tuning and parser configuration

---

**Return to**: [Quick Start Guide](quick-start.md) | [Common Use Cases](common-use-cases.md) | [DDL Operations](ddl-operations.md) | [Documentation Index](README.md)

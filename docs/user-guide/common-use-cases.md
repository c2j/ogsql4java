# Common Use Cases: SELECT, INSERT, UPDATE, DELETE

**Feature**: 001-api-user-guide
**Document Type**: Common Use Cases Reference
**Target Audience**: Java developers needing examples for standard SQL operations
**Prerequisites**: Completed [Quick Start Guide](quick-start.md), Basic SQL knowledge
**Difficulty**: Beginner

## Table of Contents

1. [SELECT Queries](#select-queries)
2. [INSERT Statements](#insert-statements)
3. [UPDATE Statements](#update-statements)
4. [DELETE Statements](#delete-statements)
5. [Next Steps](#next-steps)

---

## SELECT Queries

### Simple SELECT

Use the [SelectExamples.java](../examples/SelectExamples.java) file for working examples:

```java
// See SelectExamples.java for complete code
SELECT id, name, email FROM users WHERE active = true;
```

**When to use**: Retrieving data from a single table with filtering
**Key concepts**: Columns, WHERE clause, boolean conditions
**Related**: [Quick Start - Handle Results](quick-start.md#3-handle-results)

---

### JOIN Operations

Combine data from multiple tables:

```java
// See SelectExamples.java for complete code
SELECT u.name, o.order_date
FROM users u
INNER JOIN orders o ON u.id = o.user_id
WHERE o.status = 'completed';
```

**When to use**: Retrieving related data from multiple tables
**Key concepts**: INNER JOIN, table aliases, foreign keys
**Related**: [Advanced Features - Partitioning](advanced-features.md#partitioning)

---

### Subqueries

Nest queries within queries:

```java
// See SelectExamples.java for complete code
SELECT name, email
FROM users
WHERE id IN (SELECT user_id FROM orders WHERE total > 1000);
```

**When to use**: Filtering based on results from another query
**Key concepts**: Nested SELECT, IN clause, correlation
**Related**: [Advanced Features - Metadata Extraction](advanced-features.md#metadata-extraction)

---

## INSERT Statements

### Single Row INSERT

Add one record at a time:

```java
// See InsertExamples.java for complete code
INSERT INTO users (name, email, active)
VALUES ('John Doe', 'john@example.com', true);
```

**When to use**: Adding individual records
**Key concepts**: Column specification, VALUES clause, data types
**Related**: [DDL Operations - CREATE TABLE](ddl-operations.md#create-table)

---

### Bulk INSERT

Insert multiple rows efficiently:

```java
// See InsertExamples.java for complete code
INSERT INTO users (name, email, active) VALUES
    ('Jane Doe', 'jane@example.com', true),
    ('Bob Smith', 'bob@example.com', false),
    ('Alice Johnson', 'alice@example.com', true);
```

**When to use**: Importing or adding multiple records at once
**Key concepts**: Multiple value sets, transaction efficiency
**Related**: [Configuration - Performance](configuration.md#performance-tuning)

---

## UPDATE Statements

### Simple UPDATE

Modify existing data:

```java
// See UpdateExamples.java for complete code
UPDATE users SET active = true WHERE id = 1;
```

**When to use**: Updating specific record(s) by key
**Key concepts**: SET clause, WHERE clause for filtering
**Related**: [Error Handling - Syntax Errors](error-handling.md#syntax-errors)

---

### Conditional UPDATE

Update based on complex conditions:

```java
// See UpdateExamples.java for complete code
UPDATE products
SET price = price * 0.9
WHERE category = 'electronics' AND stock > 50;
```

**When to use**: Bulk updates with multiple conditions
**Key concepts**: Expressions in SET clause, boolean operators in WHERE
**Related**: [Advanced Features - Hints](advanced-features.md#query-optimizer-hints)

---

## DELETE Statements

### Simple DELETE

Remove specific record(s):

```java
// See DeleteExamples.java for complete code
DELETE FROM users WHERE id = 999;
```

**When to use**: Removing specific records by primary key
**Key concepts**: Primary key deletion, WHERE clause importance
**Related**: [Error Handling - Cascading Deletes](error-handling.md#foreign-key-constraints)

---

### Conditional DELETE

Remove multiple records:

```java
// See DeleteExamples.java for complete code
DELETE FROM logs
WHERE created_at < '2023-01-01' AND level = 'debug';
```

**When to use**: Bulk data cleanup or archival
**Key concepts**: Date comparisons, multiple WHERE conditions
**Related**: [Configuration - Memory Limits](configuration.md#file-size-and-memory-limits)

---

## Statement Type Reference

| SQL Operation | Java Class | Example File |
|---------------|-------------|--------------|
| SELECT | `SelectQuery` | [SelectExamples.java](../examples/SelectExamples.java) |
| INSERT | `InsertStatement` | [InsertExamples.java](../examples/InsertExamples.java) |
| UPDATE | `UpdateStatement` | [UpdateExamples.java](../examples/UpdateExamples.java) |
| DELETE | `DeleteStatement` | [DeleteExamples.java](../examples/DeleteExamples.java) |

---

## Next Steps

Continue learning with more advanced features:

- **[DDL Operations](ddl-operations.md)** - CREATE, ALTER, DROP tables including partitioning
- **[Advanced Features](advanced-features.md)** - Query optimizer hints, metadata extraction, foreign tables
- **[Error Handling](error-handling.md)** - Comprehensive error scenarios and troubleshooting
- **[Configuration](configuration.md)** - Performance tuning and parser configuration options

---

**Return to**: [Quick Start Guide](quick-start.md) | [Documentation Index](README.md)

# OpenGauss SQL Parser User Guide

**Version**: 1.0.0
**Last Updated**: 2026-01-15
**Target Audience**: Java developers using the OpenGauss SQL Parser library

---

## Overview

This comprehensive user guide helps Java developers effectively use the OpenGauss SQL Parser to parse and analyze SQL statements. Whether you're building a database management tool, a query analyzer, or integrating SQL parsing into your application, this guide provides the examples and best practices you need.

---

## Prerequisites

- Java 17 or higher
- Basic Java programming knowledge
- Understanding of SQL syntax
- Maven for dependency management

---

## Quick Start

**New to the OpenGauss SQL Parser?** Start here!

- **[Quick Start Guide](quick-start.md)** 🚀
  - Add Maven dependency (5 minutes)
  - Parse your first SQL statement
  - Handle results and errors
  - Essential: 5 minutes to first success

---

## User Guides by Topic

### Common SQL Operations

Learn the fundamentals of parsing standard SQL statements:

- **[Common Use Cases](common-use-cases.md)** 📝
  - SELECT queries (simple, joins, subqueries)
  - INSERT statements (single-row, bulk)
  - UPDATE operations (simple, conditional)
  - DELETE statements (filtered, conditional)
  - Complete examples for everyday use cases

### Database Schema Management

Create and modify database structures:

- **[DDL Operations](ddl-operations.md)** 🗄️
  - CREATE TABLE (regular, partitioned)
  - ALTER TABLE (add/modify columns)
  - DROP TABLE (safe deletion)
  - Partitioning strategies (RANGE, LIST, HASH)

### Advanced Features

Leverage OpenGauss-specific capabilities:

- **[Advanced Features](advanced-features.md)** ⚡
  - Query optimizer hints (NestLoop, HashJoin, etc.)
  - Partitioning for large datasets
  - Foreign tables (external data sources)
  - Metadata extraction (tables, columns, functions)

### Error Handling & Troubleshooting

Build robust applications:

- **[Error Handling & Troubleshooting](error-handling.md)** 🔧
  - Error types (ParseException, InputValidationException)
  - Common error scenarios and fixes
  - Large file handling and streaming
  - Symptom-based troubleshooting guide

### Performance & Configuration

Optimize for your use case:

- **[Configuration & Performance Tuning](configuration.md)** ⚙️
  - Configuration options (memory, file size, buffers)
  - Error strategies (BAIL vs DEFAULT)
  - Performance tuning tips
  - Scenario-based recommendations (small, medium, large)

---

## Example Code Files

All example code is runnable and can be tested:

| Example File | Purpose | Difficulty |
|--------------|---------|------------|
| [QuickStartExample.java](../examples/QuickStartExample.java) | Basic parsing | Beginner |
| [SelectExamples.java](../examples/SelectExamples.java) | SELECT patterns | Beginner |
| [InsertExamples.java](../examples/InsertExamples.java) | INSERT patterns | Beginner |
| [UpdateExamples.java](../examples/UpdateExamples.java) | UPDATE patterns | Beginner |
| [DeleteExamples.java](../examples/DeleteExamples.java) | DELETE patterns | Beginner |
| [DdlExamples.java](../examples/DdlExamples.java) | DDL operations | Intermediate |
| [HintsExamples.java](../examples/HintsExamples.java) | Query hints | Advanced |
| [PartitioningExamples.java](../examples/PartitioningExamples.java) | Partitioning | Advanced |
| [ForeignTableExamples.java](../examples/ForeignTableExamples.java) | Foreign tables | Advanced |
| [MetadataExtractionExamples.java](../examples/MetadataExtractionExamples.java) | Metadata API | Advanced |
| [ErrorHandlingExamples.java](../examples/ErrorHandlingExamples.java) | Error handling | Intermediate |
| [ConfigurationExamples.java](../examples/ConfigurationExamples.java) | Configuration | Advanced |

---

## Learning Paths

### For Beginners (New to SQL parsing)

1. [Quick Start Guide](quick-start.md) - 5 minutes
2. [Common Use Cases](common-use-cases.md) - Practice patterns
3. [Error Handling](error-handling.md) - Handle issues

### For Intermediate Users (SQL knowledge)

1. [Common Use Cases](common-use-cases.md) - Refresh patterns
2. [DDL Operations](ddl-operations.md) - Schema management
3. [Error Handling](error-handling.md) - Troubleshooting
4. [Configuration](configuration.md) - Performance basics

### For Advanced Users (Production applications)

1. [Advanced Features](advanced-features.md) - OpenGauss-specific features
2. [Configuration](configuration.md) - Production tuning
3. [DDL Operations](ddl-operations.md) - Partitioning strategies

### For Performance Optimization

1. [Advanced Features](advanced-features.md) - Query hints
2. [DDL Operations](ddl-operations.md) - Partitioning
3. [Configuration](configuration.md) - Tuning recommendations

---

## Key Concepts

### Statement Types

| SQL Statement | Java Class | Where to Learn |
|--------------|-------------|----------------|
| SELECT | `SelectQuery` | [Common Use Cases](common-use-cases.md) |
| INSERT | `InsertStatement` | [Common Use Cases](common-use-cases.md) |
| UPDATE | `UpdateStatement` | [Common Use Cases](common-use-cases.md) |
| DELETE | `DeleteStatement` | [Common Use Cases](common-use-cases.md) |
| CREATE TABLE | `CreateStatement` | [DDL Operations](ddl-operations.md) |
| ALTER TABLE | `AlterStatement` | [DDL Operations](ddl-operations.md) |
| DROP TABLE | `DropStatement` | [DDL Operations](ddl-operations.md) |

### Parser API

| Method | Purpose | Learn More |
|---------|---------|------------|
| `parse(sql)` | Parse single SQL string | [Quick Start](quick-start.md) |
| `parseMultiple(sql)` | Parse multiple statements | [Common Use Cases](common-use-cases.md) |
| `parseFile(file)` | Parse from file | [Error Handling](error-handling.md) |
| `parseStream(stream)` | Parse from input stream | [Error Handling](error-handling.md) |
| MetadataExtractor | Extract metadata | [Advanced Features](advanced-features.md) |

---

## Best Practices

1. **Always handle exceptions** - Wrap parse calls in try-catch
2. **Check statement types** - Use instanceof before casting
3. **Configure appropriately** - Match settings to your use case
4. **Test with real data** - Verify with your actual SQL
5. **Profile performance** - Measure before optimizing
6. **Use error messages** - Leverage detailed error context
7. **Stream large files** - Use parseFile()/parseStream() for big data

---

## Troubleshooting Quick Reference

| Issue | Where to Find |
|--------|---------------|
| Can't parse SQL | [Error Handling](error-handling.md) |
| Out of memory errors | [Configuration](configuration.md) |
| Slow parsing | [Configuration](configuration.md) |
| File too large | [Error Handling](error-handling.md) |
| Wrong statement type | [Common Use Cases](common-use-cases.md) |
| Need better performance | [Advanced Features](advanced-features.md) |

---

## Support & Resources

- **Issue Tracking**: Report bugs in GitHub issues
- **Feature Requests**: Submit enhancement ideas
- **Documentation Updates**: Contribute improvements via pull requests

---

**Version**: 1.0.0 | **Last Updated**: 2026-01-15 | **Maintainer**: OpenGauss SQL Parser Team

**Return to**: [Project README](../../README.md)

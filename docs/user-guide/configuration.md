# Configuration & Performance Tuning

**Feature**: 001-api-user-guide
**Document Type**: Configuration Reference
**Target Audience**: Java developers building production applications
**Prerequisites**: Completed [Quick Start Guide](quick-start.md), [Error Handling](error-handling.md)
**Difficulty**: Intermediate to Advanced

## Table of Contents

1. [Configuration Options](#configuration-options)
2. [Error Strategies](#error-strategies)
3. [File Size & Memory Limits](#file-size-and-memory-limits)
4. [Performance Tuning](#performance-tuning)
5. [Scenario-Based Recommendations](#scenario-based-recommendations)
6. [Next Steps](#next-steps)

---

## Configuration Options

### Overview

The OpenGauss SQL Parser provides several configuration options to adapt to different use cases:

| Option | Default | Type | Purpose |
|---------|---------|-------|---------|
| `setMemoryLimit()` | 500MB | long | Memory constraint |
| `setMaxFileSize()` | 100MB | long | File size limit |
| `setStreamBufferSize()` | 8KB | int | Streaming buffer |
| `setErrorStrategy()` | DEFAULT | ErrorStrategy | Error handling mode |

### ErrorStrategy

#### ErrorStrategy.BAIL

Fail immediately on first error:

```java
parser.setErrorStrategy(ErrorStrategy.BAIL);
```

**When to use**:
- Production systems requiring fast failure
- Batch processing where one error should stop all
- Automated pipelines

**Benefits**:
- Fastest error detection
- Minimal processing time
- Clear failure points

#### ErrorStrategy.DEFAULT

Attempt to continue with detailed errors:

```java
parser.setErrorStrategy(ErrorStrategy.DEFAULT);
```

**When to use**:
- Development and testing
- Interactive applications
- When detailed error messages needed

**Benefits**:
- More error context
- Multiple errors reported per parse
- Better for debugging

**Example file**: [ConfigurationExamples.java](../examples/ConfigurationExamples.java)

---

## File Size & Memory Limits

### Memory Limits

Constrain parser memory usage:

```java
// Small project - 250MB
parser.setMemoryLimit(250 * 1024 * 1024);

// Medium project - 1GB
parser.setMemoryLimit(1024 * 1024 * 1024);

// Large project - 4GB
parser.setMemoryLimit(4 * 1024 * 1024 * 1024);
```

**When to adjust**:
- Running in memory-constrained environments
- Parsing multiple statements concurrently
- Processing very complex SQL

### File Size Limits

Prevent processing of oversized files:

```java
// Small files only - 50MB
parser.setMaxFileSize(50 * 1024 * 1024);

// Medium files - 500MB
parser.setMaxFileSize(500 * 1024 * 1024);

// Large files - 2GB
parser.setMaxFileSize(2 * 1024 * 1024 * 1024);
```

**When to adjust**:
- Security requirements (input sanitization)
- Known maximum data sizes
- Preventing DoS attacks

### Streaming Buffer Size

Optimize for large file streaming:

```java
// Default: 8KB
parser.setStreamBufferSize(8192);

// Larger buffer: 64KB
parser.setStreamBufferSize(65536);

// Very large buffer: 256KB
parser.setStreamBufferSize(262144);
```

**When to adjust**:
- Parsing very large files
- Network streaming
- Performance profiling shows I/O bottleneck

**Example file**: [ConfigurationExamples.java](../examples/ConfigurationExamples.java)

---

## Performance Tuning

### Optimize for Speed

1. **Use ErrorStrategy.BAIL** for production
   - Fastest error detection
   - Minimal overhead

2. **Appropriate buffer sizes**
   - Larger buffers for streaming large files
   - Smaller buffers for memory constraints

3. **Reuse parser instances**
   - Avoid recreating parsers for each statement
   - Reuse for batch operations

### Optimize for Memory

1. **Configure memory limits**
   - Set to match your environment constraints
   - Prevents out-of-memory errors

2. **Stream large files**
   - Use `parseFile()` or `parseStream()` instead of `parse()`
   - Reduces peak memory usage

3. **Parse individually**
   - For multiple statements, parse each separately
   - Allows garbage collection between parses

### Partitioning for Performance

Use partitioned tables for large datasets:

```sql
CREATE TABLE events (
  id BIGINT,
  event_date DATE NOT NULL,
  event_data JSONB
) PARTITION BY RANGE (event_date) (
  PARTITION p2023_q1 VALUES LESS THAN ('2023-04-01'),
  PARTITION p2023_q2 VALUES LESS THAN ('2023-07-01'),
  PARTITION p2023_q3 VALUES LESS THAN ('2023-10-01'),
  PARTITION p2023_q4 VALUES LESS THAN ('2024-01-01')
);
```

**Benefits**:
- Partition pruning (skip irrelevant partitions)
- Parallel query execution
- Faster maintenance operations

**Related**: [DDL Operations - Partitioning](ddl-operations.md#partitioned-tables) | [Advanced Features - Hints](advanced-features.md#query-optimizer-hints)

---

## Scenario-Based Recommendations

### Small Project (< 100MB total SQL)

**Configuration**:
```java
SQLParser parser = new SQLParser();
// Use defaults - they're optimal for small projects
```

**Characteristics**:
- Few SQL statements
- Simple queries
- No streaming needed

**Performance**: Excellent (defaults optimized for this use case)

### Medium Application (100MB - 1GB SQL)

**Configuration**:
```java
SQLParser parser = new SQLParser();
parser.setMemoryLimit(512 * 1024 * 1024); // 512MB
parser.setMaxFileSize(100 * 1024 * 1024); // 100MB
parser.setErrorStrategy(ErrorStrategy.DEFAULT); // Detailed errors for debugging
```

**Characteristics**:
- Multiple SQL operations
- Some complex queries
- Occasional streaming

**Performance**: Good with tuned limits

### Large Enterprise System (> 1GB SQL)

**Configuration**:
```java
SQLParser parser = new SQLParser();
parser.setMemoryLimit(2 * 1024 * 1024 * 1024); // 2GB
parser.setMaxFileSize(500 * 1024 * 1024); // 500MB
parser.setStreamBufferSize(65536); // 64KB buffer
parser.setErrorStrategy(ErrorStrategy.BAIL); // Fast failure for production
```

**Characteristics**:
- High-volume parsing
- Complex queries and hints
- Frequent streaming
- Partitioned tables

**Performance**: Optimal with custom tuning

### Batch Processing Pipeline

**Configuration**:
```java
SQLParser parser = new SQLParser();
parser.setErrorStrategy(ErrorStrategy.BAIL); // Fast failure
parser.setMaxFileSize(100 * 1024 * 1024); // Strict limits
parser.setMemoryLimit(512 * 1024 * 1024); // Predictable memory
```

**Characteristics**:
- Automated processing
- One error should stop pipeline
- Known data sizes

**Performance**: Best for automation (fast fail)

---

## Best Practices

1. **Profile before tuning** - Measure actual memory and performance
2. **Start with defaults** - Only change if you have specific needs
3. **Set realistic limits** - Match your actual data patterns
4. **Monitor in production** - Adjust based on real usage
5. **Test edge cases** - Verify behavior with maximum expected sizes

---

## Configuration Reference

| Option | Method | Default | Recommended | Use Case |
|---------|---------|---------|-------------|
| Memory Limit | `setMemoryLimit(long)` | 500MB | Adjust per environment |
| File Size Limit | `setMaxFileSize(long)` | 100MB | Security/validation |
| Stream Buffer | `setStreamBufferSize(int)` | 8KB | Large file streaming |
| Error Strategy | `setErrorStrategy(ErrorStrategy)` | DEFAULT | BAIL for production |

---

## Next Steps

Complete your application setup:

- **[Advanced Features](advanced-features.md)** - Query optimizer hints, partitioning
- **[Error Handling](error-handling.md)** - Common error scenarios and troubleshooting

---

**Return to**: [Quick Start Guide](quick-start.md) | [Common Use Cases](common-use-cases.md) | [Documentation Index](README.md)

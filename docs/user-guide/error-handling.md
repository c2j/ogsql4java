# Error Handling & Troubleshooting

**Feature**: 001-api-user-guide
**Document Type**: Error Handling & Troubleshooting Guide
**Target Audience**: Java developers building production applications
**Prerequisites**: Completed [Quick Start Guide](quick-start.md), basic SQL knowledge
**Difficulty**: Intermediate

## Table of Contents

1. [Error Types](#error-types)
2. [Common Error Scenarios](#common-error-scenarios)
3. [Troubleshooting Guide](#troubleshooting-guide)
4. [Large Files & Streaming](#large-files--streaming)
5. [Next Steps](#next-steps)

---

## Error Types

### ParseException

Thrown when SQL syntax is invalid:

```java
// See ErrorHandlingExamples.java for complete code
try {
    SQLStatement statement = parser.parse(sql);
} catch (ParseException e) {
    System.out.println("Parse error: " + e.getMessage());
    System.out.println("Line: " + e.getLine());
    System.out.println("Column: " + e.getColumn());
}
```

**When to use**: User-provided SQL or dynamic query generation
**Recovery**: Report error to user, validate input, show example

### InputValidationException

Thrown when input violates constraints (size, memory):

```java
try {
    SQLStatement statement = parser.parseFile(file);
} catch (InputValidationException e) {
    System.out.println("Validation error: " + e.getMessage());
    System.out.println("Check file size or memory limits");
}
```

**When to use**: Parsing large files or untrusted input
**Recovery**: Adjust configuration limits, use streaming, implement pagination

**Example file**: [ErrorHandlingExamples.java](../examples/ErrorHandlingExamples.java)

---

## Common Error Scenarios

### Syntax Errors

**Symptoms**:
- "Syntax error at line X:Y"
- "Unexpected token"
- "Mismatched input"

**Common causes**:
1. **Missing keywords**: `SELEC` instead of `SELECT`
2. **Mismatched quotes**: Single quote instead of double quote
3. **Extra commas**: Trailing comma in column list
4. **Missing clauses**: SELECT without FROM

**Solution steps**:
1. Check the error message for line and column
2. Review SQL at that location
3. Compare with working examples in [Common Use Cases](common-use-cases.md)
4. Use SQL formatter for readability
5. Verify all parentheses are balanced

**Example fix**:
```sql
-- WRONG: SELEC id FROM users
-- CORRECT: SELECT id FROM users
```

**Related**: [Quick Start - Handle Errors](quick-start.md#4-handle-errors)

---

### Type Casting Errors

**Symptoms**:
- ClassCastException when casting statement
- NullPointer after cast

**Common causes**:
1. **Wrong type**: Casting to wrong statement type
2. **Not checking type**: Casting without instanceof check
3. **Assuming type**: Assuming all statements are SELECT

**Solution steps**:
1. Always use instanceof before casting
2. Handle all expected statement types
3. Use getStatementType() for logging
4. Provide default handling for unknown types

**Example fix**:
```java
-- WRONG:
SelectQuery select = (SelectQuery) statement; // May fail

-- CORRECT:
if (statement instanceof SelectQuery) {
    SelectQuery select = (SelectQuery) statement;
}
```

---

### Memory Issues

**Symptoms**:
- OutOfMemoryError
- Slow parsing performance
- High memory usage

**Common causes**:
1. **Large files**: Files exceeding memory limits
2. **Deep nesting**: Very complex subqueries
3. **Many statements**: Batch operations on large datasets

**Solution steps**:
1. Configure memory limits appropriately (see [Configuration](configuration.md))
2. Use streaming for very large files
3. Parse statements individually instead of all at once
4. Increase JVM heap size: `-Xmx2g`

**Example configuration**:
```java
parser.setMemoryLimit(500 * 1024 * 1024); // 500MB
parser.setStreamBufferSize(16384); // 16KB buffer
```

---

### Large File Issues

**Symptoms**:
- File size exceeds limits
- Slow parsing performance
- Timeout on large files

**Solution steps**:
1. Use parseFile() or parseStream() instead of parse()
2. Adjust file size limits for your use case
3. Implement progress tracking for user feedback
4. Consider chunking data into smaller files

**Example**:
```java
// See ErrorHandlingExamples.java for streaming code
ParseResult result = parser.parseFile(largeSqlFile);
if (result.isSuccess()) {
    // Process results
}
```

---

## Troubleshooting Guide

### Search by Error Message

| Error Message | Likely Cause | Solution |
|---------------|---------------|----------|
| "SELEC id FROM users" | Typo in SELECT | Fix spelling to `SELECT` |
| "missing FROM clause" | SELECT without FROM | Add FROM clause |
| "extraneous input" | Extra characters | Check for typos or extra commas |
| "file size exceeds limit" | File too large | Configure larger limit or stream |
| "out of memory" | Memory exhaustion | Increase limit or chunk data |

### Search by Symptom

| Symptom | Likely Issue | Solution |
|----------|--------------|----------|
| Parser throws exception on simple SQL | Syntax error | Check spelling and keywords |
| Cast fails at runtime | Wrong statement type | Use instanceof check |
| Slow on large files | Not streaming | Use parseFile()/parseStream() |
| Can't find specific column | Wrong AST access | Verify statement type first |

---

## Large Files & Streaming

### Configuration for Large Files

```java
parser.setMaxFileSize(500 * 1024 * 1024); // 500MB
parser.setMemoryLimit(1024 * 1024 * 1024); // 1GB
parser.setStreamBufferSize(65536); // 64KB buffer
```

### Streaming Approach

Process large files incrementally:

```java
try (InputStream stream = new FileInputStream(file)) {
    ParseResult result = parser.parseStream(stream);
    while (hasMoreData) {
        // Process chunk
    }
}
```

**Benefits**:
- Reduced memory footprint
- Progress tracking
- Better error context
- Cancellation support

**Related**: [Configuration - File Size Limits](configuration.md#file-size-and-memory-limits)

---

## Prevention Tips

1. **Validate input before parsing** - Check for basic syntax issues
2. **Use proper error handling** - Catch and handle all exceptions
3. **Configure appropriate limits** - Match to your use case
4. **Test with sample data** - Verify parser works with your SQL
5. **Log parsing errors** - Track common issues for improvement

---

## Error Handling Reference

| Error Type | Exception Class | Example File |
|-------------|------------------|--------------|
| Syntax Errors | `ParseException` | [ErrorHandlingExamples.java](../examples/ErrorHandlingExamples.java) |
| Input Validation | `InputValidationException` | [ErrorHandlingExamples.java](../examples/ErrorHandlingExamples.java) |
| Memory Issues | OutOfMemoryError | [Configuration](configuration.md#file-size-and-memory-limits) |
| Large Files | File size limit | [ErrorHandlingExamples.java](../examples/ErrorHandlingExamples.java) |

---

## Next Steps

Optimize your application:

- **[Configuration](configuration.md)** - Performance tuning and parser options
- **[Advanced Features](advanced-features.md)** - Query optimizer hints and partitioning

---

**Return to**: [Quick Start Guide](quick-start.md) | [Common Use Cases](common-use-cases.md) | [Documentation Index](README.md)

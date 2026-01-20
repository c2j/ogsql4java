# Quick Start: OpenGauss SQL Parser User Guide

**Feature**: 001-api-user-guide
**Document Type**: Quick Start Guide
**Target Audience**: Java developers new to the OpenGauss SQL Parser
**Prerequisites**: Basic Java knowledge, SQL familiarity, Java 17+ environment
**Estimated Time**: 5 minutes

## Table of Contents

1. [Add Dependency](#1-add-dependency)
2. [First Parse](#2-first-parse)
3. [Handle Results](#3-handle-results)
4. [Handle Errors](#4-handle-errors)
5. [Next Steps](#next-steps)

---

## 1. Add Dependency

Add the OpenGauss SQL Parser dependency to your Maven project:

```xml
<dependency>
    <groupId>com.sdchat</groupId>
    <artifactId>ogsql</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Expected Output**: Dependency downloaded successfully to your local Maven repository.

**Explanation**: This Maven dependency includes the parser library, ANTLR4 runtime, and all required components for SQL parsing.

---

## 2. First Parse

Create a parser instance and parse your first SQL statement:

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.exception.ParseException;

// Create parser instance
SQLParser parser = new SQLParser();

// Parse a simple SQL statement
String sql = "SELECT id, name, email FROM users WHERE active = true";
try {
    SQLStatement statement = parser.parse(sql);

    // Check statement type and access results
    if (statement instanceof SelectQuery) {
        SelectQuery select = (SelectQuery) statement;
        System.out.println("Statement type: " + statement.getStatementType());
        System.out.println("FROM clause: " + select.getFromClause());
    }
} catch (ParseException e) {
    System.out.println("Parse error: " + e.getMessage());
}
```

**Expected Output**:
```
Statement type: SELECT
FROM clause: FROM users WHERE active = true
```

**Explanation**:
- `SQLParser` is the main entry point for parsing
- `parse()` method takes a SQL string and returns an `SQLStatement` (may throw `ParseException`)
- Use `instanceof` to check the specific statement type
- Cast to the appropriate type (e.g., `SelectQuery`) to access statement-specific properties
- Always wrap parse calls in try-catch to handle potential exceptions

---

## 3. Handle Results

Parse the result and access different types of statements:

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.ast.InsertStatement;
import com.sdchat.ogsql.ast.UpdateStatement;
import com.sdchat.ogsql.exception.ParseException;

SQLParser parser = new SQLParser();

// Different statement types
String selectSql = "SELECT * FROM products WHERE price > 100";
String insertSql = "INSERT INTO users (name, email) VALUES ('John', 'john@example.com')";
String updateSql = "UPDATE users SET active = true WHERE id = 1";

// Parse and handle each type
try {
    SQLStatement stmt1 = parser.parse(selectSql);
    if (stmt1 instanceof SelectQuery) {
        SelectQuery select = (SelectQuery) stmt1;
        System.out.println("SELECT - FROM clause: " + select.getFromClause());
    }

    SQLStatement stmt2 = parser.parse(insertSql);
    if (stmt2 instanceof InsertStatement) {
        InsertStatement insert = (InsertStatement) stmt2;
        System.out.println("INSERT - Table: " + insert.getTableName());
    }

    SQLStatement stmt3 = parser.parse(updateSql);
    if (stmt3 instanceof UpdateStatement) {
        UpdateStatement update = (UpdateStatement) stmt3;
        System.out.println("UPDATE - Table: " + update.getTableName());
    }
} catch (ParseException e) {
    System.out.println("Parse error: " + e.getMessage());
}
```

**Expected Output**:
```
SELECT - FROM clause: FROM products WHERE price > 100
INSERT - Table: users
UPDATE - Table: users
```

**Explanation**:
- Different SQL statement types (SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP) return different AST classes
- Always check the type using `instanceof` before casting
- Each statement type has specific properties (tables, columns, values, etc.)
- Wrap parse calls in try-catch to handle potential `ParseException`

---

## 4. Handle Errors

Gracefully handle parsing errors:

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;

SQLParser parser = new SQLParser();

try {
    // SQL with syntax error
    String sql = "SELEC id FROM users";  // Missing 'T' in SELECT
    SQLStatement statement = parser.parse(sql);
    System.out.println("Parse successful");
} catch (ParseException e) {
    System.out.println("Parse error: " + e.getMessage());
    System.out.println("Please check your SQL syntax");
}
```

**Expected Output**:
```
Parse error: SQL statement cannot be parsed due to syntax error
Please check your SQL syntax
```

**Explanation**:
- `ParseException` is thrown when SQL has syntax errors
- Always wrap parse calls in try-catch blocks
- Error messages indicate the type of problem
- For more detailed error information with line/column numbers, use `ParseResult` instead (see Advanced Features)

---

## 5. Next Steps

Congratulations! You've successfully parsed your first SQL statements. Continue learning:

- **[Common Use Cases](common-use-cases.md)** - Learn SELECT, INSERT, UPDATE, DELETE patterns
- **[DDL Operations](ddl-operations.md)** - Understand CREATE, ALTER, DROP statements
- **[Advanced Features](advanced-features.md)** - Explore hints, partitioning, metadata extraction
- **[Error Handling](error-handling.md)** - Comprehensive error handling and troubleshooting
- **[Configuration](configuration.md)** - Performance tuning and parser configuration

---

## Quick Reference

### Common Statement Types

| SQL Statement | Java Class | Example |
|--------------|------------|---------|
| SELECT | `SelectQuery` | `SELECT * FROM users` |
| INSERT | `InsertStatement` | `INSERT INTO users VALUES (1, 'John')` |
| UPDATE | `UpdateStatement` | `UPDATE users SET name = 'Jane'` |
| DELETE | `DeleteStatement` | `DELETE FROM users WHERE id = 1` |
| CREATE TABLE | `CreateStatement` | `CREATE TABLE users (id INT)` |
| ALTER TABLE | `AlterStatement` | `ALTER TABLE users ADD COLUMN email VARCHAR` |
| DROP TABLE | `DropStatement` | `DROP TABLE users` |

### Key Methods

| Method | Purpose |
|--------|---------|
| `parser.parse(sql)` | Parse single SQL statement |
| `parser.parseMultiple(sql)` | Parse multiple statements (semicolon-separated) |
| `parser.parseFile(file)` | Parse SQL from file |
| `parser.parseStream(inputStream)` | Parse SQL from input stream |
| `statement.getStatementType()` | Get statement type as string |

---

## Troubleshooting

**Problem**: "Dependency not found" error
**Solution**: Ensure Maven is properly configured and you have internet access to download dependencies

**Problem**: "ClassNotFoundException"
**Solution**: Verify the dependency version matches your project requirements

**Problem**: "ParseException" with unclear message
**Solution**: Check your SQL syntax carefully, especially for missing keywords or misplaced clauses

---

**You completed the Quick Start in under 5 minutes! 🎉**

For more examples and advanced usage, continue to the [Common Use Cases](common-use-cases.md) guide.

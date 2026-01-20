# Quickstart Guide: OpenGauss SQL Parser

**Feature**: 001-gaussdb-parser
**Last Updated**: 2026-01-12

## Overview

This guide helps you get started with the OpenGauss SQL Parser library. The parser converts OpenGauss SQL statements into structured Abstract Syntax Trees (ASTs) that you can programmatically analyze, transform, or query for metadata.

## Prerequisites

- Java 17 or higher
- Maven 3.6+ or Gradle 7+
- IDE: IntelliJ IDEA, Eclipse, or VS Code with Java support

## Installation

### Maven Projects

Add dependency to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.sdchat</groupId>
        <artifactId>ogsql</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle Projects

Add dependency to your `build.gradle`:

```gradle
dependencies {
    implementation 'com.sdchat:ogsql:1.0.0'
}
```

## Basic Usage

### Parse a Simple SELECT Statement

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.exception.ParseException;

public class ParserExample {
    public static void main(String[] args) {
        String sql = "SELECT id, name FROM users WHERE active = true";

        try {
            SQLParser parser = new SQLParser();
            SelectQuery query = parser.parse(sql, SelectQuery.class);

            System.out.println("Query parsed successfully!");
            System.out.println("Columns: " + query.getSelectedColumns());
            System.out.println("Table: " + query.getFromClause());

        } catch (ParseException e) {
            System.err.println("Parsing failed:");
            System.err.println("Error at line " + e.getLine() +
                              ", column " + e.getColumn());
            System.err.println("Message: " + e.getMessage());
        }
    }
}
```

### Parse Multiple Statements

```java
String sql = """
    CREATE TABLE users (
        id INT PRIMARY KEY,
        name VARCHAR(255)
    );

    INSERT INTO users (id, name) VALUES (1, 'John');

    SELECT * FROM users;
    """;

SQLParser parser = new SQLParser();
List<SQLStatement> statements = parser.parseMultiple(sql);

for (SQLStatement stmt : statements) {
    System.out.println("Statement type: " + stmt.getStatementType());
}
```

### Parse with Options

```java
import java.util.HashMap;
import java.util.Map;

String sql = "SELECT * FROM users";

Map<String, Object> options = new HashMap<>();
options.put("includeHints", false);    // Don't include hints in AST
options.put("includeLocation", false);  // Don't include position info
options.put("errorStrategy", "bail");  // Fail fast on errors

ParseResult result = parser.parse(sql, options);

if (result.isSuccess()) {
    SQLStatement stmt = result.getStatement();
    System.out.println("Parsed: " + stmt.getStatementType());
} else {
    ParsingError error = result.getError();
    System.err.println("Error: " + error.getMessage());
}
```

## Working with the AST

### Traversing the AST with Visitor Pattern

```java
import com.sdchat.ogsql.ast.visitor.ASTVisitor;
import com.sdchat.ogsql.ast.*;

public class PrintVisitor implements ASTVisitor<Void> {
    @Override
    public Void visit(SelectQuery query) {
        System.out.println("SELECT Query:");
        System.out.println("  Columns: " + query.getSelectedColumns().size());
        System.out.println("  Tables: " + query.getFromClause().size());
        return null;
    }

    @Override
    public Void visit(CreateStatement statement) {
        System.out.println("CREATE " + statement.getObjectType() +
                          ": " + statement.getObjectName());
        System.out.println("  Columns: " + statement.getColumns().size());
        return null;
    }

    // Implement other visit methods...
}

// Usage:
SQLStatement stmt = parser.parse(sql);
PrintVisitor visitor = new PrintVisitor();
stmt.accept(visitor);
```

### Extracting Query Information

```java
SelectQuery query = parser.parse(sql, SelectQuery.class);

// Get selected columns
for (Column col : query.getSelectedColumns()) {
    System.out.println("Column: " + col.getName() +
                      ", Type: " + col.getDataType());
}

// Get WHERE clause
ValueExpression where = query.getWhereClause();
if (where != null) {
    System.out.println("Filter: " + formatExpression(where));
}

// Get tables
for (DataSource source : query.getFromClause()) {
    System.out.println("Table: " + source.getName() +
                      ", Alias: " + source.getAlias());
}
```

### Accessing Query Hints

```java
SelectQuery query = parser.parse(sql, SelectQuery.class);

List<PerformanceHint> hints = query.getHints();
for (PerformanceHint hint : hints) {
    System.out.println("Hint type: " + hint.getHintType());
    System.out.println("  Affected tables: " + hint.getTableReferences());

    Map<String, String> params = hint.getParameters();
    params.forEach((key, value) ->
        System.out.println("  " + key + ": " + value)
    );
}
```

### Working with Partitioned Tables

```java
CreateStatement stmt = parser.parse(sql, CreateStatement.class);
PartitioningInfo partitioning = stmt.getPartitioning();

if (partitioning != null) {
    System.out.println("Partition type: " + partitioning.getType());
    System.out.println("Partition keys: " + partitioning.getPartitionKeys());

    for (PartitionDefinition part : partitioning.getPartitions()) {
        System.out.println("Partition: " + part.getName());
        System.out.println("  Range: " +
                          part.getMinValue() + " to " +
                          part.getMaxValue());
    }
}
```

### Working with Foreign Tables

```java
CreateStatement stmt = parser.parse(sql, CreateStatement.class);
ExternalTable foreignTable = stmt.getExternalTable();

if (foreignTable != null) {
    System.out.println("Foreign table: " + foreignTable.getTableName());
    System.out.println("Server: " + foreignTable.getServerName());

    Map<String, String> options = foreignTable.getServerOptions();
    options.forEach((key, value) ->
        System.out.println("  " + key + ": " + value)
    );
}
```

## Extracting Metadata

### Extract Table and Column References

```java
import com.sdchat.ogsql.metadata.MetadataExtractor;

String sql = """
    SELECT u.name, o.order_date
    FROM users u JOIN orders o ON u.id = o.user_id
    WHERE o.total > 100;
    """;

SQLParser parser = new SQLParser();
SQLStatement stmt = parser.parse(sql);

MetadataExtractor extractor = new MetadataExtractor();
Metadata metadata = extractor.extract(stmt);

// Get all referenced tables
List<String> tables = metadata.getTables();
System.out.println("Tables: " + tables);  // [users, orders]

// Get all column references
List<ColumnReference> columns = metadata.getColumns();
for (ColumnReference col : columns) {
    System.out.println("Column: " + col.getName() +
                      ", Table: " + col.getTable() +
                      ", Location: " + col.getLocation());
}

// Get all function calls
List<FunctionCall> functions = metadata.getFunctions();
for (FunctionCall func : functions) {
    System.out.println("Function: " + func.getName() +
                      ", Args: " + func.getArgumentCount());
}
```

### Extract WHERE Conditions

```java
SelectQuery query = parser.parse(sql, SelectQuery.class);
ValueExpression where = query.getWhereClause();

if (where != null) {
    WhereExtractor extractor = new WhereExtractor();
    List<Condition> conditions = extractor.extract(where);

    for (Condition cond : conditions) {
        System.out.println("Condition: " +
                          cond.getLeft() + " " +
                          cond.getOperator() + " " +
                          cond.getRight());
    }
}
```

## Error Handling

### Handling Syntax Errors

```java
try {
    SQLStatement stmt = parser.parse(sql);
} catch (SyntaxErrorException e) {
    // Syntax error with precise location
    System.err.println("Syntax error at line " + e.getLine() +
                      ", column " + e.getColumn());
    System.err.println("Context: " + e.getContext());
    System.err.println("Suggestion: " + e.getSuggestion());

} catch (SemanticErrorException e) {
    // Semantic error (e.g., invalid partition type)
    System.err.println("Semantic error: " + e.getMessage());

} catch (InputValidationException e) {
    // Input validation error (e.g., file too large)
    System.err.println("Input error: " + e.getMessage());

} catch (ParseException e) {
    // Generic parsing error
    System.err.println("Parsing failed: " + e.getMessage());
}
```

### Parsing with Bail Error Strategy

```java
// For production: Fast fail on first error
SQLParser parser = new SQLParser();
parser.setErrorStrategy(ErrorStrategy.BAIL);

try {
    SQLStatement stmt = parser.parse(sql);
} catch (ParseException e) {
    // Fails immediately on first error
    System.err.println("Parse failed: " + e.getMessage());
}
```

### Parsing with Default Error Strategy

```java
// For development: Try to recover and find multiple errors
SQLParser parser = new SQLParser();
parser.setErrorStrategy(ErrorStrategy.DEFAULT);

ParseResult result = parser.parse(sql);

if (!result.isSuccess()) {
    List<ParsingError> errors = result.getErrors();
    for (ParsingError error : errors) {
        System.err.println("Error at line " + error.getLine() +
                          ": " + error.getMessage());
    }
}
```

## Advanced Usage

### Custom AST Visitor

```java
public class SQLFormatter implements ASTVisitor<String> {
    @Override
    public String visit(SelectQuery query) {
        StringBuilder sb = new StringBuilder("SELECT ");

        // Format columns
        for (int i = 0; i < query.getSelectedColumns().size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(formatColumn(query.getSelectedColumns().get(i)));
        }

        // Format FROM clause
        sb.append(" FROM ");
        for (DataSource source : query.getFromClause()) {
            sb.append(source.getName());
        }

        return sb.toString();
    }

    // Implement other visit methods...
}

// Usage:
SQLStatement stmt = parser.parse(sql);
SQLFormatter formatter = new SQLFormatter();
String formatted = stmt.accept(formatter);
System.out.println(formatted);
```

### Streaming Large SQL Files

```java
import java.nio.file.Files;
import java.nio.file.Path;

Path sqlFile = Path.of("large_file.sql");
SQLParser parser = new SQLParser();

// Configure for streaming
parser.setStreamingMode(true);
parser.setMaxFileSize(100 * 1024 * 1024); // 100MB

try (Stream<String> lines = Files.lines(sqlFile)) {
    String sql = String.join("\n", (String[]) lines.toArray());

    List<SQLStatement> statements = parser.parseMultiple(sql);
    System.out.println("Parsed " + statements.size() + " statements");

} catch (InputValidationException e) {
    System.err.println("File too large: " + e.getMessage());
}
```

## Performance Tips

### Optimize for Repeated Parsing

```java
// Cache parsed statements for reuse
Map<String, SQLStatement> statementCache = new HashMap<>();

SQLStatement getCachedStatement(String sql) {
    return statementCache.computeIfAbsent(sql, key -> {
        return parser.parse(sql);
    });
}
```

### Configure Memory Limits

```java
SQLParser parser = new SQLParser();

// Set memory limit (default: 500MB)
parser.setMaxMemoryBytes(500 * 1024 * 1024);

// Set statement count limit for batch parsing
parser.setMaxStatements(10000);
```

## Testing Your Code

### Unit Testing Parsed SQL

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void testSimpleSelect() {
        String sql = "SELECT id FROM users";

        SQLParser parser = new SQLParser();
        SelectQuery query = parser.parse(sql, SelectQuery.class);

        assertEquals("users",
                     query.getFromClause().get(0).getName());
        assertEquals(1,
                     query.getSelectedColumns().size());
    }

    @Test
    void testInvalidSyntax() {
        String sql = "SELECT id FROMT users";

        SQLParser parser = new SQLParser();

        assertThrows(SyntaxErrorException.class, () -> {
            parser.parse(sql);
        });
    }
}
```

## Troubleshooting

### Common Issues

**Issue**: `ClassNotFoundException: com.sdchat.ogsql.parser.SQLParser`
**Solution**: Verify Maven/Gradle dependency is correctly added and project is rebuilt

**Issue**: Parsing is slow
**Solution**:
- Use `setErrorStrategy(ErrorStrategy.BAIL)` for fast fail
- Enable streaming mode for large files
- Check for memory leaks in custom visitors

**Issue**: Error messages are not detailed
**Solution**: Use `ErrorStrategy.DEFAULT` for development to see all errors

**Issue**: Can't parse OpenGauss-specific syntax
**Solution**: Ensure you're using the latest version with all OpenGauss features supported

## Next Steps

- Read [API documentation](../contracts/api-schema.yaml) for complete API reference
- Review [data model](../data-model.md) for AST structure details
- Check [examples](../contracts/examples/sql-examples.md) for more usage patterns
- Contribute to the [OpenGauss grammar] for missing features

## Support

- Documentation: [Link to project documentation]
- Issues: [Link to GitHub issues]
- Discussion: [Link to discussion forum]

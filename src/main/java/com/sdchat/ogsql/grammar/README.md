# OpenGauss SQL Grammar

**Version**: 1.0.0
**Based on**: PostgreSQL ANTLR4 grammar from antlr/grammars-v4
**Extensions**: OpenGauss-specific features (Hints, Partitioning, Foreign Tables)

## Grammar File

- **File**: `OpenGaussSQL.g4`
- **Type**: Combined grammar (lexer + parser)
- **Package**: `com.sdchat.ogsql.grammar`

## Structure

### Parser Rules

The grammar defines SQL statement types:

- **SELECT**: Query statements with FROM, WHERE, GROUP BY, HAVING, ORDER BY, LIMIT
- **INSERT**: Insert statements with VALUES or subquery
- **UPDATE**: Update statements with SET clause
- **DELETE**: Delete statements with optional USING clause
- **CREATE**: CREATE TABLE with columns, constraints, partitioning
- **DROP**: DROP TABLE/INDEX statements
- **ALTER**: ALTER TABLE statements

### Lexer Rules

Keywords, operators, identifiers, and literals for OpenGauss SQL syntax.

### OpenGauss Extensions

#### Query Optimizer Hints

Embedded in SQL comments: `/*+ hint */`

Supported hint types:
- `NESTLOOP`: Force nested loop join
- `MERGEJOIN`: Force merge join
- `HASHJOIN`: Force hash join
- `INDEXSCAN`: Force index scan
- `BITMAPSCAN`: Use bitmap scan
- `HASHAGG`: Force hash aggregation

Example:
```sql
SELECT /*+ NESTLOOP(users orders) */ * FROM users JOIN orders ON ...
```

#### Table Partitioning

PARTITION BY clause in CREATE TABLE:

Supported strategies:
- `PARTITION BY RANGE`: Range-based partitioning
- `PARTITION BY LIST`: List-based partitioning
- `PARTITION BY HASH`: Hash-based partitioning

Example:
```sql
CREATE TABLE orders (
    id INT,
    order_date DATE
) PARTITION BY RANGE (order_date) (
    PARTITION p1 VALUES LESS THAN ('2024-01-01'),
    PARTITION p2 VALUES LESS THAN ('2025-01-01')
);
```

#### Foreign Tables

CREATE FOREIGN TABLE for external data sources:

Example:
```sql
CREATE FOREIGN TABLE ext_users (
    id INT,
    name VARCHAR(255)
) SERVER my_server OPTIONS (host 'remote', port '5432');
```

## Building the Grammar

### Generate Parser Code

```bash
mvn clean generate-sources
```

This runs the ANTLR4 Maven plugin and generates:
- `OpenGaussSQLLexer.java` - Lexer implementation
- `OpenGaussSQLParser.java` - Parser implementation
- `OpenGaussSQLBaseListener.java` - Base listener for parse tree traversal
- `OpenGaussSQLListener.java` - Listener interface

### Generated Files Location

After running `mvn generate-sources`, files are generated to:
```
target/generated-sources/antlr4/
```

The generated Java files include the package declaration `com.sdchat.ogsql.grammar`.

## Usage

### Parsing SQL

```java
import com.sdchat.ogsql.grammar.*;

// Create lexer and parser
CharStream input = CharStreams.fromString("SELECT * FROM users");
OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

// Parse SQL
ParserRuleContext tree = parser.root();
```

### Visiting Parse Tree

```java
// Custom visitor extends OpenGaussSQLBaseVisitor
public class SQLVisitor extends OpenGaussSQLBaseVisitor<String> {
    @Override
    public String visitSelectstmt(OpenGaussSQLParser.SelectstmtContext ctx) {
        // Handle SELECT statement
        return null;
    }
}
```

### Grammar-First Development

This grammar is the authoritative contract for SQL parsing. Any changes to supported SQL syntax must be made here first.

## Versioning

Grammar versioning follows semantic versioning:
- **MAJOR**: Breaking changes to existing rules
- **MINOR**: New rules or features added
- **PATCH**: Bug fixes or clarifications

Current version: **1.0.0** (MVP subset of full OpenGauss SQL)

## References

- PostgreSQL Grammar: https://github.com/antlr/grammars-v4/tree/master/sql/postgresql
- OpenGauss SQL Reference: https://opengauss.org/zh/docs/
- ANTLR4 Documentation: https://www.antlr.org/

## Limitations (MVP)

Current grammar supports MVP subset of OpenGauss SQL:
- Basic SELECT, INSERT, UPDATE, DELETE, CREATE TABLE
- Simple joins (no complex nested subqueries)
- Basic hints (NestLoop, MergeJoin, HashJoin)
- Simple partitioning (RANGE, LIST, HASH)
- Foreign table creation

Future versions will expand to full OpenGauss SQL grammar.

## Notes

- Grammar is combined (lexer + parser in one file) for maintainability
- All keywords are case-insensitive
- Comments (both `--` and `/* */`) are skipped during tokenization
- Error recovery is handled by ANTLR4's default error strategy

# Example SQL Queries for Testing

This directory contains example OpenGauss SQL queries and their expected parsing outcomes.

## Examples by User Story

### User Story 1: Parse Basic SQL Statements

#### Example 1.1: Simple SELECT
```sql
SELECT id, name, email FROM users WHERE active = true;
```

**Expected AST**:
```json
{
  "success": true,
  "statementType": "SELECT",
  "ast": {
    "distinct": false,
    "selectedColumns": [
      {"type": "COLUMN", "name": "id"},
      {"type": "COLUMN", "name": "name"},
      {"type": "COLUMN", "name": "email"}
    ],
    "fromClause": [
      {
        "type": "TABLE",
        "name": "users",
        "alias": null
      }
    ],
    "whereClause": {
      "type": "BINARY_OP",
      "operator": "=",
      "leftOperand": {"type": "COLUMN", "name": "active"},
      "rightOperand": {"type": "LITERAL", "value": "true"}
    },
    "groupByClause": [],
    "orderByClause": [],
    "hints": []
  },
  "location": {
    "startLine": 1,
    "startColumn": 1,
    "endLine": 1,
    "endColumn": 51
  }
}
```

#### Example 1.2: CREATE TABLE
```sql
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Expected AST**:
```json
{
  "success": true,
  "statementType": "CREATE_TABLE",
  "ast": {
    "objectType": "TABLE",
    "objectName": "users",
    "columns": [
      {
        "name": "id",
        "dataType": "INT",
        "nullable": false,
        "constraints": [{"type": "PRIMARY_KEY"}]
      },
      {
        "name": "name",
        "dataType": "VARCHAR(255)",
        "nullable": false,
        "constraints": [{"type": "NOT_NULL"}]
      },
      {
        "name": "email",
        "dataType": "VARCHAR(255)",
        "nullable": true,
        "constraints": [{"type": "UNIQUE"}]
      },
      {
        "name": "created_at",
        "dataType": "TIMESTAMP",
        "nullable": true,
        "defaultValue": {"type": "FUNCTION", "name": "CURRENT_TIMESTAMP"}
      }
    ],
    "constraints": [],
    "partitioning": null,
    "options": {}
  }
}
```

#### Example 1.3: Invalid SQL (Syntax Error)
```sql
SELECT id, name FROMT users;
```

**Expected Error**:
```json
{
  "success": false,
  "error": {
    "message": "Syntax error: expected FROM keyword",
    "line": 1,
    "column": 16,
    "severity": "ERROR",
    "context": "SELECT id, name FROMT",
    "suggestion": "Did you mean FROM?"
  }
}
```

---

### User Story 2: Parse OpenGauss-Specific Hints

#### Example 2.1: Single Hint
```sql
/*+ NestLoop(u o) */
SELECT u.id, o.order_date
FROM users u JOIN orders o ON u.id = o.user_id;
```

**Expected AST**:
```json
{
  "success": true,
  "statementType": "SELECT",
  "ast": {
    "distinct": false,
    "selectedColumns": [
      {"type": "COLUMN", "name": "u.id"},
      {"type": "COLUMN", "name": "o.order_date"}
    ],
    "fromClause": [
      {
        "type": "JOIN",
        "joinType": "INNER",
        "tableReferences": [
          {"type": "TABLE", "name": "users", "alias": "u"},
          {"type": "TABLE", "name": "orders", "alias": "o"}
        ],
        "joinCondition": {
          "type": "BINARY_OP",
          "operator": "=",
          "leftOperand": {"type": "COLUMN", "name": "u.id"},
          "rightOperand": {"type": "COLUMN", "name": "o.user_id"}
        }
      }
    ],
    "hints": [
      {
        "type": "NestLoop",
        "tableReferences": ["u", "o"],
        "parameters": {}
      }
    ]
  }
}
```

#### Example 2.2: Multiple Hints
```sql
/*+ HashJoin(users orders) BitmapScan(orders) */
SELECT COUNT(*) FROM users u JOIN orders o ON u.id = o.user_id;
```

**Expected AST**:
```json
{
  "success": true,
  "statementType": "SELECT",
  "ast": {
    "hints": [
      {
        "type": "HashJoin",
        "tableReferences": ["users", "orders"],
        "parameters": {}
      },
      {
        "type": "BitmapScan",
        "tableReferences": ["orders"],
        "parameters": {}
      }
    ],
    "selectedColumns": [{"type": "FUNCTION", "name": "COUNT", "arguments": [{"type": "LITERAL", "value": "*"}]}],
    "fromClause": [/* ... join definition ... */]
  }
}
```

---

### User Story 3: Parse Partitioned Table Definitions

#### Example 3.1: Range Partitioning
```sql
CREATE TABLE sales (
    id INT,
    sale_date DATE,
    amount DECIMAL(10,2),
    region VARCHAR(50)
) PARTITION BY RANGE (sale_date) (
    PARTITION p2023 VALUES LESS THAN ('2024-01-01'),
    PARTITION p2024 VALUES LESS THAN ('2025-01-01'),
    PARTITION pmax VALUES LESS THAN (MAXVALUE)
);
```

**Expected AST**:
```json
{
  "success": true,
  "statementType": "CREATE_TABLE",
  "ast": {
    "objectType": "TABLE",
    "objectName": "sales",
    "columns": [
      {"name": "id", "dataType": "INT", "nullable": true},
      {"name": "sale_date", "dataType": "DATE", "nullable": true},
      {"name": "amount", "dataType": "DECIMAL(10,2)", "nullable": true},
      {"name": "region", "dataType": "VARCHAR(50)", "nullable": true}
    ],
    "partitioning": {
      "type": "RANGE",
      "partitionKeys": ["sale_date"],
      "partitions": [
        {"name": "p2023", "minValue": null, "maxValue": "'2024-01-01'"},
        {"name": "p2024", "minValue": null, "maxValue": "'2025-01-01'"},
        {"name": "pmax", "minValue": null, "maxValue": "MAXVALUE"}
      ],
      "subpartitioning": null
    }
  }
}
```

#### Example 3.2: List Partitioning
```sql
CREATE TABLE events (
    id INT,
    event_type VARCHAR(20),
    description TEXT
) PARTITION BY LIST (event_type) (
    PARTITION p_login VALUES ('login'),
    PARTITION p_logout VALUES ('logout'),
    PARTITION p_error VALUES ('error'),
    PARTITION p_other VALUES (DEFAULT)
);
```

#### Example 3.3: Hash Partitioning
```sql
CREATE TABLE metrics (
    id INT,
    metric_name VARCHAR(100),
    value FLOAT
) PARTITION BY HASH (id) PARTITIONS 16;
```

---

### User Story 4: Parse Foreign Table Definitions

#### Example 4.1: CREATE FOREIGN TABLE
```sql
CREATE FOREIGN TABLE remote_users (
    id INT,
    name VARCHAR(255)
) SERVER postgres_server
OPTIONS (
    host 'db.example.com',
    port '5432',
    dbname 'production'
);
```

**Expected AST**:
```json
{
  "success": true,
  "statementType": "CREATE_FOREIGN_TABLE",
  "ast": {
    "tableName": "remote_users",
    "serverName": "postgres_server",
    "serverOptions": {
      "host": "db.example.com",
      "port": "5432",
      "dbname": "production"
    },
    "columns": [
      {"name": "id", "dataType": "INT", "nullable": true},
      {"name": "name", "dataType": "VARCHAR(255)", "nullable": true}
    ],
    "tableOptions": {}
  }
}
```

---

### User Story 5: Extract SQL Metadata

#### Example 5.1: Extract from SELECT with JOINs
```sql
SELECT u.name, o.total
FROM users u
JOIN orders o ON u.id = o.user_id
WHERE o.order_date > '2024-01-01';
```

**Expected Metadata**:
```json
{
  "success": true,
  "tables": ["users", "orders"],
  "columns": [
    {"name": "u.name", "table": "users", "location": "SELECT"},
    {"name": "o.total", "table": "orders", "location": "SELECT"},
    {"name": "u.id", "table": "users", "location": "JOIN"},
    {"name": "o.user_id", "table": "orders", "location": "JOIN"},
    {"name": "o.order_date", "table": "orders", "location": "WHERE"}
  ],
  "functions": [],
  "whereConditions": [
    {
      "left": "o.order_date",
      "operator": ">",
      "right": "'2024-01-01'"
    }
  ]
}
```

#### Example 5.2: Extract from SELECT with Functions
```sql
SELECT
    COUNT(*) AS total_users,
    AVG(age) AS average_age,
    MAX(created_at) AS last_created
FROM users
WHERE active = true;
```

**Expected Metadata**:
```json
{
  "success": true,
  "tables": ["users"],
  "columns": [
    {"name": "active", "table": "users", "location": "WHERE"}
  ],
  "functions": [
    {"name": "COUNT", "arguments": 1},
    {"name": "AVG", "arguments": 1},
    {"name": "MAX", "arguments": 1}
  ],
  "whereConditions": [
    {"left": "active", "operator": "=", "right": "true"}
  ]
}
```

---

## Edge Cases

### Edge Case 1: Large File (100MB)
**Input**: SQL file with 100MB of INSERT statements
**Expected**: Parser completes successfully, returns all parsed statements
**Performance**: Should complete within reasonable time (benchmark target: <100s for 100MB)

### Edge Case 2: Unicode and Multi-byte Strings
```sql
INSERT INTO messages (id, text, language)
VALUES (1, '你好世界', 'zh-CN'),
       (2, 'مرحبا', 'ar-SA'),
       (3, 'こんにちは', 'ja-JP');
```
**Expected**: Successfully parses UTF-8 encoded strings with multi-byte characters

### Edge Case 3: Nested Comments
```sql
/*
 * This is a multi-line comment
 * With nested /* inner comment */ content
 */
SELECT * FROM users; -- End of line comment
```
**Expected**: Comments are ignored, parser processes SELECT statement correctly

### Edge Case 4: Identifier Case Sensitivity
```sql
SELECT "UserName", "UserID" FROM "MyTable";
SELECT username, userid FROM mytable;
```
**Expected**: Quoted identifiers preserve case ("UserName"), unquoted are lowercase (username)

### Edge Case 5: Semicolons in String Literals
```sql
INSERT INTO snippets (code) VALUES ('SELECT * FROM users;');
```
**Expected**: Semicolon inside string literal is not treated as statement separator

### Edge Case 6: Complex Operator Precedence
```sql
SELECT a + b * c, (d OR e) AND f
FROM expressions;
```
**Expected**: Correctly interprets operator precedence (multiplication before addition)

---

## Test Data Generation

Automated test case generation should cover:
- All statement types (SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP)
- All hint types (NestLoop, MergeJoin, HashJoin, etc.)
- All partition types (RANGE, LIST, HASH)
- All join types (INNER, LEFT, RIGHT, FULL, CROSS)
- All data types (INT, VARCHAR, DATE, TIMESTAMP, etc.)
- Edge cases (Unicode, large files, comments, errors)

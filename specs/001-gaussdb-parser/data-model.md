# Data Model: OpenGauss SQL Parser

**Feature**: 001-gaussdb-parser
**Date**: 2026-01-12
**Purpose**: Define Abstract Syntax Tree (AST) entity structure and relationships

## Overview

The data model represents parsed SQL statements as hierarchical, type-safe Java objects. All entities follow the visitor pattern for traversal and support metadata extraction. The design prioritizes immutability where possible and follows JavaBean conventions for mutable fields.

## Core Hierarchy

### Base Classes

```java
// Base interface for all SQL statements
public interface SQLStatement {
    StatementType getStatementType();
    void accept(ASTVisitor<T> visitor);
}

// Statement type enumeration
public enum StatementType {
    SELECT, INSERT, UPDATE, DELETE, CREATE_TABLE,
    ALTER_TABLE, DROP_TABLE, CREATE_FOREIGN_TABLE,
    UNKNOWN
}

// Base AST visitor interface
public interface ASTVisitor<T> {
    T visit(SelectQuery query);
    T visit(CreateStatement statement);
    T visit(InsertStatement statement);
    T visit(UpdateStatement statement);
    T visit(DeleteStatement statement);
    T visit(AlterStatement statement);
    T visit(DropStatement statement);
    // ... additional visit methods for other statement types
}
```

## Entity Definitions

### 1. SQLStatement

**Purpose**: Base interface for all parsed SQL statements

**Fields**:
- `StatementType type`: Type of SQL statement (SELECT, INSERT, etc.)
- `int startLine`: Line number where statement begins
- `int startColumn`: Column number where statement begins
- `int endLine`: Line number where statement ends
- `int endColumn`: Column number where statement ends

**Relationships**: Base class for all statement types

**Validation**:
- Line and column numbers must be non-negative
- Start position must be <= end position

---

### 2. SelectQuery

**Purpose**: Represents SELECT query with all its components

**Fields**:
- `boolean distinct`: Whether DISTINCT keyword is present
- `List<Column> selectedColumns`: Columns or expressions in SELECT list
- `List<DataSource> fromClause`: Tables, joins, and subqueries in FROM clause
- `ValueExpression whereClause`: Filter condition (null if no WHERE)
- `List<ValueExpression> groupByClause`: Grouping expressions (empty if no GROUP BY)
- `ValueExpression havingClause`: Filter for groups (null if no HAVING)
- `List<OrderByItem> orderByClause`: Sort order (empty if no ORDER BY)
- `Integer limit`: Maximum rows to return (null if no LIMIT)
- `Integer offset`: Number of rows to skip (null if no OFFSET)
- `List<PerformanceHint> hints`: Query optimizer hints (empty if none)

**Relationships**:
- Many-to-Many: `Column` (selectedColumns)
- Many-to-Many: `DataSource` (fromClause)
- One-to-One: `ValueExpression` (whereClause, havingClause)
- One-to-Many: `PerformanceHint` (hints)

**Validation**:
- Selected columns cannot be empty (must select at least one item)
- FROM clause can be empty (e.g., `SELECT 1`)
- If GROUP BY present, HAVING can reference only GROUP BY columns
- OFFSET requires LIMIT

---

### 3. CreateStatement

**Purpose**: Represents CREATE TABLE/INDEX/VIEW command

**Fields**:
- `String objectType`: "TABLE", "INDEX", "VIEW", etc.
- `String objectName`: Name of created object
- `List<Column> columns`: Column definitions (null for non-table objects)
- `List<Constraint> constraints`: Table constraints (PRIMARY KEY, FOREIGN KEY, etc.)
- `PartitioningInformation partitioning`: Partition definition (null if not partitioned)
- `Map<String, String> options`: Additional options (e.g., "WITH (fillfactor=70)")

**Relationships**:
- Many-to-Many: `Column` (columns)
- Many-to-Many: `Constraint` (constraints)
- One-to-One: `PartitioningInformation` (partitioning)

**Validation**:
- Object name cannot be null or empty
- For TABLE objects: at least one column required
- Partitioning only valid for TABLE objects
- Option keys must be non-empty strings

---

### 4. PerformanceHint

**Purpose**: Represents query optimizer hint embedded in SQL comments

**Fields**:
- `String hintType`: Type of hint (e.g., "NestLoop", "MergeJoin", "HashJoin")
- `List<String> tableReferences`: Table names affected by hint
- `Map<String, String> parameters`: Hint-specific parameters (e.g., "rows=1000")

**Relationships**: Standalone entity

**Validation**:
- Hint type must be one of: NestLoop, MergeJoin, HashJoin, HashAggregate, BitmapScan, etc.
- Table references can be empty (e.g., global hints)
- Parameter keys must be valid for hint type

**Supported Hint Types**:
- `NestLoop`: Force nested loop join method
- `MergeJoin`: Force merge join method
- `HashJoin`: Force hash join method
- `HashAggregate`: Force hash aggregation
- `BitmapScan`: Use bitmap scan method
- `IndexScan`: Use specific index (parameter: "index_name")

---

### 5. PartitioningInformation

**Purpose**: Represents table partitioning strategy and configuration

**Fields**:
- `PartitionType type`: RANGE, LIST, or HASH
- `List<String> partitionKeys`: Column names used as partition keys
- `List<PartitionDefinition> partitions`: Individual partition definitions
- `PartitioningInformation subpartitioning`: Nested partitioning (null if none)

**Relationships**:
- Many-to-Many: `PartitionDefinition` (partitions)
- One-to-One: `PartitioningInformation` (subpartitioning)

**Validation**:
- Partition keys cannot be empty
- For RANGE/LIST: partitions must not be empty
- For HASH: partitions can be auto-generated
- Subpartitioning only allowed if primary partitioning exists

**Partition Types**:
- `RANGE`: Partitions based on value ranges
- `LIST`: Partitions based on discrete value lists
- `HASH`: Partitions based on hash of partition keys

---

### 6. ExternalTable

**Purpose**: Represents foreign table definition (external data source mapping)

**Fields**:
- `String tableName`: Name of foreign table
- `String serverName`: Name of foreign server
- `Map<String, String> serverOptions`: Connection parameters (e.g., "host", "port")
- `List<Column> columns`: Column definitions with external options
- `Map<String, String> tableOptions`: Table-level options

**Relationships**:
- Many-to-Many: `Column` (columns)

**Validation**:
- Table name and server name cannot be null or empty
- At least one column required
- Required server options: "host" or connection string must be present

---

### 7. Column

**Purpose**: Represents a table column definition

**Fields**:
- `String name`: Column name
- `String dataType`: SQL data type (e.g., "VARCHAR(255)", "INTEGER")
- `boolean nullable`: Whether column allows NULL (default: true)
- `ValueExpression defaultValue`: Default value expression (null if no default)
- `List<Constraint> constraints`: Column-level constraints (NOT NULL, CHECK, etc.)
- `Map<String, String> options`: Column-specific options

**Relationships**:
- Many-to-Many: `Constraint` (constraints)

**Validation**:
- Column name cannot be null or empty
- Data type must be valid SQL type
- Default value (if present) must match data type
- NOT NULL constraint conflicts with nullable=true

---

### 8. DataSource

**Purpose**: Represents a table, view, or subquery in FROM clause

**Fields**:
- `DataSourceType type`: TABLE, VIEW, SUBQUERY, JOIN
- `String name`: Table or view name (null for subqueries)
- `String alias`: Alternative name used in query (null if no alias)
- `SelectQuery subquery`: Nested SELECT query (null if not subquery)
- `JoinType joinType`: Type of join (null if not joined)
- `ValueExpression joinCondition`: ON condition for join (null if no condition)

**Relationships**:
- One-to-One: `SelectQuery` (subquery)
- One-to-One: `ValueExpression` (joinCondition)

**Validation**:
- Name and subquery cannot both be null
- Join condition required for INNER/LEFT/RIGHT/FULL joins
- Alias cannot be empty string (use null if no alias)

**Join Types**:
- `INNER`: Inner join (default)
- `LEFT`: Left outer join
- `RIGHT`: Right outer join
- `FULL`: Full outer join
- `CROSS`: Cross join (no condition)
- `NONE`: Not joined

---

### 9. ValueExpression

**Purpose**: Represents a computed value in SQL (column, literal, function, operator)

**Fields**:
- `ExpressionType type`: Type of expression
- `String columnName`: Column reference (null if not column)
- `String literalValue`: Literal value (null if not literal)
- `String functionName`: Function name (null if not function)
- `List<ValueExpression> arguments`: Function arguments (empty if not function)
- `String operator`: Binary/unary operator (null if not operator)
- `ValueExpression leftOperand`: Left side of operator (null if not operator)
- `ValueExpression rightOperand`: Right side of operator (null if not operator)

**Relationships**:
- Many-to-Many: `ValueExpression` (arguments, leftOperand, rightOperand)

**Validation**:
- Exactly one expression component must be non-null (column, literal, function, or operator)
- If function: arguments can be empty (no-arg function)
- If binary operator: both operands required
- If unary operator: left operand required

**Expression Types**:
- `COLUMN`: Reference to table column
- `LITERAL`: String, number, boolean, NULL literal
- `FUNCTION`: Function call with arguments
- `BINARY_OP`: +, -, *, /, =, !=, <, >, AND, OR
- `UNARY_OP`: NOT, -, + (unary)

---

### 10. ParsingError

**Purpose**: Represents a parsing error with detailed information

**Fields**:
- `String message`: Human-readable error description
- `int line`: Line number where error occurred
- `int column`: Column number where error occurred
- `ErrorSeverity severity`: ERROR, WARNING, INFO
- `String context`: SQL snippet around error (50 chars)
- `String suggestion`: Hint for fixing error (null if none)

**Relationships**: Standalone entity

**Validation**:
- Message cannot be null or empty
- Line and column must be non-negative
- Context length must be ≤ 100 characters
- Severity must be ERROR for syntax errors, WARNING for semantic issues

---

### 11. Additional Supporting Entities

#### OrderByItem
- `ValueExpression expression`: Value to sort by
- `boolean ascending`: Sort direction (true = ASC, false = DESC)

#### Constraint
- `ConstraintType type`: PRIMARY_KEY, FOREIGN_KEY, UNIQUE, CHECK, NOT_NULL
- `String name`: Constraint name (null if unnamed)
- `String definition`: Constraint definition (e.g., "REFERENCES users(id)")

#### PartitionDefinition
- `String name`: Partition name
- `String minValue`: Start value (for RANGE/LIST)
- `String maxValue`: End value (for RANGE/LIST)
- `List<String> values`: Explicit values (for LIST)
- `String tablespace`: Tablespace for partition (null if default)

---

## State Transitions

Not applicable (parser is stateless - each parse creates new AST).

## Relationships Summary

```
SQLStatement (interface)
├── SelectQuery
│   ├── List<Column>
│   ├── List<DataSource>
│   ├── ValueExpression (where, having)
│   ├── List<ValueExpression> (groupBy)
│   ├── List<OrderByItem>
│   └── List<PerformanceHint>
├── CreateStatement
│   ├── List<Column>
│   ├── List<Constraint>
│   └── PartitioningInformation
│       └── List<PartitionDefinition>
├── InsertStatement
├── UpdateStatement
├── DeleteStatement
├── AlterStatement
├── DropStatement
└── ExternalTable
    └── List<Column>

DataSource
├── SelectQuery (subquery)
└── ValueExpression (join condition)

ValueExpression
├── List<ValueExpression> (function arguments, operands)
└── Recursive (nested expressions)

Column
└── List<Constraint>
```

## Immutable vs Mutable Design

**Immutable**:
- `ParsingError`: Once created, never changes
- `PerformanceHint`: Hints are fixed during parse
- `PartitioningInformation`: Partition strategy doesn't change

**Mutable** (with setters):
- `SelectQuery`: Allow modifications (e.g., adding WHERE clause after parse)
- `Column`: Support optional fields during AST construction
- `DataSource`: Aliases and join conditions may be modified

Rationale: AST should be immutable after parsing, but builders/setters simplify construction. Consider using Builder pattern for mutable construction phase.

## Validation Rules Summary

1. All string names must be non-null and non-empty (object names, column names, etc.)
2. Position information (line, column) must be non-negative integers
3. Conditional fields must be valid (e.g., ORDER BY with ORDER BY present)
4. Type checking: Expression types must be compatible with operations
5. Reference checking: Column references must resolve to valid tables (in semantic analysis)
6. Constraint consistency: NOT NULL cannot have default NULL value

## Next Steps

Use this data model to create:
- `contracts/api-schema.yaml`: API contract for parser
- `contracts/examples/`: Example SQL queries and expected AST structures
- `quickstart.md`: Developer getting started guide

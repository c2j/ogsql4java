## ADDED Requirements

### Requirement: Parse SQL and extract table nodes
The system SHALL parse SQL statements and extract all table references as nodes in the relationship graph.

#### Scenario: Extract single table from simple query
- **WHEN** parsing SQL "SELECT * FROM users"
- **THEN** the graph SHALL contain exactly one node representing table "users"

#### Scenario: Extract multiple tables from JOIN query
- **WHEN** parsing SQL "SELECT * FROM users u JOIN orders o ON u.id = o.user_id"
- **THEN** the graph SHALL contain two nodes representing tables "users" and "orders"

#### Scenario: Extract tables from subquery
- **WHEN** parsing SQL "SELECT * FROM (SELECT * FROM products) AS p"
- **THEN** the graph SHALL contain node "products" and properly handle the alias "p"

### Requirement: Extract column information for each table
The system SHALL extract column references and associate them with their respective table nodes.

#### Scenario: Extract columns from SELECT clause
- **WHEN** parsing SQL "SELECT id, name, email FROM users"
- **THEN** the "users" node SHALL contain column information for "id", "name", and "email"

#### Scenario: Extract columns with table qualifiers
- **WHEN** parsing SQL "SELECT u.id, u.name, o.amount FROM users u JOIN orders o ON u.id = o.user_id"
- **THEN** each table node SHALL contain only columns referenced with its qualifier

### Requirement: Identify and represent table relationships
The system SHALL identify relationships between tables based on JOIN conditions and foreign key references.

#### Scenario: Identify INNER JOIN relationship
- **WHEN** parsing SQL with "INNER JOIN orders ON users.id = orders.user_id"
- **THEN** the graph SHALL contain an edge between "users" and "orders" nodes with relationship type "INNER_JOIN"

#### Scenario: Identify LEFT JOIN relationship
- **WHEN** parsing SQL with "LEFT JOIN orders ON users.id = orders.user_id"
- **THEN** the graph SHALL contain an edge with relationship type "LEFT_JOIN"

#### Scenario: Identify join condition columns
- **WHEN** parsing SQL with JOIN condition "ON users.id = orders.user_id AND users.status = orders.status"
- **THEN** the edge SHALL contain both join column pairs: (users.id, orders.user_id) and (users.status, orders.status)

### Requirement: Support complex SQL patterns
The system SHALL support parsing complex SQL patterns including nested subqueries, UNION, and multiple JOIN levels.

#### Scenario: Parse nested subqueries
- **WHEN** parsing SQL with multiple levels of subqueries
- **THEN** the graph SHALL flatten and represent all underlying tables with their relationships

#### Scenario: Parse UNION queries
- **WHEN** parsing SQL "SELECT * FROM table1 UNION SELECT * FROM table2"
- **THEN** the graph SHALL contain both "table1" and "table2" as separate nodes without direct relationship

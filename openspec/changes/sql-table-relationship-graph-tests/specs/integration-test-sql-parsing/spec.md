## ADDED Requirements

### Requirement: Test simple SQL parsing
The integration test SHALL verify correct parsing and graph generation for simple SQL statements.

#### Scenario: Test single table SELECT
- **WHEN** running test with SQL "SELECT id, name FROM users"
- **THEN** the test SHALL pass and verify one table node with two columns

#### Scenario: Test simple WHERE clause
- **WHEN** running test with SQL containing WHERE conditions
- **THEN** the test SHALL pass and correctly extract table and column information

### Requirement: Test complex SQL parsing
The integration test SHALL verify correct parsing and graph generation for complex SQL statements.

#### Scenario: Test multi-table JOIN
- **WHEN** running test with SQL containing multiple JOINs across 3+ tables
- **THEN** the test SHALL pass and verify all table nodes and relationship edges

#### Scenario: Test subquery parsing
- **WHEN** running test with SQL containing nested subqueries
- **THEN** the test SHALL pass and verify all underlying tables are represented

#### Scenario: Test UNION query
- **WHEN** running test with SQL containing UNION
- **THEN** the test SHALL pass and verify all tables from both sides are included

### Requirement: Verify graph structure correctness
The integration test SHALL verify the generated graph structure matches expected topology.

#### Scenario: Verify node count
- **WHEN** comparing generated graph against expected
- **THEN** the number of table nodes SHALL match the number of unique tables in SQL

#### Scenario: Verify edge count
- **WHEN** comparing generated graph against expected
- **THEN** the number of edges SHALL match the number of JOIN relationships

#### Scenario: Verify join conditions
- **WHEN** inspecting relationship edges
- **THEN** the join conditions SHALL accurately reflect the ON clause columns

### Requirement: Test output formatters
The integration test SHALL verify output formatters produce valid and complete output.

#### Scenario: Test DOT format output
- **WHEN** generating DOT format from test SQL
- **THEN** the output SHALL be valid Graphviz DOT syntax that can be rendered

#### Scenario: Test JSON format output
- **WHEN** generating JSON format from test SQL
- **THEN** the output SHALL be valid JSON with complete graph information

### Requirement: Provide test coverage metrics
The integration test SHALL demonstrate coverage of various SQL patterns.

#### Scenario: Document covered SQL patterns
- **WHEN** reviewing test documentation
- **THEN** it SHALL list all tested SQL patterns: single table, INNER JOIN, LEFT JOIN, RIGHT JOIN, FULL JOIN, subqueries, UNION

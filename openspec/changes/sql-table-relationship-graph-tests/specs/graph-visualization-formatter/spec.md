## ADDED Requirements

### Requirement: Export graph to DOT format
The system SHALL convert the table relationship graph to Graphviz DOT format for visualization.

#### Scenario: Generate DOT for single table
- **WHEN** converting a graph with single table node "users"
- **THEN** the output SHALL be valid DOT syntax representing the table as a node

#### Scenario: Generate DOT with relationships
- **WHEN** converting a graph with tables "users" and "orders" connected by JOIN
- **THEN** the output SHALL contain an edge connecting the two nodes with JOIN type label

#### Scenario: Include column information in DOT
- **WHEN** converting a graph with column details
- **THEN** the DOT output SHALL include column information as node attributes or labels

### Requirement: Export graph to JSON format
The system SHALL convert the table relationship graph to JSON format for programmatic consumption.

#### Scenario: Generate JSON structure
- **WHEN** converting a graph to JSON
- **THEN** the output SHALL contain "nodes" array with table information and "edges" array with relationships

#### Scenario: JSON node structure
- **WHEN** inspecting a node in JSON output
- **THEN** each node SHALL have "name", "alias" (if present), and "columns" array

#### Scenario: JSON edge structure
- **WHEN** inspecting an edge in JSON output
- **THEN** each edge SHALL have "source", "target", "type", and "joinConditions" array

### Requirement: Support multiple output formats
The system SHALL support extensible output formatters for different visualization tools.

#### Scenario: Add custom formatter
- **WHEN** implementing a new formatter interface
- **THEN** the system SHALL be able to use it to convert graphs to custom formats

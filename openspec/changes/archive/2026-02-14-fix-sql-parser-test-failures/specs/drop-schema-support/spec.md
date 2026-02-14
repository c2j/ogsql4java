## ADDED Requirements

### Requirement: DROP SCHEMA statement parsing
The parser SHALL successfully parse DROP SCHEMA statements in OpenGauss SQL format.

#### Scenario: Basic DROP SCHEMA
- **WHEN** parser receives "DROP SCHEMA schema_name;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: DROP SCHEMA IF EXISTS
- **WHEN** parser receives "DROP SCHEMA IF EXISTS schema_name;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: DROP SCHEMA with CASCADE
- **WHEN** parser receives "DROP SCHEMA schema_name CASCADE;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: DROP SCHEMA with RESTRICT
- **WHEN** parser receives "DROP SCHEMA schema_name RESTRICT;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: DROP SCHEMA multiple schemas
- **WHEN** parser receives "DROP SCHEMA schema1, schema2, schema3;"
- **THEN** parser SHALL return valid parse tree without syntax errors

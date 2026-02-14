## ADDED Requirements

### Requirement: Procedure body null safety
The parser SHALL handle cases where procedure body is null/missing without throwing NullPointerException.

#### Scenario: CREATE PROCEDURE without body
- **WHEN** parser receives CREATE PROCEDURE statement with null/missing procedureBody
- **THEN** parser SHALL NOT throw NullPointerException

#### Scenario: CREATE FUNCTION without body
- **WHEN** parser receives CREATE FUNCTION statement with null/missing functionBody
- **THEN** parser SHALL NOT throw NullPointerException

#### Scenario: CREATE PROCEDURE with body
- **WHEN** parser receives CREATE PROCEDURE statement with valid procedure body
- **THEN** parser SHALL return valid parse tree and extract body correctly

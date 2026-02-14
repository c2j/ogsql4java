## ADDED Requirements

### Requirement: SHOW command parsing
The parser SHALL successfully parse SHOW statements in OpenGauss SQL format.

#### Scenario: SHOW basic statement
- **WHEN** parser receives "SHOW parameter_name;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: SHOW ALL
- **WHEN** parser receives "SHOW ALL;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: SHOW with configuration parameter
- **WHEN** parser receives "SHOW max_connections;"
- **THEN** parser SHALL return valid parse tree without syntax errors

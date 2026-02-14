## ADDED Requirements

### Requirement: IF statement parsing in procedural code
The parser SHALL successfully parse IF statements within stored procedures and functions.

#### Scenario: Simple IF statement
- **WHEN** parser receives "IF condition THEN statements END IF;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: IF-ELSE statement
- **WHEN** parser receives "IF condition THEN statements ELSE statements END IF;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: IF-ELSIF-ELSE statement
- **WHEN** parser receives "IF condition1 THEN statements ELSIF condition2 THEN statements ELSE statements END IF;"
- **THEN** parser SHALL return valid parse tree without syntax errors

#### Scenario: Nested IF statements
- **WHEN** parser receives nested IF statements within procedure body
- **THEN** parser SHALL return valid parse tree without syntax errors

## ADDED Requirements

### Requirement: Stored Procedure body support
The parser SHALL support procedure body with PL/pgSQL blocks:
- BEGIN ... END blocks
- DECLARE section
- Variable declarations
- Assignment statements

#### Scenario: Simple procedure body
- **WHEN** parsing "CREATE PROCEDURE p() BEGIN x := 1; END"
- **THEN** parser SHALL recognize procedure body

#### Scenario: Procedure with DECLARE
- **WHEN** "CREATE PROCEDURE p() DECLARE y INT; BEGIN x := y; END"
- **THEN** parser SHALL handle DECLARE section

### Requirement: Cursor support
The parser SHALL support cursor operations:
- DECLARE cursor_name CURSOR FOR query
- OPEN cursor_name
- FETCH FROM cursor_name
- CLOSE cursor_name

#### Scenario: DECLARE CURSOR
- **WHEN** parsing "DECLARE c CURSOR FOR SELECT * FROM t"
- **THEN** parser SHALL handle cursor declaration

### Requirement: Exception handling support
The parser SHALL support EXCEPTION blocks:
- EXCEPTION WHEN ... THEN
- Multiple WHEN conditions
- RAISE statements

#### Scenario: Exception handling
- **WHEN** "BEGIN ... EXCEPTION WHEN division_by_zero THEN ... END"
- **THEN** parser SHALL handle EXCEPTION block

### Requirement: Control structures support
The parser SHALL support PL/pgSQL control structures:
- IF/THEN/ELSIF/ELSE/END IF
- CASE statement
- LOOP, WHILE, FOR loops
- EXIT/CONTINUE

#### Scenario: IF statement
- **WHEN** "IF x > 0 THEN y := 1; ELSE y := 0; END IF"
- **THEN** parser SHALL handle IF/ELSE

#### Scenario: FOR loop
- **WHEN** "FOR i IN 1..10 LOOP ... END LOOP"
- **THEN** parser SHALL handle FOR loop

### Requirement: RETURN in procedures
The parser SHALL support RETURN in procedures:
- RETURN
- RETURN NEXT
- RETURN QUERY

#### Scenario: RETURN NEXT
- **WHEN** "RETURN NEXT rec"
- **THEN** parser SHALL handle RETURN NEXT

### Requirement: INOUT/OUT parameters
The parser SHALL support parameter modes:
- IN parameter
- OUT parameter
- INOUT parameter

#### Scenario: INOUT parameter
- **WHEN** "CREATE PROCEDURE p(INOUT x INT)"
- **THEN** parser SHALL handle INOUT mode

### Requirement: CREATE FUNCTION with body
The parser SHALL support function body in various languages

#### Scenario: Function with PL/pgSQL body
- **WHEN** "CREATE FUNCTION f() RETURNS INT AS $$ BEGIN RETURN 1; END $$ LANGUAGE PLPGSQL"
- **THEN** parser SHALL handle function body

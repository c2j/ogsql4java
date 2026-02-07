## ADDED Requirements

### Requirement: SQL file discovery
The system SHALL automatically discover all SQL files in the tests/regress/sql directory and its subdirectories.

#### Scenario: Discover SQL files
- **WHEN** the test suite is initialized
- **THEN** the system scans tests/regress/sql directory recursively
- **THEN** all files with .sql extension are collected
- **THEN** each file path is made available for parsing

#### Scenario: Handle empty directory
- **WHEN** the tests/regress/sql directory contains no SQL files
- **THEN** the system reports that no test files were found
- **THEN** the test suite completes successfully with zero tests

### Requirement: SQL file parsing
The system SHALL parse each discovered SQL file using the ogsql parser API (SQLParser) and validate parsing success.

#### Scenario: Parse valid SQL file
- **WHEN** a SQL file containing valid SQL statements is parsed
- **THEN** the parser successfully parses the content
- **THEN** the test case passes

#### Scenario: Parse invalid SQL file
- **WHEN** a SQL file containing invalid SQL syntax is parsed
- **THEN** the parser throws a ParseException
- **THEN** the test case fails
- **THEN** the error message includes the file name and line number
- **THEN** the error message includes the parser's error details

#### Scenario: Parse empty SQL file
- **WHEN** a SQL file containing only whitespace or comments is parsed
- **THEN** the parser successfully parses the content
- **THEN** the test case passes

### Requirement: Test case generation
The system SHALL generate individual JUnit 5 test cases for each discovered SQL file.

#### Scenario: Generate test for each SQL file
- **WHEN** SQL files are discovered
- **THEN** a test case is generated for each file
- **THEN** the test case name is based on the SQL file name (sanitized if needed)
- **THEN** each test case can be executed independently

#### Scenario: Sanitize invalid test names
- **WHEN** a SQL file name contains characters invalid for Java method names
- **THEN** the system sanitizes the name to a valid identifier
- **THEN** the test case can still be executed without errors

### Requirement: Test execution integration
The system SHALL integrate with the existing JUnit 5 test framework and execute as part of the standard test suite.

#### Scenario: Run with mvn test
- **WHEN** mvn test command is executed
- **THEN** the regression tests are automatically included
- **THEN** all SQL files are parsed and validated
- **THEN** test results are included in the overall test report

#### Scenario: Run individual regression tests
- **WHEN** a specific regression test is executed
- **THEN** only that SQL file is parsed
- **THEN** the result is reported for that specific test

### Requirement: Test result reporting
The system SHALL provide clear error reporting when parsing fails.

#### Scenario: Report parsing failure
- **WHEN** a SQL file fails to parse
- **THEN** the test failure message includes the SQL file path
- **THEN** the test failure message includes the exception type
- **THEN** the test failure message includes the error location (line, column if available)
- **THEN** the test failure message includes the parser's detailed error message

#### Scenario: Report success summary
- **WHEN** all regression tests pass
- **THEN** the test output shows the total number of SQL files parsed
- **THEN** the test output confirms all tests passed

### Requirement: Test filtering support
The system SHALL support selective test execution through JUnit 5 tags.

#### Scenario: Filter by regression tag
- **WHEN** tests are executed with tag "regression"
- **THEN** only regression tests are executed
- **THEN** SQL parsing tests are included

#### Scenario: Filter by file pattern (future enhancement)
- **WHEN** tests are executed with a file pattern filter
- **THEN** only SQL files matching the pattern are tested
- **THEN** non-matching files are skipped

### Requirement: Error handling
The system SHALL handle parsing errors gracefully without crashing the entire test suite.

#### Scenario: Continue after failure
- **WHEN** one SQL file fails to parse
- **THEN** the test case for that file is marked as failed
- **THEN** subsequent SQL files continue to be parsed
- **THEN** all test results are reported at the end

#### Scenario: Handle file read errors
- **WHEN** a SQL file cannot be read (permission error, corrupted file)
- **THEN** the test case is marked as failed
- **THEN** the error message indicates the file read failure
- **THEN** the test suite continues with remaining files

### Requirement: Multi-statement handling
The system SHALL attempt to parse SQL files containing multiple statements, but may fail if the parser does not support this feature.

#### Scenario: Parse single-statement SQL file
- **WHEN** a SQL file contains a single SQL statement
- **THEN** the parser successfully parses the content
- **THEN** the test case passes

#### Scenario: Parse multi-statement SQL file
- **WHEN** a SQL file contains multiple SQL statements
- **THEN** the parser attempts to parse the content
- **THEN** if the parser supports multi-statement parsing, the test case passes
- **THEN** if the parser does not support multi-statement parsing, the test case may fail with appropriate error

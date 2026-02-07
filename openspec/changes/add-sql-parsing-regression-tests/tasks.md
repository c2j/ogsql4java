## 1. Project Setup

- [ ] 1.1 Create regression test package directory: src/test/java/com/sdchat/ogsql/regression/
- [ ] 1.2 Create resource directory for test configuration: src/test/resources/regression/
- [ ] 1.3 Verify existing parser API imports are available (SQLParser, ParseResult, ParseException)
- [ ] 1.4 Verify JUnit 5 dependencies are configured (junit-jupiter-api, junit-jupiter-params, junit-jupiter-engine)

## 2. SQL File Discovery Utility

- [ ] 2.1 Create SQLTestFileDiscovery.java class in regression package
- [ ] 2.2 Implement discoverSQLFiles() method using java.nio.file.Files.walk()
- [ ] 2.3 Add logic to filter files with .sql extension only
- [ ] 2.4 Implement scanDirectory() method to recursively scan tests/regress/sql
- [ ] 2.5 Add Path to SQL file metadata (file name, path, size)
- [ ] 2.6 Implement sanitizeTestName() method to convert file names to valid Java identifiers
- [ ] 2.7 Add handling for edge cases (empty directory, no files found)
- [ ] 2.8 Write unit tests for SQLTestFileDiscovery class

## 3. Main Test Class Structure

- [ ] 3.1 Create SQLParsingRegressionTest.java class in regression package
- [ ] 3.2 Add @Tag("regression") annotation to the test class
- [ ] 3.3 Add @ParameterizedTest annotation to the main test method
- [ ] 3.4 Add @MethodSource annotation to reference SQL file discovery method
- [ ] 3.5 Add @DisplayName annotation to provide clear test names
- [ ] 3.6 Create static method to provide test parameters (returns Arguments)

## 4. SQL Parsing Implementation

- [ ] 4.1 Implement test method that accepts Path parameter (SQL file path)
- [ ] 4.2 Add logic to read SQL file content using Files.readString()
- [ ] 4.3 Implement SQLParser instantiation and parse() method call
- [ ] 4.4 Add try-catch block for ParseException handling
- [ ] 4.5 Implement success assertion when parsing succeeds
- [ ] 4.6 Implement failure assertion with detailed error message when parsing fails
- [ ] 4.7 Add file path, exception type, and error details to failure message
- [ ] 4.8 Handle IOException for file read errors gracefully

## 5. Error Handling and Reporting

- [ ] 5.1 Create helper method to format parsing error messages
- [ ] 5.2 Include SQL file path in all error messages
- [ ] 5.3 Extract and include line/column number from ParseException if available
- [ ] 5.4 Include full parser error message in failure output
- [ ] 5.5 Add test failure callback to log failed parses for later review
- [ ] 5.6 Implement assertion that fails are isolated (don't crash entire suite)
- [ ] 5.7 Add handling for empty SQL files (whitespace/comments only)

## 6. Test Filtering and Configuration

- [ ] 6.1 Add @Tag("regression") to enable selective test execution
- [ ] 6.2 Add @Tag("slow") annotation for optional slow test categorization
- [ ] 6.3 Create configuration file to exclude directories if needed (e.g., wastebin)
- [ ] 6.4 Implement optional file pattern filtering logic
- [ ] 6.5 Add test to verify tags work correctly with Maven Surefire plugin

## 7. Integration Testing

- [ ] 7.1 Run regression tests with mvn test command
- [ ] 7.2 Verify all 1516 SQL files are discovered and parsed
- [ ] 7.3 Review initial test results and identify any immediate parser issues
- [ ] 7.4 Verify tests are included in standard Maven test report
- [ ] 7.5 Test individual test case execution
- [ ] 7.6 Verify test filtering by tag works correctly
- [ ] 7.7 Check test execution time and consider parallel execution if needed

## 8. Documentation and Cleanup

- [ ] 8.1 Add Javadoc to SQLTestFileDiscovery class
- [ ] 8.2 Add Javadoc to SQLParsingRegressionTest class
- [ ] 8.3 Add README.md in regression package explaining how to run tests
- [ ] 8.4 Document any known limitations (e.g., multi-statement handling)
- [ ] 8.5 Add configuration documentation for excluding directories
- [ ] 8.6 Verify code follows existing project style and conventions

## 9. Optional Enhancements (Future Work)

- [ ] 9.1 Add parallel test execution configuration if performance is slow
- [ ] 9.2 Implement statement splitting for multi-statement SQL files
- [ ] 9.3 Add performance benchmarking for parser
- [ ] 9.4 Create separate test suites by SQL category (DDL, DML, etc.)
- [ ] 9.5 Add expected failure list configuration for known issues
- [ ] 9.6 Generate HTML test report for parsing failures

## Context

The ogsql parser project currently has comprehensive parser capabilities but lacks automated regression testing against real-world SQL. The tests/regress/sql directory contains 1516 SQL files from the OpenGauss test suite that represent diverse SQL patterns including edge cases, complex queries, and vendor-specific syntax. These SQL files are currently not being validated by the ogsql parser, creating a risk that parser changes could introduce regressions.

The project uses:
- Java 17 with ANTLR4 4.13.1
- Spring Boot 3.5.9
- JUnit 5 for testing
- Existing parser API (SQLParser, ParseResult)
- Existing test structure in src/test/java/com/sdchat/ogsql/

Constraints:
- Must integrate with existing JUnit 5 test framework
- Must use existing parser API without modifications
- Test execution should be reasonable (not excessively slow)
- Should support selective test execution (e.g., by file pattern)
- Must handle parsing failures gracefully with clear error reporting

## Goals / Non-Goals

**Goals:**
- Create automated regression test suite that parses all 1516 SQL files from tests/regress/sql
- Integrate tests seamlessly with existing JUnit 5 framework
- Generate individual test cases for each SQL file for easy failure tracking
- Provide clear error reporting when parsing fails (file name, line number, error message)
- Support test filtering by file pattern or category
- Ensure tests can run as part of standard mvn test execution

**Non-Goals:**
- Not fixing parser bugs found during testing (that's separate work)
- Not executing the SQL against a database (only parsing)
- Not modifying the parser API or grammar
- Not creating test fixtures or test data (using existing SQL files)
- Not adding performance benchmarks

## Decisions

**Test Framework: JUnit 5 Parameterized Tests**
Use JUnit 5's @ParameterizedTest with @MethodSource to generate test cases for each SQL file. This provides:
- Individual test execution for each SQL file
- Clear test names (SQL file name)
- Standard JUnit integration
- Easy filtering with JUnit tags

**Alternative considered:** Custom test runner
- Rejected: More complex, not standard JUnit pattern, harder to maintain

**Test Structure: Two-Class Design**
1. `SQLParsingRegressionTest.java` - Main test class with parameterized tests
2. `SQLTestFileDiscovery.java` - Utility for discovering and loading SQL files

**Rationale:** Separates test logic from file discovery, making code more maintainable and testable.

**File Discovery: java.nio.file.Files.walk()**
Use Java NIO's Files.walk() to recursively find all .sql files in tests/regress/sql directory.

**Alternative considered:** Apache Commons IO FileUtils
- Rejected: Unnecessary dependency when JDK provides equivalent functionality

**Error Handling: Parse Error Capturing**
Wrap each parse attempt in try-catch to capture:
- ParseException with error details
- Line number and position from ParseResult
- Full SQL content for debugging

**Test Naming Pattern**
Use SQL file names as test identifiers (e.g., "test_parse_select_basic" for select_basic.sql). For files with invalid characters, sanitize to valid Java method names.

**Test Filtering Support**
Add JUnit tags (@Tag) to enable selective execution:
- @Tag("regression") - All regression tests
- @Tag("slow") - Tests for large SQL files (optional, if needed)

## Risks / Trade-offs

**Risk: Some SQL files are intentionally invalid or use unsupported syntax**
→ Mitigation: Create mechanism to mark expected failures with a configurable list; initially report all failures for review

**Risk: Test execution time with 1516 SQL files**
→ Mitigation: Use JUnit parallel execution (JUnit 5 supports this); consider creating a separate test profile if needed

**Risk: Large SQL files could cause memory issues**
→ Mitigation: Stream file content rather than loading entirely into memory; monitor test execution for performance issues

**Risk: Some SQL files may contain multiple statements (not supported by current parser)**
→ Mitigation: Report as known limitation; consider statement splitting logic in future enhancement

**Trade-off: Comprehensive vs. Practical Testing**
- We're testing all files which may include some that are intentionally broken
- Decision: Test all and use failure reports to identify legitimate parser issues vs. test data issues

## Migration Plan

No migration needed as this is new test infrastructure being added. Tests will be part of standard build process from the start.

**Deployment:**
1. Add test classes to src/test/java/com/sdchat/ogsql/regression/
2. Run `mvn test` to execute all tests including new regression tests
3. Review initial test results to identify any immediate parser issues

**Rollback:**
Simply remove the regression test classes if needed (they don't affect production code).

## Open Questions

- Should we exclude certain subdirectories (e.g., wastebin) from regression tests?
- Should we create separate test suites for different SQL categories (DDL, DML, etc.)?
- How should we handle files with multiple SQL statements (split them or report as error)?
- Should tests be optional in standard build or always run?

## Why

The ogsql parser needs comprehensive regression testing to ensure it can correctly parse real-world SQL from the OpenGauss test suite. Currently, we have 1516 SQL files in the tests/regress/sql directory that are not being validated by the parser. Adding regression tests will catch parsing errors early, ensure parser coverage for edge cases, and provide confidence that parser changes don't break existing functionality.

## What Changes

- Add new test infrastructure to parse and validate SQL files from tests/regress/sql directory
- Create test runner that discovers SQL files, parses them using the ogsql API, and reports results
- Generate test cases for each SQL file (1516 tests)
- Integrate with existing JUnit 5 test framework
- Add test result reporting for successful/failed parses
- Create test utilities to handle SQL file discovery and parsing

## Capabilities

### New Capabilities
- `sql-parsing-regression-testing`: Automated regression testing that parses all SQL files from tests/regress/sql directory using the ogsql parser API and validates parsing success

### Modified Capabilities
(empty)

## Impact

- Affected code: New test classes in src/test/java/com/sdchat/ogsql/regression/
- APIs: Uses existing parser API (SQLParser, ParseResult)
- Dependencies: JUnit 5 (already in project)
- Build: New test classes will run as part of mvn test
- CI: Tests will provide early detection of parser regressions

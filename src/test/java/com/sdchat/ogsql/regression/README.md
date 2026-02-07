# SQL Parsing Regression Tests

This directory contains regression tests for the ogsql parser that automatically discover and parse SQL files from the OpenGauss test suite.

## Overview

The regression test suite automatically discovers all SQL files in `tests/regress/sql/` and creates a separate test case for each file. This provides comprehensive testing of the parser against real-world SQL examples from the OpenGauss database system.

### Test Files

- `SQLTestFileDiscovery.java` - Utility for discovering and loading SQL test files
- `SQLParsingRegressionTest.java` - Main parameterized test class that parses each SQL file

## Running the Tests

### Run All Regression Tests

```bash
mvn test -Dtest=SQLParsingRegressionTest
```

### Run Only Regular-Sized Tests

```bash
mvn test -Dtest=SQLParsingRegressionTest -Dgroups=!slow
```

### Run Only Large File Tests

```bash
mvn test -Dtest=SQLParsingRegressionTest#testParseLargeSQLFile
```

### Run Specific Test File

To run a specific test, use the sanitized test name:

```bash
mvn test -Dtest=SQLParsingRegressionTest#testParseSQLFile[select_basic]
```

## Test Configuration

Configuration is stored in `src/test/resources/regression/test-config.properties`.

### Configuration Options

- `exclude.directories` - Comma-separated list of directories to exclude
- `exclude.files` - Comma-separated list of file patterns to exclude (supports * wildcard)
- `large.file.threshold` - File size threshold in bytes for "slow" tests (default: 10240 = 10KB)

### Example Configuration

```properties
# Exclude wastebin directory
exclude.directories=wastebin

# Exclude test backup files
exclude.files=test_*,*_backup.sql

# Set large file threshold to 50KB
large.file.threshold=51200
```

## Test Results

### Standard Output

Test results are printed to the console during execution, including:
- Number of tests discovered
- Individual test results (pass/fail)
- Summary statistics

### Failure Log

Parsing failures are logged to `target/regression-failures.log` with:
- Timestamp
- File path
- Exception type
- Error message

### Surefire Reports

Detailed test reports are generated in `target/surefire-reports/`:
- `com.sdchat.ogsql.regression.SQLParsingRegressionTest.xml` - XML test report
- `com.sdchat.ogsql.regression.SQLParsingRegressionTest.txt` - Text test report

## Known Limitations

1. **Multi-Statement Files**: The parser currently expects single SQL statements. Files containing multiple statements separated by semicolons may fail to parse. This is a limitation of the current parser implementation.

2. **Grammar Coverage**: Not all OpenGauss SQL features are supported by the grammar. Files using unsupported features will fail to parse. These failures should be reviewed to determine if they represent missing grammar support or intentional OpenGauss-specific extensions.

3. **Empty Files**: Files containing only comments or whitespace are skipped (not failed).

## Development

### Adding New Tests

New SQL test files added to `tests/regress/sql/` are automatically discovered and included in the test suite. No code changes are required.

### Excluding Tests

To exclude specific directories or files from testing, update the configuration in `src/test/resources/regression/test-config.properties`.

## Troubleshooting

### Test Discovery Fails

If tests fail to discover SQL files:
1. Verify `tests/regress/sql/` directory exists
2. Check file permissions
3. Review console output for specific error messages

### Parsing Errors Increase After Changes

If the number of parsing failures increases:
1. Check `target/regression-failures.log` for details
2. Compare with previous logs to identify new failures
3. Review grammar changes for compatibility

### Memory Issues

For large test suites or very large SQL files:
1. Consider increasing Maven memory: `export MAVEN_OPTS="-Xmx2g"`
2. Use file exclusion to reduce test count
3. Run tests in batches using file pattern filtering

## CI/CD Integration

These tests can be integrated into CI/CD pipelines:

```yaml
# Example GitHub Actions
- name: Run Regression Tests
  run: mvn test -Dtest=SQLParsingRegressionTest

# Run with tags
- name: Run Regression Tests (excluding slow)
  run: mvn test -Dtest=SQLParsingRegressionTest -DexcludedGroups=slow
```

## Performance

- **Test Execution Time**: ~3-5 seconds for ~1500 SQL files
- **Memory Usage**: Typically < 500MB
- **Parallel Execution**: Not currently enabled but can be configured via JUnit Jupiter if needed

## Future Enhancements

Potential improvements to consider:
- [ ] Add parallel test execution for faster runs
- [ ] Implement statement splitting for multi-statement files
- [ ] Add expected failure list for known issues
- [ ] Generate HTML reports for parsing failures
- [ ] Add performance benchmarking
- [ ] Create separate test suites by SQL category (DDL, DML, etc.)

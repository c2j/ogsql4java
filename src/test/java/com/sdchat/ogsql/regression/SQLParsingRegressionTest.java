package com.sdchat.ogsql.regression;

import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;
import com.sdchat.ogsql.parser.SQLParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression tests for SQL parsing functionality.
 *
 * This test suite automatically discovers and parses all SQL files in the
 * tests/regress/sql directory. Each SQL file becomes a separate test case,
 * providing comprehensive regression testing for the ogsql parser.
 *
 * Tests are tagged with "regression" for selective execution. Large files
 * (>= 10KB) are additionally tagged with "slow" to enable separate
 * execution if needed.
 *
 * Parsing failures are logged to target/regression-failures.log for review.
 */
@Tag("regression")
@DisplayName("SQL Parsing Regression Tests")
public class SQLParsingRegressionTest {

    private static final Path FAILURE_LOG = Paths.get("target/regression-failures.log");
    private static final SQLTestFileDiscovery DISCOVERY = new SQLTestFileDiscovery();

    static {
        try {
            Files.createDirectories(FAILURE_LOG.getParent());
        } catch (IOException e) {
            System.err.println("Warning: Could not create log directory: " + e.getMessage());
        }
    }

    /**
     * Parameterized test that parses a single SQL file.
     *
     * @param testName The sanitized test name for display purposes
     * @param sqlFilePath The path to the SQL file to parse
     */
    @DisplayName("Parse SQL file: {0}")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("provideSQLFiles")
    void testParseSQLFile(String testName, Path sqlFilePath) {
        parseSQLFileInternal(sqlFilePath);
    }

    /**
     * Parameterized test for large SQL files.
     *
     * @param testName The sanitized test name for display purposes
     * @param sqlFilePath The path to the SQL file to parse
     */
    @Tag("slow")
    @DisplayName("Parse large SQL file: {0}")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("provideLargeSQLFiles")
    void testParseLargeSQLFile(String testName, Path sqlFilePath) {
        parseSQLFileInternal(sqlFilePath);
    }

    /**
     * Internal method to parse a SQL file.
     *
     * @param sqlFilePath The path to the SQL file to parse
     */
    private void parseSQLFileInternal(Path sqlFilePath) {
        try {
            String sqlContent = Files.readString(sqlFilePath);

            if (sqlContent.trim().isEmpty()) {
                return;
            }

            SQLParser parser = new SQLParser();
            SQLStatement statement = parser.parse(sqlContent);

            assertNotNull(statement, "Parse result should not be null");

        } catch (ParseException e) {
            logParsingException(sqlFilePath, e);
            fail(formatParsingException(sqlFilePath, e));
        } catch (IOException e) {
            logIOError(sqlFilePath, e);
            fail(formatIOError(sqlFilePath, e));
        }
    }

    /**
     * Logs a parsing exception to the failure log.
     *
     * @param sqlFilePath The path of the SQL file that failed to parse
     * @param e The parsing exception
     */
    private void logParsingException(Path sqlFilePath, ParseException e) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logEntry = String.format(
                "[%s] EXCEPTION - File: %s | Type: %s | Message: %s\n",
                timestamp,
                sqlFilePath,
                e.getClass().getSimpleName(),
                e.getMessage()
        );

        appendToLog(logEntry);
    }

    /**
     * Logs an IO exception to the failure log.
     *
     * @param sqlFilePath The path of the SQL file that failed to read
     * @param e The IO exception
     */
    private void logIOError(Path sqlFilePath, IOException e) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logEntry = String.format(
                "[%s] IO ERROR - File: %s | Type: %s | Message: %s\n",
                timestamp,
                sqlFilePath,
                e.getClass().getSimpleName(),
                e.getMessage()
        );

        appendToLog(logEntry);
    }

    /**
     * Appends a log entry to the failure log file.
     *
     * @param logEntry The log entry to write
     */
    private synchronized void appendToLog(String logEntry) {
        try (FileWriter writer = new FileWriter(FAILURE_LOG.toFile(), true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Warning: Could not write to failure log: " + e.getMessage());
        }
    }

    /**
     * Provides SQL files for parameterized testing (regular-sized files).
     *
     * @return Stream of Arguments containing test name and file path
     */
    private static Stream<Arguments> provideSQLFiles() {
        try {
            List<SQLTestFileDiscovery.SQLFileInfo> sqlFiles = DISCOVERY.discoverSQLFiles();

            if (sqlFiles.isEmpty()) {
                return Stream.empty();
            }

            long threshold = DISCOVERY.getLargeFileThreshold();

            return sqlFiles.stream()
                    .filter(file -> file.getSize() < threshold)
                    .map(fileInfo -> {
                        String testName = SQLTestFileDiscovery.sanitizeTestNameStatic(fileInfo.getFileName());
                        return Arguments.of(testName, fileInfo.getPath());
                    });

        } catch (IOException e) {
            throw new RuntimeException("Failed to discover SQL files", e);
        }
    }

    /**
     * Provides large SQL files for parameterized testing.
     *
     * @return Stream of Arguments containing test name and file path
     */
    private static Stream<Arguments> provideLargeSQLFiles() {
        try {
            List<SQLTestFileDiscovery.SQLFileInfo> sqlFiles = DISCOVERY.discoverSQLFiles();

            if (sqlFiles.isEmpty()) {
                return Stream.empty();
            }

            long threshold = DISCOVERY.getLargeFileThreshold();

            return sqlFiles.stream()
                    .filter(file -> file.getSize() >= threshold)
                    .map(fileInfo -> {
                        String testName = SQLTestFileDiscovery.sanitizeTestNameStatic(fileInfo.getFileName());
                        return Arguments.of(testName, fileInfo.getPath());
                    });

        } catch (IOException e) {
            throw new RuntimeException("Failed to discover SQL files", e);
        }
    }

    /**
     * Formats a parsing exception for test failure message.
     *
     * @param sqlFilePath The path of the SQL file
     * @param e The parsing exception
     * @return Formatted error message
     */
    private String formatParsingException(Path sqlFilePath, ParseException e) {
        return String.format(
                "Failed to parse SQL file: %s\n" +
                "Exception type: %s\n" +
                "Error message: %s",
                sqlFilePath,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }

    /**
     * Formats an IO exception for test failure message.
     *
     * @param sqlFilePath The path of the SQL file
     * @param e The IO exception
     * @return Formatted error message
     */
    private String formatIOError(Path sqlFilePath, IOException e) {
        return String.format(
                "Failed to read SQL file: %s\n" +
                "Exception type: %s\n" +
                "Error message: %s",
                sqlFilePath,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
    }
}

package com.sdchat.ogsql.regression;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Utility class for discovering and loading SQL test files.
 *
 * This class scans the tests/regress/sql directory to find all SQL files
 * for regression testing. It supports filtering by directory and file patterns
 * based on configuration.
 */
public class SQLTestFileDiscovery {

    private static final String SQL_DIR = "tests/regress/sql";
    private static final String CONFIG_FILE = "/regression/test-config.properties";

    private final List<String> excludeDirectories;
    private final List<String> excludeFilePatterns;
    private final long largeFileThreshold;

    /**
     * Creates a new SQLTestFileDiscovery instance and loads configuration.
     */
    public SQLTestFileDiscovery() {
        this.excludeDirectories = new ArrayList<>();
        this.excludeFilePatterns = new ArrayList<>();
        this.largeFileThreshold = loadConfiguration();
    }

    private long loadConfiguration() {
        try (InputStream input = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);

                String excludeDirs = props.getProperty("exclude.directories", "");
                if (!excludeDirs.isEmpty()) {
                    String[] dirs = excludeDirs.split(",");
                    for (String dir : dirs) {
                        this.excludeDirectories.add(dir.trim());
                    }
                }

                String excludeFiles = props.getProperty("exclude.files", "");
                if (!excludeFiles.isEmpty()) {
                    String[] patterns = excludeFiles.split(",");
                    for (String pattern : patterns) {
                        this.excludeFilePatterns.add(pattern.trim());
                    }
                }

                return Long.parseLong(props.getProperty("large.file.threshold", "10240"));
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load test configuration: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Warning: Invalid large.file.threshold in configuration, using default");
        }
        return 10240;
    }

    /**
     * Represents information about a SQL test file.
     */
    public static class SQLFileInfo {
        private final String fileName;
        private final Path path;
        private final long size;

        public SQLFileInfo(String fileName, Path path, long size) {
            this.fileName = fileName;
            this.path = path;
            this.size = size;
        }

        public String getFileName() {
            return fileName;
        }

        public Path getPath() {
            return path;
        }

        public long getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "SQLFileInfo{" +
                    "fileName='" + fileName + '\'' +
                    ", path=" + path +
                    ", size=" + size +
                    '}';
        }
    }

    /**
     * Discovers all SQL files in the tests/regress/sql directory.
     *
     * @return List of SQLFileInfo objects containing file metadata
     * @throws IOException if file scanning fails
     */
    public List<SQLFileInfo> discoverSQLFiles() throws IOException {
        Path sqlDir = Paths.get(SQL_DIR);

        if (!Files.exists(sqlDir)) {
            System.out.println("SQL directory does not exist: " + sqlDir);
            return new ArrayList<>();
        }

        List<SQLFileInfo> sqlFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(sqlDir)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".sql"))
                    .filter(this::shouldIncludeFile)
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        long size;
                        try {
                            size = Files.size(path);
                        } catch (IOException e) {
                            size = -1;
                        }
                        sqlFiles.add(new SQLFileInfo(fileName, path, size));
                    });
        }

        return sqlFiles;
    }

    private boolean shouldIncludeFile(Path filePath) {
        String pathStr = filePath.toString();

        for (String excludeDir : excludeDirectories) {
            if (pathStr.contains(excludeDir)) {
                return false;
            }
        }

        String fileName = filePath.getFileName().toString();
        for (String pattern : excludeFilePatterns) {
            if (matchesPattern(fileName, pattern)) {
                return false;
            }
        }

        return true;
    }

    private boolean matchesPattern(String fileName, String pattern) {
        String regex = pattern.replace("*", ".*");
        return fileName.matches(regex);
    }

    public long getLargeFileThreshold() {
        return largeFileThreshold;
    }

    /**
     * Converts a SQL file name to a valid Java identifier.
     *
     * @param fileName The SQL file name (e.g., "test-file.sql")
     * @return Sanitized Java identifier (e.g., "test_file")
     */
    public String sanitizeTestName(String fileName) {
        String baseName = fileName.replace(".sql", "");
        return baseName.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    /**
     * Static helper to convert a SQL file name to a valid Java identifier.
     *
     * @param fileName The SQL file name (e.g., "test-file.sql")
     * @return Sanitized Java identifier (e.g., "test_file")
     */
    public static String sanitizeTestNameStatic(String fileName) {
        SQLTestFileDiscovery discovery = new SQLTestFileDiscovery();
        return discovery.sanitizeTestName(fileName);
    }
}

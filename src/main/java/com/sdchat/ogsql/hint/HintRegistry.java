package com.sdchat.ogsql.hint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Hint Registry (Hint Knowledge Base)
 *
 * Manages to knowledge base of valid GaussDB SQL hints by loading from
 * gaussdb_sql_plan_hints.json configuration file.
 *
 * This registry provides validation and query capabilities for hint names.
 */
public class HintRegistry {

    private static final Logger log = LoggerFactory.getLogger(HintRegistry.class);

    private static final String DEFAULT_HINT_CONFIG_FILE = "/config/gaussdb_sql_plan_hints.json";

    private Set<String> validHints = new HashSet<>();
    private boolean loaded = false;
    private String configFilePath;

    /**
     * Creates a hint registry with default configuration file.
     */
    public HintRegistry() {
        this(DEFAULT_HINT_CONFIG_FILE);
    }

    /**
     * Creates a hint registry with custom configuration file path.
     *
     * @param configFilePath Path to hint configuration file (classpath or absolute)
     */
    public HintRegistry(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    /**
     * Loads the hint configuration file.
     *
     * @return true if loaded successfully, false otherwise
     */
    public synchronized boolean load() {
        if (loaded) {
            log.debug("Hint registry already loaded");
            return true;
        }

        try {
            Set<String> hints = loadHintsFromFile(configFilePath);
            if (hints != null && !hints.isEmpty()) {
                this.validHints = Collections.unmodifiableSet(hints);
                this.loaded = true;
                log.info("Loaded {} valid hints from {}", hints.size(), configFilePath);
                return true;
            } else {
                log.warn("No hints found in configuration file: {}", configFilePath);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to load hint configuration from: {}", configFilePath, e);
            return false;
        }
    }

    /**
     * Reloads the hint configuration file.
     *
     * @return true if reloaded successfully, false otherwise
     */
    public synchronized boolean reload() {
        this.loaded = false;
        this.validHints = new HashSet<>();
        return load();
    }

    /**
     * Checks if a hint type is valid (exists in the knowledge base).
     * Validation is case-insensitive to match SQL hint usage patterns.
     *
     * @param hintType The hint type to validate
     * @return true if the hint type is valid, false otherwise
     */
    public boolean isValidHint(String hintType) {
        if (!loaded) {
            log.warn("Hint registry not loaded, cannot validate hint: {}", hintType);
            return false;
        }

        if (hintType == null || hintType.isEmpty()) {
            return false;
        }

        for (String validHint : validHints) {
            if (validHint.equalsIgnoreCase(hintType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all valid hint types.
     *
     * @return Unmodifiable set of valid hint types
     */
    public Set<String> getValidHints() {
        if (!loaded) {
            log.warn("Hint registry not loaded");
            return Collections.emptySet();
        }

        return validHints;
    }

    /**
     * Checks if the registry has been loaded.
     *
     * @return true if loaded, false otherwise
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Gets the configuration file path.
     *
     * @return Configuration file path
     */
    public String getConfigFilePath() {
        return configFilePath;
    }

    /**
     * Loads hints from configuration file.
     * Tries loading from classpath first, then falls back to absolute file path.
     *
     * @param filePath Path to configuration file
     * @return Set of valid hint types, or null if failed to load
     */
    private Set<String> loadHintsFromFile(String filePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                log.debug("Loaded hint config from classpath: {}", filePath);
                return parseHintConfig(inputStream);
            }

            log.warn("Hint config not found in classpath: {}, trying file path", filePath);
            java.io.File file = new java.io.File(filePath);
            if (file.exists()) {
                try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                    log.debug("Loaded hint config from file path: {}", filePath);
                    return parseHintConfig(fis);
                }
            } else {
                log.warn("Hint configuration file not found: {}", filePath);
                return null;
            }
        } catch (Exception e) {
            log.error("Error loading hint configuration from: {}", filePath, e);
            return null;
        }
    }

    /**
     * Parses the hint configuration JSON file.
     *
     * @param inputStream Input stream to the configuration file
     * @return Set of valid hint types, or null if failed to parse
     */
    private Set<String> parseHintConfig(InputStream inputStream) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            String jsonContent = content.toString();
            if (jsonContent == null || jsonContent.trim().isEmpty()) {
                log.warn("Hint configuration file is empty");
                return null;
            }

            Set<String> hints = new HashSet<>();

            int start = jsonContent.indexOf("\"hints\":");
            if (start == -1) {
                log.warn("Invalid hint configuration format - missing 'hints' key");
                return null;
            }

            start = jsonContent.indexOf("[", start);
            if (start == -1) {
                log.warn("Invalid hint configuration format - missing hints array");
                return null;
            }

            int end = findMatchingBracket(jsonContent, start);
            String hintsArray = jsonContent.substring(start, end + 1);

            int pos = 0;
            while ((pos = hintsArray.indexOf("\"name\":", pos)) != -1) {
                // Find the colon
                pos = hintsArray.indexOf(":", pos) + 1;
                // Skip whitespace
                while (pos < hintsArray.length() && Character.isWhitespace(hintsArray.charAt(pos))) {
                    pos++;
                }
                // Skip opening quote
                if (pos < hintsArray.length() && hintsArray.charAt(pos) == '"') {
                    pos++;
                }
                int endPos = hintsArray.indexOf("\"", pos);
                if (endPos == -1) {
                    break;
                }
                String hintName = hintsArray.substring(pos, endPos);
                if (!hintName.isEmpty()) {
                    hints.add(hintName);
                }
                pos = endPos + 1;
            }

            log.debug("Parsed {} hints from configuration", hints.size());
            return hints;
        } catch (Exception e) {
            log.error("Error parsing hint configuration", e);
            return null;
        }
    }

    /**
     * Finds the matching closing bracket for the opening bracket at the given position.
     *
     * @param content The JSON content
     * @param start The start position to search from
     * @return The position of the matching closing bracket
     */
    private int findMatchingBracket(String content, int start) {
        int bracketCount = 0;
        for (int i = start; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '[') {
                bracketCount++;
            } else if (c == ']') {
                bracketCount--;
                if (bracketCount == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
}

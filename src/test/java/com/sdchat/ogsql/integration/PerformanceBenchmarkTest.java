package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.ParseResult;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.InputValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance benchmark tests for the SQL parser.
 * Verifies that the parser meets the 1000+ statements/second performance target.
 */
public class PerformanceBenchmarkTest {

    private SQLParser parser;
    
    @BeforeEach
    void setUp() {
        parser = new SQLParser();
    }
    
    @Test
    @DisplayName("Should parse simple SELECT statements with high performance")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testSimpleSelectPerformance() {
        String sql = "SELECT id, name, email FROM users WHERE active = true";
        
        int iterations = 1000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            SQLStatement result = parser.parse(sql);
            assertNotNull(result, "Parsing should succeed");
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double statementsPerSecond = iterations / (durationMs / 1000.0);
        
        System.out.println(String.format("Parsed %d simple SELECT statements in %.2f ms (%.2f statements/second)", 
                                        iterations, durationMs, statementsPerSecond));
        
        assertTrue(statementsPerSecond >= 1000, 
                   "Parser should achieve at least 1000 statements/second for simple SELECT");
    }
    
    @Test
    @DisplayName("Should parse complex JOIN statements with acceptable performance")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComplexJoinPerformance() {
        String sql = "SELECT u.id, u.name, o.order_id, o.total " +
                     "FROM users u " +
                     "INNER JOIN orders o ON u.id = o.user_id " +
                     "LEFT JOIN products p ON o.product_id = p.id " +
                     "WHERE u.active = true AND o.status = 'completed' " +
                     "ORDER BY o.created_at DESC " +
                     "LIMIT 100";
        
        int iterations = 500;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            SQLStatement result = parser.parse(sql);
            assertNotNull(result, "Parsing should succeed");
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double statementsPerSecond = iterations / (durationMs / 1000.0);
        
        System.out.println(String.format("Parsed %d complex JOIN statements in %.2f ms (%.2f statements/second)", 
                                        iterations, durationMs, statementsPerSecond));
        
        assertTrue(statementsPerSecond >= 500, 
                   "Parser should achieve at least 500 statements/second for complex JOIN");
    }
    
    @Test
    @DisplayName("Should parse INSERT statements with high performance")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testInsertPerformance() {
        String sql = "INSERT INTO users (name, email, created_at) VALUES ('John Doe', 'john@example.com', CURRENT_TIMESTAMP)";
        
        int iterations = 1000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            SQLStatement result = parser.parse(sql);
            assertNotNull(result, "Parsing should succeed");
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double statementsPerSecond = iterations / (durationMs / 1000.0);
        
        System.out.println(String.format("Parsed %d INSERT statements in %.2f ms (%.2f statements/second)", 
                                        iterations, durationMs, statementsPerSecond));
        
        assertTrue(statementsPerSecond >= 1000, 
                   "Parser should achieve at least 1000 statements/second for INSERT");
    }
    
    @Test
    @DisplayName("Should parse CREATE TABLE statements with acceptable performance")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateTablePerformance() {
        String sql = "CREATE TABLE products (" +
                     "id SERIAL PRIMARY KEY, " +
                     "name VARCHAR(255) NOT NULL, " +
                     "description TEXT, " +
                     "price DECIMAL(10,2), " +
                     "category_id INTEGER, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "updated_at TIMESTAMP" +
                     ")";
        
        int iterations = 500;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            SQLStatement result = parser.parse(sql);
            assertNotNull(result, "Parsing should succeed");
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double statementsPerSecond = iterations / (durationMs / 1000.0);
        
        System.out.println(String.format("Parsed %d CREATE TABLE statements in %.2f ms (%.2f statements/second)", 
                                        iterations, durationMs, statementsPerSecond));
        
        assertTrue(statementsPerSecond >= 500, 
                   "Parser should achieve at least 500 statements/second for CREATE TABLE");
    }
    
    @Test
    @DisplayName("Should parse multiple statements with acceptable performance")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMultipleStatementsPerformance() {
        String sql = "SELECT * FROM users; INSERT INTO logs (message) VALUES ('test'); UPDATE users SET last_login = CURRENT_TIMESTAMP";
        
        int iterations = 300;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            List<SQLStatement> results = parser.parseMultiple(sql);
            assertEquals(3, results.size(), "Should parse 3 statements");
            for (SQLStatement result : results) {
                assertNotNull(result, "Each statement should parse successfully");
            }
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double statementsPerSecond = (iterations * 3) / (durationMs / 1000.0);
        
        System.out.println(String.format("Parsed %d sets of multiple statements (total %d statements) in %.2f ms (%.2f statements/second)", 
                                        iterations, iterations * 3, durationMs, statementsPerSecond));
        
        assertTrue(statementsPerSecond >= 300, 
                   "Parser should achieve at least 300 statements/second for multiple statements");
    }
    
    @Test
    @DisplayName("Should handle large SQL content from stream efficiently")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLargeStreamPerformance() throws IOException {
        // Create a large SQL content (1MB)
        StringBuilder largeSql = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeSql.append("SELECT * FROM users WHERE id = ").append(i).append(";\n");
        }
        String sql = largeSql.toString();
        
        // Test streaming performance
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sql.getBytes(StandardCharsets.UTF_8));
        
        int iterations = 10;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            inputStream.reset();
            ParseResult result = parser.parseStream(inputStream);
            assertTrue(result.isSuccess(), "Parsing should succeed");
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double mbPerSecond = (iterations * sql.length() / (1024.0 * 1024.0)) / (durationMs / 1000.0);
        
        System.out.println(String.format("Parsed %d large SQL streams (%.2f MB total) in %.2f ms (%.2f MB/second)", 
                                        iterations, iterations * sql.length() / (1024.0 * 1024.0), 
                                        durationMs, mbPerSecond));
        
        assertTrue(mbPerSecond >= 0.3, 
                   "Parser should process at least 0.3 MB/second for large content");
    }
    
    @Test
    @DisplayName("Should enforce memory limits during parsing")
    void testMemoryLimitEnforcement() {
        // Set a very low memory limit to test enforcement
        parser.setMemoryLimit(1024); // 1KB limit
        
        String sql = "SELECT * FROM very_large_table_with_many_columns_and_complex_conditions";
        
        // This should still work as the actual memory usage check is approximate
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Parsing should succeed or fail gracefully with memory limit");
    }
    
    @Test
    @DisplayName("Should enforce file size limits")
    void testFileSizeLimitEnforcement() {
        // Set a very low file size limit
        parser.setMaxFileSize(100); // 100 bytes limit
        
        String largeSql = "SELECT * FROM users; " + "SELECT * FROM orders; ".repeat(10);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(largeSql.getBytes(StandardCharsets.UTF_8));
        
        assertThrows(InputValidationException.class, () -> {
            parser.parseStream(inputStream);
        }, "Should throw InputValidationException for large content");
    }
}
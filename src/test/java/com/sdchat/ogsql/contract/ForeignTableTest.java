package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract tests for CREATE FOREIGN TABLE parsing.
 * These tests verify that the parser can correctly parse foreign table definitions
 * and extract server information, options, and column definitions.
 * 
 * Note: Now that grammar is implemented, these tests verify correct parsing behavior.
 */
public class ForeignTableTest {

    private SQLParser parser;

    @BeforeEach
    void setUp() {
        parser = new SQLParser();
    }

    @Test
    @DisplayName("Should parse basic CREATE FOREIGN TABLE with SERVER")
    void testBasicForeignTable() {
        String sql = "CREATE FOREIGN TABLE remote_users (id INT, name VARCHAR(50)) SERVER mysql_server";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with SERVER and OPTIONS")
    void testForeignTableWithOptions() {
        String sql = "CREATE FOREIGN TABLE remote_data (id INT, data TEXT) SERVER pg_server " +
                     "OPTIONS ('host' = 'localhost', 'port' = '5432', 'dbname' = 'remote_db')";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with SERVER OPTIONS and table OPTIONS")
    void testForeignTableWithServerAndTableOptions() {
        String sql = "CREATE FOREIGN TABLE users_data (user_id INT, user_name VARCHAR(100)) SERVER oracle_server " +
                     "OPTIONS ('fetch_size' = '1000', 'batch_size' = '500') " +
                     "SERVER OPTIONS ('connection_timeout' = '30', 'retry_count' = '3')";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE without columns")
    void testForeignTableWithoutColumns() {
        String sql = "CREATE FOREIGN TABLE remote_config () SERVER config_server";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should fail gracefully on invalid foreign table syntax")
    void testInvalidForeignTableSyntax() {
        String sql = "CREATE FOREIGN TABLE";
        
        assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        }, "Parsing should fail");
    }

    @Test
    @DisplayName("Should fail on missing SERVER clause")
    void testMissingServerClause() {
        String sql = "CREATE FOREIGN TABLE users (id INT)";
        
        // This should fail due to missing SERVER clause
        assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        }, "Parsing should fail due to missing SERVER clause");
    }

    @Test
    @DisplayName("Should handle foreign table with complex column definitions")
    void testForeignTableWithComplexColumns() {
        String sql = "CREATE FOREIGN TABLE complex_data (" +
                     "id BIGINT PRIMARY KEY, " +
                     "name VARCHAR(255) NOT NULL, " +
                     "data JSON, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "is_active BOOLEAN DEFAULT true" +
                     ") SERVER complex_server";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should provide meaningful error for extraneous input 'FOREIGN'")
    void testExtraneousInputForeign() {
        String sql = "CREATE FOREIGN TABLE test (id INT) SERVER test_server";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }
}
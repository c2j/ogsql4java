package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for foreign table parsing.
 * These tests verify end-to-end parsing of foreign table statements
 * including complex scenarios with multiple options and error handling.
 * 
 * Note: Now that grammar is implemented, these tests verify correct parsing behavior.
 */
public class ForeignTableIntegrationTest {

    private SQLParser parser;

    @BeforeEach
    void setUp() {
        parser = new SQLParser();
    }

    @Test
    @DisplayName("Should parse complete foreign table creation workflow")
    void testCompleteForeignTableWorkflow() {
        String sql = "CREATE FOREIGN TABLE customer_data (" +
                     "customer_id BIGINT OPTIONS ('column_name' 'cust_id', 'nullable' 'false'), " +
                     "customer_name VARCHAR(200) OPTIONS ('column_name' 'cust_name', 'encoding' 'UTF-8'), " +
                     "email VARCHAR(255) OPTIONS ('nullable' 'true'), " +
                     "registration_date TIMESTAMP OPTIONS ('type_mapping' 'DATETIME'), " +
                     "is_active BOOLEAN OPTIONS ('default_value' 'true')" +
                     ") SERVER mysql_production " +
                     "OPTIONS ('host' 'prod-db.example.com', 'port' '3306', 'database' 'customers') " +
                     "SERVER OPTIONS ('connection_timeout' '60', 'retry_attempts' '3')";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse multiple foreign table statements")
    void testMultipleForeignTableStatements() {
        String[] statements = {
            "CREATE FOREIGN TABLE users (id INT, name VARCHAR(100)) SERVER mysql_server",
            "CREATE FOREIGN TABLE orders (order_id INT, user_id INT, total DECIMAL(10,2)) SERVER pg_server OPTIONS ('schema' 'public')",
            "ALTER FOREIGN TABLE users OPTIONS (SET 'host' 'new-host')"
        };
        
        // Now that grammar is implemented, all should parse successfully
        for (String sql : statements) {
            SQLStatement result = parser.parse(sql);
            assertNotNull(result, "Should parse successfully: " + sql);
        }
    }

    @Test
    @DisplayName("Should handle foreign table with complex server configuration")
    void testComplexServerConfiguration() {
        String sql = "CREATE FOREIGN TABLE enterprise_data (" +
                     "data_id UUID, " +
                     "payload TEXT, " +
                     "metadata TEXT, " +
                     "created_at TIMESTAMP, " +
                     "updated_at TIMESTAMP" +
                     ") SERVER oracle_enterprise " +
                     "OPTIONS (" +
                     "  'service_name' 'PROD_SERVICE', " +
                     "  'connection_string' '(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=oracle-prod)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=PROD_SERVICE)))', " +
                     "  'fetch_size' '5000', " +
                     "  'batch_size' '1000', " +
                     "  'query_timeout' '300'" +
                     ") " +
                     "SERVER OPTIONS (" +
                     "  'connection_pool_size' '10', " +
                     "  'connection_timeout' '120', " +
                     "  'retry_attempts' '5', " +
                     "  'ssl_enabled' 'true', " +
                     "  'ssl_mode' 'require'" +
                     ")";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should handle foreign table with error recovery")
    void testForeignTableErrorRecovery() {
        String sql = "CREATE FOREIGN TABLE test_table (id INT) SERVER test_server " +
                     "OPTIONS (invalid_option_without_value)";
        
        // This should fail due to invalid option syntax
        assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        }, "Should fail due to invalid option syntax");
    }

    @Test
    @DisplayName("Should parse foreign table with PostgreSQL-specific options")
    void testPostgreSQLSpecificOptions() {
        String sql = "CREATE FOREIGN TABLE postgres_remote (" +
                     "id SERIAL, " +
                     "data TEXT, " +
                     "tags TEXT, " +
                     "geom TEXT" +
                     ") SERVER postgres_fdw " +
                     "OPTIONS (" +
                     "  'schema_name' 'public', " +
                     "  'table_name' 'remote_table', " +
                     "  'updatable' 'true', " +
                     "  'truncatable' 'false'" +
                     ") " +
                     "SERVER OPTIONS (" +
                     "  'use_remote_estimate' 'true', " +
                     "  'fdw_startup_cost' '100', " +
                     "  'fdw_tuple_cost' '0.01'" +
                     ")";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse foreign table with MySQL-specific options")
    void testMySQLSpecificOptions() {
        String sql = "CREATE FOREIGN TABLE mysql_remote (" +
                     "id INT, " +
                     "name VARCHAR(255), " +
                     "price DECIMAL(10,2), " +
                     "is_active TINYINT(1) DEFAULT 1" +
                     ") SERVER mysql_fdw " +
                     "OPTIONS (" +
                     "  'database' 'mydb', " +
                     "  'table_name' 'remote_items', " +
                     "  'charset' 'utf8mb4', " +
                     "  'fetch_size' '1000'" +
                     ") " +
                     "SERVER OPTIONS (" +
                     "  'connection_timeout' '30', " +
                     "  'read_timeout' '60', " +
                     "  'write_timeout' '60'" +
                     ")";
        
        // Now that grammar is implemented, this should parse successfully
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should handle mixed regular and foreign table statements")
    void testMixedTableStatements() {
        String[] statements = {
            "CREATE TABLE local_users (id SERIAL PRIMARY KEY, name VARCHAR(100))",
            "CREATE FOREIGN TABLE remote_orders (order_id INT, user_id INT) SERVER order_server",
            "SELECT * FROM local_users WHERE name = 'test'",
            "ALTER FOREIGN TABLE remote_orders OPTIONS (SET 'host' 'new-server')"
        };
        
        // Now that grammar is implemented, test each statement individually
        assertDoesNotThrow(() -> {
            parser.parse(statements[0]); // CREATE TABLE should succeed
        }, "CREATE TABLE should parse successfully");
        
        assertDoesNotThrow(() -> {
            parser.parse(statements[2]); // SELECT should succeed  
        }, "SELECT should parse successfully");
        
        // FOREIGN TABLE statements should now succeed
        assertDoesNotThrow(() -> {
            parser.parse(statements[1]);
        }, "CREATE FOREIGN TABLE should parse successfully");
        
        assertDoesNotThrow(() -> {
            parser.parse(statements[3]);
        }, "ALTER FOREIGN TABLE should parse successfully");
    }
}
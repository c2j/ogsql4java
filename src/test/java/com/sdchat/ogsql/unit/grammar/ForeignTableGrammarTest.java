package com.sdchat.ogsql.unit.grammar;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for foreign table grammar rules.
 * These tests verify that the ANTLR4 grammar correctly parses foreign table statements
 * and handles various syntax scenarios.
 */
public class ForeignTableGrammarTest {

    private SQLParser parser;

    @BeforeEach
    void setUp() {
        parser = new SQLParser();
    }

    @Test
    @DisplayName("Should parse basic CREATE FOREIGN TABLE")
    void testBasicCreateForeignTable() {
        String sql = "CREATE FOREIGN TABLE remote_users (id INT, name VARCHAR(100)) SERVER mysql_server";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse basic CREATE FOREIGN TABLE");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with table OPTIONS")
    void testCreateForeignTableWithTableOptions() {
        String sql = "CREATE FOREIGN TABLE remote_data (id INT, data TEXT) SERVER pg_server " +
                     "OPTIONS ('host' = 'localhost', 'port' = '5432')";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse CREATE FOREIGN TABLE with table OPTIONS");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with server and table OPTIONS")
    void testCreateForeignTableWithServerAndTableOptions() {
        String sql = "CREATE FOREIGN TABLE users_data (user_id INT, user_name VARCHAR(100)) SERVER oracle_server " +
                     "OPTIONS ('fetch_size' = '1000') " +
                     "SERVER OPTIONS ('connection_timeout' = '30')";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse CREATE FOREIGN TABLE with server and table OPTIONS");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE without columns")
    void testCreateForeignTableWithoutColumns() {
        String sql = "CREATE FOREIGN TABLE remote_config () SERVER config_server";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse CREATE FOREIGN TABLE without columns");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with column OPTIONS")
    void testCreateForeignTableWithColumnOptions() {
        String sql = "CREATE FOREIGN TABLE remote_users (" +
                     "id INT OPTIONS ('column_name' = 'user_id'), " +
                     "name VARCHAR(100) OPTIONS ('column_name' = 'full_name')" +
                     ") SERVER mysql_server";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse CREATE FOREIGN TABLE with column OPTIONS");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with complex column definitions")
    void testCreateForeignTableWithComplexColumns() {
        String sql = "CREATE FOREIGN TABLE complex_data (" +
                     "id BIGINT PRIMARY KEY, " +
                     "name VARCHAR(255) NOT NULL, " +
                     "data JSON, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "is_active BOOLEAN DEFAULT true" +
                     ") SERVER complex_server";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse CREATE FOREIGN TABLE with complex column definitions");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE to change SERVER")
    void testAlterForeignTableServer() {
        String sql = "ALTER FOREIGN TABLE remote_users SERVER new_mysql_server";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE to change SERVER");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE to modify OPTIONS")
    void testAlterForeignTableOptions() {
        String sql = "ALTER FOREIGN TABLE remote_data OPTIONS (SET 'host' 'newhost', SET 'port' '5433')";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE to modify OPTIONS");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE to add column")
    void testAlterForeignTableAddColumn() {
        String sql = "ALTER FOREIGN TABLE users_data ADD COLUMN email VARCHAR(255)";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE to add column");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE to drop column")
    void testAlterForeignTableDropColumn() {
        String sql = "ALTER FOREIGN TABLE users_data DROP COLUMN temp_field";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE to drop column");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE to alter column type")
    void testAlterForeignTableAlterColumn() {
        String sql = "ALTER FOREIGN TABLE users_data ALTER COLUMN name TYPE VARCHAR(200)";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE to alter column type");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE to rename")
    void testAlterForeignTableRename() {
        String sql = "ALTER FOREIGN TABLE old_remote_users RENAME TO new_remote_users";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE to rename");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE to set schema")
    void testAlterForeignTableSetSchema() {
        String sql = "ALTER FOREIGN TABLE remote_users SET SCHEMA external";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE to set schema");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE with multiple actions")
    void testAlterForeignTableMultipleActions() {
        String sql = "ALTER FOREIGN TABLE remote_data " +
                     "ADD COLUMN status VARCHAR(20), " +
                     "ALTER COLUMN data TYPE TEXT, " +
                     "OPTIONS (SET 'fetch_size' '2000')";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse ALTER FOREIGN TABLE with multiple actions");
    }

    @Test
    @DisplayName("Should fail on invalid foreign table syntax")
    void testInvalidForeignTableSyntax() {
        String sql = "CREATE FOREIGN TABLE";
        
        assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        }, "Should fail on invalid foreign table syntax");
    }

    @Test
    @DisplayName("Should fail on incomplete ALTER FOREIGN TABLE")
    void testIncompleteAlterForeignTable() {
        String sql = "ALTER FOREIGN TABLE users_data ADD";  // Missing column definition
        
        assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        }, "Should fail on incomplete ALTER FOREIGN TABLE");
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
                     "  'schema_name' = 'public', " +
                     "  'table_name' = 'remote_table', " +
                     "  'updatable' = 'true', " +
                     "  'truncatable' = 'false'" +
                     ") " +
                     "SERVER OPTIONS (" +
                     "  'use_remote_estimate' = 'true', " +
                     "  'fdw_startup_cost' = '100', " +
                     "  'fdw_tuple_cost' = '0.01'" +
                     ")";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse foreign table with PostgreSQL-specific options");
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
                     "  'database' = 'mydb', " +
                     "  'table_name' = 'remote_items', " +
                     "  'charset' = 'utf8mb4', " +
                     "  'fetch_size' = '1000'" +
                     ") " +
                     "SERVER OPTIONS (" +
                     "  'connection_timeout' = '30', " +
                     "  'read_timeout' = '60', " +
                     "  'write_timeout' = '60'" +
                     ")";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse foreign table with MySQL-specific options");
    }
}
package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration tests for foreign table parsing.
 * These tests verify basic foreign table functionality.
 */
public class SimpleForeignTableIntegrationTest {

    private SQLParser parser;

    @BeforeEach
    void setUp() {
        parser = new SQLParser();
    }

    @Test
    @DisplayName("Should parse basic CREATE FOREIGN TABLE")
    void testBasicForeignTable() {
        String sql = "CREATE FOREIGN TABLE remote_users (id INT, name VARCHAR(100)) SERVER mysql_server";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with table options")
    void testForeignTableWithTableOptions() {
        String sql = "CREATE FOREIGN TABLE remote_data (id INT, data TEXT) SERVER pg_server OPTIONS ('host' = 'localhost', 'port' = '5432')";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse CREATE FOREIGN TABLE with server and table options")
    void testForeignTableWithServerAndTableOptions() {
        String sql = "CREATE FOREIGN TABLE users_data (user_id INT, user_name VARCHAR(100)) SERVER oracle_server " +
                     "OPTIONS ('fetch_size' = '1000') " +
                     "SERVER OPTIONS ('connection_timeout' = '30')";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse ALTER FOREIGN TABLE")
    void testAlterForeignTable() {
        String sql = "ALTER FOREIGN TABLE remote_users OPTIONS (SET 'host' 'newhost')";
        
        SQLStatement result = parser.parse(sql);
        assertNotNull(result, "Should parse successfully");
    }

    @Test
    @DisplayName("Should parse mixed regular and foreign table statements")
    void testMixedStatements() {
        String sql1 = "CREATE TABLE local_users (id INT PRIMARY KEY, name VARCHAR(100))";
        String sql2 = "CREATE FOREIGN TABLE remote_orders (order_id INT, user_id INT) SERVER order_server";
        
        SQLStatement result1 = parser.parse(sql1);
        assertNotNull(result1, "CREATE TABLE should parse successfully");
        
        SQLStatement result2 = parser.parse(sql2);
        assertNotNull(result2, "CREATE FOREIGN TABLE should parse successfully");
    }
}
package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.ExternalTable;
import com.sdchat.ogsql.ast.Column;
import com.sdchat.ogsql.ast.StatementType;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for ExternalTable class.
 */
class ExternalTableTest {

    @Test
    void testBasicExternalTable() {
        ExternalTable externalTable = new ExternalTable("remote_users", "mysql_server");
        
        assertEquals("remote_users", externalTable.getTableName());
        assertEquals("mysql_server", externalTable.getServerName());
        assertEquals(StatementType.CREATE_FOREIGN_TABLE, externalTable.getStatementType());
        assertFalse(externalTable.hasColumns());
        assertFalse(externalTable.hasServerOptions());
        assertFalse(externalTable.hasTableOptions());
        assertEquals(0, externalTable.getColumnCount());
        assertEquals(0, externalTable.getServerOptionCount());
        assertEquals(0, externalTable.getTableOptionCount());
    }

    @Test
    void testExternalTableWithColumns() {
        ExternalTable externalTable = new ExternalTable("remote_users", "mysql_server");
        
        Column idColumn = new Column();
        idColumn.setName("id");
        idColumn.setDataType("INT");
        
        Column nameColumn = new Column();
        nameColumn.setName("name");
        nameColumn.setDataType("VARCHAR(100)");
        
        externalTable.addColumn(idColumn);
        externalTable.addColumn(nameColumn);
        
        assertEquals(2, externalTable.getColumnCount());
        assertTrue(externalTable.hasColumns());
        assertEquals(Arrays.asList(idColumn, nameColumn), externalTable.getColumns());
    }

    @Test
    void testExternalTableWithServerOptions() {
        ExternalTable externalTable = new ExternalTable("remote_users", "mysql_server");
        
        externalTable.addServerOption("host", "remote-mysql.example.com");
        externalTable.addServerOption("port", "3306");
        externalTable.addServerOption("database", "remote_db");
        
        assertEquals(3, externalTable.getServerOptionCount());
        assertTrue(externalTable.hasServerOptions());
        
        Map<String, String> expectedOptions = new HashMap<>();
        expectedOptions.put("host", "remote-mysql.example.com");
        expectedOptions.put("port", "3306");
        expectedOptions.put("database", "remote_db");
        
        assertEquals(expectedOptions, externalTable.getServerOptions());
    }

    @Test
    void testExternalTableWithTableOptions() {
        ExternalTable externalTable = new ExternalTable("remote_users", "mysql_server");
        
        externalTable.addTableOption("fetch_size", "1000");
        externalTable.addTableOption("batch_size", "500");
        
        assertEquals(2, externalTable.getTableOptionCount());
        assertTrue(externalTable.hasTableOptions());
        
        Map<String, String> expectedOptions = new HashMap<>();
        expectedOptions.put("fetch_size", "1000");
        expectedOptions.put("batch_size", "500");
        
        assertEquals(expectedOptions, externalTable.getTableOptions());
    }

    @Test
    void testConstructorValidation() {
        // Test null table name
        assertThrows(IllegalArgumentException.class, () -> new ExternalTable(null, "server"));
        
        // Test empty table name
        assertThrows(IllegalArgumentException.class, () -> new ExternalTable("", "server"));
        
        // Test whitespace-only table name
        assertThrows(IllegalArgumentException.class, () -> new ExternalTable("   ", "server"));
        
        // Test null server name
        assertThrows(IllegalArgumentException.class, () -> new ExternalTable("table", null));
        
        // Test empty server name
        assertThrows(IllegalArgumentException.class, () -> new ExternalTable("table", ""));
        
        // Test whitespace-only server name
        assertThrows(IllegalArgumentException.class, () -> new ExternalTable("table", "   "));
        
        // Test valid construction
        ExternalTable table = new ExternalTable("test_table", "test_server");
        assertEquals("test_table", table.getTableName());
        assertEquals("test_server", table.getServerName());
    }

    @Test
    void testSetNameValidation() {
        ExternalTable externalTable = new ExternalTable("original", "server");
        
        // Test valid name change
        externalTable.setTableName("new_table");
        assertEquals("new_table", externalTable.getTableName());
        
        externalTable.setServerName("new_server");
        assertEquals("new_server", externalTable.getServerName());
        
        // Test null table name
        assertThrows(IllegalArgumentException.class, () -> externalTable.setTableName(null));
        
        // Test empty table name
        assertThrows(IllegalArgumentException.class, () -> externalTable.setTableName(""));
        
        // Test null server name
        assertThrows(IllegalArgumentException.class, () -> externalTable.setServerName(null));
        
        // Test empty server name
        assertThrows(IllegalArgumentException.class, () -> externalTable.setServerName(""));
    }

    @Test
    void testAddOptionValidation() {
        ExternalTable externalTable = new ExternalTable("table", "server");
        
        // Test valid option
        externalTable.addServerOption("host", "localhost");
        assertEquals("localhost", externalTable.getServerOptions().get("host"));
        
        externalTable.addTableOption("fetch_size", "100");
        assertEquals("100", externalTable.getTableOptions().get("fetch_size"));
        
        // Test null key
        assertThrows(IllegalArgumentException.class, () -> externalTable.addServerOption(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> externalTable.addTableOption(null, "value"));
        
        // Test empty key
        assertThrows(IllegalArgumentException.class, () -> externalTable.addServerOption("", "value"));
        assertThrows(IllegalArgumentException.class, () -> externalTable.addTableOption("", "value"));
        
        // Test whitespace-only key
        assertThrows(IllegalArgumentException.class, () -> externalTable.addServerOption("   ", "value"));
        assertThrows(IllegalArgumentException.class, () -> externalTable.addTableOption("   ", "value"));
    }

    @Test
    void testSetOptionsValidation() {
        ExternalTable externalTable = new ExternalTable("table", "server");
        
        // Test null server options
        assertThrows(IllegalArgumentException.class, () -> externalTable.setServerOptions(null));
        
        // Test null table options
        assertThrows(IllegalArgumentException.class, () -> externalTable.setTableOptions(null));
        
        // Test valid options
        Map<String, String> serverOptions = new HashMap<>();
        serverOptions.put("host", "remote-host");
        externalTable.setServerOptions(serverOptions);
        assertEquals(serverOptions, externalTable.getServerOptions());
        
        Map<String, String> tableOptions = new HashMap<>();
        tableOptions.put("batch_size", "1000");
        externalTable.setTableOptions(tableOptions);
        assertEquals(tableOptions, externalTable.getTableOptions());
    }

    @Test
    void testAddColumnValidation() {
        ExternalTable externalTable = new ExternalTable("table", "server");
        
        // Test null column
        assertThrows(IllegalArgumentException.class, () -> externalTable.addColumn(null));
        
        // Test valid column
        Column column = new Column();
        column.setName("test_col");
        externalTable.addColumn(column);
        assertEquals(1, externalTable.getColumnCount());
    }

    @Test
    void testValidation() {
        ExternalTable externalTable = new ExternalTable("table", "server");
        
        // Should not throw for valid table
        assertDoesNotThrow(() -> externalTable.validate());
        
        // Test validation after setting null names (should be caught by setters)
        // But test the validate method directly
        ExternalTable validTable = new ExternalTable("test_table", "test_server");
        assertDoesNotThrow(() -> validTable.validate());
    }

    @Test
    void testToString() {
        ExternalTable externalTable = new ExternalTable("remote_users", "mysql_server");
        externalTable.addServerOption("host", "localhost");
        externalTable.addTableOption("fetch_size", "1000");
        
        String result = externalTable.toString();
        assertTrue(result.contains("remote_users"));
        assertTrue(result.contains("mysql_server"));
        assertTrue(result.contains("host"));
        assertTrue(result.contains("fetch_size"));
        assertTrue(result.contains("0 columns"));
    }
}
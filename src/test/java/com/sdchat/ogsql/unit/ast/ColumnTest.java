package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.Column;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for Column")
class ColumnTest {

    @Test
    @DisplayName("Column should set and get name")
    void testName() {
        Column column = new Column();
        column.setName("id");
        assertEquals("id", column.getName());
    }

    @Test
    @DisplayName("Column should set and get dataType")
    void testDataType() {
        Column column = new Column();
        column.setDataType("INTEGER");
        assertEquals("INTEGER", column.getDataType());
    }

    @Test
    @DisplayName("Column should set and get nullable")
    void testNullable() {
        Column column = new Column();
        column.setNullable(false);
        assertFalse(column.isNullable());
        
        column.setNullable(true);
        assertTrue(column.isNullable());
    }

    @Test
    @DisplayName("Column should default to nullable")
    void testDefaultNullable() {
        Column column = new Column();
        assertTrue(column.isNullable());
    }

    @Test
    @DisplayName("Column should set and get defaultValue")
    void testDefaultValue() {
        Column column = new Column();
        column.setDefaultValue("'test'");
        assertEquals("'test'", column.getDefaultValue());
    }

    @Test
    @DisplayName("Column constructor should set name and dataType")
    void testConstructor() {
        Column column = new Column("id", "INTEGER");
        assertEquals("id", column.getName());
        assertEquals("INTEGER", column.getDataType());
    }
}

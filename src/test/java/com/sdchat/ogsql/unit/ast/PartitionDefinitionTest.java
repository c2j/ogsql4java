package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.PartitionDefinition;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for PartitionDefinition class.
 */
class PartitionDefinitionTest {

    @Test
    void testBasicPartitionDefinition() {
        PartitionDefinition partition = new PartitionDefinition("p2023");
        
        assertEquals("p2023", partition.getName());
        assertNull(partition.getMinValue());
        assertNull(partition.getMaxValue());
        assertNull(partition.getValues());
        assertNull(partition.getTablespace());
        assertFalse(partition.hasTablespace());
        assertFalse(partition.isRangePartition());
        assertFalse(partition.isListPartition());
    }

    @Test
    void testRangePartitionDefinition() {
        PartitionDefinition partition = new PartitionDefinition("p2023");
        partition.setMinValue("2023-01-01");
        partition.setMaxValue("2023-12-31");
        
        assertEquals("p2023", partition.getName());
        assertEquals("2023-01-01", partition.getMinValue());
        assertEquals("2023-12-31", partition.getMaxValue());
        assertTrue(partition.isRangePartition());
        assertFalse(partition.isListPartition());
    }

    @Test
    void testListPartitionDefinition() {
        PartitionDefinition partition = new PartitionDefinition("p_north_america");
        List<String> countries = Arrays.asList("USA", "Canada", "Mexico");
        partition.setValues(countries);
        
        assertEquals("p_north_america", partition.getName());
        assertEquals(countries, partition.getValues());
        assertTrue(partition.isListPartition());
        assertFalse(partition.isRangePartition());
    }

    @Test
    void testPartitionWithTablespace() {
        PartitionDefinition partition = new PartitionDefinition("p2023");
        partition.setTablespace("ts_sales_2023");
        
        assertEquals("p2023", partition.getName());
        assertEquals("ts_sales_2023", partition.getTablespace());
        assertTrue(partition.hasTablespace());
    }

    @Test
    void testSetNameValidation() {
        PartitionDefinition partition = new PartitionDefinition("original");
        
        // Test valid name change
        partition.setName("new_name");
        assertEquals("new_name", partition.getName());
        
        // Test null name throws exception
        assertThrows(IllegalArgumentException.class, () -> partition.setName(null));
        
        // Test empty name throws exception
        assertThrows(IllegalArgumentException.class, () -> partition.setName(""));
        
        // Test whitespace-only name throws exception
        assertThrows(IllegalArgumentException.class, () -> partition.setName("   "));
    }

    @Test
    void testConstructorValidation() {
        // Test null name throws exception
        assertThrows(IllegalArgumentException.class, () -> new PartitionDefinition(null));
        
        // Test empty name throws exception
        assertThrows(IllegalArgumentException.class, () -> new PartitionDefinition(""));
        
        // Test whitespace-only name throws exception
        assertThrows(IllegalArgumentException.class, () -> new PartitionDefinition("   "));
        
        // Test valid construction
        PartitionDefinition partition = new PartitionDefinition("valid_name");
        assertEquals("valid_name", partition.getName());
    }

    @Test
    void testEmptyTablespaceHandling() {
        PartitionDefinition partition = new PartitionDefinition("p2023");
        
        // Empty tablespace should not be considered as having tablespace
        partition.setTablespace("");
        assertFalse(partition.hasTablespace());
        
        // Whitespace-only tablespace should not be considered as having tablespace
        partition.setTablespace("   ");
        assertFalse(partition.hasTablespace());
        
        // Valid tablespace
        partition.setTablespace("ts_sales");
        assertTrue(partition.hasTablespace());
    }

    @Test
    void testEmptyListPartitionHandling() {
        PartitionDefinition partition = new PartitionDefinition("p_empty");
        
        // Null values should not be considered as list partition
        assertFalse(partition.isListPartition());
        
        // Empty list should not be considered as list partition
        partition.setValues(Arrays.asList());
        assertFalse(partition.isListPartition());
        
        // Valid list should be considered as list partition
        partition.setValues(Arrays.asList("value1", "value2"));
        assertTrue(partition.isListPartition());
    }

    @Test
    void testToString() {
        PartitionDefinition partition = new PartitionDefinition("p2023");
        partition.setMinValue("2023-01-01");
        partition.setMaxValue("2023-12-31");
        partition.setTablespace("ts_sales_2023");
        
        String result = partition.toString();
        assertTrue(result.contains("p2023"));
        assertTrue(result.contains("2023-01-01"));
        assertTrue(result.contains("2023-12-31"));
        assertTrue(result.contains("ts_sales_2023"));
    }
}
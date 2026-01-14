package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.PartitionType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for PartitionType enum.
 */
class PartitionTypeTest {

    @Test
    void testPartitionTypeValues() {
        // Test that all expected partition types are present
        PartitionType[] types = PartitionType.values();
        assertEquals(3, types.length);
        
        // Test individual values
        assertNotNull(PartitionType.RANGE);
        assertNotNull(PartitionType.LIST);
        assertNotNull(PartitionType.HASH);
    }

    @Test
    void testPartitionTypeNames() {
        // Test that enum names match expected values
        assertEquals("RANGE", PartitionType.RANGE.name());
        assertEquals("LIST", PartitionType.LIST.name());
        assertEquals("HASH", PartitionType.HASH.name());
    }

    @Test
    void testPartitionTypeOrdinal() {
        // Test that enum ordinals are sequential
        assertEquals(0, PartitionType.RANGE.ordinal());
        assertEquals(1, PartitionType.LIST.ordinal());
        assertEquals(2, PartitionType.HASH.ordinal());
    }

    @Test
    void testPartitionTypeValueOf() {
        // Test valueOf method for string conversion
        assertEquals(PartitionType.RANGE, PartitionType.valueOf("RANGE"));
        assertEquals(PartitionType.LIST, PartitionType.valueOf("LIST"));
        assertEquals(PartitionType.HASH, PartitionType.valueOf("HASH"));
    }

    @Test
    void testPartitionTypeToString() {
        // Test toString method
        assertEquals("RANGE", PartitionType.RANGE.toString());
        assertEquals("LIST", PartitionType.LIST.toString());
        assertEquals("HASH", PartitionType.HASH.toString());
    }
}
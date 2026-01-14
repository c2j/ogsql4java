package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.PartitionType;
import com.sdchat.ogsql.ast.PartitionDefinition;
import com.sdchat.ogsql.ast.PartitioningInformation;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for PartitioningInformation class.
 */
class PartitioningInformationTest {

    @Test
    void testBasicPartitioningInformation() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        
        assertEquals(PartitionType.RANGE, partitioning.getType());
        assertNotNull(partitioning.getPartitionKeys());
        assertTrue(partitioning.getPartitionKeys().isEmpty());
        assertNotNull(partitioning.getPartitions());
        assertTrue(partitioning.getPartitions().isEmpty());
        assertFalse(partitioning.hasPartitionKeys());
        assertFalse(partitioning.hasPartitions());
        assertFalse(partitioning.hasSubpartitioning());
        assertEquals(0, partitioning.getPartitionCount());
        assertEquals(0, partitioning.getPartitionKeyCount());
    }

    @Test
    void testPartitioningWithKeys() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        partitioning.addPartitionKey("sale_date");
        partitioning.addPartitionKey("region");
        
        assertEquals(PartitionType.RANGE, partitioning.getType());
        assertEquals(2, partitioning.getPartitionKeyCount());
        assertEquals(Arrays.asList("sale_date", "region"), partitioning.getPartitionKeys());
        assertTrue(partitioning.hasPartitionKeys());
    }

    @Test
    void testPartitioningWithPartitions() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.LIST);
        partitioning.addPartitionKey("country");
        
        PartitionDefinition p1 = new PartitionDefinition("p_north_america");
        PartitionDefinition p2 = new PartitionDefinition("p_europe");
        
        partitioning.addPartition(p1);
        partitioning.addPartition(p2);
        
        assertEquals(PartitionType.LIST, partitioning.getType());
        assertEquals(2, partitioning.getPartitionCount());
        assertEquals(Arrays.asList(p1, p2), partitioning.getPartitions());
        assertTrue(partitioning.hasPartitions());
    }

    @Test
    void testSubpartitioning() {
        PartitioningInformation mainPartitioning = new PartitioningInformation(PartitionType.RANGE);
        mainPartitioning.addPartitionKey("sale_date");
        
        PartitioningInformation subPartitioning = new PartitioningInformation(PartitionType.LIST);
        subPartitioning.addPartitionKey("region");
        
        mainPartitioning.setSubpartitioning(subPartitioning);
        
        assertTrue(mainPartitioning.hasSubpartitioning());
        assertEquals(subPartitioning, mainPartitioning.getSubpartitioning());
    }

    @Test
    void testConstructorValidation() {
        // Test null type throws exception
        assertThrows(IllegalArgumentException.class, () -> new PartitioningInformation(null));
        
        // Test valid construction
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.HASH);
        assertEquals(PartitionType.HASH, partitioning.getType());
    }

    @Test
    void testSetTypeValidation() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        
        // Test null type throws exception
        assertThrows(IllegalArgumentException.class, () -> partitioning.setType(null));
        
        // Test valid type change
        partitioning.setType(PartitionType.LIST);
        assertEquals(PartitionType.LIST, partitioning.getType());
    }

    @Test
    void testSetPartitionKeysValidation() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        
        // Test null partition keys throws exception
        assertThrows(IllegalArgumentException.class, () -> partitioning.setPartitionKeys(null));
        
        // Test valid partition keys
        List<String> keys = Arrays.asList("col1", "col2");
        partitioning.setPartitionKeys(keys);
        assertEquals(keys, partitioning.getPartitionKeys());
    }

    @Test
    void testAddPartitionKeyValidation() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        
        // Test null key throws exception
        assertThrows(IllegalArgumentException.class, () -> partitioning.addPartitionKey(null));
        
        // Test empty key throws exception
        assertThrows(IllegalArgumentException.class, () -> partitioning.addPartitionKey(""));
        
        // Test whitespace-only key throws exception
        assertThrows(IllegalArgumentException.class, () -> partitioning.addPartitionKey("   "));
        
        // Test valid key
        partitioning.addPartitionKey("sale_date");
        assertEquals(Arrays.asList("sale_date"), partitioning.getPartitionKeys());
    }

    @Test
    void testSetPartitionsValidation() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        
        // Test null partitions throws exception
        assertThrows(IllegalArgumentException.class, () -> partitioning.setPartitions(null));
        
        // Test valid partitions
        List<PartitionDefinition> partitions = Arrays.asList(
            new PartitionDefinition("p1"),
            new PartitionDefinition("p2")
        );
        partitioning.setPartitions(partitions);
        assertEquals(partitions, partitioning.getPartitions());
    }

    @Test
    void testAddPartitionValidation() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        
        // Test null partition throws exception
        assertThrows(IllegalArgumentException.class, () -> partitioning.addPartition(null));
        
        // Test valid partition
        PartitionDefinition p1 = new PartitionDefinition("p1");
        partitioning.addPartition(p1);
        assertEquals(Arrays.asList(p1), partitioning.getPartitions());
    }

    @Test
    void testValidationRANGE() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        
        // RANGE without partition keys should fail validation
        assertThrows(IllegalStateException.class, () -> partitioning.validate());
        
        // RANGE with keys but no partitions should fail validation
        partitioning.addPartitionKey("sale_date");
        assertThrows(IllegalStateException.class, () -> partitioning.validate());
        
        // RANGE with keys and partitions should pass validation
        partitioning.addPartition(new PartitionDefinition("p2023"));
        assertDoesNotThrow(() -> partitioning.validate());
    }

    @Test
    void testValidationLIST() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.LIST);
        partitioning.addPartitionKey("country");
        partitioning.addPartition(new PartitionDefinition("p_north_america"));
        
        // LIST with keys and partitions should pass validation
        assertDoesNotThrow(() -> partitioning.validate());
    }

    @Test
    void testValidationHASH() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.HASH);
        partitioning.addPartitionKey("customer_id");
        
        // HASH with keys but no partitions should pass validation (partitions can be auto-generated)
        assertDoesNotThrow(() -> partitioning.validate());
        
        // HASH with keys and partitions should also pass validation
        partitioning.addPartition(new PartitionDefinition("p1"));
        assertDoesNotThrow(() -> partitioning.validate());
    }

    @Test
    void testSubpartitioningValidation() {
        PartitioningInformation mainPartitioning = new PartitioningInformation(PartitionType.RANGE);
        PartitioningInformation subPartitioning = new PartitioningInformation(PartitionType.LIST);
        
        subPartitioning.addPartitionKey("region");
        subPartitioning.addPartition(new PartitionDefinition("p_north"));
        
        mainPartitioning.setSubpartitioning(subPartitioning);
        
        // Subpartitioning without primary partitioning should fail validation
        assertThrows(IllegalStateException.class, () -> mainPartitioning.validate());
        
        // Add primary partitioning
        mainPartitioning.addPartitionKey("sale_date");
        mainPartitioning.addPartition(new PartitionDefinition("p2023"));
        
        // Now validation should pass
        assertDoesNotThrow(() -> mainPartitioning.validate());
    }

    @Test
    void testToString() {
        PartitioningInformation partitioning = new PartitioningInformation(PartitionType.RANGE);
        partitioning.addPartitionKey("sale_date");
        
        PartitionDefinition p1 = new PartitionDefinition("p2023");
        partitioning.addPartition(p1);
        
        String result = partitioning.toString();
        assertTrue(result.contains("RANGE"));
        assertTrue(result.contains("sale_date"));
        assertTrue(result.contains("p2023"));
    }
}
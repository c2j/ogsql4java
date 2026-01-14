package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.PerformanceHint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit test for PerformanceHint class.
 */
class PerformanceHintTest {

    @Test
    void testCreatePerformanceHintWithType() {
        PerformanceHint hint = new PerformanceHint("NestLoop");
        
        assertNotNull(hint);
        assertEquals("NestLoop", hint.getHintType());
        assertNotNull(hint.getTables());
        assertEquals(0, hint.getTables().size());
    }

    @Test
    void testCreatePerformanceHintWithTables() {
        List<String> tables = Arrays.asList("users", "orders");
        PerformanceHint hint = new PerformanceHint("MergeJoin", tables);
        
        assertNotNull(hint);
        assertEquals("MergeJoin", hint.getHintType());
        assertNotNull(hint.getTables());
        assertEquals(2, hint.getTables().size());
        assertEquals("users", hint.getTables().get(0));
        assertEquals("orders", hint.getTables().get(1));
    }

    @Test
    void testAddTable() {
        PerformanceHint hint = new PerformanceHint("HashJoin");
        hint.addTable("products");
        hint.addTable("categories");
        
        assertEquals(2, hint.getTables().size());
        assertEquals("products", hint.getTables().get(0));
        assertEquals("categories", hint.getTables().get(1));
    }

    @Test
    void testAddTableNullSafe() {
        PerformanceHint hint = new PerformanceHint("NestLoop");
        hint.addTable(null);
        hint.addTable("users");
        
        assertEquals(1, hint.getTables().size());
        assertEquals("users", hint.getTables().get(0));
    }

    @Test
    void testGetRawHint() {
        PerformanceHint hint = new PerformanceHint("MergeJoin");
        assertEquals("MergeJoin", hint.getRawHint());
    }

    @Test
    void testSetHintType() {
        PerformanceHint hint = new PerformanceHint("NestLoop");
        hint.setHintType("HashJoin");
        
        assertEquals("HashJoin", hint.getHintType());
    }

    @Test
    void testSetTables() {
        PerformanceHint hint = new PerformanceHint("MergeJoin");
        List<String> newTables = Arrays.asList("table1", "table2", "table3");
        hint.setTables(newTables);
        
        assertEquals(3, hint.getTables().size());
        assertEquals("table1", hint.getTables().get(0));
    }

    @Test
    void testSetTablesNullSafe() {
        PerformanceHint hint = new PerformanceHint("NestLoop");
        hint.setTables(null);
        
        assertNotNull(hint.getTables());
        assertEquals(0, hint.getTables().size());
    }

    @Test
    void testEqualsSameHint() {
        PerformanceHint hint1 = new PerformanceHint("NestLoop");
        PerformanceHint hint2 = new PerformanceHint("NestLoop");
        
        assertEquals(hint1, hint2);
    }

    @Test
    void testEqualsDifferentHintType() {
        PerformanceHint hint1 = new PerformanceHint("NestLoop");
        PerformanceHint hint2 = new PerformanceHint("HashJoin");
        
        assertNotEquals(hint1, hint2);
    }

    @Test
    void testEqualsDifferentTables() {
        PerformanceHint hint1 = new PerformanceHint("NestLoop", Arrays.asList("users"));
        PerformanceHint hint2 = new PerformanceHint("NestLoop", Arrays.asList("orders"));
        
        assertNotEquals(hint1, hint2);
    }

    @Test
    void testHashCodeConsistent() {
        PerformanceHint hint1 = new PerformanceHint("NestLoop");
        PerformanceHint hint2 = new PerformanceHint("NestLoop");
        
        assertEquals(hint1.hashCode(), hint2.hashCode());
    }

    @Test
    void testToString() {
        PerformanceHint hint = new PerformanceHint("MergeJoin", Arrays.asList("users", "orders"));
        String result = hint.toString();
        
        assertTrue(result.contains("MergeJoin"));
        assertTrue(result.contains("users"));
        assertTrue(result.contains("orders"));
    }
}

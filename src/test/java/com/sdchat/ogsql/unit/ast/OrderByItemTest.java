package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.OrderByItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for OrderByItem")
class OrderByItemTest {

    @Test
    @DisplayName("OrderByItem should set and get expression")
    void testExpression() {
        OrderByItem item = new OrderByItem();
        item.setExpression("created_at");
        assertEquals("created_at", item.getExpression());
    }

    @Test
    @DisplayName("OrderByItem should set and get ascending")
    void testAscending() {
        OrderByItem item = new OrderByItem();
        item.setAscending(true);
        assertTrue(item.isAscending());
        
        item.setAscending(false);
        assertFalse(item.isAscending());
    }

    @Test
    @DisplayName("OrderByItem should default to ascending")
    void testDefaultAscending() {
        OrderByItem item = new OrderByItem();
        assertTrue(item.isAscending());
    }

    @Test
    @DisplayName("OrderByItem constructor should set expression")
    void testConstructor() {
        OrderByItem item = new OrderByItem("created_at");
        assertEquals("created_at", item.getExpression());
    }
}

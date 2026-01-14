package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for DataSource")
class DataSourceTest {

    @Test
    @DisplayName("DataSource should set and get name")
    void testName() {
        DataSource source = new DataSource();
        source.setName("users");
        assertEquals("users", source.getName());
    }

    @Test
    @DisplayName("DataSource should set and get alias")
    void testAlias() {
        DataSource source = new DataSource();
        source.setAlias("u");
        assertEquals("u", source.getAlias());
    }

    @Test
    @DisplayName("DataSource should set and get joinType")
    void testJoinType() {
        DataSource source = new DataSource();
        source.setJoinType("LEFT");
        assertEquals("LEFT", source.getJoinType());
    }

    @Test
    @DisplayName("DataSource should set and get joinCondition")
    void testJoinCondition() {
        DataSource source = new DataSource();
        source.setJoinCondition("u.id = o.user_id");
        assertEquals("u.id = o.user_id", source.getJoinCondition());
    }

    @Test
    @DisplayName("DataSource constructor should set name")
    void testConstructor() {
        DataSource source = new DataSource("users");
        assertEquals("users", source.getName());
    }
}

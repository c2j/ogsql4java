package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.Constraint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for Constraint")
class ConstraintTest {

    @Test
    @DisplayName("Constraint should set and get type")
    void testType() {
        Constraint constraint = new Constraint();
        constraint.setType("PRIMARY_KEY");
        assertEquals("PRIMARY_KEY", constraint.getType());
    }

    @Test
    @DisplayName("Constraint should set and get name")
    void testName() {
        Constraint constraint = new Constraint();
        constraint.setName("pk_users_id");
        assertEquals("pk_users_id", constraint.getName());
    }

    @Test
    @DisplayName("Constraint should set and get definition")
    void testDefinition() {
        Constraint constraint = new Constraint();
        constraint.setDefinition("id");
        assertEquals("id", constraint.getDefinition());
    }

    @Test
    @DisplayName("Constraint constructor should set type")
    void testConstructor() {
        Constraint constraint = new Constraint("PRIMARY_KEY");
        assertEquals("PRIMARY_KEY", constraint.getType());
    }
}

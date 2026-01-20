package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.ProcedureParameter;
import com.sdchat.ogsql.ast.ProcedureParameter.ParameterMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProcedureParameter AST node class.
 * Tests constructors, getters, setters, and validation.
 */
@DisplayName("ProcedureParameter Tests")
class ProcedureParameterTest {

    @Test
    @DisplayName("Constructor with all parameters should set properties correctly")
    void testConstructorWithAllParameters() {
        ProcedureParameter param = new ProcedureParameter(
            "p1", ParameterMode.IN, "INTEGER", null, 0
        );

        assertEquals("p1", param.getName());
        assertEquals(ParameterMode.IN, param.getMode());
        assertEquals("INTEGER", param.getDataType());
        assertNull(param.getDefaultValue());
        assertEquals(0, param.getPosition());
    }

    @Test
    @DisplayName("Constructor with default value should set properties correctly")
    void testConstructorWithDefaultValue() {
        com.sdchat.ogsql.ast.ValueExpression defaultValue = new com.sdchat.ogsql.ast.ValueExpression("0");
        ProcedureParameter param = new ProcedureParameter(
            "p1", ParameterMode.IN, "INTEGER", defaultValue, 0
        );
        
        assertEquals("p1", param.getName());
        assertEquals(ParameterMode.IN, param.getMode());
        assertEquals("INTEGER", param.getDataType());
        assertNotNull(param.getDefaultValue());
        assertEquals("0", param.getDefaultValue().getLiteralValue());
    }

    @Test
    @DisplayName("Set name should update property")
    void testSetName() {
        ProcedureParameter param = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);
        param.setName("new_name");

        assertEquals("new_name", param.getName());
    }

    @Test
    @DisplayName("Set mode should update property")
    void testSetMode() {
        ProcedureParameter param = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);
        param.setMode(ParameterMode.OUT);

        assertEquals(ParameterMode.OUT, param.getMode());
    }

    @Test
    @DisplayName("Set data type should update property")
    void testSetDataType() {
        ProcedureParameter param = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);
        param.setDataType("VARCHAR(50)");

        assertEquals("VARCHAR(50)", param.getDataType());
    }

    @Test
    @DisplayName("Set default value should update property")
    void testSetDefaultValue() {
        ProcedureParameter param = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);
        com.sdchat.ogsql.ast.ValueExpression defaultValue = new com.sdchat.ogsql.ast.ValueExpression("10");
        param.setDefaultValue(defaultValue);

        assertEquals(defaultValue, param.getDefaultValue());
    }

    @Test
    @DisplayName("Set position should update property")
    void testSetPosition() {
        ProcedureParameter param = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);
        param.setPosition(5);

        assertEquals(5, param.getPosition());
    }

    @Test
    @DisplayName("Parameter modes should include all valid values")
    void testAllParameterModes() {
        ProcedureParameter.ParameterMode[] modes = ProcedureParameter.ParameterMode.values();

        assertEquals(4, modes.length);
        assertTrue(java.util.Arrays.asList(modes).contains(ProcedureParameter.ParameterMode.IN));
        assertTrue(java.util.Arrays.asList(modes).contains(ProcedureParameter.ParameterMode.OUT));
        assertTrue(java.util.Arrays.asList(modes).contains(ProcedureParameter.ParameterMode.INOUT));
        assertTrue(java.util.Arrays.asList(modes).contains(ProcedureParameter.ParameterMode.VARIADIC));
    }

    @Test
    @DisplayName("Equality should compare all fields")
    void testEquality() {
        ProcedureParameter p1 = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);
        ProcedureParameter p2 = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Inequality should fail when fields differ")
    void testInequality() {
        ProcedureParameter p1 = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", null, 0);
        ProcedureParameter p2 = new ProcedureParameter("p2", ParameterMode.IN, "INTEGER", null, 0);

        assertNotEquals(p1, p2);
    }
}

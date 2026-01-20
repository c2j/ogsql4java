package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.ProcedureSecurity;
import com.sdchat.ogsql.ast.ProcedureSecurity.AuthidType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProcedureSecurity AST node class.
 * Tests constructors, getters, setters, and validation.
 */
@DisplayName("ProcedureSecurity Tests")
class ProcedureSecurityTest {

    @Test
    @DisplayName("Default constructor should set default values")
    void testDefaultConstructor() {
        ProcedureSecurity security = new ProcedureSecurity();

        assertFalse(security.isDefiner());
        assertEquals(AuthidType.DEFINER, security.getAuthid());
        assertFalse(security.isSecurityInvoker());
    }

    @Test
    @DisplayName("Set definier should update property")
    void testSetDefiner() {
        ProcedureSecurity security = new ProcedureSecurity();
        security.setDefiner(true);

        assertTrue(security.isDefiner());
    }

    @Test
    @DisplayName("Set authid should update property")
    void testSetAuthid() {
        ProcedureSecurity security = new ProcedureSecurity();
        security.setAuthid(AuthidType.CURRENT_USER);

        assertEquals(AuthidType.CURRENT_USER, security.getAuthid());
    }

    @Test
    @DisplayName("Set security invoker should update property")
    void testSetSecurityInvoker() {
        ProcedureSecurity security = new ProcedureSecurity();
        security.setSecurityInvoker(true);

        assertTrue(security.isSecurityInvoker());
    }

    @Test
    @DisplayName("All authid types should be available")
    void testAllAuthidTypes() {
        ProcedureSecurity.AuthidType[] types = ProcedureSecurity.AuthidType.values();

        assertEquals(2, types.length);
        assertTrue(java.util.Arrays.asList(types).contains(ProcedureSecurity.AuthidType.DEFINER));
        assertTrue(java.util.Arrays.asList(types).contains(ProcedureSecurity.AuthidType.CURRENT_USER));
    }

    @Test
    @DisplayName("Mutual exclusivity: SECURITY INVOKER with AUTHID CURRENT_USER")
    void testMutualExclusivity() {
        ProcedureSecurity security = new ProcedureSecurity();
        security.setSecurityInvoker(true);
        security.setAuthid(AuthidType.CURRENT_USER);

        assertTrue(security.isSecurityInvoker());
        assertEquals(AuthidType.CURRENT_USER, security.getAuthid());
    }
}

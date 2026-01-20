package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.ProcedureBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProcedureBody AST node class.
 * Tests constructors, getters, setters, and validation.
 */
@DisplayName("ProcedureBody Tests")
class ProcedureBodyTest {

    @Test
    @DisplayName("Constructor with language and source code should set properties correctly")
    void testConstructorWithLanguageAndSource() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");

        assertEquals("plpgsql", body.getLanguage());
        assertEquals("$$ BEGIN NULL; END; $$", body.getSourceCode());
        assertNotNull(body.getDeclarations());
        assertTrue(body.getDeclarations().isEmpty());
        assertNotNull(body.getStatements());
        assertTrue(body.getStatements().isEmpty());
        assertNotNull(body.getExceptionHandlers());
        assertTrue(body.getExceptionHandlers().isEmpty());
    }

    @Test
    @DisplayName("Set language should update property")
    void testSetLanguage() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        body.setLanguage("oracle");

        assertEquals("oracle", body.getLanguage());
    }

    @Test
    @DisplayName("Set source code should update property")
    void testSetSourceCode() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        body.setSourceCode("$$ BEGIN NEW_VAR := 10; END; $$");

        assertEquals("$$ BEGIN NEW_VAR := 10; END; $$", body.getSourceCode());
    }

    @Test
    @DisplayName("Add declaration should add to list")
    void testAddDeclaration() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        com.sdchat.ogsql.ast.VariableDeclaration decl = new com.sdchat.ogsql.ast.VariableDeclaration("x", "INTEGER");

        body.addDeclaration(decl);

        assertEquals(1, body.getDeclarations().size());
    }

    @Test
    @DisplayName("Add statement should add to list")
    void testAddStatement() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        com.sdchat.ogsql.ast.Statement stmt = new com.sdchat.ogsql.ast.Statement() {};

        body.addStatement(stmt);

        assertEquals(1, body.getStatements().size());
    }

    @Test
    @DisplayName("Add exception handler should add to list")
    void testAddExceptionHandler() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        com.sdchat.ogsql.ast.ExceptionHandler handler = new com.sdchat.ogsql.ast.ExceptionHandler(
            java.util.Collections.singletonList("division_by_zero"),
            new java.util.ArrayList<>()
        );

        body.addExceptionHandler(handler);

        assertEquals(1, body.getExceptionHandlers().size());
    }

    @Test
    @DisplayName("Set declarations should replace list")
    void testSetDeclarations() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        java.util.List<com.sdchat.ogsql.ast.VariableDeclaration> decls = new java.util.ArrayList<>();
        decls.add(new com.sdchat.ogsql.ast.VariableDeclaration("x", "INTEGER"));
        decls.add(new com.sdchat.ogsql.ast.VariableDeclaration("y", "VARCHAR"));

        body.setDeclarations(decls);

        assertEquals(2, body.getDeclarations().size());
    }

    @Test
    @DisplayName("Set statements should replace list")
    void testSetStatements() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        java.util.List<com.sdchat.ogsql.ast.Statement> stmts = new java.util.ArrayList<>();
        stmts.add(new com.sdchat.ogsql.ast.Statement() {});
        stmts.add(new com.sdchat.ogsql.ast.Statement() {});

        body.setStatements(stmts);

        assertEquals(2, body.getStatements().size());
    }

    @Test
    @DisplayName("Set exception handlers should replace list")
    void testSetExceptionHandlers() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        java.util.List<com.sdchat.ogsql.ast.ExceptionHandler> handlers = new java.util.ArrayList<>();
        handlers.add(new com.sdchat.ogsql.ast.ExceptionHandler(
            java.util.Collections.singletonList("division_by_zero"),
            new java.util.ArrayList<>()
        ));

        body.setExceptionHandlers(handlers);

        assertEquals(1, body.getExceptionHandlers().size());
    }
}

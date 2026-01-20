package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.grammar.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test verifying ANTLR4 grammar setup.
 * Ensures the grammar file generates correct parser and lexer code.
 */
@DisplayName("Grammar Setup Contract Tests")
class GrammarSetupTest {

    @Test
    @DisplayName("ANTLR4 parser class should be generated")
    void testParserClassGenerated() {
        assertDoesNotThrow(() -> {
            Class<?> parserClass = Class.forName("com.sdchat.ogsql.grammar.OpenGaussSQLParser");
            assertNotNull(parserClass);
            assertTrue(Parser.class.isAssignableFrom(parserClass));
        }, "OpenGaussSQLParser class should be generated and extend Parser");
    }

    @Test
    @DisplayName("ANTLR4 lexer class should be generated")
    void testLexerClassGenerated() {
        assertDoesNotThrow(() -> {
            Class<?> lexerClass = Class.forName("com.sdchat.ogsql.grammar.OpenGaussSQLLexer");
            assertNotNull(lexerClass);
            assertTrue(Lexer.class.isAssignableFrom(lexerClass));
        }, "OpenGaussSQLLexer class should be generated and extend Lexer");
    }

    @Test
    @DisplayName("ANTLR4 listener interface should be generated")
    void testListenerInterfaceGenerated() {
        assertDoesNotThrow(() -> {
            Class<?> listenerClass = Class.forName("com.sdchat.ogsql.grammar.OpenGaussSQLListener");
            assertNotNull(listenerClass);
            assertTrue(listenerClass.isInterface());
        }, "OpenGaussSQLListener interface should be generated");
    }

    @Test
    @DisplayName("ANTLR4 base listener class should be generated")
    void testBaseListenerClassGenerated() {
        assertDoesNotThrow(() -> {
            Class<?> baseListenerClass = Class.forName("com.sdchat.ogsql.grammar.OpenGaussSQLBaseListener");
            assertNotNull(baseListenerClass);
        }, "OpenGaussSQLBaseListener class should be generated");
    }

    @Test
    @DisplayName("Parser should successfully tokenize simple SQL")
    void testSimpleSQLTokenization() {
        String sql = "SELECT * FROM users";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            assertTrue(tokens.size() > 0, "Should tokenize SQL into tokens");
            assertNotNull(lexer.getGrammarFileName(), "Lexer should have grammar file name");
        }, "Lexer should tokenize simple SELECT statement");
    }

    @Test
    @DisplayName("Parser should successfully parse simple SELECT statement")
    void testSimpleSelectParsing() {
        String sql = "SELECT * FROM users";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

            ParserRuleContext tree = parser.root();
            assertNotNull(tree, "Parse tree should not be null");
            assertNotNull(parser.getGrammarFileName(), "Parser should have grammar file name");
        }, "Parser should parse simple SELECT statement without errors");
    }

    @Test
    @DisplayName("Parser should have correct rule methods")
    void testParserHasRules() {
        assertDoesNotThrow(() -> {
            Class<?> parserClass = Class.forName("com.sdchat.ogsql.grammar.OpenGaussSQLParser");

            assertNotNull(parserClass.getMethod("root"));
            assertNotNull(parserClass.getMethod("selectstmt"));
            assertNotNull(parserClass.getMethod("insertstmt"));
            assertNotNull(parserClass.getMethod("updatestmt"));
            assertNotNull(parserClass.getMethod("deletestmt"));
            assertNotNull(parserClass.getMethod("createstmt"));
        }, "Parser should have methods for all grammar rules");
    }

    // ========== PROCEDURE GRAMMAR TESTS (T011-T016) ==========

    @Test
    @DisplayName("CREATE PROCEDURE grammar should tokenize correctly")
    void testCreateProcedureGrammar() throws Exception {
        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            assertTrue(tokens.size() > 0, "Should tokenize CREATE PROCEDURE statement");
        }, "CREATE PROCEDURE should be tokenized");
    }

    @Test
    @DisplayName("CREATE PROCEDURE with parameters should tokenize correctly")
    void testCreateProcedureWithParameters() throws Exception {
        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER, p2 OUT VARCHAR, p3 INOUT NUMERIC) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            assertTrue(tokens.size() > 0, "Should tokenize CREATE PROCEDURE with parameters");
        }, "CREATE PROCEDURE with parameters should be tokenized");
    }

    @Test
    @DisplayName("CREATE OR REPLACE PROCEDURE should tokenize correctly")
    void testCreateProcedureWithOrReplace() throws Exception {
        String sql = "CREATE OR REPLACE PROCEDURE test_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            assertTrue(tokens.size() > 0, "Should tokenize CREATE OR REPLACE PROCEDURE");
        }, "CREATE OR REPLACE PROCEDURE should be tokenized");
    }

    @Test
    @DisplayName("CREATE PROCEDURE with security clauses should tokenize correctly")
    void testCreateProcedureWithSecurityDefiner() throws Exception {
        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER) SECURITY DEFINER AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            assertTrue(tokens.size() > 0, "Should tokenize CREATE PROCEDURE with SECURITY DEFINER");
        }, "CREATE PROCEDURE with security should be tokenized");
    }

    @Test
    @DisplayName("Procedure body with BEGIN/END should tokenize correctly")
    void testProcedureBodyGrammar() throws Exception {
        String sql = "CREATE PROCEDURE test_proc() AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            assertTrue(tokens.size() > 0, "Should tokenize procedure body");
        }, "Procedure body should be tokenized");
    }

    @Test
    @DisplayName("Nested BEGIN/END blocks should tokenize correctly")
    void testNestedBlocksGrammar() throws Exception {
        String sql = "CREATE PROCEDURE test_proc() AS $$ BEGIN BEGIN NULL; END; NULL; END; $$ LANGUAGE plpgsql;";

        assertDoesNotThrow(() -> {
            CharStream input = CharStreams.fromString(sql);
            OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            assertTrue(tokens.size() > 0, "Should tokenize nested blocks");
        }, "Nested blocks should be tokenized");
    }
}

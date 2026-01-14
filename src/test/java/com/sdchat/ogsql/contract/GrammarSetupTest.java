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
}

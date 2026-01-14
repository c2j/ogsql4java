package com.sdchat.ogsql.unit.grammar;

import com.sdchat.ogsql.grammar.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for SELECT grammar rules")
class SelectGrammarTest {

    @Test
    @DisplayName("Should parse simple SELECT statement")
    void testSimpleSelect() {
        String sql = "SELECT * FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse simple SELECT");
    }

    @Test
    @DisplayName("Should parse SELECT with WHERE clause")
    void testSelectWithWhere() {
        String sql = "SELECT id FROM users WHERE id = 1";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse SELECT with WHERE");
    }

    @Test
    @DisplayName("Should parse SELECT with ORDER BY")
    void testSelectWithOrderBy() {
        String sql = "SELECT * FROM users ORDER BY id";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse SELECT with ORDER BY");
    }

    @Test
    @DisplayName("Should parse SELECT with LIMIT")
    void testSelectWithLimit() {
        String sql = "SELECT * FROM users LIMIT 10";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse SELECT with LIMIT");
    }

    @Test
    @DisplayName("Should parse SELECT with DISTINCT")
    void testSelectWithDistinct() {
        String sql = "SELECT DISTINCT name FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse SELECT with DISTINCT");
    }
}

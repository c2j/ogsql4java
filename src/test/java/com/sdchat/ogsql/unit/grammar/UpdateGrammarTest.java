package com.sdchat.ogsql.unit.grammar;

import com.sdchat.ogsql.grammar.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for UPDATE grammar rules")
class UpdateGrammarTest {

    @Test
    @DisplayName("Should parse simple UPDATE statement")
    void testSimpleUpdate() {
        String sql = "UPDATE users SET name = 'Jane'";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse simple UPDATE");
    }

    @Test
    @DisplayName("Should parse UPDATE with WHERE clause")
    void testUpdateWithWhere() {
        String sql = "UPDATE users SET name = 'Jane' WHERE id = 1";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse UPDATE with WHERE");
    }

    @Test
    @DisplayName("Should parse UPDATE with multiple SET clauses")
    void testUpdateMultipleSet() {
        String sql = "UPDATE users SET name = 'Jane', active = true WHERE id = 1";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse UPDATE with multiple SET");
    }
}

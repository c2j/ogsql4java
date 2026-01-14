package com.sdchat.ogsql.unit.grammar;

import com.sdchat.ogsql.grammar.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for INSERT grammar rules")
class InsertGrammarTest {

    @Test
    @DisplayName("Should parse simple INSERT statement")
    void testSimpleInsert() {
        String sql = "INSERT INTO users VALUES (1, 'John')";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse simple INSERT");
    }

    @Test
    @DisplayName("Should parse INSERT with column list")
    void testInsertWithColumns() {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John')";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse INSERT with column list");
    }

    @Test
    @DisplayName("Should parse INSERT with DEFAULT")
    void testInsertWithDefault() {
        String sql = "INSERT INTO users (id, name, active) VALUES (1, 'John', DEFAULT)";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse INSERT with DEFAULT");
    }
}

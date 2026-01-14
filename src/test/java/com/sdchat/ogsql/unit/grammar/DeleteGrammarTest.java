package com.sdchat.ogsql.unit.grammar;

import com.sdchat.ogsql.grammar.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for DELETE grammar rules")
class DeleteGrammarTest {

    @Test
    @DisplayName("Should parse simple DELETE statement")
    void testSimpleDelete() {
        String sql = "DELETE FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse simple DELETE");
    }

    @Test
    @DisplayName("Should parse DELETE with WHERE clause")
    void testDeleteWithWhere() {
        String sql = "DELETE FROM users WHERE id = 1";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse DELETE with WHERE");
    }
}

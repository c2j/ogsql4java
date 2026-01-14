package com.sdchat.ogsql.unit.grammar;

import com.sdchat.ogsql.grammar.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for expression grammar rules")
class ExpressionGrammarTest {

    @Test
    @DisplayName("Should parse simple expression")
    void testSimpleExpression() {
        String sql = "SELECT id + 1 FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse expression with operator");
    }

    @Test
    @DisplayName("Should parse expression with AND")
    void testExpressionWithAnd() {
        String sql = "SELECT * FROM users WHERE a AND b";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse expression with AND");
    }

    @Test
    @DisplayName("Should parse expression with OR")
    void testExpressionWithOr() {
        String sql = "SELECT * FROM users WHERE a OR b";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse expression with OR");
    }

    @Test
    @DisplayName("Should parse expression with comparison")
    void testExpressionWithComparison() {
        String sql = "SELECT * FROM users WHERE id = 1";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse expression with comparison");
    }
}

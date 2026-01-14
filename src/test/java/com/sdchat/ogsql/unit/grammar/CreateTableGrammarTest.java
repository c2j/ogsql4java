package com.sdchat.ogsql.unit.grammar;

import com.sdchat.ogsql.grammar.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for CREATE TABLE grammar rules")
class CreateTableGrammarTest {

    @Test
    @DisplayName("Should parse simple CREATE TABLE")
    void testSimpleCreateTable() {
        String sql = "CREATE TABLE users (id INT, name VARCHAR(255))";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse simple CREATE TABLE");
    }

    @Test
    @DisplayName("Should parse CREATE TABLE with constraints")
    void testCreateTableWithConstraints() {
        String sql = "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255) NOT NULL)";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse CREATE TABLE with constraints");
    }

    @Test
    @DisplayName("Should parse CREATE TABLE with DEFAULT")
    void testCreateTableWithDefault() {
        String sql = "CREATE TABLE users (id INT, active BOOLEAN DEFAULT true)";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        assertDoesNotThrow(() -> parser.root(), "Should parse CREATE TABLE with DEFAULT");
    }
}

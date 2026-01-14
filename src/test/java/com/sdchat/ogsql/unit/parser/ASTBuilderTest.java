package com.sdchat.ogsql.unit.parser;

import com.sdchat.ogsql.parser.ASTBuilder;
import com.sdchat.ogsql.grammar.*;
import com.sdchat.ogsql.ast.*;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for ASTBuilder visitor.
 */
class ASTBuilderTest {

    @Test
    void testVisitSelectStatement() {
        String sql = "SELECT id FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        ASTBuilder visitor = new ASTBuilder();
        SQLStatement statement = visitor.visit(parser.selectstmt());
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
    }

    @Test
    void testVisitInsertStatement() {
        String sql = "INSERT INTO users VALUES (1)";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        ASTBuilder visitor = new ASTBuilder();
        SQLStatement statement = visitor.visit(parser.insertstmt());
        
        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);
    }

    @Test
    void testVisitUpdateStatement() {
        String sql = "UPDATE users SET name = 'John'";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        ASTBuilder visitor = new ASTBuilder();
        SQLStatement statement = visitor.visit(parser.updatestmt());
        
        assertNotNull(statement);
        assertInstanceOf(UpdateStatement.class, statement);
    }

    @Test
    void testVisitDeleteStatement() {
        String sql = "DELETE FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        ASTBuilder visitor = new ASTBuilder();
        SQLStatement statement = visitor.visit(parser.deletestmt());
        
        assertNotNull(statement);
        assertInstanceOf(DeleteStatement.class, statement);
    }

    @Test
    void testVisitCreateStatement() {
        String sql = "CREATE TABLE users (id INT)";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        ASTBuilder visitor = new ASTBuilder();
        SQLStatement statement = visitor.visit(parser.createstmt());
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
    }

    @Test
    void testVisitDropStatement() {
        String sql = "DROP TABLE users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        ASTBuilder visitor = new ASTBuilder();
        SQLStatement statement = visitor.visit(parser.dropstmt());
        
        assertNotNull(statement);
        assertInstanceOf(DropStatement.class, statement);
    }
}

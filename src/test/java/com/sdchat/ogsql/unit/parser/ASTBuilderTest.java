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

    @Test
    void testDuplicateAliasNotAppended() {
        // Test P0 fix: Ensure subquery alias is not appended twice
        String sql = "SELECT * FROM (SELECT id FROM users) AS sub";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        ASTBuilder visitor = new ASTBuilder();
        SelectQuery query = (SelectQuery) visitor.visit(parser.selectstmt());

        assertNotNull(query);
        String fromClause = query.getFromClause();
        // AS should be stripped from alias
        assertFalse(fromClause.contains("AS sub"), "AS should be stripped from alias");
        assertTrue(fromClause.contains(" sub"), "Alias should be present");

        // Note: Subquery DataSource creation would require recursive visit() call
        // For now, we only create DataSource for simple table references, not subqueries
        assertNotNull(query.getDataSources());
        // dataSources may be empty for subqueries until recursive visit is implemented
    }

    @Test
    void testComplexJoinConditionParsing() {
        // Test P1 fix: Complex JOIN conditions with commas in IN clauses
        String sql = "SELECT * FROM orders o " +
                     "JOIN users u ON u.id IN (1,2,3) " +
                     "JOIN products p ON p.id = o.product_id";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        ASTBuilder visitor = new ASTBuilder();
        SelectQuery query = (SelectQuery) visitor.visit(parser.selectstmt());

        assertNotNull(query);

        // Verify all three tables are in dataSources
        assertNotNull(query.getDataSources());
        assertEquals(3, query.getDataSources().size());
    }

    @Test
    void testNestedParenthesesInJoinCondition() {
        // Test P1 fix: Nested parentheses in ON condition
        String sql = "SELECT * FROM table1 t1 " +
                     "JOIN table2 t2 ON (t1.id = t2.id OR (t1.status = 'active' AND t2.type = 'premium'))";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        ASTBuilder visitor = new ASTBuilder();
        SelectQuery query = (SelectQuery) visitor.visit(parser.selectstmt());

        assertNotNull(query);

        // Verify both tables are extracted
        assertNotNull(query.getDataSources());
        assertEquals(2, query.getDataSources().size());
    }

    @Test
    void testDataSourcePopulatedForSimpleTable() {
        // Test P2: Verify DataSource objects are created for simple table references
        String sql = "SELECT * FROM users u";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        ASTBuilder visitor = new ASTBuilder();
        SelectQuery query = (SelectQuery) visitor.visit(parser.selectstmt());

        assertNotNull(query);
        assertNotNull(query.getDataSources());
        assertEquals(1, query.getDataSources().size());

        DataSource dataSource = query.getDataSources().get(0);
        assertEquals("users", dataSource.getName());
        assertEquals("u", dataSource.getAlias());
    }
}

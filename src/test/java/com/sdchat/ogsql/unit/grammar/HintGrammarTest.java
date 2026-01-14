package com.sdchat.ogsql.unit.grammar;

import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import com.sdchat.ogsql.grammar.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for hint grammar rules.
 */
class HintGrammarTest {

    @Test
    void testParseNestLoopHint() {
        String sql = "SELECT /*+ NestLoop(users) */ id FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        OpenGaussSQLParser.SelectstmtContext ctx = parser.selectstmt();
        assertNotNull(ctx);
        assertNotNull(ctx.hintComment());
    }

    @Test
    void testParseMergeJoinHint() {
        String sql = "SELECT /*+ MergeJoin(users, orders) */ * FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        OpenGaussSQLParser.SelectstmtContext ctx = parser.selectstmt();
        assertNotNull(ctx);
        assertNotNull(ctx.hintComment());
    }

    @Test
    void testParseHashJoinHint() {
        String sql = "SELECT /*+ HashJoin(products) */ name FROM products";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        OpenGaussSQLParser.SelectstmtContext ctx = parser.selectstmt();
        assertNotNull(ctx);
        assertNotNull(ctx.hintComment());
    }

    @Test
    void testParseMultipleHints() {
        String sql = "SELECT /*+ NestLoop(users) MergeJoin(orders) */ id FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        OpenGaussSQLParser.SelectstmtContext ctx = parser.selectstmt();
        assertNotNull(ctx);
        assertNotNull(ctx.hintComment());
    }

    @Test
    void testParseSelectWithoutHints() {
        String sql = "SELECT id FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        OpenGaussSQLParser.SelectstmtContext ctx = parser.selectstmt();
        assertNotNull(ctx);
        assertNotNull(ctx.hintComment());
        assertNull(ctx.hintComment().HINT_COMMENT());
    }

    @Test
    void testParseHintWithMultipleTables() {
        String sql = "SELECT /*+ HashJoin(users, orders, products) */ * FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        OpenGaussSQLParser.SelectstmtContext ctx = parser.selectstmt();
        assertNotNull(ctx);
        assertNotNull(ctx.hintComment());
    }

    @Test
    void testParseHintWithEmptyTableList() {
        String sql = "SELECT /*+ NestLoop() */ id FROM users";
        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);
        
        OpenGaussSQLParser.SelectstmtContext ctx = parser.selectstmt();
        assertNotNull(ctx);
        assertNotNull(ctx.hintComment());
    }
}
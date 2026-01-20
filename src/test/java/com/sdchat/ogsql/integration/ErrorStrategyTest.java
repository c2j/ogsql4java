package com.sdchat.ogsql.integration;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.ErrorStrategy;
import com.sdchat.ogsql.exception.ParseException;
import com.sdchat.ogsql.ast.SQLStatement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for error strategy configuration.
 */
class ErrorStrategyTest {

    @Test
    void testDefaultErrorStrategy() throws Exception {
        String sql = "SELECT id FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement,
            "Parser should use default error strategy by default");
    }

    @Test
    void testDefaultStrategyCollectsErrors() {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql),
            "Default error strategy should throw exception on error");
    }

    @Test
    void testBailErrorStrategy() throws Exception {
        String sql = "SELECT id FROM users";
        SQLParser parser = new SQLParser();
        parser.setErrorStrategy(ErrorStrategy.BAIL);
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement,
            "Bail error strategy should parse valid SQL");
    }

    @Test
    void testBailStrategyFailsFast() {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        parser.setErrorStrategy(ErrorStrategy.BAIL);
        
        assertThrows(ParseException.class, () -> parser.parse(sql),
            "Bail error strategy should throw exception on first error");
    }

    @Test
    void testSwitchingErrorStrategies() throws Exception {
        String validSql = "SELECT id FROM users";
        String invalidSql = "SELEC id FROM users";
        
        SQLParser parser1 = new SQLParser();
        SQLParser parser2 = new SQLParser();
        parser2.setErrorStrategy(ErrorStrategy.BAIL);
        
        SQLStatement statement1 = parser1.parse(validSql);
        assertNotNull(statement1, "Default strategy works for valid SQL");
        
        SQLStatement statement2 = parser2.parse(validSql);
        assertNotNull(statement2, "Bail strategy works for valid SQL");
        
        assertThrows(ParseException.class, () -> parser1.parse(invalidSql));
        assertThrows(ParseException.class, () -> parser2.parse(invalidSql));
    }
}

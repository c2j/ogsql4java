package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for CREATE TABLE statement parsing.
 */
class CreateTableParseTest {

    @Test
    void testSimpleCreateTable() throws ParseException {
        String sql = "CREATE TABLE users (id INT)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
    }

    @Test
    void testCreateTableWithMultipleColumns() throws ParseException {
        String sql = "CREATE TABLE users (id INT, name VARCHAR(255))";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
    }

    @Test
    void testCreateTableWithConstraints() throws ParseException {
        String sql = "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255) NOT NULL)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
    }

    @Test
    void testCreateTableWithDefault() throws ParseException {
        String sql = "CREATE TABLE users (created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
    }
}
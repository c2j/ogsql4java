package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.InsertStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for INSERT statement parsing.
 */
class InsertParseTest {

    @Test
    void testSimpleInsert() throws ParseException {
        String sql = "INSERT INTO users VALUES (1, 'John')";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);
    }

    @Test
    void testInsertWithColumns() throws ParseException {
        String sql = "INSERT INTO users (id, name) VALUES (1, 'John')";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);
    }

    @Test
    void testInsertMultipleRows() throws ParseException {
        String sql = "INSERT INTO users VALUES (1, 'John'), (2, 'Jane')";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);
    }
}
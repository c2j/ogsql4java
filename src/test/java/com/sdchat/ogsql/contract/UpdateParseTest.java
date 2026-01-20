package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.UpdateStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for UPDATE statement parsing.
 */
class UpdateParseTest {

    @Test
    void testSimpleUpdate() throws ParseException {
        String sql = "UPDATE users SET name = 'Jane'";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(UpdateStatement.class, statement);
    }

    @Test
    void testUpdateWithWhere() throws ParseException {
        String sql = "UPDATE users SET name = 'Jane' WHERE id = 1";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(UpdateStatement.class, statement);
    }

    @Test
    void testUpdateMultipleColumns() throws ParseException {
        String sql = "UPDATE users SET name = 'Jane', email = 'jane@example.com'";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(UpdateStatement.class, statement);
    }
}
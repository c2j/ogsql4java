package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for SELECT statement parsing.
 */
class SelectParseTest {

    @Test
    void testSimpleSelect() throws ParseException {
        String sql = "SELECT id, name FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
        
        SelectQuery select = (SelectQuery) statement;
        String fromClause = select.getFromClause();
        assertNotNull(fromClause, "From clause should not be null");
    }

    @Test
    void testSelectWithWhereClause() throws ParseException {
        String sql = "SELECT id FROM users WHERE active = true";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
    }

    @Test
    void testSelectWithDistinct() throws ParseException {
        String sql = "SELECT DISTINCT name FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
    }

    @Test
    void testSelectWithOrderBy() throws ParseException {
        String sql = "SELECT id FROM users ORDER BY name";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
    }

    @Test
    void testSelectWithLimit() throws ParseException {
        String sql = "SELECT id FROM users LIMIT 10";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SelectQuery.class, statement);
    }
}
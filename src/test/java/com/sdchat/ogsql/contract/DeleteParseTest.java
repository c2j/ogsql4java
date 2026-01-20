package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.DeleteStatement;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for DELETE statement parsing.
 */
class DeleteParseTest {

    @Test
    void testSimpleDelete() throws ParseException {
        String sql = "DELETE FROM users";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(DeleteStatement.class, statement);
    }

    @Test
    void testDeleteWithWhere() throws ParseException {
        String sql = "DELETE FROM users WHERE id = 1";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(DeleteStatement.class, statement);
    }
}
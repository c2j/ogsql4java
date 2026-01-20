package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SyntaxErrorTest {

    @Test
    void testInvalidKeywordThrowsException() {
        String sql = "SELEC id FROM users";
        SQLParser parser = new SQLParser();
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }

    @Test
    void testIncompleteStatementReturnsStatement() {
        String sql = "SELECT id FROM";
        SQLParser parser = new SQLParser();
        
        com.sdchat.ogsql.ast.SQLStatement statement = parser.parse(sql);
        assertNotNull(statement, "Should return a statement for simple SELECT");
        assertInstanceOf(com.sdchat.ogsql.ast.SelectQuery.class, statement);
    }

    @Test
    void testEmptyStringThrowsParseException() {
        String sql = "";
        SQLParser parser = new SQLParser();
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }
}

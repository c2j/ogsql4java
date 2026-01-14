package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for invalid hint syntax.
 */
class InvalidHintTest {

    @Test
    void testParseSelectWithInvalidHintType() {
        String sql = "SELECT /*+ InvalidHint(users) */ id FROM users";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }

    @Test
    void testParseSelectWithUnterminatedHintComment() {
        String sql = "SELECT /*+ NestLoop(users) id FROM users";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }

    @Test
    void testParseSelectWithHintMissingClosingParen() throws Exception {
        String sql = "SELECT /*+ NestLoop(users */ id FROM users";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }

    @Test
    void testParseSelectWithHintInvalidTableList() {
        String sql = "SELECT /*+ MergeJoin() */ id FROM users";
        SQLParser parser = new SQLParser();
        
        assertThrows(ParseException.class, () -> parser.parse(sql));
    }
}

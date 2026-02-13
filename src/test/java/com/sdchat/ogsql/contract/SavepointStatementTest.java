package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SavepointStatement;
import com.sdchat.ogsql.ast.ReleaseSavepointStatement;
import com.sdchat.ogsql.ast.RollbackToSavepointStatement;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for SAVEPOINT, RELEASE SAVEPOINT, and ROLLBACK TO SAVEPOINT statements.
 */
class SavepointStatementTest {

    @Test
    void testParseSimpleSavepoint() throws ParseException {
        String sql = "SAVEPOINT my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SavepointStatement.class, statement);
        
        SavepointStatement savepoint = (SavepointStatement) statement;
        assertEquals("my_savepoint", savepoint.getIdentifier());
    }

    @Test
    void testParseSavepointWithQuotedIdentifier() throws ParseException {
        String sql = "SAVEPOINT \"My SavePoint\"";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SavepointStatement.class, statement);
        
        SavepointStatement savepoint = (SavepointStatement) statement;
        assertEquals("My SavePoint", savepoint.getIdentifier());
    }

    @Test
    void testParseReleaseSavepoint() throws ParseException {
        String sql = "RELEASE SAVEPOINT my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(ReleaseSavepointStatement.class, statement);
        
        ReleaseSavepointStatement release = (ReleaseSavepointStatement) statement;
        assertEquals("my_savepoint", release.getIdentifier());
    }

    @Test
    void testParseReleaseSavepointWithoutKeyword() throws ParseException {
        String sql = "RELEASE my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(ReleaseSavepointStatement.class, statement);
        
        ReleaseSavepointStatement release = (ReleaseSavepointStatement) statement;
        assertEquals("my_savepoint", release.getIdentifier());
    }

    @Test
    void testParseRollbackToSavepoint() throws ParseException {
        String sql = "ROLLBACK TO SAVEPOINT my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(RollbackToSavepointStatement.class, statement);
        
        RollbackToSavepointStatement rollback = (RollbackToSavepointStatement) statement;
        assertEquals("my_savepoint", rollback.getIdentifier());
    }

    @Test
    void testParseRollbackToSavepointWithoutKeyword() throws ParseException {
        String sql = "ROLLBACK TO my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(RollbackToSavepointStatement.class, statement);
        
        RollbackToSavepointStatement rollback = (RollbackToSavepointStatement) statement;
        assertEquals("my_savepoint", rollback.getIdentifier());
    }

    @Test
    void testParseRollbackWorkToSavepoint() throws ParseException {
        String sql = "ROLLBACK WORK TO SAVEPOINT my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(RollbackToSavepointStatement.class, statement);
        
        RollbackToSavepointStatement rollback = (RollbackToSavepointStatement) statement;
        assertEquals("my_savepoint", rollback.getIdentifier());
    }

    @Test
    void testSavepointStatementType() throws ParseException {
        String sql = "SAVEPOINT my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertEquals(StatementType.SAVEPOINT, statement.getStatementType());
    }

    @Test
    void testReleaseSavepointStatementType() throws ParseException {
        String sql = "RELEASE SAVEPOINT my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertEquals(StatementType.RELEASE_SAVEPOINT, statement.getStatementType());
    }

    @Test
    void testRollbackToSavepointStatementType() throws ParseException {
        String sql = "ROLLBACK TO SAVEPOINT my_savepoint";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertEquals(StatementType.ROLLBACK_TO_SAVEPOINT, statement.getStatementType());
    }

    @Test
    void testSavepointWithSemicolon() throws ParseException {
        String sql = "SAVEPOINT my_savepoint;";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(SavepointStatement.class, statement);
        
        SavepointStatement savepoint = (SavepointStatement) statement;
        assertEquals("my_savepoint", savepoint.getIdentifier());
    }

    @Test
    void testReleaseSavepointWithSemicolon() throws ParseException {
        String sql = "RELEASE SAVEPOINT my_savepoint;";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(ReleaseSavepointStatement.class, statement);
        
        ReleaseSavepointStatement release = (ReleaseSavepointStatement) statement;
        assertEquals("my_savepoint", release.getIdentifier());
    }

    @Test
    void testRollbackToSavepointWithSemicolon() throws ParseException {
        String sql = "ROLLBACK TO SAVEPOINT my_savepoint;";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(RollbackToSavepointStatement.class, statement);
        
        RollbackToSavepointStatement rollback = (RollbackToSavepointStatement) statement;
        assertEquals("my_savepoint", rollback.getIdentifier());
    }
}

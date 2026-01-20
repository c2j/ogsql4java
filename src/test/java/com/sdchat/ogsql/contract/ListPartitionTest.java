package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateStatement;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for LIST partitioning parsing.
 * Tests the grammar contract for CREATE TABLE with LIST partitioning clauses.
 */
class ListPartitionTest {

    @Test
    void testListPartitioningBasic() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, region VARCHAR(50)) PARTITION BY LIST (region)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testListPartitioningWithPartitions() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, region VARCHAR(50)) " +
                    "PARTITION BY LIST (region) (" +
                    "PARTITION p_north VALUES IN ('North', 'Northeast')," +
                    "PARTITION p_south VALUES IN ('South', 'Southeast')" +
                    ")";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testListPartitioningSingleValue() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, status VARCHAR(20)) " +
                    "PARTITION BY LIST (status) (" +
                    "PARTITION p_active VALUES IN ('ACTIVE')" +
                    ")";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testListPartitioningMultipleColumns() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, region VARCHAR(50), category VARCHAR(50)) " +
                    "PARTITION BY LIST (region, category)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testListPartitioningWithNumbers() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, dept_no INT) " +
                    "PARTITION BY LIST (dept_no) (" +
                    "PARTITION p_10 VALUES IN (10, 20)," +
                    "PARTITION p_30 VALUES IN (30, 40)" +
                    ")";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testListPartitioningErrorHandling() {
        String sql = "CREATE TABLE sales (id INT) PARTITION BY LIST";  // Missing partition key
        SQLParser parser = new SQLParser();
        
        ParseException exception = assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        });
        
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().length() > 0);
    }

    @Test
    void testListPartitioningWithDefaultPartition() throws ParseException {
        // Note: OpenGauss doesn't support DEFAULT partition for LIST, but testing basic syntax
        String sql = "CREATE TABLE sales (id INT, region VARCHAR(50)) " +
                    "PARTITION BY LIST (region) (" +
                    "PARTITION p_known VALUES IN ('North', 'South')" +
                    ")";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }
}
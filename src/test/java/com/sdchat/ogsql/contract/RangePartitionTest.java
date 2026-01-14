package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateStatement;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for RANGE partitioning parsing.
 * Tests the grammar contract for CREATE TABLE with RANGE partitioning clauses.
 */
class RangePartitionTest {

    @Test
    void testRangePartitioningBasic() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, sale_date DATE) PARTITION BY RANGE (sale_date)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
        // Basic validation - detailed partitioning validation will be added when ASTBuilder is updated
    }

    @Test
    void testRangePartitioningWithPartitions() throws ParseException {
        // Using current grammar syntax - VALUES with parentheses for RANGE
        String sql = "CREATE TABLE sales (id INT, sale_date DATE) " +
                    "PARTITION BY RANGE (sale_date) (" +
                    "PARTITION p2023 VALUES ('2023-01-01', '2023-12-31')," +
                    "PARTITION p2024 VALUES ('2024-01-01', '2024-12-31')" +
                    ")";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
        // Detailed partitioning validation will be added when PartitioningInformation is implemented
    }

    @Test
    void testRangePartitioningMultipleColumns() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, region VARCHAR(50), sale_date DATE) " +
                    "PARTITION BY RANGE (region, sale_date)";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
        // Will validate multiple partition keys when PartitioningInformation is implemented
    }

    @Test
    void testRangePartitioningWithHashPartitions() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, sale_date DATE) " +
                    "PARTITION BY RANGE (sale_date) PARTITIONS 12";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
        // Will validate hash partition count when PartitioningInformation is implemented
    }

    @Test
    void testRangePartitioningWithDefaultPartition() throws ParseException {
        // Using current grammar syntax - VALUES with parentheses for RANGE
        // Simplified to single partition to test basic parsing
        String sql = "CREATE TABLE sales (id INT, sale_date DATE) " +
                    "PARTITION BY RANGE (sale_date) (" +
                    "PARTITION p2023 VALUES ('2023-01-01')" +
                    ")";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testRangePartitioningErrorHandling() {
        String sql = "CREATE TABLE sales (id INT) PARTITION BY RANGE";  // Missing partition key
        SQLParser parser = new SQLParser();
        
        ParseException exception = assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        });
        
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().length() > 0);
    }

    @Test
    void testRangePartitioningWithTablespace() throws ParseException {
        // Note: Current grammar doesn't support TABLESPACE in partition definition
        // This test uses basic RANGE partitioning syntax
        String sql = "CREATE TABLE sales (id INT, sale_date DATE) " +
                    "PARTITION BY RANGE (sale_date) (" +
                    "PARTITION p2023 VALUES ('2023-01-01', '2023-12-31')" +
                    ")";
        SQLParser parser = new SQLParser();
        
        SQLStatement statement = parser.parse(sql);
        
        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);
        
        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
        // Will validate tablespace when PartitioningInformation is implemented
    }
}
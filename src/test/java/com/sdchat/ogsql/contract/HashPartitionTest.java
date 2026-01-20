package com.sdchat.ogsql.contract;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateStatement;
import com.sdchat.ogsql.ast.StatementType;
import com.sdchat.ogsql.exception.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract test for HASH partitioning parsing.
 * Tests CREATE TABLE with HASH partitioning clauses.
 */
class HashPartitionTest {

    @Test
    void testHashPartitioningBasic() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, customer_id INT) PARTITION BY HASH (customer_id);";

        SQLParser parser = new SQLParser();
        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);

        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testHashPartitioningMultipleColumns() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, customer_id INT, region VARCHAR(50)) " +
                     "PARTITION BY HASH (customer_id, region);";

        SQLParser parser = new SQLParser();
        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);

        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testHashPartitioningWithPartitionCount() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, customer_id INT) " +
                     "PARTITION BY HASH (customer_id) PARTITIONS 4;";

        SQLParser parser = new SQLParser();
        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);

        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testHashPartitioningLargePartitionCount() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, customer_id INT) " +
                     "PARTITION BY HASH (customer_id) PARTITIONS 64;";

        SQLParser parser = new SQLParser();
        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);

        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }

    @Test
    void testHashPartitioningErrorHandling() {
        String sql = "CREATE TABLE sales (id INT) PARTITION BY HASH";

        SQLParser parser = new SQLParser();

        ParseException exception = assertThrows(ParseException.class, () -> {
            parser.parse(sql);
        });

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().length() > 0);
    }

    @Test
    void testHashPartitioningSinglePartition() throws ParseException {
        String sql = "CREATE TABLE sales (id INT, customer_id INT) " +
                     "PARTITION BY HASH (customer_id) PARTITIONS 1";

        SQLParser parser = new SQLParser();
        SQLStatement statement = parser.parse(sql);

        assertNotNull(statement);
        assertInstanceOf(CreateStatement.class, statement);

        CreateStatement createStmt = (CreateStatement) statement;
        assertEquals(StatementType.CREATE_TABLE, createStmt.getStatementType());
    }
}

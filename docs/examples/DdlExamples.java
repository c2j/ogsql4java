package examples;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.CreateStatement;
import com.sdchat.ogsql.ast.AlterStatement;
import com.sdchat.ogsql.ast.DropStatement;

/**
 * DDL Statement Examples
 *
 * This class demonstrates CREATE, ALTER, and DROP operations
 * including partitioned tables and foreign tables.
 */
public class DdlExamples {

    public static void main(String[] args) {
        createTableExample();
        createPartitionedTableExample();
        alterTableExample();
        dropTableExample();
    }

    /**
     * Example 1: CREATE TABLE
     * Demonstrates basic table creation.
     */
    public static void createTableExample() {
        System.out.println("=== Example 1: CREATE TABLE ===");

        SQLParser parser = new SQLParser();
        String sql = "CREATE TABLE users (" +
                     "  id INT PRIMARY KEY," +
                     "  name VARCHAR(100) NOT NULL," +
                     "  email VARCHAR(255) UNIQUE," +
                     "  active BOOLEAN DEFAULT true" +
                     ")";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof CreateStatement) {
            CreateStatement create = (CreateStatement) statement;
            System.out.println("Table: " + create.getTableName());
            System.out.println("Columns: " + create.getColumns());
        }

        System.out.println();
    }

    /**
     * Example 2: CREATE PARTITIONED TABLE
     * Demonstrates RANGE partitioning for large datasets.
     */
    public static void createPartitionedTableExample() {
        System.out.println("=== Example 2: Partitioned Table ===");

        SQLParser parser = new SQLParser();
        String sql = "CREATE TABLE sales (" +
                     "  id BIGINT," +
                     "  sale_date DATE NOT NULL," +
                     "  region VARCHAR(50)," +
                     "  amount DECIMAL(12,2)" +
                     ") PARTITION BY RANGE (sale_date) (" +
                     "  PARTITION p2023 VALUES LESS THAN ('2024-01-01')," +
                     "  PARTITION p2024 VALUES LESS THAN ('2025-01-01')" +
                     ")";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof CreateStatement) {
            CreateStatement create = (CreateStatement) statement;
            System.out.println("Table: " + create.getTableName());
            System.out.println("Partition type: RANGE");
            System.out.println("Partition key: sale_date");
        }

        System.out.println();
    }

    /**
     * Example 3: ALTER TABLE
     * Demonstrates adding a column to existing table.
     */
    public static void alterTableExample() {
        System.out.println("=== Example 3: ALTER TABLE ===");

        SQLParser parser = new SQLParser();
        String sql = "ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof AlterStatement) {
            AlterStatement alter = (AlterStatement) statement;
            System.out.println("Table: " + alter.getTableName());
            System.out.println("Action: ADD COLUMN");
        }

        System.out.println();
    }

    /**
     * Example 4: DROP TABLE
     * Demonstrates table deletion.
     */
    public static void dropTableExample() {
        System.out.println("=== Example 4: DROP TABLE ===");

        SQLParser parser = new SQLParser();
        String sql = "DROP TABLE IF EXISTS temp_data";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof DropStatement) {
            DropStatement drop = (DropStatement) statement;
            System.out.println("Table: " + drop.getTableName());
            System.out.println("Options: IF EXISTS");
        }

        System.out.println();
    }
}

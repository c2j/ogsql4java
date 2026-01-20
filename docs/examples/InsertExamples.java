package examples;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.InsertStatement;

/**
 * INSERT Statement Examples
 *
 * This class demonstrates single-row and bulk INSERT operations.
 */
public class InsertExamples {

    public static void main(String[] args) {
        singleInsertExample();
        bulkInsertExample();
    }

    /**
     * Example 1: Single Row INSERT
     * Demonstrates inserting a single record.
     */
    public static void singleInsertExample() {
        System.out.println("=== Example 1: Single INSERT ===");

        SQLParser parser = new SQLParser();
        String sql = "INSERT INTO users (name, email, active) " +
                     "VALUES ('John Doe', 'john@example.com', true)";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof InsertStatement) {
            InsertStatement insert = (InsertStatement) statement;
            System.out.println("Table: " + insert.getTableName());
            System.out.println("Columns: " + insert.getColumns());
        }

        System.out.println();
    }

    /**
     * Example 2: Bulk INSERT
     * Demonstrates inserting multiple rows in a single statement.
     */
    public static void bulkInsertExample() {
        System.out.println("=== Example 2: Bulk INSERT ===");

        SQLParser parser = new SQLParser();
        String sql = "INSERT INTO users (name, email, active) VALUES " +
                     "('Jane Doe', 'jane@example.com', true), " +
                     "('Bob Smith', 'bob@example.com', false), " +
                     "('Alice Johnson', 'alice@example.com', true)";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof InsertStatement) {
            InsertStatement insert = (InsertStatement) statement;
            System.out.println("Table: " + insert.getTableName());
            System.out.println("Inserting 3 rows");
        }

        System.out.println();
    }
}

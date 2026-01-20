package examples;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.DeleteStatement;

/**
 * DELETE Statement Examples
 *
 * This class demonstrates DELETE operations with filtering.
 */
public class DeleteExamples {

    public static void main(String[] args) {
        simpleDeleteExample();
        conditionalDeleteExample();
    }

    /**
     * Example 1: Simple DELETE
     * Demonstrates deleting by primary key.
     */
    public static void simpleDeleteExample() {
        System.out.println("=== Example 1: Simple DELETE ===");

        SQLParser parser = new SQLParser();
        String sql = "DELETE FROM users WHERE id = 999";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof DeleteStatement) {
            DeleteStatement delete = (DeleteStatement) statement;
            System.out.println("Table: " + delete.getTableName());
            System.out.println("Deleting record with id = 999");
        }

        System.out.println();
    }

    /**
     * Example 2: Conditional DELETE
     * Demonstrates deleting based on condition.
     */
    public static void conditionalDeleteExample() {
        System.out.println("=== Example 2: Conditional DELETE ===");

        SQLParser parser = new SQLParser();
        String sql = "DELETE FROM logs WHERE created_at < '2023-01-01' " +
                     "AND level = 'debug'";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof DeleteStatement) {
            DeleteStatement delete = (DeleteStatement) statement;
            System.out.println("Table: " + delete.getTableName());
            System.out.println("Multiple WHERE conditions applied");
        }

        System.out.println();
    }
}

package examples;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.UpdateStatement;

/**
 * UPDATE Statement Examples
 *
 * This class demonstrates UPDATE operations with different WHERE conditions.
 */
public class UpdateExamples {

    public static void main(String[] args) {
        simpleUpdateExample();
        conditionalUpdateExample();
    }

    /**
     * Example 1: Simple UPDATE
     * Demonstrates updating a single field.
     */
    public static void simpleUpdateExample() {
        System.out.println("=== Example 1: Simple UPDATE ===");

        SQLParser parser = new SQLParser();
        String sql = "UPDATE users SET active = true WHERE id = 1";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof UpdateStatement) {
            UpdateStatement update = (UpdateStatement) statement;
            System.out.println("Table: " + update.getTableName());
            System.out.println("Updating field: active");
        }

        System.out.println();
    }

    /**
     * Example 2: Conditional UPDATE
     * Demonstrates updating based on complex WHERE condition.
     */
    public static void conditionalUpdateExample() {
        System.out.println("=== Example 2: Conditional UPDATE ===");

        SQLParser parser = new SQLParser();
        String sql = "UPDATE products SET price = price * 0.9 " +
                     "WHERE category = 'electronics' " +
                     "AND stock > 50";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof UpdateStatement) {
            UpdateStatement update = (UpdateStatement) statement;
            System.out.println("Table: " + update.getTableName());
            System.out.println("Complex WHERE condition applied");
        }

        System.out.println();
    }
}

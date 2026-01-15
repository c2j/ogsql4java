package examples;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.ast.Column;

/**
 * SELECT Statement Examples
 *
 * This class demonstrates various SELECT query patterns including simple queries,
 * joins, and subqueries.
 */
public class SelectExamples {

    public static void main(String[] args) {
        simpleSelectExample();
        joinExample();
        subqueryExample();
    }

    /**
     * Example 1: Simple SELECT Query
     * Demonstrates basic SELECT with WHERE clause.
     */
    public static void simpleSelectExample() {
        System.out.println("=== Example 1: Simple SELECT ===");

        SQLParser parser = new SQLParser();
        String sql = "SELECT id, name, email FROM users WHERE active = true";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof SelectQuery) {
            SelectQuery select = (SelectQuery) statement;
            System.out.println("Tables: " + select.getTables());
            System.out.println("Columns: " + select.getColumns());
        }

        System.out.println();
    }

    /**
     * Example 2: INNER JOIN
     * Demonstrates joining two tables.
     */
    public static void joinExample() {
        System.out.println("=== Example 2: INNER JOIN ===");

        SQLParser parser = new SQLParser();
        String sql = "SELECT u.name, o.order_date " +
                     "FROM users u " +
                     "INNER JOIN orders o ON u.id = o.user_id " +
                     "WHERE o.status = 'completed'";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof SelectQuery) {
            SelectQuery select = (SelectQuery) statement;
            System.out.println("Tables: " + select.getTables());
            System.out.println("Join type: INNER JOIN");
        }

        System.out.println();
    }

    /**
     * Example 3: Subquery
     * Demonstrates a nested SELECT statement.
     */
    public static void subqueryExample() {
        System.out.println("=== Example 3: Subquery ===");

        SQLParser parser = new SQLParser();
        String sql = "SELECT name, email FROM users " +
                     "WHERE id IN (SELECT user_id FROM orders WHERE total > 1000)";

        SQLStatement statement = parser.parse(sql);
        if (statement instanceof SelectQuery) {
            SelectQuery select = (SelectQuery) statement;
            System.out.println("Tables: " + select.getTables());
            System.out.println("Contains subquery: Yes");
        }

        System.out.println();
    }
}

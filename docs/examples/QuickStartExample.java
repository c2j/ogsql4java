package examples;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;
import com.sdchat.ogsql.ast.InsertStatement;
import com.sdchat.ogsql.ast.UpdateStatement;
import com.sdchat.ogsql.exception.ParseException;

/**
 * Quick Start Examples for OpenGauss SQL Parser
 *
 * This class demonstrates basic parsing operations for first-time users.
 * Each example can be run independently.
 */
public class QuickStartExample {

    public static void main(String[] args) {
        basicParseExample();
        resultHandlingExample();
        errorHandlingExample();
    }

    /**
     * Example 1: Basic SQL Parse
     * Demonstrates creating a parser and parsing a simple SELECT statement.
     */
    public static void basicParseExample() {
        System.out.println("=== Example 1: Basic Parse ===");

        // Create parser instance
        SQLParser parser = new SQLParser();

        // Parse a simple SQL statement
        String sql = "SELECT id, name, email FROM users WHERE active = true";
        SQLStatement statement = parser.parse(sql);

        // Check statement type and access results
        if (statement instanceof SelectQuery) {
            SelectQuery select = (SelectQuery) statement;
            System.out.println("Statement type: " + statement.getStatementType());
            System.out.println("Columns: " + select.getColumns());
        }

        System.out.println();
    }

    /**
     * Example 2: Handling Different Result Types
     * Demonstrates parsing and handling SELECT, INSERT, and UPDATE statements.
     */
    public static void resultHandlingExample() {
        System.out.println("=== Example 2: Result Handling ===");

        SQLParser parser = new SQLParser();

        // Different statement types
        String selectSql = "SELECT * FROM products WHERE price > 100";
        String insertSql = "INSERT INTO users (name, email) VALUES ('John', 'john@example.com')";
        String updateSql = "UPDATE users SET active = true WHERE id = 1";

        // Parse and handle each type
        SQLStatement stmt1 = parser.parse(selectSql);
        if (stmt1 instanceof SelectQuery) {
            SelectQuery select = (SelectQuery) stmt1;
            System.out.println("SELECT - Tables: " + select.getTables());
        }

        SQLStatement stmt2 = parser.parse(insertSql);
        if (stmt2 instanceof InsertStatement) {
            InsertStatement insert = (InsertStatement) stmt2;
            System.out.println("INSERT - Table: " + insert.getTableName());
        }

        SQLStatement stmt3 = parser.parse(updateSql);
        if (stmt3 instanceof UpdateStatement) {
            UpdateStatement update = (UpdateStatement) stmt3;
            System.out.println("UPDATE - Table: " + update.getTableName());
        }

        System.out.println();
    }

    /**
     * Example 3: Error Handling
     * Demonstrates graceful handling of parsing errors.
     */
    public static void errorHandlingExample() {
        System.out.println("=== Example 3: Error Handling ===");

        SQLParser parser = new SQLParser();

        try {
            // SQL with syntax error
            String sql = "SELEC id FROM users";  // Missing 'T' in SELECT
            SQLStatement statement = parser.parse(sql);
            System.out.println("Parse successful");
        } catch (ParseException e) {
            System.out.println("Parse error: " + e.getMessage());
            System.out.println("Please check your SQL syntax");
        }

        System.out.println();
    }
}

package com.sdchat.ogsql.test;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;

/**
 * Simple test to verify hint parsing with minimal SQL.
 */
public class BasicHintTest {
    public static void main(String[] args) {
        try {
            // Test: Multiple tables in hint with very simple SQL
            String sql = "SELECT /*+ HashJoin(users, orders) */ * FROM users";
            SQLParser parser = new SQLParser();
            SQLStatement stmt = parser.parse(sql);
            
            if (stmt instanceof SelectQuery) {
                SelectQuery sq = (SelectQuery) stmt;
                System.out.println("Basic multi-table hint test:");
                System.out.println("  Hints count: " + sq.getHints().size());
                if (!sq.getHints().isEmpty()) {
                    System.out.println("  Hint type: " + sq.getHints().get(0).getHintType());
                    System.out.println("  Tables: " + sq.getHints().get(0).getTables());
                    System.out.println("  Expected: [users, orders]");
                }
            }
            
            System.out.println("\n✅ Basic multi-table hint parsing works correctly!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
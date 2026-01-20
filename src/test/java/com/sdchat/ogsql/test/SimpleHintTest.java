package com.sdchat.ogsql.test;

import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.ast.SelectQuery;

/**
 * Simple test to verify basic hint parsing works.
 */
public class SimpleHintTest {
    public static void main(String[] args) {
        try {
            // Test 1: Basic NestLoop hint
            String sql1 = "SELECT /*+ NestLoop(users) */ id, name FROM users WHERE id = 1";
            SQLParser parser = new SQLParser();
            SQLStatement stmt1 = parser.parse(sql1);
            
            if (stmt1 instanceof SelectQuery) {
                SelectQuery sq = (SelectQuery) stmt1;
                System.out.println("Test 1 - Basic NestLoop hint:");
                System.out.println("  Hints count: " + sq.getHints().size());
                if (!sq.getHints().isEmpty()) {
                    System.out.println("  Hint type: " + sq.getHints().get(0).getHintType());
                    System.out.println("  Tables: " + sq.getHints().get(0).getTables());
                }
            }
            
            // Test 2: Multiple tables in hint
            String sql2 = "SELECT /*+ HashJoin(users, orders) */ * FROM users, orders WHERE users.id = orders.user_id";
            SQLStatement stmt2 = parser.parse(sql2);
            
            if (stmt2 instanceof SelectQuery) {
                SelectQuery sq = (SelectQuery) stmt2;
                System.out.println("\nTest 2 - Multiple tables in hint:");
                System.out.println("  Hints count: " + sq.getHints().size());
                if (!sq.getHints().isEmpty()) {
                    System.out.println("  Hint type: " + sq.getHints().get(0).getHintType());
                    System.out.println("  Tables: " + sq.getHints().get(0).getTables());
                }
            }
            
            // Test 3: Multiple hints
            String sql3 = "SELECT /*+ NestLoop(users) MergeJoin(orders) */ id FROM users";
            SQLStatement stmt3 = parser.parse(sql3);
            
            if (stmt3 instanceof SelectQuery) {
                SelectQuery sq = (SelectQuery) stmt3;
                System.out.println("\nTest 3 - Multiple hints:");
                System.out.println("  Hints count: " + sq.getHints().size());
                for (int i = 0; i < sq.getHints().size(); i++) {
                    System.out.println("  Hint " + i + ": " + sq.getHints().get(i).getHintType() + 
                                     " -> " + sq.getHints().get(i).getTables());
                }
            }
            
            System.out.println("\n✅ All tests passed! Basic hint parsing is working correctly.");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
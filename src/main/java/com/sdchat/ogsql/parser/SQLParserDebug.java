package com.sdchat.ogsql.parser;

import com.sdchat.ogsql.grammar.*;
import com.sdchat.ogsql.ast.*;
import org.antlr.v4.runtime.*;

public class SQLParserDebug {

    public static void main(String[] args) {
        SQLParser parser = new SQLParser();
        
        System.out.println("Testing SELECT:");
        try {
            SQLStatement stmt = parser.parse("SELECT * FROM users");
            System.out.println("Success! Statement type: " + (stmt != null ? stmt.getStatementType() : "null"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\nTesting CREATE:");
        try {
            SQLStatement stmt = parser.parse("CREATE TABLE users (id INT)");
            System.out.println("Success! Statement type: " + (stmt != null ? stmt.getStatementType() : "null"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

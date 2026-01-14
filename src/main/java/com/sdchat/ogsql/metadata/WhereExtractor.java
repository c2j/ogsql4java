package com.sdchat.ogsql.metadata;

import com.sdchat.ogsql.ast.ValueExpression;
import java.util.*;

/**
 * Utility class for extracting WHERE conditions from SQL expressions.
 * Provides specialized methods for analyzing WHERE clause structures.
 * 
 * Note: This implementation works with the current minimal AST structure.
 * It will be enhanced as the AST implementation becomes more complete.
 */
public class WhereExtractor {

    /**
     * Extracts all conditions from a WHERE expression.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @return List of extracted conditions
     */
    public static List<Condition> extractConditions(ValueExpression whereExpr) {
        if (whereExpr == null) {
            return Collections.emptyList();
        }
        
        // Current ValueExpression implementation is minimal
        // This would need to be enhanced when the AST is more complete
        
        return Collections.emptyList();
    }
    
    /**
     * Extracts equality conditions from a WHERE expression.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @return List of equality conditions
     */
    public static List<Condition> extractEqualityConditions(ValueExpression whereExpr) {
        List<Condition> allConditions = extractConditions(whereExpr);
        List<Condition> equalityConditions = new ArrayList<>();
        
        for (Condition condition : allConditions) {
            if (condition.isEquality()) {
                equalityConditions.add(condition);
            }
        }
        
        return equalityConditions;
    }
    
    /**
     * Extracts comparison conditions from a WHERE expression.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @return List of comparison conditions (<, >, <=, >=)
     */
    public static List<Condition> extractComparisonConditions(ValueExpression whereExpr) {
        List<Condition> allConditions = extractConditions(whereExpr);
        List<Condition> comparisonConditions = new ArrayList<>();
        
        for (Condition condition : allConditions) {
            if (condition.isComparison()) {
                comparisonConditions.add(condition);
            }
        }
        
        return comparisonConditions;
    }
    
    /**
     * Extracts conditions for a specific column.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @param columnName The column name to filter by
     * @return List of conditions for the specified column
     */
    public static List<Condition> extractConditionsForColumn(ValueExpression whereExpr, String columnName) {
        if (columnName == null || columnName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Condition> allConditions = extractConditions(whereExpr);
        List<Condition> columnConditions = new ArrayList<>();
        
        for (Condition condition : allConditions) {
            if (columnName.equals(condition.getColumnName())) {
                columnConditions.add(condition);
            }
        }
        
        return columnConditions;
    }
    
    /**
     * Extracts conditions for a specific table and column.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @param tableName The table name to filter by
     * @param columnName The column name to filter by
     * @return List of conditions for the specified table and column
     */
    public static List<Condition> extractConditionsForTableColumn(ValueExpression whereExpr, String tableName, String columnName) {
        if (columnName == null || columnName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Condition> allConditions = extractConditions(whereExpr);
        List<Condition> tableColumnConditions = new ArrayList<>();
        
        for (Condition condition : allConditions) {
            boolean tableMatch = tableName == null || tableName.equals(condition.getTableName());
            boolean columnMatch = columnName.equals(condition.getColumnName());
            
            if (tableMatch && columnMatch) {
                tableColumnConditions.add(condition);
            }
        }
        
        return tableColumnConditions;
    }
    
    /**
     * Finds the most restrictive condition for a column.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @param columnName The column name
     * @return The most restrictive condition, or null if none found
     */
    public static Condition findMostRestrictiveCondition(ValueExpression whereExpr, String columnName) {
        List<Condition> conditions = extractConditionsForColumn(whereExpr, columnName);
        
        if (conditions.isEmpty()) {
            return null;
        }
        
        // Priority: equality > comparison > other
        for (Condition condition : conditions) {
            if (condition.isEquality() && condition.hasSimpleValue()) {
                return condition;
            }
        }
        
        for (Condition condition : conditions) {
            if (condition.isComparison() && condition.hasSimpleValue()) {
                return condition;
            }
        }
        
        // Return the first condition if no equality or comparison found
        return conditions.get(0);
    }
    
    /**
     * Checks if a WHERE expression contains conditions for a specific column.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @param columnName The column name to check
     * @return true if conditions exist for the column
     */
    public static boolean hasConditionsForColumn(ValueExpression whereExpr, String columnName) {
        return !extractConditionsForColumn(whereExpr, columnName).isEmpty();
    }
    
    /**
     * Checks if a WHERE expression has any equality conditions.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @return true if equality conditions exist
     */
    public static boolean hasEqualityConditions(ValueExpression whereExpr) {
        return !extractEqualityConditions(whereExpr).isEmpty();
    }
    
    /**
     * Checks if a WHERE expression has any comparison conditions.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @return true if comparison conditions exist
     */
    public static boolean hasComparisonConditions(ValueExpression whereExpr) {
        return !extractComparisonConditions(whereExpr).isEmpty();
    }
    
    /**
     * Analyzes the complexity of a WHERE expression.
     * 
     * @param whereExpr The WHERE expression to analyze
     * @return Complexity level (SIMPLE, MODERATE, COMPLEX)
     */
    public static WhereComplexity analyzeComplexity(ValueExpression whereExpr) {
        if (whereExpr == null) {
            return WhereComplexity.SIMPLE;
        }
        
        // Current ValueExpression implementation is minimal
        // This would need to be enhanced when the AST is more complete
        
        return WhereComplexity.SIMPLE;
    }
    
    /**
     * Represents the complexity level of a WHERE clause.
     */
    public enum WhereComplexity {
        /**
         * Simple WHERE clause with single equality condition or no conditions.
         */
        SIMPLE,
        
        /**
         * Moderate WHERE clause with multiple simple conditions.
         */
        MODERATE,
        
        /**
         * Complex WHERE clause with nested conditions, subqueries, or complex expressions.
         */
        COMPLEX
    }
}
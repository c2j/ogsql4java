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

        List<Condition> conditions = new ArrayList<>();
        extractConditionsRecursive(whereExpr, conditions, null);
        return conditions;
    }

    /**
     * Recursively extracts conditions from a value expression.
     * 
     * @param expr The expression to analyze
     * @param conditions List to collect extracted conditions
     * @param tableName Default table name (can be null)
     */
    private static void extractConditionsRecursive(ValueExpression expr, List<Condition> conditions, String tableName) {
        if (expr == null) {
            return;
        }

        String operator = expr.getOperator();

        // Check if this is a comparison operation
        if (operator != null && isComparisonOperator(operator)) {
            String columnName = extractColumnName(expr.getLeftOperand());
            String value = extractLiteralValue(expr.getRightOperand());

            if (columnName != null) {
                Condition condition = new Condition(
                    columnName,
                    tableName,
                    operator,
                    value != null ? value : extractExpressionAsString(expr.getRightOperand()),
                    expr
                );
                conditions.add(condition);
            }
        } else {
            // Recursively process operands
            if (expr.getLeftOperand() != null) {
                extractConditionsRecursive(expr.getLeftOperand(), conditions, tableName);
            }
            if (expr.getRightOperand() != null) {
                extractConditionsRecursive(expr.getRightOperand(), conditions, tableName);
            }
        }
    }

    /**
     * Checks if an operator is a comparison operator.
     * 
     * @param operator The operator to check
     * @return true if it's a comparison operator
     */
    private static boolean isComparisonOperator(String operator) {
        if (operator == null) {
            return false;
        }
        switch (operator) {
            case "=":
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "<>":
            case "!=":
            case "LIKE":
            case "ILIKE":
                return true;
            default:
                return false;
        }
    }

    /**
     * Extracts column name from a value expression.
     * 
     * @param expr The expression to extract from
     * @return Column name, or null if not a column reference
     */
    private static String extractColumnName(ValueExpression expr) {
        if (expr == null) {
            return null;
        }
        String columnName = expr.getColumnName();
        if (columnName != null && !columnName.isEmpty()) {
            return columnName;
        }
        return null;
    }

    /**
     * Extracts literal value from a value expression.
     * 
     * @param expr The expression to extract from
     * @return Literal value as string, or null if not a literal
     */
    private static String extractLiteralValue(ValueExpression expr) {
        if (expr == null) {
            return null;
        }
        String literalValue = expr.getLiteralValue();
        if (literalValue != null && !literalValue.isEmpty()) {
            return literalValue;
        }
        return null;
    }

    /**
     * Converts a value expression to a string representation.
     * 
     * @param expr The expression to convert
     * @return String representation
     */
    private static String extractExpressionAsString(ValueExpression expr) {
        if (expr == null) {
            return null;
        }
        if (expr.getColumnName() != null) {
            return expr.getColumnName();
        }
        if (expr.getLiteralValue() != null) {
            return "'" + expr.getLiteralValue() + "'";
        }
        if (expr.getFunctionName() != null) {
            return expr.getFunctionName() + "(...)";
        }
        return buildOriginalExpression(expr);
    }

    /**
     * Builds the original expression string from a value expression.
     * 
     * @param expr The expression to build from
     * @return Original expression as string
     */
    private static String buildOriginalExpression(ValueExpression expr) {
        if (expr == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        if (expr.getLeftOperand() != null) {
            sb.append(buildOriginalExpression(expr.getLeftOperand()));
            sb.append(" ");
        }

        if (expr.getOperator() != null) {
            sb.append(expr.getOperator());
            sb.append(" ");
        }

        if (expr.getColumnName() != null) {
            sb.append(expr.getColumnName());
        } else if (expr.getLiteralValue() != null) {
            sb.append("'").append(expr.getLiteralValue()).append("'");
        } else if (expr.getFunctionName() != null) {
            sb.append(expr.getFunctionName()).append("(...)");
        } else if (expr.getRightOperand() != null) {
            sb.append(buildOriginalExpression(expr.getRightOperand()));
        }

        return sb.toString().trim();
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
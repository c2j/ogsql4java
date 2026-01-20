package com.sdchat.ogsql.metadata;

import com.sdchat.ogsql.ast.ValueExpression;
import java.util.Objects;

/**
 * Represents a WHERE condition in a SQL query.
 */
public class Condition {
    
    private final String columnName;
    private final String tableName;
    private final String operator;
    private final Object value;
    private final ValueExpression originalExpression;
    
    /**
     * Creates a new condition.
     * 
     * @param columnName The column name
     * @param tableName The table name (can be null)
     * @param operator The comparison operator
     * @param value The comparison value
     * @param originalExpression The original expression
     */
    public Condition(String columnName, String tableName, String operator, Object value, ValueExpression originalExpression) {
        if (columnName == null || columnName.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be null or empty");
        }
        if (operator == null || operator.trim().isEmpty()) {
            throw new IllegalArgumentException("Operator cannot be null or empty");
        }
        if (originalExpression == null) {
            throw new IllegalArgumentException("Original expression cannot be null");
        }
        
        this.columnName = columnName.trim();
        this.tableName = tableName != null ? tableName.trim() : null;
        this.operator = operator.trim().toUpperCase();
        this.value = value;
        this.originalExpression = originalExpression;
    }
    
    /**
     * Gets the column name.
     * 
     * @return The column name
     */
    public String getColumnName() {
        return columnName;
    }
    
    /**
     * Gets the table name.
     * 
     * @return The table name, or null if not specified
     */
    public String getTableName() {
        return tableName;
    }
    
    /**
     * Gets the comparison operator.
     * 
     * @return The operator (e.g., "=", "!=", "LIKE", "IN")
     */
    public String getOperator() {
        return operator;
    }
    
    /**
     * Gets the comparison value.
     * 
     * @return The value, or null if not a simple literal
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * Gets the original expression.
     * 
     * @return The original ValueExpression
     */
    public ValueExpression getOriginalExpression() {
        return originalExpression;
    }
    
    /**
     * Checks if this condition has a table qualifier.
     * 
     * @return true if table name is specified
     */
    public boolean hasTableQualifier() {
        return tableName != null && !tableName.isEmpty();
    }
    
    /**
     * Checks if this condition has a simple literal value.
     * 
     * @return true if value is a simple literal
     */
    public boolean hasSimpleValue() {
        return value != null;
    }
    
    /**
     * Gets the qualified column name (table.column or just column).
     * 
     * @return The qualified name
     */
    public String getQualifiedColumnName() {
        if (hasTableQualifier()) {
            return tableName + "." + columnName;
        }
        return columnName;
    }
    
    /**
     * Checks if this is an equality condition.
     * 
     * @return true if operator is "=" or "=="
     */
    public boolean isEquality() {
        return "=".equals(operator) || "==".equals(operator);
    }
    
    /**
     * Checks if this is an inequality condition.
     * 
     * @return true if operator is "!=" or "<>"
     */
    public boolean isInequality() {
        return "!=".equals(operator) || "<>".equals(operator);
    }
    
    /**
     * Checks if this is a comparison condition (<, >, <=, >=).
     * 
     * @return true if it's a comparison operator
     */
    public boolean isComparison() {
        return "<".equals(operator) || ">".equals(operator) ||
               "<=".equals(operator) || ">=".equals(operator);
    }
    
    /**
     * Checks if this is a LIKE condition.
     * 
     * @return true if operator is LIKE or NOT LIKE
     */
    public boolean isLike() {
        return "LIKE".equals(operator) || "NOT LIKE".equals(operator);
    }
    
    /**
     * Checks if this is an IN condition.
     * 
     * @return true if operator is IN or NOT IN
     */
    public boolean isIn() {
        return "IN".equals(operator) || "NOT IN".equals(operator);
    }
    
    /**
     * Checks if this is a BETWEEN condition.
     * 
     * @return true if operator is BETWEEN or NOT BETWEEN
     */
    public boolean isBetween() {
        return "BETWEEN".equals(operator) || "NOT BETWEEN".equals(operator);
    }
    
    /**
     * Checks if this is a NULL check condition.
     * 
     * @return true if operator contains "NULL"
     */
    public boolean isNullCheck() {
        return operator.contains("NULL");
    }
    
    /**
     * Checks if this is a negated condition (NOT).
     * 
     * @return true if operator starts with "NOT"
     */
    public boolean isNegated() {
        return operator.startsWith("NOT");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Condition that = (Condition) obj;
        return Objects.equals(columnName, that.columnName) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(operator, that.operator) &&
               Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(columnName, tableName, operator, value);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (hasTableQualifier()) {
            sb.append(tableName).append(".");
        }
        sb.append(columnName).append(" ").append(operator);
        if (hasSimpleValue()) {
            sb.append(" ").append(value);
        } else {
            sb.append(" [complex expression]");
        }
        return sb.toString();
    }
}
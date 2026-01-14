package com.sdchat.ogsql.metadata;

import java.util.Objects;

/**
 * Represents a function call in a SQL query.
 */
public class FunctionCall {
    
    private final String functionName;
    private final int argumentCount;
    private final boolean isAggregate;
    
    /**
     * Creates a new function call.
     * 
     * @param functionName The name of the function
     * @param argumentCount The number of arguments
     */
    public FunctionCall(String functionName, int argumentCount) {
        this(functionName, argumentCount, false);
    }
    
    /**
     * Creates a new function call.
     * 
     * @param functionName The name of the function
     * @param argumentCount The number of arguments
     * @param isAggregate Whether this is an aggregate function
     */
    public FunctionCall(String functionName, int argumentCount, boolean isAggregate) {
        if (functionName == null || functionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be null or empty");
        }
        if (argumentCount < 0) {
            throw new IllegalArgumentException("Argument count cannot be negative");
        }
        this.functionName = functionName.trim().toUpperCase();
        this.argumentCount = argumentCount;
        this.isAggregate = isAggregate || isAggregateFunction(functionName);
    }
    
    /**
     * Gets the function name.
     * 
     * @return The function name in uppercase
     */
    public String getFunctionName() {
        return functionName;
    }
    
    /**
     * Gets the number of arguments.
     * 
     * @return The argument count
     */
    public int getArgumentCount() {
        return argumentCount;
    }
    
    /**
     * Checks if this is an aggregate function.
     * 
     * @return true if this is an aggregate function
     */
    public boolean isAggregate() {
        return isAggregate;
    }
    
    /**
     * Checks if this is a scalar function.
     * 
     * @return true if this is a scalar (non-aggregate) function
     */
    public boolean isScalar() {
        return !isAggregate;
    }
    
    /**
     * Checks if this function has arguments.
     * 
     * @return true if the function has one or more arguments
     */
    public boolean hasArguments() {
        return argumentCount > 0;
    }
    
    /**
     * Checks if this function has no arguments.
     * 
     * @return true if the function has no arguments
     */
    public boolean hasNoArguments() {
        return argumentCount == 0;
    }
    
    /**
     * Checks if this is a common SQL aggregate function.
     * 
     * @param functionName The function name to check
     * @return true if it's a known aggregate function
     */
    private static boolean isAggregateFunction(String functionName) {
        if (functionName == null) return false;
        
        String upperName = functionName.toUpperCase();
        return upperName.equals("COUNT") || upperName.equals("SUM") || 
               upperName.equals("AVG") || upperName.equals("MIN") || 
               upperName.equals("MAX") || upperName.equals("STDDEV") ||
               upperName.equals("VARIANCE") || upperName.equals("GROUP_CONCAT");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FunctionCall that = (FunctionCall) obj;
        return argumentCount == that.argumentCount &&
               isAggregate == that.isAggregate &&
               Objects.equals(functionName, that.functionName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(functionName, argumentCount, isAggregate);
    }
    
    @Override
    public String toString() {
        return functionName + "(" + argumentCount + " args)" + (isAggregate ? " [AGGREGATE]" : "");
    }
}
package com.sdchat.ogsql.ast;

/**
 * Represents a computed value in SQL (column, literal, function, operator).
 */
public class ValueExpression {
    private String columnName;
    private String literalValue;
    private String functionName;
    private String operator;
    private ValueExpression leftOperand;
    private ValueExpression rightOperand;

    public ValueExpression() {
    }

    public ValueExpression(String literalValue) {
        this.literalValue = literalValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getLiteralValue() {
        return literalValue;
    }

    public void setLiteralValue(String literalValue) {
        this.literalValue = literalValue;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ValueExpression getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(ValueExpression leftOperand) {
        this.leftOperand = leftOperand;
    }

    public ValueExpression getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(ValueExpression rightOperand) {
        this.rightOperand = rightOperand;
    }
}

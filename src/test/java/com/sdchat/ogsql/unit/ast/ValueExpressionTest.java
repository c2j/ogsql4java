package com.sdchat.ogsql.unit.ast;

import com.sdchat.ogsql.ast.ValueExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for ValueExpression")
class ValueExpressionTest {

    @Test
    @DisplayName("ValueExpression should set and get columnName")
    void testColumnName() {
        ValueExpression expr = new ValueExpression();
        expr.setColumnName("id");
        assertEquals("id", expr.getColumnName());
    }

    @Test
    @DisplayName("ValueExpression should set and get literalValue")
    void testLiteralValue() {
        ValueExpression expr = new ValueExpression();
        expr.setLiteralValue("'test'");
        assertEquals("'test'", expr.getLiteralValue());
    }

    @Test
    @DisplayName("ValueExpression should set and get functionName")
    void testFunctionName() {
        ValueExpression expr = new ValueExpression();
        expr.setFunctionName("COUNT");
        assertEquals("COUNT", expr.getFunctionName());
    }

    @Test
    @DisplayName("ValueExpression should set and get operator")
    void testOperator() {
        ValueExpression expr = new ValueExpression();
        expr.setOperator("=");
        assertEquals("=", expr.getOperator());
    }

    @Test
    @DisplayName("ValueExpression should set and get leftOperand")
    void testLeftOperand() {
        ValueExpression expr = new ValueExpression();
        ValueExpression left = new ValueExpression();
        expr.setLeftOperand(left);
        assertEquals(left, expr.getLeftOperand());
    }

    @Test
    @DisplayName("ValueExpression should set and get rightOperand")
    void testRightOperand() {
        ValueExpression expr = new ValueExpression();
        ValueExpression right = new ValueExpression();
        expr.setRightOperand(right);
        assertEquals(right, expr.getRightOperand());
    }

    @Test
    @DisplayName("ValueExpression constructor should set literalValue")
    void testConstructor() {
        ValueExpression expr = new ValueExpression("'test'");
        assertEquals("'test'", expr.getLiteralValue());
    }
}

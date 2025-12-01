package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DivTest {
    @Test
    void testEvaluate() {
        Expression e = new Div(new Number(10), new Number(2));
        assertEquals(5.0, e.evaluate());
    }

    @Test
    void testDivisionByZero() {
        Expression e = new Div(new Number(1), new Number(0));
        assertThrows(ArithmeticException.class, e::evaluate);
    }

    @Test
    void testDerivative() {
        Expression e = new Div(new Variable("x"), new Number(2));
        assertEquals("(((1*2)-(x*0))/(2*2))", e.derivative("x").toString());
    }

    @Test
    void testSimplify() {
        Expression e = new Div(new Variable("x"), new Number(1));
        Expression simplified = e.simplify();
        assertEquals("x", simplified.toString());
    }

    @Test
    void testSimplifyDivisionBySameExpression() {
        // упрощение: (x + 1) / (x + 1) = 1
        Expression numerator = new Add(new Variable("x"), new Number(1));
        Expression denominator = new Add(new Variable("x"), new Number(1));
        Expression e = new Div(numerator, denominator);
        Expression simplified = e.simplify();
        assertEquals("1", simplified.toString());
        assertEquals(1.0, simplified.evaluate("x=5"));
    }

    @Test
    void testEvaluateComplexDivision() {
        // Проверяем вычисление сложного деления: ((a + b) / c)
        Expression numerator = new Add(new Variable("a"), new Variable("b"));
        Expression e = new Div(numerator, new Variable("c"));
        double result = e.evaluate("a=10; b=5; c=3");
        assertEquals(5.0, result); // (10 + 5) / 3 = 5
    }

    @Test
    void testSimplifyDivisionByZeroThrowsException() {
        Expression e = new Div(new Number(5), new Number(0));
        assertThrows(ArithmeticException.class, () -> {
            e.simplify();
        });
    }

    @Test
    void testSimplifyDivisionZeroByExpression() {
        // Упрощение: 0 / (x + 1) = 0
        Expression left = new Number(0);
        Expression right = new Add(new Variable("x"), new Number(1));
        Expression e = new Div(left, right);
        Expression simplified = e.simplify();
        assertEquals("0", simplified.toString());
        assertEquals(0.0, simplified.evaluate("x=5"));
    }
}

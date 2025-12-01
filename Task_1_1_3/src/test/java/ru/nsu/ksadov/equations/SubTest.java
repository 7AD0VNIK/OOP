package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SubTest {
    @Test
    void testEvaluate() {
        Expression e = new Sub(new Number(10), new Number(3));
        assertEquals(7.0, e.evaluate());
    }

    @Test
    void testDerivative() {
        Expression e = new Sub(new Variable("x"), new Number(2));
        assertEquals("(1-0)", e.derivative("x").toString());
    }

    @Test
    void testSimplify() {
        Expression e = new Sub(new Variable("x"), new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("0", simplified.toString());
    }

    @Test
    void testEvaluateWithNegativeResult() {
        Expression e = new Sub(new Number(5), new Number(10));
        assertEquals(-5.0, e.evaluate());
    }

    @Test
    void testSimplifyNestedSubtraction() {
        // ((x - x) - (y - y)) = 0
        Expression leftSub = new Sub(new Variable("x"), new Variable("x"));
        Expression rightSub = new Sub(new Variable("y"), new Variable("y"));
        Expression e = new Sub(leftSub, rightSub);
        Expression simplified = e.simplify();
        assertEquals("0", simplified.toString());
        assertEquals(0.0, simplified.evaluate());
    }

    @Test
    void testDerivativeComplex() {
        // производная сложного выражения: (x - y) по x
        Expression e = new Sub(new Variable("x"), new Variable("y"));
        Expression derivative = e.derivative("x");
        assertEquals("(1-0)", derivative.toString());
        double result = derivative.evaluate("x=5; y=3");
        assertEquals(1.0, result);
    }

    @Test
    void testSimplifySubtractionOfSameExpression() {
        // (x + 2) - (x + 2) = 0
        Expression left = new Add(new Variable("x"), new Number(2));
        Expression right = new Add(new Variable("x"), new Number(2));
        Expression e = new Sub(left, right);
        Expression simplified = e.simplify();
        assertEquals("0", simplified.toString());
        assertEquals(0.0, simplified.evaluate("x=3"));
    }
}

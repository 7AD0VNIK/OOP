package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MulTest {
    @Test
    void testEvaluate() {
        Expression e = new Mul(new Number(2), new Number(5));
        assertEquals(10.0, e.evaluate());
    }

    @Test
    void testDerivative() {
        Expression e = new Mul(new Variable("x"), new Number(3));
        assertEquals("((1*3)+(x*0))", e.derivative("x").toString());
    }

    @Test
    void testSimplify() {
        Expression e = new Mul(new Number(0), new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("0", simplified.toString());
    }

    @Test
    void testEvaluateWithVariables() {
        Expression e = new Mul(new Variable("x"), new Variable("y"));
        double result = e.evaluate("x=3; y=4");
        assertEquals(12.0, result);
    }

    @Test
    void testSimplifyBothSidesOne() {
        Expression e = new Mul(new Number(1), new Number(1));
        Expression simplified = e.simplify();
        assertEquals("1", simplified.toString());
        assertEquals(1.0, simplified.evaluate());
    }

    @Test
    void testSimplifyNestedMultiplication() {
        // (2 * 0) * x = 0
        Expression inner = new Mul(new Number(2), new Number(0));
        Expression e = new Mul(inner, new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("0", simplified.toString());
        assertEquals(0.0, simplified.evaluate());
    }

    @Test
    void testSimplifyMultiplicationByOne() {
        // Упрощение: 1 * (x + 1) = x + 1
        Expression left = new Number(1);
        Expression right = new Add(new Variable("x"), new Number(1));
        Expression e = new Mul(left, right);
        Expression simplified = e.simplify();
        assertEquals("(x+1)", simplified.toString());
        assertEquals(6.0, simplified.evaluate("x=5"));
    }

    @Test
    void testSimplifyMultiplicationWithNumbers() {
        // Упрощение: 3 * 4 = 12
        Expression e = new Mul(new Number(3), new Number(4));
        Expression simplified = e.simplify();
        assertEquals("12", simplified.toString());
        assertEquals(12.0, simplified.evaluate());
    }
}

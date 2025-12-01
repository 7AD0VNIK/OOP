package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AddTest {
    @Test
    void testEvaluate() {
        Expression e = new Add(new Number(3), new Number(4));
        assertEquals(7.0, e.evaluate());
    }

    @Test
    void testDerivative() {
        Expression e = new Add(new Variable("x"), new Number(5));
        assertEquals("(1+0)", e.derivative("x").toString());
    }

    @Test
    void testSimplify() {
        Expression e = new Add(new Number(0), new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("x", simplified.toString());
    }

    @Test
    void testEvaluateMultipleVariables() {
        Expression e = new Add(
                new Add(new Variable("a"), new Variable("b")),
                new Variable("c")
        );
        double result = e.evaluate("a=1; b=2; c=3");
        assertEquals(6.0, result);
    }

    @Test
    void testSimplifyBothSidesZero() {
        Expression e = new Add(new Number(0), new Number(0));
        Expression simplified = e.simplify();
        assertEquals("0", simplified.toString());
        assertEquals(0.0, simplified.evaluate());
    }
}

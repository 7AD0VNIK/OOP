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
}

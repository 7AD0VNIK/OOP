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
    void testSimplifyWithZero() {
        Expression e = new Add(new Number(0), new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("x", simplified.toString());
    }
}

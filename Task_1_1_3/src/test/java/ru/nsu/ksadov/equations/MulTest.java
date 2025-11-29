package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testSimplifyNumbers() {
        Expression e = new Mul(new Number(3), new Number(4));
        Expression simplified = e.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(12.0, simplified.evaluate());
    }

    @Test
    void testSimplifyMultiplyByZero() {
        Expression e = new Mul(new Number(0), new Variable("x"));
        Expression simplified = e.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(0.0, simplified.evaluate());
    }

    @Test
    void testSimplifyMultiplyByOne() {
        Expression e = new Mul(new Number(1), new Variable("x"));
        Expression simplified = e.simplify();
        assertEquals("x", simplified.toString());
    }
}

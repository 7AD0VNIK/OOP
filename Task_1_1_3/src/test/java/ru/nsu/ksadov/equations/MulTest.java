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
}

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
}

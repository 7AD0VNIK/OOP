package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BinaryOperationTest {
    @Test
    void testToStringAdd() {
        Expression e = new Add(new Number(1), new Number(2));
        assertEquals("(1+2)", e.toString());
    }

    @Test
    void testToStringSub() {
        Expression e = new Sub(new Number(5), new Variable("x"));
        assertEquals("(5-x)", e.toString());
    }

    @Test
    void testEvaluateNested() {
        Expression e = new Mul(
                new Add(new Number(2), new Number(3)), // (2+3)
                new Sub(new Number(10), new Number(4)) // (10-4)
        );
        assertEquals(30.0, e.evaluate()); // (2+3)*(10-4)=5*6=30
    }

}

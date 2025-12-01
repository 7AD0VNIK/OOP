package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testBinaryOperationInheritance() {
        // проверяем полиморфизм
        Expression e = new Add(new Number(1), new Number(2));
        assertTrue(e instanceof BinaryOperation);
        assertTrue(e instanceof Expression);

        assertEquals(3.0, e.evaluate());
        assertEquals("(1+2)", e.toString());
    }

    @Test
    void testComplexBinaryOperations() {
        Expression complex = new Add(
                new Mul(new Number(2), new Variable("x")),
                new Div(new Number(10), new Sub(new Variable("y"), new Number(1)))
        );

        // 2*3 + 10/(5-1) = 6 + 10/4 = 6 + 2.5 = 8.5
        double result = complex.evaluate("x=3; y=5");
        assertEquals(8.5, result);

        String expected = "((2*x)+(10/(y-1)))";
        assertEquals(expected, complex.toString());
    }
}

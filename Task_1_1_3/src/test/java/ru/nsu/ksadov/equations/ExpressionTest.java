package ru.nsu.ksadov.equations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionTest {
    @Test
    void testEvaluateWithAssignments() {
        Expression e = new Add(new Variable("x"), new Variable("y"));
        double result = e.evaluate("x=2; y=3");
        assertEquals(5.0, result);
    }

    @Test
    void testEvaluateWithExtraSpaces() {
        Expression e = new Mul(new Variable("a"), new Variable("b"));
        double result = e.evaluate(" a = 4 ; b=  2 ");
        assertEquals(8.0, result);
    }

    @Test
    void testEvaluateWithoutAssignments() {
        Expression e = new Add(new Number(3), new Number(7));
        assertEquals(10.0, e.evaluate());
    }

    @Test
    void testEvaluateInvalidAssignment() {
        Expression e = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> e.evaluate("x=;"));
        assertThrows(NumberFormatException.class, () -> e.evaluate("x=abc"));
    }
}

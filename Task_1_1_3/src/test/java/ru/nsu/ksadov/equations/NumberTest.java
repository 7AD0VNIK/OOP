package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class NumberTest {
    @Test
    void testEvaluate() {
        Number n = new Number(5);
        assertEquals(5.0, n.evaluate());
    }

    @Test
    void testDerivative() {
        Number n = new Number(7);
        assertEquals("0", n.derivative("x").toString());
    }
}

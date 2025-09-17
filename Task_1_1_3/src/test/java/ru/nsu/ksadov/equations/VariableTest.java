package ru.nsu.ksadov.equations;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class VariableTest {
    @Test
    void testEvaluate() {
        Variable x = new Variable("x");
        assertEquals(10.0, x.evaluate(Map.of("x", 10.0)));
    }

    @Test
    void testEvaluateMissingVariable() {
        Variable x = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> x.evaluate(Map.of()));
    }

    @Test
    void testDerivative() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        assertEquals("1", x.derivative("x").toString());
        assertEquals("0", y.derivative("x").toString());
    }
}

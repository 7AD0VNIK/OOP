package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AdvancedExpressionParserTest {

    @Test
    void testParseFactor() {
        Expression e3 = AdvancedExpressionParser.parse("(1 + 2)");
        assertEquals("(1+2)", e3.toString());
    }

    @Test
    void testParseTerm() {
        Expression expr = AdvancedExpressionParser.parse("2 / 3");
        assertEquals("(2/3)", expr.toString());
    }


    @Test
    void testParseFull() {
        Expression expr = AdvancedExpressionParser.parse("(1 + 2) * 3");
        assertEquals("((1+2)*3)", expr.toString());
    }

    @Test
    void testParseWithoutParenthesesPriority() {
        Expression expr = AdvancedExpressionParser.parse("1 + 7 * 5 - 4 / 2");
        // Ожидаем: (1 + (7*5)) - (4/2) = (1 + 35) - (2) = 34
        assertEquals(34.0, expr.evaluate(), 0.0001);
    }

    @Test
    void testParseTermSimple() {
        Expression expr = AdvancedExpressionParser.parse("4 * 5 / 2");
        assertEquals("((4*5)/2)", expr.toString());
    }

    @Test
    void testParseExpressionSimple() {
        Expression expr = AdvancedExpressionParser.parse("10 - 3 + 1");
        assertEquals("((10-3)+1)", expr.toString());
    }

    @Test
    void testIsBalancedFail() {
        try {
            AdvancedExpressionParser.parse("(1+2");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unbalanced"));
        }
    }
}

package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты для AdvancedExpressionParser.
 */
class AdvancedExpressionParserTest {

    @Test
    void testParseOperatorPrecedence() {
        Expression e = AdvancedExpressionParser.parse("3 + 2 * 5");
        assertEquals(13.0, e.evaluate());
    }

    @Test
    void testParseComplexExpression() {
        Expression e = AdvancedExpressionParser.parse("x * y + 2 * z");
        assertEquals(11.0, e.evaluate("x=3; y=1; z=4"));
    }

    @Test
    void testParseWithVariables() {
        Expression e = AdvancedExpressionParser.parse("a + b * c");
        assertEquals(14.0, e.evaluate("a=2; b=3; c=4"));
    }

    @Test
    void testParseDivision() {
        Expression e = AdvancedExpressionParser.parse("10 / 2 + 3");
        assertEquals(8.0, e.evaluate());
    }

    @Test
    void testParseWithSpaces() {
        Expression e = AdvancedExpressionParser.parse("  x  +  y  *  z  ");
        assertEquals(7.0, e.evaluate("x=1; y=2; z=3"));
    }
}

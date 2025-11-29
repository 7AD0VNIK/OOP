package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AdvancedExpressionParserTest {

    @Test
    void testParseFactor() {
        Expression e3 = AdvancedExpressionParser.parse("(1 + 2)");
        assertEquals("(1.0+2.0)", e3.toString());
    }

    @Test
    void testParseTerm() {
        Expression expr = AdvancedExpressionParser.parse("2 / 3");
        assertEquals("(2.0/3.0)", expr.toString());
    }

    @Test
    void testParseExpression() {
        Expression expr = AdvancedExpressionParser.parse("1 + 2 * 3");
        assertEquals("(1.0+(2.0*3.0))", expr.toString());
    }

    @Test
    void testParseFull() {
        Expression expr = AdvancedExpressionParser.parse("(1 + 2) * 3");
        assertEquals("((1.0+2.0)*3.0)", expr.toString());
    }
}

package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ExpressionParserTest {
    @Test
    void testParseNumber() {
        Expression e = ExpressionParser.parse("42");
        assertEquals("42", e.toString());
    }

    @Test
    void testParseVariable() {
        Expression e = ExpressionParser.parse("x");
        assertEquals("x", e.toString());
    }

    @Test
    void testParseAdd() {
        Expression e = ExpressionParser.parse("(3+4)");
        assertEquals("(3+4)", e.toString());
        assertEquals(7.0, e.evaluate());
    }

    @Test
    void testParseComplex() {
        Expression e = ExpressionParser.parse("(3+(2*x))");
        assertEquals("(3+(2*x))", e.toString());
        assertEquals(13.0, e.evaluate("x=5"));
    }
}

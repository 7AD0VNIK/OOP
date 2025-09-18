package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    // Тесты на ошибки
    @Test
    void testParseEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(""));
    }

    @Test
    void testParseUnbalancedParenthese() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("3+4)"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("((3+4)"));
    }

    @Test
    void testParseInvalidVariableName() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("2x")); // начинается с цифры
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("var!")); // содержит спецсимвол
    }

    @Test
    void testParseMissingOperand() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("3+"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("+"));
    }

    @Test
    void testParseDecimalNumbers() {
        Expression e = ExpressionParser.parse("(3.5 + 2.1)");
        assertEquals("(3.5+2.1)", e.toString());
        assertEquals(5.6, e.evaluate(), 0.0001); // delta для сравнения double
    }
}

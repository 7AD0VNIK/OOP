package ru.nsu.ksadov.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ValueTest {

    @Test
    void testValueValues() {
        Value[] values = Value.values();
        assertEquals(13, values.length);
        assertEquals(Value.TWO, values[0]);
        assertEquals(Value.ACE, values[12]);
    }

    @Test
    void testValueNames() {
        assertEquals("TWO", Value.TWO.name());
        assertEquals("ACE", Value.ACE.name());
        assertEquals("KING", Value.KING.name());
        assertEquals("QUEEN", Value.QUEEN.name());
    }
}
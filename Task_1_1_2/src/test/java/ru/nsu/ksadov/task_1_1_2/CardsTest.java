package ru.nsu.ksadov.task_1_1_2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardsTest {

    @Test
    void testToString() {
        Cards card = new Cards(Suit.SPADES, Value.KING);
        assertEquals("KING of SPADES", card.toString());
    }

    @Test
    void testGetValue() {
        Cards card = new Cards(Suit.DIAMONDS, Value.QUEEN);
        assertEquals(Value.QUEEN, card.getValue());
    }
}
package ru.nsu.ksadov.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
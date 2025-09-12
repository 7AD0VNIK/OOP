package ru.nsu.ksadov.blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuitTest {

    @Test
    void testSuitValues() {
        Suit[] suits = Suit.values();
        assertEquals(4, suits.length);
        assertEquals(Suit.HEARTS, suits[0]);
        assertEquals(Suit.SPADES, suits[1]);
        assertEquals(Suit.CLUBS, suits[2]);
        assertEquals(Suit.DIAMONDS, suits[3]);
    }

    @Test
    void testSuitNames() {
        assertEquals("HEARTS", Suit.HEARTS.name());
        assertEquals("SPADES", Suit.SPADES.name());
        assertEquals("CLUBS", Suit.CLUBS.name());
        assertEquals("DIAMONDS", Suit.DIAMONDS.name());
    }
}
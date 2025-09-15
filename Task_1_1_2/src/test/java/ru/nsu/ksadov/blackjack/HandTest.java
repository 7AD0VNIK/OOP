package ru.nsu.ksadov.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandTest {

    private Hand hand;

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void addCard() {
        Cards card = new Cards(Suit.HEARTS, Value.ACE);
        hand.addCard(card);

        assertEquals(1, hand.size());
        assertEquals(card, hand.peek(0));
    }

    @Test
    void peek() {
        Cards card1 = new Cards(Suit.HEARTS, Value.KING);
        Cards card2 = new Cards(Suit.SPADES, Value.QUEEN);

        hand.addCard(card1);
        hand.addCard(card2);

        assertEquals(card1, hand.peek(0));
        assertEquals(card2, hand.peek(1));
    }

    @Test
    void size() {
        assertEquals(0, hand.size());

        hand.addCard(new Cards(Suit.HEARTS, Value.TWO));
        assertEquals(1, hand.size());

        hand.addCard(new Cards(Suit.DIAMONDS, Value.THREE));
        assertEquals(2, hand.size());
    }

    @Test
    void getTotalValue() {
        hand.addCard(new Cards(Suit.HEARTS, Value.TEN));
        hand.addCard(new Cards(Suit.SPADES, Value.FIVE));

        assertEquals(15, hand.getTotalValue());
    }

    @Test
    void testToString_EmptyHand() {
        assertEquals("[]", hand.toString());
    }

    @Test
    void testToString() {
        Cards card = new Cards(Suit.HEARTS, Value.ACE);
        hand.addCard(card);

        assertTrue(hand.toString().contains("HEARTS"));
        assertTrue(hand.toString().contains("ACE"));
    }
}
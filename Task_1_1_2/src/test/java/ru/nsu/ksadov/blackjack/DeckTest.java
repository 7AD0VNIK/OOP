package ru.nsu.ksadov.blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testDeckCreation() {
        Deck deck = new Deck();
        assertNotNull(deck);
        assertEquals(0, deck.size());
    }

    @Test
    void fill() {
        Deck deck = new Deck();
        deck.fill();
        assertEquals(52, deck.size());
    }

    @Test
    void testShuffle() {
        Deck deck1 = new Deck();
        deck1.fill();
        String originalOrder = deck1.toString();

        Deck deck2 = new Deck();
        deck2.fill();
        deck2.shuffle();
        String shuffledOrder = deck2.toString();

        // Шанс что колоды совпадут после перемешивания очень мал
        assertNotEquals(originalOrder, shuffledOrder);
    }

    @Test
    void discard() {
        Deck deck = new Deck();
        deck.fill();
        int origSize = deck.size();

        deck.discard(0);
        assertEquals(origSize - 1, deck.size());
    }

    @Test
    void testPeek() {
        Deck deck = new Deck();
        deck.fill();

        Cards firstCard = deck.peek(0);
        assertNotNull(firstCard);
        // Первая карта после заполнения должна быть TWO of HEARTS
        assertEquals("TWO of HEARTS", firstCard.toString());
    }

    @Test
    void testDrawFromEmptyDeck() {
        Deck emptyDeck = new Deck();
        Deck targetDeck = new Deck();

        targetDeck.drawFrom(emptyDeck);
        assertEquals(0, targetDeck.size()); // Не должно добавить карт
    }

    @Test
    void testSize() {
        Deck deck = new Deck();
        assertEquals(0, deck.size());

        deck.fill();
        assertEquals(52, deck.size());
    }
}
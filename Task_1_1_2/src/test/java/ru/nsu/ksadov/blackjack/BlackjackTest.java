package ru.nsu.ksadov.blackjack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.function.Supplier;

class BlackjackTest {

    /**
     * Создаёт Blackjack с подставленным вводом и выводом.
     */
    private String runGame(String inputData, Supplier<Deck> deckSupplier) {
        Scanner testInput = new Scanner(new ByteArrayInputStream(inputData.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        new Blackjack(testInput).start(deckSupplier);

        return output.toString();
    }

    @Test
    void testGameInitialization() {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Blackjack.main(new String[]{});

        String result = output.toString();
        Assertions.assertTrue(result.contains("Welcome to Blackjack!"));
        Assertions.assertTrue(result.contains("Round 1"));
    }

    @Test
    void testPlayerBustScenario2() {
        // Игрок: hit (1), потом выход (0)
        String input = "1\n0\n";

        Deck deck = new Deck();
        // draw() берёт карты с конца списка!
        deck.addCard(new Cards(Suit.HEARTS, Value.TEN));      // игрок 1
        deck.addCard(new Cards(Suit.SPADES, Value.NINE));     // игрок 2
        deck.addCard(new Cards(Suit.DIAMONDS, Value.SEVEN));  // дилер 1
        deck.addCard(new Cards(Suit.CLUBS, Value.EIGHT));     // дилер 2
        deck.addCard(new Cards(Suit.HEARTS, Value.FIVE));     // добор игрока => перебор

        String result = runGame(input, () -> deck);

        Assertions.assertTrue(result.contains("Bust! You have: "));
        Assertions.assertTrue(result.contains("Dealer won the round!"));
    }

    @Test
    void testDealerBustScenario() {
        // Игрок сразу стоит (0), потом выход (0)
        String input = "0\n0\n";

        Deck deck = new Deck();
        deck.addCard(new Cards(Suit.HEARTS, Value.TEN));      // игрок 1
        deck.addCard(new Cards(Suit.SPADES, Value.NINE));     // игрок 2
        deck.addCard(new Cards(Suit.DIAMONDS, Value.SEVEN));  // дилер 1
        deck.addCard(new Cards(Suit.CLUBS, Value.SIX));       // дилер 2
        deck.addCard(new Cards(Suit.HEARTS, Value.KING));     // добор дилера
        deck.addCard(new Cards(Suit.SPADES, Value.QUEEN));    // ещё добор => перебор

        String result = runGame(input, () -> deck);

        Assertions.assertTrue(result.contains("Dealer's turn"));
        Assertions.assertTrue(result.contains("You won the round!"));
    }

    @Test
    void testPushScenario() {
        // Игрок сразу стоит (0), потом выход (0)
        String input = "0\n0\n";

        Deck deck = new Deck();
        deck.addCard(new Cards(Suit.HEARTS, Value.TEN));      // игрок 1
        deck.addCard(new Cards(Suit.SPADES, Value.JACK));     // игрок 2 → 20
        deck.addCard(new Cards(Suit.DIAMONDS, Value.QUEEN));  // дилер 1
        deck.addCard(new Cards(Suit.CLUBS, Value.KING));      // дилер 2 → 20

        String result = runGame(input, () -> deck);

        Assertions.assertTrue(result.contains("Push!"), "Ожидается ничья");
    }
}
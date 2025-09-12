package ru.nsu.ksadov.blackjack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class BlackjackSimpleTest {

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
    void testPlayerBustScenario() {
        String input = "1\n1\n1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Thread gameThread = new Thread(() -> Blackjack.main(new String[]{}));
        gameThread.start();

        try {
            gameThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String result = output.toString();
        Assertions.assertTrue(result.contains("Bust!") || result.contains("Dealer won"));
    }
}
package ru.nsu.ksadov.blackjack;

import java.util.Scanner;

/**
 * Главный класс игры "Блэкджек".
 */
public class Blackjack {
    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in); // один Scanner на всю игру
        int playerWins = 0;
        int dealerWins = 0;
        int round = 1;

        System.out.println("Welcome to Blackjack!");

        while (true) {
            boolean finished = false;
            System.out.println("\nRound " + round);
            System.out.println("Dealer dealt the cards");

            Deck playDeck = new Deck();
            playDeck.fill();
            playDeck.shuffle();

            Deck playerDeck = new Deck();
            Deck dealerDeck = new Deck();

            playerDeck.drawFrom(playDeck);
            playerDeck.drawFrom(playDeck);

            dealerDeck.drawFrom(playDeck);
            dealerDeck.drawFrom(playDeck);

            // ход игрока
            while (true) {
                System.out.println("Your cards: " + playerDeck
                        + " > " + playerDeck.getTotalValue());
                System.out.println("Dealer's cards: " + dealerDeck.peek(0) + ", [hidden]");
                System.out.println("Your move (1 - hit, 0 - stand):");

                if (!input.hasNext()) {
                    System.out.println("No input available. Exiting game.");
                    input.close();
                    return;
                }

                String move = input.next();

                if (move.equals("1")) {
                    playerDeck.drawFrom(playDeck);
                    System.out.println("You drew: " + playerDeck.peek(playerDeck.size() - 1));
                    if (playerDeck.getTotalValue() > 21) {
                        System.out.println("Bust! You have: " + playerDeck.getTotalValue());
                        dealerWins++;
                        finished = true;
                        break;
                    }
                } else if (move.equals("0")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1 or 0.");
                    input.nextLine(); // очистка буфера
                }
            }

            // ход дилера
            if (!finished) {
                System.out.println("\nDealer's turn");
                System.out.println("Dealer reveals hidden card: " + dealerDeck.peek(1));
                while (dealerDeck.getTotalValue() < 17) {
                    dealerDeck.drawFrom(playDeck);
                    System.out.println("Dealer drew: " + dealerDeck.peek(dealerDeck.size() - 1));
                }
            }

            int playerVal = playerDeck.getTotalValue();
            int dealerVal = dealerDeck.getTotalValue();

            System.out.println("\nYour cards: " + playerDeck + " > " + playerVal);
            System.out.println("Dealer's cards: " + dealerDeck + " > " + dealerVal);

            if (!finished) {
                if (dealerVal > 21) {
                    System.out.println("You won the round!");
                    playerWins++;
                } else if (playerVal > dealerVal) {
                    System.out.println("You won the round!");
                    playerWins++;
                } else if (playerVal == dealerVal) {
                    System.out.println("Push!");
                } else {
                    System.out.println("Dealer won the round!");
                    dealerWins++;
                }
            }

            System.out.println("Score: " + playerWins + " : " + dealerWins);
            round++;

            System.out.println("Play again? (1 - yes, 0 - exit)");
            if (!input.hasNext()) {
                System.out.println("No input available. Exiting game.");
                break;
            }

            String again = input.next();
            if (again.equals("0")) {
                break;
            }
        }

        System.out.println("\nGame over!");
        System.out.println("Final score: " + playerWins + " : " + dealerWins);

        input.close();
    }
}
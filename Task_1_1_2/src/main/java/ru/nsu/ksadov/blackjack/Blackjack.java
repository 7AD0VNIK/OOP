package ru.nsu.ksadov.blackjack;

import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Главный класс игры "Блэкджек".
 */
public class Blackjack {

    /**
     * Максимальное количество очков, при превышении которых игрок проигрывает (перебор).
     */
    private static final int MAX_SCORE = 21;

    private final Scanner input;
    private int playerWins = 0;
    private int dealerWins = 0;
    private int round = 1;

    public Blackjack() {
        this(new Scanner(System.in));
    }

    public Blackjack(Scanner input) {
        this.input = input;
    }

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        new Blackjack().start(Deck::defaultDeck);
    }

    /**
     * Запуск игрового цикла.
     */
    public void start(Supplier<Deck> deckSupplier) {
        System.out.println("Welcome to Blackjack!");

        while (true) {
            playRound(deckSupplier.get());

            System.out.println("Play again? (1 - yes, 0 - exit)");
            if (!input.hasNext() || input.next().equals("0")) {
                break;
            }
        }

        System.out.println("\nGame over!");
        System.out.println("Final score: " + playerWins + " : " + dealerWins);

        input.close();
    }

    /**
     * Проведение одного раунда игры.
     */
    private void playRound(Deck playDeck) {

        System.out.println("\nRound " + round);
        System.out.println("Dealer dealt the cards");

        playDeck.shuffle();

        Hand playerHand = new Hand();
        Hand dealerHand = new Hand();

        dealInitialCards(playDeck, playerHand, dealerHand);

        boolean finished = playerTurn(playDeck, playerHand, dealerHand);

        if (!finished) {
            dealerTurn(playDeck, dealerHand);
            determineWinner(playerHand, dealerHand);
        }

        System.out.println("Score: " + playerWins + " : " + dealerWins);
    }

    /**
     * Раздает начальные карты игроку и дилеру.
     */
    private void dealInitialCards(Deck playDeck, Hand playerHand, Hand dealerHand) {
        playerHand.addCard(playDeck.draw());
        playerHand.addCard(playDeck.draw());
        dealerHand.addCard(playDeck.draw());
        dealerHand.addCard(playDeck.draw());
    }

    /**
     * Ход игрока.
     *
     * @return true, если игрок сразу проиграл (перебор)
     */
    private boolean playerTurn(Deck playDeck, Hand playerHand, Hand dealerHand) {
        while (true) {
            System.out.println("Your cards: " + playerHand
                    + " > " + playerHand.getTotalValue());
            System.out.println("Dealer's cards: " + dealerHand.peek(0) + ", [hidden]");
            System.out.println("Your move (1 - hit, 0 - stand):");

            if (!input.hasNext()) {
                System.out.println("No input available. Exiting game.");
                System.exit(0);
            }

            String move = input.next();

            if (move.equals("1")) {
                playerHand.addCard(playDeck.draw());
                System.out.println("You drew: " + playerHand.peek(playerHand.size() - 1));
                if (playerHand.getTotalValue() > MAX_SCORE) {
                    System.out.println("Bust! You have: " + playerHand.getTotalValue());
                    System.out.println("Dealer won the round!");
                    dealerWins++;
                    return true;
                }
            } else if (move.equals("0")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 1 or 0.");
                input.nextLine(); // очистка буфера
            }
        }
    }

    /**
     * Ход дилера.
     */
    private void dealerTurn(Deck playDeck, Hand dealerHand) {

        System.out.println("\nDealer's turn");
        System.out.println("Dealer reveals hidden card: " + dealerHand.peek(1));
        while (dealerHand.getTotalValue() < 17) {
            dealerHand.addCard(playDeck.draw());
            System.out.println("Dealer drew: " + dealerHand.peek(dealerHand.size() - 1));
        }
    }

    /**
     * Определяем победителя.
     */
    private void determineWinner(Hand playerHand, Hand dealerHand) {
        int playerVal = playerHand.getTotalValue();
        int dealerVal = dealerHand.getTotalValue();

        System.out.println("\nYour cards: " + playerHand + " > " + playerVal);
        System.out.println("Dealer's cards: " + dealerHand + " > " + dealerVal);

        if (dealerVal > MAX_SCORE || playerVal > dealerVal) {
            System.out.println("You won the round!");
            playerWins++;
        } else if (playerVal == dealerVal) {
            System.out.println("Push!");
        } else {
            System.out.println("Dealer won the round!");
            dealerWins++;
        }
    }
}
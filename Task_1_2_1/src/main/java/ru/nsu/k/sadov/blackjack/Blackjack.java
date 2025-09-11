package ru.nsu.k.sadov.blackjack;

import java.util.Scanner;

public class blackjack {
    public static void main(String[] args){
        Scanner userInput = new Scanner(System.in);
        int[] score = {0, 0};
        boolean end = false;
        int roundCount = 1;
        System.out.println("Добро пожаловать в Блэкджек!");
        System.out.println("Раунд " + roundCount);
        System.out.println("Дилер раздал карты");


        Deck playDeck = new Deck();
        playDeck.createDeck();
        playDeck.shuffle();
        // создаем колоду для игрока и диллера
        Deck playerDeck = new Deck();
        Deck dealerDeck = new Deck();

        // игровой процесс
        playerDeck.draw(playDeck);
        playerDeck.draw(playDeck);

        dealerDeck.draw(playDeck);
        dealerDeck.draw(playDeck);

        while(true){
            System.out.println(playerDeck);
            System.out.println("Ваши карты: [" + playDeck.toString() + playerDeck.cardsValue() + "]");

            System.out.println("Карты дилера: " + dealerDeck.getCard(0).toString() + "<закрытая карта ]");
            System.out.println("Ваш ход");
            System.out.println("-------");
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться.");
            int response = userInput.nextInt();

            if (response == 1){
                playerDeck.draw(playDeck);
                score[0] += 1;
                score[1] += 1;
                System.out.println("Ничья! Счёт " + score[0] + ":" + score[1]);
                System.out.println(playerDeck.getCard(playDeck.sizeDeck() - 1).toString());
                // перебор
                if (playerDeck.cardsValue() > 21){
                    System.out.println("Перебор -_- вы набрали: " + playDeck.cardsValue());
                    end = true;
                }
            }
            if (response == 0){
                break;
            }
        }
        System.out.println("Карты диллера: " + dealerDeck.toString());
        if ((dealerDeck.cardsValue() > playerDeck.cardsValue()) && end == false){
            System.out.println("Диллер выиграл!");
            end = true;
            roundCount++;
        }

        while((dealerDeck.cardsValue() < 17) && end == false){
            dealerDeck.draw(playDeck);
            System.out.println("Dealer draws: " + dealerDeck.getCard(dealerDeck.sizeDeck() - 1).toString());
        }
        System.out.println("Диллер значение: " + dealerDeck.cardsValue());

        if ((dealerDeck.cardsValue() > 21) && end == false){
            System.out.println("Перебор диллера! Ты выиграл");
            end = true;
            roundCount++;
        }

        if ((playerDeck.cardsValue() == dealerDeck.cardsValue()) && end == false){
            System.out.println("Push");
            end = true;
            roundCount++;
        }

        if ((playerDeck.cardsValue() > dealerDeck.cardsValue()) && end == false){
            System.out.println("You win!!");
            end = true;
            roundCount++;
        }

        playerDeck.moveAllToDeck(playDeck);
        dealerDeck.moveAllToDeck(playDeck);


    }
}

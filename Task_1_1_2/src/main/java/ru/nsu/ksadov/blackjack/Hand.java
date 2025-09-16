package ru.nsu.ksadov.blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий руку игрока или дилера.
 */
public class Hand {
    private final List<Cards> cards;

    /**
     * Создает пустую руку.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Добавляет карту в руку.
     *
     * @param card карта для добавления
     */
    public void addCard(Cards card) {
        cards.add(card);
    }

    /**
     * Возвращает карту по индексу.
     *
     * @param index индекс карты
     * @return карта
     */
    public Cards peek(int index) {
        return cards.get(index);
    }

    /**
     * Возвращает количество карт в руке.
     *
     * @return размер руки
     */
    public int size() {
        return cards.size();
    }

    /**
     * Считает сумму очков в руке.
     *
     * @return сумма очков
     */
    public int getTotalValue() {
        int total = 0;
        int aces = 0;

        for (Cards card : cards) {
            total += card.getValue().getPoints();
            if (card.getValue() == Value.ACE) {
                aces++;
            }
        }

        // корректируем туз(ы), если перебор
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    /**
     * Возвращает список карт в строковом формате.
     */
    @Override
    public String toString() {
        return cards.toString();
    }
}

package ru.nsu.ksadov.Task_1_1_2;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий колоду карт.
 */
public class Deck {

    private final List<Cards> deck;

    /**
     * Создает пустую колоду.
     */
    public Deck() {
        deck = new ArrayList<>();
    }

    /**
     * Заполняет колоду стандартным набором карт.
     */
    public void fill() {
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                deck.add(new Cards(suit, value));
            }
        }
    }

    /**
     * Перемешивает колоду.
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cards card : deck) {
            sb.append("\n").append("  ").append(card);
        }
        return sb.toString();
    }

    /**
     * Удаляет карту из колоды по индексу.
     *
     * @param index индекс карты
     */
    public void discard(int index) {
        deck.remove(index);
    }

    /**
     * Возвращает карту по индексу.
     *
     * @param index индекс карты
     * @return карта
     */
    public Cards peek(int index) {
        return deck.get(index);
    }


    /**
     * Возвращает количество карт в колоде.
     *
     * @return размер колоды
     */
    public int size() {
        return deck.size();
    }

    /**
     * Берет карту из другой колоды.
     *
     * @param other другая колода
     */
    public void drawFrom(Deck other) {
        if (!other.deck.isEmpty()) {
            deck.add(other.peek(0));
            other.discard(0);
        }
    }

    /**
     * Считает сумму очков в колоде (для игры).
     *
     * @return сумма очков
     */
    public int getTotalValue() {
        int total = 0;
        int aces = 0;

        for (Cards card : deck) {
            switch (card.getValue()) {
                case TWO:
                    total += 2;
                    break;
                case THREE:
                    total += 3;
                    break;
                case FOUR:
                    total += 4;
                    break;
                case FIVE:
                    total += 5;
                    break;
                case SIX:
                    total += 6;
                    break;
                case SEVEN:
                    total += 7;
                    break;
                case EIGHT:
                    total += 8;
                    break;
                case NINE:
                    total += 9;
                    break;
                case TEN:
                case JACK:
                case QUEEN:
                case KING:
                    total += 10;
                    break;
                case ACE:
                    aces++;
                    total += 11;
                    break;
            }
        }

        // корректируем туз(ы), если перебор.
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

//    /**
//     * Переносит все карты в другую колоду.
//     *
//     * @param target цель (куда переносим)
//     */
//    public void moveAllTo(Deck target) {
//        target.deck.addAll(this.deck);
//        this.deck.clear();
//    }
//    /**
//     * Добавляет карту в колоду.
//     *
//     * @param card карта
//     */
//    public void put(Cards card) {
//        deck.add(card);
//    }
}

package ru.nsu.ksadov.blackjack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс, представляющий колоду карт.
 */
public class Deck {

    private final List<Cards> cards = new LinkedList<>();

    /**
     * Удаляет карту из колоды по индексу.
     *
     * @param index индекс карты
     */
    public void discard(int index) {
        cards.remove(index);
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
     * Заполняет колоду стандартными 52 картами.
     */
    public void fill() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                cards.add(new Cards(suit, value));
            }
        }
    }

    /**
     * Перемешивает колоду.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Вытягивает карту из колоды.
     *
     * @return карта сверху колоды
     */
    public Cards draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Колода пуста!");
        }
        return cards.remove(0);
    }

    /**
     * Добавляет карту в колоду (для тестов или ручной настройки).
     *
     * @param card карта, которая добавляется в колоду
     */
    public void addCard(Cards card) {
        cards.add(card);
    }

    /**
     * Возвращает размер колоды.
     *
     * @return количество оставшихся карт
     */
    public int size() {
        return cards.size();
    }

    /**
     * Создает и возвращает стандартную колоду игральных карт, заполненную и перемешанную.
     */
    public static Deck defaultDeck() {
        Deck deck = new Deck();
        deck.fill();
        deck.shuffle();
        return deck;
    }
}

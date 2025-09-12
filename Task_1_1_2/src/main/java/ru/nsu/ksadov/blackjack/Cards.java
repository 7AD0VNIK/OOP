package ru.nsu.ksadov.blackjack;

/**
 * Класс, представляющий одну карту (масть + значение).
 */
public class Cards {

    private final Suit suit;
    private final Value value;

    /**
     * Создает карту с указанной мастью и значением.
     *
     * @param suit  масть карты
     * @param value значение карты
     */
    public Cards(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * Возвращает строковое представление карты.
     *
     * @return строка с описанием карты
     */
    @Override
    public String toString() {
        return value + " of " + suit;
    }

    /**
     * Получить значение карты.
     *
     * @return значение карты
     */
    public Value getValue() {
        return this.value;
    }
}
package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void testSetStatusDoesNotThrow() {
        Order order = new Order(1);
        assertDoesNotThrow(() -> order.setStatus("Cooking"),
                "метод setStatus должен работать корректно");
    }

    @Test
    void testOrderInitialization() {
        Order order = new Order(55);
        assertEquals(55, order.getId(),
                "ID заказа должен совпадать с переданным в конструктор");
    }
}
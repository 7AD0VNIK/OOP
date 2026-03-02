package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class PizzaCourierTest {
    @Test
    void testCourierBatchDelivery() throws InterruptedException {
        PizzaBuffer<Order> storage = new PizzaBuffer<>(5);
        for (int i = 0; i < 3; i++) {
            storage.put(new Order(i));
        }
        PizzaCourier courier = new PizzaCourier(1, 2, 100, storage);
        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(100);

        assertEquals(1, storage.size());

        courier.stop();
        courierThread.interrupt();
    }

    @Test
    void testCourierStopsWhenStorageEmpty() throws InterruptedException {
        PizzaBuffer<Order> storage = new PizzaBuffer<>(5); // empty storage
        PizzaCourier courier = new PizzaCourier(2, 3, 100, storage);
        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(50);

        courier.stop();
        courierThread.interrupt();
        courierThread.join(500);

        assertFalse(courierThread.isAlive(), "Courier thread should have terminated");
    }

}
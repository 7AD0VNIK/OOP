package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PizzaCourierTest {
    @Test
    void testCourierBatchDelivery() throws InterruptedException {
        SynchronizedQueue<Order> storage = new SynchronizedQueue<>(5);
        for (int i = 0; i < 3; i++) {
            Order order = new Order(i);
            storage.put(order, () -> {
                order.setStatus("is on the storage (Initial)");
            });
        }
        PizzaCourier courier = new PizzaCourier(1, 2, 100, storage);
        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(100);

        assertEquals(1, storage.size());

        courier.stop();
        courierThread.interrupt();
    }

}
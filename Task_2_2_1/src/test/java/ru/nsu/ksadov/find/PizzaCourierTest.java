package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @Test
    void testCourierInterruption() throws InterruptedException {
        SynchronizedQueue<Order> storage = new SynchronizedQueue<>(5);

        PizzaCourier courier = new PizzaCourier(1, 2, 5000, storage);
        Thread courierThread = new Thread(courier);

        courierThread.start();
        Thread.sleep(50);

        courierThread.interrupt();
        courierThread.join(1000);

        assertFalse(courierThread.isAlive());
    }

    @Test
    void testCourierStopCoverage() throws InterruptedException {
        SynchronizedQueue<Order> storage = new SynchronizedQueue<>(5);
        PizzaCourier courier = new PizzaCourier(1, 2, 10, storage);
        Thread courierThread = new Thread(courier);
        courierThread.start();

        courier.stop();

        storage.put(new Order(99), () -> {});

        courierThread.join(1000);

        assertFalse(courierThread.isAlive());
    }
}
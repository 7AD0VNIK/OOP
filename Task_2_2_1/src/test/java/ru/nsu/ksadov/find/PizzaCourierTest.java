package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class PizzaCourierTest {
    @Test
    void testCourierBatchDelivery() throws InterruptedException, IOException {
        SynchronizedQueue<Order> storage = new SynchronizedQueue<>(5);
        for (int i = 0; i < 3; i++) {
            Order order = new Order(i);
            storage.put(order, () -> {
                order.setStatus("is on the storage (Initial)");
            });
        }

        Pizzeria pizzeria = new Pizzeria("config.json");
        PizzaCourier courier = new PizzaCourier(1, 2, 100, storage, pizzeria);
        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(100);

        assertEquals(1, storage.size());
        courierThread.interrupt();
    }

    @Test
    void testCourierInterruption() throws InterruptedException, IOException {
        SynchronizedQueue<Order> storage = new SynchronizedQueue<>(5);
        Pizzeria pizzeria = new Pizzeria("config.json");

        PizzaCourier courier = new PizzaCourier(1, 2, 5000, storage, pizzeria);
        Thread courierThread = new Thread(courier);

        courierThread.start();
        Thread.sleep(50);

        courierThread.interrupt();
        courierThread.join(1000);

        assertFalse(courierThread.isAlive());
    }

    @Test
    void testCourierStopCoverage() throws InterruptedException, IOException {
        Pizzeria pizzeria = new Pizzeria("config.json");
        SynchronizedQueue<Order> storage = new SynchronizedQueue<>(5);
        PizzaCourier courier = new PizzaCourier(1, 2, 10, storage, pizzeria);
        Thread courierThread = new Thread(courier);
        courierThread.start();

        storage.put(new Order(99), () -> {});
        Thread.sleep(100);
        courierThread.interrupt();
        courierThread.join(1000);

        assertFalse(courierThread.isAlive(), "Поток курьера должен быть остановлен");
    }
}
package ru.nsu.ksadov.find;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PizzaBakerTest {
    @Test
    void toBakeCycle() throws InterruptedException{
        PizzaBuffer<Order> storage = new PizzaBuffer<>(4);
        PizzaBuffer<Order> queue = new PizzaBuffer<>(0);
        Order order1 = new Order(2);
        Order order2 = new Order(3);

        queue.put(order1);
        queue.put(order2);
        PizzaBaker baker = new PizzaBaker(2, 100, queue, storage);
        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(400);

        assertEquals(0, queue.size());
        assertEquals(2, storage.size());
        baker.stop();
        bakerThread.interrupt();
    }
}
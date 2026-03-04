package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PizzaBakerTest {
    @Test
    void toBakeCycle() throws InterruptedException {
        SynchronizedQueue<Order> storage = new SynchronizedQueue<>(4);
        SynchronizedQueue<Order> queue = new SynchronizedQueue<>(0);
        Order order1 = new Order(2);
        Order order2 = new Order(3);

        queue.put(order1, () -> {
            order2.setStatus("Received (in queue)");
        });
        queue.put(order2, () -> {
            order2.setStatus("Received (in queue)");
        });
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
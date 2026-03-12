package ru.nsu.ksadov.find;

import java.util.List;

/**
 * Доставщик пиццы.
 */
public class PizzaCourier implements Courier {
    private final int id;
    private final int trunkCap;
    private final int delivTime;
    private final SynchronizedQueue<Order> storage;
    private final Pizzeria pizzeria;

    /**
     * Конструктор для создания курьера.
     */
    public PizzaCourier(int id, int trunkCap, int delivTime,
                        SynchronizedQueue<Order> storage, Pizzeria pizzeria) {
        this.id = id;
        this.trunkCap = trunkCap;
        this.delivTime = delivTime;
        this.storage = storage;
        this.pizzeria = pizzeria;
    }

    @Override
    public void run() {
        List<Order> batch = null;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                batch = storage.takeMultiple(trunkCap);

                for (Order order : batch) {
                    order.setStatus("is being delivered by Courier #" + id);
                }

                Thread.sleep(delivTime);

                for (Order order : batch) {
                    order.setStatus("delivered to the customer!");
                }
            }
        } catch (InterruptedException e) {
            if (batch != null) {
                batch.forEach(pizzeria::returnToStorage);
            }
            System.out.println("[Courier #" + id + "] stopped.");
        }
    }
}

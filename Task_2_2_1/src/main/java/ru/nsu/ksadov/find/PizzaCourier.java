package ru.nsu.ksadov.find;

import java.util.List;

/**
 * Доставщик пиццы.
 */
public class PizzaCourier implements Courier {
    private final int id;
    private final int trunkCap;
    private final int delivTime;
    private final PizzaBuffer<Order> storage;
    private volatile boolean isRunning = true;

    /**
     *
     * @param id
     * @param trunkCap
     * @param delivTime
     * @param storage
     */
    public PizzaCourier(int id, int trunkCap, int delivTime, PizzaBuffer<Order> storage) {
        this.id = id;
        this.trunkCap = trunkCap;
        this.delivTime = delivTime;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                List<Order> batch = storage.takeMultiple(trunkCap);

                for (Order order : batch) {
                    order.setStatus("is being delivered by Courier #" + id);
                }

                Thread.sleep(delivTime);

                for (Order order : batch) {
                    order.setStatus("delivered to the customer!");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("[Courier #" + id + "] stopped.");
        }
    }

    @Override
    public void stop() {
        this.isRunning = false; }
}

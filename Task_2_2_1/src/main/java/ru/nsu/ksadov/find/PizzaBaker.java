package ru.nsu.ksadov.find;

public class PizzaBaker implements Baker{
    private final int id;
    private final int cookingSpeed;
    private final PizzaBuffer<Order> orderQueue;
    private final PizzaBuffer<Order> storage;
    private volatile boolean isRunning = true;

    public PizzaBaker(int id, int cookingSpeed, PizzaBuffer<Order> orderQueue, PizzaBuffer<Order> storage) {
        this.id = id;
        this.cookingSpeed = cookingSpeed;
        this.orderQueue = orderQueue;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                Order order = orderQueue.take();

                order.setStatus("is being prepared by Baker #" + id);
                Thread.sleep(cookingSpeed);

                order.setStatus("is ready and waiting for storage (Baker #" + id + ")");
                storage.put(order);

                order.setStatus("is on the storage (Baker #" + id + ")");
            }
        } catch (InterruptedException e){
            System.out.println("[Baker #" + id + "] was interrupted and stopped.");
        }
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }
}

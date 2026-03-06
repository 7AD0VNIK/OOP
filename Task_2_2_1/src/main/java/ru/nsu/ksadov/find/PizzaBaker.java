package ru.nsu.ksadov.find;

/**
 * Пицца мэйкер.
 */
public class PizzaBaker implements Baker {
    private final int id;
    private final int cookingSpeed;
    private final SynchronizedQueue<Order> orderQueue;
    private final SynchronizedQueue<Order> storage;
    private final Pizzeria pizzeria;

    /**
     * Конструктор для создания пекаря.
     */
    public PizzaBaker(int id, int cookingSpeed, SynchronizedQueue<Order> orderQueue,
                      SynchronizedQueue<Order> storage, Pizzeria pizzeria) {
        this.id = id;
        this.cookingSpeed = cookingSpeed;
        this.orderQueue = orderQueue;
        this.storage = storage;
        this.pizzeria = pizzeria;
    }

    @Override
    public void run() {
        Order currOrd = null;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                currOrd = orderQueue.take();

                currOrd.setStatus("is being prepared by Baker #" + id);
                Thread.sleep(cookingSpeed);

                Order finalOrder = currOrd;
                storage.put(finalOrder, () -> {
                    finalOrder.setStatus("is on the storage");
                });
                currOrd = null;
            }
        } catch (InterruptedException e) {
            if (currOrd != null) {
                pizzeria.returnToOrderQueue(currOrd);
            }
            System.out.println("[Baker #" + id + "] was interrupted and stopped.");
        }
    }

}

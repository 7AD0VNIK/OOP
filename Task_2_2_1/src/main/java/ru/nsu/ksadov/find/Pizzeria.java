package ru.nsu.ksadov.find;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Пиццерия.
 */
public class Pizzeria {
    private final PizzaBuffer<Order> storage;
    private final PizzaBuffer<Order> orderQueue;
    private final List<Thread> workerThreads = new ArrayList<>();
    private final List<Baker> bakers = new ArrayList<>();
    private final List<Courier> couriers = new ArrayList<>();
    private final AtomicInteger orderIdGenerator = new AtomicInteger(1);
    private volatile boolean isOpen = false;

    /**
     * Конструктор пиццерии.
     */
    public Pizzeria(String configPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream(configPath);
        if (is == null) {
            throw new FileNotFoundException("Config not found: " + configPath);
        }

        PizzaConfig config = mapper.readValue(is, PizzaConfig.class);

        this.orderQueue = new PizzaBuffer<>(0);
        this.storage = new PizzaBuffer<>(config.storageCapacity);

        for (PizzaConfig.BakerConfig data : config.bakers) {
            PizzaBaker baker = new PizzaBaker(data.id, data.cookingSpeed, orderQueue, storage);
            bakers.add(baker);
            workerThreads.add(new Thread(baker));
        }

        for (PizzaConfig.CourierConfig data : config.couriers) {
            PizzaCourier courier = new PizzaCourier(data.id, data.trunkCapacity,
                    data.deliverySpeed, storage);
            couriers.add(courier);
            workerThreads.add(new Thread(courier));
        }
    }

    /**
     * Старт.
     */
    public void start() {
        isOpen = true;
        System.out.println("--- Pizzeria is now OPEN ---");
        for (Thread t : workerThreads) {
            t.start();
        }
    }

    /**
     * Создание заказа.
     */
    public void createOrder() {
        if (!isOpen) {
            return;
        }
        try {
            Order order = new Order(orderIdGenerator.getAndIncrement());
            order.setStatus("Received (in queue)");
            orderQueue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void saveUnfinishedOrders() {
        List<Order> unfinished = new ArrayList<>();
        unfinished.addAll(orderQueue.getAllAndClear());
        unfinished.addAll(storage.getAllAndClear());

        try (ObjectOutputStream oos = new
                ObjectOutputStream(new FileOutputStream("unfinished_orders.ser"))) {
            oos.writeObject(unfinished);
            System.out.println("Saved " + unfinished.size() + " unfinished orders to file.");
        } catch (IOException e) {
            System.err.println("Failed to save orders: " + e.getMessage());
        }
    }

    /**
     * Остановка и сохранение.
     */
    public void stopAndSave() {
        isOpen = false;
        System.out.println("--- Pizzeria is CLOSING. Saving state... ---");

        bakers.forEach(Baker::stop);
        couriers.forEach(Courier::stop); // Каст не нужен, у интерфейса Courier есть stop()
        workerThreads.forEach(Thread::interrupt);

        saveUnfinishedOrders();
    }
}
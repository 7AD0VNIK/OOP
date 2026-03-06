package ru.nsu.ksadov.find;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Пиццерия.
 */
public class Pizzeria {
    private final SynchronizedQueue<Order> storage;
    private final SynchronizedQueue<Order> orderQueue;
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
        InputStream is;

        File externalConfig = new File(configPath);
        if (externalConfig.exists() && externalConfig.isFile()) {
            is = new java.io.FileInputStream(externalConfig);
        } else {
            is = getClass().getClassLoader().getResourceAsStream(configPath);
        }

        if (is == null) {
            throw new FileNotFoundException("Config not found: " + configPath);
        }

        PizzeriaConfig config = mapper.readValue(is, PizzeriaConfig.class);

        this.orderQueue = new SynchronizedQueue<>(0);
        this.storage = new SynchronizedQueue<>(config.storageCapacity);

        File savedFile = new File("unfinished_orders.json");
        if (savedFile.exists()) {
            List<Order> restored = mapper.readValue(savedFile, new TypeReference<List<Order>>(){});
            for (Order o : restored) {
                orderQueue.addFirst(o);
            }
            savedFile.delete();
        }

        for (PizzeriaConfig.BakerConfig data : config.bakers) {
            PizzaBaker baker = new PizzaBaker(data.id, data.cookingSpeed, orderQueue,
                    storage, this);
            workerThreads.add(new Thread(baker));
        }

        for (PizzeriaConfig.CourierConfig data : config.couriers) {
            PizzaCourier courier = new PizzaCourier(data.id, data.trunkCapacity,
                    data.deliverySpeed, storage, this);
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
            orderQueue.put(order, () -> {
                order.setStatus("Received (in queue)");
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void saveUnfinishedOrders() {
        List<Order> unfinished = new ArrayList<>();
        unfinished.addAll(orderQueue.getAllAndClear());
        unfinished.addAll(storage.getAllAndClear());

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("unfinished_orders.json"), unfinished);
            System.out.println("Saved " + unfinished.size() + " orders to JSON.");
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

        workerThreads.forEach(Thread::interrupt);
        for (Thread t : workerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        saveUnfinishedOrders();
    }

    /**
     * Возвращене в очередь.
     */
    public void returnToOrderQueue(Order order) {
        if (order != null) {
            orderQueue.addFirst(order);
        }
    }

    /**
     * Возвращение на склад.
     */
    public void returnToStorage(Order order) {
        if (order != null) {
            storage.addFirst(order);
        }
    }
}
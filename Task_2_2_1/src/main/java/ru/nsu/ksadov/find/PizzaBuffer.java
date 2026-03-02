package ru.nsu.ksadov.find;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Буфер.
 */
public class PizzaBuffer<T> {
    private final Queue<T> items = new LinkedList<>();
    private final int cap;

    /**
     * Конструктор буфера.
     */
    public PizzaBuffer(int cap) {
        this.cap = cap;
    }

    /**
     * Положить в буфер.
     */
    public synchronized void put(T item) throws InterruptedException {
        while(cap > 0 && items.size() >= cap) {
            wait();
        }
        items.add(item);
        notifyAll();
    }

    /**
     * Взять один из буфера.
     */
    public synchronized T take() throws InterruptedException {
        while (items.isEmpty()) {
            wait();
        }
        T item = items.poll();
        notifyAll();
        return item;
    }

    /**
     * Взять несколько из буфера.
     */
    public synchronized List<T> takeMultiple(int maxCount) throws InterruptedException {
        while(items.isEmpty()) {
            wait();
        }

        List<T> batch = new LinkedList<>();
        int toTake = Math.min(maxCount, items.size());
        for(int i = 0; i < toTake; i++) {
            batch.add(items.poll());
        }
        notifyAll();
        return batch;
    }

    /**
     * Размер буфера.
     */
    public synchronized int size() {
        return items.size();
    }

    /**
     * Взять всё из буфера.
     */
    public synchronized List<T> getAllAndClear() {
        List<T> remaining = new LinkedList<>(items);
        items.clear();
        return remaining;
    }
}

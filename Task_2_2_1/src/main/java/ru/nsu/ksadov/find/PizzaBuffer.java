package ru.nsu.ksadov.find;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PizzaBuffer<T> {
    private final Queue<T> items = new LinkedList<>();
    private final int cap;

    public PizzaBuffer(int cap) {
        this.cap = cap;
    }

    public synchronized void put(T item) throws InterruptedException {
        while(cap > 0 && items.size() >= cap) {
            wait();
        }
        items.add(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (items.isEmpty()) {
            wait();
        }
        T item = items.poll();
        notifyAll();
        return item;
    }

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

    public synchronized int size() {
        return items.size();
    }

    public synchronized List<T> getAllAndClear() {
        List<T> remaining = new LinkedList<>(items);
        items.clear();
        return remaining;
    }
}

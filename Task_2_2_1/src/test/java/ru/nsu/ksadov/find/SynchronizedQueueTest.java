package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SynchronizedQueueTest {
    @Test
    void putAndTake() throws InterruptedException {
        SynchronizedQueue<String> buff = new SynchronizedQueue<>(2);
        buff.put("Pizza");
        assertEquals(1, buff.size());
        assertEquals("Pizza", buff.take());

    }

    @Test
    void takeMul() throws InterruptedException {
        SynchronizedQueue<Integer> buff = new SynchronizedQueue<>(5);
        for (int i = 0; i < 4; i++) {
            buff.put(i);
        }
        List<Integer> batch = buff.takeMultiple(3);
        assertEquals(3, batch.size());
        assertEquals(1, buff.size());
    }

    @Test
    void takeAll() throws InterruptedException {
        SynchronizedQueue<Integer> buff = new SynchronizedQueue<>(6);
        for (int i = 0; i < 6; i++) {
            buff.put(i);
        }
        List<Integer> batch = buff.getAllAndClear();
        assertEquals(6, batch.size());
        assertEquals(0, buff.size());
    }
}
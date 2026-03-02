package ru.nsu.ksadov.find;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PizzaBufferTest {
    @Test
    void putAndTake() throws InterruptedException {
        PizzaBuffer<String> buff = new PizzaBuffer<>(2);
        buff.put("Pizza");
        assertEquals(1, buff.size());
        assertEquals("Pizza", buff.take());

    }

    @Test
    void takeMul() throws InterruptedException {
        PizzaBuffer<Integer> buff = new PizzaBuffer<>(5);
        for (int i = 0; i < 4; i++) {
            buff.put(i);
        }
        List<Integer> batch = buff.takeMultiple(3);
        assertEquals(3, batch.size());
        assertEquals(1, buff.size());
    }

    @Test
    void takeAll() throws InterruptedException {
        PizzaBuffer<Integer> buff = new PizzaBuffer<>(6);
        for(int i = 0; i < 6; i++) {
            buff.put(i);
        }
        List<Integer> batch = buff.getAllAndClear();
        assertEquals(6, batch.size());
        assertEquals(0, buff.size());
    }
}
package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class PizzeriaTest {
    @Test
    void testPizzeriaInitialization() throws IOException {
        Pizzeria pizzeria = new Pizzeria("config.json");
        assertNotNull(pizzeria);
    }

    @Test
    void testStartAndStopLifecycle() throws IOException, InterruptedException {
        Pizzeria pizzeria = new Pizzeria("config.json");

        assertDoesNotThrow(pizzeria::start, "метод start() не должен выбрасывать исключений");

        for (int i = 0; i < 3; i++) {
            pizzeria.createOrder();
        }

        Thread.sleep(500);

        pizzeria.stopAndSave();
        File savedState = new File("unfinished_orders.ser");
        assertTrue(savedState.exists(), "файл unfinished_orders.ser должен быть создан после остановки");

        savedState.delete();
    }

    @Test
    void testOrderCreationWhenClosed() throws IOException {
        Pizzeria pizzeria = new Pizzeria("config.json");
        pizzeria.createOrder();
    }
}
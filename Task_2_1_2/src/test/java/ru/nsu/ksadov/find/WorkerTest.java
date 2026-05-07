package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * .
 */
class WorkerTest {

    @Test
    void testIsPrime() {
        assertTrue(Worker.isPrime(2));
        assertTrue(Worker.isPrime(17));
        assertTrue(Worker.isPrime(997));

        assertFalse(Worker.isPrime(0));
        assertFalse(Worker.isPrime(1));
        assertFalse(Worker.isPrime(-5));
        assertFalse(Worker.isPrime(10));
        assertFalse(Worker.isPrime(25));
    }

}
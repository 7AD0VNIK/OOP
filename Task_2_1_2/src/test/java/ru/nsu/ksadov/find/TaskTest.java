package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * .
 */
class TaskTest {
    @Test
    void testTaskCreationAndGetters() {
        Task task = new Task(10, 20);
        assertEquals(10, task.getStart(), "Start equals 10");
        assertEquals(20, task.getEnd(), "End equals 20");
    }
}
package ru.nsu.ksadov.find.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PointTest {
    @Test
    void testPointCreationAndGetters() {
        Point p = new Point(5, 10);
        assertEquals(5, p.x());
        assertEquals(10, p.y());
    }

    @Test
    void testPointEquality() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(2, 3);
        Point p3 = new Point(3, 2);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }
}
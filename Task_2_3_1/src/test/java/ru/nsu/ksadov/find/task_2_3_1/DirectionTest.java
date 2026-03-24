package ru.nsu.ksadov.find.task_2_3_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {
    @Test
    void testEnumValues() {
        Direction[] dirs = Direction.values();
        assertEquals(4, dirs.length);
        assertNotNull(Direction.valueOf("UP"));
        assertNotNull(Direction.valueOf("DOWN"));
        assertNotNull(Direction.valueOf("LEFT"));
        assertNotNull(Direction.valueOf("RIGHT"));
    }
}
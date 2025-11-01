package ru.nsu.ksadov.Hash_Table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ConcurrentModificationException;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HashTableTest {

    private HashTable<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
    }

    @Test
    void testPutAndGet() {
        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
        assertNull(table.get("missing"));
    }

    @Test
    void testUpdate() {
        table.update("one", 10);
        assertEquals(10, table.get("one"));
    }

    @Test
    void testRemove() {
        assertEquals(2, table.remove("two"));
        assertNull(table.get("two"));
        assertEquals(2, table.size());
    }

    @Test
    void testContainsKey() {
        assertTrue(table.containsKey("three"));
        assertFalse(table.containsKey("four"));
    }

    @Test
    void testIterator() {
        int count = 0;
        for (HashTable.Entry<String, Integer> e : table) {
            assertNotNull(e.getKey());
            assertNotNull(e.getValue());
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testConcurrentModification() {
        assertThrows(ConcurrentModificationException.class, () -> {
            for (HashTable.Entry<String, Integer> e : table) {
                table.put("new", 99);
            }
        });
    }

    @Test
    void testEquals() {
        HashTable<String, Integer> other = new HashTable<>();
        other.put("one", 1);
        other.put("two", 2);
        other.put("three", 3);
        assertEquals(table, other);
    }

    @Test
    void testToString() {
        String str = table.toString();
        assertTrue(str.contains("one=1"));
        assertTrue(str.contains("two=2"));
        assertTrue(str.contains("three=3"));
    }
}

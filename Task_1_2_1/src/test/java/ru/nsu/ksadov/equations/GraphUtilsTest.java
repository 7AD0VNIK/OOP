package ru.nsu.ksadov.equations;

import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class GraphUtilsTest {

    @Test
    public void testGraphUtilsAdjMap() {
        Graph g = new AdjList();
        g.addEdge(0, 1);
        g.addEdge(0, 2);

        Map<Integer, Set<Integer>> map = GraphUtils.adjMap(g);

        assertEquals(Set.of(0, 1, 2), map.keySet());
        assertEquals(Set.of(1, 2), map.get(0));
        assertTrue(map.get(1).isEmpty());
        assertTrue(map.get(2).isEmpty());
    }

    @Test
    public void testGraphUtilsAreEqual() {
        Graph g1 = new AdjList();
        g1.addEdge(0, 1);
        g1.addEdge(1, 2);

        Graph g2 = new AdjList();
        g2.addEdge(0, 1);
        g2.addEdge(1, 2);

        Graph g3 = new AdjList();
        g3.addEdge(0, 2);

        assertTrue(GraphUtils.areEqual(g1, g2));
        assertFalse(GraphUtils.areEqual(g1, g3));
    }
}
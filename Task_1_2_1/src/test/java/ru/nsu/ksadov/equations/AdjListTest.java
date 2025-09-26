package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class AdjListTest {

    @Test
    void testAddAndRemoveVertex() {
        Graph g = new AdjList();
        g.addVertex(1);
        g.addVertex(2);
        assertTrue(g.getVertices().contains(1));
        assertTrue(g.getVertices().contains(2));

        g.removeVertex(1);
        assertFalse(g.getVertices().contains(1));
    }

    @Test
    void testAddAndRemoveEdge() {
        Graph g = new AdjList();
        g.addEdge(1, 2);
        assertTrue(g.neighbors(1).contains(2));

        g.removeEdge(1, 2);
        assertFalse(g.neighbors(1).contains(2));
    }

    @Test
    void testNeighbors() {
        Graph g = new AdjList();
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        List<Integer> nbrs = g.neighbors(1);
        assertEquals(2, nbrs.size());
        assertTrue(nbrs.contains(2));
        assertTrue(nbrs.contains(3));
    }
}

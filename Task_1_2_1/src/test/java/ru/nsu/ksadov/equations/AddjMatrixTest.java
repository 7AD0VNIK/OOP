package ru.nsu.ksadov.equations;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AddjMatrixTest {

    @Test
    void testAddAndRemoveVertex() {
        Graph g = new AddjMatrix();
        g.addVertex(0);
        g.addVertex(1);
        assertTrue(g.getVertices().contains(0));
        assertTrue(g.getVertices().contains(1));

        g.removeVertex(0);
        assertFalse(g.getVertices().contains(0));
    }

    @Test
    void testAddAndRemoveEdge() {
        Graph g = new AddjMatrix();
        g.addEdge(0, 1);
        assertTrue(g.neighbors(0).contains(1));

        g.removeEdge(0, 1);
        assertFalse(g.neighbors(0).contains(1));
    }

    @Test
    void testNeighbors() {
        Graph g = new AddjMatrix();
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        List<Integer> nbrs = g.neighbors(0);
        assertEquals(2, nbrs.size());
    }
}

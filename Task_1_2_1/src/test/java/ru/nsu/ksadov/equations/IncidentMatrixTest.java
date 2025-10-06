package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class IncidentMatrixTest {

    @Test
    void testAddAndRemoveVertex() {
        Graph g = new IncidentMatrix();
        g.addVertex(0);
        g.addVertex(1);
        assertTrue(g.getVertices().contains(0));
        assertTrue(g.getVertices().contains(1));

        g.removeVertex(0);
        assertFalse(g.getVertices().contains(0));
    }

    @Test
    void testAddAndRemoveEdge() {
        Graph g = new IncidentMatrix();
        g.addEdge(0, 1);
        assertTrue(g.neighbors(0).contains(1));

        g.removeEdge(0, 1);
        assertFalse(g.neighbors(0).contains(1));
    }

    @Test
    void testNeighbors() {
        Graph g = new IncidentMatrix();
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        List<Integer> nbrs = g.neighbors(0);
        assertEquals(2, nbrs.size());
    }

    @Test
    void testToString() {
        Graph g = new IncidentMatrix();
        g.addEdge(1, 2);
        g.addEdge(1, 3);

        String result = g.toString();
        assertTrue(result.contains("1") || result.contains("2") || result.contains("3"));
    }

    @Test
    void testEquals() {
        Graph g1 = new IncidentMatrix();
        Graph g2 = new IncidentMatrix();
        Graph g3 = new IncidentMatrix();

        g1.addEdge(1, 2);
        g2.addEdge(1, 2);
        g3.addEdge(1, 3);

        assertTrue(g1.equals(g2));
        assertFalse(g1.equals(g3));
    }

    @Test
    void testHashCodeConsistency() {
        Graph g1 = new IncidentMatrix();
        Graph g2 = new IncidentMatrix();

        g1.addEdge(1, 2);
        g2.addEdge(1, 2);

        assertEquals(g1.hashCode(), g2.hashCode());
    }
}

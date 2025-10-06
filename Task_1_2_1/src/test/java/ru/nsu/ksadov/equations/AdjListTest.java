package ru.nsu.ksadov.equations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Test
    void testEquals() {
        Graph g1 = new AdjList();
        Graph g2 = new AdjList();
        Graph g3 = new AdjList();

        g1.addEdge(1, 2);
        g2.addEdge(1, 2);
        g3.addEdge(1, 3);

        assertTrue(g1.equals(g2));
        assertFalse(g1.equals(g3));
    }

    @Test
    void testHashCodeConsistency() {
        Graph g1 = new AdjList();
        Graph g2 = new AdjList();

        g1.addEdge(1, 2);
        g2.addEdge(1, 2);

        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    void testEmptyGraph() {
        Graph g = new AdjList();
        assertTrue(g.getVertices().isEmpty());
        assertTrue(g.neighbors(1).isEmpty());
    }

    @Test
    void testNoDuplicateEdges() {
        Graph g = new AdjList();
        g.addEdge(1, 2);
        g.addEdge(1, 2);
        assertEquals(1, g.neighbors(1).size());
    }

    @Test
    void testGetVertices() {
        Graph g = new AdjList();
        g.addVertex(1);
        g.addVertex(2);
        assertEquals(2, g.getVertices().size());
    }

    @Test
    void testReadFromFile() throws IOException {
        Graph g = new AdjList();
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, List.of("2 1", "1 2"));
        g.readFromFile(tempFile);
        assertTrue(g.neighbors(1).contains(2));
        Files.delete(tempFile);
    }
}

package ru.nsu.ksadov.equations;

import java.util.List;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

public class AlgorithmsTest {

    @Test
    public void testEmptyGraph() {
        Graph g = new AdjList();
        g.addVertex(0);
        g.addVertex(1);

        List<Integer> orderDFS = Algorithms.topologicalSortDFS(g);
        List<Integer> orderKahn = Algorithms.topologicalSortKahn(g);

        Assertions.assertEquals(2, orderDFS.size());
        Assertions.assertEquals(2, orderKahn.size());
    }

    @Test
    public void testLinearGraph() {
        Graph g = new AdjList();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        List<Integer> dfsOrder = Algorithms.topologicalSortDFS(g);
        List<Integer> kahnOrder = Algorithms.topologicalSortKahn(g);

        Assertions.assertEquals(List.of(0, 1, 2, 3), dfsOrder);
        Assertions.assertEquals(List.of(0, 1, 2, 3), kahnOrder);
    }

    @Test
    public void testCycleDetectionDFS() {
        Graph g = new AdjList();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            Algorithms.topologicalSortDFS(g);
        });
    }

    @Test
    public void testCycleDetectionKahn() {
        Graph g = new AdjList();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            Algorithms.topologicalSortKahn(g);
        });
    }
}

package ru.nsu.ksadov.equations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Класс с алгоритмами работы над графами.
 */
public class Algorithms {

    /**
     * Топологическая сортировка с помощью DFS.
     * @return порядок вершин
     */
    public static List<Integer> topologicalSortDFS(Graph g) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> onStack = new HashSet<>();
        LinkedList<Integer> order = new LinkedList<>();

        for (int v : g.getVertices()) {
            if (!visited.contains(v)) {
                dfs(v, g, visited, onStack, order);
            }
        }
        return order;
    }

    private static void dfs(int v, Graph g,
                            Set<Integer> visited,
                            Set<Integer> onStack,
                            LinkedList<Integer> order) {
        if (onStack.contains(v)) {
            throw new IllegalStateException("Цикл обнаружен");
        }
        if (visited.contains(v)) return;

        onStack.add(v);
        for (int u : g.neighbors(v)) {
            dfs(u, g, visited, onStack, order);
        }
        onStack.remove(v);
        visited.add(v);
        order.addFirst(v);
    }

    /**
     * Топологическая сортировка алгоритмом Кана.
     */
    public static List<Integer> topologicalSortKahn(Graph g) {
        Map<Integer, Integer> indegree = new HashMap<>();
        for (int v : g.getVertices()) {
            indegree.put(v, 0);
        }
        for (int v : g.getVertices()) {
            for (int u : g.neighbors(v)) {
                indegree.put(u, indegree.get(u) + 1);
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (var e : indegree.entrySet()) {
            if (e.getValue() == 0) q.add(e.getKey());
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.poll();
            order.add(v);
            for (int u : g.neighbors(v)) {
                indegree.put(u, indegree.get(u) - 1);
                if (indegree.get(u) == 0) q.add(u);
            }
        }

        if (order.size() != g.getVertices().size()) {
            throw new IllegalStateException("Цикл обнаружен");
        }

        return order;
    }
}

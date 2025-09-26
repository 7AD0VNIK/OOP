package ru.nsu.ksadov.equations;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Утилиты для работы с графами.
 */
public class GraphUtils {

    /**
     * Создает список смежности графа.
     */
    public static Map<Integer, Set<Integer>> adjMap(Graph g) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int v : g.getVertices()) {
            map.put(v, new HashSet<>(g.neighbors(v)));
        }
        return map;
    }

    /**
     * Проверяет эквивалентность двух графов.
     * Графы считаются равными, если имеют одинаковые множества вершин и рёбер.
     */
    public static boolean areEqual(Graph g1, Graph g2) {
        Map<Integer, Set<Integer>> a1 = adjMap(g1);
        Map<Integer, Set<Integer>> a2 = adjMap(g2);
        return a1.equals(a2);
    }
}

package ru.nsu.ksadov.equations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * Представление графа в виде списка смежности.
 */
public class AdjList implements Graph {
    private final Map<Integer, List<Integer>> adj = new HashMap<>();

    @Override
    public void addVertex(int v) {
        adj.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    public void removeVertex(int v) {
        adj.remove(v);
        for (List<Integer> nbrs : adj.values()) {
            nbrs.remove((Integer) v); // remove by value
        }
    }

    @Override
    public void addEdge(int v1, int v2) {
        addVertex(v1);
        addVertex(v2);
        if (!adj.get(v1).contains(v2)) adj.get(v1).add(v2);
    }

    @Override
    public void removeEdge(int v1, int v2) {
        List<Integer> list = adj.get(v1);
        if (list != null) list.remove((Integer) v2);
    }

    @Override
    public List<Integer> neighbors(int v) {
        return Collections.unmodifiableList(adj.getOrDefault(v, new ArrayList<>()));
    }

    @Override
    public Set<Integer> getVertices() {
        return Collections.unmodifiableSet(adj.keySet());
    }

    @Override
    public void readFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        String[] header = lines.get(0).split("\\s+");
        int m = Integer.parseInt(header[1]);
        for (int i = 1; i <= m; i++) {
            String[] edge = lines.get(i).split("\\s+");
            int from = Integer.parseInt(edge[0]);
            int to = Integer.parseInt(edge[1]);
            addEdge(from, to);
        }
    }


    @Override
    public String toString() {
        return adj.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Graph)) return false;
        return GraphUtils.areEqual(this, (Graph) obj);
    }

    @Override
    public int hashCode() {
        return GraphUtils.adjMap(this).hashCode();
    }
}

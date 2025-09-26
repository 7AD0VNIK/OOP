package ru.nsu.ksadov.equations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

/**
* Представление графа в виде матрицы смежности.
 */
public class AddjMatrix implements Graph {
    private final List<Integer> vertices = new ArrayList<>();
    private final List<List<Integer>> matrix = new ArrayList<>(); // matrix.get(i).get(j) == 1 если i->j

    @Override
    public void addVertex(int v) {
        if (vertices.contains(v)) return;
        vertices.add(v);
        for (List<Integer> row : matrix) row.add(0);
        List<Integer> newRow = new ArrayList<>(Collections.nCopies(vertices.size(), 0));
        matrix.add(newRow);
    }

    @Override
    public void removeVertex(int v) {
        int idx = vertices.indexOf(v);
        if (idx == -1) return;
        vertices.remove(idx);
        matrix.remove(idx);
        for (List<Integer> row : matrix) row.remove(idx);
    }

    @Override
    public void addEdge(int v1, int v2) {
        addVertex(v1);
        addVertex(v2);
        int i = vertices.indexOf(v1);
        int j = vertices.indexOf(v2);
        matrix.get(i).set(j, 1);
    }

    @Override
    public void removeEdge(int v1, int v2) {
        int i = vertices.indexOf(v1);
        int j = vertices.indexOf(v2);
        if (i == -1 || j == -1) return;
        matrix.get(i).set(j, 0);
    }

    @Override
    public List<Integer> neighbors(int v) {
        int i = vertices.indexOf(v);
        if (i == -1) return Collections.emptyList();
        List<Integer> res = new ArrayList<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (matrix.get(i).get(j) == 1) res.add(vertices.get(j));
        }
        return res;
    }

    @Override
    public Set<Integer> getVertices() {
        return new LinkedHashSet<>(vertices); // порядок вершин сохранён
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
        StringBuilder sb = new StringBuilder();
        for (List<Integer> row : matrix) {
            sb.append(row.toString()).append("\n");
        }
        return sb.toString();
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

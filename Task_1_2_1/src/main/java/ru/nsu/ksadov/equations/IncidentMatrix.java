package ru.nsu.ksadov.equations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
* Представление графа в виде матрицы инцидентности.
 */
public class IncidentMatrix implements Graph {
    private final List<Integer> vertices = new ArrayList<>();
    private final List<int[]> matrix = new ArrayList<>(); // length of each int[] == edges.size()
    private final List<int[]> edges = new ArrayList<>();  // пары [from,to]

    @Override
    public void addVertex(int v) {
        if (vertices.contains(v)) {
            return;
        }
        vertices.add(v);
        matrix.add(new int[edges.size()]);
    }

    @Override
    public void removeVertex(int v) {
        int idx = vertices.indexOf(v);
        if (idx == -1) {
            return;
        }
        vertices.remove(idx);
        matrix.remove(idx);
    }

    @Override
    public void addEdge(int v1, int v2) {
        addVertex(v1);
        addVertex(v2);
        edges.add(new int[]{v1, v2});
        int newCols = edges.size();
        for (int i = 0; i < matrix.size(); i++) {
            int[] old = matrix.get(i);
            int[] nw = Arrays.copyOf(old, newCols);
            int vertex = vertices.get(i);
            if (vertex == v1) {
                nw[newCols - 1] = 1;
            }
            else {
                if (vertex == v2) {
                    nw[newCols - 1] = -1;
                }
                else {
                    nw[newCols - 1] = 0;
                }
            }
            matrix.set(i, nw);
        }
    }

    @Override
    public void removeEdge(int v1, int v2) {
        int idx = -1;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i)[0] == v1 && edges.get(i)[1] == v2) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            return;
        }
        edges.remove(idx);
        for (int i = 0; i < matrix.size(); i++) {
            int[] old = matrix.get(i);
            int[] nw = new int[old.length - 1];
            for (int j = 0, k = 0; j < old.length; j++) {
                if (j != idx) {
                    nw[k++] = old[j];
                }
            }
            matrix.set(i, nw);
        }
    }

    @Override
    public List<Integer> neighbors(int v) {
        int idx = vertices.indexOf(v);
        if (idx == -1) return Collections.emptyList();
        int[] row = matrix.get(idx);
        List<Integer> res = new ArrayList<>();
        for (int j = 0; j < row.length; j++) {
            if (row[j] == 1) { // источник — смотрим, куда ведёт это ребро
                res.add(edges.get(j)[1]);
            }
        }
        return res;
    }

    @Override
    public Set<Integer> getVertices() {
        return new LinkedHashSet<>(vertices);
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
        for (int[] row : matrix) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Graph)) {
            return false;
        }
        return GraphUtils.areEqual(this, (Graph) obj);
    }

    @Override
    public int hashCode() {
        return GraphUtils.adjMap(this).hashCode();
    }
}

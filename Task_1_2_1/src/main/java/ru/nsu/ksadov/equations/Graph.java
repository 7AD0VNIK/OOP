package ru.nsu.ksadov.equations;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Общий интерфейс для всех представлений графа.
 * Поддерживает базовые операции:
 * 1. Добавление и удаление вершины.
 * 2. Добавление и удаление ребра.
 * 3. Получение соседей вершины.
 * 4. Чтение из файла фиксированного формата.
 * 5. Дополнительные операции (например, получение множества вершин).
 */
public interface Graph {

    /**
     * Добавляет вершину в граф.
     */
    void addVertex(int v);

    /**
     * Удаляет вершину из графа вместе со всеми рёбрами.
     */
    void removeVertex(int v);

    /**
     * Добавляет ориентированное ребро.
     */
    void addEdge(int from, int to);

    /**
     * Удаляет ориентированное ребро.
     */
    void removeEdge(int from, int to);

    /**
     * Получает список соседей вершины.
     */
    List<Integer> neighbors(int v);

    /**
     * Возвращает множество всех вершин графа.
     */
    Set<Integer> getVertices();

    /**
     * Загружает граф из файла фиксированного формата.
     * Первая строка: n m (вершины и рёбера)
     * Далее m строк: from to
     *
     * @param path путь к файлу
     * @throws IOException при ошибке чтения
     */
    void readFromFile(Path path) throws IOException;

    /**
     *Сравнивает данный граф с указанным объектом на равенство.
     */
    @Override
    boolean equals(Object o);

    /**
     * Возвращает хэш-код графа.
     */
    @Override
    int hashCode();

    /**
     * Возвращает строковое представление графа.
     */
    @Override
    String toString();
}

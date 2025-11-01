package ru.nsu.ksadov.HashhTable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Параметризованная хеш-таблица реализует хранение пар ключ-значение.
 */
public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] table;
    private int size = 0;
    private int modCount = 0;
    private int capacity;

    /**
     * Создаёт пустую хеш-таблицу стандартной ёмкости (16 элементов).
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Entry<K, V>[]) new Entry[capacity];
    }

    /**
     * Внутренний элемент хеш-таблицы (цепочка для разрешения коллизий).
     */
    public static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        /**
         * Контейнер со следующими полями.
         * @param key ключ
         * @param value значение
         * @param next следующий элемент
         */
        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    /**
     * Добавляет или обновляет элемент по ключу.
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        resizeIfNeeded();

        int index = indexFor(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        table[index] = new Entry<>(key, value, table[index]);
        size++;
        modCount++;
    }

    /**
     * Возвращает значение по ключу или null.
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        Entry<K, V> current = table[indexFor(key)];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Удаляет элемент по ключу.
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = indexFor(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                size--;
                modCount++;
                return current.value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Проверяет наличие ключа в таблице.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Обновляет значение по существующему ключу.
     */
    public void update(K key, V newValue) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        Entry<K, V> current = table[indexFor(key)];
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = newValue;
                modCount++;
                return;
            }
            current = current.next;
        }
        throw new IllegalArgumentException("Key not found: " + key);
    }

    /**
     * Текущее количество элементов в таблице.
     */
    public int size() {
        return size;
    }

    private int indexFor(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private void resizeIfNeeded() {
        if (size >= capacity * LOAD_FACTOR) {
            rehash(capacity * 2);
        }
    }

    @SuppressWarnings("unchecked")
    private void rehash(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[]) new Entry[newCapacity];
        capacity = newCapacity;
        size = 0;
        modCount++;

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    /**
     * Возвращает итератор для обхода всех элементов таблицы.
     * Итератор обходит таблицу в порядке следования элементов
     * во внутреннем массиве и связанных списках (цепочках коллизий).
     * Если в процессе итерации таблица была структурно изменена
     * (например, добавлены или удалены элементы),
     * то при следующем вызове методов {@link Iterator#hasNext()} или {@link Iterator#next()}
     * будет выброшено исключение {@link ConcurrentModificationException}.
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }


    /**
     * Внутренний итератор для обхода элементов таблицы.
     * Итератор реализует fail-fast поведение — если таблица
     * была изменена после создания итератора, дальнейший обход
     * завершится выбросом {@link ConcurrentModificationException}.
     * Обходит элементы в порядке следования ячеек внутреннего массива
     * и связей между элементами в цепочках.
     */
    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private int index = 0;
        private Entry<K, V> current = null;
        private final int expectedModCount = modCount;

        /**
        * Проверяет, есть ли в таблице следующий элемент для итерации.
        */
         @Override
        public boolean hasNext() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (current != null && current.next != null) {
                return true;
            }

            while (index < capacity && table[index] == null) {
                index++;
            }

            return index < capacity;
        }

        /**
         * Возвращает следующий элемент таблицы.
         */
        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the table");
            }

            if (current == null || current.next == null) {
                current = table[index++];
            } else {
                current = current.next;
            }
            return current;
        }
    }

    /**
     * Сравнивает текущую таблицу с другим объектом на равенство.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashTable<?, ?> other)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }

        for (Entry<K, V> entry : this) {
            @SuppressWarnings("unchecked")
            V value = ((HashTable<K, V>) other).get(entry.key);
            if (value != entry.value && (value == null || !value.equals(entry.value))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Возвращает строковое представление таблицы в виде списка пар ключ=значение.
     * Формат вывода: <pre>{@code {key1=value1, key2=value2, ...}}</pre>
     * @return строковое представление таблицы
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Entry<K, V> entry : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry);
            first = false;
        }

        sb.append("}");
        return sb.toString();
    }
}

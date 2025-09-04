package ru.nsu.k.sadov.Task_1_1_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    @Test
    void testEmpty() {
        int[] arr = {};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testSingleElement() {
        int[] arr = {42};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testSorted() {
        int[] arr = {1, 2, 3, 4, 5};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testDuplicates() {
        int[] arr = {4, 5, 3, 5, 2, 1, -1};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{-1, 1, 2, 3, 4, 5, 5}, arr);
    }

    @Test
    void testNegativeNumbers() {
        int[] arr = {-3, -1, -7, 2, 0, 5};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{-7, -3, -1, 0, 2, 5}, arr);
    }

    @Test
    void testAllEqualElements() {
        int[] arr = {7, 7, 7, 7, 7};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, arr);
    }
}
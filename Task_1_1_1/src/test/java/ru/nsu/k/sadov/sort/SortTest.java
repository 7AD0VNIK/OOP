package ru.nsu.k.sadov.sort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

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
        int[] arr = {-3, -1, -7, 2, 5};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{-7, -3, -1, 2, 5}, arr);
    }

    @Test
    void testAllEqualElements() {
        int[] arr = {7, 7, 7, 7, 7};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, arr);
    }

    @Test
    void testLargeArray() {
        int[] arr = new int[1000];
        int[] expected = new int[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr.length - i - 1;
            expected[i] = i;
        }
        Sort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testArrayWithZero() {
        int[] arr = {0, 5, -3, 0, 2, 0};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{-3, 0, 0, 0, 2, 5}, arr);
    }

    @Test
    void testMinMaxValues() {
        int[] arr = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0,
                     Integer.MAX_VALUE, Integer.MIN_VALUE};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, 0,
                                    Integer.MAX_VALUE, Integer.MAX_VALUE}, arr);
    }

    @Test
    void testRandom() {
        int[] arr = {9, 2, 8, 1, 7, 3, 6, 4, 5, 0};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, arr);
    }

    @Test
    void testMixed() {
        int[] arr = {-5, 10, -3, 8, -1, 6, 0};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{-5, -3, -1, 0, 6, 8, 10}, arr);
    }

    @Test
    void testOneNegative() {
        int[] arr = {5, 3, 8, -2, 1, 4};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{-2, 1, 3, 4, 5, 8}, arr);
    }

    @Test
    void testAllNegatives() {
        int[] arr = {-1, -5, -3, -8, -2, -4};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{-8, -5, -4, -3, -2, -1}, arr);
    }

    @Test
    void testRepeatedPattern() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9}, arr);
    }

    @Test
    void testArrayWithTwoElements() {
        int[] arr = {2, 1};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);

        arr = new int[]{1, 2};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);

        arr = new int[]{5, 5};
        Sort.heapSort(arr);
        assertArrayEquals(new int[]{5, 5}, arr);
    }
}
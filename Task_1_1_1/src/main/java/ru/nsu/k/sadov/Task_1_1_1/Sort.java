package ru.nsu.k.sadov.Task_1_1_1;

/**
 * класс Sort содержит методы для реализации алгоритма пирамидальной сортировки.
 */
public class Sort {
    /**
     * функция для поддержания свойства максимальной кучи в поддереве с корнем i.
     *
     * @param arr - массив, представляющий кучу
     * @param n   - размер кучи
     * @param i   - индекс корневого элемента поддерева
     */

    public static void heapify(int[] arr, int n, int i) {
        int lrgst = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[lrgst] < arr[left]) {
            lrgst = left;
        }

        if (right < n && arr[lrgst] < arr[right]) {
            lrgst = right;
        }

        if (lrgst != i) {
            int swap = arr[i];
            arr[i] = arr[lrgst];
            arr[lrgst] = swap;

            heapify(arr, n, lrgst);
        }
    }

    /**
     * функция для построения максимальной кучи из произвольного массива.
     *
     * @param arr - массив для преобразования в кучу
     * @param n   - размер массива
     */

    public static void buildHeap(int[] arr, int n) {
        // начинаем с последнего нелистового узла (родителя последнего элемента)
        // и движемся до корня

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
    }

    /**
     * основная функция для выполнения пирамидальной сортировки.
     *
     * @param arr - массив для сортировки
     */
    public static void heapSort(int[] arr) {
        int n = arr.length;

        buildHeap(arr, n);


        for (int i = n - 1; i > 0; i--) {
            // двигаем корень в конец
            int tmp = arr[0];
            arr[0] = arr[i];
            arr[i] = tmp;

            heapify(arr, i, 0); // вызываем для уменьшенной кучи
        }
    }

    /**
     * вспомогательная функция для вывода массива в консоль.
     *
     * @param arr - массив для вывода
     */
    public static void print(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
    }

    /**
     * основная функция для тестирования алгоритма сортировки.
     *
     * @param args - аргументы командной строки
     */
    public static void main(String[] args) {
        int[] arrr = {4, 5, 3, 5, 2, 1, -1};

        heapSort(arrr);

        print(arrr);
    }
}

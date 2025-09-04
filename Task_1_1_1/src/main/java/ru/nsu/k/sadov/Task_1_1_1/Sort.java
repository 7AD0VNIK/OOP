package ru.nsu.k.sadov.Task_1_1_1;

public class Sort {
    public static void heapify(int[] arr, int n, int i){
        int lrgst = i;
        int left = 2*i + 1;
        int right = 2*i + 2;

        if (left < n && arr[lrgst] < arr[left] ) {
            lrgst = left;
        }

        if (right < n && arr[lrgst] < arr[right] ) {
            lrgst = right;
        }

        if (lrgst != i){
            int swap = arr[i];
            arr[i] = arr[lrgst];
            arr[lrgst] = swap;

            heapify(arr, n, lrgst);
        }
    }

    public static void buildHeap(int[] arr, int n){
        for (int i = n / 2 - 1; i >= 0; i--){
            heapify(arr, n, i);
        }
    }

    public static void heapSort(int[] arr){
        int n = arr.length;

        buildHeap(arr, n);


        for (int i = n - 1; i > 0; i--){
            int tmp = arr[0];
            arr[0] = arr[i];
            arr[i] = tmp;

            heapify(arr, i, 0);
        }
    }
    public static void print(int[] arr){
        System.out.print("[");
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i]);
            if (i < arr.length - 1){
                System.out.print(", ");
            }
        }
        System.out.print("]");
    }
    public static void main(String[] args){
        int[] arrr = {4, 5, 3, 5, 2, 1, -1};

        heapSort(arrr);

        print(arrr);
    }
}

package com.go.lesson4;

/**
 * Created on 2021/10/31
 * Description(希尔排序算法)
 *
 * @author go
 */
public class Subject1 {

    private static final int TWO = 2;

    public static void main(String[] args) {
        int[] arr = new int[]{3, 2, 1, 4, 56, 8, 0, 11, 22, 14, 17, 10, 22, 26, 29};
        shellSort(arr);
        printArr(arr);
    }

    private static void printArr(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    /**
     * 希尔排序
     *
     * @param arr
     */
    public static void shellSort(int[] arr) {
        for (int inc = arr.length / TWO; inc > 0; inc /= TWO) {
            //根据增量对数组进行分组
            for (int i = inc; i < arr.length; i++) {
                int idx = i;
                //进行插入排序
                while ((idx - inc) >= 0 && arr[idx] < arr[idx - inc]) {
                    int temp = arr[idx];
                    arr[idx] = arr[idx - inc];
                    arr[idx - inc] = temp;
                    idx -= inc;
                }
            }
        }
    }

}

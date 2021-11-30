package com.go.me;

/**
 * Description: {
 * 插入排序的改进版，搜索插入位置采用二分查找定位
 * }
 * Created on 2021/11/1.
 *
 * @author go
 */
public class Demo1 {

    public static void main(String[] args) {
        int[] arr = new int[]{55, 32, 3, 2, 5, 11, 24, 90, 12, 17, 10, 22, 23, 25, 76};
        //commonInsertSort(arr);
        mendInsertSort(arr);
        printArr(arr);
    }

    private static void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    /**
     * 通用插入排序
     *
     * @param arr
     */
    public static void commonInsertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = 0;
            for (j = i - 1; j >= 0 && arr[j] > tmp; j--) {
                arr[j + 1] = arr[j];
            }
            arr[j + 1] = tmp;
        }
    }

    /**
     * 改进的插入排序算法
     *
     * @param arr
     */
    private static void mendInsertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i], left = 0, right = i - 1;
            //二分查找定位插入位置
            while (left <= right) {
                int mid = (left + right) / 2;
                if (tmp < arr[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            for (int j = i - 1; j >= left; j--) {
                arr[j + 1] = arr[j];
            }
            arr[left] = tmp;
        }
    }

}

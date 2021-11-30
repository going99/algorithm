package com.go.me;

import java.util.Random;

/**
 * Description: 动态规划算法改进
 * Created on 2021/11/1.
 *
 * @author go
 */
public class Demo2 {

    /**
     * 初始每个节点数据
     *
     * @return
     */
    private static int[][] initData() {
        int len = new Random().nextInt(98) + 2;
        int[][] arr = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j <= i; j++) {
                arr[i][j] = new Random().nextInt(98) + 2;
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        int[][] data = initData();
        int n = data.length > 5 ? 5 : data.length;
        System.out.println("n =================== " + n);
        int[][] result = new int[n][n];
        printArr(data);
        searchPath(data, result);
        printArr(result);
    }

    private static void printArr(int[][] arr) {
        for (int i = 0; i < arr.length && i < 5; i++) {
            for (int j = 0; j <= i; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void searchPath(int[][] data, int[][] result) {
        int len = result.length;
        for (int i = 0; i < len; i++) {
            if (0 == i) {
                result[0][0] = data[0][0];
                continue;
            }
            for (int j = 1; j <= i; j++) {
                result[i][j] = data[i][j] + Math.max(result[i - 1][j], result[i - 1][j - 1]);
            }
        }
    }

}

package com.go.lesson6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description: （有n(n<1亿)个数字，请使用堆排序算法输出前m(m<=n)大的数字）
 * Created on 2021/11/22.
 *
 * @author go
 */
public class Subject62 {

    private static final int N = 100000000;

    private static void printArray(List<Integer> arr, String mark) {
        System.out.println(mark);
        for (int i = 0; i < arr.size(); i++) {
            System.out.print(arr.get(i) + " ");
        }
        System.out.println();
    }

    private static List<Integer> initData() {
        List<Integer> list = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            list.add(new Random().nextInt(N));
        }
        return list;
    }

    public static void main(String[] args) {
        List<Integer> list = initData();
        //printArray(list, "初始数据");
        int M = new Random().nextInt(N);
        List<Integer> resList = preMMaxNum(list, M);
        //printArray(resList, "前 " + M + " 大数据");
    }

    private static List<Integer> preMMaxNum(List<Integer> sourceList, int M) {
        heapSortByAddress(sourceList, sourceList.size(), M);
        return sourceList.subList(0, M);
    }

    private static void heapSortByAddress(List<Integer> list, int size, int M) {
        for (int i = 0; i < size; i++) {
            if (i >= M) {
                break;
            }
            HeapSubject.findMax(list.subList(i, size), size - i);
        }
    }

}

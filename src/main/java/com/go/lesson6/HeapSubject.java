package com.go.lesson6;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 堆课题
 * Created on 2021/11/22.
 *
 * @author go
 */
public class HeapSubject {

    private static void printArray(List<Integer> arr, String mark) {
        System.out.println(mark);
        for (int i = 0; i < arr.size(); i++) {
            System.out.print(arr.get(i) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        //test();
        List<Integer> list = initData();
        findMax(list, list.size());
        printArray(list, "一次堆转换后的结果");
    }

    private static List<Integer> initData() {
        List<Integer> list = new ArrayList<>(12);
        list.add(11);
        list.add(89);
        list.add(23);
        list.add(31);
        list.add(9);
        list.add(6);
        list.add(45);
        list.add(66);
        list.add(33);
        list.add(19);
        list.add(20);
        list.add(13);
        return list;
    }

    private static void test() {
        List<Integer> list = initData();
        heapSortByAddress(list, list.size());
        //heapSort(list, list.size());
        printArray(list, "打印堆排序结果");
    }

    private static void heapSort(List<Integer> list, int size) {
        for (int i = size; i > 0; i--) {
            findMax(list, i);
            int tmp = list.get(i - 1);
            list.set(i - 1, list.get(0));
            list.set(0, tmp);
        }
    }

    private static void heapSortByAddress(List<Integer> list, int size) {
        for (int i = 0; i < size; i++) {
            findMax(list.subList(i, size), size - i);
            printArray(list, "print " + i + " 次");
        }
    }

    /**
     * 用大顶堆的堆排序思想，找出最大数放入首
     *
     * @param list
     * @param size
     */
    protected static void findMax(List<Integer> list, int size) {
        for (int i = size - 1; i > 0; i--) {
            int child = i;
            int parent = i / 2;
            if (i != size - 1 && list.get(i).intValue() < list.get(i + 1).intValue()) {
                child++;
            }
            if (list.get(parent).intValue() < list.get(child).intValue()) {
                int tmp = list.get(parent);
                list.set(parent, list.get(child));
                list.set(child, tmp);
            }
        }
    }

}

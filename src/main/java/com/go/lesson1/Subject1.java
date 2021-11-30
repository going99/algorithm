package com.go.lesson1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 2021/10/1
 * Description(
 * 题目描述：给定n个整数，请按从大到小的顺序输出其中前m大的数。
 * （1）n、m小于等于1000000000（十亿）
 * （2）给定的整数都处于区间[-5000000,5000000]
 * （3）你的算法能否做到时间复杂度为O(n)，空间复杂度为O(1)?)
 *
 * @author go Yan
 */
public class Subject1 {

    public static void main(String[] args) {
        int[] arr = generateArr(1000000000);
        long start = System.currentTimeMillis();
        List<Integer> list = outRes(1000, arr, 10000001, 5000000);
        long end = System.currentTimeMillis();
        System.out.println("(end-start) = " + (end - start));
        printArr(list);
    }

    public static int[] generateArr(int length) {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = new Random().nextInt(10000000) - 5000000;
        }
        return arr;
    }

    /**
     * 核心
     *
     * @param m
     * @param sourceArr
     * @param maxLen    区间总偏移值 offset
     * @param offset    最大负数
     * @return
     */
    public static List<Integer> outRes(int m, int[] sourceArr, int maxLen, int offset) {
        int[] hashArr = new int[maxLen];
        for (Integer val : sourceArr) {
            hashArr[val + offset]++;
        }
        List<Integer> list = new ArrayList<>(m);
        int tmp = m;
        //for (int i = hashArr.length - 1; i > 0; i--) {
        //    if (tmp <= 0) {
        //        break;
        //    }
        //    while (hashArr[i]-- > 0) {
        //        if (tmp <= 0) {
        //            break;
        //        }
        //        list.add(i - offset);
        //        tmp--;
        //    }
        //}
        for (int i = hashArr.length - 1; i > 0; i--) {
            for(int j = 0; j < hashArr[i] && tmp > 0; j++, tmp--)
            {
                list.add(i - offset);
            }
        }
        return list;
    }

    public static void printArr(List<Integer> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i % 10 == 0 && i != 0) {
                System.out.println();
            }
            System.out.print(list.get(i) + " ");
        }
    }

}

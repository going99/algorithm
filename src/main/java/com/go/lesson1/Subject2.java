package com.go.lesson1;

import com.go.common.SortUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created on 2021/10/1
 * Description(
 * 2、使用哈希排序对公司产品的订单进行排序：先按产品的品种排序升序排列，
 * 对于品种相同的产品，按订单金额进行降序排列，然后打印出每种产品卖出最多的5个订单金额；
 * （1）公司产品有100种，分别用1~100表示；
 * （2）每笔订单金额的范围是[1.00, 9999.99]，每个金额的小数点后面不超过两位；
 * （3）产品种类和订单金额分别用Type和Amount字段表示
 * （4）订单的数量很大，超过100000000个)
 *
 * @author go Yan
 */
public class Subject2 {

    public static void main(String[] args) {
        int[][] arr = generateOrder(100000000);
        int[] sort = sort(arr);
        System.out.println("sort.length = " + sort.length);
        for (int i = 0; i < arr.length; i++) {
            int[] tmp = arr[i];
            List[] lists = transArr(tmp);
            printTop(lists, 5, i);
        }
    }

    /**
     * 查询最大数
     *
     * @param arr
     * @return
     */
    private static int findMax(int[] arr) {
        int max = 0;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    private static List[] transArr(int[] arr) {
        //arr数组的下标表示金额。数值表示此金额的订单出现次数
        int max = findMax(arr);
        List[] res = new List[max + 1];
        for (int i = 0; i < arr.length; i++) {
            List list = res[arr[i]];
            if (Objects.isNull(list)) {
                list = new ArrayList();
            }
            list.add(i);
            res[arr[i]] = list;
        }
        //转化后数组下标表示订单金额出现次数，数值表示订单金额
        return res;
    }

    private static int[] sort(int[][] arr) {
        int[] res = new int[99990000];
        int idx = 0;
        for (int i = 0; i < arr.length; i++) {
            int[] rows = arr[i];
            for (int tmp = rows.length - 1; tmp >= 0; tmp--) {
                res[idx] = rows[tmp];
            }
        }
        return res;
    }

    private static void printTop(List[] list, int max, int idx) {
        System.out.print("Type = " + (idx + 1) + " Amount top " + max + " :::: ");
        for (int i = list.length - 1; i >= 0; i--) {
            if (max == 0) {
                break;
            }
            List<Integer> arr = list[i];
            if (Objects.isNull(arr)) {
                continue;
            }
            for (Integer val : arr) {
                if (max <= 0) {
                    break;
                }
                System.out.print(((float) -(val - 999899) / 100 + 1) + " ");
                max--;
            }
        }
        System.out.println();
    }

    public static int[][] generateOrder(int len) {
        int[][] arr = new int[100][999900];
        for (int i = len; i > 0; i--) {
            int type = new Random().nextInt(100);
            int amount = new Random().nextInt(999900);
            arr[type][amount]++;
        }
        return arr;
    }

}

package com.go.lesson3;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Random;

/**
 * Created on 2021/10/25
 * Description(
 * 公司生产了一批RFID犬牌，总数为100万个，号码从1~1000000。由于工厂人员疏忽，
 * 导致犬牌芯片中烧录的号码（ChipNo）与犬牌外面所贴的号码(PlateNo)不一致，
 * 比如芯片中烧录的是9603，但是所贴的号码为9604。经调查，发现这十万个犬牌中有两个连续号段的号码存在错误，
 * 请结合今天学习的内容，使用分治算法将两个号段的号码找出来，要求分别输出两个号段的起始号码与结束号码。
 * )
 * 1、总体思想：将一个难以直接解决的大问题，分割成一些规模较小的相同问题，以便各个击破，分而治之，具体如下：
 * （1）将要求解的较大规模的问题分割成m个更小规模的子问题，对这m个子问题分别求解。
 * （2）如果子问题的规模仍然不够小，则再划分为m个子问题，如此递归的进行下去，直到问题规模足够小，很容易求出其解为止。
 * （3）将求出的小规模的问题的解合并为一个更大规模的问题的解，自底向上逐步求出原来问题的解。
 * 2、分治法的适用条件，即其所能解决的问题一般具有以下几个特征：
 * （1）该问题的规模缩小到一定的程度就可以容易地解决；
 * （2）该问题可以分解为若干个规模较小的相同问题，即该问题具有最优子结构性质
 * （3）利用该问题分解出的子问题的解可以合并为该问题的解；
 * （4）该问题所分解出的各个子问题是相互独立的，即子问题之间不包含公共的子问题。
 *
 * @author go
 */
public class Subject2 {

    private final static int CHIP_LENGTH = 1000001;
    private static int[] chipArr = new int[CHIP_LENGTH];

    public static void main(String[] args) {
        SubResult initData = buildData();
        System.out.println("实际错误数据 = " + initData.toString());
        SubResult result = analysis(0, 0, 0, 0, false, 1, CHIP_LENGTH - 1);
        System.out.println("算法错误数据 = " + result.toString());
    }

    /**
     * 构建算法数据
     *
     * @return
     */
    private static SubResult buildData() {
        int begin1 = new Random().nextInt(10000) + 1;
        int end1 = new Random().nextInt(1000) + 10000;
        int begin2 = new Random().nextInt(500000) + 21000;
        int end2 = new Random().nextInt(1000) + 710000;
        for (int i = 1; i < begin1; i++) {
            chipArr[i] = i;
        }
        for (int i = (end1 + 1); i < begin2; i++) {
            chipArr[i] = i;
        }
        for (int i = (end2 + 1); i < CHIP_LENGTH; i++) {
            chipArr[i] = i;
        }
        for (int i = begin1; i <= end1; i++) {
            chipArr[i] = i != end1 ? (begin1 + 1) : begin1;
        }
        for (int i = begin2; i <= end2; i++) {
            chipArr[i] = i != end2 ? (begin2 + 1) : begin2;
        }
        return new SubResult().setBegin1(begin1).setEnd1(end1).setBegin2(begin2).setEnd2(end2);
    }

    public static int binSearch(int[] srcArray, int start, int end, int key) {
        int mid = (end - start) / 2 + start;
        if (srcArray[mid] == key) {
            return mid;
        }
        if (start >= end) {
            return -1;
        } else if (key > srcArray[mid]) {
            return binSearch(srcArray, mid + 1, end, key);
        } else if (key < srcArray[mid]) {
            return binSearch(srcArray, start, mid - 1, key);
        }
        return -1;
    }

    /**
     * 二分查找
     *
     * @param arr
     * @param key
     * @return
     */
    public static int commonBinarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        //定义middle
        int middle = 0;
        if (key < arr[low] || key > arr[high] || low > high) {
            return -1;
        }
        while (low <= high) {
            middle = (low + high) / 2;
            if (arr[middle] > key) {
                //比关键字大则关键字在左区域
                high = middle - 1;
            } else if (arr[middle] < key) {
                //比关键字小则关键字在右区域
                low = middle + 1;
            } else {
                return middle;
            }
        }
        //最后仍然没有找到，则返回-1
        return -1;
    }

    /**
     * 算法分析数据
     *
     * @return
     */
    private static SubResult analysis(int begin1, int end1, int begin2, int end2, boolean preMidHit, int low, int high) {
        SubResult result = new SubResult().setBegin1(begin1).setEnd1(end1).setBegin2(begin2).setEnd2(end2);
        if (low > high) {
            return result;
        }
        if (begin1 > 0 && end1 > 0 && begin2 > 0 && end2 > 0) {
            return result;
        }
        int mid = (low + high) / 2;
        if (preMidHit) {
            if (mid != chipArr[mid]) {
                analysis(begin1, end1, begin2, end2, true, low, mid - 1);
                analysis(begin1, end1, begin2, end2, true, mid + 1, high);
            } else {
                analysis(begin1, end1, begin2, end2, false, low, mid - 1);
                analysis(begin1, end1, begin2, end2, false, mid + 1, high);
            }
        } else {

        }

        if (mid != chipArr[mid] && preMidHit) {

            analysis(begin1, end1, begin2, end2, true, 1, 1);

        } else {
            analysis(begin1, end1, begin2, end2, false, 1, 1);

        }
        return new SubResult().setBegin1(begin1).setEnd1(end1).setBegin2(begin2).setEnd2(end2);
    }

    @Data
    class Range {
        private int min;
        private int max;
    }

    /**
     * 获取第一次的数据
     */
    private static void findRange(int low, int high, Range range, boolean preHit, int preMid) {
        int mid = (low + high) / 2;
        if (preHit) {

        } else {

        }
        if (chipArr[mid] != mid && chipArr[mid - 1] == (mid - 1)) {
            //左侧没错误，自己错误，则找到小值
            range.setMin(mid);
            if (range.getMin() > 0 && range.getMax() > 0) {
                return;
            }
        } else if (chipArr[mid] == mid && chipArr[mid + 1] != (mid + 1)) {
            range.setMax(mid + 1);
            if (range.getMin() > 0 && range.getMax() > 0) {
                return;
            }
        }
        return;
    }

}

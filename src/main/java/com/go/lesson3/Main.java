package com.go.lesson3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created on 2021/10/24
 * Description()
 *
 * @author JimGo Yan
 */
public class Main {

    private final static int BUCKET_NUM = 10;

    public static void main(String[] args) {
        // 输入元素均在 [0, 10) 这个区间内
        float[] arr = new float[]{0.12f, 2.2f, 8.8f, 7.6f, 7.2f, 6.3f, 9.0f, 1.6f, 5.6f, 2.4f, 3.2f, 5.1f, 0.33f, 0.45f, 8.9f, 9.9f, 6.6f, 1.2f, 3.5f,
                7.7f, 5.3f, 4.4f, 8.1f, 6.2f, 2.2f, 2.6f, 5.8f};
        bucketSort(arr);
        printArray(arr);
    }

    public static void bucketSort(float[] arr) {
        // 新建一个桶的集合
        ArrayList<LinkedList<Float>> buckets = new ArrayList<LinkedList<Float>>();
        for (int i = 0; i < BUCKET_NUM; i++) {
            // 新建一个桶，并将其添加到桶的集合中去。
            // 由于桶内元素会频繁的插入，所以选择 LinkedList 作为桶的数据结构
            buckets.add(new LinkedList<Float>());
        }
        // 将输入数据全部放入桶中并完成排序
        for (float data : arr) {
            int index = getBucketIndex(data);
            insertSort(buckets.get(index), data);
        }
        // 将桶中元素全部取出来并放入 arr 中输出
        int index = 0;
        for (LinkedList<Float> bucket : buckets) {
            for (Float data : bucket) {
                arr[index++] = data;
            }
        }
    }

    /**
     * 计算得到输入元素应该放到哪个桶内
     */
    public static int getBucketIndex(float data) {
        // 这里例子写的比较简单，仅使用浮点数的整数部分作为其桶的索引值
        // 实际开发中需要根据场景具体设计
        return (int) data;
    }

    /**
     * 我们选择插入排序作为桶内元素排序的方法 每当有一个新元素到来时，我们都调用该方法将其插入到恰当的位置
     */
    public static void insertSort(List<Float> bucket, float data) {
        ListIterator<Float> it = bucket.listIterator();
        boolean insertFlag = true;
        while (it.hasNext()) {
            if (data <= it.next()) {
                // 把迭代器的位置偏移回上一个位置
                it.previous();
                // 把数据插入到迭代器的当前位置
                it.add(data);
                insertFlag = false;
                break;
            }
        }
        if (insertFlag) {
            // 否则把数据插入到链表末端
            bucket.add(data);
        }
    }

    public static void printArray(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.println();
    }

}

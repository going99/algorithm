package com.go.lesson2;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created on 2021/10/25
 * Description(
 * 公司生产了一批RFID犬牌，总数为100万个，号码从1~1000000。由于工厂人员疏忽，
 * 导致犬牌芯片中烧录的号码（ChipNo）与犬牌外面所贴的号码(PlateNo)不一致，
 * 比如芯片中烧录的是9603，但是所贴的号码为9604。经调查，发现这十万个犬牌中有两个连续号段的号码存在错误，
 * 请结合今天学习的内容，使用分治算法将两个号段的号码找出来，要求分别输出两个号段的起始号码与结束号码。
 * )
 *
 * @author go
 */
public class Subject2 {

    @Data
    @ToString
    @Accessors(chain = true)
    static class Result {
        private int begin;
        private int over;
    }

    private final static int ARR_LENGTH = 1000001;
    private static int[] checkArr = new int[ARR_LENGTH];
    private final static int ERR_STAGE = 2;
    private static List<Result> results = new ArrayList<>(ERR_STAGE);

    public static void main(String[] args) {
        List<Result> beforeList = initData();
        for (Result result : beforeList) {
            System.out.println("假设连续的起止位置 ... result = " + result);
        }
        handler(1, ARR_LENGTH - 1, checkArr);
        for (Result result : results) {
            System.out.println("处理后连续起止位置 .... result = " + result);
        }
    }

    /**
     * 初始化数据
     *
     * @return
     */
    private static List<Result> initData() {
        List<Result> results = new ArrayList<>(2);
        int begin = new Random().nextInt(50000) + 1;
        int over = new Random().nextInt(100000) + 50000;
        for (int i = 1; i < begin; i++) {
            checkArr[i] = i;
        }
        for (int i = begin; i <= over; i++) {
            if (i != over) {
                checkArr[i] = (i + 1);
            } else {
                checkArr[i] = begin;
            }
        }
        results.add(new Result().setBegin(begin).setOver(over));
        begin = new Random().nextInt(300000) + 150000;
        for (int i = over + 1; i < begin; i++) {
            checkArr[i] = i;
        }
        over = new Random().nextInt(550000) + 450000;
        results.add(new Result().setBegin(begin).setOver(over));
        for (int i = begin; i <= over; i++) {
            if (i != over) {
                checkArr[i] = (i + 1);
            } else {
                checkArr[i] = begin;
            }
        }
        for (int i = over + 1; i < ARR_LENGTH; i++) {
            checkArr[i] = i;
        }
        return results;
    }

    private static void handler(int low, int high, int[] arr) {
        int mid = (low + high) / 2;
        if (low >= high) {
            return;
        }
        if (mid != arr[mid]) {
            //find one
            Result result = new Result();
            for (int i = mid - 1; i >= low; i--) {
                if (arr[i] == i) {
                    //找到起始出错位置
                    result.setBegin(i + 1);
                    break;
                }
            }
            for (int i = mid + 1; i <= high; i++) {
                if (arr[i] == i) {
                    //找到起始结束位置
                    result.setOver(i - 1);
                    break;
                }
            }
            results.add(result);
            if (results.size() >= ERR_STAGE) {
                return;
            }
            handler(low, result.getBegin() - 1, arr);
            handler(result.getOver() + 1, high, arr);
        } else {
            handler(low, mid - 1, arr);
            handler(mid + 1, high, arr);
        }
    }

}

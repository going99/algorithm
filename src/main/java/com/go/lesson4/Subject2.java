package com.go.lesson4;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * Created on 2021/10/31
 * Description(使用基数排序算法对扑克牌从大到小进行排序，扑克牌J、Q、K分别用11、12、13表示，花色大小顺序为黑、红、梅、方)
 *
 * @author go
 */
public class Subject2 {

    @Data
    @ToString
    @Accessors(chain = true)
    static class Poker {
        private int number;
        private String color;
        private int decor;

        public void print() {
            System.out.print("number=" + number + "-color=" + color + "; ");
        }
    }

    private static Map<Integer, String> map = new HashMap<>();
    private static final int SIZE = 52;
    private static List<Poker> list = new ArrayList<>(SIZE);

    private static void init() {
        map.put(1, "方");
        map.put(2, "梅");
        map.put(3, "红");
        map.put(4, "黑");
    }

    private static boolean findPoker(Poker poker) {
        for (Poker poker1 : list) {
            if (ObjectUtil.isNotNull(poker1) && poker.getDecor() == poker1.getDecor() && poker.getNumber() == poker1.getNumber()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化数据
     */
    private static void initData() {
        for (int i = 0; i < SIZE; i++) {
            while (true) {
                Poker poker = new Poker().setNumber(new Random().nextInt(13) + 1).setDecor(new Random().nextInt(4) + 1);
                poker.setColor(map.get(poker.getDecor()));
                if (!findPoker(poker)) {
                    list.add(poker);
                    break;
                } else {
                    poker = new Poker().setNumber(new Random().nextInt(13) + 1).setDecor(new Random().nextInt(4) + 1);
                    poker.setColor(map.get(poker.getDecor()));
                }
            }
        }
    }

    public static void main(String[] args) {
        init();
        initData();
        System.out.println(" 无序数组打印================== ");
        list.forEach(obj -> obj.print());
        sort();
        System.out.println("排序后数组打印================== ");
        list.forEach(obj -> obj.print());
    }

    private static void sort() {
        List<Poker>[] arr = new List[4];
        for (Poker poker : list) {
            List<Poker> pokers = arr[poker.getDecor() - 1];
            if (ObjectUtil.isNull(pokers)) {
                pokers = new ArrayList<>();
            }
            pokers.add(poker);
            arr[poker.getDecor() - 1] = pokers;
        }
        List<Poker> arrP = new ArrayList<>(52);
        for (List<Poker> pokers : arr) {
            for (Poker poker : pokers) {
                arrP.add(poker);
            }
        }
        List<Poker>[] arrK = new List[13];
        for (Poker poker : arrP) {
            List<Poker> pokers = arrK[poker.getNumber() - 1];
            if (ObjectUtil.isNull(pokers)) {
                pokers = new ArrayList<>();
            }
            pokers.add(poker);
            arrK[poker.getNumber() - 1] = pokers;
        }
        list.clear();
        for (int i = arrK.length - 1; i >= 0; i--) {
            List<Poker> pokers = arrK[i];
            for (int j = pokers.size() - 1; j >= 0; j--) {
                list.add(pokers.get(j));
            }
        }
    }

}

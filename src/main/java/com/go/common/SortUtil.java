package com.go.common;

/**
 * Created on 2021/10/3
 * Description()
 *
 * @author go Yan
 */
public final class SortUtil {

    private SortUtil() {

    }

    public static int findMax(int[] arr) {
        int max = 0;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

}

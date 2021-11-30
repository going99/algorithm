package com.go.me;

/**
 * Description: LetCode53
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * 输入[-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释 连续子数组[4,-1,2,1]的和最大，6
 * 进价：如果你已经实现复杂度为O(n)的解法，尝试使用更为那精妙的分治法求解
 * Created on 2021/11/3.
 * <p>
 * 解题思路
 * 定义sum为最大连续子数组和，遍历数组中的每一个数，起初sum为nums中的第一个数，遍历第二个数时如果sum小于等于0（没有增益效果），
 * 将第二个数赋值给sum，如果大于0（说明有可能有增益效果），将sum加等于第二个数，每次更新的sum判断是否大于之前记录的最大值ans，
 * 如果大于ans更新，否则ans保持原样，最后答案返回的就是最大值ans
 *
 * @author go
 */
public class LetCode53 {

    public static int[] nums = new int[10];

    public static void main(String[] args) {
        nums = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4, -10, 5, 9, 4, -3, 4, -2, 3};
        int res = commonSolution();
        System.out.println("res = " + res);
    }

    public static int commonSolution() {
        int sum = 0;
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (sum <= 0) {
                //  没有增益效果
                sum = nums[i];
            } else {
                //  有增益效果
                sum += nums[i];
            }
            //  判断sum是否大于最大值
            ans = Math.max(ans, sum);
        }
        return ans;
    }

    /**
     * 分治思想来处理
     *
     * @return
     */
    public static int divideAndConquer() {
        int length = nums.length;

        return 0;
    }


}

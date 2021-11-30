package com.go.me;

import java.util.Random;
import java.util.Stack;

/**
 * Description: LetCode22
 * (给出一个长度为 n 的，仅包含字符 '(' 和 ')' 的字符串，计算最长的格式正确的括号子串的长度。
 * 输入："(())"
 * 输出：4
 * 解析：对于"(())"来说，最长格式正确的子串是"(())"，所以为4。)
 * Created on 2021/11/17.
 *
 * @author go
 */
public class LetCode22 {

    private static final String LEFT = "(";
    private static final String RIGHT = ")";

    public static void main(String[] args) {
        int len = maxLenInStrByStack("())(())((()((()))");
        System.out.println("len = " + len);
        int len1 = maxLenInStrByStack("())(())((()(((()))(())");
        System.out.println("len1 = " + len1);
        int len2 = maxLenInStrByStack("()()(())(()");
        System.out.println("len2 = " + len2);
    }

    public static String initStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int ran = new Random().nextInt(2);
            sb.append(ran == 0 ? LEFT : RIGHT);
        }
        return sb.toString();
    }

    /**
     * 第一种方式使用栈的方式来实现--保证栈底元素为当前已经遍历过的元素中，最后一个没有被匹配的右括号的下标，栈中的其它元素维护左括号的下标。
     * <p>
     * 具体来说，我们在遍历给定字符串的过程中，需要始终保证栈底元素为当前已经遍历过的元素中，最后一个没有被匹配的右括号的下标，栈中的其它元素维护左括号的下标。
     * 如果遇到的是左括号’（’，我们就把它的下标放入栈中；
     * 如果遇到的是右括号‘）’，此时有两种情况；
     * 如果此时栈为空，说明当前的右括号是没有被匹配的右括号。
     * 如果此时栈不为空，右括号的下标减去栈顶元素即为「以该右括号为结尾的最长有效括号的长度」，然后用它来更新最大值即可。
     * 这里需要注意一点，因为一开始栈为空，如果此时第一个字符为左括号时，我们会将其对应的下标放入栈中，这样就不满足栈底始终保存的是最后一个没有被匹配的右括号的下标这个条件，为了保证该条件成立，我们在开始时需要往栈中放入一个元素-1。
     *
     * @param str
     * @return
     */
    public static int maxLenInStrByStack(String str) {
        int max = 0;
        Stack<Integer> stack = new Stack<>();
        String[] arr = str.split("");
        String preStr = "";
        int currLen = 0;
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            if (LEFT.equals(s)) {
                if (RIGHT.equals(preStr)) {
                    currLen = 0;
                    stack.clear();
                }
                stack.push(i);
                preStr = LEFT;
            } else {
                preStr = RIGHT;
                //如果是右括号，栈顶元素pop出去
                if (stack.empty()) {
                    continue;
                }
                stack.pop();
                currLen += 2;
                max = Math.max(max, currLen);
                if (stack.empty()) {
                    currLen = 0;
                }
            }
        }
        return max;
    }

    public static int maxLenInStrByStack1(String str) {
        int max = 0;

        return max;
    }

    /**
     * 第二种方式使用动态规划来解决-最优解问题
     * 对于求最优解问题，我们一般首先需要考虑的就是动态规划的解法。显然，求最长的括号子串是最优解问题，所有我们也可以采用动态规划的思想来求解。
     *
     * @param str
     * @return
     */
    public static int maxLenInStrByDynamic(String str) {

        return 0;
    }

}

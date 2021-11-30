package com.go.lesson6;

import com.go.lesson5.Subject1;
import com.go.lesson5.TreeNode;

import java.util.Stack;

/**
 * Created on 2021/11/24
 * Description(非递归方式求二叉树的宽度)
 *
 * @author go
 */
public class Subject61 {

    public static void main(String[] args) {
        TreeNode root = Subject1.assembleByArray(Subject1.arr);
        int maxWidth = findMaxWidth(root);
        System.out.println("maxWidth = " + maxWidth);
    }

    /**
     * 非递归的思想求二叉树的最大高度
     *
     * @param root
     * @return
     */
    private static int findMaxWidth(TreeNode root) {
        if (null == root) {
            return 0;
        }
        Stack<TreeNode> stack = new Stack<>();
        int max = 1;
        stack.push(root);
        Stack<TreeNode> childStack = new Stack<>();
        while (!stack.isEmpty()) {
            int currentWidth = 0;
            while (!stack.isEmpty()) {
                TreeNode pop = stack.pop();
                if (null != pop.getLeft()) {
                    currentWidth++;
                    childStack.push(pop.getLeft());
                }
                if (null != pop.getRight()) {
                    currentWidth++;
                    childStack.push(pop.getRight());
                }
            }
            max = Math.max(currentWidth, max);
            stack.addAll(childStack);
            childStack.clear();
        }
        return max;
    }

}

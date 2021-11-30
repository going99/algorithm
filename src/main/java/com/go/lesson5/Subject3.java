package com.go.lesson5;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Description: (二叉树求树高)
 * Created on 2021/11/21.
 *
 * @author go
 */
public class Subject3 {

    public static void main(String[] args) {
        TreeNode node = Subject1.assembleByArray(Subject1.arr);
        int high = highBinaryTreeWithRecursion(node);
        System.out.println("二叉树树高 = " + high);
        int res = maxDepthByStack(node);
        System.out.println("res = " + res);
    }

    /**
     * 递归方式求树高
     *
     * @param root
     * @return
     */
    private static int highBinaryTreeWithRecursion(TreeNode root) {
        if (null == root) {
            return 0;
        }
        int left = highBinaryTreeWithRecursion(root.getLeft());
        int right = highBinaryTreeWithRecursion(root.getRight());
        return 1 + Math.max(left, right);
    }

    /**
     * 非递归方式求树高
     *
     * @param root
     * @return
     */
    private static int maxDepthByStack(TreeNode root) {
        int max = 0;
        if (null == root) {
            return max;
        }
        Stack<TreeNode> stack = new Stack<>();
        List<TreeNode> list = new ArrayList<>();
        while (root != null || !stack.empty()) {
            while (root != null) {
                //将节点压栈
                stack.push(root);
                //将节点指向其左子节点
                root = root.getLeft();
            }
            while (!stack.isEmpty()) {
                TreeNode peek = stack.peek();
                if (null == peek.getLeft() && null == peek.getRight()) {
                    max = Math.max(max, stack.size());
                    list.add(stack.pop());
                } else if (!list.contains(peek) && null == peek.getRight()) {
                    list.add(stack.pop());
                } else if (list.contains(peek.getRight())) {
                    list.add(stack.pop());
                } else {
                    if (null != peek.getRight()) {
                        root = peek.getRight();
                    }
                    break;
                }
            }
        }
        return max;
    }

}

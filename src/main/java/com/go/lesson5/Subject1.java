package com.go.lesson5;

import java.util.Stack;

/**
 * Created on 2021/11/21
 * Description(  1、写代码实现：
 * （1）建立一棵左图所示的二叉树：
 * （2）前序、中序、后序遍历
 * （3）求树高
 * （4）以非递归的方式实现后序遍历
 * （5）在两个数组中分别有前序和中序遍历序列，试建立该二叉树（附加题，做对加5分）
 * )
 *
 * @author go
 */
public class Subject1 {

    public static Integer[] arr = new Integer[]{1, 2, 4, null, null, 5, 6, null, null, 7, null, 8, 9,
            11, null, null, 12, null, null, 10, null, 13, null, null, 3, null, null};

    private static void check() {
        Integer[] array = new Integer[]{1, 2, 4, 6, 9, null, null, null, 8, null, null, 5, 10, 12,
                15, null, null, 16, null, null, 13, null, 17, null, null, 11, null, 14, 18, null, null, 19,
                null, null, 3, 20, null, null, 21, null, 22, null, null};
        TreeNode root = assembleByArray(array);
        System.out.println("check root = " + root);
    }

    public static void main(String[] args) {
        check();
        //先序遍历存储的数组
        TreeNode root = assembleByArray(arr);
        System.out.println("root = " + root);
    }

    /**
     * 建立一颗图示的二叉树(根据数组生成二叉树)
     *
     * @param arr
     * @return
     */
    public static TreeNode assembleByArray(Integer[] arr) {
        if (null == arr || 0 == arr.length || null == arr[0]) {
            return null;
        }
        TreeNode root = new TreeNode().setData(arr[0]);
        Stack<TreeNode> stack = new Stack<>();
        TreeNode tmpRoot = root;
        stack.push(tmpRoot);
        for (int i = 1; i < arr.length; i++) {
            if (null == arr[i]) {
                if (null == arr[i - 1]) {
                    //栈顶的元素没有子节点
                    stack.pop();
                    tmpRoot = stack.peek();
                    while (null != tmpRoot.getRight()) {
                        stack.pop();
                        if (stack.isEmpty()) {
                            break;
                        }
                        tmpRoot = stack.peek();
                    }
                }
            } else {
                TreeNode node = new TreeNode().setData(arr[i]);
                stack.push(node);
                if (null == arr[i - 1]) {
                    tmpRoot.setRight(node);
                } else {
                    tmpRoot.setLeft(node);
                }
                tmpRoot = node;
            }
        }
        return root;
    }

}

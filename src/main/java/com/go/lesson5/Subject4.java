package com.go.lesson5;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Description: (以非递归方式实现二叉树的后序遍历)
 * Created on 2021/11/21.
 *
 * @author go
 */
public class Subject4 {

    public static void main(String[] args) {
        TreeNode root = Subject1.assembleByArray(Subject1.arr);
        List postList1 = postScanBinaryTreeByStack(root);
        printList(postList1);
        System.out.println();
        List postList2 = postOrderTraversalWithStack(root);
        printList(postList2);
        System.out.println();
        List preList = preOrderTraversalWithStack(root);
        printList(preList);
    }

    private static void printList(List<TreeNode> list) {
        list.forEach(obj -> System.out.print(obj.getData() + " "));
    }

    /**
     * 中序遍历-用栈的数据结构实现
     *
     * @param root
     */
    public static List midScanBinaryTreeByStack(TreeNode root) {
        List res = new ArrayList<>();
        if (null == root) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.empty()) {
            //当节点不为空时
            while (root != null) {
                //将节点压栈
                stack.push(root);
                //将节点指向其左子节点
                root = root.getLeft();
            }
            if (!stack.empty()) {
                //将栈里元素弹出来
                TreeNode node = stack.pop();
                //添加进列表中
                res.add(node.getData());
                //将节点指向其右子节点
                root = node.getRight();
            }
        }
        return res;
    }

    /**
     * 后序遍历-用栈的数据结构实现--占用一个栈的存储空间
     *
     * @param root
     * @return
     */
    public static List postScanBinaryTreeByStack(TreeNode root) {
        List res = new ArrayList<>();
        if (null == root) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.empty()) {
            //当节点不为空时
            while (root != null) {
                //将节点压栈
                stack.push(root);
                //将节点指向其左子节点
                root = root.getLeft();
            }
            while (!stack.empty()) {
                //将栈里元素弹出来---有条件的
                TreeNode peek = stack.peek();
                if (null == peek.getLeft() && null == peek.getRight()) {
                    stack.pop();
                    //添加进列表中
                    res.add(peek);
                } else if (res.contains(peek.getRight()) || null == peek.getRight()) {
                    stack.pop();
                    res.add(peek);
                } else {
                    break;
                }
            }
            if (!stack.isEmpty() && null != stack.peek().getRight()) {
                root = stack.peek().getRight();
            }
        }
        return res;
    }

    /**
     * 后序遍历-用栈的数据结构实现--占用二个栈的存储空间
     *
     * @param root
     * @return
     */
    public static List postOrderTraversalWithStack(TreeNode root) {
        List res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Stack<TreeNode> stack1 = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();
        stack1.push(root);
        while (!stack1.isEmpty()) {
            root = stack1.pop();
            stack2.push(root);
            if (root.getLeft() != null) {
                stack1.push(root.getLeft());
            }
            if (root.getRight() != null) {
                stack1.push(root.getRight());
            }
        }
        while (!stack2.isEmpty()) {
            res.add(stack2.pop());
        }
        return res;
    }

    /**
     * 前序遍历-非递归方式实现
     *
     * @param root
     * @return
     */
    public static List preOrderTraversalWithStack(TreeNode root) {
        List res = new ArrayList();
        if (null == root) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        while (null != root || !stack.empty()) {
            while (null != root) {
                if (null != root.getRight()) {
                    stack.push(root.getRight());
                }
                res.add(root);
                root = root.getLeft();
            }
            if (!stack.isEmpty()) {
                root = stack.pop();
            }
        }
        return res;
    }

}

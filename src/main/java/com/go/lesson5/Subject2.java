package com.go.lesson5;

/**
 * Description: (前序、中序、后序遍历二叉树- 递归方案)
 * Created on 2021/11/21.
 *
 * @author go
 */
public class Subject2 {

    public static void main(String[] args) {
        TreeNode node = Subject1.assembleByArray(Subject1.arr);
        System.out.println("前序遍历结果==================");
        rootFirstScan(node);
        System.out.println("\r\n" + "中序遍历结果==================");
        rootMidScan(node);
        System.out.println("\r\n" + "后序遍历结果==================");
        rootLastScan(node);
    }

    /**
     * 前序遍历
     *
     * @param root
     */
    private static void rootFirstScan(TreeNode root) {
        if (null == root) {
            return;
        }
        System.out.print(root.getData() + " ");
        rootFirstScan(root.getLeft());
        rootFirstScan(root.getRight());
    }

    /**
     * 中序遍历
     *
     * @param root
     */
    private static void rootMidScan(TreeNode root) {
        if (null == root) {
            return;
        }
        rootMidScan(root.getLeft());
        System.out.print(root.getData() + " ");
        rootMidScan(root.getRight());
    }

    /**
     * 后序遍历
     *
     * @param root
     */
    private static void rootLastScan(TreeNode root) {
        if (null == root) {
            return;
        }
        rootLastScan(root.getLeft());
        rootLastScan(root.getRight());
        System.out.print(root.getData() + " ");
    }

}
